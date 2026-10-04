package velrondevs.botania.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.math.Transformation;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import org.jetbrains.annotations.NotNull;


import java.util.function.Function;

public class ManaBlasterGeometry implements IUnbakedGeometry<ManaBlasterGeometry> {
	private final ResourceLocation gunNoClip, gunClip;

	public ManaBlasterGeometry(ResourceLocation gunNoClip, ResourceLocation gunClip) {
		this.gunNoClip = gunNoClip;
		this.gunClip = gunClip;
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		modelGetter.apply(this.gunNoClip).resolveParents(modelGetter);
		modelGetter.apply(this.gunClip).resolveParents(modelGetter);
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter,
			ModelState modelState, ItemOverrides overrides) {
		Transformation transform = context.getRootTransform();
		ModelState state = new ModelState() {
			@NotNull
			@Override
			public Transformation getRotation() {
				return transform;
			}
		};
		return ManaBlasterBakedModel.create(baker, this.gunNoClip, this.gunClip, state);
	}

	public enum Loader implements IGeometryLoader<ManaBlasterGeometry> {
		INSTANCE;

		@NotNull
		@Override
		public ManaBlasterGeometry read(JsonObject json, JsonDeserializationContext deserializationContext) {
			return new ManaBlasterGeometry(
					ResourceLocation.parse(GsonHelper.getAsString(json, "gun_noclip")),
					ResourceLocation.parse(GsonHelper.getAsString(json, "gun_clip"))
			);
		}
	}

}
