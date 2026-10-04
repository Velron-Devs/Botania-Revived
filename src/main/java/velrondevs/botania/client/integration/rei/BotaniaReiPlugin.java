package velrondevs.botania.client.integration.rei;

import me.shedaniel.math.impl.PointHelper;
import me.shedaniel.rei.api.client.gui.screen.DisplayScreen;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.comparison.ItemComparatorRegistry;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.crafting.DefaultCustomDisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.api.item.AncientWillContainer;
import velrondevs.botania.api.recipe.BotanicalBreweryRecipe;
import velrondevs.botania.api.recipe.ElvenTradeRecipe;
import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.api.recipe.PureDaisyRecipe;
import velrondevs.botania.api.recipe.RunicAltarRecipe;
import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.api.recipe.TerrestrialAgglomerationRecipe;
import velrondevs.botania.client.core.handler.CorporeaInputHandler;
import velrondevs.botania.common.crafting.LexiconElvenTradeRecipe;
import velrondevs.botania.common.item.AncientWillItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@REIPluginClient
public class BotaniaReiPlugin implements REIClientPlugin {
	private static final Supplier<ItemStack> HOVERED_STACK_GETTER = () -> {
		Screen screen = Minecraft.getInstance().screen;
		if (screen == null) {
			return ItemStack.EMPTY;
		}
		EntryStack<?> focused = ScreenRegistry.getInstance().getFocusedStack(screen, PointHelper.ofMouse());
		if (focused != null && focused.getValue() instanceof ItemStack stack) {
			return stack;
		}
		return ItemStack.EMPTY;
	};

	public BotaniaReiPlugin() {
		if (!CorporeaInputHandler.hoveredStackGetters.contains(HOVERED_STACK_GETTER)) {
			CorporeaInputHandler.hoveredStackGetters.add(HOVERED_STACK_GETTER);
		}
		CorporeaInputHandler.supportedGuiFilter = CorporeaInputHandler.supportedGuiFilter.or(screen -> screen instanceof DisplayScreen);
	}

	@Override
	public String getPluginProviderName() {
		return "Botania";
	}

	@Override
	public void registerItemComparators(ItemComparatorRegistry registry) {
		registry.registerComponents(BotaniaItems.lexicon, BotaniaItems.brewFlask, BotaniaItems.brewVial,
				BotaniaItems.bloodPendant, BotaniaItems.incenseStick, BotaniaItems.flightTiara, BotaniaItems.laputaShard);
	}

	@Override
	public void registerCategories(CategoryRegistry registry) {
		for (BotaniaReiCategory category : BotaniaReiCategories.all()) {
			registry.add(category);
		}

		registry.addWorkstations(BuiltinPlugin.CRAFTING,
				EntryStacks.of(BotaniaItems.craftingHalo),
				EntryStacks.of(BotaniaItems.autocraftingHalo));

		for (Block apothecary : BotaniaBlocks.ALL_APOTHECARIES) {
			registry.addWorkstations(BotaniaReiCategories.PETAL_APOTHECARY, EntryStacks.of(apothecary));
		}
		registry.addWorkstations(BotaniaReiCategories.MANA_INFUSION,
				EntryStacks.of(BotaniaBlocks.manaPool),
				EntryStacks.of(BotaniaBlocks.dilutedPool),
				EntryStacks.of(BotaniaBlocks.fabulousPool));
		registry.addWorkstations(BotaniaReiCategories.RUNIC_ALTAR, EntryStacks.of(BotaniaBlocks.runeAltar));
		registry.addWorkstations(BotaniaReiCategories.TERRESTRIAL_AGGLOMERATION, EntryStacks.of(BotaniaBlocks.terraPlate));
		registry.addWorkstations(BotaniaReiCategories.ELVEN_TRADE, EntryStacks.of(BotaniaBlocks.alfPortal));
		registry.addWorkstations(BotaniaReiCategories.BOTANICAL_BREWERY, EntryStacks.of(BotaniaBlocks.brewery));
		registry.addWorkstations(BotaniaReiCategories.PURE_DAISY,
				EntryStacks.of(BotaniaFlowerBlocks.pureDaisy),
				EntryStacks.of(BotaniaFlowerBlocks.pureDaisyFloating));
		registry.addWorkstations(BotaniaReiCategories.ORECHID,
				EntryStacks.of(BotaniaFlowerBlocks.orechid),
				EntryStacks.of(BotaniaFlowerBlocks.orechidFloating));
		registry.addWorkstations(BotaniaReiCategories.ORECHID_IGNEM,
				EntryStacks.of(BotaniaFlowerBlocks.orechidIgnem),
				EntryStacks.of(BotaniaFlowerBlocks.orechidIgnemFloating));
		registry.addWorkstations(BotaniaReiCategories.MARIMORPHOSIS,
				EntryStacks.of(BotaniaFlowerBlocks.marimorphosis),
				EntryStacks.of(BotaniaFlowerBlocks.marimorphosisFloating),
				EntryStacks.of(BotaniaFlowerBlocks.marimorphosisChibi),
				EntryStacks.of(BotaniaFlowerBlocks.marimorphosisChibiFloating));
	}

