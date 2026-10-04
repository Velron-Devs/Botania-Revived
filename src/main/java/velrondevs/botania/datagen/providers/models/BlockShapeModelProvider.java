package velrondevs.botania.datagen.providers.models;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import velrondevs.botania.common.lib.LibMisc;

public class BlockShapeModelProvider extends BlockModelProvider {
	public BlockShapeModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		blockCocoon();
		blockCorporeaCrystalCube();
		blockCorporeaCrystalCubeGlass();
		blockIncensePlate();
		blockIslandsIslandDry();
		blockIslandsIslandGolden();
		blockIslandsIslandGrass();
		blockIslandsIslandInfused();
		blockIslandsIslandMutated();
		blockIslandsIslandMycel();
		blockIslandsIslandPodzol();
		blockIslandsIslandScorched();
		blockIslandsIslandSnow();
		blockIslandsIslandVivid();
		blockManaDistributor();
		blockPrism();
		blockPump();
		blockPumpHead();
		blockRunicAltar();
		blockShapesCrate();
		blockShapesCreativeManaPool();
		blockShapesCreativeManaPoolFull();
		blockShapesCross();
		blockShapesCubeAllTinted();
		blockShapesCubeCheckered();
		blockShapesCubeColumnDirectional();
		blockShapesCubeColumnDirectionalHorizontal();
		blockShapesCubeColumnHorizontalX();
		blockShapesCubeColumnHorizontalZ();
		blockShapesDilutedManaPool();
		blockShapesDilutedManaPoolFull();
		blockShapesDrum();
		blockShapesEightbyeight();
		blockShapesFifteenHighAll();
		blockShapesFourHighBottomTop();
		blockShapesManaPool();
		blockShapesManaPoolFull();
		blockShapesMiniisland();
		blockShapesPetalApothecary();
		blockShapesSlabCheckered();
		blockShapesSlabTopCheckered();
		blockShapesSpreader();
		blockShapesSpreaderCore();
		blockShapesSpreaderItem();
		blockShapesSpreaderPadding();
		blockShapesSpreaderScaffolding();
		blockShapesStairsCheckered();
		blockShapesStairsCheckered90deg();
		blockShapesStairsInnerCheckered();
		blockShapesStairsInnerCheckered90deg();
		blockShapesStairsOuterCheckered();
		blockShapesStairsOuterCheckered90deg();
		blockShapesTenbytenAll();
		blockShapesThreeHighBottomTop();
		blockShapesWallInventory();
		blockShapesWallInventoryCheckered();
		blockShapesWallPost();
		blockShapesWallPostCheckered();
		blockShapesWallSide();
		blockShapesWallSideCheckered();
		blockShapesWallSideCheckered90deg();
		blockShapesWallSideTall();
		blockShapesWallSideTallCheckered();
		blockShapesWallSideTallCheckered90deg();
		blockSpawnerClaw();
		tinyPotatoAcetater();
		tinyPotatoAgendertater();
		tinyPotatoAromantictater();
		tinyPotatoArotater();
		tinyPotatoAsexualtater();
		tinyPotatoAureylian();
		tinyPotatoBase();
		tinyPotatoBitater();
		tinyPotatoBiter();
		tinyPotatoBosniaHerzegovina();
		tinyPotatoBosniaherzegovina();
		tinyPotatoBosniantater();
		tinyPotatoBotaniaHerzegovina();
		tinyPotatoBotaniaherzegovina();
		tinyPotatoBotaniatater();
		tinyPotatoBotater();
		tinyPotatoDefault();
		tinyPotatoDinnerbone();
		tinyPotatoEggtater();
		tinyPotatoEnbytater();
		tinyPotatoEutrotater();
		tinyPotatoGaytater();
		tinyPotatoGayter();
		tinyPotatoGenderfluidtater();
		tinyPotatoGirlstater();
		tinyPotatoGrumm();
		tinyPotatoHalloween();
		tinyPotatoKingdaddydmac();
		tinyPotatoKyleHyde();
		tinyPotatoLesbiabtater();
		tinyPotatoLesbiamtater();
		tinyPotatoLesbiantater();
		tinyPotatoLesbitater();
		tinyPotatoLessbientater();
		tinyPotatoLgbtater();
		tinyPotatoManytater();
		tinyPotatoNbtater();
		tinyPotatoNonbinarytater();
		tinyPotatoPahimar();
		tinyPotatoPantater();
		tinyPotatoPanter();
		tinyPotatoPluraltater();
		tinyPotatoPridetater();
		tinyPotatoSnorps();
		tinyPotatoSpooktater();
		tinyPotatoSpooky();
		tinyPotatoSystater();
		tinyPotatoSystemtater();
		tinyPotatoTacer();
		tinyPotatoTaceter();
		tinyPotatoTataro();
		tinyPotatoTategg();
		tinyPotatoTaterfluid();
		tinyPotatoTomater();
		tinyPotatoTranstater();
		tinyPotatoWiretater();
	}

	private BlockModelBuilder model(String path, String parent, String... textures) {
		BlockModelBuilder builder = getBuilder(path);
		if (parent != null) {
			builder.parent(new ModelFile.UncheckedModelFile(parent));
		}
		for (int i = 0; i < textures.length; i += 2) {
			builder.texture(textures[i], textures[i + 1]);
		}
		return builder;
	}

	private void blockCocoon() {
		BlockModelBuilder b = model("block/cocoon", "minecraft:block/block", "bottom", "botania:block/cocoon_bottom", "top", "botania:block/cocoon_top", "north", "botania:block/cocoon_north", "south", "botania:block/cocoon_south", "west", "botania:block/cocoon_west", "east", "botania:block/cocoon_east", "particle", "botania:block/cocoon_east");
		b.element().from(3, 0, 3).to(13, 14, 13)
				.face(Direction.WEST).texture("#west").uvs(3, 1, 13, 15).end()
				.face(Direction.NORTH).texture("#north").uvs(3, 1, 13, 15).end()
				.face(Direction.SOUTH).texture("#south").uvs(3, 1, 13, 15).end()
				.face(Direction.EAST).texture("#east").uvs(3, 1, 13, 15).end()
				.face(Direction.UP).texture("#top").uvs(3, 3, 13, 13).end()
				.face(Direction.DOWN).texture("#bottom").uvs(3, 3, 13, 13).end()
				.end();
	}

	private void blockCorporeaCrystalCube() {
		BlockModelBuilder b = model("block/corporea_crystal_cube", null, "cloth", "botania:block/corporea_crystal_cube_cloth", "base", "botania:block/corporea_crystal_cube_base", "particle", "botania:block/corporea_crystal_cube_cloth");
		b.element().from(5, 0, 5).to(11, 2, 11)
				.face(Direction.DOWN).texture("#base").uvs(10, 0, 16, 6).end()
				.face(Direction.NORTH).texture("#base").uvs(10, 6, 16, 8).end()
				.face(Direction.SOUTH).texture("#base").uvs(10, 6, 16, 8).end()
				.face(Direction.WEST).texture("#base").uvs(10, 8, 16, 10).end()
				.face(Direction.EAST).texture("#base").uvs(10, 8, 16, 10).end()
				.end();
		b.element().from(3, 2, 3).to(13, 2.001F, 13)
				.face(Direction.DOWN).texture("#base").uvs(0, 0, 10, 10).end()
				.end();
		b.element().from(3, 6, 3).to(13, 6.001F, 13)
				.face(Direction.UP).texture("#cloth").uvs(0, 0, 10, 10).end()
				.end();
		b.element().from(3, 1, 3).to(3.001F, 6, 5)
				.face(Direction.WEST).texture("#cloth").uvs(0, 10, 2, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(9, 10, 11, 15).end()
				.end();
		b.element().from(3, 2, 5).to(3.001F, 6, 7)
				.face(Direction.WEST).texture("#cloth").uvs(2, 11, 4, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(7, 11, 9, 15).end()
				.end();
		b.element().from(3, 1, 7).to(3.001F, 6, 9)
				.face(Direction.WEST).texture("#cloth").uvs(4, 10, 6, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(7, 10, 9, 15).end()
				.end();
		b.element().from(3, 2, 9).to(3.001F, 6, 11)
				.face(Direction.WEST).texture("#cloth").uvs(6, 11, 8, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(5, 11, 7, 15).end()
				.end();
		b.element().from(3, 1, 11).to(3.001F, 6, 13)
				.face(Direction.WEST).texture("#cloth").uvs(4, 10, 6, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(7, 10, 9, 15).end()
				.end();
		b.element().from(13, 1, 11).to(13.001F, 6, 13)
				.face(Direction.WEST).texture("#cloth").uvs(1, 10, 3, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(10, 10, 12, 15).end()
				.end();
		b.element().from(13, 2, 9).to(13.001F, 6, 11)
				.face(Direction.WEST).texture("#cloth").uvs(2, 11, 4, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(8, 11, 10, 15).end()
				.end();
		b.element().from(13, 1, 7).to(13.001F, 6, 9)
				.face(Direction.WEST).texture("#cloth").uvs(4, 10, 6, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(6, 10, 8, 15).end()
				.end();
		b.element().from(13, 2, 5).to(13.001F, 6, 7)
				.face(Direction.WEST).texture("#cloth").uvs(6, 11, 8, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(4, 11, 6, 15).end()
				.end();
		b.element().from(13, 1, 3).to(13.001F, 6, 5)
				.face(Direction.WEST).texture("#cloth").uvs(4, 10, 6, 15).end()
				.face(Direction.EAST).texture("#cloth").uvs(2, 10, 4, 15).end()
				.end();
		b.element().from(3, 1, 13).to(5, 6, 13.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(1, 10, 3, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(10, 10, 12, 15).end()
				.end();
		b.element().from(5, 2, 13).to(7, 6, 13.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(3, 11, 5, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(8, 11, 10, 15).end()
				.end();
		b.element().from(7, 1, 13).to(9, 6, 13.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(5, 10, 7, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(6, 10, 8, 15).end()
				.end();
		b.element().from(9, 2, 13).to(11, 6, 13.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(7, 11, 9, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(4, 11, 6, 15).end()
				.end();
		b.element().from(11, 1, 13).to(13, 6, 13.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(9, 10, 11, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(2, 10, 4, 15).end()
				.end();
		b.element().from(11, 1, 3).to(13, 6, 3.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(1, 10, 3, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(10, 10, 12, 15).end()
				.end();
		b.element().from(9, 2, 3).to(11, 6, 3.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(3, 11, 5, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(8, 11, 10, 15).end()
				.end();
		b.element().from(7, 1, 3).to(9, 6, 3.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(5, 10, 7, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(6, 10, 8, 15).end()
				.end();
		b.element().from(5, 2, 3).to(7, 6, 3.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(7, 11, 9, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(4, 11, 6, 15).end()
				.end();
		b.element().from(3, 1, 3).to(5, 6, 3.001F)
				.face(Direction.NORTH).texture("#cloth").uvs(9, 10, 11, 15).end()
				.face(Direction.SOUTH).texture("#cloth").uvs(2, 10, 4, 15).end()
				.end();
	}

	private void blockCorporeaCrystalCubeGlass() {
		BlockModelBuilder b = model("block/corporea_crystal_cube_glass", null, "glass", "botania:block/corporea_crystal_cube_glass", "particle", "botania:block/corporea_crystal_cube_glass");
		b.element().from(4, 4, 4).to(12, 12, 12)
				.face(Direction.DOWN).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.UP).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.NORTH).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.SOUTH).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.WEST).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.EAST).texture("#glass").uvs(4, 4, 12, 12).end()
				.end();
	}

	private void blockIncensePlate() {
		BlockModelBuilder b = model("block/incense_plate", "minecraft:block/block", "all", "botania:block/incense_plate", "particle", "botania:block/livingwood_log");
		b.element().from(2, 0, 6).to(14, 1, 10)
				.face(Direction.WEST).texture("#all").end()
				.face(Direction.NORTH).texture("#all").end()
				.face(Direction.SOUTH).texture("#all").end()
				.face(Direction.EAST).texture("#all").end()
				.face(Direction.UP).texture("#all").end()
				.face(Direction.DOWN).texture("#all").cullface(Direction.DOWN).end()
				.end();
		b.element().from(2, 1, 6).to(5, 2, 10)
				.face(Direction.WEST).texture("#all").end()
				.face(Direction.NORTH).texture("#all").end()
				.face(Direction.SOUTH).texture("#all").end()
				.face(Direction.EAST).texture("#all").end()
				.face(Direction.UP).texture("#all").end()
				.face(Direction.DOWN).texture("#all").end()
				.end();
	}

	private void blockIslandsIslandDry() {
		model("block/islands/island_dry", "botania:block/shapes/miniisland", "top", "botania:block/dry_grass_top", "side", "botania:block/dry_grass_side");
	}

	private void blockIslandsIslandGolden() {
		model("block/islands/island_golden", "botania:block/shapes/miniisland", "top", "botania:block/golden_grass_top", "side", "botania:block/golden_grass_side");
	}

	private void blockIslandsIslandGrass() {
		model("block/islands/island_grass", "botania:block/shapes/miniisland", "top", "botania:block/island_top", "side", "botania:block/island_side");
	}

	private void blockIslandsIslandInfused() {
		model("block/islands/island_infused", "botania:block/shapes/miniisland", "top", "botania:block/infused_grass_top", "side", "botania:block/infused_grass_side");
	}

	private void blockIslandsIslandMutated() {
		model("block/islands/island_mutated", "botania:block/shapes/miniisland", "top", "botania:block/mutated_grass_top", "side", "botania:block/mutated_grass_side");
	}

	private void blockIslandsIslandMycel() {
		model("block/islands/island_mycel", "botania:block/shapes/miniisland", "top", "minecraft:block/mycelium_top", "side", "minecraft:block/mycelium_side");
	}

	private void blockIslandsIslandPodzol() {
		model("block/islands/island_podzol", "botania:block/shapes/miniisland", "top", "minecraft:block/podzol_top", "side", "minecraft:block/podzol_side");
	}

	private void blockIslandsIslandScorched() {
		model("block/islands/island_scorched", "botania:block/shapes/miniisland", "top", "botania:block/scorched_grass_top", "side", "botania:block/scorched_grass_side");
	}

	private void blockIslandsIslandSnow() {
		model("block/islands/island_snow", "botania:block/shapes/miniisland", "top", "minecraft:block/snow", "side", "minecraft:block/grass_block_snow");
	}

	private void blockIslandsIslandVivid() {
		model("block/islands/island_vivid", "botania:block/shapes/miniisland", "top", "botania:block/vivid_grass_top", "side", "botania:block/vivid_grass_side");
	}

	private void blockManaDistributor() {
		BlockModelBuilder b = model("block/mana_distributor", "minecraft:block/block", "texture", "botania:block/mana_distributor", "particle", "botania:block/mana_distributor");
		b.element().from(4, 0, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#texture").uvs(8, 0, 16, 8).end()
				.face(Direction.UP).texture("#texture").uvs(8, 0, 16, 8).end()
				.face(Direction.NORTH).texture("#texture").uvs(0, 0, 8, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(0, 0, 8, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(0, 0, 8, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(0, 0, 8, 16).end()
				.end();
		b.element().from(0, 0, 4).to(4, 8, 12)
				.face(Direction.DOWN).texture("#texture").uvs(16, 8, 8, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.UP).texture("#texture").uvs(8, 12, 16, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.NORTH).texture("#texture").uvs(8, 12, 16, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.SOUTH).texture("#texture").uvs(8, 8, 16, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).texture("#texture").uvs(0, 8, 8, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(0, 8, 8, 16).end()
				.end();
		b.element().from(12, 0, 4).to(16, 8, 12)
				.face(Direction.DOWN).texture("#texture").uvs(8, 12, 16, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.UP).texture("#texture").uvs(16, 16, 8, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.NORTH).texture("#texture").uvs(8, 8, 16, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.SOUTH).texture("#texture").uvs(8, 12, 16, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).texture("#texture").uvs(0, 8, 8, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(0, 8, 8, 16).end()
				.end();
		b.element().from(4, 0, 12).to(12, 8, 16)
				.face(Direction.DOWN).texture("#texture").uvs(8, 12, 16, 8).end()
				.face(Direction.UP).texture("#texture").uvs(8, 12, 16, 16).end()
				.face(Direction.NORTH).texture("#texture").uvs(0, 8, 8, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(0, 8, 8, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(8, 12, 16, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#texture").uvs(8, 8, 16, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(4, 0, 0).to(12, 8, 4)
				.face(Direction.DOWN).texture("#texture").uvs(16, 8, 8, 12).end()
				.face(Direction.UP).texture("#texture").uvs(8, 12, 16, 16).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
				.face(Direction.NORTH).texture("#texture").uvs(0, 8, 8, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(0, 8, 8, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(8, 8, 16, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#texture").uvs(8, 12, 16, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
	}

	private void blockPrism() {
		BlockModelBuilder b = model("block/prism", "minecraft:block/block", "bottom", "botania:block/prism_top_bottom", "top", "botania:block/prism_top_bottom", "side", "botania:block/prism_side", "particle", "botania:block/prism_side");
		b.element().from(4, 0, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#bottom").uvs(4, 4, 12, 12).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").uvs(4, 4, 12, 12).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").uvs(4, 0, 12, 16).end()
				.face(Direction.SOUTH).texture("#side").uvs(4, 0, 12, 16).end()
				.face(Direction.WEST).texture("#side").uvs(4, 0, 12, 16).end()
				.face(Direction.EAST).texture("#side").uvs(4, 0, 12, 16).end()
				.end();
	}

	private void blockPump() {
		BlockModelBuilder b = model("block/pump", "minecraft:block/block", "structure", "botania:block/pump_structure", "head_sheet", "botania:block/pump_head_sheet", "particle", "botania:block/livingrock");
		b.element().from(4, 0, 14).to(12, 8, 16)
				.face(Direction.DOWN).texture("#structure").uvs(8, 2, 16, 4).end()
				.face(Direction.UP).texture("#structure").uvs(8, 2, 16, 4).end()
				.face(Direction.NORTH).texture("#structure").uvs(0, 8, 8, 16).end()
				.face(Direction.SOUTH).texture("#structure").uvs(0, 0, 8, 8).end()
				.face(Direction.WEST).texture("#structure").uvs(8, 2, 16, 0).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#structure").uvs(8, 0, 16, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(4, 0, 0).to(12, 8, 2)
				.face(Direction.DOWN).texture("#structure").uvs(8, 2, 16, 4).end()
				.face(Direction.UP).texture("#structure").uvs(8, 2, 16, 4).end()
				.face(Direction.NORTH).texture("#structure").uvs(0, 0, 8, 8).end()
				.face(Direction.SOUTH).texture("#structure").uvs(0, 8, 8, 16).end()
				.face(Direction.WEST).texture("#structure").uvs(8, 4, 16, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#structure").uvs(8, 2, 16, 4).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(6, 2, 2).to(10, 6, 10)
				.face(Direction.DOWN).texture("#structure").uvs(12, 8, 16, 16).end()
				.face(Direction.UP).texture("#structure").uvs(12, 8, 16, 16).end()
				.face(Direction.NORTH).texture("#structure").uvs(8, 12, 12, 16).end()
				.face(Direction.SOUTH).texture("#structure").uvs(8, 8, 12, 12).end()
				.face(Direction.WEST).texture("#structure").uvs(8, 4, 16, 8).end()
				.face(Direction.EAST).texture("#structure").uvs(8, 4, 16, 8).end()
				.end();
		b.element().from(4, 0, 10).to(12, 8, 14)
				.face(Direction.DOWN).texture("#head_sheet").uvs(8, 4, 16, 8).end()
				.face(Direction.UP).texture("#head_sheet").uvs(8, 0, 16, 4).end()
				.face(Direction.NORTH).texture("#head_sheet").uvs(0, 0, 8, 8).end()
				.face(Direction.SOUTH).texture("#head_sheet").uvs(0, 0, 8, 8).end()
				.face(Direction.WEST).texture("#head_sheet").uvs(8, 8, 16, 4).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#head_sheet").uvs(8, 4, 16, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
	}

	private void blockPumpHead() {
		BlockModelBuilder b = model("block/pump_head", "minecraft:block/block", "head_sheet", "botania:block/pump_head_sheet", "particle", "botania:block/livingrock");
		b.element().from(5, 1, 2).to(11, 7, 6)
				.face(Direction.DOWN).texture("#head_sheet").uvs(6, 8, 12, 12).end()
				.face(Direction.UP).texture("#head_sheet").uvs(6, 8, 12, 12).end()
				.face(Direction.NORTH).texture("#head_sheet").uvs(0, 8, 6, 14).end()
				.face(Direction.SOUTH).texture("#head_sheet").uvs(0, 8, 6, 14).end()
				.face(Direction.WEST).texture("#head_sheet").uvs(6, 12, 12, 8).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#head_sheet").uvs(6, 8, 12, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
	}

	private void blockRunicAltar() {
		BlockModelBuilder b = model("block/runic_altar", "minecraft:block/block", "bottom", "botania:block/runic_altar_bottom", "up", "botania:block/runic_altar_top", "side", "botania:block/runic_altar_side", "particle", "botania:block/livingrock");
		b.element().from(0, 6, 0).to(16, 12, 16)
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 16, 16).end()
				.face(Direction.UP).texture("#up").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 4, 16, 10).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 4, 16, 10).end()
				.face(Direction.WEST).texture("#side").uvs(0, 4, 16, 10).end()
				.face(Direction.EAST).texture("#side").uvs(0, 4, 16, 10).end()
				.end();
		b.element().from(4, 4, 4).to(12, 6, 12)
				.face(Direction.NORTH).texture("#side").uvs(4, 10, 12, 12).end()
				.face(Direction.SOUTH).texture("#side").uvs(4, 10, 12, 12).end()
				.face(Direction.WEST).texture("#side").uvs(4, 10, 12, 12).end()
				.face(Direction.EAST).texture("#side").uvs(4, 10, 12, 12).end()
				.end();
		b.element().from(2, 0, 2).to(14, 4, 14)
				.face(Direction.DOWN).texture("#bottom").uvs(2, 2, 14, 14).end()
				.face(Direction.UP).texture("#bottom").uvs(2, 2, 14, 14).end()
				.face(Direction.NORTH).texture("#side").uvs(2, 12, 14, 16).end()
				.face(Direction.SOUTH).texture("#side").uvs(2, 12, 14, 16).end()
				.face(Direction.WEST).texture("#side").uvs(2, 12, 14, 16).end()
				.face(Direction.EAST).texture("#side").uvs(2, 12, 14, 16).end()
				.end();
	}

	private void blockShapesCrate() {
		model("block/shapes/crate", "minecraft:block/cube", "down", "#bottom", "up", "#side", "north", "#side", "south", "#side", "west", "#side", "east", "#side", "particle", "#side");
	}

	private void blockShapesCreativeManaPool() {
		BlockModelBuilder b = model("block/shapes/creative_mana_pool", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 0, 0).to(16, 2, 16)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 14).to(16, 10, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 0).to(16, 10, 2)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 2).to(2, 10, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(14, 2, 2).to(16, 10, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
	}

	private void blockShapesCreativeManaPoolFull() {
		BlockModelBuilder b = model("block/shapes/creative_mana_pool_full", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 0, 0).to(16, 2, 16)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 14).to(16, 10, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 0).to(16, 10, 2)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 2).to(2, 10, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(14, 2, 2).to(16, 10, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(2, 9, 2).to(14, 9, 14)
				.face(Direction.UP).texture("#mana").end()
				.end();
	}

	private void blockShapesCross() {
		BlockModelBuilder b = model("block/shapes/cross", "minecraft:block/block", "particle", "#cross");
		b.ao(false);
		b.element().from(0.8F, 0, 8).to(15.2F, 16, 8)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end()
				.shade(false)
				.face(Direction.NORTH).texture("#cross").uvs(0, 0, 16, 16).end()
				.face(Direction.SOUTH).texture("#cross").uvs(0, 0, 16, 16).end()
				.end();
		b.element().from(8, 0, 0.8F).to(8, 16, 15.2F)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Y).angle(45).rescale(true).end()
				.shade(false)
				.face(Direction.WEST).texture("#cross").uvs(0, 0, 16, 16).end()
				.face(Direction.EAST).texture("#cross").uvs(0, 0, 16, 16).end()
				.end();
	}

	private void blockShapesCubeAllTinted() {
		BlockModelBuilder b = model("block/shapes/cube_all_tinted", "minecraft:block/block", "particle", "#all");
		b.element().from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#all").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#all").cullface(Direction.UP).tintindex(0).end()
				.face(Direction.NORTH).texture("#all").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#all").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#all").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#all").cullface(Direction.EAST).tintindex(0).end()
				.end();
	}

	private void blockShapesCubeCheckered() {
		BlockModelBuilder b = model("block/shapes/cube_checkered", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesCubeColumnDirectional() {
		model("block/shapes/cube_column_directional", "minecraft:block/cube", "particle", "#side", "down", "#bottom", "up", "#top", "north", "#side", "east", "#side", "south", "#side", "west", "#side");
	}

	private void blockShapesCubeColumnDirectionalHorizontal() {
		BlockModelBuilder b = model("block/shapes/cube_column_directional_horizontal", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
				.face(Direction.UP).texture("#side").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#top").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#bottom").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
	}

	private void blockShapesCubeColumnHorizontalX() {
		BlockModelBuilder b = model("block/shapes/cube_column_horizontal_x", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 16, 16, 0).cullface(Direction.DOWN).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.UP).texture("#side").uvs(0, 16, 16, 0).cullface(Direction.UP).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 16, 16, 0).cullface(Direction.NORTH).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 16, 16, 0).cullface(Direction.SOUTH).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).texture("#end").cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#end").cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesCubeColumnHorizontalZ() {
		BlockModelBuilder b = model("block/shapes/cube_column_horizontal_z", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#end").cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#end").cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 16, 16, 0).cullface(Direction.WEST).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#side").uvs(0, 16, 16, 0).cullface(Direction.EAST).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
	}

	private void blockShapesDilutedManaPool() {
		BlockModelBuilder b = model("block/shapes/diluted_mana_pool", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 0, 0).to(16, 1, 16)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 1, 15).to(16, 6, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 1, 0).to(16, 6, 1)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 1, 1).to(1, 6, 15)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(15, 1, 1).to(16, 6, 15)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
	}

	private void blockShapesDilutedManaPoolFull() {
		BlockModelBuilder b = model("block/shapes/diluted_mana_pool_full", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 0, 0).to(16, 1, 16)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 1, 15).to(16, 6, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 1, 0).to(16, 6, 1)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 1, 1).to(1, 6, 15)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(15, 1, 1).to(16, 6, 15)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(1, 5, 1).to(15, 5, 15)
				.face(Direction.UP).texture("#mana").end()
				.end();
	}

	private void blockShapesDrum() {
		BlockModelBuilder b = model("block/shapes/drum", "minecraft:block/block", "particle", "#side");
		b.element().from(3, 1, 3).to(13, 15, 13)
				.face(Direction.DOWN).texture("#top").uvs(3, 3, 13, 13).end()
				.face(Direction.UP).texture("#top").uvs(3, 3, 13, 13).end()
				.face(Direction.NORTH).texture("#side").uvs(3, 1, 13, 15).end()
				.face(Direction.SOUTH).texture("#side").uvs(3, 1, 13, 15).end()
				.face(Direction.WEST).texture("#side").uvs(3, 1, 13, 15).end()
				.face(Direction.EAST).texture("#side").uvs(3, 1, 13, 15).end()
				.end();
	}

	private void blockShapesEightbyeight() {
		BlockModelBuilder b = model("block/shapes/eightbyeight", "minecraft:block/block", "particle", "#top");
		b.element().from(4, 4, 4).to(12, 12, 12)
				.face(Direction.DOWN).texture("#top").uvs(4, 4, 12, 12).end()
				.face(Direction.UP).texture("#bottom").uvs(4, 4, 12, 12).end()
				.face(Direction.NORTH).texture("#north").uvs(4, 4, 12, 12).end()
				.face(Direction.SOUTH).texture("#south").uvs(4, 4, 12, 12).end()
				.face(Direction.WEST).texture("#west").uvs(4, 4, 12, 12).end()
				.face(Direction.EAST).texture("#east").uvs(4, 4, 12, 12).end()
				.end();
	}

	private void blockShapesFifteenHighAll() {
		BlockModelBuilder b = model("block/shapes/fifteen_high_all", "minecraft:block/block", "particle", "#all");
		b.element().from(0, 0, 0).to(16, 15, 16)
				.face(Direction.DOWN).texture("#all").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#all").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#all").uvs(0, 1, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#all").uvs(0, 1, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#all").uvs(0, 1, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#all").uvs(0, 1, 16, 16).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesFourHighBottomTop() {
		BlockModelBuilder b = model("block/shapes/four_high_bottom_top", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 4, 16)
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 12, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 12, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 12, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").uvs(0, 12, 16, 16).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesManaPool() {
		BlockModelBuilder b = model("block/shapes/mana_pool", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 0, 0).to(16, 2, 16)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 14).to(16, 8, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 0).to(16, 8, 2)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 2).to(2, 8, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(14, 2, 2).to(16, 8, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
	}

	private void blockShapesManaPoolFull() {
		BlockModelBuilder b = model("block/shapes/mana_pool_full", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 0, 0).to(16, 2, 16)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 14).to(16, 8, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 0).to(16, 8, 2)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(0, 2, 2).to(2, 8, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(14, 2, 2).to(16, 8, 14)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.end();
		b.element().from(2, 7, 2).to(14, 7, 14)
				.face(Direction.UP).texture("#mana").end()
				.end();
	}

	private void blockShapesMiniisland() {
		BlockModelBuilder b = model("block/shapes/miniisland", "minecraft:block/block", "bottom", "minecraft:block/dirt");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, 0, 0).scale(0.75F, 0.75F, 0.75F).end();
		b.element().from(4, 6, 4).to(12, 9, 12)
				.face(Direction.NORTH).texture("#side").uvs(0, 0, 8, 3).end()
				.face(Direction.EAST).texture("#side").uvs(3, 0, 11, 3).end()
				.face(Direction.SOUTH).texture("#side").uvs(5, 0, 13, 3).end()
				.face(Direction.WEST).texture("#side").uvs(8, 0, 16, 3).end()
				.face(Direction.UP).texture("#top").uvs(4, 4, 12, 12).end()
				.face(Direction.DOWN).texture("#bottom").uvs(4, 4, 12, 12).end()
				.end();
		b.element().from(7, 4.5F, 7).to(10, 6.5F, 11)
				.face(Direction.NORTH).texture("#bottom").uvs(10, 10, 13, 12).end()
				.face(Direction.EAST).texture("#bottom").uvs(4, 10, 8, 12).end()
				.face(Direction.SOUTH).texture("#bottom").uvs(1, 3, 4, 5).end()
				.face(Direction.WEST).texture("#bottom").uvs(11, 11, 15, 13).end()
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 3, 4).end()
				.end();
		b.element().from(5, 4, 7).to(8, 6, 10)
				.face(Direction.NORTH).texture("#bottom").uvs(10, 3, 13, 5).end()
				.face(Direction.EAST).texture("#bottom").uvs(4, 11, 7, 13).end()
				.face(Direction.SOUTH).texture("#bottom").uvs(8, 6, 11, 8).end()
				.face(Direction.WEST).texture("#bottom").uvs(13, 2, 16, 4).end()
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 3, 3).end()
				.end();
		b.element().from(6, 5, 5).to(11, 7, 8)
				.face(Direction.NORTH).texture("#bottom").uvs(0, 0, 5, 2).end()
				.face(Direction.EAST).texture("#bottom").uvs(0, 0, 3, 2).end()
				.face(Direction.SOUTH).texture("#bottom").uvs(0, 0, 5, 2).end()
				.face(Direction.WEST).texture("#bottom").uvs(0, 0, 3, 2).end()
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 5, 3).end()
				.end();
		b.element().from(7, 3.5F, 6).to(9, 5.5F, 8)
				.face(Direction.NORTH).texture("#bottom").uvs(0, 0, 2, 2).end()
				.face(Direction.EAST).texture("#bottom").uvs(0, 0, 2, 2).end()
				.face(Direction.SOUTH).texture("#bottom").uvs(0, 0, 2, 2).end()
				.face(Direction.WEST).texture("#bottom").uvs(5, 5, 7, 7).end()
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 2, 2).end()
				.end();
	}

	private void blockShapesPetalApothecary() {
		BlockModelBuilder b = model("block/shapes/petal_apothecary", "minecraft:block/block", "particle", "#side");
		b.element().from(2, 0, 2).to(14, 2, 14)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#bottom").end()
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
		b.element().from(4, 2, 4).to(12, 11, 12)
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
		b.element().from(2, 11, 2).to(14, 12, 14)
				.face(Direction.DOWN).texture("#bottom").end()
				.face(Direction.UP).texture("#top").end()
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
		b.element().from(2, 12, 13).to(14, 16, 14)
				.face(Direction.DOWN).texture("#bottom").end()
				.face(Direction.UP).texture("#top").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
		b.element().from(2, 12, 2).to(14, 16, 3)
				.face(Direction.DOWN).texture("#bottom").end()
				.face(Direction.UP).texture("#top").end()
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
		b.element().from(2, 12, 3).to(3, 16, 13)
				.face(Direction.DOWN).texture("#bottom").end()
				.face(Direction.UP).texture("#top").cullface(Direction.UP).end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
		b.element().from(13, 12, 3).to(14, 16, 13)
				.face(Direction.DOWN).texture("#bottom").end()
				.face(Direction.UP).texture("#top").cullface(Direction.UP).end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
	}

	private void blockShapesSlabCheckered() {
		BlockModelBuilder b = model("block/shapes/slab_checkered", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesSlabTopCheckered() {
		BlockModelBuilder b = model("block/shapes/slab_top_checkered", null, "particle", "#side");
		b.element().from(0, 8, 0).to(16, 16, 16)
				.face(Direction.DOWN).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.UP).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 0, 16, 8).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(0, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 0, 16, 8).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").uvs(0, 0, 16, 8).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesSpreader() {
		BlockModelBuilder b = model("block/shapes/spreader", "minecraft:block/block", "particle", "#outside");
		b.element().from(2, 2, 2).to(3, 14, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.WEST).texture("#side").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(13, 2, 2).to(14, 14, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").uvs(14, 2, 2, 14).tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 13, 2).to(13, 14, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.face(Direction.DOWN).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(3, 2, 2).to(13, 3, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.UP).texture("#inside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 3, 13).to(13, 13, 14)
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.end();
		b.element().from(6, 10, 2).to(13, 13, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 3, 2).to(10, 6, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 6, 2).to(6, 13, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(10, 3, 2).to(13, 10, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#outside").tintindex(0).end()
				.end();
	}

	private void blockShapesSpreaderCore() {
		BlockModelBuilder b = model("block/shapes/spreader_core", "minecraft:block/block", "particle", "#core");
		b.element().from(5, 5, 5).to(11, 11, 11)
				.face(Direction.NORTH).texture("#core").end()
				.face(Direction.EAST).texture("#core").end()
				.face(Direction.SOUTH).texture("#core").end()
				.face(Direction.WEST).texture("#core").end()
				.face(Direction.UP).texture("#core").end()
				.face(Direction.DOWN).texture("#core").end()
				.end();
	}

	private void blockShapesSpreaderItem() {
		BlockModelBuilder b = model("block/shapes/spreader_item", "minecraft:block/block", "particle", "#outside");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, 0, 0).scale(0.75F, 0.75F, 0.75F).end()
				.transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 3, 0).scale(0.3F, 0.3F, 0.3F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0, 90, 0).translation(0, 0, 0).scale(0.6F, 0.6F, 0.6F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(75, 45, 0).translation(0, 2.5F, 0).scale(0.45F, 0.45F, 0.45F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, 45, 0).translation(0, 0, 0).scale(0.5F, 0.5F, 0.5F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 225, 0).translation(0, 0, 0).scale(0.5F, 0.5F, 0.5F).end()
				.transform(ItemDisplayContext.HEAD).rotation(0, 0, 0).translation(0, 0, 0).scale(1.3F, 1.3F, 1.3F).end();
		b.element().from(5, 5, 5).to(11, 11, 11)
				.face(Direction.NORTH).texture("#core").end()
				.face(Direction.EAST).texture("#core").end()
				.face(Direction.SOUTH).texture("#core").end()
				.face(Direction.WEST).texture("#core").end()
				.face(Direction.UP).texture("#core").end()
				.face(Direction.DOWN).texture("#core").end()
				.end();
		b.element().from(2, 2, 2).to(3, 14, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.EAST).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.WEST).texture("#side").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(13, 2, 2).to(14, 14, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.EAST).texture("#side").uvs(14, 2, 2, 14).tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.WEST).texture("#inside").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 13, 2).to(13, 14, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.face(Direction.DOWN).texture("#inside").tintindex(0).end()
				.end();
		b.element().from(3, 2, 2).to(13, 3, 14)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.face(Direction.UP).texture("#inside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 3, 13).to(13, 13, 14)
				.face(Direction.NORTH).texture("#inside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#back").tintindex(0).end()
				.end();
		b.element().from(6, 10, 2).to(13, 13, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.DOWN).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 3, 2).to(10, 6, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.UP).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(3, 6, 2).to(6, 13, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.EAST).texture("#outside").tintindex(0).end()
				.end();
		b.element().from(10, 3, 2).to(13, 10, 3)
				.face(Direction.NORTH).texture("#outside").tintindex(0).end()
				.face(Direction.SOUTH).texture("#inside").tintindex(0).end()
				.face(Direction.WEST).texture("#outside").tintindex(0).end()
				.end();
	}

	private void blockShapesSpreaderPadding() {
		BlockModelBuilder b = model("block/shapes/spreader_padding", "minecraft:block/block", "particle", "#side");
		b.element().from(1, 1, 1).to(2, 15, 15)
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.UP).texture("#front").end()
				.end();
		b.element().from(14, 1, 1).to(15, 15, 15)
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.UP).texture("#front").end()
				.end();
		b.element().from(2, 1, 14).to(14, 15, 15)
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.UP).texture("#front").end()
				.end();
		b.element().from(2, 1, 1).to(14, 15, 2)
				.face(Direction.NORTH).texture("#side").end()
				.face(Direction.SOUTH).texture("#side").end()
				.face(Direction.UP).texture("#front").end()
				.end();
		b.element().from(1, 1, 1).to(15, 2, 15)
				.face(Direction.DOWN).texture("#back").rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).end()
				.end();
	}

	private void blockShapesSpreaderScaffolding() {
		BlockModelBuilder b = model("block/shapes/spreader_scaffolding", "minecraft:block/block", "particle", "#top");
		b.element().from(0, 15.99F, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#top").cullface(Direction.UP).tintindex(0).end()
				.face(Direction.DOWN).texture("#top").uvs(0, 16, 16, 0).tintindex(0).end()
				.end();
		b.element().from(0, 0, 0).to(1.997F, 16, 1.997F)
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.EAST).texture("#side").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(0, 0, 14.003F).to(1.997F, 16, 16)
				.face(Direction.NORTH).texture("#side").tintindex(0).end()
				.face(Direction.EAST).texture("#side").tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(14.003F, 0, 14.003F).to(16, 16, 16)
				.face(Direction.NORTH).texture("#side").tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.WEST).texture("#side").tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(14.003F, 0, 0).to(16, 16, 1.997F)
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.EAST).texture("#side").cullface(Direction.EAST).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").tintindex(0).end()
				.face(Direction.WEST).texture("#side").tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(1.997F, 14.003F, 0).to(14.003F, 16, 1.997F)
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").uvs(2, 2, 14, 4).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").tintindex(0).end()
				.end();
		b.element().from(1.997F, 14.003F, 14.003F).to(14.003F, 16, 16)
				.face(Direction.NORTH).texture("#side").uvs(14, 0, 2, 2).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").tintindex(0).end()
				.end();
		b.element().from(14.003F, 14.003F, 1.997F).to(16, 16, 14.003F)
				.face(Direction.EAST).texture("#side").uvs(14, 0, 2, 2).cullface(Direction.EAST).tintindex(0).end()
				.face(Direction.WEST).texture("#side").uvs(14, 2, 2, 4).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").tintindex(0).end()
				.end();
		b.element().from(0, 14.003F, 1.997F).to(1.997F, 16, 14.003F)
				.face(Direction.EAST).texture("#side").tintindex(0).end()
				.face(Direction.WEST).texture("#side").uvs(14, 0, 2, 2).cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").tintindex(0).end()
				.end();
		b.element().from(0, 1.99F, 0).to(16, 1.997F, 16)
				.face(Direction.UP).texture("#top").tintindex(0).end()
				.face(Direction.DOWN).texture("#top").uvs(0, 16, 16, 0).tintindex(0).end()
				.end();
		b.element().from(2, 0, 0).to(14, 2, 2)
				.face(Direction.NORTH).texture("#side").uvs(2, 0, 14, 2).cullface(Direction.NORTH).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").uvs(2, 2, 14, 4).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(1.997F, 0, 14.003F).to(14.003F, 1.997F, 16)
				.face(Direction.NORTH).texture("#side").uvs(14, 0, 2, 2).tintindex(0).end()
				.face(Direction.SOUTH).texture("#side").uvs(2, 0, 14, 2).cullface(Direction.SOUTH).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(14.003F, 0, 1.997F).to(16, 1.997F, 14.003F)
				.face(Direction.EAST).texture("#side").uvs(14, 0, 2, 2).cullface(Direction.EAST).tintindex(0).end()
				.face(Direction.WEST).texture("#side").uvs(14, 2, 2, 4).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
		b.element().from(0, 0, 1.997F).to(1.997F, 1.997F, 14.003F)
				.face(Direction.EAST).texture("#side").uvs(2, 0, 14, 2).tintindex(0).end()
				.face(Direction.WEST).texture("#side").uvs(14, 0, 2, 2).cullface(Direction.WEST).tintindex(0).end()
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).tintindex(0).end()
				.end();
	}

	private void blockShapesStairsCheckered() {
		BlockModelBuilder b = model("block/shapes/stairs_checkered", "minecraft:block/block", "particle", "#side");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 135, 0).translation(0, 0, 0).scale(0.625F, 0.625F, 0.625F).end()
				.transform(ItemDisplayContext.HEAD).rotation(0, -90, 0).translation(0, 0, 0).scale(1, 1, 1).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, -135, 0).translation(0, 2.5F, 0).scale(0.375F, 0.375F, 0.375F).end();
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
		b.element().from(8, 8, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#side").uvs(8, 0, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 0, 8, 8).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(8, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(0, 0, 16, 8).end()
				.face(Direction.EAST).texture("#side").uvs(0, 0, 16, 8).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesStairsCheckered90deg() {
		BlockModelBuilder b = model("block/shapes/stairs_checkered_90deg", "minecraft:block/block", "particle", "#side");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 135, 0).translation(0, 0, 0).scale(0.625F, 0.625F, 0.625F).end()
				.transform(ItemDisplayContext.HEAD).rotation(0, -90, 0).translation(0, 0, 0).scale(1, 1, 1).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(75, -135, 0).translation(0, 2.5F, 0).scale(0.375F, 0.375F, 0.375F).end();
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
		b.element().from(8, 8, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#side").uvs(8, 0, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 0, 8, 8).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#side").uvs(8, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 0, 16, 8).end()
				.face(Direction.EAST).texture("#north").uvs(0, 0, 16, 8).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesStairsInnerCheckered() {
		BlockModelBuilder b = model("block/shapes/stairs_inner_checkered", null, "particle", "#side");
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
		b.element().from(8, 8, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#side").uvs(8, 0, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 0, 8, 8).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(8, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(0, 0, 16, 8).end()
				.face(Direction.EAST).texture("#side").uvs(0, 0, 16, 8).cullface(Direction.EAST).end()
				.end();
		b.element().from(0, 8, 8).to(8, 16, 16)
				.face(Direction.UP).texture("#side").uvs(0, 8, 8, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").uvs(8, 0, 16, 8).end()
				.face(Direction.SOUTH).texture("#north").uvs(0, 0, 8, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(8, 0, 16, 8).cullface(Direction.WEST).end()
				.end();
	}

	private void blockShapesStairsInnerCheckered90deg() {
		BlockModelBuilder b = model("block/shapes/stairs_inner_checkered_90deg", null, "particle", "#side");
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
		b.element().from(8, 8, 0).to(16, 16, 16)
				.face(Direction.UP).texture("#side").uvs(8, 0, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 0, 8, 8).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#side").uvs(8, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 0, 16, 8).end()
				.face(Direction.EAST).texture("#north").uvs(0, 0, 16, 8).cullface(Direction.EAST).end()
				.end();
		b.element().from(0, 8, 8).to(8, 16, 16)
				.face(Direction.UP).texture("#side").uvs(0, 8, 8, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").uvs(8, 0, 16, 8).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 0, 8, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(8, 0, 16, 8).cullface(Direction.WEST).end()
				.end();
	}

	private void blockShapesStairsOuterCheckered() {
		BlockModelBuilder b = model("block/shapes/stairs_outer_checkered", null, "particle", "#side");
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
		b.element().from(8, 8, 8).to(16, 16, 16)
				.face(Direction.UP).texture("#side").uvs(8, 8, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 0, 8, 8).end()
				.face(Direction.SOUTH).texture("#north").uvs(8, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(8, 0, 16, 8).end()
				.face(Direction.EAST).texture("#side").uvs(0, 0, 8, 8).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesStairsOuterCheckered90deg() {
		BlockModelBuilder b = model("block/shapes/stairs_outer_checkered_90deg", null, "particle", "#side");
		b.element().from(0, 0, 0).to(16, 8, 16)
				.face(Direction.DOWN).texture("#side").uvs(0, 0, 16, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#north").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 8, 16, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.WEST).end()
				.face(Direction.EAST).texture("#north").uvs(0, 8, 16, 16).cullface(Direction.EAST).end()
				.end();
		b.element().from(8, 8, 8).to(16, 16, 16)
				.face(Direction.UP).texture("#side").uvs(8, 8, 16, 16).cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").uvs(0, 0, 8, 8).end()
				.face(Direction.SOUTH).texture("#side").uvs(8, 0, 16, 8).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(8, 0, 16, 8).end()
				.face(Direction.EAST).texture("#north").uvs(0, 0, 8, 8).cullface(Direction.EAST).end()
				.end();
	}

	private void blockShapesTenbytenAll() {
		BlockModelBuilder b = model("block/shapes/tenbyten_all", "minecraft:block/block", "particle", "#all");
		b.element().from(3, 3, 3).to(13, 13, 13)
				.face(Direction.DOWN).texture("#all").uvs(3, 3, 13, 13).end()
				.face(Direction.UP).texture("#all").uvs(3, 3, 13, 13).end()
				.face(Direction.NORTH).texture("#all").uvs(3, 3, 13, 13).end()
				.face(Direction.SOUTH).texture("#all").uvs(3, 3, 13, 13).end()
				.face(Direction.WEST).texture("#all").uvs(3, 3, 13, 13).end()
				.face(Direction.EAST).texture("#all").uvs(3, 3, 13, 13).end()
				.end();
	}

	private void blockShapesThreeHighBottomTop() {
		BlockModelBuilder b = model("block/shapes/three_high_bottom_top", "minecraft:block/block", "particle", "#side");
		b.element().from(0, 0, 0).to(16, 3, 16)
				.face(Direction.DOWN).texture("#bottom").uvs(0, 0, 16, 16).end()
				.face(Direction.UP).texture("#top").uvs(0, 0, 16, 16).end()
				.face(Direction.NORTH).texture("#side").uvs(0, 13, 16, 16).end()
				.face(Direction.SOUTH).texture("#side").uvs(0, 13, 16, 16).end()
				.face(Direction.WEST).texture("#side").uvs(0, 13, 16, 16).end()
				.face(Direction.EAST).texture("#side").uvs(0, 13, 16, 16).end()
				.end();
	}

	private void blockShapesWallInventory() {
		BlockModelBuilder b = model("block/shapes/wall_inventory", "minecraft:block/block", "particle", "#wall");
		b.ao(false);
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 135, 0).translation(0, 0, 0).scale(0.625F, 0.625F, 0.625F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0, 90, 0).translation(0, 0, 0).scale(0.5F, 0.5F, 0.5F).end();
		b.element().from(4, 0, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#bottom").uvs(4, 4, 12, 12).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").uvs(4, 4, 12, 12).end()
				.face(Direction.NORTH).texture("#wall").uvs(4, 0, 12, 16).end()
				.face(Direction.SOUTH).texture("#wall").uvs(4, 0, 12, 16).end()
				.face(Direction.WEST).texture("#wall").uvs(4, 0, 12, 16).end()
				.face(Direction.EAST).texture("#wall").uvs(4, 0, 12, 16).end()
				.end();
		b.element().from(5, 0, 0).to(11, 13, 16)
				.face(Direction.DOWN).texture("#bottom").uvs(5, 0, 11, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").uvs(5, 0, 11, 16).end()
				.face(Direction.NORTH).texture("#wall").uvs(5, 3, 11, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#wall").uvs(5, 3, 11, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#wall").uvs(0, 3, 16, 16).end()
				.face(Direction.EAST).texture("#wall").uvs(0, 3, 16, 16).end()
				.end();
	}

	private void blockShapesWallInventoryCheckered() {
		BlockModelBuilder b = model("block/shapes/wall_inventory_checkered", "minecraft:block/block", "particle", "#side");
		b.ao(false);
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 135, 0).translation(0, 0, 0).scale(0.625F, 0.625F, 0.625F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0, 90, 0).translation(0, 0, 0).scale(0.5F, 0.5F, 0.5F).end();
		b.element().from(4, 0, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#side").uvs(4, 4, 12, 12).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").uvs(4, 4, 12, 12).end()
				.face(Direction.NORTH).texture("#north").uvs(4, 0, 12, 16).end()
				.face(Direction.SOUTH).texture("#north").uvs(4, 0, 12, 16).end()
				.face(Direction.WEST).texture("#side").uvs(4, 0, 12, 16).end()
				.face(Direction.EAST).texture("#side").uvs(4, 0, 12, 16).end()
				.end();
		b.element().from(5, 0, 0).to(11, 13, 16)
				.face(Direction.DOWN).texture("#side").uvs(5, 0, 11, 16).cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").uvs(5, 0, 11, 16).end()
				.face(Direction.NORTH).texture("#north").uvs(5, 3, 11, 16).cullface(Direction.NORTH).end()
				.face(Direction.SOUTH).texture("#north").uvs(5, 3, 11, 16).cullface(Direction.SOUTH).end()
				.face(Direction.WEST).texture("#side").uvs(0, 3, 16, 16).end()
				.face(Direction.EAST).texture("#side").uvs(0, 3, 16, 16).end()
				.end();
	}

	private void blockShapesWallPost() {
		BlockModelBuilder b = model("block/shapes/wall_post", null, "particle", "#wall");
		b.element().from(4, 0, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#wall").end()
				.face(Direction.SOUTH).texture("#wall").end()
				.face(Direction.WEST).texture("#wall").end()
				.face(Direction.EAST).texture("#wall").end()
				.end();
	}

	private void blockShapesWallPostCheckered() {
		BlockModelBuilder b = model("block/shapes/wall_post_checkered", null, "particle", "#side");
		b.element().from(4, 0, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").end()
				.face(Direction.SOUTH).texture("#north").end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
	}

	private void blockShapesWallSide() {
		BlockModelBuilder b = model("block/shapes/wall_side", null, "particle", "#wall");
		b.element().from(5, 0, 0).to(11, 14, 8)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").end()
				.face(Direction.NORTH).texture("#wall").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#wall").end()
				.face(Direction.EAST).texture("#wall").end()
				.end();
	}

	private void blockShapesWallSideCheckered() {
		BlockModelBuilder b = model("block/shapes/wall_side_checkered", null, "particle", "#side");
		b.element().from(5, 0, 0).to(11, 14, 8)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").end()
				.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
	}

	private void blockShapesWallSideCheckered90deg() {
		BlockModelBuilder b = model("block/shapes/wall_side_checkered_90deg", null, "particle", "#side");
		b.element().from(5, 0, 0).to(11, 14, 8)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#north").end()
				.face(Direction.EAST).texture("#north").end()
				.end();
	}

	private void blockShapesWallSideTall() {
		BlockModelBuilder b = model("block/shapes/wall_side_tall", null, "particle", "#wall");
		b.element().from(5, 0, 0).to(11, 16, 8)
				.face(Direction.DOWN).texture("#bottom").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#top").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#wall").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#wall").end()
				.face(Direction.EAST).texture("#wall").end()
				.end();
	}

	private void blockShapesWallSideTallCheckered() {
		BlockModelBuilder b = model("block/shapes/wall_side_tall_checkered", null, "particle", "#side");
		b.element().from(5, 0, 0).to(11, 16, 8)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#north").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#side").end()
				.face(Direction.EAST).texture("#side").end()
				.end();
	}

	private void blockShapesWallSideTallCheckered90deg() {
		BlockModelBuilder b = model("block/shapes/wall_side_tall_checkered_90deg", null, "particle", "#side");
		b.element().from(5, 0, 0).to(11, 16, 8)
				.face(Direction.DOWN).texture("#side").cullface(Direction.DOWN).end()
				.face(Direction.UP).texture("#side").cullface(Direction.UP).end()
				.face(Direction.NORTH).texture("#side").cullface(Direction.NORTH).end()
				.face(Direction.WEST).texture("#north").end()
				.face(Direction.EAST).texture("#north").end()
				.end();
	}

	private void blockSpawnerClaw() {
		BlockModelBuilder b = model("block/spawner_claw", "minecraft:block/block", "texture", "botania:block/life_imbuer", "particle", "botania:block/life_imbuer");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(0, 4, 0).scale(0.625F, 0.625F, 0.625F).end();
		b.element().from(2, 0, 2).to(14, 2, 14)
				.face(Direction.DOWN).texture("#texture").uvs(0, 0, 12, 12).end()
				.face(Direction.UP).texture("#texture").uvs(0, 0, 12, 12).end()
				.face(Direction.NORTH).texture("#texture").uvs(0, 12, 12, 14).end()
				.face(Direction.SOUTH).texture("#texture").uvs(0, 12, 12, 14).end()
				.face(Direction.WEST).texture("#texture").uvs(0, 12, 12, 14).end()
				.face(Direction.EAST).texture("#texture").uvs(0, 12, 12, 14).end()
				.end();
		b.element().from(-2, 0, 7).to(2, 4, 9)
				.face(Direction.DOWN).texture("#texture").uvs(12, 4, 16, 6).end()
				.face(Direction.UP).texture("#texture").uvs(12, 4, 16, 6).end()
				.face(Direction.NORTH).texture("#texture").uvs(12, 0, 16, 4).end()
				.face(Direction.SOUTH).texture("#texture").uvs(12, 0, 16, 4).end()
				.face(Direction.WEST).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 6, 14, 10).end()
				.end();
		b.element().from(-2, -2, 7).to(0, 0, 9)
				.face(Direction.DOWN).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.UP).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.NORTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(14, 14, 16, 16).end()
				.end();
		b.element().from(-1, -6, 7).to(0, -2, 9)
				.face(Direction.DOWN).texture("#texture").uvs(15, 12, 16, 10).end()
				.face(Direction.UP).texture("#texture").uvs(12, 10, 13, 12).end()
				.face(Direction.NORTH).texture("#texture").uvs(12, 12, 13, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(13, 12, 14, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(12, 12, 14, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 12, 14, 16).end()
				.end();
		b.element().from(14, 0, 7).to(18, 4, 9)
				.face(Direction.DOWN).texture("#texture").uvs(12, 4, 16, 6).end()
				.face(Direction.UP).texture("#texture").uvs(12, 4, 16, 6).end()
				.face(Direction.NORTH).texture("#texture").uvs(12, 0, 16, 4).end()
				.face(Direction.SOUTH).texture("#texture").uvs(12, 0, 16, 4).end()
				.face(Direction.WEST).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 6, 14, 10).end()
				.end();
		b.element().from(16, -2, 7).to(18, 0, 9)
				.face(Direction.DOWN).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.UP).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.NORTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(14, 14, 16, 16).end()
				.end();
		b.element().from(16, -6, 7).to(17, -2, 9)
				.face(Direction.DOWN).texture("#texture").uvs(15, 10, 16, 12).end()
				.face(Direction.UP).texture("#texture").uvs(12, 10, 13, 12).end()
				.face(Direction.NORTH).texture("#texture").uvs(13, 12, 14, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(12, 12, 13, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(14, 12, 12, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 12, 14, 16).end()
				.end();
		b.element().from(7, 0, 14).to(9, 4, 18)
				.face(Direction.DOWN).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.UP).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.NORTH).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.SOUTH).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.WEST).texture("#texture").uvs(12, 0, 16, 4).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 0, 16, 4).end()
				.end();
		b.element().from(7, -2, 16).to(9, 0, 18)
				.face(Direction.DOWN).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.UP).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.NORTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(14, 14, 16, 16).end()
				.end();
		b.element().from(7, -6, 16).to(9, -2, 17)
				.face(Direction.DOWN).texture("#texture").uvs(12, 15, 14, 16).end()
				.face(Direction.UP).texture("#texture").uvs(12, 12, 14, 13).end()
				.face(Direction.NORTH).texture("#texture").uvs(14, 12, 12, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(12, 12, 14, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(12, 12, 13, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(13, 12, 14, 16).end()
				.end();
		b.element().from(7, 0, -2).to(9, 4, 2)
				.face(Direction.DOWN).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.UP).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.NORTH).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.SOUTH).texture("#texture").uvs(12, 6, 14, 10).end()
				.face(Direction.WEST).texture("#texture").uvs(12, 0, 16, 4).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 0, 16, 4).end()
				.end();
		b.element().from(7, -2, -2).to(9, 0, 0)
				.face(Direction.DOWN).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.UP).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.NORTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(14, 14, 16, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(14, 14, 16, 16).end()
				.end();
		b.element().from(7, -6, -1).to(9, -2, 0)
				.face(Direction.DOWN).texture("#texture").uvs(14, 15, 12, 16).end()
				.face(Direction.UP).texture("#texture").uvs(12, 12, 14, 13).end()
				.face(Direction.NORTH).texture("#texture").uvs(12, 12, 14, 16).end()
				.face(Direction.SOUTH).texture("#texture").uvs(14, 12, 12, 16).end()
				.face(Direction.WEST).texture("#texture").uvs(13, 12, 14, 16).end()
				.face(Direction.EAST).texture("#texture").uvs(12, 12, 13, 16).end()
				.end();
	}

	private void tinyPotatoAcetater() {
		model("tiny_potato/acetater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/ace");
	}

	private void tinyPotatoAgendertater() {
		model("tiny_potato/agendertater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/agender");
	}

	private void tinyPotatoAromantictater() {
		model("tiny_potato/aromantictater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/aro");
	}

	private void tinyPotatoArotater() {
		model("tiny_potato/arotater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/aro");
	}

	private void tinyPotatoAsexualtater() {
		model("tiny_potato/asexualtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/ace");
	}

	private void tinyPotatoAureylian() {
		model("tiny_potato/aureylian", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/aureylian");
	}

	private void tinyPotatoBase() {
		BlockModelBuilder b = model("tiny_potato/base", "minecraft:block/cube_all", "all", "botania:block/tiny_potato/default");
		b.transforms()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-36, 137, 20).translation(1, 3, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 162, 0).translation(0, 3.2F, 1).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.HEAD).translation(0, 14.5F, 0).end();
	}

	private void tinyPotatoBitater() {
		model("tiny_potato/bitater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bi");
	}

	private void tinyPotatoBiter() {
		model("tiny_potato/biter", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bi");
	}

	private void tinyPotatoBosniaHerzegovina() {
		model("tiny_potato/bosnia_herzegovina", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoBosniaherzegovina() {
		model("tiny_potato/bosniaherzegovina", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoBosniantater() {
		model("tiny_potato/bosniantater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoBotaniaHerzegovina() {
		model("tiny_potato/botania_herzegovina", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoBotaniaherzegovina() {
		model("tiny_potato/botaniaherzegovina", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoBotaniatater() {
		model("tiny_potato/botaniatater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoBotater() {
		model("tiny_potato/botater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/bosnia");
	}

	private void tinyPotatoDefault() {
		BlockModelBuilder b = model("tiny_potato/default", "botania:tiny_potato/base");
		b.element().from(6, 0, 6).to(10, 6, 10)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
	}

	private void tinyPotatoDinnerbone() {
		BlockModelBuilder b = model("tiny_potato/dinnerbone", "botania:tiny_potato/base");
		b.element().from(10, 6, 6).to(6, 0, 10)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
	}

	private void tinyPotatoEggtater() {
		model("tiny_potato/eggtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/trans");
	}

	private void tinyPotatoEnbytater() {
		model("tiny_potato/enbytater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/wire");
	}

	private void tinyPotatoEutrotater() {
		model("tiny_potato/eutrotater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/wire");
	}

	private void tinyPotatoGaytater() {
		model("tiny_potato/gaytater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/pride");
	}

	private void tinyPotatoGayter() {
		model("tiny_potato/gayter", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/pride");
	}

	private void tinyPotatoGenderfluidtater() {
		model("tiny_potato/genderfluidtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/genderfluid");
	}

	private void tinyPotatoGirlstater() {
		model("tiny_potato/girlstater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/lesbian");
	}

	private void tinyPotatoGrumm() {
		model("tiny_potato/grumm", "botania:tiny_potato/dinnerbone");
	}

	private void tinyPotatoHalloween() {
		model("tiny_potato/halloween", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/halloween");
	}

	private void tinyPotatoKingdaddydmac() {
		BlockModelBuilder b = model("tiny_potato/kingdaddydmac", "botania:tiny_potato/base");
		b.transforms()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-38, 146, 6).translation(-2.5F, 3.5F, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(-38, 146, 6).translation(2, 3.5F, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 162, 0).translation(-1, 2, 1).scale(0.25F, 0.25F, 0.25F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 162, 0).translation(1, 2, 1.5F).scale(0.25F, 0.25F, 0.25F).end()
				.transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(1, 3, 0).scale(0.25F, 0.25F, 0.25F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(2, 0, 0).scale(0.5F, 0.5F, 0.5F).end()
				.transform(ItemDisplayContext.GUI).rotation(30, 225, 0).translation(-1.25F, 0, 0).scale(0.625F, 0.625F, 0.625F).end();
		b.element().from(6, 0, 6).to(10, 6, 10)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
		b.element().from(-2, 0, 6).to(2, 6, 10)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
	}

	private void tinyPotatoKyleHyde() {
		model("tiny_potato/kyle_hyde", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/gray");
	}

	private void tinyPotatoLesbiabtater() {
		model("tiny_potato/lesbiabtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/lesbian");
	}

	private void tinyPotatoLesbiamtater() {
		model("tiny_potato/lesbiamtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/lesbian");
	}

	private void tinyPotatoLesbiantater() {
		model("tiny_potato/lesbiantater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/lesbian");
	}

	private void tinyPotatoLesbitater() {
		model("tiny_potato/lesbitater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/lesbian");
	}

	private void tinyPotatoLessbientater() {
		model("tiny_potato/lessbientater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/lesbian");
	}

	private void tinyPotatoLgbtater() {
		model("tiny_potato/lgbtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/pride");
	}

	private void tinyPotatoManytater() {
		model("tiny_potato/manytater", "botania:tiny_potato/snorps");
	}

	private void tinyPotatoNbtater() {
		model("tiny_potato/nbtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/wire");
	}

	private void tinyPotatoNonbinarytater() {
		model("tiny_potato/nonbinarytater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/wire");
	}

	private void tinyPotatoPahimar() {
		BlockModelBuilder b = model("tiny_potato/pahimar", "botania:tiny_potato/base");
		b.transforms()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-36, 137, 0).translation(1, 3.75F, 0).scale(0.4F, 0.4F, 0.4F).end();
		b.element().from(6, 0, 6).to(10, 1.8F, 10)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
	}

	private void tinyPotatoPantater() {
		model("tiny_potato/pantater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/pan");
	}

	private void tinyPotatoPanter() {
		model("tiny_potato/panter", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/pan");
	}

	private void tinyPotatoPluraltater() {
		model("tiny_potato/pluraltater", "botania:tiny_potato/snorps");
	}

	private void tinyPotatoPridetater() {
		model("tiny_potato/pridetater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/pride");
	}

	private void tinyPotatoSnorps() {
		BlockModelBuilder b = model("tiny_potato/snorps", "botania:tiny_potato/base");
		b.element().from(6, 0, 6).to(10, 6, 10)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
		b.element().from(4, 0, 7).to(6, 3, 9)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
		b.element().from(10, 0, 7).to(12, 3, 9)
				.face(Direction.DOWN).texture("#all").uvs(12, 4, 8, 0).end()
				.face(Direction.UP).texture("#all").uvs(8, 4, 4, 0).end()
				.face(Direction.NORTH).texture("#all").uvs(4, 4, 8, 10).end()
				.face(Direction.SOUTH).texture("#all").uvs(12, 4, 16, 10).end()
				.face(Direction.WEST).texture("#all").uvs(8, 4, 12, 10).end()
				.face(Direction.EAST).texture("#all").uvs(0, 4, 4, 10).end()
				.end();
	}

	private void tinyPotatoSpooktater() {
		model("tiny_potato/spooktater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/halloween");
	}

	private void tinyPotatoSpooky() {
		model("tiny_potato/spooky", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/halloween");
	}

	private void tinyPotatoSystater() {
		model("tiny_potato/systater", "botania:tiny_potato/snorps");
	}

	private void tinyPotatoSystemtater() {
		model("tiny_potato/systemtater", "botania:tiny_potato/snorps");
	}

	private void tinyPotatoTacer() {
		model("tiny_potato/tacer", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/ace");
	}

	private void tinyPotatoTaceter() {
		model("tiny_potato/taceter", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/ace");
	}

	private void tinyPotatoTataro() {
		model("tiny_potato/tataro", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/aro");
	}

	private void tinyPotatoTategg() {
		model("tiny_potato/tategg", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/trans");
	}

	private void tinyPotatoTaterfluid() {
		model("tiny_potato/taterfluid", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/genderfluid");
	}

	private void tinyPotatoTomater() {
		BlockModelBuilder b = model("tiny_potato/tomater", "botania:tiny_potato/base", "all", "botania:block/tiny_potato/tomato");
		b.transforms()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(-36, 137, 20).translation(1, 3.325F, 0).scale(0.4F, 0.4F, 0.4F).end();
		b.element().from(5.5F, 0, 5.5F).to(10.5F, 5, 10.5F)
				.face(Direction.DOWN).texture("#all").uvs(0, 5, 5, 10).end()
				.face(Direction.UP).texture("#all").uvs(0, 0, 5, 5).end()
				.face(Direction.NORTH).texture("#all").uvs(5, 0, 10, 5).end()
				.face(Direction.SOUTH).texture("#all").uvs(5, 5, 10, 10).end()
				.face(Direction.WEST).texture("#all").uvs(10, 5, 15, 10).end()
				.face(Direction.EAST).texture("#all").uvs(10, 0, 15, 5).end()
				.end();
	}

	private void tinyPotatoTranstater() {
		model("tiny_potato/transtater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/trans");
	}

	private void tinyPotatoWiretater() {
		model("tiny_potato/wiretater", "botania:tiny_potato/default", "all", "botania:block/tiny_potato/wire");
	}

	@Override
	public String getName() {
		return "Botania shape and block models";
	}
}
