package velrondevs.botania.client.integration.ponder;

import java.util.List;
import java.util.Map;

final class PonderStringsLenses {
	private PonderStringsLenses() {}

	static void register(Map<String, List<String>> scenes) {
		scenes.put("lens_normal", List.of(
				"The Mana Lens",
				"The Mana Lens does nothing by itself. It is the base every other lens is crafted from. Right-click a Spreader with a lens to fit it",
				"Craft any lens with a Dye to colour its bursts, or with a Mana Pearl for rainbow bursts",
				"Two different lenses crafted together with a Slimeball make a Composite Lens with both effects, as long as their effects do not clash"));
		scenes.put("lens_speed", List.of(
				"The Velocity Lens",
				"The Velocity Lens makes bursts fly twice as fast, so they land, and the Spreader fires again, much sooner",
				"In exchange, bursts carry 25% less Mana, stay full only a third as long and then lose Mana twice as fast"));
		scenes.put("lens_power", List.of(
				"The Potency Lens",
				"The Potency Lens doubles the Mana in every burst: 320 from a Mana Spreader. Great for machines that need big bursts",
				"Bursts are 15% slower and lose Mana twice as fast once they start fading, and the Spreader must hold a full double burst before firing"));
		scenes.put("lens_time", List.of(
				"The Resistance Lens",
				"The Resistance Lens keeps bursts at full Mana 2.25 times longer before they start fading",
				"Bursts fly 20% slower, but still reach much further without losing anything. Use it for long Mana lines"));
		scenes.put("lens_efficiency", List.of(
				"The Efficiency Lens",
				"With the Efficiency Lens, bursts stay full 10% longer and, once they start fading, lose Mana five times more slowly",
				"Targets at the edge of a Spreader's range receive nearly the whole burst instead of a few scraps"));
		scenes.put("lens_bounce", List.of(
				"The Bounce Lens",
				"The Bounce Lens makes bursts bounce off solid blocks like a ball instead of stopping",
				"Use it to reach receivers around corners. A burst keeps bouncing until it hits a receiver or runs out of Mana"));
		scenes.put("lens_gravity", List.of(
				"The Gravity Lens",
				"The Gravity Lens pulls bursts down so they fly in an arc. They also stay full 20% longer",
				"Aim the Spreader upwards with the Wand of the Forest to lob Mana over walls and other obstacles"));
		scenes.put("lens_mine", List.of(
				"The Bore Lens",
				"Bursts with a Bore Lens break the blocks they hit, just like mining them, and keep flying",
				"Every block broken costs the burst 24 Mana, and the drops fall where the block was",
				"It mines up to diamond level and never touches unbreakable blocks or Mana receivers. A Pulse Spreader gives precise control"));
		scenes.put("lens_damage", List.of(
				"The Damaging Lens",
				"Bursts with a Damaging Lens hurt every creature they fly through for 8 damage, four hearts",
				"Each hit costs the burst 16 Mana, and players are never harmed. Perfect for a mob grinder"));
		scenes.put("lens_phantom", List.of(
				"The Phantom Lens",
				"The Phantom Lens lets bursts pass straight through solid blocks. Only Mana receivers catch them",
				"Each block crossed makes the burst start losing Mana a little sooner, so keep walls thin"));
		scenes.put("lens_magnet", List.of(
				"The Magnetizing Lens",
				"Bursts with a Magnetizing Lens bend towards any Mana receiver within 3 blocks of their path that can still take Mana",
				"Receivers within 3 blocks of the Spreader are ignored, and full receivers stop attracting bursts"));
		scenes.put("lens_explosive", List.of(
				"The Entropic Lens",
				"Bursts with an Entropic Lens explode when they hit a block, breaking the blocks around them",
				"The power is the Mana left in the burst divided by 50: a Mana Spreader burst is almost as strong as TNT. They never explode on Mana receivers"));
		scenes.put("lens_influence", List.of(
				"The Influence Lens",
				"Bursts with an Influence Lens drag along items, experience orbs, arrows, falling blocks, TNT and other bursts within 3.5 blocks",
				"Everything nearby takes on the burst's motion, making it a handy item mover"));
		scenes.put("lens_weight", List.of(
				"The Weight Lens",
				"The Weight Lens turns the block a burst hits into a falling block, like Sand, if there is room below it",
				"It only moves blocks that Silk Touch could pick up with an iron-level tool, and never blocks with a block entity"));
		scenes.put("lens_paint", List.of(
				"The Paintslinger Lens",
				"Dye the Paintslinger Lens first. Its bursts recolour paintable blocks like Wool, Stained Glass or Terracotta, plus up to 1,000 connected blocks of the same kind",
				"Hitting a Sheep recolours every Sheep of that colour within 20 blocks, and Sparks it hits change network colour"));
		scenes.put("lens_fire", List.of(
				"The Kindle Lens",
				"Bursts with a Kindle Lens are on fire and light a fire on the face of the block they hit",
				"Fired into a Nether Portal they put it out instead"));
		scenes.put("lens_piston", List.of(
				"The Force Lens",
				"Bursts with a Force Lens push the block they hit one block further, just like a Piston",
				"It follows piston rules: up to 12 blocks in a row, and blocks a Piston cannot move stay put"));
		scenes.put("lens_light", List.of(
				"The Flash Lens",
				"Bursts with a Flash Lens leave a Mana Flash on the face of the block they hit: a light source you can walk through",
				"The Flash takes the colour of the burst, and hitting an existing Flash removes it"));
		scenes.put("lens_warp", List.of(
				"The Warp Lens",
				"Bursts with a Warp Lens that hit a Force Relay jump to the spot the relay is bound to and keep flying from there",
				"Bind a Force Relay with the Wand of the Forest. Combined with a Bore Lens, the broken blocks drop right behind the Spreader"));
		scenes.put("lens_redirect", List.of(
				"The Redirective Lens",
				"When a burst with a Redirective Lens hits another Spreader, that Spreader turns to face the one that fired it",
				"Creatures it hits turn to look at the Spreader too. Mount the Spreader on a Spreader Turntable to aim a whole chain at once"));
		scenes.put("lens_firework", List.of(
				"The Celebratory Lens",
				"Bursts with a Celebratory Lens turn into a Firework Rocket when they hit a block",
				"The firework takes the colour of the burst: combine it with a dyed lens for a colourful show"));
		scenes.put("lens_flare", List.of(
				"The Flare Lens",
				"A Spreader with a Flare Lens never fires bursts. It sends out a constant stream of light particles where it is aimed",
				"Dye the lens to pick the colour, or use a Mana Pearl for a rainbow. It needs no Mana, and Redstone switches it off"));
		scenes.put("lens_messenger", List.of(
				"The Messenger Lens",
				"Bursts with a Messenger Lens fly three times as fast and stay full three times as long, but carry only a fifth of the Mana",
				"They are made for signals: send them through a Mana Detector to trigger Redstone far away"));
		scenes.put("lens_tripwire", List.of(
				"The Tripwire Lens",
				"A Spreader with a Tripwire Lens holds its fire until a creature crosses the path its burst would take",
				"It works like an invisible tripwire: pair it with a Mana Detector or a damaging lens"));
		scenes.put("lens_storm", List.of(
				"The Storm Lens",
				"Bursts with a Storm Lens cause a powerful explosion, stronger than TNT, wherever they hit a block",
				"The blast does not depend on the Mana in the burst and breaks everything around. Handle it with care"));
	}
}
