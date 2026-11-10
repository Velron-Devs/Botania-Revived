package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.api.math.Pointing;
import net.createmod.ponder.api.client.PonderPalette;
import net.createmod.ponder.api.client.element.ElementLink;
import net.createmod.ponder.api.client.element.EntityElement;
import net.createmod.ponder.api.client.element.WorldSectionElement;
import net.createmod.ponder.api.client.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.client.scene.PonderStoryBoard;
import net.createmod.ponder.api.client.scene.SceneBuilder;
import net.createmod.ponder.api.client.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.block.block_entity.ManaFlameBlockEntity;
import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;

final class LensScenes {
	private static final int GREEN = SceneHelper.MANA_COLOR;

	private LensScenes() {}

	static void register(PonderSceneRegistrationHelper<Identifier> helper) {
		add(helper, BotaniaItems.lensNormal, LensScenes::normal);
		add(helper, BotaniaItems.lensSpeed, LensScenes::speed);
		add(helper, BotaniaItems.lensPower, LensScenes::power);
		add(helper, BotaniaItems.lensTime, LensScenes::time);
		add(helper, BotaniaItems.lensEfficiency, LensScenes::efficiency);
		add(helper, BotaniaItems.lensBounce, LensScenes::bounce);
		add(helper, BotaniaItems.lensGravity, LensScenes::gravity);
		add(helper, BotaniaItems.lensMine, LensScenes::bore);
		add(helper, BotaniaItems.lensDamage, LensScenes::damage);
		add(helper, BotaniaItems.lensPhantom, LensScenes::phantom);
		add(helper, BotaniaItems.lensMagnet, LensScenes::magnet);
		add(helper, BotaniaItems.lensExplosive, LensScenes::entropic);
		add(helper, BotaniaItems.lensInfluence, LensScenes::influence);
		add(helper, BotaniaItems.lensWeight, LensScenes::weight);
		add(helper, BotaniaItems.lensPaint, LensScenes::paint);
		add(helper, BotaniaItems.lensFire, LensScenes::kindle);
		add(helper, BotaniaItems.lensPiston, LensScenes::force);
		add(helper, BotaniaItems.lensLight, LensScenes::flash);
		add(helper, BotaniaItems.lensWarp, LensScenes::warp);
		add(helper, BotaniaItems.lensRedirect, LensScenes::redirect);
		add(helper, BotaniaItems.lensFirework, LensScenes::celebratory);
		add(helper, BotaniaItems.lensFlare, LensScenes::flare);
		add(helper, BotaniaItems.lensMessenger, LensScenes::messenger);
		add(helper, BotaniaItems.lensTripwire, LensScenes::tripwire);
		add(helper, BotaniaItems.lensStorm, LensScenes::storm);
	}

	private static void add(PonderSceneRegistrationHelper<Identifier> helper, Item lens, PonderStoryBoard board) {
		helper.forComponents(List.of(BuiltInRegistries.ITEM.getKey(lens))).addStoryBoard(SpreaderScenes.PLATE, board, BotaniaPonderTags.MANA_NETWORK);
	}

	private static final class Bench {
		final SceneBuilder scene;
		final SceneBuildingUtil util;
		final PonderText text;
		final BlockPos spreader;
		final BlockPos pool;
		final Vec3 from;
		final Vec3 to;
		int mana;

		Bench(SceneBuilder scene, SceneBuildingUtil util, String id, BlockPos spreader, BlockPos pool) {
			this.scene = scene;
			this.util = util;
			this.text = new PonderText(id);
			this.spreader = spreader;
			this.pool = pool;
			scene.title(id, PonderStrings.title(id));
			SceneKit.plate(scene, 9);
			SceneKit.put(scene, spreader, BotaniaBlocks.manaSpreader);
			SceneKit.aim(scene, util, spreader, SceneKit.EAST, 0F);
			SceneKit.spreaderMana(scene, util, spreader, 1000);
			this.from = util.vector().centerOf(spreader).add(0.5, 0, 0);
			this.to = pool == null ? util.vector().centerOf(8, spreader.getY(), spreader.getZ()).add(0.5, 0, 0) : util.vector().centerOf(pool).add(-0.5, 0, 0);
			if (pool != null) {
				SceneKit.put(scene, pool, BotaniaBlocks.manaPool);
			}
		}

		void stage(BlockPos... hidden) {
			List<BlockPos> all = new ArrayList<>(List.of(hidden));
			all.add(spreader);
			if (pool != null) {
				all.add(pool);
			}
			SceneKit.stage(scene, util, 9, 5, all.toArray(BlockPos[]::new));
			scene.idle(10);
			SceneKit.show(scene, util, spreader);
			scene.idle(8);
			if (pool != null) {
				SceneKit.show(scene, util, pool);
				scene.idle(10);
			}
		}

		void say(int ticks, Vec3 at, boolean keyFrame) {
			SceneKit.say(scene, text, ticks, at, keyFrame);
		}

		Vec3 top() {
			return util.vector().topOf(spreader);
		}

		void lens(ItemStack stack) {
			SceneKit.lens(scene, util, spreader, stack);
		}

		void shoot(int ticks, int color, int gain) {
			SceneHelper.burst(scene, from, to, ticks, color);
			mana += gain;
			if (pool != null) {
				SceneKit.poolMana(scene, util, pool, Math.min(1000000, mana));
			}
			scene.idle(4);
		}
	}

	private static ItemStack dyed(Item lens, int color) {
		ItemStack stack = new ItemStack(lens);
		LensItem.setLensColor(stack, color);
		return stack;
	}

	private static int dye(DyeColor color) {
		return ColorHelper.getColorValue(color);
	}

	private static Bench standard(SceneBuilder scene, SceneBuildingUtil util, String id) {
		return new Bench(scene, util, id, util.grid().at(1, 1, 4), util.grid().at(7, 1, 4));
	}

