package velrondevs.botania.module;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.api.BotaniaRegistries;
import velrondevs.botania.common.item.CustomCreativeTabContents;
import velrondevs.botania.common.lib.LibMisc;

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

	public <T> void bind(ResourceKey<? extends Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		modBus.addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> {
					BotaniaModules.track(module, t);
					event.register(registry, rl, () -> t);
				});
			}
		});
	}

	public void bindItems(Consumer<BiConsumer<Item, ResourceLocation>> source) {
		bind(Registries.ITEM, consumer -> source.accept((item, rl) -> {
			items.add(item);
			consumer.accept(item, rl);
		}));
	}

	public void bindBlocks(Consumer<BiConsumer<Block, ResourceLocation>> source) {
		bind(Registries.BLOCK, source);
	}

	public ResourceKey<CreativeModeTab> creativeTab(Supplier<ItemStack> icon) {
		return creativeTab(icon, true, extra -> {});
	}

	public ResourceKey<CreativeModeTab> creativeTab(Supplier<ItemStack> icon, boolean searchBar, Consumer<Consumer<ItemStack>> extraStacks) {
		ResourceKey<CreativeModeTab> key = ResourceKey.create(Registries.CREATIVE_MODE_TAB,
				ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, module.id()));
		bind(Registries.CREATIVE_MODE_TAB, consumer -> {
			CreativeModeTab.Builder builder = CreativeModeTab.builder()
					.title(Component.translatable("itemGroup." + LibMisc.MOD_ID + "." + module.id()).withStyle(style -> style.withColor(ChatFormatting.WHITE)))
					.icon(icon)
					.withTabsBefore(BotaniaRegistries.BOTANIA_TAB_KEY);
			if (searchBar) {
				builder.backgroundTexture(ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tab_botania.png")).withSearchBar();
			}
			consumer.accept(builder.build(), key.location());
		});
		modBus.addListener((BuildCreativeModeTabContentsEvent e) -> {
			if (e.getTabKey() == key) {
				for (Item item : items) {
					if (item instanceof CustomCreativeTabContents cc) {
						cc.addToCreativeTab(item, e);
					} else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
						cc.addToCreativeTab(item, e);
					} else {
						e.accept(item);
					}
				}
				extraStacks.accept(e::accept);
			}
		});
		return key;
	}
}
