package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.crafting.ManaInfusionRecipe;
import velrondevs.botania.common.crafting.PureDaisyRecipe;
import velrondevs.botania.common.crafting.StateIngredientHelper;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.datagen.providers.recipes.BotaniaRecipeProvider;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasMagicWoods;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasTags;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.IridescentColors;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;
import velrondevs.botania.module.botaniaextras.item.VibrantPlainsRodItem;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaExtrasRecipeProvider extends BotaniaRecipeProvider {
	private static final String DIR = BotaniaExtrasModule.ID + "/";

	public BotaniaExtrasRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania Extras recipes";
	}

	private static ResourceLocation id(String path) {
		return prefix(DIR + path);
	}

	private static String name(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem()).getPath();
	}

	private static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(item);
	}

	private static Criterion<InventoryChangeTrigger.TriggerInstance> has(TagKey<Item> tag) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(tag).build());
	}

	private static List<DyeColor> colorsAndBifrost() {
		List<DyeColor> list = new ArrayList<>(List.of(DyeColor.values()));
		list.add(null);
		return list;
	}

	private static TagKey<Item> dyeTag(@Nullable DyeColor color) {
		return color == null ? BotaniaExtrasTags.Items.BIFROST_DYES : color.getTag();
	}

	@Override
	protected void buildRecipes(RecipeOutput unconditional) {
		RecipeOutput output = unconditional.withConditions(BotaniaExtrasModule.INSTANCE.enabledCondition());

		for (DyeColor color : colorsAndBifrost()) {
			ItemLike dirt = BotaniaExtrasBlocks.getDirt(color);
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, dirt, 8)
					.define('D', Blocks.DIRT)
					.define('P', dyeTag(color))
					.pattern("DDD")
					.pattern("DPD")
					.pattern("DDD")
					.group("botania:iridescent_dirt")
					.unlockedBy("has_item", has(dyeTag(color)))
					.save(output, id(name(dirt)));

			ItemStack star = FrozenStarItem.forDye(color);
			star.setCount(3);
			ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, star)
					.define('E', BotaniaItems.enderAirBottle)
					.define('G', Items.GLOWSTONE_DUST)
					.define('D', dyeTag(color))
					.pattern(" E ")
					.pattern("GDG")
					.pattern(" G ")
					.group("botania:frozen_star")
					.unlockedBy("has_item", has(BotaniaItems.enderAirBottle))
					.save(output, id("frozen_star_" + (color == null ? "bifrost" : color.getSerializedName())));

			output.accept(id("mana_infusion/" + name(BotaniaExtrasItems.getSeeds(color))),
					new ManaInfusionRecipe(new ItemStack(BotaniaExtrasItems.getSeeds(color)), Ingredient.of(BotaniaExtrasBlocks.getGrass(color)), 2500, "botania:iridescent_seeds", null), null);
		}

		output.accept(id("mana_infusion/mana_powder_bifrost"),
				new ManaInfusionRecipe(new ItemStack(BotaniaItems.manaPowder), Ingredient.of(BotaniaExtrasItems.floralBifrostPowder), 400, null, null), null);

		output.accept(id("pure_daisy/iridescent_dirt"), new PureDaisyRecipe(
				StateIngredientHelper.of(BotaniaExtrasTags.Blocks.IRIDESCENT_DIRT), Blocks.DIRT.defaultBlockState(), 150, Optional.empty()), null);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.redstoneRoot)
				.requires(Items.REDSTONE)
				.requires(BotaniaExtrasTags.Items.IRIDESCENT_GRASS)
				.unlockedBy("has_item", has(BotaniaExtrasTags.Items.IRIDESCENT_GRASS))
				.save(output, id("redstone_root"));

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaExtrasBlocks.iridescentLantern)
				.define('L', Blocks.REDSTONE_LAMP)
				.define('B', BotaniaExtrasTags.Items.BIFROST_DYES)
				.pattern(" B ")
				.pattern("BLB")
				.pattern(" B ")
				.unlockedBy("has_item", has(BotaniaExtrasTags.Items.BIFROST_DYES))
				.save(output, id(name(BotaniaExtrasBlocks.iridescentLantern)));

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, BotaniaExtrasBlocks.blazeKindling)
				.define('B', Items.BLAZE_POWDER)
				.define('S', BotaniaItems.manaString)
				.pattern(" S ")
				.pattern("SBS")
				.pattern(" S ")
				.unlockedBy("has_item", has(BotaniaItems.manaString))
				.save(output, id(name(BotaniaExtrasBlocks.blazeKindling)));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.fertilizer)
				.requires(Items.BONE_MEAL)
				.requires(Ingredient.of(BotaniaExtrasItems.floralBifrostPowder), 4)
				.unlockedBy("has_item", has(BotaniaExtrasItems.floralBifrostPowder))
				.save(output, id("fertilizer_bifrost"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.shimmerrock)
				.requires(BotaniaBlocks.livingrock)
				.requires(BotaniaExtrasItems.floralBifrostPowder)
				.unlockedBy("has_item", has(BotaniaExtrasItems.floralBifrostPowder))
				.save(output, id("shimmerrock"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BotaniaBlocks.shimmerwoodPlanks)
				.requires(BotaniaBlocks.dreamwoodPlanks)
				.requires(BotaniaExtrasItems.floralBifrostPowder)
				.unlockedBy("has_item", has(BotaniaExtrasItems.floralBifrostPowder))
				.save(output, id("shimmerwood_planks"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaExtrasItems.floralBifrostPowder)
				.requires(BotaniaExtrasItems.mysticalBifrostPetal)
				.unlockedBy("has_item", has(BotaniaExtrasItems.mysticalBifrostPetal))
				.save(output, id(name(BotaniaExtrasItems.floralBifrostPowder)));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaExtrasItems.mysticalBifrostPetal, 2)
				.requires(BotaniaExtrasBlocks.bifrostFlower)
				.group("botania:mystical_bifrost_petal")
				.unlockedBy("has_item", has(BotaniaExtrasBlocks.bifrostFlower))
				.save(output, id("mystical_bifrost_petal"));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaExtrasItems.mysticalBifrostPetal, 4)
				.requires(BotaniaExtrasBlocks.tallBifrostFlower)
				.group("botania:mystical_bifrost_petal")
				.unlockedBy("has_item", has(BotaniaExtrasBlocks.tallBifrostFlower))
				.save(output, id("mystical_bifrost_petal_double"));

		quartz(output);
		woods(output);
		magicWoods(output);
		suffusion(output);
		tools(output);
		BotaniaExtrasFlowerRecipes.build(output);
	}

	private void magicWoods(RecipeOutput output) {
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			String group = "botania:" + set.name() + "_planks";
			ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, set.planks(), 4)
					.requires(set.log())
					.group(group)
					.unlockedBy("has_item", has(set.log()))
					.save(output, id(name(set.planks())));
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.slab(), 6)
					.define('Q', set.planks())
					.pattern("QQQ")
					.unlockedBy("has_item", has(set.planks()))
					.save(output, id(name(set.slab())));
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.stairs(), 4)
					.define('Q', set.planks())
					.pattern("Q  ")
					.pattern("QQ ")
					.pattern("QQQ")
					.unlockedBy("has_item", has(set.planks()))
					.save(output, id(name(set.stairs())));
		}
		BotaniaExtrasMagicWoods.MagicSet thunderous = BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.THUNDEROUS);
		BotaniaExtrasMagicWoods.MagicSet infernal = BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.INFERNAL);
		BotaniaExtrasMagicWoods.MagicSet sealing = BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.SEALING);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaExtrasItems.thunderousTwig)
				.define('Q', thunderous.log())
				.pattern("Q")
				.pattern("Q")
				.unlockedBy("has_item", has(thunderous.log()))
				.save(output, id(name(BotaniaExtrasItems.thunderousTwig)));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaExtrasItems.infernalTwig)
				.define('Q', infernal.log())
				.pattern("Q")
				.pattern("Q")
				.unlockedBy("has_item", has(infernal.log()))
				.save(output, id(name(BotaniaExtrasItems.infernalTwig)));

		smelt(output, thunderous.log(), new ItemStack(Items.CHARCOAL), 0.15F, "charcoal_thunderous");
		smelt(output, sealing.log(), new ItemStack(Items.CHARCOAL), 0.15F, "charcoal_sealing");
		smelt(output, infernal.log(), new ItemStack(BotaniaExtrasItems.flameLacedCharcoal), 0.15F, "flame_laced_charcoal");
		smelt(output, thunderous.planks(), new ItemStack(BotaniaExtrasItems.thunderousSplinters, 2), 0.1F, "thunderous_splinters");
		smelt(output, infernal.planks(), new ItemStack(BotaniaExtrasItems.infernalSplinters, 2), 0.1F, "infernal_splinters");
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			smelt(output, set.log(), new ItemStack(Items.CHARCOAL), 0.15F, "charcoal_" + set.name());
		}

		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.TORCH, 6)
				.define('C', BotaniaExtrasItems.flameLacedCharcoal)
				.define('S', Items.STICK)
				.pattern("C")
				.pattern("S")
				.unlockedBy("has_item", has(BotaniaExtrasItems.flameLacedCharcoal))
				.save(output, id("torch_flame_laced"));

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaExtrasBlocks.sonicAmplifier)
				.define('N', Items.NOTE_BLOCK)
				.define('R', sealing.planks())
				.pattern(" N ")
				.pattern("NRN")
				.pattern(" N ")
				.unlockedBy("has_item", has(sealing.planks()))
				.save(output, id(name(BotaniaExtrasBlocks.sonicAmplifier)));
	}

	private void smelt(RecipeOutput output, ItemLike input, ItemStack result, float xp, String path) {
		output.accept(id("smelting/" + path), new SmeltingRecipe("", CookingBookCategory.MISC, Ingredient.of(input), result, xp, 200), null);
	}

	private void suffusion(RecipeOutput output) {
		platform(output, BotaniaExtrasBlocks.manasteelItemPlatform, BotaniaTags.Items.NUGGETS_MANASTEEL, BotaniaBlocks.livingwoodSlab);
		platform(output, BotaniaExtrasBlocks.terrasteelItemPlatform, BotaniaTags.Items.NUGGETS_TERRASTEEL, BotaniaBlocks.livingwoodSlab);
		platform(output, BotaniaExtrasBlocks.elementiumItemPlatform, BotaniaTags.Items.NUGGETS_ELEMENTIUM, BotaniaBlocks.dreamwoodSlab);

		Ingredient purpleLeaves = Ingredient.of(BotaniaExtrasWoods.sets().get(DyeColor.PURPLE.getId()).leaves());
		Ingredient redLeaves = Ingredient.of(BotaniaExtrasWoods.sets().get(DyeColor.RED.getId()).leaves());
		Ingredient whiteLeaves = Ingredient.of(BotaniaExtrasWoods.sets().get(DyeColor.WHITE.getId()).leaves());
		Ingredient manasteel = Ingredient.of(BotaniaTags.Items.INGOTS_MANASTEEL);
		Ingredient elementium = Ingredient.of(BotaniaTags.Items.INGOTS_ELEMENTIUM);

		output.accept(id("tree_suffusion/thunderous_oak"), new TreeSuffusionRecipe(
				BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.THUNDEROUS).sapling().defaultBlockState(), 50000, 350,
				List.of(manasteel, manasteel, manasteel, Ingredient.of(BotaniaItems.runeWrath), purpleLeaves, purpleLeaves, purpleLeaves, Ingredient.of(BotaniaBlocks.teruTeruBozu))), null);
		output.accept(id("tree_suffusion/infernal_oak"), new TreeSuffusionRecipe(
				BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.INFERNAL).sapling().defaultBlockState(), 10000, 70,
				List.of(Ingredient.of(Items.NETHER_BRICK), Ingredient.of(Items.NETHER_BRICK), Ingredient.of(Items.NETHER_BRICK), Ingredient.of(BotaniaItems.runeFire), redLeaves, redLeaves, redLeaves, Ingredient.of(BotaniaBlocks.blazeBlock))), null);
		output.accept(id("tree_suffusion/sealing_oak"), new TreeSuffusionRecipe(
				BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.SEALING).sapling().defaultBlockState(), 50000, 350,
				List.of(elementium, elementium, elementium, Ingredient.of(BotaniaItems.runeWinter), whiteLeaves, whiteLeaves, whiteLeaves, Ingredient.of(ItemTags.WOOL))), null);
	}

	private void platform(RecipeOutput output, ItemLike platform, TagKey<Item> nugget, ItemLike slab) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, platform)
				.define('N', nugget)
				.define('W', slab)
				.pattern("N")
				.pattern("W")
				.unlockedBy("has_item", has(slab))
				.save(output, id(name(platform)));
	}

	private void tools(RecipeOutput output) {
		for (int i = 0; i <= VibrantPlainsRodItem.BIFROST; i++) {
			DyeColor color = VibrantPlainsRodItem.dyeOf(i);
			ItemLike dirt = BotaniaExtrasBlocks.getDirt(color);
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, VibrantPlainsRodItem.forColor(i))
					.define('D', dirt)
					.define('R', BotaniaItems.skyDirtRod)
					.define('P', BotaniaItems.pixieDust)
					.define('S', BotaniaTags.Items.GEMS_DRAGONSTONE)
					.pattern(" PD")
					.pattern(" RP")
					.pattern("S  ")
					.group("botania:vibrant_plains_rod")
					.unlockedBy("has_item", has(dirt))
					.save(output, id("vibrant_plains_rod_" + IridescentColors.name(color)));
		}
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaExtrasItems.thunderingPeaksRod)
				.define('E', BotaniaExtrasItems.thunderousSplinters)
				.define('W', BotaniaItems.runeWrath)
				.define('S', BotaniaExtrasItems.thunderousTwig)
				.define('D', BotaniaTags.Items.GEMS_DRAGONSTONE)
				.pattern(" EW")
				.pattern(" SD")
				.pattern("S  ")
				.unlockedBy("has_item", has(BotaniaExtrasItems.thunderousTwig))
				.save(output, id(name(BotaniaExtrasItems.thunderingPeaksRod)));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaExtrasItems.stormySeaRod)
				.define('P', BotaniaItems.runePride)
				.define('A', BotaniaItems.runeAir)
				.define('S', BotaniaItems.tornadoRod)
				.define('D', BotaniaItems.dreamwoodTwig)
				.pattern(" AS")
				.pattern(" DA")
				.pattern("P  ")
				.unlockedBy("has_item", has(BotaniaItems.tornadoRod))
				.save(output, id(name(BotaniaExtrasItems.stormySeaRod)));
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaExtrasItems.prismaticLakeRod)
				.define('G', Blocks.GLOWSTONE)
				.define('B', BotaniaExtrasTags.Items.BIFROST_DYES)
				.define('D', BotaniaItems.dreamwoodTwig)
				.pattern(" GB")
				.pattern(" DG")
				.pattern("D  ")
				.unlockedBy("has_item", has(BotaniaExtrasTags.Items.BIFROST_DYES))
				.save(output, id(name(BotaniaExtrasItems.prismaticLakeRod)));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaExtrasItems.phantomFlashLens)
				.requires(BotaniaItems.lensLight)
				.requires(BotaniaItems.phantomInk)
				.unlockedBy("has_item", has(BotaniaItems.lensLight))
				.save(output, id(name(BotaniaExtrasItems.phantomFlashLens)));
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaItems.lensLight)
				.requires(BotaniaExtrasItems.phantomFlashLens)
				.requires(BotaniaItems.phantomInk)
				.unlockedBy("has_item", has(BotaniaExtrasItems.phantomFlashLens))
				.save(output, id("phantom_flash_lens_undo"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaExtrasItems.holySymbol)
				.define('S', BotaniaItems.manaString)
				.define('Q', Items.QUARTZ)
				.define('G', Items.GOLD_INGOT)
				.pattern("S S")
				.pattern("Q Q")
				.pattern(" G ")
				.unlockedBy("has_item", has(BotaniaItems.manaString))
				.save(output, id(name(BotaniaExtrasItems.holySymbol)));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaExtrasItems.priestEmblemThor)
				.define('E', BotaniaItems.enderAirBottle)
				.define('T', BotaniaTags.Items.NUGGETS_TERRASTEEL)
				.define('G', BotaniaItems.lifeEssence)
				.define('W', BotaniaItems.runeWrath)
				.define('A', BotaniaExtrasItems.holySymbol)
				.pattern("EGE")
				.pattern("TAT")
				.pattern(" W ")
				.unlockedBy("has_item", has(BotaniaExtrasItems.holySymbol))
				.save(output, id(name(BotaniaExtrasItems.priestEmblemThor)));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaExtrasItems.priestEmblemSif)
				.define('D', BotaniaTags.Items.GEMS_DRAGONSTONE)
				.define('N', Items.GOLD_NUGGET)
				.define('G', BotaniaItems.lifeEssence)
				.define('P', BotaniaItems.runeEarth)
				.define('A', BotaniaExtrasItems.holySymbol)
				.pattern("DGD")
				.pattern("NAN")
				.pattern(" P ")
				.unlockedBy("has_item", has(BotaniaExtrasItems.holySymbol))
				.save(output, id(name(BotaniaExtrasItems.priestEmblemSif)));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BotaniaExtrasItems.priestEmblemNjord)
				.define('R', BotaniaItems.runeAir)
				.define('N', BotaniaTags.Items.NUGGETS_MANASTEEL)
				.define('G', BotaniaItems.lifeEssence)
				.define('P', BotaniaItems.runePride)
				.define('A', BotaniaExtrasItems.holySymbol)
				.pattern("RGR")
				.pattern("NAN")
				.pattern(" P ")
				.unlockedBy("has_item", has(BotaniaExtrasItems.holySymbol))
				.save(output, id(name(BotaniaExtrasItems.priestEmblemNjord)));
	}

	private void woods(RecipeOutput output) {
		output.accept(id("pure_daisy/iridescent_sapling"), new PureDaisyRecipe(
				StateIngredientHelper.of(BlockTags.SAPLINGS), BotaniaExtrasWoods.sapling.defaultBlockState(), 150, Optional.empty()), null);

		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			String group = set.kind() == BotaniaExtrasWoods.Kind.ALT ? "botania:" + set.name() + "_planks" : "botania:" + (set.kind() == BotaniaExtrasWoods.Kind.BIFROST ? "bifrost" : "iridescent") + "_planks";
			ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, set.planks(), 4)
					.requires(set.log())
					.group(group)
					.unlockedBy("has_item", has(set.log()))
					.save(output, id(name(set.planks())));
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.slab(), 6)
					.define('Q', set.planks())
					.pattern("QQQ")
					.unlockedBy("has_item", has(set.planks()))
					.save(output, id(name(set.slab())));
			ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.stairs(), 4)
					.define('Q', set.planks())
					.pattern("Q  ")
					.pattern("QQ ")
					.pattern("QQQ")
					.unlockedBy("has_item", has(set.planks()))
					.save(output, id(name(set.stairs())));
			if (set.kind() == BotaniaExtrasWoods.Kind.IRIDESCENT) {
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, DyeItem.byColor(set.color()))
						.requires(set.leaves())
						.group("botania:leaf_dye")
						.unlockedBy("has_item", has(set.leaves()))
						.save(output, id("leaf_dye_" + set.color().getSerializedName()));
			} else if (set.kind() == BotaniaExtrasWoods.Kind.BIFROST) {
				ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BotaniaExtrasItems.floralBifrostPowder)
						.requires(set.leaves())
						.group("botania:leaf_dye")
						.unlockedBy("has_item", has(set.leaves()))
						.save(output, id("leaf_dye_bifrost"));
			}
		}
	}

	private void quartz(RecipeOutput output) {
		Item quartz = BotaniaExtrasItems.shimmerQuartz;
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, quartz, 8)
				.define('Q', Items.QUARTZ)
				.define('C', BotaniaExtrasTags.Items.BIFROST_DYES)
				.pattern("QQQ")
				.pattern("QCQ")
				.pattern("QQQ")
				.unlockedBy("has_item", has(BotaniaExtrasTags.Items.BIFROST_DYES))
				.save(output, id(name(quartz)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaExtrasBlocks.shimmerQuartz)
				.define('Q', quartz)
				.pattern("QQ")
				.pattern("QQ")
				.unlockedBy("has_item", has(quartz))
				.save(output, id(name(BotaniaExtrasBlocks.shimmerQuartz)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaExtrasBlocks.shimmerQuartzPillar, 2)
				.define('Q', BotaniaExtrasBlocks.shimmerQuartz)
				.pattern("Q")
				.pattern("Q")
				.unlockedBy("has_item", has(quartz))
				.save(output, id(name(BotaniaExtrasBlocks.shimmerQuartzPillar)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaExtrasBlocks.shimmerQuartzChiseled)
				.define('Q', BotaniaExtrasBlocks.shimmerQuartzSlab)
				.pattern("Q")
				.pattern("Q")
				.unlockedBy("has_item", has(quartz))
				.save(output, id(name(BotaniaExtrasBlocks.shimmerQuartzChiseled)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaExtrasBlocks.shimmerQuartzSlab, 6)
				.define('Q', BotaniaExtrasBlocks.shimmerQuartz)
				.pattern("QQQ")
				.unlockedBy("has_item", has(quartz))
				.save(output, id(name(BotaniaExtrasBlocks.shimmerQuartzSlab)));
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BotaniaExtrasBlocks.shimmerQuartzStairs, 4)
				.define('Q', BotaniaExtrasBlocks.shimmerQuartz)
				.pattern("Q  ")
				.pattern("QQ ")
				.pattern("QQQ")
				.unlockedBy("has_item", has(quartz))
				.save(output, id(name(BotaniaExtrasBlocks.shimmerQuartzStairs)));
		output.accept(id("mana_infusion/shimmer_quartz_deconstruct"),
				new ManaInfusionRecipe(new ItemStack(quartz, 4), Ingredient.of(BotaniaExtrasBlocks.shimmerQuartz), 25,
						"botania:block_deconstruction", StateIngredientHelper.of(BotaniaBlocks.alchemyCatalyst)), null);
	}
}
