package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;

import java.util.List;

public enum TerraPlateProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof TerrestrialAgglomerationPlateBlockEntity plate) {
			data.putInt(JadeManaHelper.MANA, plate.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, plate.getManaRequired());
			JadeManaHelper.putStack(data, JadeManaHelper.OUTPUT, accessor, plate.getCurrentRecipeOutput());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.TERRA_PLATE;
	}
}
