package velrondevs.botania.client.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;

import org.apache.commons.lang3.ObjectUtils;
import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.*;
import velrondevs.botania.client.core.handler.CorporeaInputHandler;
import velrondevs.botania.client.gui.crafting.AssemblyHaloContainer;
import velrondevs.botania.client.integration.jei.crafting.AncientWillRecipeWrapper;
import velrondevs.botania.client.integration.jei.crafting.CompositeLensRecipeWrapper;
import velrondevs.botania.client.integration.jei.crafting.TerraShattererTippingRecipeWrapper;
import velrondevs.botania.client.integration.jei.orechid.MarimorphosisRecipeCategory;
import velrondevs.botania.client.integration.jei.orechid.OrechidIgnemRecipeCategory;
import velrondevs.botania.client.integration.jei.orechid.OrechidRecipeCategory;
import velrondevs.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import velrondevs.botania.common.crafting.LexiconElvenTradeRecipe;
import velrondevs.botania.common.crafting.recipe.AncientWillRecipe;
import velrondevs.botania.common.crafting.recipe.CompositeLensRecipe;
import velrondevs.botania.common.crafting.recipe.TerraShattererTippingRecipe;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@JeiPlugin
public class JEIBotaniaPlugin implements IModPlugin {
	private static final ResourceLocation ID = prefix("main");

	@Override
	public void registerItemSubtypes(@NotNull ISubtypeRegistration registry) {
		BotaniaSubtypes.interpreters().forEach((item, fn) -> registry.registerSubtypeInterpreter(item, new ISubtypeInterpreter<ItemStack>() {
			@Override
			public Object getSubtypeData(ItemStack stack, UidContext ctx) {
				return fn.apply(stack, ctx == UidContext.Recipe);
			}

			@Override
			public String getLegacyStringSubtypeInfo(ItemStack stack, UidContext ctx) {
				return fn.apply(stack, ctx == UidContext.Recipe);
			}
		}));
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(
				new PureDaisyRecipeCategory(registry.getJeiHelpers().getGuiHelper(), registry.getJeiHelpers().getPlatformFluidHelper()),
				new ManaPoolRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new PetalApothecaryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new RunicAltarRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new ElvenTradeRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new BreweryRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new OrechidIgnemRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new MarimorphosisRecipeCategory(registry.getJeiHelpers().getGuiHelper()),
				new TerrestrialAgglomerationRecipeCategory(registry.getJeiHelpers().getGuiHelper())
		);
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
		registration.getCraftingCategory().addExtension(AncientWillRecipe.class, new AncientWillRecipeWrapper());
		registration.getCraftingCategory().addExtension(TerraShattererTippingRecipe.class, new TerraShattererTippingRecipeWrapper());
		registration.getCraftingCategory().addExtension(CompositeLensRecipe.class, new CompositeLensRecipeWrapper());
	}

