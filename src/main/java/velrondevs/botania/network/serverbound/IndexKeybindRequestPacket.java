package velrondevs.botania.network.serverbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.corporea.CorporeaHelper;
import velrondevs.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record IndexKeybindRequestPacket(ItemStack stack) implements BotaniaPacket {
	public static final ResourceLocation ID = prefix("idx");
	public static final Type<IndexKeybindRequestPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, IndexKeybindRequestPacket> STREAM_CODEC = StreamCodec.ofMember(IndexKeybindRequestPacket::encode, IndexKeybindRequestPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, stack());
	}

	@Override
	public Type<IndexKeybindRequestPacket> type() {
		return TYPE;
	}

	public static IndexKeybindRequestPacket decode(RegistryFriendlyByteBuf buf) {
		return new IndexKeybindRequestPacket(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		var stack = this.stack();
		server.execute(() -> {
			if (player.isSpectator()) {
				return;
			}

			for (CorporeaIndexBlockEntity index : CorporeaIndexBlockEntity.getNearbyValidIndexes(player)) {
				index.performPlayerRequest(player, CorporeaHelper.instance().createMatcher(stack, true), stack.getCount());
			}
		});
	}
}
