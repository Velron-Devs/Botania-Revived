package velrondevs.botania.coremod.spectrum;

import cpw.mods.modlauncher.api.ITransformer;

import net.neoforged.neoforgespi.coremod.ICoreMod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

public final class SpectrumCoreMod implements ICoreMod {
	private static final Logger LOGGER = LoggerFactory.getLogger("botania/spectrum");

	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		Set<String> names = SpectrumJarLocator.classNames();
		if (names.isEmpty()) {
			return List.of();
		}
		LOGGER.info("Spectrum found, remapping {} Botania integration classes to the Botania Revived API", names.size());
		return List.of(new SpectrumTransformer(names));
	}
}
