package velrondevs.botania.compat.sophisticated.transfer;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import velrondevs.botania.compat.sophisticated.UpgradeSlots;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.p3pp3rf1y.sophisticatedcore.common.gui.StorageContainerMenuBase;
import net.p3pp3rf1y.sophisticatedcore.util.InventoryHelper;

import velrondevs.botania.common.lib.LibMisc;
import net.p3pp3rf1y.sophisticatedcore.common.gui.UpgradeContainerBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record StationTransferPayload(int containerId, int kind, Identifier recipeId, Identifier preferred, boolean max) implements CustomPacketPayload {
	public static final Type<StationTransferPayload> TYPE = new Type<>(Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, "station_transfer"));
	public static final StreamCodec<RegistryFriendlyByteBuf, StationTransferPayload> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, StationTransferPayload::containerId,
			ByteBufCodecs.VAR_INT, StationTransferPayload::kind,
			Identifier.STREAM_CODEC, StationTransferPayload::recipeId,
			Identifier.STREAM_CODEC, StationTransferPayload::preferred,
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
		Optional<UpgradeContainerBase<?, ?>> container = StationTransfer.find(menu, stationKind);
		if (container.isEmpty()) {
			notify(player, stationKind.missingUpgradeKey());
			return;
		}
		Optional<RecipeHolder<?>> holder = player.level().recipeAccess().byKey(ResourceKey.create(Registries.RECIPE, recipeId));
		if (holder.isEmpty() || !stationKind.acceptsRecipe(holder.get().value())) {
			notify(player, "gui.botania.sophisticated.transfer.invalid");
			return;
		}
		Item preferredItem = BuiltInRegistries.ITEM.getOptional(preferred).orElse(Items.AIR);
		List<StationTransfer.Need> needs = StationTransfer.needs(stationKind, holder.get().value(), preferredItem);
		if (needs == null) {
			notify(player, "gui.botania.sophisticated.transfer.invalid");
			return;
		}
		boolean multi = stationKind == StationKind.ELVEN_PORTAL;
		UpgradeSlots target = StationTransfer.inventory(container.get());
		boolean[] managed = StationTransfer.managed(stationKind, needs, target.getSlots());
		List<Slot> sources = StationTransfer.sources(menu, player);
		StationTransfer.Plan check = StationTransfer.plan(needs, StationTransfer.snapshot(sources), target, multi, max, true, managed);
		if (!check.ok()) {
			notify(player, "gui.botania.sophisticated.transfer.missing");
			return;
		}
		for (int slot = 0; slot < target.getSlots(); slot++) {
			ItemStack current = target.getStackInSlot(slot);
			if (managed[slot] && !check.kept()[slot] && !current.isEmpty()) {
				target.setStackInSlot(slot, ItemStack.EMPTY);
				giveBack(menu, player, current.copy());
			}
		}
		StationTransfer.Plan plan = StationTransfer.plan(needs, StationTransfer.snapshot(sources), target, multi, max, false, managed);
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
			int inserted = InventoryHelper.insert(menu.getStorageWrapper().getInventoryForUpgradeProcessing(), rest);
			rest = rest.copyWithCount(rest.getCount() - inserted);
		}
		if (!rest.isEmpty()) {
			player.drop(rest, false);
		}
	}

	private static void notify(ServerPlayer player, String key) {
		player.sendOverlayMessage(Component.translatable(key));
	}
}
