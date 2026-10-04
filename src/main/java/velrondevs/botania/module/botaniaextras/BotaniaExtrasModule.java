package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.datagen.providers.botaniaextras.BotaniaExtrasDatagen;
import velrondevs.botania.datagen.providers.botaniaextras.BotaniaExtrasFlowerTagEntries;
import velrondevs.botania.datagen.providers.botaniaextras.BotaniaExtrasTagEntries;
import velrondevs.botania.datagen.providers.botaniaextras.BotaniaExtrasUtilityTags;
import velrondevs.botania.mixin.FireBlockAccessor;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;
import velrondevs.botania.module.ModuleTagSink;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlock;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlockEntity;
import velrondevs.botania.module.botaniaextras.block.LightningRodBlockEntity;
import velrondevs.botania.module.botaniaextras.client.BotaniaExtrasClient;
import velrondevs.botania.module.botaniaextras.item.FloralBifrostPowderItem;
import velrondevs.botania.module.botaniaextras.item.IridescentSeedsItem;
import velrondevs.botania.module.botaniaextras.item.StormySeaRodItem;
import velrondevs.botania.module.botaniaextras.item.ThunderingPeaksRodItem;
import velrondevs.botania.module.botaniaextras.item.VibrantPlainsRodItem;

import vazkii.patchouli.api.PatchouliAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaExtrasModule extends BotaniaModule {
	public static final String ID = "botania_extras";
	public static final BotaniaExtrasModule INSTANCE = new BotaniaExtrasModule();

	private BotaniaExtrasModule() {
		super(ID, "Botania Extras");
	}

	@Override
	public void register(ModuleContext ctx) {
		ctx.bindBlocks(r -> {
			BotaniaExtrasBlocks.registerBlocks(r);
			registerFlammability();
		});
		ctx.bindItems(BotaniaExtrasBlocks::registerItemBlocks);
		ctx.bindBlocks(BotaniaExtrasWoods::registerBlocks);
		ctx.bindItems(BotaniaExtrasWoods::registerItemBlocks);
		ctx.bindBlocks(BotaniaExtrasMagicWoods::registerBlocks);
		ctx.bindItems(BotaniaExtrasMagicWoods::registerItemBlocks);
		ctx.bindBlocks(BotaniaExtrasFlowers::registerBlocks);
		ctx.bindItems(BotaniaExtrasFlowers::registerItemBlocks);
		ctx.bind(Registries.BLOCK_ENTITY_TYPE, BotaniaExtrasFlowers::registerBlockEntities);
		ctx.bindItems(BotaniaExtrasItems::registerItems);
		ctx.bind(Registries.BLOCK_ENTITY_TYPE, BotaniaExtrasBlockEntities::registerBlockEntities);
		ctx.bind(Registries.RECIPE_TYPE, r -> r.accept(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_TYPE, BotaniaExtrasRecipeTypes.TREE_SUFFUSION_ID));
		ctx.bind(Registries.RECIPE_SERIALIZER, r -> r.accept(BotaniaExtrasRecipeTypes.TREE_SUFFUSION_SERIALIZER, BotaniaExtrasRecipeTypes.TREE_SUFFUSION_ID));
		BotaniaExtrasUtilitiesSetup.register(ctx);
		ctx.creativeTab(() -> new ItemStack(BotaniaExtrasBlocks.bifrostDirt));

		IEventBus modBus = ctx.modBus();
		modBus.addListener((FMLCommonSetupEvent e) -> e.enqueueWork(BotaniaExtrasModule::commonSetup));
		modBus.addListener(BotaniaExtrasModule::registerCapabilities);

		IEventBus bus = NeoForge.EVENT_BUS;
		bus.addListener((LevelTickEvent.Post e) -> {
			if (e.getLevel() instanceof ServerLevel level) {
				IridescentSeedsItem.onTickEnd(level);
			}
		});
		bus.addListener((FurnaceFuelBurnTimeEvent e) -> {
			if (e.getItemStack().is(BotaniaExtrasBlocks.blazeKindling.asItem())) {
				e.setBurnTime(3200);
			} else if (e.getItemStack().is(BotaniaExtrasItems.flameLacedCharcoal)) {
				e.setBurnTime(2400);
			}
		});
		bus.addListener(LightningRodBlockEntity::onBoltJoin);
		bus.addListener(LightningRodBlockEntity::onLevelTick);
		bus.addListener((ServerStoppedEvent e) -> LightningRodBlockEntity.clear());
	}

	@Override
	public void registerClient(ModuleContext ctx) {
		BotaniaExtrasClient.init(ctx.modBus());
	}

	@Override
	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		BotaniaExtrasDatagen.gatherData(evt, lookup);
	}

	@Override
	public void addBlockTags(ModuleTagSink<Block> sink) {
		BotaniaExtrasTagEntries.addBlockTags(sink);
		BotaniaExtrasUtilityTags.addBlockTags(sink);
		BotaniaExtrasFlowerTagEntries.addBlockTags(sink);
	}

	@Override
	public void addItemTags(ModuleTagSink<Item> sink) {
		BotaniaExtrasTagEntries.addItemTags(sink);
		BotaniaExtrasUtilityTags.addItemTags(sink);
	}

	@Override
	public void addAccessoryTags(ModuleTagSink<Item> sink) {
		BotaniaExtrasTagEntries.addAccessoryTags(sink);
		BotaniaExtrasUtilityTags.addAccessoryTags(sink);
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent e) {
		e.registerItem(BotaniaCapabilities.AVATAR_WIELDABLE, (stack, ctx) -> new VibrantPlainsRodItem.AvatarBehavior(stack), BotaniaExtrasItems.vibrantPlainsRod);
		e.registerItem(BotaniaCapabilities.AVATAR_WIELDABLE, (stack, ctx) -> new ThunderingPeaksRodItem.AvatarBehavior(), BotaniaExtrasItems.thunderingPeaksRod);
		e.registerItem(BotaniaCapabilities.AVATAR_WIELDABLE, (stack, ctx) -> new StormySeaRodItem.AvatarBehavior(), BotaniaExtrasItems.stormySeaRod);
		e.registerItem(BotaniaCapabilities.BLOCK_PROVIDER, (stack, ctx) -> new VibrantPlainsRodItem.BlockProviderImpl(stack), BotaniaExtrasItems.vibrantPlainsRod);
		e.registerBlockEntity(BotaniaCapabilities.MANA_RECEIVER, BotaniaExtrasBlockEntities.DENDRIC_SUFFUSER, (be, ctx) -> be);
		e.registerBlockEntity(BotaniaCapabilities.SPARK_ATTACHABLE, BotaniaExtrasBlockEntities.DENDRIC_SUFFUSER, (be, ctx) -> be);
		List<Block> suffuserPlanks = new ArrayList<>();
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			if (set.kind() != BotaniaExtrasWoods.Kind.ALT) {
				suffuserPlanks.add(set.planks());
			}
		}
		e.registerBlock(BotaniaCapabilities.WANDABLE,
				(level, pos, state, be, ctx) -> (player, stack, side) -> DendricSuffuserBlock.tryForm(level, pos, state),
				suffuserPlanks.toArray(Block[]::new));
	}

	private static void registerFlammability() {
		FireBlockAccessor fire = (FireBlockAccessor) Blocks.FIRE;
		for (DyeColor color : DyeColor.values()) {
			fire.botania_register(BotaniaExtrasBlocks.getGrass(color), 60, 100);
			fire.botania_register(BotaniaExtrasBlocks.getTallGrass(color), 60, 100);
		}
		fire.botania_register(BotaniaExtrasBlocks.bifrostGrass, 60, 100);
		fire.botania_register(BotaniaExtrasBlocks.bifrostTallGrass, 60, 100);
		fire.botania_register(BotaniaExtrasBlocks.bifrostFlower, 60, 100);
		fire.botania_register(BotaniaExtrasBlocks.tallBifrostFlower, 60, 100);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			fire.botania_register(set.log(), 5, 5);
			fire.botania_register(set.planks(), 5, 20);
			fire.botania_register(set.slab(), 5, 20);
			fire.botania_register(set.stairs(), 5, 20);
			fire.botania_register(set.leaves(), 30, 60);
		}
		fire.botania_register(BotaniaExtrasWoods.sapling, 60, 100);
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			fire.botania_register(set.sapling(), 60, 100);
			if (set.type() == BotaniaExtrasMagicWoods.Type.INFERNAL) {
				continue;
			}
			fire.botania_register(set.log(), 5, 5);
			fire.botania_register(set.planks(), 5, 20);
			fire.botania_register(set.slab(), 5, 20);
			fire.botania_register(set.stairs(), 5, 20);
			fire.botania_register(set.leaves(), 30, 60);
		}
		fire.botania_register(BotaniaExtrasBlocks.sonicAmplifier, 5, 20);
	}

	private static void commonSetup() {
		DispenserBlock.registerBehavior(BotaniaExtrasItems.floralBifrostPowder, new FloralBifrostPowderItem.DispenseBehavior());
		PatchouliAPI.get().registerMultiblock(prefix("dendric_suffuser"), DendricSuffuserBlockEntity.MULTIBLOCK.get());
		BotaniaExtrasFlowers.registerPottedPlants();

		for (DyeColor color : DyeColor.values()) {
			BotaniaAPI.instance().registerPaintableBlock(BotaniaExtrasBlocks.getDirt(color), BotaniaExtrasBlocks::getDirt);
			ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasBlocks.getGrass(color).asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasBlocks.getTallGrass(color).asItem(), 0.5F);
			ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasItems.getSeeds(color), 0.3F);
		}
		BotaniaAPI.instance().registerPaintableBlock(BotaniaExtrasBlocks.bifrostDirt, BotaniaExtrasBlocks::getDirt);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasBlocks.bifrostGrass.asItem(), 0.3F);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasBlocks.bifrostTallGrass.asItem(), 0.5F);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasItems.bifrostSeeds, 0.3F);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasBlocks.bifrostFlower.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasBlocks.tallBifrostFlower.asItem(), 0.65F);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasItems.mysticalBifrostPetal, 0.3F);
		ComposterBlock.COMPOSTABLES.putIfAbsent(BotaniaExtrasWoods.sapling.asItem(), 0.3F);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			ComposterBlock.COMPOSTABLES.putIfAbsent(set.leaves().asItem(), 0.3F);
		}
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			ComposterBlock.COMPOSTABLES.putIfAbsent(set.leaves().asItem(), 0.3F);
			ComposterBlock.COMPOSTABLES.putIfAbsent(set.sapling().asItem(), 0.3F);
		}
	}
}
