package velrondevs.botania.client.render;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.client.model.LexiconItemModel;
import velrondevs.botania.client.model.TinyPotatoItemModel;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

@EventBusSubscriber(modid = BotaniaAPI.MODID, value = Dist.CLIENT)
public final class HeldModelRegistration {
	public static final Identifier LEXICON_MODEL_ID = prefix("lexicon");
	public static final Identifier TINY_POTATO_MODEL_ID = prefix("tiny_potato");
	public static final Identifier GAIA_HEAD_RENDERER_ID = prefix("gaia_head");

	private HeldModelRegistration() {}

	@SubscribeEvent
	public static void registerItemModels(RegisterItemModelsEvent evt) {
		evt.register(LEXICON_MODEL_ID, LexiconItemModel.Unbaked.MAP_CODEC);
		evt.register(TINY_POTATO_MODEL_ID, TinyPotatoItemModel.Unbaked.MAP_CODEC);
	}

	@SubscribeEvent
	public static void registerSpecialModels(RegisterSpecialModelRendererEvent evt) {
		evt.register(GAIA_HEAD_RENDERER_ID, GaiaHeadSpecialRenderer.Unbaked.MAP_CODEC);
	}
}
