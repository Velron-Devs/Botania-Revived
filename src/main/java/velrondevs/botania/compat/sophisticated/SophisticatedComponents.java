package velrondevs.botania.compat.sophisticated;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

public final class SophisticatedComponents {
	public static final DataComponentType<Integer> MANA = DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.NON_NEGATIVE_INT)
			.networkSynchronized(ByteBufCodecs.VAR_INT)
			.build();

	public static final DataComponentType<StationState> STATION = DataComponentType.<StationState>builder()
			.persistent(StationState.CODEC)
			.networkSynchronized(StationState.STREAM_CODEC)
			.build();

	private SophisticatedComponents() {}
}
