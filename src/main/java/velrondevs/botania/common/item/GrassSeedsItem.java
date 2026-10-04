package velrondevs.botania.common.item;

import com.google.common.collect.ImmutableMap;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.block.FloatingFlower.IslandType;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.*;

public class GrassSeedsItem extends Item implements FloatingFlowerVariant {

	private static final Map<ResourceKey<Level>, Set<BlockSwapper>> blockSwappers = new HashMap<>();
	private static final Map<IslandType, Integer> COLORS = ImmutableMap.<IslandType, Integer>builder()
			.put(IslandType.GRASS, 0x006600)
			.put(IslandType.PODZOL, 0x805E00)
			.put(IslandType.MYCEL, 0x5E0054)
			.put(IslandType.DRY, 0x66800D)
			.put(IslandType.GOLDEN, 0xBFB300)
			.put(IslandType.VIVID, 0x00801A)
			.put(IslandType.SCORCHED, 0xBF0000)
			.put(IslandType.INFUSED, 0x008C8C)
			.put(IslandType.MUTATED, 0x661A66)
			.build();

	private final IslandType type;

	public GrassSeedsItem(IslandType type, Properties props) {
		super(props);
		this.type = type;
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		ItemStack stack = ctx.getItemInHand();

		return applySeeds(world, pos, stack);
	}

	public InteractionResult applySeeds(Level world, BlockPos pos, ItemStack stack) {
		BlockState state = world.getBlockState(pos);

		if (state.is(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE) && state != stateForType(type)) {
			if (!world.isClientSide) {
				BlockSwapper swapper = addBlockSwapper(world, pos, type);
				world.setBlockAndUpdate(pos, swapper.stateToSet);
				stack.shrink(1);
			} else {
				int color = getColor(type);
				spawnParticles(world, pos, extractR(color), extractG(color), extractB(color));
			}

			return InteractionResult.sidedSuccess(world.isClientSide());
		}

		return InteractionResult.PASS;
	}

	public static void spawnParticles(Level world, BlockPos pos, float r, float g, float b) {
		for (int i = 0; i < 50; i++) {
			double x = (Math.random() - 0.5) * 3;
			double y = Math.random() - 0.5 + 1;
			double z = (Math.random() - 0.5) * 3;
			float velMul = 0.025F;

			float motionx = (float) -x * velMul;
			float motiony = (float) -y * velMul;
			float motionz = (float) -z * velMul;
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, r, g, b);
			world.addParticle(data, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, motionx, motiony, motionz);
		}
	}

	public static void onTickEnd(ServerLevel world) {
		ResourceKey<Level> dim = world.dimension();
		if (blockSwappers.containsKey(dim)) {
			blockSwappers.get(dim).removeIf(next -> next == null || !next.tick());
		}
	}

	private static BlockSwapper addBlockSwapper(Level world, BlockPos pos, IslandType type) {
		BlockSwapper swapper = new BlockSwapper(world, pos, stateForType(type));

		ResourceKey<Level> dim = world.dimension();
		blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);

		return swapper;
	}

	private static BlockState stateForType(IslandType type) {
		if (type == IslandType.PODZOL) {
			return Blocks.PODZOL.defaultBlockState();
		} else if (type == IslandType.MYCEL) {
			return Blocks.MYCELIUM.defaultBlockState();
		} else if (type == IslandType.DRY) {
			return BotaniaBlocks.dryGrass.defaultBlockState();
		} else if (type == IslandType.GOLDEN) {
			return BotaniaBlocks.goldenGrass.defaultBlockState();
		} else if (type == IslandType.VIVID) {
			return BotaniaBlocks.vividGrass.defaultBlockState();
		} else if (type == IslandType.SCORCHED) {
			return BotaniaBlocks.scorchedGrass.defaultBlockState();
		} else if (type == IslandType.INFUSED) {
			return BotaniaBlocks.infusedGrass.defaultBlockState();
		} else if (type == IslandType.MUTATED) {
			return BotaniaBlocks.mutatedGrass.defaultBlockState();
		} else {
			return Blocks.GRASS_BLOCK.defaultBlockState();
		}
	}

	private static class BlockSwapper {

		public static final int RANGE = 3;

		public static final int TICK_RANGE_HORIZONTAL = 1;

		public static final int TICK_RANGE_VERTICAL = 2;

		private final Level world;
		private final Random rand;
		private final BlockState stateToSet;

		private final BlockPos startCoords;
		private int ticksExisted = 0;

		public BlockSwapper(Level world, BlockPos coords, BlockState state) {
			this.world = world;
			stateToSet = state;
			rand = new Random(coords.hashCode());
			startCoords = coords;
		}

		public boolean tick() {
			if (++ticksExisted % 20 == 0) {
				var tickPositions = new ArrayList<BlockPos>();
				for (BlockPos pos : BlockPos.betweenClosed(startCoords.offset(-RANGE, -RANGE, -RANGE),
						startCoords.offset(RANGE, RANGE, RANGE))) {
					if (world.getBlockState(pos) == stateToSet && canPropagate(pos)) {
						tickPositions.add(pos.immutable());
					}
				}
				tickPositions.forEach(this::tickBlock);
			}

			return ticksExisted < 80;
		}

		public void tickBlock(BlockPos pos) {
			List<BlockPos> validCoords = new ArrayList<>();

			for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-TICK_RANGE_HORIZONTAL, -TICK_RANGE_VERTICAL, -TICK_RANGE_HORIZONTAL),
					pos.offset(TICK_RANGE_HORIZONTAL, TICK_RANGE_VERTICAL, TICK_RANGE_HORIZONTAL))) {

				if (targetPos.equals(pos) || world.getBlockState(targetPos) == stateToSet) {
					continue;
				}

				if (isValidSwapPosition(targetPos)) {
					validCoords.add(targetPos.immutable());
				}
			}

			if (!validCoords.isEmpty()) {
				BlockPos toSwap = validCoords.get(rand.nextInt(validCoords.size()));

				world.setBlockAndUpdate(toSwap, stateToSet);
			}
		}

		public boolean isValidSwapPosition(BlockPos pos) {
			BlockState state = world.getBlockState(pos);
			return state.is(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE) && canBeGrass(pos, state);
		}

		private boolean canBeGrass(BlockPos pos, BlockState state) {
			BlockPos abovePos = pos.above();
			BlockState aboveState = world.getBlockState(abovePos);
			if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {

				return true;
			}
			if (aboveState.getFluidState().getAmount() == 8) {

				return false;
			}
			int lightLevel = LightEngine.getLightBlockInto(world, state, pos, aboveState, abovePos, Direction.UP, aboveState.getLightBlock(world, abovePos));
			return lightLevel < world.getMaxLightLevel();
		}

		private boolean canPropagate(BlockPos pos) {
			BlockPos abovePos = pos.above();
			return canBeGrass(pos, stateToSet) && !world.getFluidState(abovePos).is(FluidTags.WATER);
		}
	}

	public static float extractR(int color) {
		return ((color >> 16) & 0xFF) / 255f;
	}

	public static float extractG(int color) {
		return ((color >> 8) & 0xFF) / 255f;
	}

	public static float extractB(int color) {
		return (color & 0xFF) / 255f;
	}

	public static int getColor(IslandType type) {
		return COLORS.get(type);
	}

	@Override
	public IslandType getIslandType(ItemStack stack) {
		return type;
	}

}
