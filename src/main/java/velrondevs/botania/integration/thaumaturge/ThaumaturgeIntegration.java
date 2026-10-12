package velrondevs.botania.integration.thaumaturge;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.registry.BotaniaCreativeTabs;
import velrondevs.botania.registry.BotaniaItems;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class ThaumaturgeIntegration {
	public static final String MOD_ID = "thaumaturge";
	public static final String MANASTEEL_HELM_REVEALING = "manasteel_helmet_revealing";
	public static final String ELEMENTIUM_HELM_REVEALING = "elementium_helmet_revealing";
	public static final String TERRASTEEL_HELM_REVEALING = "terrasteel_helmet_revealing";
	public static final int VIS_DISCOUNT = 5;
	public static final String ASPECT_PACK_PATH = "resourcepacks/thaumaturge_aspects";

	private static final ResourceLocation GOGGLES_UPGRADE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "goggles_upgrade");
	private static final ResourceLocation VIS_DISCOUNT_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "vis_discount");

	private ThaumaturgeIntegration() {}

	static ItemAttributeModifiers withVisDiscount(Item item, ItemAttributeModifiers base) {
		return BuiltInRegistries.ATTRIBUTE.getHolder(VIS_DISCOUNT_ATTRIBUTE)
				.map(attribute -> base.withModifierAdded(attribute,
						new AttributeModifier(BuiltInRegistries.ITEM.getKey(item), VIS_DISCOUNT / 100.0, AttributeModifier.Operation.ADD_VALUE),
						EquipmentSlotGroup.HEAD))
				.orElse(base);
	}

	public static void init(IEventBus modBus) {
		BotaniaCreativeTabs.bindForItems(modBus, ThaumaturgeIntegration::registerItems);
		modBus.addListener(ThaumaturgeIntegration::registerRecipeSerializers);
		modBus.addListener(ThaumaturgeIntegration::addAspectPack);
	}

	@SuppressWarnings("unchecked")
	private static void registerItems(BiConsumer<Item, ResourceLocation> consumer) {
		DataComponentType<?> type = BuiltInRegistries.DATA_COMPONENT_TYPE.get(GOGGLES_UPGRADE);
		if (type == null) {
			BotaniaAPI.LOGGER.warn("Thaumaturge is loaded but {} is not registered, the Helmets of Revealing are disabled", GOGGLES_UPGRADE);
			return;
		}
		DataComponentType<Unit> gogglesUpgrade = (DataComponentType<Unit>) type;
		register(consumer, MANASTEEL_HELM_REVEALING, new ManasteelHelmRevealingItem(
				BotaniaItems.unstackableCustomDamage().component(gogglesUpgrade, Unit.INSTANCE)));
		register(consumer, ELEMENTIUM_HELM_REVEALING, new ElementiumHelmRevealingItem(
				BotaniaItems.unstackableCustomDamage().component(gogglesUpgrade, Unit.INSTANCE)));
		register(consumer, TERRASTEEL_HELM_REVEALING, new TerrasteelHelmRevealingItem(
				BotaniaItems.unstackableCustomDamage().component(gogglesUpgrade, Unit.INSTANCE).fireResistant().rarity(Rarity.UNCOMMON)));
	}

	private static void register(BiConsumer<Item, ResourceLocation> consumer, String name, Item item) {
		BotaniaModules.trackExternal(item);
		consumer.accept(item, prefix(name));
	}

	private static void addAspectPack(AddPackFindersEvent event) {
		if (event.getPackType() != PackType.SERVER_DATA) {
			return;
		}
		Path path = ModList.get().getModContainerById("botania").get().getModInfo().getOwningFile().getFile().findResource(ASPECT_PACK_PATH.split("/"));
		PackLocationInfo info = new PackLocationInfo("mod/botania_thaumaturge_aspects", Component.literal("Botania aspects for Thaumaturge"), PackSource.BUILT_IN, Optional.empty());
		Pack pack = Pack.readMetaAndCreate(info, new PathPackResources.PathResourcesSupplier(path), PackType.SERVER_DATA, new PackSelectionConfig(true, Pack.Position.TOP, false));
		if (pack != null) {
			event.addRepositorySource(consumer -> consumer.accept(pack));
		}
	}

	private static void registerRecipeSerializers(RegisterEvent event) {
		if (event.getRegistryKey().equals(Registries.RECIPE_SERIALIZER)) {
			event.register(Registries.RECIPE_SERIALIZER, prefix("helm_revealing"), () -> HelmRevealingRecipe.SERIALIZER);
		}
	}
}
