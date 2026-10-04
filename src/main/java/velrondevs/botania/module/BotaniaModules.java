package velrondevs.botania.module;

import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.toml.TomlParser;

import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.asgard.AsgardModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.magicskies.MagicSkiesModule;
import velrondevs.botania.module.creativecrafting.CreativeCraftingModule;
import velrondevs.botania.module.exploration.ExplorationModule;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public final class BotaniaModules {
	public static final String CONFIG_SECTION = "modules";
	public static final ResourceLocation MODULE_ENABLED_CONDITION = ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "module_enabled");

	private static final List<BotaniaModule> ALL = List.of(
			BotaniaExtrasModule.INSTANCE,
			MagicSkiesModule.INSTANCE,
			ExplorationModule.INSTANCE,
			CreativeCraftingModule.INSTANCE,
			AsgardModule.INSTANCE
	);

	private static final Map<String, Boolean> STATE = new HashMap<>();
	private static final Map<Object, BotaniaModule> CONTENT = Collections.synchronizedMap(new IdentityHashMap<>());
	private static final Map<Object, Boolean> EXTERNAL = Collections.synchronizedMap(new IdentityHashMap<>());
	private static boolean loaded = false;

	private BotaniaModules() {}

	public static List<BotaniaModule> all() {
		return ALL;
	}

	public static Optional<BotaniaModule> byId(String id) {
		return ALL.stream().filter(m -> m.id().equals(id)).findFirst();
	}

	public static boolean isEnabled(String id) {
		load();
		Boolean state = STATE.get(id);
		return state != null && state;
	}

	public static boolean isModuleContent(Object object) {
		return CONTENT.containsKey(object) || EXTERNAL.containsKey(object);
	}

	public static void trackExternal(Object object) {
		EXTERNAL.put(object, Boolean.TRUE);
	}

	public static boolean isModuleBlock(Block block) {
		return isModuleContent(block);
	}

	public static boolean isModuleItem(Item item) {
		return isModuleContent(item);
	}

	public static Optional<BotaniaModule> ownerOf(Object object) {
		return Optional.ofNullable(CONTENT.get(object));
	}

	static void track(BotaniaModule module, Object object) {
		CONTENT.put(object, module);
	}

	public static void init(IEventBus modBus) {
		load();
		modBus.addListener((RegisterEvent event) -> {
			if (event.getRegistryKey().equals(NeoForgeRegistries.Keys.CONDITION_CODECS)) {
				event.register(NeoForgeRegistries.Keys.CONDITION_CODECS, MODULE_ENABLED_CONDITION, () -> ModuleEnabledCondition.CODEC);
			}
		});
		for (BotaniaModule module : ALL) {
			if (!module.isEnabled()) {
				BotaniaAPI.LOGGER.info("Botania module {} is disabled", module.id());
				continue;
			}
			ModuleContext ctx = new ModuleContext(module, modBus);
			module.register(ctx);
			if (FMLEnvironment.dist.isClient()) {
				module.registerClient(ctx);
			}
		}
	}

	public static void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		for (BotaniaModule module : ALL) {
			if (module.isEnabled()) {
				module.gatherData(evt, lookup);
			}
		}
	}

	public static void addBlockTags(ModuleTagSink<Block> sink) {
		for (BotaniaModule module : ALL) {
			if (module.isEnabled()) {
				module.addBlockTags(sink);
			}
		}
	}

	public static void addItemTags(ModuleTagSink<Item> sink) {
		for (BotaniaModule module : ALL) {
			if (module.isEnabled()) {
				module.addItemTags(sink);
			}
		}
	}

	public static void addAccessoryTags(ModuleTagSink<Item> sink) {
		for (BotaniaModule module : ALL) {
			if (module.isEnabled()) {
				module.addAccessoryTags(sink);
			}
		}
	}

	private static synchronized void load() {
		if (loaded) {
			return;
		}
		loaded = true;
		for (BotaniaModule module : ALL) {
			STATE.put(module.id(), module.enabledByDefault());
		}
		if (DatagenModLoader.isRunningDataGen()) {
			for (BotaniaModule module : ALL) {
				STATE.put(module.id(), true);
			}
			return;
		}
		Path file = FMLPaths.CONFIGDIR.get().resolve(LibMisc.MOD_ID + "-common.toml");
		if (!Files.isRegularFile(file)) {
			return;
		}
		try (Reader reader = Files.newBufferedReader(file)) {
			Config config = new TomlParser().parse(reader);
			for (BotaniaModule module : ALL) {
				Object value = config.get(CONFIG_SECTION + "." + module.id());
				if (value instanceof Boolean b) {
					STATE.put(module.id(), b);
				}
			}
		} catch (Exception e) {
			BotaniaAPI.LOGGER.warn("Could not read Botania module settings from {}, using defaults", file, e);
		}
	}
}
