package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaSounds;

public class FloralBifrostPowderItem extends Item {
	public FloralBifrostPowderItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level level = ctx.getLevel();
		BlockPos pos = ctx.getClickedPos();
		BlockState state = level.getBlockState(pos);
		if (state.is(BotaniaBlocks.manaPool)) {
			if (!level.isClientSide) {
				BlockEntity be = level.getBlockEntity(pos);
				CompoundTag data = be == null ? null : be.saveWithoutMetadata(level.registryAccess());
				level.setBlockAndUpdate(pos, BotaniaBlocks.fabulousPool.defaultBlockState());
				BlockEntity newBe = level.getBlockEntity(pos);
				if (data != null && newBe != null) {
					newBe.loadWithComponents(data, level.registryAccess());
					newBe.setChanged();
					level.sendBlockUpdated(pos, state, level.getBlockState(pos), 3);
				}
				ctx.getItemInHand().shrink(1);
			}
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		if (convertFlower(level, pos)) {
			ctx.getItemInHand().shrink(1);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return InteractionResult.PASS;
	}

	public static boolean convertFlower(Level level, BlockPos pos) {
		if (!level.getBlockState(pos).is(BotaniaTags.Blocks.MYSTICAL_FLOWERS)) {
			return false;
		}
		if (level.isClientSide) {
			for (int i = 0; i < 40; i++) {
				int rgb = Mth.hsvToRgb(level.random.nextFloat(), 1F, 1F);
				WispParticleData data = WispParticleData.wisp(0.5F, ((rgb >> 16) & 0xFF) / 255F, ((rgb >> 8) & 0xFF) / 255F, (rgb & 0xFF) / 255F);
				level.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0.125, 0);
			}
		} else {
			level.setBlockAndUpdate(pos, BotaniaExtrasBlocks.bifrostFlower.defaultBlockState());
			level.playSound(null, pos, BotaniaSounds.enchanterEnchant, SoundSource.BLOCKS, 1F, 1F);
		}
		return true;
	}

	public static class DispenseBehavior extends OptionalDispenseItemBehavior {
		@NotNull
		@Override
		protected ItemStack execute(BlockSource source, ItemStack stack) {
			Level level = source.level();
			BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
			setSuccess(convertFlower(level, target));
			if (isSuccess()) {
				stack.shrink(1);
			}
			return stack;
		}
	}
}
