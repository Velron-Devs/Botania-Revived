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

import velrondevs.botania.common.block.block_entity.ManaEnchanterBlockEntity;

import java.util.Locale;

public enum ManaEnchanterProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String STAGE = "stage";

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaEnchanterBlockEntity enchanter) {
			data.putInt(STAGE, enchanter.stage.ordinal());
			data.putInt(JadeManaHelper.MANA, enchanter.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, enchanter.getManaRequired());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.MANA_ENCHANTER;
	}
}
