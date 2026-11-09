package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.theme.IThemeHelper;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.block.flower.generating.EndoflameBlockEntity;
import velrondevs.botania.common.block.flower.generating.FluidGeneratorBlockEntity;
import velrondevs.botania.common.block.flower.generating.GourmaryllisBlockEntity;
import velrondevs.botania.common.block.flower.generating.HydroangeasBlockEntity;
import velrondevs.botania.common.block.flower.generating.MunchdewBlockEntity;
import velrondevs.botania.common.block.flower.generating.RafflowsiaBlockEntity;
import velrondevs.botania.common.block.flower.generating.SpectrolusBlockEntity;

public enum GeneratingFlowerProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String BURN = "burn";
	private static final String COOLDOWN = "cooldown";
	private static final String DECAY = "decay";
	private static final String DIGESTING = "digesting";
	private static final String STREAK = "streak";
	private static final String NEXT_COLOR = "nextColor";

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof GeneratingFlowerBlockEntity flower)) {
			return;
		}
		data.putInt(JadeManaHelper.MANA, flower.getMana());
		data.putInt(JadeManaHelper.MAX, flower.getMaxMana());
		JadeManaHelper.putBinding(data, accessor, accessor.getLevel(), flower.getBinding());

		if (flower instanceof EndoflameBlockEntity endoflame) {
			data.putInt(BURN, endoflame.getBurnTimeLeft());
		} else if (flower instanceof FluidGeneratorBlockEntity generator) {
			data.putInt(BURN, generator.getBurnTimeLeft());
			data.putInt(COOLDOWN, generator.getCooldown());
			if (generator instanceof HydroangeasBlockEntity hydroangeas) {
				data.putInt(DECAY, Math.max(0, HydroangeasBlockEntity.DECAY_TIME - hydroangeas.getPassiveDecayTicks()));
			}
		} else if (flower instanceof GourmaryllisBlockEntity gourmaryllis) {
			data.putInt(COOLDOWN, gourmaryllis.getCooldown());
			data.putInt(DIGESTING, gourmaryllis.getDigestingMana());
			data.putInt(STREAK, gourmaryllis.getStreakLength());
		} else if (flower instanceof MunchdewBlockEntity munchdew) {
			data.putInt(COOLDOWN, munchdew.getCooldown());
		} else if (flower instanceof RafflowsiaBlockEntity rafflowsia) {
			data.putInt(STREAK, rafflowsia.getStreakLength());
		} else if (flower instanceof SpectrolusBlockEntity spectrolus) {
			data.putInt(NEXT_COLOR, spectrolus.getNextColor().getId());
		}
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.GENERATING_FLOWER;
	}
}
