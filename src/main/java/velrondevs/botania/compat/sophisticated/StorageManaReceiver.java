package velrondevs.botania.compat.sophisticated;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.p3pp3rf1y.sophisticatedcore.api.IStorageWrapper;

import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.spark.ManaSpark;
import velrondevs.botania.api.mana.spark.SparkAttachable;

import java.util.List;
import java.util.function.Function;

public final class StorageManaReceiver implements ManaReceiver, SparkAttachable {
	private final BlockEntity entity;
	private final Function<BlockEntity, IStorageWrapper> wrapperLookup;

	public StorageManaReceiver(BlockEntity entity, Function<BlockEntity, IStorageWrapper> wrapperLookup) {
		this.entity = entity;
		this.wrapperLookup = wrapperLookup;
	}

	private IStorageWrapper storage() {
		if (!SophisticatedConfig.pileReceiveFromWorld()) {
			return null;
		}
		try {
			return wrapperLookup.apply(entity);
		} catch (RuntimeException e) {
			return null;
		}
	}

	private static int clamp(long value) {
		return (int) Math.max(0, Math.min(Integer.MAX_VALUE, value));
	}

	@Override
	public Level getManaReceiverLevel() {
		return entity.getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return entity.getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		IStorageWrapper storage = storage();
		return storage == null ? 0 : clamp(ManaPileUpgradeItem.availableMana(storage));
	}

	@Override
	public boolean isFull() {
		IStorageWrapper storage = storage();
		return storage == null || ManaPileUpgradeItem.freeSpace(storage) <= 0;
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return !isFull();
	}

	@Override
	public void receiveMana(int mana) {
		IStorageWrapper storage = storage();
		if (storage == null || mana == 0) {
			return;
		}
		int moved = mana > 0 ? ManaPileUpgradeItem.fillMana(storage, mana, false) : ManaPileUpgradeItem.drainMana(storage, -mana, false);
		if (moved > 0) {
			entity.setChanged();
		}
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		IStorageWrapper storage = storage();
		return storage != null && ManaPileUpgradeItem.hasPile(storage);
	}

	@Override
	public int getAvailableSpaceForMana() {
		IStorageWrapper storage = storage();
		return storage == null ? 0 : clamp(ManaPileUpgradeItem.freeSpace(storage));
	}

	@Override
	public ManaSpark getAttachedSpark() {
		Level level = entity.getLevel();
		if (level == null) {
			return null;
		}
		BlockPos above = entity.getBlockPos().above();
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(Vec3.atLowerCornerOf(above), Vec3.atLowerCornerOf(above.offset(1, 1, 1))), found -> found instanceof ManaSpark);
		return sparks.size() == 1 ? (ManaSpark) sparks.get(0) : null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return isFull();
	}
}
