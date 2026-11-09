package velrondevs.botania.client.integration.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.plugins.REICommonPlugin;
import me.shedaniel.rei.forge.REIPluginCommon;

import velrondevs.botania.compat.sophisticated.SophisticatedModule;
import velrondevs.botania.compat.sophisticated.rei.SophisticatedRei;
import velrondevs.botania.registry.BotaniaItems;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@REIPluginCommon
public class BotaniaReiCommonPlugin implements REICommonPlugin {
	@Override
	public String getPluginProviderName() {
		return "Botania";
	}

	@Override
	public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
		registry.register(prefix("display"), BotaniaReiDisplay.SERIALIZER);
	}

	@Override
	public void registerItemComparators(ItemComparatorRegistry registry) {
		registry.registerComponents(BotaniaItems.lexicon, BotaniaItems.brewFlask, BotaniaItems.brewVial,
				BotaniaItems.bloodPendant, BotaniaItems.incenseStick, BotaniaItems.flightTiara, BotaniaItems.laputaShard);
		if (SophisticatedModule.INSTANCE.isAvailable()) {
			SophisticatedRei.registerComparators(registry);
		}
	}
}
