package velrondevs.botania.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.registries.DeferredHolder;

import velrondevs.botania.common.lib.BotaniaTags;

import java.util.EnumMap;
import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaArmorMaterials {
	public static final int MANASTEEL_DURABILITY_MULTIPLIER = 16;
	public static final int MANAWEAVE_DURABILITY_MULTIPLIER = 5;
	public static final int ELEMENTIUM_DURABILITY_MULTIPLIER = 18;
	public static final int TERRASTEEL_DURABILITY_MULTIPLIER = 34;

	public static final ArmorMaterial MANASTEEL = make("manasteel", MANASTEEL_DURABILITY_MULTIPLIER, 2, 5, 6, 2, 18, "equip_manasteel", BotaniaTags.Items.INGOTS_MANASTEEL, 0);
	public static final ArmorMaterial MANAWEAVE = make("manaweave", MANAWEAVE_DURABILITY_MULTIPLIER, 1, 2, 3, 1, 18, "equip_manaweave", BotaniaTags.Items.MANAWEAVE_CLOTH, 0);
	public static final ArmorMaterial ELEMENTIUM = make("elementium", ELEMENTIUM_DURABILITY_MULTIPLIER, 2, 5, 6, 2, 18, "equip_elementium", BotaniaTags.Items.INGOTS_ELEMENTIUM, 0);
	public static final ArmorMaterial TERRASTEEL = make("terrasteel", TERRASTEEL_DURABILITY_MULTIPLIER, 3, 6, 8, 3, 26, "equip_terrasteel", BotaniaTags.Items.INGOTS_TERRASTEEL, 3);

	public static final ToolMaterial MANASTEEL_TIER = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 300, 6.2F, 2, 20, BotaniaTags.Items.INGOTS_MANASTEEL);
	public static final ToolMaterial ELEMENTIUM_TIER = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 720, 6.2F, 2, 20, BotaniaTags.Items.INGOTS_ELEMENTIUM);
	public static final ToolMaterial TERRASTEEL_TIER = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2300, 9, 4, 26, BotaniaTags.Items.INGOTS_TERRASTEEL);

	public static int durabilityMultiplier(ArmorMaterial material) {
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

	public static Item.Properties withArmorDurability(Item.Properties props, ArmorType type, ArmorMaterial material) {
		return props.humanoidArmor(material, type);
	}

	public static ResourceKey<EquipmentAsset> assetKey(String name) {
		return ResourceKey.create(EquipmentAssets.ROOT_ID, prefix(name));
	}

	private static ArmorMaterial make(String name, int durability, int boots, int leggings, int chestplate, int helmet,
			int enchantability, String equipSound, TagKey<Item> repair, float toughness) {
		Map<ArmorType, Integer> defense = new EnumMap<>(ArmorType.class);
		defense.put(ArmorType.BOOTS, boots);
		defense.put(ArmorType.LEGGINGS, leggings);
		defense.put(ArmorType.CHESTPLATE, chestplate);
		defense.put(ArmorType.HELMET, helmet);
		defense.put(ArmorType.BODY, chestplate);
		Holder<SoundEvent> sound = DeferredHolder.create(Registries.SOUND_EVENT, prefix(equipSound));
		return new ArmorMaterial(durability, defense, enchantability, sound, toughness, 0, repair, assetKey(name));
	}

	private BotaniaArmorMaterials() {}
}
