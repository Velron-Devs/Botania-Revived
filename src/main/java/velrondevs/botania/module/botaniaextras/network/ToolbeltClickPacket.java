package velrondevs.botania.module.botaniaextras.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import velrondevs.botania.module.botaniaextras.item.ToolbeltItem;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record ToolbeltClickPacket(int segment) implements BotaniaPacket {
	public static final ResourceLocation ID = prefix("toolbelt_click");
	public static final Type<ToolbeltClickPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, ToolbeltClickPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, ToolbeltClickPacket::segment, ToolbeltClickPacket::new);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		STREAM_CODEC.encode(buf, this);
	}

	@Override
	public Type<ToolbeltClickPacket> type() {
		return TYPE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> ToolbeltItem.interact(player, segment));
	}
}
