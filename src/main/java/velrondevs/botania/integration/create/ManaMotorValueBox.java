package velrondevs.botania.integration.create;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;

import dev.engine_room.flywheel.lib.transform.TransformStack;

import net.createmod.catnip.api.math.AngleHelper;
import net.createmod.catnip.api.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ManaMotorValueBox extends ValueBoxTransform.Sided {
	@Override
	protected Vec3 getSouthLocation() {
		return VecHelper.voxelSpace(8, 8, 12.5);
	}

	@Override
	public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
		Direction facing = state.getValue(ManaMotorBlock.FACING);
		return super.getLocalOffset(level, pos, state).add(Vec3.atLowerCornerOf(facing.getUnitVec3i()).scale(-1 / 16f));
	}

	@Override
	public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
		super.rotate(level, pos, state, ms);
		Direction facing = state.getValue(ManaMotorBlock.FACING);
		if (facing.getAxis() == Direction.Axis.Y) {
			return;
		}
		if (getSide() != Direction.UP) {
			return;
		}
		TransformStack.of(ms).rotateZDegrees(-AngleHelper.horizontalAngle(facing) + 180);
	}

	@Override
	protected boolean isSideActive(BlockState state, Direction direction) {
		Direction facing = state.getValue(ManaMotorBlock.FACING);
		if (facing.getAxis() != Direction.Axis.Y && direction == Direction.DOWN) {
			return false;
		}
		return direction.getAxis() != facing.getAxis();
	}
}
