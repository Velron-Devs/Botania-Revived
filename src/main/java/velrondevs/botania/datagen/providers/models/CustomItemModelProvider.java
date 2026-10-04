package velrondevs.botania.datagen.providers.models;

import com.google.gson.JsonObject;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.generators.CustomLoaderBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import velrondevs.botania.common.lib.LibMisc;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CustomItemModelProvider extends ItemModelProvider {
	public CustomItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, LibMisc.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		itemCorporeaCrystalCube();
		itemCrystalBow();
		itemLivingwoodBow();
		itemManaGun();
		itemManaGunClip();
		itemManaGunNoclip();
	}

	private ItemModelBuilder model(String path, String parent, String... textures) {
		ItemModelBuilder builder = getBuilder(path);
		if (parent != null) {
			builder.parent(new ModelFile.UncheckedModelFile(parent));
		}
		for (int i = 0; i < textures.length; i += 2) {
			builder.texture(textures[i], textures[i + 1]);
		}
		return builder;
	}

	private void itemCorporeaCrystalCube() {
		ItemModelBuilder b = model("item/corporea_crystal_cube", "minecraft:block/block", "cloth", "botania:block/corporea_crystal_cube_cloth", "base", "botania:block/corporea_crystal_cube_base", "glass", "botania:block/corporea_crystal_cube_glass", "particle", "botania:block/corporea_crystal_cube_base");
		b.element().from(4, 8, 4).to(12, 16, 12)
				.face(Direction.DOWN).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.UP).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.NORTH).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.SOUTH).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.WEST).texture("#glass").uvs(4, 4, 12, 12).end()
				.face(Direction.EAST).texture("#glass").uvs(4, 4, 12, 12).end()
				.end();
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

	private void itemCrystalBow() {
		ItemModelBuilder b = model("item/crystal_bow", "minecraft:item/generated", "layer0", "botania:item/crystal_bow");
		b.transforms()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-80, 260, -40).translation(-1, -2, 2.5F).scale(0.9F, 0.9F, 0.9F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-80, -280, 40).translation(-1, -2, 2.5F).scale(0.9F, 0.9F, 0.9F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(1.13F, 3.2F, 1.13F).scale(0.68F, 0.68F, 0.68F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(1.13F, 3.2F, 1.13F).scale(0.68F, 0.68F, 0.68F).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).model(new ModelFile.UncheckedModelFile("botania:item/crystal_bow_pulling_1")).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).predicate(ResourceLocation.parse("minecraft:pull"), 0.35F).model(new ModelFile.UncheckedModelFile("botania:item/crystal_bow_pulling_2")).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).predicate(ResourceLocation.parse("minecraft:pull"), 0.65F).model(new ModelFile.UncheckedModelFile("botania:item/crystal_bow_pulling_3")).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).predicate(ResourceLocation.parse("minecraft:pull"), 0.8F).model(new ModelFile.UncheckedModelFile("botania:item/crystal_bow_pulling_4")).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).predicate(ResourceLocation.parse("minecraft:pull"), 0.9F).model(new ModelFile.UncheckedModelFile("botania:item/crystal_bow_pulling_5")).end();
	}

	private void itemLivingwoodBow() {
		ItemModelBuilder b = model("item/livingwood_bow", "minecraft:item/generated", "layer0", "botania:item/livingwood_bow");
		b.transforms()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(-80, 260, -40).translation(-1, -2, 2.5F).scale(0.9F, 0.9F, 0.9F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(-80, -280, 40).translation(-1, -2, 2.5F).scale(0.9F, 0.9F, 0.9F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(1.13F, 3.2F, 1.13F).scale(0.68F, 0.68F, 0.68F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(1.13F, 3.2F, 1.13F).scale(0.68F, 0.68F, 0.68F).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).model(new ModelFile.UncheckedModelFile("botania:item/livingwood_bow_pulling_1")).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).predicate(ResourceLocation.parse("minecraft:pull"), 0.65F).model(new ModelFile.UncheckedModelFile("botania:item/livingwood_bow_pulling_2")).end();
		b.override().predicate(ResourceLocation.parse("minecraft:pulling"), 1).predicate(ResourceLocation.parse("minecraft:pull"), 0.9F).model(new ModelFile.UncheckedModelFile("botania:item/livingwood_bow_pulling_3")).end();
	}

	private void itemManaGun() {
		getBuilder("item/mana_gun").customLoader(ManaGunLoaderBuilder::new).models("botania:item/mana_gun_noclip", "botania:item/mana_gun_clip");
	}

	private void itemManaGunClip() {
		ItemModelBuilder b = model("item/mana_gun_clip", null, "layer0", "botania:item/mana_blaster", "layer1", "botania:item/mana_blaster_color", "layer2", "botania:item/mana_blaster_glass", "layer3", "botania:item/mana_blaster_clip");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(0, -20, -40).translation(0, 0, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 0, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.45F, 0.45F, 0.45F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 0).translation(0, 2.45F, -1.5F).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, -90, 0).translation(0, 2.45F, -1.5F).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(10, 80, -5).translation(0.8F, 2.5F, 0).scale(0.45F, 0.45F, 0.45F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(10, -80, 5).translation(-1.8F, 2.5F, 0).scale(0.45F, 0.45F, 0.45F).end();
		b.element().from(-10, 5, 5.5F).to(-3, 14, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(0, 7.01F, 3.5F, 11.49F).end()
				.face(Direction.EAST).texture("#layer0").uvs(6, 7, 8.5F, 11.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(3.5F, 7.01F, 0, 11.49F).end()
				.face(Direction.WEST).texture("#layer0").uvs(3.5F, 7.01F, 6, 11.49F).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 10.5F, 13.49F, 13.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.5F, 10.5F, 16, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(-3, 5, 5.5F).to(7, 9, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(11.01F, 14, 15.99F, 15.99F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(16, 14, 11, 16).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 5, 13.49F, 10).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.5F, 5, 15.99F, 10).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.end();
		b.element().from(7, 5, 5.5F).to(10, 14, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(0, 11.5F, 1.5F, 16).end()
				.face(Direction.EAST).texture("#layer0").uvs(1.5F, 11.5F, 4, 16).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(0, 11.5F, 1.5F, 16).end()
				.face(Direction.WEST).texture("#layer0").uvs(1.5F, 11.5F, 4, 16).end()
				.face(Direction.UP).texture("#layer0").uvs(11, 4, 13.5F, 5.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.5F, 4, 16, 5.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(10, 12, 5.5F).to(18, 14, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(4.01F, 14.5F, 8, 15.5F).end()
				.face(Direction.EAST).texture("#layer0").uvs(4, 14.5F, 6.5F, 15.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(8, 14.5F, 4, 15.5F).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 0.01F, 13.49F, 3.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(11, 0.01F, 13.5F, 4).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(10, 9, 5.5F).to(18, 11, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(8, 14.5F, 4, 15.5F).end()
				.face(Direction.EAST).texture("#layer0").uvs(6.5F, 14.5F, 4, 15.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(4, 14.5F, 8, 15.5F).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 0, 13.49F, 3.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.51F, 0.01F, 15.99F, 3.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(14, -5, 6.5F).to(19, 9, 9.5F)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Z).angle(22.5F).end()
				.face(Direction.NORTH).texture("#layer0").uvs(0, 0.01F, 2.5F, 6.99F).end()
				.face(Direction.EAST).texture("#layer0").uvs(4, 0.01F, 5.5F, 6.99F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(2.5F, 0.01F, 0, 6.99F).end()
				.face(Direction.WEST).texture("#layer0").uvs(2.5F, 0.01F, 4, 6.99F).end()
				.face(Direction.DOWN).texture("#layer0").uvs(5.5F, 0, 7, 2.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(12.5F, -6, 7).to(18.5F, -5, 9)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Z).angle(22.5F).end()
				.face(Direction.NORTH).texture("#layer0").uvs(7.5F, 0, 8, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#layer0").uvs(8, 0, 7, 0.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(8, 0.01F, 7.5F, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).texture("#layer0").uvs(7, 0.01F, 8, 0.5F).end()
				.face(Direction.UP).texture("#layer0").uvs(7, 0.01F, 8, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(7.01F, 0, 7.99F, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(10, 6, 7).to(16, 7, 9)
				.face(Direction.NORTH).texture("#layer0").uvs(5.5F, 3.01F, 8.5F, 3.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(5.5F, 3.01F, 8.5F, 3.49F).end()
				.face(Direction.UP).texture("#layer0").uvs(5.5F, 3, 8.5F, 4).end()
				.face(Direction.DOWN).texture("#layer0").uvs(5.5F, 3, 8.5F, 4).end()
				.end();
		b.element().from(13, 7, 7.5F).to(16, 9, 8.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(5.5F, 5, 6.5F, 6).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(5.5F, 5, 6.5F, 6).end()
				.face(Direction.WEST).texture("#layer0").uvs(5.5F, 5, 6.5F, 5.5F).end()
				.end();
		b.element().from(10, 7, 7).to(11, 9, 9)
				.face(Direction.NORTH).texture("#layer0").uvs(5.5F, 4, 6, 5).end()
				.face(Direction.EAST).texture("#layer0").uvs(5.5F, 4, 6.5F, 5).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(5.5F, 4, 6, 5).end()
				.end();
		b.element().from(9.5F, 11, 6).to(17.5F, 12, 10)
				.face(Direction.NORTH).texture("#layer1").uvs(0, 0, 8, 5).tintindex(2).end()
				.face(Direction.EAST).texture("#layer1").uvs(0, 0, 5, 5).tintindex(2).end()
				.face(Direction.SOUTH).texture("#layer1").uvs(0, 0, 8, 5).tintindex(2).end()
				.end();
		b.element().from(-3.5F, 9.5F, 6).to(7.5F, 13.5F, 10)
				.face(Direction.NORTH).texture("#layer2").uvs(0.01F, 7.01F, 6.99F, 7.99F).end()
				.face(Direction.EAST).texture("#layer2").uvs(7.01F, 7.01F, 9.49F, 7.99F).end()
				.face(Direction.SOUTH).texture("#layer2").uvs(6.99F, 7.01F, 0.01F, 7.99F).end()
				.face(Direction.UP).texture("#layer2").uvs(11.01F, 0.01F, 13.49F, 6.99F).end()
				.face(Direction.DOWN).texture("#layer2").uvs(8.51F, 0.01F, 6.99F, 9.99F).end()
				.end();
		b.element().from(-4.5F, 2, 5).to(8.5F, 8, 11)
				.face(Direction.NORTH).texture("#layer3").uvs(0.01F, 6.51F, 6.5F, 9.49F).end()
				.face(Direction.EAST).texture("#layer3").uvs(6.01F, 0, 9, 3).end()
				.face(Direction.SOUTH).texture("#layer3").uvs(6.5F, 6.5F, 0.01F, 9.5F).end()
				.face(Direction.WEST).texture("#layer3").uvs(6, 3, 9, 6).end()
				.face(Direction.UP).texture("#layer3").uvs(0, 0.01F, 3, 6.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer3").uvs(3, 0.01F, 6, 6.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
	}

	private void itemManaGunNoclip() {
		ItemModelBuilder b = model("item/mana_gun_noclip", null, "layer0", "botania:item/mana_blaster", "layer1", "botania:item/mana_blaster_color", "layer2", "botania:item/mana_blaster_glass");
		b.transforms()
				.transform(ItemDisplayContext.GUI).rotation(0, -20, -40).translation(0, 0, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.GROUND).rotation(0, 0, 0).translation(0, 0, 0).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.FIXED).rotation(0, 0, 0).translation(0, 0, 0).scale(0.45F, 0.45F, 0.45F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 0).translation(0, 2.45F, -1.5F).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, -90, 0).translation(0, 2.45F, -1.5F).scale(0.4F, 0.4F, 0.4F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(10, 80, -5).translation(0.8F, 2.5F, 0).scale(0.45F, 0.45F, 0.45F).end()
				.transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(10, -80, 5).translation(-1.8F, 2.5F, 0).scale(0.45F, 0.45F, 0.45F).end();
		b.element().from(-10, 5, 5.5F).to(-3, 14, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(0, 7.01F, 3.5F, 11.49F).end()
				.face(Direction.EAST).texture("#layer0").uvs(6, 7, 8.5F, 11.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(3.5F, 7.01F, 0, 11.49F).end()
				.face(Direction.WEST).texture("#layer0").uvs(3.5F, 7.01F, 6, 11.49F).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 10.5F, 13.49F, 13.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.5F, 10.5F, 16, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(-3, 5, 5.5F).to(7, 9, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(11.01F, 14, 15.99F, 15.99F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(16, 14, 11, 16).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 5, 13.49F, 10).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.5F, 5, 15.99F, 10).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).end()
				.end();
		b.element().from(7, 5, 5.5F).to(10, 14, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(0, 11.5F, 1.5F, 16).end()
				.face(Direction.EAST).texture("#layer0").uvs(1.5F, 11.5F, 4, 16).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(0, 11.5F, 1.5F, 16).end()
				.face(Direction.WEST).texture("#layer0").uvs(1.5F, 11.5F, 4, 16).end()
				.face(Direction.UP).texture("#layer0").uvs(11, 4, 13.5F, 5.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.5F, 4, 16, 5.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(10, 12, 5.5F).to(18, 14, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(4.01F, 14.5F, 8, 15.5F).end()
				.face(Direction.EAST).texture("#layer0").uvs(4, 14.5F, 6.5F, 15.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(8, 14.5F, 4, 15.5F).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 0.01F, 13.49F, 3.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(11, 0.01F, 13.5F, 4).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(10, 9, 5.5F).to(18, 11, 10.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(8, 14.5F, 4, 15.5F).end()
				.face(Direction.EAST).texture("#layer0").uvs(6.5F, 14.5F, 4, 15.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(4, 14.5F, 8, 15.5F).end()
				.face(Direction.UP).texture("#layer0").uvs(11.01F, 0, 13.49F, 3.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(13.51F, 0.01F, 15.99F, 3.99F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(14, -5, 6.5F).to(19, 9, 9.5F)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Z).angle(22.5F).end()
				.face(Direction.NORTH).texture("#layer0").uvs(0, 0.01F, 2.5F, 6.99F).end()
				.face(Direction.EAST).texture("#layer0").uvs(4, 0.01F, 5.5F, 6.99F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(2.5F, 0.01F, 0, 6.99F).end()
				.face(Direction.WEST).texture("#layer0").uvs(2.5F, 0.01F, 4, 6.99F).end()
				.face(Direction.DOWN).texture("#layer0").uvs(5.5F, 0, 7, 2.5F).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(12.5F, -6, 7).to(18.5F, -5, 9)
				.rotation().origin(8, 8, 8).axis(Direction.Axis.Z).angle(22.5F).end()
				.face(Direction.NORTH).texture("#layer0").uvs(7.5F, 0, 8, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.EAST).texture("#layer0").uvs(8, 0, 7, 0.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(8, 0.01F, 7.5F, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.WEST).texture("#layer0").uvs(7, 0.01F, 8, 0.5F).end()
				.face(Direction.UP).texture("#layer0").uvs(7, 0.01F, 8, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.face(Direction.DOWN).texture("#layer0").uvs(7.01F, 0, 7.99F, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).end()
				.end();
		b.element().from(10, 6, 7).to(16, 7, 9)
				.face(Direction.NORTH).texture("#layer0").uvs(5.5F, 3.01F, 8.5F, 3.5F).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(5.5F, 3.01F, 8.5F, 3.49F).end()
				.face(Direction.UP).texture("#layer0").uvs(5.5F, 3, 8.5F, 4).end()
				.face(Direction.DOWN).texture("#layer0").uvs(5.5F, 3, 8.5F, 4).end()
				.end();
		b.element().from(13, 7, 7.5F).to(16, 9, 8.5F)
				.face(Direction.NORTH).texture("#layer0").uvs(5.5F, 5, 6.5F, 6).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(5.5F, 5, 6.5F, 6).end()
				.face(Direction.WEST).texture("#layer0").uvs(5.5F, 5, 6.5F, 5.5F).end()
				.end();
		b.element().from(10, 7, 7).to(11, 9, 9)
				.face(Direction.NORTH).texture("#layer0").uvs(5.5F, 4, 6, 5).end()
				.face(Direction.EAST).texture("#layer0").uvs(5.5F, 4, 6.5F, 5).end()
				.face(Direction.SOUTH).texture("#layer0").uvs(5.5F, 4, 6, 5).end()
				.end();
		b.element().from(9.5F, 11, 6).to(17.5F, 12, 10)
				.face(Direction.NORTH).texture("#layer1").uvs(0, 0, 8, 5).tintindex(2).end()
				.face(Direction.EAST).texture("#layer1").uvs(0, 0, 5, 5).tintindex(2).end()
				.face(Direction.SOUTH).texture("#layer1").uvs(0, 0, 8, 5).tintindex(2).end()
				.end();
		b.element().from(-3.5F, 9.5F, 6).to(7.5F, 13.5F, 10)
				.face(Direction.NORTH).texture("#layer2").uvs(0.01F, 7.01F, 6.99F, 7.99F).end()
				.face(Direction.EAST).texture("#layer2").uvs(7.01F, 7.01F, 9.49F, 7.99F).end()
				.face(Direction.SOUTH).texture("#layer2").uvs(6.99F, 7.01F, 0.01F, 7.99F).end()
				.face(Direction.UP).texture("#layer2").uvs(11.01F, 0.01F, 13.49F, 6.99F).end()
				.face(Direction.DOWN).texture("#layer2").uvs(8.51F, 0.01F, 6.99F, 9.99F).end()
				.end();
	}

	private static class ManaGunLoaderBuilder extends CustomLoaderBuilder<ItemModelBuilder> {
		private String noClip;
		private String clip;

		private ManaGunLoaderBuilder(ItemModelBuilder parent, ExistingFileHelper existingFileHelper) {
			super(prefix("mana_gun"), parent, existingFileHelper, false);
		}

		private ItemModelBuilder models(String noClip, String clip) {
			this.noClip = noClip;
			this.clip = clip;
			return end();
		}

		@Override
		public JsonObject toJson(JsonObject json) {
			json = super.toJson(json);
			json.addProperty("gun_noclip", noClip);
			json.addProperty("gun_clip", clip);
			return json;
		}
	}

	@Override
	public String getName() {
		return "Botania custom item models";
	}
}
