package velrondevs.botania.api.item;

import net.minecraft.world.item.ItemStack;

public interface AncientWillContainer {
	enum AncientWillType {
		AHRIM,
		DHAROK,
		GUTHAN,
		TORAG,
		VERAC,
		KARIL
	}

	void addAncientWill(ItemStack stack, AncientWillType will);

	boolean hasAncientWill(ItemStack stack, AncientWillType will);

}
