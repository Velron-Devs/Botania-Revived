package velrondevs.botania.common.integration.corporea;

import net.minecraft.Util;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.corporea.CorporeaNode;
import velrondevs.botania.api.corporea.CorporeaNodeDetector;
import velrondevs.botania.api.corporea.CorporeaSpark;
import velrondevs.botania.common.impl.corporea.DummyCorporeaNode;

import java.util.*;

public class CorporeaNodeDetectors {

	private static final Deque<CorporeaNodeDetector> DETECTORS = Util.make(new ArrayDeque<>(), d -> {
		d.addLast(new VanillaNodeDetector());
	});

	public static synchronized void register(CorporeaNodeDetector detector) {
		DETECTORS.addFirst(detector);
	}

	public static CorporeaNode findNode(Level world, CorporeaSpark spark) {
		for (CorporeaNodeDetector detector : DETECTORS) {
			CorporeaNode node = detector.getNode(world, spark);
			if (node != null) {
				return node;
			}
		}
		return new DummyCorporeaNode(world, spark.getAttachPos(), spark);
	}
}
