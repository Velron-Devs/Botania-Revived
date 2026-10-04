package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.block.BotaniaWaterloggedBlock;
import velrondevs.botania.common.item.equipment.bauble.ManaseerMonocleItem;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;

public class ManaFlashBlock extends BotaniaWaterloggedBlock implements EntityBlock {
	private static final VoxelShape SHAPE = box(4, 4, 4, 12, 12, 12);

	private final boolean rainbow;

	public ManaFlashBlock(boolean rainbow, Properties properties) {
		super(properties);
		this.rainbow = rainbow;
	}

	public boolean isRainbow() {
		return rainbow;
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	@NotNull
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new ManaFlashBlockEntity(rainbow ? BotaniaExtrasBlockEntities.RAINBOW_MANA_FLASH : BotaniaExtrasBlockEntities.PHANTOM_MANA_FLASH, pos, state);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (!(level.getBlockEntity(pos) instanceof ManaFlashBlockEntity flash)) {
			return;
		}
		if (!rainbow || flash.isInvisible()) {
			Player viewer = Proxy.INSTANCE.getClientPlayer();
			if (viewer == null || !ManaseerMonocleItem.hasMonocle(viewer)) {
				return;
			}
		}
		int color = rainbow
				? Mth.hsvToRgb(((level.getGameTime() + (pos.hashCode() & 0xFFFF)) * 0.005F) % 1F, 1F, 1F)
				: flash.getColor();
		float r = (color >> 16 & 0xFF) / 255F;
		float g = (color >> 8 & 0xFF) / 255F;
		float b = (color & 0xFF) / 255F;
		double luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b;
		if (luminance < 0.1) {
			r += (float) Math.random() * 0.125F;
			g += (float) Math.random() * 0.125F;
			b += (float) Math.random() * 0.125F;
		}
		double x = pos.getX() + 0.5 + (Math.random() - 0.5) * 0.15F;
		double y = pos.getY() + 0.25 + (Math.random() - 0.5) * 0.05F;
		double z = pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.15F;
		float size = 0.2F + (float) Math.random() * 0.1F;
		float motion = 0.03F + (float) Math.random() * 0.015F;
		level.addParticle(WispParticleData.wisp(size, r, g, b, 1), x, y, z, 0, motion, 0);
	}
}
