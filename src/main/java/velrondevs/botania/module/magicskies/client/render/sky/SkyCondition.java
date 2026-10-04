package velrondevs.botania.module.magicskies.client.render.sky;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface SkyCondition {
	boolean shouldRender(ClientLevel level);

	SkyCondition ALWAYS = level -> true;

	SkyCondition NORMAL_SKY = level -> level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL;

	static SkyCondition inDimension(ResourceKey<Level> dimension) {
		return level -> level.dimension().equals(dimension);
	}
}
