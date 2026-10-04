package velrondevs.botania.client.render.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherBossRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.boss.wither.WitherBoss;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.lib.ResourcesLib;

public class PinkWitherRenderer extends WitherBossRenderer {

	private static final ResourceLocation resource = ResourceLocation.parse(ResourcesLib.MODEL_PINK_WITHER);

	public PinkWitherRenderer(EntityRendererProvider.Context ctx) {
		super(ctx);
	}

	@NotNull
	@Override
	public ResourceLocation getTextureLocation(WitherBoss entity) {
		return resource;
	}

}
