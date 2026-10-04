package velrondevs.botania.api;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;

import velrondevs.botania.api.block.WandHUD;

public final class BotaniaClientCapabilities {
	public static final BlockCapability<WandHUD, Void> WAND_HUD = BlockCapability.createVoid(ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "wand_hud"), WandHUD.class);
	public static final EntityCapability<WandHUD, Void> ENTITY_WAND_HUD = EntityCapability.createVoid(ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, "wand_hud"), WandHUD.class);

	private BotaniaClientCapabilities() {}
}
