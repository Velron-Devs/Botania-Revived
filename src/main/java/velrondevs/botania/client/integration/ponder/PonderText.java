package velrondevs.botania.client.integration.ponder;

public final class PonderText {
	private final String scene;
	private int index;

	public PonderText(String scene) {
		this.scene = scene;
	}

	public String next() {
		return PonderStrings.body(scene, ++index);
	}
}
