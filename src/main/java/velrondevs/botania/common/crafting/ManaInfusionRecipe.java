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
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Optional;

public class ManaInfusionRecipe implements velrondevs.botania.api.recipe.ManaInfusionRecipe {
	private final ItemStack output;
	private final Ingredient input;
	private final int mana;
	@Nullable
	private final StateIngredient catalyst;
	private final String group;

	public ManaInfusionRecipe(ItemStack output, Ingredient input, int mana,
			@Nullable String group, @Nullable StateIngredient catalyst) {
		Preconditions.checkArgument(mana > 0, "Mana cost must be positive");
		Preconditions.checkArgument(mana <= 1_000_001, "Mana cost must be at most a pool");
		this.output = output;
		this.input = input;
		this.mana = mana;
		this.group = group == null ? "" : group;
		this.catalyst = catalyst;
	}

	@NotNull
	@Override
	public RecipeSerializer<ManaInfusionRecipe> getSerializer() {
		return BotaniaRecipeTypes.MANA_INFUSION_SERIALIZER;
	}

	@Override
	public boolean matches(ItemStack stack) {
		return input.test(stack);
	}

	@Override
	public StateIngredient getRecipeCatalyst() {
		return catalyst;
	}

	@Override
	public int getManaToConsume() {
		return mana;
	}

	@NotNull
	@Override
	public ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return output;
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return NonNullList.of(Ingredient.EMPTY, input);
	}

	@NotNull
	@Override
	public String getGroup() {
		return group;
	}

	@NotNull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaBlocks.manaPool);
	}

	public static class Serializer implements RecipeSerializer<ManaInfusionRecipe> {
		public static final MapCodec<ManaInfusionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ItemStack.STRICT_CODEC.fieldOf("output").forGetter(recipe -> recipe.output),
				Ingredient.CODEC.fieldOf("input").forGetter(recipe -> recipe.input),
				Codec.INT.fieldOf("mana").forGetter(recipe -> recipe.mana),
				Codec.STRING.optionalFieldOf("group", "").forGetter(recipe -> recipe.group),
				StateIngredientHelper.CODEC.optionalFieldOf("catalyst").forGetter(recipe -> Optional.ofNullable(recipe.catalyst))
		).apply(instance, (output, input, mana, group, catalyst) -> new ManaInfusionRecipe(output, input, mana, group, catalyst.orElse(null))));
		public static final StreamCodec<RegistryFriendlyByteBuf, ManaInfusionRecipe> STREAM_CODEC = StreamCodec.of(
				Serializer::toNetwork, Serializer::fromNetwork);

		@Override
		public MapCodec<ManaInfusionRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, ManaInfusionRecipe> streamCodec() {
			return STREAM_CODEC;
		}

		private static ManaInfusionRecipe fromNetwork(RegistryFriendlyByteBuf buf) {
			Ingredient input = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
			ItemStack output = ItemStack.STREAM_CODEC.decode(buf);
			int mana = buf.readVarInt();
			StateIngredient catalyst = null;
			if (buf.readBoolean()) {
				catalyst = StateIngredientHelper.read(buf);
			}
			String group = buf.readUtf();
			return new ManaInfusionRecipe(output, input, mana, group, catalyst);
		}

		private static void toNetwork(RegistryFriendlyByteBuf buf, ManaInfusionRecipe recipe) {
			Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
			ItemStack.STREAM_CODEC.encode(buf, recipe.output);
			buf.writeVarInt(recipe.getManaToConsume());
			boolean hasCatalyst = recipe.getRecipeCatalyst() != null;
			buf.writeBoolean(hasCatalyst);
			if (hasCatalyst) {
				recipe.getRecipeCatalyst().write(buf);
			}
			buf.writeUtf(recipe.getGroup());
		}
	}
}
