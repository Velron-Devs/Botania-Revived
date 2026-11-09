package velrondevs.botania.common.item.equipment.armor.manaweave;

import net.minecraft.world.item.equipment.ArmorType;
import java.util.function.Consumer;
import com.google.common.base.Suppliers;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.annotations.SoftImplement;
import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.Supplier;

public class ManaweaveArmorItem extends ManasteelArmorItem {

	public ManaweaveArmorItem(ArmorType type, Properties props) {
		super(type, BotaniaAPI.instance().getManaweaveArmorMaterial(), props);
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, EquipmentSlot slot) {
		return ClientProxy.jingleTheBells ? ResourcesLib.MODEL_MANAWEAVE_NEW_HOLIDAY : ResourcesLib.MODEL_MANAWEAVE_NEW;
	}

	@NotNull
	@Override
	public Component getName(ItemStack stack) {
		if (XplatAbstractions.INSTANCE.isPhysicalClient() && ClientProxy.jingleTheBells) {
			return Component.translatable(getDescriptionId().replaceAll("manaweave", "santaweave"));
		}
		return super.getName(stack);
	}

	private static final Supplier<ItemStack[]> armorSet = Suppliers.memoize(() -> new ItemStack[] {
			new ItemStack(BotaniaItems.manaweaveHelm),
			new ItemStack(BotaniaItems.manaweaveChest),
			new ItemStack(BotaniaItems.manaweaveLegs),
			new ItemStack(BotaniaItems.manaweaveBoots)
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
			case HEAD -> stack.is(BotaniaItems.manaweaveHelm);
			case CHEST -> stack.is(BotaniaItems.manaweaveChest);
			case LEGS -> stack.is(BotaniaItems.manaweaveLegs);
			case FEET -> stack.is(BotaniaItems.manaweaveBoots);
			default -> false;
		};

	}

	@Override
	public MutableComponent getArmorSetName() {
		return Component.translatable("botania.armorset.manaweave.name");
	}

	@Override
	public void addInformationAfterShift(ItemStack stack, TooltipContext world, Consumer<Component> list, TooltipFlag flags) {
		if (XplatAbstractions.INSTANCE.isPhysicalClient() && ClientProxy.jingleTheBells) {
			list.accept(Component.translatable("botaniamisc.santaweaveInfo"));
			list.accept(Component.literal(""));
		}

		super.addInformationAfterShift(stack, world, list, flags);
	}

	@Override
	public void addArmorSetDescription(ItemStack stack, Consumer<Component> list) {
		list.accept(Component.translatable("botania.armorset.manaweave.desc0").withStyle(ChatFormatting.GRAY));
		list.accept(Component.translatable("botania.armorset.manaweave.desc1").withStyle(ChatFormatting.GRAY));
	}

	@SoftImplement("IForgeItem")
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return stack.is(BotaniaItems.manaweaveBoots);
	}
}
