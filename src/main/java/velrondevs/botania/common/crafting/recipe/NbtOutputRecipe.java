package velrondevs.botania.common.crafting.recipe;

import velrondevs.botania.common.crafting.BotaniaRecipeSerializer;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;


public class NbtOutputRecipe {
	public static final RecipeSerializer<Recipe<?>> SERIALIZER = new NbtOutputRecipe.Serializer().toVanilla();

	private static class Serializer implements BotaniaRecipeSerializer<Recipe<?>> {
		private static final MapCodec<Recipe<?>> CODEC = RecordCodecBuilder.<Recipe<?>>mapCodec(instance -> instance.group(
				Recipe.CODEC.fieldOf("recipe").forGetter(recipe -> recipe),
				TagParser.LENIENT_CODEC.optionalFieldOf("nbt", new CompoundTag()).forGetter(recipe -> new CompoundTag())
		).apply(instance, (recipe, tag) -> recipe));
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
