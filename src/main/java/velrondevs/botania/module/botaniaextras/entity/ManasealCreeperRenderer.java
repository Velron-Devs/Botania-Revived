package velrondevs.botania.module.botaniaextras.entity;

import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Creeper;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ManasealCreeperRenderer extends CreeperRenderer {
	private static final ResourceLocation TEXTURE = prefix("textures/entity/botania_extras/manaseal_creeper.png");

	public ManasealCreeperRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(Creeper creeper) {
		return TEXTURE;
	}
}
