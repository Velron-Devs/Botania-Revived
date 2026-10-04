package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Supplier;

public class NoOpRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public NoOpRecipeSerializer(Supplier<T> constructor) {
		this.codec = MapCodec.unit(constructor);
		this.streamCodec = StreamCodec.of((buf, recipe) -> {}, buf -> constructor.get());
	}

	@Override
	public MapCodec<T> codec() {
		return codec;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return streamCodec;
	}
}
