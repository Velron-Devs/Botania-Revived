package velrondevs.botania.module;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.lib.LibMisc;

import java.util.concurrent.CompletableFuture;

public abstract class BotaniaModule {
	private final String id;
	private final String displayName;

	protected BotaniaModule(String id, String displayName) {
		this.id = id;
		this.displayName = displayName;
	}

	public final String id() {
		return id;
	}

	public final String displayName() {
		return displayName;
	}

	public boolean enabledByDefault() {
		return true;
	}

	public final boolean isEnabled() {
		return BotaniaModules.isEnabled(id);
	}

	public final ICondition enabledCondition() {
		return new ModuleEnabledCondition(id);
	}

	public final String patchouliFlag() {
		return LibMisc.MOD_ID + ":" + id;
	}

	public abstract void register(ModuleContext ctx);

	public void registerClient(ModuleContext ctx) {}

	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {}

	public void addBlockTags(ModuleTagSink<Block> sink) {}

	public void addItemTags(ModuleTagSink<Item> sink) {}

	public void addAccessoryTags(ModuleTagSink<Item> sink) {}
}
