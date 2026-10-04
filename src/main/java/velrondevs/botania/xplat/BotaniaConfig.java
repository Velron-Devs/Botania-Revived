package velrondevs.botania.xplat;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.block.flower.functional.LooniumMode;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.BotaniaModules;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class BotaniaConfig {
	public interface ConfigAccess {
		boolean blockBreakParticles();
		boolean blockBreakParticlesTool();
		boolean chargingAnimationEnabled();
		boolean silentSpreaders();
		int spreaderTraceTime();
		boolean enderPickpocketEnabled();
		boolean enchanterEnabled();
		boolean relicsEnabled();
		boolean invertMagnetRing();
		int harvestLevelWeight();
		int harvestLevelBore();
		boolean gogSpawnWithLexicon();
		int gogIslandScaleMultiplier();
		List<String> rannuncarpusItemBlacklist();
		List<String> rannuncarpusModBlacklist();
		boolean witherManaRoseEnabled();
		int witherManaRoseManaPerStar();
		int witherManaRoseGenerationInterval();
		int witherManaRoseGenerationPerCycle();
		int witherManaRoseBufferSize();
		int witherManaRoseOutputPerTick();
		int witherManaRoseCooldown();
		boolean asgardandelionEnabled();
		int asgardandelionManaPerTick();
		int terraShattererMaxLevel();
		LooniumMode looniumMode();
		int looniumLootManaCost();
		int looniumLootCooldown();
		int looniumLootItemsPerCycle();
		int looniumLootRange();
	}

	public interface ClientConfigAccess {
		boolean lexiconRotatingItems();
		boolean subtlePowerSystem();
		boolean staticWandBeam();
		boolean boundBlockWireframe();
		boolean lexicon3dModel();
		double flowerParticleFrequency();
		boolean elfPortalParticlesEnabled();
		boolean renderAccessories();
		boolean enableSeasonalFeatures();
		boolean enableFancySkybox();
		boolean enableFancySkyboxInNormalWorlds();
		int manaBarHeight();
		boolean staticFloaters();
		boolean debugInfo();
		boolean referencesEnabled();
		boolean splashesEnabled();
		boolean useShaders();
	}

	private static ConfigAccess config = null;
	private static ClientConfigAccess clientConfig = null;

	public static ConfigAccess common() {
		return config;
	}

	public static ClientConfigAccess client() {
		return clientConfig;
	}

	public static void setCommon(ConfigAccess access) {
		if (config != null) {
			BotaniaAPI.LOGGER.warn("ConfigAccess was replaced! Old {} New {}",
					config.getClass().getName(), access.getClass().getName());
		}
		config = access;
	}

	public static void setClient(ClientConfigAccess access) {
		if (clientConfig != null) {
			BotaniaAPI.LOGGER.warn("ClientConfigAccess was replaced! Old {} New {}",
					clientConfig.getClass().getName(), access.getClass().getName());
		}
		clientConfig = access;
	}

	public static void resetPatchouliFlags() {
		PatchouliAPI.get().setConfigFlag("botania:relics", common().relicsEnabled());
		PatchouliAPI.get().setConfigFlag("botania:enchanter", common().enchanterEnabled());
		PatchouliAPI.get().setConfigFlag("botania:ender_hand_pickpocket", common().enderPickpocketEnabled());
		PatchouliAPI.get().setConfigFlag("botania:wither_mana_rose", common().witherManaRoseEnabled());
		PatchouliAPI.get().setConfigFlag("botania:loonium_mobs", common().looniumMode() == LooniumMode.MOBS);
		for (BotaniaModule module : BotaniaModules.all()) {
			PatchouliAPI.get().setConfigFlag(module.patchouliFlag(), module.isEnabled());
		}
	}
}
