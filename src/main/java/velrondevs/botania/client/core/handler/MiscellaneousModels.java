package velrondevs.botania.client.core.handler;

import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.SimpleModelWrapper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import net.neoforged.neoforge.client.model.standalone.UnbakedStandaloneModel;

import velrondevs.botania.api.BotaniaAPIClient;
import velrondevs.botania.api.block.FloatingFlower;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.client.model.FloatingFlowerGeometry;
import velrondevs.botania.client.model.PlatformBakedModel;
import velrondevs.botania.client.render.block_entity.CorporeaCrystalCubeBlockEntityRenderer;
import velrondevs.botania.client.render.block_entity.ManaPumpBlockEntityRenderer;
import velrondevs.botania.common.block.FloatingSpecialFlowerBlock;
import velrondevs.botania.common.block.PlatformBlock;
import velrondevs.botania.common.block.decor.FloatingFlowerBlock;
import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.item.equipment.bauble.FlugelTiaraItem;
import velrondevs.botania.common.item.equipment.bauble.ThirdEyeItem;
import velrondevs.botania.common.item.relic.KeyOfTheKingsLawItem;
import velrondevs.botania.common.lib.LibMisc;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class MiscellaneousModels {
	private static final Identifier goldfishModelId = prefix("icon/goldfish");
	private static final Identifier phiFlowerModelId = prefix("icon/phiflower");
	private static final Identifier nerfBatModelId = prefix("icon/nerfbat");
	private static final Identifier bloodPendantChainId = prefix("icon/blood_pendant_chain");
	private static final Identifier bloodPendantGemId = prefix("icon/blood_pendant_gem");
	private static final Identifier[] kingKeyWeaponModelIds = IntStream.range(0, KeyOfTheKingsLawItem.WEAPON_TYPES)
			.mapToObj(i -> prefix("icon/gate_weapon_" + i)).toArray(Identifier[]::new);
	private static final Identifier terrasteelHelmWillModelId = prefix("icon/will_flame");
	private static final Identifier[] thirdEyeLayerIds = IntStream.range(0, ThirdEyeItem.Renderer.NUM_LAYERS)
			.mapToObj(i -> prefix("icon/third_eye_" + i)).toArray(Identifier[]::new);
	private static final Identifier pyroclastGemId = prefix("icon/lava_pendant_gem");
	private static final Identifier crimsonGemId = prefix("icon/super_lava_pendant_gem");
	private static final Identifier itemFinderGemId = prefix("icon/itemfinder_gem");
	private static final Identifier cirrusGemId = prefix("icon/cloud_pendant_gem");
	private static final Identifier nimbusGemId = prefix("icon/super_cloud_pendant_gem");
	private static final Identifier snowflakePendantGemId = prefix("icon/ice_pendant_gem");
	private static final Identifier[] tiaraWingIconIds = IntStream.range(0, FlugelTiaraItem.WING_TYPES)
			.mapToObj(i -> prefix("icon/tiara_wing_" + (i + 1))).toArray(Identifier[]::new);
	private static final Identifier corporeaCrystalCubeGlassId = prefix("block/corporea_crystal_cube_glass");
	private static final Identifier manaPumpHead = prefix("block/pump_head");
	private static final Identifier elvenSpreaderCoreId = prefix("block/elven_spreader_core");
	private static final Identifier gaiaSpreaderCoreId = prefix("block/gaia_spreader_core");
	private static final Identifier manaSpreaderCoreId = prefix("block/mana_spreader_core");
	private static final Identifier redstoneSpreaderCoreId = prefix("block/redstone_spreader_core");
	private static final Identifier manaSpreaderScaffoldingId = prefix("block/mana_spreader_scaffolding");
	private static final Identifier elvenSpreaderScaffoldingId = prefix("block/elven_spreader_scaffolding");
	private static final Identifier gaiaSpreaderScaffoldingId = prefix("block/gaia_spreader_scaffolding");
	private static final Identifier elementiumSpreaderCoreId = prefix("block/elementium_spreader_core");
	private static final Identifier elementiumSpreaderScaffoldingId = prefix("block/elementium_spreader_scaffolding");
	private static final Identifier terrasteelSpreaderCoreId = prefix("block/terrasteel_spreader_core");
	private static final Identifier terrasteelSpreaderScaffoldingId = prefix("block/terrasteel_spreader_scaffolding");
	private static final Identifier alfsteelSpreaderCoreId = prefix("block/alfsteel_spreader_core");
	private static final Identifier alfsteelSpreaderScaffoldingId = prefix("block/alfsteel_spreader_scaffolding");
	private static final Map<DyeColor, Identifier> spreaderPaddingIds = new EnumMap<>(ColorHelper.supportedColors().collect(Collectors.toMap(Function.identity(), color -> prefix("block/" + color.getSerializedName() + "_spreader_padding"))));

	public static final MiscellaneousModels INSTANCE = new MiscellaneousModels();

	private final Map<Identifier, Consumer<BlockStateModelPart>> modelConsumers;
	private final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> modelKeys = new LinkedHashMap<>();
	private final Map<Identifier, StandaloneModelKey<BlockStateModelPart>> taterKeys = new LinkedHashMap<>();
	private final Map<Identifier, BlockStateModelPart> taterModels = new HashMap<>();
	private final Map<FloatingFlower.IslandType, StandaloneModelKey<BlockStateModelPart>> islandKeys = new HashMap<>();
	private final Map<FloatingFlower.IslandType, BlockStateModelPart> islandModels = new HashMap<>();

	public boolean registeredModels = false;

	public final BlockStateModelPart[] tiaraWingIcons;
	public final BlockStateModelPart[] thirdEyeLayers;

	public BlockStateModelPart goldfishModel,
			phiFlowerModel,
			nerfBatModel,
			bloodPendantChain,
			bloodPendantGem,
			snowflakePendantGem,
			itemFinderGem,
			pyroclastGem,
			crimsonGem,
			cirrusGem,
			nimbusGem,
			terrasteelHelmWillModel,
			elvenSpreaderCore,
			gaiaSpreaderCore,
			manaSpreaderCore,
			redstoneSpreaderCore,
			manaSpreaderScaffolding,
			elvenSpreaderScaffolding,
			gaiaSpreaderScaffolding,
			elementiumSpreaderCore,
			elementiumSpreaderScaffolding,
			terrasteelSpreaderCore,
			terrasteelSpreaderScaffolding,
			alfsteelSpreaderCore,
			alfsteelSpreaderScaffolding;

	public final HashMap<DyeColor, BlockStateModelPart> spreaderPaddings = new HashMap<>();

	public final BlockStateModelPart[] kingKeyWeaponModels;

	public void onModelRegister(ResourceManager rm, BiConsumer<StandaloneModelKey<BlockStateModelPart>, UnbakedStandaloneModel<BlockStateModelPart>> consumer) {
		modelKeys.clear();
		taterKeys.clear();
		for (Identifier id : modelConsumers.keySet()) {
			StandaloneModelKey<BlockStateModelPart> key = new StandaloneModelKey<>(id::toString);
			modelKeys.put(id, key);
			consumer.accept(key, id.getPath().startsWith("icon/") ? iconModel(id) : SimpleUnbakedStandaloneModel.simpleModelWrapper(id));
		}

		registerIslands();
		islandKeys.clear();
		for (var e : BotaniaAPIClient.instance().getRegisteredIslandTypeModels().entrySet()) {
			StandaloneModelKey<BlockStateModelPart> key = new StandaloneModelKey<>(e.getValue()::toString);
			islandKeys.put(e.getKey(), key);
			consumer.accept(key, SimpleUnbakedStandaloneModel.simpleModelWrapper(e.getValue()));
		}
		for (Identifier id : findTaters(rm)) {
			StandaloneModelKey<BlockStateModelPart> key = new StandaloneModelKey<>(id::toString);
			taterKeys.put(id, key);
			consumer.accept(key, SimpleUnbakedStandaloneModel.simpleModelWrapper(id));
		}

		registeredModels = true;
	}

	public static UnbakedStandaloneModel<BlockStateModelPart> iconModel(Identifier id) {
		return new SimpleUnbakedStandaloneModel<>(id, (model, baker, name) -> {
			var slots = model.getTopTextureSlots();
			return new SimpleModelWrapper(model.bakeTopGeometry(slots, baker, BlockModelRotation.IDENTITY),
					model.getTopAmbientOcclusion(), model.resolveParticleMaterial(slots, baker));
		});
	}

	private static void registerIslands() {
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.GRASS, prefix("block/islands/island_grass"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.PODZOL, prefix("block/islands/island_podzol"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.MYCEL, prefix("block/islands/island_mycel"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.SNOW, prefix("block/islands/island_snow"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.DRY, prefix("block/islands/island_dry"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.GOLDEN, prefix("block/islands/island_golden"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.VIVID, prefix("block/islands/island_vivid"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.SCORCHED, prefix("block/islands/island_scorched"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.INFUSED, prefix("block/islands/island_infused"));
		BotaniaAPIClient.instance().registerIslandTypeModel(FloatingFlower.IslandType.MUTATED, prefix("block/islands/island_mutated"));
	}

	private static List<Identifier> findTaters(ResourceManager rm) {
		List<Identifier> ret = new ArrayList<>();
		for (Identifier model : rm.listResources(ResourcesLib.PREFIX_MODELS + ResourcesLib.PREFIX_TINY_POTATO, s -> s.getPath().endsWith(ResourcesLib.ENDING_JSON)).keySet()) {
			if (LibMisc.MOD_ID.equals(model.getNamespace())) {
				String path = model.getPath();
				path = path.substring(ResourcesLib.PREFIX_MODELS.length(), path.length() - ResourcesLib.ENDING_JSON.length());
				ret.add(Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, path));
			}
		}
		return ret;
	}

	public void onModifyBakingResult(ModelBakery.BakingResult result) {
		Map<BlockState, BlockStateModel> models = result.blockStateModels();
		for (Block block : BuiltInRegistries.BLOCK) {
			if (block instanceof FloatingFlowerBlock || block instanceof FloatingSpecialFlowerBlock) {
				for (BlockState state : block.getStateDefinition().getPossibleStates()) {
					models.computeIfPresent(state, (s, model) -> new FloatingFlowerGeometry.Baked(model));
				}
			}
			if (block instanceof PlatformBlock) {
				for (BlockState state : block.getStateDefinition().getPossibleStates()) {
					models.computeIfPresent(state, (s, model) -> new PlatformBakedModel(model));
				}
			}
		}
	}

	public void onModelBake(ModelManager manager) {
		if (!registeredModels) {
			return;
		}
		modelKeys.forEach((id, key) -> modelConsumers.get(id).accept(manager.getStandaloneModel(key)));
		taterModels.clear();
		taterKeys.forEach((id, key) -> taterModels.put(id, manager.getStandaloneModel(key)));
		islandModels.clear();
		islandKeys.forEach((type, key) -> islandModels.put(type, manager.getStandaloneModel(key)));
	}

	public BlockStateModelPart getIslandModel(FloatingFlower.IslandType type) {
		BlockStateModelPart ret = islandModels.get(type);
		return ret != null ? ret : islandModels.get(FloatingFlower.IslandType.GRASS);
	}

	public BlockStateModelPart getTaterModel(Identifier id) {
		return taterModels.get(id);
	}

	private MiscellaneousModels() {
		modelConsumers = new HashMap<>();
		modelConsumers.put(elvenSpreaderCoreId, bakedModel -> this.elvenSpreaderCore = bakedModel);
		modelConsumers.put(gaiaSpreaderCoreId, bakedModel -> this.gaiaSpreaderCore = bakedModel);
		modelConsumers.put(manaSpreaderCoreId, bakedModel -> this.manaSpreaderCore = bakedModel);
		modelConsumers.put(redstoneSpreaderCoreId, bakedModel -> this.redstoneSpreaderCore = bakedModel);
		modelConsumers.put(manaSpreaderScaffoldingId, bakedModel -> this.manaSpreaderScaffolding = bakedModel);
		modelConsumers.put(elvenSpreaderScaffoldingId, bakedModel -> this.elvenSpreaderScaffolding = bakedModel);
		modelConsumers.put(gaiaSpreaderScaffoldingId, bakedModel -> this.gaiaSpreaderScaffolding = bakedModel);
		modelConsumers.put(elementiumSpreaderCoreId, bakedModel -> this.elementiumSpreaderCore = bakedModel);
		modelConsumers.put(elementiumSpreaderScaffoldingId, bakedModel -> this.elementiumSpreaderScaffolding = bakedModel);
		modelConsumers.put(terrasteelSpreaderCoreId, bakedModel -> this.terrasteelSpreaderCore = bakedModel);
		modelConsumers.put(terrasteelSpreaderScaffoldingId, bakedModel -> this.terrasteelSpreaderScaffolding = bakedModel);
		modelConsumers.put(alfsteelSpreaderCoreId, bakedModel -> this.alfsteelSpreaderCore = bakedModel);
		modelConsumers.put(alfsteelSpreaderScaffoldingId, bakedModel -> this.alfsteelSpreaderScaffolding = bakedModel);
		for (var color : spreaderPaddingIds.keySet()) {
			modelConsumers.put(spreaderPaddingIds.get(color), bakedModel -> spreaderPaddings.put(color, bakedModel));
		}

		modelConsumers.put(corporeaCrystalCubeGlassId, bakedModel -> CorporeaCrystalCubeBlockEntityRenderer.cubeModel = bakedModel);
		modelConsumers.put(manaPumpHead, bakedModel -> ManaPumpBlockEntityRenderer.headModel = bakedModel);

		modelConsumers.put(goldfishModelId, bakedModel -> this.goldfishModel = bakedModel);
		modelConsumers.put(phiFlowerModelId, bakedModel -> this.phiFlowerModel = bakedModel);
		modelConsumers.put(nerfBatModelId, bakedModel -> this.nerfBatModel = bakedModel);
		modelConsumers.put(bloodPendantChainId, bakedModel -> this.bloodPendantChain = bakedModel);
		modelConsumers.put(bloodPendantGemId, bakedModel -> this.bloodPendantGem = bakedModel);
		modelConsumers.put(terrasteelHelmWillModelId, bakedModel -> this.terrasteelHelmWillModel = bakedModel);
		modelConsumers.put(pyroclastGemId, bakedModel -> this.pyroclastGem = bakedModel);
		modelConsumers.put(crimsonGemId, bakedModel -> this.crimsonGem = bakedModel);
		modelConsumers.put(itemFinderGemId, bakedModel -> this.itemFinderGem = bakedModel);
		modelConsumers.put(cirrusGemId, bakedModel -> this.cirrusGem = bakedModel);
		modelConsumers.put(nimbusGemId, bakedModel -> this.nimbusGem = bakedModel);
		modelConsumers.put(snowflakePendantGemId, bakedModel -> this.snowflakePendantGem = bakedModel);

		kingKeyWeaponModels = getBakedModels(modelConsumers, kingKeyWeaponModelIds);
		thirdEyeLayers = getBakedModels(modelConsumers, thirdEyeLayerIds);
		tiaraWingIcons = getBakedModels(modelConsumers, tiaraWingIconIds);
	}

	private static BlockStateModelPart[] getBakedModels(Map<Identifier, Consumer<BlockStateModelPart>> consumers, Identifier[] ids) {
		final BlockStateModelPart[] bakedModels = new BlockStateModelPart[ids.length];
		for (int i = 0; i < ids.length; i++) {
			int index = i;
			consumers.put(ids[index], bakedModel -> bakedModels[index] = bakedModel);
		}
		return bakedModels;
	}
}
