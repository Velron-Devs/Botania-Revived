package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.block.block_entity.ExposedSimpleInventoryBlockEntity;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;

import java.util.List;

public class LivingwoodFunnelBlockEntity extends ExposedSimpleInventoryBlockEntity {
	private static final String TAG_COOLDOWN = "transferCooldown";
	private static final int COOLDOWN = 8;

	private int cooldown = -1;

	public LivingwoodFunnelBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaExtrasUtilities.FUNNEL, pos, state);
	}

	public ItemStack getDisplayed() {
		return getItemHandler().getItem(0);
	}

	@Override
	protected SimpleContainer createItemHandler() {
		return new SimpleContainer(1) {
			@Override
			public int getMaxStackSize() {
				return 1;
			}
		};
	}

	@Override
	public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);
		tag.putInt(TAG_COOLDOWN, cooldown);
	}

	@Override
	public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);
		cooldown = tag.getInt(TAG_COOLDOWN);
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (level != null && !level.isClientSide) {
			VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
			level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
		}
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, LivingwoodFunnelBlockEntity funnel) {
		funnel.cooldown--;
		if (funnel.cooldown > 0) {
			return;
		}
		funnel.cooldown = 0;
		if (!state.getValue(HopperBlock.ENABLED)) {
			return;
		}
		boolean moved = false;
		if (!funnel.isEmpty()) {
			moved = funnel.push(level, pos, state);
		}
		if (funnel.isEmpty()) {
			moved = funnel.pull(level, pos) || moved;
		}
		if (moved) {
			funnel.cooldown = COOLDOWN;
			funnel.setChanged();
		}
	}

	private boolean push(Level level, BlockPos pos, BlockState state) {
		Direction facing = state.getValue(HopperBlock.FACING);
		BlockPos targetPos = pos.relative(facing);
		ItemStack one = getItem(0).copyWithCount(1);
		Container target = HopperBlockEntity.getContainerAt(level, targetPos);
		if (target != null) {
			ItemStack remainder = HopperBlockEntity.addItem(this, target, one, facing.getOpposite());
			if (remainder.isEmpty()) {
				target.setChanged();
				removeItem(0, 1);
				return true;
			}
			return false;
		}
		IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, facing.getOpposite());
		if (handler != null && ItemHandlerHelper.insertItem(handler, one, false).isEmpty()) {
			removeItem(0, 1);
			return true;
		}
		return false;
	}

	private boolean pull(Level level, BlockPos pos) {
		BlockPos abovePos = pos.above();
		Container above = HopperBlockEntity.getContainerAt(level, abovePos);
		if (above != null) {
			int[] slots;
			if (above instanceof WorldlyContainer worldly) {
				slots = worldly.getSlotsForFace(Direction.DOWN);
			} else {
				slots = new int[above.getContainerSize()];
				for (int i = 0; i < slots.length; i++) {
					slots[i] = i;
				}
			}
			for (int slot : slots) {
				ItemStack stack = above.getItem(slot);
				if (stack.isEmpty() || !passesFilter(stack)) {
					continue;
				}
				if (above instanceof WorldlyContainer worldly && !worldly.canTakeItemThroughFace(slot, stack, Direction.DOWN)) {
					continue;
				}
				setItem(0, stack.copyWithCount(1));
				above.removeItem(slot, 1);
				above.setChanged();
				return true;
			}
			return false;
		}
		IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, abovePos, Direction.DOWN);
		if (handler != null) {
			for (int slot = 0; slot < handler.getSlots(); slot++) {
				ItemStack simulated = handler.extractItem(slot, 1, true);
				if (!simulated.isEmpty() && passesFilter(simulated)) {
					ItemStack extracted = handler.extractItem(slot, 1, false);
					if (!extracted.isEmpty()) {
						setItem(0, extracted);
						return true;
					}
				}
			}
			return false;
		}
		List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, new AABB(abovePos), e -> e.isAlive() && !e.getItem().isEmpty());
		for (ItemEntity entity : items) {
			ItemStack stack = entity.getItem();
			if (passesFilter(stack)) {
				setItem(0, stack.copyWithCount(1));
				stack.shrink(1);
				if (stack.isEmpty()) {
					entity.discard();
				} else {
					entity.setItem(stack);
				}
				return true;
			}
		}
		return false;
	}

	public boolean passesFilter(ItemStack stack) {
		if (level == null) {
			return true;
		}
		boolean filtered = false;
		for (Direction dir : Direction.Plane.HORIZONTAL) {
			for (ItemFrame frame : level.getEntitiesOfClass(ItemFrame.class, new AABB(worldPosition.relative(dir)))) {
				ItemStack shown = frame.getItem();
				if (!shown.isEmpty()) {
					filtered = true;
					if (ItemStack.isSameItem(stack, shown)) {
						return true;
					}
				}
			}
		}
		return !filtered;
	}

	public static class WandHud implements WandHUD {
		private final LivingwoodFunnelBlockEntity funnel;

		public WandHud(LivingwoodFunnelBlockEntity funnel) {
			this.funnel = funnel;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			ItemStack stack = funnel.getDisplayed();
			if (!stack.isEmpty()) {
				int x = mc.getWindow().getGuiScaledWidth() / 2 + 8;
				int y = mc.getWindow().getGuiScaledHeight() / 2 - 12;
				RenderHelper.renderHUDBox(gui, x, y, x + 24, y + 24);
				gui.renderItem(stack, x + 4, y + 4);
			}
		}
	}
}
