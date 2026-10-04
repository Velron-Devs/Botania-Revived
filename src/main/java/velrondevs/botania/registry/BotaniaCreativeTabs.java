package velrondevs.botania.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.api.BotaniaRegistries;
import velrondevs.botania.common.item.CustomCreativeTabContents;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class BotaniaCreativeTabs {
	private static final Set<Item> ITEMS_TO_ADD = new LinkedHashSet<>();

	private BotaniaCreativeTabs() {}

	public static void bindForItems(IEventBus modBus, Consumer<BiConsumer<Item, ResourceLocation>> source) {
		modBus.addListener((RegisterEvent event) -> {
			if (event.getRegistryKey().equals(Registries.ITEM)) {
				source.accept((t, rl) -> {
					ITEMS_TO_ADD.add(t);
					event.register(Registries.ITEM, rl, () -> t);
				});
			}
		});
	}

	public static void registerTabs(BiConsumer<CreativeModeTab, ResourceLocation> consumer) {
		consumer.accept(CreativeModeTab.builder()
				.title(Component.translatable("itemGroup.botania").withStyle(style -> style.withColor(ChatFormatting.WHITE)))
				.icon(() -> new ItemStack(BotaniaItems.lexicon))
				.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
				.backgroundTexture(ResourceLocation.withDefaultNamespace("textures/gui/container/creative_inventory/tab_botania.png"))
				.withSearchBar()
				.build(),
				BotaniaRegistries.BOTANIA_TAB_KEY.location());
	}

	public static void buildContents(BuildCreativeModeTabContentsEvent e) {
		if (e.getTabKey() == BotaniaRegistries.BOTANIA_TAB_KEY) {
			for (Item item : ITEMS_TO_ADD) {
				if (item instanceof CustomCreativeTabContents cc) {
					cc.addToCreativeTab(item, e);
				} else if (item instanceof BlockItem bi && bi.getBlock() instanceof CustomCreativeTabContents cc) {
					cc.addToCreativeTab(item, e);
				} else {
					e.accept(item);
				}
			}
		}
	}
}
