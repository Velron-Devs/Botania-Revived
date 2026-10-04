package velrondevs.botania.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.brew.BrewContainer;
import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BotanicalBreweryRecipe implements velrondevs.botania.api.recipe.BotanicalBreweryRecipe {
	private final Brew brew;
	private final NonNullList<Ingredient> inputs;

	public BotanicalBreweryRecipe(Brew brew, Ingredient... inputs) {
		this.brew = brew;
		this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
	}

	@Override
	public boolean matches(RecipeInput inv, @NotNull Level world) {
		List<Ingredient> inputsMissing = new ArrayList<>(inputs);

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty()) {
				break;
			}

			if (stack.getItem() instanceof BrewContainer) {
				continue;
			}

			boolean matchedOne = false;

			Iterator<Ingredient> iter = inputsMissing.iterator();
			while (iter.hasNext()) {
				Ingredient input = iter.next();
				if (input.test(stack)) {
					iter.remove();
					matchedOne = true;
					break;
				}
			}

			if (!matchedOne) {
				return false;
			}
		}

		return inputsMissing.isEmpty();
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@NotNull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.brewery);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.BREW_SERIALIZER;
	}

	@Override
	public Brew getBrew() {
		return brew;
	}

	@Override
	public int getManaUsage() {
		return brew.getManaCost();
	}

	@Override
	public ItemStack getOutput(ItemStack stack) {
		if (stack.isEmpty() || !(stack.getItem() instanceof BrewContainer container)) {
			return new ItemStack(Items.GLASS_BOTTLE);
		}

		return container.getItemForBrew(brew, stack);
	}

	@Override
	public int hashCode() {
		return 31 * brew.hashCode() ^ inputs.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof BotanicalBreweryRecipe brewRecipe
				&& brew == brewRecipe.brew
				&& inputs.equals(brewRecipe.inputs);
	}

	public static class Serializer implements RecipeSerializer<BotanicalBreweryRecipe> {
		public static final Codec<Brew> BREW_CODEC = ResourceLocation.CODEC.comapFlatMap(
				id -> BotaniaAPI.instance().getBrewRegistry().getOptional(id)
						.map(DataResult::success)
						.orElseGet(() -> DataResult.error(() -> "Unknown brew " + id)),
				brew -> BotaniaAPI.instance().getBrewRegistry().getKey(brew));
		public static final StreamCodec<RegistryFriendlyByteBuf, Brew> BREW_STREAM_CODEC = ResourceLocation.STREAM_CODEC.map(
				id -> BotaniaAPI.instance().getBrewRegistry().get(id),
				brew -> BotaniaAPI.instance().getBrewRegistry().getKey(brew)).cast();
		public static final MapCodec<BotanicalBreweryRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				BREW_CODEC.fieldOf("brew").forGetter(BotanicalBreweryRecipe::getBrew),
				RecipeUtils.INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(BotanicalBreweryRecipe::getIngredients)
		).apply(instance, (brew, inputs) -> new BotanicalBreweryRecipe(brew, inputs.toArray(new Ingredient[0]))));
		public static final StreamCodec<RegistryFriendlyByteBuf, BotanicalBreweryRecipe> STREAM_CODEC = StreamCodec.composite(
				BREW_STREAM_CODEC, BotanicalBreweryRecipe::getBrew,
				RecipeUtils.INGREDIENTS_STREAM_CODEC, BotanicalBreweryRecipe::getIngredients,
				(brew, inputs) -> new BotanicalBreweryRecipe(brew, inputs.toArray(new Ingredient[0])));

		@Override
		public MapCodec<BotanicalBreweryRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, BotanicalBreweryRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
