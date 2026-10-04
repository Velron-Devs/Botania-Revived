package velrondevs.botania.module.botaniaextras.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import velrondevs.botania.api.block_entity.GeneratingFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.common.helper.DelayHelper;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;
import velrondevs.botania.registry.BotaniaBlocks;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.Map;

public class CrysanthermumBlockEntity extends GeneratingFlowerBlockEntity {
	public static final String TAG_TEMPERATURE = "temperature";
	public static final String TAG_RECENT_STONES = "recentStones";
	public static final int RANGE = 1;
	public static final int MAX_TEMPERATURE = 8;
	public static final int MAX_MANA = 8000;
	public static final int DECAY_INTERVAL = 600;
	public static final int GENERATE_INTERVAL = 5;
	public static final int MANA_PER_PULSE = 5;
	public static final int EAT_COOLDOWN = 10;
	public static final int HISTORY_SIZE = 8;
	public static final int FUNGAL = 3;
	public static final int[] STONE_TEMPERATURES = { 2, 1, -3, 0, -3, 3, -4, 4 };

	private static final Map<Block, Integer> STONE_TYPES = new IdentityHashMap<>();

	static {
		Block[] stones = {
				BotaniaBlocks.biomeStoneForest, BotaniaBlocks.biomeStonePlains, BotaniaBlocks.biomeStoneMountain, BotaniaBlocks.biomeStoneFungal,
				BotaniaBlocks.biomeStoneSwamp, BotaniaBlocks.biomeStoneDesert, BotaniaBlocks.biomeStoneTaiga, BotaniaBlocks.biomeStoneMesa
		};
		Block[] cobblestones = {
				BotaniaBlocks.biomeCobblestoneForest, BotaniaBlocks.biomeCobblestonePlains, BotaniaBlocks.biomeCobblestoneMountain, BotaniaBlocks.biomeCobblestoneFungal,
				BotaniaBlocks.biomeCobblestoneSwamp, BotaniaBlocks.biomeCobblestoneDesert, BotaniaBlocks.biomeCobblestoneTaiga, BotaniaBlocks.biomeCobblestoneMesa
		};
		for (int i = 0; i < stones.length; i++) {
			STONE_TYPES.put(stones[i], i);
			STONE_TYPES.put(cobblestones[i], i);
		}
	}

	private int temperature = 0;
	private final Deque<Integer> recentStones = new ArrayDeque<>();
	private int eatCooldown = 0;

