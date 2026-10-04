package velrondevs.botania.integration.arsnouveau;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ArsNouveauConfig {
	private static final ModConfigSpec.DoubleValue MANA_PER_SOURCE;
	private static final ModConfigSpec.IntValue MAX_TRANSFER_RATE;
	private static final ModConfigSpec.DoubleValue ARS_TO_BOTANIA;
	private static final ModConfigSpec.DoubleValue BOTANIA_TO_ARS;
	private static final ModConfigSpec.BooleanValue BOTANIA_PAYS_SPELLS;
	private static final ModConfigSpec.BooleanValue ARS_POWERS_BOTANIA_ITEMS;
	private static final ModConfigSpec.BooleanValue OVERFLOW_CHARGES_BOTANIA_ITEMS;
	private static final ModConfigSpec.BooleanValue MANA_COOKIE_REGEN;
	private static final ModConfigSpec SPEC;

	static {
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		builder.push("source_containers");
		MANA_PER_SOURCE = builder.defineInRange("mana_per_source", 15.0, 0.001, 1000000.0);
		MAX_TRANSFER_RATE = builder.defineInRange("max_transfer_rate", 1000, 1, 1000000);
		builder.pop();
		builder.push("player_mana");
		BOTANIA_PAYS_SPELLS = builder.define("botania_pays_spells", true);
		BOTANIA_TO_ARS = builder.defineInRange("botania_to_ars", 0.006, 0.000001, 1000000.0);
		ARS_POWERS_BOTANIA_ITEMS = builder.define("ars_powers_botania_items", true);
		ARS_TO_BOTANIA = builder.defineInRange("ars_to_botania", 4.0, 0.000001, 1000000.0);
		OVERFLOW_CHARGES_BOTANIA_ITEMS = builder.define("overflow_charges_botania_items", false);
		builder.pop();
		builder.push("items");
		MANA_COOKIE_REGEN = builder.define("mana_cookie_regen", true);
		builder.pop();
		SPEC = builder.build();
	}

	private ArsNouveauConfig() {}

	static void register(ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, SPEC, "botania-ars_nouveau-common.toml");
	}

	static double manaPerSource() {
		return MANA_PER_SOURCE.get();
	}

	static int maxTransferRate() {
		return MAX_TRANSFER_RATE.get();
	}

	static double arsToBotania() {
		return ARS_TO_BOTANIA.get();
	}

	static double botaniaToArs() {
		return BOTANIA_TO_ARS.get();
	}

	static boolean botaniaPaysSpells() {
		return BOTANIA_PAYS_SPELLS.get();
	}

	static boolean arsPowersBotaniaItems() {
		return ARS_POWERS_BOTANIA_ITEMS.get();
	}

	static boolean overflowChargesBotaniaItems() {
		return OVERFLOW_CHARGES_BOTANIA_ITEMS.get();
	}

	static boolean manaCookieRegen() {
		return MANA_COOKIE_REGEN.get();
	}
}
