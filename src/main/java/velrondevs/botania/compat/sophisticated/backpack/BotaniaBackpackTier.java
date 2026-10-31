package velrondevs.botania.compat.sophisticated.backpack;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.SophisticatedConfig;
import velrondevs.botania.compat.sophisticated.SophisticatedOptional;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;

public enum BotaniaBackpackTier {
	MANASTEEL("manasteel", 1200F, 280, 8, LibMisc.MOD_ID + ":manasteel_ingots", true, null),
	ELEMENTIUM("elementium", 1200F, 380, 9, LibMisc.MOD_ID + ":elementium_ingots", true, null),
	TERRASTEEL("terrasteel", 1800F, 480, 10, LibMisc.MOD_ID + ":terrasteel_ingots", true, null),
	GAIA("gaia", 2400F, 595, 10, LibMisc.MOD_ID + ":gaia_ingot", false, null),
	ALFSTEEL("alfsteel", 3600F, 695, 10, SophisticatedOptional.ALFSTEEL_INGOT, false, SophisticatedOptional.MASTERFUL_BOTANY);

	private final String material;
	private final float explosionResistance;
	private final int defaultSlots;
	private final int defaultUpgradeSlots;
	private final String ingot;
	private final boolean ingotIsTag;
	private final String requiredMod;

	BotaniaBackpackTier(String material, float explosionResistance, int defaultSlots, int defaultUpgradeSlots, String ingot,
			boolean ingotIsTag, String requiredMod) {
		this.material = material;
		this.explosionResistance = explosionResistance;
		this.defaultSlots = defaultSlots;
		this.defaultUpgradeSlots = defaultUpgradeSlots;
		this.ingot = ingot;
		this.ingotIsTag = ingotIsTag;
		this.requiredMod = requiredMod;
	}

	public static List<BotaniaBackpackTier> active() {
		return Arrays.stream(values()).filter(BotaniaBackpackTier::isActive).toList();
	}

	public String material() {
		return material;
	}

	public String id() {
		return material + "_backpack";
	}

	public String clips() {
		return material + "_clips";
	}

	public float explosionResistance() {
		return explosionResistance;
	}

	public int defaultSlots() {
		return defaultSlots;
	}

	public int defaultUpgradeSlots() {
		return defaultUpgradeSlots;
	}

	public String ingot() {
		return ingot;
	}

	public boolean ingotIsTag() {
		return ingotIsTag;
	}

	public String requiredMod() {
		return requiredMod;
	}

	public boolean isActive() {
		return SophisticatedOptional.isLoaded(requiredMod);
	}

	public IntSupplier slots() {
		return () -> SophisticatedConfig.backpackSlots(material);
	}

	public IntSupplier upgradeSlots() {
		return () -> SophisticatedConfig.backpackUpgradeSlots(material);
	}

	public BotaniaBackpackTier previous() {
		return ordinal() == 0 ? null : values()[ordinal() - 1];
	}
}
