package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.ParticleEmitter;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.state.properties.SculkSensorPhase;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class FunctionalFlowerScenesWorld {
	private FunctionalFlowerScenesWorld() {}

	static void agricarnation(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "agricarnation", 11);
		BlockPos flower = kit.at(5, 1, 5);
		BlockPos pool = kit.at(1, 1, 5);
		List<BlockPos> crops = new ArrayList<>();
		List<Block> types = new ArrayList<>();
		int[] rows = { 2, 3, 7, 8 };
		Block[] rowCrops = { Blocks.WHEAT, Blocks.WHEAT, Blocks.CARROTS, Blocks.POTATOES };
		for (int r = 0; r < rows.length; r++) {
			for (int x = 2; x <= 8; x++) {
				BlockPos crop = kit.at(x, 1, rows[r]);
				crops.add(crop);
				types.add(rowCrops[r]);
				kit.set(crop.below(), Blocks.FARMLAND.defaultBlockState().setValue(FarmBlock.MOISTURE, 7));
				kit.set(crop, rowCrops[r]);
			}
		}

		kit.flower(flower, BotaniaFlowerBlocks.agricarnation);
		kit.say(90, kit.top(flower));
		kit.idle(20);
		kit.show(util.select().fromTo(2, 1, 2, 8, 1, 3).add(util.select().fromTo(2, 1, 7, 8, 1, 8)));
		kit.idle(80);

		kit.pool(pool, 400000);
		kit.idle(10);
		kit.key(130, kit.top(pool));
		kit.idle(30);
		kit.bind(flower, pool);
		kit.idle(80);

		kit.key(120, kit.top(flower));
		kit.idle(10);
		int[] ages = new int[crops.size()];
		Random random = new Random(11);
		int mana = 400000;
		for (int step = 0; step < 40; step++) {
			int index = random.nextInt(crops.size());
			if (ages[index] >= CropBlock.MAX_AGE) {
				continue;
			}
			ages[index] = Math.min(CropBlock.MAX_AGE, ages[index] + 1 + random.nextInt(2));
			int age = ages[index];
			BlockPos crop = crops.get(index);
			scene.world().setBlock(crop, ((CropBlock) types.get(index)).getStateForAge(age), false);
			kit.growth(crop);
			if (step % 6 == 0) {
				mana -= 5000;
				kit.poolMana(pool, mana);
				kit.feed(pool, flower);
			} else {
				kit.idle(4);
			}
		}
		kit.idle(10);

		kit.key(110, kit.top(flower));
		kit.area(flower, 5, 1, 4, 110, PonderPalette.GREEN);
		kit.area(flower, 2, 0, 0, 110, PonderPalette.BLUE);
		kit.idle(120);

		for (int i = 0; i < crops.size(); i++) {
			if (ages[i] < CropBlock.MAX_AGE) {
				ages[i] = CropBlock.MAX_AGE;
				scene.world().setBlock(crops.get(i), ((CropBlock) types.get(i)).getStateForAge(CropBlock.MAX_AGE), false);
				kit.growth(crops.get(i));
				kit.idle(2);
			}
		}
		kit.idle(10);
		kit.redstone(kit.at(6, 1, 5));
		kit.key(120, kit.top(flower));
		kit.idle(130);
	}

	static void clayconia(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "clayconia", 11);
		BlockPos flower = kit.at(5, 1, 5);
		BlockPos pool = kit.at(1, 1, 9);
		int[][] sand = { { 3, 3 }, { 4, 2 }, { 7, 3 }, { 8, 4 }, { 2, 6 }, { 3, 7 }, { 6, 8 }, { 7, 7 }, { 8, 6 }, { 5, 2 }, { 2, 4 }, { 6, 3 } };
		List<BlockPos> sandPos = new ArrayList<>();
		for (int i = 0; i < sand.length; i++) {
			BlockPos pos = kit.at(sand[i][0], 1, sand[i][1]);
			sandPos.add(pos);
			kit.set(pos, i % 4 == 3 ? Blocks.RED_SAND : Blocks.SAND);
		}

		kit.flower(flower, BotaniaFlowerBlocks.clayconia);
		kit.say(80, kit.top(flower));
		kit.idle(15);
		kit.show(kit.positions(sandPos.toArray(new BlockPos[0])));
		kit.idle(75);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.feed(pool, flower);
		kit.idle(70);

		kit.key(120, kit.top(flower));
		kit.idle(15);
		int mana = 300000;
		for (BlockPos pos : sandPos) {
			kit.beam(kit.top(flower), kit.center(pos), 0x7B8792);
			scene.world().destroyBlock(pos);
			kit.item(kit.center(pos), new Vec3(0, 0.15, 0), new ItemStack(Items.CLAY_BALL));
			mana -= 2000;
			kit.poolMana(pool, mana);
			kit.idle(10);
		}
		kit.idle(30);

		kit.key(110, kit.top(flower));
		kit.area(flower, 5, 1, 3, 110, PonderPalette.GREEN);
		kit.area(flower, 2, 1, 1, 110, PonderPalette.BLUE);
		kit.idle(130);
	}

	static void jadedAmaranthus(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "jaded_amaranthus", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos pool = kit.at(1, 1, 7);

		kit.flower(flower, BotaniaFlowerBlocks.jadedAmaranthus);
		kit.say(80, kit.top(flower));
		kit.idle(90);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(90, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		kit.key(130, kit.top(flower));
		kit.idle(10);
		Random random = new Random(5);
		List<BlockPos> used = new ArrayList<>();
		used.add(flower);
		used.add(pool);
		used.add(kit.at(5, 1, 4));
		int mana = 300000;
		for (int i = 0; i < 14; i++) {
			BlockPos spot;
			do {
				spot = kit.at(random.nextInt(9), 1, random.nextInt(9));
			} while (used.contains(spot));
			used.add(spot);
			kit.beam(kit.top(flower), kit.center(spot), 0x961283);
			kit.place(spot, BotaniaBlocks.getFlower(DyeColor.byId(random.nextInt(16))));
			kit.sparkle(spot, 0x961283, 6);
			mana -= 100;
			kit.poolMana(pool, mana);
			kit.idle(12);
		}
		kit.idle(20);

		kit.key(120, kit.top(flower));
		kit.area(flower, 4, 120);
		kit.idle(40);
		kit.redstone(kit.at(5, 1, 4));
		kit.idle(90);
	}

	static void orechid(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "orechid", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(0, 1, 6);
		int[][] stones = { { 1, 0, 1 }, { 2, 0, 1 }, { 4, 0, 1 }, { 5, 0, 2 }, { 1, 0, 4 }, { 5, 0, 4 }, { 2, 0, 5 }, { 4, 0, 5 }, { 1, 1, 2 }, { 5, 1, 5 }, { 0, 0, 3 }, { 6, 0, 3 } };
		boolean[] deep = { false, false, false, false, false, false, false, false, false, false, true, true };
		Block[] ores = { Blocks.COAL_ORE, Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.IRON_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.COAL_ORE, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_COAL_ORE };
		conversion(kit, flower, pool, BotaniaFlowerBlocks.orechid, stones, deep, Blocks.STONE, Blocks.DEEPSLATE, ores, 0x818181, true);
	}

	static void orechidIgnem(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "orechid_ignem", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(0, 1, 6);
		int[][] stones = { { 1, 0, 1 }, { 2, 0, 1 }, { 4, 0, 1 }, { 5, 0, 2 }, { 1, 0, 4 }, { 5, 0, 4 }, { 2, 0, 5 }, { 4, 0, 5 }, { 1, 1, 2 }, { 5, 1, 5 } };
		boolean[] deep = new boolean[stones.length];
		Block[] ores = { Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.ANCIENT_DEBRIS, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE, Blocks.NETHER_QUARTZ_ORE };
		conversion(kit, flower, pool, BotaniaFlowerBlocks.orechidIgnem, stones, deep, Blocks.NETHERRACK, Blocks.NETHERRACK, ores, 0xAE3030, false);
	}

	private static void conversion(FunctionalSceneKit kit, BlockPos flower, BlockPos pool, Block flowerBlock, int[][] stones, boolean[] deep,
			Block stone, Block deepStone, Block[] outputs, int color, boolean extraText) {
		List<BlockPos> raised = new ArrayList<>();
		for (int i = 0; i < stones.length; i++) {
			BlockPos pos = kit.at(stones[i][0], stones[i][1], stones[i][2]);
			kit.set(pos, deep[i] ? deepStone : stone);
			if (pos.getY() > 0) {
				raised.add(pos);
			}
		}

		kit.flower(flower, flowerBlock);
		kit.say(70, kit.top(flower));
		kit.idle(10);
		if (!raised.isEmpty()) {
			kit.show(kit.positions(raised.toArray(new BlockPos[0])));
		}
		kit.idle(70);

		kit.pool(pool, 200000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.bind(flower, pool);
		kit.idle(60);

		kit.key(130, kit.top(flower));
		kit.idle(15);
		int mana = 200000;
		for (int i = 0; i < stones.length; i++) {
			if (deep[i]) {
				continue;
			}
			convert(kit, flower, pool, kit.at(stones[i][0], stones[i][1], stones[i][2]), outputs[i], color);
			mana -= 1500;
			kit.poolMana(pool, mana);
		}
		kit.idle(20);

		kit.key(120, kit.top(flower));
		kit.area(flower, 3, 1, 1, 120, PonderPalette.GREEN);
		kit.idle(20);
		for (int i = 0; i < stones.length; i++) {
			if (deep[i]) {
				convert(kit, flower, pool, kit.at(stones[i][0], stones[i][1], stones[i][2]), outputs[i], color);
			}
		}
		kit.idle(80);

		if (extraText) {
			kit.redstone(kit.at(3, 1, 4));
			kit.key(120, kit.top(flower));
			kit.idle(130);
		} else {
			kit.idle(10);
			kit.redstone(kit.at(3, 1, 4));
			kit.idle(60);
		}
	}

	private static void convert(FunctionalSceneKit kit, BlockPos flower, BlockPos pool, BlockPos target, Block output, int color) {
		kit.feed(pool, flower);
		kit.beam(kit.top(flower), kit.center(target), color);
		kit.change(target, output);
		kit.sparkle(kit.center(target).add(0, 0.5, 0), 0xFF4FD8, 6);
		kit.idle(8);
	}

	static void marimorphosis(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "marimorphosis", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos pool = kit.at(8, 1, 8);
		List<BlockPos> stones = new ArrayList<>();
		Random random = new Random(3);
		for (int x = 0; x < 9; x++) {
			for (int z = 0; z < 9; z++) {
				if ((x + z) % 3 == 0 && !(x == 4 && z == 4) && !(x == 8 && z == 8)) {
					stones.add(kit.at(x, 0, z));
				}
			}
		}
		stones.add(kit.at(2, 1, 6));
		stones.add(kit.at(2, 2, 6));
		stones.add(kit.at(6, 1, 2));
		for (BlockPos pos : stones) {
			kit.set(pos, Blocks.STONE);
		}

		kit.flower(flower, BotaniaFlowerBlocks.marimorphosis);
		kit.say(70, kit.top(flower));
		kit.idle(10);
		kit.show(kit.positions(kit.at(2, 1, 6), kit.at(2, 2, 6), kit.at(6, 1, 2)));
		kit.idle(70);

		kit.pool(pool, 200000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(80);

		kit.key(150, kit.top(flower));
		kit.idle(15);
		Block[] others = { BotaniaBlocks.biomeStonePlains, BotaniaBlocks.biomeStoneMountain, BotaniaBlocks.biomeStoneTaiga };
		java.util.Collections.shuffle(stones, random);
		int mana = 200000;
		for (int i = 0; i < stones.size(); i++) {
			BlockPos pos = stones.get(i);
			Block output = i % 7 == 5 ? others[(i / 7) % others.length] : BotaniaBlocks.biomeStoneForest;
			kit.beam(kit.top(flower), kit.center(pos), 0x769897);
			kit.change(pos, output);
			if (i % 4 == 0) {
				mana -= 100;
				kit.poolMana(pool, mana);
				kit.feed(pool, flower);
			} else {
				kit.idle(6);
			}
		}
		kit.idle(30);

		kit.key(140, kit.top(flower));
		kit.area(flower, 4, 2, 2, 140, PonderPalette.GREEN);
		kit.area(flower, 2, 1, 1, 140, PonderPalette.BLUE);
		kit.idle(150);
	}

	static void bubbell(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "bubbell", 9);
		BlockPos flower = kit.at(1, 1, 4);
		BlockPos pool = kit.at(1, 1, 7);
		List<BlockPos> water = new ArrayList<>();
		for (int x = 3; x <= 8; x++) {
			for (int y = 1; y <= 2; y++) {
				for (int z = 0; z <= 8; z++) {
					BlockPos pos = kit.at(x, y, z);
					water.add(pos);
					kit.set(pos, Blocks.WATER);
				}
			}
		}

		kit.flower(flower, BotaniaFlowerBlocks.bubbell);
		kit.show(util.select().fromTo(3, 1, 0, 8, 2, 8));
		kit.idle(15);
		kit.say(90, kit.top(flower));
		kit.idle(100);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(110, kit.top(pool));
		kit.idle(20);
		kit.bind(flower, pool);
		kit.idle(60);

		kit.key(130, kit.top(flower));
		kit.idle(10);
		int mana = 300000;
		List<BlockPos> cleared = new ArrayList<>();
		for (int radius = 2; radius <= 8; radius++) {
			for (BlockPos pos : water) {
				if (!cleared.contains(pos) && flower.distSqr(pos) < radius * radius) {
					cleared.add(pos);
					kit.set(pos, Blocks.AIR);
					kit.particles(kit.center(pos), ParticleTypes.BUBBLE_POP, 2, 0.6);
				}
			}
			mana -= 6000;
			kit.poolMana(pool, mana);
			kit.feed(pool, flower);
			kit.idle(1);
		}
		kit.idle(110);

		kit.poolMana(pool, 0);
		kit.idle(10);
		kit.key(120, kit.top(flower));
		kit.idle(30);
		for (int i = cleared.size() - 1; i >= 0; i--) {
			kit.set(cleared.get(i), Blocks.WATER);
			if (i % 6 == 0) {
				kit.idle(1);
			}
		}
		kit.idle(100);
	}

	static void exoflame(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "exoflame", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(5, 1, 5);
		BlockPos[] furnaces = { kit.at(1, 1, 1), kit.at(3, 1, 1), kit.at(5, 1, 1) };
		Block[] types = { Blocks.FURNACE, Blocks.SMOKER, Blocks.BLAST_FURNACE };
		ItemStack[] inputs = { new ItemStack(Items.RAW_IRON), new ItemStack(Items.BEEF), new ItemStack(Items.RAW_GOLD) };
		ItemStack[] outputs = { new ItemStack(Items.IRON_INGOT), new ItemStack(Items.COOKED_BEEF), new ItemStack(Items.GOLD_INGOT) };
		for (int i = 0; i < furnaces.length; i++) {
			kit.set(furnaces[i], types[i].defaultBlockState().setValue(AbstractFurnaceBlock.FACING, Direction.SOUTH));
		}

		kit.flower(flower, BotaniaFlowerBlocks.exoflame);
		kit.say(80, kit.top(flower));
		kit.idle(10);
		kit.show(kit.positions(furnaces));
		kit.idle(20);
		for (int i = 0; i < furnaces.length; i++) {
			kit.icon(kit.top(furnaces[i]), inputs[i], 50);
		}
		kit.idle(60);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(120, kit.top(pool));
		kit.idle(20);
		int mana = 300000;
		for (BlockPos furnace : furnaces) {
			kit.feed(pool, flower);
			kit.beam(kit.top(flower), kit.center(furnace), 0xFF6A00);
			scene.world().modifyBlock(furnace, state -> state.setValue(AbstractFurnaceBlock.LIT, true), false);
			kit.particles(kit.util.vector().blockSurface(furnace, Direction.SOUTH), ParticleTypes.FLAME, 6, 0.6);
			mana -= 300;
			kit.poolMana(pool, mana);
			kit.idle(5);
		}
		kit.idle(80);

		kit.key(120, kit.top(flower));
		kit.idle(10);
		for (int round = 0; round < 3; round++) {
			for (int i = 0; i < furnaces.length; i++) {
				kit.particles(kit.util.vector().blockSurface(furnaces[i], Direction.SOUTH), ParticleTypes.FLAME, 4, 0.6);
				kit.particles(kit.top(furnaces[i]), ParticleTypes.SMOKE, 4, 0.5);
				kit.item(kit.util.vector().blockSurface(furnaces[i], Direction.SOUTH).add(0, 0.1, 0.1), new Vec3(0, 0.2, 0.08), outputs[i].copy());
				kit.idle(8);
			}
			kit.idle(16);
		}
		kit.idle(30);

		kit.key(120, kit.top(flower));
		kit.area(flower, 5, 1, 2, 120, PonderPalette.GREEN);
		kit.idle(130);
	}

	static void bergamute(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "bergamute", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos noteBlock = kit.at(2, 1, 4);
		BlockPos sensor = kit.at(8, 1, 4);
		kit.set(noteBlock, Blocks.NOTE_BLOCK);
		kit.set(sensor, Blocks.SCULK_SENSOR);

		kit.flower(flower, BotaniaFlowerBlocks.bergamute);
		kit.say(80, kit.top(flower));
		kit.idle(90);

		kit.show(noteBlock);
		kit.idle(15);
		kit.key(140, kit.top(noteBlock));
		kit.idle(10);
		for (int i = 0; i < 6; i++) {
			notes(kit, noteBlock, i * 4);
			if (i % 2 == 0) {
				kit.sparkle(flower, SceneHelper.POOL_COLOR, 6);
			}
			kit.idle(20);
		}
		kit.idle(30);

		kit.show(sensor);
		kit.idle(15);
		kit.key(150, kit.top(sensor));
		kit.idle(10);
		Vec3 from = kit.center(noteBlock);
		Vec3 to = kit.center(sensor);
		for (int i = 0; i < 2; i++) {
			notes(kit, noteBlock, 6 + i * 6);
			kit.beam(from, kit.center(flower), 0x0E7A84);
			kit.sparkle(flower, SceneHelper.POOL_COLOR, 6);
			scene.overlay().showLine(PonderPalette.RED, from, to, 30);
			kit.idle(40);
		}
		scene.world().hideSection(util.select().position(flower), Direction.UP);
		kit.idle(25);
		notes(kit, noteBlock, 18);
		kit.beam(from, to, 0x0E7A84);
		scene.overlay().showLine(PonderPalette.GREEN, from, to, 30);
		kit.idle(5);
		scene.world().modifyBlock(sensor, state -> state.setValue(SculkSensorBlock.PHASE, SculkSensorPhase.ACTIVE), false);
		scene.effects().indicateRedstone(sensor);
		kit.idle(35);
		scene.world().modifyBlock(sensor, state -> state.setValue(SculkSensorBlock.PHASE, SculkSensorPhase.INACTIVE), false);
		kit.idle(10);
		kit.show(flower);
		kit.idle(20);

		kit.key(130, kit.top(flower));
		kit.area(flower, 4, 130);
		kit.idle(140);
	}

	private static void notes(FunctionalSceneKit kit, BlockPos noteBlock, int note) {
		double color = note / 24.0;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(ParticleTypes.NOTE, x, y, z, color, 0, 0);
		kit.scene.effects().emitParticles(kit.top(noteBlock).add(0, 0.3, 0), emitter, 1, 1);
	}

	static void manastar(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "manastar", 7);
		BlockPos flower = kit.at(2, 1, 3);
		BlockPos pool = kit.at(3, 1, 3);
		BlockPos spreader = kit.at(6, 1, 3);
		BlockPos consumer = kit.at(3, 1, 5);

		kit.flower(flower, BotaniaFlowerBlocks.manastar);
		kit.say(100, kit.top(flower));
		kit.idle(110);

		kit.pool(pool, 200000);
		kit.idle(15);
		kit.key(110, kit.top(pool));
		scene.overlay().showOutline(PonderPalette.GREEN, "manastar_sides", util.select().position(1, 1, 3).add(util.select().position(3, 1, 3))
				.add(util.select().position(2, 1, 2)).add(util.select().position(2, 1, 4)), 110);
		kit.idle(120);

		kit.place(spreader, BotaniaBlocks.manaSpreader);
		kit.idle(15);
		kit.key(130, kit.top(flower));
		kit.idle(10);
		int mana = 200000;
		for (int shot = 0; shot < 6; shot++) {
			SceneHelper.burst(scene, kit.center(spreader).add(-0.5, 0, 0), kit.center(pool).add(0.5, 0, 0), 10, SceneHelper.MANA_COLOR);
			mana += 40000;
			kit.poolMana(pool, mana);
			manastarWisps(kit, flower, 0x0D0DFF, 8);
		}
		kit.idle(20);

		kit.place(consumer, BotaniaFlowerBlocks.orechid);
		kit.idle(15);
		kit.key(130, kit.top(flower));
		kit.idle(10);
		for (int pull = 0; pull < 7; pull++) {
			SceneHelper.burst(scene, kit.center(pool), kit.center(consumer), 8, SceneHelper.POOL_COLOR);
			mana -= 50000;
			kit.poolMana(pool, Math.max(0, mana));
			manastarWisps(kit, flower, 0xFF0D0D, 8);
		}
		kit.idle(40);
	}

	private static void manastarWisps(FunctionalSceneKit kit, BlockPos flower, int color, int ticks) {
		for (int i = 0; i < ticks; i++) {
			if (i % 2 == 0) {
				SceneHelper.wisp(kit.scene, kit.center(flower).add((Math.random() - 0.5) * 0.3, 0.2 + Math.random() * 0.3, (Math.random() - 0.5) * 0.3), color);
			}
			kit.idle(1);
		}
	}
}
