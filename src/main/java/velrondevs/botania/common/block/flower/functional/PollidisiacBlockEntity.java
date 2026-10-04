package velrondevs.botania.common.block.flower.functional;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.Wandable;
import velrondevs.botania.api.block_entity.FunctionalFlowerBlockEntity;
import velrondevs.botania.api.block_entity.RadiusDescriptor;
import velrondevs.botania.common.helper.DelayHelper;
import velrondevs.botania.common.helper.EntityHelper;
import velrondevs.botania.mixin.AnimalAccessor;
import velrondevs.botania.mixin.MushroomCowAccessor;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import java.util.*;
import java.util.function.Predicate;

public class PollidisiacBlockEntity extends FunctionalFlowerBlockEntity implements Wandable {
	private static final String TAG_FEEDING_MODE = "mode";
	private static final int RANGE = 6;
	private static final int MANA_COST = 12;

	@NotNull
	private Mode mode = Mode.FEED_ADULTS;

	public PollidisiacBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaFlowerBlocks.POLLIDISIAC, pos, state);
	}

	@Override
	public void tickFlower() {
		super.tickFlower();

		if (!getLevel().isClientSide && getMana() >= MANA_COST) {
			List<ItemEntity> items = getItems();
			if (!items.isEmpty()) {
				List<Animal> animals = getAnimals();
				feedAnimal(animals, items);
			}
		}
	}

	private @NotNull List<ItemEntity> getItems() {
		var pickupBounds = new AABB(getBlockPos()).inflate(RANGE);
		return getLevel().getEntitiesOfClass(ItemEntity.class, pickupBounds,
				itemEntity -> DelayHelper.canInteractWith(this, itemEntity));
	}

	private @NotNull List<Animal> getAnimals() {
		var bounds = new AABB(getEffectivePos()).inflate(RANGE);
		return getLevel().getEntitiesOfClass(Animal.class, bounds, mode);
	}

	private void feedAnimal(List<Animal> animals, List<ItemEntity> items) {

		Collections.shuffle(animals);

		animals.sort(Comparator.comparing(Animal::isBaby).thenComparingInt(animal -> Math.min(animal.getAge(), 0)));

		boolean did = false;
		for (Animal animal : animals) {

			if (animal.getAge() == 0 && !animal.isInLove() || animal.getAge() < -600 && -animal.getAge() % 100 == 0) {
				for (ItemEntity item : items) {
					if (!animal.isFood(item.getItem())) {
						continue;
					}
					consumeFoodItemAndMana(item);
					did = true;

					if (animal.isBaby()) {
						animal.ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(-animal.getAge()), true);
					} else {
						animal.setInLoveTime(1200);
						((AnimalAccessor) animal).botania_setLoveCause(null);
					}
					getLevel().broadcastEntityEvent(animal, EntityEvent.IN_LOVE_HEARTS);
					break;
				}

				if (getMana() < MANA_COST) {
					break;
				}
			}

			if (!animal.isBaby() && isBrownMooshroomWithoutEffect(animal)) {
				for (ItemEntity item : items) {
					ItemStack stack = item.getItem();
					if (!stack.is(ItemTags.SMALL_FLOWERS)) {
						continue;
					}
					var effect = SuspiciousEffectHolder.tryGet(stack.getItem());
					if (effect == null) {
						continue;
					}
					consumeFoodItemAndMana(item);
					did = true;

					MushroomCowAccessor cowAccessor = (MushroomCowAccessor) animal;
					cowAccessor.setStewEffects(effect.getSuspiciousEffects());
					animal.playSound(SoundEvents.MOOSHROOM_EAT, 2.0F, 1.0F);
					break;
				}

				if (getMana() < MANA_COST) {
					break;
				}
			}
		}
		if (did) {
			sync();
		}
	}

	private void consumeFoodItemAndMana(ItemEntity itemEntity) {
		EntityHelper.shrinkItem(itemEntity);
		addMana(-MANA_COST);
	}

	private static boolean isBrownMooshroomWithoutEffect(Animal animal) {
		if (animal instanceof MushroomCow mushroomCow && mushroomCow.getVariant() == MushroomCow.MushroomType.BROWN) {
			MushroomCowAccessor cowAccessor = (MushroomCowAccessor) animal;
			return cowAccessor.getStewEffects() == null;
		}
		return false;
	}

	@Override
	public RadiusDescriptor getRadius() {
		return RadiusDescriptor.Rectangle.square(getEffectivePos(), RANGE);
	}

	@Override
	public RadiusDescriptor getSecondaryRadius() {
		return getBlockPos().equals(getEffectivePos()) ? null : RadiusDescriptor.Rectangle.square(getBlockPos(), RANGE);
	}

	@Override
	public int getMaxMana() {
		return 120;
	}

	@Override
	public int getColor() {
		return 0xCF4919;
	}

	@NotNull
	public Mode getMode() {
		return this.mode;
	}

	@Override
	public boolean onUsedByWand(@Nullable Player player, ItemStack stack, Direction side) {
		if (player == null || player.isShiftKeyDown()) {
			this.mode = this.mode.getNextMode();
			setChanged();
			sync();

			return true;
		}
		return false;
	}

	@Override
	public void readFromPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.readFromPacketNBT(cmp, registries);
		this.mode = Mode.forName(cmp.getString(TAG_FEEDING_MODE));
	}

	@Override
	public void writeToPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		super.writeToPacketNBT(cmp, registries);
		cmp.putString(TAG_FEEDING_MODE, this.mode.getSerializedName());
	}

	public enum Mode implements StringRepresentable, Predicate<Animal> {
		FEED_ADULTS("feed_adults", Predicate.not(Animal::isBaby)),
		FEED_BABIES("feed_babies", Animal::isBaby),
		FEED_ALL("feed_all", animal -> true);

		@SuppressWarnings("deprecation")
		private static final EnumCodec<Mode> CODEC = StringRepresentable.fromEnum(Mode::values);

		public static Mode forName(String name) {
			return CODEC.byName(name, FEED_ADULTS);
		}

		@NotNull
		private final String name;
		@NotNull
		private final Predicate<Animal> predicate;

		Mode(@NotNull String name, @NotNull Predicate<Animal> predicate) {
			this.name = name;
			this.predicate = predicate;
		}

		@Override
		public boolean test(Animal animal) {
			return predicate.test(animal);
		}

		@NotNull
		@Override
		public String getSerializedName() {
			return this.name;
		}

		@NotNull
		public Mode getNextMode() {
			Mode[] modes = values();
			int nextMode = ordinal() + 1;
			return modes[nextMode % modes.length];
		}
	}

	public static class WandHud extends BindableFlowerWandHud<PollidisiacBlockEntity> {
		public WandHud(PollidisiacBlockEntity flower) {
			super(flower);
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			String filter = I18n.get("botaniamisc.pollidisiac." + flower.getMode().getSerializedName());
			int filterWidth = mc.font.width(filter);
			int filterTextStart = (mc.getWindow().getGuiScaledWidth() - filterWidth) / 2;
			int halfMinWidth = (filterWidth + 4) / 2;
			int centerY = mc.getWindow().getGuiScaledHeight() / 2;

			super.renderHUD(gui, mc, halfMinWidth, halfMinWidth, 40);
			gui.drawString(mc.font, filter, filterTextStart, centerY + 30, flower.getColor());
		}
	}
}
