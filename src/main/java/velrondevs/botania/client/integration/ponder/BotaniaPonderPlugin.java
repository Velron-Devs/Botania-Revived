package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.client.registration.PonderPlugin;
import net.createmod.ponder.api.client.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.client.registration.PonderTagRegistrationHelper;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import velrondevs.botania.common.lib.LibMisc;

public class BotaniaPonderPlugin implements PonderPlugin {
	private static void bindMissingComponents() {
		if (!velrondevs.botania.integration.CreateCompat.isLoaded()) {
			try {
				velrondevs.botania.integration.catnip.CatnipConfigDefaults.init();
			} catch (LinkageError e) {
				velrondevs.botania.api.BotaniaAPI.LOGGER.warn("The installed Catnip version has no config API", e);
			}
		}
		BuiltInRegistries.ITEM.listElements().forEach(holder -> {
			try {
				holder.components();
			} catch (NullPointerException e) {
				holder.bindComponents(DataComponentMap.builder()
						.set(DataComponents.ITEM_MODEL, holder.key().identifier())
						.set(DataComponents.ITEM_NAME, Component.translatable(holder.value().getDescriptionId()))
						.set(DataComponents.MAX_STACK_SIZE, 64)
						.build());
			}
		});
	}

	@Override
	public String getModId() {
		return LibMisc.MOD_ID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<Identifier> helper) {
		bindMissingComponents();
		BotaniaPonderScenes.register(helper);
	}

	@Override
	public void registerTags(PonderTagRegistrationHelper<Identifier> helper) {
		bindMissingComponents();
		BotaniaPonderTags.register(helper);
	}
}
