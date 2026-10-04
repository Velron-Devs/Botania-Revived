package velrondevs.botania.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.source.ISourceTile;
import com.hollingsworth.arsnouveau.api.source.ISpecialSourceProvider;
import com.hollingsworth.arsnouveau.api.source.SourceManager;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.mana.ManaNetworkEvent;
import velrondevs.botania.api.mana.ManaNetworkAction;
import velrondevs.botania.api.mana.ManaBlockType;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

final class PoolSourceProvider implements ISpecialSourceProvider {
	private static final Set<ManaPoolBlockEntity> REGISTERED = Collections.newSetFromMap(new WeakHashMap<>());

	private final ManaPoolBlockEntity pool;
	private final PoolSourceTile tile;

	private PoolSourceProvider(ManaPoolBlockEntity pool) {
		this.pool = pool;
		this.tile = new PoolSourceTile(pool);
	}

	static void onManaNetworkEvent(ManaNetworkEvent event) {
		if (event.getType() != ManaBlockType.POOL || event.getAction() != ManaNetworkAction.ADD
				|| !(event.getReceiver() instanceof ManaPoolBlockEntity pool)) {
			return;
		}
		Level level = pool.getLevel();
		if (level == null || level.isClientSide() || !REGISTERED.add(pool)) {
			return;
		}
		SourceManager.INSTANCE.addInterface(level, new PoolSourceProvider(pool));
	}

	@Override
	public ISourceTile getSource() {
		return tile;
	}

	@Override
	public boolean isValid() {
		return !pool.isRemoved() && pool.getLevel() != null;
	}

	@Override
	public BlockPos getCurrentPos() {
		return pool.getBlockPos();
	}
}
