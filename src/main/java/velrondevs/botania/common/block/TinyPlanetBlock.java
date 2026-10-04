package velrondevs.botania.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.mana.ManaCollisionGhost;
import velrondevs.botania.common.block.block_entity.TinyPlanetBlockEntity;
import velrondevs.botania.registry.BotaniaBlockEntities;

public class TinyPlanetBlock extends BotaniaWaterloggedBlock implements ManaCollisionGhost, EntityBlock {

	private static final VoxelShape AABB = box(3, 3, 3, 13, 13, 13);

	public TinyPlanetBlock(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
		return AABB;
	}

	@NotNull
	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TinyPlanetBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, BotaniaBlockEntities.TINY_PLANET, TinyPlanetBlockEntity::commonTick);
	}

	@Override
	public Behaviour getGhostBehaviour() {
		return Behaviour.SKIP_ALL;
	}
}
