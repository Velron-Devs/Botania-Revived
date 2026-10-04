package velrondevs.botania.common.impl;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorMaterial;
import velrondevs.botania.registry.BotaniaArmorMaterials;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.BotaniaRegistries;
import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.api.configdata.ConfigDataManager;
import velrondevs.botania.api.corporea.CorporeaNodeDetector;
import velrondevs.botania.api.internal.ManaNetwork;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.common.block.flower.functional.SolegnoliaBlockEntity;
import velrondevs.botania.common.config.ConfigDataManagerImpl;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.handler.ManaNetworkHandler;
import velrondevs.botania.common.integration.corporea.CorporeaNodeDetectors;
import velrondevs.botania.common.item.relic.RingOfLokiItem;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaSounds;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class BotaniaAPIImpl implements BotaniaAPI {

	private ConfigDataManager configDataManager = new ConfigDataManagerImpl();

	@Override
	public int apiVersion() {
		return 2;
	}

	@Nullable
	@Override
	@SuppressWarnings("unchecked")
	public Registry<Brew> getBrewRegistry() {
		return (Registry<Brew>) BuiltInRegistries.REGISTRY.get(BotaniaRegistries.BREWS.location());
	}

	@Override
	public Holder<ArmorMaterial> getManasteelArmorMaterial() {
		return BotaniaArmorMaterials.MANASTEEL;
	}

	@Override
	public Holder<ArmorMaterial> getElementiumArmorMaterial() {
		return BotaniaArmorMaterials.ELEMENTIUM;
	}

	@Override
	public Holder<ArmorMaterial> getManaweaveArmorMaterial() {
		return BotaniaArmorMaterials.MANAWEAVE;
	}

	@Override
	public Holder<ArmorMaterial> getTerrasteelArmorMaterial() {
		return BotaniaArmorMaterials.TERRASTEEL;
	}

	@Override
	public Tier getManasteelItemTier() {
		return BotaniaArmorMaterials.MANASTEEL_TIER;
	}

	@Override
	public Tier getElementiumItemTier() {
		return BotaniaArmorMaterials.ELEMENTIUM_TIER;
	}

	@Override
	public Tier getTerrasteelItemTier() {
		return BotaniaArmorMaterials.TERRASTEEL_TIER;
	}

	@Override
	public ManaNetwork getManaNetworkInstance() {
		return ManaNetworkHandler.instance;
	}

	@Override
	public Container getAccessoriesInventory(Player player) {
		return EquipmentHandler.getAllWorn(player);
	}

	@Override
	public void breakOnAllCursors(Player player, ItemStack stack, BlockPos pos, Direction side) {
		RingOfLokiItem.breakOnAllCursors(player, stack, pos, side);
	}

	@Override
	public boolean hasSolegnoliaAround(Entity e) {
		return SolegnoliaBlockEntity.hasSolegnoliaAround(e);
	}

	@Override
	public void sparkleFX(Level world, double x, double y, double z, float r, float g, float b, float size, int m) {
		SparkleParticleData data = SparkleParticleData.sparkle(size, r, g, b, m);
		world.addParticle(data, x, y, z, 0, 0, 0);
	}

	private final Map<ResourceLocation, Function<DyeColor, Block>> paintableBlocks = new ConcurrentHashMap<>();

	@Override
	public Map<ResourceLocation, Function<DyeColor, Block>> getPaintableBlocks() {
		return Collections.unmodifiableMap(paintableBlocks);
	}

	@Override
	public void registerPaintableBlock(ResourceLocation block, Function<DyeColor, Block> transformer) {
		paintableBlocks.put(block, transformer);
	}

	@Override
	public void registerCorporeaNodeDetector(CorporeaNodeDetector detector) {
		CorporeaNodeDetectors.register(detector);
	}

	@Override
	public ConfigDataManager getConfigData() {
		return configDataManager;
	}

	@Override
	public void setConfigData(ConfigDataManager configDataManager) {
		this.configDataManager = configDataManager;
	}
}
