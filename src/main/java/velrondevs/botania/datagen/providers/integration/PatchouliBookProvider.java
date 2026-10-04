package velrondevs.botania.datagen.providers.integration;

import com.google.gson.JsonObject;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class PatchouliBookProvider extends JsonFileProvider {
	public PatchouliBookProvider(PackOutput output) {
		super(output, PackOutput.Target.DATA_PACK, "patchouli_books", "Botania Patchouli book");
	}

	@Override
	protected void addFiles(Map<ResourceLocation, JsonObject> files) {
		JsonObject book = new JsonObject();
		book.addProperty("name", "item.botania.lexicon");
		book.addProperty("landing_text", "botania.landing");
		book.addProperty("version", "buildNumber");
		book.addProperty("book_texture", "patchouli:textures/gui/book_green.png");
		book.addProperty("model", "botania:lexicon");
		book.addProperty("open_sound", "botania:lexicon_open");
		book.addProperty("flip_sound", "botania:lexicon_page");
		book.addProperty("creative_tab", "botania");
		book.addProperty("dont_generate_book", true);
		book.addProperty("custom_book_item", "botania:lexicon");
		book.addProperty("advancements_tab", "botania:main/root");
		book.addProperty("use_resource_pack", true);
		book.addProperty("i18n", true);
		JsonObject macros = new JsonObject();
		macros.addProperty("$(item)", "$(1)");
		macros.addProperty("$(thing)", "$(4)");
		book.add("macros", macros);
		files.put(prefix("lexicon/book"), book);
	}
}
