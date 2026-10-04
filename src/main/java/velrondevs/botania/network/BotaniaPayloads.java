package velrondevs.botania.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import velrondevs.botania.network.clientbound.*;
import velrondevs.botania.network.serverbound.*;

import java.util.function.Consumer;

public class BotaniaPayloads {
	public static void register(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar("1").executesOn(HandlerThread.NETWORK);

		serverbound(registrar, DodgePacket.TYPE, DodgePacket.STREAM_CODEC, DodgePacket::handle);
		serverbound(registrar, IndexKeybindRequestPacket.TYPE, IndexKeybindRequestPacket.STREAM_CODEC, IndexKeybindRequestPacket::handle);
		serverbound(registrar, IndexStringRequestPacket.TYPE, IndexStringRequestPacket.STREAM_CODEC, IndexStringRequestPacket::handle);
		serverbound(registrar, JumpPacket.TYPE, JumpPacket.STREAM_CODEC, JumpPacket::handle);
		serverbound(registrar, LeftClickPacket.TYPE, LeftClickPacket.STREAM_CODEC, LeftClickPacket::handle);

		clientbound(registrar, AvatarSkiesRodPacket.TYPE, AvatarSkiesRodPacket.STREAM_CODEC, AvatarSkiesRodPacket.Handler::handle);
		clientbound(registrar, BotaniaEffectPacket.TYPE, BotaniaEffectPacket.STREAM_CODEC, BotaniaEffectPacket.Handler::handle);
		clientbound(registrar, GogWorldPacket.TYPE, GogWorldPacket.STREAM_CODEC, GogWorldPacket.Handler::handle);
		clientbound(registrar, ItemAgePacket.TYPE, ItemAgePacket.STREAM_CODEC, ItemAgePacket.Handler::handle);
		clientbound(registrar, SpawnGaiaGuardianPacket.TYPE, SpawnGaiaGuardianPacket.STREAM_CODEC, SpawnGaiaGuardianPacket.Handler::handle);
		clientbound(registrar, UpdateItemsRemainingPacket.TYPE, UpdateItemsRemainingPacket.STREAM_CODEC, UpdateItemsRemainingPacket.Handler::handle);
	}

	private static <T extends BotaniaPacket> void serverbound(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec, TriConsumer<T, MinecraftServer, ServerPlayer> handler) {
		registrar.playToServer(type, codec, (IPayloadHandler<T>) (packet, ctx) -> {
			ServerPlayer player = (ServerPlayer) ctx.player();
			handler.accept(packet, player.getServer(), player);
		});
	}

	private static <T extends BotaniaPacket> void clientbound(PayloadRegistrar registrar, CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec, Consumer<T> handler) {
		registrar.playToClient(type, codec, (IPayloadHandler<T>) (packet, ctx) -> handler.accept(packet));
	}
}
