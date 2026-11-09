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

public enum ManaSpreaderProvider implements IBlockComponentProvider {
	INSTANCE;

	private static final String LENS = "lens";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof ManaSpreaderBlockEntity spreader)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, spreader.getCurrentMana());
		int max = JadeManaHelper.getInt(data, JadeManaHelper.MAX, spreader.getMaxMana());
		JadeManaHelper.addManaBar(tooltip, config, mana, max, JadeManaHelper.manaColor(config, spreader.getVariant().hudColor));

		JadeManaHelper.addBinding(tooltip, config, data, accessor, "botania.jade.target", "botania.jade.no_target");

		ItemStack lens = data.contains(JadeManaHelper.BOUND) ? JadeManaHelper.getStack(data, LENS, accessor) : spreader.getItemHandler().getItem(0);
		if (!lens.isEmpty()) {
			JadeManaHelper.addItemLine(tooltip, lens, Component.translatable("botania.jade.lens", lens.getHoverName()));
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.MANA_SPREADER;
	}
}
