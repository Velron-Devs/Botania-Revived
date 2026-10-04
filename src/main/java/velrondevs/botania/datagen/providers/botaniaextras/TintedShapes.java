package velrondevs.botania.datagen.providers.botaniaextras;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class TintedShapes {
	public static final ResourceLocation COLUMN = shape("tinted_cube_column");
	public static final ResourceLocation SLAB = shape("tinted_slab");
	public static final ResourceLocation SLAB_TOP = shape("tinted_slab_top");
	public static final ResourceLocation STAIRS = shape("tinted_stairs");
	public static final ResourceLocation STAIRS_INNER = shape("tinted_stairs_inner");
	public static final ResourceLocation STAIRS_OUTER = shape("tinted_stairs_outer");

	private TintedShapes() {}

	private static ResourceLocation shape(String name) {
		return prefix("block/botania_extras/shapes/" + name);
	}

	public static void register(BiConsumer<ResourceLocation, Supplier<JsonElement>> out) {
		out.accept(COLUMN, () -> model(
				new El(0, 0, 0, 16, 16, 16)
						.face("down", 0, 0, 16, 16, "end", "down")
						.face("up", 0, 0, 16, 16, "end", "up")
						.face("north", 0, 0, 16, 16, "side", "north")
						.face("south", 0, 0, 16, 16, "side", "south")
						.face("west", 0, 0, 16, 16, "side", "west")
						.face("east", 0, 0, 16, 16, "side", "east")));
		out.accept(SLAB, () -> model(bottomSlab()));
		out.accept(SLAB_TOP, () -> model(
				new El(0, 8, 0, 16, 16, 16)
						.face("down", 0, 0, 16, 16, "bottom", null)
						.face("up", 0, 0, 16, 16, "top", "up")
						.face("north", 0, 0, 16, 8, "side", "north")
						.face("south", 0, 0, 16, 8, "side", "south")
						.face("west", 0, 0, 16, 8, "side", "west")
						.face("east", 0, 0, 16, 8, "side", "east")));
		out.accept(STAIRS, () -> model(bottomSlab(),
				new El(8, 8, 0, 16, 16, 16)
						.face("up", 8, 0, 16, 16, "top", "up")
						.face("north", 0, 0, 8, 8, "side", "north")
						.face("south", 8, 0, 16, 8, "side", "south")
						.face("west", 0, 0, 16, 8, "side", null)
						.face("east", 0, 0, 16, 8, "side", "east")));
		out.accept(STAIRS_INNER, () -> model(bottomSlab(),
				new El(8, 8, 0, 16, 16, 16)
						.face("up", 8, 0, 16, 16, "top", "up")
						.face("north", 0, 0, 8, 8, "side", "north")
						.face("south", 8, 0, 16, 8, "side", "south")
						.face("west", 0, 0, 16, 8, "side", null)
						.face("east", 0, 0, 16, 8, "side", "east"),
				new El(0, 8, 8, 8, 16, 16)
						.face("up", 0, 8, 8, 16, "top", "up")
						.face("north", 8, 0, 16, 8, "side", null)
						.face("south", 0, 0, 8, 8, "side", "south")
						.face("west", 8, 0, 16, 8, "side", "west")));
		out.accept(STAIRS_OUTER, () -> model(bottomSlab(),
				new El(8, 8, 8, 16, 16, 16)
						.face("up", 8, 8, 16, 16, "top", "up")
						.face("north", 0, 0, 8, 8, "side", null)
						.face("south", 8, 0, 16, 8, "side", "south")
						.face("west", 8, 0, 16, 8, "side", null)
						.face("east", 0, 0, 8, 8, "side", "east")));
	}

	private static El bottomSlab() {
		return new El(0, 0, 0, 16, 8, 16)
				.face("down", 0, 0, 16, 16, "bottom", "down")
				.face("up", 0, 0, 16, 16, "top", null)
				.face("north", 0, 8, 16, 16, "side", "north")
				.face("south", 0, 8, 16, 16, "side", "south")
				.face("west", 0, 8, 16, 16, "side", "west")
				.face("east", 0, 8, 16, 16, "side", "east");
	}

	private static JsonObject model(El... elements) {
		JsonObject root = new JsonObject();
		root.addProperty("parent", "minecraft:block/block");
		JsonObject textures = new JsonObject();
		textures.addProperty("particle", "#side");
		root.add("textures", textures);
		JsonArray array = new JsonArray();
		for (El element : elements) {
			array.add(element.json());
		}
		root.add("elements", array);
		return root;
	}

	private static final class El {
		private final JsonObject json = new JsonObject();
		private final JsonObject faces = new JsonObject();

		El(int x1, int y1, int z1, int x2, int y2, int z2) {
			json.add("from", array(x1, y1, z1));
			json.add("to", array(x2, y2, z2));
			json.add("faces", faces);
		}

		El face(String direction, int u1, int v1, int u2, int v2, String texture, @Nullable String cull) {
			JsonObject face = new JsonObject();
			face.add("uv", array(u1, v1, u2, v2));
			face.addProperty("texture", "#" + texture);
			if (cull != null) {
				face.addProperty("cullface", cull);
			}
			face.addProperty("tintindex", 0);
			faces.add(direction, face);
			return this;
		}

		JsonObject json() {
			return json;
		}

		private static JsonArray array(int... values) {
			JsonArray array = new JsonArray();
			for (int value : values) {
				array.add(value);
			}
			return array;
		}
	}
}
