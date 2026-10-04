package velrondevs.botania.api.block_entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPIClient;
import velrondevs.botania.api.block.Bound;
import velrondevs.botania.api.block.WandBindable;
import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.helper.MathHelper;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.Objects;

public abstract class BindableSpecialFlowerBlockEntity<T> extends SpecialFlowerBlockEntity implements WandBindable {

	private final Class<T> bindClass;

	protected @Nullable BlockPos bindingPos = null;
	private static final String TAG_BINDING = "binding";

	public BindableSpecialFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, Class<T> bindClass) {
		super(type, pos, state);
		this.bindClass = bindClass;
	}

	public abstract int getBindingRadius();

	public abstract @Nullable BlockPos findClosestTarget();

	@Override
	protected void tickFlower() {
		super.tickFlower();

		if (Bound.UNBOUND_POS.equals(bindingPos)) {
			setBindingPos(null);
		} else if (ticksExisted == 1 && !level.isClientSide) {

			if (bindingPos == null || !isValidBinding()) {
				setBindingPos(findClosestTarget());
			}
		}
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		if (placer != null && placer.isHolding(BotaniaItems.obedienceStick)) {
			setBindingPos(Bound.UNBOUND_POS);
		}
		super.setPlacedBy(level, pos, state, placer, stack);
	}

	public @Nullable BlockPos getBindingPos() {
		return bindingPos;
	}

	public void setBindingPos(@Nullable BlockPos bindingPos) {
		boolean changed = !Objects.equals(this.bindingPos, bindingPos);

		this.bindingPos = bindingPos;

		if (changed) {
			setChanged();
			sync();
		}
	}

	public @Nullable T findBindCandidateAt(BlockPos pos) {
		if (level == null || pos == null) {
			return null;
		}

		BlockEntity be = level.getBlockEntity(pos);
		if (bindClass.isInstance(be)) {
			return bindClass.cast(be);
		}
		if (be != null) {
			ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(level, pos, null);
			if (bindClass.isInstance(receiver)) {
				return bindClass.cast(receiver);
			}
		}
		return null;
	}

	public @Nullable T findBoundTile() {
		return findBindCandidateAt(bindingPos);
	}

	public boolean wouldBeValidBinding(@Nullable BlockPos pos) {
		if (level == null || pos == null || !level.isLoaded(pos) || MathHelper.distSqr(getBlockPos(), pos) > (long) getBindingRadius() * getBindingRadius()) {
			return false;
		} else {
			return findBindCandidateAt(pos) != null;
		}
	}

	public boolean isValidBinding() {
		return wouldBeValidBinding(bindingPos);
	}

	@Override
	public BlockPos getBinding() {

		return isValidBinding() ? bindingPos : null;
	}

	@Override
	public boolean canSelect(Player player, ItemStack wand, BlockPos pos, Direction side) {
		return true;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		if (wouldBeValidBinding(pos)) {
			setBindingPos(pos);
			return true;
		}

		return false;
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);

		if (bindingPos != null) {
			cmp.put(TAG_BINDING, NbtUtils.writeBlockPos(bindingPos));
		}
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);

		if (cmp.contains(TAG_BINDING)) {
			if (cmp.contains(TAG_BINDING, Tag.TAG_COMPOUND)) {
				CompoundTag binding = cmp.getCompound(TAG_BINDING);
				bindingPos = new BlockPos(binding.getInt("X"), binding.getInt("Y"), binding.getInt("Z"));
			} else {
				bindingPos = NbtUtils.readBlockPos(cmp, TAG_BINDING).orElse(null);
			}
		} else {

			if (cmp.contains("collectorX")) {
				bindingPos = new BlockPos(cmp.getInt("collectorX"), cmp.getInt("collectorY"), cmp.getInt("collectorZ"));
			} else if (cmp.contains("poolX")) {
				bindingPos = new BlockPos(cmp.getInt("poolX"), cmp.getInt("poolY"), cmp.getInt("poolZ"));
			}

			if (bindingPos != null && bindingPos.getY() == -1) {
				bindingPos = null;
			}
		}
	}

	public abstract int getMana();

	public abstract void addMana(int mana);

	public abstract int getMaxMana();

	public abstract int getColor();

	public abstract ItemStack getDefaultHudIcon();

	public ItemStack getHudIcon() {
		T boundTile = findBoundTile();
		if (boundTile != null) {
			return new ItemStack(((BlockEntity) boundTile).getBlockState().getBlock().asItem());
		}
		return getDefaultHudIcon();
	}

	public static class BindableFlowerWandHud<F extends BindableSpecialFlowerBlockEntity<?>> implements WandHUD {
		protected final F flower;

		public BindableFlowerWandHud(F flower) {
			this.flower = flower;
		}

		public void renderHUD(GuiGraphics gui, Minecraft mc, int minLeft, int minRight, int minDown) {
			String name = I18n.get(flower.getBlockState().getBlock().getDescriptionId());
			int color = flower.getColor();

			int centerX = mc.getWindow().getGuiScaledWidth() / 2;
			int centerY = mc.getWindow().getGuiScaledHeight() / 2;
			int left = (Math.max(102, mc.font.width(name)) + 4) / 2;

			int right = left + 20;

			left = Math.max(left, minLeft);
			right = Math.max(right, minRight);

			RenderHelper.renderHUDBox(gui, centerX - left, centerY + 8, centerX + right, centerY + Math.max(30, minDown));

			BotaniaAPIClient.instance().drawComplexManaHUD(gui, color, flower.getMana(), flower.getMaxMana(),
					name, flower.getHudIcon(), flower.isValidBinding());
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			renderHUD(gui, mc, 0, 0, 0);
		}
	}
}
