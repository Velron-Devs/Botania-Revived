package velrondevs.botania.module.asgard;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.xplat.BotaniaConfig;

public class AsgardandelionBlockEntity extends GeneratingFlowerBlockEntity {
	public static final int DEFAULT_MANA_PER_TICK = 1000000;
	public static final int MAX_MANA_PER_TICK = 100000000;
	public static final int COLOR = 0x9208FF;

	public AsgardandelionBlockEntity(BlockPos pos, BlockState state) {
		super(AsgardFlowers.ASGARDANDELION, pos, state);
	}

	public static boolean isEnabled() {
		try {
			return BotaniaConfig.common().asgardandelionEnabled();
		} catch (RuntimeException e) {
			return true;
		}
	}

	public static int manaPerTick() {
		try {
			return BotaniaConfig.common().asgardandelionManaPerTick();
		} catch (RuntimeException e) {
			return DEFAULT_MANA_PER_TICK;
		}
	}

	@Override
	public void tickFlower() {
		if (!getLevel().isClientSide && isEnabled()) {
			int missing = getMaxMana() - getMana();
			if (missing > 0) {
				addMana(Math.min(manaPerTick(), missing));
			}
		}
		super.tickFlower();
	}

	@Override
	public int getMaxMana() {
		return manaPerTick();
	}

	@Override
	public int getColor() {
		return COLOR;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), getBindingRadius());
	}
}
