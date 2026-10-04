package velrondevs.botania.module.botaniaextras.crafting;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.crafting.StateIngredientHelper;
import velrondevs.botania.common.crafting.recipe.RecipeUtils;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;

import java.util.List;

public class TreeSuffusionRecipe implements Recipe<RecipeInput> {
	public static final int MAX_INGREDIENTS = 8;

	private final BlockState output;
	private final int mana;
	private final int throttle;
	private final NonNullList<Ingredient> inputs;

	public TreeSuffusionRecipe(BlockState output, int mana, int throttle, List<Ingredient> inputs) {
		Preconditions.checkArgument(inputs.size() <= MAX_INGREDIENTS, "Cannot have more than " + MAX_INGREDIENTS + " ingredients");
		Preconditions.checkArgument(mana >= 0, "Mana must be nonnegative");
		this.output = output;
		this.mana = mana;
		this.throttle = throttle;
		this.inputs = NonNullList.copyOf(inputs);
	}

	public BlockState getOutputState() {
		return output;
	}

	public int getMana() {
		return mana;
	}

	public int getThrottle() {
		return throttle;
	}

	public List<Ingredient> getInputs() {
		return inputs;
	}

	@Override
	public boolean matches(@NotNull RecipeInput input, @NotNull Level level) {
		return assign(input) != null;
	}

	public int[] assign(RecipeInput input) {
		int[] slots = new int[inputs.size()];
		boolean[] used = new boolean[input.size()];
		for (int i = 0; i < inputs.size(); i++) {
			slots[i] = -1;
			for (int j = 0; j < input.size(); j++) {
				ItemStack stack = input.getItem(j);
				if (!used[j] && !stack.isEmpty() && inputs.get(i).test(stack)) {
					used[j] = true;
					slots[i] = j;
					break;
				}
			}
			if (slots[i] == -1) {
				return null;
			}
		}
		return slots;
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull RecipeInput input, @NotNull HolderLookup.Provider registries) {
		return getResultItem(registries).copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@NotNull
	@Override
	public ItemStack getResultItem(@NotNull HolderLookup.Provider registries) {
		return new ItemStack(output.getBlock());
	}

	@NotNull
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return inputs;
	}

	@NotNull
	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(BotaniaExtrasBlocks.terrasteelItemPlatform);
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaExtrasRecipeTypes.TREE_SUFFUSION_SERIALIZER;
	}

	@NotNull
	@Override
	public RecipeType<?> getType() {
		return BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE;
	}

	public static class Serializer implements RecipeSerializer<TreeSuffusionRecipe> {
		public static final MapCodec<TreeSuffusionRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				StateIngredientHelper.BLOCK_STATE_CODEC.fieldOf("output").forGetter(TreeSuffusionRecipe::getOutputState),
				Codec.INT.fieldOf("mana").forGetter(TreeSuffusionRecipe::getMana),
				Codec.INT.optionalFieldOf("throttle", -1).forGetter(TreeSuffusionRecipe::getThrottle),
				RecipeUtils.INGREDIENTS_CODEC.fieldOf("ingredients").forGetter(TreeSuffusionRecipe::getInputs)
		).apply(instance, TreeSuffusionRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, TreeSuffusionRecipe> STREAM_CODEC = StreamCodec.composite(
				ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), TreeSuffusionRecipe::getOutputState,
				ByteBufCodecs.VAR_INT, TreeSuffusionRecipe::getMana,
				ByteBufCodecs.VAR_INT, TreeSuffusionRecipe::getThrottle,
				RecipeUtils.INGREDIENTS_STREAM_CODEC, TreeSuffusionRecipe::getInputs,
				TreeSuffusionRecipe::new);

		@Override
		public MapCodec<TreeSuffusionRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, TreeSuffusionRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
