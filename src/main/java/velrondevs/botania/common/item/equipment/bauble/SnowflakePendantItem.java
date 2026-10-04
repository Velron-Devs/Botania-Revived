package velrondevs.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.neoforged.neoforge.common.util.BlockSnapshot;
import net.neoforged.neoforge.event.EventHooks;

import velrondevs.botania.client.core.handler.MiscellaneousModels;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.annotations.SoftImplement;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.mixin.BiomeAccessor;

public class SnowflakePendantItem extends BaubleItem {

	public SnowflakePendantItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	private static void freezeWater(LivingEntity living, Level level, BlockPos pos, int enchantmentLevel) {
		if (living.onGround()) {
			BlockState frostedIce = Blocks.FROSTED_ICE.defaultBlockState();
			int radius = Math.min(16, 2 + enchantmentLevel);
			BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos();

			for (BlockPos targetPos : BlockPos.betweenClosed(pos.offset(-radius, -1, -radius), pos.offset(radius, -1, radius))) {
				if (targetPos.closerToCenterThan(living.position(), radius)) {
					abovePos.set(targetPos.getX(), targetPos.getY() + 1, targetPos.getZ());
					if (level.getBlockState(abovePos).isAir()) {
						BlockState targetState = level.getBlockState(targetPos);
						if (targetState == FrostedIceBlock.meltsInto()
								&& frostedIce.canSurvive(level, targetPos)
								&& level.isUnobstructed(frostedIce, targetPos, CollisionContext.empty())
								&& !EventHooks.onBlockPlace(living, BlockSnapshot.create(level.dimension(), level, targetPos), Direction.UP)) {
							level.setBlockAndUpdate(targetPos, frostedIce);
							level.scheduleTick(targetPos, Blocks.FROSTED_ICE, Mth.nextInt(living.getRandom(), 60, 120));
						}
					}
				}
			}
		}
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (!entity.level().isClientSide && !entity.isShiftKeyDown()) {
			boolean lastOnGround = entity.onGround();
			entity.setOnGround(true);
			freezeWater(entity, entity.level(), entity.blockPosition(), 8);
			entity.setOnGround(lastOnGround);

			int x;
			int y = Mth.floor(entity.getY());
			int z;
			BlockState blockstate = Blocks.SNOW.defaultBlockState();

			for (int l = 0; l < 4; ++l) {
				x = Mth.floor(entity.getX() + (double) ((float) (l % 2 * 2 - 1) * 0.25F));
				z = Mth.floor(entity.getZ() + (double) ((float) (l / 2 % 2 * 2 - 1) * 0.25F));
				BlockPos blockpos = new BlockPos(x, y, z);

				if (entity.level().isEmptyBlock(blockpos) && blockstate.canSurvive(entity.level(), blockpos)) {
					var biome = entity.level().getBiome(blockpos);
					if (((BiomeAccessor) (Object) biome.value()).callGetTemperature(blockpos) < 0.9F) {
						entity.level().setBlockAndUpdate(blockpos, blockstate);
					}
				}
			}
		} else if (entity.level().isClientSide && !entity.isShiftKeyDown()) {
			if (entity.level().random.nextFloat() >= 0.25F) {
				entity.level().addParticle(new BlockParticleOption(ParticleTypes.FALLING_DUST, Blocks.SNOW_BLOCK.defaultBlockState()), entity.getX() + entity.level().random.nextFloat() * 0.6 - 0.3, entity.getY() + 1.1, entity.getZ() + entity.level().random.nextFloat() * 0.6 - 0.3, 0, -0.15, 0);
			}
		}
	}

	@SoftImplement("IForgeItem")
	public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
		return true;
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			boolean armor = !living.getItemBySlot(EquipmentSlot.CHEST).isEmpty();
			bipedModel.body.translateAndRotate(ms);
			ms.translate(-0.25, 0.5, armor ? 0.05 : 0.12);
			ms.scale(0.5F, -0.5F, -0.5F);

			BakedModel model = MiscellaneousModels.INSTANCE.snowflakePendantGem;
			VertexConsumer buffer = buffers.getBuffer(Sheets.cutoutBlockSheet());
			Minecraft.getInstance().getBlockRenderer().getModelRenderer()
					.renderModel(ms.last(), buffer, null, model, 1, 1, 1, light, OverlayTexture.NO_OVERLAY);
		}
	}

}
