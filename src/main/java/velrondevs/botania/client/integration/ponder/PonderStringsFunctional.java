package velrondevs.botania.client.integration.ponder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class PonderStringsFunctional {
	public static final String PREFIX = "functional_flower_";

	private static final Map<String, List<String>> ENGLISH = new LinkedHashMap<>();
	private static final Map<String, List<String>> SPANISH = new LinkedHashMap<>();

	@FunctionalInterface
	public interface LangConsumer {
		void accept(String key, String english, String spanish);
	}

	private PonderStringsFunctional() {}

	static String sceneId(String flower) {
		return PREFIX + flower;
	}

	private static void add(String flower, String[] english, String[] spanish) {
		if (english.length != spanish.length) {
			throw new IllegalStateException("Ponder text mismatch for " + flower);
		}
		ENGLISH.put(sceneId(flower), List.of(english));
		SPANISH.put(sceneId(flower), List.of(spanish));
	}

	static void register(Map<String, List<String>> scenes) {
		scenes.putAll(ENGLISH);
	}

	public static void lang(LangConsumer consumer) {
		Map<String, String[]> sorted = new TreeMap<>();
		for (Map.Entry<String, List<String>> entry : ENGLISH.entrySet()) {
			List<String> english = entry.getValue();
			List<String> spanish = SPANISH.get(entry.getKey());
			String base = "botania.ponder." + entry.getKey();
			sorted.put(base + ".header", new String[] { english.get(0), spanish.get(0) });
			for (int i = 1; i < english.size(); i++) {
				sorted.put(base + ".text_" + i, new String[] { english.get(i), spanish.get(i) });
			}
		}
		sorted.forEach((key, values) -> consumer.accept(key, values[0], values[1]));
	}

	static {
		add("agricarnation", new String[] {
				"Growing Plants with the Agricarnation",
				"The Agricarnation makes plants around it grow faster: crops, saplings, sugar cane, cacti and many more",
				"Like most functional flowers it runs on Mana from a Mana Pool. It binds by itself to the nearest Pool within 10 blocks, or you can bind it with the Wand of the Forest: click the flower, then the Pool",
				"Every 6 ticks it picks a random spot in its area and gives the plant there an extra growth tick. Each boost costs 5 Mana",
				"It covers an 11x11 area, from 1 block below to 4 blocks above the flower. The Agricarnation Petite only covers 5x5",
				"Azaleas get a bone meal attempt instead. Grass, mushrooms and mangrove leaves are ignored. A Redstone signal turns the flower off"
		}, new String[] {
				"Hacer crecer plantas con la agricarnaci\u00f3n",
				"La agricarnaci\u00f3n hace que las plantas a su alrededor crezcan m\u00e1s r\u00e1pido: cultivos, brotes, ca\u00f1a de az\u00facar, cactus y muchas m\u00e1s",
				"Como casi todas las flores funcionales, funciona con man\u00e1 de una pila de man\u00e1. Se vincula sola a la pila m\u00e1s cercana a menos de 10 bloques, o puedes vincularla con la varita del bosque: haz clic en la flor y luego en la pila",
				"Cada 6 ticks elige un punto al azar de su \u00e1rea y le da a la planta que hay all\u00ed un tick de crecimiento extra. Cada impulso cuesta 5 de man\u00e1",
				"Cubre un \u00e1rea de 11x11, desde 1 bloque por debajo hasta 4 bloques por encima de la flor. La agricarnacioncita solo cubre 5x5",
				"Las azaleas reciben un intento de polvo de hueso en su lugar. La hierba, los champi\u00f1ones y las hojas de mangle se ignoran. Una se\u00f1al de redstone apaga la flor"
		});
		add("bellethorn", new String[] {
				"Hurting Mobs with the Bellethorne",
				"The Bellethorne hurts every creature near it except players. Use it to kill the mobs that end up in a farm",
				"It takes its Mana from a Mana Pool within 10 blocks. Each hit deals 4 damage and costs 24 Mana",
				"Every 5 ticks it strikes each mob in range that is not still flashing red from the last hit. Witches take 20 damage",
				"Its range is 6 blocks in every direction, a 13x13x13 cube. The Bellethorne Petite only reaches 1 block",
				"Careful: animals, villagers and pets get hurt too. Only players are safe. A Redstone signal turns the flower off"
		}, new String[] {
				"Herir criaturas con la bellap\u00faa",
				"La bellap\u00faa hiere a todas las criaturas cercanas excepto a los jugadores. \u00dasala para matar a los mobs que acaban en una granja",
				"Obtiene su man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada golpe hace 4 de da\u00f1o y cuesta 24 de man\u00e1",
				"Cada 5 ticks golpea a cada mob en rango que no siga parpadeando en rojo por el golpe anterior. Las brujas reciben 20 de da\u00f1o",
				"Su alcance es de 6 bloques en todas direcciones, un cubo de 13x13x13. La bellap\u00faita solo alcanza 1 bloque",
				"Cuidado: tambi\u00e9n hiere a animales, aldeanos y mascotas. Solo los jugadores est\u00e1n a salvo. Una se\u00f1al de redstone apaga la flor"
		});
		add("bergamute", new String[] {
				"Muffling Sounds with the Bergamute",
				"The Bergamute muffles sounds around it. It does not need any Mana",
				"Sounds made within 4 blocks of a Bergamute play at half volume. Every extra Bergamute nearby halves them again, up to 8 flowers",
				"It also blocks vibrations: a Sculk Sensor does not notice vibrations that travel through the Bergamute's range",
				"Horns do not harvest blocks in its range, and the Drum of the Gathering skips the mobs inside it. A Redstone signal turns the flower off"
		}, new String[] {
				"Amortiguar sonidos con la bergamute",
				"La bergamute amortigua los sonidos a su alrededor. No necesita man\u00e1",
				"Los sonidos producidos a menos de 4 bloques de una bergamute suenan a la mitad de volumen. Cada bergamute extra cercana lo reduce otra vez a la mitad, hasta 8 flores",
				"Tambi\u00e9n bloquea vibraciones: un sensor de sculk no detecta las vibraciones que atraviesan el rango de la bergamute",
				"Los cuernos no cosechan bloques en su rango y el tambor de la recolecci\u00f3n ignora a los mobs que hay dentro. Una se\u00f1al de redstone apaga la flor"
		});
		add("bubbell", new String[] {
				"Air Bubbles with the Bubbell",
				"The Bubbell pushes water away, making an air bubble you can walk and breathe in",
				"It draws Mana from a Mana Pool within 10 blocks and burns 4 Mana every tick while active, so it needs a steady supply",
				"The bubble starts small and grows by one block every half second, up to a radius of 12 blocks. The Bubbell Petite stops at 6",
				"The water is not destroyed, only replaced by special air. When the flower runs out of Mana or is broken, the water flows back"
		}, new String[] {
				"Burbujas de aire con la burbumpana",
				"La burbumpana aparta el agua y crea una burbuja de aire en la que puedes caminar y respirar",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques y gasta 4 de man\u00e1 cada tick mientras est\u00e1 activa, as\u00ed que necesita un suministro constante",
				"La burbuja empieza peque\u00f1a y crece un bloque cada medio segundo, hasta un radio de 12 bloques. La burbumpanita se detiene en 6",
				"El agua no se destruye, solo se sustituye por un aire especial. Cuando la flor se queda sin man\u00e1 o se rompe, el agua vuelve"
		});
		add("clayconia", new String[] {
				"Making Clay with the Clayconia",
				"The Clayconia turns Sand into Clay Balls",
				"It draws Mana from a Mana Pool within 10 blocks. Every Sand block it converts costs 80 Mana",
				"Every quarter of a second it removes a random Sand block in its area and drops a Clay Ball where it was. Red Sand works too",
				"Its area is 11x11 blocks and reaches 3 blocks up and down. The Clayconia Petite covers 5x5, 1 block up and down"
		}, new String[] {
				"Hacer arcilla con la arciconia",
				"La arciconia convierte la arena en bolas de arcilla",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada bloque de arena convertido cuesta 80 de man\u00e1",
				"Cada cuarto de segundo elimina un bloque de arena al azar de su \u00e1rea y suelta una bola de arcilla en su lugar. La arena roja tambi\u00e9n sirve",
				"Su \u00e1rea es de 11x11 bloques y llega 3 bloques hacia arriba y hacia abajo. La arciconita cubre 5x5, 1 bloque arriba y abajo"
		});
		add("daffomill", new String[] {
				"Blowing Items with the Daffomill",
				"The Daffomill blows dropped items away in the direction it faces",
				"It draws Mana from a Mana Pool within 10 blocks, but barely uses any: 1 Mana keeps it blowing for a whole second",
				"Items in a strip 5 blocks wide and 16 blocks long in front of it are pushed along. It reaches 3 blocks up and down too",
				"It faces the way you were looking when you placed it. Sneak and right-click it with the Wand of the Forest to turn it clockwise",
				"A Redstone signal stops the wind. Use it to carry drops to a Hopperhock or a collection point"
		}, new String[] {
				"Soplar objetos con el polinillo",
				"El polinillo empuja con su viento los objetos del suelo en la direcci\u00f3n a la que mira",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques, pero apenas lo usa: 1 de man\u00e1 lo mantiene soplando un segundo entero",
				"Empuja los objetos en una franja de 5 bloques de ancho y 16 de largo delante de \u00e9l. Tambi\u00e9n alcanza 3 bloques arriba y abajo",
				"Mira hacia donde mirabas al colocarlo. Ag\u00e1chate y haz clic derecho con la varita del bosque para girarlo en el sentido de las agujas del reloj",
				"Una se\u00f1al de redstone detiene el viento. \u00dasalo para llevar el bot\u00edn hasta una tolvalcea o un punto de recogida"
		});
		add("dreadthorn", new String[] {
				"Culling Animals with the Dreadthorne",
				"The Dreadthorne is a Bellethorne that only attacks adult animals. Babies, monsters and players are left alone",
				"It draws Mana from a Mana Pool within 10 blocks. Each hit deals 4 damage and costs 30 Mana",
				"Put it in an animal pen: the adults are killed for their drops while the babies grow up safely",
				"It reaches 6 blocks in every direction and strikes every 5 ticks. A Redstone signal turns it off, for example while you breed the animals"
		}, new String[] {
				"Sacrificar animales con la temep\u00faa",
				"La temep\u00faa es una bellap\u00faa que solo ataca a animales adultos. Las cr\u00edas, los monstruos y los jugadores no sufren da\u00f1o",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada golpe hace 4 de da\u00f1o y cuesta 30 de man\u00e1",
				"Ponla en un corral: los adultos mueren y sueltan su bot\u00edn mientras las cr\u00edas crecen a salvo",
				"Alcanza 6 bloques en todas direcciones y golpea cada 5 ticks. Una se\u00f1al de redstone la apaga, por ejemplo mientras cr\u00edas a los animales"
		});
		add("exoflame", new String[] {
				"Heating Furnaces with the Exoflame",
				"The Exoflame heats the furnaces around it, so they smelt without any fuel",
				"It draws Mana from a Mana Pool within 10 blocks. Whenever a furnace runs out of heat, it adds 10 seconds of burning for 300 Mana",
				"It also makes them cook 50% faster. Furnaces, Smokers and Blast Furnaces all work",
				"Its area is 11x11 blocks, 2 blocks up and down. It only heats furnaces that have something to smelt"
		}, new String[] {
				"Calentar hornos con la exollama",
				"La exollama calienta los hornos a su alrededor, as\u00ed que funden sin combustible",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada vez que un horno se queda sin calor, le a\u00f1ade 10 segundos de fuego por 300 de man\u00e1",
				"Adem\u00e1s cocinan un 50 % m\u00e1s r\u00e1pido. Funciona con hornos, ahumadores y altos hornos",
				"Su \u00e1rea es de 11x11 bloques, 2 bloques arriba y abajo. Solo calienta los hornos que tienen algo que fundir"
		});
		add("fallen_kanade", new String[] {
				"Healing with the Fallen Kanade",
				"The Fallen Kanade heals players and tamed animals standing near it",
				"It draws Mana from a Mana Pool within 10 blocks. Every heal costs 120 Mana",
				"Anyone in range without Regeneration gets Regeneration III for 3 seconds, renewed as soon as it ends",
				"Its range is only 2 blocks, a 5x5x5 area. It works on players, tamed wolves, cats and parrots, and tamed horses. Wild animals and monsters are ignored"
		}, new String[] {
				"Curar con la kanade ca\u00edda",
				"La kanade ca\u00edda cura a los jugadores y a los animales domesticados que est\u00e1n cerca",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada curaci\u00f3n cuesta 120 de man\u00e1",
				"Quien est\u00e9 en rango sin regeneraci\u00f3n recibe regeneraci\u00f3n III durante 3 segundos, que se renueva en cuanto termina",
				"Su alcance es de solo 2 bloques, un \u00e1rea de 5x5x5. Funciona con jugadores, lobos, gatos y loros domesticados y caballos domados. Los animales salvajes y los monstruos se ignoran"
		});
		add("heisei_dream", new String[] {
				"Turning Monsters against Each Other",
				"The Heisei Dream makes monsters fight each other",
				"It draws Mana from a Mana Pool within 10 blocks. Turning one monster against another costs 100 Mana",
				"When at least two monsters are within 5 blocks, it picks one that is not already fighting a monster and makes it attack another one",
				"Its area is 11x11x11 blocks. It only affects hostile mobs and cannot be turned off with Redstone"
		}, new String[] {
				"Enfrentar a los monstruos entre s\u00ed",
				"El sue\u00f1o de Heisei hace que los monstruos luchen entre ellos",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Volver a un monstruo contra otro cuesta 100 de man\u00e1",
				"Cuando hay al menos dos monstruos a menos de 5 bloques, elige uno que no est\u00e9 ya luchando contra un monstruo y hace que ataque a otro",
				"Su \u00e1rea es de 11x11x11 bloques. Solo afecta a mobs hostiles y no se puede apagar con redstone"
		});
		add("hopperhock", new String[] {
				"Collecting Items with the Hopperhock",
				"The Hopperhock picks up items lying around it and puts them into an inventory next to it",
				"Place a chest or any other inventory touching the flower on any side. Items within 6 blocks are moved into it",
				"It works without Mana, but with Mana from a nearby Mana Pool its range grows to 10 blocks. It only spends 1 Mana on the ticks when it moves items",
				"An Item Frame on an inventory works as a filter. Sneak and right-click the flower with the Wand of the Forest to switch between accepting only the framed items, rejecting them, or ignoring the frames",
				"The Hopperhock Petite reaches 1 block, or 2 with Mana. A Redstone signal turns either version off"
		}, new String[] {
				"Recoger objetos con la tolvalcea",
				"La tolvalcea recoge los objetos que hay por el suelo a su alrededor y los mete en un inventario junto a ella",
				"Coloca un cofre u otro inventario tocando la flor por cualquier lado. Los objetos a menos de 6 bloques se meten en \u00e9l",
				"Funciona sin man\u00e1, pero con man\u00e1 de una pila cercana su alcance crece a 10 bloques. Solo gasta 1 de man\u00e1 en los ticks en los que mueve objetos",
				"Un marco en un inventario funciona como filtro. Ag\u00e1chate y haz clic derecho en la flor con la varita del bosque para alternar entre aceptar solo los objetos de los marcos, rechazarlos o ignorar los marcos",
				"La tolvalceita alcanza 1 bloque, o 2 con man\u00e1. Una se\u00f1al de redstone apaga ambas versiones"
		});
		add("hyacidus", new String[] {
				"Poisoning Mobs with the Hyacidus",
				"The Hyacidus poisons the creatures around it. Players are never affected",
				"It draws Mana from a Mana Pool within 10 blocks. Every mob it poisons costs 20 Mana",
				"Mobs in range get Poison for 3 seconds, renewed as soon as it wears off. Poison never kills: it stops at half a heart",
				"Its range is 6 blocks in every direction. Undead mobs and spiders are immune to poison. A Redstone signal turns the flower off"
		}, new String[] {
				"Envenenar criaturas con el hi\u00e1cido",
				"El hi\u00e1cido envenena a las criaturas a su alrededor. Nunca afecta a los jugadores",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada mob envenenado cuesta 20 de man\u00e1",
				"Los mobs en rango reciben veneno durante 3 segundos, que se renueva en cuanto se acaba. El veneno nunca mata: se detiene en medio coraz\u00f3n",
				"Su alcance es de 6 bloques en todas direcciones. Los no muertos y las ara\u00f1as son inmunes al veneno. Una se\u00f1al de redstone apaga la flor"
		});
		add("jaded_amaranthus", new String[] {
				"Planting Flowers with the Jaded Amaranthus",
				"The Jaded Amaranthus plants Mystical Flowers around it",
				"It draws Mana from a Mana Pool within 10 blocks. Every flower costs 100 Mana",
				"Every 1.5 seconds it picks a random spot within 4 blocks and plants a flower of a random colour on the highest block there",
				"It covers a 9x9 area. The flowers need ground they can grow on, like grass or dirt. A Redstone signal turns it off"
		}, new String[] {
				"Plantar flores con el amaranto harto",
				"El amaranto harto planta flores m\u00edsticas a su alrededor",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada flor cuesta 100 de man\u00e1",
				"Cada 1,5 segundos elige un punto al azar a menos de 4 bloques y planta una flor de un color al azar sobre el bloque m\u00e1s alto de ese punto",
				"Cubre un \u00e1rea de 9x9. Las flores necesitan un suelo en el que puedan crecer, como hierba o tierra. Una se\u00f1al de redstone lo apaga"
		});
		add("jiyuulia", new String[] {
				"Pushing Mobs away with the Jiyuulia",
				"The Jiyuulia pushes mobs away, keeping an area clear",
				"It draws Mana from a Mana Pool within 10 blocks and burns 1 Mana every 4 ticks while it has any, about 5 per second",
				"Every mob within 8 blocks is pushed away from the flower, twice as hard as a Tangleberrie pulls",
				"Players and bosses are not affected. The Jiyuulia Petite only reaches 3 blocks"
		}, new String[] {
				"Apartar criaturas con la jiyuulia",
				"La jiyuulia aleja a los mobs y mantiene un \u00e1rea despejada",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques y gasta 1 de man\u00e1 cada 4 ticks mientras le quede, unos 5 por segundo",
				"Todos los mobs a menos de 8 bloques son empujados lejos de la flor, el doble de fuerte de lo que tira una enredabaya",
				"No afecta a jugadores ni a jefes. La jiyuulita solo alcanza 3 bloques"
		});
		add("labellia", new String[] {
				"Naming with the Labellia",
				"The Labellia names mobs and items using Name Tags",
				"Rename a Name Tag in an Anvil and throw it right onto the flower",
				"Every mob and every dropped item within 2 blocks, at the flower's height, gets that name, and the Name Tag is used up. Named mobs never despawn",
				"It draws Mana from a Mana Pool within 10 blocks. Each Name Tag costs 500 Mana. A Redstone signal turns it off"
		}, new String[] {
				"Poner nombres con la labellia",
				"La labellia pone nombre a mobs y objetos usando etiquetas",
				"Renombra una etiqueta en un yunque y l\u00e1nzala justo encima de la flor",
				"Todos los mobs y objetos del suelo a menos de 2 bloques, a la altura de la flor, reciben ese nombre, y la etiqueta se gasta. Los mobs con nombre nunca desaparecen",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada etiqueta cuesta 500 de man\u00e1. Una se\u00f1al de redstone la apaga"
		});
		add("loonium", new String[] {
				"Summoning Loot with the Loonium",
				"The Loonium conjures treasure from dungeon loot",
				"It needs a lot of Mana: 35000 for each use. Bind it to a Mana Pool within 10 blocks that is kept well filled",
				"By default it works every 10 seconds and drops one random item from dungeon loot within 3 blocks of it. Cost, interval, amount and radius can be changed in the config",
				"Placed inside a structure, like a Stronghold or a Desert Pyramid, it uses that structure's loot instead",
				"The config can switch it to MOBS mode. Then every 5 seconds it summons a monster within 5 blocks that carries the loot and drops it when killed",
				"Those monsters get Regeneration, Fire Resistance, Resistance and Strength. Nothing spawns on Peaceful or while 10 mobs are within 9 blocks. A Redstone signal turns the flower off"
		}, new String[] {
				"Invocar bot\u00edn con la botinia",
				"La botinia conjura tesoros del bot\u00edn de las mazmorras",
				"Necesita mucho man\u00e1: 35000 por cada uso. Vinc\u00falala a una pila de man\u00e1 a menos de 10 bloques que se mantenga bien llena",
				"Por defecto act\u00faa cada 10 segundos y suelta un objeto al azar del bot\u00edn de mazmorra a menos de 3 bloques de ella. El coste, el intervalo, la cantidad y el radio se pueden cambiar en la configuraci\u00f3n",
				"Si se coloca dentro de una estructura, como una fortaleza o una pir\u00e1mide del desierto, usa el bot\u00edn de esa estructura",
				"La configuraci\u00f3n puede cambiarla al modo MOBS. Entonces cada 5 segundos invoca un monstruo a menos de 5 bloques que lleva el bot\u00edn y lo suelta al morir",
				"Esos monstruos reciben regeneraci\u00f3n, resistencia al fuego, resistencia y fuerza. No aparece nada en pac\u00edfico ni mientras haya 10 mobs a menos de 9 bloques. Una se\u00f1al de redstone apaga la flor"
		});
		add("manastar", new String[] {
				"Watching Mana with the Manastar",
				"The Manastar shows whether the Mana Pools next to it are filling up or draining. It does not use any Mana",
				"Place it right beside one or more Mana Pools. Only Pools touching its four sides count",
				"While the Pools gain Mana, it gives off blue sparks",
				"While they lose Mana the sparks turn red. It compares the total every 3 seconds"
		}, new String[] {
				"Vigilar el man\u00e1 con la manastrella",
				"La manastrella muestra si las pilas de man\u00e1 junto a ella se est\u00e1n llenando o vaciando. No usa man\u00e1",
				"Col\u00f3cala justo al lado de una o varias pilas de man\u00e1. Solo cuentan las pilas que tocan sus cuatro lados",
				"Mientras las pilas ganan man\u00e1, suelta chispas azules",
				"Mientras pierden man\u00e1, las chispas se vuelven rojas. Compara el total cada 3 segundos"
		});
		add("marimorphosis", new String[] {
				"Metamorphic Stone with the Marimorphosis",
				"The Marimorphosis turns Stone into Metamorphic Stone",
				"It draws Mana from a Mana Pool within 10 blocks. With the default settings each block costs 25 Mana",
				"Every half second it converts a random Stone block in its area. The kind of stone depends on the biome: a forest mostly gives Forest Stone, a desert Desert Stone",
				"Its area is 9x9 blocks, 2 blocks up and down. The Marimorphosis Petite covers 5x5, 1 block up and down. Cost, speed and area can be changed in the config, and a Redstone signal turns it off"
		}, new String[] {
				"Piedra metam\u00f3rfica con la marimorfosis",
				"La marimorfosis convierte la piedra en piedra metam\u00f3rfica",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Con la configuraci\u00f3n por defecto cada bloque cuesta 25 de man\u00e1",
				"Cada medio segundo convierte un bloque de piedra al azar de su \u00e1rea. El tipo de piedra depende del bioma: en un bosque sale sobre todo piedra de bosque, en un desierto piedra de desierto",
				"Su \u00e1rea es de 9x9 bloques, 2 bloques arriba y abajo. La marimorfosita cubre 5x5, 1 bloque arriba y abajo. El coste, la velocidad y el \u00e1rea se pueden cambiar en la configuraci\u00f3n, y una se\u00f1al de redstone la apaga"
		});
		add("medumone", new String[] {
				"Freezing Mobs with the Medumone",
				"The Medumone freezes the creatures around it in place. Players can still move freely",
				"Without Mana it does nothing. It draws Mana from a Mana Pool within 10 blocks and spends 1 Mana every tick for each mob it holds, so keep the Pool well supplied",
				"Every mob within 6 blocks gets an extreme Slowness that stops it completely, as long as the flower has Mana",
				"A Redstone signal turns it off, and the mobs can walk again"
		}, new String[] {
				"Congelar criaturas con la medumona",
				"La medumona deja inm\u00f3viles a las criaturas a su alrededor. Los jugadores pueden moverse con libertad",
				"Sin man\u00e1 no hace nada. Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques y gasta 1 de man\u00e1 cada tick por cada mob que retiene, as\u00ed que mant\u00e9n la pila bien abastecida",
				"Todos los mobs a menos de 6 bloques reciben una lentitud extrema que los detiene por completo, mientras la flor tenga man\u00e1",
				"Una se\u00f1al de redstone la apaga y los mobs pueden volver a caminar"
		});
		add("orechid", new String[] {
				"Making Ores with the Orechid",
				"The Orechid turns Stone into ores",
				"It draws Mana from a Mana Pool within 10 blocks. With the default settings every ore costs 100 Mana",
				"Every 5 seconds it picks a random Stone block in its area and turns it into a random ore. Common ores like Coal and Iron appear most often",
				"Deepslate turns into the deepslate versions of the ores. Its area is 7x7 blocks, 1 block up and down",
				"Cost and area can be changed in the config. In Garden of Glass it works much faster. A Redstone signal turns it off"
		}, new String[] {
				"Crear menas con la menarqu\u00eddea",
				"La menarqu\u00eddea convierte la piedra en menas",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Con la configuraci\u00f3n por defecto cada mena cuesta 100 de man\u00e1",
				"Cada 5 segundos elige un bloque de piedra al azar de su \u00e1rea y lo convierte en una mena al azar. Las menas comunes, como el carb\u00f3n y el hierro, salen m\u00e1s a menudo",
				"La pizarra profunda se convierte en las versiones de pizarra profunda de las menas. Su \u00e1rea es de 7x7 bloques, 1 bloque arriba y abajo",
				"El coste y el \u00e1rea se pueden cambiar en la configuraci\u00f3n. En Garden of Glass trabaja mucho m\u00e1s r\u00e1pido. Una se\u00f1al de redstone la apaga"
		});
		add("orechid_ignem", new String[] {
				"Nether Ores with the Orechid Ignem",
				"The Orechid Ignem turns Netherrack into Nether ores",
				"It draws Mana from a Mana Pool within 10 blocks. With the default settings every ore costs 120 Mana",
				"Every 5 seconds it turns a random Netherrack block in its area into Nether Quartz Ore, Nether Gold Ore or, rarely, Ancient Debris",
				"Its area is 7x7 blocks, 1 block up and down. By default it works in any dimension, but the config can limit it to dimensions with a ceiling, like the Nether. A Redstone signal turns it off"
		}, new String[] {
				"Menas del Nether con la menarqu\u00eddea \u00edgnea",
				"La menarqu\u00eddea \u00edgnea convierte la piedra del Nether en menas del Nether",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Con la configuraci\u00f3n por defecto cada mena cuesta 120 de man\u00e1",
				"Cada 5 segundos convierte un bloque de piedra del Nether al azar de su \u00e1rea en mena de cuarzo del Nether, mena de oro del Nether o, rara vez, escombros ancestrales",
				"Su \u00e1rea es de 7x7 bloques, 1 bloque arriba y abajo. Por defecto funciona en cualquier dimensi\u00f3n, pero la configuraci\u00f3n puede limitarla a dimensiones con techo, como el Nether. Una se\u00f1al de redstone la apaga"
		});
		add("pollidisiac", new String[] {
				"Breeding Animals with the Pollidisiac",
				"The Pollidisiac feeds animals with the food dropped near it",
				"It draws Mana from a Mana Pool within 10 blocks. Each feeding costs 12 Mana",
				"Drop the food an animal likes, like Wheat for Cows, within 6 blocks. Adults that can breed fall in love and pair up to make babies",
				"Sneak and right-click it with the Wand of the Forest to choose who gets fed: adults only, babies only, or both. Fed babies grow up faster",
				"It can also feed small flowers to Brown Mooshrooms, which then give Suspicious Stew"
		}, new String[] {
				"Criar animales con el polidis\u00edaco",
				"El polidis\u00edaco alimenta a los animales con la comida que cae cerca de \u00e9l",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada vez que alimenta cuesta 12 de man\u00e1",
				"Suelta la comida que le gusta a cada animal, como trigo para las vacas, a menos de 6 bloques. Los adultos que pueden criar entran en celo y se emparejan para tener cr\u00edas",
				"Ag\u00e1chate y haz clic derecho con la varita del bosque para elegir a qui\u00e9n alimenta: solo adultos, solo cr\u00edas o ambos. Las cr\u00edas alimentadas crecen m\u00e1s r\u00e1pido",
				"Tambi\u00e9n puede dar flores peque\u00f1as a las champi\u00f1acas marrones, que luego dan estofado sospechoso"
		});
		add("rannuncarpus", new String[] {
				"Placing Blocks with the Rannuncarpus",
				"The Rannuncarpus picks up dropped blocks and places them in the world for you",
				"It looks at the block two below it, under the block it is planted on, and only places on top of blocks of that same type. Here that is Sandstone",
				"Every half second it takes a block item within 2 blocks of it and places it on a free matching spot up to 6 blocks away and 6 up or down",
				"Mana is optional: with Mana from a nearby Mana Pool the range grows to 8 blocks, for 1 Mana per block placed. The Rannuncarpus Petite places within 2 blocks, or 3 with Mana",
				"Sneak and right-click it with the Wand of the Forest to also match the exact block state. An Item Frame on the block below acts as an item filter. A Redstone signal turns it off"
		}, new String[] {
				"Colocar bloques con el ranuncarpo",
				"El ranuncarpo recoge bloques del suelo y los coloca en el mundo por ti",
				"Se fija en el bloque que hay dos por debajo, bajo el bloque en el que est\u00e1 plantado, y solo coloca encima de bloques de ese mismo tipo. Aqu\u00ed es arenisca",
				"Cada medio segundo toma un objeto de bloque a menos de 2 bloques y lo coloca en un hueco v\u00e1lido a hasta 6 bloques de distancia y 6 arriba o abajo",
				"El man\u00e1 es opcional: con man\u00e1 de una pila cercana el alcance crece a 8 bloques, por 1 de man\u00e1 por bloque colocado. El ranuncarpito coloca a 2 bloques, o 3 con man\u00e1",
				"Ag\u00e1chate y haz clic derecho con la varita del bosque para que tambi\u00e9n compare el estado exacto del bloque. Un marco en el bloque de abajo funciona como filtro de objetos. Una se\u00f1al de redstone lo apaga"
		});
		add("solegnolia", new String[] {
				"Blocking Magnets with the Solegnolia",
				"The Solegnolia stops magnets from working near it. It does not need any Mana",
				"Items within 5 blocks of it cannot be pulled by a Ring of Magnetization or other magnets",
				"A player standing inside its range does not attract items either. Place it next to farms and machines that must keep their items",
				"The Solegnolia Petite only protects 1 block around it. A Redstone signal turns either version off"
		}, new String[] {
				"Bloquear imanes con la solegnolia",
				"La solegnolia impide que los imanes funcionen cerca de ella. No necesita man\u00e1",
				"Los objetos a menos de 5 bloques no pueden ser atra\u00eddos por un anillo de magnetizaci\u00f3n ni por otros imanes",
				"Un jugador dentro de su rango tampoco atrae objetos. Col\u00f3cala junto a granjas y m\u00e1quinas que deban conservar sus objetos",
				"La solegnolita solo protege 1 bloque a su alrededor. Una se\u00f1al de redstone apaga ambas versiones"
		});
		add("spectranthemum", new String[] {
				"Teleporting Items with the Spectranthemum",
				"The Spectranthemum teleports items dropped near it to another place",
				"Bind it with the Wand of the Forest: right-click the flower, then the block the items should go to. Sneak while holding the wand to see the target",
				"Items within 2 blocks of the flower appear on top of the target block",
				"It draws Mana from a Mana Pool within 10 blocks. Each teleport costs 2 Mana per item for every block of distance: 64 items sent 10 blocks cost 1280 Mana. It holds up to 5000",
				"Items that store Mana, like Mana Tablets, are never teleported. A Redstone signal turns it off"
		}, new String[] {
				"Teletransportar objetos con el espectrantemo",
				"El espectrantemo teletransporta a otro lugar los objetos que caen cerca de \u00e9l",
				"Vinc\u00falalo con la varita del bosque: haz clic derecho en la flor y luego en el bloque al que deben ir los objetos. Ag\u00e1chate con la varita en la mano para ver el destino",
				"Los objetos a menos de 2 bloques de la flor aparecen encima del bloque de destino",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada teletransporte cuesta 2 de man\u00e1 por objeto y por bloque de distancia: 64 objetos enviados a 10 bloques cuestan 1280 de man\u00e1. Guarda hasta 5000",
				"Los objetos que almacenan man\u00e1, como las tabletas de man\u00e1, nunca se teletransportan. Una se\u00f1al de redstone lo apaga"
		});
		add("tangleberrie", new String[] {
				"Holding Mobs with the Tangleberrie",
				"The Tangleberrie keeps mobs from wandering away",
				"It draws Mana from a Mana Pool within 10 blocks and burns 1 Mana every 4 ticks while it has any, about 5 per second",
				"Any mob that gets between 6 and 7 blocks away is pulled back toward the flower, so it stays inside a circle of 6 blocks",
				"Players and bosses are not affected. The Tangleberrie Petite keeps mobs within 2 blocks, catching them up to 3 blocks away"
		}, new String[] {
				"Retener criaturas con la enredabaya",
				"La enredabaya impide que los mobs se alejen",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques y gasta 1 de man\u00e1 cada 4 ticks mientras le quede, unos 5 por segundo",
				"Cualquier mob que se aleje entre 6 y 7 bloques es arrastrado de vuelta hacia la flor, as\u00ed que se queda dentro de un c\u00edrculo de 6 bloques",
				"No afecta a jugadores ni a jefes. La enredabayita retiene a los mobs a 2 bloques, atrap\u00e1ndolos hasta a 3 bloques"
		});
		add("tigerseye", new String[] {
				"Taming Creepers with the Tigerseye",
				"The Tigerseye stops Creepers from exploding",
				"A Creeper within 10 blocks horizontally and 4 vertically has its fuse reset every tick, so it can never explode near the flower. This works even without Mana",
				"With Mana from a nearby Mana Pool it also pacifies each Creeper for good, for 70 Mana: the Creeper stops hunting players and runs away from them, even after leaving the area",
				"Its area is 21x21 blocks and 9 blocks tall, enough to protect a whole base"
		}, new String[] {
				"Calmar creepers con el tigr\u00f3culo",
				"El tigr\u00f3culo impide que los creepers exploten",
				"Un creeper a menos de 10 bloques en horizontal y 4 en vertical ve su mecha reiniciada cada tick, as\u00ed que nunca puede explotar cerca de la flor. Esto funciona incluso sin man\u00e1",
				"Con man\u00e1 de una pila cercana tambi\u00e9n calma a cada creeper para siempre, por 70 de man\u00e1: deja de perseguir a los jugadores y huye de ellos, incluso fuera del \u00e1rea",
				"Su \u00e1rea es de 21x21 bloques y 9 de alto, suficiente para proteger una base entera"
		});
		add("vinculotus", new String[] {
				"Catching Endermen with the Vinculotus",
				"The Vinculotus catches teleporting Endermen",
				"It draws Mana from a Mana Pool within 10 blocks. Each captured teleport costs 50 Mana, and it only works while it holds more than 50",
				"When an Enderman teleports to a spot within 64 blocks of the flower, it lands right next to the flower instead",
				"With several Vinculotus around, a random one is chosen. Perfect for Enderman farms. A Redstone signal turns it off"
		}, new String[] {
				"Atrapar endermans con el vinculoto",
				"El vinculoto atrapa a los endermans que se teletransportan",
				"Obtiene man\u00e1 de una pila de man\u00e1 a menos de 10 bloques. Cada teletransporte atrapado cuesta 50 de man\u00e1, y solo funciona mientras guarde m\u00e1s de 50",
				"Cuando un enderman se teletransporta a un punto a menos de 64 bloques de la flor, aparece justo al lado de la flor",
				"Si hay varios vinculotos, se elige uno al azar. Perfecto para granjas de endermans. Una se\u00f1al de redstone lo apaga"
		});
	}
}
