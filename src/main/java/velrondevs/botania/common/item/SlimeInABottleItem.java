package velrondevs.botania.common.item;

import org.jetbrains.annotations.Nullable;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
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
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot eqSlot) {
		if (!world.isClientSide()) {
			boolean slime = NarslimmusBlockEntity.isSlimeChunk(world, entity.blockPosition());
			ItemNBTHelper.setBoolean(stack, TAG_ACTIVE, slime);
		}
	}
}
