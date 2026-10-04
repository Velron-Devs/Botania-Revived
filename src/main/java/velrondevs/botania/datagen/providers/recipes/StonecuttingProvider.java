package velrondevs.botania.datagen.providers.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.lib.LibBlockNames;
import velrondevs.botania.common.lib.ResourceLocationHelper;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StonecuttingProvider extends BotaniaRecipeProvider {
	public StonecuttingProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		for (String variant : LibBlockNames.METAMORPHIC_VARIANTS) {
			registerForMetamorphic(variant, consumer);
		}

		for (String color : LibBlockNames.PAVEMENT_VARIANTS) {
			registerForPavement(color, consumer);
		}

		for (String variant : LibBlockNames.QUARTZ_VARIANTS) {
			registerForQuartz(variant, consumer);
		}

		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockSlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockStairs);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockWall);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockPolished);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockPolishedSlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockPolishedStairs);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockPolishedWall);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrick);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrickSlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrickStairs);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrickWall);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockBrickChiseled);
		stonecutting(consumer, BotaniaBlocks.livingrock, BotaniaBlocks.livingrockSlate);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockPolishedSlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockPolishedStairs);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockPolishedWall);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrick);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrickSlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrickStairs);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrickWall);
		stonecutting(consumer, BotaniaBlocks.livingrockPolished, BotaniaBlocks.livingrockBrickChiseled);
		stonecutting(consumer, BotaniaBlocks.livingrockBrick, BotaniaBlocks.livingrockBrickSlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrockBrick, BotaniaBlocks.livingrockBrickStairs);
		stonecutting(consumer, BotaniaBlocks.livingrockBrick, BotaniaBlocks.livingrockBrickWall);
		stonecutting(consumer, BotaniaBlocks.livingrockBrick, BotaniaBlocks.livingrockBrickChiseled);
		stonecutting(consumer, BotaniaBlocks.livingrockBrickMossy, BotaniaBlocks.livingrockBrickMossySlab, 2);
		stonecutting(consumer, BotaniaBlocks.livingrockBrickMossy, BotaniaBlocks.livingrockBrickMossyStairs);
		stonecutting(consumer, BotaniaBlocks.livingrockBrickMossy, BotaniaBlocks.livingrockBrickMossyWall);
		stonecutting(consumer, BotaniaBlocks.shimmerrock, BotaniaBlocks.shimmerrockSlab, 2);
		stonecutting(consumer, BotaniaBlocks.shimmerrock, BotaniaBlocks.shimmerrockStairs);

		stonecutting(consumer, BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaSlab, 2);
		stonecutting(consumer, BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaStairs);
		stonecutting(consumer, BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrick);
		stonecutting(consumer, BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrickSlab, 2);
		stonecutting(consumer, BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrickStairs);
		stonecutting(consumer, BotaniaBlocks.corporeaBlock, BotaniaBlocks.corporeaBrickWall);
		stonecutting(consumer, BotaniaBlocks.corporeaBrick, BotaniaBlocks.corporeaBrickSlab, 2);
		stonecutting(consumer, BotaniaBlocks.corporeaBrick, BotaniaBlocks.corporeaBrickStairs);
		stonecutting(consumer, BotaniaBlocks.corporeaBrick, BotaniaBlocks.corporeaBrickWall);

		List<Item> allAzulejos = IntStream.range(0, 16).mapToObj(i -> "azulejo_" + i)
				.map(ResourceLocationHelper::prefix)
				.map(BuiltInRegistries.ITEM::get)
				.collect(Collectors.toList());
		for (Item azulejo : allAzulejos) {
			anyToAnyStonecutting(consumer, allAzulejos, azulejo);
		}
	}

	private void registerForQuartz(String variant, RecipeOutput consumer) {
		Block base = BuiltInRegistries.BLOCK.get(prefix(variant));
		Block slab = BuiltInRegistries.BLOCK.get(prefix(variant + LibBlockNames.SLAB_SUFFIX));
		Block stairs = BuiltInRegistries.BLOCK.get(prefix(variant + LibBlockNames.STAIR_SUFFIX));
		Block chiseled = BuiltInRegistries.BLOCK.get(prefix("chiseled_" + variant));
		Block pillar = BuiltInRegistries.BLOCK.get(prefix(variant + "_pillar"));
		stonecutting(consumer, base, slab, 2);
		stonecutting(consumer, base, stairs);
		stonecutting(consumer, base, chiseled);
		stonecutting(consumer, base, pillar);
	}

	private void registerForPavement(String color, RecipeOutput consumer) {
		Block base = BuiltInRegistries.BLOCK.get(prefix(color + LibBlockNames.PAVEMENT_SUFFIX));
		Block slab = BuiltInRegistries.BLOCK.get(prefix(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.SLAB_SUFFIX));
		Block stair = BuiltInRegistries.BLOCK.get(prefix(color + LibBlockNames.PAVEMENT_SUFFIX + LibBlockNames.STAIR_SUFFIX));
		stonecutting(consumer, base, slab, 2);
		stonecutting(consumer, base, stair);
	}

	private void registerForMetamorphic(String variant, RecipeOutput consumer) {
		Block base = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone"));
		Block slab = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.SLAB_SUFFIX));
		Block stair = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.STAIR_SUFFIX));
		Block wall = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_stone" + LibBlockNames.WALL_SUFFIX));
		Block brick = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks"));
		Block brickSlab = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.SLAB_SUFFIX));
		Block brickStair = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.STAIR_SUFFIX));
		Block brickWall = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks" + LibBlockNames.WALL_SUFFIX));
		Block chiseledBrick = BuiltInRegistries.BLOCK.get(prefix("chiseled_" + LibBlockNames.METAMORPHIC_PREFIX + variant + "_bricks"));
		Block cobble = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone"));
		Block cobbleSlab = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.SLAB_SUFFIX));
		Block cobbleStair = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.STAIR_SUFFIX));
		Block cobbleWall = BuiltInRegistries.BLOCK.get(prefix(LibBlockNames.METAMORPHIC_PREFIX + variant + "_cobblestone" + LibBlockNames.WALL_SUFFIX));

		stonecutting(consumer, base, slab, 2);
		stonecutting(consumer, base, stair);
		stonecutting(consumer, base, wall);
		stonecutting(consumer, base, brick);
		stonecutting(consumer, base, brickSlab, 2);
		stonecutting(consumer, base, brickStair);
		stonecutting(consumer, base, brickWall);
		stonecutting(consumer, base, chiseledBrick);

		stonecutting(consumer, brick, brickSlab, 2);
		stonecutting(consumer, brick, brickStair);
		stonecutting(consumer, brick, brickWall);
		stonecutting(consumer, brick, chiseledBrick);

		stonecutting(consumer, cobble, cobbleSlab, 2);
		stonecutting(consumer, cobble, cobbleStair);
		stonecutting(consumer, cobble, cobbleWall);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania stonecutting recipes";
	}

	protected ResourceLocation idFor(ItemLike a, ItemLike b) {
		ResourceLocation aId = BuiltInRegistries.ITEM.getKey(a.asItem());
		ResourceLocation bId = BuiltInRegistries.ITEM.getKey(b.asItem());
		return prefix("stonecutting/" + aId.getPath() + "_to_" + bId.getPath());
	}

	protected void stonecutting(RecipeOutput consumer, ItemLike input, ItemLike output) {
		stonecutting(consumer, input, output, 1);
	}

	protected void stonecutting(RecipeOutput consumer, ItemLike input, ItemLike output, int count) {
		consumer.accept(idFor(input, output), new StonecutterRecipe("", Ingredient.of(input), new ItemStack(output, count)), null);
	}

	protected void anyToAnyStonecutting(RecipeOutput consumer, List<? extends ItemLike> inputs, ItemLike output) {
		Ingredient input = Ingredient.of(inputs.stream().filter(obj -> output != obj).toArray(ItemLike[]::new));
		consumer.accept(prefix("stonecutting/" + BuiltInRegistries.ITEM.getKey(output.asItem()).getPath()), new StonecutterRecipe("", input, new ItemStack(output)), null);
	}

	protected ResourceLocation prefix(String path) {
		return ResourceLocationHelper.prefix(path);
	}
}
