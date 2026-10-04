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

import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.List;

public enum ManaPoolProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String OUTPUTTING = "outputting";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof ManaPoolBlockEntity pool)) {
			return;
		}
		CompoundTag data = accessor.getServerData();
		int mana = JadeManaHelper.getInt(data, JadeManaHelper.MANA, pool.getCurrentMana());
		int max = JadeManaHelper.getInt(data, JadeManaHelper.MAX, pool.getMaxMana());
		JadeManaHelper.addManaBar(tooltip, config, mana, max, JadeManaHelper.manaColor(config, 0x0095FF));

		if (config.get(BotaniaJadeIds.MANA_POOL_RECIPE)) {
			ItemStack held = accessor.getPlayer().getMainHandItem();
			if (!held.isEmpty()) {
				ManaInfusionRecipe recipe = pool.getMatchingRecipe(held, accessor.getLevel().getBlockState(accessor.getPosition().below()));
				if (recipe != null) {
					ItemStack output = recipe.getResultItem(accessor.getLevel().registryAccess());
					int cost = recipe.getManaToConsume();
					float progress = cost <= 0 ? 1F : (float) mana / cost;
					JadeManaHelper.addRecipeRow(tooltip, List.of(held.copyWithCount(1)), progress, output);
				}
			}
		}

		if (accessor.showDetails()) {
			boolean outputting = data.contains(OUTPUTTING) ? data.getBoolean(OUTPUTTING) : pool.isOutputtingPower();
			tooltip.add(JadeManaHelper.gray(Component.translatable(outputting ? "botaniamisc.outputtingPower" : "botaniamisc.inputtingPower")));
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (accessor.getBlockEntity() instanceof ManaPoolBlockEntity pool) {
			data.putInt(JadeManaHelper.MANA, pool.getCurrentMana());
			data.putInt(JadeManaHelper.MAX, pool.getMaxMana());
			data.putBoolean(OUTPUTTING, pool.isOutputtingPower());
		}
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.MANA_POOL;
	}
}
