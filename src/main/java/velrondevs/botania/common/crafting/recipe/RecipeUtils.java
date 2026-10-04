package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import it.unimi.dsi.fastutil.ints.IntSet;

import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RecipeUtils {
	public static final Codec<List<Ingredient>> INGREDIENTS_CODEC = Ingredient.CODEC_NONEMPTY.listOf();
	public static final StreamCodec<RegistryFriendlyByteBuf, List<Ingredient>> INGREDIENTS_STREAM_CODEC = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list());

	public static <T extends ShapedRecipe> RecipeSerializer<T> shapedWrapper(Function<ShapedRecipe, T> factory) {
		return wrapper(RecipeSerializer.SHAPED_RECIPE, factory);
	}

	public static <T extends ShapelessRecipe> RecipeSerializer<T> shapelessWrapper(Function<ShapelessRecipe, T> factory) {
		return wrapper(RecipeSerializer.SHAPELESS_RECIPE, factory);
	}

	private static <B extends Recipe<?>, T extends B> RecipeSerializer<T> wrapper(RecipeSerializer<B> base, Function<B, T> factory) {
		MapCodec<T> codec = base.codec().xmap(factory, recipe -> recipe);
		StreamCodec<RegistryFriendlyByteBuf, T> streamCodec = base.streamCodec().map(factory, recipe -> recipe);
		return new RecipeSerializer<>() {
			@Override
			public MapCodec<T> codec() {
				return codec;
			}

			@Override
			public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
				return streamCodec;
			}
		};
	}

	public static boolean matches(List<Ingredient> inputs, RecipeInput inv, @Nullable IntSet usedSlots) {
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
					if (usedSlots != null) {
						usedSlots.add(i);
					}
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

	public static NonNullList<ItemStack> getRemainingItemsSub(RecipeInput inv, Function<ItemStack, ItemStack> specialHandler) {
		NonNullList<ItemStack> ret = NonNullList.withSize(inv.size(), ItemStack.EMPTY);

		for (int i = 0; i < ret.size(); ++i) {
			ItemStack item = inv.getItem(i);
			ItemStack special = specialHandler.apply(item);
			if (special != null) {
				ret.set(i, special);
			} else if (item.hasCraftingRemainingItem()) {
				ret.set(i, item.getCraftingRemainingItem());
			}
		}

		return ret;
	}
}
