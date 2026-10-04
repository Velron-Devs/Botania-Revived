package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.common.block.block_entity.AvatarBlockEntity;
import velrondevs.botania.common.block.block_entity.LifeImbuerBlockEntity;

public enum ManaStorageProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static int maxMana(BlockEntity be) {
		if (be instanceof AvatarBlockEntity) {
			return AvatarBlockEntity.MAX_MANA;
		}
		if (be instanceof LifeImbuerBlockEntity) {
			return LifeImbuerBlockEntity.MAX_MANA;
		}
		return 0;
	}

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof ManaReceiver receiver)) {
			return;
		}
		int max = maxMana(accessor.getBlockEntity());
		if (max <= 0) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, receiver.getCurrentMana());
		JadeManaHelper.addManaBar(tooltip, config, Math.min(mana, max), max, JadeManaHelper.manaColor(config, 0x0095FF));
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaReceiver receiver) {
			data.putInt(JadeManaHelper.MANA, receiver.getCurrentMana());
		}
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.MANA_STORAGE;
	}
}
