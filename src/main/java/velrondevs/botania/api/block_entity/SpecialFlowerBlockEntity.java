package velrondevs.botania.api.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.block.*;
import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.common.annotations.SoftImplement;
import velrondevs.botania.common.block.block_entity.red_string.RedStringSpooferBlockEntity;
import velrondevs.botania.common.block.decor.FloatingFlowerBlock;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaBlocks;

public abstract class SpecialFlowerBlockEntity extends BlockEntity implements FloatingFlowerProvider {
	public static final int PODZOL_DELAY = 5;
	public static final int MYCELIUM_DELAY = 10;

	private final FloatingFlower floatingData = new FloatingFlowerImpl() {
		@Override
		public ItemStack getDisplayStack() {
			ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(getType());
			return BuiltInRegistries.ITEM.getOptional(id).map(ItemStack::new).orElse(super.getDisplayStack());
		}
	};

	public int ticksExisted = 0;

	public boolean overgrowth = false;

	public boolean overgrowthBoost = false;
	private BlockPos positionOverride;
	private boolean isFloating;

	public static final String TAG_TICKS_EXISTED = "ticksExisted";
	private static final String TAG_FLOATING_DATA = "floating";

	public SpecialFlowerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, SpecialFlowerBlockEntity self) {
		if (self.isFloating != state.is(BotaniaTags.Blocks.FLOATING_FLOWERS)) {
			BotaniaAPI.LOGGER.error("Special flower changed floating state, this is not supported!", new Throwable());
			self.isFloating = !self.isFloating;
		}
		BlockEntity tileBelow = level.getBlockEntity(worldPosition.below());
		if (tileBelow instanceof RedStringSpooferBlockEntity relay) {
			BlockPos coords = relay.getBinding();
			if (coords != null) {
				self.positionOverride = coords;
				self.tickFlower();

				return;
			} else {
				self.positionOverride = null;
			}
		} else {
			self.positionOverride = null;
		}

		boolean special = self.isOnSpecialSoil();
		if (special) {
			self.overgrowth = true;
			if (self.isOvergrowthAffected()) {
				self.tickFlower();
				self.overgrowthBoost = true;
			}
		}
		self.tickFlower();
		self.overgrowth = false;
		self.overgrowthBoost = false;
	}

	@Nullable
	@Override
	public FloatingFlower getFloatingData() {
		if (hasLevel() && isFloating()) {
			return floatingData;
		}
		return null;
	}

	public final boolean isFloating() {
		return this.isFloating;
	}

	public final void setFloating(boolean floating) {
		this.isFloating = floating;
	}

	public boolean isOnSpecialSoil() {
		if (isFloating()) {
			return false;
		} else {
			return level.getBlockState(worldPosition.below()).is(BotaniaBlocks.enchantedSoil);
		}
	}

	public final BlockPos getEffectivePos() {
		return positionOverride != null ? positionOverride : getBlockPos();
	}

	protected void tickFlower() {
		ticksExisted++;
	}

	@Override
	public final void loadAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.loadAdditional(cmp, registries);
		if (cmp.contains(TAG_TICKS_EXISTED)) {
			ticksExisted = cmp.getInt(TAG_TICKS_EXISTED);
		}
		if (getBlockState().getBlock() instanceof FloatingFlowerBlock) {
			setFloating(true);
		}

		FloatingFlower.IslandType oldType = floatingData.getIslandType();
		readFromPacketNBT(cmp, registries);
		if (isFloating() && oldType != floatingData.getIslandType() && level != null) {
			level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
		}
	}

	@Override
	public final void saveAdditional(CompoundTag cmp, HolderLookup.Provider registries) {
		super.saveAdditional(cmp, registries);
		cmp.putInt(TAG_TICKS_EXISTED, ticksExisted);
		writeToPacketNBT(cmp, registries);
	}

	@NotNull
	@Override
	public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		var tag = new CompoundTag();
		writeToPacketNBT(tag, registries);
		return tag;
	}

	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		if (isFloating()) {
			cmp.put(TAG_FLOATING_DATA, floatingData.writeNBT());
		}
	}

	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		if (cmp.contains(TAG_FLOATING_DATA)) {
			floatingData.readNBT(cmp.getCompound(TAG_FLOATING_DATA));
		}
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void sync() {
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {}

	@Nullable
	public abstract RadiusDescriptor getRadius();

	@Nullable
	public RadiusDescriptor getSecondaryRadius() {
		return null;
	}

	public boolean isOvergrowthAffected() {
		return true;
	}

	public int getComparatorSignal() {
		return 0;
	}

	public int getModulatedDelay() {
		if (isFloating()) {
			FloatingFlower.IslandType type = floatingData.getIslandType();
			if (type == FloatingFlower.IslandType.MYCEL) {
				return MYCELIUM_DELAY;
			} else if (type == FloatingFlower.IslandType.PODZOL) {
				return PODZOL_DELAY;
			}
		} else {
			BlockState below = level.getBlockState(getBlockPos().below());
			if (below.is(Blocks.MYCELIUM)) {
				return MYCELIUM_DELAY;
			}

			if (below.is(Blocks.PODZOL)) {
				return PODZOL_DELAY;
			}
		}

		return 0;
	}

	@SoftImplement("RenderDataBlockEntity")
	@Nullable
	public Object getRenderData() {
		if (isFloating()) {
			return floatingData.getIslandType();
		}
		return null;
	}

	public void emitParticle(ParticleOptions options, double xOffset, double yOffset, double zOffset, double xSpeed, double ySpeed, double zSpeed) {
		if (!level.isClientSide) {
			return;
		}
		Vec3 offset = level.getBlockState(getEffectivePos()).getOffset(level, getEffectivePos());
		level.addParticle(options,
				getEffectivePos().getX() + offset.x + xOffset,
				getEffectivePos().getY() + offset.y + yOffset,
				getEffectivePos().getZ() + offset.z + zOffset,
				xSpeed, ySpeed, zSpeed
		);
	}

	protected String[] itemDataKeys() {
		return new String[0];
	}

	@Override
	protected void collectImplicitComponents(DataComponentMap.Builder components) {
		super.collectImplicitComponents(components);
		String[] keys = itemDataKeys();
		if (keys.length == 0 || level == null) {
			return;
		}
		CompoundTag full = new CompoundTag();
		writeToPacketNBT(full, level.registryAccess());
		CompoundTag data = new CompoundTag();
		for (String key : keys) {
			if (full.contains(key)) {
				data.put(key, full.get(key).copy());
			}
		}
		if (!data.isEmpty()) {
			BlockEntity.addEntityType(data, getType());
			components.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(data));
		}
	}
}
