package velrondevs.botania;

import com.google.common.base.Suppliers;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.LootTableLoadEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.*;
import net.neoforged.neoforge.event.entity.player.*;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.api.BotaniaRegistries;
import velrondevs.botania.api.block.HornHarvestable;
import velrondevs.botania.api.block.PhantomInkableBlock;
import velrondevs.botania.api.block.Wandable;
import velrondevs.botania.api.corporea.CorporeaHelper;
import velrondevs.botania.api.item.AvatarWieldable;
import velrondevs.botania.api.item.BlockProvider;
import velrondevs.botania.api.item.CoordBoundItem;
import velrondevs.botania.api.item.Relic;
import velrondevs.botania.api.mana.*;
import velrondevs.botania.api.mana.spark.SparkAttachable;
import velrondevs.botania.capability.CapabilityUtil;
import velrondevs.botania.capability.RedStringContainerCapProvider;
import velrondevs.botania.common.PlayerAccess;
import velrondevs.botania.common.block.*;
import velrondevs.botania.common.block.block_entity.*;
import velrondevs.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import velrondevs.botania.common.block.block_entity.mana.PowerGeneratorBlockEntity;
import velrondevs.botania.common.block.block_entity.red_string.RedStringContainerBlockEntity;
import velrondevs.botania.common.block.flower.functional.DaffomillBlockEntity;
import velrondevs.botania.common.block.flower.functional.LooniumBlockEntity;
import velrondevs.botania.common.block.flower.functional.TigerseyeBlockEntity;
import velrondevs.botania.common.block.flower.functional.VinculotusBlockEntity;
import velrondevs.botania.common.block.mana.DrumBlock;
import velrondevs.botania.common.block.mana.ManaDetectorBlock;
import velrondevs.botania.common.block.mana.ManaVoidBlock;
import velrondevs.botania.common.block.red_string.RedStringInterceptorBlock;
import velrondevs.botania.common.brew.effect.SoulCrossMobEffect;
import velrondevs.botania.common.command.SkyblockCommand;
import velrondevs.botania.common.config.ConfigDataManagerImpl;
import velrondevs.botania.common.entity.GaiaGuardianEntity;
import velrondevs.botania.common.handler.*;
import velrondevs.botania.common.helper.ColorHelper;
import velrondevs.botania.common.helper.PlayerHelper;
import velrondevs.botania.common.impl.BotaniaAPIImpl;
import velrondevs.botania.common.impl.DefaultHornHarvestable;
import velrondevs.botania.common.impl.corporea.DefaultCorporeaMatchers;
import velrondevs.botania.common.integration.corporea.CorporeaNodeDetectors;
import velrondevs.botania.common.item.*;
import velrondevs.botania.common.item.equipment.armor.terrasteel.TerrasteelHelmItem;
import velrondevs.botania.common.item.equipment.bauble.*;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraBladeItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraShattererItem;
import velrondevs.botania.common.item.equipment.tool.terrasteel.TerraTruncatorItem;
import velrondevs.botania.common.item.material.EnderAirItem;
import velrondevs.botania.common.item.relic.*;
import velrondevs.botania.common.item.rod.*;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.common.loot.LootHandler;
import velrondevs.botania.common.world.SkyblockChunkGenerator;
import velrondevs.botania.common.world.SkyblockWorldEvents;
import velrondevs.botania.config.BotaniaConfigSpec;
import velrondevs.botania.integration.InventorySorterIntegration;
import velrondevs.botania.integration.arsnouveau.ArsNouveauIntegration;
import velrondevs.botania.integration.botanypots.BotaniaBotanyPotsIntegration;
import velrondevs.botania.integration.corporea.ItemHandlerCorporeaNodeDetector;
import velrondevs.botania.integration.curios.CurioIntegration;
import velrondevs.botania.integration.kubejs.BotaniaKubeJSIntegration;
import velrondevs.botania.module.BotaniaModules;
import velrondevs.botania.network.BotaniaPayloads;
import velrondevs.botania.registry.BotaniaArmorMaterials;
import velrondevs.botania.registry.BotaniaAttachments;
import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaBrews;
import velrondevs.botania.registry.BotaniaCreativeTabs;
import velrondevs.botania.registry.BotaniaCriteriaTriggers;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaFeatures;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaLootModifiers;
import velrondevs.botania.registry.BotaniaMobEffects;
import velrondevs.botania.registry.BotaniaParticles;
import velrondevs.botania.registry.BotaniaRecipeTypes;
import velrondevs.botania.registry.BotaniaRunes;
import velrondevs.botania.registry.BotaniaRegistryCreation;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.registry.BotaniaStats;
import velrondevs.botania.xplat.XplatAbstractions;
import velrondevs.botania.xplat.XplatImpl;

