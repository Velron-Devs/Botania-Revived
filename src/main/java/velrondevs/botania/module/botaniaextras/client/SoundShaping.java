package velrondevs.botania.module.botaniaextras.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.client.sounds.SoundBufferLibrary;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;

import velrondevs.botania.module.botaniaextras.BotaniaExtrasTags;

import java.util.concurrent.CompletableFuture;

public final class SoundShaping {
	private static final double SEALING_RANGE = 8;
	private static final double AMPLIFIER_RANGE = 2;
	private static final float SEALING_FACTOR = 0.5F;
	private static final float AMPLIFIER_FACTOR = 5F;
	private static final int SCAN_RANGE = 8;

	private SoundShaping() {}

	private static boolean shapes(BlockState state) {
		return state.is(BotaniaExtrasTags.Blocks.SEALING_WOOD) || state.is(BotaniaExtrasTags.Blocks.SOUND_AMPLIFIERS);
	}

	public static void onPlaySound(PlaySoundEvent e) {
		SoundInstance sound = e.getSound();
		if (sound == null || sound instanceof TickableSoundInstance || sound instanceof ScaledSound || sound.isRelative()) {
			return;
		}
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) {
			return;
		}
		float multiplier = multiplier(level, sound.getX(), sound.getY(), sound.getZ());
		if (multiplier != 1F) {
			e.setSound(new ScaledSound(sound, multiplier));
		}
	}

	public static float multiplier(ClientLevel level, double x, double y, double z) {
		boolean sealed = false;
		float amplified = 1F;
		int minX = Mth.floor(x - SCAN_RANGE);
		int maxX = Mth.floor(x + SCAN_RANGE);
		int minY = Mth.floor(y - SCAN_RANGE);
		int maxY = Mth.floor(y + SCAN_RANGE);
		int minZ = Mth.floor(z - SCAN_RANGE);
		int maxZ = Mth.floor(z + SCAN_RANGE);
		for (int cx = minX >> 4; cx <= maxX >> 4; cx++) {
			for (int cz = minZ >> 4; cz <= maxZ >> 4; cz++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
				if (chunk == null) {
					continue;
				}
				for (int sy = Math.max(minY, level.getMinBuildHeight()) >> 4; sy <= Math.min(maxY, level.getMaxBuildHeight() - 1) >> 4; sy++) {
					int index = chunk.getSectionIndexFromSectionY(sy);
					if (index < 0 || index >= chunk.getSections().length) {
						continue;
					}
					LevelChunkSection section = chunk.getSection(index);
					if (section.hasOnlyAir() || !section.maybeHas(SoundShaping::shapes)) {
						continue;
					}
					int fromX = Math.max(minX, cx << 4);
					int toX = Math.min(maxX, (cx << 4) + 15);
					int fromY = Math.max(minY, sy << 4);
					int toY = Math.min(maxY, (sy << 4) + 15);
					int fromZ = Math.max(minZ, cz << 4);
					int toZ = Math.min(maxZ, (cz << 4) + 15);
					for (int bx = fromX; bx <= toX; bx++) {
						for (int by = fromY; by <= toY; by++) {
							for (int bz = fromZ; bz <= toZ; bz++) {
								BlockState state = section.getBlockState(bx & 15, by & 15, bz & 15);
								if (!shapes(state)) {
									continue;
								}
								double dx = bx + 0.5 - x;
								double dy = by + 0.5 - y;
								double dz = bz + 0.5 - z;
								double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
								if (state.is(BotaniaExtrasTags.Blocks.SOUND_AMPLIFIERS)) {
									if (dist <= AMPLIFIER_RANGE) {
										amplified *= AMPLIFIER_FACTOR;
									}
								} else if (dist <= SEALING_RANGE) {
									sealed = true;
								}
							}
						}
					}
				}
			}
		}
		return amplified * (sealed ? SEALING_FACTOR : 1F);
	}

	private static final class ScaledSound implements SoundInstance {
		private final SoundInstance delegate;
		private final float multiplier;

		private ScaledSound(SoundInstance delegate, float multiplier) {
			this.delegate = delegate;
			this.multiplier = multiplier;
		}

		@Override
		public ResourceLocation getLocation() {
			return delegate.getLocation();
		}

		@Override
		public WeighedSoundEvents resolve(SoundManager manager) {
			return delegate.resolve(manager);
		}

		@Override
		public Sound getSound() {
			return delegate.getSound();
		}

		@Override
		public SoundSource getSource() {
			return delegate.getSource();
		}

		@Override
		public boolean isLooping() {
			return delegate.isLooping();
		}

		@Override
		public boolean isRelative() {
			return delegate.isRelative();
		}

		@Override
		public int getDelay() {
			return delegate.getDelay();
		}

		@Override
		public float getVolume() {
			return delegate.getVolume() * multiplier;
		}

		@Override
		public float getPitch() {
			return delegate.getPitch();
		}

		@Override
		public double getX() {
			return delegate.getX();
		}

		@Override
		public double getY() {
			return delegate.getY();
		}

		@Override
		public double getZ() {
			return delegate.getZ();
		}

		@Override
		public Attenuation getAttenuation() {
			return delegate.getAttenuation();
		}

		@Override
		public boolean canStartSilent() {
			return delegate.canStartSilent();
		}

		@Override
		public boolean canPlaySound() {
			return delegate.canPlaySound();
		}

		@Override
		public CompletableFuture<AudioStream> getStream(SoundBufferLibrary library, Sound sound, boolean looping) {
			return delegate.getStream(library, sound, looping);
		}
	}
}
