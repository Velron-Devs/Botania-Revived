package velrondevs.botania.datagen.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModelWithOverrides {
	private final ResourceLocation parent;
	private final TextureSlot[] requiredTextures;

	public ModelWithOverrides(ResourceLocation parent, TextureSlot... requiredTextures) {
		this.parent = parent;
		this.requiredTextures = requiredTextures;
	}

	public void create(ResourceLocation modelId, TextureMapping textures, OverrideHolder overrides, BiConsumer<ResourceLocation, Supplier<JsonElement>> consumer) {
		Map<TextureSlot, ResourceLocation> textureMap = Streams.concat(Arrays.stream(this.requiredTextures), textures.getForced())
				.collect(ImmutableMap.toImmutableMap(Function.identity(), textures::get));
		consumer.accept(modelId, () -> {
			JsonObject ret = new JsonObject();
			ret.addProperty("parent", parent.toString());
			if (!textureMap.isEmpty()) {
				JsonObject textureJson = new JsonObject();
				textureMap.forEach((k, path) -> textureJson.addProperty(k.getId(), path.toString()));
				ret.add("textures", textureJson);
			}
			JsonArray overridesJson = overrides.toJson();
			if (overridesJson != null) {
				ret.add("overrides", overridesJson);
			}
			return ret;
		});
	}

}
