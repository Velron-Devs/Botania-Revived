package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.common.block.flower.generating.EndoflameBlockEntity;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

final class FlowerScenes {
	private FlowerScenes() {}

	private static void poolMana(SceneBuilder scene, SceneBuildingUtil util, BlockPos pool, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", mana));
	}

	static void generating(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("generating_flowers");
		scene.title("generating_flowers", PonderStrings.title("generating_flowers"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos flower = util.grid().at(1, 1, 3);
		BlockPos spreader = util.grid().at(3, 1, 3);
		BlockPos pool = util.grid().at(5, 1, 3);
		BlockPos waterNorth = util.grid().at(1, 1, 2);
		BlockPos waterSouth = util.grid().at(1, 1, 4);
		Vec3 flowerCenter = util.vector().centerOf(flower);
		Vec3 spreaderCenter = util.vector().centerOf(spreader);
		Vec3 poolCenter = util.vector().centerOf(pool);

		scene.world().showSection(util.select().position(flower), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(90).text(text.next()).pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(100);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(15);
		ElementLink<EntityElement> coal = scene.world().createItemEntity(util.vector().centerOf(1, 4, 3), Vec3.ZERO, new ItemStack(Items.COAL));
		scene.idle(20);
		scene.world().modifyEntity(coal, Entity::discard);
		scene.world().modifyBlockEntityNBT(util.select().position(flower), EndoflameBlockEntity.class, tag -> tag.putInt("burnTime", 3000));
		SceneHelper.sparkles(scene, util.vector().topOf(flower), 0xFF8800, 12);
		scene.idle(60);

		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(8);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(flower, Direction.UP), Pointing.DOWN, 25).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(30);
		scene.overlay().showControls(util.vector().blockSurface(spreader, Direction.UP), Pointing.DOWN, 25).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.overlay().showLine(PonderPalette.GREEN, flowerCenter, spreaderCenter, 60);
		scene.idle(20);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.idle(40);
		for (int shot = 1; shot <= 3; shot++) {
			SceneHelper.burst(scene, spreaderCenter.add(0.5, 0, 0), poolCenter.add(-0.5, 0, 0), 12, SceneHelper.MANA_COLOR);
			poolMana(scene, util, pool, shot * 60000);
			scene.idle(8);
		}
		scene.idle(40);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.world().setBlock(waterNorth, Blocks.WATER.defaultBlockState(), true);
		scene.world().setBlock(waterSouth, Blocks.WATER.defaultBlockState(), true);
		scene.idle(15);
		scene.world().setBlock(flower, BotaniaFlowerBlocks.hydroangeas.defaultBlockState(), true);
		scene.idle(20);
		for (int shot = 4; shot <= 5; shot++) {
			SceneHelper.sparkles(scene, util.vector().topOf(flower), 0x3366FF, 10);
			SceneHelper.burst(scene, spreaderCenter.add(0.5, 0, 0), poolCenter.add(-0.5, 0, 0), 12, SceneHelper.MANA_COLOR);
			poolMana(scene, util, pool, shot * 60000);
			scene.idle(12);
		}
		scene.idle(50);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(120);
	}

	static void functional(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("functional_flowers");
		scene.title("functional_flowers", PonderStrings.title("functional_flowers"));
		scene.configureBasePlate(0, 0, 11);
		scene.scaleSceneView(0.7F);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos flower = util.grid().at(5, 1, 5);
		BlockPos spreader = util.grid().at(9, 1, 5);
		BlockPos pool = util.grid().at(10, 1, 5);
		int[][] stones = { { 2, 3 }, { 3, 8 }, { 7, 2 }, { 8, 8 }, { 1, 5 }, { 5, 9 } };
		Block[] ores = { Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.COPPER_ORE };

		scene.world().showSection(util.select().position(flower), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(110);

		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(8);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		scene.idle(20);
		Vec3 from = util.vector().centerOf(spreader).add(-0.5, 0, 0);
		Vec3 to = util.vector().centerOf(flower).add(0.5, 0, 0);
		for (int shot = 0; shot < 2; shot++) {
			SceneHelper.burst(scene, from, to, 16, SceneHelper.MANA_COLOR);
			scene.idle(10);
		}
		scene.idle(40);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(flower, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.overlay().showOutline(PonderPalette.GREEN, "orechid_range", util.select().fromTo(0, 1, 0, 10, 1, 10), 110);
		scene.idle(120);

		for (int[] stone : stones) {
			scene.world().showSection(util.select().position(stone[0], 1, stone[1]), Direction.DOWN);
			scene.idle(3);
		}
		scene.idle(10);
		scene.overlay().showText(120).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(20);
		for (int i = 0; i < stones.length; i++) {
			BlockPos target = util.grid().at(stones[i][0], 1, stones[i][1]);
			SceneHelper.burst(scene, from, to, 8, SceneHelper.MANA_COLOR);
			SceneHelper.path(scene, 0xFF4FD8, 6, util.vector().topOf(flower), util.vector().centerOf(target));
			scene.world().setBlock(target, ores[i].defaultBlockState(), true);
			SceneHelper.sparkles(scene, util.vector().topOf(target), 0xFF4FD8, 8);
			scene.idle(10);
		}
		scene.idle(40);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(120);
	}
}
