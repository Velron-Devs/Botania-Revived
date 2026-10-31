package velrondevs.botania.compat.sophisticated.transfer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.compat.sophisticated.StationUpgradeContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record StationTransferPayload(int containerId, int kind, ResourceLocation recipeId, ResourceLocation preferred, boolean max) implements CustomPacketPayload {
	public static final Type<StationTransferPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, "station_transfer"));
	public static final StreamCodec<RegistryFriendlyByteBuf, StationTransferPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StationTransferPayload::containerId,
			ByteBufCodecs.VAR_INT, StationTransferPayload::kind,
			ResourceLocation.STREAM_CODEC, StationTransferPayload::recipeId,
			ResourceLocation.STREAM_CODEC, StationTransferPayload::preferred,
			ByteBufCodecs.BOOL, StationTransferPayload::max,
			StationTransferPayload::new);

	public static void register(RegisterPayloadHandlersEvent event) {
		event.registrar("1").playToServer(TYPE, STREAM_CODEC, (IPayloadHandler<StationTransferPayload>) StationTransferPayload::handle);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static void handle(StationTransferPayload payload, IPayloadContext context) {
		if (context.player() instanceof ServerPlayer player) {
			payload.apply(player);
		}
	}

	private void apply(ServerPlayer player) {
		if (!(player.containerMenu instanceof StorageContainerMenuBase<?> menu) || menu.containerId != containerId || !menu.stillValid(player)) {
			return;
		}
		StationKind stationKind = StationKind.byId(kind);
		if (stationKind == null) {
			return;
		}
		Optional<StationUpgradeContainer<?, ?>> container = StationTransfer.find(menu, stationKind);
		if (container.isEmpty()) {
			notify(player, stationKind.missingUpgradeKey());
			return;
		}
		Optional<RecipeHolder<?>> holder = player.serverLevel().getRecipeManager().byKey(recipeId);
		if (holder.isEmpty() || !stationKind.acceptsRecipe(holder.get().value())) {
			notify(player, "gui.botania.sophisticated.transfer.invalid");
			return;
		}
		Item preferredItem = BuiltInRegistries.ITEM.get(preferred);
		List<StationTransfer.Need> needs = StationTransfer.needs(stationKind, holder.get().value(), preferredItem);
		if (needs == null) {
			notify(player, "gui.botania.sophisticated.transfer.invalid");
			return;
		}
		boolean multi = stationKind == StationKind.ELVEN_PORTAL;
		IItemHandlerModifiable target = container.get().getUpgradeWrapper().getInventory();
		List<Slot> sources = StationTransfer.sources(menu, player);
		StationTransfer.Plan check = StationTransfer.plan(needs, StationTransfer.snapshot(sources), target, multi, max, true);
		if (!check.ok()) {
			notify(player, "gui.botania.sophisticated.transfer.missing");
			return;
		}
		for (int slot = 0; slot < target.getSlots(); slot++) {
			ItemStack current = target.getStackInSlot(slot);
			if (!check.kept()[slot] && !current.isEmpty()) {
				target.setStackInSlot(slot, ItemStack.EMPTY);
				giveBack(menu, player, current.copy());
			}
		}
		StationTransfer.Plan plan = StationTransfer.plan(needs, StationTransfer.snapshot(sources), target, multi, max, false);
		if (!plan.ok()) {
			notify(player, "gui.botania.sophisticated.transfer.missing");
			return;
		}
		List<ItemStack> removed = new ArrayList<>();
		boolean complete = true;
		for (int i = 0; i < sources.size() && complete; i++) {
			int count = plan.takes()[i];
			if (count <= 0) {
				continue;
			}
			Slot slot = sources.get(i);
			ItemStack taken = slot.mayPickup(player) ? slot.remove(count) : ItemStack.EMPTY;
			if (!taken.isEmpty()) {
				removed.add(taken);
				slot.setChanged();
			}
			complete = taken.getCount() == count;
		}
		if (!complete) {
			for (ItemStack stack : removed) {
				giveBack(menu, player, stack);
			}
			notify(player, "gui.botania.sophisticated.transfer.missing");
			return;
		}
		for (int slot = 0; slot < plan.slots().length; slot++) {
			if (!plan.slots()[slot].isEmpty()) {
				target.setStackInSlot(slot, plan.slots()[slot].copy());
			}
		}
		menu.broadcastChanges();
	}

	private static void giveBack(StorageContainerMenuBase<?> menu, ServerPlayer player, ItemStack stack) {
		ItemStack rest = stack;
		player.getInventory().add(rest);
		if (!rest.isEmpty()) {
			rest = InventoryHelper.insertIntoInventory(rest, menu.getStorageWrapper().getInventoryForUpgradeProcessing(), false);
		}
		if (!rest.isEmpty()) {
			player.drop(rest, false);
		}
	}

	private static void notify(ServerPlayer player, String key) {
		player.displayClientMessage(Component.translatable(key), true);
	}
}
