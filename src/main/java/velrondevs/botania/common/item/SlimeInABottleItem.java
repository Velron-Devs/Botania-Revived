package velrondevs.botania.common.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import velrondevs.botania.common.block.flower.generating.NarslimmusBlockEntity;
import velrondevs.botania.common.helper.ItemNBTHelper;

public class SlimeInABottleItem extends Item {
	public static final String TAG_ACTIVE = "active";

	public SlimeInABottleItem(Properties builder) {
		super(builder);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level world, Entity entity, int something, boolean somethingelse) {
		if (!world.isClientSide) {
			boolean slime = NarslimmusBlockEntity.isSlimeChunk(world, entity.blockPosition());
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, slime);
		}
	}
}
