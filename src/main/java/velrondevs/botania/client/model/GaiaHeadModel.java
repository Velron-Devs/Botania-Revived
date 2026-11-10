package velrondevs.botania.client.model;

import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.object.skull.SkullModelBase;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.level.block.SkullBlock;

import velrondevs.botania.client.render.block_entity.GaiaHeadBlockEntityRenderer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GaiaHeadModel extends SkullModelBase {
	private static final List<SkullBlock.Types> VIEW_TYPES = List.of(
			SkullBlock.Types.PLAYER, SkullBlock.Types.SKELETON, SkullBlock.Types.WITHER_SKELETON,
			SkullBlock.Types.ZOMBIE, SkullBlock.Types.CREEPER, SkullBlock.Types.DRAGON);

	private final Map<SkullBlock.Type, SkullModelBase> models;

	public GaiaHeadModel(EntityModelSet modelSet) {
		this(createModels(modelSet));
	}

	private GaiaHeadModel(Map<SkullBlock.Type, SkullModelBase> models) {
		super(createRoot(models));
		this.models = models;
	}

	private static Map<SkullBlock.Type, SkullModelBase> createModels(EntityModelSet modelSet) {
		Map<SkullBlock.Type, SkullModelBase> models = new LinkedHashMap<>();
		for (SkullBlock.Types type : VIEW_TYPES) {
			SkullModelBase model = SkullBlockRenderer.createModel(modelSet, type);
			if (model != null) {
				models.put(type, model);
			}
		}
		return models;
	}

	private static ModelPart createRoot(Map<SkullBlock.Type, SkullModelBase> models) {
		Map<String, ModelPart> children = new HashMap<>();
		models.forEach((type, model) -> children.put(type.getSerializedName(), model.root()));
		return new ModelPart(List.of(), children);
	}

	public SkullModelBase getModel(SkullBlock.Type type) {
		SkullModelBase model = models.get(type);
		return model != null ? model : models.get(SkullBlock.Types.PLAYER);
	}

	@Override
	public void setupAnim(SkullModelBase.State state) {
		super.setupAnim(state);
		SkullModelBase current = getModel(GaiaHeadBlockEntityRenderer.getViewType());
		models.values().forEach(model -> model.root().visible = model == current);
		if (current != null) {
			current.setupAnim(state);
		}
	}
}
