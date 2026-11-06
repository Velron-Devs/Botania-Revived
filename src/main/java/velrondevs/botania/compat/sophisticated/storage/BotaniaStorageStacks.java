package velrondevs.botania.compat.sophisticated.storage;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.p3pp3rf1y.sophisticatedstorage.Config;
import net.p3pp3rf1y.sophisticatedstorage.block.BarrelBlock;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageWrapper;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockBase;
import net.p3pp3rf1y.sophisticatedstorage.block.WoodStorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.item.BarrelBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.item.ChestBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.item.StorageBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.item.WoodStorageBlockItem;
import net.p3pp3rf1y.sophisticatedstorage.util.GenericWoodStorageHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class BotaniaStorageStacks {
	public static final WoodType DEFAULT_WOOD = WoodType.ACACIA;

	private BotaniaStorageStacks() {}

	public static void applyDefaultWood(ItemStack stack) {
		if (WoodStorageBlockItem.getWoodType(stack).isPresent()) {
			return;
		}
		if (StorageBlockItem.getMainColorFromComponentHolder(stack).isPresent()
				&& StorageBlockItem.getAccentColorFromComponentHolder(stack).isPresent()) {
			return;
		}
		WoodStorageBlockItem.setWoodType(stack, DEFAULT_WOOD);
	}

	public static void applyDefaultWood(WoodStorageBlockEntity entity) {
		StorageWrapper wrapper = entity.getStorageWrapper();
		if (entity.getWoodType().isEmpty() && !(wrapper.hasMainColor() && wrapper.hasAccentColor())) {
			entity.setWoodType(DEFAULT_WOOD);
		}
	}

	public static void addCreativeVariants(CreativeModeTab.Output output, Block block) {
		boolean configLoaded = Config.CLIENT_SPEC.isLoaded();
		boolean single = configLoaded && Config.CLIENT.showSingleWoodVariantOnly.get();
		for (WoodType wood : woodVariants(configLoaded, single)) {
			output.accept(WoodStorageBlockItem.setWoodType(new ItemStack(block), wood));
		}
		if (block instanceof BarrelBlock) {
			ItemStack flat = WoodStorageBlockItem.setWoodType(new ItemStack(block), DEFAULT_WOOD);
			BarrelBlockItem.setFlatTop(flat, true);
			output.accept(flat);
		}
	}

	private static Collection<WoodType> woodVariants(boolean configLoaded, boolean single) {
		if (single) {
			return List.of(DEFAULT_WOOD);
		}
		List<WoodType> woods = new ArrayList<>(WoodStorageBlockBase.CUSTOM_TEXTURE_WOOD_TYPES.keySet());
		if (configLoaded && Config.CLIENT.showHigherTierGenericWoodVariants.get()) {
			for (WoodType generic : GenericWoodStorageHelper.getGenericWoodTypes()) {
				if (!woods.contains(generic)) {
					woods.add(generic);
				}
			}
		}
		return woods;
	}

	public static String subtypeKey(ItemStack stack) {
		StringBuilder key = new StringBuilder();
		key.append(WoodStorageBlockItem.getWoodType(stack).map(WoodType::name).orElse(""));
		key.append('|').append(StorageBlockItem.getMainColorFromComponentHolder(stack).map(String::valueOf).orElse(""));
		key.append('|').append(StorageBlockItem.getAccentColorFromComponentHolder(stack).map(String::valueOf).orElse(""));
		if (stack.getItem() instanceof BarrelBlockItem) {
			key.append("|flat=").append(BarrelBlockItem.isFlatTop(stack));
		} else if (stack.getItem() instanceof ChestBlockItem) {
			key.append("|double=").append(ChestBlockItem.isDoubleChest(stack));
		}
		return key.toString();
	}
}
