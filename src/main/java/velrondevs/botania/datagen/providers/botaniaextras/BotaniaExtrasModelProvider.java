package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.datagen.providers.models.BlockstateProvider;
import velrondevs.botania.mixin.BlockModelGeneratorsAccessor;
import velrondevs.botania.mixin.TextureSlotAccessor;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasMagicWoods;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlock;
import velrondevs.botania.module.botaniaextras.block.IridescentLanternBlock;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaExtrasModelProvider extends BlockstateProvider {
	private static final String DIR = "botania_extras/";
	private static final ModelTemplate CUBE_ALL_TINTED = new ModelTemplate(Optional.of(prefix("block/shapes/cube_all_tinted")), Optional.empty(), TextureSlot.ALL);
	private static final ModelTemplate TINTED_COLUMN = new ModelTemplate(Optional.of(TintedShapes.COLUMN), Optional.empty(), TextureSlot.SIDE, TextureSlot.END);
	private static final ModelTemplate TINTED_SLAB = new ModelTemplate(Optional.of(TintedShapes.SLAB), Optional.empty(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);
	private static final ModelTemplate TINTED_SLAB_TOP = new ModelTemplate(Optional.of(TintedShapes.SLAB_TOP), Optional.empty(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);
	private static final ModelTemplate TINTED_STAIRS = new ModelTemplate(Optional.of(TintedShapes.STAIRS), Optional.empty(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);
	private static final ModelTemplate TINTED_STAIRS_INNER = new ModelTemplate(Optional.of(TintedShapes.STAIRS_INNER), Optional.empty(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);
	private static final ModelTemplate TINTED_STAIRS_OUTER = new ModelTemplate(Optional.of(TintedShapes.STAIRS_OUTER), Optional.empty(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP);
	private static final TextureSlot LAYER1 = TextureSlotAccessor.make("layer1");
	private static final ModelTemplate GENERATED_1 = new ModelTemplate(Optional.of(ResourceLocation.parse("item/generated")), Optional.empty(), TextureSlot.LAYER0, LAYER1);
	private static final ModelTemplate HANDHELD_2 = new ModelTemplate(Optional.of(ResourceLocation.parse("item/handheld")), Optional.empty(), TextureSlot.LAYER0, LAYER1);

	public BotaniaExtrasModelProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Extras blockstates and models";
	}

	private static ResourceLocation blockTex(String name) {
		return prefix("block/" + DIR + name);
	}

	private static ResourceLocation itemTex(String name) {
		return prefix("item/" + DIR + name);
	}

	private static ResourceLocation sharedModel(String name) {
		return prefix("block/" + DIR + name);
	}

	private static ResourceLocation blockModel(Block block) {
		ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
		return id.withPrefix("block/");
	}

	private static ResourceLocation itemModel(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem()).withPrefix("item/");
	}

	private void parentItem(ItemLike item, ResourceLocation parent) {
		modelOutput.accept(itemModel(item), new DelegatedModel(parent));
	}

	private void flatItem(ItemLike item, ResourceLocation texture) {
		ModelTemplates.FLAT_ITEM.create(itemModel(item), TextureMapping.layer0(texture), modelOutput);
	}

	private void doublePlant(Block block, ResourceLocation bottom, ResourceLocation top) {
		blockstates.add(MultiVariantGenerator.multiVariant(block).with(
				PropertyDispatch.property(DoublePlantBlock.HALF)
						.select(DoubleBlockHalf.LOWER, Variant.variant().with(VariantProperties.MODEL, bottom))
						.select(DoubleBlockHalf.UPPER, Variant.variant().with(VariantProperties.MODEL, top))));
	}

	@Override
	protected void registerStatesAndModels() {
		ResourceLocation dirtModel = CUBE_ALL_TINTED.create(sharedModel("iridescent_dirt"), TextureMapping.cube(blockTex("iridescent_dirt")), modelOutput);
		ResourceLocation grassModel = ModelTemplates.TINTED_CROSS.create(sharedModel("iridescent_grass"), TextureMapping.cross(blockTex("iridescent_grass")), modelOutput);
		ResourceLocation tallBottom = ModelTemplates.TINTED_CROSS.create(sharedModel("iridescent_tall_grass_bottom"), TextureMapping.cross(blockTex("iridescent_tall_grass_bottom")), modelOutput);
		ResourceLocation tallTop = ModelTemplates.TINTED_CROSS.create(sharedModel("iridescent_tall_grass_top"), TextureMapping.cross(blockTex("iridescent_tall_grass_top")), modelOutput);
		for (DyeColor color : DyeColor.values()) {
			Block dirt = BotaniaExtrasBlocks.getDirt(color);
			singleVariantBlockState(dirt, dirtModel);
			parentItem(dirt, dirtModel);

			Block grass = BotaniaExtrasBlocks.getGrass(color);
			singleVariantBlockState(grass, grassModel);
			flatItem(grass, blockTex("iridescent_grass"));

			Block tall = BotaniaExtrasBlocks.getTallGrass(color);
			doublePlant(tall, tallBottom, tallTop);
			flatItem(tall, blockTex("iridescent_tall_grass_top"));

			GENERATED_1.create(itemModel(BotaniaExtrasItems.getSeeds(color)), new TextureMapping()
					.put(TextureSlot.LAYER0, itemTex("iridescent_seeds"))
					.put(LAYER1, itemTex("iridescent_seeds_overlay")), modelOutput);
		}

		ResourceLocation bifrostDirtModel = ModelTemplates.CUBE_ALL.create(blockModel(BotaniaExtrasBlocks.bifrostDirt), TextureMapping.cube(blockTex("bifrost_dirt")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.bifrostDirt, bifrostDirtModel);
		parentItem(BotaniaExtrasBlocks.bifrostDirt, bifrostDirtModel);

		ResourceLocation bifrostGrassModel = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasBlocks.bifrostGrass), TextureMapping.cross(blockTex("bifrost_grass")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.bifrostGrass, bifrostGrassModel);
		flatItem(BotaniaExtrasBlocks.bifrostGrass, blockTex("bifrost_grass"));

		ResourceLocation bifrostTallBottom = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasBlocks.bifrostTallGrass).withSuffix("_bottom"), TextureMapping.cross(blockTex("bifrost_tall_grass_bottom")), modelOutput);
		ResourceLocation bifrostTallTop = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasBlocks.bifrostTallGrass).withSuffix("_top"), TextureMapping.cross(blockTex("bifrost_tall_grass_top")), modelOutput);
		doublePlant(BotaniaExtrasBlocks.bifrostTallGrass, bifrostTallBottom, bifrostTallTop);
		flatItem(BotaniaExtrasBlocks.bifrostTallGrass, blockTex("bifrost_tall_grass_top"));

		GENERATED_1.create(itemModel(BotaniaExtrasItems.bifrostSeeds), new TextureMapping()
				.put(TextureSlot.LAYER0, itemTex("iridescent_seeds"))
				.put(LAYER1, itemTex("iridescent_seeds_overlay")), modelOutput);

		ResourceLocation flowerModel = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasBlocks.bifrostFlower), TextureMapping.cross(blockTex("mystical_bifrost_flower")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.bifrostFlower, flowerModel);
		flatItem(BotaniaExtrasBlocks.bifrostFlower, blockTex("mystical_bifrost_flower"));

		ResourceLocation tallFlowerBottom = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasBlocks.tallBifrostFlower).withSuffix("_bottom"), TextureMapping.cross(blockTex("tall_mystical_bifrost_flower_bottom")), modelOutput);
		ResourceLocation tallFlowerTop = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasBlocks.tallBifrostFlower).withSuffix("_top"), TextureMapping.cross(blockTex("tall_mystical_bifrost_flower_top")), modelOutput);
		doublePlant(BotaniaExtrasBlocks.tallBifrostFlower, tallFlowerBottom, tallFlowerTop);
		flatItem(BotaniaExtrasBlocks.tallBifrostFlower, blockTex("tall_mystical_bifrost_flower_top"));

		ResourceLocation lanternModel = CUBE_ALL_TINTED.create(blockModel(BotaniaExtrasBlocks.iridescentLantern), TextureMapping.cube(blockTex("iridescent_lantern")), modelOutput);
		ResourceLocation lanternBifrostModel = ModelTemplates.CUBE_ALL.create(blockModel(BotaniaExtrasBlocks.iridescentLantern).withSuffix("_bifrost"), TextureMapping.cube(blockTex("iridescent_lantern_bifrost")), modelOutput);
		blockstates.add(MultiVariantGenerator.multiVariant(BotaniaExtrasBlocks.iridescentLantern).with(
				PropertyDispatch.property(IridescentLanternBlock.POWER).generate(power -> Variant.variant()
						.with(VariantProperties.MODEL, power >= 15 ? lanternBifrostModel : lanternModel))));
		parentItem(BotaniaExtrasBlocks.iridescentLantern, lanternModel);

		ResourceLocation starModel = ModelTemplates.PARTICLE_ONLY.create(blockModel(BotaniaExtrasBlocks.frozenStar), TextureMapping.particle(itemTex("frozen_star")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.frozenStar, starModel);
		flatItem(BotaniaExtrasItems.frozenStar, itemTex("frozen_star"));

		ResourceLocation kindlingModel = ModelTemplates.CUBE_ALL.create(blockModel(BotaniaExtrasBlocks.blazeKindling), TextureMapping.cube(blockTex("blaze_kindling")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.blazeKindling, kindlingModel);
		parentItem(BotaniaExtrasBlocks.blazeKindling, kindlingModel);

		ResourceLocation quartzSide = blockTex("shimmer_quartz_side");
		ResourceLocation quartzTop = blockTex("shimmer_quartz_top");
		ResourceLocation quartzModel = ModelTemplates.CUBE_BOTTOM_TOP.create(blockModel(BotaniaExtrasBlocks.shimmerQuartz), new TextureMapping()
				.put(TextureSlot.SIDE, quartzSide).put(TextureSlot.TOP, quartzTop).put(TextureSlot.BOTTOM, quartzTop), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.shimmerQuartz, quartzModel);
		parentItem(BotaniaExtrasBlocks.shimmerQuartz, quartzModel);

		ResourceLocation chiseledModel = ModelTemplates.CUBE_COLUMN.create(blockModel(BotaniaExtrasBlocks.shimmerQuartzChiseled),
				TextureMapping.column(blockTex("chiseled_shimmer_quartz_side"), blockTex("chiseled_shimmer_quartz_end")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.shimmerQuartzChiseled, chiseledModel);
		parentItem(BotaniaExtrasBlocks.shimmerQuartzChiseled, chiseledModel);

		ResourceLocation pillarModel = ModelTemplates.CUBE_COLUMN.create(blockModel(BotaniaExtrasBlocks.shimmerQuartzPillar),
				TextureMapping.column(blockTex("shimmer_quartz_pillar_side"), blockTex("shimmer_quartz_pillar_end")), modelOutput);
		blockstates.add(BlockModelGeneratorsAccessor.createAxisAlignedPillarBlock(BotaniaExtrasBlocks.shimmerQuartzPillar, pillarModel));
		parentItem(BotaniaExtrasBlocks.shimmerQuartzPillar, pillarModel);

		slabBlock(new HashSet<>(), BotaniaExtrasBlocks.shimmerQuartzSlab, quartzModel, quartzSide, quartzTop, quartzTop);
		parentItem(BotaniaExtrasBlocks.shimmerQuartzSlab, blockModel(BotaniaExtrasBlocks.shimmerQuartzSlab));
		stairsBlock(new HashSet<>(), BotaniaExtrasBlocks.shimmerQuartzStairs, quartzSide, quartzTop, quartzTop);
		parentItem(BotaniaExtrasBlocks.shimmerQuartzStairs, blockModel(BotaniaExtrasBlocks.shimmerQuartzStairs));

		flatItem(BotaniaExtrasItems.floralBifrostPowder, itemTex("floral_bifrost_powder"));
		flatItem(BotaniaExtrasItems.mysticalBifrostPetal, itemTex("mystical_bifrost_petal"));
		flatItem(BotaniaExtrasItems.shimmerQuartz, itemTex("quartz_shimmer"));

		woods();
		magicWoods();
		suffusion();
		tools();
		flowers();
	}

	private void flowers() {
		ModelTemplate cross = new ModelTemplate(Optional.of(prefix("block/shapes/cross")), Optional.empty(), TextureSlot.CROSS);
		ResourceLocation texture = prefix("block/crysanthermum");
		ResourceLocation model = cross.create(blockModel(BotaniaExtrasFlowers.crysanthermum), new TextureMapping().put(TextureSlot.CROSS, texture), modelOutput);
		singleVariantBlockState(BotaniaExtrasFlowers.crysanthermum, model);
		flatItem(BotaniaExtrasFlowers.crysanthermum, texture);
		singleVariantBlockState(BotaniaExtrasFlowers.crysanthermumFloating, blockModel(BotaniaExtrasFlowers.crysanthermumFloating));
		singleVariantBlockState(BotaniaExtrasFlowers.crysanthermumPotted, blockModel(BotaniaExtrasFlowers.crysanthermumPotted));
	}

	private void tools() {
		HANDHELD_2.create(itemModel(BotaniaExtrasItems.vibrantPlainsRod), new TextureMapping()
				.put(TextureSlot.LAYER0, itemTex("vibrant_plains_rod"))
				.put(LAYER1, itemTex("vibrant_plains_rod_overlay")), modelOutput);
		HANDHELD_2.create(itemModel(BotaniaExtrasItems.prismaticLakeRod), new TextureMapping()
				.put(TextureSlot.LAYER0, itemTex("prismatic_lake_rod"))
				.put(LAYER1, itemTex("prismatic_lake_rod_overlay")), modelOutput);
		ModelTemplates.FLAT_HANDHELD_ITEM.create(itemModel(BotaniaExtrasItems.thunderingPeaksRod), TextureMapping.layer0(itemTex("thundering_peaks_rod")), modelOutput);
		ModelTemplates.FLAT_HANDHELD_ITEM.create(itemModel(BotaniaExtrasItems.stormySeaRod), TextureMapping.layer0(itemTex("stormy_sea_rod")), modelOutput);
		GENERATED_1.create(itemModel(BotaniaExtrasItems.phantomFlashLens), new TextureMapping()
				.put(TextureSlot.LAYER0, prefix("item/lens"))
				.put(LAYER1, prefix("item/lens_light")), modelOutput);
		flatItem(BotaniaExtrasItems.holySymbol, itemTex("holy_symbol"));
		flatItem(BotaniaExtrasItems.priestEmblemThor, itemTex("priest_emblem_thor"));
		flatItem(BotaniaExtrasItems.priestEmblemSif, itemTex("priest_emblem_sif"));
		flatItem(BotaniaExtrasItems.priestEmblemNjord, itemTex("priest_emblem_njord"));
		flatItem(BotaniaExtrasItems.aesirEmblem, itemTex("aesir_emblem"));
		for (String name : List.of("thor", "sif", "njord", "aesir")) {
			ResourceLocation texture = itemTex(name.equals("aesir") ? "aesir_emblem_render" : "priest_emblem_" + name + "_render");
			ModelTemplates.FLAT_ITEM.create(prefix("icon/botania_extras/priest_emblem_" + name), TextureMapping.layer0(texture), modelOutput);
		}
		for (Block flash : List.of(BotaniaExtrasBlocks.rainbowManaFlash, BotaniaExtrasBlocks.phantomManaFlash)) {
			ResourceLocation model = ModelTemplates.PARTICLE_ONLY.create(blockModel(flash), TextureMapping.particle(ResourceLocation.parse("block/fire_0")), modelOutput);
			singleVariantBlockState(flash, model);
		}
	}

	private void woods() {
		TintedShapes.register(modelOutput);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			woodSet(set);
		}
		ResourceLocation saplingModel = ModelTemplates.CROSS.create(blockModel(BotaniaExtrasWoods.sapling), TextureMapping.cross(blockTex("iridescent_sapling")), modelOutput);
		singleVariantBlockState(BotaniaExtrasWoods.sapling, saplingModel);
		flatItem(BotaniaExtrasWoods.sapling, blockTex("iridescent_sapling"));
	}

	private void woodSet(BotaniaExtrasWoods.WoodSet set) {
		boolean tinted = set.kind() == BotaniaExtrasWoods.Kind.IRIDESCENT;
		woodModels(tinted ? "iridescent" : set.name(), tinted, set.log(), set.planks(), set.leaves(), set.slab(), set.stairs());
	}

	private void magicWoods() {
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			woodModels(set.name(), false, set.log(), set.planks(), set.leaves(), set.slab(), set.stairs());
			ResourceLocation saplingModel = ModelTemplates.CROSS.create(blockModel(set.sapling()), TextureMapping.cross(blockTex(set.name() + "_oak_sapling")), modelOutput);
			singleVariantBlockState(set.sapling(), saplingModel);
			flatItem(set.sapling(), blockTex(set.name() + "_oak_sapling"));
		}
		flatItem(BotaniaExtrasItems.thunderousTwig, itemTex("thunderous_twig"));
		flatItem(BotaniaExtrasItems.thunderousSplinters, itemTex("thunderous_splinters"));
		flatItem(BotaniaExtrasItems.infernalTwig, itemTex("infernal_twig"));
		flatItem(BotaniaExtrasItems.infernalSplinters, itemTex("infernal_splinters"));
		flatItem(BotaniaExtrasItems.flameLacedCharcoal, itemTex("flame_laced_charcoal"));
	}

	private void suffusion() {
		for (String name : List.of("manasteel", "terrasteel", "elementium")) {
			Block platform = switch (name) {
				case "manasteel" -> BotaniaExtrasBlocks.manasteelItemPlatform;
				case "terrasteel" -> BotaniaExtrasBlocks.terrasteelItemPlatform;
				default -> BotaniaExtrasBlocks.elementiumItemPlatform;
			};
			ResourceLocation top = blockTex(name + "_item_platform");
			TextureMapping mapping = new TextureMapping().put(TextureSlot.SIDE, blockTex(name + "_item_platform_side")).put(TextureSlot.BOTTOM, top).put(TextureSlot.TOP, top);
			ResourceLocation model = ModelTemplates.SLAB_BOTTOM.create(blockModel(platform), mapping, modelOutput);
			singleVariantBlockState(platform, model);
			parentItem(platform, model);
		}
		ResourceLocation tinted = sharedModel("iridescent_planks");
		ResourceLocation bifrost = sharedModel("bifrost_planks");
		blockstates.add(MultiVariantGenerator.multiVariant(BotaniaExtrasBlocks.dendricSuffuser).with(
				PropertyDispatch.property(DendricSuffuserBlock.COLOR).generate(index -> Variant.variant()
						.with(VariantProperties.MODEL, index == DendricSuffuserBlock.BIFROST ? bifrost : tinted))));
		ResourceLocation amplifier = ModelTemplates.CUBE_ALL.create(blockModel(BotaniaExtrasBlocks.sonicAmplifier), TextureMapping.cube(blockTex("sonic_amplifier")), modelOutput);
		singleVariantBlockState(BotaniaExtrasBlocks.sonicAmplifier, amplifier);
		parentItem(BotaniaExtrasBlocks.sonicAmplifier, amplifier);
	}

	private void woodModels(String base, boolean tinted, Block log, Block planks, Block leaves, Block slab, Block stairs) {
		ResourceLocation side = blockTex(base + "_log_side");
		ResourceLocation end = blockTex(base + "_log_top");
		ResourceLocation planksTex = blockTex(base + "_planks");
		ResourceLocation leavesTex = blockTex(base + "_leaves");

		ResourceLocation logModel = (tinted ? TINTED_COLUMN : ModelTemplates.CUBE_COLUMN).create(sharedModel(base + "_log"), TextureMapping.column(side, end), modelOutput);
		ResourceLocation planksModel = (tinted ? CUBE_ALL_TINTED : ModelTemplates.CUBE_ALL).create(sharedModel(base + "_planks"), TextureMapping.cube(planksTex), modelOutput);
		ResourceLocation leavesModel = (tinted ? CUBE_ALL_TINTED : ModelTemplates.CUBE_ALL).create(sharedModel(base + "_leaves"), TextureMapping.cube(leavesTex), modelOutput);
		TextureMapping planksMapping = new TextureMapping().put(TextureSlot.SIDE, planksTex).put(TextureSlot.BOTTOM, planksTex).put(TextureSlot.TOP, planksTex);
		ResourceLocation slabModel = (tinted ? TINTED_SLAB : ModelTemplates.SLAB_BOTTOM).create(sharedModel(base + "_slab"), planksMapping, modelOutput);
		ResourceLocation slabTopModel = (tinted ? TINTED_SLAB_TOP : ModelTemplates.SLAB_TOP).create(sharedModel(base + "_slab_top"), planksMapping, modelOutput);
		ResourceLocation stairsModel = (tinted ? TINTED_STAIRS : ModelTemplates.STAIRS_STRAIGHT).create(sharedModel(base + "_stairs"), planksMapping, modelOutput);
		ResourceLocation stairsInner = (tinted ? TINTED_STAIRS_INNER : ModelTemplates.STAIRS_INNER).create(sharedModel(base + "_stairs_inner"), planksMapping, modelOutput);
		ResourceLocation stairsOuter = (tinted ? TINTED_STAIRS_OUTER : ModelTemplates.STAIRS_OUTER).create(sharedModel(base + "_stairs_outer"), planksMapping, modelOutput);

		blockstates.add(BlockModelGeneratorsAccessor.createAxisAlignedPillarBlock(log, logModel));
		singleVariantBlockState(planks, planksModel);
		singleVariantBlockState(leaves, leavesModel);
		slabBlockWithModels(new HashSet<>(), slab, slabModel, slabTopModel, planksModel);
		stairsBlockWithModels(new HashSet<>(), stairs, stairsInner, stairsInner, stairsModel, stairsModel, stairsOuter, stairsOuter);

		parentItem(log, logModel);
		parentItem(planks, planksModel);
		parentItem(leaves, leavesModel);
		parentItem(slab, slabModel);
		parentItem(stairs, stairsModel);
	}
}
