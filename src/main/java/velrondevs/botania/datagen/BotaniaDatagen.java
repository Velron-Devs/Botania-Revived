package velrondevs.botania.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.datagen.providers.BotaniaAtlasProvider;
import velrondevs.botania.datagen.providers.DatapackRegistryProvider;
import velrondevs.botania.datagen.providers.LooniumStructureConfigurationProvider;
import velrondevs.botania.datagen.providers.advancements.AdvancementProvider;
import velrondevs.botania.datagen.providers.integration.BotanyPotsDataProvider;
import velrondevs.botania.datagen.providers.integration.CuriosSlotProvider;
import velrondevs.botania.datagen.providers.integration.PatchouliBookProvider;
import velrondevs.botania.datagen.providers.integration.QuarkAttributeTooltipProvider;
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

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LibMisc.MOD_ID)
public class BotaniaDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent evt) {
		DataGenerator generator = evt.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper fileHelper = evt.getExistingFileHelper();
		ExistingFileHelper disabledHelper = new ExistingFileHelper(Collections.emptyList(), Collections.emptySet(), false, null, null);
		boolean server = evt.includeServer();
		boolean client = evt.includeClient();

		DatapackRegistryProvider datapackRegistries = generator.addProvider(server, new DatapackRegistryProvider(output, evt.getLookupProvider()));
		CompletableFuture<HolderLookup.Provider> lookup = datapackRegistries.getRegistryProvider();

		generator.addProvider(server, new BotaniaLootTableProvider(output, lookup));
		generator.addProvider(server, new LooniumStructureConfigurationProvider(output, lookup));

		BlockTagProvider blockTagProvider = generator.addProvider(server, new BlockTagProvider(output, lookup));
		generator.addProvider(server, new ItemTagProvider(output, lookup, blockTagProvider.contentsGetter()));
		generator.addProvider(server, new EntityTagProvider(output, lookup));
		generator.addProvider(server, new BannerPatternTagsProvider(output, lookup));
		generator.addProvider(server, new BiomeTagProvider(output, lookup));
		generator.addProvider(server, new DamageTypeTagProvider(output, lookup));

		ConventionBlockTagProvider conventionBlockTagProvider = generator.addProvider(server, new ConventionBlockTagProvider(output, lookup, disabledHelper));
		generator.addProvider(server, new ConventionItemTagProvider(output, lookup, conventionBlockTagProvider.contentsGetter(), disabledHelper));

		generator.addProvider(server, new StonecuttingProvider(output, lookup));
		generator.addProvider(server, new CraftingRecipeProvider(output, lookup));
		generator.addProvider(server, new ConventionRecipeProvider(output, lookup));
		generator.addProvider(server, new SmeltingProvider(output, lookup));
		generator.addProvider(server, new ElvenTradeProvider(output, lookup));
		generator.addProvider(server, new ManaInfusionProvider(output, lookup));
		generator.addProvider(server, new PureDaisyProvider(output, lookup));
		generator.addProvider(server, new BrewProvider(output, lookup));
		generator.addProvider(server, new PetalApothecaryProvider(output, lookup));
		generator.addProvider(server, new RunicAltarProvider(output, lookup));
		generator.addProvider(server, new TerrestrialAgglomerationProvider(output, lookup));
		generator.addProvider(server, new OrechidProvider(output, lookup));
		generator.addProvider(server, AdvancementProvider.create(output, lookup, fileHelper));

		generator.addProvider(server, new CuriosSlotProvider(output, fileHelper, lookup));
		generator.addProvider(server, new PatchouliBookProvider(output));
		generator.addProvider(server, new BotanyPotsDataProvider(output));

		generator.addProvider(client, new BlockstateProvider(output));
		generator.addProvider(client, new FloatingFlowerModelProvider(output));
		generator.addProvider(client, new ItemModelProvider(output));
		generator.addProvider(client, new PottedPlantModelProvider(output));
		generator.addProvider(client, new BlockShapeModelProvider(output, fileHelper));
		generator.addProvider(client, new CustomItemModelProvider(output, fileHelper));
		generator.addProvider(client, new BotaniaSoundDefinitionsProvider(output, fileHelper));
		generator.addProvider(client, new BotaniaParticleDescriptionProvider(output, fileHelper));
		generator.addProvider(client, new QuarkAttributeTooltipProvider(output));
		generator.addProvider(client, new BotaniaLanguageProvider(output));
		generator.addProvider(client, new BotaniaAtlasProvider(output, evt.getLookupProvider(), "botania", disabledHelper));

		BotaniaModules.gatherData(evt, lookup);
	}
}
