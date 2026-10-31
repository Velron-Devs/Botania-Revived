package velrondevs.botania.compat.sophisticated.jei;

import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.IRecipeTransferRegistration;

import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedstorage.common.gui.StorageContainerMenu;

import velrondevs.botania.client.integration.jei.BreweryRecipeCategory;
import velrondevs.botania.client.integration.jei.ElvenTradeRecipeCategory;
import velrondevs.botania.client.integration.jei.RunicAltarRecipeCategory;
import velrondevs.botania.compat.sophisticated.SophisticatedModule;
import velrondevs.botania.compat.sophisticated.transfer.StationKind;

public final class SophisticatedJei {
	private SophisticatedJei() {}

	public static void registerTransfers(IRecipeTransferRegistration registry) {
		if (!SophisticatedModule.INSTANCE.isAvailable()) {
			return;
		}
		IRecipeTransferHandlerHelper helper = registry.getTransferHelper();
		if (SophisticatedModule.INSTANCE.hasBackpacks()) {
			registerFor(registry, helper, BackpackContainer.class);
		}
		if (SophisticatedModule.INSTANCE.hasStorage()) {
			registerFor(registry, helper, StorageContainerMenu.class);
		}
	}

	private static <C extends StorageContainerMenuBase<?>> void registerFor(IRecipeTransferRegistration registry, IRecipeTransferHandlerHelper helper, Class<C> menuClass) {
		registry.addRecipeTransferHandler(new StationTransferHandler<>(helper, menuClass, StationKind.RUNIC_ALTAR, RunicAltarRecipeCategory.TYPE), RunicAltarRecipeCategory.TYPE);
		registry.addRecipeTransferHandler(new StationTransferHandler<>(helper, menuClass, StationKind.ELVEN_PORTAL, ElvenTradeRecipeCategory.TYPE), ElvenTradeRecipeCategory.TYPE);
		registry.addRecipeTransferHandler(new StationTransferHandler<>(helper, menuClass, StationKind.BREWERY, BreweryRecipeCategory.TYPE), BreweryRecipeCategory.TYPE);
	}
}
