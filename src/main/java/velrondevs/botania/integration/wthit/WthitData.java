package velrondevs.botania.integration.wthit;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.block_entity.BindableSpecialFlowerBlockEntity;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.api.mana.ManaCollector;
import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.recipe.ContainerRecipeInput;
import velrondevs.botania.api.state.BotaniaStateProperties;
import velrondevs.botania.api.state.enums.AlfheimPortalState;
import velrondevs.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import velrondevs.botania.common.block.block_entity.AvatarBlockEntity;
import velrondevs.botania.common.block.block_entity.BotaniaBlockEntity;
import velrondevs.botania.common.block.block_entity.BreweryBlockEntity;
import velrondevs.botania.common.block.block_entity.CocoonBlockEntity;
import velrondevs.botania.common.block.block_entity.HoveringHourglassBlockEntity;
import velrondevs.botania.common.block.block_entity.IncensePlateBlockEntity;
import velrondevs.botania.common.block.block_entity.LifeImbuerBlockEntity;
import velrondevs.botania.common.block.block_entity.ManaEnchanterBlockEntity;
import velrondevs.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import velrondevs.botania.common.block.block_entity.RunicAltarBlockEntity;
import velrondevs.botania.common.block.block_entity.TerrestrialAgglomerationPlateBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.ManaSpreaderBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.PowerGeneratorBlockEntity;
import velrondevs.botania.common.block.flower.PureDaisyBlockEntity;
import velrondevs.botania.common.block.flower.generating.HydroangeasBlockEntity;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class WthitData {
	public static final String ROOT = "botania";

	public static final String MANA = "mana";
	public static final String MAX_MANA = "maxMana";
	public static final String FLOWER_COLOR = "flowerColor";
	public static final String OUTPUTTING = "outputting";
	public static final String LENS = "lens";
	public static final String TARGET = "target";
	public static final String BOUND = "bound";
	public static final String BURN = "burn";
	public static final String COOLDOWN = "cooldown";
	public static final String DECAY = "decay";
	public static final String DECAY_MAX = "decayMax";
	public static final String DIGESTING = "digesting";
	public static final String OVERGROWTH = "overgrowth";
	public static final String INPUTS = "inputs";
	public static final String OUTPUT = "output";
	public static final String PROGRESS = "progress";
	public static final String FLUID = "fluid";
	public static final String STAGE = "stage";
	public static final String PORTAL = "portal";
	public static final String PYLONS = "pylons";
	public static final String ENERGY = "energy";
	public static final String MAX_ENERGY = "maxEnergy";
	public static final String TIME_LEFT = "timeLeft";
	public static final String TIME_DONE = "timeDone";
	public static final String TIME_TOTAL = "timeTotal";
	public static final String CONVERTING = "converting";
	public static final String ACTIVE = "active";

	private WthitData() {}

	public static boolean isBotania(BlockEntity be) {
		ResourceLocation id = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(be.getType());
		return id != null && BotaniaAPI.MODID.equals(id.getNamespace());
	}

	public static CompoundTag collect(BlockEntity be, boolean server) {
		CompoundTag tag = new CompoundTag();
		Level level = be.getLevel();
		if (level == null || !isBotania(be)) {
			return tag;
		}
		HolderLookup.Provider registries = level.registryAccess();

		if (be instanceof SpecialFlowerBlockEntity flower) {
			collectFlower(tag, flower, registries, server);
			return tag;
		}

		if (be instanceof ManaPool pool) {
			mana(tag, pool.getCurrentMana(), pool.getMaxMana());
			tag.putBoolean(OUTPUTTING, pool.isOutputtingPower());
		} else if (be instanceof ManaSpreaderBlockEntity spreader) {
			mana(tag, spreader.getCurrentMana(), spreader.getMaxMana());
			putStack(tag, LENS, spreader.getItemHandler().getItem(0), registries);
			BlockPos binding = spreader.getBinding();
			if (binding != null) {
				tag.putLong(TARGET, binding.asLong());
			}
		} else if (be instanceof RunicAltarBlockEntity altar) {
			collectRunicAltar(tag, altar, level, registries);
		} else if (be instanceof TerrestrialAgglomerationPlateBlockEntity plate) {
			int target = plate.getTargetMana();
			if (target > 0) {
				mana(tag, plate.getCurrentMana(), target);
				putStacks(tag, INPUTS, plate.getCurrentInputs(), registries);
				putStack(tag, OUTPUT, plate.getCurrentOutput(), registries);
				tag.putFloat(PROGRESS, Math.min(1F, (float) plate.getCurrentMana() / target));
			} else if (plate.getCurrentMana() > 0) {
				tag.putInt(MANA, plate.getCurrentMana());
			}
		} else if (be instanceof PetalApothecaryBlockEntity apothecary) {
			collectApothecary(tag, apothecary, level, registries);
		} else if (be instanceof BreweryBlockEntity brewery) {
			int cost = brewery.getManaCost();
			if (cost > 0 && brewery.recipe != null) {
				mana(tag, brewery.getCurrentMana(), cost);
				ItemStack container = brewery.getItemHandler().getItem(0);
				List<ItemStack> inputs = new ArrayList<>();
				Container inv = brewery.getItemHandler();
				for (int i = 0; i < inv.getContainerSize(); i++) {
					ItemStack stack = inv.getItem(i);
					if (!stack.isEmpty()) {
						inputs.add(stack);
					}
				}
				putStacks(tag, INPUTS, inputs, registries);
				putStack(tag, OUTPUT, brewery.recipe.getOutput(container), registries);
				tag.putFloat(PROGRESS, Math.min(1F, (float) brewery.getCurrentMana() / cost));
			}
		} else if (be instanceof ManaEnchanterBlockEntity enchanter) {
			tag.putString(STAGE, enchanter.stage.name().toLowerCase(Locale.ROOT));
			int required = enchanter.getCurrentMana() + enchanter.getAvailableSpaceForMana();
			if (required > 0 && !enchanter.itemToEnchant.isEmpty()) {
				mana(tag, enchanter.getCurrentMana(), required);
				putStack(tag, OUTPUT, enchanter.itemToEnchant, registries);
			}
		} else if (be instanceof AvatarBlockEntity avatar) {
			mana(tag, avatar.getCurrentMana(), avatar.getMaxMana());
			tag.putBoolean(ACTIVE, avatar.isEnabled());
		} else if (be instanceof LifeImbuerBlockEntity imbuer) {
			mana(tag, imbuer.getCurrentMana(), imbuer.getMaxMana());
		} else if (be instanceof PowerGeneratorBlockEntity generator) {
			tag.putInt(ENERGY, generator.getEnergy());
			tag.putInt(MAX_ENERGY, PowerGeneratorBlockEntity.MAX_ENERGY);
		} else if (be instanceof ManaCollector collector) {
			mana(tag, collector.getCurrentMana(), collector.getMaxMana());
		} else if (be instanceof AlfheimPortalBlockEntity portal) {
			BlockState state = portal.getBlockState();
			if (state.hasProperty(BotaniaStateProperties.ALFPORTAL_STATE)) {
				tag.putBoolean(PORTAL, state.getValue(BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF);
			}
			if (server) {
				tag.putInt(PYLONS, portal.locatePylons(false).size());
			}
		} else if (be instanceof HoveringHourglassBlockEntity hourglass) {
			int total = hourglass.getTotalTime();
			if (total > 0) {
				CompoundTag nbt = packet(hourglass, registries);
				tag.putInt(TIME_DONE, nbt.getInt("time"));
				tag.putInt(TIME_TOTAL, total);
			}
		} else if (be instanceof CocoonBlockEntity cocoon) {
			tag.putInt(TIME_DONE, cocoon.timePassed);
			tag.putInt(TIME_TOTAL, CocoonBlockEntity.TOTAL_TIME);
		} else if (be instanceof IncensePlateBlockEntity plate) {
			if (plate.burning) {
				CompoundTag nbt = packet(plate, registries);
				tag.putInt(TIME_LEFT, nbt.getInt("timeLeft"));
			}
		} else if (be instanceof ManaReceiver receiver && receiver.getCurrentMana() > 0) {
			tag.putInt(MANA, receiver.getCurrentMana());
		}
		return tag;
	}

	private static void collectFlower(CompoundTag tag, SpecialFlowerBlockEntity flower, HolderLookup.Provider registries, boolean server) {
		CompoundTag nbt = new CompoundTag();
		flower.writeToPacketNBT(nbt, registries);

		if (flower instanceof BindableSpecialFlowerBlockEntity<?> bindable) {
			mana(tag, bindable.getMana(), bindable.getMaxMana());
			tag.putInt(FLOWER_COLOR, bindable.getColor());
			BlockPos binding = server ? bindable.getBinding() : bindable.getBindingPos();
			tag.putBoolean(BOUND, binding != null);
			if (binding != null) {
				tag.putLong(TARGET, binding.asLong());
			}
		}

		if (nbt.getInt("burnTime") > 0) {
			tag.putInt(BURN, nbt.getInt("burnTime"));
		}
		if (nbt.getInt("cooldown") > 0) {
			tag.putInt(COOLDOWN, nbt.getInt("cooldown"));
		}
		if (nbt.getInt("digestingMana") > 0) {
			tag.putInt(DIGESTING, nbt.getInt("digestingMana"));
		}
		if (flower instanceof HydroangeasBlockEntity && nbt.contains(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS)) {
			tag.putInt(DECAY, Math.max(0, HydroangeasBlockEntity.DECAY_TIME - nbt.getInt(HydroangeasBlockEntity.TAG_PASSIVE_DECAY_TICKS)));
			tag.putInt(DECAY_MAX, HydroangeasBlockEntity.DECAY_TIME);
		}
		if (flower instanceof PureDaisyBlockEntity) {
			int converting = 0;
			for (int i = 0; nbt.contains("ticksRemaining" + i); i++) {
				if (nbt.getInt("ticksRemaining" + i) > 0) {
					converting++;
				}
			}
			tag.putInt(CONVERTING, converting);
		}
		if (flower.overgrowth) {
			tag.putBoolean(OVERGROWTH, true);
		}
	}

	private static void collectRunicAltar(CompoundTag tag, RunicAltarBlockEntity altar, Level level, HolderLookup.Provider registries) {
		List<ItemStack> inputs = items(altar.getItemHandler());
		int target = altar.getTargetMana();
		if (target > 0) {
			mana(tag, altar.getCurrentMana(), target);
			tag.putFloat(PROGRESS, Math.min(1F, (float) altar.getCurrentMana() / target));
		}
		if (!inputs.isEmpty()) {
			putStacks(tag, INPUTS, inputs, registries);
			ContainerRecipeInput input = new ContainerRecipeInput(altar.getItemHandler());
			level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.RUNE_TYPE, input, level)
					.map(RecipeHolder::value)
					.ifPresent(recipe -> putStack(tag, OUTPUT, recipe.assemble(input, registries), registries));
		}
	}

	private static void collectApothecary(CompoundTag tag, PetalApothecaryBlockEntity apothecary, Level level, HolderLookup.Provider registries) {
		tag.putString(FLUID, apothecary.getFluid().getSerializedName());
		List<ItemStack> inputs = items(apothecary.getItemHandler());
		if (!inputs.isEmpty()) {
			putStacks(tag, INPUTS, inputs, registries);
			ContainerRecipeInput input = new ContainerRecipeInput(apothecary.getItemHandler());
			level.getRecipeManager().getRecipeFor(BotaniaRecipeTypes.PETAL_TYPE, input, level)
					.map(RecipeHolder::value)
					.ifPresent(recipe -> {
						putStack(tag, OUTPUT, recipe.assemble(input, registries), registries);
						tag.putFloat(PROGRESS, 1F);
					});
		}
	}

	private static List<ItemStack> items(Container inv) {
		List<ItemStack> list = new ArrayList<>();
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if (stack.isEmpty()) {
				break;
			}
			list.add(stack);
		}
		return list;
	}

	private static CompoundTag packet(BotaniaBlockEntity be, HolderLookup.Provider registries) {
		CompoundTag nbt = new CompoundTag();
		be.writePacketNBT(nbt, registries);
		return nbt;
	}

	private static void mana(CompoundTag tag, int mana, int max) {
		tag.putInt(MANA, mana);
		tag.putInt(MAX_MANA, max);
	}

	private static void putStack(CompoundTag tag, String key, ItemStack stack, HolderLookup.Provider registries) {
		if (!stack.isEmpty()) {
			tag.put(key, stack.save(registries));
		}
	}

	private static void putStacks(CompoundTag tag, String key, List<ItemStack> stacks, HolderLookup.Provider registries) {
		List<ItemStack> merged = new ArrayList<>();
		for (ItemStack stack : stacks) {
			if (stack.isEmpty()) {
				continue;
			}
			boolean found = false;
			for (ItemStack existing : merged) {
				if (ItemStack.isSameItemSameComponents(existing, stack)) {
					existing.grow(stack.getCount());
					found = true;
					break;
				}
			}
			if (!found) {
				merged.add(stack.copy());
			}
		}
		if (merged.isEmpty()) {
			return;
		}
		ListTag list = new ListTag();
		for (ItemStack stack : merged) {
			CompoundTag entry = new CompoundTag();
			entry.put("stack", stack.copyWithCount(1).save(registries));
			entry.putInt("count", stack.getCount());
			list.add(entry);
		}
		tag.put(key, list);
	}

	public static ItemStack getStack(CompoundTag tag, String key, HolderLookup.Provider registries) {
		return tag.contains(key, Tag.TAG_COMPOUND) ? ItemStack.parseOptional(registries, tag.getCompound(key)) : ItemStack.EMPTY;
	}

	public static List<ItemStack> getStacks(CompoundTag tag, String key, HolderLookup.Provider registries) {
		List<ItemStack> result = new ArrayList<>();
		ListTag list = tag.getList(key, Tag.TAG_COMPOUND);
		for (int i = 0; i < list.size(); i++) {
			CompoundTag entry = list.getCompound(i);
			ItemStack stack = ItemStack.parseOptional(registries, entry.getCompound("stack"));
			if (!stack.isEmpty()) {
				stack.setCount(Math.max(1, entry.getInt("count")));
				result.add(stack);
			}
		}
		return result;
	}

	@Nullable
	public static BlockPos getPos(CompoundTag tag, String key) {
		return tag.contains(key, Tag.TAG_LONG) ? BlockPos.of(tag.getLong(key)) : null;
	}
}
