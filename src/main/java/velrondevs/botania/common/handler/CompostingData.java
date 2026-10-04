package velrondevs.botania.common.handler;

import net.minecraft.world.level.ItemLike;

import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.function.BiConsumer;

public class CompostingData {
	public static void init(BiConsumer<ItemLike, Float> registrationMethod) {

		final float chanceLowest = 0.3f;
		final float chanceLow = 0.5f;
		final float chanceMid = 0.65f;
		final float chanceHigh = 0.85f;

		ColorHelper.supportedColors().forEach(dyeColor -> {
			registrationMethod.accept(BotaniaItems.getPetal(dyeColor), chanceLowest);
			registrationMethod.accept(BotaniaBlocks.getPetalBlock(dyeColor), chanceLow);
			registrationMethod.accept(BotaniaBlocks.getFlower(dyeColor), chanceMid);
			registrationMethod.accept(BotaniaBlocks.getDoubleFlower(dyeColor), chanceMid);
			registrationMethod.accept(BotaniaBlocks.getMushroom(dyeColor), chanceMid);
		});

		registrationMethod.accept(BotaniaBlocks.cellBlock, chanceHigh);
	}
}
