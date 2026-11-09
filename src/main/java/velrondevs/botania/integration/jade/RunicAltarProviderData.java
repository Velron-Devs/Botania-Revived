package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import velrondevs.botania.common.block.block_entity.RunicAltarBlockEntity;

import java.util.List;

public enum RunicAltarProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof RunicAltarBlockEntity altar) {
			data.putInt(JadeManaHelper.MANA, altar.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, altar.getTargetMana());
			JadeManaHelper.putStack(data, JadeManaHelper.OUTPUT, accessor, altar.getCurrentRecipeOutput());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.RUNIC_ALTAR;
	}
}
