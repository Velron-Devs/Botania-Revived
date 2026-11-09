package velrondevs.botania.common.item.equipment.tool.elementium;

import net.minecraft.server.level.ServerLevel;
import velrondevs.botania.common.lib.LibItemNames;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.item.equipment.tool.manasteel.ManasteelShearsItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;

public class ElementiumShearsItem extends ManasteelShearsItem {

	public ElementiumShearsItem(Properties props) {
		super(props, LibItemNames.ELEMENTIUM_INGOT, LibItemNames.MANASTEEL_INGOT);
	}

	@NotNull
	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@NotNull
	@Override
	public InteractionResult use(Level world, Player player, @NotNull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	@Override
	public void onUseTick(Level world, @NotNull LivingEntity living, @NotNull ItemStack stack, int count) {
		if (world.isClientSide()) {
			return;
		}

		if (count != getUseDuration(stack, living) && count % 5 == 0) {
			int range = 12;
			List<Entity> shearables = world.getEntitiesOfClass(Entity.class, new AABB(living.getX() - range, living.getY() - range, living.getZ() - range, living.getX() + range, living.getY() + range, living.getZ() + range), Shearable.class::isInstance);
			if (shearables.size() > 0) {
				for (Entity entity : shearables) {
					if (entity instanceof Shearable shearable && shearable.readyForShearing()) {
						shearable.shear((ServerLevel) world, living.getSoundSource(), stack);
						stack.hurtAndBreak(1, living, living.getUsedItemHand().asEquipmentSlot());
						break;
					}
				}
			}
		}
	}

	@Override
	public int getSortingPriority(ItemStack stack, BlockState state) {
		return super.getSortingPriority(stack, state) + 100;
	}
}
