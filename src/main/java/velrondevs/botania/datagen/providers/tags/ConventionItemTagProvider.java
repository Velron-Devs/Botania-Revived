package velrondevs.botania.datagen.providers.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.BotaniaModules;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.registry.BotaniaItems.*;

public class ConventionItemTagProvider extends IntrinsicHolderTagsProvider<Item> {
	public static final TagKey<Item> STORAGE_BLOCKS_QUARTZ = c("storage_blocks/quartz");

	private final CompletableFuture<TagsProvider.TagLookup<Block>> blockTags;
	private final Map<TagKey<Block>, TagKey<Item>> tagsToCopy = new LinkedHashMap<>();

	public ConventionItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider,
			CompletableFuture<TagsProvider.TagLookup<Item>> parent, CompletableFuture<TagsProvider.TagLookup<Block>> blockTags) {
		super(packOutput, Registries.ITEM, lookupProvider, parent, item -> item.builtInRegistryHolder().key(), LibMisc.MOD_ID);
		this.blockTags = blockTags;
	}

	protected void copy(TagKey<Block> blockTag, TagKey<Item> itemTag) {
		this.tagsToCopy.put(blockTag, itemTag);
	}

	@Override
	protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
		return super.createContentsProvider().thenCombine(this.blockTags, (provider, lookup) -> {
			this.tagsToCopy.forEach((fromBlockTag, toItemTag) -> {
				TagBuilder toBuilder = this.getOrCreateRawBuilder(toItemTag);
				TagBuilder fromBuilder = lookup.apply(fromBlockTag).orElseThrow(() -> new IllegalStateException("Missing block tag " + fromBlockTag.location()));
				fromBuilder.build().forEach(toBuilder::add);
				fromBuilder.getRemoveEntries().forEach(toBuilder::remove);
			});
			return provider;
		});
	}

	@Override
	public String getName() {
		return "Botania item tags (convention)";
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(c("dusts/mana")).addTag(BotaniaTags.Items.DUSTS_MANA);
		this.tag(Tags.Items.DUSTS).addTag(c("dusts/mana"));

		this.tag(c("gems/dragonstone")).addTag(BotaniaTags.Items.GEMS_DRAGONSTONE);
		this.tag(c("gems/mana_diamond")).addTag(BotaniaTags.Items.GEMS_MANA_DIAMOND);
		this.tag(Tags.Items.GEMS).addTag(c("gems/dragonstone")).addTag(c("gems/mana_diamond"));

		this.tag(c("ingots/elementium")).addTag(BotaniaTags.Items.INGOTS_ELEMENTIUM);
		this.tag(c("ingots/manasteel")).addTag(BotaniaTags.Items.INGOTS_MANASTEEL);
		this.tag(c("ingots/terrasteel")).addTag(BotaniaTags.Items.INGOTS_TERRASTEEL);
		this.tag(Tags.Items.INGOTS).addTag(c("ingots/elementium"))
				.addTag(c("ingots/manasteel"))
				.addTag(c("ingots/terrasteel"));

		this.tag(c("nuggets/elementium")).addTag(BotaniaTags.Items.NUGGETS_ELEMENTIUM);
		this.tag(c("nuggets/manasteel")).addTag(BotaniaTags.Items.NUGGETS_MANASTEEL);
		this.tag(c("nuggets/terrasteel")).addTag(BotaniaTags.Items.NUGGETS_TERRASTEEL);
		this.tag(Tags.Items.NUGGETS).addTag(c("nuggets/elementium"))
				.addTag(c("nuggets/manasteel"))
				.addTag(c("nuggets/terrasteel"));

		this.copyToSameName(ConventionBlockTagProvider.ELEMENTIUM);
		this.copyToSameName(ConventionBlockTagProvider.MANASTEEL);
		this.copyToSameName(ConventionBlockTagProvider.TERRASTEEL);
		this.copyToSameName(ConventionBlockTagProvider.MANA_DIAMOND);
		this.copyToSameName(ConventionBlockTagProvider.DRAGONSTONE);
		this.copyToSameName(ConventionBlockTagProvider.BLAZE_MESH);
		ColorHelper.supportedColors().map(ConventionBlockTagProvider.PETAL_BLOCKS::get).forEach(this::copyToSameName);
		this.copy(ConventionBlockTagProvider.MUSHROOMS, Tags.Items.MUSHROOMS);
		this.copy(ConventionBlockTagProvider.STORAGE_BLOCKS_QUARTZ, STORAGE_BLOCKS_QUARTZ);
		this.copy(Tags.Blocks.STORAGE_BLOCKS, Tags.Items.STORAGE_BLOCKS);
		this.copy(Tags.Blocks.GLASS_BLOCKS, Tags.Items.GLASS_BLOCKS);
		this.copy(Tags.Blocks.GLASS_PANES, Tags.Items.GLASS_PANES);
		this.copy(Tags.Blocks.FENCES_WOODEN, Tags.Items.FENCES_WOODEN);
		this.copy(Tags.Blocks.FENCE_GATES_WOODEN, Tags.Items.FENCE_GATES_WOODEN);

		this.tag(BotaniaTags.Items.LENS_GLUE).add(Items.HONEY_BOTTLE).addOptionalTag(Tags.Items.SLIME_BALLS);

		this.generateToolTags();
		this.generateAccessoryTags();
		this.generateQuarkTags();
	}

	private void generateToolTags() {
		this.tag(Tags.Items.TOOLS_SHEAR).add(manasteelShears, elementiumShears);
		this.tag(Tags.Items.TOOLS_BOW).add(livingwoodBow, crystalBow);
	}

	private void generateAccessoryTags() {
		tag(accessory("belt")).add(
				knockbackBelt, speedUpBelt, superTravelBelt, travelBelt
		);
		tag(accessory("body")).add(
				balanceCloak, holyCloak, invisibilityCloak, thirdEye, unholyCloak
		);
		tag(accessory("charm")).add(
				divaCharm, goddessCharm, monocle, tinyPlanet
		);
		tag(accessory("head")).add(flightTiara, itemFinder);
		tag(accessory("necklace")).add(
				bloodPendant, cloudPendant, icePendant, lavaPendant,
				superCloudPendant, superLavaPendant
		);
		tag(accessory("ring")).add(
				auraRing, auraRingGreater, dodgeRing, lokiRing, magnetRing, magnetRingGreater,
				manaRing, manaRingGreater, miningRing, odinRing, pixieRing, reachRing,
				swapRing, thorRing, waterRing
		);
		BotaniaModules.addAccessoryTags(this::tag);
		tag(accessory("curio")).add(
				blackBowtie, blackTie,
				redGlasses, puffyScarf,
				engineerGoggles, eyepatch,
				wickedEyepatch, redRibbons,
				pinkFlowerBud, polkaDottedBows,
				blueButterfly, catEars,
				witchPin, devilTail,
				kamuiEye, googlyEyes,
				fourLeafClover, clockEye,
				unicornHorn, devilHorns,
				hyperPlus, botanistEmblem,
				ancientMask, eerieMask,
				alienAntenna, anaglyphGlasses,
				orangeShades, grouchoGlasses,
				thickEyebrows, lusitanicShield,
				tinyPotatoMask, questgiverMark,
				thinkingHand
		);
	}

	private void generateQuarkTags() {
		tag(quark("big_harvest_hoes")).add(elementiumHoe);
		tag(quark("reacharound_able")).add(dirtRod, cobbleRod, blackHoleTalisman);
	}

	private static TagKey<Item> accessory(String name) {
		return ItemTags.create(Identifier.fromNamespaceAndPath("curios", name));
	}

	private static TagKey<Item> c(String name) {
		return ItemTags.create(Identifier.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Item> quark(String name) {
		return ItemTags.create(Identifier.fromNamespaceAndPath("quark", name));
	}

	private void copyToSameName(TagKey<Block> source) {
		this.copy(source, ItemTags.create(source.location()));
	}
}
