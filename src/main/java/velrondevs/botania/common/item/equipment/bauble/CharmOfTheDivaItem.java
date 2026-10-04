package velrondevs.botania.common.item.equipment.bauble;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TickTask;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.block.flower.functional.HeiseiDreamBlockEntity;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.mixin.CreeperAccessor;
import velrondevs.botania.mixin.EntityAccessor;
import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;
import velrondevs.botania.registry.BotaniaItems;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.function.Predicate;

public class CharmOfTheDivaItem extends BaubleItem {
	public static final int MANA_COST = 250;
	public static final int CHARM_RANGE = 20;

	public CharmOfTheDivaItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	private static Predicate<Mob> getCharmTargetPredicate(Player player, Mob mobToCharm) {
		return mob -> mob != mobToCharm && mob.isAlive() && mob.canBeSeenAsEnemy() && !mob.isPassengerOfSameVehicle(mobToCharm)
				&& (!(mob instanceof TamableAnimal tamable) || !tamable.isOwnedBy(player))
				&& (mob instanceof Enemy || mob instanceof NeutralMob neutralMob && (neutralMob.isAngryAt(player)
						|| mob.getTarget() instanceof TamableAnimal targetTamable && targetTamable.isOwnedBy(player)));
	}

	private static void charmMobs(ItemStack amulet, Player player, Mob target) {
		if (!ManaItemHandler.instance().requestManaExact(amulet, player, MANA_COST, false)) {
			return;
		}
		if (target.isAlive()
				&& (target instanceof Enemy || target instanceof NeutralMob)

				&& (!(target instanceof TamableAnimal tamable) || !tamable.isOwnedBy(player))

				&& player.position().closerThan(target.position(), CHARM_RANGE)) {
			List<Mob> potentialTargets = player.level().getEntitiesOfClass(Mob.class,
					AABB.ofSize(target.position(), 2 * CHARM_RANGE, 2 * CHARM_RANGE, 2 * CHARM_RANGE),
					getCharmTargetPredicate(player, target));
			if (!potentialTargets.isEmpty() && HeiseiDreamBlockEntity.brainwashEntity(target, potentialTargets)) {
				target.heal(target.getMaxHealth());
				((EntityAccessor) target).callUnsetRemoved();
				if (target instanceof Creeper) {
					((CreeperAccessor) target).setCurrentFuseTime(2);
				}

				ManaItemHandler.instance().requestManaExact(amulet, player, MANA_COST, true);
				player.level().playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.divaCharm, SoundSource.PLAYERS, 1F, 1F);
				XplatAbstractions.INSTANCE.sendToTracking(target, new BotaniaEffectPacket(EffectType.DIVA_EFFECT, target.getX(), target.getY(), target.getZ(), target.getId()));
			}
		}
	}

	public static void onEntityDamaged(Player player, LivingEntity entity) {
		if (entity instanceof Mob target
				&& !target.level().isClientSide

				&& target.canUsePortal(false) && target.canChangeDimensions(target.level(), target.level())
				&& Math.random() < 0.6) {
			MinecraftServer server = player.level().getServer();
			ItemStack amulet = EquipmentHandler.findOrEmpty(BotaniaItems.divaCharm, player);

			if (server != null && !amulet.isEmpty()) {

				server.tell(new TickTask(0, () -> charmMobs(amulet, player, target)));
			}
		}
	}

	public static class Renderer implements AccessoryRenderer {
		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.head.translateAndRotate(ms);
			ms.translate(0.15, -0.42, -0.35);
			ms.scale(0.4F, -0.4F, -0.4F);
			Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.NONE,
					light, OverlayTexture.NO_OVERLAY, ms, buffers, living.level(), living.getId());
		}
	}
}
