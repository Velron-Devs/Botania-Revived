package velrondevs.botania.client.integration.ponder;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.registry.BotaniaItems;

final class CorporeaScenes {
	private static final int CORPOREA_COLOR = 0xA040FF;

	private CorporeaScenes() {}

	static void network(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("corporea_network");
		scene.title("corporea_network", PonderStrings.title("corporea_network"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos chestFirst = util.grid().at(5, 1, 1);
		BlockPos chestSecond = util.grid().at(5, 1, 5);
		BlockPos block = util.grid().at(1, 1, 1);
		BlockPos index = util.grid().at(1, 1, 5);
		Vec3 firstSpark = util.vector().centerOf(chestFirst).add(0, 0.75, 0);
		Vec3 secondSpark = util.vector().centerOf(chestSecond).add(0, 0.75, 0);
		Vec3 masterSpark = util.vector().centerOf(block).add(0, 0.75, 0);
		Vec3 indexSpark = util.vector().centerOf(index).add(0, 0.75, 0);

		scene.world().modifyBlockEntity(chestFirst, ChestBlockEntity.class, be -> be.setItem(0, new ItemStack(Items.IRON_INGOT, 32)));
		scene.world().modifyBlockEntity(chestSecond, ChestBlockEntity.class, be -> be.setItem(0, new ItemStack(Items.APPLE, 16)));
		scene.world().showSection(util.select().position(chestFirst), Direction.DOWN);
		scene.idle(6);
		scene.world().showSection(util.select().position(chestSecond), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(90).text(text.next()).pointAt(util.vector().topOf(chestFirst)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(chestFirst, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.corporeaSpark));
		scene.idle(30);
		SceneHelper.corporeaSpark(scene, firstSpark, false);
		scene.idle(8);
		SceneHelper.corporeaSpark(scene, secondSpark, false);
		scene.idle(70);

		scene.world().showSection(util.select().position(block), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(block)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(block, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.corporeaSparkMaster));
		scene.idle(30);
		SceneHelper.corporeaSpark(scene, masterSpark, true);
		scene.idle(80);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(masterSpark).placeNearTarget();
		scene.overlay().showLine(PonderPalette.BLUE, masterSpark, firstSpark, 90);
		scene.overlay().showLine(PonderPalette.BLUE, masterSpark, secondSpark, 90);
		scene.overlay().showLine(PonderPalette.BLUE, firstSpark, secondSpark, 90);
		scene.idle(110);

		scene.world().showSection(util.select().position(index), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(index)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(index, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.corporeaSpark));
		scene.idle(30);
		SceneHelper.corporeaSpark(scene, indexSpark, false);
		scene.idle(20);
		scene.overlay().showLine(PonderPalette.BLUE, indexSpark, masterSpark, 80);
		scene.overlay().showLine(PonderPalette.BLUE, indexSpark, secondSpark, 80);
		scene.idle(80);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(index)).placeNearTarget();
		scene.idle(25);
		SceneHelper.path(scene, CORPOREA_COLOR, 12, firstSpark, indexSpark);
		scene.world().createItemEntity(util.vector().topOf(index).add(0.6, 0.6, 0), new Vec3(0.05, 0.25, 0), new ItemStack(Items.IRON_INGOT));
		scene.idle(100);

		scene.overlay().showText(100).text(text.next()).attachKeyFrame().pointAt(indexSpark).placeNearTarget();
		scene.idle(110);
	}

	static void funnel(SceneBuilder scene, SceneBuildingUtil util) {
		PonderText text = new PonderText("corporea_funnel");
		scene.title("corporea_funnel", PonderStrings.title("corporea_funnel"));
		scene.configureBasePlate(0, 0, 7);
		scene.showBasePlate();
		scene.idle(5);

		BlockPos block = util.grid().at(1, 1, 1);
		BlockPos chestStorage = util.grid().at(5, 1, 1);
		BlockPos chestTarget = util.grid().at(3, 1, 3);
		BlockPos funnel = util.grid().at(3, 2, 3);
		BlockPos redstone = util.grid().at(4, 2, 3);
		Vec3 masterSpark = util.vector().centerOf(block).add(0, 0.75, 0);
		Vec3 storageSpark = util.vector().centerOf(chestStorage).add(0, 0.75, 0);
		Vec3 funnelSpark = util.vector().centerOf(funnel).add(0, 0.75, 0);

		scene.world().modifyBlockEntity(chestStorage, ChestBlockEntity.class, be -> be.setItem(0, new ItemStack(Items.IRON_INGOT, 64)));
		scene.world().showSection(util.select().fromTo(1, 1, 1, 5, 1, 1), Direction.DOWN);
		scene.idle(6);
		SceneHelper.corporeaSpark(scene, masterSpark, true);
		scene.idle(4);
		SceneHelper.corporeaSpark(scene, storageSpark, false);
		scene.idle(10);
		scene.world().showSection(util.select().fromTo(3, 1, 3, 3, 2, 3), Direction.DOWN);
		scene.idle(10);
		scene.overlay().showText(100).text(text.next()).pointAt(util.vector().topOf(funnel)).placeNearTarget();
		scene.overlay().showControls(util.vector().blockSurface(funnel, Direction.UP), Pointing.DOWN, 40).rightClick().withItem(new ItemStack(BotaniaItems.corporeaSpark));
		scene.idle(30);
		SceneHelper.corporeaSpark(scene, funnelSpark, false);
		scene.idle(20);
		scene.overlay().showLine(PonderPalette.BLUE, funnelSpark, masterSpark, 70);
		scene.overlay().showLine(PonderPalette.BLUE, funnelSpark, storageSpark, 70);
		scene.idle(80);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().blockSurface(funnel, Direction.NORTH)).placeNearTarget();
		scene.world().createEntity(level -> {
			ItemFrame frame = new ItemFrame(level, funnel.north(), Direction.NORTH);
			frame.setItem(new ItemStack(Items.IRON_INGOT), false);
			return frame;
		});
		scene.idle(120);

		scene.overlay().showText(110).text(text.next()).attachKeyFrame().pointAt(util.vector().topOf(chestTarget)).placeNearTarget();
		scene.idle(10);
		scene.world().setBlock(redstone, Blocks.REDSTONE_BLOCK.defaultBlockState(), true);
		scene.world().modifyBlock(funnel, state -> state.setValue(BlockStateProperties.POWERED, true), false);
		scene.effects().indicateRedstone(redstone);
		scene.idle(15);
		SceneHelper.path(scene, CORPOREA_COLOR, 14, storageSpark, funnelSpark);
		scene.world().modifyBlockEntity(chestTarget, ChestBlockEntity.class, be -> be.setItem(0, new ItemStack(Items.IRON_INGOT)));
		SceneHelper.sparkles(scene, util.vector().topOf(chestTarget), CORPOREA_COLOR, 10);
		scene.idle(40);
		scene.world().setBlock(redstone, Blocks.AIR.defaultBlockState(), false);
		scene.world().modifyBlock(funnel, state -> state.setValue(BlockStateProperties.POWERED, false), false);
		scene.idle(70);
	}
}
