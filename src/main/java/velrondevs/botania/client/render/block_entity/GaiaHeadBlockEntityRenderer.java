package velrondevs.botania.client.render.block_entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.blockentity.state.SkullBlockRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.monster.skeleton.WitherSkeleton;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.phys.Vec3;

import org.jspecify.annotations.Nullable;

import velrondevs.botania.client.core.helper.RenderHelper;

public class GaiaHeadBlockEntityRenderer extends SkullBlockRenderer {
	public GaiaHeadBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
		super(ctx);
	}

	public static SkullBlock.Type getViewType() {
		Entity view = Minecraft.getInstance().getCameraEntity();
		SkullBlock.Type type = SkullBlock.Types.PLAYER;

		if (view instanceof Skeleton) {
			type = SkullBlock.Types.SKELETON;
		} else if (view instanceof WitherSkeleton) {
			type = SkullBlock.Types.WITHER_SKELETON;
		} else if (view instanceof WitherBoss) {
			type = SkullBlock.Types.WITHER_SKELETON;
		} else if (view instanceof Zombie) {
			type = SkullBlock.Types.ZOMBIE;
		} else if (view instanceof Creeper) {
			type = SkullBlock.Types.CREEPER;
		} else if (view instanceof EnderDragon) {
			type = SkullBlock.Types.DRAGON;
		}
		return type;
	}

	public static RenderType getViewRenderType() {
		SkullBlock.Type type = getViewType();
		Identifier texture = null;
		if (type == SkullBlock.Types.PLAYER && Minecraft.getInstance().getCameraEntity() instanceof AbstractClientPlayer player) {
			texture = player.getSkin().body().texturePath();
		}
		if (texture == null) {
			texture = SkullBlockRenderer.SKIN_BY_TYPE.get(type);
		}
		return RenderHelper.getDopplegangerLayer(texture);
	}

	@Override
	public void extractRenderState(SkullBlockEntity blockEntity, SkullBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
		super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.skullType = getViewType();
		state.renderType = getViewRenderType();
	}
}
