package velrondevs.botania.api.item;

import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.api.block.Avatar;

public interface AvatarWieldable {

	void onAvatarUpdate(Avatar tile);

	ResourceLocation getOverlayResource(Avatar tile);

}
