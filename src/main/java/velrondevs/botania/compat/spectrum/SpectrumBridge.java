package velrondevs.botania.compat.spectrum;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;

import velrondevs.botania.network.BotaniaPacket;
import velrondevs.botania.xplat.XplatAbstractions;

public final class SpectrumBridge {

	private SpectrumBridge() {
	}

	public static void sendToTracking(XplatAbstractions xplat, Entity entity, CustomPacketPayload packet) {
		if (entity.level().isClientSide) {
			return;
		}
		if (packet instanceof BlackLotusDissolveEffectPacket dissolve) {
			xplat.sendToTracking(entity, dissolve.toEffectPacket());
		} else if (packet instanceof BotaniaPacket botaniaPacket) {
			xplat.sendToTracking(entity, botaniaPacket);
		} else {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, packet);
		}
	}

}
