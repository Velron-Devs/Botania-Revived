package velrondevs.botania.module.botaniaextras.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.module.botaniaextras.ClericalColors;
import velrondevs.botania.module.botaniaextras.client.EmblemModels;
import velrondevs.botania.registry.BotaniaDamageTypes;

import java.util.List;

public class PriestEmblemItem extends BaubleItem {
	public enum Type {
		THOR("thor"),
		SIF("sif"),
		NJORD("njord");

		private final String id;

		Type(String id) {
			this.id = id;
		}

		public String id() {
			return id;
		}
	}

	public static final int COST = 2;
	private static final String TAG_ACTIVE = "active";
	private static final String TAG_DANGEROUS = "dangerous";

	private final Type type;

	public PriestEmblemItem(Type type, Properties props) {
		super(props);
		this.type = type;
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer(EmblemModels.id(type.id()))));
	}

	public Type type() {
		return type;
	}

	public static boolean isActive(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ACTIVE, false);
	}

	public static void setActive(ItemStack stack, boolean active) {
		if (isActive(stack) != active) {
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, active);
		}
	}

	public static boolean isDangerous(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_DANGEROUS, false);
	}

	public static void setDangerous(ItemStack stack, boolean dangerous) {
		if (isDangerous(stack) != dangerous) {
			ItemNBTHelper.setBoolean(stack, TAG_DANGEROUS, dangerous);
		}
	}

	public static boolean hasEmblem(LivingEntity living, Type type) {
		return !EquipmentHandler.findOrEmpty(stack -> {
			if (stack.getItem() instanceof PriestEmblemItem emblem) {
				return emblem.type == type && isActive(stack);
			}
			return stack.getItem() instanceof AesirEmblemItem && isActive(stack);
		}, living).isEmpty();
	}

	public static void punishUnequip(ItemStack stack, LivingEntity entity) {
		boolean exempt = entity instanceof Player player && player.isCreative();
		if (!entity.level().isClientSide && isDangerous(stack) && !exempt) {
			entity.hurt(BotaniaDamageTypes.Sources.lackOfFaith(entity.level().registryAccess()), 6F);
			entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 150, 0));
			entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 150, 2));
			entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2));
			entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 300, 2));
		}
		setDangerous(stack, false);
	}

	public static Vec3 headOrientation(LivingEntity entity) {
		float yaw = -entity.getYRot() * Mth.DEG_TO_RAD - Mth.PI;
		float pitch = -(entity.getXRot() - 90) * Mth.DEG_TO_RAD;
		float f1 = Mth.cos(yaw);
		float f2 = Mth.sin(yaw);
		float f3 = -Mth.cos(pitch);
		float f4 = Mth.sin(pitch);
		return new Vec3(f2 * f3, f4, f1 * f3);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.botania_extras.lackOfFaith").withStyle(ChatFormatting.RED));
		super.appendHoverText(stack, context, tooltip, flags);
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity entity) {
		super.onEquipped(stack, entity);
		if (!entity.level().isClientSide) {
			setDangerous(stack, false);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity entity) {
		punishUnequip(stack, entity);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity.tickCount % 10 != 0) {
			return;
		}
		if (!entity.level().isClientSide) {
			if (entity instanceof Player player) {
				setActive(stack, ManaItemHandler.instance().requestManaExact(stack, player, COST, true));
				setDangerous(stack, true);
			}
		} else if (isActive(stack)) {
			spawnEffects(entity);
		}
	}

	private void spawnEffects(LivingEntity entity) {
		switch (type) {
			case THOR -> {
				Vec3 head = VecHelper.fromEntityCenter(entity).add(0, 0.75, 0);
				Vec3 shift = head.add(headOrientation(entity));
				int color = ClericalColors.get(entity);
				Proxy.INSTANCE.lightningFX(entity.level(), head, shift, 2.0F,
						color == ClericalColors.NONE ? 0x0079C4 : color, color == ClericalColors.NONE ? 0xAADFFF : ClericalColors.brighter2(color));
			}
			case SIF -> {
				int color = ClericalColors.get(entity, 0x964B00);
				for (int i = 0; i < 7; i++) {
					float mx = (float) (Math.random() - 0.5) * 0.15F;
					float mz = (float) (Math.random() - 0.5) * 0.15F;
					WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, ClericalColors.red(color), ClericalColors.green(color), ClericalColors.blue(color));
					entity.level().addParticle(data, entity.getX(), entity.getY(), entity.getZ(), mx, 0.0075F, mz);
				}
			}
			case NJORD -> {
				int color = ClericalColors.get(entity, 0x0000FF);
				for (int i = 0; i < 7; i++) {
					Vec3 vec = headOrientation(entity).scale(0.52);
					SparkleParticleData data = SparkleParticleData.sparkle(1.0F, ClericalColors.red(color), ClericalColors.green(color), ClericalColors.blue(color), 5);
					entity.level().addParticle(data, entity.getX() + vec.x, entity.getY() + vec.y, entity.getZ() + vec.z, 0, 0, 0);
				}
			}
		}
	}

	public static class Renderer implements AccessoryRenderer {
		private final ResourceLocation modelId;

		public Renderer(ResourceLocation modelId) {
			this.modelId = modelId;
		}

		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			BakedModel model = EmblemModels.get(modelId);
			if (model == null) {
				return;
			}
			boolean armor = !living.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(-0.25, 0.5, armor ? 0.05 : 0.12);
			ms.scale(0.5F, -0.5F, -0.5F);
			VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer()
					.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
		}
	}
}
