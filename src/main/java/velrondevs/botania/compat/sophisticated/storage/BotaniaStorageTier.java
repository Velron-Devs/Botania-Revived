package velrondevs.botania.compat.sophisticated.storage;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.SophisticatedOptional;

import java.util.Arrays;
import java.util.List;

public enum BotaniaStorageTier {
	MANASTEEL("manasteel", "Manasteel", "manacero", 280, 6, 0F, false, LibMisc.MOD_ID + ":manasteel_ingots", true, null),
	ELEMENTIUM("elementium", "Elementium", "elementio", 380, 7, 0F, false, LibMisc.MOD_ID + ":elementium_ingots", true, null),
	TERRASTEEL("terrasteel", "Terrasteel", "terracero", 480, 8, 1200F, true, LibMisc.MOD_ID + ":terrasteel_ingots", true, null),
	GAIA("gaia", "Gaia", "Gaia", 595, 10, 1200F, true, LibMisc.MOD_ID + ":gaia_ingot", false, null),
	ALFSTEEL("alfsteel", "Alfsteel", "alfsteel", 695, 10, 1200F, true, SophisticatedOptional.ALFSTEEL_INGOT, false, SophisticatedOptional.MASTERFUL_BOTANY);

	private final String id;
	private final String english;
	private final String spanish;
	private final int inventorySlots;
	private final int upgradeSlots;
	private final float explosionResistance;
	private final boolean fireResistant;
	private final String ingot;
	private final boolean ingotIsTag;
	private final String requiredMod;

	BotaniaStorageTier(String id, String english, String spanish, int inventorySlots, int upgradeSlots, float explosionResistance,
			boolean fireResistant, String ingot, boolean ingotIsTag, String requiredMod) {
		this.id = id;
		this.english = english;
		this.spanish = spanish;
		this.inventorySlots = inventorySlots;
		this.upgradeSlots = upgradeSlots;
		this.explosionResistance = explosionResistance;
		this.fireResistant = fireResistant;
		this.ingot = ingot;
		this.ingotIsTag = ingotIsTag;
		this.requiredMod = requiredMod;
	}

	public static List<BotaniaStorageTier> active() {
		return Arrays.stream(values()).filter(BotaniaStorageTier::isActive).toList();
	}

	public String id() {
		return id;
	}

	public String english() {
		return english;
	}

	public String spanish() {
		return spanish;
	}

	public int defaultInventorySlots() {
		return inventorySlots;
	}

	public int defaultUpgradeSlots() {
		return upgradeSlots;
	}

	public float explosionResistance() {
		return explosionResistance;
	}

	public boolean fireResistant() {
		return fireResistant;
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

	public String barrelName() {
		return id + "_barrel";
	}

	public String chestName() {
		return id + "_chest";
	}

	public String previousId() {
		BotaniaStorageTier previous = previous();
		return previous == null ? "netherite" : previous.id();
	}

	public String upgradeName() {
		return previousId() + "_to_" + id + "_tier_upgrade";
	}

	public BotaniaStorageTier previous() {
		return ordinal() == 0 ? null : values()[ordinal() - 1];
	}
}
