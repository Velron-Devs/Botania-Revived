package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.api.math.Pointing;
import net.createmod.ponder.api.client.element.ElementLink;
import net.createmod.ponder.api.client.element.EntityElement;
import net.createmod.ponder.api.client.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.client.scene.PonderStoryBoard;
import net.createmod.ponder.api.client.scene.SceneBuilder;
import net.createmod.ponder.api.client.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class GeneratingFlowerScenes {
	private static final String PLATE_7 = "flower_plate_7";
	private static final String PLATE_9 = "flower_plate_9";
	private static final Set<Block> COVERED = Set.of(
			BotaniaFlowerBlocks.endoflame,
			BotaniaFlowerBlocks.endoflameFloating,
			BotaniaFlowerBlocks.hydroangeas,
			BotaniaFlowerBlocks.hydroangeasFloating,
			BotaniaFlowerBlocks.thermalily,
			BotaniaFlowerBlocks.thermalilyFloating,
			BotaniaFlowerBlocks.entropinnyum,
			BotaniaFlowerBlocks.entropinnyumFloating,
			BotaniaFlowerBlocks.gourmaryllis,
			BotaniaFlowerBlocks.gourmaryllisFloating,
			BotaniaFlowerBlocks.kekimurus,
			BotaniaFlowerBlocks.kekimurusFloating,
			BotaniaFlowerBlocks.munchdew,
			BotaniaFlowerBlocks.munchdewFloating,
			BotaniaFlowerBlocks.narslimmus,
			BotaniaFlowerBlocks.narslimmusFloating,
			BotaniaFlowerBlocks.rafflowsia,
			BotaniaFlowerBlocks.rafflowsiaFloating,
			BotaniaFlowerBlocks.rosaArcana,
			BotaniaFlowerBlocks.rosaArcanaFloating,
			BotaniaFlowerBlocks.shulkMeNot,
			BotaniaFlowerBlocks.shulkMeNotFloating,
			BotaniaFlowerBlocks.spectrolus,
			BotaniaFlowerBlocks.spectrolusFloating,
			BotaniaFlowerBlocks.dandelifeon,
			BotaniaFlowerBlocks.dandelifeonFloating,
			BotaniaFlowerBlocks.witherManaRose,
			BotaniaFlowerBlocks.witherManaRoseFloating);

	private GeneratingFlowerScenes() {}

	static void register(PonderSceneRegistrationHelper<Identifier> helper) {
		add(helper, PLATE_7, GeneratingFlowerScenes::endoflame, BotaniaFlowerBlocks.endoflame, BotaniaFlowerBlocks.endoflameFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::hydroangeas, BotaniaFlowerBlocks.hydroangeas, BotaniaFlowerBlocks.hydroangeasFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::thermalily, BotaniaFlowerBlocks.thermalily, BotaniaFlowerBlocks.thermalilyFloating);
		add(helper, PLATE_9, GeneratingFlowerScenes::entropinnyum, BotaniaFlowerBlocks.entropinnyum, BotaniaFlowerBlocks.entropinnyumFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::gourmaryllis, BotaniaFlowerBlocks.gourmaryllis, BotaniaFlowerBlocks.gourmaryllisFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::kekimurus, BotaniaFlowerBlocks.kekimurus, BotaniaFlowerBlocks.kekimurusFloating);
		add(helper, PLATE_9, GeneratingFlowerScenes::munchdew, BotaniaFlowerBlocks.munchdew, BotaniaFlowerBlocks.munchdewFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::narslimmus, BotaniaFlowerBlocks.narslimmus, BotaniaFlowerBlocks.narslimmusFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::rafflowsia, BotaniaFlowerBlocks.rafflowsia, BotaniaFlowerBlocks.rafflowsiaFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::rosaArcana, BotaniaFlowerBlocks.rosaArcana, BotaniaFlowerBlocks.rosaArcanaFloating);
		add(helper, PLATE_9, GeneratingFlowerScenes::shulkMeNot, BotaniaFlowerBlocks.shulkMeNot, BotaniaFlowerBlocks.shulkMeNotFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::spectrolus, BotaniaFlowerBlocks.spectrolus, BotaniaFlowerBlocks.spectrolusFloating);
		add(helper, PLATE_9, GeneratingFlowerScenes::dandelifeon, BotaniaFlowerBlocks.dandelifeon, BotaniaFlowerBlocks.dandelifeonFloating);
		add(helper, PLATE_7, GeneratingFlowerScenes::witherManaRose, BotaniaFlowerBlocks.witherManaRose, BotaniaFlowerBlocks.witherManaRoseFloating);
	}

	static boolean covers(Block block) {
		return COVERED.contains(block);
	}

	private static void add(PonderSceneRegistrationHelper<Identifier> helper, String schematic, PonderStoryBoard board, Block... blocks) {
		helper.forComponents(Arrays.stream(blocks).map(BuiltInRegistries.BLOCK::getKey).toList())
				.addStoryBoard(schematic, board, BotaniaPonderTags.FLOWERS);
	}

	private static PonderText start(SceneBuilder scene, String id, int size) {
		scene.title(id, PonderStrings.title(id));
		SceneKit.plate(scene, size);
		return new PonderText(id);
	}

	private static void network(SceneBuilder scene, SceneBuildingUtil util, BlockPos spreader, BlockPos pool, Block spreaderBlock, float rotation) {
		SceneKit.put(scene, spreader, spreaderBlock);
		SceneKit.aim(scene, util, spreader, rotation, 0F);
		SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
	}

	private static void reveal(SceneBuilder scene, SceneBuildingUtil util, BlockPos spreader, BlockPos pool) {
		SceneKit.show(scene, util, spreader);
		scene.idle(5);
		SceneKit.show(scene, util, pool);
		scene.idle(10);
	}

	private static Vec3 ground(SceneBuildingUtil util, int x, int z) {
		return util.vector().topOf(x, 0, z);
	}

	private static ElementLink<EntityElement> eat(SceneBuilder scene, Vec3 at, ItemStack stack, int fall) {
		ElementLink<EntityElement> item = SceneKit.drop(scene, at.add(0, 1.5, 0), stack);
		scene.idle(fall);
		SceneKit.remove(scene, item);
		SceneKit.burst(scene, at.add(0, 0.2, 0), new ItemParticleOption(ParticleTypes.ITEM, stack.getItem()), 10, 0.2, 0.1);
		return item;
	}

	static void endoflame(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_endoflame", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.endoflame);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 100, top, false);
		SceneKit.range(scene, util, 0, 1, 0, 6, 1, 6, 100);
		scene.idle(110);

		SceneKit.say(scene, text, 90, top, true);
		scene.idle(10);
		Vec3 coalAt = ground(util, 1, 2);
		ElementLink<EntityElement> coal = SceneKit.drop(scene, coalAt.add(0, 1.5, 0), new ItemStack(Items.COAL));
		scene.idle(30);
		SceneKit.remove(scene, coal);
		SceneKit.burst(scene, coalAt.add(0, 0.2, 0), ParticleTypes.LARGE_SMOKE, 3, 0.2, 0.02);
		SceneKit.burst(scene, coalAt.add(0, 0.2, 0), ParticleTypes.FLAME, 5, 0.3, 0.02);
		SceneKit.aura(scene, heart.add(0, 0.2, 0), ParticleTypes.FLAME, 0.3, 0.3, 400);
		scene.idle(60);

		SceneKit.say(scene, text, 100, top, true);
		scene.idle(110);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 120000, 5, 12, SceneHelper.MANA_COLOR);
		scene.idle(20);

		SceneKit.say(scene, text, 110, top, true);
		ElementLink<EntityElement> charcoal = SceneKit.drop(scene, ground(util, 4, 2).add(0, 1.5, 0), new ItemStack(Items.CHARCOAL));
		scene.idle(10);
		SceneKit.drop(scene, ground(util, 2, 4).add(0, 1.5, 0), new ItemStack(Items.LAVA_BUCKET));
		scene.idle(60);
		SceneKit.remove(scene, charcoal);
		SceneKit.burst(scene, ground(util, 4, 2).add(0, 0.2, 0), ParticleTypes.FLAME, 5, 0.3, 0.02);
		SceneKit.aura(scene, heart.add(0, 0.2, 0), ParticleTypes.FLAME, 0.3, 0.3, 60);
		scene.idle(70);
	}

	static void hydroangeas(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_hydroangeas", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.hydroangeas);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		BlockState water = Blocks.WATER.defaultBlockState();
		scene.idle(15);

		SceneKit.say(scene, text, 100, top, false);
		SceneKit.range(scene, util, 2, 1, 2, 4, 1, 4, 100);
		scene.idle(110);

		BlockPos lone = util.grid().at(2, 1, 3);
		SceneKit.say(scene, text, 100, util.vector().topOf(lone), true);
		SceneKit.change(scene, lone, water);
		scene.idle(25);
		SceneKit.put(scene, lone, Blocks.AIR.defaultBlockState());
		SceneKit.burst(scene, util.vector().centerOf(lone), ParticleTypes.SPLASH, 12, 0.6, 0.1);
		SceneKit.wispAura(scene, heart, 0x532FE0, 0.6, 40);
		scene.idle(75);

		BlockPos[] pond = { util.grid().at(1, 1, 1), util.grid().at(2, 1, 1), util.grid().at(1, 1, 2), util.grid().at(2, 1, 2) };
		BlockPos single = util.grid().at(4, 1, 4);
		SceneKit.say(scene, text, 130, util.vector().topOf(pond[3]), true);
		for (BlockPos pos : pond) {
			SceneKit.change(scene, pos, water);
		}
		SceneKit.change(scene, single, water);
		scene.idle(25);
		SceneKit.put(scene, single, Blocks.AIR.defaultBlockState());
		SceneKit.burst(scene, util.vector().centerOf(single), ParticleTypes.SPLASH, 12, 0.6, 0.1);
		SceneKit.wispAura(scene, heart, 0x532FE0, 0.6, 40);
		scene.idle(45);
		for (int drink = 0; drink < 2; drink++) {
			SceneKit.burst(scene, util.vector().centerOf(pond[3]), ParticleTypes.SPLASH, 12, 0.6, 0.1);
			SceneKit.wispAura(scene, heart, 0x532FE0, 0.6, 40);
			scene.idle(45);
		}

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), true);
		SceneKit.wispAura(scene, heart, 0x532FE0, 0.5, 160);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 40000, 4, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);

		SceneKit.say(scene, text, 110, top, true);
		scene.idle(20);
		SceneKit.change(scene, flower, Blocks.DEAD_BUSH.defaultBlockState());
		scene.idle(100);
	}

	static void thermalily(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_thermalily", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 2);
		BlockPos pool = util.grid().at(5, 1, 4);
		BlockPos comparator = util.grid().at(3, 1, 4);
		BlockPos lamp = util.grid().at(3, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.thermalily);
		SceneKit.put(scene, comparator, Blocks.COMPARATOR.defaultBlockState()
				.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
				.setValue(BlockStateProperties.POWERED, true));
		SceneKit.put(scene, lamp, Blocks.REDSTONE_LAMP.defaultBlockState().setValue(BlockStateProperties.LIT, true));
		SceneKit.stage(scene, util, 7, 3, spreader, pool, comparator, lamp);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		BlockPos lavaA = util.grid().at(2, 1, 3);
		BlockPos lavaB = util.grid().at(4, 1, 2);
		scene.idle(15);

		SceneKit.say(scene, text, 110, top, false);
		SceneKit.range(scene, util, 2, 1, 2, 4, 1, 4, 80);
		SceneKit.change(scene, lavaA, Blocks.LAVA.defaultBlockState());
		SceneKit.change(scene, lavaB, Blocks.LAVA.defaultBlockState());
		scene.idle(40);
		SceneKit.put(scene, lavaA, Blocks.AIR.defaultBlockState());
		SceneKit.burst(scene, util.vector().centerOf(lavaA), ParticleTypes.LAVA, 6, 0.6, 0.1);
		SceneKit.wispAura(scene, heart.add(0, 0.3, 0), 0xD03C00, 0.7, 330);
		scene.idle(80);

		SceneKit.say(scene, text, 100, top, true);
		scene.idle(110);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 160000, 10, 3, SceneHelper.MANA_COLOR);
		scene.idle(20);

		SceneKit.say(scene, text, 120, top, true);
		SceneKit.aura(scene, heart.add(0, 0.2, 0), ParticleTypes.SMOKE, 0.5, 0.3, 250);
		scene.idle(50);
		SceneKit.put(scene, lavaB, Blocks.AIR.defaultBlockState());
		SceneKit.burst(scene, util.vector().centerOf(lavaB), ParticleTypes.LARGE_SMOKE, 8, 0.6, 0.05);
		scene.idle(80);

		SceneKit.show(scene, util, comparator);
		scene.idle(5);
		SceneKit.show(scene, util, lamp);
		scene.idle(10);
		SceneKit.say(scene, text, 100, util.vector().topOf(comparator), true);
		scene.effects().indicateRedstone(comparator);
		scene.idle(110);
	}

	static void entropinnyum(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_entropinnyum", 9);
		BlockPos flower = util.grid().at(4, 1, 4);
		BlockPos spreader = util.grid().at(7, 1, 1);
		BlockPos pool = util.grid().at(7, 1, 6);
		BlockPos tnt = util.grid().at(1, 1, 6);
		BlockPos wet = util.grid().at(1, 1, 2);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.entropinnyum);
		SceneKit.stage(scene, util, 9, 4, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 130, top, false);
		SceneKit.range(scene, util, 0, 1, 0, 8, 1, 8, 90);
		SceneKit.change(scene, tnt, Blocks.TNT.defaultBlockState());
		scene.idle(25);
		scene.overlay().showControls(util.vector().blockSurface(tnt, Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(Items.FLINT_AND_STEEL));
		scene.idle(20);
		SceneKit.put(scene, tnt, Blocks.AIR.defaultBlockState());
		Vec3 tntAt = ground(util, 1, 6);
		ElementLink<EntityElement> primed = primedTnt(scene, tntAt, 50);
		scene.idle(48);
		SceneKit.remove(scene, primed);
		SceneKit.burst(scene, tntAt.add(0, 0.5, 0), ParticleTypes.EXPLOSION, 4, 1.2, 0);
		SceneHelper.sparkles(scene, tntAt.add(0, 0.5, 0), 0xCB0000, 20);
		SceneHelper.path(scene, 0xCB0000, 10, tntAt.add(0, 0.5, 0), heart);
		SceneHelper.sparkles(scene, top, 0xCB0000, 15);
		scene.idle(20);

		SceneKit.say(scene, text, 100, top, true);
		SceneKit.wispAura(scene, heart, 0xCB0000, 0.6, 100);
		scene.idle(110);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 200000, 6, 14, SceneHelper.MANA_COLOR);
		scene.idle(20);

		SceneKit.say(scene, text, 120, util.vector().topOf(wet), true);
		SceneKit.change(scene, wet, Blocks.WATER.defaultBlockState());
		scene.idle(15);
		Vec3 wetAt = ground(util, 1, 2);
		ElementLink<EntityElement> drowned = primedTnt(scene, wetAt, 40);
		scene.idle(40);
		SceneKit.remove(scene, drowned);
		SceneKit.burst(scene, wetAt.add(0, 0.5, 0), ParticleTypes.EXPLOSION, 2, 0.8, 0);
		SceneKit.burst(scene, wetAt.add(0, 0.5, 0), ParticleTypes.SPLASH, 20, 1, 0.2);
		scene.idle(30);
		SceneKit.burst(scene, top, ParticleTypes.ANGRY_VILLAGER, 4, 0.6, 0);
		scene.idle(50);
	}

	private static ElementLink<EntityElement> primedTnt(SceneBuilder scene, Vec3 at, int fuse) {
		return scene.world().createEntity(level -> {
			PrimedTnt entity = new PrimedTnt(EntityType.TNT, level);
			entity.setPos(at.x, at.y, at.z);
			entity.setFuse(fuse);
			return entity;
		});
	}

	static void gourmaryllis(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_gourmaryllis", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.gourmaryllis);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 90, top, false);
		SceneKit.range(scene, util, 2, 1, 2, 4, 1, 4, 90);
		scene.idle(100);

		ItemStack steak = new ItemStack(Items.COOKED_BEEF);
		SceneKit.say(scene, text, 100, top, true);
		scene.idle(10);
		eat(scene, ground(util, 2, 3), steak, 25);
		scene.idle(75);

		SceneKit.say(scene, text, 90, top, true);
		SceneKit.aura(scene, heart.add(0, 0.2, 0), new ItemParticleOption(ParticleTypes.ITEM, steak.getItem()), 0.3, 0.2, 80);
		scene.idle(85);
		SceneHelper.sparkles(scene, top, 0xD3D604, 15);
		scene.idle(20);

		SceneKit.say(scene, text, 110, top, true);
		scene.idle(10);
		ItemStack bread = new ItemStack(Items.BREAD);
		eat(scene, ground(util, 2, 4), bread, 25);
		SceneKit.aura(scene, heart.add(0, 0.2, 0), new ItemParticleOption(ParticleTypes.ITEM, bread.getItem()), 0.3, 0.2, 50);
		scene.idle(5);
		ElementLink<EntityElement> cookie = SceneKit.drop(scene, ground(util, 4, 3).add(0, 1.5, 0), new ItemStack(Items.COOKIE));
		scene.idle(25);
		SceneKit.remove(scene, cookie);
		SceneKit.burst(scene, ground(util, 4, 3).add(0, 0.2, 0), ParticleTypes.LARGE_SMOKE, 6, 0.3, 0.02);
		scene.idle(25);
		SceneHelper.sparkles(scene, top, 0xD3D604, 8);
		scene.idle(40);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 130, top, true);
		SceneKit.bind(scene, util, flower, spreader);
		Item[] menu = { Items.CARROT, Items.APPLE, Items.BAKED_POTATO };
		int mana = 0;
		for (int i = 0; i < menu.length; i++) {
			ItemStack food = new ItemStack(menu[i]);
			eat(scene, ground(util, 2 + i, 2), food, 22);
			SceneKit.aura(scene, heart.add(0, 0.2, 0), new ItemParticleOption(ParticleTypes.ITEM, food.getItem()), 0.3, 0.2, 30);
			scene.idle(30);
			SceneHelper.sparkles(scene, top, 0xD3D604, 8 + i * 4);
			SceneKit.deliver(scene, util, from, to, pool, mana, mana + 60000 + i * 20000, 2, 12, SceneHelper.MANA_COLOR);
			mana += 60000 + i * 20000;
		}
		scene.idle(30);
	}

	static void kekimurus(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_kekimurus", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		BlockPos cake = util.grid().at(1, 1, 1);
		BlockPos cake2 = util.grid().at(1, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.kekimurus);
		SceneKit.put(scene, cake, Blocks.CAKE);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 100, util.vector().topOf(cake), false);
		SceneKit.range(scene, util, 0, 1, 0, 6, 1, 6, 100);
		scene.idle(110);

		SceneKit.say(scene, text, 110, top, true);
		scene.idle(10);
		biteCake(scene, util, cake, 7, top);
		scene.idle(20);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 150000, 6, 12, SceneHelper.MANA_COLOR);
		scene.idle(20);

		SceneKit.change(scene, cake2, Blocks.CAKE.defaultBlockState());
		scene.idle(10);
		SceneKit.say(scene, text, 100, util.vector().topOf(cake2), true);
		biteCake(scene, util, cake2, 3, top);
		scene.idle(60);
	}

	private static void biteCake(SceneBuilder scene, SceneBuildingUtil util, BlockPos cake, int bites, Vec3 top) {
		BlockParticleOption crumbs = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.CAKE.defaultBlockState());
		for (int bite = 1; bite <= bites; bite++) {
			int slices = bite;
			if (slices > 6) {
				scene.world().destroyBlock(cake);
			} else {
				scene.world().modifyBlock(cake, state -> state.setValue(CakeBlock.BITES, slices), false);
				SceneKit.burst(scene, util.vector().centerOf(cake), crumbs, 10, 0.5, 0.1);
			}
			SceneHelper.sparkles(scene, top, 0x935D28, 6);
			scene.idle(14);
		}
	}

	static void munchdew(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_munchdew", 9);
		BlockPos flower = util.grid().at(5, 1, 4);
		BlockPos spreader = util.grid().at(7, 1, 1);
		BlockPos pool = util.grid().at(7, 1, 6);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.munchdew);
		List<BlockPos> leaves = new ArrayList<>();
		List<Integer> distances = new ArrayList<>();
		for (int y = 1; y <= 4; y++) {
			SceneKit.put(scene, util.grid().at(2, y, 2), Blocks.OAK_LOG);
		}
		for (int y = 3; y <= 6; y++) {
			for (int x = 0; x <= 4; x++) {
				for (int z = 0; z <= 4; z++) {
					int dx = Math.abs(x - 2);
					int dz = Math.abs(z - 2);
					boolean inside = switch (y) {
						case 3 -> true;
						case 4 -> dx + dz < 4;
						case 5 -> dx <= 1 && dz <= 1;
						default -> dx + dz <= 1;
					};
					if (!inside || dx == 0 && dz == 0 && y <= 4) {
						continue;
					}
					int distance = Math.min(7, dx + dz + Math.max(0, y - 4));
					BlockPos pos = util.grid().at(x, y, z);
					SceneKit.put(scene, pos, Blocks.OAK_LEAVES.defaultBlockState()
							.setValue(LeavesBlock.PERSISTENT, true)
							.setValue(LeavesBlock.DISTANCE, Math.max(1, distance)));
					leaves.add(pos);
					distances.add(distance);
				}
			}
		}
		SceneKit.stage(scene, util, 9, 7, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 110, top, false);
		SceneKit.range(scene, util, 0, 1, 0, 8, 7, 8, 100);
		scene.idle(120);

		SceneKit.say(scene, text, 110, top, true);
		Set<BlockPos> remaining = new HashSet<>(leaves);
		int eaten = 0;
		while (!remaining.isEmpty()) {
			BlockPos best = null;
			int bestDistance = -1;
			for (int i = 0; i < leaves.size(); i++) {
				BlockPos pos = leaves.get(i);
				if (remaining.contains(pos) && distances.get(i) > bestDistance && exposed(pos, remaining)) {
					best = pos;
					bestDistance = distances.get(i);
				}
			}
			if (best == null) {
				break;
			}
			remaining.remove(best);
			scene.world().destroyBlock(best);
			if (++eaten % 4 == 0) {
				SceneHelper.sparkles(scene, top, 0x79C42F, 4);
			}
			scene.idle(3);
		}
		scene.idle(20);

		SceneKit.say(scene, text, 110, top, true);
		SceneKit.wispAura(scene, heart, 0x808080, 0.5, 120);
		scene.idle(125);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 160000, 6, 16, SceneHelper.MANA_COLOR);
		scene.idle(40);
	}

	private static boolean exposed(BlockPos pos, Set<BlockPos> leaves) {
		for (Direction dir : Direction.values()) {
			BlockPos side = pos.relative(dir);
			boolean log = side.getX() == 2 && side.getZ() == 2 && side.getY() >= 1 && side.getY() <= 4;
			if (!leaves.contains(side) && !log && side.getY() > 0) {
				return true;
			}
		}
		return false;
	}

	static void narslimmus(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_narslimmus", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.narslimmus);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 90, top, false);
		SceneKit.range(scene, util, 1, 1, 1, 5, 1, 5, 90);
		scene.idle(100);

		SceneKit.say(scene, text, 150, top, true);
		int[] sizes = { 1, 2, 4 };
		Vec3[][] routes = {
				{ new Vec3(0.3, 1, 3.5), new Vec3(1.6, 1, 3.5) },
				{ new Vec3(0.5, 1, 6.5), new Vec3(1.7, 1, 5.3) },
				{ new Vec3(0.5, 1, 0.5), new Vec3(1.8, 1, 1.8) } };
		for (int i = 0; i < sizes.length; i++) {
			int size = sizes[i];
			Vec3 start = routes[i][0];
			ElementLink<EntityElement> slime = SceneKit.mob(scene, EntityType.SLIME, start, -90F, s -> s.setSize(size, true));
			scene.idle(5);
			SceneKit.move(scene, slime, start, routes[i][1], 20, 0.4);
			SceneKit.remove(scene, slime);
			SceneKit.burst(scene, routes[i][1].add(0, 0.3 * size, 0), ParticleTypes.ITEM_SLIME, 8 * size, 0.5 * size, 0.1);
			SceneHelper.sparkles(scene, top, 0x71C373, 5 * size);
			scene.idle(15);
		}
		scene.idle(10);

		Vec3 eggAt = ground(util, 2, 4);
		SceneKit.say(scene, text, 130, eggAt, true);
		scene.overlay().showControls(eggAt, Pointing.DOWN, 25).rightClick().withItem(new ItemStack(Items.SLIME_SPAWN_EGG));
		scene.idle(20);
		ElementLink<EntityElement> tame = SceneKit.mob(scene, EntityType.SLIME, eggAt, 0F, s -> s.setSize(2, true));
		scene.idle(120);
		SceneKit.remove(scene, tame);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 100, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 200000, 6, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);
	}

	static void rafflowsia(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_rafflowsia", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(6, 1, 1);
		BlockPos pool = util.grid().at(6, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.rafflowsia);
		BlockPos[] meals = { util.grid().at(1, 1, 1), util.grid().at(1, 1, 3), util.grid().at(1, 1, 5), util.grid().at(3, 1, 5), util.grid().at(4, 1, 1) };
		Block[] kinds = { BotaniaFlowerBlocks.hydroangeas, BotaniaFlowerBlocks.bellethorn, BotaniaFlowerBlocks.orechid, BotaniaFlowerBlocks.endoflame, BotaniaFlowerBlocks.endoflame };
		for (int i = 0; i < meals.length; i++) {
			SceneKit.put(scene, meals[i], kinds[i]);
		}
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 100, top, false);
		SceneKit.range(scene, util, 0, 1, 0, 6, 1, 6, 100);
		scene.idle(110);

		SceneKit.say(scene, text, 130, top, true);
		for (int i = 0; i < 4; i++) {
			scene.idle(12);
			scene.world().destroyBlock(meals[i]);
			SceneHelper.path(scene, 0x502C76, 8, util.vector().centerOf(meals[i]), heart);
			SceneHelper.sparkles(scene, top, 0x502C76, 6 + i * 4);
		}
		scene.idle(30);

		SceneKit.say(scene, text, 100, top, true);
		scene.idle(15);
		scene.world().destroyBlock(meals[4]);
		SceneHelper.path(scene, 0x777777, 8, util.vector().centerOf(meals[4]), heart);
		SceneHelper.sparkles(scene, top, 0x777777, 4);
		scene.idle(70);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 300000, 7, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);
	}

	static void rosaArcana(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_rosa_arcana", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.rosaArcana);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 90, top, false);
		SceneKit.range(scene, util, 2, 1, 2, 4, 1, 4, 90);
		scene.idle(100);

		SceneKit.say(scene, text, 100, top, true);
		List<ElementLink<EntityElement>> orbs = new ArrayList<>();
		double[][] spots = { { 2.4, 3.3 }, { 4.5, 2.6 }, { 3.6, 4.4 }, { 2.7, 4.6 }, { 4.3, 3.7 } };
		for (double[] spot : spots) {
			orbs.add(orb(scene, new Vec3(spot[0], 2.6, spot[1]), 7));
			scene.idle(4);
		}
		scene.idle(20);
		for (ElementLink<EntityElement> orb : orbs) {
			SceneKit.remove(scene, orb);
			SceneHelper.sparkles(scene, top, 0xFF8EF8, 4);
			scene.idle(4);
		}
		scene.idle(50);

		ItemStack sword = new ItemStack(Items.DIAMOND_SWORD);
		sword.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
		Vec3 swordAt = ground(util, 2, 3);
		SceneKit.say(scene, text, 130, swordAt, true);
		ElementLink<EntityElement> enchanted = SceneKit.drop(scene, swordAt.add(0, 1.5, 0), sword);
		scene.idle(30);
		SceneKit.remove(scene, enchanted);
		SceneKit.burst(scene, swordAt.add(0, 0.3, 0), ParticleTypes.ENCHANT, 20, 0.5, 0.3);
		SceneKit.drop(scene, swordAt.add(0, 0.1, 0), new ItemStack(Items.DIAMOND_SWORD));
		List<ElementLink<EntityElement>> released = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			released.add(orb(scene, heart.add((i - 1) * 0.3, 0.4, 0), 10));
		}
		scene.idle(20);
		for (ElementLink<EntityElement> orb : released) {
			SceneKit.remove(scene, orb);
		}
		SceneHelper.sparkles(scene, top, 0xFF8EF8, 15);
		scene.idle(70);

		Vec3 player = ground(util, 4, 3);
		SceneKit.say(scene, text, 120, player, true);
		SceneKit.trail(scene, player.add(0, 0.8, 0), heart, 15, ParticleTypes.ENCHANT);
		SceneKit.trail(scene, player.add(0, 0.6, 0), heart, 15, ParticleTypes.ENCHANT);
		SceneHelper.sparkles(scene, top, 0xFF8EF8, 10);
		scene.idle(90);

		reveal(scene, util, spreader, pool);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 120000, 5, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);
	}

	private static ElementLink<EntityElement> orb(SceneBuilder scene, Vec3 at, int value) {
		return scene.world().createEntity(level -> new ExperienceOrb(level, at.x, at.y, at.z, value));
	}

	static void shulkMeNot(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_shulk_me_not", 9);
		BlockPos flower = util.grid().at(4, 1, 4);
		BlockPos spreader = util.grid().at(7, 1, 1);
		BlockPos pool = util.grid().at(7, 1, 7);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.shulkMeNot);
		SceneKit.stage(scene, util, 9, 4, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		Vec3 shulkerAt = ground(util, 1, 7);
		Vec3 zombieAt = ground(util, 2, 2);
		scene.idle(15);

		ElementLink<EntityElement> shulker = SceneKit.mob(scene, EntityType.SHULKER, shulkerAt, 0F, s -> {});
		ElementLink<EntityElement> zombie = SceneKit.mob(scene, EntityType.ZOMBIE, zombieAt, 0F, z -> {});
		scene.idle(10);
		SceneKit.say(scene, text, 100, top, false);
		SceneKit.range(scene, util, 0, 1, 0, 8, 1, 8, 100);
		scene.idle(110);

		SceneKit.say(scene, text, 140, zombieAt.add(0, 2, 0), true);
		SceneKit.trail(scene, shulkerAt.add(0, 0.8, 0), zombieAt.add(0, 1.2, 0), 25, ParticleTypes.END_ROD);
		Vec3 lifted = zombieAt.add(0, 1.6, 0);
		for (int i = 1; i <= 30; i++) {
			Vec3 pos = zombieAt.lerp(lifted, i / 30.0);
			scene.world().modifyEntity(zombie, e -> e.setPos(pos.x, pos.y, pos.z));
			if (i % 3 == 0) {
				SceneKit.burst(scene, pos.add(0, 1, 0), ParticleTypes.END_ROD, 1, 0.6, 0.01);
			}
			scene.idle(1);
		}
		scene.idle(15);
		SceneKit.remove(scene, zombie);
		SceneKit.remove(scene, shulker);
		SceneKit.burst(scene, lifted.add(0, 1, 0), ParticleTypes.EXPLOSION, 4, 1, 0);
		SceneKit.burst(scene, shulkerAt.add(0, 0.5, 0), ParticleTypes.EXPLOSION, 4, 1, 0);
		SceneKit.burst(scene, heart, ParticleTypes.PORTAL, 40, 0.2, 0.6);
		SceneHelper.sparkles(scene, top, 0x815598, 20);
		scene.idle(40);

		SceneKit.say(scene, text, 100, top, true);
		SceneKit.wispAura(scene, heart, 0x815598, 0.6, 100);
		scene.idle(110);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 300000, 8, 14, SceneHelper.MANA_COLOR);
		scene.idle(30);
	}

	static void spectrolus(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_spectrolus", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.spectrolus);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		SceneKit.say(scene, text, 120, top, false);
		Item[] rainbow = { Items.WHITE_WOOL, Items.ORANGE_WOOL, Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.YELLOW_WOOL };
		for (int i = 0; i < rainbow.length; i++) {
			scene.overlay().showControls(top.add(0, 0.6, 0), Pointing.DOWN, 18).withItem(new ItemStack(rainbow[i]));
			scene.idle(22);
		}
		scene.idle(20);

		SceneKit.say(scene, text, 110, top, true);
		scene.idle(10);
		eat(scene, ground(util, 2, 3), new ItemStack(Items.WHITE_WOOL), 25);
		SceneHelper.sparkles(scene, top, 0xF9FFFE, 10);
		scene.idle(20);
		eat(scene, ground(util, 2, 4), new ItemStack(Items.ORANGE_WOOL), 25);
		SceneHelper.sparkles(scene, top, 0xF9801D, 10);
		scene.idle(30);

		SceneKit.say(scene, text, 90, ground(util, 4, 3), true);
		scene.idle(10);
		ElementLink<EntityElement> black = SceneKit.drop(scene, ground(util, 4, 3).add(0, 1.5, 0), new ItemStack(Items.BLACK_WOOL));
		scene.idle(25);
		SceneKit.remove(scene, black);
		SceneKit.burst(scene, ground(util, 4, 3).add(0, 0.2, 0), ParticleTypes.LARGE_SMOKE, 6, 0.3, 0.02);
		scene.idle(60);

		SceneKit.say(scene, text, 130, top, true);
		Vec3 sheepStart = ground(util, 0, 3);
		Vec3 sheepEnd = ground(util, 3, 3);
		ElementLink<EntityElement> sheep = SceneKit.mob(scene, EntityType.SHEEP, sheepStart, -90F, s -> s.setColor(DyeColor.MAGENTA));
		scene.idle(10);
		SceneKit.move(scene, sheep, sheepStart, sheepEnd, 40, 0);
		scene.idle(10);
		SceneKit.remove(scene, sheep);
		SceneKit.burst(scene, sheepEnd.add(0, 0.8, 0), new ItemParticleOption(ParticleTypes.ITEM, Items.MUTTON), 15, 0.4, 0.1);
		SceneKit.burst(scene, sheepEnd.add(0, 0.8, 0), new ItemParticleOption(ParticleTypes.ITEM, Items.MAGENTA_WOOL), 15, 0.4, 0.1);
		SceneHelper.sparkles(scene, top, 0xC74EBD, 15);
		scene.idle(70);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, top, true);
		scene.overlay().showControls(top, Pointing.DOWN, 30).withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(35);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 150000, 5, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);
	}

	static void dandelifeon(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_dandelifeon", 9);
		int center = 6;
		BlockPos flower = util.grid().at(center, 1, center);
		BlockPos redstone = util.grid().at(center + 1, 1, center);
		BlockPos spreader = util.grid().at(4, 1, 8);
		BlockPos pool = util.grid().at(8, 1, 8);
		network(scene, util, spreader, pool, BotaniaBlocks.manaSpreader, SceneKit.EAST);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.dandelifeon);
		SceneKit.put(scene, redstone, Blocks.REDSTONE_BLOCK);
		SceneKit.stage(scene, util, 9, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);
		scene.idle(15);

		int n = 9;
		int[][] cells = new int[n][n];
		boolean[][] blocked = new boolean[n][n];
		for (int[] row : cells) {
			Arrays.fill(row, -1);
		}
		blocked[center][center] = true;
		blocked[center + 1][center] = true;
		int[][] glider = { { 1, 0 }, { 2, 1 }, { 0, 2 }, { 1, 2 }, { 2, 2 } };

		SceneKit.say(scene, text, 130, top, false);
		scene.effects().indicateRedstone(redstone);
		SceneKit.range(scene, util, 0, 1, 0, 8, 1, 8, 60);
		scene.idle(40);
		for (int[] cell : glider) {
			BlockPos pos = util.grid().at(cell[0], 1, cell[1]);
			scene.overlay().showControls(util.vector().topOf(pos), Pointing.DOWN, 8).rightClick().withItem(new ItemStack(BotaniaBlocks.cellBlock));
			SceneKit.change(scene, pos, BotaniaBlocks.cellBlock.defaultBlockState());
			cells[cell[0]][cell[1]] = 0;
			scene.idle(10);
		}
		scene.idle(50);

		SceneKit.say(scene, text, 110, util.vector().topOf(util.grid().at(1, 1, 1)), true);
		scene.idle(20);
		boolean consumed = false;
		int mana = 0;
		for (int step = 0; step < 24; step++) {
			int[][] next = new int[n][n];
			int[][] old = new int[n][n];
			boolean wipe = false;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int oldLife = cells[i][j];
					int adj = 0;
					int max = -1;
					for (int dx = -1; dx <= 1; dx++) {
						for (int dz = -1; dz <= 1; dz++) {
							int x = i + dx;
							int z = j + dz;
							if ((dx != 0 || dz != 0) && x >= 0 && z >= 0 && x < n && z < n && cells[x][z] >= 0) {
								adj++;
								max = Math.max(max, cells[x][z]);
							}
						}
					}
					int newLife;
					if (adj == 3 && oldLife < 0) {
						newLife = max + 1;
					} else if ((adj == 2 || adj == 3) && oldLife >= 0) {
						newLife = oldLife + 1;
					} else {
						newLife = -1;
					}
					if (Math.abs(i - center) <= 1 && Math.abs(j - center) <= 1 && newLife >= 0) {
						if (oldLife == 1) {
							newLife = -1;
						} else {
							oldLife = newLife;
							newLife = -2;
							wipe = true;
						}
					}
					next[i][j] = newLife;
					old[i][j] = oldLife;
				}
			}
			List<BlockPos> eatenNow = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					int newLife = next[i][j];
					if (newLife != -2 && wipe) {
						newLife = -1;
					}
					BlockPos pos = util.grid().at(i, 1, j);
					boolean present = cells[i][j] >= 0;
					if (newLife == -2) {
						if (!present && !blocked[i][j]) {
							mana += Math.min(old[i][j], 100) * 60;
							eatenNow.add(pos);
						}
						if (present) {
							SceneKit.put(scene, pos, Blocks.AIR.defaultBlockState());
						}
						cells[i][j] = -1;
					} else if (newLife >= 0 && !blocked[i][j]) {
						if (!present) {
							SceneKit.put(scene, pos, BotaniaBlocks.cellBlock.defaultBlockState());
						}
						cells[i][j] = Math.min(newLife, 100);
					} else {
						if (present) {
							SceneKit.put(scene, pos, Blocks.AIR.defaultBlockState());
						}
						cells[i][j] = -1;
					}
				}
			}
			for (BlockPos pos : eatenNow) {
				SceneHelper.path(scene, 0x9C0A7E, 4, util.vector().centerOf(pos), heart);
			}
			if (wipe && !consumed) {
				consumed = true;
				SceneHelper.sparkles(scene, top, 0x9C0A7E, 20);
				SceneKit.say(scene, text, 110, top, true);
				scene.idle(110);
				break;
			}
			scene.idle(9);
		}
		if (!consumed) {
			SceneKit.say(scene, text, 110, top, true);
			scene.idle(110);
		}

		SceneKit.say(scene, text, 110, top, true);
		SceneKit.wispAura(scene, heart, 0x9C0A7E, 0.5, 100);
		scene.idle(120);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, Math.max(60000, mana * 20), 4, 12, SceneHelper.MANA_COLOR);
		scene.idle(30);
	}

	static void witherManaRose(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = start(scene, "flower_wither_mana_rose", 7);
		BlockPos flower = util.grid().at(3, 1, 3);
		BlockPos spreader = util.grid().at(5, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 5);
		network(scene, util, spreader, pool, BotaniaBlocks.gaiaSpreader, SceneKit.SOUTH);
		SceneKit.put(scene, flower, BotaniaFlowerBlocks.witherManaRose);
		SceneKit.stage(scene, util, 7, 3, spreader, pool);
		Vec3 top = util.vector().topOf(flower);
		Vec3 heart = util.vector().centerOf(flower);
		Vec3 from = util.vector().centerOf(spreader).add(0, 0, 0.5);
		Vec3 to = util.vector().centerOf(pool).add(0, 0, -0.5);
		scene.idle(15);

		Vec3 starAt = ground(util, 2, 3);
		SceneKit.say(scene, text, 100, top, false);
		scene.idle(15);
		ElementLink<EntityElement> star = SceneKit.drop(scene, starAt.add(0, 1.5, 0), new ItemStack(Items.NETHER_STAR));
		scene.idle(30);
		SceneKit.remove(scene, star);
		SceneKit.burst(scene, starAt.add(0, 0.2, 0), ParticleTypes.SOUL, 10, 0.3, 0.08);
		SceneKit.burst(scene, starAt.add(0, 0.2, 0), ParticleTypes.LARGE_SMOKE, 3, 0.2, 0.02);
		SceneKit.aura(scene, heart.add(0, 0.2, 0), ParticleTypes.SMOKE, 0.3, 0.3, 420);
		scene.idle(65);

		reveal(scene, util, spreader, pool);
		SceneKit.say(scene, text, 110, top, true);
		SceneKit.bind(scene, util, flower, spreader);
		SceneKit.deliver(scene, util, from, to, pool, 0, 500000, 12, 6, SceneHelper.MANA_COLOR);
		scene.idle(10);

		SceneKit.say(scene, text, 110, util.vector().topOf(spreader), true);
		SceneKit.deliver(scene, util, from, to, pool, 500000, 800000, 8, 6, SceneHelper.MANA_COLOR);
		scene.idle(40);

		SceneKit.say(scene, text, 110, top, true);
		SceneKit.wispAura(scene, heart, 0x333333, 0.6, 110);
		scene.idle(20);
		SceneKit.drop(scene, ground(util, 4, 3).add(0, 1.5, 0), new ItemStack(Items.NETHER_STAR));
		scene.idle(100);
	}
}
