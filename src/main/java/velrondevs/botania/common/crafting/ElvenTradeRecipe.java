package velrondevs.botania.common.crafting;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ElvenTradeRecipe implements velrondevs.botania.api.recipe.ElvenTradeRecipe {
	private final ImmutableList<ItemStack> outputs;
	private final NonNullList<Ingredient> inputs;

	public ElvenTradeRecipe(ItemStack[] outputs, Ingredient... inputs) {
		this.outputs = ImmutableList.copyOf(outputs);
		this.inputs = NonNullList.create();
		this.inputs.addAll(Arrays.asList(inputs));
	}

	@Override
	public Optional<List<ItemStack>> match(List<ItemStack> stacks) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);
		List<ItemStack> stacksToRemove = new ArrayList<>();

		for (ItemStack stack : stacks) {
			if (stack.isEmpty()) {
				continue;
			}
			if (inputsMissing.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int i = 0; i < inputsMissing.size(); i++) {
				Ingredient ingr = inputsMissing.get(i);
				if (ingr.test(stack)) {
					if (!stacksToRemove.contains(stack)) {
						stacksToRemove.add(stack);
					}
					stackIndex = i;
					break;
				}
			}

			if (stackIndex != -1) {
				inputsMissing.remove(stackIndex);
			}
		}

		return inputsMissing.isEmpty() ? Optional.of(stacksToRemove) : Optional.empty();
	}

	@Override
	public boolean containsItem(ItemStack stack) {
		for (Ingredient input : inputs) {
			if (input.test(stack)) {
				return true;
			}
		}
		return false;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.ELVEN_TRADE_SERIALIZER;
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@NotNull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.alfPortal);
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputs;
	}

	@Override
	public List<ItemStack> getOutputs(List<ItemStack> inputs) {
		return getOutputs();
	}

	public static class Serializer implements RecipeSerializer<ElvenTradeRecipe> {
		private static final Codec<List<ItemStack>> OUTPUTS_CODEC = Codec.either(ItemStack.STRICT_CODEC.listOf(), ItemStack.STRICT_CODEC)
				.xmap(either -> either.map(list -> list, List::of), Either::left);
		public static final MapCodec<ElvenTradeRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				OUTPUTS_CODEC.fieldOf("output").forGetter(ElvenTradeRecipe::getOutputs),
				Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(ElvenTradeRecipe::getIngredients)
		).apply(instance, Serializer::create));
		public static final StreamCodec<RegistryFriendlyByteBuf, ElvenTradeRecipe> STREAM_CODEC = StreamCodec.composite(
				RecipeUtils.INGREDIENTS_STREAM_CODEC, ElvenTradeRecipe::getIngredients,
				ItemStack.LIST_STREAM_CODEC, ElvenTradeRecipe::getOutputs,
				(inputs, outputs) -> new ElvenTradeRecipe(outputs.toArray(new ItemStack[0]), inputs.toArray(new Ingredient[0])));

		private static ElvenTradeRecipe create(List<ItemStack> outputs, List<Ingredient> inputs) {
			return new ElvenTradeRecipe(outputs.toArray(new ItemStack[0]),
					inputs.stream().filter(ing -> !ing.isEmpty()).toArray(Ingredient[]::new));
		}

		@Override
		public MapCodec<ElvenTradeRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ElvenTradeRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
