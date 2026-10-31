package velrondevs.botania.compat.sophisticated;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.neoforged.fml.ModList;

public final class SophisticatedOptional {
	public static final String ATM_TIERS = "sophisticatedatm";
	public static final String MASTERFUL_BOTANY = "masterfulbotany";
	public static final String ATM_UNOBTAINIUM_BACKPACK = ATM_TIERS + ":unobtainium_backpack";
	public static final String ATM_UNOBTAINIUM_BARREL = ATM_TIERS + ":unobtainium_barrel";
	public static final String ATM_UNOBTAINIUM_CHEST = ATM_TIERS + ":unobtainium_chest";
	public static final String ALFSTEEL_INGOT = MASTERFUL_BOTANY + ":alfsteel_ingot";

	private SophisticatedOptional() {}

	public static boolean isLoaded(String modId) {
		if (modId == null) {
			return true;
		}
		ModList list = ModList.get();
		return list != null && list.isLoaded(modId);
	}

	public static boolean atmTiers() {
		return isLoaded(ATM_TIERS);
	}

	public static JsonObject modLoaded(String modId) {
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "neoforge:mod_loaded");
		condition.addProperty("modid", modId);
		return condition;
	}

	public static JsonObject notModLoaded(String modId) {
		JsonObject condition = new JsonObject();
		condition.addProperty("type", "neoforge:not");
		condition.add("value", modLoaded(modId));
		return condition;
	}

	public static void addTierCondition(JsonArray conditions, String requiredMod) {
		if (requiredMod != null) {
			conditions.add(modLoaded(requiredMod));
		}
	}
}
