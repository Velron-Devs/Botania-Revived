package velrondevs.botania.coremod.sophisticated;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;

import net.neoforged.fml.loading.LoadingModList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

final class TankTierTransformer implements ITransformer<ClassNode> {
	private static final Logger LOGGER = LoggerFactory.getLogger("botania/tank_tiers");
	private static final String TARGET = "net.p3pp3rf1y.sophisticatedstorage.block.TankTier";
	private static final String STORAGE_MOD = "sophisticatedstorage";
	private static final String SUPPLIER = "java/util/function/Supplier";
	private static final String CONFIGS = "velrondevs/botania/compat/sophisticated/storage/BotaniaTankConfigs";
	private static final String[][] TIERS = {
			{"manasteel", null},
			{"elementium", null},
			{"terrasteel", null},
			{"gaia", null},
			{"alfsteel", "masterfulbotany"}
	};

	@Override
	public ClassNode transform(ClassNode node, ITransformerVotingContext context) {
		LoadingModList mods = LoadingModList.get();
		if (mods == null || mods.getModFileById(STORAGE_MOD) == null) {
			return node;
		}
		List<String> ids = new ArrayList<>();
		for (String[] tier : TIERS) {
			if (tier[1] == null || mods.getModFileById(tier[1]) != null) {
				ids.add(tier[0]);
			}
		}
		if (ids.isEmpty()) {
			return node;
		}
		String self = node.name;
		List<String> existing = new ArrayList<>();
		for (FieldNode field : node.fields) {
			if ((field.access & Opcodes.ACC_ENUM) != 0) {
				existing.add(field.name);
			}
		}
		for (String id : ids) {
			String constant = id.toUpperCase(Locale.ROOT);
			if (existing.contains(constant)) {
				return node;
			}
		}
		MethodNode clinit = find(node, "<clinit>");
		MethodNode values = find(node, "$values");
		if (clinit == null || values == null) {
			LOGGER.warn("TankTier has an unexpected layout, Botania tank tiers were not added");
			return node;
		}
		AbstractInsnNode anchor = null;
		for (AbstractInsnNode insn : clinit.instructions) {
			if (insn instanceof MethodInsnNode call && call.getOpcode() == Opcodes.INVOKESTATIC && call.name.equals("$values")) {
				anchor = insn;
				break;
			}
		}
		if (anchor == null) {
			LOGGER.warn("TankTier static initializer has an unexpected layout, Botania tank tiers were not added");
			return node;
		}
		String descriptor = "L" + self + ";";
		int ordinal = existing.size();
		InsnList created = new InsnList();
		List<String> all = new ArrayList<>(existing);
		for (String id : ids) {
			String constant = id.toUpperCase(Locale.ROOT);
			node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_ENUM, constant, descriptor, null, null));
			created.add(new TypeInsnNode(Opcodes.NEW, self));
			created.add(new InsnNode(Opcodes.DUP));
			created.add(new LdcInsnNode(constant));
			created.add(push(ordinal));
			created.add(new LdcInsnNode(id));
			created.add(new MethodInsnNode(Opcodes.INVOKESTATIC, CONFIGS, "supplier", "(Ljava/lang/String;)L" + SUPPLIER + ";", false));
			created.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, self, "<init>", "(Ljava/lang/String;IL" + SUPPLIER + ";)V", false));
			created.add(new FieldInsnNode(Opcodes.PUTSTATIC, self, constant, descriptor));
			all.add(constant);
			ordinal++;
		}
		clinit.instructions.insertBefore(anchor, created);
		clinit.maxStack = Math.max(clinit.maxStack, 8);
		InsnList body = new InsnList();
		body.add(push(all.size()));
		body.add(new TypeInsnNode(Opcodes.ANEWARRAY, self));
		for (int i = 0; i < all.size(); i++) {
			body.add(new InsnNode(Opcodes.DUP));
			body.add(push(i));
			body.add(new FieldInsnNode(Opcodes.GETSTATIC, self, all.get(i), descriptor));
			body.add(new InsnNode(Opcodes.AASTORE));
		}
		body.add(new InsnNode(Opcodes.ARETURN));
		values.instructions = body;
		values.tryCatchBlocks.clear();
		if (values.localVariables != null) {
			values.localVariables.clear();
		}
		values.maxStack = Math.max(values.maxStack, 4);
		LOGGER.info("Added Botania tank tiers to Sophisticated Storage: {}", ids);
		return node;
	}

	private static MethodNode find(ClassNode node, String name) {
		for (MethodNode method : node.methods) {
			if (method.name.equals(name)) {
				return method;
			}
		}
		return null;
	}

	private static AbstractInsnNode push(int value) {
		if (value >= -1 && value <= 5) {
			return new InsnNode(Opcodes.ICONST_0 + value);
		}
		return new IntInsnNode(Opcodes.BIPUSH, value);
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<ClassNode>> targets() {
		return Set.of(Target.targetClass(TARGET));
	}

	@Override
	public TargetType<ClassNode> getTargetType() {
		return TargetType.CLASS;
	}
}
