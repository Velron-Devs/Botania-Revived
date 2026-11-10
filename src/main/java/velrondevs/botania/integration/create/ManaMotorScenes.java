package velrondevs.botania.integration.create;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;

import net.createmod.catnip.api.math.Pointing;
import net.createmod.ponder.api.client.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.client.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.api.client.scene.SceneBuilder;
import net.createmod.ponder.api.client.scene.SceneBuildingUtil;
import net.createmod.ponder.api.client.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.mana.spark.SparkUpgradeType;
import velrondevs.botania.client.integration.ponder.BotaniaPonderTags;
import velrondevs.botania.client.integration.ponder.PonderStrings;
import velrondevs.botania.client.integration.ponder.PonderText;
import velrondevs.botania.client.integration.ponder.SceneHelper;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.registry.BotaniaItems;

public final class ManaMotorScenes {
	private static final String SCENE = "mana_motor";

	private ManaMotorScenes() {}

	public static void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
		helper.addStoryBoard(CreateIntegration.MANA_MOTOR.getId(), SCENE, ManaMotorScenes::manaMotor, BotaniaPonderTags.MANA_NETWORK);
	}

	public static void registerTags(PonderTagRegistrationHelper<Identifier> helper) {
		helper.addToTag(AllCreatePonderTags.KINETIC_SOURCES).add(CreateIntegration.MANA_MOTOR.getId());
	}

	private static void motorState(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos motor, int mana, boolean active) {
		scene.world().modifyBlockEntityNBT(util.select().position(motor), ManaMotorBlockEntity.class, tag -> {
			tag.putInt("mana", mana);
			tag.putBoolean("manaActive", active);
		});
	}

	private static void poolMana(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pool, int mana) {
		scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", mana));
	}

	static void manaMotor(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		PonderText text = new PonderText(SCENE);
		scene.title(SCENE, PonderStrings.title(SCENE));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos spreader = util.grid().at(0, 1, 3);
		BlockPos motor = util.grid().at(2, 1, 3);
		BlockPos pool = util.grid().at(2, 1, 5);
		BlockPos lowerCog = util.grid().at(5, 1, 3);
		BlockPos upperCog = util.grid().at(5, 2, 3);
		Selection drive = util.select().fromTo(2, 1, 3, 5, 1, 3);
		Selection relay = util.select().fromTo(3, 1, 3, 5, 1, 3);
		Selection upper = util.select().position(upperCog);
		Selection kinetics = drive.copy().add(upper);
		Vec3 burstFrom = util.vector().centerOf(spreader).add(0.5, 0, 0);
		Vec3 burstTo = util.vector().centerOf(motor).add(-0.5, 0, 0);
		Vec3 motorSpark = util.vector().centerOf(motor).add(0, 0.75, 0);
		Vec3 poolSpark = util.vector().centerOf(pool).add(0, 0.75, 0);
		Vec3 panel = util.vector().blockSurface(motor, Direction.NORTH).add(-1 / 16f, 0, 3 / 16f);

		scene.world().showSection(util.select().position(motor), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(motor)).placeNearTarget();
		scene.idle(20);
		scene.world().showSection(relay, Direction.WEST);
		scene.idle(8);
		scene.world().showSection(upper, Direction.DOWN);
		scene.idle(62);

		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		scene.idle(15);
		for (int shot = 1; shot <= 4; shot++) {
			SceneHelper.burst(scene, burstFrom, burstTo, 10, SceneHelper.MANA_COLOR);
			motorState(scene, util, motor, shot * 2000, false);
			scene.idle(6);
		}
		scene.idle(10);

		motorState(scene, util, motor, 8000, true);
		scene.world().setKineticSpeed(drive, 32);
		scene.world().setKineticSpeed(upper, -32);
		scene.effects().rotationSpeedIndicator(motor);
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(lowerCog)).placeNearTarget();
		for (int shot = 0; shot < 5; shot++) {
			SceneHelper.burst(scene, burstFrom, burstTo, 10, SceneHelper.MANA_COLOR);
			scene.idle(8);
		}

		poolMana(scene, util, pool, 600000);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(pool)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(motor, Direction.UP), Pointing.DOWN, 30).rightClick().withItem(new ItemStack(BotaniaItems.spark));
		scene.idle(20);
		SceneHelper.spark(scene, motorSpark, SparkUpgradeType.NONE);
		scene.idle(15);
		scene.overlay().showControls(util.vector().blockSurface(pool, Direction.UP), Pointing.DOWN, 30).rightClick().withItem(new ItemStack(BotaniaItems.spark));
		scene.idle(20);
		SceneHelper.spark(scene, poolSpark, SparkUpgradeType.NONE);
		scene.idle(15);
		for (int shot = 1; shot <= 4; shot++) {
			SceneHelper.burst(scene, poolSpark, motorSpark, 12, SceneHelper.POOL_COLOR);
			poolMana(scene, util, pool, 600000 - shot * 50000);
			scene.idle(4);
		}
		scene.idle(20);

		scene.overlay().showFilterSlotInput(panel, Direction.NORTH, 80);
		scene.overlay().showControls(panel, Pointing.DOWN, 50).rightClick();
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(panel).placeNearTarget();
		scene.idle(40);
		scene.world().modifyBlockEntityNBT(util.select().position(motor), ManaMotorBlockEntity.class, tag -> tag.putInt("ScrollValue", 64));
		scene.world().multiplyKineticSpeed(kinetics, 2);
		scene.effects().rotationSpeedIndicator(motor);
		scene.idle(50);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(upperCog)).placeNearTarget();
		for (int shot = 1; shot <= 5; shot++) {
			SceneHelper.burst(scene, poolSpark, motorSpark, 12, SceneHelper.POOL_COLOR);
			poolMana(scene, util, pool, 400000 - shot * 80000);
			scene.idle(8);
		}
		scene.idle(10);

		motorState(scene, util, motor, 0, false);
		scene.world().setKineticSpeed(kinetics, 0);
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(motor)).placeNearTarget();
		scene.idle(60);
		for (int shot = 1; shot <= 2; shot++) {
			SceneHelper.burst(scene, burstFrom, burstTo, 10, SceneHelper.MANA_COLOR);
			motorState(scene, util, motor, shot * 2000, false);
			scene.idle(6);
		}
		motorState(scene, util, motor, 4000, true);
		scene.world().setKineticSpeed(drive, 64);
		scene.world().setKineticSpeed(upper, -64);
		scene.effects().rotationSpeedIndicator(motor);
		scene.idle(50);
	}
}
