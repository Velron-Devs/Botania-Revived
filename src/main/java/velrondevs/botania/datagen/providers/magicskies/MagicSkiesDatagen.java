package velrondevs.botania.datagen.providers.magicskies;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public final class MagicSkiesDatagen {
	private MagicSkiesDatagen() {}

	public static void gatherData(GatherDataEvent evt) {
		PackOutput output = evt.getGenerator().getPackOutput();
		evt.getGenerator().addProvider(evt.includeServer(), new MagicSkiesDimensionProvider(output, MagicSkiesDimensionProvider.Kind.DIMENSION_TYPE));
		evt.getGenerator().addProvider(evt.includeServer(), new MagicSkiesDimensionProvider(output, MagicSkiesDimensionProvider.Kind.DIMENSION));
	}
}
