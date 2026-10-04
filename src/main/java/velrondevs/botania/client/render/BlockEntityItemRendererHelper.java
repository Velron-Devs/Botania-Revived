package velrondevs.botania.client.render;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import velrondevs.botania.client.render.block_entity.TEISR;
import velrondevs.botania.client.render.entity.EntityRenderers;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BlockEntityItemRendererHelper {

	private static final BlockEntityWithoutLevelRenderer RENDERER = new BlockEntityWithoutLevelRenderer(null, null) {
		private final Map<Item, TEISR> renderers = new IdentityHashMap<>();

		@Override
		public void renderByItem(ItemStack stack, ItemDisplayContext transform,
				PoseStack ps, MultiBufferSource buffers, int light, int overlay) {
			var renderer = renderers.computeIfAbsent(stack.getItem(), i -> {
				var block = Block.byItem(i);
				return EntityRenderers.BE_ITEM_RENDERER_FACTORIES.get(block).apply(block);
			});
			renderer.render(stack, transform, ps, buffers, light, overlay);
		}
	};

	public static final IClientItemExtensions PROPS = new IClientItemExtensions() {
		@Override
		public BlockEntityWithoutLevelRenderer getCustomRenderer() {
			return BlockEntityItemRendererHelper.RENDERER;
		}
	};

	public static void initItem(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(PROPS);
	}
}
