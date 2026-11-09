package velrondevs.botania.compat.sophisticated;

import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeItem;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeType;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeWrapperBase;

import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class ManaLinkUpgradeItem extends UpgradeItemBase<ManaLinkUpgradeItem.Wrapper> {
	public static final UpgradeType<Wrapper> TYPE = new UpgradeType<>(Wrapper::new);

	public ManaLinkUpgradeItem() {
		super(UpgradeLimits.of(SophisticatedConfig::linkMaxPerStorage));
	}

	@Override
	public UpgradeType<Wrapper> getType() {
		return TYPE;
	}

	@Override
	public List<IUpgradeItem.UpgradeConflictDefinition> getUpgradeConflicts() {
		return List.of();
	}

	public static boolean isActive(IStorageWrapper storage) {
		for (Wrapper link : storage.getUpgradeHandler().getTypeWrappers(TYPE)) {
			if (link.isEnabled()) {
				return true;
			}
		}
		return false;
	}

	public static class Wrapper extends UpgradeWrapperBase<Wrapper, ManaLinkUpgradeItem> {
		protected Wrapper(IStorageWrapper storageWrapper, ItemStack upgrade, Consumer<ItemStack> upgradeSaveHandler) {
			super(storageWrapper, upgrade, upgradeSaveHandler);
		}

		@Override
		public boolean hideSettingsTab() {
			return true;
		}
	}
}
