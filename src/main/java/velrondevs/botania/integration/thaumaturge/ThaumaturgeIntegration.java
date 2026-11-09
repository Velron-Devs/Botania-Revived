package velrondevs.botania.integration.thaumaturge;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.resource.JarContentsPackResources;
import net.neoforged.neoforgespi.language.IModInfo;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.registry.BotaniaCreativeTabs;
import velrondevs.botania.registry.BotaniaItems;

import java.util.Optional;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class ThaumaturgeIntegration {
	public static final String MOD_ID = "thaumaturge";
	public static final String MANASTEEL_HELM_REVEALING = "manasteel_helmet_revealing";
	public static final String ELEMENTIUM_HELM_REVEALING = "elementium_helmet_revealing";
	public static final String TERRASTEEL_HELM_REVEALING = "terrasteel_helmet_revealing";
	public static final String MANA_INKWELL = "mana_inkwell";
	public static final int VIS_DISCOUNT = 5;
	public static final String ASPECT_PACK_PATH = "resourcepacks/thaumaturge_aspects";

	private static final Identifier GOGGLES_UPGRADE = Identifier.fromNamespaceAndPath(MOD_ID, "goggles_upgrade");

	@Nullable
	private static Item manaInkwell;

	private ThaumaturgeIntegration() {}

	public static void init(IEventBus modBus) {
		BotaniaCreativeTabs.bindForItems(modBus, ThaumaturgeIntegration::registerItems);
		modBus.addListener(ThaumaturgeIntegration::registerRecipeSerializers);
		modBus.addListener(ThaumaturgeIntegration::addAspectPack);
		modBus.addListener(ThaumaturgeIntegration::registerCapabilities);
	}

	@SuppressWarnings("unchecked")
	private static void registerItems(BiConsumer<Item, Identifier> consumer) {
		manaInkwell = new ManaInkwellItem(new Item.Properties().durability(ManaInkwellItem.MAX_INK)
				.component(DataComponents.DAMAGE, ManaInkwellItem.MAX_INK).rarity(Rarity.UNCOMMON));
		register(consumer, MANA_INKWELL, manaInkwell);

		Optional<DataComponentType<?>> type = BuiltInRegistries.DATA_COMPONENT_TYPE.getOptional(GOGGLES_UPGRADE);
		if (type.isEmpty()) {
			BotaniaAPI.LOGGER.warn("Thaumaturge is loaded but {} is not registered, the Helmets of Revealing are disabled", GOGGLES_UPGRADE);
			return;
		}
		DataComponentType<Unit> gogglesUpgrade = (DataComponentType<Unit>) type.get();
		register(consumer, MANASTEEL_HELM_REVEALING, new ManasteelHelmRevealingItem(
				BotaniaItems.unstackableCustomDamage().component(gogglesUpgrade, Unit.INSTANCE)));
		register(consumer, ELEMENTIUM_HELM_REVEALING, new ElementiumHelmRevealingItem(
				BotaniaItems.unstackableCustomDamage().component(gogglesUpgrade, Unit.INSTANCE)));
		register(consumer, TERRASTEEL_HELM_REVEALING, new TerrasteelHelmRevealingItem(
				BotaniaItems.unstackableCustomDamage().component(gogglesUpgrade, Unit.INSTANCE).fireResistant().rarity(Rarity.UNCOMMON)));
	}

	private static void register(BiConsumer<Item, Identifier> consumer, String name, Item item) {
		BotaniaModules.trackExternal(item);
		consumer.accept(item, prefix(name));
	}

	private static void addAspectPack(AddPackFindersEvent event) {
		if (event.getPackType() != PackType.SERVER_DATA) {
			return;
		}
		Optional<IModInfo> modInfo = ModList.get().getModContainerById(LibMisc.MOD_ID).map(container -> container.getModInfo());
		if (modInfo.isEmpty()) {
			return;
		}
		PackLocationInfo info = new PackLocationInfo("mod/botania_thaumaturge_aspects", Component.literal("Botania aspects for Thaumaturge"), PackSource.BUILT_IN, Optional.empty());
		Pack pack = Pack.readMetaAndCreate(info,
				new JarContentsPackResources.JarContentsResourcesSupplier(modInfo.get().getOwningFile().getFile().getContents(), ASPECT_PACK_PATH),
				PackType.SERVER_DATA, new PackSelectionConfig(true, Pack.Position.TOP, false));
		if (pack != null) {
			event.addRepositorySource(consumer -> consumer.accept(pack));
		}
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		if (manaInkwell != null) {
			event.registerItem(BotaniaCapabilities.MANA_ITEM, (stack, ctx) -> new ManaInkwellItem.ManaItemImpl(stack), manaInkwell);
		}
	}

	private static void registerRecipeSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
			event.register(Registries.RECIPE_SERIALIZER, prefix("helm_revealing"), () -> HelmRevealingRecipe.SERIALIZER);
		}
	}
}
