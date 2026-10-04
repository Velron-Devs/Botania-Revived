package velrondevs.botania.api.item;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.DyeColor;

public interface SparkEntity {

	BlockPos getAttachPos();

	DyeColor getNetwork();

	void setNetwork(DyeColor color);

	default Entity entity() {
		return (Entity) this;
	}
}
