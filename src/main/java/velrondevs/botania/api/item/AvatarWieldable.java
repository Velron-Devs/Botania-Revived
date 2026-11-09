package velrondevs.botania.api.item;

import net.minecraft.resources.Identifier;

import velrondevs.botania.api.block.Avatar;

public interface AvatarWieldable {

	void onAvatarUpdate(Avatar tile);

	Identifier getOverlayResource(Avatar tile);

}
