package velrondevs.botania.integration.botanypots;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.darkhax.bookshelf.common.api.util.DataHelper;
import net.darkhax.botanypots.common.api.context.BlockEntityContext;
import net.darkhax.botanypots.common.api.context.BotanyPotContext;
import net.darkhax.botanypots.common.api.data.recipes.crop.Crop;
import net.darkhax.botanypots.common.impl.block.entity.BotanyPotBlockEntity;
import net.darkhax.botanypots.common.impl.data.recipe.soil.BasicSoil;
import net.darkhax.botanypots.common.impl.data.recipe.soil.BlockDerivedSoil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.crafting.RecipeSerializer;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.mana.ManaPool;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class ManaSoil extends BasicSoil {
	public static final int CHECK_INTERVAL = 20;
	private static final int SCAN_INTERVAL = 40;

	public static final MapCodec<ManaSoil> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BlockDerivedSoil.BlockProperties.CODEC.forGetter(ManaSoil::getBlockProperties),
			Codec.floatRange(0F, Float.MAX_VALUE).optionalFieldOf("mana_growth_bonus", 1.0F).forGetter(ManaSoil::getManaGrowthBonus),
			Codec.intRange(1, 100000).optionalFieldOf("mana_per_tick", 2).forGetter(ManaSoil::getManaPerTick),
			Codec.intRange(1, 8).optionalFieldOf("pool_radius", 3).forGetter(ManaSoil::getPoolRadius)
	).apply(instance, ManaSoil::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, ManaSoil> STREAM = StreamCodec.composite(
			BlockDerivedSoil.BlockProperties.STREAM, ManaSoil::getBlockProperties,
			ByteBufCodecs.FLOAT, ManaSoil::getManaGrowthBonus,
			ByteBufCodecs.VAR_INT, ManaSoil::getManaPerTick,
			ByteBufCodecs.VAR_INT, ManaSoil::getPoolRadius,
			ManaSoil::new
	);

	public static final RecipeSerializer<ManaSoil> SERIALIZER = DataHelper.recipeSerializer(CODEC, STREAM);

	private static final Map<BotanyPotBlockEntity, PotState> STATES = Collections.synchronizedMap(new WeakHashMap<>());

	private final BlockDerivedSoil.BlockProperties blockProperties;
	private final float manaGrowthBonus;
	private final int manaPerTick;
	private final int poolRadius;

	public ManaSoil(BlockDerivedSoil.BlockProperties blockProperties, float manaGrowthBonus, int manaPerTick, int poolRadius) {
		super(blockProperties.toBasic());
		this.blockProperties = blockProperties;
		this.manaGrowthBonus = manaGrowthBonus;
		this.manaPerTick = manaPerTick;
		this.poolRadius = poolRadius;
	}

	public BlockDerivedSoil.BlockProperties getBlockProperties() {
		return blockProperties;
	}

	public float getManaGrowthBonus() {
		return manaGrowthBonus;
	}

	public int getManaPerTick() {
		return manaPerTick;
	}

	public int getPoolRadius() {
		return poolRadius;
	}

	@NotNull
	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public void onTick(BotanyPotContext context, Level level) {
		if (level.isClientSide || !(context instanceof BlockEntityContext beContext)) {
			return;
		}
		BotanyPotBlockEntity pot = beContext.pot();
		long time = level.getGameTime();
		PotState state = STATES.computeIfAbsent(pot, p -> new PotState());
		if (time < state.nextCheck) {
			return;
		}
		state.nextCheck = time + CHECK_INTERVAL;

		Crop crop = beContext.getCrop();
		if (crop == null || !crop.isGrowthSustained(beContext, level)) {
			return;
		}
		if (pot.growthTime() >= beContext.getRequiredGrowthTicks()) {
			return;
		}

		ManaPool pool = state.findPool(level, pot.getBlockPos(), poolRadius, time);
		int cost = manaPerTick * CHECK_INTERVAL;
		if (pool == null || pool.getCurrentMana() < cost) {
			return;
		}
		pool.receiveMana(-cost);
		pot.updateGrowthTime(pot.growthTime() + manaGrowthBonus * CHECK_INTERVAL);
	}

	private static final class PotState {
		private long nextCheck;
		private long nextScan;
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
