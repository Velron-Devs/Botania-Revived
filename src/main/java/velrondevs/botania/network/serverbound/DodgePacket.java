package velrondevs.botania.network.serverbound;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.equipment.bauble.RingOfDexterousMotionItem;
import velrondevs.botania.network.BotaniaPacket;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaSounds;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class DodgePacket implements BotaniaPacket {
	public static final DodgePacket INSTANCE = new DodgePacket();
	public static final ResourceLocation ID = prefix("do");
	public static final Type<DodgePacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, DodgePacket> STREAM_CODEC = StreamCodec.ofMember(DodgePacket::encode, DodgePacket::decode);

	public static DodgePacket decode(RegistryFriendlyByteBuf buf) {
		return INSTANCE;
	}

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {

	}

	@Override
	public Type<DodgePacket> type() {
		return TYPE;
	}

	public void handle(MinecraftServer server, ServerPlayer player) {
		server.execute(() -> {
			player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.dash, SoundSource.PLAYERS, 1F, 1F);

			ItemStack ringStack = EquipmentHandler.findOrEmpty(BotaniaItems.dodgeRing, player);
			if (ringStack.isEmpty()) {
				player.connection.disconnect(Component.translatable("botaniamisc.invalidDodge"));
				return;
			}

			player.causeFoodExhaustion(0.3F);
			ItemNBTHelper.setInt(ringStack, RingOfDexterousMotionItem.TAG_DODGE_COOLDOWN, RingOfDexterousMotionItem.MAX_CD);
		});
	}
}
