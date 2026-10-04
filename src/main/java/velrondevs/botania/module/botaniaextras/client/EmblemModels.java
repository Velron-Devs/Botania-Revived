package velrondevs.botania.module.botaniaextras.client;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class EmblemModels {
	public static final List<String> NAMES = List.of("thor", "sif", "njord", "aesir");

	private static final Map<ResourceLocation, BakedModel> MODELS = new HashMap<>();

	private EmblemModels() {}

	public static ResourceLocation id(String name) {
		return prefix("icon/botania_extras/priest_emblem_" + name);
	}

	public static void register(ModelEvent.RegisterAdditional e) {
		for (String name : NAMES) {
			e.register(ModelResourceLocation.standalone(id(name)));
		}
	}

	public static void bake(ModelEvent.ModifyBakingResult e) {
		MODELS.clear();
		for (String name : NAMES) {
			BakedModel model = e.getModels().get(ModelResourceLocation.standalone(id(name)));
			if (model != null) {
				MODELS.put(id(name), model);
			}
		}
	}

	@Nullable
	public static BakedModel get(ResourceLocation id) {
		return MODELS.get(id);
	}
}
