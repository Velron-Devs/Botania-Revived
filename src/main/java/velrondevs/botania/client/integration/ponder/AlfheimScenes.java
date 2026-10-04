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
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.state.BotaniaStateProperties;
import velrondevs.botania.api.state.enums.AlfheimPortalState;
import velrondevs.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import velrondevs.botania.registry.BotaniaItems;

final class AlfheimScenes {
	private AlfheimScenes() {}

	private static AlfheimPortalState openState(SceneBuilder scene, BlockPos core) {
		try {
			Rotation rotation = AlfheimPortalBlockEntity.MULTIBLOCK.get().validate(scene.getScene().getWorld(), core);
			if (rotation == Rotation.CLOCKWISE_90 || rotation == Rotation.COUNTERCLOCKWISE_90) {
				return AlfheimPortalState.ON_X;
			}
		} catch (RuntimeException ignored) {}
		return AlfheimPortalState.ON_Z;
	}

	static void portal(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("alfheim_portal");
		scene.title("alfheim_portal", PonderStrings.title("alfheim_portal"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos core = util.grid().at(2, 1, 3);
		AlfheimPortalState open = openState(scene, core);

		scene.world().showSection(util.select().fromTo(0, 1, 3, 4, 5, 3), Direction.DOWN);
		scene.idle(15);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(core)).placeNearTarget();
		scene.idle(90);

		scene.world().showSection(util.select().fromTo(1, 1, 1, 3, 2, 1), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(util.grid().at(1, 2, 1))).placeNearTarget();
		scene.idle(100);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(core)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(core, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(40);
		scene.world().modifyBlock(core, state -> state.setValue(BotaniaStateProperties.ALFPORTAL_STATE, open), false);
		scene.idle(110);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(3, 3, 3)).placeNearTarget();
		scene.idle(10);
		ElementLink<EntityElement> first = scene.world().createItemEntity(util.vector().centerOf(2, 6, 3).add(-0.15, 0, 0), Vec3.ZERO, new ItemStack(BotaniaItems.manaSteel));
		ElementLink<EntityElement> second = scene.world().createItemEntity(util.vector().centerOf(2, 6, 3).add(0.15, 0, 0), Vec3.ZERO, new ItemStack(BotaniaItems.manaSteel));
		scene.idle(22);
		scene.world().modifyEntity(first, Entity::discard);
		scene.world().modifyEntity(second, Entity::discard);
		SceneHelper.sparkles(scene, util.vector().centerOf(2, 3, 3), 0x00FF55, 30);
		scene.idle(25);
		scene.world().createItemEntity(util.vector().centerOf(2, 2, 3), new Vec3(0, 0.2, 0.15), new ItemStack(BotaniaItems.elementium));
		scene.idle(80);
	}

	static void gaiaRitual(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("gaia_ritual");
		scene.title("gaia_ritual", PonderStrings.title("gaia_ritual"));
		scene.configureBasePlate(0, 0, 9);
		scene.scaleSceneView(0.8F);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos beacon = util.grid().at(4, 2, 4);
		BlockPos[] pylons = { util.grid().at(0, 3, 0), util.grid().at(8, 3, 0), util.grid().at(0, 3, 8), util.grid().at(8, 3, 8) };

		scene.world().showSection(util.select().fromTo(3, 1, 3, 5, 1, 5), Direction.DOWN);
		scene.idle(8);
		scene.world().showSection(util.select().position(beacon), Direction.DOWN);
		scene.idle(8);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(beacon)).placeNearTarget();
		scene.idle(90);

		scene.world().showSection(util.select().fromTo(0, 1, 0, 8, 4, 8).substract(util.select().fromTo(3, 1, 3, 5, 2, 5)), Direction.DOWN);
		scene.idle(15);
		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(pylons[0])).placeNearTarget();
		scene.idle(100);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().centerOf(beacon)).placeNearTarget();
		scene.idle(90);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(beacon)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(beacon, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.lifeEssence));
		scene.idle(45);
		for (int tick = 0; tick < 40; tick++) {
			for (BlockPos pylon : pylons) {
				SceneHelper.wisp(scene, util.vector().centerOf(pylon).lerp(util.vector().centerOf(beacon), tick / 40.0), SceneHelper.GAIA_COLOR);
			}
			scene.idle(1);
		}
		SceneHelper.sparkles(scene, util.vector().topOf(beacon), SceneHelper.GAIA_COLOR, 30);
		scene.idle(60);
	}
}
