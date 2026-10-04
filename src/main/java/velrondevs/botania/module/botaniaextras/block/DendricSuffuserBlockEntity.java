package velrondevs.botania.module.botaniaextras.block;

import com.google.common.base.Predicates;
import com.google.common.base.Suppliers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.internal.VanillaPacketDispatcher;
import velrondevs.botania.api.mana.ManaPool;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.spark.ManaSpark;
import velrondevs.botania.api.mana.spark.SparkAttachable;
import velrondevs.botania.api.mana.spark.SparkHelper;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.block.block_entity.BotaniaBlockEntity;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasRecipeTypes;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasTags;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.crafting.TreeSuffusionRecipe;
import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.XplatAbstractions;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DendricSuffuserBlockEntity extends BotaniaBlockEntity implements ManaReceiver, SparkAttachable {
	private static final String TAG_STAGE = "stage";
	private static final String TAG_MANA = "mana";
	private static final String TAG_MANA_REQUIRED = "manaRequired";
	private static final String TAG_THROTTLE = "throttle";
	private static final int CRAFT_EFFECT_EVENT = 0;
	private static final int CHECK_INTERVAL = 10;

	public static final int CORE_HEIGHT = 4;
	public static final int SAPLING_DEPTH = 3;

	private static final int[][] PLATFORMS = {{-3, 3}, {-4, 0}, {0, 4}, {-3, -3}, {0, -4}, {3, -3}, {4, 0}, {3, 3}};
	private static final int[][] PLANKS = {{2, 2}, {2, 1}, {2, -1}, {2, -2}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}, {-2, 2}, {-2, 1}, {-2, -1}, {-2, -2}};
	private static final int[][] OBSIDIAN = {{3, 2}, {3, 1}, {3, 0}, {3, -1}, {3, -2}, {2, 3}, {2, 0}, {2, -3}, {1, 3}, {1, 0}, {1, -3}, {0, 3}, {0, 2}, {0, 1}, {0, -1}, {0, -2}, {0, -3},
			{-1, 3}, {-1, 0}, {-1, -3}, {-2, 3}, {-2, 0}, {-2, -3}, {-3, 2}, {-3, 1}, {-3, 0}, {-3, -1}, {-3, -2}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

	private static final Supplier<IStateMatcher> OBSIDIAN_MATCHER = Suppliers.memoize(() -> PatchouliAPI.get().predicateMatcher(
			Blocks.OBSIDIAN,
			state -> state.is(Blocks.OBSIDIAN) || state.is(Blocks.CRYING_OBSIDIAN)));
	private static final Supplier<IStateMatcher> PLANKS_MATCHER = Suppliers.memoize(() -> PatchouliAPI.get().predicateMatcher(
			BotaniaExtrasWoods.sets().get(0).planks(),
			state -> state.is(BotaniaExtrasTags.Blocks.SUFFUSER_PLANKS)));
	private static final Supplier<IStateMatcher> DIRT_MATCHER = Suppliers.memoize(() -> PatchouliAPI.get().predicateMatcher(
			BotaniaExtrasBlocks.getDirt(null),
			state -> state.is(BotaniaExtrasTags.Blocks.IRIDESCENT_DIRT)));
	private static final Supplier<IStateMatcher> PLATFORM_MATCHER = Suppliers.memoize(() -> PatchouliAPI.get().predicateMatcher(
			BotaniaExtrasBlocks.manasteelItemPlatform,
			state -> state.getBlock() instanceof ItemPlatformBlock));
	private static final Supplier<IStateMatcher> FORMED_CORE_MATCHER = Suppliers.memoize(() -> PatchouliAPI.get().predicateMatcher(
			BotaniaExtrasBlocks.dendricSuffuser,
			state -> state.getBlock() instanceof DendricSuffuserBlock));

	private static String[][] pattern(boolean showSapling) {
		String[][] layers = new String[CORE_HEIGHT + 1][9];
		for (String[] layer : layers) {
			for (int row = 0; row < 9; row++) {
				layer[row] = "_________";
			}
		}
		put(layers, CORE_HEIGHT, 0, 0, 'C');
		for (int[] p : PLATFORMS) {
			put(layers, CORE_HEIGHT - 1, p[0], p[1], 'P');
			put(layers, CORE_HEIGHT - SAPLING_DEPTH, p[0], p[1], 'D');
		}
		if (showSapling) {
			put(layers, CORE_HEIGHT - SAPLING_DEPTH, 0, 0, 'S');
		}
		for (int[] p : OBSIDIAN) {
			put(layers, CORE_HEIGHT - 4, p[0], p[1], 'B');
		}
		for (int[] p : PLANKS) {
			put(layers, CORE_HEIGHT - 4, p[0], p[1], 'W');
		}
		put(layers, CORE_HEIGHT - 4, 0, 0, '0');
		String[][] topFirst = new String[layers.length][];
		for (int i = 0; i < layers.length; i++) {
			topFirst[i] = layers[layers.length - 1 - i];
		}
		return topFirst;
	}

	private static void put(String[][] layers, int y, int x, int z, char c) {
		char[] row = layers[y][z + 4].toCharArray();
		row[x + 4] = c;
		layers[y][z + 4] = new String(row);
	}

	public static final Supplier<IMultiblock> MULTIBLOCK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			pattern(true),
			'C', PLANKS_MATCHER.get(),
			'P', BotaniaBlocks.manaPylon,
			'D', PLATFORM_MATCHER.get(),
			'S', BotaniaExtrasWoods.sapling,
			'B', OBSIDIAN_MATCHER.get(),
			'W', PLANKS_MATCHER.get(),
			'0', DIRT_MATCHER.get()));

	private static final Supplier<IMultiblock> UNFORMED_CHECK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			pattern(false),
			'C', PLANKS_MATCHER.get(),
			'P', BotaniaBlocks.manaPylon,
			'D', PLATFORM_MATCHER.get(),
			'B', OBSIDIAN_MATCHER.get(),
			'W', PLANKS_MATCHER.get(),
			'0', DIRT_MATCHER.get()));

	private static final Supplier<IMultiblock> FORMED_CHECK = Suppliers.memoize(() -> PatchouliAPI.get().makeMultiblock(
			pattern(false),
			'C', FORMED_CORE_MATCHER.get(),
			'P', BotaniaBlocks.manaPylon,
			'D', PLATFORM_MATCHER.get(),
			'B', OBSIDIAN_MATCHER.get(),
			'W', PLANKS_MATCHER.get(),
			'0', DIRT_MATCHER.get()));

	private record PlatformInput(List<ItemStack> items) implements RecipeInput {
		@Override
		public ItemStack getItem(int index) {
			return items.get(index);
		}

		@Override
		public int size() {
			return items.size();
		}
	}

	private int stage = 0;
	private int mana = 0;
	private int manaRequired = 0;
	private int throttle = -1;
	private int ticksAlive = 0;

	public DendricSuffuserBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaExtrasBlockEntities.DENDRIC_SUFFUSER, pos, state);
	}

	public static boolean canSuffuserExist(Level level, BlockPos corePos) {
		return UNFORMED_CHECK.get().validate(level, corePos.below(CORE_HEIGHT), Rotation.NONE);
	}

	private boolean isFormed() {
		return FORMED_CHECK.get().validate(level, worldPosition.below(CORE_HEIGHT), Rotation.NONE);
	}

	public int getSignal() {
		return stage == 1 ? 1 : 0;
	}

	public int getStage() {
		return stage;
	}

	public int getManaRequired() {
		return manaRequired;
	}

	private List<ItemPlatformBlockEntity> platforms() {
		List<ItemPlatformBlockEntity> list = new ArrayList<>(PLATFORMS.length);
		for (int[] p : PLATFORMS) {
			if (level.getBlockEntity(worldPosition.offset(p[0], -SAPLING_DEPTH, p[1])) instanceof ItemPlatformBlockEntity platform) {
				list.add(platform);
			}
		}
		return list;
	}

	private PlatformInput collectInput(List<ItemPlatformBlockEntity> owners) {
		List<ItemStack> items = new ArrayList<>();
		for (ItemPlatformBlockEntity platform : platforms()) {
			ItemStack stack = platform.getDisplayed();
			if (!stack.isEmpty()) {
				items.add(stack);
				owners.add(platform);
			}
		}
		return new PlatformInput(items);
	}

	private TreeSuffusionRecipe findRecipe(PlatformInput input) {
		if (input.items().isEmpty() || !(level.getBlockState(worldPosition.below(SAPLING_DEPTH)).getBlock() instanceof IridescentSaplingBlock)) {
			return null;
		}
		return level.getRecipeManager().getRecipeFor(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE, input, level)
				.map(RecipeHolder::value).orElse(null);
	}

	public ItemStack hudIcon() {
		TreeSuffusionRecipe recipe = findRecipe(collectInput(new ArrayList<>()));
		return recipe == null ? ItemStack.EMPTY : new ItemStack(recipe.getOutputState().getBlock());
	}

	public static void commonTick(Level level, BlockPos pos, BlockState state, DendricSuffuserBlockEntity self) {
		self.ticksAlive++;
		if (level.isClientSide) {
			self.clientTick();
			return;
		}
		if (self.ticksAlive % CHECK_INTERVAL == 0 && !self.isFormed()) {
			level.setBlockAndUpdate(pos, DendricSuffuserBlock.planksFor(state).defaultBlockState());
			XplatAbstractions.INSTANCE.sendToNear(level, pos, new BotaniaEffectPacket(EffectType.ENCHANTER_DESTROY,
					pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
			level.playSound(null, pos, BotaniaSounds.enchanterFade, SoundSource.BLOCKS, 1F, 1F);
			return;
		}

		List<ItemPlatformBlockEntity> owners = new ArrayList<>();
		PlatformInput input = self.collectInput(owners);
		TreeSuffusionRecipe recipe = self.findRecipe(input);

		if (recipe == null) {
			if (self.stage != 0 || self.mana != 0 || self.manaRequired != 0) {
				self.stage = 0;
				self.mana = 0;
				self.manaRequired = 0;
				self.throttle = -1;
				self.sync();
			}
			return;
		}

		switch (self.stage) {
			case 0 -> {
				self.mana = 0;
				self.manaRequired = recipe.getMana();
				self.throttle = recipe.getThrottle();
				self.stage = 1;
				self.sync();
			}
			default -> {
				if (self.mana >= self.manaRequired) {
					self.craft(level, pos, recipe, input, owners);
				} else {
					self.gatherMana();
				}
			}
		}
	}

	private void gatherMana() {
		ManaSpark spark = getAttachedSpark();
		if (spark != null) {
			var otherSparks = SparkHelper.getSparksAround(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, spark.getNetwork());
			for (var otherSpark : otherSparks) {
				if (spark != otherSpark && otherSpark.getAttachedManaReceiver() instanceof ManaPool) {
					otherSpark.registerTransfer(spark);
				}
			}
		}
		if (ticksAlive % 5 == 0) {
			sync();
		}
	}

	private void craft(Level level, BlockPos pos, TreeSuffusionRecipe recipe, PlatformInput input, List<ItemPlatformBlockEntity> owners) {
		int[] slots = recipe.assign(input);
		if (slots == null) {
			return;
		}
		level.setBlock(pos.below(SAPLING_DEPTH), recipe.getOutputState(), Block.UPDATE_ALL);
		for (int slot : slots) {
			owners.get(slot).getItemHandler().setItem(0, ItemStack.EMPTY);
		}
		stage = 0;
		mana = 0;
		manaRequired = 0;
		throttle = -1;
		level.blockEvent(pos, getBlockState().getBlock(), CRAFT_EFFECT_EVENT, 0);
		sync();
	}

	private void clientTick() {
		if (stage != 1) {
			return;
		}
		if (ticksAlive % 10 == 0) {
			for (int i = 0; i < 36; i++) {
				double radian = Math.toRadians(i * 10);
				double x = worldPosition.getX() + 0.5 + Math.cos(radian) * 3;
				double z = worldPosition.getZ() + 0.5 + Math.sin(radian) * 3;
				WispParticleData data = WispParticleData.wisp(0.3F, 0F, 1F, 1F);
				level.addParticle(data, x, worldPosition.getY() - SAPLING_DEPTH + 0.5, z, 0, 0.01, 0);
			}
		}
		for (int[] p : PLATFORMS) {
			if (level.getBlockEntity(worldPosition.offset(p[0], -SAPLING_DEPTH, p[1])) instanceof ItemPlatformBlockEntity platform && !platform.getDisplayed().isEmpty() && mana > 0) {
				if (level.random.nextInt(3) == 0) {
					double fromX = worldPosition.getX() + p[0] + 0.5;
					double fromY = worldPosition.getY() - SAPLING_DEPTH + 1.2;
					double fromZ = worldPosition.getZ() + p[1] + 0.5;
					double dx = (worldPosition.getX() + 0.5 - fromX) / 30;
					double dy = (worldPosition.getY() + 0.5 - fromY) / 30;
					double dz = (worldPosition.getZ() + 0.5 - fromZ) / 30;
					WispParticleData data = WispParticleData.wisp(0.2F + level.random.nextFloat() * 0.1F, 1F, 1F, 1F);
					level.addParticle(data, fromX, fromY, fromZ, dx, dy, dz);
				}
			}
		}
	}

	@Override
	public boolean triggerEvent(int event, int param) {
		if (event == CRAFT_EFFECT_EVENT) {
			if (level.isClientSide) {
				for (int i = 0; i < 25; i++) {
					SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), (float) Math.random(), (float) Math.random(), (float) Math.random(), 10);
					level.addParticle(data, worldPosition.getX() + Math.random() * 0.4 + 0.3, worldPosition.getY() - SAPLING_DEPTH + 0.5 + Math.random(), worldPosition.getZ() + Math.random() * 0.4 + 0.3, 0, 0, 0);
				}
				level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), BotaniaSounds.enchanterEnchant, SoundSource.BLOCKS, 1F, 1F, false);
			}
			return true;
		}
		return super.triggerEvent(event, param);
	}

	public void sync() {
		setChanged();
		VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
	}

	@Override
	public void writePacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		cmp.putInt(TAG_STAGE, stage);
		cmp.putInt(TAG_MANA, mana);
		cmp.putInt(TAG_MANA_REQUIRED, manaRequired);
		cmp.putInt(TAG_THROTTLE, throttle);
	}

	@Override
	public void readPacketNBT(CompoundTag cmp, HolderLookup.Provider registries) {
		stage = cmp.getInt(TAG_STAGE);
		mana = cmp.getInt(TAG_MANA);
		manaRequired = cmp.getInt(TAG_MANA_REQUIRED);
		throttle = cmp.contains(TAG_THROTTLE) ? cmp.getInt(TAG_THROTTLE) : -1;
	}

	@Override
	public Level getManaReceiverLevel() {
		return getLevel();
	}

	@Override
	public BlockPos getManaReceiverPos() {
		return getBlockPos();
	}

	@Override
	public int getCurrentMana() {
		return mana;
	}

	@Override
	public boolean isFull() {
		return mana >= manaRequired;
	}

	@Override
	public void receiveMana(int mana) {
		this.mana = Math.min(manaRequired, this.mana + mana);
	}

	@Override
	public boolean canReceiveManaFromBursts() {
		return manaRequired > 0;
	}

	@Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public ManaSpark getAttachedSpark() {
		List<Entity> sparks = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition.getX(), worldPosition.getY() + 1, worldPosition.getZ(), worldPosition.getX() + 1, worldPosition.getY() + 2, worldPosition.getZ() + 1), Predicates.instanceOf(ManaSpark.class));
		if (sparks.size() == 1) {
			return (ManaSpark) sparks.get(0);
		}
		return null;
	}

	@Override
	public boolean areIncomingTranfersDone() {
		return stage != 1;
	}

	@Override
	public int getAvailableSpaceForMana() {
		int space = Math.max(0, manaRequired - mana);
		if (throttle > 0) {
			return Math.min(throttle, space);
		}
		return space;
	}

	public static class WandHud implements WandHUD {
		private final DendricSuffuserBlockEntity suffuser;

		public WandHud(DendricSuffuserBlockEntity suffuser) {
			this.suffuser = suffuser;
		}

		@Override
		public void renderHUD(GuiGraphics gui, Minecraft mc) {
			if (suffuser.manaRequired > 0 && suffuser.stage == 1) {
				int x = mc.getWindow().getGuiScaledWidth() / 2 + 8;
				int y = mc.getWindow().getGuiScaledHeight() / 2 - 12;
				RenderHelper.renderHUDBox(gui, x, y, x + 24, y + 24);
				RenderHelper.renderProgressPie(gui, x + 4, y + 4, (float) suffuser.mana / (float) suffuser.manaRequired, suffuser.hudIcon());
			}
		}
	}
}
