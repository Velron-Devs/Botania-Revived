package velrondevs.botania.datagen.providers.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import velrondevs.botania.common.crafting.ElvenTradeRecipe;
import velrondevs.botania.common.crafting.LexiconElvenTradeRecipe;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ElvenTradeProvider extends BotaniaRecipeProvider {
	public ElvenTradeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		elven(consumer, id("dreamwood_log"), new ItemStack(BotaniaBlocks.dreamwoodLog), Ingredient.of(BotaniaBlocks.livingwoodLog));
		elven(consumer, id("dreamwood"), new ItemStack(BotaniaBlocks.dreamwood), Ingredient.of(BotaniaBlocks.livingwood));

		Ingredient manaDiamond = Ingredient.of(BotaniaTags.Items.GEMS_MANA_DIAMOND);
		Ingredient manaSteel = Ingredient.of(BotaniaTags.Items.INGOTS_MANASTEEL);
		elven(consumer, id("elementium"), new ItemStack(BotaniaItems.elementium), manaSteel, manaSteel);
		elven(consumer, id("elementium_block"), new ItemStack(BotaniaBlocks.elementiumBlock), Ingredient.of(BotaniaBlocks.manasteelBlock), Ingredient.of(BotaniaBlocks.manasteelBlock));

		elven(consumer, id("pixie_dust"), new ItemStack(BotaniaItems.pixieDust), Ingredient.of(BotaniaItems.manaPearl));
		elven(consumer, id("dragonstone"), new ItemStack(BotaniaItems.dragonstone), manaDiamond);
		elven(consumer, id("dragonstone_block"), new ItemStack(BotaniaBlocks.dragonstoneBlock), Ingredient.of(BotaniaBlocks.manaDiamondBlock));

		elven(consumer, id("elf_quartz"), new ItemStack(BotaniaItems.elfQuartz), Ingredient.of(Items.QUARTZ));
		elven(consumer, id("elf_glass"), new ItemStack(BotaniaBlocks.elfGlass), Ingredient.of(BotaniaBlocks.manaGlass));

		elven(consumer, id("iron_return"), new ItemStack(Items.IRON_INGOT), Ingredient.of(Items.IRON_INGOT));
		elven(consumer, id("iron_block_return"), new ItemStack(Blocks.IRON_BLOCK), Ingredient.of(Blocks.IRON_BLOCK));
		elven(consumer, id("ender_pearl_return"), new ItemStack(Items.ENDER_PEARL), Ingredient.of(Items.ENDER_PEARL));
		elven(consumer, id("diamond_return"), new ItemStack(Items.DIAMOND), Ingredient.of(Items.DIAMOND));
		elven(consumer, id("diamond_block_return"), new ItemStack(Blocks.DIAMOND_BLOCK), Ingredient.of(Blocks.DIAMOND_BLOCK));

		consumer.accept(id("lexicon_elven"), new LexiconElvenTradeRecipe(), null);
	}

	private static ResourceLocation id(String path) {
		return prefix("elven_trade/" + path);
	}

	@Override
	public String getName() {
		return "Botania elven trade recipes";
	}

	private static void elven(RecipeOutput consumer, ResourceLocation id, ItemStack output, Ingredient... inputs) {
		consumer.accept(id, new ElvenTradeRecipe(new ItemStack[] { output }, inputs), null);
	}
}
