package velrondevs.botania.common.item.brew;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.brew.BrewContainer;
import velrondevs.botania.api.brew.BrewItem;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.CustomCreativeTabContents;
import velrondevs.botania.registry.BotaniaBrews;

import java.util.List;

public class IncenseStickItem extends Item implements BrewItem, BrewContainer, CustomCreativeTabContents {

	private static final String TAG_BREW_KEY = "brewKey";
	public static final int TIME_MULTIPLIER = 60;

	public IncenseStickItem(Properties builder) {
		super(builder);
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(this);
		for (Brew brew : BotaniaAPI.instance().getBrewRegistry()) {
			ItemStack brewStack = getItemForBrew(brew, new ItemStack(this));
			if (!brewStack.isEmpty()) {
				output.accept(brewStack);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext world, List<Component> list, TooltipFlag flags) {
		Brew brew = getBrew(stack);
		if (brew == BotaniaBrews.fallbackBrew) {
			list.add(Component.translatable("botaniamisc.notInfused").withStyle(ChatFormatting.LIGHT_PURPLE));
			return;
		}

		list.add(Component.translatable("botaniamisc.brewOf", Component.translatable(brew.getTranslationKey(stack))).withStyle(ChatFormatting.LIGHT_PURPLE));
		BaseBrewItem.addPotionTooltip(brew.getPotionEffects(stack), list, TIME_MULTIPLIER, world.tickRate());
	}

	@Override
	public Brew getBrew(ItemStack stack) {
		String key = ItemNBTHelper.getString(stack, TAG_BREW_KEY, "");
		return BotaniaAPI.instance().getBrewRegistry().get(ResourceLocation.tryParse(key));
	}

	public static void setBrew(ItemStack stack, Brew brew) {
		setBrew(stack, BotaniaAPI.instance().getBrewRegistry().getKey(brew));
	}

	public static void setBrew(ItemStack stack, ResourceLocation brew) {
		ItemNBTHelper.setString(stack, TAG_BREW_KEY, brew.toString());
	}

	@Override
	public ItemStack getItemForBrew(Brew brew, ItemStack stack) {
		if (!brew.canInfuseIncense() || brew.getPotionEffects(stack).size() != 1 || brew.getPotionEffects(stack).get(0).getEffect().value().isInstantenous()) {
			return ItemStack.EMPTY;
		}

		ItemStack brewStack = new ItemStack(this);
		setBrew(brewStack, brew);
		return brewStack;
	}

	@Override
	public int getManaCost(Brew brew, ItemStack stack) {
		return brew.getManaCost() * 10;
	}
}
