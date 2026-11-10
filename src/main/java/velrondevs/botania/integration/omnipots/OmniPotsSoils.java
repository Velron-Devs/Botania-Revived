package velrondevs.botania.integration.omnipots;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public final class OmniPotsSoils {
	public static final String MOD_ID = "omnipots";
	public static final int CHECK_INTERVAL = 20;
	private static final int SCAN_INTERVAL = 40;
	private static final int MANA_PER_TICK = 2;
	private static final int POOL_RADIUS = 3;
	private static final float MANA_GROWTH_BONUS = 1.0F;

	private static final Map<BlockEntity, PotState> STATES = Collections.synchronizedMap(new WeakHashMap<>());

	private OmniPotsSoils() {}

	@Nullable
	private static Block soilBlock(ItemStack stack) {
		return stack.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : null;
	}

	private static float baseMultiplier(ItemStack stack) {
		Block block = soilBlock(stack);
		if (block == null) {
			return 0F;
		}
		if (block == BotaniaBlocks.enchantedSoil) {
			return 1.5F;
		}
		if (block == BotaniaBlocks.infusedGrass) {
			return 1.15F;
		}
		if (block == BotaniaBlocks.vividGrass || block == BotaniaBlocks.mutatedGrass) {
			return 1.1F;
		}
		if (block == BotaniaBlocks.goldenGrass) {
			return 1.05F;
		}
		if (block == BotaniaBlocks.dryGrass || block == BotaniaBlocks.scorchedGrass) {
			return 1.0F;
		}
		return 0F;
	}

	public static boolean isBotaniaSoil(ItemStack stack) {
		return baseMultiplier(stack) > 0F;
	}

	public static float speedMultiplier(BlockEntity pot, ItemStack soil) {
		float base = baseMultiplier(soil);
		if (base <= 0F) {
			return 0F;
		}
		PotState state = STATES.get(pot);
		return state != null && state.powered ? base + MANA_GROWTH_BONUS : base;
	}

	public static void tickMana(BlockEntity pot, ItemStack soil) {
		Level level = pot.getLevel();
		if (level == null || level.isClientSide()) {
			return;
		}
		if (soilBlock(soil) != BotaniaBlocks.infusedGrass) {
			STATES.remove(pot);
			return;
		}
		long time = level.getGameTime();
		PotState state = STATES.computeIfAbsent(pot, p -> new PotState());
		if (time < state.nextCheck) {
			return;
		}
		state.nextCheck = time + CHECK_INTERVAL;

		ManaPool pool = state.findPool(level, pot.getBlockPos(), POOL_RADIUS, time);
		int cost = MANA_PER_TICK * CHECK_INTERVAL;
		if (pool == null || pool.getCurrentMana() < cost) {
			state.powered = false;
			return;
		}
		pool.receiveMana(-cost);
		state.powered = true;
	}

	private static final class PotState {
		private long nextCheck;
		private long nextScan;
		private boolean powered;
		private BlockPos poolPos;

		private ManaPool findPool(Level level, BlockPos origin, int radius, long time) {
			if (poolPos != null) {
				ManaPool cached = poolAt(level, poolPos);
				if (cached != null) {
					return cached;
				}
				poolPos = null;
			}
			if (time < nextScan) {
				return null;
			}
			nextScan = time + SCAN_INTERVAL;
			ManaPool best = null;
			double bestDist = Double.MAX_VALUE;
			for (BlockPos pos : BlockPos.betweenClosed(origin.offset(-radius, -radius, -radius), origin.offset(radius, radius, radius))) {
				ManaPool pool = poolAt(level, pos);
				if (pool != null) {
					double dist = pos.distSqr(origin);
					if (dist < bestDist) {
						bestDist = dist;
						best = pool;
						poolPos = pos.immutable();
					}
				}
			}
			return best;
		}

		private static ManaPool poolAt(Level level, BlockPos pos) {
			if (!level.isLoaded(pos)) {
				return null;
			}
			BlockEntity be = level.getBlockEntity(pos);
			return be instanceof ManaPool pool && !be.isRemoved() ? pool : null;
		}
	}
}
