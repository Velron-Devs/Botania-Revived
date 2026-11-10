package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;

final class SpreaderScenes {
	static final String PLATE = "mana_plate_9";
	static final int RED = 0xFF2020;
	static final int PINK = 0xFF45C4;
	static final int PURPLE = 0xE040FF;
	static final int TEAL = 0x2EE8A0;
	static final int ORANGE = 0xFF9A2E;

	private SpreaderScenes() {}

	static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		add(helper, BotaniaBlocks.manaSpreader, SpreaderScenes::mana);
		add(helper, BotaniaBlocks.redstoneSpreader, SpreaderScenes::redstone);
		add(helper, BotaniaBlocks.elvenSpreader, SpreaderScenes::elven);
		add(helper, BotaniaBlocks.elementiumSpreader, SpreaderScenes::elementium);
		add(helper, BotaniaBlocks.terrasteelSpreader, SpreaderScenes::terrasteel);
		add(helper, BotaniaBlocks.gaiaSpreader, SpreaderScenes::gaia);
		if (BotaniaBlocks.alfsteelSpreader != null) {
			add(helper, BotaniaBlocks.alfsteelSpreader, SpreaderScenes::alfsteel);
		}
	}

	private static void add(PonderSceneRegistrationHelper<ResourceLocation> helper, Block block, PonderStoryBoard board) {
		helper.forComponents(List.of(BuiltInRegistries.BLOCK.getKey(block))).addStoryBoard(PLATE, board, BotaniaPonderTags.MANA_NETWORK);
	}

	private static PonderText start(SceneBuilder scene, String id) {
		scene.title(id, PonderStrings.title(id));
		SceneKit.plate(scene, 9);
		return new PonderText(id);
	}

	private static void spreader(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos, Block block, float rotation) {
		SceneKit.put(scene, pos, block);
		SceneKit.aim(scene, util, pos, rotation, 0F);
	}

	record Lane(Vec3 from, Vec3 to, int flight, int color, BlockPos pool, int perBurst, int startMana) {}

	static void race(SceneBuilder scene, SceneBuildingUtil util, int ticks, Lane... lanes) {
		int[] mana = new int[lanes.length];
		for (int i = 0; i < lanes.length; i++) {
			mana[i] = lanes[i].startMana();
		}
		for (int t = 0; t < ticks; t++) {
			for (int i = 0; i < lanes.length; i++) {
				Lane lane = lanes[i];
				int phase = t % (lane.flight() + 2);
				if (phase <= lane.flight()) {
					Vec3 at = lane.from().lerp(lane.to(), (double) phase / lane.flight());
					int color = lane.color();
					float r = (color >> 16 & 0xFF) / 255F;
					float g = (color >> 8 & 0xFF) / 255F;
					float b = (color & 0xFF) / 255F;
					scene.effects().emitParticles(at, (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.3F, r, g, b, 3F), x, y, z, 0, 0, 0), 2F, 1);
				}
				if (phase == lane.flight()) {
					mana[i] = Math.min(1000000, mana[i] + lane.perBurst());
					SceneKit.poolMana(scene, util, lane.pool(), mana[i]);
				}
			}
			scene.idle(1);
		}
	}

	static void mana(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "spreader_mana");
		BlockPos spreader = util.grid().at(1, 1, 4);
		BlockPos pool = util.grid().at(7, 1, 4);
		BlockPos near = util.grid().at(3, 1, 4);
		BlockPos back = util.grid().at(0, 1, 4);
		BlockPos redstone = util.grid().at(1, 1, 5);
		spreader(scene, util, spreader, BotaniaBlocks.manaSpreader, SceneKit.EAST);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, near, BotaniaBlocks.manaPool);
		SceneKit.put(scene, back, BotaniaBlocks.manaPool);
		SceneKit.poolMana(scene, util, back, 600000);
		SceneKit.stage(scene, util, 9, 3, spreader, pool, near, back);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		Vec3 toNear = util.vector().centerOf(near).add(-0.5, 0, 0);
		scene.idle(10);

		SceneKit.show(scene, util, spreader);
		scene.idle(10);
		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), false);
		SceneKit.spreaderMana(scene, util, spreader, 1000);
		scene.idle(30);
		SceneKit.show(scene, util, pool);
		scene.idle(15);
		SceneKit.deliver(scene, util, from, to, pool, 0, 32000, 1, 25, SceneHelper.MANA_COLOR);
		scene.idle(40);

		SceneKit.say(scene, text, 110, util.vector().centerOf(4, 1, 4).add(0, 0.6, 0), true);
		SceneKit.deliver(scene, util, from, to, pool, 32000, 96000, 2, 25, SceneHelper.MANA_COLOR);
		scene.idle(55);

		SceneKit.say(scene, text, 120, util.vector().topOf(near), true);
		scene.world().hideSection(util.select().position(pool), Direction.UP);
		scene.idle(10);
		SceneKit.show(scene, util, near);
		scene.idle(15);
		SceneKit.deliver(scene, util, from, toNear, near, 0, 192000, 6, 5, SceneHelper.MANA_COLOR);
		scene.idle(50);

		SceneKit.say(scene, text, 130, util.vector().topOf(spreader), true);
		SceneKit.change(scene, redstone, Blocks.REDSTONE_BLOCK.defaultBlockState());
		scene.effects().indicateRedstone(redstone);
		scene.idle(40);
		SceneKit.show(scene, util, back);
		scene.idle(20);
		SceneKit.change(scene, redstone, Blocks.AIR.defaultBlockState());
		scene.idle(10);
		for (int shot = 1; shot <= 5; shot++) {
			SceneHelper.burst(scene, from, toNear, 5, SceneHelper.MANA_COLOR);
			SceneKit.poolMana(scene, util, near, 192000 + shot * 32000);
			SceneKit.poolMana(scene, util, back, 600000 - shot * 60000);
			scene.idle(2);
		}
		scene.idle(50);
	}

	static void redstone(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "spreader_redstone");
		BlockPos spreader = util.grid().at(1, 1, 4);
		BlockPos pool = util.grid().at(7, 1, 4);
		BlockPos back = util.grid().at(0, 1, 4);
		BlockPos lever = util.grid().at(1, 1, 3);
		spreader(scene, util, spreader, BotaniaBlocks.redstoneSpreader, SceneKit.EAST);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, back, BotaniaBlocks.manaPool);
		SceneKit.poolMana(scene, util, back, 500000);
		SceneKit.stage(scene, util, 9, 3, spreader, pool, back);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		scene.idle(10);

		SceneKit.show(scene, util, spreader);
		scene.idle(10);
		SceneKit.show(scene, util, pool);
		scene.idle(10);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), false);
		SceneKit.spreaderMana(scene, util, spreader, 1000);
		scene.idle(120);

		SceneKit.say(scene, text, 110, util.vector().topOf(lever), true);
		for (int pulse = 1; pulse <= 3; pulse++) {
			pulse(scene, lever);
			SceneHelper.burst(scene, from, to, 25, RED);
			SceneKit.poolMana(scene, util, pool, pulse * 40000);
			scene.idle(20);
		}
		scene.idle(20);

		scene.world().hideSection(util.select().position(pool), Direction.UP);
		scene.idle(10);
		BlockPos[] stones = { util.grid().at(4, 1, 4), util.grid().at(5, 1, 4), util.grid().at(6, 1, 4) };
		for (BlockPos stone : stones) {
			SceneKit.change(scene, stone, Blocks.STONE.defaultBlockState());
			scene.idle(3);
		}
		SceneKit.say(scene, text, 130, util.vector().topOf(stones[0]), true);
		SceneKit.lens(scene, util, spreader, new ItemStack(BotaniaItems.lensMine));
		for (int i = 0; i < stones.length; i++) {
			pulse(scene, lever);
			SceneHelper.burst(scene, from, util.vector().centerOf(stones[i]).add(-0.5, 0, 0), 10 + i * 5, RED);
			scene.world().destroyBlock(stones[i]);
			SceneKit.drop(scene, util.vector().centerOf(stones[i]), new ItemStack(Items.COBBLESTONE));
			scene.idle(15);
		}
		scene.idle(20);

		SceneKit.show(scene, util, back);
		scene.idle(15);
		SceneKit.say(scene, text, 110, util.vector().topOf(back), true);
		scene.idle(10);
		for (int pulse = 1; pulse <= 2; pulse++) {
			pulse(scene, lever);
			SceneKit.poolMana(scene, util, back, 500000 - pulse * 80000);
			SceneKit.fadingBurst(scene, from, util.vector().centerOf(8, 1, 4).add(0.5, 0, 0), 32, RED, 0.7);
			scene.idle(15);
		}
		scene.idle(50);
	}

	private static void pulse(SceneBuilder scene, BlockPos lever) {
		SceneKit.change(scene, lever, Blocks.REDSTONE_BLOCK.defaultBlockState());
		scene.effects().indicateRedstone(lever);
		scene.idle(6);
		SceneKit.put(scene, lever, Blocks.AIR.defaultBlockState());
	}

	static void elven(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "spreader_elven");
		BlockPos spreader = util.grid().at(1, 1, 3);
		BlockPos pool = util.grid().at(7, 1, 3);
		BlockPos basic = util.grid().at(1, 1, 5);
		BlockPos basicPool = util.grid().at(7, 1, 5);
		spreader(scene, util, spreader, BotaniaBlocks.elvenSpreader, SceneKit.EAST);
		spreader(scene, util, basic, BotaniaBlocks.manaSpreader, SceneKit.EAST);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, basicPool, BotaniaBlocks.manaPool);
		SceneKit.stage(scene, util, 9, 3, spreader, pool, basic, basicPool);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		Vec3 fromBasic = util.vector().centerOf(basic).add(0.5, 0, 0);
		Vec3 toBasic = util.vector().centerOf(basicPool).add(-0.5, 0, 0);
		scene.idle(10);

		SceneKit.show(scene, util, spreader);
		scene.idle(10);
		SceneKit.show(scene, util, pool);
		scene.idle(10);
		SceneKit.say(scene, text, 120, util.vector().topOf(spreader), false);
		scene.idle(20);
		SceneKit.deliver(scene, util, from, to, pool, 0, 36000, 1, 20, PINK);
		scene.idle(80);

		SceneKit.say(scene, text, 110, util.vector().centerOf(4, 1, 3).add(0, 0.6, 0), true);
		SceneKit.deliver(scene, util, from, to, pool, 36000, 108000, 2, 20, PINK);
		scene.idle(65);

		SceneKit.show(scene, util, basic);
		scene.idle(5);
		SceneKit.show(scene, util, basicPool);
		scene.idle(10);
		SceneKit.say(scene, text, 140, util.vector().topOf(pool), true);
		race(scene, util, 170,
				new Lane(from, to, 20, PINK, pool, 36000, 108000),
				new Lane(fromBasic, toBasic, 25, SceneHelper.MANA_COLOR, basicPool, 24000, 0));
		scene.idle(20);

		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), true);
		SceneKit.lens(scene, util, spreader, new ItemStack(BotaniaItems.lensSpeed));
		SceneKit.deliver(scene, util, from, to, pool, 400000, 460000, 2, 10, PINK);
		scene.idle(60);
	}

	static void gaia(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "spreader_gaia");
		BlockPos spreader = util.grid().at(1, 1, 3);
		BlockPos pool = util.grid().at(7, 1, 3);
		BlockPos elven = util.grid().at(1, 1, 5);
		BlockPos elvenPool = util.grid().at(7, 1, 5);
		BlockPos flower = util.grid().at(1, 1, 1);
		spreader(scene, util, spreader, BotaniaBlocks.gaiaSpreader, SceneKit.EAST);
		spreader(scene, util, elven, BotaniaBlocks.terrasteelSpreader, SceneKit.EAST);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, elvenPool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.witherManaRose);
		SceneKit.stage(scene, util, 9, 3, spreader, pool, elven, elvenPool, flower);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		Vec3 fromElven = util.vector().centerOf(elven).add(0.5, 0, 0);
		Vec3 toElven = util.vector().centerOf(elvenPool).add(-0.5, 0, 0);
		scene.idle(10);

		SceneKit.show(scene, util, spreader);
		scene.idle(10);
		SceneKit.show(scene, util, pool);
		scene.idle(10);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), false);
		scene.idle(20);
		SceneKit.deliver(scene, util, from, to, pool, 0, 64000, 1, 12, SceneHelper.MANA_COLOR);
		scene.idle(80);

		SceneKit.show(scene, util, elven);
		scene.idle(5);
		SceneKit.show(scene, util, elvenPool);
		scene.idle(10);
		SceneKit.say(scene, text, 140, util.vector().centerOf(4, 1, 3).add(0, 0.6, 0), true);
		race(scene, util, 160,
				new Lane(from, to, 12, SceneHelper.MANA_COLOR, pool, 64000, 64000),
				new Lane(fromElven, toElven, 14, TEAL, elvenPool, 48000, 0));
		scene.idle(20);

		scene.world().hideSection(util.select().position(elven), Direction.UP);
		scene.world().hideSection(util.select().position(elvenPool), Direction.UP);
		scene.idle(15);
		SceneKit.show(scene, util, flower);
		scene.idle(10);
		SceneKit.say(scene, text, 120, util.vector().topOf(flower), true);
		SceneKit.bind(scene, util, flower, spreader);
		ElementLink<EntityElement> star = SceneKit.drop(scene, util.vector().topOf(0, 0, 1).add(0, 1.5, 0), new ItemStack(Items.NETHER_STAR));
		scene.idle(25);
		SceneKit.remove(scene, star);
		SceneKit.burst(scene, util.vector().topOf(0, 0, 1).add(0, 0.2, 0), ParticleTypes.SOUL, 10, 0.3, 0.08);
		SceneKit.deliver(scene, util, from, to, pool, 760000, 1000000, 4, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);

		SceneKit.change(scene, flower, BotaniaFlowerBlocks.hydroangeas.defaultBlockState());
		SceneKit.poolMana(scene, util, pool, 200000);
		scene.idle(10);
		SceneKit.say(scene, text, 130, util.vector().topOf(spreader), true);
		for (int charge = 1; charge <= 4; charge++) {
			SceneKit.wispAura(scene, util.vector().centerOf(flower), 0x532FE0, 0.6, 15);
			SceneKit.spreaderMana(scene, util, spreader, charge * 160);
			SceneHelper.sparkles(scene, util.vector().topOf(spreader), SceneHelper.MANA_COLOR, 3);
			scene.idle(18);
		}
		SceneKit.deliver(scene, util, from, to, pool, 200000, 264000, 1, 12, SceneHelper.MANA_COLOR);
		SceneKit.spreaderMana(scene, util, spreader, 0);
		scene.idle(50);
	}
	static void elementium(SceneBuilder scene, SceneBuildingUtil util) {
		tier(scene, util, "spreader_elementium", BotaniaBlocks.elementiumSpreader, PURPLE, 17, 32000, BotaniaBlocks.elvenSpreader, PINK, 20, 24000, false);
	}

	static void terrasteel(SceneBuilder scene, SceneBuildingUtil util) {
		tier(scene, util, "spreader_terrasteel", BotaniaBlocks.terrasteelSpreader, TEAL, 14, 48000, BotaniaBlocks.elementiumSpreader, PURPLE, 17, 32000, true);
	}

	private static void tier(SceneBuilder scene, SceneBuildingUtil util, String id, Block block, int color, int flight, int perBurst,
			Block previous, int previousColor, int previousFlight, int previousPerBurst, boolean plate) {
		PonderText text = start(scene, id);
		BlockPos spreader = util.grid().at(1, 1, 3);
		BlockPos pool = util.grid().at(7, 1, 3);
		BlockPos rival = util.grid().at(1, 1, 5);
		BlockPos rivalPool = util.grid().at(7, 1, 5);
		BlockPos flower = util.grid().at(1, 1, 1);
		BlockPos machine = util.grid().at(7, 1, 1);
		spreader(scene, util, spreader, block, SceneKit.EAST);
		spreader(scene, util, rival, previous, SceneKit.EAST);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, rivalPool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.thermalily);
		SceneKit.put(scene, machine, BotaniaBlocks.terraPlate);
		SceneKit.stage(scene, util, 9, 3, spreader, pool, rival, rivalPool, flower, machine);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		Vec3 fromRival = util.vector().centerOf(rival).add(0.5, 0, 0);
		Vec3 toRival = util.vector().centerOf(rivalPool).add(-0.5, 0, 0);
		scene.idle(10);

		SceneKit.show(scene, util, spreader);
		scene.idle(10);
		SceneKit.show(scene, util, pool);
		scene.idle(10);
		SceneKit.say(scene, text, 120, util.vector().topOf(spreader), false);
		scene.idle(20);
		SceneKit.deliver(scene, util, from, to, pool, 0, perBurst, 1, flight, color);
		scene.idle(85);

		SceneKit.say(scene, text, 120, util.vector().centerOf(4, 1, 3).add(0, 0.6, 0), true);
		SceneKit.fadingBurst(scene, from, util.vector().centerOf(8, 1, 3).add(0.5, 0, 0), flight + 4, color, 0.75);
		SceneKit.deliver(scene, util, from, to, pool, perBurst, perBurst * 3, 2, flight, color);
		scene.idle(70);

		SceneKit.show(scene, util, rival);
		scene.idle(5);
		SceneKit.show(scene, util, rivalPool);
		scene.idle(10);
		SceneKit.say(scene, text, 140, util.vector().topOf(pool), true);
		race(scene, util, 160,
				new Lane(from, to, flight, color, pool, perBurst, perBurst * 3),
				new Lane(fromRival, toRival, previousFlight, previousColor, rivalPool, previousPerBurst, 0));
		scene.idle(20);

		scene.world().hideSection(util.select().position(rival), Direction.UP);
		scene.world().hideSection(util.select().position(rivalPool), Direction.UP);
		scene.idle(15);
		BlockPos target = plate ? machine : pool;
		SceneKit.show(scene, util, plate ? machine : flower);
		scene.idle(10);
		SceneKit.say(scene, text, 130, util.vector().topOf(plate ? machine : flower), true);
		if (plate) {
			SceneKit.aim(scene, util, spreader, 198.4F, 0F);
			scene.idle(10);
			Vec3 plateAt = util.vector().centerOf(machine).add(-0.4, -0.3, 0.2);
			for (int shot = 0; shot < 4; shot++) {
				SceneHelper.burst(scene, util.vector().centerOf(spreader), plateAt, flight + 2, color);
				SceneHelper.sparkles(scene, util.vector().topOf(machine), color, 6);
				scene.idle(3);
			}
		} else {
			SceneKit.bind(scene, util, flower, spreader);
			SceneKit.wispAura(scene, util.vector().centerOf(flower).add(0, 0.3, 0), 0xD03C00, 0.7, 120);
			for (int charge = 1; charge <= 4; charge++) {
				SceneKit.spreaderMana(scene, util, spreader, charge * 600);
				SceneHelper.sparkles(scene, util.vector().topOf(spreader), color, 3);
				SceneKit.deliver(scene, util, from, to, target, perBurst * 3 + perBurst * charge * 2, perBurst * 3 + perBurst * charge * 2, 1, flight, color);
			}
		}
		scene.idle(50);
	}

	static void alfsteel(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "spreader_alfsteel");
		BlockPos spreader = util.grid().at(1, 1, 3);
		BlockPos pool = util.grid().at(7, 1, 3);
		BlockPos gaia = util.grid().at(1, 1, 5);
		BlockPos gaiaPool = util.grid().at(7, 1, 5);
		BlockPos[] flowers = { util.grid().at(0, 1, 1), util.grid().at(1, 1, 1), util.grid().at(2, 1, 1) };
		spreader(scene, util, spreader, BotaniaBlocks.alfsteelSpreader, SceneKit.EAST);
		spreader(scene, util, gaia, BotaniaBlocks.gaiaSpreader, SceneKit.EAST);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
		SceneKit.put(scene, gaiaPool, BotaniaBlocks.manaPool);
		for (BlockPos flower : flowers) {
			SceneKit.put(scene, flower, BotaniaFlowerBlocks.thermalily);
		}
		SceneKit.stage(scene, util, 9, 3, spreader, pool, gaia, gaiaPool, flowers[0], flowers[1], flowers[2]);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		Vec3 fromGaia = util.vector().centerOf(gaia).add(0.5, 0, 0);
		Vec3 toGaia = util.vector().centerOf(gaiaPool).add(-0.5, 0, 0);
		scene.idle(10);

		SceneKit.show(scene, util, spreader);
		scene.idle(10);
		SceneKit.show(scene, util, pool);
		scene.idle(10);
		SceneKit.say(scene, text, 130, util.vector().topOf(spreader), false);
		scene.idle(20);
		SceneKit.deliver(scene, util, from, to, pool, 0, 32000, 1, 5, ORANGE);
		scene.idle(100);

		SceneKit.say(scene, text, 120, util.vector().centerOf(4, 1, 3).add(0, 0.6, 0), true);
		SceneKit.deliver(scene, util, from, to, pool, 32000, 96000, 2, 5, ORANGE);
		scene.idle(90);

		SceneKit.show(scene, util, gaia);
		scene.idle(5);
		SceneKit.show(scene, util, gaiaPool);
		scene.idle(10);
		SceneKit.say(scene, text, 150, util.vector().topOf(pool), true);
		race(scene, util, 160,
				new Lane(from, to, 5, ORANGE, pool, 32000, 96000),
				new Lane(fromGaia, toGaia, 12, SceneHelper.MANA_COLOR, gaiaPool, 16000, 0));
		scene.idle(20);

		scene.world().hideSection(util.select().position(gaia), Direction.UP);
		scene.world().hideSection(util.select().position(gaiaPool), Direction.UP);
		scene.idle(15);
		for (BlockPos flower : flowers) {
			SceneKit.show(scene, util, flower);
			scene.idle(5);
		}
		scene.idle(10);
		SceneKit.say(scene, text, 130, util.vector().topOf(flowers[1]), true);
		SceneKit.spreaderMana(scene, util, spreader, 0);
		for (BlockPos flower : flowers) {
			SceneKit.wispAura(scene, util.vector().centerOf(flower).add(0, 0.3, 0), 0xD03C00, 0.7, 90);
			scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(flower), util.vector().centerOf(spreader), 60);
		}
		for (int charge = 1; charge <= 4; charge++) {
			SceneKit.spreaderMana(scene, util, spreader, charge * 6400);
			SceneHelper.sparkles(scene, util.vector().topOf(spreader), ORANGE, 4);
			scene.idle(18);
		}
		scene.idle(20);

		SceneKit.say(scene, text, 140, util.vector().topOf(spreader), true);
		SceneKit.deliver(scene, util, from, to, pool, 800000, 928000, 4, 5, ORANGE);
		SceneKit.spreaderMana(scene, util, spreader, 0);
		scene.idle(60);
	}

}
