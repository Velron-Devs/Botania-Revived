package velrondevs.botania.client.integration.jei;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.LaputaShardItem;
import velrondevs.botania.common.item.LexicaBotaniaItem;
import velrondevs.botania.common.item.ManaTabletItem;
import velrondevs.botania.common.item.WandOfTheForestItem;
import velrondevs.botania.common.item.brew.BaseBrewItem;
import velrondevs.botania.common.item.equipment.bauble.FlugelTiaraItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasModule;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;
import velrondevs.botania.module.botaniaextras.item.VibrantPlainsRodItem;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public final class BotaniaSubtypes {
	private static final BiFunction<ItemStack, Boolean, String> BREW = (stack, recipe) -> BaseBrewItem.getSubtype(stack);
	private static final BiFunction<ItemStack, Boolean, String> MANA = (stack, recipe) -> String.valueOf(XplatAbstractions.INSTANCE.findManaItem(stack).getMana());

	private BotaniaSubtypes() {}

	public static Map<Item, BiFunction<ItemStack, Boolean, String>> interpreters() {
		Map<Item, BiFunction<ItemStack, Boolean, String>> map = new LinkedHashMap<>();
		map.put(BotaniaItems.brewVial, BREW);
		map.put(BotaniaItems.brewFlask, BREW);
		map.put(BotaniaItems.incenseStick, BREW);
		map.put(BotaniaItems.bloodPendant, BREW);
		map.put(BotaniaItems.flightTiara, (stack, recipe) -> String.valueOf(FlugelTiaraItem.getVariant(stack)));
		map.put(BotaniaItems.lexicon, (stack, recipe) -> String.valueOf(ItemNBTHelper.getBoolean(stack, LexicaBotaniaItem.TAG_ELVEN_UNLOCK, false)));
		map.put(BotaniaItems.laputaShard, (stack, recipe) -> String.valueOf(LaputaShardItem.getShardLevel(stack)));
		map.put(BotaniaItems.terraPick, (stack, recipe) -> {
			if (recipe) {
				return String.valueOf(TerraShattererItem.isTipped(stack));
			}
			return String.valueOf(TerraShattererItem.getMana_(stack)) + TerraShattererItem.isTipped(stack);
		});
		BiFunction<ItemStack, Boolean, String> wand = (stack, recipe) -> WandOfTheForestItem.getColor1(stack) + ":" + WandOfTheForestItem.getColor2(stack);
		map.put(BotaniaItems.twigWand, wand);
		map.put(BotaniaItems.dreamwoodWand, wand);
		map.put(BotaniaItems.manaTablet, (stack, recipe) -> MANA.apply(stack, recipe) + ManaTabletItem.isStackCreative(stack));
		map.put(BotaniaItems.manaRing, MANA);
		map.put(BotaniaItems.manaRingGreater, MANA);
		if (BotaniaExtrasModule.INSTANCE.isEnabled()) {
			map.put(BotaniaExtrasItems.frozenStar, (stack, recipe) -> FrozenStarItem.getColor(stack) + ":" + FrozenStarItem.getSize(stack));
			map.put(BotaniaExtrasItems.vibrantPlainsRod, (stack, recipe) -> String.valueOf(VibrantPlainsRodItem.getColorIndex(stack)));
		}
		return map;
	}
}
