package velrondevs.botania.network.serverbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class LeftClickPacket implements BotaniaPacket {
	public static final LeftClickPacket INSTANCE = new LeftClickPacket();
	public static final ResourceLocation ID = prefix("lc");
	public static final Type<LeftClickPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, LeftClickPacket> STREAM_CODEC = StreamCodec.ofMember(LeftClickPacket::encode, LeftClickPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {

	}

	@Override
	public Type<LeftClickPacket> type() {
		return TYPE;
	}

	public static LeftClickPacket decode(RegistryFriendlyByteBuf buf) {
		return INSTANCE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {

		float scale = player.getAttackStrengthScale(0F);
		server.execute(() -> TerraBladeItem.trySpawnBurst(player, scale));
	}
}
