package velrondevs.botania.module.botaniaextras.client;

import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.item.DyeColor;

import java.util.List;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

import velrondevs.botania.api.BotaniaClientCapabilities;
import velrondevs.botania.client.render.block_entity.SpecialFlowerBlockEntityRenderer;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlockEntities;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasFlowers;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasWoods;
import velrondevs.botania.module.botaniaextras.IridescentColors;
import velrondevs.botania.module.botaniaextras.block.CrysanthermumBlockEntity;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlock;
import velrondevs.botania.module.botaniaextras.block.DendricSuffuserBlockEntity;
import velrondevs.botania.module.botaniaextras.block.IridescentLanternBlock;

public final class BotaniaExtrasClient {
	private BotaniaExtrasClient() {}

	public static void init(IEventBus modBus) {
		modBus.addListener(BotaniaExtrasClient::blockColors);
		modBus.addListener(BotaniaExtrasTints::register);
		modBus.addListener(BotaniaExtrasClient::renderers);
		modBus.addListener(EmblemModels::register);
		modBus.addListener(EmblemModels::bake);
		modBus.addListener(BotaniaExtrasClient::capabilities);
		NeoForge.EVENT_BUS.addListener(SoundShaping::onPlaySound);
		BotaniaExtrasUtilitiesClient.init(modBus);
	}

	private static void blockColors(RegisterColorHandlersEvent.BlockTintSources e) {
		for (DyeColor color : DyeColor.values()) {
			int rgb = IridescentColors.rgb(color);
			e.register(List.of((BlockTintSource) state -> rgb),
					BotaniaExtrasBlocks.getDirt(color), BotaniaExtrasBlocks.getGrass(color), BotaniaExtrasBlocks.getTallGrass(color));
		}
		e.register(List.of((BlockTintSource) state -> IridescentColors.lanternColor(state.getValue(IridescentLanternBlock.POWER))),
				BotaniaExtrasBlocks.iridescentLantern);
		e.register(List.of((BlockTintSource) state -> {
			int index = state.getValue(DendricSuffuserBlock.COLOR);
			return index < DendricSuffuserBlock.BIFROST ? IridescentColors.rgb(DyeColor.byId(index)) : -1;
		}), BotaniaExtrasBlocks.dendricSuffuser);
		for (BotaniaExtrasWoods.WoodSet set : BotaniaExtrasWoods.sets()) {
			if (set.kind() == BotaniaExtrasWoods.Kind.IRIDESCENT) {
				int rgb = IridescentColors.rgb(set.color());
				e.register(List.of((BlockTintSource) state -> rgb),
						set.log(), set.planks(), set.leaves(), set.slab(), set.stairs());
			}
		}
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
