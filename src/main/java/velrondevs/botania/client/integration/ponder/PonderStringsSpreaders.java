package velrondevs.botania.client.integration.ponder;

import java.util.List;
import java.util.Map;

final class PonderStringsSpreaders {
	private PonderStringsSpreaders() {}

	static void register(Map<String, List<String>> scenes) {
		scenes.put("spreader_mana", List.of(
				"The Mana Spreader",
				"The Mana Spreader is the basic Spreader. It stores 1,000 Mana and fires green bursts of 160 Mana",
				"A burst keeps all its Mana for its first 3 seconds, about 12 blocks, then loses 4 Mana per tick and vanishes after about 20 blocks",
				"It only fires a new burst once the last one has landed, so the closer the target, the faster the Mana flows",
				"A Redstone signal stops it. It also pulls Mana from a Pool touching it, unless that Pool is its target"));
		scenes.put("spreader_redstone", List.of(
				"The Pulse Mana Spreader",
				"The Pulse Mana Spreader has the stats of the Mana Spreader, 1,000 Mana stored and 160 per burst, but its bursts are red",
				"It never fires on its own: every Redstone pulse it receives fires exactly one burst",
				"It fires even with no receiver in sight, which makes it perfect for lenses that act on blocks, like the Bore Lens",
				"It keeps pulling Mana from a Pool touching it even when that Pool is the one it aims at"));
		scenes.put("spreader_elven", List.of(
				"The Elven Mana Spreader",
				"The Elven Mana Spreader is made of Dreamwood. Its pink bursts carry 240 Mana, 50% more than the Mana Spreader. It still stores 1,000 Mana",
				"Its bursts fly 25% faster and stay full for 4 seconds: about 20 blocks without loss and up to 35 blocks in total",
				"Bigger and faster bursts land more often, so over the same distance it moves almost twice as much Mana as a Mana Spreader",
				"Like every Spreader it takes lenses and is aimed with the Wand of the Forest. The next tier is the Elementium Spreader"));
		scenes.put("spreader_elementium", List.of(
				"The Elementium Spreader",
				"The Elementium Spreader is the tier after the Elven Mana Spreader. It stores 2,400 Mana, more than twice as much, and fires purple bursts of 320 Mana",
				"Its bursts fly 50% faster than a Mana Spreader's and stay full for about 28 blocks, then lose 8 Mana per tick and vanish after about 40 blocks",
				"Compared with the Elven Spreader, every burst carries a third more Mana and lands sooner",
				"Its larger buffer takes in more Mana from strong flowers before it is full, so less is wasted"));
		scenes.put("spreader_terrasteel", List.of(
				"The Terrasteel Spreader",
				"The Terrasteel Spreader comes right before the Gaia tier. It stores 4,000 Mana and fires teal bursts of 480 Mana",
				"Its bursts fly 75% faster than a Mana Spreader's and stay full for about 37 blocks, then lose 12 Mana per tick and vanish after about 50 blocks",
				"Compared with the Elementium Spreader, every burst carries 50% more Mana and flies faster again",
				"It suits long Mana lines and hungry machines like the Terrestrial Agglomeration Plate"));
		scenes.put("spreader_gaia", List.of(
				"The Gaia Mana Spreader",
				"The Gaia Mana Spreader is the top tier, above the Terrasteel Spreader. It stores 6,400 Mana and fires bursts of 640 Mana, four times as much as the Mana Spreader",
				"Its bursts fly twice as fast and stay full for 6 seconds, about 48 blocks, then lose 20 Mana per tick. Here it races a Terrasteel Spreader",
				"It is the best match for flowers that produce a lot of Mana at once, like the Wither Mana Rose or the Entropinnyum",
				"It only fires once it holds a full 640 Mana burst, so a weak flower makes it wait between shots"));
		scenes.put("spreader_alfsteel", List.of(
				"The AlfSteel Spreader",
				"The AlfSteel Spreader only exists when Masterful Botany is installed. It is a Gaia Mana Spreader reinforced with two Alfsteel Ingots and a Dragonstone, and it fires orange bursts",
				"With the default multiplier of 4, every burst carries 1,280 Mana, twice as much as a Gaia burst, and flies twice as fast",
				"Here it races a Gaia Mana Spreader over the same distance: twice the Mana per burst, landing twice as often, means four times the Mana flow",
				"Its buffer also grows to 25,600 Mana, four times the Gaia one, so several strong flowers can feed it without wasting Mana",
				"The multiplier can be changed in the Botania common config, in the Masterful Botany integration section, from 1 to 1000. Burst Mana, speed and buffer all scale with it"));
	}
}
