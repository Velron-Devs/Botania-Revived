package velrondevs.botania.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface BotaniaPacket extends CustomPacketPayload {
	void encode(RegistryFriendlyByteBuf buf);
}
