package velrondevs.botania.datagen.providers.models;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.model.DelegatedModel;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.RangeSelectItemModel;
import net.minecraft.client.renderer.special.SkullSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.WallBlock;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.client.BotaniaItemProperties;
import velrondevs.botania.client.model.ManaBlasterItemModel;
import velrondevs.botania.client.render.BlockEntityItemRendererHelper;
import velrondevs.botania.client.render.ColorHandler;
import velrondevs.botania.common.block.*;
import velrondevs.botania.common.block.decor.BotaniaMushroomBlock;
import velrondevs.botania.common.block.decor.FloatingFlowerBlock;
import velrondevs.botania.common.block.decor.FlowerMotifBlock;
import velrondevs.botania.common.block.decor.PetalBlock;
import velrondevs.botania.common.block.mana.ManaPoolBlock;
import velrondevs.botania.common.block.mana.ManaSpreaderBlock;
import velrondevs.botania.common.item.BottledManaItem;
import velrondevs.botania.common.item.brew.BaseBrewItem;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.common.item.material.MysticalPetalItem;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.datagen.util.ItemDefinitions;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;
import static velrondevs.botania.registry.BotaniaItems.*;

public class ItemModelProvider implements DataProvider {
	private static final TextureSlot LAYER3 = TextureSlot.create("layer3");
	private static final Identifier GENERATED = Identifier.withDefaultNamespace("item/generated");
	private static final Identifier HANDHELD = Identifier.withDefaultNamespace("item/handheld");
	private static final ModelTemplate GENERATED_1 = new ModelTemplate(Optional.of(GENERATED), Optional.empty(), TextureSlot.LAYER0, TextureSlot.LAYER1);
	private static final ModelTemplate GENERATED_2 = new ModelTemplate(Optional.of(GENERATED), Optional.empty(), TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2);
	private static final ModelTemplate HANDHELD_1 = new ModelTemplate(Optional.of(HANDHELD), Optional.empty(), TextureSlot.LAYER0, TextureSlot.LAYER1);
	private static final ModelTemplate HANDHELD_2 = new ModelTemplate(Optional.of(HANDHELD), Optional.empty(), TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2);
	private static final ModelTemplate HANDHELD_3 = new ModelTemplate(Optional.of(HANDHELD), Optional.empty(), TextureSlot.LAYER0, TextureSlot.LAYER1, TextureSlot.LAYER2, LAYER3);
	private static final ModelTemplate WALL_INVENTORY = new ModelTemplate(Optional.of(prefix("block/shapes/wall_inventory")), Optional.empty(), TextureSlot.TOP, TextureSlot.BOTTOM, TextureSlot.WALL);
	private static final ModelTemplate WALL_INVENTORY_CHECKERED = new ModelTemplate(Optional.of(prefix("block/shapes/wall_inventory_checkered")), Optional.empty(), TextureSlot.NORTH, TextureSlot.SIDE);
	private static final TextureSlot OUTSIDE = TextureSlot.create("outside");
	private static final TextureSlot CORE = TextureSlot.create("core");
	private static final ModelTemplate SPREADER = new ModelTemplate(Optional.of(prefix("block/shapes/spreader_item")), Optional.empty(), TextureSlot.SIDE, TextureSlot.BACK, TextureSlot.INSIDE, OUTSIDE, CORE);

	private final PackOutput packOutput;
	private final Map<Identifier, ModelInstance> models = new HashMap<>();
	private final ItemDefinitions definitions = new ItemDefinitions();
	private final Map<Item, List<ItemTintSource>> tints = new HashMap<>();

	public ItemModelProvider(PackOutput packOutput) {
		this.packOutput = packOutput;
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		models.clear();
		definitions.clear();
		tints.clear();
		registerTints();

		Set<Item> items = BuiltInRegistries.ITEM.stream().filter(i -> LibMisc.MOD_ID.equals(BuiltInRegistries.ITEM.getKey(i).getNamespace()) && !BotaniaModules.isModuleItem(i))
				.collect(Collectors.toSet());
		registerItemBlocks(takeAll(items, i -> i instanceof BlockItem).stream().map(i -> (BlockItem) i).collect(Collectors.toSet()));
		registerItemOverrides(items);
		registerItems(items);
		registerStandaloneModels();

		definitions.fillFromItemModels(models.keySet(), this::tintsOf);

		return CompletableFuture.allOf(
				ItemDefinitions.saveModels(cache, packOutput, models),
				definitions.save(cache, packOutput));
	}

