package velrondevs.botania.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;

import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.special.NoDataSpecialModelRenderer;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.level.block.SkullBlock;

import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.model.GaiaHeadModel;
import velrondevs.botania.client.render.block_entity.GaiaHeadBlockEntityRenderer;
import velrondevs.botania.common.block.GaiaHeadBlock;

import java.util.function.Consumer;

public class GaiaHeadSpecialRenderer implements NoDataSpecialModelRenderer {
	private final SkullModelBase model;

	public GaiaHeadSpecialRenderer(SkullModelBase model) {
		this.model = model;
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
		SkullBlockRenderer.submitSkull(0, poseStack, submitNodeCollector, lightCoords, model, GaiaHeadBlockEntityRenderer.getViewRenderType(), outlineColor, null);
	}

	@Override
	public void getExtents(Consumer<Vector3fc> output) {
		SkullModelBase extentsModel = model instanceof GaiaHeadModel gaia ? gaia.getModel(SkullBlock.Types.PLAYER) : model;
		if (extentsModel == null) {
			return;
		}
		SkullModelBase.State state = new SkullModelBase.State();
		extentsModel.setupAnim(state);
		extentsModel.root().getExtentsForGui(new PoseStack(), output);
	}

	public enum Unbaked implements NoDataSpecialModelRenderer.Unbaked {
		INSTANCE;

		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public @Nullable GaiaHeadSpecialRenderer bake(SpecialModelRenderer.BakingContext context) {
			SkullModelBase model = SkullBlockRenderer.createModel(context.entityModelSet(), GaiaHeadBlock.GAIA_TYPE);
			return model == null ? null : new GaiaHeadSpecialRenderer(model);
		}
	}
}
