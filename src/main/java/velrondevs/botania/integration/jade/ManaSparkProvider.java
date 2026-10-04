package velrondevs.botania.integration.jade;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;

import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.api.mana.spark.SparkUpgradeType;
import velrondevs.botania.common.entity.ManaSparkEntity;
import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.item.SparkAugmentItem;

public enum ManaSparkProvider implements IEntityComponentProvider {
	INSTANCE;

	@Override
	public void appendTooltip(ITooltip tooltip, EntityAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getEntity() instanceof ManaSparkEntity spark)) {
			return;
		}
		DyeColor network = spark.getNetwork();
		ItemStack wool = new ItemStack(ColorHelper.WOOL_MAP.apply(network));
		JadeManaHelper.addItemLine(tooltip, wool, Component.translatable("botania.jade.spark.network", Component.translatable("color.minecraft." + network.getName())));

		SparkUpgradeType upgrade = spark.getUpgrade();
		ItemStack augment = SparkAugmentItem.getByType(upgrade);
		if (!augment.isEmpty()) {
			JadeManaHelper.addItemLine(tooltip, augment, Component.translatable("botania.jade.spark.augment", augment.getHoverName()));
		}
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.MANA_SPARK;
	}
}
