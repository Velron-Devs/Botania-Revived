package velrondevs.botania.module.magicskies.client.render.sky;

public record SkyPalette(
		float planetScale,
		float raySpeed,
		float[] rayColorA,
		float[] rayColorB,
		float rainbowScale,
		float starRotationSpeed,
		float[] starColorA,
		float[] starColorB) {

	public static final SkyPalette CLASSIC = new SkyPalette(
			20F,
			1F,
			new float[] { 1F, 0.4F, 0.4F },
			new float[] { 0.4F, 1F, 0.7F },
			10F,
			1F,
			new float[] { 0.5F, 1F, 1F },
			new float[] { 1F, 0.75F, 0.75F });

	public static final SkyPalette AURORA = new SkyPalette(
			24F,
			0.6F,
			new float[] { 0.4F, 0.6F, 1F },
			new float[] { 0.6F, 1F, 0.9F },
			12F,
			0.5F,
			new float[] { 0.6F, 0.8F, 1F },
			new float[] { 0.8F, 0.6F, 1F });
}
