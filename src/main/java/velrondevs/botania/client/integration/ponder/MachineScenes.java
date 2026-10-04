package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.brew.BrewContainer;
import velrondevs.botania.api.state.BotaniaStateProperties;
import velrondevs.botania.common.block.block_entity.BreweryBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaPrismBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaBrews;
import velrondevs.botania.registry.BotaniaItems;

final class MachineScenes {
	private static final int WHITE = 0xFFFFFF;

	private MachineScenes() {}

	private static void insertLens(SceneBuilder scene, SceneBuildingUtil util, BlockPos spreader, Item lens) {
		scene.overlay().showControls(util.vector().blockSurface(spreader, Direction.UP), Pointing.DOWN, 30).rightClick().withItem(new ItemStack(lens));
		scene.idle(20);
		scene.world().modifyBlockEntity(spreader, ManaSpreaderBlockEntity.class, be -> be.getItemHandler().setItem(0, new ItemStack(lens)));
		scene.idle(15);
	}

	private static void poolMana(SceneBuilder scene, SceneBuildingUtil util, BlockPos pool, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", mana));
	}

	static void lenses(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("lenses");
		scene.title("lenses", PonderStrings.title("lenses"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spreader = util.grid().at(1, 1, 1);
		BlockPos pool = util.grid().at(5, 1, 1);
		BlockPos poolBounce = util.grid().at(1, 1, 6);
		Selection wall = util.select().fromTo(4, 1, 2, 4, 1, 6);
		Vec3 spreaderCenter = util.vector().centerOf(spreader);
		Vec3 from = spreaderCenter.add(0.5, 0, 0);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);

		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(8);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(90).text(text.next()).pointAt(util.vector().topOf(spreader)).placeNearTarget();
		insertLens(scene, util, spreader, BotaniaItems.lensNormal);
		scene.idle(50);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		insertLens(scene, util, spreader, BotaniaItems.lensSpeed);
		SceneHelper.burst(scene, from, to, 5, SceneHelper.MANA_COLOR);
		poolMana(scene, util, pool, 100000);
		scene.idle(60);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		insertLens(scene, util, spreader, BotaniaItems.lensPower);
		SceneHelper.burst(scene, from, to, 24, SceneHelper.MANA_COLOR);
		poolMana(scene, util, pool, 300000);
		scene.idle(50);

		scene.world().showSection(wall, Direction.DOWN);
		scene.idle(6);
		scene.world().showSection(util.select().position(poolBounce), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(util.grid().at(4, 1, 4))).placeNearTarget();
		insertLens(scene, util, spreader, BotaniaItems.lensBounce);
		for (int i = 1; i <= 5; i++) {
			float rotation = 180F - i * 9F;
			scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putFloat("rotationX", rotation));
			scene.idle(2);
		}
		scene.idle(10);
		Vec3 hit = new Vec3(4.0, spreaderCenter.y, 4.0);
		SceneHelper.path(scene, SceneHelper.MANA_COLOR, 12, spreaderCenter, hit, util.vector().centerOf(poolBounce));
		poolMana(scene, util, poolBounce, 120000);
		scene.idle(60);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		scene.idle(120);
	}

