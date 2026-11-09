package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.common.block.block_entity.BreweryBlockEntity;

import java.util.List;

public enum BreweryProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof BreweryBlockEntity brewery) {
			data.putInt(JadeManaHelper.MANA, brewery.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, brewery.getManaCost());
			ItemStack container = brewery.getItemHandler().getItem(0);
			if (brewery.recipe != null && !container.isEmpty()) {
				JadeManaHelper.putStack(data, JadeManaHelper.OUTPUT, accessor, brewery.recipe.getOutput(container));
			}
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.BREWERY;
	}
}
