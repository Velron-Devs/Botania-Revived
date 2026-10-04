package velrondevs.botania.api.corporea;

import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class CorporeaRequestEvent extends Event implements ICancellableEvent {

	private final CorporeaRequestMatcher matcher;
	private final int count;
	private final CorporeaSpark spark;
	private final boolean dryRun;

	public CorporeaRequestEvent(CorporeaRequestMatcher matcher, int count, CorporeaSpark spark, boolean dryRun) {
		this.matcher = matcher;
		this.count = count;
		this.spark = spark;
		this.dryRun = dryRun;
	}

	public CorporeaRequestMatcher getMatcher() {
		return matcher;
	}

	public int getCount() {
		return count;
	}

	public CorporeaSpark getSpark() {
		return spark;
	}

	public boolean isDryRun() {
		return dryRun;
	}
}
