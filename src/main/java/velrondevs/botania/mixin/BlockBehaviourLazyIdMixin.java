package velrondevs.botania.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.registry.BotaniaLazyIds;

import java.util.Optional;

@Mixin(BlockBehaviour.class)
public class BlockBehaviourLazyIdMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;effectiveDrops()Ljava/util/Optional;"))
	private Optional<ResourceKey<LootTable>> botania$lazyDrops(BlockBehaviour.Properties properties) {
		BlockPropertiesAccessor accessor = (BlockPropertiesAccessor) properties;
		return accessor.botania$getId() == null ? Optional.empty() : accessor.botania$effectiveDrops();
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;effectiveDescriptionId()Ljava/lang/String;"))
	private String botania$lazyDescriptionId(BlockBehaviour.Properties properties) {
		BlockPropertiesAccessor accessor = (BlockPropertiesAccessor) properties;
		return accessor.botania$getId() == null ? BotaniaLazyIds.PENDING_DESCRIPTION : accessor.botania$effectiveDescriptionId();
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void botania$trackPending(BlockBehaviour.Properties properties, CallbackInfo ci) {
		if (((BlockPropertiesAccessor) properties).botania$getId() == null) {
			BotaniaLazyIds.trackBlock((BlockBehaviour) (Object) this);
		}
	}
}
