package velrondevs.botania.common.handler;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ServerLevelAccessor;

import velrondevs.botania.common.entity.PixieEntity;
import velrondevs.botania.common.helper.PlayerHelper;
import velrondevs.botania.common.item.equipment.armor.elementium.ElementiumHelmItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public final class PixieHandler {

	private PixieHandler() {}

	public static final Holder<Attribute> PIXIE_SPAWN_CHANCE = DeferredHolder.create(Registries.ATTRIBUTE, prefix("pixie_spawn_chance"));

	private static final List<Supplier<MobEffectInstance>> effectSuppliers = List.of(
			() -> new MobEffectInstance(MobEffects.BLINDNESS, 40, 0),
			() -> new MobEffectInstance(MobEffects.WITHER, 50, 0),
			() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0),
			() -> new MobEffectInstance(MobEffects.WEAKNESS, 40, 0)
	);

	public static void registerAttribute(BiConsumer<Attribute, ResourceLocation> r) {
		r.accept(new RangedAttribute("attribute.name.botania.pixieSpawnChance", 0, 0, 1), prefix("pixie_spawn_chance"));
	}

	public static AttributeModifier makeModifier(EquipmentSlot slot, String name, double amount) {
		return new AttributeModifier(prefix("pixie_spawn_chance/" + slot.getName()), amount, AttributeModifier.Operation.ADD_VALUE);
	}

	public static void onDamageTaken(Player player, DamageSource source) {
		if (!player.level().isClientSide && source.getEntity() instanceof LivingEntity livingSource) {

			double chance = player.getAttributes().hasAttribute(PIXIE_SPAWN_CHANCE)
					? player.getAttributeValue(PIXIE_SPAWN_CHANCE) : 0;
			ItemStack sword = PlayerHelper.getFirstHeldItem(player, s -> s.is(BotaniaItems.elementiumSword));

			if (Math.random() < chance) {
				PixieEntity pixie = new PixieEntity(player.level());
				pixie.setPos(player.getX(), player.getY() + 2, player.getZ());

				if (((ElementiumHelmItem) BotaniaItems.elementiumHelm).hasArmorSet(player)) {
					pixie.setApplyPotionEffect(effectSuppliers.get(player.level().random.nextInt(effectSuppliers.size())).get());
				}

				float dmg = 4;
				if (!sword.isEmpty()) {
					dmg += 2;
				}

				pixie.setProps(livingSource, player, 0, dmg);
				pixie.finalizeSpawn((ServerLevelAccessor) player.level(), player.level().getCurrentDifficultyAt(pixie.blockPosition()),
						MobSpawnType.EVENT, null);
				player.level().addFreshEntity(pixie);
			}
		}
	}
}
