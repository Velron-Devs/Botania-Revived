package velrondevs.botania.client.integration.ponder;

final class PonderText {
	private final String scene;
	private int index;

	PonderText(String scene) {
		this.scene = scene;
	}

	String next() {
		return PonderStrings.body(scene, ++index);
	}
}
