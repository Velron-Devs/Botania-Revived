package velrondevs.botania.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
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

import org.jspecify.annotations.Nullable;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.item.ManaBlasterItem;

import java.util.function.Consumer;

public class ManaBlasterItemModel implements ItemModel {
	private static final Matrix4fc LENS_TRANSFORM = new Transformation(new Vector3f(-0.4F, 0.2F, 0.0F), VecHelper.rotateY(90), new Vector3f(0.625F, 0.625F, 0.625F), null).getMatrix();
	private final ItemModel gun;
	private final ItemModel gunClip;

	public ManaBlasterItemModel(ItemModel gun, ItemModel gunClip) {
		this.gun = gun;
		this.gunClip = gunClip;
	}

	@Override
	public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext,
			@Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		(ManaBlasterItem.hasClip(item) ? gunClip : gun).update(output, item, resolver, displayContext, level, owner, seed);
		ItemStack lens = ManaBlasterItem.getLens(item);
		if (!lens.isEmpty()) {
			ItemStackRenderState lensState = new ItemStackRenderState();
			resolver.updateForTopItem(lensState, lens, ItemDisplayContext.NONE, level, owner, seed);
			ItemStackRenderState.LayerRenderState layer = output.newLayer();
			layer.setLocalTransform(LENS_TRANSFORM);
			layer.setupSpecialModel(LensRenderer.INSTANCE, lensState);
			output.appendModelIdentityElement(lens.getItem());
		}
	}

	private enum LensRenderer implements SpecialModelRenderer<ItemStackRenderState> {
		INSTANCE;

		@Override
		public void submit(@Nullable ItemStackRenderState argument, PoseStack poseStack, SubmitNodeCollector submitNodeCollector,
				int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
			if (argument != null) {
				argument.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
			}
		}

		@Override
		public void getExtents(Consumer<Vector3fc> output) {
			output.accept(new Vector3f(0, 0, 0));
			output.accept(new Vector3f(1, 1, 1));
		}

		@Override
		public @Nullable ItemStackRenderState extractArgument(ItemStack stack) {
			return null;
		}
	}

	public record Unbaked(ItemModel.Unbaked gun, ItemModel.Unbaked gunClip) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				ItemModels.CODEC.fieldOf("gun").forGetter(Unbaked::gun),
				ItemModels.CODEC.fieldOf("gun_clip").forGetter(Unbaked::gunClip)
		).apply(i, Unbaked::new));

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			gun.resolveDependencies(resolver);
			gunClip.resolveDependencies(resolver);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			return new ManaBlasterItemModel(gun.bake(context, transformation), gunClip.bake(context, transformation));
		}
	}

}
