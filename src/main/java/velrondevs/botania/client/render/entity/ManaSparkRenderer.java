package velrondevs.botania.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;

import velrondevs.botania.common.entity.ManaSparkEntity;

import java.util.Objects;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ManaSparkRenderer extends BaseSparkRenderer<ManaSparkEntity> {
	private final TextureAtlasSprite dispersiveIcon;
	private final TextureAtlasSprite dominantIcon;
	private final TextureAtlasSprite recessiveIcon;
	private final TextureAtlasSprite isolatedIcon;

	public ManaSparkRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
		var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
		this.dispersiveIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_dispersive")));
		this.dominantIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_dominant")));
		this.recessiveIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_recessive")));
		this.isolatedIcon = Objects.requireNonNull(atlas.apply(prefix("item/spark_upgrade_rune_isolated")));
	}

	@Override
	public TextureAtlasSprite getSpinningIcon(ManaSparkEntity entity) {
		return switch (entity.getUpgrade()) {
			case NONE -> null;
			case DISPERSIVE -> this.dispersiveIcon;
			case DOMINANT -> this.dominantIcon;
			case RECESSIVE -> this.recessiveIcon;
			case ISOLATED -> this.isolatedIcon;
		};
	}

}
