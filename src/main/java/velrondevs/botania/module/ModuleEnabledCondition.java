package velrondevs.botania.module;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.neoforged.neoforge.common.conditions.ICondition;

public record ModuleEnabledCondition(String module) implements ICondition {
	public static final MapCodec<ModuleEnabledCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			Codec.STRING.fieldOf("module").forGetter(ModuleEnabledCondition::module)
	).apply(instance, ModuleEnabledCondition::new));

	@Override
	public boolean test(IContext context) {
		return BotaniaModules.isEnabled(module);
	}

	@Override
	public MapCodec<? extends ICondition> codec() {
		return CODEC;
	}

	@Override
	public String toString() {
		return "module_enabled(\"" + module + "\")";
	}
}
