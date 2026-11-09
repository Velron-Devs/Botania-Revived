package velrondevs.botania.client.integration.rrv;

import java.util.ArrayList;
import java.util.List;

public final class BotaniaRrvLayout {
	private final int offsetX;
	private final int offsetY;
	private final List<int[]> positions = new ArrayList<>();
	private final List<Group> groups = new ArrayList<>();

	private record Group(List<Integer> slots, boolean ring) {}

	public BotaniaRrvLayout(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public int fixed(int x, int y) {
		positions.add(new int[] { x + offsetX, y + offsetY });
		return positions.size() - 1;
	}

	public BotaniaRrvLayout ring(int cx, int cy, int radius, int count) {
		List<Integer> slots = new ArrayList<>();
		double step = 360.0 / count;
		for (int i = 0; i < count; i++) {
			int x = rotateX(cx, cy - radius, cx, cy, step * i) + offsetX;
			int y = rotateY(cx, cy - radius, cx, cy, step * i) + offsetY;
			if (!overlaps(x, y)) {
				positions.add(new int[] { x, y });
				slots.add(positions.size() - 1);
			}
		}
		groups.add(new Group(slots, true));
		return this;
	}

	public BotaniaRrvLayout row(int x, int y, int count) {
		List<Integer> slots = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			positions.add(new int[] { x + offsetX + 18 * i, y + offsetY });
			slots.add(positions.size() - 1);
		}
		groups.add(new Group(slots, false));
		return this;
	}

	private boolean overlaps(int x, int y) {
		for (int[] pos : positions) {
			if (Math.abs(pos[0] - x) < 16 && Math.abs(pos[1] - y) < 16) {
				return true;
			}
		}
		return false;
	}

	public int size() {
		return positions.size();
	}

	public int[] position(int index) {
		return positions.get(index);
	}

	public List<Integer> placeRow(int row, int amount, boolean centered) {
		List<Group> rows = groups.stream().filter(g -> !g.ring()).toList();
		if (amount <= 0 || row >= rows.size()) {
			return List.of();
		}
		List<Integer> slots = rows.get(row).slots();
		int count = Math.min(amount, slots.size());
		int start = centered ? (slots.size() - count) / 2 : 0;
		return slots.subList(start, start + count);
	}

	public List<Integer> placeRing(int amount) {
		List<Group> rings = groups.stream().filter(Group::ring).toList();
		if (amount <= 0 || rings.isEmpty()) {
			return List.of();
		}
		Group chosen = null;
		for (Group group : rings) {
			if (group.slots().size() >= amount) {
				chosen = group;
				break;
			}
		}
		if (chosen == null) {
			chosen = rings.get(0);
			for (Group group : rings) {
				if (group.slots().size() > chosen.slots().size()) {
					chosen = group;
				}
			}
		}
		List<Integer> slots = chosen.slots();
		int count = Math.min(amount, slots.size());
		List<Integer> result = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			result.add(slots.get((int) Math.round((double) i * slots.size() / count) % slots.size()));
		}
		return result;
	}

	public static int rotateX(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.cos(rad) * (x - cx) - Math.sin(rad) * (y - cy) + cx);
	}

	public static int rotateY(int x, int y, int cx, int cy, double degrees) {
		double rad = Math.toRadians(degrees);
		return (int) (Math.sin(rad) * (x - cx) - Math.cos(rad) * (y - cy) + cy);
	}
}
