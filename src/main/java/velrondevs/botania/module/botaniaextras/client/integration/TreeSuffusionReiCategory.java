package velrondevs.botania.module.botaniaextras.client.integration;

import me.shedaniel.math.Point;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.client.integration.rei.BotaniaReiCategory;
import velrondevs.botania.client.integration.rei.BotaniaReiDisplay;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;

import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class TreeSuffusionReiCategory extends BotaniaReiCategory {
	public static final CategoryIdentifier<BotaniaReiDisplay> ID = CategoryIdentifier.of(prefix("tree_suffusion"));
	private static final ResourceLocation PETAL_TEXTURE = prefix("textures/gui/petal_overlay.png");

	public TreeSuffusionReiCategory() {
		super(ID, "botania.nei.treeSuffusion", EntryStacks.of(BotaniaExtrasBlocks.terrasteelItemPlatform), 106, 107);
	}

	@Override
	protected void addContent(List<Widget> widgets, BotaniaReiDisplay display, Point origin) {
		final int centerX = 44;
		final int centerY = 40;
		final int posYIngredients = 8;
		List<EntryIngredient> ingredients = display.ingredients();
		double step = 360.0 / ingredients.size();
		widgets.add(manaBar(origin, 2, 100, display.mana(), ManaPoolBlockEntity.MAX_MANA / 10));
		widgets.add(texture(origin, PETAL_TEXTURE, 21, 0, 85, 82, 42, 11));
		for (int i = 0; i < ingredients.size(); i++) {
			widgets.add(slot(origin,
					rotateX(centerX, posYIngredients, centerX, centerY, step * i),
					rotateY(centerX, posYIngredients, centerX, centerY, step * i), ingredients.get(i)).markInput());
		}
		widgets.add(slot(origin, centerX, centerY + 1, EntryStacks.of(BotaniaExtrasWoods.sapling)));
		widgets.add(slot(origin, centerX + 38, 5, display.output()).markOutput());
	}
}
