package velrondevs.botania.module.botaniaextras.client.integration;

import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import velrondevs.botania.client.integration.rrv.BotaniaRrvPlugin;
import velrondevs.botania.client.integration.rrv.BotaniaRrvRecipe;
import velrondevs.botania.client.integration.rrv.BotaniaRrvRecipeType;
import velrondevs.botania.client.integration.rrv.BotaniaRrvTypes;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;

import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasRrv {
	public static final BotaniaRrvRecipeType TREE_SUFFUSION = new BotaniaRrvRecipeType(prefix("tree_suffusion"),
			"botania.nei.treeSuffusion", () -> BotaniaExtrasBlocks.terrasteelItemPlatform, 140, 148, BotaniaRrvTypes.altarLayout(0),
			BotaniaRrvRecipeType.stations(() -> BotaniaExtrasBlocks.manasteelItemPlatform, () -> BotaniaExtrasBlocks.terrasteelItemPlatform,
					() -> BotaniaExtrasBlocks.elementiumItemPlatform, () -> BotaniaExtrasWoods.sapling));

	private BotaniaExtrasRrv() {}

	public static void addRecipes(List<ReliableClientRecipe> list) {
		if (!BotaniaModules.isEnabled(BotaniaExtrasModule.ID)) {
			return;
		}
		for (RecipeHolder<TreeSuffusionRecipe> holder : BotaniaRrvPlugin.recipes(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE)) {
			TreeSuffusionRecipe recipe = holder.value();
			list.add(new BotaniaRrvRecipe(TREE_SUFFUSION, holder.id().identifier())
					.render(BotaniaRrvPlugin.texture(prefix("textures/gui/petal_overlay.png"), 21 + BotaniaRrvTypes.ALTAR_DX,
							BotaniaRrvTypes.ALTAR_DY, 85, 82, 42, 11))
					.render(BotaniaRrvPlugin.manaBar(3, 141, recipe.getMana(), ManaPoolBlockEntity.MAX_MANA / 10))
					.ringInputs(BotaniaRrvPlugin.contents(recipe.getIngredients()))
					.catalyst(BotaniaRrvTypes.ALTAR_CENTER, SlotContent.of(new ItemStack(BotaniaExtrasWoods.sapling)))
					.output(BotaniaRrvTypes.ALTAR_OUTPUT, SlotContent.of(recipe.getResultItem())));
		}
	}
}
