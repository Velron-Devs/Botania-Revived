package velrondevs.botania.compat.spectrum;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;

public record BlackLotusDissolveEffectPacket(BlockPos pos) implements CustomPacketPayload {

	public BotaniaEffectPacket toEffectPacket() {
		return new BotaniaEffectPacket(EffectType.BLACK_LOTUS_DISSOLVE, pos.getX(), pos.getY() + 0.5, pos.getZ());
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return BotaniaEffectPacket.TYPE;
	}

}