	public CrysanthermumBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaExtrasFlowers.CRYSANTHERMUM, pos, state);
	}

	public static int stoneType(ItemStack stack) {
		if (stack.getItem() instanceof BlockItem blockItem) {
			Integer type = STONE_TYPES.get(blockItem.getBlock());
			return type == null ? -1 : type;
		}
		return -1;
	}

	public static int comparatorSignal(int temperature) {
		return (int) ((temperature + MAX_TEMPERATURE) / (float) (2 * MAX_TEMPERATURE) * 15F);
	}

	public static int colorFor(int temperature) {
		float distance = (temperature + MAX_TEMPERATURE) / (float) (2 * MAX_TEMPERATURE);
		int hue = (int) (distance * (360 - 235)) + 235;
		return Mth.hsvToRgb(hue / 360F, 1F, 1F);
	}

	public static int targetFor(float baseTemperature) {
		return Mth.clamp(Math.round((baseTemperature - 0.7F) * 8F), -MAX_TEMPERATURE, MAX_TEMPERATURE);
	}

	public static int manaPerPulse(int temperature, int target) {
		if (temperature == 0 || Math.abs(temperature) >= MAX_TEMPERATURE) {
			return 0;
		}
		float closeness = 1F - Math.abs(temperature - target) / (float) (2 * MAX_TEMPERATURE);
		return Math.max(1, Math.round(MANA_PER_PULSE * closeness * closeness));
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = Mth.clamp(temperature, -MAX_TEMPERATURE, MAX_TEMPERATURE);
	}

	public int getBiomeTarget() {
		return targetFor(getLevel().getBiome(getEffectivePos()).value().getBaseTemperature());
	}

	private void changed() {
		setChanged();
		sync();
		getLevel().updateNeighbourForOutputSignal(getBlockPos(), getBlockState().getBlock());
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (getLevel().isClientSide) {
			return;
		}

		if (eatCooldown > 0) {
			eatCooldown--;
		}

		if (ticksExisted % DECAY_INTERVAL == 0 && temperature != 0) {
			setTemperature(temperature + (temperature > 0 ? -1 : 1));
			changed();
		}

		if (eatCooldown == 0) {
			eatStone();
		}

		if (ticksExisted % GENERATE_INTERVAL == 0 && getMana() < getMaxMana()) {
			int amount = manaPerPulse(temperature, getBiomeTarget());
			if (amount > 0) {
				addMana(amount);
				sync();
			}
		}
	}

	private void eatStone() {
		BlockPos pos = getEffectivePos();
		AABB area = new AABB(Vec3.atLowerCornerOf(pos.offset(-RANGE, -RANGE, -RANGE)), Vec3.atLowerCornerOf(pos.offset(RANGE + 1, RANGE + 1, RANGE + 1)));
		for (ItemEntity item : getLevel().getEntitiesOfClass(ItemEntity.class, area)) {
			if (!DelayHelper.canInteractWith(this, item)) {
				continue;
			}
			ItemStack stack = item.getItem();
			int type = stoneType(stack);
			if (type < 0) {
				continue;
			}

			int base = type == FUNGAL ? getLevel().getRandom().nextInt(9) - 4 : STONE_TEMPERATURES[type];
			int repeats = 0;
			for (int recent : recentStones) {
				if (recent == type) {
					repeats++;
				}
			}
			setTemperature(temperature + base / (1 << repeats));
			recentStones.addLast(type);
			while (recentStones.size() > HISTORY_SIZE) {
				recentStones.removeFirst();
			}

			if (getLevel() instanceof ServerLevel serverLevel) {
				serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), item.getX(), item.getY(), item.getZ(), 10, 0.1D, 0.1D, 0.1D, 0.05D);
			}
			getLevel().playSound(null, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.4F, 1.2F);
			getLevel().gameEvent(null, GameEvent.BLOCK_ACTIVATE, pos);
			EntityHelper.shrinkItem(item);
			eatCooldown = EAT_COOLDOWN;
			changed();
			return;
		}
	}

	@Override
	public int getComparatorSignal() {
		return comparatorSignal(temperature);
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return MAX_MANA;
	}

	@Override
	public int getColor() {
		return colorFor(temperature);
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);
		cmp.putInt(TAG_TEMPERATURE, temperature);
		cmp.putIntArray(TAG_RECENT_STONES, recentStones.stream().mapToInt(Integer::intValue).toArray());
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);
		setTemperature(cmp.getInt(TAG_TEMPERATURE));
		recentStones.clear();
		for (int type : cmp.getIntArray(TAG_RECENT_STONES)) {
			recentStones.addLast(type);
		}
	}

	public static class WandHud extends BindableSpecialFlowerBlockEntity.BindableFlowerWandHud<CrysanthermumBlockEntity> {
		public WandHud(CrysanthermumBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			String name = I18n.get("botaniamisc.botania_extras.temperature." + (flower.getTemperature() + MAX_TEMPERATURE));
			int half = mc.font.width(name) / 2 + 2;
			super.renderHUD(gui, mc, half, half, 42);
			int centerX = mc.getWindow().getGuiScaledWidth() / 2;
			int centerY = mc.getWindow().getGuiScaledHeight() / 2;
			gui.drawString(mc.font, name, centerX - mc.font.width(name) / 2, centerY + 31, flower.getColor(), true);
		}
	}
}
