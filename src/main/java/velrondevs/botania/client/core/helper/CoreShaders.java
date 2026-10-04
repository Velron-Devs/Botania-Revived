package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import velrondevs.botania.network.TriConsumer;
import velrondevs.botania.xplat.BotaniaConfig;

import java.util.function.Consumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class CoreShaders {
	private static ShaderInstance starfieldShaderInstance;
	private static ShaderInstance doppleganger;
	private static ShaderInstance manaPool;
	private static ShaderInstance terraPlate;
	private static ShaderInstance enchanter;
	private static ShaderInstance pylon;
	private static ShaderInstance halo;
	private static ShaderInstance filmGrainParticle;
	private static ShaderInstance dopplegangerBar;

	public static void init(TriConsumer<ResourceLocation, VertexFormat, Consumer<ShaderInstance>> registrations) {
		registrations.accept(
				prefix("starfield"),
				DefaultVertexFormat.POSITION,
				inst -> starfieldShaderInstance = inst
		);
		registrations.accept(
				prefix("doppleganger"),
				DefaultVertexFormat.NEW_ENTITY,
				inst -> doppleganger = inst
		);
		registrations.accept(
				prefix("mana_pool"),
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				inst -> manaPool = inst
		);
		registrations.accept(
				prefix("terra_plate_rune"),
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				inst -> terraPlate = inst
		);
		registrations.accept(
				prefix("enchanter_rune"),
				DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
				inst -> enchanter = inst
		);
		registrations.accept(
				prefix("pylon"),
				DefaultVertexFormat.NEW_ENTITY,
				inst -> pylon = inst
		);
		registrations.accept(
				prefix("halo"),
				DefaultVertexFormat.POSITION_TEX_COLOR,
				inst -> halo = inst
		);
		registrations.accept(
				prefix("film_grain_particle"),
				DefaultVertexFormat.PARTICLE,
				inst -> filmGrainParticle = inst
		);
		registrations.accept(
				prefix("doppleganger_bar"),
				DefaultVertexFormat.POSITION_TEX,
				inst -> dopplegangerBar = inst
		);
	}

	public static ShaderInstance starfield() {

		return starfieldShaderInstance;
	}

	public static ShaderInstance doppleganger() {
		if (BotaniaConfig.client().useShaders()) {
			return doppleganger;
		} else {
			return GameRenderer.getRendertypeEntityTranslucentShader();
		}
	}

	public static ShaderInstance manaPool() {
		if (BotaniaConfig.client().useShaders()) {
			return manaPool;
		} else {
			return GameRenderer.getPositionColorTexLightmapShader();
		}
	}

	public static ShaderInstance terraPlate() {
		if (BotaniaConfig.client().useShaders()) {
			return terraPlate;
		} else {
			return GameRenderer.getPositionColorTexLightmapShader();
		}
	}

	public static ShaderInstance enchanter() {
		if (BotaniaConfig.client().useShaders()) {
			return enchanter;
		} else {
			return GameRenderer.getPositionColorTexLightmapShader();
		}
	}

	public static ShaderInstance pylon() {
		if (BotaniaConfig.client().useShaders()) {
			return pylon;
		} else {
			return GameRenderer.getRendertypeEntityTranslucentShader();
		}
	}

	public static ShaderInstance halo() {
		if (BotaniaConfig.client().useShaders()) {
			return halo;
		} else {
			return GameRenderer.getPositionTexColorShader();
		}
	}

	public static ShaderInstance filmGrainParticle() {
		if (BotaniaConfig.client().useShaders()) {
			return filmGrainParticle;
		} else {
			return GameRenderer.getParticleShader();
		}
	}

	public static ShaderInstance dopplegangerBar() {
		if (BotaniaConfig.client().useShaders()) {
			return dopplegangerBar;
		} else {
			return GameRenderer.getPositionTexShader();
		}
	}
}
