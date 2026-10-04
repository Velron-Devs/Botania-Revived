package velrondevs.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
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

import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

public class RunicAltarRecipe implements velrondevs.botania.api.recipe.RunicAltarRecipe {
	private final ItemStack output;
	private final NonNullList<Ingredient> inputs;
	private final int mana;

	public RunicAltarRecipe(ItemStack output, int mana, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 16, "Cannot have more than 16 ingredients");
		this.output = output;
		this.inputs = NonNullList.of(Ingredient.EMPTY, inputs);
		this.mana = mana;
	}

	@Override
	public boolean matches(RecipeInput inv, @NotNull Level world) {
		return RecipeUtils.matches(inputs, inv, null);
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
		return new ItemStack(BotaniaBlocks.runeAltar);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.RUNE_SERIALIZER;
	}

	@Override
	public int getManaUsage() {
		return mana;
	}

	@FunctionalInterface
	public interface Factory<T extends RunicAltarRecipe> {
		T create(ItemStack output, int mana, Ingredient... inputs);
	}

	public static class Serializer<T extends RunicAltarRecipe> implements RecipeSerializer<T> {
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

		public Serializer(Factory<T> factory) {
			this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					ItemStack.STRICT_CODEC.fieldOf("output").forGetter(recipe -> ((RunicAltarRecipe) recipe).output),
					Codec.INT.fieldOf("mana").forGetter(recipe -> ((RunicAltarRecipe) recipe).mana),
					RecipeUtils.INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(recipe -> ((RunicAltarRecipe) recipe).inputs)
			).apply(instance, (output, mana, inputs) -> factory.create(output, mana, inputs.toArray(new Ingredient[0]))));
			this.streamCodec = StreamCodec.of(
					(buf, recipe) -> {
						RecipeUtils.INGREDIENTS_STREAM_CODEC.encode(buf, recipe.getIngredients());
						ItemStack.STREAM_CODEC.encode(buf, ((RunicAltarRecipe) recipe).output);
						buf.writeVarInt(recipe.getManaUsage());
					},
					buf -> {
						Ingredient[] inputs = RecipeUtils.INGREDIENTS_STREAM_CODEC.decode(buf).toArray(new Ingredient[0]);
						ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
						int mana = buf.readVarInt();
						return factory.create(output, mana, inputs);
					});
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

}
