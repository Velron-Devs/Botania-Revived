package velrondevs.botania.api.mana;

import net.neoforged.bus.api.Event;

public class ManaNetworkEvent extends Event {
	private final ManaReceiver thing;
	private final ManaBlockType type;
	private final ManaNetworkAction action;

	public ManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		this.thing = thing;
		this.type = type;
		this.action = action;
	}

	public ManaReceiver getReceiver() {
		return thing;
	}

	public ManaBlockType getType() {
		return type;
	}

	public ManaNetworkAction getAction() {
		return action;
	}
}