	private static ItemTintSource tint(ColorHandler.Kind kind) {
		return new ColorHandler.Tint(kind);
	}

	private static ItemTintSource none() {
		return ItemModelUtils.constantTint(-1);
	}

	private void tint(List<ItemTintSource> sources, Item... targets) {
		for (Item item : targets) {
			tints.put(item, sources);
		}
	}

	private void registerTints() {
		tint(List.of(tint(ColorHandler.Kind.HUE_CYCLE)), lifeEssence, gaiaIngot);
		tint(List.of(none(), tint(ColorHandler.Kind.WAND_FIRST), tint(ColorHandler.Kind.WAND_SECOND)), twigWand, dreamwoodWand);
		tint(List.of(tint(ColorHandler.Kind.BLOCK)),
				BotaniaBlocks.petalBlockWhite.asItem(), BotaniaBlocks.petalBlockOrange.asItem(), BotaniaBlocks.petalBlockMagenta.asItem(), BotaniaBlocks.petalBlockLightBlue.asItem(),
				BotaniaBlocks.petalBlockYellow.asItem(), BotaniaBlocks.petalBlockLime.asItem(), BotaniaBlocks.petalBlockPink.asItem(), BotaniaBlocks.petalBlockGray.asItem(),
				BotaniaBlocks.petalBlockSilver.asItem(), BotaniaBlocks.petalBlockCyan.asItem(), BotaniaBlocks.petalBlockPurple.asItem(), BotaniaBlocks.petalBlockBlue.asItem(),
				BotaniaBlocks.petalBlockBrown.asItem(), BotaniaBlocks.petalBlockGreen.asItem(), BotaniaBlocks.petalBlockRed.asItem(), BotaniaBlocks.petalBlockBlack.asItem(),
				BotaniaBlocks.manaPool.asItem(), BotaniaBlocks.creativePool.asItem(), BotaniaBlocks.dilutedPool.asItem(), BotaniaBlocks.fabulousPool.asItem(),
				BotaniaBlocks.gaiaSpreader.asItem());
		tint(List.of(none(), tint(ColorHandler.Kind.MANA_FILL)), manaMirror, manaTablet);
		tint(List.of(tint(ColorHandler.Kind.SPELL_CLOTH)), spellCloth);
		tint(List.of(none(), tint(ColorHandler.Kind.BREW)), bloodPendant, incenseStick, brewFlask, brewVial);
		tint(List.of(tint(ColorHandler.Kind.BLASTER_LENS), none(), tint(ColorHandler.Kind.BLASTER_BURST)), manaGun);
		tint(List.of(none(), tint(ColorHandler.Kind.ENDER_DAGGER)), enderDagger);
		tint(List.of(none(), tint(ColorHandler.Kind.TERRA_PICK)), terraPick);
		BuiltInRegistries.ITEM.stream().filter(i -> i instanceof LensItem && LibMisc.MOD_ID.equals(BuiltInRegistries.ITEM.getKey(i).getNamespace()))
				.forEach(i -> tints.putIfAbsent(i, List.of(tint(ColorHandler.Kind.LENS))));
	}

	private List<ItemTintSource> tintsOf(Item item) {
		return tints.getOrDefault(item, List.of());
	}

	private ItemModel.Unbaked model(Identifier modelId, Item item) {
		return ItemDefinitions.model(modelId, tintsOf(item));
	}

	private ItemModel.Unbaked flag(BotaniaItemProperties.FlagKind kind, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
		return ItemModelUtils.conditional(new BotaniaItemProperties.Flag(kind), onTrue, onFalse);
	}

	private static Material texture(Identifier id) {
		return new Material(id);
	}

	private static final String[] ICONS = {
			"blood_pendant_chain", "blood_pendant_gem", "cloud_pendant_gem",
			"gate_weapon_0", "gate_weapon_1", "gate_weapon_2", "gate_weapon_3", "gate_weapon_4", "gate_weapon_5",
			"gate_weapon_6", "gate_weapon_7", "gate_weapon_8", "gate_weapon_9", "gate_weapon_10", "gate_weapon_11",
			"goldfish", "ice_pendant_gem", "itemfinder_gem", "lava_pendant_gem", "nerfbat", "phiflower",
			"super_cloud_pendant_gem", "super_lava_pendant_gem", "third_eye_0", "third_eye_1", "third_eye_2",
			"tiara_wing_1", "tiara_wing_2", "tiara_wing_3", "tiara_wing_4", "tiara_wing_5", "tiara_wing_6",
			"tiara_wing_7", "tiara_wing_8", "tiara_wing_9", "will_flame"
	};

