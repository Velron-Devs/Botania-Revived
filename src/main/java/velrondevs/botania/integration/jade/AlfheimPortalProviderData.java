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

public enum AlfheimPortalProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String PYLONS = "pylons";

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
	public Identifier getUid() {
		return BotaniaJadeIds.ALFHEIM_PORTAL;
	}
}
