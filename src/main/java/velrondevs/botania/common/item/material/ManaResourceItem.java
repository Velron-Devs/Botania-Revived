package velrondevs.botania.common.item.material;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.entity.GaiaGuardianEntity;
import velrondevs.botania.registry.BotaniaItems;

public class ManaResourceItem extends Item {
	public ManaResourceItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();

		if (stack.is(BotaniaItems.terrasteel) || stack.is(BotaniaItems.gaiaIngot)) {
			return GaiaGuardianEntity.spawn(ctx.getPlayer(), stack, ctx.getLevel(), ctx.getClickedPos(), stack.is(BotaniaItems.gaiaIngot))
					? InteractionResult.sidedSuccess(ctx.getLevel().isClientSide())
					: InteractionResult.FAIL;
		} else if (stack.is(BotaniaItems.livingroot)) {
			return Items.BONE_MEAL.useOn(ctx);
		}

		return super.useOn(ctx);
	}
}
