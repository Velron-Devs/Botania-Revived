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

public enum TerraPlateProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof TerrestrialAgglomerationPlateBlockEntity)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int required = data.getIntOr(JadeManaHelper.MAX, 0);
		if (required <= 0) {
			return;
		}
		int mana = data.getIntOr(JadeManaHelper.MANA, 0);
		JadeManaHelper.addManaBar(tooltip, config, Math.min(mana, required), required, JadeManaHelper.manaColor(config, 0x2BD94C));
		ItemStack output = JadeManaHelper.getStack(data, JadeManaHelper.OUTPUT, accessor);
		if (!output.isEmpty()) {
			JadeManaHelper.addRecipeRow(tooltip, List.of(), (float) mana / required, output);
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.TERRA_PLATE;
	}
}
