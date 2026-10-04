package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.common.lib.LibMisc;

public class BotaniaPonderPlugin implements PonderPlugin {
	@Override
	public String getModId() {
		return LibMisc.MOD_ID;
	}

	@Override
	public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		BotaniaPonderScenes.register(helper);
	}

	@Override
	public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
		BotaniaPonderTags.register(helper);
	}
}
