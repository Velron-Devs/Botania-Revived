package velrondevs.botania.mixin;

import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Item.class)
public interface ItemLazyAccessor {
	@Mutable
	@Accessor("descriptionId")
	void botania$setDescriptionId(String descriptionId);
}
