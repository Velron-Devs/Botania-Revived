package velrondevs.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.client.core.SkyblockWorldInfo;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class GogWorldPacket implements BotaniaPacket {
	public static final GogWorldPacket INSTANCE = new GogWorldPacket();
	public static final ResourceLocation ID = prefix("gog");
	public static final Type<GogWorldPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, GogWorldPacket> STREAM_CODEC = StreamCodec.ofMember(GogWorldPacket::encode, GogWorldPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {}

	@Override
	public Type<GogWorldPacket> type() {
		return TYPE;
	}

	public static GogWorldPacket decode(RegistryFriendlyByteBuf buf) {
		return INSTANCE;
	}

	public static class Handler {
		public static void handle(GogWorldPacket packet) {
			Minecraft.getInstance().execute(() -> {
				if (Minecraft.getInstance().level.getLevelData() instanceof SkyblockWorldInfo skyblockInfo) {
					skyblockInfo.markGardenOfGlass();
				}
			});
		}
	}
}
