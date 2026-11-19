package velrondevs.botania.coremod.sophisticated;

import cpw.mods.modlauncher.api.ITransformer;

import net.neoforged.neoforgespi.coremod.ICoreMod;

import java.util.List;

public final class SophisticatedTankCoreMod implements ICoreMod {
	@Override
	public Iterable<? extends ITransformer<?>> getTransformers() {
		return List.of(new TankTierTransformer());
	}
}
