package velrondevs.botania.xplat;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.ServiceUtil;
import velrondevs.botania.api.block.WandHUD;
import velrondevs.botania.network.BotaniaPacket;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public interface ClientXplatAbstractions {
	ResourceLocation FLOATING_FLOWER_MODEL_LOADER_ID = prefix("floating_flower");
	ResourceLocation MANA_GUN_MODEL_LOADER_ID = prefix("mana_gun");

	void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay);

	void sendToServer(BotaniaPacket packet);

	@Nullable
	WandHUD findWandHud(Level level, BlockPos pos, BlockState state, @Nullable BlockEntity be);

	@Nullable
	WandHUD findWandHud(Entity entity);

	BakedModel wrapPlatformModel(BakedModel original);
	void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap);
	void restoreLastFilter(AbstractTexture texture);
	void tessellateBlock(Level level, BlockState state, BlockPos pos, PoseStack ps, MultiBufferSource buffers, int overlay);

	void markSpriteActive(TextureAtlasSprite sprite);

	ClientXplatAbstractions INSTANCE = ServiceUtil.findService(ClientXplatAbstractions.class, null);

	static ClientXplatAbstractions instance() {
		return INSTANCE;
	}
}
