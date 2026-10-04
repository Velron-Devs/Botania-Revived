package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.mana.spark.SparkUpgradeType;
import velrondevs.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.registry.BotaniaItems;

final class SparkScenes {
	private SparkScenes() {}

	private static void poolMana(SceneBuilder scene, SceneBuildingUtil util, BlockPos pool, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", mana));
	}

	static void sparks(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("mana_sparks");
		scene.title("mana_sparks", PonderStrings.title("mana_sparks"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos pool = util.grid().at(1, 1, 3);
		BlockPos plate = util.grid().at(5, 1, 3);
		Vec3 poolSpark = util.vector().centerOf(pool).add(0, 0.75, 0);
		Vec3 plateSpark = util.vector().centerOf(plate).add(0, 0.75, 0);

		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(90).text(text.next()).pointAt(util.vector().topOf(pool)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(pool, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.spark));
		scene.idle(30);
		SceneHelper.spark(scene, poolSpark, SparkUpgradeType.NONE);
		scene.idle(70);

		scene.world().showSection(util.select().position(plate), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(plate)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(plate, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.spark));
		scene.idle(30);
		SceneHelper.spark(scene, plateSpark, SparkUpgradeType.NONE);
		scene.idle(40);
		for (int shot = 1; shot <= 5; shot++) {
			SceneHelper.burst(scene, poolSpark, plateSpark, 14, SceneHelper.POOL_COLOR);
			poolMana(scene, util, pool, 600000 - shot * 100000);
			int mana = shot * 100000;
			scene.world().modifyBlockEntityNBT(util.select().position(plate), TerrestrialAgglomerationPlateBlockEntity.class, tag -> tag.putInt("mana", mana));
			scene.idle(6);
		}
		scene.idle(40);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(poolSpark).placeNearTarget();
		scene.overlay().showControls(poolSpark, Pointing.DOWN, 40).rightClick().withItem(new ItemStack(Items.RED_DYE));
		scene.idle(100);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(plateSpark).placeNearTarget();
		scene.overlay().showControls(plateSpark, Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(110);
	}

	static void augments(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("spark_augments");
		scene.title("spark_augments", PonderStrings.title("spark_augments"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos poolLeft = util.grid().at(1, 1, 3);
		BlockPos poolCenter = util.grid().at(3, 1, 3);
		BlockPos poolRight = util.grid().at(5, 1, 3);
		Vec3 leftSpark = util.vector().centerOf(poolLeft).add(0, 0.75, 0);
		Vec3 centerSpark = util.vector().centerOf(poolCenter).add(0, 0.75, 0);
		Vec3 rightSpark = util.vector().centerOf(poolRight).add(0, 0.75, 0);

		poolMana(scene, util, poolCenter, 900000);
		scene.world().showSection(util.select().layer(1), Direction.DOWN);
		scene.idle(15);
		SceneHelper.spark(scene, leftSpark, SparkUpgradeType.NONE);
		ElementLink<EntityElement> center = SceneHelper.spark(scene, centerSpark, SparkUpgradeType.NONE);
		SceneHelper.spark(scene, rightSpark, SparkUpgradeType.NONE);
		scene.idle(15);
		scene.overlay().showText(90).text(text.next()).pointAt(centerSpark).placeNearTarget();
		scene.overlay().showControls(centerSpark, Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.sparkUpgradeRecessive));
		scene.idle(40);
		scene.world().modifyEntity(center, Entity::discard);
		center = SceneHelper.spark(scene, centerSpark, SparkUpgradeType.RECESSIVE);
		scene.idle(60);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(centerSpark).placeNearTarget();
		scene.idle(15);
		for (int step = 1; step <= 3; step++) {
			SceneHelper.flow(scene, centerSpark, new Vec3[] { leftSpark, rightSpark }, 12, SceneHelper.POOL_COLOR);
			int side = step * 150000;
			int middle = 900000 - step * 300000;
			poolMana(scene, util, poolLeft, side);
			poolMana(scene, util, poolRight, side);
			poolMana(scene, util, poolCenter, middle);
			scene.idle(6);
		}
		scene.idle(50);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(centerSpark).placeNearTarget();
		poolMana(scene, util, poolLeft, 450000);
		poolMana(scene, util, poolRight, 450000);
		poolMana(scene, util, poolCenter, 0);
		scene.overlay().showControls(centerSpark, Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.sparkUpgradeDominant));
		scene.idle(40);
		scene.world().modifyEntity(center, Entity::discard);
		center = SceneHelper.spark(scene, centerSpark, SparkUpgradeType.DOMINANT);
		scene.idle(30);
		for (int step = 1; step <= 3; step++) {
			SceneHelper.flow(scene, leftSpark, new Vec3[] { centerSpark }, 12, SceneHelper.POOL_COLOR);
			SceneHelper.flow(scene, rightSpark, new Vec3[] { centerSpark }, 12, SceneHelper.POOL_COLOR);
			int side = 450000 - step * 150000;
			poolMana(scene, util, poolLeft, side);
			poolMana(scene, util, poolRight, side);
			poolMana(scene, util, poolCenter, step * 300000);
			scene.idle(6);
		}
		scene.idle(50);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(centerSpark).placeNearTarget();
		scene.overlay().showControls(centerSpark, Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.sparkUpgradeDispersive));
		scene.idle(110);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(centerSpark).placeNearTarget();
		scene.overlay().showControls(centerSpark, Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.sparkUpgradeIsolated));
		scene.idle(110);
	}
}
