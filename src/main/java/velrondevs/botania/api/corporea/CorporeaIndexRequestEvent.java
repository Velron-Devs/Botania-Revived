package velrondevs.botania.api.corporea;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;

public class CorporeaIndexRequestEvent extends Event implements ICancellableEvent {
	private final ServerPlayer requester;
	private final CorporeaRequestMatcher request;
	private final int requestCount;
	private final CorporeaSpark indexSpark;

	public CorporeaIndexRequestEvent(ServerPlayer requester, CorporeaRequestMatcher request, int requestCount, CorporeaSpark indexSpark) {
		this.requester = requester;
		this.request = request;
		this.requestCount = requestCount;
		this.indexSpark = indexSpark;
	}

	public ServerPlayer getRequester() {
		return requester;
	}

	public CorporeaRequestMatcher getMatcher() {
		return request;
	}

	public int getRequestCount() {
		return requestCount;
	}

	public CorporeaSpark getIndexSpark() {
		return indexSpark;
	}
}
