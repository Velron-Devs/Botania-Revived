package velrondevs.botania.module.botaniaextras.entity;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;

public class ManasealCreeperEntity extends Creeper {
	public static final int RANGE = 3;
	public static final int DURATION = 120;
	public static final int CHARGED_DURATION = 1200;

	public ManasealCreeperEntity(EntityType<? extends Creeper> type, Level level) {
		super(type, level);
		CompoundTag tag = new CompoundTag();
		tag.putByte("ExplosionRadius", (byte) 1);
		super.readAdditionalSaveData(tag);
	}

	@Override
	public void remove(RemovalReason reason) {
		if (reason == RemovalReason.DISCARDED && dead && !level().isClientSide) {
			stormNearbyPlayers();
		}
		super.remove(reason);
	}

	private void stormNearbyPlayers() {
		boolean charged = isPowered();
		double range = charged ? RANGE * 2 : RANGE;
		int duration = charged ? CHARGED_DURATION : DURATION;
		Holder<MobEffect> storm = BotaniaExtrasUtilities.MANATIDE_STORM;
		for (Player player : level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(range))) {
			player.addEffect(new MobEffectInstance(storm, duration, 0));
		}
	}
}
