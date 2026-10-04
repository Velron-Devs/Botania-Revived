package velrondevs.botania.common.item.equipment.bauble;

import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.client.lib.ResourcesLib;

public class GlobetrottersSashItem extends SojournersSashItem {

	private static final ResourceLocation texture = ResourceLocation.parse(ResourcesLib.MODEL_SUPER_TRAVEL_BELT);

	public GlobetrottersSashItem(Properties props) {
		super(props, 0.085F, 0.3F, 4F);
	}

	@Override
	public ResourceLocation getRenderTexture() {
		return texture;
	}
}
