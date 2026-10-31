package velrondevs.botania.compat.sophisticated;

import net.minecraft.resources.ResourceLocation;
import net.p3pp3rf1y.sophisticatedcore.upgrades.IUpgradeCountLimitConfig;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeGroup;

import java.util.function.IntSupplier;

final class UpgradeLimits {
	static final IUpgradeCountLimitConfig UNLIMITED = of(() -> Integer.MAX_VALUE);

	private UpgradeLimits() {}

	static IUpgradeCountLimitConfig of(IntSupplier perStorage) {
		return new IUpgradeCountLimitConfig() {
			@Override
			public int getMaxUpgradesPerStorage(String storageType, ResourceLocation upgradeName) {
				return perStorage.getAsInt();
			}

			@Override
			public int getMaxUpgradesInGroupPerStorage(String storageType, UpgradeGroup upgradeGroup) {
				return Integer.MAX_VALUE;
			}
		};
	}
}
