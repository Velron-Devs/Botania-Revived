package velrondevs.botania.integration.arsnouveau;

import com.hollingsworth.arsnouveau.api.source.AbstractSourceMachine;
import com.hollingsworth.arsnouveau.api.source.ISourceCap;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.mana.ManaPool;

import java.util.Optional;

final class SourceManaReceiver implements ManaPool {
	private final AbstractSourceMachine machine;

	SourceManaReceiver(AbstractSourceMachine machine) {
		this.machine = machine;
	}

	private ISourceCap cap() {
		return machine.getSourceStorage();
	}

	private double ratio() {
		return ArsNouveauConfig.manaPerSource();
	}

	private double remainder() {
		return machine.getData(ArsNouveauAttachments.MANA_REMAINDER);
	}

	private void setRemainder(double value) {
		if (value != remainder()) {
			machine.setData(ArsNouveauAttachments.MANA_REMAINDER, value);
		}
	}

	@Override
	public Level getManaReceiverLevel() {
		return machine.getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return machine.getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return (int) Math.min(Integer.MAX_VALUE, Math.round(cap().getSource() * ratio() + remainder()));
	}

	@Override
	public int getMaxMana() {
		return (int) Math.min(Integer.MAX_VALUE, Math.round(cap().getSourceCapacity() * ratio()));
	}

	@Override
	public boolean isFull() {
		return !cap().canAcceptSource(1);
	}

	@Override
	public void receiveMana(int mana) {
		if (mana == 0) {
			return;
		}
		double ratio = ratio();
		ISourceCap cap = cap();
		if (mana > 0) {
			double total = remainder() + mana;
			int sources = (int) Math.floor(total / ratio);
			if (sources > 0) {
				int accepted = cap.receiveSource(sources, false);
				setRemainder(accepted < sources ? 0.0 : total - sources * ratio);
			} else {
				setRemainder(total);
			}
		} else {
			double total = remainder() + mana;
			if (total >= 0) {
				setRemainder(total);
				return;
			}
			int needed = (int) Math.ceil(-total / ratio);
			int extracted = cap.extractSource(needed, false);
			setRemainder(Math.max(0.0, total + extracted * ratio));
		}
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return true;
	}

	@Override
	public boolean isOutputtingPower() {
		return false;
	}

	@Override
	public Optional<DyeColor> getColor() {
		return Optional.of(DyeColor.PURPLE);
	}

	@Override
	public void setColor(Optional<DyeColor> color) {}
}
