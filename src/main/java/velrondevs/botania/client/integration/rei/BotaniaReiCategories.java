package velrondevs.botania.client.integration.rei;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Label;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.recipe.OrechidRecipe;
import velrondevs.botania.client.integration.shared.OrechidUIHelper;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import java.util.ArrayList;
import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaReiCategories {
	public static final CategoryIdentifier<BotaniaReiDisplay> MANA_INFUSION = CategoryIdentifier.of(prefix("mana_infusion"));
	public static final CategoryIdentifier<BotaniaReiDisplay> PETAL_APOTHECARY = CategoryIdentifier.of(prefix("petal_apothecary"));
	public static final CategoryIdentifier<BotaniaReiDisplay> RUNIC_ALTAR = CategoryIdentifier.of(prefix("runic_altar"));
	public static final CategoryIdentifier<BotaniaReiDisplay> TERRESTRIAL_AGGLOMERATION = CategoryIdentifier.of(prefix("terrestrial_agglomeration"));
	public static final CategoryIdentifier<BotaniaReiDisplay> ELVEN_TRADE = CategoryIdentifier.of(prefix("elven_trade"));
	public static final CategoryIdentifier<BotaniaReiDisplay> BOTANICAL_BREWERY = CategoryIdentifier.of(prefix("botanical_brewery"));
	public static final CategoryIdentifier<BotaniaReiDisplay> PURE_DAISY = CategoryIdentifier.of(prefix("pure_daisy"));
	public static final CategoryIdentifier<BotaniaReiDisplay> ORECHID = CategoryIdentifier.of(prefix("orechid"));
	public static final CategoryIdentifier<BotaniaReiDisplay> ORECHID_IGNEM = CategoryIdentifier.of(prefix("orechid_ignem"));
	public static final CategoryIdentifier<BotaniaReiDisplay> MARIMORPHOSIS = CategoryIdentifier.of(prefix("marimorphosis"));

	private static final ResourceLocation PURE_DAISY_TEXTURE = prefix("textures/gui/pure_daisy_overlay.png");
	private static final ResourceLocation PETAL_TEXTURE = prefix("textures/gui/petal_overlay.png");
	private static final ResourceLocation TERRA_TEXTURE = prefix("textures/gui/terrasteel_jei_overlay.png");
	private static final ResourceLocation BREWERY_TEXTURE = prefix("textures/gui/nei_brewery.png");
	private static final ResourceLocation ELVEN_TEXTURE = prefix("textures/gui/elven_trade_overlay.png");

	private BotaniaReiCategories() {}

	public static List<BotaniaReiCategory> all() {
		return List.of(
				new ManaInfusion(),
				new PetalApothecary(),
				new RunicAltar(),
				new TerrestrialAgglomeration(),
				new ElvenTrade(),
				new BotanicalBrewery(),
				new PureDaisy(),
				new Orechid(ORECHID, "botania.nei.orechid", EntryStacks.of(BotaniaFlowerBlocks.orechid), false),
				new Orechid(ORECHID_IGNEM, "botania.nei.orechidIgnem", EntryStacks.of(BotaniaFlowerBlocks.orechidIgnem), false),
				new Orechid(MARIMORPHOSIS, "botania.nei.marimorphosis", EntryStacks.of(BotaniaFlowerBlocks.marimorphosis), true)
		);
	}

	private static void addAltar(List<Widget> widgets, Point origin, BotaniaReiDisplay display, EntryStack<?> altar) {
		final int centerX = 44;
		final int centerY = 40;
		final int posYIngredients = 8;
		final int posYReagents = 30;
		List<EntryIngredient> ingredients = display.ingredients();
		List<EntryIngredient> reagents = display.extras();
		double step = 360.0 / ingredients.size();
		widgets.add(BotaniaReiCategory.texture(origin, PETAL_TEXTURE, 21, 0, 85, 82, 42, 11));
		for (int i = 0; i < ingredients.size(); i++) {
			widgets.add(BotaniaReiCategory.slot(origin,
					BotaniaReiCategory.rotateX(centerX, posYIngredients, centerX, centerY, step * i),
					BotaniaReiCategory.rotateY(centerX, posYIngredients, centerX, centerY, step * i), ingredients.get(i)).markInput());
		}
		if (!reagents.isEmpty()) {
			double reagentStep = 360.0 / (reagents.size() + 1);
			widgets.add(BotaniaReiCategory.slot(origin,
					BotaniaReiCategory.rotateX(centerX, posYReagents + 1, centerX, centerY, 0),
					BotaniaReiCategory.rotateY(centerX, posYReagents, centerX, centerY, 0), altar));
			for (int i = 0; i < reagents.size(); i++) {
				widgets.add(BotaniaReiCategory.slot(origin,
						BotaniaReiCategory.rotateX(centerX, posYReagents, centerX, centerY, reagentStep * (i + 1)),
						BotaniaReiCategory.rotateY(centerX, posYReagents, centerX, centerY, reagentStep * (i + 1)), reagents.get(i)).markInput());
			}
		} else {
			widgets.add(BotaniaReiCategory.slot(origin, centerX, centerY + 1, altar));
		}
		widgets.add(BotaniaReiCategory.slot(origin, centerX + 38, 5, display.output()).markOutput());
	}

	private static void addPureDaisy(List<Widget> widgets, Point origin, BotaniaReiDisplay display, EntryStack<?> flower) {
		widgets.add(BotaniaReiCategory.texture(origin, PURE_DAISY_TEXTURE, 17, 0, 65, 44, 0, 0));
		widgets.add(BotaniaReiCategory.slot(origin, 10, 13, display.ingredients().get(0)).markInput());
		widgets.add(BotaniaReiCategory.slot(origin, 39, 13, flower));
		widgets.add(BotaniaReiCategory.slot(origin, 68, 13, display.output()).markOutput());
	}

	private static class ManaInfusion extends BotaniaReiCategory {
		private static final EntryStack<?> POOL;

		static {
			ItemStack stack = new ItemStack(BotaniaBlocks.manaPool);
			ItemNBTHelper.setBoolean(stack, "RenderFull", true);
			POOL = EntryStacks.of(stack);
		}

		ManaInfusion() {
			super(MANA_INFUSION, "botania.nei.manaPool", EntryStacks.of(BotaniaBlocks.manaPool), 116, 65);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			widgets.add(texture(origin, PURE_DAISY_TEXTURE, 28, 0, 65, 44, 0, 0));
			widgets.add(manaBar(origin, 7, 50, display.mana(), ManaPoolBlockEntity.MAX_MANA / 10));
			widgets.add(slot(origin, 21, 13, display.ingredients().get(0)).markInput());
			widgets.add(slot(origin, 50, 13, POOL));
			if (!display.extras().isEmpty()) {
				widgets.add(slot(origin, 0, 13, display.extras().get(0)).markInput());
			}
			widgets.add(slot(origin, 79, 13, display.output()).markOutput());
		}
	}

	private static class PetalApothecary extends BotaniaReiCategory {
		PetalApothecary() {
			super(PETAL_APOTHECARY, "botania.nei.petalApothecary", EntryStacks.of(BotaniaBlocks.defaultAltar), 106, 107);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			addAltar(widgets, origin, display, EntryStacks.of(BotaniaBlocks.defaultAltar));
		}
	}

	private static class RunicAltar extends BotaniaReiCategory {
		RunicAltar() {
			super(RUNIC_ALTAR, "botania.nei.runicAltar", EntryStacks.of(BotaniaBlocks.runeAltar), 106, 107);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			widgets.add(manaBar(origin, 2, 100, display.mana(), ManaPoolBlockEntity.MAX_MANA / 10));
			addAltar(widgets, origin, display, EntryStacks.of(BotaniaBlocks.runeAltar));
		}
	}

	private static class TerrestrialAgglomeration extends BotaniaReiCategory {
		TerrestrialAgglomeration() {
			super(TERRESTRIAL_AGGLOMERATION, "botania.nei.terraPlate", EntryStacks.of(BotaniaBlocks.terraPlate), 106, 107);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			final int centerX = 45;
			final int centerY = 30;
			List<EntryIngredient> ingredients = display.ingredients();
			double step = 360.0 / ingredients.size();
			widgets.add(manaBar(origin, 2, 100, display.mana(), ManaPoolBlockEntity.MAX_MANA));
			widgets.add(texture(origin, TERRA_TEXTURE, centerX - 23, centerY - 23, 64, 64, 42, 29));
			for (int i = 0; i < ingredients.size(); i++) {
				widgets.add(slot(origin, rotateX(centerX, centerY - 30, centerX, centerY, step * i),
						rotateY(centerX, centerY - 30, centerX, centerY, step * i), ingredients.get(i)).markInput());
			}
			widgets.add(slot(origin, centerX, 80, EntryStacks.of(BotaniaBlocks.terraPlate)));
			widgets.add(slot(origin, centerX, centerY, display.output()).markOutput());
		}
	}

	private static class ElvenTrade extends BotaniaReiCategory {
		ElvenTrade() {
			super(ELVEN_TRADE, "botania.nei.elvenTrade", EntryStacks.of(BotaniaBlocks.alfPortal), 120, 90);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			widgets.add(texture(origin, ELVEN_TEXTURE, 10, 5, 71, 75, 20, 19));
			int swirlX = origin.x + 12;
			int swirlY = origin.y + 22;
			widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
				TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
						.apply(prefix("block/alfheim_portal_swirl"));
				graphics.blit(swirlX, swirlY, 0, 48, 48, sprite);
			}));
			int sx = 35;
			for (EntryIngredient ing : display.ingredients()) {
				widgets.add(slot(origin, sx, 0, ing).markInput());
				sx += 18;
			}
			sx = 83;
			for (EntryIngredient out : display.getOutputEntries()) {
				widgets.add(slot(origin, sx, 40, out).markOutput());
				sx += 18;
			}
		}
	}

	private static class BotanicalBrewery extends BotaniaReiCategory {
		BotanicalBrewery() {
			super(BOTANICAL_BREWERY, "botania.nei.brewery", EntryStacks.of(BotaniaBlocks.brewery), 120, 65);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			widgets.add(texture(origin, BREWERY_TEXTURE, 0, 0, 86, 55, 28, 6));
			widgets.add(Widgets.createSlot(new Point(origin.x + 10, origin.y + 35)).entries(display.extras().get(0)).markInput());
			List<EntryIngredient> ingredients = display.ingredients();
			int sx = 58 - (ingredients.size() - 1) * 9;
			for (EntryIngredient ing : ingredients) {
				widgets.add(slot(origin, sx, 1, ing).markInput());
				sx += 18;
			}
			widgets.add(Widgets.createSlot(new Point(origin.x + 58, origin.y + 35)).entries(display.output()).markOutput());
		}
	}

	private static class PureDaisy extends BotaniaReiCategory {
		PureDaisy() {
			super(PURE_DAISY, "botania.nei.pureDaisy", EntryStacks.of(BotaniaFlowerBlocks.pureDaisy), 96, 44);
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			addPureDaisy(widgets, origin, display, EntryStacks.of(BotaniaFlowerBlocks.pureDaisy));
		}
	}

	private static class Orechid extends BotaniaReiCategory {
		private final EntryStack<?> flower;
		private final boolean biomeTooltip;

		Orechid(CategoryIdentifier<BotaniaReiDisplay> id, String titleKey, EntryStack<?> flower, boolean biomeTooltip) {
			super(id, titleKey, flower, 96, 44);
			this.flower = flower;
			this.biomeTooltip = biomeTooltip;
		}

		@Override
		protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
			addPureDaisy(widgets, origin, display, flower);
			OrechidRecipe recipe = display.orechid();
			if (recipe == null) {
				return;
			}
			Double chance = OrechidUIHelper.getChance(recipe, null);
			if (chance == null) {
				return;
			}
			double chanceValue = chance;
			Label label = Widgets.createLabel(new Point(origin.x + 90, origin.y + 3), OrechidUIHelper.getPercentageComponent(chanceValue))
					.rightAligned().noShadow().color(0xFF555555, 0xFFBBBBBB).tooltip(chanceTooltip(chanceValue, recipe));
			widgets.add(label);
		}

		private Component[] chanceTooltip(double chance, OrechidRecipe recipe) {
			var ratio = OrechidUIHelper.getRatioForChance(chance);
			List<Component> lines = new ArrayList<>();
			lines.add(OrechidUIHelper.getRatioTooltipComponent(ratio));
			if (biomeTooltip) {
				OrechidUIHelper.getBiomeChanceAndRatioTooltipComponents(chance, recipe).forEach(lines::add);
			}
			return lines.toArray(new Component[0]);
		}
	}
}
