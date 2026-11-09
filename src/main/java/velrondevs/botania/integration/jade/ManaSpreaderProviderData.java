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

import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;

public enum ManaSpreaderProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String LENS = "lens";

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaSpreaderBlockEntity spreader) {
			data.putInt(JadeManaHelper.MANA, spreader.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, spreader.getMaxMana());
			JadeManaHelper.putBinding(data, accessor, accessor.getLevel(), spreader.getBinding());
			JadeManaHelper.putStack(data, LENS, accessor, spreader.getItemHandler().getItem(0));
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.MANA_SPREADER;
	}
}
