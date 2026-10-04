package velrondevs.botania.datagen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.resources.ResourceLocation;

public class SimpleModelSupplierWithOverrides extends DelegatedModel {
	private final OverrideHolder overrides;

	public SimpleModelSupplierWithOverrides(ResourceLocation parent, OverrideHolder overrides) {
		super(parent);
		this.overrides = overrides;
	}

	@Override
	public JsonElement get() {
		JsonObject obj = (JsonObject) super.get();
		JsonArray overridesJson = overrides.toJson();
		if (overridesJson != null) {
			obj.add("overrides", overridesJson);
		}
		return obj;
	}
}
