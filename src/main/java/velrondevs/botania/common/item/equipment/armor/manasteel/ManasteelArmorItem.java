package velrondevs.botania.common.item.equipment.armor.manasteel;

import net.minecraft.world.item.equipment.ArmorType;
import java.util.ArrayList;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import net.minecraft.server.level.ServerLevel;
import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.item.PhantomInkable;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.client.gui.TooltipHandler;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.equipment.CustomDamageItem;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.registry.BotaniaArmorMaterials;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ManasteelArmorItem extends Item implements CustomDamageItem, PhantomInkable {

	private static final String TAG_PHANTOM_INK = "phantomInk";

	public final ArmorType type;
	private final ArmorMaterial material;

	public ManasteelArmorItem(ArmorType type, Properties props) {
		this(type, BotaniaAPI.instance().getManasteelArmorMaterial(), props);
	}

	public ManasteelArmorItem(ArmorType type, ArmorMaterial mat, Properties props) {
		super(BotaniaArmorMaterials.withArmorDurability(props, type, mat));
		this.material = mat;
		this.type = type;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot eqSlot) {
		if (entity instanceof Player player) {
			if (!world.isClientSide() && stack.getDamageValue() > 0 && ManaItemHandler.instance().requestManaExact(stack, player, getManaPerDamage() * 2, true)) {
				stack.setDamageValue(stack.getDamageValue() - 1);
			}
		}
	}

	@Override
	public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<Item> onBroken) {
		return ToolCommons.damageItemIfPossible(stack, amount, entity, getManaPerDamage());
	}

	public EquipmentSlot getEquipmentSlot() {
		return type.getSlot();
	}

	public ArmorType getType() {
		return type;
	}

	public ArmorMaterial getMaterial() {
		return material;
	}

	protected int getManaPerDamage() {
		return 70;
	}

	@NotNull
	public final Identifier getArmorTexture(ItemStack stack, EquipmentSlot slot) {
		return Identifier.parse(hasPhantomInk(stack) ? ResourcesLib.MODEL_INVISIBLE_ARMOR : getArmorTextureAfterInk(stack, slot));
	}

	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ResourcesLib.MODEL_MANASTEEL_NEW;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext world, TooltipDisplay tooltipDisplay, Consumer<Component> list, TooltipFlag flags) {
		List<Component> shiftLines = new ArrayList<>();
		TooltipHandler.addOnShift(shiftLines, () -> addInformationAfterShift(stack, world, list, flags));
		shiftLines.forEach(list);
	}

	public void addInformationAfterShift(ItemStack stack, TooltipContext world, Consumer<Component> list, TooltipFlag flags) {
		Player player = Proxy.INSTANCE.getClientPlayer();
		list.accept(getArmorSetTitle(player));
		addArmorSetDescription(stack, list);
		ItemStack[] stacks = getArmorSetStacks();
		for (ItemStack armor : stacks) {
			MutableComponent cmp = Component.literal(" - ").append(armor.getHoverName());
			EquipmentSlot slot = ((ManasteelArmorItem) armor.getItem()).getEquipmentSlot();
			cmp.withStyle(hasArmorSetItem(player, slot) ? ChatFormatting.GREEN : ChatFormatting.GRAY);
			list.accept(cmp);
		}
		if (hasPhantomInk(stack)) {
			list.accept(Component.translatable("botaniamisc.hasPhantomInk").withStyle(ChatFormatting.GRAY));
		}
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(BotaniaItems.manasteelHelm),
			new ItemStack(BotaniaItems.manasteelChest),
			new ItemStack(BotaniaItems.manasteelLegs),
			new ItemStack(BotaniaItems.manasteelBoots)
	});

	public ItemStack[] getArmorSetStacks() {
		return armorSet.get();
	}

	public boolean hasArmorSet(Player player) {
		return hasArmorSetItem(player, EquipmentSlot.HEAD) && hasArmorSetItem(player, EquipmentSlot.CHEST) && hasArmorSetItem(player, EquipmentSlot.LEGS) && hasArmorSetItem(player, EquipmentSlot.FEET);
	}

	public boolean hasArmorSetItem(Player player, EquipmentSlot slot) {
		if (player == null || player.getInventory() == null) {
			return false;
		}

		ItemStack stack = player.getItemBySlot(slot);
		if (stack.isEmpty()) {
			return false;
		}

		return switch (slot) {
			case HEAD -> stack.getItem() instanceof ManasteelHelmItem;
			case CHEST -> stack.is(BotaniaItems.manasteelChest);
			case LEGS -> stack.is(BotaniaItems.manasteelLegs);
			case FEET -> stack.is(BotaniaItems.manasteelBoots);
			default -> false;
		};

	}

	private int getSetPiecesEquipped(Player player) {
		int pieces = 0;
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR && hasArmorSetItem(player, slot)) {
				pieces++;
			}
		}

		return pieces;
	}

	public MutableComponent getArmorSetName() {
		return Component.translatable("botania.armorset.manasteel.name");
	}

	private Component getArmorSetTitle(Player player) {
		Component end = getArmorSetName()
				.append(" (" + getSetPiecesEquipped(player) + "/" + getArmorSetStacks().length + ")")
				.withStyle(ChatFormatting.GRAY);
		return Component.translatable("botaniamisc.armorset")
				.append(" ")
				.append(end);
	}

	public void addArmorSetDescription(ItemStack stack, Consumer<Component> list) {
		list.accept(Component.translatable("botania.armorset.manasteel.desc").withStyle(ChatFormatting.GRAY));
	}

	@Override
	public boolean hasPhantomInk(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_PHANTOM_INK, false);
	}

	@Override
	public void setPhantomInk(ItemStack stack, boolean ink) {
		ItemNBTHelper.setBoolean(stack, TAG_PHANTOM_INK, ink);
	}
}
