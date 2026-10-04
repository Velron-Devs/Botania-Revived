package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasRecipeTypes {
	public static final ResourceLocation TREE_SUFFUSION_ID = prefix("tree_suffusion");

	public static final RecipeType<TreeSuffusionRecipe> TREE_SUFFUSION_TYPE = new RecipeType<>() {
		@Override
		public String toString() {
			return BuiltInRegistries.RECIPE_TYPE.getKey(this).toString();
		}
	};
	public static final RecipeSerializer<TreeSuffusionRecipe> TREE_SUFFUSION_SERIALIZER = new TreeSuffusionRecipe.Serializer();

	private BotaniaExtrasRecipeTypes() {}
}
