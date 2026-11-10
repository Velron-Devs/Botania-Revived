package velrondevs.botania.client.integration.ponder;

import java.util.List;
import java.util.Map;

final class PonderStringsGenerating {
	private PonderStringsGenerating() {}

	static void register(Map<String, List<String>> scenes) {
		scenes.put("flower_endoflame", List.of(
				"Burning Fuel with the Endoflame",
				"The Endoflame burns furnace fuel lying on the ground within 3 blocks of it, a 7x7x7 area",
				"Drop Coal, Charcoal, Logs or any other fuel. It picks up one item at a time and starts burning it",
				"It burns for half the furnace burn time and makes 1.5 Mana per tick. One Coal lasts 40 seconds and gives 1,200 Mana",
				"It only stores 300 Mana. Bind it to a Spreader with the Wand of the Forest so the Mana reaches a Pool",
				"New fuel waits until the current item burns out. Lava Buckets and fuels that leave a container behind are ignored"));
		scenes.put("flower_hydroangeas", List.of(
				"Drinking Water with the Hydroangeas",
				"The Hydroangeas drinks Water source blocks in the 8 blocks around it, on its own level",
				"Each drink keeps it working for 2 seconds and gives about 13 Mana. Rain makes it 50% faster",
				"A lone source block is drunk and disappears. Sources touching two or more other water blocks stay, so a 2x2 pool feeds it forever",
				"It only stores 150 Mana and makes very little, so bind it to a Spreader and plant several of them",
				"After one hour it withers and turns into a Dead Bush. Overgrowth Seeds do not speed it up"));
		scenes.put("flower_thermalily", List.of(
				"Burning Lava with the Thermalily",
				"The Thermalily drinks Lava source blocks in the 8 blocks around it, on its own level. The Lava is consumed",
				"One Lava block keeps it burning for 30 seconds at 45 Mana per tick: 27,000 Mana in total",
				"It only holds 750 Mana, so put its Spreader right next to the Pool: the Spreader must fire fast or the extra Mana is lost",
				"Then it has to cool down for a random time between 20 seconds and 5 minutes. Lava drunk while cooling is wasted and restarts the cooldown",
				"A Redstone Comparator reads how long the next cooldown will be, so you can feed it Lava at the right moment"));
		scenes.put("flower_entropinnyum", List.of(
				"Absorbing Explosions with the Entropinnyum",
				"The Entropinnyum feeds on TNT that explodes within 12 blocks of it. The explosion is absorbed and breaks nothing",
				"Each TNT fills it with 6,500 Mana at once, but it only absorbs TNT while it is completely empty",
				"Bind it to a Spreader and wait until it has passed on all its Mana before lighting the next TNT",
				"TNT exploding in water is ignored, and TNT duplicated with pistons and slime only gives 3 Mana and makes the flower angry"));
		scenes.put("flower_gourmaryllis", List.of(
				"Feeding the Gourmaryllis",
				"The Gourmaryllis eats food items dropped right next to it, in a 3x3x3 area",
				"Mana grows with the square of the food's hunger value: 70 x value x value. A Steak (8) gives 4,480 Mana",
				"It digests for half a second per hunger point, and the Mana arrives all at once with a burp",
				"Food that lands while it is still digesting is eaten for nothing! Feed it one item at a time",
				"Vary the menu: each different food in a row raises a bonus up to x1.8, while repeating the same food gives a half, a third and so on"));
		scenes.put("flower_kekimurus", List.of(
				"Eating Cake with the Kekimurus",
				"The Kekimurus eats Cakes placed within 5 blocks of it, in an 11x11x11 area",
				"Every 4 seconds it takes one slice for 1,800 Mana. A whole Cake has 7 slices: 12,600 Mana",
				"It only eats when it has room for a whole slice, so a bound Spreader must keep emptying it",
				"Place several Cakes around it: it finishes one before starting the next"));
		scenes.put("flower_munchdew", List.of(
				"Eating Leaves with the Munchdew",
				"The Munchdew eats Leaves within 8 blocks horizontally and up to 16 blocks above it",
				"Every 4 ticks it eats one leaf block that touches air, starting with those furthest from the logs. Each one gives 160 Mana",
				"Once it runs out of leaves it has to rest for 80 seconds before it eats again",
				"It holds 10,000 Mana. A tree farm keeps it busy, but bind it to a Spreader that can keep up"));
		scenes.put("flower_narslimmus", List.of(
				"Eating Slimes with the Narslimmus",
				"The Narslimmus eats Slimes that come within 2 blocks of it, a 5x5x5 area",
				"Bigger Slimes are worth more: 2,400 Mana for a small one, 4,800 for a medium one and 19,200 for a large one",
				"Only Slimes that spawned naturally inside a slime chunk count. Spawner, Spawn Egg and swamp Slimes outside slime chunks are ignored",
				"Bind it to a Spreader: a single large Slime fills it completely"));
		scenes.put("flower_rafflowsia", List.of(
				"Devouring Flowers with the Rafflowsia",
				"The Rafflowsia devours other Botania special flowers within 5 blocks of it, one every 2 seconds",
				"Each flower different from the last ones extends a streak. The first flowers give about 2,000 Mana, a long streak up to 638,554",
				"Eating the same flower again breaks the streak: it gives a half, then a third, and so on",
				"It can hold all that Mana, so bind it to a Spreader and keep a supply of varied flowers nearby"));
		scenes.put("flower_rosa_arcana", List.of(
				"Turning Experience into Mana with the Rosa Arcana",
				"The Rosa Arcana turns experience into Mana. It works in the 3x3x3 area around it",
				"Experience orbs touching it are absorbed: 50 Mana for every experience point",
				"Enchanted items and books are disenchanted: it keeps the curses, drops the item and releases the experience of the enchantments",
				"A player standing next to it on the ground loses 1 experience point per tick, also worth 50 Mana each. It stores 6,000 Mana"));
		scenes.put("flower_shulk_me_not", List.of(
				"Generating Mana with the Shulk Me Not",
				"The Shulk Me Not feeds on Shulkers fighting other monsters within 8 blocks of it",
				"When a Shulker's target is a hostile mob under Levitation, both vanish in a burst of Mana",
				"Each pair gives 75,000 Mana at once, but only when the flower is completely empty",
				"Bind it to a fast Spreader: 75,000 Mana takes many bursts to empty before it can feed again"));
		scenes.put("flower_spectrolus", List.of(
				"Eating Wool with the Spectrolus",
				"The Spectrolus eats Wool in rainbow order: White, Orange, Magenta, Light Blue, Yellow and so on until Black",
				"Each Wool of the expected colour dropped next to it gives 1,200 Mana",
				"Wool of any other colour is destroyed and gives nothing",
				"A Sheep of the right colour standing on it gives 5,000 Mana, but every Sheep standing on it dies. Babies give just 1",
				"Look at it with the Wand of the Forest to see the next colour. It stores 16,000 Mana"));
		scenes.put("flower_dandelifeon", List.of(
				"Playing the Game of Life with the Dandelifeon",
				"The Dandelifeon runs Conway's Game of Life with Cellular Blocks in a 25x25 area on its level, but only while it gets a Redstone signal",
				"Every half second a generation passes: a cell with 2 or 3 neighbours survives, and an empty spot with exactly 3 grows a new cell",
				"Cells that reach the 3x3 area around the flower are eaten: 60 Mana for every generation they lived, up to 6,000 per cell",
				"Eating cells wipes out every other cell in the area, so design patterns that live long before they arrive",
				"It stores 50,000 Mana. Cellular Blocks are placed by hand and new cells only grow on air"));
		scenes.put("flower_wither_mana_rose", List.of(
				"Burning Nether Stars with the Wither Mana Rose",
				"The Wither Mana Rose consumes Nether Stars dropped right next to it",
				"Each Nether Star is worth 200,000 Mana, released at 850 Mana per tick: about 12 seconds of nonstop output",
				"Nothing is wasted: when its 8,500 Mana buffer is full it just waits. A Gaia Spreader empties it much faster",
				"Once a star is used up, it rests for 80 seconds before taking another one"));
	}
}
