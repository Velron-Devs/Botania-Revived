package velrondevs.botania.integration.jade;

import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.common.block.AlfheimPortalBlock;
import velrondevs.botania.common.block.AvatarBlock;
import velrondevs.botania.common.block.FloatingSpecialFlowerBlock;
import velrondevs.botania.common.block.PetalApothecaryBlock;
import velrondevs.botania.common.block.SpecialFlowerBlock;
import velrondevs.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import velrondevs.botania.common.block.block_entity.AvatarBlockEntity;
import velrondevs.botania.common.block.block_entity.BreweryBlockEntity;
import velrondevs.botania.common.block.block_entity.LifeImbuerBlockEntity;
import velrondevs.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import velrondevs.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import velrondevs.botania.common.block.block_entity.RunicAltarBlockEntity;
import velrondevs.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.common.block.mana.BotanicalBreweryBlock;
import velrondevs.botania.common.block.mana.LifeImbuerBlock;
import velrondevs.botania.common.block.mana.ManaEnchanterBlock;
import velrondevs.botania.common.block.mana.ManaPoolBlock;
import velrondevs.botania.common.block.mana.ManaSpreaderBlock;
import velrondevs.botania.common.block.mana.RunicAltarBlock;
import velrondevs.botania.common.block.mana.TerrestrialAgglomerationPlateBlock;
import velrondevs.botania.common.entity.ManaSparkEntity;

@WailaPlugin
public class BotaniaJadePlugin implements IWailaPlugin {
	@Override
	public void register(IWailaCommonRegistration registration) {
		registration.registerBlockDataProvider(ManaPoolProvider.INSTANCE, ManaPoolBlockEntity.class);
		registration.registerBlockDataProvider(ManaSpreaderProvider.INSTANCE, ManaSpreaderBlockEntity.class);
		registration.registerBlockDataProvider(GeneratingFlowerProvider.INSTANCE, GeneratingFlowerBlockEntity.class);
		registration.registerBlockDataProvider(FunctionalFlowerProvider.INSTANCE, FunctionalFlowerBlockEntity.class);
		registration.registerBlockDataProvider(RunicAltarProvider.INSTANCE, RunicAltarBlockEntity.class);
		registration.registerBlockDataProvider(TerraPlateProvider.INSTANCE, TerrestrialAgglomerationPlateBlockEntity.class);
		registration.registerBlockDataProvider(PetalApothecaryProvider.INSTANCE, PetalApothecaryBlockEntity.class);
		registration.registerBlockDataProvider(BreweryProvider.INSTANCE, BreweryBlockEntity.class);
		registration.registerBlockDataProvider(ManaEnchanterProvider.INSTANCE, ManaEnchanterBlockEntity.class);
		registration.registerBlockDataProvider(AlfheimPortalProvider.INSTANCE, AlfheimPortalBlockEntity.class);
		registration.registerBlockDataProvider(ManaStorageProvider.INSTANCE, AvatarBlockEntity.class);
		registration.registerBlockDataProvider(ManaStorageProvider.INSTANCE, LifeImbuerBlockEntity.class);
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.addConfig(BotaniaJadeIds.ENABLED, true);
		registration.addConfig(BotaniaJadeIds.SHOW_NUMBERS, true);
		registration.addConfig(BotaniaJadeIds.COMPACT_NUMBERS, false);
		registration.addConfig(BotaniaJadeIds.NATIVE_COLORS, false);
		registration.addConfig(BotaniaJadeIds.MANA_BAR_COLOR, JadeManaHelper.DEFAULT_MANA_COLOR, JadeManaHelper::isValidColor);
		registration.addConfig(BotaniaJadeIds.PROGRESS_BAR_COLOR, JadeManaHelper.DEFAULT_PROGRESS_COLOR, JadeManaHelper::isValidColor);
		registration.addConfig(BotaniaJadeIds.SHOW_BINDING, true);
		registration.addConfig(BotaniaJadeIds.MANA_POOL_RECIPE, true);
		registration.addConfig(BotaniaJadeIds.GENERATING_FLOWER_TIMERS, true);

		registration.registerBlockComponent(ManaPoolProvider.INSTANCE, ManaPoolBlock.class);
		registration.registerBlockComponent(ManaSpreaderProvider.INSTANCE, ManaSpreaderBlock.class);
		registration.registerBlockComponent(GeneratingFlowerProvider.INSTANCE, SpecialFlowerBlock.class);
		registration.registerBlockComponent(GeneratingFlowerProvider.INSTANCE, FloatingSpecialFlowerBlock.class);
		registration.registerBlockComponent(FunctionalFlowerProvider.INSTANCE, SpecialFlowerBlock.class);
		registration.registerBlockComponent(FunctionalFlowerProvider.INSTANCE, FloatingSpecialFlowerBlock.class);
		registration.registerBlockComponent(RunicAltarProvider.INSTANCE, RunicAltarBlock.class);
		registration.registerBlockComponent(TerraPlateProvider.INSTANCE, TerrestrialAgglomerationPlateBlock.class);
		registration.registerBlockComponent(PetalApothecaryProvider.INSTANCE, PetalApothecaryBlock.class);
		registration.registerBlockComponent(BreweryProvider.INSTANCE, BotanicalBreweryBlock.class);
		registration.registerBlockComponent(ManaEnchanterProvider.INSTANCE, ManaEnchanterBlock.class);
		registration.registerBlockComponent(AlfheimPortalProvider.INSTANCE, AlfheimPortalBlock.class);
		registration.registerBlockComponent(ManaStorageProvider.INSTANCE, AvatarBlock.class);
		registration.registerBlockComponent(ManaStorageProvider.INSTANCE, LifeImbuerBlock.class);
		registration.registerEntityComponent(ManaSparkProvider.INSTANCE, ManaSparkEntity.class);
	}
}
