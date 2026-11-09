package velrondevs.botania.mixin;

import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.registry.BotaniaLazyIds;

@Mixin(Item.class)
public class ItemLazyIdMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;effectiveDescriptionId()Ljava/lang/String;"))
	private String botania$lazyDescriptionId(Item.Properties properties) {
		ItemPropertiesLazyAccessor accessor = (ItemPropertiesLazyAccessor) properties;
		return accessor.botania$getId() == null ? BotaniaLazyIds.PENDING_DESCRIPTION : accessor.botania$effectiveDescriptionId();
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;effectiveModel()Lnet/minecraft/resources/Identifier;"))
	private Identifier botania$lazyModel(Item.Properties properties) {
		return ((ItemPropertiesLazyAccessor) properties).botania$getId() == null ? BotaniaLazyIds.PENDING_ID : properties.effectiveModel();
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;itemIdOrThrow()Lnet/minecraft/resources/ResourceKey;"))
	private ResourceKey<Item> botania$lazyItemId(Item.Properties properties) {
		return ((ItemPropertiesLazyAccessor) properties).botania$getId();
	}

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/component/DataComponentInitializers;add(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/component/DataComponentInitializers$Initializer;)V"))
	private <T> void botania$lazyInitializer(DataComponentInitializers initializers, ResourceKey<T> key, DataComponentInitializers.Initializer<T> initializer) {
		if (key != null) {
			initializers.add(key, initializer);
		}
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void botania$trackPending(Item.Properties properties, CallbackInfo ci) {
		if (((ItemPropertiesLazyAccessor) properties).botania$getId() == null) {
			BotaniaLazyIds.trackItem((Item) (Object) this, properties);
		}
	}
}
