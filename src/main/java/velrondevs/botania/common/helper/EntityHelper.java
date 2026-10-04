package velrondevs.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;

public class EntityHelper {

	public static void shrinkItem(ItemEntity entity) {
		entity.getItem().shrink(1);
		syncItem(entity);
	}

	public static void syncItem(ItemEntity entity) {
		var save = entity.getItem();
		entity.setItem(ItemStack.EMPTY);
		entity.setItem(save);
	}

	public static void addStaticEffect(LivingEntity living, Holder<MobEffect> mobEffect, int amplifier) {
		MobEffectInstance effect = living.getEffect(mobEffect);
		if (effect == null || effect.getAmplifier() < amplifier
				|| effect.getAmplifier() == amplifier && !effect.isInfiniteDuration()) {
			living.addEffect(new MobEffectInstance(mobEffect, MobEffectInstance.INFINITE_DURATION,
					amplifier, false, true, true));
		}
	}

	public static void convertStaticEffectToFinite(LivingEntity living, Holder<MobEffect> mobEffect, int expectedAmplifier, int duration) {
		MobEffectInstance effect = living.getEffect(mobEffect);
		if (effect != null && effect.getAmplifier() == expectedAmplifier && effect.isInfiniteDuration()) {
			living.removeEffect(mobEffect);
			living.addEffect(new MobEffectInstance(mobEffect, duration, expectedAmplifier, false, true, true));
		}
	}

	public static void removeStaticEffect(LivingEntity living, Holder<MobEffect> mobEffect, int expectedAmplifier) {
		MobEffectInstance effect = living.getEffect(mobEffect);
		if (effect != null && effect.getAmplifier() == expectedAmplifier && effect.isInfiniteDuration()) {
			living.removeEffect(mobEffect);
		}
	}

	public static void addTeleportTicketIfFarAway(Entity entity, BlockPos sourcePos) {
		ChunkPos entityChunk = new ChunkPos(entity.blockPosition());
		ChunkPos sourceChunk = new ChunkPos(sourcePos);
		if (entity.level() instanceof ServerLevel serverLevel && entityChunk.getChessboardDistance(sourceChunk) > 2) {
			serverLevel.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, entityChunk, 0, entity.getId());
		}
	}

	public static InteractionHand otherHand(InteractionHand hand) {
		return hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
	}
}
