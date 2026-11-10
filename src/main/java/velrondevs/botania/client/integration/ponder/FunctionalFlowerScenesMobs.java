package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.client.PonderPalette;
import net.createmod.ponder.api.client.element.ElementLink;
import net.createmod.ponder.api.client.element.EntityElement;
import net.createmod.ponder.api.client.scene.SceneBuilder;
import net.createmod.ponder.api.client.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.registry.BotaniaFlowerBlocks;

final class FunctionalFlowerScenesMobs {
	private static final int POISON = 0x87A363;
	private static final int SLOWNESS = 0x8BAFE0;
	private static final int REGENERATION = 0xCD5CAB;

	private FunctionalFlowerScenesMobs() {}

	static void bellethorn(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "bellethorn", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 6);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.bellethorn);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		Vec3 start = new Vec3(0.5, 1, 0.5);
		Vec3 stop = new Vec3(1.7, 1, 1.7);
		ElementLink<EntityElement> zombie = kit.mob(EntityType.ZOMBIE, start, FunctionalSceneKit.yaw(start, kit.center(flower)));
		kit.idle(10);
		kit.key(130, flowerTop);
		kit.walk(zombie, start, stop, 25);
		int mana = 300000;
		for (int hit = 0; hit < 4; hit++) {
			kit.beam(flowerTop, stop.add(0, 1, 0), 0xBA3421);
			kit.hurt(zombie);
			mana -= 24;
			kit.poolMana(pool, mana);
			kit.idle(11);
		}
		kit.beam(flowerTop, stop.add(0, 1, 0), 0xBA3421);
		kit.kill(zombie);
		kit.item(stop.add(0, 0.3, 0), new Vec3(0, 0.15, 0), new ItemStack(Items.ROTTEN_FLESH));
		kit.idle(40);

		kit.key(110, flowerTop);
		kit.area(flower, 3, 0, 3, 110, PonderPalette.GREEN);
		kit.area(flower, 1, 0, 1, 110, PonderPalette.BLUE);
		kit.idle(120);

		Vec3 cowPos = new Vec3(5.5, 1, 1.5);
		ElementLink<EntityElement> cow = kit.mob(EntityType.COW, cowPos, 90);
		kit.idle(15);
		kit.key(140, cowPos.add(0, 1.2, 0));
		for (int hit = 0; hit < 2; hit++) {
			kit.beam(flowerTop, cowPos.add(0, 0.8, 0), 0xBA3421);
			kit.hurt(cow);
			kit.idle(11);
		}
		kit.redstone(kit.at(3, 1, 4));
		kit.idle(130);
	}

	static void dreadthorn(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "dreadthorn", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 6);
		Vec3 flowerTop = kit.top(flower);
		Vec3[] adults = { new Vec3(1.5, 1, 1.5), new Vec3(5.2, 1, 2.0) };
		Vec3 babyPos = new Vec3(1.8, 1, 5.2);
		Vec3 zombiePos = new Vec3(5.5, 1, 4.5);

		kit.flower(flower, BotaniaFlowerBlocks.dreadthorn);
		ElementLink<EntityElement> cowA = kit.mob(EntityType.COW, adults[0], 30);
		ElementLink<EntityElement> cowB = kit.mob(EntityType.COW, adults[1], 120);
		ElementLink<EntityElement> baby = kit.mob(EntityType.COW, babyPos, 200, entity -> ((Cow) entity).setBaby(true));
		ElementLink<EntityElement> zombie = kit.mob(EntityType.ZOMBIE, zombiePos, 90);
		kit.idle(10);
		kit.say(110, flowerTop);
		kit.idle(120);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		kit.key(130, flowerTop);
		kit.idle(15);
		int mana = 300000;
		for (int hit = 0; hit < 2; hit++) {
			kit.beam(flowerTop, adults[0].add(0, 0.8, 0), 0x260B45);
			kit.beam(flowerTop, adults[1].add(0, 0.8, 0), 0x260B45);
			kit.hurt(cowA);
			kit.hurt(cowB);
			mana -= 60;
			kit.poolMana(pool, mana);
			kit.idle(11);
		}
		kit.beam(flowerTop, adults[0].add(0, 0.8, 0), 0x260B45);
		kit.beam(flowerTop, adults[1].add(0, 0.8, 0), 0x260B45);
		scene.world().modifyEntity(cowB, entity -> {
			((LivingEntity) entity).animateHurt(0);
			((LivingEntity) entity).setHealth(0);
		});
		kit.kill(cowA);
		kit.vanish(cowB);
		kit.item(adults[0].add(0, 0.3, 0), new Vec3(0, 0.15, 0), new ItemStack(Items.BEEF));
		kit.item(adults[0].add(0, 0.3, 0), new Vec3(0.05, 0.15, 0), new ItemStack(Items.LEATHER));
		kit.item(adults[1].add(0, 0.3, 0), new Vec3(0, 0.15, 0), new ItemStack(Items.BEEF));
		kit.idle(20);
		kit.sparkle(babyPos.add(0, 0.6, 0), 0x55FF55, 8);
		kit.idle(60);

		kit.key(130, flowerTop);
		kit.area(flower, 3, 0, 3, 130, PonderPalette.GREEN);
		kit.idle(40);
		kit.redstone(kit.at(3, 1, 4));
		kit.idle(100);
	}

	static void hyacidus(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "hyacidus", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 6);
		Vec3 flowerTop = kit.top(flower);
		Vec3 pigPos = new Vec3(1.5, 1, 1.5);
		Vec3 sheepPos = new Vec3(5.3, 1, 1.8);
		Vec3 zombiePos = new Vec3(1.5, 1, 5.3);

		kit.flower(flower, BotaniaFlowerBlocks.hyacidus);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		ElementLink<EntityElement> pig = kit.mob(EntityType.PIG, pigPos, 30);
		ElementLink<EntityElement> sheep = kit.mob(EntityType.SHEEP, sheepPos, 120);
		kit.idle(15);
		kit.key(150, flowerTop);
		kit.beam(flowerTop, pigPos.add(0, 0.6, 0), 0x8B438F);
		kit.beam(flowerTop, sheepPos.add(0, 0.8, 0), 0x8B438F);
		for (int tick = 0; tick < 150; tick += 5) {
			kit.effect(pig, POISON, 2);
			kit.effect(sheep, POISON, 2);
			if (tick % 25 == 0) {
				kit.hurt(pig);
				kit.hurt(sheep);
			}
			if (tick == 60) {
				kit.feed(pool, flower);
				kit.poolMana(pool, 299960);
				kit.beam(flowerTop, pigPos.add(0, 0.6, 0), 0x8B438F);
				kit.beam(flowerTop, sheepPos.add(0, 0.8, 0), 0x8B438F);
				tick += 5;
			} else {
				kit.idle(5);
			}
		}

		ElementLink<EntityElement> zombie = kit.mob(EntityType.ZOMBIE, zombiePos, 160);
		kit.idle(15);
		kit.key(140, zombiePos.add(0, 2, 0));
		kit.area(flower, 3, 0, 3, 140, PonderPalette.GREEN);
		for (int tick = 0; tick < 80; tick += 5) {
			kit.effect(pig, POISON, 2);
			kit.effect(sheep, POISON, 2);
			kit.idle(5);
		}
		kit.redstone(kit.at(3, 1, 4));
		kit.idle(70);
		kit.vanish(zombie);
		kit.idle(10);
	}

	static void medumone(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "medumone", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 1);
		Vec3 flowerTop = kit.top(flower);
		Vec3 start = new Vec3(0.5, 1, 5.5);
		Vec3 middle = new Vec3(2.6, 1, 5.5);
		Vec3 end = new Vec3(6.5, 1, 5.5);

		kit.flower(flower, BotaniaFlowerBlocks.medumone);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.pool(pool, 0);
		ElementLink<EntityElement> zombie = kit.mob(EntityType.ZOMBIE, start, -90);
		kit.idle(10);
		kit.key(140, kit.top(pool));
		kit.walk(zombie, start, middle, 60);
		kit.poolMana(pool, 300000);
		kit.feed(pool, flower);
		kit.idle(60);

		kit.key(120, flowerTop);
		kit.beam(flowerTop, middle.add(0, 1, 0), 0x3D2204);
		for (int tick = 0; tick < 120; tick += 4) {
			kit.effect(zombie, SLOWNESS, 3);
			kit.idle(4);
		}

		kit.redstone(kit.at(3, 1, 4));
		kit.key(110, flowerTop);
		kit.idle(20);
		kit.walk(zombie, middle, end, 70);
		kit.idle(40);
	}

	static void heiseiDream(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "heisei_dream", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos pool = kit.at(8, 1, 8);
		Vec3 flowerTop = kit.top(flower);
		Vec3 a = new Vec3(2.0, 1, 2.0);
		Vec3 b = new Vec3(6.5, 1, 2.5);
		Vec3 c = new Vec3(2.0, 1, 6.5);

		kit.flower(flower, BotaniaFlowerBlocks.heiseiDream);
		ElementLink<EntityElement> zombieA = kit.mob(EntityType.ZOMBIE, a, FunctionalSceneKit.yaw(a, kit.center(flower)));
		ElementLink<EntityElement> skeleton = kit.mob(EntityType.SKELETON, b, FunctionalSceneKit.yaw(b, kit.center(flower)));
		ElementLink<EntityElement> zombieC = kit.mob(EntityType.ZOMBIE, c, FunctionalSceneKit.yaw(c, kit.center(flower)));
		kit.idle(10);
		kit.say(80, flowerTop);
		kit.idle(90);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		kit.key(140, flowerTop);
		kit.idle(10);
		kit.beam(flowerTop, a.add(0, 1.6, 0), 0xFF219D);
		kit.sparkle(a.add(0, 1.8, 0), 0xFF219D, 10);
		kit.poolMana(pool, 299900);
		kit.idle(10);
		kit.face(zombieA, FunctionalSceneKit.yaw(a, b));
		scene.overlay().showLine(PonderPalette.RED, a.add(0, 1.5, 0), b.add(0, 1.5, 0), 40);
		kit.idle(20);
		Vec3 meet = new Vec3(5.3, 1, 2.4);
		kit.walk(zombieA, a, meet, 30);
		kit.face(skeleton, FunctionalSceneKit.yaw(b, meet));
		for (int round = 0; round < 3; round++) {
			kit.hurt(skeleton);
			kit.idle(12);
			kit.hurt(zombieA);
			kit.idle(12);
		}
		kit.beam(flowerTop, c.add(0, 1.6, 0), 0xFF219D);
		kit.sparkle(c.add(0, 1.8, 0), 0xFF219D, 10);
		kit.face(zombieC, FunctionalSceneKit.yaw(c, b));
		kit.idle(10);
		kit.kill(skeleton);
		kit.item(b.add(0, 0.3, 0), new Vec3(0, 0.15, 0), new ItemStack(Items.BONE));
		kit.idle(30);

		kit.key(120, flowerTop);
		kit.area(flower, 5, 0, 5, 120, PonderPalette.GREEN);
		kit.idle(130);
	}

	static void tigerseye(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "tigerseye", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos pool = kit.at(8, 1, 8);
		Vec3 flowerTop = kit.top(flower);
		Vec3 start = new Vec3(0.5, 1, 0.5);
		Vec3 close = new Vec3(2.5, 1, 2.5);
		Vec3 away = new Vec3(0.5, 1, 7.5);

		kit.flower(flower, BotaniaFlowerBlocks.tigerseye);
		kit.say(80, flowerTop);
		kit.idle(90);

		ElementLink<EntityElement> creeper = kit.mob(EntityType.CREEPER, start, FunctionalSceneKit.yaw(start, close));
		kit.idle(10);
		kit.key(160, flowerTop);
		kit.walk(creeper, start, close, 40);
		for (int attempt = 0; attempt < 3; attempt++) {
			scene.world().modifyEntity(creeper, entity -> ((Creeper) entity).setSwellDir(1));
			kit.idle(14);
			scene.world().modifyEntity(creeper, entity -> ((Creeper) entity).setSwellDir(-1));
			kit.sparkle(flower, 0xB1A618, 8);
			kit.idle(24);
		}
		kit.idle(20);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(160, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.beam(flowerTop, close.add(0, 1, 0), 0xB1A618);
		kit.poolMana(pool, 299930);
		for (int i = 0; i < 4; i++) {
			kit.sparkle(close.add(0, 0.6, 0), 0xB1A618, 14);
			kit.idle(3);
		}
		kit.idle(30);
		kit.face(creeper, FunctionalSceneKit.yaw(close, away));
		kit.idle(10);
		kit.walk(creeper, close, away, 50);
		kit.idle(30);

		kit.key(110, flowerTop);
		kit.area(flower, 10, 0, 4, 110, PonderPalette.GREEN);
		kit.idle(120);
	}

	static void fallenKanade(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "fallen_kanade", 7);
		BlockPos flower = kit.at(3, 1, 3);
		BlockPos pool = kit.at(6, 1, 6);
		Vec3 flowerTop = kit.top(flower);
		Vec3 wolfPos = new Vec3(2.0, 1, 2.2);
		Vec3 cowPos = new Vec3(4.6, 1, 4.4);
		Vec3 zombiePos = new Vec3(0.5, 1, 5.5);

		kit.flower(flower, BotaniaFlowerBlocks.fallenKanade);
		ElementLink<EntityElement> wolf = kit.mob(EntityType.WOLF, wolfPos, 140, entity -> {
			((Wolf) entity).setTame(true, true);
			((Wolf) entity).setHealth(10);
		});
		kit.idle(10);
		kit.say(90, flowerTop);
		kit.idle(100);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(100, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(70);

		kit.key(150, flowerTop);
		kit.beam(flowerTop, wolfPos.add(0, 0.6, 0), 0xFFFF00);
		kit.poolMana(pool, 299880);
		for (int tick = 0; tick < 150; tick += 5) {
			kit.effect(wolf, REGENERATION, 2);
			if (tick % 15 == 0) {
				float health = Math.min(40, 10 + tick / 5F);
				scene.world().modifyEntity(wolf, entity -> ((Wolf) entity).setHealth(health));
				kit.around(wolf, ParticleTypes.HEART, 1);
			}
			kit.idle(5);
		}

		ElementLink<EntityElement> cow = kit.mob(EntityType.COW, cowPos, 220);
		ElementLink<EntityElement> zombie = kit.mob(EntityType.ZOMBIE, zombiePos, 60);
		kit.idle(10);
		kit.key(150, flowerTop);
		kit.area(flower, 2, 0, 2, 150, PonderPalette.GREEN);
		for (int tick = 0; tick < 150; tick += 10) {
			kit.effect(wolf, REGENERATION, 2);
			kit.idle(10);
		}
		kit.vanish(cow);
		kit.vanish(zombie);
		kit.idle(10);
	}

	static void tangleberrie(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "tangleberrie", 13);
		BlockPos flower = kit.at(6, 1, 6);
		BlockPos pool = kit.at(10, 1, 10);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.tangleberrie);
		kit.say(80, flowerTop);
		kit.idle(90);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(110, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(80);

		Vec3 cowStart = new Vec3(5.5, 1, 6.5);
		Vec3 cowEdge = new Vec3(0.3, 1, 6.5);
		Vec3 sheepStart = new Vec3(6.5, 1, 7.5);
		Vec3 sheepEdge = new Vec3(6.5, 1, 12.7);
		ElementLink<EntityElement> cow = kit.mob(EntityType.COW, cowStart, 90);
		ElementLink<EntityElement> sheep = kit.mob(EntityType.SHEEP, sheepStart, 0);
		kit.idle(10);
		kit.key(160, flowerTop);
		for (int round = 0; round < 2; round++) {
			for (int i = 1; i <= 50; i++) {
				double t = i / 50.0;
				kit.moveTo(cow, cowStart.lerp(cowEdge, t));
				kit.moveTo(sheep, sheepStart.lerp(sheepEdge, t));
				if (i % 10 == 0) {
					kit.ring(flower, 6, 0x4B797C);
				}
				kit.idle(1);
			}
			kit.around(cow, ParticleTypes.ENCHANTED_HIT, 6);
			kit.around(sheep, ParticleTypes.ENCHANTED_HIT, 6);
			kit.ring(flower, 6, 0x4B797C);
			Vec3 cowBack = new Vec3(1.5, 1, 6.5);
			Vec3 sheepBack = new Vec3(6.5, 1, 11.5);
			for (int i = 1; i <= 15; i++) {
				double t = i / 15.0;
				kit.moveTo(cow, cowEdge.lerp(cowBack, t));
				kit.moveTo(sheep, sheepEdge.lerp(sheepBack, t));
				kit.idle(1);
			}
			cowEdge = new Vec3(0.3, 1, 6.5);
			cowStart = cowBack;
			sheepStart = sheepBack;
			kit.idle(10);
		}
		kit.idle(30);

		kit.key(130, flowerTop);
		for (int i = 0; i < 6; i++) {
			kit.ring(flower, 6, 0x4B797C);
			kit.ring(flower, 2, 0x55AAFF);
			kit.idle(20);
		}
		kit.idle(20);
	}

	static void jiyuulia(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "jiyuulia", 11);
		BlockPos flower = kit.at(5, 1, 5);
		BlockPos pool = kit.at(9, 1, 9);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.jiyuulia);
		Vec3[] starts = { new Vec3(3.8, 1, 5.5), new Vec3(6.7, 1, 4.2), new Vec3(5.5, 1, 7.0) };
		Vec3[] ends = { new Vec3(0.4, 1, 5.5), new Vec3(10.6, 1, 0.6), new Vec3(5.5, 1, 10.6) };
		EntityType<?>[] types = { EntityType.ZOMBIE, EntityType.SKELETON, EntityType.SPIDER };
		@SuppressWarnings("unchecked")
		ElementLink<EntityElement>[] mobs = new ElementLink[3];
		for (int i = 0; i < 3; i++) {
			mobs[i] = kit.mob(types[i], starts[i], FunctionalSceneKit.yaw(starts[i], kit.center(flower)));
		}
		kit.idle(10);
		kit.say(80, flowerTop);
		kit.idle(90);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(110, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(60);

		kit.key(140, flowerTop);
		kit.idle(10);
		for (int i = 1; i <= 50; i++) {
			double t = i / 50.0;
			double eased = t * t;
			for (int m = 0; m < 3; m++) {
				kit.moveTo(mobs[m], starts[m].lerp(ends[m], eased));
			}
			if (i % 10 == 0) {
				kit.ring(flower, 3 + i / 10.0, 0xBD9ACA);
			}
			kit.idle(1);
		}
		kit.idle(60);

		kit.key(120, flowerTop);
		for (int i = 0; i < 5; i++) {
			kit.ring(flower, 3, 0x55AAFF);
			kit.idle(20);
		}
		kit.idle(30);
	}

	static void vinculotus(SceneBuilder scene, SceneBuildingUtil util) {
		FunctionalSceneKit kit = new FunctionalSceneKit(scene, util, "vinculotus", 9);
		BlockPos flower = kit.at(4, 1, 4);
		BlockPos pool = kit.at(8, 1, 8);
		Vec3 flowerTop = kit.top(flower);

		kit.flower(flower, BotaniaFlowerBlocks.vinculotus);
		kit.say(80, flowerTop);
		kit.idle(90);

		kit.pool(pool, 300000);
		kit.idle(10);
		kit.key(120, kit.top(pool));
		kit.idle(20);
		kit.feed(pool, flower);
		kit.idle(80);

		Vec3[] starts = { new Vec3(0.8, 1, 0.8), new Vec3(8.0, 1, 1.0) };
		Vec3[] landings = { new Vec3(3.2, 1, 4.6), new Vec3(5.4, 1, 3.6) };
		@SuppressWarnings("unchecked")
		ElementLink<EntityElement>[] endermen = new ElementLink[2];
		for (int i = 0; i < 2; i++) {
			endermen[i] = kit.mob(EntityType.ENDERMAN, starts[i], FunctionalSceneKit.yaw(starts[i], kit.center(flower)));
		}
		kit.idle(20);
		kit.key(150, flowerTop);
		kit.idle(20);
		int mana = 300000;
		for (int i = 0; i < 2; i++) {
			kit.around(endermen[i], ParticleTypes.PORTAL, 40);
			kit.idle(10);
			kit.moveTo(endermen[i], landings[i]);
			kit.face(endermen[i], FunctionalSceneKit.yaw(landings[i], kit.center(flower)) + 180);
			kit.around(endermen[i], ParticleTypes.PORTAL, 40);
			kit.beam(flowerTop, landings[i].add(0, 1.5, 0), 0x0A6051);
			kit.sparkle(flower, 0x0A6051, 8);
			mana -= 50;
			kit.poolMana(pool, mana);
			kit.idle(40);
		}
		kit.idle(30);

		kit.redstone(kit.at(4, 1, 5));
		kit.key(130, flowerTop);
		kit.idle(140);
	}
}
