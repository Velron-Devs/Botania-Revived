package velrondevs.botania.api.internal;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface ManaBurst {

	BlockPos NO_SOURCE = new BlockPos(0, Integer.MIN_VALUE, 0);

	boolean isFake();

	int getColor();

	void setColor(int color);

	int getMana();

	void setMana(int mana);

	int getStartingMana();

	void setStartingMana(int mana);

	int getMinManaLoss();

	void setMinManaLoss(int minManaLoss);

	float getManaLossPerTick();

	void setManaLossPerTick(float mana);

	float getBurstGravity();

	void setGravity(float gravity);

	BlockPos getBurstSourceBlockPos();

	void setBurstSourceCoords(BlockPos pos);

	Optional<GlobalPos> getBurstSource();

	void setBurstSource(@Nullable GlobalPos sourcePos);

	default boolean isBurstSourceDimension(Level testLevel) {
		return getBurstSource().map(GlobalPos::dimension).map(testLevel.dimension()::equals).orElse(false);
	}

	ItemStack getSourceLens();

	void setSourceLens(ItemStack lens);

	boolean hasAlreadyCollidedAt(BlockPos pos);

	void setCollidedAt(BlockPos pos);

	int getTicksExisted();

	void setFake(boolean fake);

	void setShooterUUID(UUID uuid);

	UUID getShooterUUID();

	void ping();

	boolean hasWarped();

	void setWarped(boolean warped);

	int getOrbitTime();

	void setOrbitTime(int time);

	boolean hasTripped();

	void setTripped(boolean tripped);

	@Nullable
	BlockPos getMagnetizedPos();

	void setMagnetizePos(@Nullable BlockPos pos);

	boolean hasLeftSource();

	default ThrowableProjectile entity() {
		return (ThrowableProjectile) this;
	}
}
