package velrondevs.botania.common.block.dispenser;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.LevelEvent;

import org.jetbrains.annotations.NotNull;

public abstract class ProjectileBehavior extends DefaultDispenseItemBehavior {
	@NotNull
	@Override
	protected ItemStack execute(BlockSource source, ItemStack stack) {
		Level level = source.level();
		Position position = DispenserBlock.getDispensePosition(source);
		Direction direction = source.state().getValue(DispenserBlock.FACING);
		Projectile projectile = getProjectile(level, position, stack);
		projectile.shoot(direction.getStepX(), direction.getStepY() + 0.1F, direction.getStepZ(), getPower(), getUncertainty());
		level.addFreshEntity(projectile);
		stack.shrink(1);
		return stack;
	}

	@Override
	protected void playSound(BlockSource source) {
		source.level().levelEvent(LevelEvent.SOUND_DISPENSER_PROJECTILE_LAUNCH, source.pos(), 0);
	}

	protected abstract Projectile getProjectile(Level level, Position pos, ItemStack stack);

	protected float getUncertainty() {
		return 6.0F;
	}

	protected float getPower() {
		return 1.1F;
	}
}
