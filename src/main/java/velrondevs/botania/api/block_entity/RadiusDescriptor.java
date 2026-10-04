package velrondevs.botania.api.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public sealed interface RadiusDescriptor permits RadiusDescriptor.Circle,RadiusDescriptor.Rectangle {
	record Circle(BlockPos subtileCoords, double radius) implements RadiusDescriptor {
	}

	record Rectangle(BlockPos subtileCoords, AABB aabb) implements RadiusDescriptor {
		public static Rectangle square(BlockPos subtileCoords, int expand) {
			return new Rectangle(subtileCoords, new AABB(Vec3.atLowerCornerOf(subtileCoords.offset(-expand, 0, -expand)), Vec3.atLowerCornerOf(subtileCoords.offset(expand + 1, 0, expand + 1))));
		}
	}
}
