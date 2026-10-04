package velrondevs.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

public class ForcePushHelper implements AutoCloseable {

	private static final ThreadLocal<Deque<BlockPos>> forcePushOriginStack = ThreadLocal.withInitial(ArrayDeque::new);

	private static final ThreadLocal<Deque<Map.Entry<Boolean, Direction>>> movementTypeContextStack = ThreadLocal.withInitial(ArrayDeque::new);

	public static boolean isForcePush() {
		return !forcePushOriginStack.get().isEmpty();
	}

	public static BlockPos getForcePushOrigin() {
		if (!isForcePush()) {
			throw new IllegalStateException("Not currently performing a Force Relay or Force Lens push");
		}
		return forcePushOriginStack.get().peek();
	}

	public static void pushMovementTypeContext(boolean extending, Direction pushDir) {
		movementTypeContextStack.get().push(Map.entry(extending, pushDir));
	}

	public static void popMovementTypeContext() {
		movementTypeContextStack.get().pop();
	}

	public static boolean isExtendingMovementContext() {
		Map.Entry<Boolean, Direction> entry = movementTypeContextStack.get().peek();
		return entry == null || entry.getKey();
	}

	public static Direction getMovementContextDirection() {
		Map.Entry<Boolean, Direction> entry = movementTypeContextStack.get().peek();
		return entry != null ? entry.getValue() : null;
	}

	public ForcePushHelper(BlockPos pushLocation) {
		forcePushOriginStack.get().push(pushLocation.immutable());
	}

	@Override
	public void close() {
		forcePushOriginStack.get().pop();
	}
}