	private static final Comparator<RecipeHolder<?>> BY_ID = Comparator.comparing(RecipeHolder::id);

	private static <T extends net.minecraft.world.item.crafting.Recipe<net.minecraft.world.item.crafting.RecipeInput>> List<RecipeHolder<T>> sorted(
			RecipeManager manager, net.minecraft.world.item.crafting.RecipeType<T> type, Comparator<? super RecipeHolder<T>> comparator) {
		List<RecipeHolder<T>> list = new ArrayList<>(manager.getAllRecipesFor(type));
		list.sort(comparator);
		return list;
	}

	private static String catalystKey(RecipeHolder<ManaInfusionRecipe> holder) {
		StateIngredient catalyst = holder.value().getRecipeCatalyst();
		return catalyst == null ? "" : catalyst.toString();
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		RecipeManager manager = registry.getRecipeManager();

		for (RecipeHolder<PetalApothecaryRecipe> holder : sorted(manager, BotaniaRecipeTypes.PETAL_TYPE, BY_ID)) {
			ResourceLocation id = holder.id();
			if (id.equals(prefix("petal_apothecary/daybloom_motif")) || id.equals(prefix("petal_apothecary/nightshade_motif"))) {
				continue;
			}
			registry.add(BotaniaReiDisplays.petalApothecary(holder));
		}
		for (RecipeHolder<ManaInfusionRecipe> holder : sorted(manager, BotaniaRecipeTypes.MANA_INFUSION_TYPE,
				Comparator.comparing(BotaniaReiPlugin::catalystKey)
						.thenComparing(holder -> holder.value().getGroup())
						.thenComparing(BY_ID))) {
			registry.add(BotaniaReiDisplays.manaInfusion(holder));
		}
		for (RecipeHolder<RunicAltarRecipe> holder : sorted(manager, BotaniaRecipeTypes.RUNE_TYPE, BY_ID)) {
			registry.add(BotaniaReiDisplays.runicAltar(holder));
		}
		for (RecipeHolder<TerrestrialAgglomerationRecipe> holder : sorted(manager, BotaniaRecipeTypes.TERRA_PLATE_TYPE, BY_ID)) {
			registry.add(BotaniaReiDisplays.terrestrialAgglomeration(holder));
		}
		for (RecipeHolder<ElvenTradeRecipe> holder : sorted(manager, BotaniaRecipeTypes.ELVEN_TRADE_TYPE, BY_ID)) {
			ElvenTradeRecipe recipe = holder.value();
			if (!(recipe instanceof LexiconElvenTradeRecipe)) {
				List<ItemStack> outputs = recipe.getOutputs();
				if (recipe.getIngredients().size() == 1 && outputs.size() == 1 && recipe.containsItem(outputs.get(0))) {
					continue;
				}
			}
			registry.add(BotaniaReiDisplays.elvenTrade(holder));
		}
		List<ItemStack> containers = List.of(BotaniaItems.vial, BotaniaItems.flask, BotaniaItems.incenseStick, BotaniaItems.bloodPendant)
				.stream().map(ItemStack::new).toList();
		for (RecipeHolder<BotanicalBreweryRecipe> holder : sorted(manager, BotaniaRecipeTypes.BREW_TYPE, BY_ID)) {
			for (ItemStack container : containers) {
				if (!holder.value().getOutput(container.copy()).isEmpty()) {
					registry.add(BotaniaReiDisplays.botanicalBrewery(holder, container));
				}
			}
		}
		for (RecipeHolder<PureDaisyRecipe> holder : sorted(manager, BotaniaRecipeTypes.PURE_DAISY_TYPE, BY_ID)) {
			registry.add(BotaniaReiDisplays.pureDaisy(holder));
		}

		Comparator<RecipeHolder<? extends OrechidRecipe>> byWeight =
				Comparator.<RecipeHolder<? extends OrechidRecipe>, Integer>comparing(holder -> holder.value().getWeight()).reversed();
		Comparator<RecipeHolder<? extends OrechidRecipe>> orechidOrder = byWeight.thenComparing(BY_ID);
		for (var holder : sorted(manager, BotaniaRecipeTypes.ORECHID_TYPE, orechidOrder)) {
			registry.add(BotaniaReiDisplays.orechid(BotaniaReiCategories.ORECHID, holder));
		}
		for (var holder : sorted(manager, BotaniaRecipeTypes.ORECHID_IGNEM_TYPE, orechidOrder)) {
			registry.add(BotaniaReiDisplays.orechid(BotaniaReiCategories.ORECHID_IGNEM, holder));
		}
		for (var holder : sorted(manager, BotaniaRecipeTypes.MARIMORPHOSIS_TYPE, orechidOrder)) {
			registry.add(BotaniaReiDisplays.orechid(BotaniaReiCategories.MARIMORPHOSIS, holder));
		}

		registerCraftingDisplays(registry);
	}

