package velrondevs.botania.module.botaniaextras.item;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import velrondevs.botania.api.item.CosmeticBauble;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.module.botaniaextras.ClericalColor;
import velrondevs.botania.module.botaniaextras.IridescentColors;

import java.util.List;

public class CoatOfArmsItem extends BaubleItem implements CosmeticBauble, ClericalColor {
	public static final List<String> NAMES = List.of("chilean", "french", "japanese", "germanic", "greek", "icelandic",
			"irish", "israeli", "jamaican", "singaporean", "south_african", "spanish", "swiss", "texan", "ukrainian",
			"american", "fabulous", "parisian");
	public static final int FABULOUS = 16;
	public static final int PARISIAN = 17;

	private static final int[] COLORS = {
			0x00137F, 0x0043FF, 0xFF0037, 0xFFD800,
			0x002EFF, 0x001A8E, 0x009944, 0x003BFF,
			0x00FF3B, 0xFF003B, 0x603A20, 0xFFFF00,
			0xFF0015, 0x0048FF, 0xFFD400, 0xFFFFFF,
			0xFFFFFF, 0xFF0037
	};

	private final int index;

	public CoatOfArmsItem(int index, Properties props) {
		super(props);
		this.index = index;
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	public int index() {
		return index;
	}

	public static String itemName(int index) {
		return "coat_of_arms_" + NAMES.get(index);
	}

	@Override
	public int clericalColor(ItemStack stack) {
		if (index == FABULOUS) {
			return IridescentColors.rainbow(System.currentTimeMillis() / 50F);
		}
		if (index == PARISIAN) {
			return -1;
		}
		return COLORS[index];
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.cosmeticBauble").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY));
		super.appendHoverText(stack, context, tooltip, flags);
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.body.translateAndRotate(ms);
			ms.translate(0.12, 0.25, -0.14);
			ms.mulPose(VecHelper.rotateZ(10F));
			ms.scale(0.35F, -0.35F, -0.35F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
					light, OverlayTexture.NO_OVERLAY, ms, buffers, Minecraft.getInstance().level, 0);
		}
	}
}
