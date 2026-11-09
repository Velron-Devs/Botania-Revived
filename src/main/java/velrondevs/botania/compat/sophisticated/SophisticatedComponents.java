package velrondevs.botania.compat.sophisticated;

import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.util.ExtraCodecs;

import java.util.HashMap;
import java.util.Map;

public final class SophisticatedComponents {
	public static final DataComponentType<Integer> MANA = DataComponentType.<Integer>builder()
			.persistent(ExtraCodecs.NON_NEGATIVE_INT)
			.networkSynchronized(ByteBufCodecs.VAR_INT)
			.build();

	public static final DataComponentType<StationState> STATION = DataComponentType.<StationState>builder()
			.persistent(StationState.CODEC)
			.networkSynchronized(StationState.STREAM_CODEC)
			.build();

	public static final DataComponentType<Boolean> PILE_DISCHARGE = DataComponentType.<Boolean>builder()
			.persistent(Codec.BOOL)
			.networkSynchronized(ByteBufCodecs.BOOL)
			.build();

	public static final DataComponentType<Boolean> PILE_INFUSION = DataComponentType.<Boolean>builder()
			.persistent(Codec.BOOL)
			.networkSynchronized(ByteBufCodecs.BOOL)
			.build();

	public static final DataComponentType<Map<Item, Integer>> PILE_INFUSED = DataComponentType.<Map<Item, Integer>>builder()
			.persistent(Codec.unboundedMap(BuiltInRegistries.ITEM.byNameCodec(), ExtraCodecs.POSITIVE_INT))
			.networkSynchronized(ByteBufCodecs.map(HashMap::new, ByteBufCodecs.registry(Registries.ITEM), ByteBufCodecs.VAR_INT))
			.build();

	private SophisticatedComponents() {}
}
