package velrondevs.botania.api.item;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface Relic {

	void bindToUUID(UUID uuid);

	@Nullable
	UUID getSoulbindUUID();

	void tickBinding(Player player);

	@Nullable
	default ResourceLocation getAdvancement() {
		return null;
	}

	default boolean shouldDamageWrongPlayer() {
		return true;
	}

	boolean isRightPlayer(Player player);

}
