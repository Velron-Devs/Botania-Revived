package velrondevs.botania.common.item.relic;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import velrondevs.botania.api.item.Relic;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaItems;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class RingOfOdinItem extends RelicBaubleItem {

	public RingOfOdinItem(Properties props) {
		super(props);
	}

	@Override
	public void onValidPlayerWornTick(Player player) {
		if (player.isOnFire()) {
			player.clearFire();
		}
	}

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getEquippedAttributeModifiers(ItemStack stack) {
		Multimap<Holder<Attribute>, AttributeModifier> attributes = HashMultimap.create();
		attributes.put(Attributes.MAX_HEALTH,
				new AttributeModifier(getBaubleModifierId(stack), 20, AttributeModifier.Operation.ADD_VALUE));
		return attributes;
	}

	public static boolean onPlayerAttacked(Player player, DamageSource src) {
		return (src.is(BotaniaTags.DamageTypes.RING_OF_ODIN_IMMUNE))
				&& !EquipmentHandler.findOrEmpty(BotaniaItems.odinRing, player).isEmpty();
	}

	public static Relic makeRelic(ItemStack stack) {
		return new RelicImpl(stack, prefix("challenge/odin_ring"));
	}

}
