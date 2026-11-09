package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

public enum GeneratingFlowerProvider implements IBlockComponentProvider {
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
			int digesting = data.getIntOr(DIGESTING, 0);
			if (digesting > 0) {
				tooltip.add(Component.translatable("botania.jade.digesting", JadeManaHelper.number(config, digesting), JadeManaHelper.time(data.getIntOr(COOLDOWN, 0))));
			} else if (data.getIntOr(COOLDOWN, 0) > 0) {
				tooltip.add(Component.translatable("botania.jade.cooldown", JadeManaHelper.time(data.getIntOr(COOLDOWN, 0))));
			}
			if (data.getIntOr(BURN, 0) > 0) {
				tooltip.add(IThemeHelper.get().success(Component.translatable("botania.jade.burn_time", JadeManaHelper.time(data.getIntOr(BURN, 0)))));
			}
			if (data.getIntOr(STREAK, 0) > 0) {
				tooltip.add(Component.translatable("botania.jade.streak", data.getIntOr(STREAK, 0)));
			}
			if (data.contains(NEXT_COLOR)) {
				DyeColor color = DyeColor.byId(data.getIntOr(NEXT_COLOR, 0));
				ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(color));
				JadeManaHelper.addItemLine(tooltip, wool, Component.translatable("botania.jade.next_color", Component.translatable("color.minecraft." + color.getName())));
			}
			if (data.contains(DECAY)) {
				int left = data.getIntOr(DECAY, 0);
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
	public Identifier getUid() {
		return BotaniaJadeIds.GENERATING_FLOWER;
	}
}
