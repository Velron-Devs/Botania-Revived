package velrondevs.botania.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.source.ISourceTile;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

final class PoolSourceTile implements ISourceTile {
	private final ManaPoolBlockEntity pool;

	PoolSourceTile(ManaPoolBlockEntity pool) {
		this.pool = pool;
	}

	private static double ratio() {
		return ArsNouveauConfig.manaPerSource();
	}

	private static int toMana(int source) {
		return (int) Math.min(Integer.MAX_VALUE, Math.round(source * ratio()));
	}

	@Override
	public int getTransferRate() {
		return ArsNouveauConfig.maxTransferRate();
	}

	@Override
	public boolean canAcceptSource() {
		return pool.getAvailableSpaceForMana() >= ratio();
	}

	@Override
	public int getSource() {
		return (int) Math.min(Integer.MAX_VALUE, Math.floor(pool.getCurrentMana() / ratio()));
	}

	@Override
	public int getMaxSource() {
		return (int) Math.min(Integer.MAX_VALUE, Math.floor(pool.getMaxMana() / ratio()));
	}

	@Override
	public int setSource(int source) {
		int target = Math.max(0, Math.min(pool.getMaxMana(), toMana(source)));
		pool.receiveMana(target - pool.getCurrentMana());
		return getSource();
	}

	@Override
	public int addSource(int source, boolean simulate) {
		if (source <= 0) {
			return 0;
		}
		int space = (int) Math.min(Integer.MAX_VALUE, Math.floor(pool.getAvailableSpaceForMana() / ratio()));
		int accepted = Math.min(source, space);
		if (accepted > 0 && !simulate) {
			pool.receiveMana(toMana(accepted));
		}
		return Math.max(0, accepted);
	}

	@Override
	public int addSource(int source) {
		addSource(source, false);
		return getSource();
	}

	@Override
	public int removeSource(int source, boolean simulate) {
		if (source <= 0) {
			return 0;
		}
		int removed = Math.min(source, getSource());
		if (removed > 0 && !simulate) {
			pool.receiveMana(-toMana(removed));
		}
		return Math.max(0, removed);
	}

	@Override
	public int removeSource(int source) {
		removeSource(source, false);
		return getSource();
	}
}