import vazkii.patchouli.api.PatchouliAPI;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@Mod(LibMisc.MOD_ID)
public class Botania {
	private static IEventBus modBus;

	public static IEventBus modBus() {
		return modBus;
	}

	public Botania(IEventBus modEventBus, ModContainer container) {
		modBus = modEventBus;
		coreInit(container);
		registryInit();
		BotaniaModules.init(modBus);
		modBus.addListener(this::commonSetup);
		modBus.addListener(BotaniaPayloads::register);
		modBus.addListener(BotaniaRegistryCreation::registerRegistry);
		modBus.addListener(this::registerCapabilities);
		registerEvents();
		if (ModList.get().isLoaded("kubejs")) {
			BotaniaKubeJSIntegration.init(modBus);
		}
		if (ModList.get().isLoaded("botanypots")) {
			BotaniaBotanyPotsIntegration.init(modBus);
		}
		if (ModList.get().isLoaded("ars_nouveau")) {
			ArsNouveauIntegration.init(modBus, container);
		}
	}

	public void commonSetup(FMLCommonSetupEvent evt) {
		evt.enqueueWork(BotaniaBlocks::addDispenserBehaviours);
		evt.enqueueWork(() -> {
			BiConsumer<ResourceLocation, Supplier<? extends Block>> consumer = (resourceLocation, blockSupplier) -> ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(resourceLocation, blockSupplier);
			BotaniaBlocks.registerFlowerPotPlants(consumer);
			BotaniaFlowerBlocks.registerFlowerPotPlants(consumer);
		});
		BotaniaBlocks.addAxeStripping();
		PaintableData.init();
		evt.enqueueWork(() -> CompostingData.init((itemLike, chance) -> ComposterBlock.COMPOSTABLES.putIfAbsent(itemLike.asItem(), (float) chance)));
		DefaultCorporeaMatchers.init();
		PlayerHelper.setFakePlayerClass(FakePlayer.class);

		PatchouliAPI.get().registerMultiblock(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.alfPortal), AlfheimPortalBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.terraPlate), TerrestrialAgglomerationPlateBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(BuiltInRegistries.BLOCK.getKey(BotaniaBlocks.enchanter), ManaEnchanterBlockEntity.MULTIBLOCK.get());
		PatchouliAPI.get().registerMultiblock(prefix("gaia_ritual"), GaiaGuardianEntity.ARENA_MULTIBLOCK.get());

