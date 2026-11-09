package velrondevs.botania.compat.sophisticated.rei;

import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.integration.rei.BotaniaReiCategories;
import velrondevs.botania.client.integration.rei.BotaniaReiDisplay;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;
import velrondevs.botania.compat.sophisticated.UpgradeSlots;
import velrondevs.botania.compat.sophisticated.transfer.StationKind;
import velrondevs.botania.compat.sophisticated.transfer.StationTransfer;
import velrondevs.botania.compat.sophisticated.transfer.StationTransferPayload;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.List;
import java.util.Optional;

public class StationReiTransferHandler implements TransferHandler {
	@Override
	public Result handle(Context context) {
		if (!(context.getMenu() instanceof StorageContainerMenuBase<?> menu) || !(context.getDisplay() instanceof BotaniaReiDisplay display)) {
			return Result.createNotApplicable();
		}
		StationKind kind = kindOf(display);
		LocalPlayer player = context.getMinecraft().player;
		if (kind == null || player == null || player.level() == null) {
			return Result.createNotApplicable();
		}
		Optional<UpgradeContainerBase<?, ?>> container = StationTransfer.find(menu, kind);
		if (container.isEmpty()) {
			return Result.createFailed(Component.translatable(kind.missingUpgradeKey())).blocksFurtherHandling();
		}
		Item preferred = kind == StationKind.BREWERY ? containerItem(display) : Items.AIR;
		Identifier recipeId = recipeId(display, kind, preferred);
		if (recipeId == null) {
			return Result.createNotApplicable();
		}
		Optional<RecipeHolder<?>> holder = Optional.ofNullable(BotaniaRecipeTypes.recipeMap(player.level()).byKey(ResourceKey.create(Registries.RECIPE, recipeId)));
		if (holder.isEmpty() || !kind.acceptsRecipe(holder.get().value())) {
			return Result.createNotApplicable();
		}
		List<StationTransfer.Need> needs = StationTransfer.needs(kind, holder.get().value(), preferred);
		if (needs == null) {
			return Result.createFailed(Component.translatable("gui.botania.sophisticated.transfer.invalid")).blocksFurtherHandling();
		}
		UpgradeSlots target = StationTransfer.inventory(container.get());
		boolean[] managed = StationTransfer.managed(kind, needs, target.getSlots());
		List<Slot> sources = StationTransfer.sources(menu, player);
		StationTransfer.Plan plan = StationTransfer.plan(needs, StationTransfer.snapshot(sources), target,
				kind == StationKind.ELVEN_PORTAL, context.isStackedCrafting(), true, managed);
		if (!plan.ok()) {
			return Result.createFailed(Component.translatable("gui.botania.sophisticated.transfer.missing")).blocksFurtherHandling();
		}
		if (context.isActuallyCrafting()) {
			ClientPacketDistributor.sendToServer(new StationTransferPayload(menu.containerId, kind.ordinal(), recipeId,
					BuiltInRegistries.ITEM.getKey(preferred), context.isStackedCrafting()));
		}
		return Result.createSuccessful().blocksFurtherHandling();
	}

	@Nullable
	private static StationKind kindOf(BotaniaReiDisplay display) {
		if (display.getCategoryIdentifier().equals(BotaniaReiCategories.RUNIC_ALTAR)) {
			return StationKind.RUNIC_ALTAR;
		}
		if (display.getCategoryIdentifier().equals(BotaniaReiCategories.ELVEN_TRADE)) {
			return StationKind.ELVEN_PORTAL;
		}
		if (display.getCategoryIdentifier().equals(BotaniaReiCategories.BOTANICAL_BREWERY)) {
			return StationKind.BREWERY;
		}
		if (display.getCategoryIdentifier().equals(BotaniaReiCategories.MANA_INFUSION)) {
			return StationKind.MANA_INFUSION;
		}
		return null;
	}

	private static Item containerItem(BotaniaReiDisplay display) {
		for (EntryIngredient extra : display.extras()) {
			for (EntryStack<?> entry : extra) {
				if (entry.getValue() instanceof ItemStack stack) {
					return stack.getItem();
				}
			}
		}
		return Items.AIR;
	}

	@Nullable
	private static Identifier recipeId(BotaniaReiDisplay display, StationKind kind, Item container) {
		Identifier id = display.getDisplayLocation().orElse(null);
		if (id == null || kind != StationKind.BREWERY) {
			return id;
		}
		Identifier containerId = BuiltInRegistries.ITEM.getKey(container);
		String suffix = "/" + containerId.getNamespace() + "/" + containerId.getPath();
		if (!id.getPath().endsWith(suffix)) {
			return null;
		}
		return Identifier.fromNamespaceAndPath(id.getNamespace(), id.getPath().substring(0, id.getPath().length() - suffix.length()));
	}
}
