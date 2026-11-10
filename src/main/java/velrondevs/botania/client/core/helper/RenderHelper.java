package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractEndPortalRenderer;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.LayeringTransform;
import net.minecraft.client.renderer.rendertype.OutputTarget;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.SpriteId;
import com.mojang.math.Axis;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import org.joml.Matrix4f;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.client.render.block_entity.PylonBlockEntityRenderer;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.item.equipment.bauble.FlugelTiaraItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class RenderHelper {
	private static final RenderType STAR;
	public static final RenderType RECTANGLE;
	public static final RenderType CIRCLE;
	public static final RenderType RED_STRING;
	public static final RenderType LINE_1_NO_DEPTH;
	public static final RenderType LINE_4_NO_DEPTH;
	public static final RenderType LINE_5_NO_DEPTH;
	public static final RenderType LINE_8_NO_DEPTH;
	public static final RenderType SPARK;
	public static final RenderType LIGHT_RELAY;
	public static final RenderType ICON_OVERLAY;
	public static final RenderType BABYLON_ICON;
	public static final RenderType MANA_POOL_WATER;
	public static final RenderType TERRA_PLATE;
	public static final RenderType ENCHANTER;
	public static final RenderType HALO;
	public static final RenderType MANA_PYLON_GLOW = getPylonGlow("mana_pylon_glow", PylonBlockEntityRenderer.MANA_TEXTURE);
	public static final RenderType NATURA_PYLON_GLOW = getPylonGlow("natura_pylon_glow", PylonBlockEntityRenderer.NATURA_TEXTURE);
	public static final RenderType GAIA_PYLON_GLOW = getPylonGlow("gaia_pylon_glow", PylonBlockEntityRenderer.GAIA_TEXTURE);
	public static final RenderType MANA_PYLON_GLOW_DIRECT = getPylonGlowDirect("mana_pylon_glow_direct", PylonBlockEntityRenderer.MANA_TEXTURE);
	public static final RenderType NATURA_PYLON_GLOW_DIRECT = getPylonGlowDirect("natura_pylon_glow_direct", PylonBlockEntityRenderer.NATURA_TEXTURE);
	public static final RenderType GAIA_PYLON_GLOW_DIRECT = getPylonGlowDirect("gaia_pylon_glow_direct", PylonBlockEntityRenderer.GAIA_TEXTURE);

	public static final RenderType ASTROLABE_PREVIEW;
	public static final RenderType STARFIELD;
	public static final RenderType LIGHTNING;
	public static final RenderType TRANSLUCENT;

	private static final int ITEM_AND_PADDING_WIDTH = 20;

	private static final double INITIAL_OFFSET = 0.005;
	private static final double OFFSET_INCREMENT = 0.001;

	private static double offY = INITIAL_OFFSET;

	public static double getOffY() {
		return offY;
	}

	public static void incrementOffY() {
		offY += OFFSET_INCREMENT;
	}

	public static void onWorldRenderLast() {
		offY = INITIAL_OFFSET;
	}

	private static Identifier safeTexture(Identifier texture) {
		if (texture == null) {
			velrondevs.botania.api.BotaniaAPI.LOGGER.error("RenderType created with a null texture", new Throwable());
			return net.minecraft.client.renderer.texture.MissingTextureAtlasSprite.getLocation();
		}
		return texture;
	}

	private static RenderType make(String name, RenderSetup setup) {
		return RenderType.create(ResourcesLib.PREFIX_MOD + name, setup);
	}

	static {
		STAR = make("star", RenderSetup.builder(CoreShaders.STAR).createRenderSetup());

		RECTANGLE = make("rectangle_highlight", RenderSetup.builder(CoreShaders.HIGHLIGHT_QUADS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).sortOnUpload().createRenderSetup());
		CIRCLE = make("circle_highlight", RenderSetup.builder(CoreShaders.HIGHLIGHT_TRIANGLES)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET).createRenderSetup());

		RED_STRING = makeLine("red_string", RenderPipelines.LINES, 1, false);
		LINE_1_NO_DEPTH = makeLine("line_1_no_depth", CoreShaders.LINES_NO_DEPTH, 1, true);
		LINE_4_NO_DEPTH = makeLine("line_4_no_depth", CoreShaders.LINES_NO_DEPTH, 4, true);
		LINE_5_NO_DEPTH = makeLine("line_5_no_depth", CoreShaders.LINES_NO_DEPTH, 5, true);
		LINE_8_NO_DEPTH = makeLine("line_8_no_depth", CoreShaders.LINES_NO_DEPTH, 8, true);

		SPARK = make("spark", RenderSetup.builder(CoreShaders.TEXTURED_LIGHTMAP)
				.withTexture("Sampler0", TextureAtlas.LOCATION_ITEMS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.useLightmap().affectsCrumbling().createRenderSetup());
		LIGHT_RELAY = makeShaded("light_relay", BotaniaShaders.HALO, CoreShaders.TEXTURED_COLOR, b -> b
				.withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.affectsCrumbling());
		ICON_OVERLAY = make("icon_overlay", RenderSetup.builder(CoreShaders.TEXTURED_LIGHTMAP)
				.withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.useLightmap().affectsCrumbling().createRenderSetup());
		MANA_POOL_WATER = makeShaded("mana_pool_water", BotaniaShaders.MANA_POOL, CoreShaders.TEXTURED_LIGHTMAP, b -> b
				.withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.useLightmap());
		TERRA_PLATE = makeShaded("terra_plate_rune", BotaniaShaders.TERRA_PLATE, CoreShaders.TEXTURED_LIGHTMAP, b -> b
				.withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.useLightmap());
		ENCHANTER = makeShaded("enchanter_rune", BotaniaShaders.ENCHANTER, CoreShaders.TEXTURED_LIGHTMAP, b -> b
				.withTexture("Sampler0", TextureAtlas.LOCATION_BLOCKS)
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.useLightmap());

		BABYLON_ICON = makeShaded("babylon", BotaniaShaders.HALO_NO_CULL, CoreShaders.TEXTURED_COLOR_NO_CULL, b -> b
				.withTexture("Sampler0", Identifier.parse(ResourcesLib.MISC_BABYLON))
				.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET)
				.affectsCrumbling());

		HALO = makeShaded("halo", BotaniaShaders.HALO_NO_CULL, CoreShaders.TEXTURED_COLOR_NO_CULL, b -> b
				.withTexture("Sampler0", FlugelTiaraItem.textureHalo)
				.affectsCrumbling());

		STARFIELD = make("starfield", RenderSetup.builder(CoreShaders.starfield())
				.withTexture("Sampler0", AbstractEndPortalRenderer.END_SKY_LOCATION)
				.withTexture("Sampler1", AbstractEndPortalRenderer.END_PORTAL_LOCATION)
				.createRenderSetup());
		LIGHTNING = make("lightning", RenderSetup.builder(CoreShaders.LIGHTNING_QUADS).sortOnUpload().createRenderSetup());
		TRANSLUCENT = RenderTypes.entityTranslucentCullItemTarget(TextureAtlas.LOCATION_BLOCKS);
		ASTROLABE_PREVIEW = RenderTypes.entityTranslucentCullItemTarget(TextureAtlas.LOCATION_BLOCKS);
	}

	private static RenderType makeLine(String name, RenderPipeline pipeline, float width, boolean direct) {
		RenderSetup.RenderSetupBuilder builder = RenderSetup.builder(pipeline)
				.setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING);
		if (!direct) {
			builder = builder.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET);
		}
		RenderType type = make(name, builder.createRenderSetup());
		DeferredBufferSource.setLineWidth(type, width);
		return type;
	}

	private static RenderType getPylonGlowDirect(String name, Identifier texture) {
		return getPylonGlow(name, texture, true);
	}

	private static RenderType getPylonGlow(String name, Identifier texture) {
		return getPylonGlow(name, texture, false);
	}

	private static RenderType getPylonGlow(String name, Identifier texture, boolean direct) {
		return makeShaded(name, BotaniaShaders.PYLON, RenderPipelines.ENTITY_TRANSLUCENT, b -> {
			RenderSetup.RenderSetupBuilder builder = b.withTexture("Sampler0", safeTexture(texture))
					.useLightmap()
					.useOverlay();
			return direct ? builder : builder.setOutputTarget(OutputTarget.ITEM_ENTITY_TARGET);
		});
	}

	private static RenderType makeShaded(String name, RenderPipeline shader, RenderPipeline fallback, Function<RenderSetup.RenderSetupBuilder, RenderSetup.RenderSetupBuilder> setup) {
		return BotaniaShaders.shaded(ResourcesLib.PREFIX_MOD + name, shader, fallback, pipeline -> setup.apply(RenderSetup.builder(pipeline)).createRenderSetup());
	}

	private static final Function<Identifier, RenderType> HALO_LAYERS = Util.memoize(texture -> make("crafting_halo",
			RenderSetup.builder(CoreShaders.TEXTURED_COLOR_NO_CULL).withTexture("Sampler0", safeTexture(texture)).sortOnUpload().createRenderSetup()));

	public static RenderType getHaloLayer(Identifier texture) {
		return HALO_LAYERS.apply(texture);
	}

	private static final Function<Identifier, RenderType> DOPPLEGANGER = Util.memoize(texture -> makeShaded("doppleganger",
			BotaniaShaders.DOPPLEGANGER, RenderPipelines.ENTITY_TRANSLUCENT, b -> b
					.withTexture("Sampler0", safeTexture(texture))
					.useLightmap()
					.useOverlay()
					.affectsCrumbling()
					.sortOnUpload()));

	public static RenderType getDopplegangerLayer(Identifier texture) {
		return BotaniaShaders.resolve(DOPPLEGANGER.apply(texture));
	}

	public static TextureAtlasSprite blockSprite(Identifier id) {
		return Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_BLOCKS, id));
	}

	public static TextureAtlasSprite itemSprite(Identifier id) {
		return Minecraft.getInstance().getAtlasManager().get(new SpriteId(TextureAtlas.LOCATION_ITEMS, id));
	}

	public static MultiBufferSource buffers(SubmitNodeCollector collector) {
		return new DeferredBufferSource(collector);
	}

	public static void drawTexturedModalRect(GuiGraphicsExtractor gui, Identifier textureId, int x, int y, int u, int v, int width, int height) {
		gui.blit(RenderPipelines.GUI_TEXTURED, textureId, x, y, u, v, width, height, 256, 256);
	}

	public static void drawTexturedModalRect(GuiGraphicsExtractor gui, Identifier textureId, int x, int y, int u, int v, int width, int height, int color) {
		gui.blit(RenderPipelines.GUI_TEXTURED, textureId, x, y, u, v, width, height, 256, 256, color);
	}

	public static int opaque(int color) {
		return (color & 0xFF000000) == 0 ? color | 0xFF000000 : color;
	}

	public static void drawString(GuiGraphicsExtractor gui, Font font, @Nullable String text, int x, int y, int color) {
		gui.text(font, text, x, y, opaque(color));
	}

	public static void drawString(GuiGraphicsExtractor gui, Font font, @Nullable String text, int x, int y, int color, boolean shadow) {
		gui.text(font, text, x, y, opaque(color), shadow);
	}

	public static void drawString(GuiGraphicsExtractor gui, Font font, Component text, int x, int y, int color) {
		gui.text(font, text, x, y, opaque(color));
	}

	public static void drawString(GuiGraphicsExtractor gui, Font font, Component text, int x, int y, int color, boolean shadow) {
		gui.text(font, text, x, y, opaque(color), shadow);
	}

	public static void drawString(GuiGraphicsExtractor gui, Font font, FormattedCharSequence text, int x, int y, int color) {
		gui.text(font, text, x, y, opaque(color));
	}

	public static void drawString(GuiGraphicsExtractor gui, Font font, FormattedCharSequence text, int x, int y, int color, boolean shadow) {
		gui.text(font, text, x, y, opaque(color), shadow);
	}

	public static void drawCenteredString(GuiGraphicsExtractor gui, Font font, String text, int x, int y, int color) {
		gui.centeredText(font, text, x, y, opaque(color));
	}

	public static void drawCenteredString(GuiGraphicsExtractor gui, Font font, Component text, int x, int y, int color) {
		gui.centeredText(font, text, x, y, opaque(color));
	}

	public static void drawCenteredString(GuiGraphicsExtractor gui, Font font, FormattedCharSequence text, int x, int y, int color) {
		gui.centeredText(font, text, x, y, opaque(color));
	}

	public static void renderStar(PoseStack ms, SubmitNodeCollector collector, int color, float xScale, float yScale, float zScale, long seed) {
		renderStar(ms, buffers(collector), color, xScale, yScale, zScale, seed);
	}

	public static void renderStar(PoseStack ms, MultiBufferSource buffers, int color, float xScale, float yScale, float zScale, long seed) {
		VertexConsumer buffer = buffers.getBuffer(STAR);

		float ticks = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks;
		float semiPeriodTicks = 200;
		float f1 = Mth.abs(Mth.sin((float) Math.PI / semiPeriodTicks * ticks))
				* 0.9F + 0.1F;

		float f2 = f1 > 0.F ? (f1 - 0.7F) / 0.2F : 0;
		Random random = new Random(seed);

		ms.pushPose();
		ms.scale(xScale, yScale, zScale);

		for (int i = 0; i < (f1 + f1 * f1) / 2F * 90F + 30F; i++) {
			ms.mulPose(VecHelper.rotateX(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateY(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateZ(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateX(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateY(random.nextFloat() * 360F));
			ms.mulPose(VecHelper.rotateZ(random.nextFloat() * 360F + f1 * 90F));
			float f3 = random.nextFloat() * 20F + 5F + f2 * 10F;
			float f4 = random.nextFloat() * 2F + 1F + f2 * 2F;
			float r = ((color & 0xFF0000) >> 16) / 255F;
			float g = ((color & 0xFF00) >> 8) / 255F;
			float b = (color & 0xFF) / 255F;
			Matrix4f mat = ms.last().pose();
			Runnable center = () -> buffer.addVertex(mat, 0, 0, 0).setColor(r, g, b, f1);
			Runnable[] vertices = {
					() -> buffer.addVertex(mat, -0.866F * f4, f3, -0.5F * f4).setColor(0, 0, 0, 0),
					() -> buffer.addVertex(mat, 0.866F * f4, f3, -0.5F * f4).setColor(0, 0, 0, 0),
					() -> buffer.addVertex(mat, 0, f3, 1F * f4).setColor(0, 0, 0, 0),
					() -> buffer.addVertex(mat, -0.866F * f4, f3, -0.5F * f4).setColor(0, 0, 0, 0)
			};
			triangleFan(center, vertices);
		}

		ms.popPose();
	}

	public static void triangleFan(Runnable center, Runnable... vertices) {
		triangleFan(center, Arrays.asList(vertices));
	}

	public static void triangleFan(Runnable center, List<Runnable> vertices) {
		for (int i = 0; i < vertices.size() - 1; i++) {
			center.run();
			vertices.get(i).run();
			vertices.get(i + 1).run();
		}
	}

	public static void flatRectangle(VertexConsumer buffer, Matrix4f mat,
			float xMin, float xMax, float y, float zMin, float zMax,
			int r, int g, int b, int a) {
		buffer.addVertex(mat, xMax, y, zMin).setColor(r, g, b, a);
		buffer.addVertex(mat, xMin, y, zMin).setColor(r, g, b, a);
		buffer.addVertex(mat, xMin, y, zMax).setColor(r, g, b, a);
		buffer.addVertex(mat, xMax, y, zMax).setColor(r, g, b, a);
	}

	public static void guiGeometry(GuiGraphicsExtractor gui, RenderPipeline pipeline, int x0, int y0, int x1, int y1,
			BiConsumer<Matrix3x2fc, VertexConsumer> builder) {
		Matrix3x2f pose = new Matrix3x2f(gui.pose());
		ScreenRectangle scissor = gui.peekScissorStack();
		ScreenRectangle bounds = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds(pose);
		if (scissor != null) {
			bounds = scissor.intersection(bounds);
		}
		gui.submitGuiElementRenderState(new GuiGeometry(pipeline, TextureSetup.noTexture(), pose, builder, scissor, bounds));
	}

	public record GuiGeometry(RenderPipeline pipeline, TextureSetup textureSetup, Matrix3x2f pose,
			BiConsumer<Matrix3x2fc, VertexConsumer> builder,
			@Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements GuiElementRenderState {
		@Override
		public void buildVertices(VertexConsumer vertexConsumer) {
			builder.accept(pose, vertexConsumer);
		}
	}

	public static void renderProgressPie(GuiGraphicsExtractor gui, int x, int y, float progress, ItemStack stack) {
		gui.item(stack, x, y);

		int r = 10;
		int centerX = x + 8;
		int centerY = y + 8;
		int degs = (int) (360 * progress);
		float a = 0.5F + 0.2F * ((float) Math.cos((double) (ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks) / 10) * 0.5F + 0.5F);
		int alpha = (int) (a * 255) & 0xFF;
		int centerColor = alpha << 24 | 0x007F7F;
		int edgeColor = alpha << 24 | 0x00FF7F;

		gui.nextStratum();
		guiGeometry(gui, CoreShaders.GUI_TRIANGLES, centerX - r, centerY - r, centerX + r, centerY + r, (pose, buf) -> {
			for (int i = degs; i > 0; i--) {
				float rad0 = (i - 90) / 180F * (float) Math.PI;
				float rad1 = (i - 1 - 90) / 180F * (float) Math.PI;
				buf.addVertexWith2DPose(pose, centerX, centerY).setColor(centerColor);
				buf.addVertexWith2DPose(pose, centerX + Mth.cos(rad0) * r, centerY + Mth.sin(rad0) * r).setColor(edgeColor);
				buf.addVertexWith2DPose(pose, centerX + Mth.cos(rad1) * r, centerY + Mth.sin(rad1) * r).setColor(edgeColor);
			}
		});
	}

	public static void renderItem(ItemStack stack, ItemDisplayContext context, PoseStack ms, SubmitNodeCollector collector,
			int light, int overlay, @Nullable Level level, int seed) {
		if (stack.isEmpty()) {
			return;
		}
		ItemStackRenderState state = new ItemStackRenderState();
		Minecraft.getInstance().getItemModelResolver().updateForTopItem(state, stack, context, level, null, seed);
		state.submit(ms, collector, light, overlay, 0);
	}

	public static void renderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext context, PoseStack ms, SubmitNodeCollector collector,
			int light, int overlay) {
		if (stack.isEmpty()) {
			return;
		}
		ItemStackRenderState state = new ItemStackRenderState();
		Minecraft.getInstance().getItemModelResolver().updateForLiving(state, stack, context, entity);
		state.submit(ms, collector, light, overlay, 0);
	}

	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, PoseStack ms, SubmitNodeCollector collector, int light, int overlay, @Nullable BlockStateModelPart model) {
		ms.pushPose();
		if (model != null) {
			ms.translate(-0.5D, -0.5D, -0.5D);
			renderModelPart(model, color, ms, buffers(collector), light, overlay);
		} else {
			ItemStackRenderState state = new ItemStackRenderState();
			Minecraft.getInstance().getItemModelResolver().updateForLiving(state, stack, ItemDisplayContext.NONE, entity);
			state.submit(ms, new ColorOverrideSubmitNodeCollector(collector, color), light, overlay, 0);
		}
		ms.popPose();
	}

	public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, PoseStack ms, SubmitNodeCollector collector, int light, int overlay) {
		renderItemCustomColor(entity, stack, color, ms, collector, light, overlay, null);
	}

	public static List<BakedQuad> allQuads(BlockStateModelPart model) {
		List<BakedQuad> quads = new ArrayList<>();
		for (Direction direction : Direction.values()) {
			quads.addAll(model.getQuads(direction));
		}
		quads.addAll(model.getQuads(null));
		return quads;
	}

	public static void renderModelPart(BlockStateModelPart model, int color, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		if (model == null) {
			return;
		}
		renderQuads(ms.last(), buffers, allQuads(model), color, light, overlay);
	}

	public static void renderModelPart(BlockStateModelPart model, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		renderModelPart(model, 0xFFFFFFFF, ms, buffers, light, overlay);
	}

	public static void renderQuads(PoseStack.Pose pose, MultiBufferSource buffers, List<BakedQuad> quads, int color, int light, int overlay) {
		QuadInstance instance = new QuadInstance();
		instance.setColor(color);
		instance.setLightCoords(light);
		instance.setOverlayCoords(overlay);
		for (BakedQuad quad : quads) {
			buffers.getBuffer(quad.materialInfo().itemRenderType()).putBakedQuad(pose, quad, instance);
		}
	}

	public static void renderBlockState(BlockState state, PoseStack ms, MultiBufferSource buffers, @Nullable RenderType type, int color, int light, int overlay) {
		BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
		List<BlockStateModelPart> parts = new ArrayList<>();
		model.collectParts(RandomSource.create(42L), parts);
		renderBlockParts(state, parts, null, null, ms, buffers, type, color, light, overlay);
	}

	public static void renderBlockInLevel(@Nullable Level level, BlockState state, BlockPos pos, PoseStack ms, MultiBufferSource buffers, int overlay) {
		BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(state);
		List<BlockStateModelPart> parts = new ArrayList<>();
		RandomSource random = RandomSource.create(state.getSeed(pos));
		BlockAndTintGetter getter = level instanceof BlockAndTintGetter g ? g : null;
		if (getter != null) {
			model.collectParts(getter, pos, state, random, parts);
		} else {
			model.collectParts(random, parts);
		}
		int light = level != null ? LevelRenderer.getLightCoords(level, pos) : LightCoordsUtil.FULL_BRIGHT;
		renderBlockParts(state, parts, getter, pos, ms, buffers, null, 0xFFFFFFFF, light, overlay);
	}

	private static void renderBlockParts(BlockState state, List<BlockStateModelPart> parts, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos,
			PoseStack ms, MultiBufferSource buffers, @Nullable RenderType type, int color, int light, int overlay) {
		QuadInstance instance = new QuadInstance();
		instance.setLightCoords(light);
		instance.setOverlayCoords(overlay);
		for (BlockStateModelPart part : parts) {
			for (int i = 0; i <= Direction.values().length; i++) {
				List<BakedQuad> quads = part.getQuads(i == Direction.values().length ? null : Direction.values()[i]);
				for (BakedQuad quad : quads) {
					int quadColor = color;
					if (quad.materialInfo().isTinted()) {
						BlockTintSource tint = Minecraft.getInstance().getBlockColors().getTintSource(state, quad.materialInfo().tintIndex());
						if (tint != null) {
							int tintColor = level != null && pos != null ? tint.colorInWorld(state, level, pos) : tint.color(state);
							quadColor = ARGB.multiply(quadColor, ARGB.opaque(tintColor));
						}
					}
					instance.setColor(quadColor);
					VertexConsumer buffer = buffers.getBuffer(type != null ? type : quad.materialInfo().itemRenderType());
					buffer.putBakedQuad(ms.last(), quad, instance);
				}
			}
		}
	}

	public static void renderItemLikeEntity(ItemStack stack, float age, float bobOffs, PoseStack ms, SubmitNodeCollector collector, int light, @Nullable Level level) {
		ms.pushPose();
		float bob = Mth.sin(age / 10.0F + bobOffs) * 0.1F + 0.1F;
		ms.translate(0F, bob + 0.25F, 0F);
		ms.mulPose(Axis.YP.rotation(age / 20.0F + bobOffs));
		renderItem(stack, ItemDisplayContext.GROUND, ms, collector, light, OverlayTexture.NO_OVERLAY, level, 0);
		ms.popPose();
	}

	public static void renderQuads(PoseStack.Pose pose, VertexConsumer buffer, List<BakedQuad> quads, int color, int light, int overlay) {
		QuadInstance instance = new QuadInstance();
		instance.setColor(color);
		instance.setLightCoords(light);
		instance.setOverlayCoords(overlay);
		for (BakedQuad quad : quads) {
			buffer.putBakedQuad(pose, quad, instance);
		}
	}

	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			float startX, float startY, float endX, float endY,
			int uvStartX, int uvStartY, int uvEndX, int uvEndY,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		Matrix4f mat = ms.last().pose();
		float red = ((color >> 16) & 0xFF) / 255F;
		float green = ((color >> 8) & 0xFF) / 255F;
		float blue = (color & 0xFF) / 255F;

		buffer.addVertex(mat, startX, endY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvStartX / 16F), icon.getV(uvEndY / 16F)).setLight(light);
		buffer.addVertex(mat, endX, endY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvEndX / 16F), icon.getV(uvEndY / 16F)).setLight(light);
		buffer.addVertex(mat, endX, startY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvEndX / 16F), icon.getV(uvStartY / 16F)).setLight(light);
		buffer.addVertex(mat, startX, startY, 0).setColor(red, green, blue, alpha).setUv(icon.getU(uvStartX / 16F), icon.getV(uvStartY / 16F)).setLight(light);
	}

	public static void renderIconCropped(
			PoseStack ms, VertexConsumer buffer,
			int uvStartX, int uvStartY, int uvEndX, int uvEndY,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		renderIconFullBright(
				ms, buffer,
				uvStartX / 16F, uvStartY / 16F, uvEndX / 16F, uvEndY / 16F,
				uvStartX, uvStartY, uvEndX, uvEndY,
				icon, color, alpha, light
		);
	}

	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			TextureAtlasSprite icon, int color, float alpha, int light) {
		renderIconCropped(
				ms, buffer,
				0, 0, 16, 16,
				icon, color, alpha, light
		);
	}

	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			TextureAtlasSprite icon, int color, float alpha) {
		int fullbright = 0xF000F0;
		renderIconFullBright(ms, buffer, icon, color, alpha, fullbright);
	}

	public static void renderIconFullBright(
			PoseStack ms, VertexConsumer buffer,
			TextureAtlasSprite icon, float alpha) {
		renderIconFullBright(ms, buffer, icon, 0xFFFFFF, alpha);
	}

	public static void renderGuiItemAlpha(GuiGraphicsExtractor gui, ItemStack stack, int x, int y, int alpha) {
		gui.item(stack, x, y);
	}

	public static void renderHUDBox(GuiGraphicsExtractor gui, int startX, int startY, int endX, int endY) {
		gui.fill(startX, startY, endX, endY, 0x40000000);
		gui.fill(startX - 2, startY - 2, endX + 2, endY + 2, 0x40000000);
	}

	public static void renderItemWithName(GuiGraphicsExtractor gui, Minecraft mc, ItemStack itemStack, int startX, int startY, int color) {
		if (!itemStack.isEmpty()) {
			gui.text(mc.font, itemStack.getHoverName(), startX + ITEM_AND_PADDING_WIDTH, startY + 4, opaque(color));
			gui.item(itemStack, startX, startY);
		}
	}

	public static void renderItemWithNameCentered(GuiGraphicsExtractor gui, Minecraft mc, ItemStack itemStack, int startY, int color) {
		int centerX = mc.getWindow().getGuiScaledWidth() / 2;
		int startX = centerX - (ITEM_AND_PADDING_WIDTH + mc.font.width(itemStack.getHoverName())) / 2;
		renderItemWithName(gui, mc, itemStack, startX, startY, color);
	}

	public static int itemWithNameWidth(Minecraft mc, ItemStack itemStack) {
		return ITEM_AND_PADDING_WIDTH + mc.font.width(itemStack.getHoverName());
	}

	public record GhostVertexConsumer(VertexConsumer wrapped, int alpha) implements VertexConsumer {
		@Override
		public VertexConsumer addVertex(float x, float y, float z) {
			wrapped.addVertex(x, y, z);
			return this;
		}

		@Override
		public VertexConsumer setColor(int red, int green, int blue, int alpha) {
			wrapped.setColor(red, green, blue, (alpha * this.alpha) / 0xFF);
			return this;
		}

		@Override
		public VertexConsumer setColor(int color) {
			int a = (color >>> 24) * this.alpha / 0xFF;
			wrapped.setColor((color & 0xFFFFFF) | a << 24);
			return this;
		}

		@Override
		public VertexConsumer setUv(float u, float v) {
			wrapped.setUv(u, v);
			return this;
		}

		@Override
		public VertexConsumer setUv1(int u, int v) {
			wrapped.setUv1(u, v);
			return this;
		}

		@Override
		public VertexConsumer setUv2(int u, int v) {
			wrapped.setUv2(u, v);
			return this;
		}

		@Override
		public VertexConsumer setNormal(float x, float y, float z) {
			wrapped.setNormal(x, y, z);
			return this;
		}

		@Override
		public VertexConsumer setLineWidth(float width) {
			wrapped.setLineWidth(width);
			return this;
		}
	}

	private RenderHelper() {}
}
