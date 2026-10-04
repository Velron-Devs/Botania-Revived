package velrondevs.botania.integration.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.api.state.BotaniaStateProperties;
import velrondevs.botania.api.state.enums.AlfheimPortalState;
import velrondevs.botania.common.block.block_entity.AlfheimPortalBlockEntity;

import java.util.List;

public enum AlfheimPortalProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String PYLONS = "pylons";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof AlfheimPortalBlockEntity)) {
			return;
		}
		boolean open = accessor.getBlockState().hasProperty(BotaniaStateProperties.ALFPORTAL_STATE)
				&& accessor.getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF;
		tooltip.add(open
				? IThemeHelper.get().success(Component.translatable("botania.jade.portal.open"))
				: IThemeHelper.get().warning(Component.translatable("botania.jade.portal.closed")));

		CompoundTag data = accessor.getServerData();
		if (data.contains(PYLONS)) {
			tooltip.add(Component.translatable("botania.jade.portal.pylons", data.getInt(PYLONS), AlfheimPortalBlockEntity.MIN_REQUIRED_PYLONS));
			int max = data.getInt(JadeManaHelper.MAX);
			if (max > 0) {
				JadeManaHelper.addManaBar(tooltip, config, data.getInt(JadeManaHelper.MANA), max, JadeManaHelper.manaColor(config, 0x0095FF));
			}
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof AlfheimPortalBlockEntity portal)) {
			return;
		}
		List<BlockPos> pylons = portal.locatePylons(false);
		long mana = 0;
		long max = 0;
		for (BlockPos pylon : pylons) {
			if (accessor.getLevel().getBlockEntity(pylon.below()) instanceof ManaPool pool) {
				mana += pool.getCurrentMana();
				max += pool.getMaxMana();
			}
		}
		data.putInt(PYLONS, pylons.size());
		data.putInt(JadeManaHelper.MANA, (int) Math.min(Integer.MAX_VALUE, mana));
		data.putInt(JadeManaHelper.MAX, (int) Math.min(Integer.MAX_VALUE, max));
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.ALFHEIM_PORTAL;
	}
}