	private static void registerCraftingDisplays(DisplayRegistry registry) {
		Item[] wills = {
				BotaniaItems.ancientWillAhrim, BotaniaItems.ancientWillDharok, BotaniaItems.ancientWillGuthan,
				BotaniaItems.ancientWillKaril, BotaniaItems.ancientWillTorag, BotaniaItems.ancientWillVerac
		};
		for (Item will : wills) {
			ItemStack helm = new ItemStack(BotaniaItems.terrasteelHelm);
			ItemStack result = helm.copy();
			((AncientWillContainer) result.getItem()).addAncientWill(result, ((AncientWillItem) will).type);
			ResourceLocation willId = BuiltInRegistries.ITEM.getKey(will);
			registry.add(DefaultCustomDisplay.simple(
					List.of(EntryIngredients.of(helm), EntryIngredients.of(will)),
					List.of(EntryIngredients.of(result)),
					Optional.of(prefix("crafting/ancient_will/" + willId.getPath()))));
		}

		ItemStack tipped = new ItemStack(BotaniaItems.terraPick);
		TerraShattererItem.setTipped(tipped);
		registry.add(DefaultCustomDisplay.simple(
				List.of(EntryIngredients.of(BotaniaItems.terraPick), EntryIngredients.of(BotaniaItems.elementiumPick)),
				List.of(EntryIngredients.of(tipped)),
				Optional.of(prefix("crafting/terra_shatterer_tipping"))));

		List<Item> lenses = StreamSupport.stream(BuiltInRegistries.ITEM.getTagOrEmpty(BotaniaTags.Items.LENS).spliterator(), false)
				.map(holder -> holder.value())
				.filter(item -> {
					ItemStack stack = new ItemStack(item);
					return !((LensItem) item).isControlLens(stack) && ((LensItem) item).isCombinable(stack);
				})
				.toList();
		for (Item first : lenses) {
			ItemStack firstStack = new ItemStack(first);
			var firstBuilder = EntryIngredient.builder();
			var secondBuilder = EntryIngredient.builder();
			var outputBuilder = EntryIngredient.builder();
			for (Item second : lenses) {
				if (second == first) {
					continue;
				}
				ItemStack secondStack = new ItemStack(second);
				if (((LensItem) first).canCombineLenses(firstStack, secondStack)) {
					firstBuilder.add(EntryStacks.of(firstStack.copy()));
					secondBuilder.add(EntryStacks.of(secondStack.copy()));
					outputBuilder.add(EntryStacks.of(((LensItem) first).setCompositeLens(firstStack.copy(), secondStack)));
				}
			}
			EntryIngredient firstIngredient = firstBuilder.build();
			if (firstIngredient.isEmpty()) {
				continue;
			}
			ResourceLocation firstId = BuiltInRegistries.ITEM.getKey(first);
			registry.add(DefaultCustomDisplay.simple(
					List.of(firstIngredient, EntryIngredients.of(Items.SLIME_BALL), secondBuilder.build()),
					List.of(outputBuilder.build()),
					Optional.of(prefix("crafting/composite_lens/" + firstId.getPath()))));
		}
	}
}
