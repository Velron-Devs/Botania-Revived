package velrondevs.botania.common.item;

import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.InteractionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.advancements.UseItemSuccessTrigger;
import velrondevs.botania.common.entity.PinkWitherEntity;
import velrondevs.botania.registry.BotaniaEntities;
import velrondevs.botania.registry.BotaniaSounds;

import java.util.List;

public class PinkinatorItem extends Item {

	public PinkinatorItem(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public InteractionResult use(Level world, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		int range = 16;
		List<WitherBoss> withers = world.getEntitiesOfClass(WitherBoss.class, new AABB(player.getX() - range, player.getY() - range, player.getZ() - range, player.getX() + range, player.getY() + range, player.getZ() + range));
		for (WitherBoss wither : withers) {
			if (wither.isAlive() && !(wither instanceof PinkWitherEntity)) {
				if (!world.isClientSide()) {
					wither.discard();
					PinkWitherEntity pink = BotaniaEntities.PINK_WITHER.create(world, EntitySpawnReason.CONVERSION);
					pink.snapTo(wither.getX(), wither.getY(), wither.getZ(), wither.getYRot(), wither.getXRot());
					pink.setNoAi(wither.isNoAi());
					if (wither.hasCustomName()) {
						pink.setCustomName(wither.getCustomName());
						pink.setCustomNameVisible(wither.isCustomNameVisible());
					}
					pink.finalizeSpawn((ServerLevelAccessor) world, ((ServerLevelAccessor) world).getCurrentDifficultyAt(pink.blockPosition()), EntitySpawnReason.CONVERSION, null);
					world.addFreshEntity(pink);
					pink.spawnAnim();
					pink.playSound(BotaniaSounds.pinkinator, 1F, (1F + (world.getRandom().nextFloat() - world.getRandom().nextFloat()) * 0.2F) * 0.7F);
					UseItemSuccessTrigger.INSTANCE.trigger((ServerPlayer) player, stack, (ServerLevel) world, player.getX(), player.getY(), player.getZ());
					stack.shrink(1);
				}
				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext world, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag flags) {
		tooltip.accept(Component.translatable("botaniamisc.pinkinatorDesc").withStyle(ChatFormatting.GRAY));
	}

}
