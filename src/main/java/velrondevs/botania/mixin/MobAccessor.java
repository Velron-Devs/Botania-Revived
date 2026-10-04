package velrondevs.botania.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface MobAccessor {
	@Nullable
	@Invoker("getAmbientSound")
	SoundEvent botania_getAmbientSound();

	@Accessor
	void setLootTable(ResourceKey<LootTable> lootTable);

	@Accessor
	GoalSelector getGoalSelector();

	@Accessor
	GoalSelector getTargetSelector();
}
