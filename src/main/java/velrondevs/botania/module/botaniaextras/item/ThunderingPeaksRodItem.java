package velrondevs.botania.module.botaniaextras.item;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.Avatar;
import velrondevs.botania.api.item.AvatarWieldable;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.common.entity.GaiaGuardianEntity;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.module.botaniaextras.ClericalColors;
import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ThunderingPeaksRodItem extends Item {
	private static final ResourceLocation AVATAR_OVERLAY = prefix("textures/model/botania_extras/avatar_thundering_peaks.png");
	private static final Map<UUID, Integer> TARGETS = new HashMap<>();

	private static final int COST_AVATAR = 150;
	private static final int COST = 300;
	private static final int PRIEST_COST = 200;
	private static final int THOR_COST = 700;
	private static final int PROWESS_COST = 50;

	private static final int SPEED = 90;
	private static final int PRIEST_SPEEDUP = 30;
	private static final int THOR_SPEEDUP = 10;
	private static final int PROWESS_SPEEDUP = 10;

	private static final float DAMAGE = 8F;
	private static final float PRIEST_POWERUP = 3F;
	private static final float THOR_POWERUP = 7F;
	private static final float PROWESS_POWERUP = 2F;

	private static final double CHAIN_RANGE = 7;
	private static final double PRIEST_RANGEUP = -1;
	private static final double THOR_RANGEUP = 1;
	private static final double PROWESS_RANGEUP = 1;

	private static final int TARGET_COUNT = 4;
	private static final int PRIEST_TARGETS = 2;
	private static final int THOR_TARGETS = -2;
	private static final int PROWESS_TARGETS = 1;

	public ThunderingPeaksRodItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public UseAnim getUseAnimation(@NotNull ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
		return 72000;
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(stack);
	}

	@Override
	public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity living, int timeLeft) {
		TARGETS.remove(living.getUUID());
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remaining) {
		if (!(living instanceof Player player)) {
			return;
		}
		boolean thor = !EquipmentHandler.findOrEmpty(BotaniaItems.thorRing, player).isEmpty();
		boolean priest = PriestEmblemItem.hasEmblem(player, PriestEmblemItem.Type.THOR);
		boolean prowess = ManaItemHandler.instance().hasProficiency(player, stack);
		int speed = getSpeed(thor, prowess, priest);

		if (level.isClientSide) {
			int interval = Math.max(1, speed / 10);
			if (remaining != getUseDuration(stack, player) && remaining % interval == 0
					&& ManaItemHandler.instance().requestManaExactForTool(stack, player, getCost(thor, prowess, priest), false)) {
				Vec3 head = VecHelper.fromEntityCenter(player).add(0, 0.75, 0).add(player.getLookAngle().scale(-0.25));
				int color = ClericalColors.get(player);
				Proxy.INSTANCE.lightningFX(level, head, head.add(PriestEmblemItem.headOrientation(player)), 2.0F,
						color == ClericalColors.NONE ? 0x0079C4 : color, color == ClericalColors.NONE ? 0xAADFFF : ClericalColors.brighter2(color));
			}
			return;
		}

		int cost = getCost(thor, prowess, priest);
		if (remaining == getUseDuration(stack, player) || !ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, false)) {
			return;
		}
		LivingEntity target = findTarget(level, player, TARGETS.getOrDefault(player.getUUID(), -1));
		if (target == null) {
			TARGETS.remove(player.getUUID());
			return;
		}
		TARGETS.put(player.getUUID(), target.getId());

		if (remaining % speed == 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true)) {
			float damage = getDamage(thor, prowess, priest);
			if (thor) {
				LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
				if (bolt != null) {
					bolt.moveTo(target.getX(), target.getY(), target.getZ());
					bolt.setVisualOnly(true);
					level.addFreshEntity(bolt);
				}
			} else {
				level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 2F, 0.8F + level.random.nextFloat() * 0.2F);
			}
			target.hurt(player.damageSources().playerAttack(player), damage);
			IntList hit = new IntArrayList();
			hit.add(target.getId());
			chain(level, target, player.damageSources().playerAttack(player), getRange(thor, prowess, priest), getTargetCap(thor, prowess, priest), damage, hit);
			XplatAbstractions.INSTANCE.sendToTracking(player, new BotaniaEffectPacket(EffectType.THUNDERCALLER_EFFECT,
					player.getX(), player.getY() + player.getBbHeight() / 2.0, player.getZ(), hit.toArray(new int[0])));
		}
	}

	private static void chain(Level level, LivingEntity first, DamageSource source, double range, int targets, float damage, IntList hit) {
		Predicate<Entity> selector = e -> e instanceof LivingEntity && e instanceof Enemy && !(e instanceof Player) && !hit.contains(e.getId());
		LivingEntity previous = first;
		float dmg = damage;
		for (int i = 0; i <= targets; i++) {
			List<Entity> entities = level.getEntities(previous, new AABB(previous.getX() - range, previous.getY() - range, previous.getZ() - range,
					previous.getX() + range, previous.getY() + range, previous.getZ() + range), selector);
			if (entities.isEmpty()) {
				break;
			}
			LivingEntity next = (LivingEntity) entities.get(level.random.nextInt(entities.size()));
			next.hurt(source, dmg);
			hit.add(next.getId());
			previous = next;
			dmg--;
		}
	}

	@Nullable
	private static LivingEntity findTarget(Level level, Player player, int trial) {
		double range = 12;
		List<LivingEntity> potential = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(range),
				e -> e instanceof Enemy && !(e instanceof Player) && e.isAlive());
		if (trial >= 0) {
			Entity existing = level.getEntity(trial);
			if (existing instanceof LivingEntity living && potential.contains(living)) {
				return living;
			}
		}
		if (potential.isEmpty()) {
			return null;
		}
		return potential.get(level.random.nextInt(potential.size()));
	}

	public static int getCost(boolean thor, boolean prowess, boolean priest) {
		return COST + (thor ? THOR_COST : 0) + (prowess ? PROWESS_COST : 0) + (priest ? PRIEST_COST : 0);
	}

	public static int getSpeed(boolean thor, boolean prowess, boolean priest) {
		return SPEED - (thor ? THOR_SPEEDUP : 0) - (prowess ? PROWESS_SPEEDUP : 0) - (priest ? PRIEST_SPEEDUP : 0);
	}

	public static float getDamage(boolean thor, boolean prowess, boolean priest) {
		return DAMAGE + (thor ? THOR_POWERUP : 0) + (prowess ? PROWESS_POWERUP : 0) + (priest ? PRIEST_POWERUP : 0);
	}

	public static double getRange(boolean thor, boolean prowess, boolean priest) {
		return CHAIN_RANGE + (thor ? THOR_RANGEUP : 0) + (prowess ? PROWESS_RANGEUP : 0) + (priest ? PRIEST_RANGEUP : 0);
	}

	public static int getTargetCap(boolean thor, boolean prowess, boolean priest) {
		return TARGET_COUNT + (thor ? THOR_TARGETS : 0) + (prowess ? PROWESS_TARGETS : 0) + (priest ? PRIEST_TARGETS : 0);
	}

	public static class AvatarBehavior implements AvatarWieldable {
		private static final double RANGE = 18;

		@Override
		public void onAvatarUpdate(Avatar tile) {
			BlockEntity te = (BlockEntity) tile;
			Level level = te.getLevel();
			if (level == null || level.isClientSide || !tile.isEnabled() || tile.getElapsedFunctionalTicks() % 100 != 0) {
				return;
			}
			ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(level, te.getBlockPos(), te.getBlockState(), te, null);
			if (receiver == null || receiver.getCurrentMana() < COST_AVATAR) {
				return;
			}
			BlockPos pos = te.getBlockPos();
			List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(RANGE),
					e -> e instanceof Enemy && !(e instanceof Player) && !(e instanceof GaiaGuardianEntity) && e.isAlive());
			if (entities.isEmpty()) {
				return;
			}
			LivingEntity target = entities.get(level.random.nextInt(entities.size()));
			DamageSource source = level.damageSources().lightningBolt();
			target.hurt(source, DAMAGE);
			level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.BLOCKS, 2F, 0.8F + level.random.nextFloat() * 0.2F);
			IntList hit = new IntArrayList();
			hit.add(target.getId());
			chain(level, target, source, CHAIN_RANGE, TARGET_COUNT, DAMAGE, hit);
			receiver.receiveMana(-COST_AVATAR);
			XplatAbstractions.INSTANCE.sendToNear(level, pos, new BotaniaEffectPacket(EffectType.THUNDERCALLER_EFFECT,
					pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, hit.toArray(new int[0])));
		}

		@Override
		public ResourceLocation getOverlayResource(Avatar tile) {
			return AVATAR_OVERLAY;
		}
	}
}
