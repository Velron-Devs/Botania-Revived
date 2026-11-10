package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.ParticleEmitter;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.Consumer;

final class SceneKit {
	static final float EAST = 180F;
	static final float SOUTH = 90F;
	static final float WEST = 0F;
	static final float NORTH = 270F;

	private SceneKit() {}

	static void bounds(SceneBuilder scene, int size) {
		int height = Math.max(size, 8);
		scene.addInstruction(ponderScene -> {
			PonderLevel level = ponderScene.getWorld();
			BoundingBox box = level.getBounds();
			level.setBounds(new BoundingBox(Math.min(box.minX(), 0), Math.min(box.minY(), 0), Math.min(box.minZ(), 0),
					Math.max(box.maxX(), size - 1), Math.max(box.maxY(), height - 1), Math.max(box.maxZ(), size - 1)));
		});
	}

	static void plate(SceneBuilder scene, int size) {
		bounds(scene, size);
		scene.configureBasePlate(0, 0, size);
		scene.showBasePlate();
		scene.idle(5);
	}

	static void put(SceneBuilder scene, BlockPos pos, BlockState state) {
		scene.world().setBlock(pos, state, false);
	}

	static void put(SceneBuilder scene, BlockPos pos, Block block) {
		put(scene, pos, block.defaultBlockState());
	}

	static void change(SceneBuilder scene, BlockPos pos, BlockState state) {
		scene.world().setBlock(pos, state, true);
	}

	static void show(SceneBuilder scene, SceneBuildingUtil util, BlockPos pos) {
		scene.world().showSection(util.select().position(pos), Direction.DOWN);
	}

	static void stage(SceneBuilder scene, SceneBuildingUtil util, int size, int height, BlockPos... hidden) {
		Selection selection = util.select().fromTo(0, 1, 0, size - 1, height, size - 1);
		for (BlockPos pos : hidden) {
			selection = selection.substract(util.select().position(pos));
		}
		scene.world().showSection(selection, Direction.DOWN);
	}

	static void say(SceneBuilder scene, PonderText text, int ticks, Vec3 at, boolean keyFrame) {
		TextElementBuilder builder = scene.overlay().showText(ticks).text(text.next()).pointAt(at).placeNearTarget();
		if (keyFrame) {
			builder.attachKeyFrame();
		}
	}

	static void range(SceneBuilder scene, SceneBuildingUtil util, int x0, int y0, int z0, int x1, int y1, int z1, int ticks) {
		scene.overlay().showOutline(PonderPalette.GREEN, "range" + x0 + y0 + z0, util.select().fromTo(x0, y0, z0, x1, y1, z1), ticks);
	}

	static void poolMana(SceneBuilder scene, SceneBuildingUtil util, BlockPos pool, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", mana));
	}

	static void spreaderMana(SceneBuilder scene, SceneBuildingUtil util, BlockPos spreader, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", mana));
	}

