package velrondevs.botania.coremod.spectrum;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TargetType;
import cpw.mods.modlauncher.api.TransformerVoteResult;

import org.objectweb.asm.tree.ClassNode;

import java.util.HashSet;
import java.util.Set;

final class SpectrumTransformer implements ITransformer<ClassNode> {
	private final Set<Target<ClassNode>> targets = new HashSet<>();

	SpectrumTransformer(Set<String> classNames) {
		for (String name : classNames) {
			targets.add(Target.targetClass(name));
		}
	}

	@Override
	public ClassNode transform(ClassNode input, ITransformerVotingContext context) {
		return SpectrumRewriter.rewrite(input);
	}

	@Override
	public TransformerVoteResult castVote(ITransformerVotingContext context) {
		return TransformerVoteResult.YES;
	}

	@Override
	public Set<Target<ClassNode>> targets() {
		return targets;
	}

	@Override
	public TargetType<ClassNode> getTargetType() {
		return TargetType.CLASS;
	}
}
