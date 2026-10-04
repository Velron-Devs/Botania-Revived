package velrondevs.botania.module.asgard;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import velrondevs.botania.common.block.FloatingSpecialFlowerBlock;
import velrondevs.botania.common.item.block.SpecialFlowerBlockItem;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaMobEffects;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class AsgardFlowers {
	public static final ResourceLocation ASGARDANDELION_ID = prefix("asgardandelion");
	public static final ResourceLocation ASGARDANDELION_FLOATING_ID = prefix("floating_asgardandelion");
	public static final ResourceLocation ASGARDANDELION_POTTED_ID = prefix("potted_asgardandelion");

	public static final Block asgardandelion = XplatAbstractions.INSTANCE.createSpecialFlowerBlock(BotaniaMobEffects.clear, 1,
			BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), () -> AsgardFlowers.ASGARDANDELION);
	public static final Block asgardandelionFloating = new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> AsgardFlowers.ASGARDANDELION);
	public static final Block asgardandelionPotted = new FlowerPotBlock(asgardandelion,
			BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

	public static final BlockEntityType<AsgardandelionBlockEntity> ASGARDANDELION = XplatAbstractions.INSTANCE.createBlockEntityType(
			AsgardandelionBlockEntity::new, asgardandelion, asgardandelionFloating);

	private AsgardFlowers() {}

	public static List<Block> all() {
		return List.of(asgardandelion, asgardandelionFloating, asgardandelionPotted);
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		r.accept(asgardandelion, ASGARDANDELION_ID);
		r.accept(asgardandelionFloating, ASGARDANDELION_FLOATING_ID);
		r.accept(asgardandelionPotted, ASGARDANDELION_POTTED_ID);
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = BotaniaItems.defaultBuilder().rarity(Rarity.EPIC);
		r.accept(new SpecialFlowerBlockItem(asgardandelion, props), BuiltInRegistries.BLOCK.getKey(asgardandelion));
		r.accept(new SpecialFlowerBlockItem(asgardandelionFloating, props), BuiltInRegistries.BLOCK.getKey(asgardandelionFloating));
	}

	public static void registerBlockEntities(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		r.accept(ASGARDANDELION, ASGARDANDELION_ID);
	}

	public static void registerPottedPlants() {
		((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ASGARDANDELION_ID, () -> asgardandelionPotted);
	}
}
