package velrondevs.botania.compat.sophisticated.backpack;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.Config;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackLinkedStorageResolver;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModBlocks;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;

import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.ModuleContext;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public final class BotaniaBackpackRegistry {
	private static final Map<BotaniaBackpackTier, BackpackBlock> BLOCKS = new EnumMap<>(BotaniaBackpackTier.class);
	private static final Map<BotaniaBackpackTier, BackpackItem> ITEMS = new EnumMap<>(BotaniaBackpackTier.class);

	private BotaniaBackpackRegistry() {}

	public static Collection<BackpackBlock> blocks() {
		return BLOCKS.values();
	}

	public static Collection<BackpackItem> items() {
		return ITEMS.values();
	}

	public static void register(ModuleContext ctx) {
		ctx.bindBlocks(consumer -> {
			for (BotaniaBackpackTier tier : BotaniaBackpackTier.active()) {
				BackpackBlock block = new BackpackBlock(tier.explosionResistance());
				BLOCKS.put(tier, block);
				consumer.accept(block, id(tier));
			}
		});
		ctx.bindItems(consumer -> {
			for (BotaniaBackpackTier tier : BotaniaBackpackTier.active()) {
				BackpackItem item = new BackpackItem(tier.slots(), tier.upgradeSlots(), () -> BLOCKS.get(tier), Item.Properties::fireResistant);
				ITEMS.put(tier, item);
				consumer.accept(item, id(tier));
			}
		});
		ctx.modBus().addListener((BlockEntityTypeAddBlocksEvent event) ->
				event.modify(ModBlocks.BACKPACK_TILE_TYPE.get(), BLOCKS.values().toArray(new Block[0])));
		ctx.modBus().addListener(BotaniaBackpackRegistry::registerCapabilities);
		ctx.modBus().addListener((FMLCommonSetupEvent event) -> event.enqueueWork(BotaniaBackpackRegistry::registerBehaviors));
	}

	private static ResourceLocation id(BotaniaBackpackTier tier) {
		return ResourceLocation.fromNamespaceAndPath(LibMisc.MOD_ID, tier.id());
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		Item[] items = ITEMS.values().toArray(new Item[0]);
		event.registerItem(Capabilities.ItemHandler.ITEM, (stack, context) -> {
			IBackpackWrapper wrapper = wrapper(stack);
			return wrapper.getContentsUuid().isEmpty() ? EmptyItemHandler.INSTANCE : wrapper.getInventoryForInputOutput();
		}, items);
		event.registerItem(Capabilities.FluidHandler.ITEM, (stack, context) -> {
			if (!Config.SERVER.itemFluidHandlerEnabled.get()) {
				return null;
			}
			return BackpackWrapper.getCapabilityFluidHandler(stack).orElse(null);
		}, items);
		event.registerItem(Capabilities.EnergyStorage.ITEM, (stack, context) -> wrapper(stack).getEnergyStorage().orElse(null), items);
	}

	private static IBackpackWrapper wrapper(ItemStack stack) {
		return BackpackLinkedStorageResolver.resolveServerCanonicalHost(stack).orElseGet(() -> BackpackWrapper.fromStack(stack));
	}

	private static void registerBehaviors() {
		for (BackpackItem item : ITEMS.values()) {
			DispenserBlock.registerBehavior(item, new Dispense());
			CauldronInteraction.WATER.map().put(item, new Wash());
		}
	}

	private static final class Dispense extends OptionalDispenseItemBehavior {
		@Override
		protected ItemStack execute(BlockSource source, ItemStack stack) {
			setSuccess(false);
			if (stack.getItem() instanceof BackpackItem backpackItem) {
				Direction facing = source.state().getValue(DispenserBlock.FACING);
				BlockPos pos = source.pos().relative(facing);
				Direction against = source.level().isEmptyBlock(pos.below()) ? facing.getOpposite() : Direction.UP;
				Direction direction = facing.getAxis() == Direction.Axis.Y ? Direction.NORTH : facing.getOpposite();
				setSuccess(backpackItem.tryPlace(null, direction, new DirectionalPlaceContext(source.level(), pos, facing, stack, against)).consumesAction());
			}
			return stack;
		}
	}

	private static final class Wash implements CauldronInteraction {
		@Override
		public ItemInteractionResult interact(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
			IBackpackWrapper wrapper = BackpackWrapper.fromStack(stack);
			if (isDefault(wrapper)) {
				return ItemInteractionResult.FAIL;
			}
			if (!level.isClientSide) {
				wrapper.setColors(BackpackWrapper.DEFAULT_MAIN_COLOR, BackpackWrapper.DEFAULT_ACCENT_COLOR);
				LayeredCauldronBlock.lowerFillLevel(state, level, pos);
			}
			return ItemInteractionResult.sidedSuccess(level.isClientSide);
		}

		private static boolean isDefault(IStorageWrapper wrapper) {
			return wrapper.getAccentColor() == BackpackWrapper.DEFAULT_ACCENT_COLOR && wrapper.getMainColor() == BackpackWrapper.DEFAULT_MAIN_COLOR;
		}
	}
}
