package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Blocks;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.common.block.FloatingSpecialFlowerBlock;
import velrondevs.botania.common.block.SpecialFlowerBlock;
import velrondevs.botania.common.block.flower.ManastarBlockEntity;
import velrondevs.botania.common.block.flower.PureDaisyBlockEntity;
import velrondevs.botania.common.lib.LibBlockNames;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class BotaniaPonderScenes {
	private BotaniaPonderScenes() {}

	static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		helper.forComponents(ids(BotaniaBlocks.manaSpreader, BotaniaBlocks.redstoneSpreader, BotaniaBlocks.elvenSpreader, BotaniaBlocks.gaiaSpreader))
				.addStoryBoard("spreader_pool", ManaScenes::spreader, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(ids(BotaniaBlocks.manaPool, BotaniaBlocks.dilutedPool, BotaniaBlocks.fabulousPool, BotaniaBlocks.creativePool))
				.addStoryBoard("mana_pool", ManaScenes::pool, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(apothecaryIds())
				.addStoryBoard("apothecary", CraftingScenes::apothecary, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(ids(BotaniaBlocks.runeAltar))
				.addStoryBoard("runic_altar", CraftingScenes::runicAltar, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(ids(BotaniaBlocks.terraPlate))
				.addStoryBoard("terra_plate", CraftingScenes::terraPlate, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(ids(BotaniaBlocks.enchanter))
				.addStoryBoard("enchanter", CraftingScenes::enchanter, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(List.of(BuiltInRegistries.BLOCK.getKey(BotaniaFlowerBlocks.pureDaisy)))
				.addStoryBoard("pure_daisy", CraftingScenes::pureDaisy, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(ids(BotaniaBlocks.alfPortal))
				.addStoryBoard("alfheim_portal", AlfheimScenes::portal, BotaniaPonderTags.ALFHEIM);
		helper.forComponents(ids(BotaniaBlocks.gaiaPylon, Blocks.BEACON))
				.addStoryBoard("gaia_ritual", AlfheimScenes::gaiaRitual, BotaniaPonderTags.ALFHEIM);

		helper.forComponents(lensIds())
				.addStoryBoard("lens", MachineScenes::lenses, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(ids(BotaniaBlocks.prism))
				.addStoryBoard("prism", MachineScenes::prism, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(ids(BotaniaBlocks.manaDetector))
				.addStoryBoard("detector", MachineScenes::detector, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(itemIds(BotaniaItems.spark))
				.addStoryBoard("sparks", SparkScenes::sparks, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(itemIds(BotaniaItems.sparkUpgradeDispersive, BotaniaItems.sparkUpgradeDominant, BotaniaItems.sparkUpgradeRecessive, BotaniaItems.sparkUpgradeIsolated))
				.addStoryBoard("spark_upgrades", SparkScenes::augments, BotaniaPonderTags.MANA_NETWORK);
		helper.forComponents(flowerIds(true))
				.addStoryBoard("generating_flowers", FlowerScenes::generating, BotaniaPonderTags.FLOWERS);
		helper.forComponents(flowerIds(false))
				.addStoryBoard("functional_flowers", FlowerScenes::functional, BotaniaPonderTags.FLOWERS);
		helper.forComponents(ids(BotaniaBlocks.brewery))
				.addStoryBoard("brewery", MachineScenes::brewery, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(ids(BotaniaBlocks.alchemyCatalyst, BotaniaBlocks.conjurationCatalyst))
				.addStoryBoard("catalysts", MachineScenes::catalysts, BotaniaPonderTags.CRAFTING_DEVICES);
		helper.forComponents(join(ids(BotaniaBlocks.corporeaIndex, BotaniaBlocks.corporeaCrystalCube), itemIds(BotaniaItems.corporeaSpark, BotaniaItems.corporeaSparkMaster)))
				.addStoryBoard("corporea_network", CorporeaScenes::network, BotaniaPonderTags.CORPOREA);
		helper.forComponents(ids(BotaniaBlocks.corporeaFunnel))
				.addStoryBoard("corporea_funnel", CorporeaScenes::funnel, BotaniaPonderTags.CORPOREA);
	}

	private static List<ResourceLocation> ids(Block... blocks) {
		return Arrays.stream(blocks).map(BuiltInRegistries.BLOCK::getKey).toList();
	}

	private static List<ResourceLocation> join(List<ResourceLocation> first, List<ResourceLocation> second) {
		List<ResourceLocation> result = new ArrayList<>(first);
		result.addAll(second);
		return result;
	}

	private static List<ResourceLocation> itemIds(Item... items) {
		return Arrays.stream(items).map(BuiltInRegistries.ITEM::getKey).toList();
	}

	private static List<ResourceLocation> lensIds() {
		return BuiltInRegistries.ITEM.keySet().stream()
				.filter(id -> LibMisc.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith("lens_"))
				.toList();
	}

	private static List<ResourceLocation> flowerIds(boolean generating) {
		List<ResourceLocation> result = new ArrayList<>();
		for (Block block : BuiltInRegistries.BLOCK) {
			ResourceLocation id = BuiltInRegistries.BLOCK.getKey(block);
			if (!LibMisc.MOD_ID.equals(id.getNamespace())
					|| !(block instanceof SpecialFlowerBlock || block instanceof FloatingSpecialFlowerBlock)
					|| !(block instanceof EntityBlock entityBlock)) {
				continue;
			}
			var be = entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
			if (!(be instanceof SpecialFlowerBlockEntity) || be instanceof PureDaisyBlockEntity || be instanceof ManastarBlockEntity) {
				continue;
			}
			if (be instanceof GeneratingFlowerBlockEntity == generating) {
				result.add(id);
			}
		}
		return result;
	}

	private static List<ResourceLocation> apothecaryIds() {
		return BuiltInRegistries.BLOCK.keySet().stream()
				.filter(id -> LibMisc.MOD_ID.equals(id.getNamespace()) && id.getPath().startsWith(LibBlockNames.APOTHECARY_PREFIX))
				.toList();
	}
}
