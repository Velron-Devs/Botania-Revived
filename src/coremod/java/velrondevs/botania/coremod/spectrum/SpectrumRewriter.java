package velrondevs.botania.coremod.spectrum;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.MethodRemapper;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.HashMap;
import java.util.Map;

public final class SpectrumRewriter {
	public static final String SOURCE_ROOT = "vazkii/botania/";
	public static final String TARGET_ROOT = "velrondevs/botania/";
	public static final String COMPAT = "velrondevs/botania/compat/spectrum/";

	private static final Map<String, String> CLASSES = new HashMap<>();
	private static final Map<String, Redirect> METHOD_REDIRECTS = new HashMap<>();

	private record Redirect(int opcode, String owner, String name, String desc, boolean itf) {}

	static {
		CLASSES.put("vazkii/botania/common/handler/BotaniaSounds", TARGET_ROOT + "registry/BotaniaSounds");
		CLASSES.put("vazkii/botania/network/clientbound/BlackLotusDissolveEffectPacket", COMPAT + "BlackLotusDissolveEffectPacket");

		String entity = "Lnet/minecraft/world/entity/Entity;";
		String payload = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;";
		METHOD_REDIRECTS.put("vazkii/botania/xplat/XplatAbstractions.sendToTracking(" + entity + payload + ")V",
				new Redirect(Opcodes.INVOKESTATIC, COMPAT + "SpectrumBridge", "sendToTracking",
						"(L" + TARGET_ROOT + "xplat/XplatAbstractions;" + entity + payload + ")V", false));
	}

	private SpectrumRewriter() {}

	public static String mapClass(String internalName) {
		String mapped = CLASSES.get(internalName);
		if (mapped != null) {
			return mapped;
		}
		if (internalName.startsWith(SOURCE_ROOT)) {
			return TARGET_ROOT + internalName.substring(SOURCE_ROOT.length());
		}
		return internalName;
	}

	public static ClassNode rewrite(ClassNode input) {
		ClassNode output = new ClassNode();
		input.accept(new Remap(output));
		return output;
	}

	private static final class Mapper extends Remapper {
		@Override
		public String map(String internalName) {
			return mapClass(internalName);
		}
	}

	private static final class Remap extends ClassRemapper {
		Remap(ClassVisitor next) {
			super(Opcodes.ASM9, next, new Mapper());
		}

		@Override
		protected MethodVisitor createMethodRemapper(MethodVisitor methodVisitor) {
			return new MethodRemap(methodVisitor, remapper);
		}
	}

	private static final class MethodRemap extends MethodRemapper {
		MethodRemap(MethodVisitor next, Remapper remapper) {
			super(Opcodes.ASM9, next, remapper);
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
			Redirect redirect = METHOD_REDIRECTS.get(owner + "." + name + descriptor);
			if (redirect != null) {
				mv.visitMethodInsn(redirect.opcode(), redirect.owner(), redirect.name(), redirect.desc(), redirect.itf());
				return;
			}
			super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
		}
	}
}
