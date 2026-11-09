package velrondevs.botania.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.block_entity.PlatformBlockEntity;
import velrondevs.botania.common.lib.BotaniaTags;

import java.util.List;

public class PlatformBakedModel extends DelegateBlockStateModel {
	public PlatformBakedModel(BlockStateModel originalModel) {
		super(originalModel);
	}

	@Nullable
	private static BlockState getHeldState(BlockAndTintGetter level, BlockPos pos) {
		if (level.getBlockEntity(pos) instanceof PlatformBlockEntity platform) {
			BlockState heldState = new PlatformBlockEntity.PlatformData(platform).state();
			if (heldState != null && !heldState.is(BotaniaTags.Blocks.UNSUPPORTED_PLATFORM_DISGUISE)) {
				return heldState;
			}
		}
		return null;
	}

	private static BlockStateModel getModel(BlockState state) {
		return Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		BlockState heldState = getHeldState(level, pos);
		if (heldState == null) {
			super.collectParts(level, pos, state, random, parts);
		} else {
			getModel(heldState).collectParts(level, pos, heldState, random, parts);
		}
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		BlockState heldState = getHeldState(level, pos);
		if (heldState == null) {
			return super.particleMaterial(level, pos, state);
		}
		return getModel(heldState).particleMaterial(level, pos, heldState);
	}

	@Override
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		BlockState heldState = getHeldState(level, pos);
		if (heldState == null) {
			return super.materialFlags(level, pos, state);
		}
		return getModel(heldState).materialFlags(level, pos, heldState);
	}
}
