package velrondevs.botania.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;
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

	default Map<ResourceLocation, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.emptyMap();
	}

	default void registerPaintableBlock(Block block, Function<DyeColor, Block> transformer) {
		registerPaintableBlock(BuiltInRegistries.BLOCK.getKey(block), transformer);
	}

	default void registerPaintableBlock(ResourceLocation blockId, Function<DyeColor, Block> transformer) {

	}

	Holder<ArmorMaterial> DUMMY_ARMOR_MATERIAL = ArmorMaterials.LEATHER;

	Tier DUMMY_ITEM_TIER = new Tier() {
		@Override
		public int getUses() {
			return 0;
		}

		@Override
		public float getSpeed() {
			return 0;
		}

		@Override
		public float getAttackDamageBonus() {
			return 0;
		}

		@NotNull
		@Override
		public TagKey<Block> getIncorrectBlocksForDrops() {
			return BlockTags.INCORRECT_FOR_WOODEN_TOOL;
		}

		@Override
		public int getEnchantmentValue() {
			return 0;
		}

		@NotNull
		@Override
		public Ingredient getRepairIngredient() {
			return Ingredient.EMPTY;
		}
	};

	default Holder<ArmorMaterial> getManasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default Holder<ArmorMaterial> getElementiumArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default Holder<ArmorMaterial> getManaweaveArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default Holder<ArmorMaterial> getTerrasteelArmorMaterial() {
		return DUMMY_ARMOR_MATERIAL;
	}

	default Tier getManasteelItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default Tier getElementiumItemTier() {
		return DUMMY_ITEM_TIER;
	}

	default Tier getTerrasteelItemTier() {
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
