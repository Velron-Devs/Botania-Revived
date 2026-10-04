package velrondevs.botania.datagen.providers.asgard;

import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.crafting.RecipeTerraPlate;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.datagen.providers.recipes.BotaniaRecipeProvider;
import velrondevs.botania.module.asgard.AsgardFlowers;
import velrondevs.botania.module.asgard.AsgardModule;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class AsgardRecipeProvider extends BotaniaRecipeProvider {
	public static final int COST_ASGARDANDELION = ManaPoolBlockEntity.MAX_MANA;

	public AsgardRecipeProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania Asgard recipes";
	}

	private static ResourceLocation id(String path) {
		return prefix(AsgardModule.ID + "/" + path);
	}

	private static Criterion<InventoryChangeTrigger.TriggerInstance> has(ItemLike item) {
		return InventoryChangeTrigger.TriggerInstance.hasItems(item);
	}

	private static void add(List<Ingredient> list, int count, ItemLike item) {
		for (int i = 0; i < count; i++) {
			list.add(Ingredient.of(item));
		}
	}

	@Override
	protected void buildRecipes(RecipeOutput unconditional) {
		RecipeOutput output = unconditional.withConditions(AsgardModule.INSTANCE.enabledCondition());

		List<Ingredient> inputs = new ArrayList<>();
		inputs.add(Ingredient.of(BotaniaFlowerBlocks.witherManaRose));
		inputs.add(Ingredient.of(Items.DANDELION));
		add(inputs, 8, BotaniaItems.lifeEssence);
		add(inputs, 4, BotaniaBlocks.terrasteelBlock);
		add(inputs, 4, BotaniaBlocks.dragonstoneBlock);
		output.accept(id("terra_plate/asgardandelion"),
				new RecipeTerraPlate(COST_ASGARDANDELION, NonNullList.of(Ingredient.EMPTY, inputs.toArray(new Ingredient[0])), new ItemStack(AsgardFlowers.asgardandelion)), null);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, AsgardFlowers.asgardandelionFloating)
				.requires(BotaniaTags.Items.FLOATING_FLOWERS)
				.requires(AsgardFlowers.asgardandelion)
				.group("botania:floating_flower")
				.unlockedBy("has_item", has(AsgardFlowers.asgardandelion))
				.save(output, id("floating_asgardandelion"));
	}
}
