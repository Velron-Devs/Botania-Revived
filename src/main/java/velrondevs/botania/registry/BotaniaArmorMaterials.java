package velrondevs.botania.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.registries.DeferredHolder;


import java.util.EnumMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaArmorMaterials {
	public static final int MANASTEEL_DURABILITY_MULTIPLIER = 16;
	public static final int MANAWEAVE_DURABILITY_MULTIPLIER = 5;
	public static final int ELEMENTIUM_DURABILITY_MULTIPLIER = 18;
	public static final int TERRASTEEL_DURABILITY_MULTIPLIER = 34;

	public static final Holder<ArmorMaterial> MANASTEEL = DeferredHolder.create(Registries.ARMOR_MATERIAL, prefix("manasteel"));
	public static final Holder<ArmorMaterial> MANAWEAVE = DeferredHolder.create(Registries.ARMOR_MATERIAL, prefix("manaweave"));
	public static final Holder<ArmorMaterial> ELEMENTIUM = DeferredHolder.create(Registries.ARMOR_MATERIAL, prefix("elementium"));
	public static final Holder<ArmorMaterial> TERRASTEEL = DeferredHolder.create(Registries.ARMOR_MATERIAL, prefix("terrasteel"));

	public static final Tier MANASTEEL_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 300, 6.2F, 2, 20,
			() -> Ingredient.of(BotaniaItems.manaSteel));
	public static final Tier ELEMENTIUM_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 720, 6.2F, 2, 20,
			() -> Ingredient.of(BotaniaItems.elementium));
	public static final Tier TERRASTEEL_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2300, 9, 4, 26,
			() -> Ingredient.of(BotaniaItems.terrasteel));

	public static int durabilityMultiplier(Holder<ArmorMaterial> material) {
		if (material == MANASTEEL) {
			return MANASTEEL_DURABILITY_MULTIPLIER;
		} else if (material == MANAWEAVE) {
			return MANAWEAVE_DURABILITY_MULTIPLIER;
		} else if (material == ELEMENTIUM) {
			return ELEMENTIUM_DURABILITY_MULTIPLIER;
		} else if (material == TERRASTEEL) {
			return TERRASTEEL_DURABILITY_MULTIPLIER;
		}
		return 0;
	}

	public static Item.Properties withArmorDurability(Item.Properties props, ArmorItem.Type type, Holder<ArmorMaterial> material) {
		return props.durability(type.getDurability(durabilityMultiplier(material)));
	}

	public static void registerArmorMaterials(BiConsumer<ArmorMaterial, ResourceLocation> r) {
		r.accept(make("manasteel", 2, 5, 6, 2, 18, BotaniaSounds.equipManasteel, () -> BotaniaItems.manaSteel, 0), prefix("manasteel"));
		r.accept(make("manaweave", 1, 2, 3, 1, 18, BotaniaSounds.equipManaweave, () -> BotaniaItems.manaweaveCloth, 0), prefix("manaweave"));
		r.accept(make("elementium", 2, 5, 6, 2, 18, BotaniaSounds.equipElementium, () -> BotaniaItems.elementium, 0), prefix("elementium"));
		r.accept(make("terrasteel", 3, 6, 8, 3, 26, BotaniaSounds.equipTerrasteel, () -> BotaniaItems.terrasteel, 3), prefix("terrasteel"));
	}

	private static ArmorMaterial make(String name, int boots, int leggings, int chestplate, int helmet,
			int enchantability, SoundEvent equipSound, Supplier<Item> repairItem, float toughness) {
		var defense = new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class);
		defense.put(ArmorItem.Type.BOOTS, boots);
		defense.put(ArmorItem.Type.LEGGINGS, leggings);
		defense.put(ArmorItem.Type.CHESTPLATE, chestplate);
		defense.put(ArmorItem.Type.HELMET, helmet);
		defense.put(ArmorItem.Type.BODY, chestplate);
		return new ArmorMaterial(defense, enchantability, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(equipSound),
				() -> Ingredient.of(repairItem.get()), List.of(new ArmorMaterial.Layer(prefix(name))), toughness, 0);
	}

	private BotaniaArmorMaterials() {}
}
