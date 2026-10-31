package velrondevs.botania.compat.sophisticated.transfer;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.compat.sophisticated.BreweryUpgradeContainer;
import velrondevs.botania.compat.sophisticated.ElvenPortalUpgradeContainer;
import velrondevs.botania.compat.sophisticated.RunicAltarUpgradeContainer;

public enum StationKind {
	RUNIC_ALTAR("runic_altar", RunicAltarUpgradeContainer.class, RunicAltarRecipe.class),
	ELVEN_PORTAL("elven_portal", ElvenPortalUpgradeContainer.class, ElvenTradeRecipe.class),
	BREWERY("brewery", BreweryUpgradeContainer.class, BotanicalBreweryRecipe.class);

	private final String key;
	private final Class<?> containerClass;
	private final Class<?> recipeClass;

	StationKind(String key, Class<?> containerClass, Class<?> recipeClass) {
		this.key = key;
		this.containerClass = containerClass;
		this.recipeClass = recipeClass;
	}

	public String key() {
		return key;
	}

	public Class<?> containerClass() {
		return containerClass;
	}

	public boolean acceptsRecipe(Object recipe) {
		return recipeClass.isInstance(recipe);
	}

	public String missingUpgradeKey() {
		return "gui.botania.sophisticated.transfer.no_upgrade." + key;
	}

	@Nullable
	public static StationKind byId(int id) {
		StationKind[] values = values();
		return id >= 0 && id < values.length ? values[id] : null;
	}
}
