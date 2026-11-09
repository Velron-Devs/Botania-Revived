package velrondevs.botania.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootTable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(BlockBehaviour.class)
public interface BlockBehaviourLazyAccessor {
	@Mutable
	@Accessor("drops")
	void botania$setDrops(Optional<ResourceKey<LootTable>> drops);

	@Mutable
	@Accessor("descriptionId")
	void botania$setDescriptionId(String descriptionId);
}
