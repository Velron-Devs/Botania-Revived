package velrondevs.botania.api.mana;

public interface ManaCollisionGhost {
	enum Behaviour {

		SKIP_ALL,

		RUN_ALL,

		RUN_RECEIVER_TRIGGER,
	}

	Behaviour getGhostBehaviour();

}
