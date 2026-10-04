package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.block.Avatar;
import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.api.item.AvatarWieldable;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.entity.GaiaGuardianEntity;
import velrondevs.botania.module.botaniaextras.ClericalColors;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.Predicate;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class StormySeaRodItem extends Item {
	private static final ResourceLocation AVATAR_OVERLAY = prefix("textures/model/botania_extras/avatar_stormy_sea.png");

	private static final int COST = 5;
	private static final int PROWESS_COST = -1;
	private static final int PRIEST_COST = 2;
	private static final int AVATAR_COST = 4;

	private static final double VELOCITY = 0.05;
	private static final double PROWESS_VELOCITY = 0.02;
	private static final double PRIEST_VELOCITY = 0.07;

	private static final int RANGE = 5;
	private static final int PRIEST_RANGE = 2;

	private static final Predicate<Entity> PLAYER_SELECTOR = e -> (e instanceof LivingEntity && !(e instanceof GaiaGuardianEntity))
			|| (e instanceof Projectile && !(e instanceof ManaBurst));
	private static final Predicate<Entity> AVATAR_SELECTOR = e -> e instanceof LivingEntity && !(e instanceof Player) && !(e instanceof GaiaGuardianEntity);

	public StormySeaRodItem(Properties props) {
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

	public static int getCost(boolean prowess, boolean priest) {
		return COST + (prowess ? PROWESS_COST : 0) + (priest ? PRIEST_COST : 0);
	}

	public static double getVelocity(boolean prowess, boolean priest) {
		return VELOCITY + (prowess ? PROWESS_VELOCITY : 0) + (priest ? PRIEST_VELOCITY : 0);
	}

	public static int getRange(boolean priest) {
		return RANGE + (priest ? PRIEST_RANGE : 0);
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remaining) {
		if (!(living instanceof Player player)) {
			return;
		}
		boolean priest = PriestEmblemItem.hasEmblem(player, PriestEmblemItem.Type.NJORD);
		boolean prowess = ManaItemHandler.instance().hasProficiency(player, stack);
		int cost = getCost(prowess, priest);
		int range = getRange(priest);
		if (!ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, false)) {
			return;
		}
		if (level.isClientSide) {
			if (remaining % 5 == 0) {
				int color = ClericalColors.get(player, 0x0000FF);
				particleRing(level, player.getX(), player.getY(), player.getZ(), range, ClericalColors.red(color), ClericalColors.green(color), ClericalColors.blue(color));
			}
			return;
		}
		List<Entity> entities = level.getEntities(player, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range,
				player.getX() + range, player.getY() + range, player.getZ() + range), PLAYER_SELECTOR);
		if (push(player.getX(), player.getY(), player.getZ(), range, getVelocity(prowess, priest), entities)) {
			if (remaining % 3 == 0) {
				level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 0.4F, 1F);
			}
			ManaItemHandler.instance().requestManaExactForTool(stack, player, cost, true);
		}
	}

	private static void particleRing(Level level, double x, double y, double z, int range, float r, float g, float b) {
		float m = 0.15F;
		float mv = 0.35F;
		for (int i = 0; i < 360; i += 8) {
			double rad = i * Math.PI / 180.0;
			double dx = x - Math.cos(rad) * range;
			double dz = z - Math.sin(rad) * range;
			WispParticleData data = WispParticleData.wisp(0.2F, r, g, b);
			level.addParticle(data, dx, y + 0.5, dz, (Math.random() - 0.5) * m, (Math.random() - 0.5) * mv, (Math.random() - 0.5) * m);
		}
	}

	private static boolean push(double x, double y, double z, int range, double velocity, List<Entity> entities) {
		boolean pushed = false;
		for (Entity entity : entities) {
			double dx = entity.getX() - x;
			double dy = entity.getY() - (y + 1);
			double dz = entity.getZ() - z;
			double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
			if (dist <= range) {
				entity.setDeltaMovement(velocity * dx, velocity * dy, velocity * dz);
				entity.hurtMarked = true;
				entity.fallDistance = 0F;
				pushed = true;
			}
		}
		return pushed;
	}

	public static class AvatarBehavior implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(Avatar tile) {
			BlockEntity te = (BlockEntity) tile;
			Level level = te.getLevel();
			if (level == null) {
				return;
			}
			ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(level, te.getBlockPos(), te.getBlockState(), te, null);
			if (receiver == null || receiver.getCurrentMana() < AVATAR_COST || !tile.isEnabled()) {
				return;
			}
			BlockPos pos = te.getBlockPos();
			if (level.isClientSide) {
				if (tile.getElapsedFunctionalTicks() % 5 == 0) {
					particleRing(level, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, RANGE, 0F, 0F, 1F);
				}
				return;
			}
			List<Entity> entities = level.getEntities((Entity) null, new AABB(pos).inflate(RANGE), AVATAR_SELECTOR);
			if (push(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, RANGE, VELOCITY, entities)) {
				if (tile.getElapsedFunctionalTicks() % 3 == 0) {
					level.playSound(null, pos, SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.BLOCKS, 0.4F, 1F);
				}
				receiver.receiveMana(-AVATAR_COST);
			}
		}

		@Override
		public ResourceLocation getOverlayResource(Avatar tile) {
			return AVATAR_OVERLAY;
		}
	}
}
