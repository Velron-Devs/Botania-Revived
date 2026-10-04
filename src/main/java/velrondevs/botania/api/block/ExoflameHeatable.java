package velrondevs.botania.api.block;

public interface ExoflameHeatable {

	boolean canSmelt();

	int getBurnTime();

	void boostBurnTime();

	void boostCookTime();
}
