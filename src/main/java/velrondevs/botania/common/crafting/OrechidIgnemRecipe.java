package velrondevs.botania.common.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Optional;

public class OrechidIgnemRecipe extends OrechidRecipe {
	public OrechidIgnemRecipe(StateIngredient input, StateIngredient output, int weight, Optional<CacheableFunction> successFunction) {
		super(input, output, weight, successFunction);
	}

	private OrechidIgnemRecipe(OrechidRecipe recipe) {
		this(recipe.getInput(), recipe.getOutput(), recipe.getWeight(), recipe.getSuccessFunction());
	}

	@NotNull
	@Override
	public RecipeType<? extends OrechidIgnemRecipe> getType() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.ORECHID_IGNEM_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<OrechidIgnemRecipe> {
		public static final MapCodec<OrechidIgnemRecipe> CODEC = OrechidRecipe.Serializer.CODEC
				.xmap(OrechidIgnemRecipe::new, recipe -> recipe);
		public static final StreamCodec<RegistryFriendlyByteBuf, OrechidIgnemRecipe> STREAM_CODEC = OrechidRecipe.Serializer.STREAM_CODEC
				.map(OrechidIgnemRecipe::new, recipe -> recipe);

		@Override
		public MapCodec<OrechidIgnemRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, OrechidIgnemRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
