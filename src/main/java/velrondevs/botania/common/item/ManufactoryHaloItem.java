package velrondevs.botania.common.item;

import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.registry.BotaniaSounds;

import java.util.List;

public class ManufactoryHaloItem extends AssemblyHaloItem {
	public static final String TAG_ACTIVE = "active";

	private static final Identifier glowTexture = Identifier.parse(ResourcesLib.MISC_GLOW_CYAN);

	public ManufactoryHaloItem(Item.Properties props) {
		super(props);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot eqSlot) {
		super.inventoryTick(stack, world, entity, eqSlot);
		boolean equipped = eqSlot == EquipmentSlot.MAINHAND;

		if (!world.isClientSide() && entity instanceof Player player && !equipped && isActive(stack)) {

			for (int i = 1; i < SEGMENTS; i++) {
				tryCraft(player, stack, i, false);
			}
		}
	}

	@Override
	public Identifier getGlowResource(ItemStack stack) {
		return isActive(stack) ? glowTexture : super.getGlowResource(stack);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack stack, TooltipContext world, TooltipDisplay tooltipDisplay, Consumer<Component> stacks, @NotNull TooltipFlag flags) {
		if (isActive(stack)) {
			stacks.accept(Component.translatable("botaniamisc.active"));
		} else {
			stacks.accept(Component.translatable("botaniamisc.inactive"));
		}
	}

	@NotNull
	@Override
	public InteractionResult use(Level world, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (getSegmentLookedAt(stack, player) == 0 && player.isSecondaryUseActive()) {
			togglePassive(stack, player, world);
			return InteractionResult.SUCCESS;
		}

		return super.use(world, player, hand);
	}

	@Override
	public boolean overrideOtherStackedOnMe(@NotNull ItemStack stack, @NotNull ItemStack cursor, @NotNull Slot slot,
			@NotNull ClickAction click, Player player, @NotNull SlotAccess access) {
		Level world = player.level();
		if (click == ClickAction.SECONDARY && slot.allowModification(player) && cursor.isEmpty()) {
			togglePassive(stack, player, world);
			access.set(cursor);
			return true;
		}
		return false;
	}

	private void togglePassive(ItemStack stack, LivingEntity living, Level world) {
		ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, !isActive(stack));
		if (living instanceof Player player && world != null) {
			world.playSound(player, player.getX(), player.getY(), player.getZ(), BotaniaSounds.manufactoryHaloConfigure, SoundSource.NEUTRAL, 1F, 1F);
		}
	}

	private static boolean isActive(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, true);
	}
}
