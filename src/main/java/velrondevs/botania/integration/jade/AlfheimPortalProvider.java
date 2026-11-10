package velrondevs.botania.integration.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

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

public enum AlfheimPortalProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final String PYLONS = "pylons";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof AlfheimPortalBlockEntity portalBe)) {
			return;
		}
		boolean open = accessor.getBlockState().hasProperty(BotaniaStateProperties.ALFPORTAL_STATE)
				&& accessor.getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF;
		tooltip.add(open
				? IThemeHelper.get().success(Component.translatable("botania.jade.portal.open"))
				: IThemeHelper.get().warning(Component.translatable("botania.jade.portal.closed")));

		CompoundTag data = accessor.getServerData();
		if (data.contains(PYLONS)) {
			tooltip.add(Component.translatable("botania.jade.portal.pylons", data.getIntOr(PYLONS, 0), AlfheimPortalBlockEntity.MIN_REQUIRED_PYLONS));
			int max = data.getIntOr(JadeManaHelper.MAX, 0);
			if (max > 0) {
				JadeManaHelper.addManaBar(tooltip, config, data.getIntOr(JadeManaHelper.MANA, 0), max, JadeManaHelper.manaColor(config, 0x0095FF));
			}
		}
		JadeManaHelper.addNodeBoost(tooltip, portalBe.getNodeBoostMultiplier());
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.ALFHEIM_PORTAL;
	}
}
