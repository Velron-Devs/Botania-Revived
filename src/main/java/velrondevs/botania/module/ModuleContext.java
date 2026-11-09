package velrondevs.botania.module;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.registry.BotaniaCreativeSections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class ModuleContext {
	private final BotaniaModule module;
	private final IEventBus modBus;
	private final List<Item> items = new ArrayList<>();

	ModuleContext(BotaniaModule module, IEventBus modBus) {
		this.module = module;
		this.modBus = modBus;
	}

	public BotaniaModule module() {
		return module;
	}

	public IEventBus modBus() {
		return modBus;
	}

	public List<Item> items() {
		return Collections.unmodifiableList(items);
	}

	public <T> void bind(ResourceKey<? extends Registry<T>> registry, Consumer<BiConsumer<T, Identifier>> source) {
		modBus.addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> {
					BotaniaModules.track(module, t);
					event.register(registry, rl, () -> t);
				});
			}
		});
	}

	public void bindItems(Consumer<BiConsumer<Item, Identifier>> source) {
		bind(Registries.ITEM, consumer -> source.accept((item, rl) -> {
			items.add(item);
			consumer.accept(item, rl);
		}));
	}

	public void bindBlocks(Consumer<BiConsumer<Block, Identifier>> source) {
		bind(Registries.BLOCK, source);
	}

	public BotaniaCreativeSections.Section creativeSection(String id, Supplier<ItemStack> icon) {
		return creativeSection(id, icon, null);
	}

	public BotaniaCreativeSections.Section creativeSection(String id, Supplier<ItemStack> icon, Consumer<CreativeModeTab.Output> extra) {
		return BotaniaCreativeSections.registerModuleSection(module, id, icon, items, extra);
	}
}
