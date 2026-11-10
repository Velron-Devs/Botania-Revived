package velrondevs.botania.common.crafting;

import velrondevs.botania.api.recipe.BotaniaRecipe;
import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

public class RunicAltarRecipe implements velrondevs.botania.api.recipe.RunicAltarRecipe {
	private final ItemStackTemplate output;
	private final NonNullList<Ingredient> inputs;
	private final int mana;
	private final boolean returnRunes;

	public RunicAltarRecipe(ItemStack output, int mana, Ingredient... inputs) {
		this(ItemStackTemplate.fromNonEmptyStack(output), mana, inputs);
	}

	public RunicAltarRecipe(ItemStackTemplate output, int mana, Ingredient... inputs) {
		this(output, mana, true, inputs);
	}

	public RunicAltarRecipe(ItemStackTemplate output, int mana, boolean returnRunes, Ingredient... inputs) {
		Preconditions.checkArgument(inputs.length <= 16, "Cannot have more than 16 ingredients");
		this.output = output;
		this.inputs = RecipeUtils.ingredientList(inputs);
		this.mana = mana;
		this.returnRunes = returnRunes;
	}

	@Override
	public boolean matches(RecipeInput inv, @NotNull Level world) {
		return RecipeUtils.matches(inputs, inv, null);
	}

	@NotNull
	@Override
	public final ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return output.create();
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
	public RecipeSerializer<? extends BotaniaRecipe> getSerializer() {
		return BotaniaRecipeTypes.RUNE_SERIALIZER;
	}

	@Override
	public int getManaUsage() {
		return mana;
	}

	@Override
	public boolean returnsRunes() {
		return returnRunes;
	}

	@FunctionalInterface
	public interface Factory<T extends RunicAltarRecipe> {
		T create(ItemStackTemplate output, int mana, boolean returnRunes, Ingredient... inputs);
	}

	public static class Serializer<T extends RunicAltarRecipe> implements BotaniaRecipeSerializer<T> {
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

		public Serializer(Factory<T> factory) {
			this.codec = RecordCodecBuilder.mapCodec(instance -> instance.group(
					net.minecraft.world.item.ItemStackTemplate.CODEC.fieldOf("output").forGetter(recipe -> ((RunicAltarRecipe) recipe).output),
					Codec.INT.fieldOf("mana").forGetter(recipe -> ((RunicAltarRecipe) recipe).mana),
					RecipeUtils.INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(recipe -> ((RunicAltarRecipe) recipe).inputs),
					Codec.BOOL.optionalFieldOf("return_runes", true).forGetter(recipe -> ((RunicAltarRecipe) recipe).returnRunes)
			).apply(instance, (output, mana, inputs, returnRunes) -> factory.create(output, mana, returnRunes, inputs.toArray(new Ingredient[0]))));
			this.streamCodec = StreamCodec.of(
					(buf, recipe) -> {
						RecipeUtils.INGREDIENTS_STREAM_CODEC.encode(buf, recipe.getIngredients());
						ItemStackTemplate.STREAM_CODEC.encode(buf, ((RunicAltarRecipe) recipe).output);
						buf.writeVarInt(recipe.getManaUsage());
						buf.writeBoolean(recipe.returnsRunes());
					},
					buf -> {
						Ingredient[] inputs = RecipeUtils.INGREDIENTS_STREAM_CODEC.decode(buf).toArray(new Ingredient[0]);
						ItemStackTemplate output = ItemStackTemplate.STREAM_CODEC.decode(buf);
						int mana = buf.readVarInt();
						boolean returnRunes = buf.readBoolean();
						return factory.create(output, mana, returnRunes, inputs);
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
