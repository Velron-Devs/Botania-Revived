package velrondevs.botania.xplat;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.*;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.network.PacketDistributor;
import velrondevs.botania.Botania;
import velrondevs.botania.capability.CapabilityUtil;

import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.api.block.*;
import velrondevs.botania.api.block_entity.SpecialFlowerBlockEntity;
import velrondevs.botania.api.corporea.CorporeaIndexRequestEvent;
import velrondevs.botania.api.corporea.CorporeaRequestEvent;
import velrondevs.botania.api.corporea.CorporeaRequestMatcher;
import velrondevs.botania.api.corporea.CorporeaSpark;
import velrondevs.botania.api.item.AvatarWieldable;
import velrondevs.botania.api.item.BlockProvider;
import velrondevs.botania.api.item.CoordBoundItem;
import velrondevs.botania.api.item.Relic;
import velrondevs.botania.api.mana.*;
import velrondevs.botania.api.mana.spark.SparkAttachable;
import velrondevs.botania.api.recipe.ElvenPortalUpdateEvent;
import velrondevs.botania.common.block.SpecialFlowerBlock;
import velrondevs.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.internal_caps.*;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.integration.curios.CurioIntegration;
import velrondevs.botania.mixin.AbstractFurnaceBlockEntityCanBurnAccessor;
import velrondevs.botania.network.BotaniaPacket;
import velrondevs.botania.registry.BotaniaAttachments;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class XplatImpl implements XplatAbstractions {
	@Override
	public boolean isForge() {
		return true;
	}

	@Override
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public boolean isDevEnvironment() {
		return !FMLLoader.isProduction();
	}

	@Override
	public boolean isPhysicalClient() {
		return FMLLoader.getDist() == Dist.CLIENT;
	}

	@Override
	public String getBotaniaVersion() {
		return ModList.get().getModContainerById(LibMisc.MOD_ID).get()
				.getModInfo().getVersion().toString();
	}

	@Nullable
	@Override
	public AvatarWieldable findAvatarWieldable(ItemStack stack) {
		return stack.getCapability(BotaniaCapabilities.AVATAR_WIELDABLE);
	}

	@Nullable
	@Override
	public BlockProvider findBlockProvider(ItemStack stack) {
		return stack.getCapability(BotaniaCapabilities.BLOCK_PROVIDER);
	}

	@Nullable
	@Override
	public CoordBoundItem findCoordBoundItem(ItemStack stack) {
		return stack.getCapability(BotaniaCapabilities.COORD_BOUND_ITEM);
	}

	@Nullable
	@Override
	public ManaItem findManaItem(ItemStack stack) {
		return stack.getCapability(BotaniaCapabilities.MANA_ITEM);
	}

	@Nullable
	@Override
	public Relic findRelic(ItemStack stack) {
		return stack.getCapability(BotaniaCapabilities.RELIC);
	}

	@Nullable
	@Override
	public ExoflameHeatable findExoflameHeatable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.EXOFLAME_HEATABLE, level, pos, state, be);
	}

	@Nullable
	@Override
	public HornHarvestable findHornHarvestable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.HORN_HARVEST, level, pos, state, be);
	}

	@Nullable
	@Override
	public HourglassTrigger findHourglassTrigger(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.HOURGLASS_TRIGGER, level, pos, state, be);
	}

	@Nullable
	@Override
	public ManaCollisionGhost findManaGhost(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.MANA_GHOST, level, pos, state, be);
	}

	@Nullable
	@Override
	public ManaReceiver findManaReceiver(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be, @Nullable Direction direction) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.MANA_RECEIVER, level, pos, state, be, direction);
	}

	@Nullable
	@Override
	public SparkAttachable findSparkAttachable(Level level, BlockPos pos, BlockState blockState, @Nullable BlockEntity be, Direction direction) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.SPARK_ATTACHABLE, level, pos, blockState, be, direction);
	}

	@Nullable
	@Override
	public ManaTrigger findManaTrigger(Level level, BlockPos pos, BlockState state, @org.jetbrains.annotations.Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.MANA_TRIGGER, level, pos, state, be);
	}

	@Nullable
	@Override
	public Wandable findWandable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.WANDABLE, level, pos, state, be);
	}

	@Nullable
	@Override
	public PhantomInkableBlock findPhantomInkable(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be) {
		return CapabilityUtil.findCapability(BotaniaCapabilities.PHANTOM_INKABLE, level, pos, state, be);
	}

	@Override
	public boolean isFluidContainer(ItemEntity item) {
		return item.getItem().getCapability(Capabilities.FluidHandler.ITEM) != null;
	}

	@Override
	public boolean extractFluidFromItemEntity(ItemEntity item, Fluid fluid) {
		var h = item.getItem().getCapability(Capabilities.FluidHandler.ITEM);
		if (h == null) {
			return false;
		}
		var extracted = h.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
		var success = extracted.getFluid() == fluid && extracted.getAmount() == FluidType.BUCKET_VOLUME;
		if (success) {
			h.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
			item.setItem(h.getContainer());
		}
		return success;
	}

	@Override
	public boolean extractFluidFromPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var stack = player.getItemInHand(hand);
		var h = stack.getCapability(Capabilities.FluidHandler.ITEM);
		if (h == null) {
			return false;
		}
		var extracted = h.drain(FluidType.BUCKET_VOLUME, IFluidHandler.FluidAction.SIMULATE);
		var success = extracted.getFluid() == fluid && extracted.getAmount() == FluidType.BUCKET_VOLUME;
		if (success && !player.getAbilities().instabuild) {
			h.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
			player.setItemInHand(hand, h.getContainer());
		}
		return success;
	}

	@Override
	public boolean insertFluidIntoPlayerItem(Player player, InteractionHand hand, Fluid fluid) {
		var stack = player.getItemInHand(hand);
		if (stack.isEmpty()) {
			return false;
		}

		ItemStack toFill = stack.copy();
		toFill.setCount(1);

		var fluidHandler = toFill.getCapability(Capabilities.FluidHandler.ITEM);
		if (fluidHandler != null) {
			var fluidToFill = new FluidStack(fluid, FluidType.BUCKET_VOLUME);
			int filled = fluidHandler.fill(fluidToFill, IFluidHandler.FluidAction.SIMULATE);

			if (filled == FluidType.BUCKET_VOLUME) {
				if (!player.getAbilities().instabuild) {
					fluidHandler.fill(fluidToFill, IFluidHandler.FluidAction.EXECUTE);
					stack.shrink(1);
					ItemStack result = fluidHandler.getContainer();
					if (stack.isEmpty()) {
						player.setItemInHand(hand, result);
					} else {
						player.getInventory().placeItemBackInInventory(result);
					}
				}
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack fillItemWithWater(ItemStack stackToFill, Player player) {
		ItemStack split = stackToFill.copyWithCount(1);
		Optional<IFluidHandlerItem> optionalHandler = FluidUtil.getFluidHandler(split);
		if (optionalHandler.isPresent()) {
			var handler = optionalHandler.get();
			if (handler.fill(new FluidStack(Fluids.WATER, FluidType.BUCKET_VOLUME),
					IFluidHandler.FluidAction.EXECUTE) > 0) {
				return handler.getContainer();
			}
		} else if (split.is(Items.GLASS_BOTTLE)) {
			return PotionContents.createItemStack(Items.POTION, Potions.WATER);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public boolean hasInventory(Level level, BlockPos pos, Direction sideOfPos) {
		var state = level.getBlockState(pos);
		return level.getCapability(Capabilities.ItemHandler.BLOCK, pos, sideOfPos) != null
				|| state.getBlock() instanceof WorldlyContainerHolder wch
						&& wch.getContainer(state, level, pos).getSlotsForFace(sideOfPos).length > 0;
	}

	@Override
	public ItemStack insertToInventory(Level level, BlockPos pos, Direction sideOfPos, ItemStack toInsert, boolean simulate) {
		var state = level.getBlockState(pos);
		IItemHandler handler = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, sideOfPos);
		if (handler == null && state.getBlock() instanceof WorldlyContainerHolder wch) {
			handler = new SidedInvWrapper(wch.getContainer(state, level, pos), sideOfPos);
		}
		if (handler == null) {
			return toInsert;
		}

		if (simulate || !state.is(BotaniaTags.Blocks.SINGLE_ITEM_INSERT)) {
			return ItemHandlerHelper.insertItemStacked(handler, toInsert, simulate);
		}

		int maxInserts = toInsert.getCount();
		for (int i = 0; i < maxInserts; i++) {
			ItemStack single = toInsert.copyWithCount(1);
			if (!ItemHandlerHelper.insertItemStacked(handler, single, false).isEmpty()) {
				break;
			}
			toInsert.setCount(toInsert.getCount() - 1);
		}
		return toInsert;
	}

	@Override
	public EthicalComponent ethicalComponent(PrimedTnt tnt) {
		return tnt.getData(BotaniaAttachments.TNT_ETHICAL);
	}

	@Override
	public SpectralRailComponent ghostRailComponent(AbstractMinecart cart) {
		return cart.getData(BotaniaAttachments.GHOST_RAIL);
	}

	@Override
	public ItemFlagsComponent itemFlagsComponent(ItemEntity item) {
		return item.getData(BotaniaAttachments.INTERNAL_ITEM);
	}

	@Override
	public KeptItemsComponent keptItemsComponent(Player player, boolean reviveCaps) {
		return player.getData(BotaniaAttachments.KEPT_ITEMS);
	}

	@Nullable
	@Override
	public LooniumComponent looniumComponent(LivingEntity entity) {
		return entity instanceof Mob ? entity.getData(BotaniaAttachments.LOONIUM_DROP) : null;
	}

	@Override
	public NarslimmusComponent narslimmusComponent(Slime slime) {
		return slime.getData(BotaniaAttachments.NARSLIMMUS);
	}

	@Override
	public TigerseyeComponent tigersEyeComponent(Creeper creeper) {
		return creeper.getData(BotaniaAttachments.TIGERSEYE);
	}

	@Override
	public boolean fireCorporeaRequestEvent(CorporeaRequestMatcher matcher, int itemCount, CorporeaSpark spark, boolean dryRun) {
		return NeoForge.EVENT_BUS.post(new CorporeaRequestEvent(matcher, itemCount, spark, dryRun)).isCanceled();
	}

	@Override
	public boolean fireCorporeaIndexRequestEvent(ServerPlayer player, CorporeaRequestMatcher request, int count, CorporeaSpark spark) {
		return NeoForge.EVENT_BUS.post(new CorporeaIndexRequestEvent(player, request, count, spark)).isCanceled();
	}

	@Override
	public void fireManaItemEvent(Player player, List<ItemStack> toReturn) {
		NeoForge.EVENT_BUS.post(new ManaItemsEvent(player, toReturn));
	}

	@Override
	public float fireManaDiscountEvent(Player player, float discount, ItemStack tool) {
		var evt = new ManaDiscountEvent(player, discount, tool);
		NeoForge.EVENT_BUS.post(evt);
		return evt.getDiscount();
	}

	@Override
	public boolean fireManaProficiencyEvent(Player player, ItemStack tool, boolean proficient) {
		var evt = new ManaProficiencyEvent(player, tool, proficient);
		NeoForge.EVENT_BUS.post(evt);
		return evt.isProficient();
	}

	@Override
	public void fireElvenPortalUpdateEvent(BlockEntity portal, AABB bounds, boolean open, List<ItemStack> stacksInside) {
		NeoForge.EVENT_BUS.post(new ElvenPortalUpdateEvent(portal, bounds, open, stacksInside));
	}

	@Override
	public void fireManaNetworkEvent(ManaReceiver thing, ManaBlockType type, ManaNetworkAction action) {
		NeoForge.EVENT_BUS.post(new ManaNetworkEvent(thing, type, action));
	}

	@Override
	public Packet<? super ClientGamePacketListener> toVanillaClientboundPacket(BotaniaPacket packet) {
		return new ClientboundCustomPayloadPacket(packet);
	}

	@Override
	public void sendToPlayer(Player player, BotaniaPacket packet) {
		if (!player.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, packet);
		}
	}

	@Override
	public void sendToNear(Level level, BlockPos pos, BotaniaPacket packet) {
		if (level instanceof ServerLevel serverLevel) {
			var chunkpos = new ChunkPos(pos);
			var players = serverLevel.getChunkSource().chunkMap.getPlayers(chunkpos, false);
			var vanillaPacket = new ClientboundCustomPayloadPacket(packet);
			for (var player : players) {
				if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 64 * 64) {
					player.connection.send(vanillaPacket);
				}
			}
		}
	}

	@Override
	public void sendToTracking(Entity e, BotaniaPacket packet) {
		if (!e.level().isClientSide) {
			PacketDistributor.sendToPlayersTrackingEntityAndSelf(e, packet);
		}
	}

	@Override
	public boolean isSpecialFlowerBlock(Block b) {
		return b instanceof SpecialFlowerBlock;
	}

	@Override
	public FlowerBlock createSpecialFlowerBlock(Holder<MobEffect> effect, int effectDuration,
			BlockBehaviour.Properties props,
			Supplier<BlockEntityType<? extends SpecialFlowerBlockEntity>> beType,
			boolean hasComparatorOutput) {
		return new SpecialFlowerBlock(effect, effectDuration, props, beType, hasComparatorOutput);
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
		return BlockEntityType.Builder.of(func::apply, blocks).build(null);
	}

	@Override
	public void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener) {
		switch (type) {
			case CLIENT_RESOURCES -> Botania.modBus().addListener(
					(RegisterClientReloadListenersEvent e) -> e.registerReloadListener(listener));
			case SERVER_DATA -> NeoForge.EVENT_BUS.addListener(
					(AddReloadListenerEvent e) -> e.addListener(listener));
		}
	}

	@Override
	public Item.Properties defaultItemBuilder() {
		return new Item.Properties();
	}

	@Override
	public Item.Properties noRepairOnForge(Item.Properties builder) {
		return builder.setNoRepair();
	}

	@Override
	public <T extends AbstractContainerMenu> MenuType<T> createMenuType(TriFunction<Integer, Inventory, RegistryFriendlyByteBuf, T> constructor) {
		return IMenuTypeExtension.create(constructor::apply);
	}

	@Nullable
	@Override
	public EquipmentHandler tryCreateEquipmentHandler() {
		if (XplatAbstractions.INSTANCE.isModLoaded("curios")) {
			CurioIntegration.init();
			return new CurioIntegration();
		}
		return null;
	}

	@Override
	public void openMenu(ServerPlayer player, MenuProvider menu, Consumer<RegistryFriendlyByteBuf> writeInitialData) {
		player.openMenu(menu, writeInitialData);
	}

	@Override
	public Holder<Attribute> getReachDistanceAttribute() {
		return Attributes.BLOCK_INTERACTION_RANGE;
	}

	@Override
	public Holder<Attribute> getStepHeightAttribute() {
		return Attributes.STEP_HEIGHT;
	}

	@Override
	public TagKey<Block> getOreTag() {
		return Tags.Blocks.ORES;
	}

	@Override
	public boolean isInGlassTag(BlockState state) {
		return state.is(Tags.Blocks.GLASS_BLOCKS) || state.is(Tags.Blocks.GLASS_PANES);
	}

	@Override
	public boolean canFurnaceBurn(AbstractFurnaceBlockEntity furnace, @Nullable RecipeHolder<?> recipe, NonNullList<ItemStack> items, int maxStackSize) {
		return AbstractFurnaceBlockEntityCanBurnAccessor.callCanBurn(furnace.getLevel().registryAccess(), recipe, items, maxStackSize, furnace);
	}

	@Override
	public Fluid getBucketFluid(BucketItem item) {
		return item.content;
	}

	@Override
	public int getSmeltingBurnTime(ItemStack stack) {
		return stack.getBurnTime(RecipeType.SMELTING);
	}

	@Override
	public boolean preventsRemoteMovement(ItemEntity entity) {
		return entity.getPersistentData().getBoolean("PreventRemoteMovement");
	}

	public static final Map<Block, Block> CUSTOM_STRIPPABLES = new HashMap<>();

	@Override
	public void addAxeStripping(Block input, Block output) {
		CUSTOM_STRIPPABLES.put(input, output);
	}

	@Override
	public int transferEnergyToNeighbors(Level level, BlockPos pos, int energy) {
		for (Direction e : Direction.values()) {
			BlockPos neighbor = pos.relative(e);
			if (!level.hasChunkAt(neighbor)) {
				continue;
			}

			IEnergyStorage storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighbor, e.getOpposite());
			if (storage == null) {
				storage = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighbor, null);
			}

			if (storage != null) {
				energy -= storage.receiveEnergy(energy, false);

				if (energy <= 0) {
					return 0;
				}
			}
		}
		return energy;
	}

	@Nullable
	@Override
	public FoodProperties getFoodProperties(ItemStack stack) {
		return stack.getFoodProperties(null);
	}

	@Override
	public boolean canToolLightFire(ItemStack stack) {
		return stack.is(Items.FLINT_AND_STEEL) || stack.canPerformAction(ItemAbilities.FIRESTARTER_LIGHT);
	}

	@Override
	public boolean isRedStringContainerTarget(BlockEntity be) {
		if (be.getLevel() == null) {
			return false;
		}
		for (Direction dir : Direction.values()) {
			if (be.getLevel().getCapability(Capabilities.ItemHandler.BLOCK, be.getBlockPos(), be.getBlockState(), be, dir) != null) {
				return true;
			}
		}
		return false;
	}

	@Override
	public RedStringContainerBlockEntity newRedStringContainer(BlockPos pos, BlockState state) {
		return new RedStringContainerBlockEntity(pos, state);
	}

	@Override
	public BlockSetType registerBlockSetType(String name, boolean canOpenByHand, SoundType soundType, SoundEvent doorClose, SoundEvent doorOpen, SoundEvent trapdoorClose, SoundEvent trapdoorOpen, SoundEvent pressurePlateClickOff, SoundEvent pressurePlateClickOn, SoundEvent buttonClickOff, SoundEvent buttonClickOn) {
		return BlockSetType.register(new BlockSetType("botania:" + name, canOpenByHand, canOpenByHand, true, BlockSetType.PressurePlateSensitivity.EVERYTHING, soundType, doorClose, doorOpen, trapdoorClose, trapdoorOpen, pressurePlateClickOff, pressurePlateClickOn, buttonClickOff, buttonClickOn));
	}

	@Override
	public WoodType registerWoodType(String name, BlockSetType setType, SoundType soundType, SoundType hangingSignSoundType, SoundEvent fenceGateClose, SoundEvent fenceGateOpen) {
		return WoodType.register(new WoodType("botania:" + name, setType, soundType, hangingSignSoundType, fenceGateClose, fenceGateOpen));
	}

}
