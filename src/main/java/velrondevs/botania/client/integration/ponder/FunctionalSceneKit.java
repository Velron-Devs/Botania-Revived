package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.api.math.Pointing;
import net.createmod.ponder.api.client.scene.ParticleEmitter;
import net.createmod.ponder.api.client.PonderPalette;
import net.createmod.ponder.api.client.element.ElementLink;
import net.createmod.ponder.api.client.element.EntityElement;
import net.createmod.ponder.api.client.scene.SceneBuilder;
import net.createmod.ponder.api.client.scene.SceneBuildingUtil;
import net.createmod.ponder.api.client.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.Consumer;

final class FunctionalSceneKit {
	final SceneBuilder scene;
	final SceneBuildingUtil util;
	final int size;
	private final PonderText text;
	private int outlines;

	FunctionalSceneKit(SceneBuilder scene, SceneBuildingUtil util, String flower, int size) {
		this.scene = scene;
		this.util = util;
		this.size = size;
		String id = PonderStringsFunctional.sceneId(flower);
		this.text = new PonderText(id);
		scene.title(id, PonderStrings.title(id));
		scene.configureBasePlate(0, 0, size);
		if (size >= 13) {
			scene.scaleSceneView(0.6F);
		} else if (size >= 11) {
			scene.scaleSceneView(0.7F);
		} else if (size >= 9) {
			scene.scaleSceneView(0.85F);
		}
		scene.showBasePlate();
		scene.idle(5);
	}

	BlockPos at(int x, int y, int z) {
		return util.grid().at(x, y, z);
	}

	Vec3 top(BlockPos pos) {
		return util.vector().topOf(pos);
	}

	Vec3 center(BlockPos pos) {
		return util.vector().centerOf(pos);
	}

	Vec3 feet(BlockPos pos) {
		return Vec3.atBottomCenterOf(pos);
	}

	void idle(int ticks) {
		scene.idle(ticks);
	}

	void set(BlockPos pos, BlockState state) {
		scene.world().setBlock(pos, state, false);
	}

	void set(BlockPos pos, Block block) {
		set(pos, block.defaultBlockState());
	}

	void change(BlockPos pos, BlockState state) {
		scene.world().setBlock(pos, state, true);
	}

	void change(BlockPos pos, Block block) {
		change(pos, block.defaultBlockState());
	}

	void show(BlockPos pos) {
		scene.world().showSection(util.select().position(pos), Direction.DOWN);
	}

	void show(Selection selection) {
		scene.world().showSection(selection, Direction.DOWN);
	}

	void place(BlockPos pos, BlockState state) {
		set(pos, state);
		show(pos);
	}

	void place(BlockPos pos, Block block) {
		place(pos, block.defaultBlockState());
	}

	void hide(BlockPos pos) {
		scene.world().hideSection(util.select().position(pos), Direction.UP);
	}

	Selection positions(BlockPos... positions) {
		Selection selection = util.select().position(positions[0]);
		for (int i = 1; i < positions.length; i++) {
			selection = selection.add(util.select().position(positions[i]));
		}
		return selection;
	}

	void say(int ticks, Vec3 at) {
		scene.overlay().showText(ticks).text(text.next()).pointAt(at).placeNearTarget();
	}

	void key(int ticks, Vec3 at) {
		scene.overlay().showText(ticks).text(text.next()).attachKeyFrame().pointAt(at).placeNearTarget();
	}

	void flower(BlockPos pos, Block block) {
		place(pos, block);
		scene.idle(10);
	}

	void pool(BlockPos pos, int mana) {
		place(pos, BotaniaBlocks.manaPool);
		poolMana(pos, mana);
	}

