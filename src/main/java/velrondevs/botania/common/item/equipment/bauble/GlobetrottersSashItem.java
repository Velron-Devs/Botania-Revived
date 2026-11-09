package velrondevs.botania.common.item.equipment.bauble;

import net.minecraft.resources.Identifier;

import velrondevs.botania.client.lib.ResourcesLib;

public class GlobetrottersSashItem extends SojournersSashItem {

	private static final Identifier texture = Identifier.parse(ResourcesLib.MODEL_SUPER_TRAVEL_BELT);

	public GlobetrottersSashItem(Properties props) {
		super(props, 0.085F, 0.3F, 4F);
	}

	@Override
	public Identifier getRenderTexture() {
		return texture;
	}
}
