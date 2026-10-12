# Integración con KubeJS

[English version](KubeJS.md)

Botania Revived incluye una integración opcional con KubeJS. Desde los scripts permite hacer dos cosas:

- registrar runas nuevas (scripts de inicio);
- agregar recetas para las máquinas de Botania: Altar rúnico, Botica de pétalos, Infusión de maná (Pila de maná), Comercio élfico, Placa de aglomeración terrestre, Fermentador botánico y Margarita pura (scripts de servidor).

Todo vive en un único grupo de eventos llamado `BotaniaEvents`, con dos eventos:

| Evento | Carpeta de scripts | Para qué sirve |
| --- | --- | --- |
| `BotaniaEvents.registry(event => ...)` | `startup_scripts` | Registrar runas |
| `BotaniaEvents.server(event => ...)` | `server_scripts` | Agregar recetas de máquinas |

## Contenido

1. [Requisitos](#requisitos)
2. [Dónde poner los scripts](#dónde-poner-los-scripts)
3. [Cuándo se ejecutan los eventos](#cuándo-se-ejecutan-los-eventos)
4. [Registrar runas](#registrar-runas)
5. [Recetas de máquinas](#recetas-de-máquinas)
6. [Ids de receta](#ids-de-receta)
7. [Quitar o reemplazar recetas existentes](#quitar-o-reemplazar-recetas-existentes)
8. [Errores comunes y mensajes de error](#errores-comunes-y-mensajes-de-error)
9. [Eventos nativos de Java para otros mods](#eventos-nativos-de-java-para-otros-mods)
10. [Licencia](#licencia)

## Requisitos

| Componente | Versión |
| --- | --- |
| Minecraft | 1.21.1 |
| Cargador | NeoForge 21.1 |
| KubeJS | 2101.7.2-build.368 o posterior (la línea de KubeJS para Minecraft 1.21.1, "2101") |
| Rhino | 2101.2.7-build.85 o posterior (se instala junto con KubeJS) |

KubeJS es una dependencia opcional. Sin él, Botania Revived funciona con normalidad y el grupo `BotaniaEvents` simplemente no existe.

Los scripts son JavaScript normal ejecutado por Rhino, el motor de scripts que usa KubeJS. Los ejemplos de esta página usan solo sintaxis simple (funciones flecha y arreglos) que los scripts de KubeJS admiten.

## Dónde poner los scripts

KubeJS crea su carpeta la primera vez que se inicia el juego con él instalado. Está en el directorio del juego (el mismo lugar que `mods` y `config`):

```
kubejs/
  startup_scripts/    runas: BotaniaEvents.registry
  server_scripts/     recetas: BotaniaEvents.server
  assets/             modelos, texturas y archivos de idioma de tus runas
  data/               archivos normales de datapack
```

Se carga cualquier archivo `.js` dentro de esas carpetas; el nombre del archivo no importa. Los registros se escriben en `logs/kubejs/startup.log` y `logs/kubejs/server.log`.

## Cuándo se ejecutan los eventos

| Paso | Qué ocurre |
| --- | --- |
| 1. Inicio del juego | KubeJS carga todos los scripts de inicio. |
| 2. Registro de ítems (todavía durante el inicio) | Botania lanza su evento de registro de runas. Sus 16 runas se registran con ese mismo evento y `BotaniaEvents.registry` se lanza como parte de él; cada llamada a `event.rune(...)` agrega un ítem. |
| 3. Carga de mundo, inicio del servidor o `/reload` | KubeJS carga los scripts de servidor (registrando los manejadores de `BotaniaEvents.server`). |
| 4. Recarga de recetas (mismo paso) | El gestor de recetas termina de cargar todas las recetas de datapacks y después Botania lanza `BotaniaEvents.server`. Tus recetas se crean y se agregan; la consola imprime `[Botania] Added N machine recipes from scripts`. |

Consecuencias:

- Las runas solo se pueden agregar al iniciar el juego. Cambiar un script de inicio exige reiniciar el juego por completo; `/reload` no lo vuelve a ejecutar.
- Las recetas de máquinas se reconstruyen en cada `/reload`, así que puedes editar un script de servidor y probarlo con `/reload` sin salir del mundo.
- Si ningún script tiene un manejador de `BotaniaEvents.server`, no se lanza nada y no se registra nada.
- El evento de servidor se ejecuta después del `ServerEvents.recipes` propio de KubeJS. Mira [Quitar o reemplazar recetas existentes](#quitar-o-reemplazar-recetas-existentes).

En un servidor multijugador el script de inicio (runas) debe estar en el servidor y en cada cliente, porque cambia el registro de ítems. Los scripts de servidor solo hacen falta en el servidor.

## Registrar runas

Las runas son los ítems que se usan como catalizadores en las recetas del Altar rúnico. Una runa registrada desde un script se comporta igual que las de Botania: sirve como ingrediente en recetas del Altar rúnico, el altar la devuelve después de fabricar (salvo que el jugador esté en creativo) y aparece en la pestaña creativa de Botania.

### Sintaxis

```js
// kubejs/startup_scripts/botania_runes.js
BotaniaEvents.registry(event => {
  event.rune('kubejs:rune_of_stone')
})
```

| Llamada | Argumento | Resultado |
| --- | --- | --- |
| `event.rune(id)` | Id del ítem, escrito como `namespace:nombre`. Sin namespace el id queda como `kubejs:<nombre>`. Solo letras minúsculas, dígitos, `_`, `-`, `.` y `/`. | Registra la runa y devuelve el ítem. Si el id ya lo usa otra runa (por ejemplo `botania:rune_water`) o no es válido, se registra un error en el log de inicio y no se registra nada. |

### Darle modelo, textura y nombre a la runa

El script solo registra el ítem. Sin recursos, el ítem muestra el modelo de marcador de posición y la clave de traducción sin traducir. Agrégalos en la carpeta `kubejs/assets` (la carpeta funciona como un paquete de recursos). Para `kubejs:rune_of_stone`:

`kubejs/assets/kubejs/models/item/rune_of_stone.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "kubejs:item/rune_of_stone"
  }
}
```

`kubejs/assets/kubejs/textures/item/rune_of_stone.png`: un PNG de 16x16.

`kubejs/assets/kubejs/lang/es_es.json`

```json
{
  "item.kubejs.rune_of_stone": "Runa de piedra"
}
```

(Para inglés, el mismo contenido en `en_us.json`.)

Los archivos de `kubejs/assets` son recursos de cliente: recárgalos con F3+T o reinicia el juego.

### Ejemplo completo: una runa nueva y su receta

```js
// kubejs/startup_scripts/botania_runes.js
BotaniaEvents.registry(event => {
  event.rune('kubejs:rune_of_stone')
})
```

```js
// kubejs/server_scripts/botania_runes.js
BotaniaEvents.server(event => {
  event.runicAltar('2x kubejs:rune_of_stone', [
    'botania:rune_earth',
    'botania:manasteel_ingot',
    'minecraft:stone',
    'minecraft:cobblestone'
  ]).mana(4000).id('kubejs:rune_of_stone')
})
```

## Recetas de máquinas

Todas las llamadas de receta se hacen sobre el objeto `event` de `BotaniaEvents.server`. Cada llamada crea una receta y la devuelve, así que los ajustes extra se encadenan con `.`:

```js
BotaniaEvents.server(event => {
  event.manaInfusion('botania:manasteel_ingot', 'minecraft:iron_ingot')
    .mana(3000)
    .id('kubejs:my_manasteel')
})
```

Puedes tener varios manejadores de `BotaniaEvents.server` en todos los archivos que quieras; todas las recetas se reúnen y se agregan juntas.

Las recetas que fallan (un argumento incorrecto, un ajuste obligatorio que falta) se omiten y se escribe un error en la consola. El resto de las recetas se carga igual.

### Cómo escribir ítems e ingredientes

| Tipo | Dónde se usa | Formas admitidas |
| --- | --- | --- |
| Ítem de salida | salidas de todas las recetas | `'minecraft:diamond'`, `'3x minecraft:diamond'`, `Item.of('minecraft:diamond', 3)`. Debe ser un ítem real; los tags no son válidos. |
| Lista de ingredientes | `inputs` de altar, botica, comercio élfico, placa terrestre, fermentador | Un valor o un arreglo. Cada entrada: `'minecraft:iron_ingot'`, `'#c:ingots/iron'` (tag), `'3x minecraft:iron_ingot'`, `'2x #c:ingots/iron'`. |
| Ingrediente único | `input` de la infusión de maná, `reagent` de la botica | Una sola entrada, sin cantidad (o `1x`). |
| Bloque | entrada y `catalyst` de la infusión de maná, entrada de la margarita pura | `'minecraft:stone'`, `'#minecraft:logs'` (tag de bloques), `'minecraft:oak_log[axis=y]'` (un estado de bloque exacto) o un arreglo de ellos. |
| Bloque de salida | salida de la margarita pura | `'botania:livingrock'` o un estado de bloque como `'minecraft:oak_log[axis=y]'`. Se rechaza el aire. |
| Brebaje | primer argumento de `brew` | Id del brebaje con namespace, por ejemplo `'botania:speed'`. |

Una cantidad delante de un ingrediente (`'3x ...'`) significa "ese ingrediente tres veces": equivale a escribirlo tres veces en el arreglo. No es el tamaño de pila dentro de una sola ranura.

### Altar rúnico: `event.runicAltar(output, inputs)`

Bloque: Altar rúnico. Los ingredientes se tiran sobre el altar, que se llena de maná. Como siempre, la fabricación se completa con la Varita del bosque y consume una Piedra viviente tirada sobre el altar.

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `output` | sí | Ítem resultante, con cantidad. |
| `inputs` | sí | Lista de ingredientes. Máximo 16 entradas (contadas después de expandir `Nx`). |
| `.mana(n)` | sí | Maná que necesita el altar. Las recetas propias de Botania usan valores desde 5200 (Runa de agua) hasta 100000. |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

```js
BotaniaEvents.server(event => {
  event.runicAltar('2x minecraft:diamond', [
    '4x botania:manasteel_ingot',
    'botania:rune_water',
    'botania:rune_fire',
    '#c:ingots/iron'
  ]).mana(6000).id('kubejs:diamond_from_runes')
})
```

### Botica de pétalos: `event.petalApothecary(output, inputs)`

Bloque: Botica de pétalos. Los pétalos y otros ítems se tiran en una botica llena de agua y el reactivo (una semilla por defecto) dispara la fabricación.

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `output` | sí | Ítem resultante, con cantidad. |
| `inputs` | sí | Lista de ingredientes. Máximo 16 entradas. |
| `.reagent(item)` | no | El ítem que dispara la fabricación. Por defecto: cualquier ítem del tag `botania:seed_apothecary_reagent` (semillas de trigo, remolacha, sandía y calabaza, más el tag `c:seeds`). Un único ingrediente, cantidad 1. |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

```js
BotaniaEvents.server(event => {
  event.petalApothecary('minecraft:poppy', ['2x botania:red_petal', 'botania:white_petal'])
    .reagent('botania:livingwood_twig')
    .id('kubejs:poppy_from_petals')
})
```

### Infusión de maná: `event.manaInfusion(output, input)`

Bloque: Pila de maná. Un ítem arrojado a la pila se convierte mientras la pila paga el maná. Opcionalmente se exige un bloque debajo de la pila (un catalizador).

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `output` | sí | Ítem resultante, con cantidad. |
| `input` | sí | Un único ingrediente con cantidad 1 (un ítem o un tag). |
| `.mana(n)` | sí | Costo de maná. Debe ser mayor que 0 y como máximo 1000001. |
| `.catalyst(block)` | no | Bloque que debe estar debajo de la pila: `'botania:alchemy_catalyst'`, `'botania:conjuration_catalyst'`, un tag de bloques, un estado de bloque o un arreglo. |
| `.group(nombre)` | no | Nombre de grupo opcional (una etiqueta de texto compartida por recetas relacionadas). Por defecto: ninguno. |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

```js
BotaniaEvents.server(event => {
  event.manaInfusion('2x minecraft:emerald', 'minecraft:diamond')
    .mana(3000)
    .catalyst('botania:conjuration_catalyst')
    .id('kubejs:diamond_to_emeralds')

  event.manaInfusion('botania:manasteel_ingot', '#c:ingots/copper').mana(3000)
})
```

### Comercio élfico: `event.elvenTrade(outputs, inputs)`

Bloque: portal de Alfheim (Núcleo de acceso élfico). Los ítems tirados al portal abierto se cambian por las salidas.

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `outputs` | sí | Uno o más ítems resultantes, cada uno con su cantidad. Al menos uno. |
| `inputs` | sí | Lista de ingredientes. |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

Un comercio élfico no tiene costo de maná ni otros ajustes.

```js
BotaniaEvents.server(event => {
  event.elvenTrade(['minecraft:netherite_scrap', '3x minecraft:gold_nugget'], ['minecraft:ancient_debris'])
    .id('kubejs:ancient_debris_trade')

  event.elvenTrade(['botania:dragonstone'], ['2x botania:mana_diamond'])
})
```

### Placa de aglomeración terrestre: `event.terraPlate(output, inputs)`

Bloque: Placa de aglomeración terrestre. Los ítems se tiran sobre la placa y esta se alimenta con maná.

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `output` | sí | Ítem resultante, con cantidad. |
| `inputs` | sí | Lista de ingredientes. |
| `.mana(n)` | sí | Maná que necesita la placa. El Lingote de terracero de Botania usa 500000. |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

```js
BotaniaEvents.server(event => {
  event.terraPlate('minecraft:nether_star', [
    'botania:terrasteel_ingot',
    'botania:mana_diamond',
    'botania:mana_pearl'
  ]).mana(250000).id('kubejs:nether_star')
})
```

### Fermentador botánico: `event.brew(brew, inputs)`

Bloque: Fermentador botánico. El brebaje (el efecto de poción) ya existe en Botania; la receta solo define con qué ingredientes se obtiene. No se pueden crear brebajes nuevos desde scripts.

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `brew` | sí | Id del brebaje **con** el namespace `botania:`. |
| `inputs` | sí | Lista de ingredientes (el frasco no es un ingrediente). |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

Ids de brebaje disponibles en Botania: `botania:speed`, `botania:strength`, `botania:haste`, `botania:healing`, `botania:jump_boost`, `botania:regen`, `botania:regen_weak`, `botania:resistance`, `botania:fire_resistance`, `botania:water_breathing`, `botania:invisibility`, `botania:night_vision`, `botania:absorption`, `botania:overload`, `botania:soul_cross`, `botania:feather_feet`, `botania:emptiness`, `botania:bloodthirst`, `botania:allure`, `botania:clear`. También se aceptan los brebajes de otros mods, con sus propios ids.

```js
BotaniaEvents.server(event => {
  event.brew('botania:speed', ['minecraft:nether_wart', 'minecraft:sugar', 'minecraft:glowstone_dust'])
    .id('kubejs:speed_brew_alt')
})
```

### Margarita pura: `event.pureDaisy(input, output)`

Bloque: Margarita pura. La flor convierte poco a poco los bloques que la rodean.

| Parte | Obligatoria | Descripción |
| --- | --- | --- |
| `input` | sí | Bloque (o tag de bloques, estado de bloque o arreglo) que se convierte. |
| `output` | sí | Bloque o estado de bloque que lo reemplaza. |
| `.time(n)` | no | Tiempo de conversión. Por defecto 150. Debe ser 0 o más. La flor revisa sus ocho bloques vecinos de a uno por tick, así que cada bloque avanza una vez cada 8 ticks: el valor por defecto es de aproximadamente un minuto. Más alto es más lento. |
| `.id('ns:nombre')` | no | Mira [Ids de receta](#ids-de-receta). |

Solo están disponibles las conversiones de salida fija. Las recetas que copian propiedades del bloque de entrada (como la receta existente de troncos a tronco de madera viviente) no se pueden crear desde scripts.

```js
BotaniaEvents.server(event => {
  event.pureDaisy('minecraft:mud', 'minecraft:clay').time(100).id('kubejs:mud_to_clay')

  event.pureDaisy(['minecraft:dirt', 'minecraft:coarse_dirt'], 'minecraft:grass_block')
})
```

### Comprobar si una receta existe: `event.exists(id)`

`event.exists('botania:runic_altar/water')` devuelve `true` si ya hay cargada una receta con ese id (de Botania, de otro mod o de un datapack, después de las eliminaciones hechas en `ServerEvents.recipes`). Las recetas que declaras en manejadores de `BotaniaEvents.server` solo se agregan cuando todos los manejadores han terminado, así que `exists` no las ve. Sirve para agregar una receta solo cuando otra no está presente, evitando errores de id duplicado:

```js
BotaniaEvents.server(event => {
  if (!event.exists('botania:runic_altar/water')) {
    event.runicAltar('2x botania:rune_water', ['botania:manasteel_ingot', 'minecraft:sugar_cane'])
      .mana(5200)
      .id('botania:runic_altar/water')
  }
})
```

## Ids de receta

| Situación | Id resultante |
| --- | --- |
| `.id('mymod:nombre')` | exactamente `mymod:nombre` |
| `.id('nombre')` (sin namespace) | `kubejs:nombre` |
| Sin `.id(...)` | `kubejs:botania_<máquina>/<salida>`, por ejemplo `kubejs:botania_runic_altar/rune_of_stone`. Si ya está ocupado, se agrega `_2`, `_3`... |

`<máquina>` es una de `runic_altar`, `petal_apothecary`, `mana_infusion`, `elven_trade`, `terra_plate`, `brew`, `pure_daisy`. `<salida>` es el nombre del ítem de salida (la primera salida en el comercio élfico), el nombre del brebaje o el nombre del bloque de salida.

Las recetas con id explícito se registran primero, así que se quedan con el id frente a las automáticas. Un id solo se puede usar una vez: usar el id de una receta existente (por ejemplo `botania:runic_altar/water`) es un error, a menos que primero quites esa receta (mira la sección siguiente).

Los ids sirven para los visores de recetas, los logros y para quitar la receta más tarde desde otro script. Dale un id explícito a todo lo que quieras mantener estable.

## Quitar o reemplazar recetas existentes

Orden de las operaciones durante una recarga de recetas:

1. KubeJS ejecuta `ServerEvents.recipes` al principio de todo, sobre los archivos de receta sin procesar. Aquí actúan `event.remove(...)`, `event.replaceInput(...)` y similares.
2. El juego interpreta todas las recetas.
3. Botania lanza `BotaniaEvents.server` y agrega tus recetas.

Lo que esto significa:

- **Las recetas existentes de Botania se pueden quitar con el evento de recetas estándar de KubeJS**, porque en ese momento siguen siendo archivos de receta normales:

  ```js
  ServerEvents.recipes(event => {
    event.remove({ id: 'botania:runic_altar/water' })
    event.remove({ type: 'botania:pure_daisy' })
  })
  ```

  Los ids de tipo de receta son `botania:runic_altar`, `botania:petal_apothecary`, `botania:mana_infusion`, `botania:elven_trade`, `botania:terra_plate`, `botania:brew`, `botania:pure_daisy` y `botania:state_copying_pure_daisy`. Los ids de las recetas propias de Botania tienen la forma `botania:<máquina>/<nombre>`, por ejemplo `botania:runic_altar/water`, `botania:brew/speed`, `botania:terra_plate/terrasteel_ingot`.

- **Las recetas agregadas con `BotaniaEvents.server` no se pueden quitar con `ServerEvents.recipes`**, porque todavía no existen cuando este se ejecuta. Para deshacerte de una, borra o cambia la línea que la crea.

- **Para reemplazar una receta**: quita la antigua en `ServerEvents.recipes` y agrega la nueva en `BotaniaEvents.server`. Puedes reutilizar el id antiguo, porque la eliminación lo dejó libre:

  ```js
  ServerEvents.recipes(event => {
    event.remove({ id: 'botania:runic_altar/water' })
  })

  BotaniaEvents.server(event => {
    event.runicAltar('2x botania:rune_water', [
      'botania:manasteel_ingot',
      'minecraft:sugar_cane',
      'minecraft:bone_meal'
    ]).mana(3000).id('botania:runic_altar/water')
  })
  ```

  No intentes reemplazar una receta agregándola con el mismo id sin quitar la antigua: se informa como id duplicado y tu receta nueva no se agrega.

- Los archivos de receta normales de datapack (`kubejs/data/<namespace>/recipe/...`) en los formatos de Botania siguen funcionando igual que siempre. `BotaniaEvents.server` es una alternativa a escribir esos JSON a mano.

## Errores comunes y mensajes de error

Los errores se escriben en el registro del juego, en `logs/kubejs/server.log` (recetas) o `logs/kubejs/startup.log` (runas), y se cuentan en el resumen de scripts de KubeJS (`... with N errors and M warnings`). Los mensajes empiezan con el archivo de script y la línea (por ejemplo `server_scripts:my_recipes.js#4`) y después `[Botania]`. En algunos casos la ubicación que se muestra es un marco interno (`EventHandlerContainer.java#41`); en ese caso guíate por la descripción de la receta que aparece en el mensaje para encontrar la línea.

A veces el mismo problema muestra el motivo dos veces seguidas; es normal.

| Mensaje (abreviado) | Causa | Solución |
| --- | --- | --- |
| `[Botania] Failed to add Botania runic altar recipe kubejs:x: Missing mana, call .mana(n)` | `runicAltar`, `manaInfusion` o `terraPlate` sin `.mana(n)`. | Agrega `.mana(n)`. |
| `[Botania] Invalid output in Botania runic altar recipe: Failed to read item stack from minecraft:not_an_item: Item with ID minecraft:not_an_item does not exist!` | Id de ítem mal escrito o inexistente (también ocurre con una runa que no se registró en el script de inicio). | Revisa el id; para runas propias revisa el script de inicio y reinicia el juego. |
| `[Botania] Invalid input in Botania mana infusion recipe: Expected a single ingredient with count 1, got 2` | El `input` de `manaInfusion` (o `.reagent(...)`) tiene una cantidad mayor que 1 o es una lista de varios ítems. | Usa un solo ítem o tag sin cantidad. |
| `[Botania] Invalid brew in Botania brew recipe: Unknown brew botania:nope` | El id del brebaje no existe. Un id de brebaje sin namespace se lee como `minecraft:`. | Usa `botania:<brebaje>` de la lista anterior. |
| `[Botania] Invalid input in Botania pure daisy recipe: Unknown block minecraft:nope_block` | El id de bloque de una entrada de la Margarita pura no existe. | Corrige el id del bloque. |
| `[Botania] Failed to add Botania runic altar recipe kubejs:x: Duplicate recipe id kubejs:x` | Otra receta ya usa ese id (tuya o de otro mod). | Usa otro id o quita primero la receta antigua. |
| `... Cannot have more than 16 ingredients` | Receta de Altar rúnico o de Botica de pétalos con más de 16 ingredientes (contados después de expandir `Nx`). | Usa 16 o menos. |
| `... Mana cost must be positive` / `... Mana cost must be at most a pool` | El `.mana(n)` de `manaInfusion` es 0 o mayor que 1000001 (un valor negativo se informa como maná que falta). | Usa un valor de 1 a 1000001. |
| `... Time must not be negative` | `.time(n)` de `pureDaisy` menor que 0. | Usa 0 o más. |
| `[Botania] Failed to register rune kubejs:x` (log de inicio) | El id de la runa no es válido o ya está registrado. | Usa un id nuevo en minúsculas. |

Otras cosas a tener en cuenta:

- Una receta con cualquier argumento inválido se omite por completo; el resto del script sigue ejecutándose.
- Si un script lanza un error de JavaScript normal dentro del manejador (un error de tipeo, una variable sin definir, un número de argumentos incorrecto), KubeJS lo registra y detiene ese manejador en la línea que falla. Las recetas declaradas antes de esa línea se agregan igual.
- `BotaniaEvents.registry` solo funciona en `startup_scripts` y `BotaniaEvents.server` solo en `server_scripts`. KubeJS rechaza usarlos en otra carpeta.
- `.mana`, `.catalyst`, `.reagent`, `.time` y `.group` existen solo en las máquinas indicadas arriba. Llamar a `.mana(...)` en un comercio élfico o en un brebaje es un error de script.
- Una runa propia sin modelo ni entrada de idioma funciona, pero se ve como el modelo de marcador de posición; el registro muestra `Unable to load model: 'kubejs:item/<nombre>'`. Agrega los recursos como se explica en [Registrar runas](#registrar-runas).

## Eventos nativos de Java para otros mods

La capa de KubeJS es una envoltura delgada sobre dos eventos que cualquier mod puede usar directamente. Están en `velrondevs.botania.event.BotaniaEvents` y funcionan con o sin KubeJS instalado.

### `BotaniaEvents.RegisterRunes` (bus de eventos del mod)

Se lanza durante el registro de ítems. Botania registra sus propias 16 runas con este mismo evento. Los oyentes se agregan al bus de eventos del mod, en el constructor de tu mod:

| Método | Descripción |
| --- | --- |
| `Item register(ResourceLocation id)` | Crea y registra una runa simple (`RuneItem`) con propiedades por defecto. |
| `Item register(ResourceLocation id, Item rune)` | Registra tu propio ítem. Para que se comporte como runa (la devuelve el altar, color en la botica) debe extender `velrondevs.botania.common.item.material.RuneItem`. |

Registrar el mismo id dos veces en un evento lanza `IllegalArgumentException`. Las runas registradas se agregan a la pestaña creativa de Botania.

### `BotaniaEvents.RegisterMachineRecipes` (bus de eventos del juego)

Se lanza en el servidor cada vez que se cargan las recetas (carga de mundo, inicio del servidor, `/reload`), después de interpretar las recetas de datapacks. No se lanza en el cliente; las recetas se sincronizan a los clientes como siempre. Los oyentes se agregan a `NeoForge.EVENT_BUS`.

| Método | Descripción |
| --- | --- |
| `boolean exists(ResourceLocation id)` | Indica si el id lo usa una receta cargada o ya agregada. |
| `void add(ResourceLocation id, Recipe<?> recipe)` | Agrega cualquier receta. Lanza `IllegalArgumentException` si el id está duplicado. |
| `List<RecipeHolder<?>> getAdded()` | Recetas agregadas hasta ahora. |
| `void runicAltar(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs)` | Altar rúnico. |
| `void petalApothecary(ResourceLocation id, ItemStack output, Ingredient reagent, Ingredient... inputs)` | Botica de pétalos. |
| `void manaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana)` | Infusión de maná sin catalizador. |
| `void manaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana, StateIngredient catalyst)` | Infusión de maná con catalizador. |
| `void elvenTrade(ResourceLocation id, ItemStack[] outputs, Ingredient... inputs)` | Comercio élfico. |
| `void terraPlate(ResourceLocation id, int mana, ItemStack output, Ingredient... inputs)` | Placa de aglomeración terrestre. |
| `void brew(ResourceLocation id, Brew brew, Ingredient... inputs)` | Fermentador botánico. |
| `void pureDaisy(ResourceLocation id, StateIngredient input, BlockState output, int time)` | Margarita pura. |

Ejemplo:

```java
public MyMod(IEventBus modBus) {
    modBus.addListener(MyMod::registerRunes);
    NeoForge.EVENT_BUS.addListener(MyMod::registerMachineRecipes);
}

private static void registerRunes(BotaniaEvents.RegisterRunes event) {
    event.register(ResourceLocation.fromNamespaceAndPath("mymod", "rune_of_stone"));
}

private static void registerMachineRecipes(BotaniaEvents.RegisterMachineRecipes event) {
    ResourceLocation id = ResourceLocation.fromNamespaceAndPath("mymod", "runic_altar/diamond");
    if (!event.exists(id)) {
        event.runicAltar(id, new ItemStack(Items.DIAMOND), 5000,
                Ingredient.of(BotaniaItems.runeEarth), Ingredient.of(Items.STONE));
    }
}
```

Las recetas agregadas por este evento viven solo en memoria y se reconstruyen en cada recarga; no son archivos de un datapack. Los nombres de clase y paquete son los de este port (`velrondevs.botania`), no los del Botania original.

## Licencia

Mira la sección "License and attribution" de la descripción del mod.
