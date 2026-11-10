package velrondevs.botania.client;

import com.google.common.base.Suppliers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.BotaniaAPIClient;
import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.api.mana.ManaBarTooltip;
import velrondevs.botania.client.core.handler.*;
import velrondevs.botania.client.core.helper.CoreShaders;
import velrondevs.botania.client.core.proxy.ClientProxy;
import velrondevs.botania.client.fx.BotaniaParticleProviders;
import velrondevs.botania.client.gui.HUDHandler;
import velrondevs.botania.client.gui.ManaBarTooltipComponent;
import velrondevs.botania.client.gui.TooltipHandler;
import velrondevs.botania.client.gui.bag.FlowerPouchGui;
import velrondevs.botania.client.gui.box.BaubleBoxGui;
import velrondevs.botania.client.model.BotaniaLayerDefinitions;
import velrondevs.botania.client.model.FloatingFlowerGeometry;
import velrondevs.botania.client.model.ManaBlasterItemModel;
import velrondevs.botania.client.model.armor.ArmorModels;
import velrondevs.botania.client.render.BlockEntityItemRendererHelper;
import velrondevs.botania.client.render.ColorHandler;
import velrondevs.botania.client.render.entity.EntityRenderers;
import velrondevs.botania.client.render.world.WorldOverlays;
import velrondevs.botania.common.block.GaiaHeadBlock;
import velrondevs.botania.common.block.block_entity.corporea.CorporeaIndexBlockEntity;
import velrondevs.botania.common.item.equipment.armor.manasteel.ManasteelArmorItem;
import velrondevs.botania.common.item.equipment.bauble.RingOfDexterousMotionItem;
import velrondevs.botania.registry.BotaniaBlockEntities;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.ClientXplatAbstractions;

import vazkii.patchouli.api.BookDrawScreenEvent;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@EventBusSubscriber(modid = BotaniaAPI.MODID, value = Dist.CLIENT)
public class BotaniaClient {
	public static final Identifier BLOCK_ENTITY_ITEM_RENDERER_ID = prefix("block_entity");

	@SubscribeEvent
	public static void registerMenuScreens(RegisterMenuScreensEvent e) {
		e.register(BotaniaItems.FLOWER_BAG_CONTAINER, FlowerPouchGui::new);
		e.register(BotaniaItems.BAUBLE_BOX_CONTAINER, BaubleBoxGui::new);
	}

