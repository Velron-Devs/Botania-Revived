package velrondevs.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;

import velrondevs.botania.network.BotaniaPacket;
import velrondevs.botania.xplat.XplatAbstractions;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record ItemAgePacket(int entityId, int timeCounter) implements BotaniaPacket {

	public static final ResourceLocation ID = prefix("ia");
	public static final Type<ItemAgePacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, ItemAgePacket> STREAM_CODEC = StreamCodec.ofMember(ItemAgePacket::encode, ItemAgePacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeVarInt(entityId());
		buf.writeVarInt(timeCounter());
	}

	@Override
	public Type<ItemAgePacket> type() {
		return TYPE;
	}

	public static ItemAgePacket decode(RegistryFriendlyByteBuf buf) {
		return new ItemAgePacket(buf.readVarInt(), buf.readVarInt());
	}

	public static class Handler {
		public static void handle(ItemAgePacket packet) {
			int entityId = packet.entityId();
			int counter = packet.timeCounter();
			Minecraft.getInstance().execute(() -> {
				Entity e = Minecraft.getInstance().level.getEntity(entityId);
				if (e instanceof ItemEntity item) {
					XplatAbstractions.INSTANCE.itemFlagsComponent(item).timeCounter = counter;
				}
			});
		}
	}

}
