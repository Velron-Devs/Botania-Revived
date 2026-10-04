package velrondevs.botania.common.block.block_entity.mana;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.block.Wandable;
import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.common.block.block_entity.BotaniaBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class SpreaderTurntableBlockEntity extends BotaniaBlockEntity implements Wandable {
	private static final String TAG_SPEED = "speed";
	private static final String TAG_BACKWARDS = "backwards";

	private int speed = 1;
	private boolean backwards = false;

	public SpreaderTurntableBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.TURNTABLE, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, SpreaderTurntableBlockEntity self) {
		if (!level.hasNeighborSignal(worldPosition)) {
			BlockEntity tile = level.getBlockEntity(worldPosition.above());
			if (tile instanceof ManaSpreaderBlockEntity spreader) {
				spreader.rotationX += self.speed * (self.backwards ? -1 : 1);
				if (spreader.rotationX >= 360F) {
					spreader.rotationX -= 360F;
				}
				if (!level.isClientSide) {
					spreader.checkForReceiver();
				}
			}
		}
	}

	@Override
	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_SPEED, speed);
		cmp.putBoolean(TAG_BACKWARDS, backwards);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		speed = cmp.getInt(TAG_SPEED);
		backwards = cmp.getBoolean(TAG_BACKWARDS);
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack wand, Direction side) {
		if ((player != null && player.isShiftKeyDown()) || (player == null && side == Direction.DOWN)) {
			backwards = !backwards;
		} else {
			speed = speed == 6 ? 1 : speed + 1;
		}
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
		return true;
	}

	public static class WandHud implements WandHUD {
		private final SpreaderTurntableBlockEntity turntable;

		public WandHud(SpreaderTurntableBlockEntity turntable) {
			this.turntable = turntable;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			char motion = turntable.backwards ? '<' : '>';
			String speed = ChatFormatting.BOLD + "";
			for (int i = 0; i < turntable.speed; i++) {
				speed = speed + motion;
			}

			int strWidth = mc.font.width(speed);
			int x = (mc.getWindow().getGuiScaledWidth() - strWidth) / 2;
			int y = mc.getWindow().getGuiScaledHeight() / 2 + 8;

			RenderHelper.renderHUDBox(gui, x - 2, y, x + strWidth + 2, y + 12);
			gui.drawString(mc.font, speed, x, y + 2, ChatFormatting.WHITE.getColor());
		}
	}

}
