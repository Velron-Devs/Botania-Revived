package velrondevs.botania.common.item.equipment.bauble;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.common.item.StoneOfTemperanceItem;
import velrondevs.botania.registry.BotaniaItems;

public class RingOfChordataItem extends BaubleItem {

	private static final int MANA_COST = 3;
	private static final int CONDUIT_POWER_AMPLIFIER = 0;
	private static final int DOLPHINS_GRACE_AMPLIFIER = 0;
	private static final int OUT_OF_MANA_DURATION = 60;

	public RingOfChordataItem(Properties props) {
		super(props);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity living) {
		if (!(living instanceof Player player) || player.level().isClientSide) {
			return;
		}
		if (player.isInWaterOrBubble()) {

			ItemStack result = EquipmentHandler.findOrEmpty(BotaniaItems.waterRing, living);
			if (result != stack) {
				return;
			}

			boolean hasMana = ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false);
			if (hasMana) {
				EntityHelper.addStaticEffect(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER);

				if (StoneOfTemperanceItem.hasTemperanceActive(player)) {
					EntityHelper.removeStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
				} else {
					EntityHelper.addStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
				}
				ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, true);
			} else {

				EntityHelper.convertStaticEffectToFinite(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER,
						OUT_OF_MANA_DURATION);
				EntityHelper.convertStaticEffectToFinite(living, MobEffects.DOLPHINS_GRACE,
						DOLPHINS_GRACE_AMPLIFIER, OUT_OF_MANA_DURATION);
			}
		} else {
			onUnequipped(stack, living);
		}
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity living) {
		if (living instanceof Player player && player.isInWaterOrBubble()
				&& ManaItemHandler.instance().requestManaExact(stack, player, MANA_COST, false)) {
			EntityHelper.addStaticEffect(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER);
			if (!StoneOfTemperanceItem.hasTemperanceActive(player)) {
				EntityHelper.addStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
			}
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity living) {
		EntityHelper.removeStaticEffect(living, MobEffects.CONDUIT_POWER, CONDUIT_POWER_AMPLIFIER);
		EntityHelper.removeStaticEffect(living, MobEffects.DOLPHINS_GRACE, DOLPHINS_GRACE_AMPLIFIER);
	}

}
