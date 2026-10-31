package velrondevs.botania.compat.sophisticated.storage;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.Property;
import net.p3pp3rf1y.sophisticatedstorage.block.BarrelBlock;
import net.p3pp3rf1y.sophisticatedstorage.block.ChestBlock;
import net.p3pp3rf1y.sophisticatedstorage.block.ChestBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockBase;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.init.ModBlocks;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.compat.sophisticated.SophisticatedOptional;

import java.util.List;
import java.util.Optional;

public class BotaniaStorageTierUpgradeItem extends Item {
	private final BotaniaStorageTier target;

	public BotaniaStorageTierUpgradeItem(BotaniaStorageTier target) {
		super(properties(target));
		this.target = target;
	}

	private static Item.Properties properties(BotaniaStorageTier target) {
		Item.Properties properties = new Item.Properties();
		return target.fireResistant() ? properties.fireResistant() : properties;
	}

	public BotaniaStorageTier target() {
		return target;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.GRAY));
		if (target.previous() == null && SophisticatedOptional.atmTiers()) {
			tooltip.add(Component.translatable(getDescriptionId() + ".tooltip.atm").withStyle(ChatFormatting.AQUA));
		}
		super.appendHoverText(stack, context, tooltip, flag);
	}

	@Override
	public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
		if (!(block instanceof BarrelBlock) && !(block instanceof ChestBlock)) {
			return InteractionResult.PASS;
		}
		Player player = context.getPlayer();
		if (!isSource(block)) {
			if (!level.isClientSide && player != null) {
				player.displayClientMessage(Component.translatable("message.botania.storage_tier_upgrade.wrong_tier",
						Component.translatable("botania.sophisticated.tier." + sourceTierKey())), true);
			}
			return InteractionResult.FAIL;
		}
		if (!(level instanceof ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (!(blockEntity instanceof StorageBlockEntity storage)) {
			return InteractionResult.PASS;
		}
		StorageBlockEntity authority = storage;
		if (storage instanceof ChestBlockEntity chest && chest.getMainChestBlockEntity() != null) {
			authority = chest.getMainChestBlockEntity();
		}
		if (!authority.canUpgradeStorageTier(serverLevel)) {
			if (player != null) {
				player.playNotifySound(SoundEvents.NOTE_BLOCK_BASS.value(), SoundSource.PLAYERS, 1.0F, 0.7F);
				player.displayClientMessage(Component.translatable("message.botania.storage_tier_upgrade.linked_main_only"), true);
			}
			return InteractionResult.FAIL;
		}
		if (isBlocked(storage)) {
			return InteractionResult.PASS;
		}
		boolean doubleChest = block instanceof ChestBlock && state.getValue(ChestBlock.TYPE) != ChestType.SINGLE;
		int required = doubleChest ? 2 : 1;
		boolean creative = player != null && player.getAbilities().instabuild;
		if (!creative && stack.getCount() < required) {
			if (player != null) {
				player.displayClientMessage(Component.translatable("message.botania.storage_tier_upgrade.count", required, stack.getHoverName()), true);
			}
			return InteractionResult.FAIL;
		}
		boolean upgraded;
		if (doubleChest) {
			upgraded = storage instanceof ChestBlockEntity chest && upgradeDouble(serverLevel, pos, state, chest);
		} else {
			upgraded = upgradeSingle(serverLevel, pos, state, storage);
		}
		if (!upgraded) {
			return InteractionResult.PASS;
		}
		level.playSound(null, pos, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 0.8F, 1.0F);
		if (!creative) {
			stack.shrink(required);
		}
		return InteractionResult.SUCCESS;
	}

	private String sourceTierKey() {
		return target.previous() == null && SophisticatedOptional.atmTiers() ? "netherite_or_unobtainium" : target.previousId();
	}

	private boolean isSource(Block block) {
		BotaniaStorageTier tier = BotaniaStorageRegistry.tierOf(block);
		if (tier != null) {
			return tier == target.previous();
		}
		return target.previous() == null && isBaseTier(block);
	}

	private static boolean isBaseTier(Block block) {
		if (block == ModBlocks.NETHERITE_BARREL.get() || block == ModBlocks.NETHERITE_CHEST.get()) {
			return true;
		}
		return SophisticatedOptional.atmTiers() && (isRegistered(SophisticatedOptional.ATM_UNOBTAINIUM_BARREL, block)
				|| isRegistered(SophisticatedOptional.ATM_UNOBTAINIUM_CHEST, block));
	}

	private static boolean isRegistered(String id, Block block) {
		ResourceLocation key = ResourceLocation.tryParse(id);
		if (key == null) {
			return false;
		}
		Optional<Block> registered = BuiltInRegistries.BLOCK.getOptional(key);
		return registered.isPresent() && registered.get() == block;
	}

	private StorageBlockBase targetBlock(Block source) {
		return source instanceof BarrelBlock ? BotaniaStorageRegistry.barrel(target) : BotaniaStorageRegistry.chest(target);
	}

	private static boolean isBlocked(StorageBlockEntity storage) {
		return storage.isOpen() || storage instanceof WoodStorageBlockEntity wood && wood.isPacked();
	}

	private boolean upgradeSingle(ServerLevel level, BlockPos pos, BlockState state, StorageBlockEntity storage) {
		StorageBlockBase block = targetBlock(state.getBlock());
		StorageBlockEntity result = replace(level, pos, storage, convert(state, block), block.getNumberOfInventorySlots(), block.getNumberOfUpgradeSlots());
		result.setBeingUpgraded(false);
		return true;
	}

	private boolean upgradeDouble(ServerLevel level, BlockPos pos, BlockState state, ChestBlockEntity clicked) {
		BlockPos otherPos = pos.relative(ChestBlock.getConnectedDirection(state));
		BlockState otherState = level.getBlockState(otherPos);
		if (!(otherState.getBlock() instanceof ChestBlock) || !isSource(otherState.getBlock())
				|| otherState.getValue(ChestBlock.TYPE) == ChestType.SINGLE) {
			return false;
		}
		if (!(level.getBlockEntity(otherPos) instanceof ChestBlockEntity other) || isBlocked(other)) {
			return false;
		}
		StorageBlockBase block = BotaniaStorageRegistry.chest(target);
		boolean clickedIsMain = clicked.isMainChest();
		int single = block.getNumberOfInventorySlots();
		int upgrades = block.getNumberOfUpgradeSlots();
		BlockState clickedState = convert(state, block);
		BlockState otherTargetState = convert(otherState, block);
		clicked.setBeingUpgraded(true);
		other.setBeingUpgraded(true);
		StorageBlockEntity newClicked = replace(level, pos, clicked, clickedState, clickedIsMain ? single * 2 : single, upgrades);
		StorageBlockEntity newOther = replace(level, otherPos, other, otherTargetState, clickedIsMain ? single : single * 2, upgrades);
		newClicked.setBeingUpgraded(false);
		newOther.setBeingUpgraded(false);
		otherTargetState.updateNeighbourShapes(level, otherPos, 3);
		if ((clickedIsMain ? newClicked : newOther) instanceof ChestBlockEntity main) {
			main.changeSlots(main.getStorageWrapper().getInventoryHandler().getSlots());
		}
		return true;
	}

	private static StorageBlockEntity replace(ServerLevel level, BlockPos pos, StorageBlockEntity old, BlockState newState, int inventorySlots, int upgradeSlots) {
		CompoundTag data = new CompoundTag();
		old.saveAdditional(data, level.registryAccess());
		StorageBlockEntity fresh = ((StorageBlockBase) newState.getBlock()).newBlockEntity(pos, newState);
		fresh.setBeingUpgraded(true);
		fresh.loadAdditional(data, level.registryAccess());
		old.setBeingUpgraded(true);
		old.closeMenusForThisBlock();
		level.removeBlockEntity(pos);
		level.removeBlock(pos, false);
		level.setBlock(pos, newState, 3);
		level.setBlockEntity(fresh);
		if (fresh.isLinkedStorage()) {
			if (!fresh.completePrimaryLinkedStorageTierUpgrade(level, inventorySlots, upgradeSlots)) {
				BotaniaAPI.LOGGER.warn("Could not update the linked storage at {} after a tier upgrade", pos);
			}
		} else {
			fresh.changeStorageSize(inventorySlots - fresh.getStorageWrapper().getInventoryHandler().getSlots(),
					upgradeSlots - fresh.getStorageWrapper().getUpgradeHandler().getSlots());
		}
		fresh.setChanged();
		level.sendBlockUpdated(pos, newState, newState, 3);
		return fresh;
	}

	private static BlockState convert(BlockState from, StorageBlockBase block) {
		BlockState result = block.defaultBlockState();
		for (Property<?> property : from.getProperties()) {
			if (property != BarrelBlock.OPEN && result.hasProperty(property)) {
				result = copy(from, result, property);
			}
		}
		return result;
	}

	private static <T extends Comparable<T>> BlockState copy(BlockState from, BlockState to, Property<T> property) {
		return to.setValue(property, from.getValue(property));
	}
}
