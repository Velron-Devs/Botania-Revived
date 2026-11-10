package velrondevs.botania.client.integration.ears;

import com.unascribed.ears.api.EarsFeatureType;
import com.unascribed.ears.api.OverrideResult;
import com.unascribed.ears.api.registry.EarsInhibitorRegistry;
import com.unascribed.ears.api.registry.EarsStateOverriderRegistry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.item.PhantomInkable;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.item.equipment.bauble.FlugelTiaraItem;
import velrondevs.botania.common.lib.LibMisc;

public class EarsIntegration {
	public static void register() {
		EarsStateOverriderRegistry.register(LibMisc.MOD_ID, (state, peer) -> {
			EquipmentSlot slot = switch (state) {
				case WEARING_HELMET -> EquipmentSlot.HEAD;
				case WEARING_CHESTPLATE -> EquipmentSlot.CHEST;
				case WEARING_LEGGINGS -> EquipmentSlot.LEGS;
				case WEARING_BOOTS -> EquipmentSlot.FEET;
				default -> null;
			};

			if (slot == null) {
				return OverrideResult.DEFAULT;
			}

			ItemStack stack = getEquipped(peer, slot);
			if (!(stack.getItem() instanceof PhantomInkable item)) {
				return OverrideResult.DEFAULT;
			}

			if (!item.hasPhantomInk(stack)) {
				return OverrideResult.DEFAULT;
			}

			return OverrideResult.FALSE;
		});

		EarsInhibitorRegistry.register(LibMisc.MOD_ID, (type, peer) -> {
			if (type != EarsFeatureType.WINGS) {
				return false;
			}

			Player player = getPlayer(peer);
			if (player == null) {
				return false;
			}

			Container equipment = EquipmentHandler.getAllWorn(player);

			for (int slot = 0; slot < equipment.getContainerSize(); slot++) {
				ItemStack item = equipment.getItem(slot);
				if (item.getItem() instanceof FlugelTiaraItem tiara &&
						tiara.hasRender(item, player) &&
						FlugelTiaraItem.getVariant(item) > 0) {
					return true;
				}
			}

			return false;
		});
	}

	private static ItemStack getEquipped(Object peer, EquipmentSlot slot) {
		if (peer instanceof HumanoidRenderState state) {
			return switch (slot) {
				case HEAD -> state.headEquipment;
				case CHEST -> state.chestEquipment;
				case LEGS -> state.legsEquipment;
				case FEET -> state.feetEquipment;
				default -> ItemStack.EMPTY;
			};
		}
		if (peer instanceof Player player) {
			return player.getItemBySlot(slot);
		}
		return ItemStack.EMPTY;
	}

	@Nullable
	private static Player getPlayer(Object peer) {
		if (peer instanceof Player player) {
			return player;
		}
		if (peer instanceof AvatarRenderState state) {
			ClientLevel level = Minecraft.getInstance().level;
			if (level != null && level.getEntity(state.id) instanceof Player player) {
				return player;
			}
		}
		return null;
	}
}
