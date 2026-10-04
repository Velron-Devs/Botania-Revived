package velrondevs.botania.integration.wthit;

import mcp.mobius.waila.api.ICommonRegistrar;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IDataWriter;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerAccessor;
import mcp.mobius.waila.api.IWailaCommonPlugin;
import mcp.mobius.waila.api.IntFormat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

import velrondevs.botania.api.BotaniaAPI;

public class BotaniaWthitPlugin implements IWailaCommonPlugin {
	public static final ResourceLocation MANA_ENABLED = id("mana.enabled");
	public static final ResourceLocation MANA_COLOR = id("mana.color");
	public static final ResourceLocation MANA_SHORT = id("mana.short_numbers");
	public static final ResourceLocation POOL_MODE = id("pool.mode");
	public static final ResourceLocation POOL_RECIPE = id("pool.recipe");
	public static final ResourceLocation SPREADER_LENS = id("spreader.lens");
	public static final ResourceLocation SPREADER_TARGET = id("spreader.target");
	public static final ResourceLocation FLOWER_MANA = id("flower.mana");
	public static final ResourceLocation FLOWER_COLOR = id("flower.flower_color");
	public static final ResourceLocation FLOWER_BINDING = id("flower.binding");
	public static final ResourceLocation FLOWER_TIMERS = id("flower.timers");
	public static final ResourceLocation CRAFTING_RECIPE = id("crafting.recipe");
	public static final ResourceLocation CRAFTING_STATUS = id("crafting.status");
	public static final ResourceLocation PROGRESS_ENABLED = id("progress.enabled");
	public static final ResourceLocation PROGRESS_COLOR = id("progress.color");
	public static final ResourceLocation ENERGY_ENABLED = id("energy.enabled");
	public static final ResourceLocation ENERGY_COLOR = id("energy.color");
	public static final ResourceLocation PORTAL_ENABLED = id("portal.enabled");

	private static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, path);
	}

	@Override
	public void register(ICommonRegistrar registrar) {
		registrar.localConfig(MANA_ENABLED, true);
		registrar.localConfig(MANA_COLOR, 0x0095FF, IntFormat.RGB_HEX);
		registrar.localConfig(MANA_SHORT, false);
		registrar.localConfig(POOL_MODE, true);
		registrar.localConfig(POOL_RECIPE, true);
		registrar.localConfig(SPREADER_LENS, true);
		registrar.localConfig(SPREADER_TARGET, true);
		registrar.localConfig(FLOWER_MANA, true);
		registrar.localConfig(FLOWER_COLOR, true);
		registrar.localConfig(FLOWER_BINDING, true);
		registrar.localConfig(FLOWER_TIMERS, true);
		registrar.localConfig(CRAFTING_RECIPE, true);
		registrar.localConfig(CRAFTING_STATUS, true);
		registrar.localConfig(PROGRESS_ENABLED, true);
		registrar.localConfig(PROGRESS_COLOR, 0x3FBF3F, IntFormat.RGB_HEX);
		registrar.localConfig(ENERGY_ENABLED, true);
		registrar.localConfig(ENERGY_COLOR, 0xD03030, IntFormat.RGB_HEX);
		registrar.localConfig(PORTAL_ENABLED, true);

		registrar.blockData(DataProvider.INSTANCE, BlockEntity.class);
	}

	public enum DataProvider implements IDataProvider<BlockEntity> {
		INSTANCE;

		@Override
		public void appendData(IDataWriter data, IServerAccessor<BlockEntity> accessor, IPluginConfig config) {
			BlockEntity be = accessor.getTarget();
			if (be == null || !WthitData.isBotania(be)) {
				return;
			}
			CompoundTag tag = WthitData.collect(be, true);
			if (!tag.isEmpty()) {
				data.raw().put(WthitData.ROOT, tag);
			}
		}
	}
}
