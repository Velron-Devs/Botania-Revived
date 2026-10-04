package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block.PetalApothecary;
import velrondevs.botania.common.block.PetalApothecaryBlock;
import velrondevs.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import velrondevs.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import velrondevs.botania.common.block.block_entity.RunicAltarBlockEntity;
import velrondevs.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.common.block.flower.PureDaisyBlockEntity;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.ArrayList;
import java.util.List;

final class CraftingScenes {
	private static final BlockPos[] DAISY_OFFSETS = {
			new BlockPos(-1, 0, -1),
			new BlockPos(-1, 0, 0),
			new BlockPos(-1, 0, 1),
			new BlockPos(0, 0, 1),
			new BlockPos(1, 0, 1),
			new BlockPos(1, 0, 0),
			new BlockPos(1, 0, -1),
			new BlockPos(0, 0, -1),
	};

	private CraftingScenes() {}

	static void apothecary(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("petal_apothecary");
		scene.title("petal_apothecary", PonderStrings.title("petal_apothecary"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos altar = util.grid().at(2, 1, 2);
		scene.world().showSection(util.select().position(altar), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(altar, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(Items.WATER_BUCKET));
		scene.idle(30);
		scene.world().modifyBlock(altar, state -> state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.WATER), false);
		scene.idle(60);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.idle(15);
		for (int slot = 0; slot < 4; slot++) {
			double dx = (slot % 2 - 0.5) * 0.2;
			double dz = (slot / 2 - 0.5) * 0.2;
			ElementLink<EntityElement> petal = scene.world().createItemEntity(util.vector().centerOf(2, 4, 2).add(dx, 0, dz), Vec3.ZERO, new ItemStack(BotaniaItems.whitePetal));
			scene.idle(16);
			scene.world().modifyEntity(petal, Entity::discard);
			int index = slot;
			scene.world().modifyBlockEntity(altar, PetalApothecaryBlockEntity.class, be -> be.getItemHandler().setItem(index, new ItemStack(BotaniaItems.whitePetal)));
			scene.idle(8);
		}
		scene.idle(30);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.idle(15);
		ElementLink<EntityElement> seeds = scene.world().createItemEntity(util.vector().centerOf(2, 4, 2), Vec3.ZERO, new ItemStack(Items.WHEAT_SEEDS));
		scene.idle(18);
		scene.world().modifyEntity(seeds, Entity::discard);
		scene.world().modifyBlockEntity(altar, PetalApothecaryBlockEntity.class, be -> {
			be.getItemHandler().clearContent();
			be.triggerEvent(1, 0);
		});
		scene.world().modifyBlock(altar, state -> state.setValue(PetalApothecaryBlock.FLUID, PetalApothecary.State.EMPTY), false);
		scene.world().createItemEntity(util.vector().topOf(altar).add(0, 0.3, 0), new Vec3(0, 0.25, 0), new ItemStack(BotaniaFlowerBlocks.pureDaisy));
		scene.idle(80);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.idle(90);
	}

	static void pureDaisy(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("pure_daisy");
		scene.title("pure_daisy", PonderStrings.title("pure_daisy"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos daisy = util.grid().at(2, 1, 2);
		scene.world().showSection(util.select().layer(1), Direction.DOWN);
		scene.idle(15);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(daisy)).placeNearTarget();
		scene.idle(90);

		for (int i = 0; i < DAISY_OFFSETS.length; i++) {
			BlockPos target = daisy.offset(DAISY_OFFSETS[i]);
			String key = "ticksRemaining" + i;
			int index = i;
			scene.world().modifyBlockEntityNBT(util.select().position(daisy), PureDaisyBlockEntity.class, tag -> tag.putInt(key, 40));
			scene.idle(22);
			scene.world().setBlock(target, BotaniaBlocks.livingrock.defaultBlockState(), false);
			scene.world().modifyBlockEntity(daisy, PureDaisyBlockEntity.class, be -> be.triggerEvent(0, index));
			scene.world().modifyBlockEntityNBT(util.select().position(daisy), PureDaisyBlockEntity.class, tag -> tag.putInt(key, -1));
			scene.idle(6);
		}
		scene.idle(10);
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(daisy)).placeNearTarget();
		scene.idle(100);
	}

	static void runicAltar(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("runic_altar");
		scene.title("runic_altar", PonderStrings.title("runic_altar"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos altar = util.grid().at(1, 1, 2);
		BlockPos spreader = util.grid().at(3, 1, 2);
		BlockPos pool = util.grid().at(4, 1, 2);

		scene.world().showSection(util.select().position(altar), Direction.DOWN);
		scene.idle(8);
		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(8);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.idle(90);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.idle(10);
		List<ItemStack> ingredients = List.of(
				new ItemStack(BotaniaItems.manaPowder),
				new ItemStack(BotaniaItems.manaSteel),
				new ItemStack(Items.BONE_MEAL),
				new ItemStack(Items.SUGAR_CANE),
				new ItemStack(Items.FISHING_ROD));
		for (int slot = 0; slot < ingredients.size(); slot++) {
			ItemStack stack = ingredients.get(slot);
			int index = slot;
			ElementLink<EntityElement> item = scene.world().createItemEntity(util.vector().centerOf(1, 4, 2), Vec3.ZERO, stack.copy());
			scene.idle(18);
			scene.world().modifyEntity(item, Entity::discard);
			scene.world().modifyBlockEntity(altar, RunicAltarBlockEntity.class, be -> be.getItemHandler().setItem(index, stack.copy()));
			scene.idle(6);
		}
		scene.world().modifyBlockEntityNBT(util.select().position(altar), RunicAltarBlockEntity.class, tag -> tag.putInt("manaToGet", 5200));
		scene.idle(30);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(spreader)).placeNearTarget();
		scene.idle(20);
		Vec3 from = util.vector().centerOf(spreader).add(-0.5, 0, 0);
		Vec3 to = util.vector().centerOf(altar).add(0.5, 0, 0);
		for (int shot = 1; shot <= 4; shot++) {
			SceneHelper.burst(scene, from, to, 12, SceneHelper.MANA_COLOR);
			int mana = shot == 4 ? 5100 : shot * 1300;
			scene.world().modifyBlockEntityNBT(util.select().position(altar), RunicAltarBlockEntity.class, tag -> tag.putInt("mana", mana));
			scene.idle(10);
		}
		scene.idle(20);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(altar)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(altar, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaBlocks.livingrock));
		scene.idle(40);
		scene.world().modifyBlockEntity(altar, RunicAltarBlockEntity.class, be -> {
			be.getItemHandler().clearContent();
			be.triggerEvent(2, 0);
		});
		scene.world().modifyBlockEntityNBT(util.select().position(altar), RunicAltarBlockEntity.class, tag -> {
			tag.putInt("mana", 0);
			tag.putInt("manaToGet", 0);
		});
		scene.world().createItemEntity(util.vector().topOf(altar).add(0, 0.3, 0), new Vec3(0, 0.25, 0), new ItemStack(BotaniaItems.runeWater, 2));
		scene.idle(80);
	}

	static void terraPlate(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("terra_plate");
		scene.title("terra_plate", PonderStrings.title("terra_plate"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos plate = util.grid().at(3, 2, 3);
		BlockPos spreader = util.grid().at(6, 2, 3);
		BlockPos pool = util.grid().at(6, 1, 3);

		scene.world().showSection(util.select().fromTo(2, 1, 2, 4, 1, 4), Direction.DOWN);
		scene.idle(10);
		scene.world().showSection(util.select().position(plate), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(plate)).placeNearTarget();
		scene.idle(90);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(plate)).placeNearTarget();
		scene.idle(10);
		List<ElementLink<EntityElement>> items = new ArrayList<>();
		ItemStack[] stacks = { new ItemStack(BotaniaItems.manaSteel), new ItemStack(BotaniaItems.manaPearl), new ItemStack(BotaniaItems.manaDiamond) };
		double[][] offsets = { { -0.15, -0.1 }, { 0.15, -0.1 }, { 0, 0.15 } };
		for (int i = 0; i < stacks.length; i++) {
			items.add(scene.world().createItemEntity(util.vector().centerOf(3, 5, 3).add(offsets[i][0], 0, offsets[i][1]), Vec3.ZERO, stacks[i]));
			scene.idle(14);
		}
		scene.idle(30);

		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(6);
		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(spreader)).placeNearTarget();
		scene.idle(20);
		Vec3 from = util.vector().centerOf(spreader).add(-0.5, 0, 0);
		Vec3 to = util.vector().centerOf(plate).add(0.5, -0.3, 0);
		for (int shot = 1; shot <= 5; shot++) {
			SceneHelper.burst(scene, from, to, 14, SceneHelper.MANA_COLOR);
			int mana = shot * 100000 - (shot == 5 ? 1 : 0);
			scene.world().modifyBlockEntityNBT(util.select().position(plate), TerrestrialAgglomerationPlateBlockEntity.class, tag -> tag.putInt("mana", mana));
			scene.idle(10);
		}
		scene.idle(20);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(plate)).placeNearTarget();
		for (ElementLink<EntityElement> item : items) {
			scene.world().modifyEntity(item, Entity::discard);
		}
		scene.world().modifyBlockEntityNBT(util.select().position(plate), TerrestrialAgglomerationPlateBlockEntity.class, tag -> tag.putInt("mana", 0));
		SceneHelper.sparkles(scene, util.vector().topOf(plate), 0x00FFC8, 25);
		scene.world().createItemEntity(util.vector().topOf(plate).add(0, 0.3, 0), new Vec3(0, 0.25, 0), new ItemStack(BotaniaItems.terrasteel));
		scene.idle(100);
	}

	static void enchanter(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("mana_enchanter");
		scene.title("mana_enchanter", PonderStrings.title("mana_enchanter"));
		scene.configureBasePlate(0, 0, 11);
		scene.scaleSceneView(0.8F);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos enchanter = util.grid().at(5, 2, 5);
		BlockPos spreader = util.grid().at(9, 2, 5);
		BlockPos pool = util.grid().at(9, 1, 5);
		Selection machine = util.select().layers(1, 3).substract(util.select().fromTo(9, 1, 5, 9, 2, 5));

		scene.world().showSection(util.select().layer(1).substract(util.select().position(pool)), Direction.DOWN);
		scene.idle(10);
		scene.world().showSection(util.select().layer(2).substract(util.select().position(spreader)), Direction.DOWN);
		scene.idle(10);
		scene.world().showSection(util.select().layer(3), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(110).text(text.next()).pointAt(util.vector().topOf(enchanter)).placeNearTarget();
		scene.overlay().showOutline(PonderPalette.BLUE, "enchanter_machine", machine, 60);
		scene.idle(120);

		scene.world().modifyBlockEntity(enchanter, ManaEnchanterBlockEntity.class, be -> be.itemToEnchant = new ItemStack(Items.DIAMOND_PICKAXE));
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(enchanter).add(0, 0.5, 0)).placeNearTarget();
		scene.idle(10);
		List<ElementLink<EntityElement>> books = new ArrayList<>();
		books.add(scene.world().createItemEntity(util.vector().centerOf(3, 4, 5), Vec3.ZERO, new ItemStack(Items.ENCHANTED_BOOK)));
		scene.idle(8);
		books.add(scene.world().createItemEntity(util.vector().centerOf(7, 4, 5), Vec3.ZERO, new ItemStack(Items.ENCHANTED_BOOK)));
		scene.idle(90);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(enchanter)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(enchanter, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(40);
		scene.world().modifyBlockEntity(enchanter, ManaEnchanterBlockEntity.class, be -> {
			be.stage = ManaEnchanterBlockEntity.State.GATHER_MANA;
			be.stageTicks = 0;
		});
		scene.world().modifyBlockEntityNBT(util.select().position(enchanter), ManaEnchanterBlockEntity.class, tag -> {
			tag.putInt("manaRequired", 20000);
			tag.putInt("mana", 0);
		});
		scene.idle(50);

		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(6);
		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(spreader)).placeNearTarget();
		scene.idle(20);
		Vec3 from = util.vector().centerOf(spreader).add(-0.5, 0, 0);
		Vec3 to = util.vector().centerOf(enchanter).add(0.5, 0, 0);
		for (int shot = 1; shot <= 4; shot++) {
			SceneHelper.burst(scene, from, to, 16, SceneHelper.MANA_COLOR);
			int mana = shot * 5000;
			scene.world().modifyBlockEntityNBT(util.select().position(enchanter), ManaEnchanterBlockEntity.class, tag -> tag.putInt("mana", mana));
			scene.idle(10);
		}
		scene.idle(20);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(enchanter).add(0, 0.5, 0)).placeNearTarget();
		for (ElementLink<EntityElement> book : books) {
			scene.world().modifyEntity(book, Entity::discard);
		}
		scene.world().modifyBlockEntity(enchanter, ManaEnchanterBlockEntity.class, be -> {
			ItemStack enchanted = new ItemStack(Items.DIAMOND_PICKAXE);
			enchanted.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
			be.itemToEnchant = enchanted;
			be.stage = ManaEnchanterBlockEntity.State.DO_ENCHANT;
			be.stageTicks = 0;
		});
		scene.idle(100);
		scene.world().modifyBlockEntity(enchanter, ManaEnchanterBlockEntity.class, be -> {
			be.triggerEvent(0, 0);
			be.stage = ManaEnchanterBlockEntity.State.IDLE;
			be.stageTicks = 0;
		});
		scene.idle(60);
	}
}
