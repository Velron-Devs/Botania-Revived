package velrondevs.botania.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.configdata.ConfigDataManager;
import velrondevs.botania.api.corporea.CorporeaNodeDetector;
import velrondevs.botania.api.internal.DummyManaNetwork;
import velrondevs.botania.api.internal.ManaNetwork;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

public interface BotaniaAPI {
	String MODID = "botania";
	String GOG_MODID = "gardenofglass";
	Logger LOGGER = LoggerFactory.getLogger(MODID);

	BotaniaAPI INSTANCE = ServiceUtil.findService(BotaniaAPI.class, () -> new BotaniaAPI() {});

	static BotaniaAPI instance() {
		return INSTANCE;
	}

	default int apiVersion() {
		return 0;
	}

	@Nullable
	default Registry<Brew> getBrewRegistry() {
		return null;
	}

	default Map<Identifier, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.emptyMap();
	}

	default void registerPaintableBlock(Block block, Function<DyeColor, Block> transformer) {
		registerPaintableBlock(BuiltInRegistries.BLOCK.getKey(block), transformer);
	}

	default void registerPaintableBlock(Identifier blockId, Function<DyeColor, Block> transformer) {

	}

	ArmorMaterial DUMMY_ARMOR_MATERIAL = ArmorMaterials.LEATHER;

	ToolMaterial DUMMY_ITEM_TIER = ToolMaterial.WOOD;

	default ArmorMaterial getManasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ArmorMaterial getElementiumArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ArmorMaterial getManaweaveArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ArmorMaterial getTerrasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default ToolMaterial getManasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default ToolMaterial getElementiumItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default ToolMaterial getTerrasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default ManaNetwork getManaNetworkInstance() {
		return DummyManaNetwork.instance;
	}

	default Container getAccessoriesInventory(Player player) {
		return new SimpleContainer(0);
	}

	default void breakOnAllCursors(Player player, ItemStack stack, BlockPos pos, Direction side) {}

	default boolean hasSolegnoliaAround(Entity e) {
		return false;
	}

	default void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float size, int m) {}

	default void registerCorporeaNodeDetector(CorporeaNodeDetector detector) {

	}

	default ConfigDataManager getConfigData() {
		return null;
	}

	default void setConfigData(ConfigDataManager configDataManager) {

	}
}
