package velrondevs.botania.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.api.BotaniaRegistries;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class BotaniaCreativeTabs {
	private BotaniaCreativeTabs() {}

	public static void bindForItems(IEventBus modBus, Consumer<BiConsumer<Item, Identifier>> source) {
		modBus.addListener((RegisterEvent event) -> {
			if (event.getRegistryKey().equals(Registries.ITEM)) {
				source.accept((t, rl) -> {
					BotaniaCreativeSections.addBaseItem(t);
					event.register(Registries.ITEM, rl, () -> t);
				});
			}
		});
	}

	public static void registerTabs(BiConsumer<CreativeModeTab, Identifier> consumer) {
		consumer.accept(CreativeModeTab.builder()
				.title(Component.translatable("itemGroup.botania").withStyle(style -> style.withColor(ChatFormatting.WHITE)))
				.icon(() -> new ItemStack(BotaniaItems.lexicon))
				.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
				.backgroundTexture(Identifier.withDefaultNamespace("textures/gui/container/creative_inventory/tab_botania.png"))
				.withSearchBar()
				.displayItems((params, output) -> {
					if (net.neoforged.fml.ModList.get().isLoaded("fractal")) {
						velrondevs.botania.compat.fractal.FractalSubTabs.fillParent(params, output);
					} else {
						BotaniaCreativeSections.activeSections().forEach(section -> section.fill(output));
					}
				})
				.build(),
				BotaniaRegistries.BOTANIA_TAB_KEY.identifier());
	}
}
