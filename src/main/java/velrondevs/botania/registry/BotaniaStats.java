package velrondevs.botania.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaStats {
	public static final ResourceLocation CORPOREA_ITEMS_REQUESTED = register("corporea_items_requested", StatFormatter.DEFAULT);

	public static final ResourceLocation LUMINIZER_ONE_CM = register("luminizer_one_cm", StatFormatter.DISTANCE);

	public static final ResourceLocation TINY_POTATOES_PETTED = register("tiny_potatoes_petted", StatFormatter.DEFAULT);

	private static ResourceLocation register(String name, StatFormatter formatter) {
		ResourceLocation id = prefix(name);
		Registry.register(BuiltInRegistries.CUSTOM_STAT, id, id);
		Stats.CUSTOM.get(id, formatter);
		return id;
	}

	public static void init() {

	}
}
