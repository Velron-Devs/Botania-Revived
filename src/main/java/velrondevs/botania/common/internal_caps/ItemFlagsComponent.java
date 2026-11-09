package velrondevs.botania.common.internal_caps;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class ItemFlagsComponent extends SerializableComponent {
	public boolean elvenPortalSpawned = false;
	public boolean apothecarySpawned = false;
	public boolean manaInfusionSpawned = false;
	public boolean runicAltarSpawned = false;

	public int timeCounter = 0;

	private static final String TAG_PORTAL_SPAWNED = "ElvenPortalSpawned";
	private static final String TAG_APOTHECARY_SPAWNED = "ApothecarySpawned";
	private static final String TAG_INFUSION_SPAWNED = "ManaInfusionSpawned";
	private static final String TAG_ALTAR_SPAWNED = "RunicAltarSpawned";

	private static final String TAG_TIME_COUNTER = "timeCounter";

	@Override
	public void readFromNbt(CompoundTag tag, HolderLookup.Provider registries) {
		elvenPortalSpawned = tag.getBooleanOr(TAG_PORTAL_SPAWNED, false);
		apothecarySpawned = tag.getBooleanOr(TAG_APOTHECARY_SPAWNED, false);
		manaInfusionSpawned = tag.getBooleanOr(TAG_INFUSION_SPAWNED, false);
		runicAltarSpawned = tag.getBooleanOr(TAG_ALTAR_SPAWNED, false);
		timeCounter = tag.getIntOr(TAG_TIME_COUNTER, 0);

		if (tag.getBooleanOr("_elvenPortal", false)) {
			elvenPortalSpawned = true;
		}
		if (tag.getIntOr("manaInfusionCooldown", 0) > 0) {
			manaInfusionSpawned = true;
		}
		if (tag.getIntOr("runicAltarCooldown", 0) > 0) {
			runicAltarSpawned = true;
		}
	}

	@Override
	public void writeToNbt(CompoundTag tag, HolderLookup.Provider registries) {
		tag.putBoolean(TAG_PORTAL_SPAWNED, elvenPortalSpawned);
		tag.putBoolean(TAG_APOTHECARY_SPAWNED, apothecarySpawned);
		tag.putBoolean(TAG_INFUSION_SPAWNED, manaInfusionSpawned);
		tag.putBoolean(TAG_ALTAR_SPAWNED, runicAltarSpawned);
		tag.putInt(TAG_TIME_COUNTER, timeCounter);
	}

	public void tick() {
		timeCounter++;
	}

	public boolean spawnedByInWorldRecipe() {
		return elvenPortalSpawned || apothecarySpawned || runicAltarSpawned || manaInfusionSpawned;
	}
}
