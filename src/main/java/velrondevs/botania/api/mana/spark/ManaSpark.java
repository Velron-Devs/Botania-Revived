package velrondevs.botania.api.mana.spark;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.item.SparkEntity;
import velrondevs.botania.api.mana.ManaReceiver;

import java.util.Collection;

public interface ManaSpark extends SparkEntity {

	@Nullable
	SparkAttachable getAttachedTile();

	@Nullable
	ManaReceiver getAttachedManaReceiver();

	Collection<ManaSpark> getOutgoingTransfers();

	void registerTransfer(ManaSpark entity);

	default void checkReceiverFull() {}

	void updateTransfers();

	SparkUpgradeType getUpgrade();

	void setUpgrade(SparkUpgradeType upgrade);

	boolean areIncomingTransfersDone();
}
