package velrondevs.botania.client.integration.rrv;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import java.util.ArrayList;
import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaRrvTypes {
	public static final int ALTAR_DX = 17;
	public static final int ALTAR_DY = 21;
	public static final int TERRA_DX = 16;
	public static final int TERRA_DY = 31;
	public static final int BREWERY_DX = 10;

	public static final int ALTAR_OUTPUT = 0;
	public static final int ALTAR_CENTER = 1;
	public static final int ALTAR_REAGENT = 2;

	public static final int INFUSION_INPUT = 0;
	public static final int INFUSION_POOL = 1;
	public static final int INFUSION_CATALYST = 2;
	public static final int INFUSION_OUTPUT = 3;

	public static final int TERRA_OUTPUT = 0;
	public static final int TERRA_PLATE = 1;

	public static final int BREWERY_CONTAINER = 0;
	public static final int BREWERY_OUTPUT = 1;

	public static final int DAISY_INPUT = 0;
	public static final int DAISY_FLOWER = 1;
	public static final int DAISY_OUTPUT = 2;

	public static final BotaniaRrvRecipeType PETAL_APOTHECARY = new BotaniaRrvRecipeType(prefix("petal_apothecary"),
			"botania.nei.petalApothecary", () -> BotaniaBlocks.defaultAltar, 140, 140, altarLayout(2),
			() -> {
				List<ItemLike> list = new ArrayList<>();
				for (Block block : BotaniaBlocks.ALL_APOTHECARIES) {
					list.add(block);
				}
				return list;
			});

	public static final BotaniaRrvRecipeType MANA_INFUSION = new BotaniaRrvRecipeType(prefix("mana_infusion"),
			"botania.nei.manaPool", () -> BotaniaBlocks.manaPool, 116, 65, infusionLayout(),
			BotaniaRrvRecipeType.stations(() -> BotaniaBlocks.manaPool, () -> BotaniaBlocks.dilutedPool, () -> BotaniaBlocks.fabulousPool));

	public static final BotaniaRrvRecipeType RUNIC_ALTAR = new BotaniaRrvRecipeType(prefix("runic_altar"),
			"botania.nei.runicAltar", () -> BotaniaBlocks.runeAltar, 140, 148, altarLayout(1),
			BotaniaRrvRecipeType.stations(() -> BotaniaBlocks.runeAltar));

	public static final BotaniaRrvRecipeType TERRESTRIAL_AGGLOMERATION = new BotaniaRrvRecipeType(prefix("terrestrial_agglomeration"),
			"botania.nei.terraPlate", () -> BotaniaBlocks.terraPlate, 140, 148, terraLayout(),
			BotaniaRrvRecipeType.stations(() -> BotaniaBlocks.terraPlate));

	public static final BotaniaRrvRecipeType ELVEN_TRADE = new BotaniaRrvRecipeType(prefix("elven_trade"),
			"botania.nei.elvenTrade", () -> BotaniaBlocks.alfPortal, 160, 90, elvenLayout(),
			BotaniaRrvRecipeType.stations(() -> BotaniaBlocks.alfPortal));

	public static final BotaniaRrvRecipeType BOTANICAL_BREWERY = new BotaniaRrvRecipeType(prefix("botanical_brewery"),
			"botania.nei.brewery", () -> BotaniaBlocks.brewery, 150, 65, breweryLayout(),
			BotaniaRrvRecipeType.stations(() -> BotaniaBlocks.brewery));

	public static final BotaniaRrvRecipeType PURE_DAISY = daisyType("pure_daisy", "botania.nei.pureDaisy",
			BotaniaRrvRecipeType.stations(() -> BotaniaFlowerBlocks.pureDaisy, () -> BotaniaFlowerBlocks.pureDaisyFloating));

	public static final BotaniaRrvRecipeType ORECHID = daisyType("orechid", "botania.nei.orechid",
			BotaniaRrvRecipeType.stations(() -> BotaniaFlowerBlocks.orechid, () -> BotaniaFlowerBlocks.orechidFloating));

	public static final BotaniaRrvRecipeType ORECHID_IGNEM = daisyType("orechid_ignem", "botania.nei.orechidIgnem",
			BotaniaRrvRecipeType.stations(() -> BotaniaFlowerBlocks.orechidIgnem, () -> BotaniaFlowerBlocks.orechidIgnemFloating));

	public static final BotaniaRrvRecipeType MARIMORPHOSIS = daisyType("marimorphosis", "botania.nei.marimorphosis",
			BotaniaRrvRecipeType.stations(() -> BotaniaFlowerBlocks.marimorphosis, () -> BotaniaFlowerBlocks.marimorphosisFloating,
					() -> BotaniaFlowerBlocks.marimorphosisChibi, () -> BotaniaFlowerBlocks.marimorphosisChibiFloating));

	private BotaniaRrvTypes() {}

	public static BotaniaRrvLayout altarLayout(int reagents) {
		BotaniaRrvLayout layout = new BotaniaRrvLayout(ALTAR_DX + 1, ALTAR_DY + 1);
		final int cx = 44;
		final int cy = 40;
		layout.fixed(cx + 38, 5);
		if (reagents > 0) {
			layout.fixed(BotaniaRrvLayout.rotateX(cx, 31, cx, cy, 0), BotaniaRrvLayout.rotateY(cx, 30, cx, cy, 0));
			double step = 360.0 / (reagents + 1);
			for (int i = 0; i < reagents; i++) {
				layout.fixed(BotaniaRrvLayout.rotateX(cx, 30, cx, cy, step * (i + 1)),
						BotaniaRrvLayout.rotateY(cx, 30, cx, cy, step * (i + 1)));
			}
		} else {
			layout.fixed(cx, cy + 1);
		}
		return layout.ring(cx, cy, 32, 12).ring(cx, cy, 58, 20);
	}

	private static BotaniaRrvLayout infusionLayout() {
		BotaniaRrvLayout layout = new BotaniaRrvLayout(1, 1);
		layout.fixed(21, 13);
		layout.fixed(50, 13);
		layout.fixed(0, 13);
		layout.fixed(79, 13);
		return layout;
	}

	private static BotaniaRrvLayout terraLayout() {
		BotaniaRrvLayout layout = new BotaniaRrvLayout(TERRA_DX + 1, TERRA_DY + 1);
		final int cx = 45;
		final int cy = 30;
		layout.fixed(cx, cy);
		layout.fixed(cx, 80);
		return layout.ring(cx, cy, 30, 12).ring(cx, cy, 58, 20);
	}

	private static BotaniaRrvLayout elvenLayout() {
		return new BotaniaRrvLayout(1, 1).row(35, 0, 4).row(83, 40, 4);
	}

	private static BotaniaRrvLayout breweryLayout() {
		BotaniaRrvLayout layout = new BotaniaRrvLayout(BREWERY_DX + 1, 1);
		layout.fixed(10, 35);
		layout.fixed(58, 35);
		return layout.row(-5, 1, 8);
	}

	private static BotaniaRrvRecipeType daisyType(String id, String titleKey, java.util.function.Supplier<List<ItemLike>> stations) {
		BotaniaRrvLayout layout = new BotaniaRrvLayout(1, 1);
		layout.fixed(10, 13);
		layout.fixed(39, 13);
		layout.fixed(68, 13);
		return new BotaniaRrvRecipeType(prefix(id), titleKey, () -> stations.get().get(0), 96, 44, layout, stations);
	}
}
