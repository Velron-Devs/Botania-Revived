package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;

import velrondevs.botania.integration.create.CreateIntegration;
import velrondevs.botania.integration.create.ManaMotorScenes;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class BotaniaPonderTags {
	public static final ResourceLocation MANA_NETWORK = prefix(PonderStrings.MANA_NETWORK);
	static final ResourceLocation CRAFTING_DEVICES = prefix(PonderStrings.CRAFTING_DEVICES);
	static final ResourceLocation ALFHEIM = prefix(PonderStrings.ALFHEIM);
	static final ResourceLocation FLOWERS = prefix(PonderStrings.FLOWERS);
	static final ResourceLocation CORPOREA = prefix(PonderStrings.CORPOREA);

	private BotaniaPonderTags() {}

	static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
		helper.registerTag(MANA_NETWORK)
				.title(PonderStrings.tagTitle(PonderStrings.MANA_NETWORK))
				.description(PonderStrings.tagDescription(PonderStrings.MANA_NETWORK))
				.item(BotaniaBlocks.manaSpreader)
				.addToIndex()
				.register();
		helper.registerTag(CRAFTING_DEVICES)
				.title(PonderStrings.tagTitle(PonderStrings.CRAFTING_DEVICES))
				.description(PonderStrings.tagDescription(PonderStrings.CRAFTING_DEVICES))
				.item(BotaniaBlocks.runeAltar)
				.addToIndex()
				.register();
		helper.registerTag(ALFHEIM)
				.title(PonderStrings.tagTitle(PonderStrings.ALFHEIM))
				.description(PonderStrings.tagDescription(PonderStrings.ALFHEIM))
				.item(BotaniaBlocks.alfPortal)
				.addToIndex()
				.register();
		helper.registerTag(FLOWERS)
				.title(PonderStrings.tagTitle(PonderStrings.FLOWERS))
				.description(PonderStrings.tagDescription(PonderStrings.FLOWERS))
				.item(BotaniaFlowerBlocks.endoflame)
				.addToIndex()
				.register();
		helper.registerTag(CORPOREA)
				.title(PonderStrings.tagTitle(PonderStrings.CORPOREA))
				.description(PonderStrings.tagDescription(PonderStrings.CORPOREA))
				.item(BotaniaBlocks.corporeaIndex)
				.addToIndex()
				.register();
		if (ModList.get().isLoaded(CreateIntegration.MOD_ID)) {
			ManaMotorScenes.registerTags(helper);
		}
	}
}
