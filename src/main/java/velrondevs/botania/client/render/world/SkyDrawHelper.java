package velrondevs.botania.client.render.world;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuSampler;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.attribute.EnvironmentAttributeProbe;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.MoonPhase;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.client.core.handler.ClientTickHandler;

import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Consumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class SkyDrawHelper {
	private SkyDrawHelper() {}

	private static final BlendFunction SKY_TRANSLUCENT = new BlendFunction(
			SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);

	public static final RenderPipeline TEXTURED_TRANSLUCENT = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(prefix("pipeline/sky_textured_translucent"))
			.withVertexShader("core/position_tex")
			.withFragmentShader("core/position_tex")
			.withSampler("Sampler0")
			.withColorTargetState(new ColorTargetState(SKY_TRANSLUCENT))
			.withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
			.withCull(false)
			.build();

	public static final RenderPipeline TEXTURED_ADDITIVE = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(prefix("pipeline/sky_textured_additive"))
			.withVertexShader("core/position_tex")
			.withFragmentShader("core/position_tex")
			.withSampler("Sampler0")
			.withColorTargetState(new ColorTargetState(BlendFunction.OVERLAY))
			.withVertexFormat(DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS)
			.withCull(false)
			.build();

	public static final RenderPipeline STARS_TRANSLUCENT = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET)
			.withLocation(prefix("pipeline/sky_stars_translucent"))
			.withVertexShader("core/stars")
			.withFragmentShader("core/stars")
			.withColorTargetState(new ColorTargetState(SKY_TRANSLUCENT))
			.withVertexFormat(DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS)
			.withCull(false)
			.build();

	private static final List<RenderPipeline> PIPELINES = List.of(TEXTURED_TRANSLUCENT, TEXTURED_ADDITIVE, STARS_TRANSLUCENT);

	private static final Identifier SUN_SPRITE = Identifier.withDefaultNamespace("sun");

	public record Frame(ClientLevel level, float partialTicks, float timeOfDay, float rainLevel,
			float starBrightness, long dayTime, float animTicks) {
		public float effectiveCelestialAngle() {
			return timeOfDay > 0.5F ? 0.5F - (timeOfDay - 0.5F) : timeOfDay;
		}
	}

	private static GpuBuffer topSkyBuffer;
	private static GpuBuffer bottomSkyBuffer;
	private static GpuBuffer sunriseBuffer;
	private static StaticQuads vanillaStars;

	public record StaticQuads(GpuBuffer buffer, int indexCount) {}

	public static void registerPipelines(Consumer<RenderPipeline> registrations) {
		PIPELINES.forEach(registrations);
	}

	public static Frame captureFrame(ClientLevel level, Camera camera, float partialTicks) {
		EnvironmentAttributeProbe probe = camera.attributeProbe();
		float sunAngle = probe.getValue(EnvironmentAttributes.SUN_ANGLE, partialTicks);
		float timeOfDay = sunAngle / 360F;
		timeOfDay -= (float) Math.floor(timeOfDay);
		float starBrightness = probe.getValue(EnvironmentAttributes.STAR_BRIGHTNESS, partialTicks);
		long dayTime = level.getDefaultClockTime();
		if (dayTime == 0L) {
			dayTime = level.getOverworldClockTime();
		}
		return new Frame(level, partialTicks, timeOfDay, level.getRainLevel(partialTicks), starBrightness, dayTime, ClientTickHandler.total());
	}

	public static Matrix4f modelView(Matrix4fc base, PoseStack pose) {
		return new Matrix4f(base).mul(pose.last().pose());
	}

	public static AbstractTexture texture(Identifier id) {
		return Minecraft.getInstance().getTextureManager().getTexture(id);
	}

	public static void drawMesh(RenderPipeline pipeline, MeshData mesh, Matrix4fc modelView, Vector4f color,
			@Nullable GpuTextureView texture, @Nullable GpuSampler sampler) {
		try (mesh) {
			GpuBuffer vertices = pipeline.getVertexFormat().uploadImmediateVertexBuffer(mesh.vertexBuffer());
			int indexCount = mesh.drawState().indexCount();
			RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(mesh.drawState().mode());
			GpuBuffer indexBuffer = indices.getBuffer(indexCount);
			GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
					.writeTransform(modelView, color, new Vector3f(), new Matrix4f());
			RenderTarget target = Minecraft.getInstance().getMainRenderTarget();
			try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder()
					.createRenderPass(() -> "Botania sky", target.getColorTextureView(), OptionalInt.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
				pass.setPipeline(pipeline);
				RenderSystem.bindDefaultUniforms(pass);
				pass.setUniform("DynamicTransforms", transforms);
				if (texture != null && sampler != null) {
					pass.bindTexture("Sampler0", texture, sampler);
				}
				pass.setVertexBuffer(0, vertices);
				pass.setIndexBuffer(indexBuffer, indices.type());
				pass.drawIndexed(0, 0, indexCount, 1);
			}
		}
	}

	public static void drawMesh(RenderPipeline pipeline, MeshData mesh, Matrix4fc modelView, Vector4f color, Identifier textureId) {
		AbstractTexture texture = texture(textureId);
		drawMesh(pipeline, mesh, modelView, color, texture.getTextureView(), texture.getSampler());
	}

	public static void drawQuads(RenderPipeline pipeline, StaticQuads quads, Matrix4fc modelView, Vector4f color) {
		RenderSystem.AutoStorageIndexBuffer indices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
		GpuBuffer indexBuffer = indices.getBuffer(quads.indexCount());
		GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
				.writeTransform(modelView, color, new Vector3f(), new Matrix4f());
		RenderTarget target = Minecraft.getInstance().getMainRenderTarget();
		try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder()
				.createRenderPass(() -> "Botania sky stars", target.getColorTextureView(), OptionalInt.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
			pass.setPipeline(pipeline);
			RenderSystem.bindDefaultUniforms(pass);
			pass.setUniform("DynamicTransforms", transforms);
			pass.setVertexBuffer(0, quads.buffer());
			pass.setIndexBuffer(indexBuffer, indices.type());
			pass.drawIndexed(0, 0, quads.indexCount(), 1);
		}
	}

	private static void drawFan(RenderPipeline pipeline, GpuBuffer buffer, int vertexCount, Matrix4fc modelView, Vector4f color) {
		GpuBufferSlice transforms = RenderSystem.getDynamicUniforms()
				.writeTransform(modelView, color, new Vector3f(), new Matrix4f());
		RenderTarget target = Minecraft.getInstance().getMainRenderTarget();
		try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder()
				.createRenderPass(() -> "Botania sky fan", target.getColorTextureView(), OptionalInt.empty(), target.getDepthTextureView(), OptionalDouble.empty())) {
			pass.setPipeline(pipeline);
			RenderSystem.bindDefaultUniforms(pass);
			pass.setUniform("DynamicTransforms", transforms);
			pass.setVertexBuffer(0, buffer);
			pass.draw(0, vertexCount);
		}
	}

	public static BufferBuilder begin(VertexFormat format) {
		return Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, format);
	}

	public static void drawSkyDisc(Matrix4fc modelView, int skyColor) {
		if (topSkyBuffer == null) {
			topSkyBuffer = buildSkyDisc("Botania top sky", 16.0F);
		}
		drawFan(RenderPipelines.SKY, topSkyBuffer, 10, modelView, ARGB.vector4fFromARGB32(skyColor));
	}

	public static void drawDarkDisc(Matrix4fc modelView) {
		if (bottomSkyBuffer == null) {
			bottomSkyBuffer = buildSkyDisc("Botania bottom sky", -16.0F);
		}
		Matrix4f mv = new Matrix4f(modelView).translate(0.0F, 12.0F, 0.0F);
		drawFan(RenderPipelines.SKY, bottomSkyBuffer, 10, mv, new Vector4f(0.0F, 0.0F, 0.0F, 1.0F));
	}

	public static void drawSunriseAndSunset(Matrix4fc modelView, float sunAngle, int color) {
		float alpha = ARGB.alphaFloat(color);
		if (alpha <= 0.001F) {
			return;
		}
		if (sunriseBuffer == null) {
			sunriseBuffer = buildSunriseFan();
		}
		PoseStack pose = new PoseStack();
		pose.mulPose(Axis.XP.rotationDegrees(90.0F));
		float angle = Mth.sin(sunAngle) < 0.0F ? 180.0F : 0.0F;
		pose.mulPose(Axis.ZP.rotationDegrees(angle + 90.0F));
		Matrix4f mv = modelView(modelView, pose).scale(1.0F, 1.0F, alpha);
		drawFan(RenderPipelines.SUNRISE_SUNSET, sunriseBuffer, 18, mv, ARGB.vector4fFromARGB32(color));
	}

	public static StaticQuads vanillaStars() {
		if (vanillaStars == null) {
			vanillaStars = buildVanillaStars();
		}
		return vanillaStars;
	}

	public static void drawVanillaStars(RenderPipeline pipeline, Matrix4fc modelView, Vector4f color) {
		drawQuads(pipeline, vanillaStars(), modelView, color);
	}

	public static void drawSun(Matrix4fc modelView, float size, float alpha) {
		TextureAtlas atlas = celestials();
		TextureAtlasSprite sprite = atlas.getSprite(SUN_SPRITE);
		drawCelestialQuad(atlas, modelView, size, alpha, sprite.getU0(), sprite.getV0(), sprite.getU1(), sprite.getV1());
	}

	public static void drawMoon(Matrix4fc modelView, MoonPhase phase, float size, float alpha) {
		TextureAtlas atlas = celestials();
		TextureAtlasSprite sprite = atlas.getSprite(Identifier.withDefaultNamespace("moon/" + phase.getSerializedName()));
		drawCelestialQuad(atlas, modelView, size, alpha, sprite.getU1(), sprite.getV1(), sprite.getU0(), sprite.getV0());
	}

	private static TextureAtlas celestials() {
		return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.CELESTIALS);
	}

	private static void drawCelestialQuad(TextureAtlas atlas, Matrix4fc modelView, float size, float alpha,
			float u0, float v0, float u1, float v1) {
		Matrix4f mv = new Matrix4f(modelView).translate(0.0F, 100.0F, 0.0F).scale(size, 1.0F, size);
		BufferBuilder builder = begin(DefaultVertexFormat.POSITION_TEX);
		builder.addVertex(-1.0F, 0.0F, -1.0F).setUv(u0, v0);
		builder.addVertex(1.0F, 0.0F, -1.0F).setUv(u1, v0);
		builder.addVertex(1.0F, 0.0F, 1.0F).setUv(u1, v1);
		builder.addVertex(-1.0F, 0.0F, 1.0F).setUv(u0, v1);
		drawMesh(RenderPipelines.CELESTIAL, builder.buildOrThrow(), mv, new Vector4f(1.0F, 1.0F, 1.0F, alpha),
				atlas.getTextureView(), atlas.getSampler());
	}

	public static GpuBuffer upload(String name, MeshData mesh) {
		try (mesh) {
			return RenderSystem.getDevice().createBuffer(() -> name, GpuBuffer.USAGE_VERTEX, mesh.vertexBuffer());
		}
	}

	private static GpuBuffer buildSkyDisc(String name, float y) {
		try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(10 * DefaultVertexFormat.POSITION.getVertexSize())) {
			BufferBuilder builder = new BufferBuilder(bytes, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
			float x = Math.signum(y) * 512.0F;
			builder.addVertex(0.0F, y, 0.0F);
			for (int i = -180; i <= 180; i += 45) {
				builder.addVertex(x * Mth.cos(i * Mth.DEG_TO_RAD), y, 512.0F * Mth.sin(i * Mth.DEG_TO_RAD));
			}
			return upload(name, builder.buildOrThrow());
		}
	}

	private static GpuBuffer buildSunriseFan() {
		try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(18 * DefaultVertexFormat.POSITION_COLOR.getVertexSize())) {
			BufferBuilder builder = new BufferBuilder(bytes, VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
			builder.addVertex(0.0F, 100.0F, 0.0F).setColor(ARGB.white(1.0F));
			for (int i = 0; i <= 16; i++) {
				float angle = i * Mth.TWO_PI / 16.0F;
				float sin = Mth.sin(angle);
				float cos = Mth.cos(angle);
				builder.addVertex(sin * 120.0F, cos * 120.0F, -cos * 40.0F).setColor(ARGB.white(0.0F));
			}
			return upload("Botania sunrise fan", builder.buildOrThrow());
		}
	}

	private static StaticQuads buildVanillaStars() {
		RandomSource random = RandomSource.create(10842L);
		try (ByteBufferBuilder bytes = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 1500 * 4)) {
			BufferBuilder builder = new BufferBuilder(bytes, VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
			for (int i = 0; i < 1500; i++) {
				float x = random.nextFloat() * 2.0F - 1.0F;
				float y = random.nextFloat() * 2.0F - 1.0F;
				float z = random.nextFloat() * 2.0F - 1.0F;
				float starSize = 0.15F + random.nextFloat() * 0.1F;
				float lengthSq = Mth.lengthSquared(x, y, z);
				if (lengthSq > 0.010000001F && lengthSq < 1.0F) {
					Vector3f center = new Vector3f(x, y, z).normalize(100.0F);
					float zRot = (float) (random.nextDouble() * Math.PI * 2.0);
					Matrix3f rotation = new Matrix3f().rotateTowards(new Vector3f(center).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);
					builder.addVertex(new Vector3f(starSize, -starSize, 0.0F).mul(rotation).add(center));
					builder.addVertex(new Vector3f(starSize, starSize, 0.0F).mul(rotation).add(center));
					builder.addVertex(new Vector3f(-starSize, starSize, 0.0F).mul(rotation).add(center));
					builder.addVertex(new Vector3f(-starSize, -starSize, 0.0F).mul(rotation).add(center));
				}
			}
			MeshData mesh = builder.buildOrThrow();
			int indexCount = mesh.drawState().indexCount();
			return new StaticQuads(upload("Botania stars", mesh), indexCount);
		}
	}

	public static void logFailure(String what, Throwable t) {
		BotaniaAPI.LOGGER.error("Botania sky renderer '{}' failed, falling back to the vanilla sky", what, t);
	}
}
