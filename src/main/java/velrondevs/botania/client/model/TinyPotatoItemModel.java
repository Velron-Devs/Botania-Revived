package velrondevs.botania.client.model;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import org.joml.Matrix4fc;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.client.render.block_entity.TinyPotatoBlockEntityRenderer;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TinyPotatoItemModel implements ItemModel {
	private final ItemModel base;
	private final ModelRenderProperties properties;
	private final Matrix4fc transformation;
	private final Map<BlockStateModelPart, Vector3fc[]> extents = new IdentityHashMap<>();

	public TinyPotatoItemModel(ItemModel base, ModelRenderProperties properties, Matrix4fc transformation) {
		this.base = base;
		this.properties = properties;
		this.transformation = transformation;
	}

	@Override
	public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext,
			@Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		if (item.has(DataComponents.CUSTOM_NAME) || ClientProxy.dootDoot) {
			BlockStateModelPart tater = TinyPotatoBlockEntityRenderer.getModelFromDisplayName(item.getHoverName());
			if (tater != null) {
				List<BakedQuad> quads = RenderHelper.allQuads(tater);
				output.appendModelIdentityElement(this);
				output.appendModelIdentityElement(tater);
				ItemStackRenderState.LayerRenderState layer = output.newLayer();
				if (item.hasFoil()) {
					layer.setFoilType(ItemStackRenderState.FoilType.STANDARD);
					output.setAnimated();
					output.appendModelIdentityElement(ItemStackRenderState.FoilType.STANDARD);
				}
				Vector3fc[] taterExtents = extents.computeIfAbsent(tater, t -> CuboidItemModelWrapper.computeExtents(quads));
				layer.setExtents(() -> taterExtents);
				layer.setLocalTransform(transformation);
				properties.applyToLayer(layer, displayContext);
				layer.prepareQuadList().addAll(quads);
				return;
			}
		}
		base.update(output, item, resolver, displayContext, level, owner, seed);
	}

	public record Unbaked(Identifier model) implements ItemModel.Unbaked {
		public static final MapCodec<Unbaked> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model)
		).apply(i, Unbaked::new));

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}

		@Override
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			resolver.markDependency(model);
		}

		@Override
		public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
			ItemModel base = new CuboidItemModelWrapper.Unbaked(model, Optional.empty(), List.of()).bake(context, transformation);
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel resolvedModel = baker.getModel(model);
			ModelRenderProperties properties = ModelRenderProperties.fromResolvedModel(baker, resolvedModel, resolvedModel.getTopTextureSlots());
			return new TinyPotatoItemModel(base, properties, transformation);
		}
	}
}
