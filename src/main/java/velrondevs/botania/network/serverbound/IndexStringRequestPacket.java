package velrondevs.botania.network.serverbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import velrondevs.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record IndexStringRequestPacket(String message) implements BotaniaPacket {
	public static final ResourceLocation ID = prefix("idxs");
	public static final Type<IndexStringRequestPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, IndexStringRequestPacket> STREAM_CODEC = StreamCodec.ofMember(IndexStringRequestPacket::encode, IndexStringRequestPacket::decode);

	public static IndexStringRequestPacket decode(RegistryFriendlyByteBuf buf) {
		return new IndexStringRequestPacket(buf.readUtf());
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeUtf(message);
	}

	@Override
	public Type<IndexStringRequestPacket> type() {
		return TYPE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> CorporeaIndexBlockEntity.onChatMessage(player, message()));
	}
}
