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

import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.List;

public enum ManaPoolProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String OUTPUTTING = "outputting";

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaPoolBlockEntity pool) {
			data.putInt(JadeManaHelper.MANA, pool.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, pool.getMaxMana());
			data.putBoolean(OUTPUTTING, pool.isOutputtingPower());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.MANA_POOL;
	}
}
