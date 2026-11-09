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

public enum RunicAltarProvider implements IBlockComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof RunicAltarBlockEntity altar)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, altar.getCurrentMana());
		int required = JadeManaHelper.getInt(data, JadeManaHelper.MAX, altar.getTargetMana());
		if (required <= 0) {
			return;
		}
		JadeManaHelper.addManaBar(tooltip, config, Math.min(mana, required), required, JadeManaHelper.manaColor(config, 0x00E4D7));
		ItemStack output = JadeManaHelper.getStack(data, JadeManaHelper.OUTPUT, accessor);
		if (!output.isEmpty()) {
			JadeManaHelper.addRecipeRow(tooltip, List.of(), (float) mana / required, output);
		}
		if (mana >= required) {
			tooltip.add(IThemeHelper.get().success(Component.translatable("botania.jade.ready_wand")));
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.RUNIC_ALTAR;
	}
}
