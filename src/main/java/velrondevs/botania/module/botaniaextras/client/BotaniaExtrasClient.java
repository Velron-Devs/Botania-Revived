package velrondevs.botania.module.botaniaextras.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.DyeColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasMagicWoods;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.IridescentColors;
import velrondevs.botania.module.botaniaextras.block.CrysanthermumBlockEntity;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlock;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlockEntity;
import velrondevs.botania.module.botaniaextras.block.IridescentLanternBlock;
import velrondevs.botania.module.botaniaextras.item.FrozenStarItem;
import velrondevs.botania.module.botaniaextras.item.VibrantPlainsRodItem;

public final class BotaniaExtrasClient {
	private BotaniaExtrasClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaExtrasClient::clientSetup);
		modBus.addListener(BotaniaExtrasClient::blockColors);
		modBus.addListener(BotaniaExtrasClient::itemColors);
		modBus.addListener(BotaniaExtrasClient::renderers);
		modBus.addListener(EmblemModels::register);
		modBus.addListener(EmblemModels::bake);
		modBus.addListener(BotaniaExtrasClient::capabilities);
		NeoForge.EVENT_BUS.addListener(SoundShaping::onPlaySound);
		BotaniaExtrasUtilitiesClient.init(modBus);
	}

	private static int rainbow() {
		return IridescentColors.rainbow(ClientTickHandler.total());
	}

	private static void clientSetup(FMLClientSetupEvent e) {
		RenderType cutout = RenderType.cutout();
		for (DyeColor color : DyeColor.values()) {
			ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasBlocks.getGrass(color), cutout);
			ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasBlocks.getTallGrass(color), cutout);
		}
		ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasBlocks.bifrostGrass, cutout);
		ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasBlocks.bifrostTallGrass, cutout);
		ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasBlocks.bifrostFlower, cutout);
		ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasBlocks.tallBifrostFlower, cutout);
		ItemBlockRenderTypes.setRenderLayer(BotaniaExtrasWoods.sapling, cutout);
		for (BotaniaExtrasMagicWoods.MagicSet set : BotaniaExtrasMagicWoods.sets()) {
			ItemBlockRenderTypes.setRenderLayer(set.sapling(), cutout);
		}
	}

	private static void blockColors(RegisterColorHandlersEvent.Block e) {
		for (DyeColor color : DyeColor.values()) {
			int rgb = IridescentColors.rgb(color);
			e.register((state, level, pos, tint) -> tint == 0 ? rgb : -1,
					BotaniaExtrasBlocks.getDirt(color), BotaniaExtrasBlocks.getGrass(color), BotaniaExtrasBlocks.getTallGrass(color));
		}
		e.register((state, level, pos, tint) -> tint == 0 ? IridescentColors.lanternColor(state.getValue(IridescentLanternBlock.POWER)) : -1,
				BotaniaExtrasBlocks.iridescentLantern);
		e.register((state, level, pos, tint) -> {
			int index = state.getValue(DendricSuffuserBlock.COLOR);
			return tint == 0 && index < DendricSuffuserBlock.BIFROST ? IridescentColors.rgb(DyeColor.byId(index)) : -1;
		}, BotaniaExtrasBlocks.dendricSuffuser);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			if (set.kind() == BotaniaExtrasWoods.Kind.IRIDESCENT) {
				int rgb = IridescentColors.rgb(set.color());
				e.register((state, level, pos, tint) -> tint == 0 ? rgb : -1,
						set.log(), set.planks(), set.leaves(), set.slab(), set.stairs());
			}
		}
	}

	private static void itemColors(RegisterColorHandlersEvent.Item e) {
		for (DyeColor color : DyeColor.values()) {
			int rgb = FastColor.ARGB32.opaque(IridescentColors.rgb(color));
			e.register((stack, tint) -> tint == 0 ? rgb : -1,
					BotaniaExtrasBlocks.getDirt(color), BotaniaExtrasBlocks.getGrass(color), BotaniaExtrasBlocks.getTallGrass(color),
					BotaniaExtrasItems.getSeeds(color));
		}
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			if (set.kind() == BotaniaExtrasWoods.Kind.IRIDESCENT) {
				int rgb = FastColor.ARGB32.opaque(IridescentColors.rgb(set.color()));
				e.register((stack, tint) -> tint == 0 ? rgb : -1,
						set.log(), set.planks(), set.leaves(), set.slab(), set.stairs());
			}
		}
		e.register((stack, tint) -> tint == 0 ? FastColor.ARGB32.opaque(rainbow()) : -1,
				BotaniaExtrasItems.bifrostSeeds, BotaniaExtrasItems.floralBifrostPowder, BotaniaExtrasItems.mysticalBifrostPetal);
		e.register((stack, tint) -> tint == 0 ? FastColor.ARGB32.opaque(IridescentColors.lanternColor(0)) : -1,
				BotaniaExtrasBlocks.iridescentLantern);
		e.register((stack, tint) -> {
			if (tint != 1) {
				return -1;
			}
			int index = VibrantPlainsRodItem.getColorIndex(stack);
			return FastColor.ARGB32.opaque(index >= VibrantPlainsRodItem.BIFROST ? rainbow() : IridescentColors.rgb(VibrantPlainsRodItem.dyeOf(index)));
		}, BotaniaExtrasItems.vibrantPlainsRod);
		e.register((stack, tint) -> tint == 0 ? FastColor.ARGB32.opaque(rainbow()) : -1, BotaniaExtrasItems.prismaticLakeRod);
		e.register((stack, tint) -> tint == 0 ? FastColor.ARGB32.opaque(((LensItem) stack.getItem()).getLensColor(stack, Minecraft.getInstance().level)) : -1, BotaniaExtrasItems.phantomFlashLens);
		e.register((stack, tint) -> {
			if (tint != 0) {
				return -1;
			}
			int color = FrozenStarItem.getColor(stack);
			return FastColor.ARGB32.opaque(color == FrozenStarItem.RAINBOW ? rainbow() : color);
		}, BotaniaExtrasItems.frozenStar);
	}

	private static void renderers(EntityRenderersEvent.RegisterRenderers e) {
		e.registerBlockEntityRenderer(BotaniaExtrasBlockEntities.FROZEN_STAR, FrozenStarRenderer::new);
		e.registerBlockEntityRenderer(BotaniaExtrasBlockEntities.ITEM_PLATFORM, ItemPlatformRenderer::new);
		e.registerBlockEntityRenderer(BotaniaExtrasFlowers.CRYSANTHERMUM, SpecialFlowerBlockEntityRenderer::new);
	}

	private static void capabilities(RegisterCapabilitiesEvent e) {
		e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, BotaniaExtrasBlockEntities.DENDRIC_SUFFUSER, (be, ctx) -> new DendricSuffuserBlockEntity.WandHud(be));
		e.registerBlockEntity(BotaniaClientCapabilities.WAND_HUD, BotaniaExtrasFlowers.CRYSANTHERMUM, (be, ctx) -> new CrysanthermumBlockEntity.WandHud(be));
	}
}
