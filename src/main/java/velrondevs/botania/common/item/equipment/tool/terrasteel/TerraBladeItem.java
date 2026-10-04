package velrondevs.botania.common.item.equipment.tool.terrasteel;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.internal.ManaBurst;
import velrondevs.botania.api.mana.BurstProperties;
import velrondevs.botania.api.mana.LensEffectItem;
import velrondevs.botania.common.entity.ManaBurstEntity;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelSwordItem;
import velrondevs.botania.network.serverbound.LeftClickPacket;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.ClientXplatAbstractions;

import java.util.List;

public class TerraBladeItem extends ManasteelSwordItem implements LensEffectItem {

	private static final int MANA_PER_DAMAGE = 100;

	public TerraBladeItem(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), props);
	}

	public static void leftClick(ItemStack stack) {
		if (!stack.isEmpty() && stack.getItem() instanceof TerraBladeItem) {
			ClientXplatAbstractions.INSTANCE.sendToServer(LeftClickPacket.INSTANCE);
		}
	}

	public static InteractionResult attackEntity(Player player, Level world, InteractionHand hand, Entity target, @Nullable EntityHitResult hit) {
		if (!player.level().isClientSide) {
			trySpawnBurst(player);
		}
		return InteractionResult.PASS;
	}

	public static void trySpawnBurst(Player player) {
		trySpawnBurst(player, player.getAttackStrengthScale(0F));
	}

	public static void trySpawnBurst(Player player, float attackStrength) {
		if (!player.isSpectator()
				&& !player.getMainHandItem().isEmpty()
				&& player.getMainHandItem().is(BotaniaItems.terraSword)
				&& attackStrength == 1) {
			ManaBurstEntity burst = getBurst(player, player.getMainHandItem());
			player.level().addFreshEntity(burst);
			player.getMainHandItem().hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.terraBlade, SoundSource.PLAYERS, 1F, 1F);
		}
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	public static ManaBurstEntity getBurst(Player player, ItemStack stack) {
		ManaBurstEntity burst = new ManaBurstEntity(player);

		float motionModifier = 7F;

		burst.setColor(0x20FF20);
		burst.setMana(MANA_PER_DAMAGE);
		burst.setStartingMana(MANA_PER_DAMAGE);
		burst.setMinManaLoss(40);
		burst.setManaLossPerTick(4F);
		burst.setGravity(0F);
		burst.setDeltaMovement(burst.getDeltaMovement().scale(motionModifier));

		burst.setSourceLens(stack.copy());
		return burst;
	}

	@Override
	public void apply(ItemStack stack, BurstProperties props, Level level) {}

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		return shouldKill;
	}

	@Override
	public void updateBurst(ManaBurst burst, ItemStack stack) {
		Projectile entity = burst.entity();
		AABB axis = new AABB(entity.getX(), entity.getY(), entity.getZ(), entity.xOld, entity.yOld, entity.zOld).inflate(1);
		List<LivingEntity> entities = entity.level().getEntitiesOfClass(LivingEntity.class, axis);
		Entity thrower = entity.getOwner();

		for (LivingEntity living : entities) {
			if (living == thrower || living instanceof Player livingPlayer && thrower instanceof Player throwingPlayer
					&& !throwingPlayer.canHarmPlayer(livingPlayer)) {
				continue;
			}

			if (living.hurtTime == 0) {
				int cost = MANA_PER_DAMAGE / 3;
				int mana = burst.getMana();
				if (mana >= cost) {
					burst.setMana(mana - cost);
					float damage = 4F + BotaniaAPI.instance().getTerrasteelItemTier().getAttackDamageBonus();
					if (!burst.isFake() && !entity.level().isClientSide) {
						DamageSource source = living.damageSources().magic();
						if (thrower instanceof Player player) {
							source = player.damageSources().playerAttack(player);
						} else if (thrower instanceof LivingEntity livingEntity) {
							source = livingEntity.damageSources().mobAttack(livingEntity);
						}
						living.hurt(source, damage);
						entity.discard();
						break;
					}
				}
			}
		}
	}

	@Override
	public boolean doParticles(ManaBurst burst, ItemStack stack) {
		return true;
	}
}
