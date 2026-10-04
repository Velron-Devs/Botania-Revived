package velrondevs.botania.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.event.SpellCostCalcEvent;
import com.hollingsworth.arsnouveau.api.util.ManaUtil;
import com.hollingsworth.arsnouveau.common.capability.ManaCap;
import com.hollingsworth.arsnouveau.setup.registry.CapabilityRegistry;
import com.hollingsworth.arsnouveau.setup.registry.ModPotions;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.common.impl.mana.PlayerManaSources;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

final class ArsManaBridge implements PlayerManaSources.Source {
	private static final ItemStack SPELL_PAYER = new ItemStack(Items.STICK);
	private static final ItemStack OVERFLOW_CHARGER = new ItemStack(Items.STICK);
	private static final int COOKIE_REGEN_TICKS = 3600;

	@Override
	public int getAvailable(Player player, ItemStack requestor) {
		if (!ArsNouveauConfig.arsPowersBotaniaItems() || requestor == SPELL_PAYER || requestor == OVERFLOW_CHARGER
				|| requestor.isEmpty() || XplatAbstractions.INSTANCE.findManaItem(requestor) != null) {
			return 0;
		}
		ManaCap cap = CapabilityRegistry.getMana(player);
		if (cap == null) {
			return 0;
		}
		return (int) Math.min(Integer.MAX_VALUE, Math.floor(cap.getCurrentMana() * ArsNouveauConfig.arsToBotania()));
	}

	@Override
	public int take(Player player, ItemStack requestor, int amount, boolean simulate) {
		ManaCap cap = CapabilityRegistry.getMana(player);
		if (cap == null || amount <= 0) {
			return 0;
		}
		if (!simulate) {
			cap.removeMana(amount / ArsNouveauConfig.arsToBotania());
			sync(player, cap);
		}
		return amount;
	}

	private static void sync(Player player, ManaCap cap) {
		if (player instanceof ServerPlayer serverPlayer && serverPlayer.connection != null) {
			cap.syncToClient(serverPlayer);
		}
	}

	static void onSpellCost(SpellCostCalcEvent.Pre event) {
		if (!ArsNouveauConfig.botaniaPaysSpells()) {
			return;
		}
		LivingEntity caster = event.context.getUnwrappedCaster();
		if (!(caster instanceof ServerPlayer player) || player.isCreative()) {
			return;
		}
		ManaCap cap = CapabilityRegistry.getMana(player);
		if (cap == null || event.currentCost <= 0 || event.currentCost > cap.getMaxMana()) {
			return;
		}
		double deficit = event.currentCost - cap.getCurrentMana();
		if (deficit <= 0) {
			return;
		}
		double ratio = ArsNouveauConfig.botaniaToArs();
		int needed = (int) Math.ceil(deficit / ratio);
		if (ManaItemHandler.instance().requestManaExact(SPELL_PAYER, player, needed, true)) {
			cap.addMana(needed * ratio);
			sync(player, cap);
		}
	}

	static void onPlayerTick(PlayerTickEvent.Post event) {
		if (!(event.getEntity() instanceof ServerPlayer player) || player.tickCount % 20 != 0
				|| !ArsNouveauConfig.overflowChargesBotaniaItems()) {
			return;
		}
		ManaCap cap = CapabilityRegistry.getMana(player);
		if (cap == null || cap.getMaxMana() <= 0 || cap.getCurrentMana() < cap.getMaxMana()) {
			return;
		}
		int mana = (int) (ManaUtil.getManaRegen(player) * ArsNouveauConfig.arsToBotania());
		if (mana > 0) {
			ManaItemHandler.instance().dispatchMana(OVERFLOW_CHARGER, player, mana, true);
		}
	}

	static void onFinishUsingItem(LivingEntityUseItemEvent.Finish event) {
		LivingEntity entity = event.getEntity();
		if (entity.level().isClientSide() || !ArsNouveauConfig.manaCookieRegen() || !event.getItem().is(BotaniaItems.manaCookie)) {
			return;
		}
		entity.addEffect(new MobEffectInstance(ModPotions.MANA_REGEN_EFFECT, COOKIE_REGEN_TICKS, 1));
	}
}
