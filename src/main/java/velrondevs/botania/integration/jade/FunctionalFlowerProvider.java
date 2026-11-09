package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;

public enum FunctionalFlowerProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final String REDSTONE = "redstone";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof FunctionalFlowerBlockEntity flower)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, flower.getMana());
		int max = JadeManaHelper.getInt(data, JadeManaHelper.MAX, flower.getMaxMana());
		JadeManaHelper.addManaBar(tooltip, config, mana, max, JadeManaHelper.manaColor(config, flower.getColor()));

		boolean redstone = data.contains(REDSTONE) ? data.getBooleanOr(REDSTONE, false) : flower.acceptsRedstone() && flower.redstoneSignal > 0;
		if (redstone) {
			tooltip.add(IThemeHelper.get().warning(Component.translatable("botania.jade.redstone_disabled")));
		}

		if (flower.isOvergrowthAffected() && flower.isOnSpecialSoil()) {
			tooltip.add(IThemeHelper.get().success(Component.translatable("botania.jade.overgrowth")));
		}

		JadeManaHelper.addBinding(tooltip, config, data, accessor, "botania.jade.bound_to", "botania.jade.not_bound");
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.FUNCTIONAL_FLOWER;
	}
}
