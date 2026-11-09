package velrondevs.botania.datagen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;

import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JsonModelBuilder {
	private final Identifier id;
	@Nullable
	private String parent;
	@Nullable
	private Boolean ambientOcclusion;
	private final Map<String, String> textures = new LinkedHashMap<>();
	private final List<ElementBuilder> elements = new ArrayList<>();
	private final TransformsBuilder transforms = new TransformsBuilder();

	public JsonModelBuilder(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}

	public JsonModelBuilder parent(String parent) {
		this.parent = parent;
		return this;
	}

	public JsonModelBuilder parent(Identifier parent) {
		return parent(parent.toString());
	}

	public JsonModelBuilder texture(String key, String texture) {
		textures.put(key, texture);
		return this;
	}

	public JsonModelBuilder texture(String key, Identifier texture) {
		return texture(key, texture.toString());
	}

	public JsonModelBuilder ao(boolean ambientOcclusion) {
		this.ambientOcclusion = ambientOcclusion;
		return this;
	}

	public ElementBuilder element() {
		ElementBuilder element = new ElementBuilder();
		elements.add(element);
		return element;
	}

	public TransformsBuilder transforms() {
		return transforms;
	}

	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		if (parent != null) {
			json.addProperty("parent", parent);
		}
		if (ambientOcclusion != null) {
			json.addProperty("ambientocclusion", ambientOcclusion);
		}
		if (!transforms.transforms.isEmpty()) {
			JsonObject display = new JsonObject();
			transforms.transforms.forEach((ctx, vec) -> display.add(ctx.getSerializedName(), vec.toJson()));
			json.add("display", display);
		}
		if (!textures.isEmpty()) {
			JsonObject textureJson = new JsonObject();
			textures.forEach(textureJson::addProperty);
			json.add("textures", textureJson);
		}
		if (!elements.isEmpty()) {
			JsonArray elementJson = new JsonArray();
			elements.forEach(e -> elementJson.add(e.toJson()));
			json.add("elements", elementJson);
		}
		return json;
	}

	private static JsonArray vec(float... values) {
		JsonArray array = new JsonArray();
		for (float value : values) {
			if (value == (int) value) {
				array.add((int) value);
			} else {
				array.add(value);
			}
		}
		return array;
	}

	public enum FaceRotation {
		ZERO(0),
		CLOCKWISE_90(90),
		UPSIDE_DOWN(180),
		COUNTERCLOCKWISE_90(270);

		private final int rotation;

		FaceRotation(int rotation) {
			this.rotation = rotation;
		}
	}

	public class ElementBuilder {
		private float[] from = { 0, 0, 0 };
		private float[] to = { 16, 16, 16 };
		private boolean shade = true;
		private final Map<Direction, FaceBuilder> faces = new EnumMap<>(Direction.class);
		@Nullable
		private RotationBuilder rotation;

		public ElementBuilder from(float x, float y, float z) {
			this.from = new float[] { x, y, z };
			return this;
		}

		public ElementBuilder to(float x, float y, float z) {
			this.to = new float[] { x, y, z };
			return this;
		}

		public ElementBuilder shade(boolean shade) {
			this.shade = shade;
			return this;
		}

		public FaceBuilder face(Direction direction) {
			return faces.computeIfAbsent(direction, d -> new FaceBuilder(this));
		}

		public RotationBuilder rotation() {
			if (rotation == null) {
				rotation = new RotationBuilder(this);
			}
			return rotation;
		}

		public JsonModelBuilder end() {
			return JsonModelBuilder.this;
		}

		private JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.add("from", vec(from));
			json.add("to", vec(to));
			if (rotation != null) {
				json.add("rotation", rotation.toJson());
			}
			if (!shade) {
				json.addProperty("shade", false);
			}
			JsonObject faceJson = new JsonObject();
			faces.forEach((dir, face) -> faceJson.add(dir.getSerializedName(), face.toJson()));
			json.add("faces", faceJson);
			return json;
		}
	}

	public static class FaceBuilder {
		private final ElementBuilder parent;
		@Nullable
		private String texture;
		private float @Nullable [] uvs;
		@Nullable
		private Direction cullface;
		private FaceRotation rotation = FaceRotation.ZERO;
		private int tintIndex = -1;

		private FaceBuilder(ElementBuilder parent) {
			this.parent = parent;
		}

		public FaceBuilder texture(String texture) {
			this.texture = texture;
			return this;
		}

		public FaceBuilder uvs(float u1, float v1, float u2, float v2) {
			this.uvs = new float[] { u1, v1, u2, v2 };
			return this;
		}

		public FaceBuilder cullface(@Nullable Direction cullface) {
			this.cullface = cullface;
			return this;
		}

		public FaceBuilder rotation(FaceRotation rotation) {
			this.rotation = rotation;
			return this;
		}

		public FaceBuilder tintindex(int tintIndex) {
			this.tintIndex = tintIndex;
			return this;
		}

		public ElementBuilder end() {
			return parent;
		}

		private JsonObject toJson() {
			JsonObject json = new JsonObject();
			if (uvs != null) {
				json.add("uv", vec(uvs));
			}
			if (texture == null) {
				throw new IllegalStateException("Face without texture");
			}
			json.addProperty("texture", texture);
			if (cullface != null) {
				json.addProperty("cullface", cullface.getSerializedName());
			}
			if (rotation != FaceRotation.ZERO) {
				json.addProperty("rotation", rotation.rotation);
			}
			if (tintIndex != -1) {
				json.addProperty("tintindex", tintIndex);
			}
			return json;
		}
	}

	public static class RotationBuilder {
		private final ElementBuilder parent;
		private float[] origin = { 8, 8, 8 };
		private Direction.Axis axis = Direction.Axis.Y;
		private float angle;
		private boolean rescale;

		private RotationBuilder(ElementBuilder parent) {
			this.parent = parent;
		}

		public RotationBuilder origin(float x, float y, float z) {
			this.origin = new float[] { x, y, z };
			return this;
		}

		public RotationBuilder axis(Direction.Axis axis) {
			this.axis = axis;
			return this;
		}

		public RotationBuilder angle(float angle) {
			this.angle = angle;
			return this;
		}

		public RotationBuilder rescale(boolean rescale) {
			this.rescale = rescale;
			return this;
		}

		public ElementBuilder end() {
			return parent;
		}

		private JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.add("origin", vec(origin));
			json.addProperty("axis", axis.getSerializedName().toLowerCase(Locale.ROOT));
			json.add("angle", vec(angle).get(0));
			if (rescale) {
				json.addProperty("rescale", true);
			}
			return json;
		}
	}

	public class TransformsBuilder {
		private final Map<ItemDisplayContext, TransformVecBuilder> transforms = new LinkedHashMap<>();

		public TransformVecBuilder transform(ItemDisplayContext context) {
			return transforms.computeIfAbsent(context, c -> new TransformVecBuilder(this));
		}

		public JsonModelBuilder end() {
			return JsonModelBuilder.this;
		}
	}

	public static class TransformVecBuilder {
		private final TransformsBuilder parent;
		private float[] rotation = { 0, 0, 0 };
		private float[] translation = { 0, 0, 0 };
		private float[] scale = { 1, 1, 1 };

		private TransformVecBuilder(TransformsBuilder parent) {
			this.parent = parent;
		}

		public TransformVecBuilder rotation(float x, float y, float z) {
			this.rotation = new float[] { x, y, z };
			return this;
		}

		public TransformVecBuilder translation(float x, float y, float z) {
			this.translation = new float[] { x, y, z };
			return this;
		}

		public TransformVecBuilder scale(float x, float y, float z) {
			this.scale = new float[] { x, y, z };
			return this;
		}

		public TransformVecBuilder scale(float scale) {
			return scale(scale, scale, scale);
		}

		public TransformsBuilder end() {
			return parent;
		}

		private JsonObject toJson() {
			JsonObject json = new JsonObject();
			json.add("rotation", vec(rotation));
			json.add("translation", vec(translation));
			json.add("scale", vec(scale));
			return json;
		}
	}
}
