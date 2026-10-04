package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.block.flower.generating.EndoflameBlockEntity;
import velrondevs.botania.common.block.flower.generating.FluidGeneratorBlockEntity;
import velrondevs.botania.common.block.flower.generating.GourmaryllisBlockEntity;
import velrondevs.botania.common.block.flower.generating.HydroangeasBlockEntity;
import velrondevs.botania.common.block.flower.generating.MunchdewBlockEntity;
import velrondevs.botania.common.block.flower.generating.RafflowsiaBlockEntity;
import velrondevs.botania.common.block.flower.generating.SpectrolusBlockEntity;

public enum GeneratingFlowerProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String BURN = "burn";
	private static final String COOLDOWN = "cooldown";
	private static final String DECAY = "decay";
	private static final String DIGESTING = "digesting";
	private static final String STREAK = "streak";
	private static final String NEXT_COLOR = "nextColor";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof GeneratingFlowerBlockEntity flower)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, flower.getMana());
		int max = JadeManaHelper.getInt(data, JadeManaHelper.MAX, flower.getMaxMana());
		JadeManaHelper.addManaBar(tooltip, config, mana, max, JadeManaHelper.manaColor(config, flower.getColor()));

		if (config.get(BotaniaJadeIds.GENERATING_FLOWER_TIMERS)) {
			int digesting = data.getInt(DIGESTING);
			if (digesting > 0) {
				tooltip.add(Component.translatable("botania.jade.digesting", JadeManaHelper.number(config, digesting), JadeManaHelper.time(data.getInt(COOLDOWN))));
			} else if (data.getInt(COOLDOWN) > 0) {
				tooltip.add(Component.translatable("botania.jade.cooldown", JadeManaHelper.time(data.getInt(COOLDOWN))));
			}
			if (data.getInt(BURN) > 0) {
				tooltip.add(IThemeHelper.get().success(Component.translatable("botania.jade.burn_time", JadeManaHelper.time(data.getInt(BURN)))));
			}
			if (data.getInt(STREAK) > 0) {
				tooltip.add(Component.translatable("botania.jade.streak", data.getInt(STREAK)));
			}
			if (data.contains(NEXT_COLOR)) {
				DyeColor color = DyeColor.byId(data.getInt(NEXT_COLOR));
				ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(color));
				JadeManaHelper.addItemLine(tooltip, wool, Component.translatable("botania.jade.next_color", Component.translatable("color.minecraft." + color.getName())));
			}
			if (data.contains(DECAY)) {
				int left = data.getInt(DECAY);
				JadeManaHelper.addProgressBar(tooltip, config, (float) left / HydroangeasBlockEntity.DECAY_TIME,
						Component.translatable("botania.jade.decay", JadeManaHelper.time(left)));
			}
		}

		if (flower.isOvergrowthAffected() && flower.isOnSpecialSoil()) {
			tooltip.add(IThemeHelper.get().success(Component.translatable("botania.jade.overgrowth")));
		}

		JadeManaHelper.addBinding(tooltip, config, data, accessor, "botania.jade.bound_to", "botania.jade.not_bound");
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof GeneratingFlowerBlockEntity flower)) {
			return;
		}
		data.putInt(JadeManaHelper.MANA, flower.getMana());
		data.putInt(JadeManaHelper.MAX, flower.getMaxMana());
		JadeManaHelper.putBinding(data, accessor, accessor.getLevel(), flower.getBinding());

		if (flower instanceof EndoflameBlockEntity endoflame) {
			data.putInt(BURN, endoflame.getBurnTimeLeft());
		} else if (flower instanceof FluidGeneratorBlockEntity generator) {
			data.putInt(BURN, generator.getBurnTimeLeft());
			data.putInt(COOLDOWN, generator.getCooldown());
			if (generator instanceof HydroangeasBlockEntity hydroangeas) {
				data.putInt(DECAY, Math.max(0, HydroangeasBlockEntity.DECAY_TIME - hydroangeas.getPassiveDecayTicks()));
			}
		} else if (flower instanceof GourmaryllisBlockEntity gourmaryllis) {
			data.putInt(COOLDOWN, gourmaryllis.getCooldown());
			data.putInt(DIGESTING, gourmaryllis.getDigestingMana());
			data.putInt(STREAK, gourmaryllis.getStreakLength());
		} else if (flower instanceof MunchdewBlockEntity munchdew) {
			data.putInt(COOLDOWN, munchdew.getCooldown());
		} else if (flower instanceof RafflowsiaBlockEntity rafflowsia) {
			data.putInt(STREAK, rafflowsia.getStreakLength());
		} else if (flower instanceof SpectrolusBlockEntity spectrolus) {
			data.putInt(NEXT_COLOR, spectrolus.getNextColor().getId());
		}
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.GENERATING_FLOWER;
	}
}
