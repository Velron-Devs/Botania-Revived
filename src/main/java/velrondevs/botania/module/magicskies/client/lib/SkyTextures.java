package velrondevs.botania.module.magicskies.client.lib;

import net.minecraft.resources.Identifier;

import velrondevs.botania.client.lib.ResourcesLib;

public final class SkyTextures {
	private SkyTextures() {}

	public static final Identifier SKYBOX = Identifier.parse(ResourcesLib.MISC_SKYBOX);
	public static final Identifier RAINBOW = Identifier.parse(ResourcesLib.MISC_RAINBOW);

	public static final Identifier[] PLANETS = new Identifier[] {
			Identifier.parse(ResourcesLib.MISC_PLANET + "0.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "1.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "2.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "3.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "4.png"),
			Identifier.parse(ResourcesLib.MISC_PLANET + "5.png")
	};
}
