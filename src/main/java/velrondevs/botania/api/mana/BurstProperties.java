package velrondevs.botania.api.mana;

public final class BurstProperties {

	public int maxMana;
	public int ticksBeforeManaLoss;
	public float manaLossPerTick;
	public float gravity;
	public float motionModifier;

	public int color;

	public BurstProperties(int maxMana, int ticksBeforeManaLoss, float manaLossPerTick, float gravity, float motionModifier, int color) {
		this.maxMana = maxMana;
		this.ticksBeforeManaLoss = ticksBeforeManaLoss;
		this.manaLossPerTick = manaLossPerTick;
		this.gravity = gravity;
		this.motionModifier = motionModifier;
		this.color = color;
	}

}
