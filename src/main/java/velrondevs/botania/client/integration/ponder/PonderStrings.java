package velrondevs.botania.client.integration.ponder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class PonderStrings {
	private PonderStrings() {}

	public static final String MANA_NETWORK = "mana_network";
	public static final String CRAFTING_DEVICES = "crafting_devices";
	public static final String ALFHEIM = "alfheim";
	public static final String FLOWERS = "flowers";
	public static final String CORPOREA = "corporea";

	private static final Map<String, List<String>> SCENES = new LinkedHashMap<>();
	private static final Map<String, List<String>> TAGS = new LinkedHashMap<>();

	static {
		SCENES.put("mana_spreader", List.of(
				"Moving Mana with Spreaders",
				"Generating flowers, like the Endoflame, make Mana. On their own they can only hand it to a nearby Spreader",
				"Bind the flower to the Mana Spreader with the Wand of the Forest: right-click the flower, then the Spreader",
				"The Spreader fires bursts of Mana in the direction it faces",
				"A burst delivers its Mana to the first receiver it hits, such as a Mana Pool",
				"Shift and right-click a Spreader with the Wand of the Forest to aim it. Make sure nothing blocks the way"));
		SCENES.put("mana_pool", List.of(
				"Storing Mana in Pools",
				"Mana Pools store up to one million units of Mana. Spreaders, Sparks and Mana Tablets can all fill them",
				"Throw items into a Pool holding enough Mana and some of them are transformed. Iron Ingots become Manasteel Ingots",
				"Blocks placed underneath, like the Alchemy and Conjuration Catalysts, unlock even more recipes"));
		SCENES.put("petal_apothecary", List.of(
				"Crafting with the Petal Apothecary",
				"The Petal Apothecary turns petals into Mystical Flowers and other magical items. Start by filling it with water",
				"Toss petals into the water. They must match a recipe, here four White Petals",
				"Add the catalyst, usually Wheat Seeds, to finish the recipe. The water is used up",
				"The Apothecary comes in many styles, but they all work the same way"));
		SCENES.put("pure_daisy", List.of(
				"Purifying with the Pure Daisy",
				"The Pure Daisy slowly transforms the eight blocks around it. It does not need any Mana",
				"Stone becomes Livingrock and logs become Livingwood. Check the recipe viewer for every conversion"));
		SCENES.put("runic_altar", List.of(
				"Crafting Runes at the Runic Altar",
				"The Runic Altar crafts Runes. It needs Mana, so place it near a Mana Pool and a Spreader",
				"Throw the ingredients onto the altar. They float around it until a recipe matches",
				"A Spreader aimed at the altar supplies the Mana. Every recipe needs a fixed amount",
				"Once the altar is full of Mana, right-click it with Livingrock to craft. The Livingrock is consumed"));
		SCENES.put("terra_plate", List.of(
				"Terrestrial Agglomeration",
				"The Terrestrial Agglomeration Plate sits on a base of Lapis Blocks and Livingrock",
				"Throw a Manasteel Ingot, a Mana Pearl and a Mana Diamond onto the plate",
				"It needs a huge amount of Mana, 500,000 units. Spreaders keep firing at it until it is full",
				"When the plate is full, the items fuse into a Terrasteel Ingot"));
		SCENES.put("mana_enchanter", List.of(
				"Enchanting with the Mana Enchanter",
				"The Mana Enchanter applies enchantments to items using Mana. It stands on a platform of Obsidian, surrounded by Mystical Flowers and six Mana Pylons",
				"Place the item to enchant on the Enchanter and throw Enchanted Books with the wanted enchantments next to it",
				"Right-click the Enchanter with the Wand of the Forest to begin. The Pylons start to glow",
				"Spreaders must deliver the Mana the enchantments cost",
				"When enough Mana is gathered, the enchantments are applied to the item"));
		SCENES.put("alfheim_portal", List.of(
				"Opening the Elven Gateway",
				"The Gateway Core is built into a frame of Livingwood and Glimmering Livingwood",
				"Two Natura Pylons, each on top of a Mana Pool with Mana in it, must stand within five blocks",
				"Right-click the Gateway Core with the Wand of the Forest to open it. Opening it drains Mana from the pools",
				"Throw items through the portal to trade with the elves. Two Manasteel Ingots become one Elementium Ingot"));
		SCENES.put("gaia_ritual", List.of(
				"The Gaia Guardian Ritual",
				"Place a Beacon on a 3x3 platform of Iron Blocks",
				"Then put a Gaia Pylon on each corner, four blocks away from the Beacon and one block higher",
				"Clear a large arena around it, about 25x25 blocks, free of other blocks",
				"Right-click the Beacon with a Gaia Spirit to summon the Gaia Guardian"));
		SCENES.put("lenses", List.of(
				"Changing Bursts with Lenses",
				"Lenses change how a Spreader's bursts behave. Right-click a Spreader with a lens to fit it into its lens slot",
				"The Velocity Lens makes bursts much faster, but they carry less Mana and lose it sooner",
				"The Potency Lens doubles the Mana in every burst, at the cost of speed. Good for big machines",
				"The Bounce Lens makes bursts rebound off solid blocks instead of stopping, so they can reach receivers around corners",
				"There are many more: Kindle sets fire, Flash makes light, Force pushes blocks, Magnetizing homes in on receivers, Gravity makes bursts fall. Check each lens in the recipe viewer"));
		SCENES.put("mana_prism", List.of(
				"Sending Bursts through a Mana Prism",
				"A Mana Prism never blocks a burst: bursts fly straight through it, so it can sit right in the path of a Spreader",
				"Right-click the Prism with a lens to put it inside. Every burst crossing it takes that lens's effect and turns white. Here a Velocity Lens speeds the burst up",
				"A Redstone signal switches the lens off, and bursts cross the Prism unchanged"));
		SCENES.put("mana_detector", List.of(
				"Detecting Bursts with the Mana Detector",
				"The Mana Detector reacts to every burst passing through it. The burst is not stopped and keeps flying to its target",
				"Each burst makes the Detector emit a short Redstone pulse, enough to light a lamp or trigger a circuit",
				"Use it to know when a Spreader is working, to count bursts, or to start a machine only when Mana is flowing"));
		SCENES.put("mana_sparks", List.of(
				"Moving Mana with Sparks",
				"Sparks move Mana without Spreaders. Right-click a Mana Pool, a Mana Enchanter or a Terrestrial Agglomeration Plate with a Spark to place one above it",
				"Put a Spark on the machine too. While the machine is crafting, it draws Mana from the Pools that have a Spark within twelve blocks",
				"Right-click a Spark with Dye to colour it. Sparks only link with Sparks of the same colour, so you can keep separate networks",
				"Right-click a Spark with the Wand of the Forest to see its network. Sneak-click to take the Spark back"));
		SCENES.put("spark_augments", List.of(
				"Spark Augments",
				"Spark Augments change what a Spark does. Right-click a Spark with an augment to apply it. The Wand of the Forest removes it again",
				"A Recessive Spark pushes the Mana of its Pool into the Pools of ordinary and Dispersive Sparks",
				"A Dominant Spark does the opposite: it pulls Mana from the Pools of ordinary Sparks into its own Pool",
				"A Dispersive Spark hands its Pool's Mana to Mana items carried by nearby players, such as Mana Tablets",
				"An Isolated Spark is left out of every transfer, so it never gives or takes Mana from the others"));
		SCENES.put("generating_flowers", List.of(
				"Generating Flowers",
				"Generating flowers make Mana by themselves. Each one needs something different to work, and none of them can store much Mana",
				"The Endoflame burns fuel such as Coal that lies on the ground around it",
				"Bind the flower to a Spreader with the Wand of the Forest. The Spreader collects the Mana and fires it at a Pool or any other receiver",
				"The Hydroangeas drinks the water around it, and rests for a while between drinks. Place water next to it",
				"Many others exist: the Thermalily burns lava, the Gourmaryllis eats food, the Entropinnyum feeds on explosions. Read each one in the Lexicon"));
		SCENES.put("functional_flowers", List.of(
				"Functional Flowers",
				"Functional flowers work for you. The Orechid turns stone into ore, the Rannuncarpus places blocks, the Bellethorn hurts nearby creatures, and many more",
				"Most of them need Mana to work. Aim a Spreader at the flower, or bind the flower to a Mana Pool, to keep it supplied",
				"Every flower only acts inside an area around it. Holding the Wand of the Forest while looking at a flower shows that area",
				"The Orechid slowly turns stone in its area into ore, and spends a lot of Mana every time",
				"Mini versions of some flowers work in a smaller area, and floating versions can hover wherever you want"));
		SCENES.put("botanical_brewery", List.of(
				"Brewing with the Botanical Brewery",
				"The Botanical Brewery makes potions with Mana. Start by putting an empty Managlass Vial on it",
				"Then add the ingredients, one by one. A brew of Speed needs Nether Wart, Sugar and Redstone. Each brew has its own recipe",
				"The Brewery needs Mana to finish, so aim a Spreader at it or place a Pool next to it",
				"When it has enough Mana, the Brewery fills the vial and pops it out. Drink it to get the effect",
				"Until the brew starts, right-click the Brewery with an empty hand to take the items back"));
		SCENES.put("catalysts", List.of(
				"Mana Pool Catalysts",
				"A Catalyst placed directly under a Mana Pool unlocks new Mana recipes. The Alchemy Catalyst transmutes items: here, Cobblestone becomes Sand",
				"The Conjuration Catalyst duplicates some items, like Coal, but costs far more Mana",
				"Each recipe only works with its own Catalyst. Check the recipe viewer to see them all"));
		SCENES.put("corporea_network", List.of(
				"Corporea Networks",
				"Corporea moves items instantly through a network of Corporea Sparks. Put a Corporea Spark above every chest or other inventory",
				"Every network needs one Master Corporea Spark. It cannot read its own inventory, so place it on a block without one, like a Corporea Block",
				"Sparks link with the ones within about eight blocks of them, and the network can be extended much further by chaining Sparks",
				"A Corporea Spark above a Corporea Index connects it to the network. Stand close to the Index and say the name of an item in chat, like iron ingot",
				"The Index asks the network for the item and it pops out next to the Index",
				"Corporea Sparks of different colours do not link, so several networks can share the same area"));
		SCENES.put("corporea_funnel", List.of(
				"Requesting Items with the Corporea Funnel",
				"The Corporea Funnel asks a Corporea network for an item whenever it receives a Redstone signal. Put a Corporea Spark above it",
				"Hang an Item Frame with the wanted item on the Funnel. Rotating the item sets the amount: 1, 2, 4, 8, 16, 32, 48 or 64",
				"The item goes into an inventory one or two blocks below the Funnel, or is dropped if there is none"));

		TAGS.put(MANA_NETWORK, List.of("Mana Network", "Moving and storing Mana between blocks"));
		TAGS.put(CRAFTING_DEVICES, List.of("Crafting Devices", "Machines that craft items with petals, Mana and a little patience"));
		TAGS.put(ALFHEIM, List.of("Alfheim", "Gateways to the elves and the guardians of nature"));
		TAGS.put(FLOWERS, List.of("Flowers", "Flowers that make Mana and flowers that work for you"));
		TAGS.put(CORPOREA, List.of("Corporea", "Moving items instantly through a network of Sparks"));
	}

	public static String title(String scene) {
		return SCENES.get(scene).get(0);
	}

	public static String body(String scene, int index) {
		List<String> texts = SCENES.get(scene);
		if (texts == null || index < 1 || index >= texts.size()) {
			throw new IllegalArgumentException("No Ponder text " + index + " for scene " + scene);
		}
		return texts.get(index);
	}

	public static String tagTitle(String tag) {
		return TAGS.get(tag).get(0);
	}

	public static String tagDescription(String tag) {
		return TAGS.get(tag).get(1);
	}
}