	@Override
	public void registerRecipes(@NotNull IRecipeRegistration registry) {
		registry.addRecipes(BreweryRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.BREW_TYPE, BY_ID));
		registry.addRecipes(PureDaisyRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.PURE_DAISY_TYPE, BY_ID));
		registry.addRecipes(PetalApothecaryRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.PETAL_TYPE, BY_ID));
		registry.addRecipes(ElvenTradeRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.ELVEN_TRADE_TYPE, BY_ID));
		registry.addRecipes(RunicAltarRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.RUNE_TYPE, BY_ID));
		registry.addRecipes(ManaPoolRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.MANA_INFUSION_TYPE, BY_CATALYST.thenComparing(BY_GROUP).thenComparing(BY_ID)));
		registry.addRecipes(TerrestrialAgglomerationRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.TERRA_PLATE_TYPE, BY_ID));

		Comparator<RecipeHolder<? extends OrechidRecipe>> comp = BY_WEIGHT.thenComparing(BY_ID);
		registry.addRecipes(OrechidRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.ORECHID_TYPE, comp));
		registry.addRecipes(OrechidIgnemRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.ORECHID_IGNEM_TYPE, comp));
		registry.addRecipes(MarimorphosisRecipeCategory.TYPE, sortRecipes(BotaniaRecipeTypes.MARIMORPHOSIS_TYPE, comp));
	}

	private static final Comparator<RecipeHolder<? extends Recipe<?>>> BY_ID = Comparator.comparing(RecipeHolder::id);
	private static final Comparator<RecipeHolder<? extends Recipe<?>>> BY_GROUP = Comparator.comparing(holder -> holder.value().getGroup());
	private static final Comparator<RecipeHolder<? extends OrechidRecipe>> BY_WEIGHT = Comparator.<RecipeHolder<? extends OrechidRecipe>, Integer>comparing(holder -> holder.value().getWeight()).reversed();
	private static final Comparator<RecipeHolder<ManaInfusionRecipe>> BY_CATALYST = (l, r) -> {
		StateIngredient left = l.value().getRecipeCatalyst();
		StateIngredient right = r.value().getRecipeCatalyst();
		if (left == null) {
			return right == null ? 0 : -1;
		} else if (right == null) {
			return 1;
		} else {
			return left.toString().compareTo(right.toString());
		}
	};

	private static <I extends RecipeInput, T extends Recipe<I>> List<T> sortRecipes(RecipeType<T> type, Comparator<? super RecipeHolder<T>> comparator) {
		List<RecipeHolder<T>> list = new ArrayList<>(Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(type));
		list.sort(comparator);
		return list.stream().map(RecipeHolder::value).toList();
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
		registry.addRecipeTransferHandler(AssemblyHaloContainer.class, null, RecipeTypes.CRAFTING, 1, 9, 10, 36);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.brewery), BreweryRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.alfPortal), ElvenTradeRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.manaPool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.dilutedPool), ManaPoolRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.fabulousPool), ManaPoolRecipeCategory.TYPE);

		for (Block apothecary : BotaniaBlocks.ALL_APOTHECARIES) {
			registry.addRecipeCatalyst(new ItemStack(apothecary), PetalApothecaryRecipeCategory.TYPE);
		}

		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechid), OrechidRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechidFloating), OrechidRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechidIgnem), OrechidIgnemRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.orechidIgnemFloating), OrechidIgnemRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosis), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosisChibi), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosisFloating), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.marimorphosisChibiFloating), MarimorphosisRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.pureDaisy), PureDaisyRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaFlowerBlocks.pureDaisyFloating), PureDaisyRecipeCategory.TYPE);

		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.runeAltar), RunicAltarRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaBlocks.terraPlate), TerrestrialAgglomerationRecipeCategory.TYPE);
		registry.addRecipeCatalyst(new ItemStack(BotaniaItems.autocraftingHalo), RecipeTypes.CRAFTING);
		registry.addRecipeCatalyst(new ItemStack(BotaniaItems.craftingHalo), RecipeTypes.CRAFTING);
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
		IRecipeManager recipeRegistry = jeiRuntime.getRecipeManager();

		for (ElvenTradeRecipe recipe : AlfheimPortalBlockEntity.elvenTradeRecipes(Minecraft.getInstance().level)) {
			if (recipe instanceof LexiconElvenTradeRecipe) {
				continue;
			}
			List<Ingredient> inputs = recipe.getIngredients();
			List<ItemStack> outputs = recipe.getOutputs();
			if (inputs.size() == 1 && outputs.size() == 1 && recipe.containsItem(outputs.get(0))) {
				recipeRegistry.hideRecipes(ElvenTradeRecipeCategory.TYPE, List.of(recipe));
			}
		}

		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		recipeManager.byKey(prefix("petal_apothecary/daybloom_motif"))
				.ifPresent(r -> {
					if (r.value() instanceof PetalApothecaryRecipe pr) {
						recipeRegistry.hideRecipes(PetalApothecaryRecipeCategory.TYPE, List.of(pr));
					}
				});
		recipeManager.byKey(prefix("petal_apothecary/nightshade_motif"))
				.ifPresent(r -> {
					if (r.value() instanceof PetalApothecaryRecipe pr) {
						recipeRegistry.hideRecipes(PetalApothecaryRecipeCategory.TYPE, List.of(pr));
					}
				});

		HOVERED_STACK_GETTER.jeiRuntimeRef = new WeakReference<>(jeiRuntime);
		if (!CorporeaInputHandler.hoveredStackGetters.contains(HOVERED_STACK_GETTER)) {
			CorporeaInputHandler.hoveredStackGetters.add(HOVERED_STACK_GETTER);
		}
		CorporeaInputHandler.supportedGuiFilter = CorporeaInputHandler.supportedGuiFilter.or(gui -> gui instanceof IRecipesGui);
	}

	@NotNull
	@Override
	public ResourceLocation getPluginUid() {
		return ID;
	}

	private static final HoveredStackGetter HOVERED_STACK_GETTER = new HoveredStackGetter();

	private static class HoveredStackGetter implements Supplier<ItemStack> {

		WeakReference<IJeiRuntime> jeiRuntimeRef;

		@Override
		public ItemStack get() {
			var jeiRuntime = jeiRuntimeRef.get();
			if (jeiRuntime == null) {
				return ItemStack.EMPTY;
			}
			return ObjectUtils.getFirstNonNull(
					() -> jeiRuntime.getIngredientListOverlay().getIngredientUnderMouse(VanillaTypes.ITEM_STACK),
					() -> jeiRuntime.getRecipesGui().getIngredientUnderMouse(VanillaTypes.ITEM_STACK).orElse(null),
					() -> jeiRuntime.getBookmarkOverlay().getIngredientUnderMouse(VanillaTypes.ITEM_STACK),
					() -> ItemStack.EMPTY
			);
		}
	}
}
