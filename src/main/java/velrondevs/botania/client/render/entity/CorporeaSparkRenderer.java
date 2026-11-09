package velrondevs.botania.client.render.entity;

import velrondevs.botania.client.core.helper.RenderHelper;

import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import velrondevs.botania.common.entity.CorporeaSparkEntity;

import java.util.Objects;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CorporeaSparkRenderer extends BaseSparkRenderer<CorporeaSparkEntity> {
	private final TextureAtlasSprite corporeaWorldSprite;
	private final TextureAtlasSprite corporeaMasterWorldSprite;
	private final TextureAtlasSprite corporeaCreativeWorldSprite;

	public CorporeaSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		this.corporeaWorldSprite = Objects.requireNonNull(RenderHelper.itemSprite(prefix("item/corporea_spark")));
		this.corporeaMasterWorldSprite = Objects.requireNonNull(RenderHelper.itemSprite(prefix("item/corporea_spark_master")));
		this.corporeaCreativeWorldSprite = Objects.requireNonNull(RenderHelper.itemSprite(prefix("item/corporea_spark_creative")));
	}

	@Override
	public TextureAtlasSprite getBaseIcon(CorporeaSparkEntity entity) {
		if (entity.isCreative()) {
			return this.corporeaCreativeWorldSprite;
		} else if (entity.isMaster()) {
			return this.corporeaMasterWorldSprite;
		} else {
			return this.corporeaWorldSprite;
		}
	}

}
