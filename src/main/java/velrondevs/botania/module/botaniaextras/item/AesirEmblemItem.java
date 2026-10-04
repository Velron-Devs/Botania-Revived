package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec3;

import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.module.botaniaextras.ClericalColors;
import velrondevs.botania.module.botaniaextras.client.EmblemModels;

import java.util.List;

public class AesirEmblemItem extends BaubleItem {
	public static final int COST = 8;

	public AesirEmblemItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new PriestEmblemItem.Renderer(EmblemModels.id("aesir"))));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(Component.translatable("botaniamisc.creative").withStyle(ChatFormatting.GRAY));
		tooltip.add(Component.translatable("botaniamisc.botania_extras.lackOfFaith").withStyle(ChatFormatting.RED));
		super.appendHoverText(stack, context, tooltip, flags);
	}

	@Override
	public void onEquipped(ItemStack stack, LivingEntity entity) {
		super.onEquipped(stack, entity);
		if (!entity.level().isClientSide) {
			PriestEmblemItem.setDangerous(stack, false);
		}
	}

	@Override
	public void onUnequipped(ItemStack stack, LivingEntity entity) {
		PriestEmblemItem.punishUnequip(stack, entity);
	}

	@Override
	public void onWornTick(ItemStack stack, LivingEntity entity) {
		if (entity.tickCount % 10 != 0) {
			return;
		}
		if (!entity.level().isClientSide) {
			if (entity instanceof Player player) {
				PriestEmblemItem.setActive(stack, ManaItemHandler.instance().requestManaExact(stack, player, COST, true));
				PriestEmblemItem.setDangerous(stack, true);
			}
		} else if (PriestEmblemItem.isActive(stack)) {
			Vec3 shift = PriestEmblemItem.headOrientation(entity);
			int color = ClericalColors.get(entity, 0xFFFFFF);
			WispParticleData data = WispParticleData.wisp((float) Math.random() * 0.15F + 0.15F, ClericalColors.red(color), ClericalColors.green(color), ClericalColors.blue(color));
			entity.level().addParticle(data, entity.getX() + shift.x * 0.25, entity.getY() + shift.y * 0.25, entity.getZ() + shift.z * 0.25,
					shift.x * 0.025, shift.y * 0.025, shift.z * 0.025);
		}
	}
}
