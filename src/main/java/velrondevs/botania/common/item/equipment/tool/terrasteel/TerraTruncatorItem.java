package velrondevs.botania.common.item.equipment.tool.terrasteel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.item.equipment.tool.BlockStartBreakItem;
import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.item.SequentialBreaker;
import velrondevs.botania.common.item.StoneOfTemperanceItem;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelAxeItem;

import java.util.*;

public class TerraTruncatorItem extends ManasteelAxeItem implements SequentialBreaker, BlockStartBreakItem {

	private static final int BLOCK_SWAP_RATE = 10;

	public static final int BLOCK_RANGE = 32;

	private static final int LEAF_BLOCK_RANGE = 3;

	private static final int MANA_PER_DAMAGE = 100;

	private static final Map<ResourceKey<Level>, Set<BlockSwapper>> blockSwappers = new HashMap<>();

	private static boolean tickingSwappers = false;

	public TerraTruncatorItem(Properties props) {
		super(BotaniaAPI.instance().getTerrasteelItemTier(), 5.0F, -3.0F, props);
	}

	public static boolean shouldBreak(Player player) {
		return !player.isShiftKeyDown() && !StoneOfTemperanceItem.hasTemperanceActive(player);
	}

	@Override
	public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, Player player) {
		BlockHitResult raycast = ToolCommons.raytraceFromEntity(player, 10, false);
		if (raycast.getType() == HitResult.Type.BLOCK) {
			Direction face = raycast.getDirection();
			breakOtherBlock(player, stack, pos, pos, face);
			if (player.isSecondaryUseActive()) {
				BotaniaAPI.instance().breakOnAllCursors(player, stack, pos, face);
			}
		}

		return false;
	}

	@Override
	public int getManaPerDamage() {
		return MANA_PER_DAMAGE;
	}

	@Override
	public void breakOtherBlock(Player player, ItemStack stack, BlockPos pos, BlockPos originPos, Direction side) {
		if (shouldBreak(player) && !tickingSwappers) {
			addBlockSwapper(player.level(), player, stack, pos);
		}
	}

	public static void onTickEnd(ServerLevel world) {
		ResourceKey<Level> dim = world.dimension();
		if (blockSwappers.containsKey(dim)) {
			tickingSwappers = true;
			Set<BlockSwapper> swappers = blockSwappers.get(dim);

			swappers.removeIf(next -> next == null || !next.tick());
			tickingSwappers = false;
		}
	}

	private static void addBlockSwapper(Level world, Player player, ItemStack stack, BlockPos origCoords) {

		if (world.isClientSide) {
			return;
		}

		BlockSwapper swapper = new BlockSwapper(world, player, stack, origCoords, TerraTruncatorItem.BLOCK_RANGE);

		ResourceKey<Level> dim = world.dimension();
		blockSwappers.computeIfAbsent(dim, d -> new HashSet<>()).add(swapper);
	}

	private static class BlockSwapper {

		public static final int SINGLE_BLOCK_RADIUS = 1;

		private final Level world;
		private final Player player;
		private final ItemStack truncator;

		private final PriorityQueue<SwapCandidate> candidateQueue = new PriorityQueue<>();

		private final Set<BlockPos> completedCoords = new HashSet<>();

		public BlockSwapper(Level world, Player player, ItemStack truncator, BlockPos origCoords, int range) {
			this.world = world;
			this.player = player;
			this.truncator = truncator;

			candidateQueue.offer(new SwapCandidate(origCoords, range));
		}

		public boolean tick() {

			if (candidateQueue.isEmpty()) {
				return false;
			}

			int remainingSwaps = BLOCK_SWAP_RATE;
			while (remainingSwaps > 0 && !candidateQueue.isEmpty()) {
				SwapCandidate cand = candidateQueue.poll();

				if (completedCoords.contains(cand.coordinates)) {
					continue;
				}

				if (cand.range <= 0) {
					continue;
				}

				ToolCommons.removeBlockWithDrops(player, truncator, world,
						cand.coordinates,
						state -> state.is(BlockTags.MINEABLE_WITH_AXE) || state.is(BlockTags.LEAVES));

				remainingSwaps--;

				completedCoords.add(cand.coordinates);

				for (BlockPos adj : adjacent(cand.coordinates)) {
					var state = world.getBlockState(adj);

					boolean isWood = state.is(BlockTags.LOGS);
					boolean isLeaf = state.is(BlockTags.LEAVES);

					boolean shouldPropagateThrough = isWood || isLeaf
							|| state.is(Blocks.MANGROVE_ROOTS);

					if (!shouldPropagateThrough) {
						continue;
					}

					int newRange = isLeaf ? Math.min(LEAF_BLOCK_RANGE, cand.range - 1) : cand.range - 1;

					candidateQueue.offer(new SwapCandidate(adj, newRange));
				}
			}

			return true;
		}

		public List<BlockPos> adjacent(BlockPos original) {
			List<BlockPos> coords = new ArrayList<>();

			for (int dx = -SINGLE_BLOCK_RADIUS; dx <= SINGLE_BLOCK_RADIUS; dx++) {
				for (int dy = -SINGLE_BLOCK_RADIUS; dy <= SINGLE_BLOCK_RADIUS; dy++) {
					for (int dz = -SINGLE_BLOCK_RADIUS; dz <= SINGLE_BLOCK_RADIUS; dz++) {

						if (dx == 0 && dy == 0 && dz == 0) {
							continue;
						}

						coords.add(original.offset(dx, dy, dz));
					}
				}
			}

			return coords;
		}

		public record SwapCandidate(BlockPos coordinates, int range) implements Comparable<SwapCandidate> {
			@Override
			public int compareTo(@NotNull SwapCandidate other) {

				return other.range - range;
			}
		}
	}

}
