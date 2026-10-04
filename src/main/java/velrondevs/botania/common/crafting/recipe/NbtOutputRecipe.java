package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

import velrondevs.botania.common.helper.ItemNBTHelper;

public class NbtOutputRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new NbtOutputRecipe.Serializer();

	private static class Serializer implements RecipeSerializer<Recipe<?>> {
		private static final MapCodec<Recipe<?>> CODEC = RecordCodecBuilder.<Recipe<?>>mapCodec(instance -> instance.group(
				Recipe.CODEC.fieldOf("recipe").forGetter(recipe -> recipe),
				TagParser.AS_CODEC.fieldOf("nbt").forGetter(recipe -> ItemNBTHelper.getTagCopy(recipe.getResultItem(RegistryAccess.EMPTY)))
		).apply(instance, (recipe, tag) -> {
			ItemNBTHelper.setTag(recipe.getResultItem(RegistryAccess.EMPTY), tag);
			return recipe;
		}));
		private static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC = StreamCodec.of(
				(buf, recipe) -> {
					throw new IllegalStateException("NbtOutputRecipe should not be sent over network");
				},
				buf -> {
					throw new IllegalStateException("NbtOutputRecipe should not be sent over network");
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
