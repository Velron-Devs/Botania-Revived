package velrondevs.botania.common.block.flower.functional;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block.Bound;
import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.common.helper.DelayHelper;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public class SpectranthemumBlockEntity extends FunctionalFlowerBlockEntity {
	private static final String TAG_BIND_X = "bindX";
	private static final String TAG_BIND_Y = "bindY";
	private static final String TAG_BIND_Z = "bindZ";

	private static final int BASE_COST = 2;
	private static final int RANGE = 2;

	private BlockPos bindPos = Bound.UNBOUND_POS;

	public SpectranthemumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.SPECTRANTHEMUM, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && redstoneSignal == 0 && getLevel().hasChunkAt(bindPos)) {
			BlockPos pos = getEffectivePos();

			boolean did = false;

			List<ItemEntity> items = getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(Vec3.atLowerCornerOf(pos.offset(-RANGE, -RANGE, -RANGE)), Vec3.atLowerCornerOf(pos.offset(RANGE + 1, RANGE + 1, RANGE + 1))));

			for (ItemEntity item : items) {
				if (!DelayHelper.canInteractWith(this, item)) {
					continue;
				}

				ItemStack stack = item.getItem();
				if (XplatAbstractions.INSTANCE.findManaItem(stack) != null) {
					continue;
				}

				double cost = BASE_COST * stack.getCount() * Math.sqrt(bindPos.distToCenterSqr(item.position()));
				if (getMana() >= cost) {
					spawnExplosionParticles(item, 10);
					BlockPos sourcePos = item.blockPosition();
					item.setPos(bindPos.getX() + 0.5, bindPos.getY() + 1.5, bindPos.getZ() + 0.5);
					EntityHelper.addTeleportTicketIfFarAway(item, sourcePos);
					item.setDeltaMovement(Vec3.ZERO);
					spawnExplosionParticles(item, 10);
					addMana(-(int) cost);
					did = true;
				}
			}

			if (did) {
				sync();
			}
		}
	}

	static void spawnExplosionParticles(Entity item, int p) {
		XplatAbstractions.INSTANCE.sendToTracking(item, new BotaniaEffectPacket(EffectType.ITEM_SMOKE, item.getX(), item.getY(), item.getZ(), item.getId(), p));
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);
		cmp.putInt(TAG_BIND_X, bindPos.getX());
		cmp.putInt(TAG_BIND_Y, bindPos.getY());
		cmp.putInt(TAG_BIND_Z, bindPos.getZ());
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);
		bindPos = new BlockPos(
				cmp.getInt(TAG_BIND_X),
				cmp.getInt(TAG_BIND_Y),
				cmp.getInt(TAG_BIND_Z)
		);
	}

	@Override
	public boolean acceptsRedstone() {
		return true;
	}

	@Override
	public int getColor() {
		return 0x98BCFF;
	}

	@Override
	public int getMaxMana() {
		return 5000;
	}

	@Override
	public boolean bindTo(Player player, ItemStack wand, BlockPos pos, Direction side) {
		boolean bound = super.bindTo(player, wand, pos, side);

		if (!bound && !pos.equals(bindPos) && !pos.equals(getEffectivePos())) {
			bindPos = pos;
			sync();

			return true;
		}

		return bound;
	}

	@Override
	public BlockPos getBinding() {
		return Proxy.INSTANCE.getClientPlayer().isShiftKeyDown() && bindPos.getY() != Integer.MIN_VALUE ? bindPos : super.getBinding();
	}

}
