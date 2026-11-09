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

public enum FunctionalFlowerProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String REDSTONE = "redstone";

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof FunctionalFlowerBlockEntity flower) {
			data.putInt(JadeManaHelper.MANA, flower.getMana());
			data.putInt(JadeManaHelper.MAX, flower.getMaxMana());
			data.putBoolean(REDSTONE, flower.acceptsRedstone() && flower.redstoneSignal > 0);
			JadeManaHelper.putBinding(data, accessor, accessor.getLevel(), flower.getBinding());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.FUNCTIONAL_FLOWER;
	}
}