		OrechidManager.registerListener();
		ConfigDataManagerImpl.registerListener();
		CraftyCrateBlockEntity.registerListener();
		CorporeaNodeDetectors.register(new ItemHandlerCorporeaNodeDetector());
		if (ModList.get().isLoaded("inventorysorter")) {
			InventorySorterIntegration.init();
		}
	}

	private void coreInit(ModContainer container) {
		BotaniaAPI.LOGGER.debug("API instances: {}",
				List.of(BotaniaAPI.instance(), XplatAbstractions.instance(),
						CorporeaHelper.instance(), ManaItemHandler.instance()));

		BotaniaConfigSpec.setup(container);
		EquipmentHandler.init();
	}

	private void registryInit() {
		bind(Registries.SOUND_EVENT, BotaniaSounds::init);
		bind(Registries.BLOCK, consumer -> {
			BotaniaBlocks.registerBlocks(consumer);
			BotaniaBlockFlammability.register();
		});
		BotaniaCreativeTabs.bindForItems(modBus, BotaniaBlocks::registerItemBlocks);
		bind(Registries.BLOCK_ENTITY_TYPE, BotaniaBlockEntities::registerTiles);
		modBus.addListener(BotaniaRunes::registerDefaults);
		BotaniaCreativeTabs.bindForItems(modBus, BotaniaItems::registerItems);
		bind(Registries.BLOCK, BotaniaFlowerBlocks::registerBlocks);
		BotaniaCreativeTabs.bindForItems(modBus, BotaniaFlowerBlocks::registerItemBlocks);
		BotaniaCreativeTabs.bindForItems(modBus, BotaniaItems::registerRunes);
		bind(Registries.BLOCK_ENTITY_TYPE, BotaniaFlowerBlocks::registerTEs);
		bind(Registries.ARMOR_MATERIAL, BotaniaArmorMaterials::registerArmorMaterials);
		bind(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, BotaniaAttachments::registerAttachments);

		bind(Registries.MENU, BotaniaItems::registerMenuTypes);
		bind(Registries.RECIPE_SERIALIZER, BotaniaItems::registerRecipeSerializers);
		bind(Registries.RECIPE_TYPE, BotaniaRecipeTypes::submitRecipeTypes);
		bind(Registries.RECIPE_SERIALIZER, BotaniaRecipeTypes::submitRecipeSerializers);

		bind(Registries.ENTITY_TYPE, BotaniaEntities::registerEntities);
		modBus.addListener((EntityAttributeCreationEvent e) -> BotaniaEntities.registerAttributes((type, builder) -> e.put(type, builder.build())));
		modBus.addListener((EntityAttributeModificationEvent e) -> {
			e.add(EntityType.PLAYER, PixieHandler.PIXIE_SPAWN_CHANCE);
		});
		bind(Registries.ATTRIBUTE, PixieHandler::registerAttribute);

		bind(Registries.MOB_EFFECT, BotaniaMobEffects::registerPotions);
		bind(BotaniaRegistries.BREWS, BotaniaBrews::submitRegistrations);

		bind(Registries.FEATURE, BotaniaFeatures::registerFeatures);
		bind(Registries.CHUNK_GENERATOR, SkyblockChunkGenerator::submitRegistration);

		bind(Registries.TRIGGER_TYPE, BotaniaCriteriaTriggers::init);
		bind(Registries.PARTICLE_TYPE, BotaniaParticles::registerParticles);

		bind(Registries.LOOT_CONDITION_TYPE, BotaniaLootModifiers::submitLootConditions);
		bind(Registries.LOOT_FUNCTION_TYPE, BotaniaLootModifiers::submitLootFunctions);
		modBus.addListener((RegisterEvent evt) -> {
			if (evt.getRegistryKey().equals(Registries.CUSTOM_STAT)) {
				BotaniaStats.init();
			}
		});
		bind(Registries.CREATIVE_MODE_TAB, BotaniaCreativeTabs::registerTabs);
		modBus.addListener(BotaniaCreativeTabs::buildContents);
	}

	private static <T> void bind(ResourceKey<? extends Registry<T>> registry, Consumer<BiConsumer<T, ResourceLocation>> source) {
		modBus.addListener((RegisterEvent event) -> {
			if (registry.equals(event.getRegistryKey())) {
				source.accept((t, rl) -> event.register(registry, rl, () -> t));
			}
		});
	}

	private void registerEvents() {
		IEventBus bus = NeoForge.EVENT_BUS;

		int blazeTime = 2400 * (XplatAbstractions.INSTANCE.gogLoaded() ? 5 : 10);
		bus.addListener((FurnaceFuelBurnTimeEvent e) -> {
			if (e.getItemStack().is(BotaniaBlocks.blazeBlock.asItem())) {
				e.setBurnTime(blazeTime);
			}
		});

		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
				InteractionResult result = SkyblockWorldEvents.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
				if (result == InteractionResult.SUCCESS) {
					e.setCanceled(true);
					e.setCancellationResult(InteractionResult.SUCCESS);
				}
			});
		}
		bus.addListener((PlayerInteractEvent.LeftClickBlock e) -> ((ShiftingCrustRodItem) BotaniaItems.exchangeRod).onLeftClick(
				e.getEntity(), e.getLevel(), e.getHand(), e.getPos(), e.getFace()));
		bus.addListener((PlayerInteractEvent.LeftClickEmpty e) -> TerraBladeItem.leftClick(e.getItemStack()));
		bus.addListener((AttackEntityEvent e) -> TerraBladeItem.attackEntity(
				e.getEntity(), e.getEntity().level(), InteractionHand.MAIN_HAND, e.getTarget(), null));
		bus.addListener((RegisterCommandsEvent e) -> this.registerCommands(
				e.getDispatcher(), e.getCommandSelection() == Commands.CommandSelection.DEDICATED));
		bus.addListener((CanPlayerSleepEvent e) -> {
			Player.BedSleepingProblem problem = SleepingHandler.trySleep(e.getEntity(), e.getPos());
			if (problem != null) {
				e.setProblem(problem);
			}
		});
		bus.addListener((PlayerEvent.StartTracking e) -> DaffomillBlockEntity.onItemTrack(e.getEntity(), (ServerPlayer) e.getEntity()));
		bus.addListener((LootTableLoadEvent e) -> LootHandler.lootLoad(e.getName(), b -> e.getTable().addPool(b.build())));
		bus.addListener((ManaNetworkEvent e) -> ManaNetworkHandler.instance.onNetworkEvent(e.getReceiver(), e.getType(), e.getAction()));
		bus.addListener((EntityJoinLevelEvent e) -> {
			if (!e.getLevel().isClientSide) {
				TigerseyeBlockEntity.pacifyAfterLoad(e.getEntity(), (ServerLevel) e.getLevel());
			}
		});

		bus.addListener((ServerAboutToStartEvent e) -> this.serverAboutToStart(e.getServer()));
		bus.addListener((ServerStoppingEvent e) -> this.serverStopping(e.getServer()));
		bus.addListener((PlayerEvent.PlayerLoggedOutEvent e) -> FlugelTiaraItem.playerLoggedOut((ServerPlayer) e.getEntity()));
		bus.addListener((PlayerEvent.Clone e) -> ResoluteIvyItem.onPlayerRespawn(e.getOriginal(), e.getEntity(), !e.isWasDeath()));
		bus.addListener((LevelTickEvent.Post e) -> {
			if (e.getLevel() instanceof ServerLevel level) {
				CommonTickHandler.onTick(level);
				GrassSeedsItem.onTickEnd(level);
				TerraTruncatorItem.onTickEnd(level);
			}
		});
		bus.addListener((PlayerInteractEvent.RightClickBlock e) -> {
			RedStringInterceptorBlock.onInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
			RingOfLokiItem.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand(), e.getHitVec());
		});
		bus.addListener((PlayerInteractEvent.RightClickItem e) -> {
			InteractionResultHolder<ItemStack> result = EnderAirItem.onPlayerInteract(e.getEntity(), e.getLevel(), e.getHand());
			if (result.getResult().consumesAction()) {
				e.setCanceled(true);
				e.setCancellationResult(result.getResult());
			}
		});

		bus.addListener((AnvilUpdateEvent e) -> {
			if (SpellbindingClothItem.shouldDenyAnvil(e.getLeft(), e.getRight())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((BlockEvent.BreakEvent e) -> {
			if (ToolCommons.onBlockStartBreak(e.getPlayer().getMainHandItem(), e.getPos(), e.getPlayer())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((BlockEvent.BlockToolModificationEvent e) -> {
			if (e.getItemAbility() == ItemAbilities.AXE_STRIP) {
				BlockState input = e.getState();
				Block output = XplatImpl.CUSTOM_STRIPPABLES.get(input.getBlock());
				if (output != null) {
					e.setFinalState(output.withPropertiesOf(input));
				}
			}
		});
		bus.addListener((EntityTeleportEvent.EnderEntity e) -> {
			if (e.getEntityLiving() instanceof EnderMan em) {
				var newPos = VinculotusBlockEntity.onEndermanTeleport(em, e.getTargetX(), e.getTargetY(), e.getTargetZ());
				if (newPos != null) {
					e.setTargetX(newPos.x());
					e.setTargetY(newPos.y());
					e.setTargetZ(newPos.z());
				}
			}
		});
		bus.addListener((ExplosionEvent.Detonate e) -> {
			if (BenevolentGoddessCharmItem.shouldProtectExplosion(e.getLevel(), e.getExplosion().center())) {
				e.getExplosion().clearToBlow();
			}
		});
		bus.addListener((ItemEntityPickupEvent.Pre e) -> {
			if (FlowerPouchItem.onPickupItem(e.getItemEntity(), e.getPlayer())) {
				e.setCanPickup(TriState.FALSE);
			}
		});
		bus.addListener((LivingDropsEvent e) -> {
			var living = e.getEntity();
			LooniumBlockEntity.dropLooniumItems(living, stack -> {
				e.getDrops().clear();
				if (!stack.isEmpty()) {
					var ent = new ItemEntity(living.level(), living.getX(), living.getY(), living.getZ(), stack);
					ent.setDefaultPickUpDelay();
					e.getDrops().add(ent);
				}
			});
		});
		bus.addListener((LivingDeathEvent e) -> {
			if (e.getSource().getEntity() instanceof LivingEntity killer) {
				SoulCrossMobEffect.onEntityKill(e.getEntity(), killer);
			}
		});
		bus.addListener((LivingEvent.LivingJumpEvent e) -> SojournersSashItem.onPlayerJump(e.getEntity()));
		bus.addListener((LivingIncomingDamageEvent e) -> {
			if (e.getEntity() instanceof Player player
					&& RingOfOdinItem.onPlayerAttacked(player, e.getSource())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((ItemTossEvent e) -> RingOfMagnetizationItem.onTossItem(e.getPlayer()));
		bus.addListener(EventPriority.LOW, (LivingIncomingDamageEvent e) -> {
			if (e.getEntity() instanceof Player player) {
				Container worn = EquipmentHandler.getAllWorn(player);
				for (int i = 0; i < worn.getContainerSize(); i++) {
					ItemStack stack = worn.getItem(i);
					if (stack.getItem() instanceof CloakOfVirtueItem cloak) {
						e.setAmount(cloak.onPlayerDamage(player, e.getSource(), e.getAmount()));
					}
				}

				PixieHandler.onDamageTaken(player, e.getSource());
			}
			if (e.getSource().getDirectEntity() instanceof Player player) {
				CharmOfTheDivaItem.onEntityDamaged(player, e.getEntity());
			}
		});
		bus.addListener((PlayerTickEvent.Pre e) -> {
			Player player = e.getEntity();
			FlugelTiaraItem.updatePlayerFlyStatus(player);
			SojournersSashItem.tickBelt(player);
			if (!player.level().isClientSide()) {
				EnderOverseerBlockEntity.checkLookingAtEnderOverseer(player);
			}
		});
		bus.addListener((LivingFallEvent e) -> {
			if (e.getEntity() instanceof Player player) {
				e.setDistance(SojournersSashItem.onPlayerFall(player, e.getDistance()));
			}
		});
		bus.addListener(EventPriority.LOW, (CriticalHitEvent e) -> {
			if (e.getEntity().level().isClientSide
					|| !e.isCriticalHit()
					|| !TerrasteelHelmItem.hasTerraArmorSet(e.getEntity())
					|| !(e.getTarget() instanceof LivingEntity target)) {
				return;
			}
			e.setDamageMultiplier(e.getDamageMultiplier() * TerrasteelHelmItem.getCritDamageMult(e.getEntity()));
			((PlayerAccess) e.getEntity()).botania$setCritTarget(target);
		});
		bus.addListener((PlayerEvent.ItemCraftedEvent e) -> AssemblyHaloItem.onItemCrafted(e.getEntity(), e.getInventory()));
	}

	private static final Supplier<Map<Item, Function<ItemStack, AvatarWieldable>>> AVATAR_WIELDABLES = Suppliers.memoize(() -> Map.of(
			BotaniaItems.dirtRod, s -> new LandsRodItem.AvatarBehavior(),
			BotaniaItems.diviningRod, s -> new PlentifulMantleRodItem.AvatarBehavior(),
			BotaniaItems.fireRod, s -> new HellsRodItem.AvatarBehavior(),
			BotaniaItems.missileRod, s -> new UnstableReservoirRodItem.AvatarBehavior(),
			BotaniaItems.rainbowRod, s -> new BifrostRodItem.AvatarBehavior(),
			BotaniaItems.tornadoRod, s -> new SkiesRodItem.AvatarBehavior()
	));

	private static final Supplier<Map<Item, Function<ItemStack, BlockProvider>>> BLOCK_PROVIDER = Suppliers.memoize(() -> Map.of(
			BotaniaItems.dirtRod, LandsRodItem.BlockProviderImpl::new,
			BotaniaItems.skyDirtRod, LandsRodItem.BlockProviderImpl::new,
			BotaniaItems.blackHoleTalisman, BlackHoleTalismanItem.BlockProviderImpl::new,
			BotaniaItems.cobbleRod, s -> new DepthsRodItem.BlockProviderImpl(),
			BotaniaItems.enderHand, EnderHandItem.BlockProviderImpl::new,
			BotaniaItems.terraformRod, s -> new TerraFirmaRodItem.BlockProviderImpl()
	));

	private static final Supplier<Map<Item, Function<ItemStack, CoordBoundItem>>> COORD_BOUND_ITEM = Suppliers.memoize(() -> Map.of(
			BotaniaItems.flugelEye, EyeOfTheFlugelItem.CoordBoundItemImpl::new,
			BotaniaItems.manaMirror, ManaMirrorItem.CoordBoundItemImpl::new,
			BotaniaItems.twigWand, WandOfTheForestItem.CoordBoundItemImpl::new,
			BotaniaItems.dreamwoodWand, WandOfTheForestItem.CoordBoundItemImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, ManaItem>>> MANA_ITEM = Suppliers.memoize(() -> Map.of(
			BotaniaItems.manaMirror, ManaMirrorItem.ManaItemImpl::new,
			BotaniaItems.manaRing, BandOfManaItem.ManaItemImpl::new,
			BotaniaItems.manaRingGreater, GreaterBandOfManaItem.GreaterManaItemImpl::new,
			BotaniaItems.manaTablet, ManaTabletItem.ManaItemImpl::new,
			BotaniaItems.terraPick, TerraShattererItem.ManaItemImpl::new
	));

	private static final Supplier<Map<Item, Function<ItemStack, Relic>>> RELIC = Suppliers.memoize(() -> Map.of(
			BotaniaItems.dice, DiceOfFateItem::makeRelic,
			BotaniaItems.flugelEye, EyeOfTheFlugelItem::makeRelic,
			BotaniaItems.infiniteFruit, FruitOfGrisaiaItem::makeRelic,
			BotaniaItems.kingKey, KeyOfTheKingsLawItem::makeRelic,
			BotaniaItems.lokiRing, RingOfLokiItem::makeRelic,
			BotaniaItems.odinRing, RingOfOdinItem::makeRelic,
			BotaniaItems.thorRing, RingOfThorItem::makeRelic
	));

	private static <T> void registerItemCaps(RegisterCapabilitiesEvent e, ItemCapability<T, Void> cap, Map<Item, Function<ItemStack, T>> map) {
		map.forEach((item, factory) -> e.registerItem(cap, (stack, ctx) -> factory.apply(stack), item));
	}

	private void registerCapabilities(RegisterCapabilitiesEvent e) {
		registerItemCaps(e, BotaniaCapabilities.AVATAR_WIELDABLE, AVATAR_WIELDABLES.get());
		registerItemCaps(e, BotaniaCapabilities.BLOCK_PROVIDER, BLOCK_PROVIDER.get());
		registerItemCaps(e, BotaniaCapabilities.COORD_BOUND_ITEM, COORD_BOUND_ITEM.get());
		registerItemCaps(e, BotaniaCapabilities.MANA_ITEM, MANA_ITEM.get());
		registerItemCaps(e, BotaniaCapabilities.RELIC, RELIC.get());

		e.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new CapabilityUtil.WaterBowlFluidHandler(stack), BotaniaItems.waterBowl);
		e.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new CapabilityUtil.ExtrapolatedBucketFluidHandler(stack), BotaniaItems.openBucket);

		if (EquipmentHandler.instance instanceof CurioIntegration) {
			Item[] baubles = BuiltInRegistries.ITEM.stream().filter(i -> i instanceof BaubleItem).toArray(Item[]::new);
			CurioIntegration.registerCapabilities(e, baubles);
		}

		registerBlockLookasides(e);

		for (BlockEntityType<?> type : BuiltInRegistries.BLOCK_ENTITY_TYPE) {
			e.registerBlockEntity(BotaniaCapabilities.EXOFLAME_HEATABLE, type,
					(be, ctx) -> be instanceof AbstractFurnaceBlockEntity furnace ? new ExoflameFurnaceHandler.FurnaceExoflameHeatable(furnace) : null);
		}

		for (BlockEntityType<?> type : botaniaBlockEntityTypes()) {
			e.registerBlockEntity(Capabilities.ItemHandler.BLOCK, type,
					(be, side) -> be instanceof ExposedSimpleInventoryBlockEntity inv ? new SidedInvWrapper(inv, null) : null);
			e.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, type,
					(be, side) -> be instanceof PowerGeneratorBlockEntity gen ? new GeneratorEnergyView(gen) : null);
		}

		e.registerBlockEntity(Capabilities.ItemHandler.BLOCK, BotaniaBlockEntities.RED_STRING_CONTAINER, RedStringContainerCapProvider::getItemHandler);

		e.registerBlockEntity(BotaniaCapabilities.HOURGLASS_TRIGGER, BotaniaBlockEntities.ANIMATED_TORCH,
				(be, ctx) -> hourglass -> be.toggle());

		for (var type : BlockEntityConstants.SELF_WANDADBLE_BES) {
			registerSelf(e, BotaniaCapabilities.WANDABLE, type, Wandable.class);
		}
		for (var type : BlockEntityConstants.SELF_PHANTOM_INKABLE_BES) {
			registerSelf(e, BotaniaCapabilities.PHANTOM_INKABLE, type, PhantomInkableBlock.class);
		}
		for (var type : BlockEntityConstants.SELF_MANA_TRIGGER_BES) {
			registerSelf(e, BotaniaCapabilities.MANA_TRIGGER, type, ManaTrigger.class);
		}
		for (var type : BlockEntityConstants.SELF_MANA_RECEIVER_BES) {
			registerSelf(e, BotaniaCapabilities.MANA_RECEIVER, type, ManaReceiver.class);
		}
		for (var type : BlockEntityConstants.SELF_SPARK_ATTACHABLE_BES) {
			registerSelf(e, BotaniaCapabilities.SPARK_ATTACHABLE, type, SparkAttachable.class);
		}
	}

	private static <T, C> void registerSelf(RegisterCapabilitiesEvent e, BlockCapability<T, C> cap, BlockEntityType<?> type, Class<T> clazz) {
		e.registerBlockEntity(cap, type, (be, ctx) -> clazz.cast(be));
	}

	private static List<BlockEntityType<?>> botaniaBlockEntityTypes() {
		List<BlockEntityType<?>> ret = new ArrayList<>();
		for (var entry : BuiltInRegistries.BLOCK_ENTITY_TYPE.entrySet()) {
			if (entry.getKey().location().getNamespace().equals(LibMisc.MOD_ID)) {
				ret.add(entry.getValue());
			}
		}
		return ret;
	}

	private record GeneratorEnergyView(PowerGeneratorBlockEntity gen) implements IEnergyStorage {
		@Override
		public int getEnergyStored() {
			return gen.getEnergy();
		}

		@Override
		public int getMaxEnergyStored() {
			return PowerGeneratorBlockEntity.MAX_ENERGY;
		}

		@Override
		public boolean canExtract() {
			return false;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return 0;
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return 0;
		}

		@Override
		public boolean canReceive() {
			return false;
		}
	}

	private void registerBlockLookasides(RegisterCapabilitiesEvent e) {
		e.registerBlock(BotaniaCapabilities.HORN_HARVEST, (w, p, s, be, ctx) -> (world, pos, stack, hornType, living) -> hornType == HornHarvestable.EnumHornType.CANOPY,
				Blocks.VINE, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT, Blocks.TWISTING_VINES,
				Blocks.TWISTING_VINES_PLANT, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
		e.registerBlock(BotaniaCapabilities.HORN_HARVEST, (w, p, s, be, ctx) -> DefaultHornHarvestable.INSTANCE,
				ColorHelper.supportedColors().map(BotaniaBlocks::getMushroom).toArray(Block[]::new));
		e.registerBlock(BotaniaCapabilities.HORN_HARVEST, (w, p, s, be, ctx) -> DefaultHornHarvestable.INSTANCE,
				ColorHelper.supportedColors().map(BotaniaBlocks::getShinyFlower).toArray(Block[]::new));
		e.registerBlock(BotaniaCapabilities.MANA_GHOST, (w, p, s, be, ctx) -> ((ManaCollisionGhost) s.getBlock()),
				BotaniaBlocks.manaDetector,
				BotaniaBlocks.abstrusePlatform, BotaniaBlocks.infrangiblePlatform, BotaniaBlocks.spectralPlatform,
				BotaniaBlocks.prism, BotaniaBlocks.tinyPlanet);
		e.registerBlock(BotaniaCapabilities.MANA_RECEIVER, (w, p, s, be, ctx) -> new ManaVoidBlock.ManaReceiverImpl(w, p, s), BotaniaBlocks.manaVoid);
		e.registerBlock(BotaniaCapabilities.MANA_TRIGGER, (w, p, s, be, ctx) -> new DrumBlock.ManaTriggerImpl(w, p, s),
				BotaniaBlocks.canopyDrum, BotaniaBlocks.wildDrum, BotaniaBlocks.gatheringDrum);
		e.registerBlock(BotaniaCapabilities.MANA_TRIGGER, (w, p, s, be, ctx) -> new ManastormChargeBlock.ManaTriggerImpl(w, p, s), BotaniaBlocks.manaBomb);
		e.registerBlock(BotaniaCapabilities.MANA_TRIGGER, (w, p, s, be, ctx) -> new ManaDetectorBlock.ManaTriggerImpl(w, p, s), BotaniaBlocks.manaDetector);
		e.registerBlock(BotaniaCapabilities.WANDABLE,
				(world, pos, state, be, ctx) -> (player, stack, side) -> ((ForceRelayBlock) state.getBlock()).onUsedByWand(player, stack, world, pos),
				BotaniaBlocks.pistonRelay);
	}

	private void serverAboutToStart(MinecraftServer server) {
		if (BotaniaAPI.instance().getClass() != BotaniaAPIImpl.class) {
			String clname = BotaniaAPI.instance().getClass().getName();
			throw new IllegalAccessError("The Botania API has been overriden. "
					+ "This will cause crashes and compatibility issues, and that's why it's marked as"
					+ " \"Do not Override\". Whoever had the brilliant idea of overriding it needs to go"
					+ " back to elementary school and learn to read. (Actual classname: " + clname + ")");
		}

		if (server.isDedicatedServer()) {
			ContributorList.firstStart();
		}
	}

	private void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, boolean dedicated) {
		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockCommand.register(dispatcher);
		}
	}

	private void serverStopping(MinecraftServer server) {
		ManaNetworkHandler.instance.clear();
		CorporeaIndexBlockEntity.clearIndexCache();
	}

}
