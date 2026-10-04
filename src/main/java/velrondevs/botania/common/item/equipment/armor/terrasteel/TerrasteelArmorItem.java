package velrondevs.botania.common.item.equipment.armor.terrasteel;

import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class TerrasteelArmorItem extends ManasteelArmorItem {

	public TerrasteelArmorItem(Type type, Properties props) {
		super(type, BotaniaAPI.instance().getTerrasteelArmorMaterial(), props);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ResourcesLib.MODEL_TERRASTEEL_NEW;
	}

	@NotNull
	@Override
	public ItemAttributeModifiers getDefaultAttributeModifiers(@NotNull ItemStack stack) {
		EquipmentSlot slot = getType().getSlot();
		int reduction = getMaterial().value().getDefense(getType());
		return super.getDefaultAttributeModifiers(stack).withModifierAdded(Attributes.KNOCKBACK_RESISTANCE,
				new AttributeModifier(prefix("terrasteel_armor_knockback_resistance/" + getType().getName()), (double) reduction / 20, AttributeModifier.Operation.ADD_VALUE),
				EquipmentSlotGroup.bySlot(slot));
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(BotaniaItems.terrasteelHelm),
			new ItemStack(BotaniaItems.terrasteelChest),
			new ItemStack(BotaniaItems.terrasteelLegs),
			new ItemStack(BotaniaItems.terrasteelBoots)
	});

	@Override
	public ItemStack[] getArmorSetStacks() {
		return armorSet.get();
	}

	@Override
	public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
		if (player == null) {
			return false;
		}

		ItemStack stack = player.getItemBySlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		return switch (slot) {
			case HEAD -> stack.is(BotaniaItems.terrasteelHelm);
			case CHEST -> stack.is(BotaniaItems.terrasteelChest);
			case LEGS -> stack.is(BotaniaItems.terrasteelLegs);
			case FEET -> stack.is(BotaniaItems.terrasteelBoots);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return Component.translatable("botania.armorset.terrasteel.name");
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, List<Component> list) {
		list.add(Component.translatable("botania.armorset.terrasteel.desc0").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botania.armorset.terrasteel.desc1").withStyle(ChatFormatting.GRAY));
		list.add(Component.translatable("botania.armorset.terrasteel.desc2").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
		return true;
	}
}
