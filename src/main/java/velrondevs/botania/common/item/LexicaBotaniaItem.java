package velrondevs.botania.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.advancements.UseItemSuccessTrigger;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaSounds;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.List;

public class LexicaBotaniaItem extends Item implements ItemWithBannerPattern, CustomCreativeTabContents {

	public static final String TAG_ELVEN_UNLOCK = "botania:elven_unlock";

	public LexicaBotaniaItem(Properties settings) {
		super(settings);
	}

	public static boolean isOpen() {
		return BuiltInRegistries.ITEM.getKey(BotaniaItems.lexicon).equals(PatchouliAPI.get().getOpenBookGui());
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		output.accept(this);
		ItemStack creative = new ItemStack(this);
		ItemNBTHelper.setBoolean(creative, TAG_ELVEN_UNLOCK, true);
		output.accept(creative);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		tooltip.add(getEdition().copy().withStyle(ChatFormatting.GRAY));
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);

		if (playerIn instanceof ServerPlayer player) {
			UseItemSuccessTrigger.INSTANCE.trigger(player, stack, player.serverLevel(), player.getX(), player.getY(), player.getZ());
			PatchouliAPI.get().openBookGUI(player, BuiltInRegistries.ITEM.getKey(this));
			playerIn.playSound(BotaniaSounds.lexiconOpen, 1F, (float) (0.7 + Math.random() * 0.4));
		}

		return InteractionResultHolder.sidedSuccess(stack, worldIn.isClientSide());
	}

	public static Component getEdition() {
		try {
			return PatchouliAPI.get().getSubtitle(BuiltInRegistries.ITEM.getKey(BotaniaItems.lexicon));
		} catch (IllegalArgumentException e) {
			return Component.literal("");
		}
	}

	public static Component getTitle(ItemStack stack) {
		Component title = stack.getHoverName();

		String akashicTomeNBT = "akashictome:displayName";
		if (ItemNBTHelper.verifyExistance(stack, akashicTomeNBT)) {
			CompoundTag nameTextComponent = ItemNBTHelper.getCompound(stack, akashicTomeNBT, false);
			title = Component.Serializer.fromJson(nameTextComponent.getString("text"), ItemStackSerialization.registries());
		}

		return title;
	}

	public static boolean isElven(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ELVEN_UNLOCK, false);
	}

	public static BlockHitResult doRayTrace(Level world, Player player, ClipContext.Fluid fluidMode) {
		return Item.getPlayerPOVHitResult(world, player, fluidMode);
	}

	@Override
	public TagKey<BannerPattern> getBannerPattern() {
		return BotaniaTags.BannerPatterns.PATTERN_ITEM_LEXICON;
	}
}
