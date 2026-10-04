package velrondevs.botania.network.serverbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.item.equipment.bauble.CirrusAmuletItem;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class JumpPacket implements BotaniaPacket {
	public static final JumpPacket INSTANCE = new JumpPacket();
	public static final ResourceLocation ID = prefix("jmp");
	public static final Type<JumpPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, JumpPacket> STREAM_CODEC = StreamCodec.ofMember(JumpPacket::encode, JumpPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {

	}

	@Override
	public Type<JumpPacket> type() {
		return TYPE;
	}

	public static JumpPacket decode(RegistryFriendlyByteBuf buf) {
		return INSTANCE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			ItemStack amuletStack = EquipmentHandler.findOrEmpty(s -> s.getItem() instanceof CirrusAmuletItem, player);
			if (!amuletStack.isEmpty()) {
				player.causeFoodExhaustion(0.3F);
				player.fallDistance = 0;

				CirrusAmuletItem.setJumping(player);
			}
		});
	}
}
