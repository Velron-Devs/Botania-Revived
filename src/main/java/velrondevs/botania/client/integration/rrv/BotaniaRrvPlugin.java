package velrondevs.botania.client.integration.rrv;

import cc.cassian.rrv.api.ReliableRecipeViewerClientPlugin;
import cc.cassian.rrv.api.client.RecipeScreenContext;
import cc.cassian.rrv.api.recipe.ItemView;
import cc.cassian.rrv.api.recipe.ReliableClientRecipe;
import cc.cassian.rrv.client.recipe.ClientRecipeManager;
import cc.cassian.rrv.common.builtin.crafting.CraftingClientRecipe;
import cc.cassian.rrv.common.builtin.interaction.WorldInteractionClientRecipe;
import cc.cassian.rrv.common.extra.FluidStack;
import cc.cassian.rrv.common.recipe.inventory.SlotContent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;

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
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.gui.HUDHandler;
import velrondevs.botania.client.integration.shared.OrechidUIHelper;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.crafting.LexiconElvenTradeRecipe;
import velrondevs.botania.common.helper.ItemNBTHelper;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static velrondevs.botania.client.integration.rrv.BotaniaRrvTypes.*;
import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaRrvPlugin implements ReliableRecipeViewerClientPlugin {
	private static final Identifier PURE_DAISY_TEXTURE = prefix("textures/gui/pure_daisy_overlay.png");
	private static final Identifier PETAL_TEXTURE = prefix("textures/gui/petal_overlay.png");
	private static final Identifier TERRA_TEXTURE = prefix("textures/gui/terrasteel_jei_overlay.png");
	private static final Identifier BREWERY_TEXTURE = prefix("textures/gui/nei_brewery.png");
	private static final Identifier ELVEN_TEXTURE = prefix("textures/gui/elven_trade_overlay.png");

	@Override
	public void onIntegrationInitialize() {
		ItemView.addClientRecipeProvider(BotaniaRrvPlugin::provide);
	}

	private static void provide(List<ReliableClientRecipe> list) {
		if (Minecraft.getInstance().level == null) {
			return;
		}
		addPetalApothecary(list);
		addManaInfusion(list);
		addRunicAltar(list);
		addTerraPlate(list);
		addElvenTrade(list);
		addBrewery(list);
		addPureDaisy(list);
		addOrechid(list, ORECHID, BotaniaRecipeTypes.ORECHID_TYPE, false);
		addOrechid(list, ORECHID_IGNEM, BotaniaRecipeTypes.ORECHID_IGNEM_TYPE, false);
		addOrechid(list, MARIMORPHOSIS, BotaniaRecipeTypes.MARIMORPHOSIS_TYPE, true);
		addCrafting(list);
		addWorldInteractions(list);
		velrondevs.botania.module.botaniaextras.client.integration.BotaniaExtrasRrv.addRecipes(list);
	}

	public static <T extends Recipe<RecipeInput>> List<RecipeHolder<T>> recipes(RecipeType<T> type) {
		List<RecipeHolder<T>> list = new ArrayList<>();
		try {
			list.addAll(ClientRecipeManager.INSTANCE.getRecipesForType(type));
		} catch (RuntimeException ignored) {}
		if (list.isEmpty()) {
			list.addAll(BotaniaRecipeTypes.getAllRecipesFor(null, type));
		}
		list.sort(Comparator.comparing(holder -> holder.id().identifier()));
		return list;
	}

	public static List<SlotContent> contents(List<Ingredient> ingredients) {
		return ingredients.stream().map(SlotContent::of).toList();
	}

	private static SlotContent of(ItemLike item) {
		return SlotContent.of(new ItemStack(item));
	}

	private static SlotContent blocks(StateIngredient ingredient, boolean fluids) {
		List<ItemStack> stacks = new ArrayList<>();
		for (BlockState state : ingredient.getDisplayed()) {
			if (fluids && !state.getFluidState().isEmpty()) {
				stacks.add(new FluidStack(state.getFluidState().getType()).createItemStack());
			} else {
				ItemStack stack = new ItemStack(state.getBlock());
				if (!stack.isEmpty()) {
					stacks.add(stack);
				}
			}
		}
		return SlotContent.of(stacks);
	}

	public static Consumer<RecipeScreenContext> texture(Identifier texture, int x, int y, int width, int height, int u, int v) {
		return ctx -> RenderHelper.drawTexturedModalRect(ctx.guiGraphics(), texture, x, y, u, v, width, height);
	}

	public static Consumer<RecipeScreenContext> manaBar(int x, int y, int mana, int maxMana) {
		return ctx -> HUDHandler.renderManaBar(ctx.guiGraphics(), x, y, 0x0000FF, 0.75F, mana, maxMana);
	}

	private static void addAltarRecipe(List<ReliableClientRecipe> list, BotaniaRrvRecipeType type, RecipeHolder<?> holder,
			List<Ingredient> ingredients, ItemStack output, ItemLike center, List<SlotContent> reagents, int mana) {
		BotaniaRrvRecipe recipe = new BotaniaRrvRecipe(type, holder.id().identifier())
				.render(texture(PETAL_TEXTURE, 21 + ALTAR_DX, ALTAR_DY, 85, 82, 42, 11))
				.ringInputs(contents(ingredients))
				.catalyst(ALTAR_CENTER, of(center))
				.output(ALTAR_OUTPUT, SlotContent.of(output));
		for (int i = 0; i < reagents.size(); i++) {
			recipe.input(ALTAR_REAGENT + i, reagents.get(i));
		}
		if (mana > 0) {
			recipe.render(manaBar(3, 141, mana, ManaPoolBlockEntity.MAX_MANA / 10));
		}
		list.add(recipe);
	}

	private static void addPetalApothecary(List<ReliableClientRecipe> list) {
		for (RecipeHolder<PetalApothecaryRecipe> holder : recipes(BotaniaRecipeTypes.PETAL_TYPE)) {
			Identifier id = holder.id().identifier();
			if (id.equals(prefix("petal_apothecary/daybloom_motif")) || id.equals(prefix("petal_apothecary/nightshade_motif"))) {
				continue;
			}
			PetalApothecaryRecipe recipe = holder.value();
			addAltarRecipe(list, PETAL_APOTHECARY, holder, recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY),
					BotaniaBlocks.defaultAltar, List.of(SlotContent.of(recipe.getReagent()), of(Items.WATER_BUCKET)), 0);
		}
	}

	private static void addRunicAltar(List<ReliableClientRecipe> list) {
		for (RecipeHolder<RunicAltarRecipe> holder : recipes(BotaniaRecipeTypes.RUNE_TYPE)) {
			RunicAltarRecipe recipe = holder.value();
			addAltarRecipe(list, RUNIC_ALTAR, holder, recipe.getIngredients(), recipe.getResultItem(RegistryAccess.EMPTY),
					BotaniaBlocks.runeAltar, List.of(of(BotaniaBlocks.livingrock)), recipe.getManaUsage());
		}
	}

	private static void addManaInfusion(List<ReliableClientRecipe> list) {
		ItemStack pool = new ItemStack(BotaniaBlocks.manaPool);
		ItemNBTHelper.setBoolean(pool, "RenderFull", true);
		List<BotaniaRrvRecipe> recipes = new ArrayList<>();
		for (RecipeHolder<ManaInfusionRecipe> holder : recipes(BotaniaRecipeTypes.MANA_INFUSION_TYPE)) {
			ManaInfusionRecipe recipe = holder.value();
			BotaniaRrvRecipe display = new BotaniaRrvRecipe(MANA_INFUSION, holder.id().identifier())
					.render(texture(PURE_DAISY_TEXTURE, 28, 0, 65, 44, 0, 0))
					.render(manaBar(7, 50, recipe.getManaToConsume(), ManaPoolBlockEntity.MAX_MANA / 10))
					.catalyst(INFUSION_POOL, SlotContent.of(pool))
					.output(INFUSION_OUTPUT, SlotContent.of(recipe.getResultItem(RegistryAccess.EMPTY)));
			if (!recipe.getIngredients().isEmpty()) {
				display.input(INFUSION_INPUT, SlotContent.of(recipe.getIngredients().get(0)));
			}
			StateIngredient catalyst = recipe.getRecipeCatalyst();
			String catalystKey = "";
			if (catalyst != null) {
				display.input(INFUSION_CATALYST, blocks(catalyst, false));
				catalystKey = catalyst.getDisplayed().stream().findFirst()
						.map(state -> BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString()).orElse("");
			}
			display.sort(catalystKey + "|" + recipe.group(), 0);
			recipes.add(display);
		}
		recipes.sort(Comparator.comparing(BotaniaRrvRecipe::sortGroup));
		list.addAll(recipes);
	}

	private static void addTerraPlate(List<ReliableClientRecipe> list) {
		for (RecipeHolder<TerrestrialAgglomerationRecipe> holder : recipes(BotaniaRecipeTypes.TERRA_PLATE_TYPE)) {
			TerrestrialAgglomerationRecipe recipe = holder.value();
			list.add(new BotaniaRrvRecipe(TERRESTRIAL_AGGLOMERATION, holder.id().identifier())
					.render(texture(TERRA_TEXTURE, 22 + TERRA_DX, 7 + TERRA_DY, 64, 64, 42, 29))
					.render(manaBar(3, 141, recipe.getMana(), ManaPoolBlockEntity.MAX_MANA))
					.ringInputs(contents(recipe.getIngredients()))
					.catalyst(TERRA_PLATE, of(BotaniaBlocks.terraPlate))
					.output(TERRA_OUTPUT, SlotContent.of(recipe.getResultItem(RegistryAccess.EMPTY))));
		}
	}

	private static void addElvenTrade(List<ReliableClientRecipe> list) {
		Consumer<RecipeScreenContext> swirl = ctx -> ctx.guiGraphics().blitSprite(RenderPipelines.GUI_TEXTURED,
				RenderHelper.blockSprite(prefix("block/alfheim_portal_swirl")), 12, 22, 48, 48);
		for (RecipeHolder<ElvenTradeRecipe> holder : recipes(BotaniaRecipeTypes.ELVEN_TRADE_TYPE)) {
			ElvenTradeRecipe recipe = holder.value();
			List<ItemStack> outputs = recipe.getOutputs();
			if (!(recipe instanceof LexiconElvenTradeRecipe) && recipe.getIngredients().size() == 1
					&& outputs.size() == 1 && recipe.containsItem(outputs.get(0))) {
				continue;
			}
			BotaniaRrvRecipe display = new BotaniaRrvRecipe(ELVEN_TRADE, holder.id().identifier())
					.render(texture(ELVEN_TEXTURE, 10, 5, 71, 75, 20, 19))
					.render(swirl);
			List<SlotContent> inputs = contents(recipe.getIngredients());
			display.inputs(ELVEN_TRADE.layout().placeRow(0, inputs.size(), false), inputs);
			List<Integer> outputSlots = ELVEN_TRADE.layout().placeRow(1, outputs.size(), false);
			for (int i = 0; i < outputSlots.size(); i++) {
				display.output(outputSlots.get(i), SlotContent.of(outputs.get(i)));
			}
			list.add(display);
		}
	}

	private static void addBrewery(List<ReliableClientRecipe> list) {
		List<ItemStack> containers = List.of(BotaniaItems.vial, BotaniaItems.flask, BotaniaItems.incenseStick, BotaniaItems.bloodPendant)
				.stream().map(ItemStack::new).toList();
		for (RecipeHolder<BotanicalBreweryRecipe> holder : recipes(BotaniaRecipeTypes.BREW_TYPE)) {
			BotanicalBreweryRecipe recipe = holder.value();
			for (ItemStack container : containers) {
				ItemStack output = recipe.getOutput(container.copy());
				if (output.isEmpty()) {
					continue;
				}
				Identifier containerId = BuiltInRegistries.ITEM.getKey(container.getItem());
				Identifier id = holder.id().identifier().withSuffix("/" + containerId.getNamespace() + "/" + containerId.getPath());
				List<SlotContent> ingredients = contents(recipe.getIngredients());
				list.add(new BotaniaRrvRecipe(BOTANICAL_BREWERY, id)
						.render(texture(BREWERY_TEXTURE, BREWERY_DX, 0, 86, 55, 28, 6))
						.input(BREWERY_CONTAINER, SlotContent.of(container))
						.inputs(BOTANICAL_BREWERY.layout().placeRow(0, ingredients.size(), true), ingredients)
						.output(BREWERY_OUTPUT, SlotContent.of(output))
						.background(BREWERY_CONTAINER)
						.background(BREWERY_OUTPUT));
			}
		}
	}

	private static BotaniaRrvRecipe daisyRecipe(BotaniaRrvRecipeType type, RecipeHolder<?> holder, SlotContent input,
			ItemLike flower, SlotContent output) {
		return new BotaniaRrvRecipe(type, holder.id().identifier())
				.render(texture(PURE_DAISY_TEXTURE, 17, 0, 65, 44, 0, 0))
				.input(DAISY_INPUT, input)
				.catalyst(DAISY_FLOWER, of(flower))
				.output(DAISY_OUTPUT, output);
	}

	private static void addPureDaisy(List<ReliableClientRecipe> list) {
		for (RecipeHolder<PureDaisyRecipe> holder : recipes(BotaniaRecipeTypes.PURE_DAISY_TYPE)) {
			PureDaisyRecipe recipe = holder.value();
			list.add(daisyRecipe(PURE_DAISY, holder, blocks(recipe.getInput(), true), BotaniaFlowerBlocks.pureDaisy,
					of(recipe.getOutputState().getBlock())));
		}
	}

	private static <T extends OrechidRecipe> void addOrechid(List<ReliableClientRecipe> list, BotaniaRrvRecipeType type,
			RecipeType<T> recipeType, boolean biomeTooltip) {
		ItemLike flower = type.getCraftReferences().isEmpty() ? BotaniaFlowerBlocks.orechid : type.getCraftReferences().get(0).getItem();
		List<RecipeHolder<T>> holders = new ArrayList<>(recipes(recipeType));
		holders.sort(Comparator.<RecipeHolder<T>, Integer>comparing(holder -> holder.value().getWeight()).reversed()
				.thenComparing(holder -> holder.id().identifier()));
		for (RecipeHolder<T> holder : holders) {
			OrechidRecipe recipe = holder.value();
			BotaniaRrvRecipe display = daisyRecipe(type, holder, blocks(recipe.getInput(), false), flower, blocks(recipe.getOutput(), false));
			Double chance = OrechidUIHelper.getChance(recipe, null);
			if (chance != null) {
				double value = chance;
				Component text = OrechidUIHelper.getPercentageComponent(value);
				display.render(ctx -> {
					int width = ctx.font().width(text);
					RenderHelper.drawString(ctx.guiGraphics(), ctx.font(), text, 90 - width, 3, 0xFF555555, false);
					List<Component> lines = new ArrayList<>();
					lines.add(OrechidUIHelper.getRatioTooltipComponent(OrechidUIHelper.getRatioForChance(value)));
					if (biomeTooltip) {
						OrechidUIHelper.getBiomeChanceAndRatioTooltipComponents(value, recipe).forEach(lines::add);
					}
					ctx.setTooltip(lines, new Rect2i(90 - width, 3, width, 9));
				});
			}
			list.add(display);
		}
	}

	private static void addCrafting(List<ReliableClientRecipe> list) {
		Item[] wills = {
				BotaniaItems.ancientWillAhrim, BotaniaItems.ancientWillDharok, BotaniaItems.ancientWillGuthan,
				BotaniaItems.ancientWillKaril, BotaniaItems.ancientWillTorag, BotaniaItems.ancientWillVerac
		};
		for (Item will : wills) {
			ItemStack result = new ItemStack(BotaniaItems.terrasteelHelm);
			((AncientWillContainer) result.getItem()).addAncientWill(result, ((AncientWillItem) will).type);
			list.add(new CraftingClientRecipe.Builder(prefix("crafting/ancient_will/" + BuiltInRegistries.ITEM.getKey(will).getPath()),
					of(BotaniaItems.terrasteelHelm), of(will))
					.setResult(result)
					.build());
		}

		ItemStack tipped = new ItemStack(BotaniaItems.terraPick);
		TerraShattererItem.setTipped(tipped);
		list.add(new CraftingClientRecipe.Builder(prefix("crafting/terra_shatterer_tipping"),
				of(BotaniaItems.terraPick), of(BotaniaItems.elementiumPick))
				.setResult(tipped)
				.build());

		List<Item> lenses = new ArrayList<>();
		for (Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(BotaniaTags.Items.LENS)) {
			Item item = holder.value();
			if (item instanceof LensItem lens) {
				ItemStack stack = new ItemStack(item);
				if (!lens.isControlLens(stack) && lens.isCombinable(stack)) {
					lenses.add(item);
				}
			}
		}
		for (Item first : lenses) {
			ItemStack firstStack = new ItemStack(first);
			List<ItemStack> seconds = new ArrayList<>();
			List<ItemStack> outputs = new ArrayList<>();
			for (Item second : lenses) {
				if (second == first) {
					continue;
				}
				ItemStack secondStack = new ItemStack(second);
				if (((LensItem) first).canCombineLenses(firstStack, secondStack)) {
					seconds.add(secondStack);
					outputs.add(((LensItem) first).setCompositeLens(firstStack.copy(), secondStack));
				}
			}
			if (seconds.isEmpty()) {
				continue;
			}
			list.add(new CraftingClientRecipe.Builder(prefix("crafting/composite_lens/" + BuiltInRegistries.ITEM.getKey(first).getPath()),
					SlotContent.of(firstStack), of(Items.SLIME_BALL), SlotContent.of(seconds))
					.setResult(SlotContent.of(outputs))
					.build());
		}
	}

	private static void addWorldInteractions(List<ReliableClientRecipe> list) {
		SlotContent dirt = SlotContent.ofBlockTag(BotaniaTags.Blocks.PASTURE_SEED_REPLACEABLE);
		Map<Item, ItemLike> seeds = new LinkedHashMap<>();
		seeds.put(BotaniaItems.grassSeeds, Blocks.GRASS_BLOCK);
		seeds.put(BotaniaItems.mycelSeeds, Blocks.MYCELIUM);
		seeds.put(BotaniaItems.podzolSeeds, Blocks.PODZOL);
		seeds.put(BotaniaItems.drySeeds, BotaniaBlocks.dryGrass);
		seeds.put(BotaniaItems.goldenSeeds, BotaniaBlocks.goldenGrass);
		seeds.put(BotaniaItems.vividSeeds, BotaniaBlocks.vividGrass);
		seeds.put(BotaniaItems.scorchedSeeds, BotaniaBlocks.scorchedGrass);
		seeds.put(BotaniaItems.infusedSeeds, BotaniaBlocks.infusedGrass);
		seeds.put(BotaniaItems.mutatedSeeds, BotaniaBlocks.mutatedGrass);
		seeds.forEach((seed, grass) -> list.add(new WorldInteractionClientRecipe(
				BuiltInRegistries.ITEM.getKey(seed).withPrefix("world/grass_conversion/"), SlotContent.of(dirt), of(seed), of(grass))));

		list.add(new WorldInteractionClientRecipe(prefix("world/grass_conversion/enchanted_soil"),
				of(Blocks.GRASS_BLOCK), of(BotaniaItems.overgrowthSeed), of(BotaniaBlocks.enchantedSoil)));

		Map<Item, SlotContent> rods = new LinkedHashMap<>();
		rods.put(BotaniaItems.dirtRod, of(Blocks.DIRT));
		rods.put(BotaniaItems.skyDirtRod, of(Blocks.DIRT));
		rods.put(BotaniaItems.cobbleRod, of(Blocks.COBBLESTONE));
		rods.put(BotaniaItems.waterRod, SlotContent.of(new FluidStack(Fluids.WATER)));
		rods.forEach((rod, out) -> list.add(new WorldInteractionClientRecipe(
				BuiltInRegistries.ITEM.getKey(rod).withPrefix("world/rod/"), SlotContent.of(), of(rod), out)));

		try {
			for (RecipeHolder<SmeltingRecipe> holder : ClientRecipeManager.INSTANCE.getRecipesForType(RecipeType.SMELTING)) {
				SmeltingRecipe recipe = holder.value();
				List<ItemStack> inputs = recipe.input().items()
						.map(Holder::value)
						.filter(item -> item instanceof BlockItem)
						.map(ItemStack::new)
						.toList();
				if (inputs.isEmpty()) {
					continue;
				}
				ItemStack output = recipe.assemble(new SingleRecipeInput(inputs.get(0)));
				if (output.isEmpty() || !(output.getItem() instanceof BlockItem)) {
					continue;
				}
				list.add(new WorldInteractionClientRecipe(holder.id().identifier().withPrefix("world/molten_core_rod/"),
						SlotContent.of(inputs), of(BotaniaItems.smeltRod), SlotContent.of(output)));
			}
		} catch (RuntimeException ignored) {}

		list.add(new WorldInteractionClientRecipe(prefix("world/wandable/enchanter"),
				of(Blocks.LAPIS_BLOCK), SlotContent.of(List.of(new ItemStack(BotaniaItems.twigWand), new ItemStack(BotaniaItems.dreamwoodWand))),
				of(BotaniaBlocks.enchanter)));
	}
}
