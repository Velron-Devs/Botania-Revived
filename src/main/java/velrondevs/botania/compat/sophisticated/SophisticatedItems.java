package velrondevs.botania.compat.sophisticated;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;

import velrondevs.botania.common.lib.LibMisc;

import java.util.function.BiConsumer;

public final class SophisticatedItems {
	public static final String MANASTEEL_STACK = "manasteel_stack_upgrade";
	public static final String ELEMENTIUM_STACK = "elementium_stack_upgrade";
	public static final String TERRASTEEL_STACK = "terrasteel_stack_upgrade";
	public static final String GAIA_STACK = "gaia_stack_upgrade";
	public static final String ALFSTEEL_STACK = "alfsteel_stack_upgrade";
	public static final String MANA_PILE = "mana_pile_upgrade";
	public static final String MANA_LINK = "mana_link_upgrade";
	public static final String RUNIC_ALTAR = "runic_altar_upgrade";
	public static final String ELVEN_PORTAL = "elven_portal_upgrade";
	public static final String BREWERY = "brewery_upgrade";
	public static final String PETAL_APOTHECARY = "petal_apothecary_upgrade";
	public static final String TERRA_PLATE = "remote_terra_plate_upgrade";

	public static final Item MANASTEEL_STACK_UPGRADE = new BotaniaStackUpgradeItem(1_000_000D);
	public static final Item ELEMENTIUM_STACK_UPGRADE = new BotaniaStackUpgradeItem(2_000_000D);
	public static final Item TERRASTEEL_STACK_UPGRADE = new BotaniaStackUpgradeItem(3_000_000D);
	public static final Item GAIA_STACK_UPGRADE = new BotaniaStackUpgradeItem(12_000_000_000D);
	public static final Item MANA_PILE_UPGRADE = new ManaPileUpgradeItem();
	public static final Item MANA_LINK_UPGRADE = new ManaLinkUpgradeItem();
	public static final Item RUNIC_ALTAR_UPGRADE = new RunicAltarUpgradeItem();
	public static final Item ELVEN_PORTAL_UPGRADE = new ElvenPortalUpgradeItem();
	public static final Item BREWERY_UPGRADE = new BreweryUpgradeItem();
	public static final Item PETAL_APOTHECARY_UPGRADE = new PetalApothecaryUpgradeItem();
	public static final Item TERRA_PLATE_UPGRADE = new TerraPlateUpgradeItem();

	private SophisticatedItems() {}

	static void register(BiConsumer<Item, Identifier> consumer) {
		consumer.accept(MANASTEEL_STACK_UPGRADE, id(MANASTEEL_STACK));
		consumer.accept(ELEMENTIUM_STACK_UPGRADE, id(ELEMENTIUM_STACK));
		consumer.accept(TERRASTEEL_STACK_UPGRADE, id(TERRASTEEL_STACK));
		consumer.accept(GAIA_STACK_UPGRADE, id(GAIA_STACK));
		if (SophisticatedOptional.isLoaded(SophisticatedOptional.MASTERFUL_BOTANY)) {
			consumer.accept(new BotaniaStackUpgradeItem(Integer.MAX_VALUE), id(ALFSTEEL_STACK));
		}
		consumer.accept(MANA_PILE_UPGRADE, id(MANA_PILE));
		consumer.accept(MANA_LINK_UPGRADE, id(MANA_LINK));
		consumer.accept(RUNIC_ALTAR_UPGRADE, id(RUNIC_ALTAR));
		consumer.accept(ELVEN_PORTAL_UPGRADE, id(ELVEN_PORTAL));
		consumer.accept(BREWERY_UPGRADE, id(BREWERY));
		consumer.accept(PETAL_APOTHECARY_UPGRADE, id(PETAL_APOTHECARY));
		consumer.accept(TERRA_PLATE_UPGRADE, id(TERRA_PLATE));
	}

	static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, path);
	}
}
