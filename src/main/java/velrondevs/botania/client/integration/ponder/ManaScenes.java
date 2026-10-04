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
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.common.block.flower.generating.EndoflameBlockEntity;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

final class ManaScenes {
	private ManaScenes() {}

	static void spreader(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("mana_spreader");
		scene.title("mana_spreader", PonderStrings.title("mana_spreader"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos flower = util.grid().at(0, 1, 2);
		BlockPos spreader = util.grid().at(2, 1, 2);
		BlockPos pool = util.grid().at(4, 1, 2);
		Vec3 spreaderCenter = util.vector().centerOf(spreader);
		Vec3 poolCenter = util.vector().centerOf(pool);

		scene.world().showSection(util.select().position(flower), Direction.DOWN);
		scene.world().modifyBlockEntityNBT(util.select().position(flower), EndoflameBlockEntity.class, tag -> tag.putInt("burnTime", 6000));
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(flower)).placeNearTarget();
		scene.idle(90);

		scene.world().showSection(util.select().position(spreader), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showControls(util.vector().blockSurface(flower, Direction.UP), Pointing.DOWN, 25).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.idle(30);
		scene.overlay().showControls(util.vector().blockSurface(spreader, Direction.UP), Pointing.DOWN, 25).rightClick().withItem(new ItemStack(BotaniaItems.twigWand));
		scene.overlay().showText(70).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		scene.overlay().showLine(PonderPalette.GREEN, util.vector().centerOf(flower), spreaderCenter, 60);
		scene.idle(20);
		scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putInt("mana", 1000));
		scene.idle(60);

		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(60).text(text.next()).attachKeyFrame().pointAt(spreaderCenter.add(0.5, 0.2, 0)).placeNearTarget();
		scene.idle(20);
		for (int shot = 1; shot <= 4; shot++) {
			SceneHelper.burst(scene, spreaderCenter.add(0.5, 0, 0), poolCenter.add(-0.5, 0, 0), 12, SceneHelper.MANA_COLOR);
			int mana = shot * 120000;
			scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", mana));
			scene.idle(8);
		}
		scene.overlay().showText(70).text(text.next()).pointAt(util.vector().topOf(pool)).placeNearTarget();
		scene.idle(80);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(spreader)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(spreader, Direction.NORTH), Pointing.RIGHT, 40).rightClick().whileSneaking().withItem(new ItemStack(BotaniaItems.twigWand));
		for (int i = 1; i <= 8; i++) {
			float rotation = 180F - i * 4F;
			scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putFloat("rotationX", rotation));
			scene.idle(2);
		}
		scene.idle(10);
		for (int i = 1; i <= 8; i++) {
			float rotation = 148F + i * 4F;
			scene.world().modifyBlockEntityNBT(util.select().position(spreader), ManaSpreaderBlockEntity.class, tag -> tag.putFloat("rotationX", rotation));
			scene.idle(2);
		}
		scene.idle(40);
	}

	static void pool(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("mana_pool");
		scene.title("mana_pool", PonderStrings.title("mana_pool"));
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos pool = util.grid().at(2, 1, 2);
		scene.world().showSection(util.select().position(pool), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(80).text(text.next()).pointAt(util.vector().topOf(pool)).placeNearTarget();
		scene.idle(90);

		scene.overlay().showText(90).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(pool)).placeNearTarget();
		scene.idle(15);
		ElementLink<EntityElement> iron = scene.world().createItemEntity(util.vector().centerOf(2, 4, 2), Vec3.ZERO, new ItemStack(Items.IRON_INGOT));
		scene.idle(22);
		scene.world().modifyEntity(iron, Entity::discard);
		scene.world().modifyBlockEntity(pool, ManaPoolBlockEntity.class, be -> be.triggerEvent(0, 0));
		scene.world().modifyBlockEntityNBT(util.select().position(pool), ManaPoolBlockEntity.class, tag -> tag.putInt("mana", 240000));
		scene.world().createItemEntity(util.vector().topOf(pool).add(0, 0.3, 0), new Vec3(0, 0.25, 0), new ItemStack(BotaniaItems.manaSteel));
		scene.idle(70);

		scene.overlay().showText(80).text(text.next()).attachKeyFrame().pointAt(util.vector().blockSurface(util.grid().at(2, 0, 2), Direction.UP)).placeNearTarget();
		scene.idle(10);
		scene.world().setBlock(util.grid().at(2, 0, 2), BotaniaBlocks.alchemyCatalyst.defaultBlockState(), true);
		scene.idle(90);
	}
}
