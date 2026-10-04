package velrondevs.botania.client;

import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.common.block.mana.ManaPoolBlock;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.*;
import velrondevs.botania.common.item.brew.BaseBrewItem;
import velrondevs.botania.common.item.equipment.bauble.RingOfMagnetizationItem;
import velrondevs.botania.common.item.equipment.tool.bow.LivingwoodBowItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import velrondevs.botania.common.item.relic.FruitOfGrisaiaItem;
import velrondevs.botania.common.item.rod.SkiesRodItem;
import velrondevs.botania.network.TriConsumer;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.Locale;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaItemProperties {
	public static void init(TriConsumer<ItemLike, ResourceLocation, ClampedItemPropertyFunction> consumer) {
		consumer.accept(BotaniaItems.baubleBox, prefix("open"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, BaubleBoxItem.TAG_OPEN, false) ? 1 : 0);
		consumer.accept(BotaniaItems.blackHoleTalisman, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, BlackHoleTalismanItem.TAG_ACTIVE, false) ? 1 : 0);
		consumer.accept(BotaniaItems.manaBottle, prefix("swigs_taken"),
				(stack, world, entity, seed) -> {
					int swigsLeft = BottledManaItem.getSwigsLeft(stack) - 1;
					int totalSwigs = BottledManaItem.SWIGS - 1;
					return swigsLeft == totalSwigs ? 0.0F : Math.nextUp((totalSwigs - swigsLeft) / (float) totalSwigs);
				});

		ResourceLocation vuvuzelaId = prefix("vuvuzela");
		ClampedItemPropertyFunction isVuvuzela = (stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("vuvuzela") ? 1 : 0;
		consumer.accept(BotaniaItems.grassHorn, vuvuzelaId, isVuvuzela);
		consumer.accept(BotaniaItems.leavesHorn, vuvuzelaId, isVuvuzela);
		consumer.accept(BotaniaItems.snowHorn, vuvuzelaId, isVuvuzela);

		consumer.accept(BotaniaItems.lexicon, prefix("elven"), (stack, world, living, seed) -> LexicaBotaniaItem.isElven(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.manaCookie, prefix("totalbiscuit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().toLowerCase(Locale.ROOT).contains("totalbiscuit") ? 1F : 0F);
		consumer.accept(BotaniaItems.slimeBottle, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, SlimeInABottleItem.TAG_ACTIVE, false) ? 1.0F : 0.0F);
		consumer.accept(BotaniaItems.spawnerMover, prefix("full"),
				(stack, world, entity, seed) -> LifeAggregatorItem.hasData(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.temperanceStone, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, StoneOfTemperanceItem.TAG_ACTIVE, false) ? 1 : 0);
		consumer.accept(BotaniaItems.twigWand, prefix("bindmode"),
				(stack, world, entity, seed) -> WandOfTheForestItem.getBindMode(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.dreamwoodWand, prefix("bindmode"),
				(stack, world, entity, seed) -> WandOfTheForestItem.getBindMode(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.autocraftingHalo, prefix("active"),
				(stack, world, entity, seed) -> ItemNBTHelper.getBoolean(stack, ManufactoryHaloItem.TAG_ACTIVE, true) ? 1 : 0);

		ResourceLocation poolFullId = prefix("full");
		ClampedItemPropertyFunction poolFull = (stack, world, entity, seed) -> {
			Block block = ((BlockItem) stack.getItem()).getBlock();
			boolean renderFull = ((ManaPoolBlock) block).variant == ManaPoolBlock.Variant.CREATIVE || ItemNBTHelper.getBoolean(stack, "RenderFull", false);
			return renderFull ? 1F : 0F;
		};
		consumer.accept(BotaniaBlocks.manaPool, poolFullId, poolFull);
		consumer.accept(BotaniaBlocks.dilutedPool, poolFullId, poolFull);
		consumer.accept(BotaniaBlocks.creativePool, poolFullId, poolFull);
		consumer.accept(BotaniaBlocks.fabulousPool, poolFullId, poolFull);

		ClampedItemPropertyFunction brewGetter = (stack, world, entity, seed) -> {
			BaseBrewItem item = ((BaseBrewItem) stack.getItem());
			int swigsLeft = item.getSwigsLeft(stack) - 1;
			int totalSwigs = item.getSwigs() - 1;
			return swigsLeft == totalSwigs ? 0.0F : Math.nextUp((totalSwigs - swigsLeft) / (float) totalSwigs);
		};
		consumer.accept(BotaniaItems.brewVial, prefix("swigs_taken"), brewGetter);
		consumer.accept(BotaniaItems.brewFlask, prefix("swigs_taken"), brewGetter);

		ResourceLocation holidayId = prefix("holiday");
		ClampedItemPropertyFunction holidayGetter = (stack, worldIn, entityIn, seed) -> ClientProxy.jingleTheBells ? 1 : 0;
		consumer.accept(BotaniaItems.manaweaveHelm, holidayId, holidayGetter);
		consumer.accept(BotaniaItems.manaweaveChest, holidayId, holidayGetter);
		consumer.accept(BotaniaItems.manaweaveBoots, holidayId, holidayGetter);
		consumer.accept(BotaniaItems.manaweaveLegs, holidayId, holidayGetter);

		ClampedItemPropertyFunction ringOnGetter = (stack, worldIn, entityIn, seed) -> RingOfMagnetizationItem.getCooldown(stack) <= 0 ? 1 : 0;
		consumer.accept(BotaniaItems.magnetRing, prefix("active"), ringOnGetter);
		consumer.accept(BotaniaItems.magnetRingGreater, prefix("active"), ringOnGetter);

		consumer.accept(BotaniaItems.elementiumShears, prefix("reddit"),
				(stack, world, entity, seed) -> stack.getHoverName().getString().equalsIgnoreCase("dammit reddit") ? 1F : 0F);
		consumer.accept(BotaniaItems.manasteelSword, prefix("elucidator"),
				(stack, world, entity, seed) -> "the elucidator".equals(stack.getHoverName().getString().toLowerCase(Locale.ROOT).trim()) ? 1 : 0);
		consumer.accept(BotaniaItems.terraAxe, prefix("active"),
				(stack, world, entity, seed) -> entity instanceof Player player && !TerraTruncatorItem.shouldBreak(player) ? 0 : 1);
		consumer.accept(BotaniaItems.terraPick, prefix("tipped"),
				(stack, world, entity, seed) -> TerraShattererItem.isTipped(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.terraPick, prefix("active"),
				(stack, world, entity, seed) -> TerraShattererItem.isEnabled(stack) ? 1 : 0);
		consumer.accept(BotaniaItems.infiniteFruit, prefix("boot"),
				(stack, worldIn, entity, seed) -> FruitOfGrisaiaItem.isBoot(stack) ? 1F : 0F);
		consumer.accept(BotaniaItems.tornadoRod, prefix("active"),
				(stack, world, living, seed) -> SkiesRodItem.isFlying(stack) ? 1 : 0);

		ClampedItemPropertyFunction pulling = (stack, worldIn, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
		ClampedItemPropertyFunction pull = (stack, worldIn, entity, seed) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				LivingwoodBowItem item = ((LivingwoodBowItem) stack.getItem());
				return entity.getUseItem() != stack
						? 0.0F
						: (stack.getUseDuration(entity) - entity.getUseItemRemainingTicks()) * item.chargeVelocityMultiplier() / 20.0F;
			}
		};
		consumer.accept(BotaniaItems.livingwoodBow, ResourceLocation.parse("pulling"), pulling);
		consumer.accept(BotaniaItems.livingwoodBow, ResourceLocation.parse("pull"), pull);
		consumer.accept(BotaniaItems.crystalBow, ResourceLocation.parse("pulling"), pulling);
		consumer.accept(BotaniaItems.crystalBow, ResourceLocation.parse("pull"), pull);
	}

	private BotaniaItemProperties() {}
}