	private void registerStandaloneModels() {
		for (String icon : ICONS) {
			ModelTemplates.FLAT_ITEM.create(prefix("icon/" + icon), TextureMapping.layer0(texture(prefix("item/" + icon))), models::put);
		}
		bow(crystalBow, new float[] { 0.35F, 0.65F, 0.8F, 0.9F });
		bow(livingwoodBow, new float[] { 0.65F, 0.9F });
	}

	private void bow(Item bow, float[] thresholds) {
		Identifier base = ModelLocationUtils.getModelLocation(bow);
		ModelTemplate pulling = new ModelTemplate(Optional.of(base), Optional.empty(), TextureSlot.LAYER0);
		int stages = thresholds.length + 1;
		List<Identifier> pullingModels = new ArrayList<>();
		for (int i = 1; i <= stages; i++) {
			pullingModels.add(pulling.create(ModelLocationUtils.getModelLocation(bow, "_pulling_" + i),
					TextureMapping.layer0(TextureMapping.getItemTexture(bow, "_pulling_" + i)), models::put));
		}
		List<RangeSelectItemModel.Entry> entries = new ArrayList<>();
		for (int i = 0; i < thresholds.length; i++) {
			entries.add(ItemModelUtils.override(ItemModelUtils.plainModel(pullingModels.get(i + 1)), thresholds[i]));
		}
		definitions.accept(bow, ItemModelUtils.conditional(ItemModelUtils.isUsingItem(),
				ItemModelUtils.rangeSelect(new BotaniaItemProperties.BowPull(), ItemModelUtils.plainModel(pullingModels.get(0)), entries),
				ItemModelUtils.plainModel(base)));
	}

	private void registerItems(Set<Item> items) {
		ItemModel.Unbaked gun = model(prefix("item/mana_gun_noclip"), manaGun);
		ItemModel.Unbaked gunClip = model(prefix("item/mana_gun_clip"), manaGun);
		definitions.accept(manaGun, new ManaBlasterItemModel.Unbaked(gun, gunClip));
		items.remove(manaGun);

		takeAll(items, i -> i instanceof LensItem).forEach(i -> {
			Identifier lens;
			if (i == lensTime || i == lensWarp || i == lensFire || i == lensTripwire) {
				lens = prefix("item/lens_small");
			} else {
				lens = prefix("item/lens");
			}
			GENERATED_1.create(ModelLocationUtils.getModelLocation(i),
					TextureMapping.layer0(texture(lens)).put(TextureSlot.LAYER1, TextureMapping.getItemTexture(i)), models::put);
		});

		for (Item i : new Item[] { bloodPendant, incenseStick, manaMirror, manaTablet }) {
			GENERATED_1.create(ModelLocationUtils.getModelLocation(i),
					TextureMapping.layer0(TextureMapping.getItemTexture(i))
							.put(TextureSlot.LAYER1, TextureMapping.getItemTexture(i, "_overlay")),
					models::put);
			items.remove(i);
		}

		HANDHELD_1.create(ModelLocationUtils.getModelLocation(enderDagger),
				TextureMapping.layer0(TextureMapping.getItemTexture(enderDagger))
						.put(TextureSlot.LAYER1, TextureMapping.getItemTexture(enderDagger, "_overlay")),
				models::put);
		items.remove(enderDagger);

		GENERATED_2.create(ModelLocationUtils.getModelLocation(thirdEye),
				new TextureMapping().put(TextureSlot.LAYER0, TextureMapping.getItemTexture(thirdEye, "_0"))
						.put(TextureSlot.LAYER1, TextureMapping.getItemTexture(thirdEye, "_1"))
						.put(TextureSlot.LAYER2, TextureMapping.getItemTexture(thirdEye, "_2")),
				models::put);
		items.remove(thirdEye);

		takeAll(items, cobbleRod, dirtRod, diviningRod, elementiumAxe, elementiumPick, elementiumShovel, elementiumHoe, elementiumSword,
				exchangeRod, fireRod, glassPick, gravityRod, manasteelAxe, manasteelPick, manasteelShears, manasteelShovel, manasteelHoe,
				missileRod, obedienceStick, rainbowRod, smeltRod, starSword, terraSword, terraformRod, thunderSword, waterRod,
				kingKey, skyDirtRod).forEach(i -> ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(i), models::put));

