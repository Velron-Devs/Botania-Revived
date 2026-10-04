package velrondevs.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.mixin.HurtByTargetGoalAccessor;
import velrondevs.botania.mixin.MobAccessor;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HeiseiDreamBlockEntity extends FunctionalFlowerBlockEntity {
	private static final int RANGE = 5;
	private static final int COST = 100;

	public HeiseiDreamBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.HEISEI_DREAM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		List<Mob> mobs = getLevel().getEntitiesOfClass(Mob.class, new AABB(Vec3.atLowerCornerOf(getEffectivePos().offset(-RANGE, -RANGE, -RANGE)), Vec3.atLowerCornerOf(getEffectivePos().offset(RANGE + 1, RANGE + 1, RANGE + 1))), mob -> mob instanceof Enemy && mob.isAlive());

		if (mobs.size() > 1 && getMana() >= COST) {
			Collections.shuffle(mobs);
			for (Mob mob : mobs) {
				if (brainwashEntity(mob, mobs)) {
					addMana(-COST);
					sync();
					break;
				}
			}
		}
	}

	public static boolean brainwashEntity(Mob entity, List<Mob> mobs) {
		LivingEntity target = entity.getTarget();
		boolean did = false;

		if (!(target instanceof Enemy)) {
			Mob newTarget;
			do {
				newTarget = mobs.get(entity.level().random.nextInt(mobs.size()));
			} while (newTarget == entity);

			entity.setTarget(null);

			GoalSelector targetSelector = ((MobAccessor) entity).getTargetSelector();
			for (WrappedGoal entry : targetSelector.getAvailableGoals()) {
				if (entry.getGoal() instanceof HurtByTargetGoal goal) {

					var ignoreClasses = ((HurtByTargetGoalAccessor) goal).getIgnoreDamageClasses();
					Arrays.fill(ignoreClasses, Void.TYPE);

					targetSelector.removeGoal(goal);
					targetSelector.addGoal(-1, goal);
					break;
				}
			}

			entity.setLastHurtByMob(newTarget);
			did = true;
		}

		return did;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getColor() {
		return 0xFF219D;
	}

	@Override
	public int getMaxMana() {
		return 1000;
	}

}
