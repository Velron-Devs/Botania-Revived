package velrondevs.botania.api.configdata;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;

import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Optional;

public final class LooniumMobSpawnList {
	public static final Codec<LooniumMobSpawnList> CODEC = LooniumMobSpawnData.CODEC.listOf()
			.xmap(LooniumMobSpawnList::create, LooniumMobSpawnList::unwrap);

	private final List<LooniumMobSpawnData> entries;
	private final int totalWeight;

	private LooniumMobSpawnList(List<LooniumMobSpawnData> entries) {
		this.entries = ImmutableList.copyOf(entries);
		int total = 0;
		for (LooniumMobSpawnData entry : this.entries) {
			total += entry.getWeight();
		}
		this.totalWeight = total;
	}

	public static LooniumMobSpawnList create(List<LooniumMobSpawnData> entries) {
		return new LooniumMobSpawnList(entries);
	}

	public static LooniumMobSpawnList create(LooniumMobSpawnData... entries) {
		return new LooniumMobSpawnList(List.of(entries));
	}

	public boolean isEmpty() {
		return entries.isEmpty();
	}

	public List<LooniumMobSpawnData> unwrap() {
		return entries;
	}

	public Optional<LooniumMobSpawnData> getRandom(RandomSource random) {
		if (totalWeight <= 0) {
			return Optional.empty();
		}
		int selection = random.nextInt(totalWeight);
		for (LooniumMobSpawnData entry : entries) {
			selection -= entry.getWeight();
			if (selection < 0) {
				return Optional.of(entry);
			}
		}
		return Optional.empty();
	}

	@Override
	public String toString() {
		return entries.toString();
	}
}
