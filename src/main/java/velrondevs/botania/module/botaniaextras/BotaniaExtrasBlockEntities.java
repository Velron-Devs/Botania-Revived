package velrondevs.botania.module.botaniaextras;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlockEntity;
import velrondevs.botania.module.botaniaextras.block.FrozenStarBlockEntity;
import velrondevs.botania.module.botaniaextras.block.ItemPlatformBlockEntity;
import velrondevs.botania.module.botaniaextras.block.LightningRodBlockEntity;
import velrondevs.botania.module.botaniaextras.block.ManaFlashBlockEntity;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasBlockEntities {
	public static final BlockEntityType<FrozenStarBlockEntity> FROZEN_STAR = XplatAbstractions.INSTANCE.createBlockEntityType(FrozenStarBlockEntity::new, BotaniaExtrasBlocks.frozenStar);
	public static final BlockEntityType<ManaFlashBlockEntity> RAINBOW_MANA_FLASH = XplatAbstractions.INSTANCE.createBlockEntityType((pos, state) -> new ManaFlashBlockEntity(BotaniaExtrasBlockEntities.RAINBOW_MANA_FLASH, pos, state), BotaniaExtrasBlocks.rainbowManaFlash);
	public static final BlockEntityType<ManaFlashBlockEntity> PHANTOM_MANA_FLASH = XplatAbstractions.INSTANCE.createBlockEntityType((pos, state) -> new ManaFlashBlockEntity(BotaniaExtrasBlockEntities.PHANTOM_MANA_FLASH, pos, state), BotaniaExtrasBlocks.phantomManaFlash);

	public static final BlockEntityType<LightningRodBlockEntity> LIGHTNING_ROD = XplatAbstractions.INSTANCE.createBlockEntityType(LightningRodBlockEntity::new, BotaniaExtrasMagicWoods.get(BotaniaExtrasMagicWoods.Type.THUNDEROUS).log());
	public static final BlockEntityType<ItemPlatformBlockEntity> ITEM_PLATFORM = XplatAbstractions.INSTANCE.createBlockEntityType(ItemPlatformBlockEntity::new, BotaniaExtrasBlocks.manasteelItemPlatform, BotaniaExtrasBlocks.terrasteelItemPlatform, BotaniaExtrasBlocks.elementiumItemPlatform);
	public static final BlockEntityType<DendricSuffuserBlockEntity> DENDRIC_SUFFUSER = XplatAbstractions.INSTANCE.createBlockEntityType(DendricSuffuserBlockEntity::new, BotaniaExtrasBlocks.dendricSuffuser);

	private BotaniaExtrasBlockEntities() {}

	public static void registerBlockEntities(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		r.accept(FROZEN_STAR, prefix("frozen_star"));
		r.accept(RAINBOW_MANA_FLASH, prefix("rainbow_mana_flash"));
		r.accept(PHANTOM_MANA_FLASH, prefix("phantom_mana_flash"));
		r.accept(LIGHTNING_ROD, prefix("lightning_rod"));
		r.accept(ITEM_PLATFORM, prefix("item_platform"));
		r.accept(DENDRIC_SUFFUSER, prefix("dendric_suffuser"));
	}
}
