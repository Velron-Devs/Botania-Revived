package velrondevs.botania.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.datagen.providers.BotaniaAtlasProvider;
import velrondevs.botania.datagen.providers.DatapackRegistryProvider;
import velrondevs.botania.datagen.providers.LooniumStructureConfigurationProvider;
import velrondevs.botania.datagen.providers.advancements.AdvancementProvider;
import velrondevs.botania.datagen.providers.integration.CuriosSlotProvider;
import velrondevs.botania.datagen.providers.integration.PatchouliBookProvider;
import velrondevs.botania.datagen.providers.integration.ThaumaturgeDataProvider;
import velrondevs.botania.datagen.providers.lang.BotaniaLanguageProvider;
import velrondevs.botania.datagen.providers.loot.BotaniaLootTableProvider;
import velrondevs.botania.datagen.providers.models.BlockShapeModelProvider;
import velrondevs.botania.datagen.providers.models.BlockstateProvider;
import velrondevs.botania.datagen.providers.models.CustomItemModelProvider;
import velrondevs.botania.datagen.providers.models.FloatingFlowerModelProvider;
import velrondevs.botania.datagen.providers.models.ItemModelProvider;
import velrondevs.botania.datagen.providers.models.PottedPlantModelProvider;
import velrondevs.botania.datagen.providers.particles.BotaniaParticleDescriptionProvider;
import velrondevs.botania.datagen.providers.recipes.BrewProvider;
import velrondevs.botania.datagen.providers.recipes.ConventionRecipeProvider;
import velrondevs.botania.datagen.providers.recipes.CraftingRecipeProvider;
import velrondevs.botania.datagen.providers.recipes.ElvenTradeProvider;
import velrondevs.botania.datagen.providers.recipes.ManaInfusionProvider;
import velrondevs.botania.datagen.providers.recipes.OrechidProvider;
import velrondevs.botania.datagen.providers.recipes.PetalApothecaryProvider;
import velrondevs.botania.datagen.providers.recipes.PureDaisyProvider;
import velrondevs.botania.datagen.providers.recipes.RunicAltarProvider;
import velrondevs.botania.datagen.providers.recipes.SmeltingProvider;
import velrondevs.botania.datagen.providers.recipes.StonecuttingProvider;
import velrondevs.botania.datagen.providers.recipes.TerrestrialAgglomerationProvider;
import velrondevs.botania.datagen.providers.sounds.BotaniaSoundDefinitionsProvider;
import velrondevs.botania.datagen.providers.tags.BannerPatternTagsProvider;
import velrondevs.botania.datagen.providers.tags.BiomeTagProvider;
import velrondevs.botania.datagen.providers.tags.BlockTagProvider;
import velrondevs.botania.datagen.providers.tags.ConventionBlockTagProvider;
import velrondevs.botania.datagen.providers.tags.ConventionItemTagProvider;
import velrondevs.botania.datagen.providers.tags.DamageTypeTagProvider;
import velrondevs.botania.datagen.providers.tags.EntityTagProvider;
import velrondevs.botania.datagen.providers.tags.ItemTagProvider;
import velrondevs.botania.module.BotaniaModules;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BotaniaDatagen {
	@SubscribeEvent
	public static void gatherServerData(GatherDataEvent.Server evt) {
		PackOutput output = evt.getGenerator().getPackOutput();

		DatapackRegistryProvider datapackRegistries = evt.addProvider(new DatapackRegistryProvider(output, evt.getLookupProvider()));
		CompletableFuture<HolderLookup.Provider> lookup = datapackRegistries.getRegistryProvider();

		evt.addProvider(new velrondevs.botania.datagen.providers.ComponentBindingProvider(lookup));
		evt.addProvider(new BotaniaLootTableProvider(output, lookup));
		evt.addProvider(new LooniumStructureConfigurationProvider(output, lookup));

		BlockTagProvider blockTagProvider = evt.addProvider(new BlockTagProvider(output, lookup));
		ItemTagProvider itemTagProvider = evt.addProvider(new ItemTagProvider(output, lookup, blockTagProvider.contentsGetter()));
		evt.addProvider(new EntityTagProvider(output, lookup));
		evt.addProvider(new BannerPatternTagsProvider(output, lookup));
		evt.addProvider(new BiomeTagProvider(output, lookup));
		evt.addProvider(new DamageTypeTagProvider(output, lookup));

		ConventionBlockTagProvider conventionBlockTagProvider = evt.addProvider(new ConventionBlockTagProvider(output, lookup, blockTagProvider.contentsGetter()));
		evt.addProvider(new ConventionItemTagProvider(output, lookup, itemTagProvider.contentsGetter(), conventionBlockTagProvider.contentsGetter()));

		evt.addProvider(new StonecuttingProvider(output, lookup));
		evt.addProvider(new CraftingRecipeProvider(output, lookup));
		evt.addProvider(new ConventionRecipeProvider(output, lookup));
		evt.addProvider(new SmeltingProvider(output, lookup));
		evt.addProvider(new ElvenTradeProvider(output, lookup));
		evt.addProvider(new ManaInfusionProvider(output, lookup));
		evt.addProvider(new PureDaisyProvider(output, lookup));
		evt.addProvider(new BrewProvider(output, lookup));
		evt.addProvider(new PetalApothecaryProvider(output, lookup));
		evt.addProvider(new RunicAltarProvider(output, lookup));
		evt.addProvider(new TerrestrialAgglomerationProvider(output, lookup));
		evt.addProvider(new OrechidProvider(output, lookup));
		evt.addProvider(AdvancementProvider.create(output, lookup));

		evt.addProvider(new PatchouliBookProvider(output));
		if (net.neoforged.fml.ModList.get().isLoaded("curios")) {
			evt.addProvider(new CuriosSlotProvider(output, lookup));
		}
		evt.addProvider(new ThaumaturgeDataProvider(output, false));

		BotaniaModules.gatherData(evt, lookup);
	}

	@SubscribeEvent
	public static void gatherClientData(GatherDataEvent.Client evt) {
		PackOutput output = evt.getGenerator().getPackOutput();

		evt.addProvider(new BlockstateProvider(output));
		evt.addProvider(new FloatingFlowerModelProvider(output));
		evt.addProvider(new ItemModelProvider(output));
		evt.addProvider(new PottedPlantModelProvider(output));
		evt.addProvider(new BlockShapeModelProvider(output));
		evt.addProvider(new CustomItemModelProvider(output));
		evt.addProvider(new BotaniaSoundDefinitionsProvider(output));
		evt.addProvider(new BotaniaParticleDescriptionProvider(output));
		evt.addProvider(new BotaniaLanguageProvider(output));
		evt.addProvider(new BotaniaAtlasProvider(output, evt.getLookupProvider(), LibMisc.MOD_ID));
		evt.addProvider(new ThaumaturgeDataProvider(output, true));

		BotaniaModules.gatherData(evt, evt.getLookupProvider());
	}
}
