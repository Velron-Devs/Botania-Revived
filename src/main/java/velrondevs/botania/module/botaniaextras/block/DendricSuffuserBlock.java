package velrondevs.botania.module.botaniaextras.block;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.block.BotaniaBlock;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;

public class DendricSuffuserBlock extends BaseEntityBlock {
	public static final MapCodec<DendricSuffuserBlock> CODEC = simpleCodec(DendricSuffuserBlock::new);
	public static final int BIFROST = 16;
	public static final IntegerProperty COLOR = IntegerProperty.create("color", 0, BIFROST);

	public DendricSuffuserBlock(Properties properties) {
		super(properties);
		registerDefaultState(stateDefinition.any().setValue(COLOR, 0));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(COLOR);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
		return new ItemStack(planksFor(state));
	}

	public static Block planksFor(BlockState state) {
		return BotaniaExtrasWoods.sets().get(Math.min(state.getValue(COLOR), BIFROST)).planks();
	}

	public static int colorIndexOf(BlockState planks) {
		var sets = BotaniaExtrasWoods.sets();
		for (int i = 0; i <= BIFROST; i++) {
			if (sets.get(i).planks() == planks.getBlock()) {
				return i;
			}
		}
		return -1;
	}

	public static boolean tryForm(Level level, BlockPos pos, BlockState planks) {
		int index = colorIndexOf(planks);
		if (index < 0 || !DendricSuffuserBlockEntity.canSuffuserExist(level, pos)) {
			return false;
		}
		if (!level.isClientSide) {
			level.setBlockAndUpdate(pos, BotaniaExtrasBlocks.dendricSuffuser.defaultBlockState().setValue(COLOR, index));
			level.playSound(null, pos, BotaniaSounds.enchanterForm, SoundSource.BLOCKS, 1F, 1F);
		} else {
			for (int i = 0; i < 50; i++) {
				double x = (Math.random() - 0.5) * 6;
				double y = (Math.random() - 0.5) * 6;
				double z = (Math.random() - 0.5) * 6;
				float velMul = 0.07F;
				WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, (float) Math.random(), (float) Math.random(), (float) Math.random());
				level.addParticle(data, pos.getX() + 0.5 + x, pos.getY() + 0.5 + y, pos.getZ() + 0.5 + z, -x * velMul, -y * velMul, -z * velMul);
			}
		}
		return true;
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		return level.getBlockEntity(pos) instanceof DendricSuffuserBlockEntity suffuser ? suffuser.getSignal() : 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new DendricSuffuserBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return BotaniaBlock.createTickerHelper(type, BotaniaExtrasBlockEntities.DENDRIC_SUFFUSER, DendricSuffuserBlockEntity::commonTick);
	}
}