	static void aim(SceneBuilder scene, SceneBuildingUtil util, BlockPos spreader, float rotationX, float rotationY) {
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> {
			tag.putFloat("rotationX", rotationX);
			tag.putFloat("rotationY", rotationY);
		});
	}

	static void lens(SceneBuilder scene, SceneBuildingUtil util, BlockPos spreader, ItemStack lens) {
		scene.overlay().showControls(util.vector().blockSurface(spreader, Direction.UP), Pointing.DOWN, 30).rightClick().withItem(lens.copy());
		scene.idle(20);
		scene.world().modifyBlockEntity(spreader, ManaSpreaderBlockEntity.class, be -> be.getItemHandler().setItem(0, lens.copy()));
		scene.idle(10);
	}

	static void bind(SceneBuilder scene, SceneBuildingUtil util, BlockPos flower, BlockPos spreader) {
		scene.overlay().showControls(util.vector().blockSurface(flower, Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(25);
		scene.overlay().showControls(util.vector().blockSurface(spreader, Direction.UP), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(flower), util.vector().centerOf(spreader), 40);
		scene.idle(25);
	}

	static void deliver(SceneBuilder scene, SceneBuildingUtil util, Vec3 from, Vec3 to, BlockPos pool, int startMana, int endMana, int shots, int ticks, int color) {
		for (int shot = 1; shot <= shots; shot++) {
			SceneHelper.burst(scene, from, to, ticks, color);
			poolMana(scene, util, pool, startMana + (endMana - startMana) * shot / shots);
			scene.idle(2);
		}
	}

	static ParticleEmitter emitter(ParticleOptions particle, double spread, double speed) {
		return (level, x, y, z) -> level.addParticle(particle,
				x + (Math.random() - 0.5) * spread, y + (Math.random() - 0.5) * spread, z + (Math.random() - 0.5) * spread,
				(Math.random() - 0.5) * speed, Math.random() * speed, (Math.random() - 0.5) * speed);
	}

	static void burst(SceneBuilder scene, Vec3 at, ParticleOptions particle, int amount, double spread, double speed) {
		scene.effects().emitParticles(at, emitter(particle, spread, speed), amount, 1);
	}

	static void aura(SceneBuilder scene, Vec3 at, ParticleOptions particle, double chance, double spread, int ticks) {
		ParticleEmitter inner = emitter(particle, spread, 0.02);
		scene.effects().emitParticles(at, (level, x, y, z) -> {
			if (Math.random() < chance) {
				inner.create(level, x, y, z);
			}
		}, 1, ticks);
	}

	static void wispAura(SceneBuilder scene, Vec3 at, int color, double chance, int ticks) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		scene.effects().emitParticles(at, (level, x, y, z) -> {
			if (Math.random() < chance) {
				level.addParticle(WispParticleData.wisp((float) Math.random() / 6 + 0.05F, r, g, b, 1),
						x + (Math.random() - 0.5) * 0.3, y + Math.random() * 0.3, z + (Math.random() - 0.5) * 0.3, 0, 0.01 + Math.random() * 0.02, 0);
			}
		}, 1, ticks);
	}

	static void trail(SceneBuilder scene, Vec3 from, Vec3 to, int ticks, ParticleOptions particle) {
		ParticleEmitter emitter = emitter(particle, 0.1, 0.01);
		for (int i = 0; i <= ticks; i++) {
			scene.effects().emitParticles(from.lerp(to, (double) i / ticks), emitter, 2, 1);
			scene.idle(1);
		}
	}

	static void fadingBurst(SceneBuilder scene, Vec3 from, Vec3 to, int ticks, int color, double fadeFrom) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		for (int i = 0; i <= ticks; i++) {
			double t = (double) i / ticks;
			float size = t <= fadeFrom ? 0.3F : (float) (0.3 * (1 - (t - fadeFrom) / (1 - fadeFrom))) + 0.03F;
			scene.effects().emitParticles(from.lerp(to, t), (level, x, y, z) -> level.addParticle(WispParticleData.wisp(size, r, g, b, 3F), x, y, z, 0, 0, 0), 2F, 1);
			scene.idle(1);
		}
	}

	static void curve(SceneBuilder scene, int color, int ticks, Vec3... points) {
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		ParticleEmitter emitter = (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.3F, r, g, b, 3F), x, y, z, 0, 0, 0);
		for (int i = 0; i <= ticks; i++) {
			scene.effects().emitParticles(bezier((double) i / ticks, points), emitter, 2F, 1);
			scene.idle(1);
		}
	}

	private static Vec3 bezier(double t, Vec3... points) {
		Vec3[] work = points.clone();
		for (int n = work.length - 1; n > 0; n--) {
			for (int i = 0; i < n; i++) {
				work[i] = work[i].lerp(work[i + 1], t);
			}
		}
		return work[0];
	}

	static ElementLink<EntityElement> drop(SceneBuilder scene, Vec3 at, ItemStack stack) {
		return scene.world().createItemEntity(at, Vec3.ZERO, stack);
	}

	static <T extends Mob> ElementLink<EntityElement> mob(SceneBuilder scene, EntityType<T> type, Vec3 at, float yaw, Consumer<T> setup) {
		return scene.world().createEntity(level -> {
			T mob = type.create(level);
			mob.setPos(at.x, at.y, at.z);
			mob.setNoAi(true);
			mob.setYRot(yaw);
			mob.yRotO = yaw;
			mob.setYHeadRot(yaw);
			mob.yHeadRotO = yaw;
			mob.yBodyRot = yaw;
			mob.yBodyRotO = yaw;
			setup.accept(mob);
			return mob;
		});
	}

	static void move(SceneBuilder scene, ElementLink<EntityElement> link, Vec3 from, Vec3 to, int ticks, double hop) {
		for (int i = 1; i <= ticks; i++) {
			double t = (double) i / ticks;
			Vec3 pos = from.lerp(to, t).add(0, hop * Math.abs(Math.sin(t * Math.PI * 3)), 0);
			scene.world().modifyEntity(link, e -> e.setPos(pos.x, pos.y, pos.z));
			scene.idle(1);
		}
	}

	static void remove(SceneBuilder scene, ElementLink<EntityElement> link) {
		scene.world().modifyEntity(link, Entity::discard);
	}
}
