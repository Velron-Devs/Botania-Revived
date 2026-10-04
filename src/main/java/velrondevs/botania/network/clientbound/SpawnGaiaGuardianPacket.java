package velrondevs.botania.network.clientbound;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import velrondevs.botania.common.entity.GaiaGuardianEntity;
import velrondevs.botania.network.BotaniaPacket;

import java.util.UUID;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public record SpawnGaiaGuardianPacket(ClientboundAddEntityPacket inner, int playerCount, boolean hardMode,
		BlockPos source, UUID bossInfoId) implements BotaniaPacket {

	public static final ResourceLocation ID = prefix("spg");
	public static final Type<SpawnGaiaGuardianPacket> TYPE = new Type<>(ID);
	public static final StreamCodec<RegistryFriendlyByteBuf, SpawnGaiaGuardianPacket> STREAM_CODEC = StreamCodec.ofMember(SpawnGaiaGuardianPacket::encode, SpawnGaiaGuardianPacket::decode);

	@Override
	public void encode(RegistryFriendlyByteBuf buf) {
		ClientboundAddEntityPacket.STREAM_CODEC.encode(buf, inner());
		buf.writeVarInt(playerCount());
		buf.writeBoolean(hardMode());
		buf.writeBlockPos(source());
		buf.writeUUID(bossInfoId());
	}

	@Override
	public Type<SpawnGaiaGuardianPacket> type() {
		return TYPE;
	}

	public static SpawnGaiaGuardianPacket decode(RegistryFriendlyByteBuf buf) {
		return new SpawnGaiaGuardianPacket(
				ClientboundAddEntityPacket.STREAM_CODEC.decode(buf),
				buf.readVarInt(),
				buf.readBoolean(),
				buf.readBlockPos(),
				buf.readUUID()
		);
	}

	public static class Handler {
		public static void handle(SpawnGaiaGuardianPacket packet) {
			var inner = packet.inner();
			int playerCount = packet.playerCount();
			boolean hardMode = packet.hardMode();
			BlockPos source = packet.source();
			UUID bossInfoUuid = packet.bossInfoId();

			Minecraft.getInstance().execute(() -> {
				var player = Minecraft.getInstance().player;
				if (player != null) {
					player.connection.handleAddEntity(inner);
					Entity e = player.level().getEntity(inner.getId());
					if (e instanceof GaiaGuardianEntity dopple) {
						dopple.readSpawnData(playerCount, hardMode, source, bossInfoUuid);
					}
				}
			});
		}
	}
}
