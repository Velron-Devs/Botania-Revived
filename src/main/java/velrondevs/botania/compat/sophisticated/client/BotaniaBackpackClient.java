package velrondevs.botania.compat.sophisticated.client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;

import velrondevs.botania.compat.sophisticated.backpack.BackpackColors;
import velrondevs.botania.compat.sophisticated.backpack.BotaniaBackpackRegistry;

import java.util.List;

public final class BotaniaBackpackClient {
	private BotaniaBackpackClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaBackpackClient::registerBlockColors);
	}

	private static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
		event.register(List.of(new BackpackTint(true), new BackpackTint(false)), BotaniaBackpackRegistry.blocks().toArray(new Block[0]));
	}

	private record BackpackTint(boolean main) implements BlockTintSource {
		@Override
		public int color(BlockState state) {
			return main ? BackpackColors.DEFAULT_MAIN : BackpackColors.DEFAULT_ACCENT;
		}

		@Override
		public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
			return WorldHelper.getBlockEntity(level, pos, BackpackBlockEntity.class)
					.map(be -> main ? be.getMainColor() : be.getAccentColor())
					.orElse(color(state));
		}
	}
}
