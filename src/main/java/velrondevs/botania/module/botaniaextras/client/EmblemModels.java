package velrondevs.botania.module.botaniaextras.client;

import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.client.core.handler.MiscellaneousModels;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class EmblemModels {
	public static final List<String> NAMES = List.of("thor", "sif", "njord", "aesir");

	private static final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> KEYS = new LinkedHashMap<>();
	private static final Map<Identifier, BlockStateModelPart> MODELS = new HashMap<>();

	private EmblemModels() {}

	public static Identifier id(String name) {
		return prefix("icon/botania_extras/priest_emblem_" + name);
	}

	public static void register(ModelEvent.RegisterStandalone e) {
		KEYS.clear();
		for (String name : NAMES) {
			Identifier id = id(name);
			StandaloneModelKey<BlockStateModelPart> key = new StandaloneModelKey<>(id::toString);
			KEYS.put(id, key);
			e.register(key, MiscellaneousModels.iconModel(id));
		}
	}

	public static void bake(ModelEvent.BakingCompleted e) {
		MODELS.clear();
		KEYS.forEach((id, key) -> {
			BlockStateModelPart model = e.getModelManager().getStandaloneModel(key);
			if (model != null) {
				MODELS.put(id, model);
			}
		});
	}

	@Nullable
	public static BlockStateModelPart get(Identifier id) {
		return MODELS.get(id);
	}
}
