package velrondevs.botania.common.crafting;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Optional;

public class PureDaisyRecipe implements velrondevs.botania.api.recipe.PureDaisyRecipe {

	public static final int DEFAULT_TIME = 150;

	protected final StateIngredient input;
	protected final BlockState outputState;
	private final int time;
	private final Optional<CacheableFunction> function;

	public PureDaisyRecipe(StateIngredient input, BlockState state, int time, Optional<CacheableFunction> function) {
		Preconditions.checkArgument(time >= 0, "Time must be nonnegative");
		this.input = input;
		this.outputState = state;
		this.time = time;
		this.function = function;
	}

	@Override
	public boolean matches(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy, BlockState state) {
		return input.test(state) && outputState != state;
	}

	@Override
	public boolean set(Level world, BlockPos pos, SpecialFlowerBlockEntity pureDaisy) {
		if (!world.isClientSide) {
			boolean success = world.setBlockAndUpdate(pos, outputState);
			if (success) {
				var serverLevel = (ServerLevel) world;
				var server = serverLevel.getServer();
				this.function.flatMap(f -> f.get(server.getFunctions())).ifPresent(command -> {
					var context = server.getFunctions().getGameLoopSender()
							.withLevel(serverLevel)
							.withPosition(Vec3.atBottomCenterOf(pos));
					server.getFunctions().execute(command, context);
				});
			}
			return success;
		}
		return true;
	}

	@Override
	public StateIngredient getInput() {
		return input;
	}

	@Override
	public BlockState getOutputState() {
		return outputState;
	}

	@Override
	public Optional<CacheableFunction> getSuccessFunction() {
		return this.function;
	}

	@Override
	public int getTime() {
		return time;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.PURE_DAISY_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<PureDaisyRecipe> {
		public static final MapCodec<PureDaisyRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				StateIngredientHelper.CODEC.fieldOf("input").forGetter(PureDaisyRecipe::getInput),
				StateIngredientHelper.BLOCK_STATE_CODEC.fieldOf("output").forGetter(PureDaisyRecipe::getOutputState),
				Codec.INT.optionalFieldOf("time", DEFAULT_TIME).forGetter(PureDaisyRecipe::getTime),
				CacheableFunction.CODEC.optionalFieldOf("success_function").forGetter(PureDaisyRecipe::getSuccessFunction)
		).apply(instance, PureDaisyRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, PureDaisyRecipe> STREAM_CODEC = StreamCodec.composite(
				StateIngredientHelper.STREAM_CODEC, PureDaisyRecipe::getInput,
				ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), PureDaisyRecipe::getOutputState,
				ByteBufCodecs.VAR_INT, PureDaisyRecipe::getTime,
				(input, output, time) -> new PureDaisyRecipe(input, output, time, Optional.empty()));

		@Override
		public MapCodec<PureDaisyRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, PureDaisyRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
