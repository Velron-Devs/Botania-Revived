package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.common.crafting.PureDaisyRecipe;
import velrondevs.botania.common.crafting.StateIngredientHelper;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Optional;

public class StateCopyingPureDaisyRecipe extends PureDaisyRecipe {
	public StateCopyingPureDaisyRecipe(StateIngredient input, Block block, int time) {
		super(input, block.defaultBlockState(), time, Optional.empty());
	}

	@Override
	public boolean matches(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy, BlockState state) {
		return input.test(state) && outputState.getBlock().withPropertiesOf(state) != state;
	}

	@Override
	public boolean set(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy) {
		if (!world.isClientSide) {
			Block block = getOutputState().getBlock();
			world.setBlockAndUpdate(pos, block.withPropertiesOf(world.getBlockState(pos)));
		}
		return true;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.COPYING_PURE_DAISY_SERIALIZER;
	}

	public Block getOutputBlock() {
		return getOutputState().getBlock();
	}

	public static class Serializer implements RecipeSerializer<StateCopyingPureDaisyRecipe> {
		public static final MapCodec<StateCopyingPureDaisyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				StateIngredientHelper.CODEC.fieldOf("input").forGetter(StateCopyingPureDaisyRecipe::getInput),
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("output").forGetter(StateCopyingPureDaisyRecipe::getOutputBlock),
				Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(StateCopyingPureDaisyRecipe::getTime)
		).apply(instance, StateCopyingPureDaisyRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, StateCopyingPureDaisyRecipe> STREAM_CODEC = StreamCodec.composite(
				StateIngredientHelper.STREAM_CODEC, StateCopyingPureDaisyRecipe::getInput,
				ByteBufCodecs.registry(Registries.BLOCK), StateCopyingPureDaisyRecipe::getOutputBlock,
				ByteBufCodecs.VAR_INT, StateCopyingPureDaisyRecipe::getTime,
				StateCopyingPureDaisyRecipe::new);

		@Override
		public MapCodec<StateCopyingPureDaisyRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, StateCopyingPureDaisyRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
