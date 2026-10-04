package velrondevs.botania.common.item.equipment.bauble;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.ManaItemHandler;

public class BandOfAuraItem extends BaubleItem {

	private final int interval;

	public BandOfAuraItem(Properties props, int interval) {
		super(props);
		this.interval = 5 * interval;
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide && entity instanceof Player player && player.tickCount % interval == 0) {
			ManaItemHandler.instance().dispatchManaExact(stack, player, 5, true);
		}
	}
}