	@SubscribeEvent
	public static void registerGuiLayers(RegisterGuiLayersEvent e) {
		e.registerAbove(VanillaGuiLayers.EXPERIENCE_LEVEL, prefix("hud"),
				(guiGraphics, deltaTracker) -> HUDHandler.onDrawScreenPost(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false)));
	}

	@SubscribeEvent
	public static void clientInit(FMLClientSetupEvent evt) {

		BotaniaAPI.LOGGER.debug("Client API instances: {}",
				List.of(BotaniaAPIClient.instance(), ClientXplatAbstractions.instance()));

		var bus = NeoForge.EVENT_BUS;
		bus.addListener((BookDrawScreenEvent e) -> KonamiHandler.renderBook(e.getBook(), e.getScreen(), e.getMouseX(), e.getMouseY(), e.getPartialTicks(), e.getGraphics()));
		bus.addListener((ClientTickEvent.Post e) -> {
			ClientTickHandler.clientTickEnd(Minecraft.getInstance());
			KonamiHandler.clientTick(Minecraft.getInstance());
		});
		bus.addListener((ItemTooltipEvent e) -> TooltipHandler.onTooltipEvent(e.getItemStack(), e.getFlags(), e.getToolTip()));
		bus.addListener((ScreenEvent.KeyPressed.Post e) -> CorporeaInputHandler.buttonPressed(e.getKeyEvent()));

		bus.addListener(EventPriority.HIGH, (ClientChatEvent e) -> {
			var player = Minecraft.getInstance().player;
			if (player != null && CorporeaIndexBlockEntity.ClientHandler.onChat(player, e.getMessage())) {
				e.setCanceled(true);
			}
		});
		bus.addListener((CustomizeGuiOverlayEvent.BossEventProgress e) -> {
			var result = BossBarHandler.onBarRender(e.getGuiGraphics(), e.getX(), e.getY(),
					e.getBossEvent(), true);
			result.ifPresent(increment -> {
				e.setCanceled(true);
				e.setIncrement(increment);
			});
		});
		bus.addListener((InputEvent.Key e) -> {
			RingOfDexterousMotionItem.ClientLogic.onKeyDown();
			KonamiHandler.handleInput(e.getKey(), e.getAction(), e.getModifiers());
		});
		bus.addListener((RenderFrameEvent.Pre e) -> ClientTickHandler.renderTick(e.getPartialTick().getGameTimeDeltaPartialTick(true)));
		bus.addListener(WorldOverlays::onSubmitCustomGeometry);
		bus.addListener(velrondevs.botania.client.render.world.SkyblockSkyRenderer::onExtractLevelRenderState);

		ClientProxy.initSeasonal();
		if (net.neoforged.fml.ModList.get().isLoaded("ears")) {
			velrondevs.botania.client.integration.ears.EarsIntegration.register();
		}
		if (velrondevs.botania.integration.catnip.PonderGate.enabled()) {
			try {
				velrondevs.botania.client.integration.ponder.BotaniaPonderIntegration.init();
			} catch (LinkageError error) {
				velrondevs.botania.api.BotaniaAPI.LOGGER.warn("The installed Ponder version is not compatible with the Botania scenes", error);
			}
		}
	}

	@SubscribeEvent
	public static void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent e) {
		e.register(ManaBarTooltip.class, ManaBarTooltipComponent::new);
	}

	@SubscribeEvent
	public static void registerPictureInPictureRenderers(RegisterPictureInPictureRenderersEvent e) {
		if (velrondevs.botania.integration.catnip.PonderGate.enabled()) {
			try {
				velrondevs.botania.client.integration.ponder.BotaniaPonderIntegration.registerPictureInPictureRenderers(e);
			} catch (LinkageError error) {
				velrondevs.botania.api.BotaniaAPI.LOGGER.warn("Could not register the Ponder renderers", error);
			}
		}
	}

	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent e) {
		ClientProxy.initKeybindings(e::registerCategory, e::register);
	}

	private static final Supplier<Map<BlockEntityType<?>, Function<BlockEntity, WandHUD>>> WAND_HUD = Suppliers.memoize(() -> {
		var ret = new IdentityHashMap<BlockEntityType<?>, Function<BlockEntity, WandHUD>>();
		BotaniaBlockEntities.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		BotaniaFlowerBlocks.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		return Collections.unmodifiableMap(ret);
	});

	private static final Supplier<Map<EntityType<?>, Function<Entity, WandHUD>>> ENTITY_WAND_HUD = Suppliers.memoize(() -> {
		var ret = new IdentityHashMap<EntityType<?>, Function<Entity, WandHUD>>();
		BotaniaEntities.registerWandHudCaps((factory, types) -> {
			for (var type : types) {
				ret.put(type, factory);
			}
		});
		return Collections.unmodifiableMap(ret);
	});

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent e) {
		WAND_HUD.get().forEach((type, factory) -> e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, type, (be, ctx) -> factory.apply(be)));
		ENTITY_WAND_HUD.get().forEach((type, factory) -> e.registerEntity(BotaniaClientCapabilities.ENTITY_WAND_HUD, type, (entity, ctx) -> factory.apply(entity)));
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent e) {
		for (Item item : BuiltInRegistries.ITEM) {
			if (item instanceof ManasteelArmorItem) {
				e.registerItem(new IClientItemExtensions() {
					@Override
					public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType layerType, Model original) {
						Model model = ArmorModels.get(stack);
						return model != null ? model : original;
					}

					@Override
					public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
						if (stack.getItem() instanceof ManasteelArmorItem armor) {
							Identifier texture = armor.getArmorTexture(stack, armor.getEquipmentSlot());
							if (texture != null) {
								return texture;
							}
						}
						return _default;
					}
				}, item);
			}
		}
	}

	@SubscribeEvent
	public static void registerModelLoader(ModelEvent.RegisterLoaders evt) {
		evt.register(ClientXplatAbstractions.FLOATING_FLOWER_MODEL_LOADER_ID, FloatingFlowerGeometry.Loader.INSTANCE);
	}

	@SubscribeEvent
	public static void registerItemModels(RegisterItemModelsEvent evt) {
		evt.register(ClientXplatAbstractions.MANA_GUN_MODEL_LOADER_ID, ManaBlasterItemModel.Unbaked.MAP_CODEC);
	}

	@SubscribeEvent
	public static void registerSpecialModels(RegisterSpecialModelRendererEvent evt) {
		evt.register(BLOCK_ENTITY_ITEM_RENDERER_ID, BlockEntityItemRendererHelper.Unbaked.MAP_CODEC);
	}

	@SubscribeEvent
	public static void registerConditionalProperties(RegisterConditionalItemModelPropertyEvent evt) {
		BotaniaItemProperties.initConditional(evt::register);
	}

	@SubscribeEvent
	public static void registerRangeProperties(RegisterRangeSelectItemModelPropertyEvent evt) {
		BotaniaItemProperties.initRange(evt::register);
	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterStandalone evt) {
		var resourceManager = Minecraft.getInstance().getResourceManager();
		MiscellaneousModels.INSTANCE.onModelRegister(resourceManager, evt::register);
	}

	@SubscribeEvent
	public static void onModifyBakingResult(ModelEvent.ModifyBakingResult evt) {
		MiscellaneousModels.INSTANCE.onModifyBakingResult(evt.getBakingResult());
	}

	@SubscribeEvent
	public static void onModelBake(ModelEvent.BakingCompleted evt) {
		MiscellaneousModels.INSTANCE.onModelBake(evt.getModelManager());
	}

	@SubscribeEvent
	public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions evt) {
		BotaniaLayerDefinitions.init(evt::registerLayerDefinition);
	}

	@SubscribeEvent
	public static void registerSkullModels(EntityRenderersEvent.CreateSkullModels evt) {
		evt.registerSkullModel(GaiaHeadBlock.GAIA_TYPE, velrondevs.botania.client.model.GaiaHeadModel::new, net.minecraft.resources.Identifier.withDefaultNamespace("textures/entity/player/wide/steve.png"));
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers evt) {
		EntityRenderers.registerBlockEntityRenderers(evt::registerBlockEntityRenderer);
		EntityRenderers.registerEntityRenderers(evt::registerEntityRenderer);
	}

	@SubscribeEvent
	public static void registerParticleFactories(RegisterParticleProvidersEvent evt) {
		BotaniaParticleProviders.registerFactories(new BotaniaParticleProviders.Consumer() {
				@Override
			public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
				evt.registerSpriteSet(type, constructor::apply);
			}
		});
	}

	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.BlockTintSources evt) {
		ColorHandler.submitBlocks(evt::register);
	}

	@SubscribeEvent
	public static void registerItemTints(RegisterColorHandlersEvent.ItemTintSources evt) {
		evt.register(ColorHandler.TINT_ID, ColorHandler.Tint.MAP_CODEC);
	}

	@SubscribeEvent
	public static void initAuxiliaryRender(EntityRenderersEvent.AddLayers evt) {
		for (var playerModelType : evt.getSkins()) {
			AvatarRenderer<AbstractClientPlayer> renderer = evt.getPlayerRenderer(playerModelType);
			if (renderer != null) {
				EntityRenderers.addAuxiliaryPlayerRenders(renderer, renderer::addLayer);
			}
		}
	}

	@SubscribeEvent
	public static void registerDebugEntries(RegisterDebugEntriesEvent evt) {
		evt.register(DebugHandler.ID, DebugHandler.INSTANCE);
	}

	@SubscribeEvent
	public static void registerPipelines(RegisterRenderPipelinesEvent evt) {
		CoreShaders.init(evt::registerPipeline);
		evt.registerPipeline(velrondevs.botania.client.core.helper.StarfieldPipeline.PIPELINE);
		velrondevs.botania.client.render.world.SkyDrawHelper.registerPipelines(evt::registerPipeline);
	}
}