		takeAll(items, i -> true).forEach(i -> ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(i), models::put));
	}

	private void singleOverride(Item item, ModelTemplate template, Identifier overrideModel, BotaniaItemProperties.FlagKind kind) {
		template.create(overrideModel, TextureMapping.layer0(texture(overrideModel)), models::put);
		Identifier base = template.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item), models::put);
		definitions.accept(item, flag(kind, model(overrideModel, item), model(base, item)));
	}

	private void singleGeneratedOverride(Item item, Identifier overrideModel, BotaniaItemProperties.FlagKind kind) {
		singleOverride(item, ModelTemplates.FLAT_ITEM, overrideModel, kind);
	}

	private void singleGeneratedSuffixOverride(Item item, String suffix, BotaniaItemProperties.FlagKind kind) {
		singleGeneratedOverride(item, ModelLocationUtils.getModelLocation(item, suffix), kind);
	}

	private void singleHandheldOverride(Item item, Identifier overrideModel, BotaniaItemProperties.FlagKind kind) {
		singleOverride(item, ModelTemplates.FLAT_HANDHELD_ITEM, overrideModel, kind);
	}

	private void singleHandheldSuffixOverride(Item item, String suffix, BotaniaItemProperties.FlagKind kind) {
		singleHandheldOverride(item, ModelLocationUtils.getModelLocation(item, suffix), kind);
	}

	private void registerItemOverrides(Set<Item> items) {
		items.remove(livingwoodBow);
		items.remove(crystalBow);

		singleGeneratedSuffixOverride(baubleBox, "_open", BotaniaItemProperties.FlagKind.BAUBLE_BOX_OPEN);
		items.remove(baubleBox);

		singleGeneratedSuffixOverride(blackHoleTalisman, "_active", BotaniaItemProperties.FlagKind.BLACK_HOLE_ACTIVE);
		items.remove(blackHoleTalisman);

		registerBrewContainer((BaseBrewItem) brewFlask, flask);
		items.remove(brewFlask);

		registerBrewContainer((BaseBrewItem) brewVial, vial);
		items.remove(brewVial);

		singleHandheldOverride(elementiumShears, prefix("item/dammitreddit"), BotaniaItemProperties.FlagKind.REDDIT);
		items.remove(elementiumShears);

		Identifier vuvuzela = prefix("item/vuvuzela");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(vuvuzela, TextureMapping.layer0(texture(vuvuzela)), models::put);
		for (Item i : new Item[] { grassHorn, leavesHorn, snowHorn }) {
			Identifier base = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(i), models::put);
			definitions.accept(i, flag(BotaniaItemProperties.FlagKind.VUVUZELA, model(vuvuzela, i), model(base, i)));
			items.remove(i);
		}

		singleGeneratedOverride(infiniteFruit, prefix("item/dasboot"), BotaniaItemProperties.FlagKind.BOOT);
		items.remove(infiniteFruit);

		singleGeneratedSuffixOverride(lexicon, "_elven", BotaniaItemProperties.FlagKind.ELVEN);
		items.remove(lexicon);

		singleGeneratedSuffixOverride(magnetRing, "_active", BotaniaItemProperties.FlagKind.MAGNET_ACTIVE);
		items.remove(magnetRing);

		singleGeneratedSuffixOverride(magnetRingGreater, "_active", BotaniaItemProperties.FlagKind.MAGNET_ACTIVE);
		items.remove(magnetRingGreater);

		List<RangeSelectItemModel.Entry> bottleEntries = new ArrayList<>();
		for (int i = 1; i < BottledManaItem.SWIGS; i++) {
			Identifier overrideModel = ModelLocationUtils.getModelLocation(manaBottle, "_" + i);
			ModelTemplates.FLAT_ITEM.create(overrideModel, TextureMapping.layer0(texture(overrideModel)), models::put);
			bottleEntries.add(ItemModelUtils.override(model(overrideModel, manaBottle), (float) i / (BottledManaItem.SWIGS - 1)));
		}
		Identifier bottleBase = ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(manaBottle), TextureMapping.layer0(manaBottle), models::put);
		definitions.accept(manaBottle, ItemModelUtils.rangeSelect(new BotaniaItemProperties.SwigsTaken(), model(bottleBase, manaBottle), bottleEntries));
		items.remove(manaBottle);

		singleGeneratedOverride(manaCookie, prefix("item/totalbiscuit"), BotaniaItemProperties.FlagKind.TOTALBISCUIT);
		items.remove(manaCookie);

		singleHandheldOverride(manasteelSword, prefix("item/elucidator"), BotaniaItemProperties.FlagKind.ELUCIDATOR);
		items.remove(manasteelSword);

		for (Item i : new Item[] { manaweaveHelm, manaweaveChest, manaweaveLegs, manaweaveBoots }) {
			singleGeneratedSuffixOverride(i, "_holiday", BotaniaItemProperties.FlagKind.HOLIDAY);
			items.remove(i);
		}

		singleGeneratedSuffixOverride(slimeBottle, "_active", BotaniaItemProperties.FlagKind.SLIME_ACTIVE);
		items.remove(slimeBottle);

		singleGeneratedSuffixOverride(spawnerMover, "_full", BotaniaItemProperties.FlagKind.SPAWNER_FULL);
		items.remove(spawnerMover);

		singleGeneratedSuffixOverride(temperanceStone, "_active", BotaniaItemProperties.FlagKind.TEMPERANCE_ACTIVE);
		items.remove(temperanceStone);

		singleHandheldSuffixOverride(terraAxe, "_active", BotaniaItemProperties.FlagKind.TERRA_AXE_ACTIVE);
		items.remove(terraAxe);

		singleGeneratedSuffixOverride(autocraftingHalo, "_active", BotaniaItemProperties.FlagKind.AUTOCRAFTING_ACTIVE);
		items.remove(autocraftingHalo);

		Identifier enabledModel = ModelLocationUtils.getModelLocation(terraPick, "_active");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(enabledModel, TextureMapping.layer0(texture(enabledModel)), models::put);
		Identifier tippedModel = ModelLocationUtils.getModelLocation(terraPick, "_tipped");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(tippedModel, TextureMapping.layer0(texture(tippedModel)), models::put);
		Identifier tippedEnabledModel = ModelLocationUtils.getModelLocation(terraPick, "_tipped_active");
		ModelTemplates.FLAT_HANDHELD_ITEM.create(tippedEnabledModel, TextureMapping.layer0(texture(tippedEnabledModel)), models::put);
		Identifier terraPickBase = ModelTemplates.FLAT_HANDHELD_ITEM.create(ModelLocationUtils.getModelLocation(terraPick), TextureMapping.layer0(terraPick), models::put);
		definitions.accept(terraPick, flag(BotaniaItemProperties.FlagKind.TERRA_PICK_TIPPED,
				flag(BotaniaItemProperties.FlagKind.TERRA_PICK_ACTIVE, model(tippedEnabledModel, terraPick), model(tippedModel, terraPick)),
				flag(BotaniaItemProperties.FlagKind.TERRA_PICK_ACTIVE, model(enabledModel, terraPick), model(terraPickBase, terraPick))));
		items.remove(terraPick);

		singleHandheldSuffixOverride(tornadoRod, "_active", BotaniaItemProperties.FlagKind.TORNADO_ACTIVE);
		items.remove(tornadoRod);

		wand(twigWand);
		items.remove(twigWand);

		wand(dreamwoodWand);
		items.remove(dreamwoodWand);
	}

	private void wand(Item wand) {
		TextureMapping textures = TextureMapping.layer0(wand)
				.put(TextureSlot.LAYER1, TextureMapping.getItemTexture(wand, "_top"))
				.put(TextureSlot.LAYER2, TextureMapping.getItemTexture(wand, "_bottom"));
		Identifier bind = HANDHELD_3.create(ModelLocationUtils.getModelLocation(wand, "_bind"),
				textures.copyAndUpdate(LAYER3, TextureMapping.getItemTexture(wand, "_bind")), models::put);
		Identifier base = HANDHELD_2.create(ModelLocationUtils.getModelLocation(wand), textures, models::put);
		definitions.accept(wand, flag(BotaniaItemProperties.FlagKind.BINDMODE, model(bind, wand), model(base, wand)));
	}

	private void registerBrewContainer(BaseBrewItem brewItem, Item emptyItem) {
		List<RangeSelectItemModel.Entry> entries = new ArrayList<>();
		for (int i = 1; i < brewItem.getSwigs(); i++) {
			Identifier overrideModel = ModelLocationUtils.getModelLocation(brewItem, "_" + i);
			GENERATED_1.create(overrideModel,
					TextureMapping.layer0(emptyItem).put(TextureSlot.LAYER1, texture(overrideModel)),
					models::put);
			entries.add(ItemModelUtils.override(model(overrideModel, brewItem), (float) i / (brewItem.getSwigs() - 1)));
		}
		Identifier base = GENERATED_1.create(ModelLocationUtils.getModelLocation(brewItem),
				TextureMapping.layer0(emptyItem).put(TextureSlot.LAYER1, TextureMapping.getItemTexture(brewItem, "_0")),
				models::put);
		definitions.accept(brewItem, ItemModelUtils.rangeSelect(new BotaniaItemProperties.SwigsTaken(), model(base, brewItem), entries));
	}

	private void registerItemBlocks(Set<BlockItem> itemBlocks) {
		Item crystalCube = BotaniaBlocks.corporeaCrystalCube.asItem();
		definitions.accept(crystalCube, ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(crystalCube)));
		itemBlocks.remove(crystalCube);

		takeAll(itemBlocks, i -> {
			var id = BuiltInRegistries.BLOCK.getKey(i.getBlock());
			return id.getNamespace().equals(LibMisc.MOD_ID) && i.getBlock() instanceof FloatingFlowerBlock;
		}).forEach(i -> definitions.accept(i, ItemModelUtils.composite(
				ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(i.getBlock())),
				ItemModelUtils.plainModel(prefix("block/islands/island_grass")))));

		GENERATED_1.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.animatedTorch.asItem()),
				TextureMapping.layer0(Blocks.REDSTONE_TORCH).put(TextureSlot.LAYER1, texture(prefix("block/animated_torch_glimmer"))), models::put);
		itemBlocks.remove(BotaniaBlocks.animatedTorch.asItem());

		Item gaiaHead = BotaniaBlocks.gaiaHead.asItem();
		definitions.accept(gaiaHead, ItemModelUtils.specialModel(ModelLocationUtils.decorateItemModelLocation("template_skull"),
				BlockModelGenerators.SKULL_TRANSFORM, new SkullSpecialRenderer.Unbaked(GaiaHeadBlock.GAIA_TYPE)));
		itemBlocks.remove(gaiaHead);

		takeAll(itemBlocks, i -> i.getBlock() instanceof BotaniaDoubleFlowerBlock).forEach(i -> {
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(TextureMapping.getBlockTexture(i.getBlock(), "_top")), models::put);
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof PetalBlock).forEach(i -> {
			models.put(ModelLocationUtils.getModelLocation(i), new DelegatedModel(prefix("block/petal_block")));
		});

		models.put(ModelLocationUtils.getModelLocation(BotaniaBlocks.livingwoodFramed.asItem()), new DelegatedModel(prefix("block/framed_livingwood_horizontal_z")));
		models.put(ModelLocationUtils.getModelLocation(BotaniaBlocks.dreamwoodFramed.asItem()), new DelegatedModel(prefix("block/framed_dreamwood_horizontal_z")));
		itemBlocks.remove(BotaniaBlocks.livingwoodFramed.asItem());
		itemBlocks.remove(BotaniaBlocks.dreamwoodFramed.asItem());

		takeAll(itemBlocks, i -> i.getBlock() instanceof IronBarsBlock).forEach(i -> {
			String name = BuiltInRegistries.ITEM.getKey(i).getPath();
			String baseName = name.substring(0, name.length() - "_pane".length());
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(texture(prefix("block/" + baseName))), models::put);
		});

		Predicate<BlockItem> defaultGenerated = i -> {
			Block b = i.getBlock();
			return XplatAbstractions.INSTANCE.isSpecialFlowerBlock(b)
					|| b instanceof BotaniaMushroomBlock
					|| b instanceof LuminizerBlock
					|| b instanceof BotaniaFlowerBlock
					|| b == BotaniaBlocks.ghostRail;
		};
		takeAll(itemBlocks, defaultGenerated).forEach(i -> {
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(i.getBlock()), models::put);
		});

		takeAll(itemBlocks, b -> b.getBlock() instanceof FlowerMotifBlock).forEach(i -> {
			String name = BuiltInRegistries.ITEM.getKey(i).getPath();
			Identifier texName = prefix("block/" + name.replace("_motif", ""));
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(texture(texName)), models::put);
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof ManaPoolBlock).forEach(i -> {
			Identifier fullModel = ModelLocationUtils.getModelLocation(i.getBlock(), "_full");
			Identifier emptyModel = ModelLocationUtils.getModelLocation(i.getBlock());
			definitions.accept(i, flag(BotaniaItemProperties.FlagKind.POOL_FULL, model(fullModel, i), model(emptyModel, i)));
		});

		takeAll(itemBlocks, Stream.of(BotaniaBlocks.livingwoodWall, BotaniaBlocks.livingwoodStrippedWall,
				BotaniaBlocks.dreamwoodWall, BotaniaBlocks.dreamwoodStrippedWall)
				.map(b -> (BlockItem) b.asItem())
				.toArray(BlockItem[]::new)).forEach(i -> {
					String name = BuiltInRegistries.ITEM.getKey(i).getPath();
					String baseName = name.substring(0, name.length() - "_wall".length()) + "_log";
					ModelTemplates.WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(i),
							new TextureMapping().put(TextureSlot.WALL, texture(prefix("block/" + baseName))), models::put);
				});

		WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.biomeBrickPlainsWall.asItem()),
				new TextureMapping()
						.put(TextureSlot.TOP, TextureMapping.getBlockTexture(BotaniaBlocks.biomeBrickPlains, "_top"))
						.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(BotaniaBlocks.biomeBrickPlains, "_top"))
						.put(TextureSlot.WALL, TextureMapping.getBlockTexture(BotaniaBlocks.biomeBrickPlains)),
				models::put);
		itemBlocks.remove(BotaniaBlocks.biomeBrickPlainsWall.asItem());

		WALL_INVENTORY_CHECKERED.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.biomeBrickMesaWall.asItem()),
				new TextureMapping()
						.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(BotaniaBlocks.biomeBrickMesa))
						.put(TextureSlot.NORTH, TextureMapping.getBlockTexture(BotaniaBlocks.biomeBrickMesa, "_mirrored")),
				models::put);
		itemBlocks.remove(BotaniaBlocks.biomeBrickMesaWall.asItem());

		takeAll(itemBlocks, i -> i.getBlock() instanceof WallBlock).forEach(i -> {
			String name = BuiltInRegistries.ITEM.getKey(i).getPath();
			String baseName = name.substring(0, name.length() - "_wall".length());
			ModelTemplates.WALL_INVENTORY.create(ModelLocationUtils.getModelLocation(i),
					new TextureMapping().put(TextureSlot.WALL, texture(prefix("block/" + baseName))), models::put);
		});

		takeAll(itemBlocks, i -> i.getBlock() instanceof ManaSpreaderBlock).forEach(i -> {
			String name = BuiltInRegistries.ITEM.getKey(i).getPath();
			String outside;
			if (i.getBlock() == BotaniaBlocks.elvenSpreader) {
				outside = "dreamwood_log_3";
			} else if (i.getBlock() == BotaniaBlocks.gaiaSpreader || i.getBlock() == BotaniaBlocks.elementiumSpreader || i.getBlock() == BotaniaBlocks.terrasteelSpreader) {
				outside = name + "_outside";
			} else {
				outside = "livingwood_log";
			}
			String inside;
			if (i.getBlock() == BotaniaBlocks.elvenSpreader) {
				inside = "stripped_dreamwood_log_3";
			} else if (i.getBlock() == BotaniaBlocks.gaiaSpreader || i.getBlock() == BotaniaBlocks.elementiumSpreader || i.getBlock() == BotaniaBlocks.terrasteelSpreader) {
				inside = name + "_inside";
			} else {
				inside = "stripped_livingwood_log";
			}
			SPREADER.create(ModelLocationUtils.getModelLocation(i),
					new TextureMapping()
							.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(i.getBlock(), "_side"))
							.put(OUTSIDE, texture(prefix("block/" + outside)))
							.put(TextureSlot.BACK, TextureMapping.getBlockTexture(i.getBlock(), "_back"))
							.put(TextureSlot.INSIDE, texture(prefix("block/" + inside)))
							.put(CORE, TextureMapping.getBlockTexture(i.getBlock(), "_core")),
					models::put);
		});

		takeAll(itemBlocks, BotaniaBlocks.avatar.asItem(), BotaniaBlocks.bellows.asItem(),
				BotaniaBlocks.brewery.asItem(), BotaniaBlocks.corporeaIndex.asItem(), BotaniaBlocks.gaiaPylon.asItem(),
				BotaniaBlocks.hourglass.asItem(), BotaniaBlocks.manaPylon.asItem(), BotaniaBlocks.naturaPylon.asItem())
				.forEach(i -> builtinEntity((BlockItem) i, 0.0));

		takeAll(itemBlocks, BotaniaBlocks.teruTeruBozu.asItem())
				.forEach(i -> builtinEntity((BlockItem) i, 2.5));

		takeAll(itemBlocks, i -> i instanceof MysticalPetalItem).forEach(i -> {
			ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(i), TextureMapping.layer0(TextureMapping.getItemTexture(i)), models::put);
		});

		ModelTemplates.FENCE_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.dreamwoodFence.asItem()),
				TextureMapping.defaultTexture(BotaniaBlocks.dreamwoodPlanks), models::put);
		itemBlocks.remove(BotaniaBlocks.dreamwoodFence.asItem());

		ModelTemplates.FENCE_INVENTORY.create(ModelLocationUtils.getModelLocation(BotaniaBlocks.livingwoodFence.asItem()),
				TextureMapping.defaultTexture(BotaniaBlocks.livingwoodPlanks), models::put);
		itemBlocks.remove(BotaniaBlocks.livingwoodFence.asItem());

		models.put(ModelLocationUtils.getModelLocation(BotaniaBlocks.elfGlass.asItem()), new DelegatedModel(prefix("block/elf_glass_0")));
		itemBlocks.remove(BotaniaBlocks.elfGlass.asItem());

		itemBlocks.forEach(i -> models.put(ModelLocationUtils.getModelLocation(i), new DelegatedModel(ModelLocationUtils.getModelLocation(i.getBlock()))));
	}

	private static final String BUILTIN_ENTITY_DISPLAY_STR =
			"""
					{
						"gui": {
							"rotation": [30, 45, 0],
							"translation": [0, 0, 0],
							"scale": [0.625, 0.625, 0.625]
						},
						"ground": {
							"rotation": [0, 0, 0],
							"translation": [0, 3, 0],
							"scale": [0.25, 0.25, 0.25]
						},
						"head": {
							"rotation": [0, 180, 0],
							"translation": [0, 0, 0],
							"scale": [1, 1, 1]
						},
						"fixed": {
							"rotation": [0, 180, 0],
							"translation": [0, 0, 0],
							"scale": [0.5, 0.5, 0.5]
						},
						"thirdperson_righthand": {
							"rotation": [75, 315, 0],
							"translation": [0, 2.5, 0],
							"scale": [0.375, 0.375, 0.375]
						},
						"firstperson_righthand": {
							"rotation": [0, 315, 0],
							"translation": [0, 0, 0],
							"scale": [0.4, 0.4, 0.4]
						}
					}""";
	private static final JsonElement BUILTIN_ENTITY_DISPLAY = new Gson().fromJson(BUILTIN_ENTITY_DISPLAY_STR, JsonElement.class);

	private void builtinEntity(BlockItem i, double handYOffset) {
		final JsonElement display;
		if (handYOffset == 0.0) {
			display = BUILTIN_ENTITY_DISPLAY;
		} else {
			display = BUILTIN_ENTITY_DISPLAY.deepCopy();
			display.getAsJsonObject().getAsJsonObject("firstperson_righthand")
					.getAsJsonArray("translation").set(1, new JsonPrimitive(handYOffset));
		}
		Identifier base = ModelLocationUtils.getModelLocation(i);
		Identifier parent = ModelLocationUtils.getModelLocation(i.getBlock());
		models.put(base, () -> {
			JsonObject json = new JsonObject();
			json.addProperty("parent", parent.toString());
			json.add("display", display);
			return json;
		});
		definitions.accept(i, ItemModelUtils.specialModel(base, new BlockEntityItemRendererHelper.Unbaked(i.getBlock())));
	}

	@SafeVarargs
	@SuppressWarnings("varargs")
	public static <T> Collection<T> takeAll(Set<? extends T> src, T... items) {
		List<T> ret = Arrays.asList(items);
		for (T item : items) {
			if (!src.contains(item)) {
				BotaniaAPI.LOGGER.warn("Item {} not found in set", item);
			}
		}
		if (!src.removeAll(ret)) {
			BotaniaAPI.LOGGER.warn("takeAll array didn't yield anything ({})", Arrays.toString(items));
		}
		return ret;
	}

	public static <T> Collection<T> takeAll(Set<T> src, Predicate<T> pred) {
		List<T> ret = new ArrayList<>();

		Iterator<T> iter = src.iterator();
		while (iter.hasNext()) {
			T item = iter.next();
			if (pred.test(item)) {
				iter.remove();
				ret.add(item);
			}
		}

		if (ret.isEmpty()) {
			BotaniaAPI.LOGGER.warn("takeAll predicate yielded nothing", new Throwable());
		}
		return ret;
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania item models";
	}
}
