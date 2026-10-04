package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.IridescentColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class IridescentSeedsItem extends Item {
	private static final Map<ResourceKey<Level>, Set<BlockSwapper>> SWAPPERS = new HashMap<>();

	@Nullable
	private final DyeColor color;

	public IridescentSeedsItem(@Nullable DyeColor color, Properties properties) {
		super(properties);
		this.color = color;
	}

	@Nullable
	public DyeColor getColor() {
		return color;
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (!isConvertible(state)) {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			BlockState dirt = BotaniaExtrasBlocks.getDirt(color).defaultBlockState();
			level.setBlockAndUpdate(pos, dirt);
			convertPlantAbove(level, pos, color);
			SWAPPERS.computeIfAbsent(level.dimension(), d -> new HashSet<>()).add(new BlockSwapper(level, pos, color));
			ctx.getItemInHand().shrink(1);
		} else {
			spawnParticles(level, pos);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	private void spawnParticles(Level level, BlockPos pos) {
		for (int i = 0; i < 50; i++) {
			int rgb = color == null ? Mth.hsvToRgb(level.random.nextFloat(), 1F, 1F) : IridescentColors.rgb(color);
			float r = ((rgb >> 16) & 0xFF) / 255F;
			float g = ((rgb >> 8) & 0xFF) / 255F;
			float b = (rgb & 0xFF) / 255F;
			double x = (Math.random() - 0.5) * 3;
			double y = Math.random() - 0.5 + 1;
			double z = (Math.random() - 0.5) * 3;
			float velMul = 0.025F;
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, r, g, b);
			level.addParticle(data, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z,
					(float) -x * velMul, (float) -y * velMul, (float) -z * velMul);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(IridescentColors.displayName(color).copy().withStyle(ChatFormatting.GRAY));
	}

	private static boolean isConvertible(BlockState state) {
		return state.is(Blocks.DIRT) || state.is(Blocks.GRASS_BLOCK);
	}

	private static void convertPlantAbove(Level level, BlockPos pos, @Nullable DyeColor color) {
		BlockPos above = pos.above();
		BlockState aboveState = level.getBlockState(above);
		if (aboveState.is(Blocks.SHORT_GRASS)) {
			level.setBlock(above, BotaniaExtrasBlocks.getGrass(color).defaultBlockState(), Block.UPDATE_ALL);
		} else if (aboveState.is(Blocks.TALL_GRASS) && aboveState.getValue(DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
			BlockState tall = BotaniaExtrasBlocks.getTallGrass(color).defaultBlockState();
			level.setBlock(above.above(), tall.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), Block.UPDATE_CLIENTS);
			level.setBlock(above, tall.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), Block.UPDATE_CLIENTS);
		}
	}

	public static void onTickEnd(ServerLevel level) {
		Set<BlockSwapper> swappers = SWAPPERS.get(level.dimension());
		if (swappers != null) {
			swappers.removeIf(s -> !s.tick());
		}
	}

	private static class BlockSwapper {
		private static final int RANGE = 3;
		private static final int TICK_RANGE = 1;

		private final Level level;
		private final Random rand;
		private final BlockPos start;
		@Nullable
		private final DyeColor color;
		private final BlockState dirt;
		private int ticksExisted = 0;

		BlockSwapper(Level level, BlockPos start, @Nullable DyeColor color) {
			this.level = level;
			this.start = start.immutable();
			this.color = color;
			this.dirt = BotaniaExtrasBlocks.getDirt(color).defaultBlockState();
			this.rand = new Random(start.getX() ^ start.getY() ^ start.getZ());
		}

		boolean tick() {
			ticksExisted++;
			if (ticksExisted % 20 == 0) {
				List<BlockPos> active = new ArrayList<>();
				for (int i = -RANGE; i <= RANGE; i++) {
					for (int j = -RANGE; j <= RANGE; j++) {
						BlockPos pos = start.offset(i, 0, j);
						if (level.getBlockState(pos) == dirt) {
							active.add(pos);
						}
					}
				}
				active.forEach(this::tickBlock);
			}
			return ticksExisted < 80;
		}

		private void tickBlock(BlockPos pos) {
			List<BlockPos> valid = new ArrayList<>();
			for (int x = -TICK_RANGE; x <= TICK_RANGE; x++) {
				for (int z = -TICK_RANGE; z <= TICK_RANGE; z++) {
					if (x == 0 && z == 0) {
						continue;
					}
					BlockPos target = pos.offset(x, 0, z);
					if (isValidSwapPosition(target)) {
						valid.add(target);
					}
				}
			}
			if (!valid.isEmpty()) {
				BlockPos toSwap = valid.get(rand.nextInt(valid.size()));
				level.setBlockAndUpdate(toSwap, dirt);
				convertPlantAbove(level, toSwap, color);
			}
		}

		private boolean isValidSwapPosition(BlockPos pos) {
			BlockState state = level.getBlockState(pos);
			BlockPos above = pos.above();
			return isConvertible(state) && level.getBlockState(above).getLightBlock(level, above) <= 1;
		}
	}
}
