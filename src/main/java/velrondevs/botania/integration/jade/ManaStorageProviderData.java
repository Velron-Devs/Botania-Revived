package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.common.block.block_entity.AvatarBlockEntity;
import velrondevs.botania.common.block.block_entity.LifeImbuerBlockEntity;

public enum ManaStorageProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaReceiver receiver) {
			data.putInt(JadeManaHelper.MANA, receiver.getCurrentMana());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.MANA_STORAGE;
	}
}