	void poolMana(BlockPos pos, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(pos), ManaPoolBlockEntity.class, tag -> {
			tag.putInt("mana", mana);
			tag.putInt("manaCap", ManaPoolBlockEntity.MAX_MANA);
		});
	}

	void feed(BlockPos pool, BlockPos flower) {
		SceneHelper.burst(scene, center(pool).add(0, 0.1, 0), center(flower), 8, SceneHelper.POOL_COLOR);
	}

	void beam(Vec3 from, Vec3 to, int color) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.25F, r, g, b, 1.5F), x, y, z, 0, 0, 0);
		int steps = Math.max(2, (int) (from.distanceTo(to) * 3));
		for (int i = 0; i <= steps; i++) {
			scene.effects().emitParticles(from.lerp(to, (double) i / steps), emitter, 1, 1);
		}
	}

	void wand(BlockPos pos) {
		scene.overlay().showControls(util.vector().blockSurface(pos, Direction.UP), Pointing.DOWN, 35).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
	}

	void wandSneak(BlockPos pos) {
		scene.overlay().showControls(util.vector().blockSurface(pos, Direction.UP), Pointing.DOWN, 35).rightClick().whileSneaking().withItem(new ItemStack(BotaniaItems.twigWand));
	}

	void bind(BlockPos flower, BlockPos pool) {
		wand(flower);
		scene.idle(25);
		wand(pool);
		scene.overlay().showLine(PonderPalette.GREEN, center(flower), center(pool), 50);
		scene.idle(25);
	}

	void icon(Vec3 at, ItemStack stack, int ticks) {
		scene.overlay().showControls(at, Pointing.DOWN, ticks).withItem(stack);
	}

	void area(BlockPos center, int radius, int down, int up, int ticks, PonderPalette color) {
		int x0 = Math.max(0, center.getX() - radius);
		int z0 = Math.max(0, center.getZ() - radius);
		int x1 = Math.min(size - 1, center.getX() + radius);
		int z1 = Math.min(size - 1, center.getZ() + radius);
		int y0 = Math.max(0, center.getY() - down);
		int y1 = center.getY() + up;
		scene.overlay().showOutline(color, "functional_area_" + outlines++, util.select().fromTo(x0, y0, z0, x1, y1, z1), ticks);
	}

	void area(BlockPos center, int radius, int ticks) {
		area(center, radius, 0, 0, ticks, PonderPalette.GREEN);
	}

	void ring(BlockPos center, double radius, int color) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.3F, r, g, b, 2F), x, y, z, 0, 0, 0);
		Vec3 c = center(center).add(0, -0.4, 0);
		int points = (int) (radius * 10);
		for (int i = 0; i < points; i++) {
			double angle = Math.PI * 2 * i / points;
			scene.effects().emitParticles(c.add(Math.cos(angle) * radius, 0, Math.sin(angle) * radius), emitter, 1, 1);
		}
	}

	void sparkle(BlockPos pos, int color, int amount) {
		SceneHelper.sparkles(scene, top(pos), color, amount);
	}

	void sparkle(Vec3 at, int color, int amount) {
		SceneHelper.sparkles(scene, at, color, amount);
	}

	void particles(Vec3 at, ParticleOptions particle, int amount, double spread) {
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(particle,
				x + (Math.random() - 0.5) * spread, y + Math.random() * spread * 0.5, z + (Math.random() - 0.5) * spread, 0, 0.02, 0);
		scene.effects().emitParticles(at, emitter, amount, 1);
	}

	void growth(BlockPos pos) {
		particles(center(pos), ParticleTypes.HAPPY_VILLAGER, 6, 0.8);
	}

	void redstone(BlockPos pos) {
		place(pos, Blocks.REDSTONE_BLOCK);
		scene.idle(5);
		scene.effects().indicateRedstone(pos);
	}

	ElementLink<EntityElement> mob(EntityType<?> type, Vec3 pos, float yaw) {
		return mob(type, pos, yaw, entity -> {});
	}

	ElementLink<EntityElement> mob(EntityType<?> type, Vec3 pos, float yaw, Consumer<Entity> setup) {
		return scene.world().createEntity(level -> {
			Entity entity = type.create(level, EntitySpawnReason.LOAD);
			entity.setPos(pos.x, pos.y, pos.z);
			entity.xo = pos.x;
			entity.yo = pos.y;
			entity.zo = pos.z;
			rotate(entity, yaw);
			if (entity instanceof Mob mob) {
				mob.setNoAi(true);
			}
			setup.accept(entity);
			return entity;
		});
	}

	private static void rotate(Entity entity, float yaw) {
		entity.setYRot(yaw);
		entity.yRotO = yaw;
		if (entity instanceof LivingEntity living) {
			living.yBodyRot = yaw;
			living.yBodyRotO = yaw;
			living.yHeadRot = yaw;
			living.yHeadRotO = yaw;
		}
	}

	void moveTo(ElementLink<EntityElement> link, Vec3 pos) {
		scene.world().modifyEntity(link, entity -> entity.setPos(pos.x, pos.y, pos.z));
	}

	void face(ElementLink<EntityElement> link, float yaw) {
		scene.world().modifyEntity(link, entity -> rotate(entity, yaw));
	}

	void walk(ElementLink<EntityElement> link, Vec3 from, Vec3 to, int ticks) {
		for (int i = 1; i <= ticks; i++) {
			moveTo(link, from.lerp(to, (double) i / ticks));
			scene.idle(1);
		}
	}

	static float yaw(Vec3 from, Vec3 to) {
		return (float) (Math.toDegrees(Math.atan2(to.z - from.z, to.x - from.x)) - 90);
	}

	void hurt(ElementLink<EntityElement> link) {
		scene.world().modifyEntity(link, entity -> {
			if (entity instanceof LivingEntity living) {
				living.animateHurt(0);
			}
			around(entity, ParticleTypes.DAMAGE_INDICATOR, 3);
		});
	}

	void effect(ElementLink<EntityElement> link, int color, int amount) {
		ParticleOptions particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xFF000000 | color);
		scene.world().modifyEntity(link, entity -> around(entity, particle, amount));
	}

	void around(ElementLink<EntityElement> link, ParticleOptions particle, int amount) {
		scene.world().modifyEntity(link, entity -> around(entity, particle, amount));
	}

	static void around(Entity entity, ParticleOptions particle, int amount) {
		for (int i = 0; i < amount; i++) {
			double x = entity.getX() + (Math.random() - 0.5) * entity.getBbWidth() * 1.5;
			double y = entity.getY() + Math.random() * entity.getBbHeight();
			double z = entity.getZ() + (Math.random() - 0.5) * entity.getBbWidth() * 1.5;
			entity.level().addParticle(particle, x, y, z, 0, 0.02, 0);
		}
	}

	void kill(ElementLink<EntityElement> link) {
		scene.world().modifyEntity(link, entity -> {
			if (entity instanceof LivingEntity living) {
				living.animateHurt(0);
				living.setHealth(0);
			}
		});
		scene.idle(18);
		vanish(link);
	}

	void vanish(ElementLink<EntityElement> link) {
		scene.world().modifyEntity(link, entity -> {
			around(entity, ParticleTypes.POOF, 12);
			entity.discard();
		});
	}

	ElementLink<EntityElement> item(Vec3 pos, Vec3 motion, ItemStack stack) {
		return scene.world().createItemEntity(pos, motion, stack);
	}

	ElementLink<EntityElement> drop(BlockPos pos, ItemStack stack) {
		return item(top(pos).add(0, 0.6, 0), new Vec3(0, 0.1, 0), stack);
	}

	void take(ElementLink<EntityElement> link) {
		scene.world().modifyEntity(link, entity -> {
			around(entity, ParticleTypes.SMOKE, 8);
			entity.discard();
		});
	}

	void push(ElementLink<EntityElement> link, Vec3 motion) {
		scene.world().modifyEntity(link, entity -> entity.setDeltaMovement(entity.getDeltaMovement().add(motion)));
	}
}
