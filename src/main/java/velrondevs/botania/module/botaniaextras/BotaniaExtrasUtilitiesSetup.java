package velrondevs.botania.module.botaniaextras;

import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;

import velrondevs.botania.module.ModuleContext;

public final class BotaniaExtrasUtilitiesSetup {
	private BotaniaExtrasUtilitiesSetup() {}

	public static void register(ModuleContext ctx) {
		ctx.bindBlocks(BotaniaExtrasUtilities::registerBlocks);
		ctx.bindItems(BotaniaExtrasUtilities::registerItems);
		ctx.bind(Registries.BLOCK_ENTITY_TYPE, BotaniaExtrasUtilities::registerBlockEntities);
		ctx.bind(Registries.ENTITY_TYPE, BotaniaExtrasUtilities::registerEntities);
		ctx.bind(Registries.MOB_EFFECT, BotaniaExtrasUtilities::registerEffects);
		ctx.bind(Registries.RECIPE_SERIALIZER, BotaniaExtrasUtilities::registerSerializers);

		IEventBus modBus = ctx.modBus();
		modBus.addListener(BotaniaExtrasUtilities::registerAttributes);
		modBus.addListener(BotaniaExtrasUtilities::registerSpawns);
		modBus.addListener(BotaniaExtrasUtilities::registerCapabilities);
		modBus.addListener(BotaniaExtrasUtilities::registerPayloads);
	}
}
