package velrondevs.botania.common.item.lens;

import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.phys.HitResult;

import velrondevs.botania.api.internal.ManaBurst;

import java.util.List;

public class CelebratoryLens extends Lens {

	@Override
	public boolean collideBurst(ManaBurst burst, HitResult pos, boolean isManaBlock, boolean shouldKill, ItemStack stack) {
		Projectile entity = burst.entity();
		if (pos.getType() == HitResult.Type.BLOCK) {
			if (!entity.level().isClientSide && !burst.isFake() && !isManaBlock) {
				ItemStack fireworkStack = generateFirework(burst.getColor());

				FireworkRocketEntity rocket = new FireworkRocketEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), fireworkStack);
				rocket.setOwner(entity.getOwner());
				entity.level().addFreshEntity(rocket);
			}
			return true;
		}

		return shouldKill;
	}

	private ItemStack generateFirework(int color) {
		ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);

		int type = 1;
		double rand = Math.random();
		if (rand > 0.25) {
			if (rand > 0.9) {
				type = 2;
			} else {
				type = 0;
			}
		}

		boolean flicker = false;
		boolean trail = false;
		if (Math.random() < 0.05) {
			if (Math.random() < 0.5) {
				flicker = true;
			} else {
				trail = true;
			}
		}

		FireworkExplosion explosion = new FireworkExplosion(FireworkExplosion.Shape.byId(type), IntList.of(color), IntList.of(), trail, flicker);
		int flight = (int) (Math.random() * 3 + 2);
		stack.set(DataComponents.FIREWORKS, new Fireworks(flight, List.of(explosion)));

		return stack;
	}

}
