package velrondevs.botania.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Optional;

public class OrechidRecipe implements velrondevs.botania.api.recipe.OrechidRecipe {
	private final StateIngredient input;
	private final StateIngredient output;
	private final int weight;
	private final Optional<CacheableFunction> successFunction;

	public OrechidRecipe(StateIngredient input, StateIngredient output, int weight, Optional<CacheableFunction> successFunction) {
		this.input = input;
		this.output = output;
		this.weight = weight;
		this.successFunction = successFunction;
	}

	@Override
	public StateIngredient getInput() {
		return input;
	}

	@Override
	public StateIngredient getOutput() {
		return output;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public Optional<CacheableFunction> getSuccessFunction() {
		return this.successFunction;
	}

	@NotNull
	@Override
	public RecipeType<? extends OrechidRecipe> getType() {
		return BotaniaRecipeTypes.ORECHID_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.ORECHID_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<OrechidRecipe> {
		public static final MapCodec<OrechidRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				StateIngredientHelper.NON_AIR_CODEC.fieldOf("input").forGetter(OrechidRecipe::getInput),
				StateIngredientHelper.NON_AIR_CODEC.fieldOf("output").forGetter(OrechidRecipe::getOutput),
				Codec.INT.fieldOf("weight").forGetter(OrechidRecipe::getWeight),
				CacheableFunction.CODEC.optionalFieldOf("success_function").forGetter(OrechidRecipe::getSuccessFunction)
		).apply(instance, OrechidRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, OrechidRecipe> STREAM_CODEC = StreamCodec.composite(
				StateIngredientHelper.STREAM_CODEC, OrechidRecipe::getInput,
				StateIngredientHelper.STREAM_CODEC, OrechidRecipe::getOutput,
				ByteBufCodecs.VAR_INT, OrechidRecipe::getWeight,
				(input, output, weight) -> new OrechidRecipe(input, output, weight, Optional.empty()));

		@Override
		public MapCodec<OrechidRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, OrechidRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
