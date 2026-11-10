package velrondevs.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.handler.RenderLexicon;
import velrondevs.botania.xplat.BotaniaConfig;

import java.util.function.Consumer;

public class LexiconItemModel implements ItemModel {
	private final ItemModel model;

	public LexiconItemModel(ItemModel model) {
		this.model = model;
	}

	@Override
	public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext,
			@Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		if (displayContext.firstPerson() && BotaniaConfig.client().lexicon3dModel()) {
			output.appendModelIdentityElement(this);
			output.setAnimated();
			ItemStackRenderState.LayerRenderState layer = output.newLayer();
			layer.setupSpecialModel(HandRenderer.INSTANCE, new HandState(item, displayContext));
			return;
		}
		model.update(output, item, resolver, displayContext, level, owner, seed);
	}

	private record HandState(ItemStack stack, ItemDisplayContext displayContext) {}

	private enum HandRenderer implements SpecialModelRenderer<HandState> {
		INSTANCE;

		@Override
		public void submit(@Nullable HandState argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
				int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
			if (argument != null) {
				RenderLexicon.renderHand(argument.stack(), argument.displayContext(), argument.displayContext().leftHand(),
						poseStack, submitNodeCollector, lightCoords);
			}
		}

		@Override
		public void getExtents(Consumer<Vector3fc> output) {
			output.accept(new Vector3f(0, 0, 0));
			output.accept(new Vector3f(1, 1, 1));
		}

		@Override
		public @Nullable HandState extractArgument(ItemStack stack) {
			return null;
		}
	}

	public record Unbaked(ItemModel.Unbaked model) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				ItemModels.CODEC.fieldOf("model").forGetter(Unbaked::model)
		).apply(i, Unbaked::new));

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			model.resolveDependencies(resolver);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			return new LexiconItemModel(model.bake(context, transformation));
		}
	}
}
