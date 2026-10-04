package velrondevs.botania.module.magicskies.client.lib;

import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.client.lib.ResourcesLib;

public final class SkyTextures {
	private SkyTextures() {}

	public static final ResourceLocation SKYBOX = ResourceLocation.parse(ResourcesLib.MISC_SKYBOX);
	public static final ResourceLocation RAINBOW = ResourceLocation.parse(ResourcesLib.MISC_RAINBOW);

	public static final ResourceLocation[] PLANETS = new ResourceLocation[] {
			ResourceLocation.parse(ResourcesLib.MISC_PLANET + "0.png"),
			ResourceLocation.parse(ResourcesLib.MISC_PLANET + "1.png"),
			ResourceLocation.parse(ResourcesLib.MISC_PLANET + "2.png"),
			ResourceLocation.parse(ResourcesLib.MISC_PLANET + "3.png"),
			ResourceLocation.parse(ResourcesLib.MISC_PLANET + "4.png"),
			ResourceLocation.parse(ResourcesLib.MISC_PLANET + "5.png")
	};
}
