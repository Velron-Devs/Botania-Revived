package velrondevs.botania.common.item.relic;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;

import velrondevs.botania.api.item.Relic;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;
import java.util.UUID;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class RingOfTheAesirItem extends RingOfLokiItem {

	private static final ResourceLocation HEALTH_MODIFIER_ID = prefix("aesir_ring_health");

	public RingOfTheAesirItem(Properties props) {
		super(props);
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, null);
	}

	public static ItemStack findWithAesir(Item ring, Player player) {
		ItemStack aesir = EquipmentHandler.findOrEmpty(BotaniaItems.aesirRing, player);
		return aesir.isEmpty() ? EquipmentHandler.findOrEmpty(ring, player) : aesir;
	}

	public static void onToss(ItemTossEvent e) {
		Player player = e.getPlayer();
		ItemEntity entity = e.getEntity();
		ItemStack stack = entity.getItem();
		if (player.level().isClientSide || !player.isShiftKeyDown() || !stack.is(BotaniaItems.aesirRing)) {
			return;
		}
		UUID owner = makeRelic(stack).getSoulbindUUID();
		ItemStack thor = new ItemStack(BotaniaItems.thorRing);
		ItemStack odin = new ItemStack(BotaniaItems.odinRing);
		ItemStack loki = new ItemStack(BotaniaItems.lokiRing);
		if (owner != null) {
			RingOfThorItem.makeRelic(thor).bindToUUID(owner);
			RingOfOdinItem.makeRelic(odin).bindToUUID(owner);
			RingOfLokiItem.makeRelic(loki).bindToUUID(owner);
		}
		entity.setItem(thor);
		for (ItemStack ring : List.of(odin, loki)) {
			ItemEntity extra = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), ring);
			extra.setDeltaMovement(entity.getDeltaMovement().add((player.getRandom().nextDouble() - 0.5) * 0.1, 0.05, (player.getRandom().nextDouble() - 0.5) * 0.1));
			extra.setPickUpDelay(40);
			entity.level().addFreshEntity(extra);
		}
	}

	@Override
	public void onValidPlayerWornTick(Player player) {
		if (player.isOnFire()) {
			player.clearFire();
		}
		if (!player.level().isClientSide) {
			updateHealthModifier(player, EquipmentHandler.findOrEmpty(BotaniaItems.odinRing, player).isEmpty());
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		super.onUnequipped(stack, living);
		if (!living.level().isClientSide) {
			updateHealthModifier(living, false);
		}
	}

	private static void updateHealthModifier(LivingEntity living, boolean apply) {
		AttributeInstance health = living.getAttribute(Attributes.MAX_HEALTH);
		if (health == null) {
			return;
		}
		if (apply) {
			if (!health.hasModifier(HEALTH_MODIFIER_ID)) {
				health.addTransientModifier(new AttributeModifier(HEALTH_MODIFIER_ID, 20, AttributeModifier.Operation.ADD_VALUE));
			}
		} else if (health.hasModifier(HEALTH_MODIFIER_ID)) {
			health.removeModifier(HEALTH_MODIFIER_ID);
			if (living.getHealth() > living.getMaxHealth()) {
				living.setHealth(living.getMaxHealth());
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.aesirRing.thor").withStyle(ChatFormatting.GREEN));
		tooltip.add(Component.translatable("botaniamisc.aesirRing.loki").withStyle(ChatFormatting.BLUE));
		tooltip.add(Component.translatable("botaniamisc.aesirRing.odin").withStyle(ChatFormatting.RED));
		super.appendHoverText(stack, context, tooltip, flags);
	}
}
