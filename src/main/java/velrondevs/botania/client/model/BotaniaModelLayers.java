package velrondevs.botania.client.model;

import net.minecraft.client.model.geom.ModelLayerLocation;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaModelLayers {
	public static final ModelLayerLocation AVATAR = make("avatar");
	public static final ModelLayerLocation BELLOWS = make("bellows");
	public static final ModelLayerLocation BREWERY = make("brewery");
	public static final ModelLayerLocation BOTANIA_BOOK = make("botania_book");
	public static final ModelLayerLocation CLOAK = make("cloak");
	public static final ModelLayerLocation CORPOREA_INDEX = make("corporea_index");
	public static final ModelLayerLocation HOURGLASS = make("hourglass");
	public static final ModelLayerLocation ELEMENTIUM_INNER_ARMOR = make("elementium_armor", "inner_armor");
	public static final ModelLayerLocation ELEMENTIUM_OUTER_ARMOR = make("elementium_armor", "outer_armor");
	public static final ModelLayerLocation MANASTEEL_INNER_ARMOR = make("manasteel_armor", "inner_armor");
	public static final ModelLayerLocation MANASTEEL_OUTER_ARMOR = make("manasteel_armor", "outer_armor");
	public static final ModelLayerLocation MANAWEAVE_INNER_ARMOR = make("manaweave_armor", "inner_armor");
	public static final ModelLayerLocation MANAWEAVE_OUTER_ARMOR = make("manaweave_armor", "outer_armor");
	public static final ModelLayerLocation PIXIE = make("pixie");
	public static final ModelLayerLocation PYLON_GAIA = make("pylon_gaia");
	public static final ModelLayerLocation PYLON_MANA = make("pylon_mana");
	public static final ModelLayerLocation PYLON_NATURA = make("pylon_natura");
	public static final ModelLayerLocation TERRASTEEL_INNER_ARMOR = make("terrasteel_armor", "inner_armor");
	public static final ModelLayerLocation TERRASTEEL_OUTER_ARMOR = make("terrasteel_armor", "outer_armor");
	public static final ModelLayerLocation TERU_TERU_BOZU = make("teru_teru_bozu");

	private static ModelLayerLocation make(String name) {
		return make(name, "main");
	}

	private static ModelLayerLocation make(String name, String layer) {

		return new ModelLayerLocation(prefix(name), layer);
	}

	public static void init() {}
}
