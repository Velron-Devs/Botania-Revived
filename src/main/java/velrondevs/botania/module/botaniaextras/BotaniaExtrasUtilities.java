package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.registries.DeferredHolder;

import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.module.botaniaextras.block.LivingwoodFunnelBlock;
import velrondevs.botania.module.botaniaextras.block.LivingwoodFunnelBlockEntity;
import velrondevs.botania.module.botaniaextras.crafting.ColorizerDyeRecipe;
import velrondevs.botania.module.botaniaextras.effect.ManatideStormEffect;
import velrondevs.botania.module.botaniaextras.entity.ManasealCreeperEntity;
import velrondevs.botania.module.botaniaextras.item.ClericalColorizerItem;
import velrondevs.botania.module.botaniaextras.item.CoatOfArmsItem;
import velrondevs.botania.module.botaniaextras.item.LotusRemnantItem;
import velrondevs.botania.module.botaniaextras.item.ToolbeltItem;
import velrondevs.botania.module.botaniaextras.network.ToolbeltClickPacket;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasUtilities {
	public static final ResourceLocation FUNNEL_ID = prefix("livingwood_funnel");
	public static final ResourceLocation MANASEAL_CREEPER_ID = prefix("manaseal_creeper");
	public static final ResourceLocation MANATIDE_STORM_ID = prefix("manatide_storm");

	public static final Holder<MobEffect> MANATIDE_STORM = DeferredHolder.create(Registries.MOB_EFFECT, MANATIDE_STORM_ID);

	public static final Block livingwoodFunnel = new LivingwoodFunnelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_RED)
			.strength(2.0F).sound(SoundType.WOOD).noOcclusion().forceSolidOn());
	public static final BlockEntityType<LivingwoodFunnelBlockEntity> FUNNEL = XplatAbstractions.INSTANCE.createBlockEntityType(LivingwoodFunnelBlockEntity::new, livingwoodFunnel);

	public static final EntityType<ManasealCreeperEntity> MANASEAL_CREEPER = EntityType.Builder.<ManasealCreeperEntity>of(ManasealCreeperEntity::new, MobCategory.MONSTER)
			.sized(0.6F, 1.7F).clientTrackingRange(8).build(MANASEAL_CREEPER_ID.toString());

	public static final Item toolbelt = new ToolbeltItem(BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item clericalColorizer = new ClericalColorizerItem(BotaniaItems.defaultBuilder().stacksTo(1));
	public static final Item wiltedLotus = new LotusRemnantItem(false, BotaniaItems.defaultBuilder());
	public static final Item deathlyLotus = new LotusRemnantItem(true, BotaniaItems.defaultBuilder().rarity(Rarity.RARE));
	public static final Item manasealCreeperSpawnEgg = new SpawnEggItem(MANASEAL_CREEPER, 0xCC11D3, 0xFB9BFF, BotaniaItems.defaultBuilder());

	private static final List<Item> COATS = new ArrayList<>();

	static {
		for (int i = 0; i < CoatOfArmsItem.NAMES.size(); i++) {
			COATS.add(new CoatOfArmsItem(i, BotaniaItems.defaultBuilder().stacksTo(1)));
		}
	}

	private BotaniaExtrasUtilities() {}

	public static List<Item> coats() {
		return Collections.unmodifiableList(COATS);
	}

	public static void registerBlocks(BiConsumer<Block, ResourceLocation> r) {
		r.accept(livingwoodFunnel, FUNNEL_ID);
	}

	public static void registerBlockEntities(BiConsumer<BlockEntityType<?>, ResourceLocation> r) {
		r.accept(FUNNEL, FUNNEL_ID);
	}

	public static void registerEntities(BiConsumer<EntityType<?>, ResourceLocation> r) {
		r.accept(MANASEAL_CREEPER, MANASEAL_CREEPER_ID);
	}

	public static void registerEffects(BiConsumer<MobEffect, ResourceLocation> r) {
		r.accept(new ManatideStormEffect(), MANATIDE_STORM_ID);
	}

	public static void registerSerializers(BiConsumer<RecipeSerializer<?>, ResourceLocation> r) {
		r.accept(ColorizerDyeRecipe.SERIALIZER, prefix("clerical_colorizer_dye"));
	}

	public static void registerAttributes(EntityAttributeCreationEvent e) {
		e.put(MANASEAL_CREEPER, Creeper.createAttributes().build());
	}

	public static void registerSpawns(RegisterSpawnPlacementsEvent e) {
		e.register(MANASEAL_CREEPER, SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
				Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
	}

	public static void registerItems(BiConsumer<Item, ResourceLocation> r) {
		r.accept(new BlockItem(livingwoodFunnel, BotaniaItems.defaultBuilder()), FUNNEL_ID);
		r.accept(toolbelt, prefix("toolbelt"));
		r.accept(clericalColorizer, prefix("clerical_colorizer"));
		for (int i = 0; i < COATS.size(); i++) {
			r.accept(COATS.get(i), prefix(CoatOfArmsItem.itemName(i)));
		}
		r.accept(wiltedLotus, prefix("wilted_lotus"));
		r.accept(deathlyLotus, prefix("deathly_lotus"));
		r.accept(manasealCreeperSpawnEgg, prefix("manaseal_creeper_spawn_egg"));
	}

	public static void registerCapabilities(RegisterCapabilitiesEvent e) {
		e.registerItem(BotaniaCapabilities.BLOCK_PROVIDER, (stack, ctx) -> new ToolbeltItem.BlockProviderImpl(stack), toolbelt);
		e.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FUNNEL, (be, side) -> new SidedInvWrapper(be, side));
	}

	public static void registerPayloads(RegisterPayloadHandlersEvent e) {
		e.registrar("1").executesOn(HandlerThread.NETWORK).playToServer(ToolbeltClickPacket.TYPE, ToolbeltClickPacket.STREAM_CODEC,
				(IPayloadHandler<ToolbeltClickPacket>) (packet, ctx) -> {
					ServerPlayer player = (ServerPlayer) ctx.player();
					packet.handle(player.getServer(), player);
				});
	}
}
