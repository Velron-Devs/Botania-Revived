package velrondevs.botania.common.block.flower.functional;

import velrondevs.botania.common.block.RecipeLookup;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.common.handler.OrechidManager;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public class OrechidBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int DEFAULT_COST = 100;
	private static final int COST_GOG = 700;
	private static final int DELAY = 100;
	private static final int DELAY_GOG = 2;
	private static final int DEFAULT_RANGE = 3;
	private static final int DEFAULT_RANGE_Y = 1;

	protected OrechidBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public OrechidBlockEntity(BlockPos pos, BlockState state) {
		this(BotaniaFlowerBlocks.ORECHID, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide() || redstoneSignal > 0 || !canOperate()) {
			return;
		}

		if (getMana() >= getCost() && ticksExisted % getDelay() == 0) {
			BlockPos coords = getCoordsToPut();
			if (coords != null) {
				trySetRecipe(coords, findMatchingRecipe(coords));
			}
		}
	}

	protected void playSound(BlockPos coords) {
		getLevel().playSound(null, coords, BotaniaSounds.orechid, SoundSource.BLOCKS, 1F, 1F);
	}

	@Nullable
	private OrechidRecipe findMatchingRecipe(BlockPos coords) {
		BlockState input = level.getBlockState(coords);
		List<Weighted<OrechidRecipe>> values = new ArrayList<>();
		for (OrechidRecipe recipe : OrechidManager.getMatchingRecipes(RecipeLookup.manager(getLevel()), getRecipeType(), input)) {
			values.add(new Weighted<>(recipe, recipe.getWeight(getLevel(), coords)));
		}
		return WeightedRandom.getRandomItem(getLevel().getRandom(), values, Weighted::weight)
				.map(Weighted::value)
				.orElse(null);
	}

	private void trySetRecipe(BlockPos coords, @Nullable OrechidRecipe recipe) {
		if (recipe == null) {
			return;
		}

		BlockState state = recipe.getOutput(level, coords).pick(level.getRandom());
		if (getLevel().setBlockAndUpdate(coords, state)) {
			if (BotaniaConfig.common().blockBreakParticles()) {
				getLevel().levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, coords, Block.getId(state));
			}
			playSound(coords);
			addMana(-getCost());
			getLevel().gameEvent(null, GameEvent.BLOCK_CHANGE, coords);

			var serverLevel = (ServerLevel) this.level;
			var server = serverLevel.getServer();
			recipe.getSuccessFunction().flatMap(function -> function.get(server.getFunctions())).ifPresent(command -> {
				var context = server.getFunctions().getGameLoopSender()
						.withLevel(serverLevel)
						.withPosition(Vec3.atBottomCenterOf(coords));
				server.getFunctions().execute(command, context);
			});

			sync();
		}
	}

	private BlockPos getCoordsToPut() {
		List<BlockPos> possibleCoords = new ArrayList<>();
		var matcher = getReplaceMatcher();
		for (BlockPos pos : BlockPos.betweenClosed(getEffectivePos().offset(-getRange(), -getRangeY(), -getRange()),
				getEffectivePos().offset(getRange(), getRangeY(), getRange()))) {
			BlockState state = getLevel().getBlockState(pos);
			if (matcher.test(state)) {
				possibleCoords.add(pos.immutable());
			}
		}

		if (possibleCoords.isEmpty()) {
			return null;
		}
		return possibleCoords.get(getLevel().getRandom().nextInt(possibleCoords.size()));
	}

	public boolean canOperate() {
		return true;
	}

	public RecipeType<? extends OrechidRecipe> getRecipeType() {
		return BotaniaRecipeTypes.ORECHID_TYPE;
	}

	public Predicate<BlockState> getReplaceMatcher() {
		return state -> !OrechidManager.getMatchingRecipes(
				RecipeLookup.manager(this.getLevel()),
				this.getRecipeType(),
				state
		).isEmpty();
	}

	public int getCost() {
		return XplatAbstractions.INSTANCE.gogLoaded() ? COST_GOG : configOr(DEFAULT_COST, BotaniaConfig.ConfigAccess::orechidCost);
	}

	public int getDelay() {
		return XplatAbstractions.INSTANCE.gogLoaded() ? DELAY_GOG : DELAY;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), getRange());
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	public int getRange() {
		return configOr(DEFAULT_RANGE, BotaniaConfig.ConfigAccess::orechidRange);
	}

	public int getRangeY() {
		return configOr(DEFAULT_RANGE_Y, BotaniaConfig.ConfigAccess::orechidRangeY);
	}

	protected static int configOr(int fallback, ToIntFunction<BotaniaConfig.ConfigAccess> getter) {
		var config = BotaniaConfig.common();
		return config == null ? fallback : getter.applyAsInt(config);
	}

	@Override
	public int getColor() {
		return 0x818181;
	}

	@Override
	public int getMaxMana() {
		return getCost();
	}

}
