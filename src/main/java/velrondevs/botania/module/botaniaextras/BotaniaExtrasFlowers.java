package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

import velrondevs.botania.common.block.FloatingSpecialFlowerBlock;
import velrondevs.botania.common.item.block.SpecialFlowerBlockItem;
import velrondevs.botania.module.botaniaextras.block.CrysanthermumBlockEntity;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasFlowers {
	public static final ResourceLocation CRYSANTHERMUM_ID = prefix("crysanthermum");
	public static final ResourceLocation CRYSANTHERMUM_FLOATING_ID = prefix("floating_crysanthermum");
	public static final ResourceLocation CRYSANTHERMUM_POTTED_ID = prefix("potted_crysanthermum");

	public static final Block crysanthermum = XplatAbstractions.INSTANCE.createSpecialFlowerBlock(MobEffects.FIRE_RESISTANCE, 100,
			BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY), () -> BotaniaExtrasFlowers.CRYSANTHERMUM, true);
	public static final Block crysanthermumFloating = new FloatingSpecialFlowerBlock(BotaniaBlocks.FLOATING_PROPS, () -> BotaniaExtrasFlowers.CRYSANTHERMUM, true);
	public static final Block crysanthermumPotted = new FlowerPotBlock(crysanthermum,
			BlockBehaviour.Properties.of().instabreak().noOcclusion().pushReaction(PushReaction.DESTROY));

	public static final BlockEntityType<CrysanthermumBlockEntity> CRYSANTHERMUM = XplatAbstractions.INSTANCE.createBlockEntityType(
			CrysanthermumBlockEntity::new, crysanthermum, crysanthermumFloating);

	private BotaniaExtrasFlowers() {}

	public static List<Block> all() {
		return List.of(crysanthermum, crysanthermumFloating, crysanthermumPotted);
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		r.accept(crysanthermum, CRYSANTHERMUM_ID);
		r.accept(crysanthermumFloating, CRYSANTHERMUM_FLOATING_ID);
		r.accept(crysanthermumPotted, CRYSANTHERMUM_POTTED_ID);
	}

	public static void registerItemBlocks(BiConsumer<Item, ResourceLocation> r) {
		Item.Properties props = BotaniaItems.defaultBuilder();
		r.accept(new SpecialFlowerBlockItem(crysanthermum, props), BuiltInRegistries.BLOCK.getKey(crysanthermum));
		r.accept(new SpecialFlowerBlockItem(crysanthermumFloating, props), BuiltInRegistries.BLOCK.getKey(crysanthermumFloating));
	}

	public static void registerBlockEntities(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		r.accept(CRYSANTHERMUM, CRYSANTHERMUM_ID);
	}

	public static void registerPottedPlants() {
		((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(CRYSANTHERMUM_ID, () -> crysanthermumPotted);
	}
}
