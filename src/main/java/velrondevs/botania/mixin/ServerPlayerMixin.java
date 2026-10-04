package velrondevs.botania.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.world.SkyblockWorldEvents;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaStats;
import velrondevs.botania.xplat.XplatAbstractions;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

	@Inject(at = @At("RETURN"), method = "initMenu")
	private void onLogin(CallbackInfo ci) {
		if (XplatAbstractions.INSTANCE.gogLoaded()) {
			SkyblockWorldEvents.onPlayerJoin((ServerPlayer) (Object) this);
		}
	}

	@Inject(
		at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getVehicle()Lnet/minecraft/world/entity/Entity;"),
		method = "checkRidingStatistics", locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void trackLuminizerTravel(double dx, double dy, double dz, CallbackInfo ci, int cm, Entity mount) {
		if (mount.getType() == BotaniaEntities.PLAYER_MOVER) {
			((ServerPlayer) (Object) this).awardStat(BotaniaStats.LUMINIZER_ONE_CM, cm);
		}
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void onTick(CallbackInfo ci) {
		if (EquipmentHandler.instance instanceof EquipmentHandler.InventoryEquipmentHandler) {
			((EquipmentHandler.InventoryEquipmentHandler) EquipmentHandler.instance).onPlayerTick((Player) (Object) this);
		}
	}

}
