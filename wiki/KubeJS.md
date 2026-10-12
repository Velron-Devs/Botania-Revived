# KubeJS integration

[Versión en español](KubeJS.es.md)

Botania Revived adds an optional KubeJS integration. It lets you do two things from scripts:

- register new runes (startup scripts);
- add recipes for Botania's machines: Runic Altar, Petal Apothecary, Mana Infusion (Mana Pool), Elven Trade, Terrestrial Agglomeration Plate, Botanical Brewery and Pure Daisy (server scripts).

Everything lives in one event group called `BotaniaEvents`, with two events:

| Event | Script folder | What it is for |
| --- | --- | --- |
| `BotaniaEvents.registry(event => ...)` | `startup_scripts` | Register runes |
| `BotaniaEvents.server(event => ...)` | `server_scripts` | Add machine recipes |

## Contents

1. [Requirements](#requirements)
2. [Where to put the scripts](#where-to-put-the-scripts)
3. [When the events run](#when-the-events-run)
4. [Registering runes](#registering-runes)
5. [Machine recipes](#machine-recipes)
6. [Recipe ids](#recipe-ids)
7. [Removing or replacing existing recipes](#removing-or-replacing-existing-recipes)
8. [Common mistakes and error messages](#common-mistakes-and-error-messages)
9. [Native Java events for other mods](#native-java-events-for-other-mods)
10. [License](#license)

## Requirements

| Component | Version |
| --- | --- |
| Minecraft | 1.21.1 |
| Loader | NeoForge 21.1 |
| KubeJS | 2101.7.2-build.368 or newer (the KubeJS line for Minecraft 1.21.1, "2101") |
| Rhino | 2101.2.7-build.85 or newer (installed together with KubeJS) |

KubeJS is an optional dependency. Without it Botania Revived runs normally and the `BotaniaEvents` group simply does not exist.

Scripts are plain JavaScript run by Rhino, the script engine that KubeJS uses. The examples in this page use only simple syntax (arrow functions and arrays) that KubeJS scripts support.

## Where to put the scripts

KubeJS creates its folder the first time the game starts with it installed. It is located in the game directory (the same place as `mods` and `config`):

```
kubejs/
  startup_scripts/    runes: BotaniaEvents.registry
  server_scripts/     recipes: BotaniaEvents.server
  assets/             models, textures and lang files for your runes
  data/               regular datapack files
```

Any `.js` file inside those folders is loaded; the file name does not matter. Logs are written to `logs/kubejs/startup.log` and `logs/kubejs/server.log`.

## When the events run

| Step | What happens |
| --- | --- |
| 1. Game launch | KubeJS loads every startup script. |
| 2. Item registration (still during launch) | Botania posts its rune registration event. Its own 16 runes are registered by that event and `BotaniaEvents.registry` is posted as part of it; every `event.rune(...)` call adds an item. |
| 3. World load, server start or `/reload` | KubeJS loads the server scripts (registering the `BotaniaEvents.server` handlers). |
| 4. Recipe reload (same step) | The recipe manager finishes loading all datapack recipes, then Botania posts `BotaniaEvents.server`. Your recipes are created and added; the console prints `[Botania] Added N machine recipes from scripts`. |

Consequences:

- Runes can only be added at game launch. Changing a startup script needs a full restart of the game; `/reload` does not re-run it.
- Machine recipes are rebuilt on every `/reload`, so you can edit a server script and test it with `/reload` without leaving the world.
- If no script has a `BotaniaEvents.server` handler, nothing is posted and nothing is logged.
- The server event runs after KubeJS's own `ServerEvents.recipes`. See [Removing or replacing existing recipes](#removing-or-replacing-existing-recipes).

On a multiplayer server the startup script (runes) has to be present on the server and on every client, because it changes the item registry. The server scripts are only needed on the server.

## Registering runes

Runes are the items consumed as catalysts in Runic Altar recipes. A rune registered from a script behaves exactly like Botania's own: it can be used as an ingredient in Runic Altar recipes and the altar gives it back after crafting (unless the player is in creative mode), and it appears in the Botania creative tab.

### Syntax

```js
// kubejs/startup_scripts/botania_runes.js
BotaniaEvents.registry(event => {
  event.rune('kubejs:rune_of_stone')
})
```

| Call | Argument | Result |
| --- | --- | --- |
| `event.rune(id)` | Item id, written as `namespace:name`. Without a namespace the id becomes `kubejs:<name>`. Lowercase letters, digits, `_`, `-`, `.` and `/` only. | Registers the rune and returns the item. If the id is already taken by another rune (for example `botania:rune_water`) or is invalid, an error is logged in the startup log and nothing is registered. |

### Giving the rune a model, texture and name

The script only registers the item. Without resources the item shows the missing-model placeholder and a raw translation key. Add them in the `kubejs/assets` folder (the folder works as a resource pack). For `kubejs:rune_of_stone`:

`kubejs/assets/kubejs/models/item/rune_of_stone.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "kubejs:item/rune_of_stone"
  }
}
```

`kubejs/assets/kubejs/textures/item/rune_of_stone.png`: a 16x16 PNG.

`kubejs/assets/kubejs/lang/en_us.json`

```json
{
  "item.kubejs.rune_of_stone": "Rune of Stone"
}
```

Resource files in `kubejs/assets` are client resources: reload them with F3+T or restart the game.

### Complete example: a new rune and its recipe

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

## Machine recipes

All recipe calls are made on the `event` object of `BotaniaEvents.server`. Each call creates a recipe and returns it, so extra settings are chained with `.`:

```js
BotaniaEvents.server(event => {
  event.manaInfusion('botania:manasteel_ingot', 'minecraft:iron_ingot')
    .mana(3000)
    .id('kubejs:my_manasteel')
})
```

You can have several `BotaniaEvents.server` handlers in as many files as you like; all the recipes are collected and added together.

Recipes that fail (a wrong argument, a missing required setting) are skipped and an error is written to the console. The rest of the recipes still load.

### How to write items and ingredients

| Kind | Where it is used | Accepted forms |
| --- | --- | --- |
| Output item | outputs of every recipe | `'minecraft:diamond'`, `'3x minecraft:diamond'`, `Item.of('minecraft:diamond', 3)`. Must be a real item; tags are not valid. |
| Ingredient list | `inputs` of altar, apothecary, elven trade, terra plate, brewery | One value or an array. Each entry: `'minecraft:iron_ingot'`, `'#c:ingots/iron'` (tag), `'3x minecraft:iron_ingot'`, `'2x #c:ingots/iron'`. |
| Single ingredient | `input` of mana infusion, `reagent` of petal apothecary | One entry, with no count (or `1x`). |
| Block | input and `catalyst` of mana infusion, input of pure daisy | `'minecraft:stone'`, `'#minecraft:logs'` (block tag), `'minecraft:oak_log[axis=y]'` (an exact block state), or an array of those. |
| Output block | output of pure daisy | `'botania:livingrock'` or a block state such as `'minecraft:oak_log[axis=y]'`. Air is rejected. |
| Brew | first argument of `brew` | Brew id with namespace, for example `'botania:speed'`. |

A count in front of an ingredient (`'3x ...'`) means "that ingredient three times": it is the same as writing it three times in the array. It is not a stack size inside a single slot.

### Runic Altar: `event.runicAltar(output, inputs)`

Block: Runic Altar. The inputs are thrown on the altar, which is filled with mana. As usual, the craft is completed with the Wand of the Forest and consumes one Livingrock thrown on the altar.

| Part | Required | Description |
| --- | --- | --- |
| `output` | yes | Result item, with count. |
| `inputs` | yes | List of ingredients. Maximum 16 entries (counted after expanding `Nx`). |
| `.mana(n)` | yes | Mana the altar needs. Botania's own recipes use values from 5200 (Rune of Water) to 100000. |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

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

### Petal Apothecary: `event.petalApothecary(output, inputs)`

Block: Petal Apothecary. Petals and other items are thrown into a water-filled apothecary, and the reagent (a seed by default) triggers the craft.

| Part | Required | Description |
| --- | --- | --- |
| `output` | yes | Result item, with count. |
| `inputs` | yes | List of ingredients. Maximum 16 entries. |
| `.reagent(item)` | no | The item that triggers the craft. Default: any item in the tag `botania:seed_apothecary_reagent` (wheat, beetroot, melon and pumpkin seeds, plus the `c:seeds` tag). One single ingredient, count 1. |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

```js
BotaniaEvents.server(event => {
  event.petalApothecary('minecraft:poppy', ['2x botania:red_petal', 'botania:white_petal'])
    .reagent('botania:livingwood_twig')
    .id('kubejs:poppy_from_petals')
})
```

### Mana Infusion: `event.manaInfusion(output, input)`

Block: Mana Pool. An item thrown in the pool is converted while the pool pays the mana. Optionally a block under the pool (a catalyst) is required.

| Part | Required | Description |
| --- | --- | --- |
| `output` | yes | Result item, with count. |
| `input` | yes | One single ingredient with count 1 (an item or a tag). |
| `.mana(n)` | yes | Mana cost. Must be greater than 0 and at most 1000001. |
| `.catalyst(block)` | no | Block that must be under the pool: `'botania:alchemy_catalyst'`, `'botania:conjuration_catalyst'`, a block tag, a block state, or an array. |
| `.group(name)` | no | Optional group name (a text label shared by related recipes). Default: none. |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

```js
BotaniaEvents.server(event => {
  event.manaInfusion('2x minecraft:emerald', 'minecraft:diamond')
    .mana(3000)
    .catalyst('botania:conjuration_catalyst')
    .id('kubejs:diamond_to_emeralds')

  event.manaInfusion('botania:manasteel_ingot', '#c:ingots/copper').mana(3000)
})
```

### Elven Trade: `event.elvenTrade(outputs, inputs)`

Block: Alfheim portal (the Elven Gateway). Items thrown into the open portal are traded for the outputs.

| Part | Required | Description |
| --- | --- | --- |
| `outputs` | yes | One or more result items, each with its own count. At least one. |
| `inputs` | yes | List of ingredients. |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

An Elven Trade has no mana cost and no other settings.

```js
BotaniaEvents.server(event => {
  event.elvenTrade(['minecraft:netherite_scrap', '3x minecraft:gold_nugget'], ['minecraft:ancient_debris'])
    .id('kubejs:ancient_debris_trade')

  event.elvenTrade(['botania:dragonstone'], ['2x botania:mana_diamond'])
})
```

### Terrestrial Agglomeration Plate: `event.terraPlate(output, inputs)`

Block: Terrestrial Agglomeration Plate. The items are thrown on top of the plate and it is fed with mana.

| Part | Required | Description |
| --- | --- | --- |
| `output` | yes | Result item, with count. |
| `inputs` | yes | List of ingredients. |
| `.mana(n)` | yes | Mana the plate needs. Botania's Terrasteel Ingot uses 500000. |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

```js
BotaniaEvents.server(event => {
  event.terraPlate('minecraft:nether_star', [
    'botania:terrasteel_ingot',
    'botania:mana_diamond',
    'botania:mana_pearl'
  ]).mana(250000).id('kubejs:nether_star')
})
```

### Botanical Brewery: `event.brew(brew, inputs)`

Block: Botanical Brewery. The brew (the potion effect) already exists in Botania; the recipe only defines which ingredients produce it. You cannot create new brews from scripts.

| Part | Required | Description |
| --- | --- | --- |
| `brew` | yes | Brew id **with** the `botania:` namespace. |
| `inputs` | yes | List of ingredients (the vial is not an ingredient). |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

Available brew ids from Botania: `botania:speed`, `botania:strength`, `botania:haste`, `botania:healing`, `botania:jump_boost`, `botania:regen`, `botania:regen_weak`, `botania:resistance`, `botania:fire_resistance`, `botania:water_breathing`, `botania:invisibility`, `botania:night_vision`, `botania:absorption`, `botania:overload`, `botania:soul_cross`, `botania:feather_feet`, `botania:emptiness`, `botania:bloodthirst`, `botania:allure`, `botania:clear`. Brews added by other mods are accepted too, using their own ids.

```js
BotaniaEvents.server(event => {
  event.brew('botania:speed', ['minecraft:nether_wart', 'minecraft:sugar', 'minecraft:glowstone_dust'])
    .id('kubejs:speed_brew_alt')
})
```

### Pure Daisy: `event.pureDaisy(input, output)`

Block: Pure Daisy. The flower slowly converts the blocks around it.

| Part | Required | Description |
| --- | --- | --- |
| `input` | yes | Block (or block tag, block state, or an array) that gets converted. |
| `output` | yes | Block or block state that replaces it. |
| `.time(n)` | no | Conversion time. Default 150. Must be 0 or more. The flower checks its eight neighbouring blocks one per tick, so each block advances once every 8 ticks: the default is about one minute. Higher is slower. |
| `.id('ns:name')` | no | See [Recipe ids](#recipe-ids). |

Only fixed-output conversions are available. Recipes that copy block properties from the input (such as the existing log to livingwood-log recipe) cannot be created from scripts.

```js
BotaniaEvents.server(event => {
  event.pureDaisy('minecraft:mud', 'minecraft:clay').time(100).id('kubejs:mud_to_clay')

  event.pureDaisy(['minecraft:dirt', 'minecraft:coarse_dirt'], 'minecraft:grass_block')
})
```

### Checking if a recipe exists: `event.exists(id)`

`event.exists('botania:runic_altar/water')` returns `true` if a recipe with that id is already loaded (from Botania, another mod or a datapack, after any removals made in `ServerEvents.recipes`). Recipes you declare in `BotaniaEvents.server` handlers are only added when all handlers have finished, so `exists` does not see them. It is useful to add a recipe only when another one is absent, avoiding duplicate-id errors:

```js
BotaniaEvents.server(event => {
  if (!event.exists('botania:runic_altar/water')) {
    event.runicAltar('2x botania:rune_water', ['botania:manasteel_ingot', 'minecraft:sugar_cane'])
      .mana(5200)
      .id('botania:runic_altar/water')
  }
})
```

## Recipe ids

| Situation | Resulting id |
| --- | --- |
| `.id('mymod:name')` | exactly `mymod:name` |
| `.id('name')` (no namespace) | `kubejs:name` |
| No `.id(...)` | `kubejs:botania_<machine>/<output>`, for example `kubejs:botania_runic_altar/rune_of_stone`. If it is already taken, `_2`, `_3`... is appended. |

`<machine>` is one of `runic_altar`, `petal_apothecary`, `mana_infusion`, `elven_trade`, `terra_plate`, `brew`, `pure_daisy`. `<output>` is the output item name (the first output for Elven Trade), the brew name or the output block name.

Recipes with an explicit id are registered first, so they win the id over automatic ones. An id can only be used once: using the id of an existing recipe (for example `botania:runic_altar/water`) is an error, unless you first remove that recipe (see the next section).

Ids are useful for recipe viewers, advancements and for removing the recipe later in another script. Give an explicit id to anything you want to keep stable.

## Removing or replacing existing recipes

Order of operations during a recipe reload:

1. KubeJS runs `ServerEvents.recipes` at the very start, on the raw recipe files. This is where `event.remove(...)`, `event.replaceInput(...)` and so on act.
2. The game parses all the recipes.
3. Botania posts `BotaniaEvents.server` and adds your recipes.

What this means:

- **Botania's existing recipes can be removed with the standard KubeJS recipe event**, because they are still ordinary recipe files at that point:

  ```js
  ServerEvents.recipes(event => {
    event.remove({ id: 'botania:runic_altar/water' })
    event.remove({ type: 'botania:pure_daisy' })
  })
  ```

  Recipe type ids are `botania:runic_altar`, `botania:petal_apothecary`, `botania:mana_infusion`, `botania:elven_trade`, `botania:terra_plate`, `botania:brew`, `botania:pure_daisy` and `botania:state_copying_pure_daisy`. Botania's own recipe ids look like `botania:<machine>/<name>`, for example `botania:runic_altar/water`, `botania:brew/speed`, `botania:terra_plate/terrasteel_ingot`.

- **Recipes added with `BotaniaEvents.server` cannot be removed with `ServerEvents.recipes`**, because they do not exist yet when it runs. To get rid of one, delete or change the line that creates it.

- **To replace a recipe**: remove the old one in `ServerEvents.recipes` and add the new one in `BotaniaEvents.server`. You can reuse the old id, because it has been freed by the removal:

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

  Do not try to replace a recipe by adding it with the same id without removing the old one: that is reported as a duplicate id and your new recipe is not added.

- Regular datapack recipe files (`kubejs/data/<namespace>/recipe/...`) in Botania's formats keep working exactly as before. `BotaniaEvents.server` is an alternative to writing those JSON files by hand.

## Common mistakes and error messages

Errors are written to the game log, to `logs/kubejs/server.log` (recipes) or `logs/kubejs/startup.log` (runes), and are counted in the KubeJS script summary (`... with N errors and M warnings`). Messages start with the script file and line (for example `server_scripts:my_recipes.js#4`) and then `[Botania]`. In some cases the location shown is an internal frame (`EventHandlerContainer.java#41`); in that case rely on the recipe description in the message to find the line.

The same problem sometimes shows the reason twice in a row; that is normal.

| Message (shortened) | Cause | Fix |
| --- | --- | --- |
| `[Botania] Failed to add Botania runic altar recipe kubejs:x: Missing mana, call .mana(n)` | `runicAltar`, `manaInfusion` or `terraPlate` without `.mana(n)`. | Add `.mana(n)`. |
| `[Botania] Invalid output in Botania runic altar recipe: Failed to read item stack from minecraft:not_an_item: Item with ID minecraft:not_an_item does not exist!` | Misspelled or missing item id (also happens with a rune that was not registered in the startup script). | Check the id; for custom runes check the startup script and restart the game. |
| `[Botania] Invalid input in Botania mana infusion recipe: Expected a single ingredient with count 1, got 2` | `input` of `manaInfusion` (or `.reagent(...)`) has a count above 1 or is a list of several items. | Use one item or tag with no count. |
| `[Botania] Invalid brew in Botania brew recipe: Unknown brew botania:nope` | The brew id does not exist. A brew id without a namespace is read as `minecraft:`. | Use `botania:<brew>` from the list above. |
| `[Botania] Invalid input in Botania pure daisy recipe: Unknown block minecraft:nope_block` | The block id of a Pure Daisy input does not exist. | Fix the block id. |
| `[Botania] Failed to add Botania runic altar recipe kubejs:x: Duplicate recipe id kubejs:x` | Another recipe already uses that id (yours or another mod's). | Use another id or remove the old recipe first. |
| `... Cannot have more than 16 ingredients` | Runic Altar or Petal Apothecary recipe with more than 16 ingredients (counted after expanding `Nx`). | Use 16 or fewer. |
| `... Mana cost must be positive` / `... Mana cost must be at most a pool` | `.mana(n)` of `manaInfusion` is 0 or above 1000001 (a negative value is reported as missing mana). | Use a value from 1 to 1000001. |
| `... Time must not be negative` | `.time(n)` of `pureDaisy` below 0. | Use 0 or more. |
| `[Botania] Failed to register rune kubejs:x` (startup log) | The rune id is invalid or already registered. | Use a new, lowercase id. |

Other things to watch for:

- A recipe with any invalid argument is skipped entirely; the rest of the script keeps running.
- If a script throws an ordinary JavaScript error inside the handler (a typo, an undefined variable, a wrong number of arguments), KubeJS logs it and stops that handler at the failing line. Recipes declared before the failing line are still added.
- `BotaniaEvents.registry` only works in `startup_scripts` and `BotaniaEvents.server` only in `server_scripts`. Using them in another folder is rejected by KubeJS.
- `.mana`, `.catalyst`, `.reagent`, `.time` and `.group` exist only on the machines listed above. Calling `.mana(...)` on an elven trade or a brew is a script error.
- A custom rune without a model and a lang entry works, but looks like the missing-model placeholder; the log shows `Unable to load model: 'kubejs:item/<name>'`. Add the assets as described in [Registering runes](#registering-runes).

## Native Java events for other mods

The KubeJS layer is a thin wrapper over two events that any mod can use directly. They live in `velrondevs.botania.event.BotaniaEvents`, and work with or without KubeJS installed.

### `BotaniaEvents.RegisterRunes` (mod event bus)

Posted during item registration. Botania registers its own 16 runes through this same event. Listeners are added to the mod event bus, in your mod's constructor:

| Method | Description |
| --- | --- |
| `Item register(ResourceLocation id)` | Creates and registers a plain rune (`RuneItem`) with default properties. |
| `Item register(ResourceLocation id, Item rune)` | Registers your own item. For it to behave as a rune (returned by the altar, apothecary tint) it should extend `velrondevs.botania.common.item.material.RuneItem`. |

Registering the same id twice in one event throws `IllegalArgumentException`. Registered runes are added to the Botania creative tab.

### `BotaniaEvents.RegisterMachineRecipes` (game event bus)

Posted on the server every time recipes are loaded (world load, server start, `/reload`), after the datapack recipes were parsed. It is not posted on the client; recipes are synchronized to clients as usual. Listeners are added to `NeoForge.EVENT_BUS`.

| Method | Description |
| --- | --- |
| `boolean exists(ResourceLocation id)` | Is the id used by a loaded or already-added recipe. |
| `void add(ResourceLocation id, Recipe<?> recipe)` | Adds any recipe. Throws `IllegalArgumentException` on a duplicate id. |
| `List<RecipeHolder<?>> getAdded()` | Recipes added so far. |
| `void runicAltar(ResourceLocation id, ItemStack output, int mana, Ingredient... inputs)` | Runic Altar. |
| `void petalApothecary(ResourceLocation id, ItemStack output, Ingredient reagent, Ingredient... inputs)` | Petal Apothecary. |
| `void manaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana)` | Mana Infusion without catalyst. |
| `void manaInfusion(ResourceLocation id, ItemStack output, Ingredient input, int mana, StateIngredient catalyst)` | Mana Infusion with catalyst. |
| `void elvenTrade(ResourceLocation id, ItemStack[] outputs, Ingredient... inputs)` | Elven Trade. |
| `void terraPlate(ResourceLocation id, int mana, ItemStack output, Ingredient... inputs)` | Terrestrial Agglomeration Plate. |
| `void brew(ResourceLocation id, Brew brew, Ingredient... inputs)` | Botanical Brewery. |
| `void pureDaisy(ResourceLocation id, StateIngredient input, BlockState output, int time)` | Pure Daisy. |

Example:

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

Recipes added through this event live only in memory and are rebuilt on each reload; they are not files in a datapack. The class and package names are those of this port (`velrondevs.botania`), not the original Botania's.

## License

See the "License and attribution" section of the mod description.
