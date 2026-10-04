package velrondevs.botania.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.source.ISourceCap;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

final class PoolSourceCap implements ISourceCap {
	private final ManaPoolBlockEntity pool;

	PoolSourceCap(ManaPoolBlockEntity pool) {
		this.pool = pool;
	}

	private static double ratio() {
		return ArsNouveauConfig.manaPerSource();
	}

	private static int toMana(int source) {
		return (int) Math.min(Integer.MAX_VALUE, Math.round(source * ratio()));
	}

	@Override
	public boolean canAcceptSource(int source) {
		return receiveSource(source, true) > 0;
	}

	@Override
	public boolean canProvideSource(int source) {
		return extractSource(source, true) > 0;
	}

	@Override
	public int getMaxExtract() {
		return ArsNouveauConfig.maxTransferRate();
	}

	@Override
	public int getMaxReceive() {
		return ArsNouveauConfig.maxTransferRate();
	}

	@Override
	public int getSource() {
		return (int) Math.min(Integer.MAX_VALUE, Math.floor(pool.getCurrentMana() / ratio()));
	}

	@Override
	public int getSourceCapacity() {
		return (int) Math.min(Integer.MAX_VALUE, Math.floor(pool.getMaxMana() / ratio()));
	}

	@Override
	public void setSource(int source) {
		int target = Math.max(0, Math.min(pool.getMaxMana(), toMana(source)));
		pool.receiveMana(target - pool.getCurrentMana());
	}

	@Override
	public void setMaxSource(int max) {}

	@Override
	public int receiveSource(int source, boolean simulate) {
		if (source <= 0) {
			return 0;
		}
		int space = (int) Math.min(Integer.MAX_VALUE, Math.floor(pool.getAvailableSpaceForMana() / ratio()));
		int accepted = Math.min(Math.min(source, getMaxReceive()), space);
		if (accepted <= 0) {
			return 0;
		}
		if (!simulate) {
			pool.receiveMana(toMana(accepted));
		}
		return accepted;
	}

	@Override
	public int extractSource(int source, boolean simulate) {
		if (source <= 0) {
			return 0;
		}
		int extracted = Math.min(Math.min(source, getMaxExtract()), getSource());
		if (extracted <= 0) {
			return 0;
		}
		if (!simulate) {
			pool.receiveMana(-toMana(extracted));
		}
		return extracted;
	}
}
