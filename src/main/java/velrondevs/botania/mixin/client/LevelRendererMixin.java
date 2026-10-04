package velrondevs.botania.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import velrondevs.botania.client.core.SkyblockWorldInfo;
import velrondevs.botania.client.render.world.SkyblockSkyRenderer;
import velrondevs.botania.client.render.world.WorldOverlays;
import velrondevs.botania.xplat.BotaniaConfig;

@Mixin(value = LevelRenderer.class, priority = 900)
public class LevelRendererMixin {
	@Shadow
	@Nullable
	private VertexBuffer starBuffer;

	@Shadow
	@Final
	private RenderBuffers renderBuffers;
	@Shadow
	@Nullable
	private ClientLevel level;
	@Unique
	private static final Matrix4f SUN_SCALE = new Matrix4f().scale(2F, 1F, 2F);

	@Unique
	private static final Matrix4f MOON_SCALE = new Matrix4f().scale(1.5F, 1F, 1.5F);

	@Unique
	private static boolean isGogSky() {
		Level world = Minecraft.getInstance().level;
		boolean isGog = world.getLevelData() instanceof SkyblockWorldInfo skyblockInfo && skyblockInfo.isGardenOfGlass();
		return BotaniaConfig.client().enableFancySkybox()
				&& world.dimension() == Level.OVERWORLD
				&& (BotaniaConfig.client().enableFancySkyboxInNormalWorlds() || isGog);
	}

	@Inject(
		method = "renderSky",
		slice = @Slice(
			from = @At(
				ordinal = 0, value = "INVOKE",
				target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"
			)
		),
		at = @At(
			shift = At.Shift.AFTER,
			ordinal = 0,
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V"
		),
		require = 0
	)
	private void renderExtras(Matrix4f frustumMatrix, Matrix4f projMat, float partialTicks, Camera camera,
			boolean foggy, Runnable resetFog, CallbackInfo ci, @Local PoseStack ms) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderExtra(ms, Minecraft.getInstance().level, partialTicks, 0);
		}
	}

	@ModifyVariable(
		method = "renderSky",
		slice = @Slice(
			from = @At(ordinal = 1, value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getTimeOfDay(F)F"),
			to = @At(ordinal = 0, value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V")
		),
		at = @At(value = "CONSTANT", args = "floatValue=30.0"),
		ordinal = 2,
		require = 0
	)
	private Matrix4f makeSunBigger(Matrix4f matrix) {
		if (isGogSky()) {

			matrix = new Matrix4f(matrix);
			matrix.mul(SUN_SCALE);
		}
		return matrix;
	}

	@ModifyVariable(
		method = "renderSky",
		slice = @Slice(
			from = @At(ordinal = 0, value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V"),
			to = @At(ordinal = 1, value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/resources/ResourceLocation;)V")
		),
		at = @At(value = "CONSTANT", args = "floatValue=20.0"),
		ordinal = 2,
		require = 0
	)
	private Matrix4f makeMoonBigger(Matrix4f matrix) {
		if (isGogSky()) {
			matrix.mul(MOON_SCALE);
		}
		return matrix;
	}

	@Inject(
		method = "renderSky",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getStarBrightness(F)F"),
		require = 0
	)
	private void renderExtraStars(Matrix4f frustumMatrix, Matrix4f projMat, float partialTicks, Camera camera,
			boolean foggy, Runnable resetFog, CallbackInfo ci, @Local PoseStack ms) {
		if (isGogSky()) {
			SkyblockSkyRenderer.renderStars(starBuffer, ms, projMat, partialTicks, resetFog);
		}
	}

	@Inject(
		method = "renderLevel",
		at = @At(
			shift = At.Shift.AFTER,
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/debug/DebugRenderer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;DDD)V",
			ordinal = 0
		)
	)
	private void renderOverlays(DeltaTracker deltaTracker, boolean drawBlockOutline, Camera camera, GameRenderer gameRenderer,
			LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projMat, CallbackInfo ci, @Local PoseStack ps) {
		WorldOverlays.renderWorldLast(camera, deltaTracker.getGameTimeDeltaPartialTick(false), ps, renderBuffers, level);
	}
}
