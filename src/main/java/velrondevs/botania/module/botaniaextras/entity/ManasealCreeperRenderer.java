package velrondevs.botania.module.botaniaextras.entity;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.Identifier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ManasealCreeperRenderer extends CreeperRenderer {
	private static final Identifier TEXTURE = prefix("textures/entity/botania_extras/manaseal_creeper.png");

	public ManasealCreeperRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public Identifier getTextureLocation(net.minecraft.client.renderer.entity.state.CreeperRenderState state) {
		return TEXTURE;
	}
}
