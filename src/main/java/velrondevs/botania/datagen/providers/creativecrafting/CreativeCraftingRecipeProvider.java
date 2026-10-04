package velrondevs.botania.datagen.providers.creativecrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.crafting.RecipeTerraPlate;
import velrondevs.botania.datagen.providers.recipes.BotaniaRecipeProvider;
import velrondevs.botania.module.creativecrafting.CreativeCraftingModule;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CreativeCraftingRecipeProvider extends BotaniaRecipeProvider {
	public static final int COST_CREATIVE_POOL = ManaPoolBlockEntity.MAX_MANA;
	public static final int COST_CREATIVE_TABLET = ManaPoolBlockEntity.MAX_MANA;
	public static final int COST_CREATIVE_SPARK = ManaPoolBlockEntity.MAX_MANA / 2;
	public static final int COST_INFRANGIBLE_PLATFORM = ManaPoolBlockEntity.MAX_MANA / 2;

	public CreativeCraftingRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania Creative Crafting recipes";
	}

	private static ResourceLocation id(String name) {
		return prefix(CreativeCraftingModule.ID + "/terra_plate/" + name);
	}

	private static Ingredient of(ItemLike item) {
		return Ingredient.of(item);
	}

	private static void add(List<Ingredient> list, int count, ItemLike item) {
		for (int i = 0; i < count; i++) {
			list.add(of(item));
		}
	}

	private static NonNullList<Ingredient> list(List<Ingredient> ingredients) {
		return NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0]));
	}

	@Override
	protected void buildRecipes(RecipeOutput unconditional) {
		RecipeOutput output = unconditional.withConditions(CreativeCraftingModule.INSTANCE.enabledCondition());

		List<Ingredient> pool = new ArrayList<>();
		pool.add(of(BotaniaBlocks.fabulousPool));
		pool.add(of(BotaniaItems.manaTablet));
		add(pool, 4, BotaniaBlocks.dragonstoneBlock);
		add(pool, 4, BotaniaItems.lifeEssence);
		output.accept(id("creative_pool"), new RecipeTerraPlate(COST_CREATIVE_POOL, list(pool), new ItemStack(BotaniaBlocks.creativePool)), null);

		List<Ingredient> tablet = new ArrayList<>();
		tablet.add(of(BotaniaItems.manaTablet));
		for (ItemLike rune : List.of(BotaniaItems.runeLust, BotaniaItems.runeGluttony, BotaniaItems.runeGreed, BotaniaItems.runeSloth,
				BotaniaItems.runeWrath, BotaniaItems.runeEnvy, BotaniaItems.runePride)) {
			tablet.add(of(rune));
		}
		add(tablet, 4, BotaniaBlocks.terrasteelBlock);
		add(tablet, 4, BotaniaItems.lifeEssence);
		output.accept(id("creative_mana_tablet"), new RecipeTerraPlate(COST_CREATIVE_TABLET, list(tablet), CreativeCraftingModule.creativeTablet()), null);

		List<Ingredient> spark = new ArrayList<>();
		spark.add(of(BotaniaItems.corporeaSparkMaster));
		spark.add(of(BotaniaItems.runeMana));
		add(spark, 4, BotaniaBlocks.elementiumBlock);
		add(spark, 4, BotaniaItems.pixieDust);
		add(spark, 4, BotaniaItems.lifeEssence);
		output.accept(id("creative_corporea_spark"), new RecipeTerraPlate(COST_CREATIVE_SPARK, list(spark), new ItemStack(BotaniaItems.corporeaSparkCreative)), null);

		List<Ingredient> platform = new ArrayList<>();
		add(platform, 4, BotaniaBlocks.abstrusePlatform);
		platform.add(of(BotaniaBlocks.dragonstoneBlock));
		add(platform, 4, BotaniaItems.terrasteel);
		output.accept(id("infrangible_platform"), new RecipeTerraPlate(COST_INFRANGIBLE_PLATFORM, list(platform), new ItemStack(BotaniaBlocks.infrangiblePlatform, 4)), null);
	}
}
