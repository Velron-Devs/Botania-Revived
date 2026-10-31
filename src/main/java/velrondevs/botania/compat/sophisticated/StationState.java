package velrondevs.botania.compat.sophisticated;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record StationState(int status, int mana, int cost) {
	public static final int IDLE = 0;
	public static final int NO_PILE = 1;
	public static final int NEED_MANA = 2;
	public static final int NO_SPACE = 3;
	public static final int NO_REAGENT = 4;
	public static final int WORKING = 5;

	public static final StationState EMPTY = new StationState(IDLE, 0, 0);

	public static final Codec<StationState> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("status").forGetter(StationState::status),
			Codec.INT.fieldOf("mana").forGetter(StationState::mana),
			Codec.INT.fieldOf("cost").forGetter(StationState::cost)
	).apply(instance, StationState::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, StationState> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StationState::status,
			ByteBufCodecs.VAR_INT, StationState::mana,
			ByteBufCodecs.VAR_INT, StationState::cost,
			StationState::new);
}
