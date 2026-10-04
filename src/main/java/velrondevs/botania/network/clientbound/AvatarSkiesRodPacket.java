package velrondevs.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.common.item.rod.SkiesRodItem;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record AvatarSkiesRodPacket(boolean elytra) implements BotaniaPacket {
	public static final ResourceLocation ID = prefix("atr");
	public static final Type<AvatarSkiesRodPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, AvatarSkiesRodPacket> STREAM_CODEC = StreamCodec.ofMember(AvatarSkiesRodPacket::encode, AvatarSkiesRodPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		buf.writeBoolean(elytra);
	}

	@Override
	public Type<AvatarSkiesRodPacket> type() {
		return TYPE;
	}

	public static AvatarSkiesRodPacket decode(RegistryFriendlyByteBuf buf) {
		return new AvatarSkiesRodPacket(buf.readBoolean());
	}

	public static class Handler {
		public static void handle(AvatarSkiesRodPacket packet) {
			boolean elytra = packet.elytra();

			Minecraft.getInstance().execute(
					new Runnable() {
						@Override
						public void run() {
							var player = Minecraft.getInstance().player;
							var world = Minecraft.getInstance().level;
							if (elytra) {
								SkiesRodItem.doAvatarElytraBoost(player, world);
							} else {
								SkiesRodItem.doAvatarJump(player, world);
							}
						}
					}

			);
		}
	}
}
