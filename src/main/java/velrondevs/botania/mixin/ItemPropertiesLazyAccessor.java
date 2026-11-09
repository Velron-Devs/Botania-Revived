package velrondevs.botania.mixin;

import net.minecraft.core.component.DataComponentInitializers;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.DependantName;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Item.Properties.class)
public interface ItemPropertiesLazyAccessor {
	@Accessor("id")
	ResourceKey<Item> botania$getId();

	@Accessor("id")
	void botania$setId(ResourceKey<Item> id);

	@Accessor("descriptionId")
	DependantName<Item, String> botania$getDescriptionId();

	@Accessor("ITEM_DESCRIPTION_ID")
	static DependantName<Item, String> botania$itemDescriptionId() {
		throw new AssertionError();
	}

	@Invoker("effectiveDescriptionId")
	String botania$effectiveDescriptionId();

	@Invoker("finalizeInitializer")
	DataComponentInitializers.Initializer<Item> botania$finalizeInitializer(Component name, Identifier model);
}
