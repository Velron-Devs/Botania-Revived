package velrondevs.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.gui.ItemsRemainingRenderHandler;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record UpdateItemsRemainingPacket(ItemStack stack, int count, @Nullable Component tooltip) implements BotaniaPacket {

	public static final ResourceLocation ID = prefix("rem");
	public static final Type<UpdateItemsRemainingPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateItemsRemainingPacket> STREAM_CODEC = StreamCodec.ofMember(UpdateItemsRemainingPacket::encode, UpdateItemsRemainingPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, stack);
		buf.writeVarInt(count);
		buf.writeBoolean(tooltip != null);
		if (tooltip != null) {
			ComponentSerialization.TRUSTED_STREAM_CODEC.encode(buf, tooltip);
		}
	}

	@Override
	public Type<UpdateItemsRemainingPacket> type() {
		return TYPE;
	}

	public static UpdateItemsRemainingPacket decode(RegistryFriendlyByteBuf buf) {
		return new UpdateItemsRemainingPacket(
				ItemStack.OPTIONAL_STREAM_CODEC.decode(buf),
				buf.readVarInt(),
				buf.readBoolean() ? ComponentSerialization.TRUSTED_STREAM_CODEC.decode(buf) : null
		);
	}

	public static class Handler {
		public static void handle(UpdateItemsRemainingPacket packet) {
			ItemStack stack = packet.stack();
			int count = packet.count();
			Component tooltip = packet.tooltip();
			Minecraft.getInstance().execute(() -> ItemsRemainingRenderHandler.set(stack, count, tooltip));
		}
	}
}
