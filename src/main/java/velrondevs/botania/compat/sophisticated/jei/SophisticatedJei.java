package velrondevs.botania.compat.sophisticated.jei;

import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedstorage.common.gui.StorageContainerMenu;

import velrondevs.botania.client.integration.jei.BreweryRecipeCategory;
import velrondevs.botania.client.integration.jei.ElvenTradeRecipeCategory;
import velrondevs.botania.client.integration.jei.ManaPoolRecipeCategory;
import velrondevs.botania.client.integration.jei.RunicAltarRecipeCategory;
import velrondevs.botania.compat.sophisticated.SophisticatedItems;
import velrondevs.botania.compat.sophisticated.SophisticatedModule;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageRegistry;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageStacks;
import velrondevs.botania.compat.sophisticated.storage.StorageSwitch;
import velrondevs.botania.compat.sophisticated.transfer.StationKind;

public final class SophisticatedJei {
	private SophisticatedJei() {}

	public static void registerSubtypes(ISubtypeRegistration registry) {
		if (!SophisticatedModule.INSTANCE.isAvailable() || !SophisticatedModule.INSTANCE.hasStorage() || !StorageSwitch.enabled()) {
			return;
		}
		ISubtypeInterpreter<ItemStack> interpreter = new ISubtypeInterpreter<>() {
			@Override
			public Object getSubtypeData(ItemStack stack, UidContext context) {
				return BotaniaStorageStacks.subtypeKey(stack);
			}

			@Override
			public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext context) {
				return BotaniaStorageStacks.subtypeKey(stack);
			}
		};
		for (BlockItem item : BotaniaStorageRegistry.barrelItems()) {
			registry.registerSubtypeInterpreter(item, interpreter);
		}
		for (BlockItem item : BotaniaStorageRegistry.chestItems()) {
			registry.registerSubtypeInterpreter(item, interpreter);
		}
	}

	public static void registerCatalysts(IRecipeCatalystRegistration registry) {
		if (!SophisticatedModule.INSTANCE.isEnabled() || !SophisticatedModule.INSTANCE.isAvailable()) {
			return;
		}
		registry.addRecipeCatalyst(new ItemStack(SophisticatedItems.MANA_PILE_UPGRADE), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(SophisticatedItems.RUNIC_ALTAR_UPGRADE), RunicAltarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(SophisticatedItems.ELVEN_PORTAL_UPGRADE), ElvenTradeRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(SophisticatedItems.BREWERY_UPGRADE), BreweryRecipeCategory.TYPE);
	}

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
		registry.addRecipeTransferHandler(new StationTransferHandler<>(helper, menuClass, StationKind.MANA_INFUSION, ManaPoolRecipeCategory.TYPE), ManaPoolRecipeCategory.TYPE);
	}
}
