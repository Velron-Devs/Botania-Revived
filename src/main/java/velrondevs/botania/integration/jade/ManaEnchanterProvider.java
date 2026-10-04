package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.common.block.block_entity.ManaEnchanterBlockEntity;

import java.util.Locale;

public enum ManaEnchanterProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String STAGE = "stage";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof ManaEnchanterBlockEntity enchanter)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int stageId = JadeManaHelper.getInt(data, STAGE, enchanter.stage.ordinal());
		ManaEnchanterBlockEntity.State[] states = ManaEnchanterBlockEntity.State.values();
		ManaEnchanterBlockEntity.State stage = stageId >= 0 && stageId < states.length ? states[stageId] : ManaEnchanterBlockEntity.State.IDLE;
		tooltip.add(Component.translatable("botania.jade.enchanter.stage", Component.translatable("botania.jade.enchanter.stage." + stage.name().toLowerCase(Locale.ROOT))));

		int required = JadeManaHelper.getInt(data, JadeManaHelper.MAX, enchanter.getManaRequired());
		if (required > 0) {
			int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, enchanter.getCurrentMana());
			JadeManaHelper.addManaBar(tooltip, config, Math.min(mana, required), required, JadeManaHelper.manaColor(config, 0x0095FF));
		}

		ItemStack item = enchanter.itemToEnchant;
		if (!item.isEmpty()) {
			JadeManaHelper.addItemLine(tooltip, item, item.getHoverName());
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaEnchanterBlockEntity enchanter) {
			data.putInt(STAGE, enchanter.stage.ordinal());
			data.putInt(JadeManaHelper.MANA, enchanter.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, enchanter.getManaRequired());
		}
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.MANA_ENCHANTER;
	}
}