	static void prism(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("mana_prism");
		scene.title("mana_prism", PonderStrings.title("mana_prism"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spreader = util.grid().at(1, 1, 3);
		BlockPos prism = util.grid().at(3, 1, 3);
		BlockPos pool = util.grid().at(5, 1, 3);
		BlockPos redstone = util.grid().at(3, 1, 2);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 prismCenter = util.vector().centerOf(prism);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);

		scene.world().showSection(util.select().layer(1), Direction.DOWN);
		scene.idle(15);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(prism)).placeNearTarget();
		scene.idle(20);
		SceneHelper.path(scene, SceneHelper.MANA_COLOR, 10, from, prismCenter, to);
		poolMana(scene, util, pool, 100000);
		scene.idle(50);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(prism)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(prism, Direction.UP), Pointing.DOWN, 30).rightClick().withItem(new ItemStack(BotaniaItems.lensSpeed));
		scene.idle(25);
		scene.world().modifyBlockEntity(prism, ManaPrismBlockEntity.class, be -> be.getItemHandler().setItem(0, new ItemStack(BotaniaItems.lensSpeed)));
		scene.world().modifyBlock(prism, state -> state.setValue(BotaniaStateProperties.HAS_LENS, true), false);
		scene.idle(20);
		SceneHelper.burst(scene, from, prismCenter, 10, SceneHelper.MANA_COLOR);
		SceneHelper.burst(scene, prismCenter, to, 5, WHITE);
		poolMana(scene, util, pool, 200000);
		scene.idle(60);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(prism)).placeNearTarget();
		scene.idle(10);
		scene.world().setBlock(redstone, Blocks.REDSTONE_BLOCK.defaultBlockState(), true);
		scene.world().modifyBlock(prism, state -> state.setValue(BlockStateProperties.POWERED, true), false);
		scene.effects().indicateRedstone(redstone);
		scene.idle(30);
		SceneHelper.path(scene, SceneHelper.MANA_COLOR, 10, from, prismCenter, to);
		poolMana(scene, util, pool, 300000);
		scene.idle(70);
	}

	static void detector(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("mana_detector");
		scene.title("mana_detector", PonderStrings.title("mana_detector"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spreader = util.grid().at(1, 1, 3);
		BlockPos detector = util.grid().at(3, 1, 3);
		BlockPos pool = util.grid().at(5, 1, 3);
		BlockPos lamp = util.grid().at(3, 1, 2);
		Vec3 from = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 detectorCenter = util.vector().centerOf(detector);
		Vec3 to = util.vector().centerOf(pool).add(-0.5, 0, 0);

		scene.world().showSection(util.select().layer(1), Direction.DOWN);
		scene.idle(15);
		scene.overlay().showText(90).text(text.next()).pointAt(util.vector().topOf(detector)).placeNearTarget();
		scene.idle(100);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(lamp)).placeNearTarget();
		for (int shot = 1; shot <= 2; shot++) {
			SceneHelper.burst(scene, from, detectorCenter, 10, SceneHelper.MANA_COLOR);
			scene.world().modifyBlock(detector, state -> state.setValue(BlockStateProperties.POWERED, true), false);
			scene.world().modifyBlock(lamp, state -> state.setValue(BlockStateProperties.LIT, true), false);
			scene.effects().indicateRedstone(detector);
			SceneHelper.sparkles(scene, util.vector().topOf(detector), 0xFF3333, 8);
			SceneHelper.burst(scene, detectorCenter, to, 10, SceneHelper.MANA_COLOR);
			poolMana(scene, util, pool, shot * 100000);
			scene.idle(4);
			scene.world().modifyBlock(detector, state -> state.setValue(BlockStateProperties.POWERED, false), false);
			scene.world().modifyBlock(lamp, state -> state.setValue(BlockStateProperties.LIT, false), false);
			scene.idle(30);
		}

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(detector)).placeNearTarget();
		scene.idle(100);
	}

	static void brewery(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("botanical_brewery");
		scene.title("botanical_brewery", PonderStrings.title("botanical_brewery"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos brewery = util.grid().at(1, 1, 3);
		BlockPos spreader = util.grid().at(3, 1, 3);
		BlockPos pool = util.grid().at(4, 1, 3);

		scene.world().showSection(util.select().position(brewery), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(brewery)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(brewery, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.vial));
		scene.idle(30);
		scene.world().modifyBlockEntity(brewery, BreweryBlockEntity.class, be -> be.getItemHandler().setItem(0, new ItemStack(BotaniaItems.vial)));
		scene.idle(60);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(brewery)).placeNearTarget();
		scene.idle(15);
		ItemStack[] ingredients = { new ItemStack(Items.NETHER_WART), new ItemStack(Items.SUGAR), new ItemStack(Items.REDSTONE) };
		for (int slot = 0; slot < ingredients.length; slot++) {
			ItemStack stack = ingredients[slot];
			int index = slot + 1;
			ElementLink<EntityElement> item = scene.world().createItemEntity(util.vector().centerOf(1, 4, 3), Vec3.ZERO, stack.copy());
			scene.idle(18);
			scene.world().modifyEntity(item, Entity::discard);
			scene.world().modifyBlockEntity(brewery, BreweryBlockEntity.class, be -> be.getItemHandler().setItem(index, stack.copy()));
			scene.idle(8);
		}
		scene.idle(40);

		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(6);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(spreader)).placeNearTarget();
		scene.idle(20);
		Vec3 from = util.vector().centerOf(spreader).add(-0.5, 0, 0);
		Vec3 to = util.vector().centerOf(brewery).add(0.5, 0, 0);
		for (int shot = 1; shot <= 4; shot++) {
			SceneHelper.burst(scene, from, to, 8, SceneHelper.MANA_COLOR);
			SceneHelper.sparkles(scene, util.vector().topOf(brewery), 0x59B7FF, 6);
			scene.idle(10);
		}
		scene.idle(20);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(brewery)).placeNearTarget();
		scene.world().modifyBlockEntity(brewery, BreweryBlockEntity.class, be -> {
			be.getItemHandler().clearContent();
			be.triggerEvent(0, 0x59B7FF);
		});
		ItemStack brew = ((BrewContainer) BotaniaItems.vial).getItemForBrew(BotaniaBrews.speed, new ItemStack(BotaniaItems.vial));
		scene.world().createItemEntity(util.vector().topOf(brewery).add(0, 0.5, 0), new Vec3(0, 0.25, 0), brew);
		scene.idle(100);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(brewery)).placeNearTarget();
		scene.idle(100);
	}

	static void catalysts(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("catalysts");
		scene.title("catalysts", PonderStrings.title("catalysts"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos catalyst = util.grid().at(2, 1, 2);
		BlockPos pool = util.grid().at(2, 2, 2);

		scene.world().showSection(util.select().fromTo(2, 1, 2, 2, 2, 2), Direction.DOWN);
		scene.idle(12);
		scene.overlay().showText(90).text(text.next()).pointAt(util.vector().blockSurface(catalyst, Direction.NORTH)).placeNearTarget();
		scene.idle(20);
		ElementLink<EntityElement> cobble = scene.world().createItemEntity(util.vector().centerOf(2, 5, 2), Vec3.ZERO, new ItemStack(Items.COBBLESTONE));
		scene.idle(22);
		scene.world().modifyEntity(cobble, Entity::discard);
		scene.world().modifyBlockEntity(pool, ManaPoolBlockEntity.class, be -> be.triggerEvent(0, 0));
		poolMana(scene, util, pool, 499950);
		scene.world().createItemEntity(util.vector().topOf(pool).add(0, 0.3, 0), new Vec3(0, 0.25, 0), new ItemStack(Items.SAND));
		scene.idle(90);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().blockSurface(catalyst, Direction.NORTH)).placeNearTarget();
		scene.world().setBlock(catalyst, BotaniaBlocks.conjurationCatalyst.defaultBlockState(), true);
		scene.idle(25);
		ElementLink<EntityElement> coal = scene.world().createItemEntity(util.vector().centerOf(2, 5, 2), Vec3.ZERO, new ItemStack(Items.COAL));
		scene.idle(22);
		scene.world().modifyEntity(coal, Entity::discard);
		scene.world().modifyBlockEntity(pool, ManaPoolBlockEntity.class, be -> be.triggerEvent(0, 0));
		poolMana(scene, util, pool, 497850);
		scene.world().createItemEntity(util.vector().topOf(pool).add(0, 0.3, 0), new Vec3(0, 0.25, 0), new ItemStack(Items.COAL, 2));
		scene.idle(90);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(pool)).placeNearTarget();
		scene.idle(100);
	}
}
