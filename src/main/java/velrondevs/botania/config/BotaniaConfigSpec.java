package velrondevs.botania.config;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;

import org.apache.commons.lang3.tuple.Pair;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.compat.masterful.MasterfulBotanyCompat;
import velrondevs.botania.common.block.flower.functional.LooniumBlockEntity;
import velrondevs.botania.common.block.flower.functional.LooniumMode;
import velrondevs.botania.common.block.flower.functional.MarimorphosisBlockEntity;
import velrondevs.botania.common.block.flower.functional.OrechidBlockEntity;
import velrondevs.botania.common.block.flower.functional.OrechidIgnemBlockEntity;
import velrondevs.botania.common.block.flower.generating.WitherManaRoseBlockEntity;
import velrondevs.botania.module.asgard.AsgardandelionBlockEntity;
import velrondevs.botania.common.entity.GaiaGuardianEntity;
import velrondevs.botania.common.entity.ManaSparkEntity;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackTier;
import velrondevs.botania.compat.sophisticated.storage.BotaniaStorageTier;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.xplat.BotaniaConfig;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class BotaniaConfigSpec {
	private static <T> T safe(ModConfigSpec.ConfigValue<T> value) {
		try {
			return value.get();
		} catch (IllegalStateException e) {
			return value.getDefault();
		}
	}


	private static class Client implements BotaniaConfig.ClientConfigAccess {
		public final ModConfigSpec.BooleanValue useShaders;
		public final ModConfigSpec.BooleanValue lexiconRotatingItems;
		public final ModConfigSpec.BooleanValue subtlePowerSystem;
		public final ModConfigSpec.BooleanValue staticWandBeam;
		public final ModConfigSpec.BooleanValue boundBlockWireframe;
		public final ModConfigSpec.BooleanValue lexicon3dModel;
		public final ModConfigSpec.DoubleValue flowerParticleFrequency;
		public final ModConfigSpec.BooleanValue elfPortalParticlesEnabled;
		public final ModConfigSpec.BooleanValue renderAccessories;
		public final ModConfigSpec.BooleanValue enableSeasonalFeatures;
		public final ModConfigSpec.BooleanValue enableFancySkybox;
		public final ModConfigSpec.BooleanValue enableFancySkyboxInNormalWorlds;

		public final ModConfigSpec.IntValue manaBarHeight;

		public final ModConfigSpec.BooleanValue staticFloaters;
		public final ModConfigSpec.BooleanValue debugInfo;
		public final ModConfigSpec.BooleanValue referencesEnabled;
		public final ModConfigSpec.BooleanValue splashesEnabled;

		public Client(ModConfigSpec.Builder builder) {
			builder.push("rendering");
			useShaders = builder
					.comment("Set this to false to disable the use of shaders for some of the mod's renders. (Requires game restart)")
					.define("shaders", true);
			boundBlockWireframe = builder
					.comment("Set this to false to disable the wireframe when looking a block bound to something (spreaders, flowers, etc).")
					.define("boundBlockWireframe", true);
			renderAccessories = builder
					.comment("Set this to false to disable rendering of accessories in the player.")
					.define("accessories", true);
			manaBarHeight = builder
					.comment("The height of the mana display bar in above the XP bar. You can change this if you have a mod that changes where the XP bar is.")
					.defineInRange("manaBarHeight", 29, 0, Integer.MAX_VALUE);
			staticFloaters = builder
					.comment("Set this to true if you use lots of floating flowers and are experiencing rendering lag. Will disable the floating flowers' animations and render them statically for a major performance boost. Hit F3+A in-world after toggling this.")
					.define("staticFloaters", false);

			builder.push("fancySkybox");
			enableFancySkybox = builder
					.comment("Set this to false to disable the fancy skybox in Garden of Glass")
					.define("enabled", true);
			enableFancySkyboxInNormalWorlds = builder
					.comment("Set this to true to enable the fancy skybox in non Garden of Glass worlds. (Does not require Garden of Glass loaded to use, needs 'fancySkybox.enabled' to be true as well)")
					.define("normalWorlds", false);
			builder.pop();

			builder.push("lexicon");
			lexiconRotatingItems = builder
					.comment("Set this to false to disable the rotating items in the petal and rune entries in the Lexica Botania.")
					.define("rotatingItems", true);
			lexicon3dModel = builder
					.comment("Set this to false to disable the animated 3D render for the Lexica Botania.")
					.define("render_3d", true);
			builder.pop(2);

			builder.push("particles");
			subtlePowerSystem = builder
					.comment("Set this to true to set the power system's particles to be a lot more subtle. Good for low-end systems, if the particles are causing lag.")
					.define("powerSystem", false);
			staticWandBeam = builder
					.comment("Set this to true to use a static wand beam that shows every single position of the burst, similar to the way it used to work on old Botania versions. Warning: Disabled by default because it may be laggy.")
					.define("staticWandBeam", false);
			flowerParticleFrequency = builder
					.comment("The frequency in which particles spawn from normal (worldgen) mystical flowers")
					.defineInRange("flowerFrequency", 0.75, Double.MIN_VALUE, Double.MAX_VALUE);
			elfPortalParticlesEnabled = builder
					.comment("Set this to false to disable the particles in the elven portal.")
					.define("elvenPortal", true);
			builder.pop();

			enableSeasonalFeatures = builder
					.comment("Set this to false to disable seasonal features, such as halloween and christmas.")
					.define("seasonalFeatures", true);
			debugInfo = builder
					.comment("Set to false to disable Botania's messages in the F3 debug screen")
					.define("debugInfo", true);
			referencesEnabled = builder
					.comment("Set this to false to disable the references in the flower tooltips. (You monster D:)")
					.define("references", true);
			splashesEnabled = builder
					.comment("Set this to false to disable Botania's splashes in the main menu.")
					.define("splashes", true);
		}

		@Override
		public boolean lexiconRotatingItems() {
			return safe(lexiconRotatingItems);
		}

		@Override
		public boolean subtlePowerSystem() {
			return safe(subtlePowerSystem);
		}

		@Override
		public boolean staticWandBeam() {
			return safe(staticWandBeam);
		}

		@Override
		public boolean boundBlockWireframe() {
			return safe(boundBlockWireframe);
		}

		@Override
		public boolean lexicon3dModel() {
			return safe(lexicon3dModel);
		}

		@Override
		public double flowerParticleFrequency() {
			return safe(flowerParticleFrequency);
		}

		@Override
		public boolean elfPortalParticlesEnabled() {
			return safe(elfPortalParticlesEnabled);
		}

		@Override
		public boolean renderAccessories() {
			return safe(renderAccessories);
		}

		@Override
		public boolean enableSeasonalFeatures() {
			return safe(enableSeasonalFeatures);
		}

		@Override
		public boolean enableFancySkybox() {
			return safe(enableFancySkybox);
		}

		@Override
		public boolean enableFancySkyboxInNormalWorlds() {
			return safe(enableFancySkyboxInNormalWorlds);
		}

		@Override
		public int manaBarHeight() {
			return safe(manaBarHeight);
		}

		@Override
		public boolean staticFloaters() {
			return safe(staticFloaters);
		}

		@Override
		public boolean debugInfo() {
			return safe(debugInfo);
		}

		@Override
		public boolean referencesEnabled() {
			return safe(referencesEnabled);
		}

		@Override
		public boolean splashesEnabled() {
			return safe(splashesEnabled);
		}

		@Override
		public boolean useShaders() {
			return this.useShaders.get();
		}
	}

	public static final Client CLIENT;
	public static final ModConfigSpec CLIENT_SPEC;
	static {
		final Pair<Client, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Client::new);
		CLIENT_SPEC = specPair.getRight();
		CLIENT = specPair.getLeft();
	}

	private static class Common implements BotaniaConfig.ConfigAccess {
		public final ModConfigSpec.BooleanValue blockBreakParticles;
		public final ModConfigSpec.BooleanValue blockBreakParticlesTool;
		public final ModConfigSpec.BooleanValue chargingAnimationEnabled;
		public final ModConfigSpec.BooleanValue silentSpreaders;
		public final ModConfigSpec.IntValue spreaderTraceTime;
		public final ModConfigSpec.BooleanValue flowerForceCheck;
		public final ModConfigSpec.BooleanValue enderPickpocketEnabled;

		public final ModConfigSpec.BooleanValue enchanterEnabled;
		public final ModConfigSpec.BooleanValue relicsEnabled;
		public final ModConfigSpec.BooleanValue invertMagnetRing;

		public final ModConfigSpec.IntValue harvestLevelWeight;
		public final ModConfigSpec.IntValue harvestLevelBore;

		public final ModConfigSpec.BooleanValue gogSpawnWithLexicon;
		public final ModConfigSpec.IntValue gogIslandScaleMultiplier;

		public final ModConfigSpec.BooleanValue witherManaRoseEnabled;
		public final ModConfigSpec.IntValue witherManaRoseManaPerStar;
		public final ModConfigSpec.IntValue witherManaRoseGenerationInterval;
		public final ModConfigSpec.IntValue witherManaRoseGenerationPerCycle;
		public final ModConfigSpec.IntValue witherManaRoseBufferSize;
		public final ModConfigSpec.IntValue witherManaRoseOutputPerTick;
		public final ModConfigSpec.IntValue witherManaRoseCooldown;
		public final ModConfigSpec.BooleanValue asgardandelionEnabled;
		public final ModConfigSpec.IntValue asgardandelionManaPerTick;
		public final ModConfigSpec.IntValue terraShattererMaxLevel;
		public final ModConfigSpec.EnumValue<LooniumMode> looniumMode;
		public final ModConfigSpec.IntValue looniumLootManaCost;
		public final ModConfigSpec.IntValue looniumLootCooldown;
		public final ModConfigSpec.IntValue looniumLootItemsPerCycle;
		public final ModConfigSpec.IntValue looniumLootRange;
		public final ModConfigSpec.IntValue fabulousManaPoolCapacity;
		public final ModConfigSpec.BooleanValue sparkBatchTransfer;
		public final ModConfigSpec.IntValue sparkBatchAmount;
		public final ModConfigSpec.IntValue orechidCost;
		public final ModConfigSpec.IntValue orechidRange;
		public final ModConfigSpec.IntValue orechidRangeY;
		public final ModConfigSpec.IntValue orechidIgnemCost;
		public final ModConfigSpec.IntValue orechidIgnemRange;
		public final ModConfigSpec.IntValue orechidIgnemRangeY;
		public final ModConfigSpec.BooleanValue orechidIgnemAnyDimension;
		public final ModConfigSpec.IntValue marimorphosisCost;
		public final ModConfigSpec.IntValue marimorphosisInterval;
		public final ModConfigSpec.IntValue marimorphosisRange;
		public final ModConfigSpec.IntValue marimorphosisRangeY;
		public final ModConfigSpec.BooleanValue entropinnyumAcceptOverflow;
		public final ModConfigSpec.IntValue gaiaGuardianDamageCap;
		public final ModConfigSpec.BooleanValue reducedUpdateFrequency;
		public final ModConfigSpec.IntValue hopperhockInterval;
		public final ModConfigSpec.IntValue functionalFlowerRedstoneInterval;

		public final ModConfigSpec.ConfigValue<List<? extends String>> rannuncarpusItemBlacklist;
		public final ModConfigSpec.ConfigValue<List<? extends String>> rannuncarpusModBlacklist;
		public final Map<String, ModConfigSpec.BooleanValue> modules = new LinkedHashMap<>();
		public final ArsNouveauValues arsNouveau;
		public final SophisticatedValues sophisticated;
		public final ManaMotorValues manaMotor;
		public final ThaumaturgeNodeBoostValues thaumaturgeNodeBoost;
		public final ModConfigSpec.DoubleValue alfsteelSpreaderSpeedMultiplier;

		public Common(ModConfigSpec.Builder builder) {
			builder.push("blockBreakingParticles");
			blockBreakParticles = builder
					.comment("Set this to false to remove the block breaking particles from the flowers and other items in the mod.")
					.define("enabled", true);
			blockBreakParticlesTool = builder
					.comment("Set this to false to remove the block breaking particles from the Terra Shatterer, as there can be a good amount in higher levels.")
					.define("toolEnabled", true);
			builder.pop();

			builder.push("manaSpreaders");
			silentSpreaders = builder
					.comment("Set this to true to disable the mana spreader shooting sound")
					.define("silent", false);
			spreaderTraceTime = builder
					.comment("How many ticks into the future will mana spreaders attempt to predict where mana bursts go? Setting this lower will improve spreader performance, but will cause them to not fire at targets that are too far away.")
					.defineInRange("traceTime", 400, 1, Integer.MAX_VALUE);
			builder.pop();

			builder.push("harvestLevels");
			harvestLevelWeight = builder
					.comment("The harvest level of the Mana Lens: Weight. 3 is diamond level. Defaults to 2 (iron level)")
					.defineInRange("weightLens", 2, 0, Integer.MAX_VALUE);
			harvestLevelBore = builder
					.comment("The harvest level of the Mana Lens: Bore. 3 is diamond level. Defaults to 3")
					.defineInRange("boreLens", 3, 0, Integer.MAX_VALUE);
			builder.pop();

			chargingAnimationEnabled = builder
					.comment("Set this to false to disable the animation when an item is charging on top of a mana pool")
					.define("chargeAnimation", true);
			flowerForceCheck = builder
					.comment("Turn this off ONLY IF you're on an extremely large world with an exaggerated count of Mana Spreaders/Mana Pools and are experiencing TPS lag. This toggles whether flowers are strict with their checking for connecting to pools/spreaders or just check whenever possible.")
					.define("flowerBindingForceCheck", true);
			enderPickpocketEnabled = builder
					.comment("Set to false to disable the ability for the Hand of Ender to pickpocket other players' ender chests")
					.define("enderPickpocket", true);
			enchanterEnabled = builder
					.comment("Set this to false to disable the Mana Enchanter. Since some people find it OP or something. This only disables the entry and creation. Old ones that are already in the world will stay.")
					.define("manaEnchanter", true);
			relicsEnabled = builder
					.comment("Set this to false to disable the Relic System. This only disables the entries, drops and achievements. Old ones that are already in the world will stay.")
					.define("relics", true);
			invertMagnetRing = builder
					.comment("Set this to true to invert the Ring of Magnetization's controls (from shift to stop to shift to work)")
					.define("invertMagnetRing", false);
			gogSpawnWithLexicon = builder
					.comment("Set this to false to disable spawning with a Lexica Botania in Garden of Glass worlds, if you are modifying the modpack's progression to not start with Botania.")
					.define("gardenOfGlass.spawnWithLexicon", true);
			gogIslandScaleMultiplier = builder
					.comment("""
						The multiplier for island distances for multiplayer Garden of Glass worlds.
						Islands are placed on a grid with 256 blocks between points, with the spawn island always being placed on 256, 256.
						By default, the scale is 8, putting each island on points separated by 2048 blocks.
						Values below 4 (1024 block spacing) are not recommended due to Nether portal collisions.""")
					.defineInRange("gardenOfGlass.islandScaleMultiplier", 8, 1, 512);
			rannuncarpusItemBlacklist = builder
					.comment("List of item registry names that will be ignored by rannuncarpuses when placing blocks.")
					.defineList("rannuncarpus.itemBlacklist", Collections.emptyList(), o -> o instanceof String s && ResourceLocation.tryParse(s) != null);
			rannuncarpusModBlacklist = builder
					.comment("List of mod names for rannuncarpuses to ignore.\n" +
							"Ignores Storage Drawers by default due to crashes with placing drawer blocks without player involvement.")
					.defineList("rannuncarpus.modBlacklist", Collections.singletonList("storagedrawers"), o -> o instanceof String s && ResourceLocation.tryParse(s + ":test") != null);

			builder.push("witherManaRose");
			witherManaRoseEnabled = builder.define("enabled", true);
			witherManaRoseManaPerStar = builder.defineInRange("manaPerStar", WitherManaRoseBlockEntity.DEFAULT_MANA_PER_STAR, 1, Integer.MAX_VALUE);
			witherManaRoseGenerationInterval = builder.defineInRange("generationInterval", WitherManaRoseBlockEntity.DEFAULT_GENERATION_INTERVAL, 1, 1200);
			witherManaRoseGenerationPerCycle = builder.defineInRange("generationPerCycle", WitherManaRoseBlockEntity.DEFAULT_GENERATION_PER_CYCLE, 1, Integer.MAX_VALUE);
			witherManaRoseBufferSize = builder.defineInRange("bufferSize", WitherManaRoseBlockEntity.DEFAULT_BUFFER_SIZE, 1, Integer.MAX_VALUE);
			witherManaRoseOutputPerTick = builder.defineInRange("outputPerTick", WitherManaRoseBlockEntity.DEFAULT_OUTPUT_PER_TICK, 1, Integer.MAX_VALUE);
			witherManaRoseCooldown = builder.defineInRange("cooldown", WitherManaRoseBlockEntity.DEFAULT_COOLDOWN, 0, Integer.MAX_VALUE);
			builder.pop();

			builder.push("loonium");
			looniumMode = builder
					.comment("LOOT: the Loonium drops dungeon and structure loot directly as items (Botania 1.7.10 behavior). MOBS: the Loonium spawns monsters that carry the loot and drop it when killed.")
					.defineEnum("mode", LooniumMode.LOOT);
			looniumLootManaCost = builder
					.comment("Mana spent per cycle in LOOT mode.")
					.defineInRange("lootManaCost", LooniumBlockEntity.DEFAULT_LOOT_MANA_COST, 1, 10000000);
			looniumLootCooldown = builder
					.comment("Ticks between cycles in LOOT mode.")
					.defineInRange("lootCooldown", LooniumBlockEntity.DEFAULT_LOOT_COOLDOWN, 1, 1200000);
			looniumLootItemsPerCycle = builder
					.comment("Items dropped per cycle in LOOT mode.")
					.defineInRange("lootItemsPerCycle", LooniumBlockEntity.DEFAULT_LOOT_ITEMS_PER_CYCLE, 1, 64);
			looniumLootRange = builder
					.comment("Drop radius in blocks in LOOT mode.")
					.defineInRange("lootRange", LooniumBlockEntity.DEFAULT_LOOT_RANGE, 1, 16);
			builder.pop();

			builder.push("asgardandelion");
			asgardandelionEnabled = builder.define("enabled", true);
			asgardandelionManaPerTick = builder.defineInRange("manaPerTick", AsgardandelionBlockEntity.DEFAULT_MANA_PER_TICK, 1, AsgardandelionBlockEntity.MAX_MANA_PER_TICK);
			builder.pop();

			builder.push("terraShatterer");
			terraShattererMaxLevel = builder
					.comment("Highest level the Terra Shatterer can reach. Level 0 = rank D (0 mana), 1 = rank C (10,000 mana), 2 = rank B (1,000,000 mana), 3 = rank A (10,000,000 mana), 4 = rank S (100,000,000 mana), 5 = rank SS (1,000,000,000 mana).")
					.defineInRange("maxLevel", TerraShattererItem.MAX_LEVEL, 0, TerraShattererItem.MAX_LEVEL);
			builder.pop();

			builder.push("balance");
			builder.push("mana_pool");
			fabulousManaPoolCapacity = builder.defineInRange("fabulous_capacity", 100000000, 1, Integer.MAX_VALUE);
			builder.pop();
			builder.push("mana_spark");
			sparkBatchTransfer = builder.define("batch_transfer", true);
			sparkBatchAmount = builder.defineInRange("batch_amount", ManaSparkEntity.DEFAULT_BATCH_AMOUNT, 1, Integer.MAX_VALUE);
			builder.pop();
			builder.push("flowers");
			orechidCost = builder.defineInRange("orechid_cost", 100, 1, Integer.MAX_VALUE);
			orechidRange = builder.defineInRange("orechid_range", 3, 0, 64);
			orechidRangeY = builder.defineInRange("orechid_range_y", 1, 0, 64);
			orechidIgnemCost = builder.defineInRange("orechid_ignem_cost", 120, 1, Integer.MAX_VALUE);
			orechidIgnemRange = builder.defineInRange("orechid_ignem_range", 3, 0, 64);
			orechidIgnemRangeY = builder.defineInRange("orechid_ignem_range_y", 1, 0, 64);
			orechidIgnemAnyDimension = builder.define("orechid_ignem_any_dimension", true);
			marimorphosisCost = builder.defineInRange("marimorphosis_cost", 25, 1, MarimorphosisBlockEntity.MAX_MANA);
			marimorphosisInterval = builder.defineInRange("marimorphosis_interval", 10, 1, 1200);
			marimorphosisRange = builder.defineInRange("marimorphosis_range", 4, 0, 64);
			marimorphosisRangeY = builder.defineInRange("marimorphosis_range_y", 2, 0, 64);
			entropinnyumAcceptOverflow = builder.define("entropinnyum_accept_overflow", true);
			builder.pop();
			builder.push("gaia_guardian");
			gaiaGuardianDamageCap = builder.defineInRange("damage_cap", 10000, 1, Integer.MAX_VALUE);
			builder.pop(2);

			builder.push("performance");
			reducedUpdateFrequency = builder.define("reduced_update_frequency", true);
			hopperhockInterval = builder.defineInRange("hopperhock_interval", 40, 1, 1200);
			functionalFlowerRedstoneInterval = builder.defineInRange("functional_flower_redstone_interval", 20, 1, 1200);
			builder.pop();

			builder.push("integrations");
			arsNouveau = new ArsNouveauValues(builder);
			sophisticated = new SophisticatedValues(builder);
			manaMotor = new ManaMotorValues(builder);
			builder.push("masterful_botany");
			alfsteelSpreaderSpeedMultiplier = builder
					.comment("Speed multiplier of the Alfsteel Mana Spreader (only exists when Masterful Botany is installed) relative to the Gaia Mana Spreader. Its mana throughput per second, internal capacity and burst mana scale with this value.")
					.defineInRange("alfsteel_spreader_speed_multiplier", MasterfulBotanyCompat.DEFAULT_ALFSTEEL_SPREADER_SPEED_MULTIPLIER, 1.0, 1000.0);
			builder.pop();
			thaumaturgeNodeBoost = new ThaumaturgeNodeBoostValues(builder);
			builder.pop();

			builder.push(BotaniaModules.CONFIG_SECTION);
			for (BotaniaModule module : BotaniaModules.all()) {
				String action = module.enabledByDefault() ? "Set this to false to disable" : "Set this to true to enable";
				modules.put(module.id(), builder
						.comment(action + " the " + module.displayName() + " module. A disabled module registers no blocks, items, recipes, Lexicon entries or client rendering. Requires a game restart.")
						.gameRestart()
						.define(module.id(), module.enabledByDefault()));
			}
			builder.pop();
		}

		@Override
		public boolean blockBreakParticles() {
			return safe(blockBreakParticles);
		}

		@Override
		public boolean blockBreakParticlesTool() {
			return safe(blockBreakParticlesTool);
		}

		@Override
		public boolean chargingAnimationEnabled() {
			return safe(chargingAnimationEnabled);
		}

		@Override
		public boolean silentSpreaders() {
			return safe(silentSpreaders);
		}

		@Override
		public int spreaderTraceTime() {
			return safe(spreaderTraceTime);
		}

		@Override
		public boolean enderPickpocketEnabled() {
			return safe(enderPickpocketEnabled);
		}

		@Override
		public boolean enchanterEnabled() {
			return safe(enchanterEnabled);
		}

		@Override
		public boolean relicsEnabled() {
			return safe(relicsEnabled);
		}

		@Override
		public boolean invertMagnetRing() {
			return safe(invertMagnetRing);
		}

		@Override
		public int harvestLevelWeight() {
			return safe(harvestLevelWeight);
		}

		@Override
		public int harvestLevelBore() {
			return safe(harvestLevelBore);
		}

		@Override
		public boolean gogSpawnWithLexicon() {
			return safe(gogSpawnWithLexicon);
		}

		@Override
		public int gogIslandScaleMultiplier() {
			return safe(gogIslandScaleMultiplier);
		}

		@Override
		public boolean witherManaRoseEnabled() {
			return safe(witherManaRoseEnabled);
		}

		@Override
		public int witherManaRoseManaPerStar() {
			return safe(witherManaRoseManaPerStar);
		}

		@Override
		public int witherManaRoseGenerationInterval() {
			return safe(witherManaRoseGenerationInterval);
		}

		@Override
		public int witherManaRoseGenerationPerCycle() {
			return safe(witherManaRoseGenerationPerCycle);
		}

		@Override
		public int witherManaRoseBufferSize() {
			return safe(witherManaRoseBufferSize);
		}

		@Override
		public int witherManaRoseOutputPerTick() {
			return safe(witherManaRoseOutputPerTick);
		}

		@Override
		public int witherManaRoseCooldown() {
			return safe(witherManaRoseCooldown);
		}

		@Override
		public boolean asgardandelionEnabled() {
			return safe(asgardandelionEnabled);
		}

		@Override
		public int asgardandelionManaPerTick() {
			return safe(asgardandelionManaPerTick);
		}

		@Override
		public LooniumMode looniumMode() {
			return safe(looniumMode);
		}

		@Override
		public int looniumLootManaCost() {
			return safe(looniumLootManaCost);
		}

		@Override
		public int looniumLootCooldown() {
			return safe(looniumLootCooldown);
		}

		@Override
		public int looniumLootItemsPerCycle() {
			return safe(looniumLootItemsPerCycle);
		}

		@Override
		public int looniumLootRange() {
			return safe(looniumLootRange);
		}

		@Override
		public int terraShattererMaxLevel() {
			return safe(terraShattererMaxLevel);
		}

		@Override
		public int fabulousManaPoolCapacity() {
			return safe(fabulousManaPoolCapacity);
		}

		@Override
		public boolean sparkBatchTransfer() {
			return safe(sparkBatchTransfer);
		}

		@Override
		public int sparkBatchAmount() {
			return safe(sparkBatchAmount);
		}

		@Override
		public int orechidCost() {
			return safe(orechidCost);
		}

		@Override
		public int orechidRange() {
			return safe(orechidRange);
		}

		@Override
		public int orechidRangeY() {
			return safe(orechidRangeY);
		}

		@Override
		public int orechidIgnemCost() {
			return safe(orechidIgnemCost);
		}

		@Override
		public int orechidIgnemRange() {
			return safe(orechidIgnemRange);
		}

		@Override
		public int orechidIgnemRangeY() {
			return safe(orechidIgnemRangeY);
		}

		@Override
		public boolean orechidIgnemAnyDimension() {
			return safe(orechidIgnemAnyDimension);
		}

		@Override
		public int marimorphosisCost() {
			return safe(marimorphosisCost);
		}

		@Override
		public int marimorphosisInterval() {
			return safe(marimorphosisInterval);
		}

		@Override
		public int marimorphosisRange() {
			return safe(marimorphosisRange);
		}

		@Override
		public int marimorphosisRangeY() {
			return safe(marimorphosisRangeY);
		}

		@Override
		public boolean entropinnyumAcceptOverflow() {
			return safe(entropinnyumAcceptOverflow);
		}

		@Override
		public int gaiaGuardianDamageCap() {
			return safe(gaiaGuardianDamageCap);
		}

		@Override
		public boolean reducedUpdateFrequency() {
			return safe(reducedUpdateFrequency);
		}

		@Override
		public int hopperhockInterval() {
			return safe(hopperhockInterval);
		}

		@Override
		public int functionalFlowerRedstoneInterval() {
			return safe(functionalFlowerRedstoneInterval);
		}

		@Override
		public double alfsteelSpreaderSpeedMultiplier() {
			return safe(alfsteelSpreaderSpeedMultiplier);
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> rannuncarpusItemBlacklist() {
			return (List<String>) rannuncarpusItemBlacklist.get();
		}

		@SuppressWarnings("unchecked")
		@Override
		public List<String> rannuncarpusModBlacklist() {
			return (List<String>) rannuncarpusModBlacklist.get();
		}
	}

	public static final class ArsNouveauValues {
		public final ModConfigSpec.DoubleValue manaPerSource;
		public final ModConfigSpec.IntValue maxTransferRate;
		public final ModConfigSpec.DoubleValue arsToBotania;
		public final ModConfigSpec.DoubleValue botaniaToArs;
		public final ModConfigSpec.BooleanValue botaniaPaysSpells;
		public final ModConfigSpec.BooleanValue arsPowersBotaniaItems;
		public final ModConfigSpec.BooleanValue overflowChargesBotaniaItems;
		public final ModConfigSpec.BooleanValue manaCookieRegen;

		private ArsNouveauValues(ModConfigSpec.Builder builder) {
			builder.push("ars_nouveau");
			builder.push("source_containers");
			manaPerSource = builder.defineInRange("mana_per_source", 15.0, 0.001, 1000000.0);
			maxTransferRate = builder.defineInRange("max_transfer_rate", 1000, 1, 1000000);
			builder.pop();
			builder.push("player_mana");
			botaniaPaysSpells = builder.define("botania_pays_spells", true);
			botaniaToArs = builder.defineInRange("botania_to_ars", 0.006, 0.000001, 1000000.0);
			arsPowersBotaniaItems = builder.define("ars_powers_botania_items", true);
			arsToBotania = builder.defineInRange("ars_to_botania", 4.0, 0.000001, 1000000.0);
			overflowChargesBotaniaItems = builder.define("overflow_charges_botania_items", false);
			builder.pop();
			builder.push("items");
			manaCookieRegen = builder.define("mana_cookie_regen", true);
			builder.pop(2);
		}
	}

	public static final class SophisticatedValues {
		private static final int MAX_INVENTORY_SLOTS = 1000;
		public static final int DEFAULT_PILE_BASE_CAPACITY = 1000000;

		public final ModConfigSpec.IntValue pileBaseCapacity;
		public final ModConfigSpec.BooleanValue pileMigrateDefaults;
		public final ModConfigSpec.BooleanValue pileCapacityMigrated;
		public final ModConfigSpec.DoubleValue pileStackRatio;
		public final ModConfigSpec.IntValue pileMaxPerStorage;
		public final ModConfigSpec.BooleanValue pileAbsorbFromItems;
		public final ModConfigSpec.IntValue pileAbsorbInterval;
		public final ModConfigSpec.IntValue pileAbsorbAmount;
		public final ModConfigSpec.BooleanValue pileAbsorbFromSlot;
		public final ModConfigSpec.BooleanValue pilePullFromPlayer;
		public final ModConfigSpec.BooleanValue pileReceiveFromWorld;
		public final ModConfigSpec.BooleanValue pileChargeItems;
		public final ModConfigSpec.IntValue pileChargeInterval;
		public final ModConfigSpec.IntValue pileChargeRate;
		public final ModConfigSpec.IntValue pileInfusionInterval;
		public final ModConfigSpec.IntValue pileInfusionMax;
		public final ModConfigSpec.BooleanValue pileInfusionAutoFeed;
		public final ModConfigSpec.BooleanValue pileInfusionAutoOutput;
		public final ModConfigSpec.BooleanValue linkEnabled;
		public final ModConfigSpec.IntValue linkMaxPerStorage;
		public final ModConfigSpec.BooleanValue backpacksEnabled;
		public final Map<String, ModConfigSpec.IntValue> backpackSlots = new HashMap<>();
		public final Map<String, ModConfigSpec.IntValue> backpackUpgradeSlots = new HashMap<>();
		public final ModConfigSpec.BooleanValue storageEnabled;
		public final Map<BotaniaStorageTier, ModConfigSpec.IntValue> storageSlots = new EnumMap<>(BotaniaStorageTier.class);
		public final Map<BotaniaStorageTier, ModConfigSpec.IntValue> storageUpgradeSlots = new EnumMap<>(BotaniaStorageTier.class);
		public final ModConfigSpec.BooleanValue altarEnabled;
		public final ModConfigSpec.IntValue altarMaxPerStorage;
		public final ModConfigSpec.IntValue altarInterval;
		public final ModConfigSpec.IntValue altarCostPercent;
		public final ModConfigSpec.BooleanValue altarRequireWand;
		public final ModConfigSpec.BooleanValue portalEnabled;
		public final ModConfigSpec.IntValue portalMaxPerStorage;
		public final ModConfigSpec.IntValue portalInterval;
		public final ModConfigSpec.IntValue portalCostPerTrade;
		public final ModConfigSpec.BooleanValue portalRequirePylons;
		public final ModConfigSpec.BooleanValue breweryEnabled;
		public final ModConfigSpec.IntValue breweryMaxPerStorage;
		public final ModConfigSpec.IntValue breweryInterval;
		public final ModConfigSpec.IntValue breweryCostPercent;
		public final ModConfigSpec.BooleanValue apothecaryEnabled;
		public final ModConfigSpec.IntValue apothecaryMaxPerStorage;
		public final ModConfigSpec.IntValue apothecaryInterval;
		public final ModConfigSpec.BooleanValue apothecaryAutoFeed;
		public final ModConfigSpec.BooleanValue apothecaryConsumeWater;
		public final ModConfigSpec.BooleanValue terraPlateEnabled;
		public final ModConfigSpec.IntValue terraPlateMaxPerStorage;
		public final ModConfigSpec.IntValue terraPlateInterval;
		public final ModConfigSpec.IntValue terraPlateCostPercent;
		public final ModConfigSpec.BooleanValue terraPlateAutoFeed;
		public final ModConfigSpec.BooleanValue terraPlateRequireSpark;
		public final ModConfigSpec.BooleanValue stationAutoFeed;
		public final ModConfigSpec.IntValue stationMaxPerInterval;

		private SophisticatedValues(ModConfigSpec.Builder builder) {
			builder.push("sophisticated");
			builder.push("mana_pile");
			pileBaseCapacity = builder.defineInRange("base_capacity", DEFAULT_PILE_BASE_CAPACITY, 1, Integer.MAX_VALUE);
			pileMigrateDefaults = builder.define("migrate_defaults", true);
			pileCapacityMigrated = builder.define("capacity_migrated", false);
			pileStackRatio = builder.defineInRange("stack_upgrade_ratio", 0.5, 0.0, 1000.0);
			pileMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			pileAbsorbFromItems = builder.define("absorb_from_items", true);
			pileAbsorbInterval = builder.defineInRange("absorb_interval_ticks", 20, 1, 1200);
			pileAbsorbAmount = builder.defineInRange("absorb_amount_per_interval", 10000, 1, Integer.MAX_VALUE);
			pileAbsorbFromSlot = builder.define("absorb_from_slot", true);
			pilePullFromPlayer = builder.define("pull_from_player_items", true);
			pileReceiveFromWorld = builder.define("receive_from_world", true);
			pileChargeItems = builder.define("charge_items", true);
			pileChargeInterval = builder.defineInRange("charge_interval_ticks", 20, 1, 1200);
			pileChargeRate = builder.defineInRange("charge_rate", 10000, 1, Integer.MAX_VALUE);
			pileInfusionInterval = builder.defineInRange("infusion_interval_ticks", 20, 1, 1200);
			pileInfusionMax = builder.defineInRange("infusion_max_per_interval", 4, 1, 64);
			pileInfusionAutoFeed = builder.define("infusion_auto_feed", true);
			pileInfusionAutoOutput = builder.define("infusion_auto_output", true);
			builder.pop();
			builder.push("mana_link");
			linkEnabled = builder.define("enabled", true);
			linkMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			builder.pop();
			builder.push("backpacks");
			backpacksEnabled = builder.define("enabled", true);
			for (BotaniaBackpackTier tier : BotaniaBackpackTier.values()) {
				builder.push(tier.material());
				backpackSlots.put(tier.material(), builder.defineInRange("inventory_slots", tier.defaultSlots(), 1, MAX_INVENTORY_SLOTS));
				backpackUpgradeSlots.put(tier.material(), builder.defineInRange("upgrade_slots", tier.defaultUpgradeSlots(), 0, 10));
				builder.pop();
			}
			builder.pop();
			builder.push("storage");
			storageEnabled = builder.define("enabled", true);
			for (BotaniaStorageTier tier : BotaniaStorageTier.values()) {
				builder.push(tier.id());
				storageSlots.put(tier, builder.defineInRange("inventory_slots", tier.defaultInventorySlots(), 1, MAX_INVENTORY_SLOTS));
				storageUpgradeSlots.put(tier, builder.defineInRange("upgrade_slots", tier.defaultUpgradeSlots(), 0, 10));
				builder.pop();
			}
			builder.pop();
			builder.push("runic_altar");
			altarEnabled = builder.define("enabled", true);
			altarMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			altarInterval = builder.defineInRange("interval_ticks", 20, 1, 1200);
			altarCostPercent = builder.defineInRange("cost_percent", 100, 0, 10000);
			altarRequireWand = builder.define("require_wand", true);
			builder.pop();
			builder.push("elven_portal");
			portalEnabled = builder.define("enabled", true);
			portalMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			portalInterval = builder.defineInRange("interval_ticks", 20, 1, 1200);
			portalCostPerTrade = builder.defineInRange("cost_per_trade", 500, 0, 1000000);
			portalRequirePylons = builder.define("require_pylons", true);
			builder.pop();
			builder.push("brewery");
			breweryEnabled = builder.define("enabled", true);
			breweryMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			breweryInterval = builder.defineInRange("interval_ticks", 20, 1, 1200);
			breweryCostPercent = builder.defineInRange("cost_percent", 100, 0, 10000);
			builder.pop();
			builder.push("petal_apothecary");
			apothecaryEnabled = builder.define("enabled", true);
			apothecaryMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			apothecaryInterval = builder.defineInRange("interval_ticks", 20, 1, 1200);
			apothecaryAutoFeed = builder.define("auto_feed", true);
			apothecaryConsumeWater = builder.define("consume_water", true);
			builder.pop();
			builder.push("terra_plate");
			terraPlateEnabled = builder.define("enabled", true);
			terraPlateMaxPerStorage = builder.defineInRange("max_per_storage", 1, 1, 64);
			terraPlateInterval = builder.defineInRange("interval_ticks", 20, 1, 1200);
			terraPlateCostPercent = builder.defineInRange("cost_percent", 100, 0, 10000);
			terraPlateAutoFeed = builder.define("auto_feed", true);
			terraPlateRequireSpark = builder.define("require_spark", true);
			builder.pop();
			builder.push("stations");
			stationAutoFeed = builder.define("auto_feed", true);
			stationMaxPerInterval = builder.defineInRange("max_per_interval", 1, 1, 64);
			builder.pop(2);
		}
	}

	public static final class ManaMotorValues {
		public final ModConfigSpec.BooleanValue enabled;
		public final ModConfigSpec.IntValue manaCapacity;
		public final ModConfigSpec.IntValue defaultRpm;
		public final ModConfigSpec.IntValue minRpm;
		public final ModConfigSpec.IntValue maxRpm;
		public final ModConfigSpec.DoubleValue stressPerRpm;
		public final ModConfigSpec.DoubleValue stressUnit;
		public final ModConfigSpec.DoubleValue manaPerStressUnit;
		public final ModConfigSpec.DoubleValue baseManaPerTick;

		private ManaMotorValues(ModConfigSpec.Builder builder) {
			builder.push("create");
			builder.push("mana_motor");
			enabled = builder
					.comment("Enables the Mana Motor. When false it accepts no mana and generates no rotation.")
					.define("enabled", true);
			manaCapacity = builder
					.comment("Internal mana buffer of the Mana Motor.")
					.defineInRange("mana_capacity", 100000, 1, Integer.MAX_VALUE);
			defaultRpm = builder
					.comment("RPM generated by a newly placed Mana Motor (adjustable in game with the value panel, like the Creative Motor).")
					.defineInRange("default_rpm", 32, 1, 256);
			minRpm = builder
					.comment("Minimum RPM the Mana Motor can be set to.")
					.defineInRange("min_rpm", 1, 1, 256);
			maxRpm = builder
					.comment("Maximum RPM the Mana Motor can be set to.")
					.defineInRange("max_rpm", 256, 1, 256);
			stressPerRpm = builder
					.comment("Stress capacity produced per RPM (su/RPM). Total stress produced = stress_per_rpm * RPM.")
					.defineInRange("stress_per_rpm", 64.0, 0.0, 1000000.0);
			stressUnit = builder
					.comment("Amount of stress (su) actually consumed by the network that counts as one stress unit for the mana cost.")
					.defineInRange("stress_unit", 256.0, 0.001, 1000000.0);
			manaPerStressUnit = builder
					.comment("Mana consumed per tick for each stress_unit of stress actually consumed by the network.")
					.defineInRange("mana_per_stress_unit", 1.0, 0.0, 1000000.0);
			baseManaPerTick = builder
					.comment("Flat mana consumed per tick while the motor is running, regardless of the stress consumed.")
					.defineInRange("base_mana_per_tick", 0.0, 0.0, 1000000.0);
			builder.pop(2);
		}
	}

	public static final class ThaumaturgeNodeBoostValues {
		public final ModConfigSpec.BooleanValue enabled;
		public final ModConfigSpec.DoubleValue speedMultiplier;
		public final ModConfigSpec.IntValue range;
		public final ModConfigSpec.BooleanValue affectGeneratingFlowers;
		public final ModConfigSpec.BooleanValue affectFunctionalFlowers;
		public final ModConfigSpec.BooleanValue affectSpreaders;
		public final ModConfigSpec.BooleanValue affectBrewery;
		public final ModConfigSpec.BooleanValue affectElvenPortal;
		public final ModConfigSpec.BooleanValue affectRunicAltar;
		public final ModConfigSpec.BooleanValue affectManaEnchanter;
		public final ModConfigSpec.BooleanValue affectSparks;
		public final ModConfigSpec.IntValue maxExtraTicksPerTick;

		private ThaumaturgeNodeBoostValues(ModConfigSpec.Builder builder) {
			builder.push("thaumaturge");
			builder.push("node_boost");
			enabled = builder
					.comment("Only used when Thaumaturge is installed. An aura node near a generating flower, a functional flower or a mana spreader makes it work faster.")
					.define("enabled", true);
			speedMultiplier = builder
					.comment("Speed multiplier applied while an aura node is within range. Flowers run their whole logic this many times per tick (fuel, items and mana costs follow the same pace). Spreaders carry this many times more mana per burst and hold this many times more mana.")
					.defineInRange("speed_multiplier", 20.0, 1.0, 1000.0);
			range = builder
					.comment("Maximum distance in blocks between the aura node and the flower or spreader.")
					.defineInRange("range", 8, 1, 64);
			affectGeneratingFlowers = builder
					.comment("Whether aura nodes speed up generating flowers.")
					.define("affect_generating_flowers", true);
			affectFunctionalFlowers = builder
					.comment("Whether aura nodes speed up functional flowers.")
					.define("affect_functional_flowers", true);
			affectSpreaders = builder
					.comment("Whether aura nodes speed up mana spreaders.")
					.define("affect_spreaders", true);
			affectBrewery = builder
					.comment("Whether aura nodes speed up the Botanical Brewery.")
					.define("affect_brewery", true);
			affectElvenPortal = builder
					.comment("Whether aura nodes speed up the Alfheim Portal trades.")
					.define("affect_elven_portal", true);
			affectRunicAltar = builder
					.comment("Whether aura nodes speed up the Runic Altar.")
					.define("affect_runic_altar", true);
			affectManaEnchanter = builder
					.comment("Whether aura nodes speed up the Mana Enchanter.")
					.define("affect_mana_enchanter", true);
			affectSparks = builder
					.comment("Whether aura nodes speed up the mana transfer of Mana Sparks.")
					.define("affect_sparks", true);
			maxExtraTicksPerTick = builder
					.comment("Performance cap: maximum number of extra updates run per game tick for a single flower, brewery, portal, altar, enchanter or spark, whatever the multiplier is. 0 disables the extra updates.")
					.defineInRange("max_extra_ticks_per_tick", 20, 0, 1000);
			builder.pop(2);
		}
	}

	private static final Common COMMON;
	private static final ModConfigSpec COMMON_SPEC;
	static {
		final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = specPair.getRight();
		COMMON = specPair.getLeft();
	}

	public static <T> T get(ModConfigSpec.ConfigValue<T> value) {
		return safe(value);
	}

	public static ArsNouveauValues arsNouveau() {
		return COMMON.arsNouveau;
	}

	public static SophisticatedValues sophisticated() {
		return COMMON.sophisticated;
	}

	public static ManaMotorValues manaMotor() {
		return COMMON.manaMotor;
	}

	public static ThaumaturgeNodeBoostValues thaumaturgeNodeBoost() {
		return COMMON.thaumaturgeNodeBoost;
	}

	public static boolean isCommonSpec(ModConfig config) {
		return config.getSpec() == COMMON_SPEC;
	}

	public static void saveCommon() {
		COMMON_SPEC.save();
	}

	public static void setup(ModContainer container) {
		container.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
		BotaniaConfig.setCommon(COMMON);

		if (XplatAbstractions.INSTANCE.isPhysicalClient()) {
			container.registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC);
			BotaniaConfig.setClient(CLIENT);
		}

		var bus = container.getEventBus();
		if (bus != null) {
			bus.addListener((ModConfigEvent.Loading e) -> onConfigLoad(e));
			bus.addListener((ModConfigEvent.Reloading e) -> onConfigLoad(e));
		}
	}

	public static void onConfigLoad(ModConfigEvent.Loading evt) {
		var config = evt.getConfig();
		if (config.getType() == ModConfig.Type.COMMON && config.getModId().equals(LibMisc.MOD_ID)) {
			BotaniaConfig.resetPatchouliFlags();
		}
	}

	public static void onConfigLoad(ModConfigEvent.Reloading evt) {
		var config = evt.getConfig();
		if (config.getType() == ModConfig.Type.COMMON && config.getModId().equals(LibMisc.MOD_ID)) {
			BotaniaConfig.resetPatchouliFlags();
		}
	}
}
