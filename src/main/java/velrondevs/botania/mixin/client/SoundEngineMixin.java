package velrondevs.botania.mixin.client;

import com.mojang.datafixers.util.Pair;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import velrondevs.botania.common.block.flower.functional.BergamuteBlockEntity;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@Mixin(SoundEngine.class)
public class SoundEngineMixin {
	@Unique
	@Nullable
	private SoundInstance tmpSound;

	@Unique
	private static Set<SoundInstance> mutedSounds;

	@Inject(at = @At("HEAD"), method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F")
	private void captureSound(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = sound;
	}

	@Inject(at = @At("HEAD"), method = "play")
	private void captureSound2(SoundInstance sound, CallbackInfo ci) {
		tmpSound = sound;
	}

	@Unique
	private static boolean shouldSilence(SoundInstance sound) {
		return sound.getSource() != SoundSource.VOICE
				&& sound.getSource() != SoundSource.MUSIC
				&& sound.getSource() != SoundSource.RECORDS
				&& sound.getSource() != SoundSource.AMBIENT;
	}

	@ModifyArg(index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F"), method = "calculateVolume(FLnet/minecraft/sounds/SoundSource;)F")
	private float bergamuateAttenuate(float volume) {
		SoundInstance sound = this.tmpSound;

		if (sound != null && shouldSilence(sound)) {

			var level = Minecraft.getInstance().level;
			Pair<Integer, BergamuteBlockEntity> countAndBerg = level == null
					? Pair.of(0, null)
					: BergamuteBlockEntity.getBergamutesNearby(level, sound.getX(), sound.getY(), sound.getZ(), 8);
			int count = countAndBerg.getFirst();
			if (count > 0) {
				if (mutedSounds == null) {
					mutedSounds = Collections.newSetFromMap(new WeakHashMap<>());
				}
				if (mutedSounds.add(sound) && Math.random() < 0.5) {
					BergamuteBlockEntity.particle(countAndBerg.getSecond());
				}

				return volume * (float) Math.pow(0.5, count);
			}
		}
		return volume;
	}

	@Inject(at = @At("RETURN"), method = "calculateVolume(Lnet/minecraft/client/resources/sounds/SoundInstance;)F")
	private void clearSound(SoundInstance sound, CallbackInfoReturnable<Float> cir) {
		tmpSound = null;
	}

	@Inject(at = @At("RETURN"), method = "play")
	private void clearSound2(SoundInstance sound, CallbackInfo ci) {
		tmpSound = null;
	}
}
