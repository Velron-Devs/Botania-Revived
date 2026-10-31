package velrondevs.botania.compat.sophisticated.jei;

import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.PacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;

import velrondevs.botania.client.integration.jei.JEIRecipeIds;
import velrondevs.botania.compat.sophisticated.StationUpgradeContainer;
import velrondevs.botania.compat.sophisticated.transfer.StationKind;
import velrondevs.botania.compat.sophisticated.transfer.StationTransfer;
import velrondevs.botania.compat.sophisticated.transfer.StationTransferPayload;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StationTransferHandler<C extends StorageContainerMenuBase<?>, R extends Recipe<?>> implements IRecipeTransferHandler<C, R> {
	private final IRecipeTransferHandlerHelper helper;
	private final Class<C> menuClass;
	private final StationKind kind;
	private final RecipeType<R> recipeType;

	public StationTransferHandler(IRecipeTransferHandlerHelper helper, Class<C> menuClass, StationKind kind, RecipeType<R> recipeType) {
		this.helper = helper;
		this.menuClass = menuClass;
		this.kind = kind;
		this.recipeType = recipeType;
	}

	@Override
	public Class<? extends C> getContainerClass() {
		return menuClass;
	}

	@Override
	public Optional<MenuType<C>> getMenuType() {
		return Optional.empty();
	}

	@Override
	public RecipeType<R> getRecipeType() {
		return recipeType;
	}

	@Override
	public IRecipeTransferError transferRecipe(C menu, R recipe, IRecipeSlotsView slotsView, Player player, boolean maxTransfer, boolean doTransfer) {
		Optional<StationUpgradeContainer<?, ?>> container = StationTransfer.find(menu, kind);
		if (container.isEmpty()) {
			return helper.createUserErrorWithTooltip(Component.translatable(kind.missingUpgradeKey()));
		}
		ResourceLocation recipeId = JEIRecipeIds.idOf(recipe);
		if (recipeId == null) {
			return helper.createInternalError();
		}
		List<IRecipeSlotView> inputs = slotsView.getSlotViews(RecipeIngredientRole.INPUT);
		Item preferred = kind == StationKind.BREWERY ? preferredContainer(inputs) : Items.AIR;
		List<StationTransfer.Need> needs = StationTransfer.needs(kind, recipe, preferred);
		if (needs == null) {
			return helper.createUserErrorWithTooltip(Component.translatable("gui.botania.sophisticated.transfer.invalid"));
		}
		IItemHandlerModifiable target = container.get().getUpgradeWrapper().getInventory();
		List<Slot> sources = StationTransfer.sources(menu, player);
		StationTransfer.Plan plan = StationTransfer.plan(needs, StationTransfer.snapshot(sources), target, kind == StationKind.ELVEN_PORTAL, maxTransfer, true);
		if (!plan.ok()) {
			List<IRecipeSlotView> missing = new ArrayList<>();
			for (int view : plan.missingViews()) {
				if (view >= 0 && view < inputs.size()) {
					missing.add(inputs.get(view));
				}
			}
			Component message = Component.translatable("gui.botania.sophisticated.transfer.missing");
			return missing.isEmpty() ? helper.createUserErrorWithTooltip(message) : helper.createUserErrorForMissingSlots(message, missing);
		}
		if (doTransfer) {
			PacketDistributor.sendToServer(new StationTransferPayload(menu.containerId, kind.ordinal(), recipeId, BuiltInRegistries.ITEM.getKey(preferred), maxTransfer));
		}
		return null;
	}

	private static Item preferredContainer(List<IRecipeSlotView> inputs) {
		if (inputs.isEmpty()) {
			return Items.AIR;
		}
		return inputs.get(0).getDisplayedItemStack().map(stack -> stack.getItem()).orElse(Items.AIR);
	}
}
