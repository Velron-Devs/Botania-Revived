package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.datagen.providers.recipes.BotaniaRecipeProvider;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.ColorizerDyeRecipe;
import velrondevs.botania.module.botaniaextras.item.CoatOfArmsItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaExtrasUtilityRecipeProvider extends BotaniaRecipeProvider {
	private static final String DIR = BotaniaExtrasModule.ID + "/";

	public BotaniaExtrasUtilityRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania Extras utility recipes";
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

	@Override
	protected void buildRecipes(RecipeOutput unconditional) {
		RecipeOutput output = unconditional.withConditions(BotaniaExtrasModule.INSTANCE.enabledCondition());

		ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, BotaniaExtrasUtilities.livingwoodFunnel)
				.define('L', BotaniaTags.Items.LIVINGWOOD_LOGS)
				.define('C', Blocks.CHEST)
				.pattern("L L")
				.pattern("LCL")
				.pattern(" L ")
				.unlockedBy("has_item", has(Blocks.CHEST))
				.save(output, id(name(BotaniaExtrasUtilities.livingwoodFunnel)));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaExtrasUtilities.toolbelt)
				.define('C', Blocks.CHEST)
				.define('L', Items.LEATHER)
				.define('P', BotaniaItems.pixieDust)
				.define('S', BotaniaItems.runeSloth)
				.pattern("CL ")
				.pattern("L L")
				.pattern("PLS")
				.unlockedBy("has_item", has(BotaniaItems.pixieDust))
				.save(output, id(name(BotaniaExtrasUtilities.toolbelt)));

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BotaniaExtrasUtilities.clericalColorizer)
				.define('P', BotaniaItems.lensPaint)
				.define('E', BotaniaTags.Items.INGOTS_ELEMENTIUM)
				.pattern("PE ")
				.pattern("E E")
				.pattern(" E ")
				.unlockedBy("has_item", has(BotaniaItems.lensPaint))
				.save(output, id(name(BotaniaExtrasUtilities.clericalColorizer)));
		SpecialRecipeBuilder.special(ColorizerDyeRecipe::new).save(output, id("dynamic/clerical_colorizer_dye").toString());

		for (int i = 0; i <= CoatOfArmsItem.FABULOUS; i++) {
			ItemLike leaves = BotaniaExtrasWoods.sets().get(i).leaves();
			ItemLike coat = BotaniaExtrasUtilities.coats().get(i);
			ShapedRecipeBuilder.shaped(RecipeCategory.MISC, coat)
					.define('L', leaves)
					.define('S', BotaniaItems.manaString)
					.pattern("LLL")
					.pattern("LSL")
					.pattern("LLL")
					.group("botania:coat_of_arms")
					.unlockedBy("has_item", has(leaves))
					.save(output, id(name(coat)));
		}
	}
}
