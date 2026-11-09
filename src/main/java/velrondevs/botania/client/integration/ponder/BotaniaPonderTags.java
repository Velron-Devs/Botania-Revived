package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.client.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.Identifier;

import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

final class BotaniaPonderTags {
	static final Identifier MANA_NETWORK = prefix(PonderStrings.MANA_NETWORK);
	static final Identifier CRAFTING_DEVICES = prefix(PonderStrings.CRAFTING_DEVICES);
	static final Identifier ALFHEIM = prefix(PonderStrings.ALFHEIM);
	static final Identifier FLOWERS = prefix(PonderStrings.FLOWERS);
	static final Identifier CORPOREA = prefix(PonderStrings.CORPOREA);

	private BotaniaPonderTags() {}

	static void register(PonderTagRegistrationHelper<Identifier> helper) {
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
	}
}
