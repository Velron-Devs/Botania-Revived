package velrondevs.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.List;

public class PetalsRecipe implements PetalApothecaryRecipe {
	private final ItemStack output;
	private final Ingredient reagent;
	private final NonNullList<Ingredient> inputs;

	public PetalsRecipe(ItemStack output, Ingredient reagent, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 16, "Cannot have more than 16 ingredients");
		this.output = output;
		this.reagent = reagent;
		this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
	}

	@Override
	public Ingredient getReagent() {
		return reagent;
	}

	@Override
	public boolean matches(RecipeInput inv, @NotNull Level world) {
		List<Ingredient> ingredientsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.size(); i++) {
			ItemStack input = inv.getItem(i);
			if (input.isEmpty()) {
				break;
			}

			int stackIndex = -1;

			for (int j = 0; j < ingredientsMissing.size(); j++) {
				Ingredient ingr = ingredientsMissing.get(j);
				if (ingr.test(input)) {
					stackIndex = j;
					break;
				}
			}

			if (stackIndex != -1) {
				ingredientsMissing.remove(stackIndex);
			} else {
				return false;
			}
		}

		return ingredientsMissing.isEmpty();
	}

	@NotNull
	@Override
	public final ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return output;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull RecipeInput inv, @NotNull HolderLookup.Provider registries) {
		return getResultItem(registries).copy();
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@NotNull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.defaultAltar);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.PETAL_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<PetalsRecipe> {
		public static final MapCodec<PetalsRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ItemStack.STRICT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
				Ingredient.CODEC.fieldOf("reagent").forGetter(recipe -> recipe.reagent),
				RecipeUtils.INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(recipe -> recipe.inputs)
		).apply(instance, (output, reagent, inputs) -> new PetalsRecipe(output, reagent, inputs.toArray(new Ingredient[0]))));
		public static final StreamCodec<RegistryFriendlyByteBuf, PetalsRecipe> STREAM_CODEC = StreamCodec.of(
				Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<PetalsRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PetalsRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static PetalsRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			Ingredient[] inputs = RecipeUtils.INGREDIENTS_STREAM_CODEC.decode(buf).toArray(new Ingredient[0]);
			Ingredient reagent = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
			ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
			return new PetalsRecipe(output, reagent, inputs);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buf, PetalsRecipe recipe) {
			RecipeUtils.INGREDIENTS_STREAM_CODEC.encode(buf, recipe.getIngredients());
			Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.reagent);
			ItemStack.STREAM_CODEC.encode(buf, recipe.output);
		}
	}

}
