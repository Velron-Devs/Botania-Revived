package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.ParticleEmitter;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;

final class FunctionalFlowerScenesItems {
	private FunctionalFlowerScenesItems() {}

	static void hopperhock(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "hopperhock", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos chest = kit.at(5, 1, 4);
		BlockPos filtered = kit.at(3, 1, 4);
		BlockPos pool = kit.at(8, 1, 8);
		Vec3 flowerTop = kit.top(flower);
		kit.set(chest, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));
		kit.set(filtered, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));

		kit.flower(flower, BotaniaFlowerBlocks.hopperhock);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.show(chest);
		kit.idle(15);
		kit.key(120, kit.top(chest));
		kit.idle(10);
		Vec3[] near = { new Vec3(2.5, 1.2, 2.5), new Vec3(6.5, 1.2, 1.5), new Vec3(2.0, 1.2, 6.5), new Vec3(6.5, 1.2, 6.0) };
		ItemStack[] loot = { new ItemStack(Items.BONE), new ItemStack(Items.STRING), new ItemStack(Items.ROTTEN_FLESH), new ItemStack(Items.ARROW) };
		collect(kit, chest, near, loot);
		kit.idle(40);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(130, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		Vec3[] far = { new Vec3(0.5, 1.2, 0.5), new Vec3(8.5, 1.2, 0.5), new Vec3(0.5, 1.2, 8.5) };
		ItemStack[] farLoot = { new ItemStack(Items.GUNPOWDER), new ItemStack(Items.SPIDER_EYE), new ItemStack(Items.FEATHER) };
		collect(kit, chest, far, farLoot);
		kit.idle(50);

		kit.show(filtered);
		kit.idle(10);
		scene.world().createEntity(level -> {
			ItemFrame frame = new ItemFrame(level, kit.at(3, 1, 3), Direction.NORTH);
			frame.setItem(new ItemStack(Items.COBBLESTONE), false);
			return frame;
		});
		kit.idle(15);
		kit.key(170, kit.top(filtered));
		kit.idle(10);
		kit.wandSneak(flower);
		kit.idle(40);
		ElementLink<EntityElement> cobble = kit.item(new Vec3(1.5, 1.2, 2.0), Vec3.ZERO, new ItemStack(Items.COBBLESTONE));
		ElementLink<EntityElement> dirt = kit.item(new Vec3(6.5, 1.2, 2.0), Vec3.ZERO, new ItemStack(Items.DIRT));
		kit.idle(20);
		kit.take(cobble);
		kit.beam(new Vec3(1.5, 1.4, 2.0), kit.center(filtered), 0x7F7F7F);
		kit.idle(10);
		kit.take(dirt);
		kit.beam(new Vec3(6.5, 1.4, 2.0), kit.center(chest), 0x7F7F7F);
		kit.idle(70);

		kit.redstone(kit.at(4, 1, 5));
		kit.key(120, flowerTop);
		kit.idle(130);
	}

	private static void collect(FunctionalSceneKit kit, BlockPos chest, Vec3[] spots, ItemStack[] stacks) {
		List<ElementLink<EntityElement>> items = new ArrayList<>();
		for (int i = 0; i < spots.length; i++) {
			items.add(kit.item(spots[i].add(0, 0.5, 0), new Vec3(0, 0.1, 0), stacks[i]));
			kit.idle(4);
		}
		kit.idle(25);
		for (int i = 0; i < spots.length; i++) {
			kit.take(items.get(i));
			kit.beam(spots[i].add(0, 0.2, 0), kit.center(chest), 0x7F7F7F);
			kit.idle(8);
		}
	}

	static void rannuncarpus(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "rannuncarpus", 9);
		BlockPos flower = kit.at(4, 2, 4);
		BlockPos soil = kit.at(4, 1, 4);
		BlockPos filter = kit.at(4, 0, 4);
		BlockPos pool = kit.at(8, 1, 0);
		Vec3 flowerTop = kit.top(flower);
		BlockPos[] spots = { kit.at(1, 0, 1), kit.at(4, 0, 1), kit.at(7, 0, 2), kit.at(1, 0, 4), kit.at(7, 0, 5), kit.at(2, 0, 7), kit.at(5, 0, 7), kit.at(7, 0, 7) };
		kit.set(soil, Blocks.DIRT);
		kit.set(flower, BotaniaFlowerBlocks.rannuncarpus);

		kit.show(soil);
		kit.idle(10);
		kit.show(flower);
		kit.idle(15);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.change(filter, Blocks.SANDSTONE);
		for (BlockPos spot : spots) {
			kit.set(spot, Blocks.SANDSTONE);
		}
		kit.idle(10);
		kit.key(150, kit.center(soil));
		scene.overlay().showOutline(PonderPalette.GREEN, "rannuncarpus_filter", util.select().position(filter), 150);
		kit.idle(40);
		scene.overlay().showOutline(PonderPalette.BLUE, "rannuncarpus_spots", kit.positions(spots), 110);
		kit.idle(120);

		kit.key(140, flowerTop);
		kit.idle(10);
		ElementLink<EntityElement> stack = kit.item(new Vec3(5.5, 1.8, 3.0), new Vec3(0, 0.1, 0), new ItemStack(Items.COBBLESTONE, 8));
		kit.idle(30);
		for (int i = 0; i < 5; i++) {
			BlockPos target = spots[i].above();
			kit.beam(new Vec3(5.5, 1.3, 3.0), kit.center(target), 0xFFD400);
			kit.place(target, Blocks.COBBLESTONE);
			kit.particles(kit.center(target), ParticleTypes.CLOUD, 4, 0.8);
			kit.idle(10);
		}
		kit.idle(30);

		kit.pool(pool, 200000);
		kit.idle(10);
		kit.key(150, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		for (int i = 5; i < spots.length; i++) {
			BlockPos target = spots[i].above();
			kit.beam(new Vec3(5.5, 1.3, 3.0), kit.center(target), 0xFFD400);
			kit.place(target, Blocks.COBBLESTONE);
			kit.particles(kit.center(target), ParticleTypes.CLOUD, 4, 0.8);
			kit.poolMana(pool, 200000 - i);
			kit.idle(10);
		}
		kit.take(stack);
		kit.idle(80);

		kit.key(160, flowerTop);
		kit.idle(10);
		kit.wandSneak(flower);
		kit.idle(60);
		kit.redstone(kit.at(5, 2, 4));
		kit.idle(100);
	}

	static void spectranthemum(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "spectranthemum", 9);
		BlockPos flower = kit.at(2, 1, 4);
		BlockPos target = kit.at(7, 1, 4);
		BlockPos pool = kit.at(2, 1, 7);
		Vec3 flowerTop = kit.top(flower);
		kit.set(target, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH));

		kit.flower(flower, BotaniaFlowerBlocks.spectranthemum);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.show(target);
		kit.idle(15);
		kit.key(150, kit.top(target));
		kit.idle(20);
		kit.wand(flower);
		kit.idle(30);
		kit.wand(target);
		scene.overlay().showLine(PonderPalette.BLUE, kit.center(flower), kit.center(target), 60);
		kit.idle(100);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(120, flowerTop);
		kit.idle(10);
		Vec3[] spots = { new Vec3(1.5, 1.2, 3.2), new Vec3(3.4, 1.2, 5.0), new Vec3(1.2, 1.2, 5.3) };
		ItemStack[] stacks = { new ItemStack(Items.IRON_INGOT, 16), new ItemStack(Items.REDSTONE, 32), new ItemStack(Items.WHEAT, 8) };
		int mana = 300000;
		for (int i = 0; i < spots.length; i++) {
			ElementLink<EntityElement> item = kit.item(spots[i].add(0, 0.6, 0), new Vec3(0, 0.1, 0), stacks[i]);
			kit.idle(25);
			kit.take(item);
			kit.beam(spots[i].add(0, 0.2, 0), kit.top(target).add(0, 0.5, 0), 0x98BCFF);
			ElementLink<EntityElement> arrived = kit.item(kit.top(target).add(0, 0.5, 0), Vec3.ZERO, stacks[i].copy());
			kit.around(arrived, ParticleTypes.SMOKE, 8);
			mana -= 200;
			kit.poolMana(pool, mana);
			kit.idle(15);
		}
		kit.idle(30);

		kit.key(160, kit.top(pool));
		kit.feed(pool, flower);
		kit.idle(160);

		ElementLink<EntityElement> tablet = kit.item(new Vec3(2.5, 1.8, 3.0), new Vec3(0, 0.1, 0), new ItemStack(BotaniaItems.manaTablet));
		kit.idle(20);
		kit.key(130, flowerTop);
		kit.idle(60);
		kit.redstone(kit.at(2, 1, 5));
		kit.idle(70);
		kit.vanish(tablet);
		kit.idle(10);
	}

	static void daffomill(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "daffomill", 9);
		BlockPos flower = kit.at(1, 1, 4);
		BlockPos pool = kit.at(1, 1, 7);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.daffomill);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(110, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(80);

		kit.key(130, flowerTop);
		scene.overlay().showOutline(PonderPalette.GREEN, "daffomill_strip", util.select().fromTo(2, 1, 2, 8, 1, 6), 130);
		Vec3[] spots = { new Vec3(2.5, 1.2, 3.0), new Vec3(3.2, 1.2, 4.6), new Vec3(2.8, 1.2, 5.8), new Vec3(4.0, 1.2, 2.6) };
		ItemStack[] stacks = { new ItemStack(Items.WHEAT_SEEDS), new ItemStack(Items.APPLE), new ItemStack(Items.STICK), new ItemStack(Items.FEATHER) };
		blow(kit, flower, spots, stacks, new Vec3(0.05, 0, 0), Direction.EAST, 90);
		kit.idle(20);

		kit.key(130, flowerTop);
		kit.idle(10);
		kit.wandSneak(flower);
		kit.idle(40);
		Vec3[] southSpots = { new Vec3(0.6, 1.2, 5.5), new Vec3(1.5, 1.2, 6.2), new Vec3(2.4, 1.2, 5.2) };
		ItemStack[] southStacks = { new ItemStack(Items.CARROT), new ItemStack(Items.BONE), new ItemStack(Items.PAPER) };
		blow(kit, flower, southSpots, southStacks, new Vec3(0, 0, 0.05), Direction.SOUTH, 70);
		kit.idle(20);

		kit.redstone(kit.at(0, 1, 4));
		kit.key(120, flowerTop);
		List<ElementLink<EntityElement>> still = new ArrayList<>();
		still.add(kit.item(new Vec3(3.0, 1.6, 4.5), new Vec3(0, 0.1, 0), new ItemStack(Items.EMERALD)));
		still.add(kit.item(new Vec3(4.5, 1.6, 3.5), new Vec3(0, 0.1, 0), new ItemStack(Items.DIAMOND)));
		kit.idle(130);
		for (ElementLink<EntityElement> item : still) {
			kit.vanish(item);
		}
		kit.idle(10);
	}

	private static void blow(FunctionalSceneKit kit, BlockPos flower, Vec3[] spots, ItemStack[] stacks, Vec3 push, Direction direction, int ticks) {
		List<ElementLink<EntityElement>> items = new ArrayList<>();
		for (int i = 0; i < spots.length; i++) {
			items.add(kit.item(spots[i], Vec3.ZERO, stacks[i]));
		}
		kit.idle(15);
		ParticleEmitter wind = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.25F + (float) Math.random() * 0.15F, 0.05F, 0.05F, 0.05F, 1F),
				x + Math.random(), y + Math.random() * 0.5, z + Math.random(), direction.getStepX() * 0.15, 0, direction.getStepZ() * 0.15);
		Vec3 origin = Vec3.atLowerCornerOf(flower).add(0, 0.2, 0);
		for (int tick = 0; tick < ticks; tick++) {
			for (ElementLink<EntityElement> item : items) {
				kit.push(item, push);
			}
			if (tick % 2 == 0) {
				kit.scene.effects().emitParticles(origin, wind, 1, 1);
			}
			kit.idle(1);
		}
		for (ElementLink<EntityElement> item : items) {
			kit.vanish(item);
		}
	}

	static void labellia(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "labellia", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 6);
		Vec3 flowerTop = kit.top(flower);
		Vec3 sheepPos = new Vec3(4.8, 1, 3.5);
		Vec3 swordPos = new Vec3(2.2, 1.1, 4.6);

		kit.flower(flower, BotaniaFlowerBlocks.labellia);
		kit.pool(pool, 300000);
		ElementLink<EntityElement> sheep = kit.mob(EntityType.SHEEP, sheepPos, 90);
		ElementLink<EntityElement> sword = kit.item(swordPos, Vec3.ZERO, new ItemStack(Items.IRON_SWORD));
		kit.idle(15);
		kit.say(90, flowerTop);
		kit.idle(100);

		ItemStack tag = new ItemStack(Items.NAME_TAG);
		tag.set(DataComponents.CUSTOM_NAME, Component.literal("Bob"));
		kit.key(120, flowerTop);
		kit.icon(flowerTop.add(0, 1.2, 0), tag, 50);
		kit.idle(40);
		ElementLink<EntityElement> thrown = kit.item(new Vec3(3.5, 2.8, 1.2), new Vec3(0, 0.12, 0.09), tag);
		kit.idle(30);
		kit.moveTo(thrown, new Vec3(3.5, 1.1, 3.5));
		kit.idle(50);

		kit.key(150, sheepPos.add(0, 1.3, 0));
		kit.area(flower, 2, 0, 0, 150, PonderPalette.GREEN);
		kit.idle(10);
		kit.take(thrown);
		kit.sparkle(flower, 0xFFEE83, 10);
		scene.world().modifyEntity(sheep, entity -> {
			entity.setCustomName(Component.literal("Bob"));
			entity.setCustomNameVisible(true);
			if (entity instanceof Mob mob) {
				mob.setPersistenceRequired();
			}
		});
		kit.around(sheep, ParticleTypes.HAPPY_VILLAGER, 10);
		kit.around(sword, ParticleTypes.INSTANT_EFFECT, 6);
		kit.idle(150);

		kit.key(120, kit.top(pool));
		kit.feed(pool, flower);
		kit.poolMana(pool, 299500);
		kit.idle(30);
		kit.redstone(kit.at(3, 1, 4));
		kit.idle(90);
	}

	static void pollidisiac(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "pollidisiac", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 6);
		Vec3 flowerTop = kit.top(flower);
		Vec3 cowA = new Vec3(1.5, 1, 1.5);
		Vec3 cowB = new Vec3(5.0, 1, 1.5);

		kit.flower(flower, BotaniaFlowerBlocks.pollidisiac);
		ElementLink<EntityElement> a = kit.mob(EntityType.COW, cowA, -90);
		ElementLink<EntityElement> b = kit.mob(EntityType.COW, cowB, 90);
		kit.idle(10);
		kit.say(80, flowerTop);
		kit.idle(90);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		kit.key(150, flowerTop);
		kit.idle(10);
		ElementLink<EntityElement> wheatA = kit.item(new Vec3(2.5, 1.8, 4.5), new Vec3(0, 0.1, 0), new ItemStack(Items.WHEAT));
		ElementLink<EntityElement> wheatB = kit.item(new Vec3(4.5, 1.8, 4.5), new Vec3(0, 0.1, 0), new ItemStack(Items.WHEAT));
		kit.idle(30);
		kit.take(wheatA);
		kit.beam(new Vec3(2.5, 1.3, 4.5), cowA.add(0, 1, 0), 0xCF4919);
		kit.idle(5);
		kit.take(wheatB);
		kit.beam(new Vec3(4.5, 1.3, 4.5), cowB.add(0, 1, 0), 0xCF4919);
		kit.poolMana(pool, 299976);
		Vec3 meetA = new Vec3(2.7, 1, 1.5);
		Vec3 meetB = new Vec3(3.8, 1, 1.5);
		for (int i = 1; i <= 40; i++) {
			double t = i / 40.0;
			kit.moveTo(a, cowA.lerp(meetA, t));
			kit.moveTo(b, cowB.lerp(meetB, t));
			if (i % 8 == 0) {
				kit.around(a, ParticleTypes.HEART, 1);
				kit.around(b, ParticleTypes.HEART, 1);
			}
			kit.idle(1);
		}
		kit.idle(20);
		ElementLink<EntityElement> baby = kit.mob(EntityType.COW, new Vec3(3.25, 1, 2.6), 0, entity -> ((Cow) entity).setBaby(true));
		kit.around(baby, ParticleTypes.HEART, 4);
		kit.idle(50);

		kit.key(150, flowerTop);
		kit.idle(10);
		kit.wandSneak(flower);
		kit.idle(40);
		ElementLink<EntityElement> wheatC = kit.item(new Vec3(3.5, 1.8, 4.6), new Vec3(0, 0.1, 0), new ItemStack(Items.WHEAT));
		kit.idle(30);
		kit.take(wheatC);
		kit.beam(new Vec3(3.5, 1.3, 4.6), new Vec3(3.25, 1.5, 2.6), 0xCF4919);
		kit.around(baby, ParticleTypes.HAPPY_VILLAGER, 8);
		kit.idle(60);

		ElementLink<EntityElement> mooshroom = kit.mob(EntityType.MOOSHROOM, new Vec3(5.2, 1, 4.0), 180, entity -> ((MushroomCow) entity).setVariant(MushroomCow.MushroomType.BROWN));
		kit.idle(15);
		kit.key(140, new Vec3(5.2, 2.2, 4.0));
		ElementLink<EntityElement> dandelion = kit.item(new Vec3(4.2, 1.8, 5.2), new Vec3(0, 0.1, 0), new ItemStack(Items.DANDELION));
		kit.idle(30);
		kit.take(dandelion);
		kit.around(mooshroom, ParticleTypes.EFFECT, 8);
		kit.idle(110);
	}

	static void loonium(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "loonium", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos pool = kit.at(8, 1, 8);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.loonium);
		kit.say(80, flowerTop);
		kit.idle(90);

		kit.pool(pool, 900000);
		kit.idle(10);
		kit.key(110, kit.top(pool));
		kit.idle(20);
		kit.bind(flower, pool);
		kit.idle(60);

		kit.key(150, flowerTop);
		kit.idle(10);
		Vec3[] drops = { new Vec3(2.5, 1.3, 3.0), new Vec3(6.0, 1.3, 5.5), new Vec3(3.5, 1.3, 6.5), new Vec3(5.5, 1.3, 2.2) };
		ItemStack[] loot = { new ItemStack(Items.SADDLE), new ItemStack(Items.GOLDEN_APPLE), new ItemStack(Items.NAME_TAG), new ItemStack(Items.MUSIC_DISC_13) };
		List<ElementLink<EntityElement>> items = new ArrayList<>();
		int mana = 900000;
		for (int i = 0; i < drops.length; i++) {
			kit.feed(pool, flower);
			mana -= 35000;
			kit.poolMana(pool, mana);
			kit.beam(flowerTop, drops[i], 0xC29D62);
			items.add(kit.item(drops[i], new Vec3(0, 0.15, 0), loot[i]));
			kit.sparkle(drops[i], 0xC29D62, 8);
			kit.idle(20);
		}
		kit.idle(40);

		BlockPos[] bricks = { kit.at(1, 1, 1), kit.at(2, 1, 1), kit.at(1, 1, 2), kit.at(7, 1, 1), kit.at(6, 1, 1), kit.at(1, 1, 7), kit.at(1, 1, 6), kit.at(2, 1, 7) };
		for (int i = 0; i < bricks.length; i++) {
			kit.set(bricks[i], i % 3 == 0 ? Blocks.MOSSY_STONE_BRICKS : i % 3 == 1 ? Blocks.CRACKED_STONE_BRICKS : Blocks.STONE_BRICKS);
		}
		kit.show(kit.positions(bricks));
		for (ElementLink<EntityElement> item : items) {
			kit.vanish(item);
		}
		kit.idle(15);
		kit.key(130, flowerTop);
		kit.idle(20);
		kit.feed(pool, flower);
		mana -= 35000;
		kit.poolMana(pool, mana);
		ElementLink<EntityElement> pearl = kit.item(new Vec3(5.5, 1.3, 3.5), new Vec3(0, 0.15, 0), new ItemStack(Items.ENDER_PEARL));
		kit.sparkle(new Vec3(5.5, 1.3, 3.5), 0xC29D62, 8);
		kit.idle(30);
		kit.feed(pool, flower);
		mana -= 35000;
		kit.poolMana(pool, mana);
		ElementLink<EntityElement> book = kit.item(new Vec3(3.0, 1.3, 5.5), new Vec3(0, 0.15, 0), new ItemStack(Items.ENCHANTED_BOOK));
		kit.sparkle(new Vec3(3.0, 1.3, 5.5), 0xC29D62, 8);
		kit.idle(60);
		kit.vanish(pearl);
		kit.vanish(book);

		kit.key(150, flowerTop);
		kit.idle(20);
		Vec3 spawn = new Vec3(6.5, 1, 3.5);
		kit.feed(pool, flower);
		mana -= 35000;
		kit.poolMana(pool, mana);
		ElementLink<EntityElement> zombie = kit.mob(EntityType.ZOMBIE, spawn, 90, entity -> ((Mob) entity).setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_APPLE)));
		kit.around(zombie, ParticleTypes.POOF, 15);
		kit.idle(120);

		kit.key(170, spawn.add(0, 2, 0));
		int[] colors = { 0xCD5CAB, 0xFF9900, 0x9146F0, 0xFFC700 };
		for (int tick = 0; tick < 100; tick += 5) {
			kit.effect(zombie, colors[(tick / 5) % colors.length], 3);
			kit.idle(5);
		}
		for (int hit = 0; hit < 3; hit++) {
			kit.hurt(zombie);
			kit.idle(12);
		}
		kit.kill(zombie);
		kit.item(spawn.add(0, 0.3, 0), new Vec3(0, 0.2, 0), new ItemStack(Items.GOLDEN_APPLE));
		kit.idle(30);
		kit.redstone(kit.at(4, 1, 5));
		kit.idle(40);
	}

	static void solegnolia(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "solegnolia", 9);
		BlockPos flower = kit.at(4, 1, 4);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.solegnolia);
		kit.say(90, flowerTop);
		kit.ring(flower, 5, 0xC99C4D);
		kit.idle(100);

		Vec3 holder = new Vec3(8.3, 1, 8.3);
		ElementLink<EntityElement> stand = kit.mob(EntityType.ARMOR_STAND, holder, 135, entity -> {
			ArmorStand armorStand = (ArmorStand) entity;
			armorStand.setShowArms(true);
			armorStand.setNoBasePlate(true);
			armorStand.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BotaniaItems.magnetRing));
		});
		Vec3[] inside = { new Vec3(3.0, 1.2, 3.2), new Vec3(5.6, 1.2, 4.0), new Vec3(4.2, 1.2, 6.0), new Vec3(6.6, 1.2, 6.2) };
		Vec3[] outside = { new Vec3(0.4, 1.2, 8.4), new Vec3(8.6, 1.2, 0.5) };
		ItemStack[] stacks = { new ItemStack(Items.WHEAT), new ItemStack(Items.CARROT), new ItemStack(Items.POTATO), new ItemStack(Items.BEETROOT), new ItemStack(Items.EGG), new ItemStack(Items.FEATHER) };
		List<ElementLink<EntityElement>> kept = new ArrayList<>();
		for (int i = 0; i < inside.length; i++) {
			kept.add(kit.item(inside[i], Vec3.ZERO, stacks[i]));
		}
		List<ElementLink<EntityElement>> pulled = new ArrayList<>();
		for (int i = 0; i < outside.length; i++) {
			pulled.add(kit.item(outside[i], Vec3.ZERO, stacks[inside.length + i]));
		}
		kit.icon(holder.add(0, 2.2, 0), new ItemStack(BotaniaItems.magnetRing), 60);
		kit.idle(15);
		kit.key(150, flowerTop);
		kit.idle(10);
		Vec3 target = holder.add(0, 0.8, 0);
		for (int i = 1; i <= 40; i++) {
			double t = i / 40.0;
			for (int p = 0; p < outside.length; p++) {
				kit.moveTo(pulled.get(p), outside[p].lerp(target, t));
			}
			if (i % 10 == 0) {
				kit.ring(flower, 5, 0xC99C4D);
			}
			kit.idle(1);
		}
		for (ElementLink<EntityElement> item : pulled) {
			kit.take(item);
		}
		kit.idle(70);

		kit.key(150, flowerTop);
		Vec3 insideSpot = new Vec3(5.6, 1, 2.4);
		kit.face(stand, FunctionalSceneKit.yaw(holder, insideSpot));
		kit.walk(stand, holder, insideSpot, 40);
		kit.ring(flower, 5, 0xC99C4D);
		kit.idle(100);

		kit.key(130, flowerTop);
		for (int i = 0; i < 3; i++) {
			kit.ring(flower, 1, 0x55AAFF);
			kit.idle(15);
		}
		kit.redstone(kit.at(4, 1, 5));
		kit.idle(80);
		for (ElementLink<EntityElement> item : kept) {
			kit.vanish(item);
		}
		kit.idle(10);
	}
}
