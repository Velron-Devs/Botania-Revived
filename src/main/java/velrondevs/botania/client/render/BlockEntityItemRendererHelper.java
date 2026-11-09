package velrondevs.botania.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.render.block_entity.TEISR;
import velrondevs.botania.client.render.entity.EntityRenderers;

import java.util.function.Consumer;

public class BlockEntityItemRendererHelper implements SpecialModelRenderer<ItemStack> {
	private final TEISR renderer;

	public BlockEntityItemRendererHelper(TEISR renderer) {
		this.renderer = renderer;
	}

	@Override
	public void submit(@Nullable ItemStack argument, PoseStack poseStack, SubmitNodeCollector collector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		if (argument != null) {
			renderer.render(argument, ItemDisplayContext.NONE, poseStack, collector, lightCoords, overlayCoords);
		}
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		output.accept(new Vector3f(0, 0, 0));
		output.accept(new Vector3f(1, 1, 1));
	}

	@Override
	public @Nullable ItemStack extractArgument(ItemStack stack) {
		return stack;
	}

	public record Unbaked(Block block) implements SpecialModelRenderer.Unbaked<ItemStack> {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(Unbaked::block)
		).apply(i, Unbaked::new));

		@Override
		public @Nullable SpecialModelRenderer<ItemStack> bake(SpecialModelRenderer.BakingContext context) {
			var factory = EntityRenderers.BE_ITEM_RENDERER_FACTORIES.get(block);
			return factory == null ? null : new BlockEntityItemRendererHelper(factory.apply(block));
		}

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
