package velrondevs.botania.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;

import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelDebugName;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.DelegateBlockStateModel;
import net.neoforged.neoforge.client.model.ExtendedUnbakedGeometry;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import net.neoforged.neoforge.client.model.UnbakedElementsHelper;
import net.neoforged.neoforge.client.model.UnbakedModelLoader;

import org.joml.Vector3f;

import velrondevs.botania.api.block.FloatingFlower;
import velrondevs.botania.api.block.FloatingFlowerProvider;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.client.core.handler.MiscellaneousModels;

import java.util.List;

public class FloatingFlowerGeometry implements ExtendedUnbakedGeometry {
	private static final Transformation MOVE_FLOWER = new Transformation(new Vector3f(0F, 0.2F, 0F), null, new Vector3f(0.5F, 0.5F, 0.5F), null);
	private final UnbakedModel unbakedFlower;

	private FloatingFlowerGeometry(UnbakedModel flower) {
		this.unbakedFlower = flower;
	}

	@Override
	public QuadCollection bake(TextureSlots textureSlots, ModelBaker baker, ModelState state, ModelDebugName debugName, ContextMap additionalProperties) {
		ModelState flowerState = UnbakedElementsHelper.composeRootTransformIntoModelState(state, MOVE_FLOWER);
		ResolvedModel flower = baker.resolveInlineModel(unbakedFlower, () -> debugName.debugName() + "_flower");
		return flower.bakeTopGeometry(flower.getTopTextureSlots(), baker, flowerState);
	}

	public static class Unbaked extends AbstractUnbakedModel {
		private final FloatingFlowerGeometry geometry;

		public Unbaked(StandardModelParameters parameters, FloatingFlowerGeometry geometry) {
			super(parameters);
			this.geometry = geometry;
		}

		@Override
		public UnbakedGeometry geometry() {
			return geometry;
		}

		@Override
		public void resolveDependencies(Resolver resolver) {
			Identifier parent = geometry.unbakedFlower.parent();
			if (parent != null) {
				resolver.markDependency(parent);
			}
			geometry.unbakedFlower.resolveDependencies(resolver);
		}
	}

	public static class Baked extends DelegateBlockStateModel {
		public Baked(BlockStateModel delegate) {
			super(delegate);
		}

		@Override
		public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
			super.collectParts(level, pos, state, random, parts);
			FloatingFlower.IslandType type = FloatingFlower.IslandType.GRASS;
			var be = level.getBlockEntity(pos);
			if (be instanceof SpecialFlowerBlockEntity special) {
				if (special.isFloating()) {
					type = special.getFloatingData().getIslandType();
				}
			} else if (be instanceof FloatingFlowerProvider provider) {
				type = provider.getFloatingData().getIslandType();
			}
			BlockStateModelPart island = MiscellaneousModels.INSTANCE.getIslandModel(type);
			if (island != null) {
				parts.add(island);
			}
		}
	}

	public enum Loader implements UnbakedModelLoader<Unbaked> {
		INSTANCE;

		@Override
		public Unbaked read(JsonObject model, JsonDeserializationContext ctx) throws JsonParseException {
			UnbakedModel flower = ctx.deserialize(model.getAsJsonObject("flower"), UnbakedModel.class);
			StandardModelParameters parameters = StandardModelParameters.parse(model, ctx);
			return new Unbaked(parameters, new FloatingFlowerGeometry(flower));
		}
	}
}