	private static Bench open(SceneBuilder scene, SceneBuildingUtil util, String id) {
		return new Bench(scene, util, id, util.grid().at(1, 1, 4), null);
	}

	static void normal(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_normal");
		BlockPos stone = util.grid().at(4, 1, 4);
		b.stage();
		b.say(120, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensNormal));
		b.shoot(25, GREEN, 40000);
		scene.idle(60);

		b.say(130, b.top(), true);
		b.lens(dyed(BotaniaItems.lensNormal, DyeColor.RED.getId()));
		b.shoot(25, dye(DyeColor.RED), 40000);
		scene.idle(10);
		b.lens(dyed(BotaniaItems.lensNormal, 16));
		DyeColor[] rainbow = { DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE, DyeColor.PURPLE };
		for (DyeColor color : rainbow) {
			SceneHelper.burst(scene, b.from, b.to, 6, dye(color));
		}
		b.mana += 40000;
		SceneKit.poolMana(scene, util, b.pool, b.mana);
		scene.idle(40);

		ItemStack composite = new ItemStack(BotaniaItems.lensSpeed);
		((LensItem) BotaniaItems.lensSpeed).setCompositeLens(composite, new ItemStack(BotaniaItems.lensMine));
		b.say(140, b.top(), true);
		scene.overlay().showControls(b.top().add(0, 0.5, 0), Pointing.DOWN, 30).withItem(new ItemStack(Items.SLIME_BALL));
		scene.idle(35);
		SceneKit.change(scene, stone, Blocks.STONE.defaultBlockState());
		scene.idle(10);
		b.lens(composite);
		SceneHelper.burst(scene, b.from, util.vector().centerOf(stone).add(-0.5, 0, 0), 5, GREEN);
		scene.world().destroyBlock(stone);
		SceneHelper.burst(scene, util.vector().centerOf(stone), b.to, 7, GREEN);
		SceneKit.poolMana(scene, util, b.pool, b.mana + 30000);
		scene.idle(60);
	}

	static void speed(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_speed");
		b.stage();
		b.shoot(25, GREEN, 40000);
		scene.idle(10);
		b.say(120, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensSpeed));
		for (int i = 0; i < 4; i++) {
			b.shoot(12, GREEN, 30000);
		}
		scene.idle(40);

		b.say(120, util.vector().centerOf(4, 1, 4).add(0, 0.6, 0), true);
		for (int i = 0; i < 4; i++) {
			b.shoot(12, GREEN, 30000);
		}
		scene.idle(60);
	}

	static void power(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_power");
		b.stage();
		b.shoot(25, GREEN, 40000);
		scene.idle(10);
		b.say(120, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensPower));
		for (int i = 0; i < 2; i++) {
			b.shoot(29, GREEN, 80000);
		}
		scene.idle(40);

		b.say(130, b.top(), true);
		SceneKit.spreaderMana(scene, util, b.spreader, 200);
		for (int charge = 1; charge <= 3; charge++) {
			scene.idle(12);
			SceneKit.spreaderMana(scene, util, b.spreader, 200 + charge * 40);
			SceneHelper.sparkles(scene, b.top(), GREEN, 3);
		}
		b.shoot(29, GREEN, 80000);
		scene.idle(60);
	}

	static void time(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_time");
		BlockPos plain = util.grid().at(1, 1, 6);
		SceneKit.put(scene, plain, BotaniaBlocks.manaSpreader);
		SceneKit.aim(scene, util, plain, SceneKit.EAST, 0F);
		b.stage(plain);
		b.say(110, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensTime));
		for (int i = 0; i < 2; i++) {
			b.shoot(31, GREEN, 40000);
		}
		scene.idle(30);

		SceneKit.show(scene, util, plain);
		scene.idle(10);
		b.say(130, util.vector().centerOf(5, 1, 5).add(0, 0.6, 0), true);
		for (int i = 0; i < 2; i++) {
			SceneKit.fadingBurst(scene, util.vector().centerOf(plain).add(0.5, 0, 0), util.vector().centerOf(8, 1, 6).add(0.5, 0, 0), 32, GREEN, 0.35);
			b.shoot(31, GREEN, 40000);
		}
		scene.idle(50);
	}

	static void efficiency(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_efficiency");
		BlockPos plain = util.grid().at(1, 1, 6);
		SceneKit.put(scene, plain, BotaniaBlocks.manaSpreader);
		SceneKit.aim(scene, util, plain, SceneKit.EAST, 0F);
		b.stage(plain);
		b.say(120, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensEfficiency));
		SceneKit.fadingBurst(scene, b.from, b.to, 25, GREEN, 0.85);
		b.mana += 40000;
		SceneKit.poolMana(scene, util, b.pool, b.mana);
		scene.idle(60);

		SceneKit.show(scene, util, plain);
		scene.idle(10);
		b.say(130, b.to, true);
		for (int i = 0; i < 2; i++) {
			SceneKit.fadingBurst(scene, util.vector().centerOf(plain).add(0.5, 0, 0), util.vector().centerOf(8, 1, 6).add(0.5, 0, 0), 30, GREEN, 0.3);
			SceneKit.fadingBurst(scene, b.from, b.to, 25, GREEN, 0.85);
			b.mana += 40000;
			SceneKit.poolMana(scene, util, b.pool, b.mana);
		}
		scene.idle(50);
	}

	static void bounce(SceneBuilder scene, SceneBuildingUtil util) {
		BlockPos target = util.grid().at(1, 1, 7);
		Bench b = new Bench(scene, util, "lens_bounce", util.grid().at(1, 1, 4), target);
		for (int z = 5; z <= 8; z++) {
			SceneKit.put(scene, util.grid().at(5, 1, z), BotaniaBlocks.livingrockBrick);
		}
		SceneKit.aim(scene, util, b.spreader, 156.8F, 0F);
		b.stage();
		Vec3 start = util.vector().centerOf(b.spreader);
		Vec3 hit = new Vec3(5.0, start.y, 6.0);
		Vec3 end = util.vector().centerOf(target);
		SceneKit.fadingBurst(scene, start, hit, 18, GREEN, 0.9);
		SceneKit.burst(scene, hit, ParticleTypes.CRIT, 6, 0.2, 0.05);
		scene.idle(15);
		b.say(120, hit.add(0, 0.6, 0), false);
		b.lens(new ItemStack(BotaniaItems.lensBounce));
		SceneHelper.path(scene, GREEN, 18, start, hit, end);
		SceneKit.poolMana(scene, util, target, 60000);
		scene.idle(50);

		b.say(120, end.add(0, 0.6, 0), true);
		for (int i = 2; i <= 3; i++) {
			SceneHelper.path(scene, GREEN, 18, start, hit, end);
			SceneKit.poolMana(scene, util, target, 60000 * i);
			scene.idle(4);
		}
		scene.idle(60);
	}

	static void gravity(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_gravity");
		for (int z = 3; z <= 5; z++) {
			SceneKit.put(scene, util.grid().at(4, 1, z), BotaniaBlocks.livingrockBrick);
			SceneKit.put(scene, util.grid().at(4, 2, z), BotaniaBlocks.livingrockBrick);
		}
		b.stage();
		b.say(110, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensGravity));
		SceneKit.fadingBurst(scene, b.from, util.vector().centerOf(4, 1, 4).add(-0.5, 0, 0), 10, GREEN, 0.9);
		SceneKit.burst(scene, util.vector().centerOf(4, 1, 4).add(-0.5, 0, 0), ParticleTypes.CRIT, 6, 0.2, 0.05);
		scene.idle(30);

		b.say(130, b.top(), true);
		scene.overlay().showControls(util.vector().blockSurface(b.spreader, Direction.NORTH), Pointing.RIGHT, 30).rightClick().whileSneaking().withItem(new ItemStack(BotaniaItems.twigWand));
		for (int i = 1; i <= 6; i++) {
			SceneKit.aim(scene, util, b.spreader, SceneKit.EAST, i * 5F);
			scene.idle(3);
		}
		scene.idle(15);
		Vec3 peak = util.vector().centerOf(4, 4, 4);
		Vec3 land = util.vector().topOf(b.pool);
		for (int i = 1; i <= 3; i++) {
			SceneKit.curve(scene, GREEN, 30, b.from, peak.add(-0.5, 1.2, 0), land.add(0, 0.3, 0));
			b.mana += 40000;
			SceneKit.poolMana(scene, util, b.pool, b.mana);
			scene.idle(4);
		}
		scene.idle(50);
	}

	static void bore(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_mine");
		BlockPos[] row = { util.grid().at(3, 1, 4), util.grid().at(4, 1, 4), util.grid().at(5, 1, 4) };
		Block[] kinds = { Blocks.STONE, Blocks.IRON_ORE, Blocks.DIRT };
		Item[] drops = { Items.COBBLESTONE, Items.RAW_IRON, Items.DIRT };
		for (int i = 0; i < row.length; i++) {
			SceneKit.put(scene, row[i], kinds[i]);
		}
		b.stage();
		b.say(110, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensMine));
		Vec3 last = b.from;
		for (int i = 0; i < row.length; i++) {
			Vec3 face = util.vector().centerOf(row[i]).add(-0.5, 0, 0);
			SceneHelper.burst(scene, last, face, 4, GREEN);
			scene.world().destroyBlock(row[i]);
			SceneKit.drop(scene, util.vector().centerOf(row[i]), new ItemStack(drops[i]));
			last = face;
		}
		SceneHelper.burst(scene, last, b.to, 6, GREEN);
		SceneKit.poolMana(scene, util, b.pool, 25000);
		scene.idle(40);

		b.say(110, util.vector().topOf(row[1]), true);
		scene.idle(120);

		SceneKit.change(scene, row[0], Blocks.OBSIDIAN.defaultBlockState());
		SceneKit.change(scene, row[2], Blocks.BEDROCK.defaultBlockState());
		scene.idle(10);
		b.say(130, util.vector().topOf(row[2]), true);
		SceneHelper.burst(scene, b.from, util.vector().centerOf(row[0]).add(-0.5, 0, 0), 4, GREEN);
		scene.world().destroyBlock(row[0]);
		SceneKit.drop(scene, util.vector().centerOf(row[0]), new ItemStack(Items.OBSIDIAN));
		SceneKit.fadingBurst(scene, util.vector().centerOf(row[0]).add(-0.5, 0, 0), util.vector().centerOf(row[2]).add(-0.5, 0, 0), 8, GREEN, 0.9);
		SceneKit.burst(scene, util.vector().centerOf(row[2]).add(-0.5, 0, 0), ParticleTypes.CRIT, 6, 0.2, 0.05);
		scene.idle(80);
	}

	static void damage(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_damage");
		b.stage();
		Vec3 mobAt = util.vector().topOf(4, 0, 4);
		ElementLink<EntityElement> zombie = SceneKit.mob(scene, EntityType.ZOMBIE, mobAt, 90F, z -> {});
		scene.idle(10);
		b.say(110, mobAt.add(0, 2, 0), false);
		b.lens(new ItemStack(BotaniaItems.lensDamage));
		for (int i = 0; i < 2; i++) {
			hurtingBurst(b, zombie, mobAt);
		}
		scene.idle(30);

		b.say(120, mobAt.add(0, 2, 0), true);
		hurtingBurst(b, zombie, mobAt);
		SceneKit.remove(scene, zombie);
		SceneKit.burst(scene, mobAt.add(0, 0.8, 0), ParticleTypes.POOF, 15, 0.6, 0.05);
		scene.idle(20);
		b.shoot(25, GREEN, 40000);
		scene.idle(50);
	}

	private static void hurtingBurst(Bench b, ElementLink<EntityElement> mob, Vec3 mobAt) {
		Vec3 middle = new Vec3(mobAt.x, b.from.y, mobAt.z);
		SceneHelper.burst(b.scene, b.from, middle, 12, GREEN);
		b.scene.world().modifyEntity(mob, e -> {
			if (e instanceof LivingEntity living) {
				living.hurtTime = 10;
				living.hurtDuration = 10;
			}
		});
		SceneKit.burst(b.scene, mobAt.add(0, 1.2, 0), ParticleTypes.DAMAGE_INDICATOR, 4, 0.4, 0.1);
		SceneHelper.burst(b.scene, middle, b.to, 12, GREEN);
		b.mana += 37000;
		SceneKit.poolMana(b.scene, b.util, b.pool, b.mana);
		b.scene.idle(15);
	}

	static void phantom(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_phantom");
		BlockPos wall2 = util.grid().at(5, 1, 4);
		for (int z = 3; z <= 5; z++) {
			SceneKit.put(scene, util.grid().at(4, 1, z), BotaniaBlocks.livingrockBrick);
			SceneKit.put(scene, util.grid().at(4, 2, z), BotaniaBlocks.livingrockBrick);
		}
		b.stage();
		Vec3 wallFace = util.vector().centerOf(4, 1, 4).add(-0.5, 0, 0);
		SceneKit.fadingBurst(scene, b.from, wallFace, 10, GREEN, 0.9);
		SceneKit.burst(scene, wallFace, ParticleTypes.CRIT, 6, 0.2, 0.05);
		scene.idle(15);
		b.say(120, util.vector().topOf(4, 2, 4), false);
		b.lens(new ItemStack(BotaniaItems.lensPhantom));
		b.shoot(25, GREEN, 40000);
		scene.idle(50);

		SceneKit.change(scene, wall2, BotaniaBlocks.livingrockBrick.defaultBlockState());
		scene.idle(10);
		b.say(120, util.vector().topOf(wall2), true);
		for (int i = 0; i < 2; i++) {
			SceneKit.fadingBurst(scene, b.from, b.to, 25, GREEN, 0.75);
			b.mana += 36000;
			SceneKit.poolMana(scene, util, b.pool, b.mana);
			scene.idle(4);
		}
		scene.idle(50);
	}

	static void magnet(SceneBuilder scene, SceneBuildingUtil util) {
		BlockPos target = util.grid().at(7, 1, 6);
		Bench b = new Bench(scene, util, "lens_magnet", util.grid().at(1, 1, 4), target);
		b.stage();
		Vec3 straightEnd = util.vector().centerOf(8, 1, 4).add(0.5, 0, 0);
		SceneKit.fadingBurst(scene, b.from, straightEnd, 30, GREEN, 0.9);
		scene.idle(10);
		b.say(120, util.vector().topOf(target), false);
		b.lens(new ItemStack(BotaniaItems.lensMagnet));
		Vec3 land = util.vector().centerOf(target).add(-0.4, 0, -0.3);
		Vec3 bend = util.vector().centerOf(5, 1, 4);
		for (int i = 1; i <= 2; i++) {
			SceneKit.curve(scene, GREEN, 26, b.from, bend, land);
			SceneKit.poolMana(scene, util, target, 500000 * i);
			scene.idle(6);
		}
		scene.idle(30);

		b.say(120, util.vector().topOf(target), true);
		SceneKit.poolMana(scene, util, target, 1000000);
		scene.idle(15);
		SceneKit.fadingBurst(scene, b.from, straightEnd, 30, GREEN, 0.9);
		scene.idle(70);
	}

	static void entropic(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_explosive");
		List<BlockPos> wall = new ArrayList<>();
		for (int x = 6; x <= 7; x++) {
			for (int y = 1; y <= 3; y++) {
				for (int z = 2; z <= 6; z++) {
					BlockPos pos = util.grid().at(x, y, z);
					SceneKit.put(scene, pos, Blocks.STONE);
					wall.add(pos);
				}
			}
		}
		b.stage();
		b.say(110, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensExplosive));
		Vec3 face = util.vector().centerOf(6, 1, 4).add(-0.5, 0, 0);
		SceneHelper.burst(scene, b.from, face, 20, GREEN);
		explode(scene, util, face, 1.6, wall);
		scene.idle(50);

		b.say(130, face.add(0, 1, 0), true);
		Vec3 deeper = util.vector().centerOf(7, 1, 4).add(-0.5, 0, 0);
		SceneHelper.burst(scene, b.from, deeper, 25, GREEN);
		explode(scene, util, deeper, 1.6, wall);
		scene.idle(80);
	}

	private static void explode(SceneBuilder scene, SceneBuildingUtil util, Vec3 at, double radius, List<BlockPos> blocks) {
		SceneKit.burst(scene, at, ParticleTypes.EXPLOSION, 5, radius, 0);
		SceneKit.burst(scene, at, ParticleTypes.EXPLOSION_EMITTER, 1, 0, 0);
		for (BlockPos pos : new ArrayList<>(blocks)) {
			if (Vec3.atCenterOf(pos).distanceTo(at) <= radius + 0.3) {
				SceneKit.put(scene, pos, Blocks.AIR.defaultBlockState());
				blocks.remove(pos);
			}
		}
	}

	static void influence(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_influence");
		b.stage();
		b.lens(new ItemStack(BotaniaItems.lensInfluence));
		double[] spots = { 3.4, 4.3, 5.1 };
		Item[] goods = { Items.IRON_INGOT, Items.GOLD_INGOT, Items.EMERALD };
		List<ElementLink<EntityElement>> items = new ArrayList<>();
		for (int i = 0; i < spots.length; i++) {
			items.add(SceneKit.drop(scene, new Vec3(spots[i], 1.5, 4.5 + (i - 1) * 0.4), new ItemStack(goods[i])));
		}
		scene.idle(20);
		b.say(130, util.vector().topOf(4, 0, 4), false);
		scene.idle(30);
		dragBurst(b, items, spots);
		scene.idle(30);

		b.say(110, b.to, true);
		scene.idle(120);
	}

	private static void dragBurst(Bench b, List<ElementLink<EntityElement>> items, double[] spots) {
		int ticks = 25;
		float[] rgb = { 0.125F, 1F, 0.125F };
		for (int t = 0; t <= ticks; t++) {
			Vec3 at = b.from.lerp(b.to, (double) t / ticks);
			b.scene.effects().emitParticles(at, (level, x, y, z) -> level.addParticle(WispParticleData.wisp(0.3F, rgb[0], rgb[1], rgb[2], 3F), x, y, z, 0, 0, 0), 2F, 1);
			for (int i = 0; i < items.size(); i++) {
				if (at.x >= spots[i]) {
					double z = 4.5 + (i - 1) * 0.4;
					double x = Math.min(at.x - 0.3 - i * 0.15, 6.4);
					b.scene.world().modifyEntity(items.get(i), e -> {
						e.setPos(x, 1.1, z);
						e.setDeltaMovement(Vec3.ZERO);
					});
				}
			}
			b.scene.idle(1);
		}
		SceneKit.poolMana(b.scene, b.util, b.pool, 40000);
	}

	static void weight(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = new Bench(scene, util, "lens_weight", util.grid().at(1, 2, 4), null);
		SceneKit.put(scene, util.grid().at(1, 1, 4), BotaniaBlocks.livingrockBrick);
		BlockPos floating = util.grid().at(6, 2, 4);
		BlockPos below = util.grid().at(6, 1, 4);
		SceneKit.put(scene, floating, Blocks.COBBLESTONE);
		b.stage();
		b.say(120, util.vector().topOf(floating), false);
		b.lens(new ItemStack(BotaniaItems.lensWeight));
		Vec3 face = util.vector().centerOf(floating).add(-0.5, 0, 0);
		SceneHelper.burst(scene, b.from, face, 20, GREEN);
		fall(scene, util, floating, below, Blocks.COBBLESTONE.defaultBlockState());
		scene.idle(50);

		SceneKit.change(scene, floating, Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
		scene.idle(10);
		b.say(120, util.vector().topOf(floating), true);
		SceneKit.fadingBurst(scene, b.from, face, 20, GREEN, 0.95);
		SceneKit.burst(scene, face, ParticleTypes.CRIT, 6, 0.2, 0.05);
		scene.idle(90);
	}

	private static void fall(SceneBuilder scene, SceneBuildingUtil util, BlockPos from, BlockPos to, BlockState state) {
		SceneKit.burst(scene, util.vector().centerOf(from), new BlockParticleOption(ParticleTypes.FALLING_DUST, state), 10, 0.9, 0.05);
		scene.idle(4);
		SceneKit.put(scene, from, Blocks.AIR.defaultBlockState());
		SceneKit.put(scene, to, state);
		SceneKit.burst(scene, util.vector().topOf(to), new BlockParticleOption(ParticleTypes.BLOCK, state), 8, 0.8, 0.05);
		scene.idle(10);
	}

	static void paint(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_paint");
		List<BlockPos> wool = new ArrayList<>();
		for (int y = 1; y <= 3; y++) {
			for (int z = 2; z <= 6; z++) {
				BlockPos pos = util.grid().at(7, y, z);
				SceneKit.put(scene, pos, Blocks.WHITE_WOOL);
				wool.add(pos);
			}
		}
		b.stage();
		int red = dye(DyeColor.RED);
		b.say(140, util.vector().topOf(7, 3, 4), false);
		b.lens(dyed(BotaniaItems.lensPaint, DyeColor.RED.getId()));
		BlockPos hit = util.grid().at(7, 1, 4);
		SceneHelper.burst(scene, b.from, util.vector().centerOf(hit).add(-0.5, 0, 0), 25, red);
		for (int ring = 0; ring <= 4; ring++) {
			for (BlockPos pos : wool) {
				if (pos.distManhattan(hit) == ring) {
					SceneKit.change(scene, pos, Blocks.RED_WOOL.defaultBlockState());
				}
			}
			scene.idle(3);
		}
		scene.idle(50);

		Vec3 first = util.vector().topOf(4, 0, 4);
		Vec3 second = util.vector().topOf(3, 0, 7);
		ElementLink<EntityElement> sheepA = SceneKit.mob(scene, EntityType.SHEEP, first, 90F, s -> s.setColor(DyeColor.WHITE));
		ElementLink<EntityElement> sheepB = SceneKit.mob(scene, EntityType.SHEEP, second, 0F, s -> s.setColor(DyeColor.WHITE));
		scene.idle(15);
		b.say(130, first.add(0, 1.5, 0), true);
		b.lens(dyed(BotaniaItems.lensPaint, DyeColor.BLUE.getId()));
		SceneHelper.burst(scene, b.from, first.add(-0.5, 0.5, 0), 12, dye(DyeColor.BLUE));
		scene.world().modifyEntity(sheepA, e -> ((Sheep) e).setColor(DyeColor.BLUE));
		scene.idle(6);
		scene.world().modifyEntity(sheepB, e -> ((Sheep) e).setColor(DyeColor.BLUE));
		SceneHelper.sparkles(scene, first.add(0, 1, 0), dye(DyeColor.BLUE), 10);
		SceneHelper.sparkles(scene, second.add(0, 1, 0), dye(DyeColor.BLUE), 10);
		scene.idle(80);
	}

	static void kindle(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_fire");
		BlockPos target = util.grid().at(6, 1, 4);
		BlockPos flame = util.grid().at(5, 1, 4);
		SceneKit.put(scene, target, Blocks.NETHERRACK);
		List<BlockPos> frame = new ArrayList<>();
		List<BlockPos> portal = new ArrayList<>();
		for (int y = 1; y <= 3; y++) {
			frame.add(util.grid().at(6, y, 3));
			frame.add(util.grid().at(6, y, 6));
			portal.add(util.grid().at(6, y, 4));
			portal.add(util.grid().at(6, y, 5));
		}
		frame.add(util.grid().at(6, 4, 4));
		frame.add(util.grid().at(6, 4, 5));
		b.stage();
		b.say(110, util.vector().topOf(target), false);
		b.lens(new ItemStack(BotaniaItems.lensFire));
		Vec3 face = util.vector().centerOf(target).add(-0.5, 0, 0);
		SceneKit.trail(scene, b.from, face, 20, ParticleTypes.FLAME);
		SceneKit.change(scene, flame, Blocks.FIRE.defaultBlockState());
		scene.idle(60);

		SceneKit.put(scene, flame, Blocks.AIR.defaultBlockState());
		scene.world().destroyBlock(target);
		scene.idle(10);
		BlockState portalState = Blocks.NETHER_PORTAL.defaultBlockState().setValue(NetherPortalBlock.AXIS, Direction.Axis.Z);
		SceneKit.change(scene, util.grid().at(6, 0, 4), Blocks.OBSIDIAN.defaultBlockState());
		SceneKit.change(scene, util.grid().at(6, 0, 5), Blocks.OBSIDIAN.defaultBlockState());
		for (BlockPos pos : frame) {
			SceneKit.change(scene, pos, Blocks.OBSIDIAN.defaultBlockState());
		}
		for (BlockPos pos : portal) {
			SceneKit.put(scene, pos, portalState);
		}
		scene.idle(20);
		b.say(110, util.vector().topOf(6, 4, 4), true);
		SceneKit.trail(scene, b.from, util.vector().centerOf(6, 1, 4).add(-0.5, 0, 0), 20, ParticleTypes.FLAME);
		for (BlockPos pos : portal) {
			scene.world().destroyBlock(pos);
		}
		scene.idle(90);
	}

	static void force(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_piston");
		BlockPos block = util.grid().at(4, 1, 4);
		BlockPos obsidian = util.grid().at(5, 1, 4);
		SceneKit.put(scene, block, Blocks.SMOOTH_STONE);
		b.stage();
		b.say(110, util.vector().topOf(block), false);
		b.lens(new ItemStack(BotaniaItems.lensPiston));
		ElementLink<WorldSectionElement> moving = scene.world().makeSectionIndependent(util.select().position(block));
		for (int push = 0; push < 2; push++) {
			Vec3 face = util.vector().centerOf(block).add(-0.5 + push, 0, 0);
			SceneHelper.burst(scene, b.from, face, 10 + push * 4, GREEN);
			scene.world().moveSection(moving, new Vec3(1, 0, 0), 4);
			scene.idle(25);
		}
		scene.idle(20);

		scene.world().hideIndependentSection(moving, Direction.UP);
		scene.idle(10);
		SceneKit.change(scene, obsidian, Blocks.OBSIDIAN.defaultBlockState());
		scene.idle(10);
		b.say(120, util.vector().topOf(obsidian), true);
		SceneKit.fadingBurst(scene, b.from, util.vector().centerOf(obsidian).add(-0.5, 0, 0), 10, GREEN, 0.95);
		SceneKit.burst(scene, util.vector().centerOf(obsidian).add(-0.5, 0, 0), ParticleTypes.CRIT, 6, 0.2, 0.05);
		scene.idle(90);
	}

	static void flash(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_light");
		BlockPos wall = util.grid().at(6, 1, 4);
		BlockPos flame = util.grid().at(5, 1, 4);
		SceneKit.put(scene, wall, BotaniaBlocks.livingrockBrick);
		SceneKit.put(scene, util.grid().at(6, 2, 4), BotaniaBlocks.livingrockBrick);
		b.stage();
		b.say(130, util.vector().topOf(flame), false);
		b.lens(new ItemStack(BotaniaItems.lensLight));
		SceneHelper.burst(scene, b.from, util.vector().centerOf(wall).add(-0.5, 0, 0), 20, GREEN);
		SceneKit.change(scene, flame, BotaniaBlocks.manaFlame.defaultBlockState());
		scene.idle(70);

		b.say(120, util.vector().topOf(flame), true);
		SceneHelper.burst(scene, b.from, util.vector().centerOf(flame).add(-0.3, 0, 0), 16, GREEN);
		scene.world().destroyBlock(flame);
		scene.idle(20);
		int blue = dye(DyeColor.LIGHT_BLUE);
		b.lens(dyed(BotaniaItems.lensLight, DyeColor.LIGHT_BLUE.getId()));
		SceneHelper.burst(scene, b.from, util.vector().centerOf(wall).add(-0.5, 0, 0), 20, blue);
		SceneKit.change(scene, flame, BotaniaBlocks.manaFlame.defaultBlockState());
		scene.world().modifyBlockEntity(flame, ManaFlameBlockEntity.class, be -> be.setColor(blue));
		scene.idle(70);
	}

	static void warp(SceneBuilder scene, SceneBuildingUtil util) {
		BlockPos target = util.grid().at(7, 1, 1);
		Bench b = new Bench(scene, util, "lens_warp", util.grid().at(1, 1, 4), target);
		BlockPos relay = util.grid().at(4, 1, 4);
		BlockPos destination = util.grid().at(4, 1, 1);
		SceneKit.put(scene, relay, BotaniaBlocks.pistonRelay);
		b.stage(relay);
		SceneKit.show(scene, util, relay);
		scene.idle(10);
		b.say(140, util.vector().topOf(relay), false);
		scene.overlay().showControls(util.vector().topOf(relay), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(25);
		scene.overlay().showControls(util.vector().topOf(destination), Pointing.DOWN, 20).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(relay), util.vector().centerOf(destination), 40);
		scene.idle(25);
		b.lens(new ItemStack(BotaniaItems.lensWarp));
		Vec3 out = util.vector().centerOf(destination);
		for (int i = 1; i <= 2; i++) {
			SceneHelper.burst(scene, b.from, util.vector().centerOf(relay).add(-0.5, 0, 0), 8, GREEN);
			SceneKit.burst(scene, util.vector().centerOf(relay), ParticleTypes.PORTAL, 15, 0.4, 0.3);
			SceneKit.burst(scene, out, ParticleTypes.PORTAL, 15, 0.4, 0.3);
			SceneHelper.burst(scene, out, util.vector().centerOf(target).add(-0.5, 0, 0), 12, GREEN);
			SceneKit.poolMana(scene, util, target, 40000 * i);
			scene.idle(10);
		}
		scene.idle(20);

		scene.world().hideSection(util.select().position(relay), Direction.UP);
		BlockPos[] stones = { util.grid().at(5, 1, 4), util.grid().at(6, 1, 4) };
		scene.idle(10);
		for (BlockPos stone : stones) {
			SceneKit.change(scene, stone, Blocks.STONE.defaultBlockState());
		}
		scene.idle(10);
		ItemStack composite = new ItemStack(BotaniaItems.lensMine);
		((LensItem) BotaniaItems.lensMine).setCompositeLens(composite, new ItemStack(BotaniaItems.lensWarp));
		b.say(140, util.vector().topOf(b.spreader), true);
		b.lens(composite);
		Vec3 behind = util.vector().centerOf(0, 1, 4);
		Vec3 last = b.from;
		for (BlockPos stone : stones) {
			Vec3 face = util.vector().centerOf(stone).add(-0.5, 0, 0);
			SceneHelper.burst(scene, last, face, 8, GREEN);
			scene.world().destroyBlock(stone);
			SceneKit.drop(scene, behind, new ItemStack(Items.COBBLESTONE));
			SceneKit.burst(scene, behind, ParticleTypes.PORTAL, 10, 0.3, 0.3);
			last = face;
		}
		scene.idle(80);
	}

	static void redirect(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_redirect");
		BlockPos other = util.grid().at(6, 1, 4);
		SceneKit.put(scene, other, BotaniaBlocks.manaSpreader);
		SceneKit.aim(scene, util, other, SceneKit.NORTH, 0F);
		b.stage(other);
		SceneKit.show(scene, util, other);
		scene.idle(10);
		b.say(130, util.vector().topOf(other), false);
		b.lens(new ItemStack(BotaniaItems.lensRedirect));
		SceneHelper.burst(scene, b.from, util.vector().centerOf(other).add(-0.5, 0, 0), 20, GREEN);
		for (int i = 1; i <= 9; i++) {
			SceneKit.aim(scene, util, other, SceneKit.NORTH + i * 10F, 0F);
			scene.idle(2);
		}
		scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(other), util.vector().centerOf(b.spreader), 40);
		scene.idle(60);

		Vec3 mobAt = util.vector().topOf(4, 0, 4);
		ElementLink<EntityElement> zombie = SceneKit.mob(scene, EntityType.ZOMBIE, mobAt, 180F, z -> {});
		scene.idle(15);
		b.say(130, mobAt.add(0, 2, 0), true);
		SceneHelper.burst(scene, b.from, mobAt.add(-0.4, 0.5, 0), 12, GREEN);
		for (int i = 1; i <= 6; i++) {
			float yaw = 180F - i * 15F;
			scene.world().modifyEntity(zombie, e -> {
				e.setYRot(yaw);
				e.setYHeadRot(yaw);
				if (e instanceof LivingEntity living) {
					living.yBodyRot = yaw;
					living.yHeadRotO = yaw;
				}
			});
			scene.idle(2);
		}
		scene.idle(90);
	}

	static void celebratory(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_firework");
		BlockPos wall = util.grid().at(7, 1, 4);
		SceneKit.put(scene, wall, BotaniaBlocks.livingrockBrick);
		SceneKit.put(scene, util.grid().at(7, 2, 4), BotaniaBlocks.livingrockBrick);
		b.stage();
		b.say(110, util.vector().topOf(7, 2, 4), false);
		b.lens(new ItemStack(BotaniaItems.lensFirework));
		Vec3 face = util.vector().centerOf(wall).add(-0.5, 0, 0);
		SceneHelper.burst(scene, b.from, face, 25, GREEN);
		rocket(scene, face, GREEN);
		scene.idle(30);

		b.say(120, face.add(0, 2, 0), true);
		int pink = dye(DyeColor.PINK);
		b.lens(dyed(BotaniaItems.lensFirework, DyeColor.PINK.getId()));
		SceneHelper.burst(scene, b.from, face, 25, pink);
		rocket(scene, face, pink);
		scene.idle(50);
	}

	private static void rocket(SceneBuilder scene, Vec3 at, int color) {
		ElementLink<EntityElement> rocket = scene.world().createEntity(level -> new FireworkRocketEntity(level, at.x, at.y, at.z, new ItemStack(Items.FIREWORK_ROCKET)));
		Vec3 top = at.add(0, 4, 0);
		for (int i = 1; i <= 20; i++) {
			Vec3 pos = at.lerp(top, i / 20.0);
			scene.world().modifyEntity(rocket, e -> {
				e.setPos(pos.x, pos.y, pos.z);
				e.setDeltaMovement(Vec3.ZERO);
			});
			scene.idle(1);
		}
		SceneKit.remove(scene, rocket);
		SceneKit.burst(scene, top, ParticleTypes.FIREWORK, 40, 1.5, 0.15);
		SceneKit.burst(scene, top, ColorParticleOption.create(ParticleTypes.FLASH, 1F, 1F, 1F), 1, 0, 0);
		SceneHelper.sparkles(scene, top, color, 30);
	}

	static void flare(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_flare");
		BlockPos redstone = util.grid().at(1, 1, 5);
		b.stage();
		b.say(120, b.top(), false);
		b.lens(dyed(BotaniaItems.lensFlare, DyeColor.RED.getId()));
		stream(scene, b.from, dye(DyeColor.RED), false, 120);
		scene.idle(125);

		b.say(130, b.top(), true);
		b.lens(dyed(BotaniaItems.lensFlare, 16));
		stream(scene, b.from, 0, true, 80);
		scene.idle(85);
		SceneKit.change(scene, redstone, Blocks.REDSTONE_BLOCK.defaultBlockState());
		scene.effects().indicateRedstone(redstone);
		scene.idle(60);
	}

	private static void stream(SceneBuilder scene, Vec3 at, int color, boolean rainbow, int ticks) {
		scene.effects().emitParticles(at, (level, x, y, z) -> {
			int hex = rainbow ? Mth.hsvToRgb((float) Math.random(), 1F, 1F) : color;
			float r = (hex >> 16 & 0xFF) / 255F;
			float g = (hex >> 8 & 0xFF) / 255F;
			float bl = (hex & 0xFF) / 255F;
			level.addParticle(WispParticleData.wisp(0.4F, r, g, bl), x, y, z, 0.15, (Math.random() - 0.5) * 0.01, (Math.random() - 0.5) * 0.01);
		}, 1, ticks);
	}

	static void messenger(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_messenger");
		BlockPos detector = util.grid().at(4, 1, 4);
		BlockPos lamp = util.grid().at(4, 1, 3);
		SceneKit.put(scene, detector, BotaniaBlocks.manaDetector);
		SceneKit.put(scene, lamp, Blocks.REDSTONE_LAMP);
		b.stage(detector, lamp);
		b.say(130, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensMessenger));
		for (int i = 0; i < 4; i++) {
			b.shoot(8, GREEN, 8000);
			scene.idle(6);
		}
		scene.idle(40);

		SceneKit.show(scene, util, detector);
		scene.idle(5);
		SceneKit.show(scene, util, lamp);
		scene.idle(10);
		b.say(130, util.vector().topOf(lamp), true);
		Vec3 mid = util.vector().centerOf(detector);
		for (int i = 0; i < 3; i++) {
			SceneHelper.burst(scene, b.from, mid, 4, GREEN);
			scene.world().modifyBlock(detector, s -> s.setValue(BlockStateProperties.POWERED, true), false);
			scene.world().modifyBlock(lamp, s -> s.setValue(BlockStateProperties.LIT, true), false);
			SceneHelper.burst(scene, mid, b.to, 4, GREEN);
			scene.idle(8);
			scene.world().modifyBlock(detector, s -> s.setValue(BlockStateProperties.POWERED, false), false);
			scene.world().modifyBlock(lamp, s -> s.setValue(BlockStateProperties.LIT, false), false);
			scene.idle(15);
		}
		scene.idle(40);
	}

	static void tripwire(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = standard(scene, util, "lens_tripwire");
		b.stage();
		b.say(120, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensTripwire));
		scene.overlay().showLine(PonderPalette.RED, b.from, b.to, 60);
		scene.idle(60);
		crossing(b, 4);
		scene.idle(20);

		b.say(120, b.top(), true);
		crossing(b, 5);
		scene.idle(60);
	}

	private static void crossing(Bench b, int x) {
		Vec3 start = b.util.vector().topOf(x, 0, 0);
		Vec3 end = b.util.vector().topOf(x, 0, 8);
		Vec3 middle = b.util.vector().topOf(x, 0, 4);
		ElementLink<EntityElement> pig = SceneKit.mob(b.scene, EntityType.PIG, start, 0F, p -> {});
		SceneKit.move(b.scene, pig, start, middle, 30, 0);
		b.shoot(25, GREEN, 40000);
		SceneKit.move(b.scene, pig, middle, end, 30, 0);
		SceneKit.remove(b.scene, pig);
	}

	static void storm(SceneBuilder scene, SceneBuildingUtil util) {
		Bench b = open(scene, util, "lens_storm");
		List<BlockPos> wall = new ArrayList<>();
		for (int x = 6; x <= 8; x++) {
			for (int y = 1; y <= 4; y++) {
				for (int z = 1; z <= 7; z++) {
					BlockPos pos = util.grid().at(x, y, z);
					SceneKit.put(scene, pos, Blocks.STONE);
					wall.add(pos);
				}
			}
		}
		b.stage();
		b.say(110, b.top(), false);
		b.lens(new ItemStack(BotaniaItems.lensStorm));
		Vec3 face = util.vector().centerOf(6, 1, 4).add(-0.5, 0, 0);
		SceneHelper.burst(scene, b.from, face, 20, GREEN);
		explode(scene, util, face, 2.8, wall);
		scene.idle(60);

		b.say(120, face.add(0, 2, 0), true);
		Vec3 deeper = new Vec3(8.0, face.y, face.z);
		SceneHelper.burst(scene, b.from, deeper.add(-0.5, 0, 0), 28, GREEN);
		explode(scene, util, deeper, 2.8, wall);
		scene.idle(80);
	}
}
