package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import velrondevs.botania.xplat.XplatAbstractions;

public class GogAlternationRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new Serializer();

	private record Alternatives(Recipe<?> gog, Recipe<?> base) {}

	private static class Serializer implements RecipeSerializer<Recipe<?>> {
		private static final MapCodec<Alternatives> ALTERNATIVES_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				Recipe.CODEC.fieldOf("gog").forGetter(Alternatives::gog),
				Recipe.CODEC.fieldOf("base").forGetter(Alternatives::base)
		).apply(instance, Alternatives::new));
		private static final MapCodec<Recipe<?>> CODEC = ALTERNATIVES_CODEC.flatXmap(
				alternatives -> {
					if (alternatives.gog().getType() != alternatives.base().getType()) {
						return DataResult.error(() -> "Subrecipes must have matching types");
					}
					return DataResult.success(XplatAbstractions.INSTANCE.gogLoaded() ? alternatives.gog() : alternatives.base());
				},
				recipe -> DataResult.error(() -> "GogAlternationRecipe cannot be encoded"));
		private static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = StreamCodec.of(
				(buf, recipe) -> {
					throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
				},
				buf -> {
					throw new IllegalStateException("GogAlternationRecipe should not be sent over network");
				});

		@Override
		public MapCodec<Recipe<?>> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
