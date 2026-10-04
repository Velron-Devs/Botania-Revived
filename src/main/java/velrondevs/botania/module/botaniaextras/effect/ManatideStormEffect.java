package velrondevs.botania.module.botaniaextras.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.mana.ManaItem;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.List;

public class ManatideStormEffect extends MobEffect {
	public static final int DRAIN = 1040;

	public ManatideStormEffect() {
		super(MobEffectCategory.HARMFUL, 0x0000C0);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean applyEffectTick(LivingEntity entity, int amplifier) {
		if (entity instanceof Player player && !player.level().isClientSide) {
			drain(player);
			wilt(player);
		}
		return true;
	}

	private static void drain(Player player) {
		List<ItemStack> stacks = new ArrayList<>(ManaItemHandler.instance().getManaItems(player));
		stacks.addAll(ManaItemHandler.instance().getManaAccesories(player));
		for (ItemStack stack : stacks) {
			ManaItem item = XplatAbstractions.INSTANCE.findManaItem(stack);
			if (item != null && item.getMana() > 0) {
				item.addMana(-Math.min(item.getMana(), DRAIN));
				return;
			}
		}
	}

	private static void wilt(Player player) {
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.is(BotaniaItems.blackLotus)) {
				player.getInventory().setItem(i, new ItemStack(BotaniaExtrasUtilities.wiltedLotus, stack.getCount()));
			} else if (stack.is(BotaniaItems.blackerLotus)) {
				player.getInventory().setItem(i, new ItemStack(BotaniaExtrasUtilities.deathlyLotus, stack.getCount()));
			}
		}
	}
}
