package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.common.block.block_entity.BreweryBlockEntity;

import java.util.List;

public enum BreweryProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof BreweryBlockEntity brewery)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int required = JadeManaHelper.getInt(data, JadeManaHelper.MAX, brewery.getManaCost());
		if (required <= 0) {
			return;
		}
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, brewery.getCurrentMana());
		JadeManaHelper.addManaBar(tooltip, config, Math.min(mana, required), required, JadeManaHelper.manaColor(config, 0xD72FFF));
		ItemStack output = JadeManaHelper.getStack(data, JadeManaHelper.OUTPUT, accessor);
		if (!output.isEmpty()) {
			JadeManaHelper.addRecipeRow(tooltip, List.of(brewery.getItemHandler().getItem(0).copyWithCount(1)), (float) mana / required, output);
		}
	}

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
	public ResourceLocation getUid() {
		return BotaniaJadeIds.BREWERY;
	}
}
