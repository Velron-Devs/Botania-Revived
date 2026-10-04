package velrondevs.botania.integration.curios;

import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

import org.jetbrains.annotations.NotNull;

import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.item.ResoluteIvyItem;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.registry.BotaniaSounds;

import java.util.function.Predicate;

public class CurioIntegration extends EquipmentHandler {
	public static void init() {
		NeoForge.EVENT_BUS.addListener(CurioIntegration::keepCurioDrops);
	}

	public static void keepCurioDrops(DropRulesEvent event) {
		event.addOverride(stack -> {
			if (ResoluteIvyItem.hasIvy(stack)) {
				ItemNBTHelper.removeEntry(stack, ResoluteIvyItem.TAG_KEEP);
				return true;
			}
			return false;
		}, DropRule.ALWAYS_KEEP);
	}

	@Override
	protected Container getAllWornItems(LivingEntity living) {
		return CuriosApi.getCuriosInventory(living)
				.map(ICuriosItemHandler::getEquippedCurios)
				.<Container>map(ItemHandlerContainer::new)
				.orElseGet(() -> new SimpleContainer(0));
	}

	@Override
	protected ItemStack findItem(Item item, LivingEntity living) {
		return CuriosApi.getCuriosInventory(living)
				.map(i -> i.findFirstCurio(item).map(SlotResult::stack).orElse(ItemStack.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	@Override
	protected ItemStack findItem(Predicate<ItemStack> pred, LivingEntity living) {
		return CuriosApi.getCuriosInventory(living)
				.map(i -> i.findFirstCurio(pred).map(SlotResult::stack).orElse(ItemStack.EMPTY))
				.orElse(ItemStack.EMPTY);
	}

	public static void registerCapabilities(RegisterCapabilitiesEvent event, Item... baubles) {
		event.registerItem(CuriosCapability.ITEM, (stack, ctx) -> new Wrapper(stack), baubles);
	}

	@Override
	public void onInit(Item item) {
		Proxy.INSTANCE.runOnClient(() -> () -> CuriosRendererRegistry.register(item, () -> Renderer.INSTANCE));
	}

	@Override
	public boolean isAccessory(ItemStack stack) {
		return super.isAccessory(stack) || stack.getCapability(CuriosCapability.ITEM) != null;
	}

	public static class Wrapper implements ICurio {
		private static final SoundInfo SOUND_INFO = new SoundInfo(BotaniaSounds.equipBauble, 0.1F, 1.3F);

		private final ItemStack stack;

		Wrapper(ItemStack stack) {
			this.stack = stack;
		}

		private BaubleItem getItem() {
			return (BaubleItem) stack.getItem();
		}

		@Override
		public ItemStack getStack() {
			return stack;
		}

		@Override
		public void curioTick(SlotContext slotContext) {
			getItem().onWornTick(stack, slotContext.entity());
		}

		@Override
		public void onEquip(SlotContext slotContext, ItemStack previousStack) {
			getItem().onEquipped(stack, slotContext.entity());
		}

		@Override
		public void onUnequip(SlotContext slotContext, ItemStack newStack) {
			getItem().onUnequipped(stack, slotContext.entity());
		}

		@Override
		public boolean canEquip(SlotContext slotContext) {
			return getItem().canEquip(stack, slotContext.entity());
		}

		@Override
		public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id) {
			return getItem().getEquippedAttributeModifiers(stack);
		}

		@Override
		public boolean canSync(SlotContext slotContext) {
			return true;
		}

		@NotNull
		@Override
		public SoundInfo getEquipSound(SlotContext slotContext) {
			return SOUND_INFO;
		}

		@Override
		public boolean canEquipFromUse(SlotContext slotContext) {
			return true;
		}
	}

	private record ItemHandlerContainer(IItemHandlerModifiable handler) implements Container {
		@Override
		public int getContainerSize() {
			return handler.getSlots();
		}

		@Override
		public boolean isEmpty() {
			for (int i = 0; i < handler.getSlots(); i++) {
				if (!handler.getStackInSlot(i).isEmpty()) {
					return false;
				}
			}
			return true;
		}

		@Override
		public ItemStack getItem(int slot) {
			return handler.getStackInSlot(slot);
		}

		@Override
		public ItemStack removeItem(int slot, int count) {
			ItemStack stack = handler.getStackInSlot(slot);
			return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
		}

		@Override
		public ItemStack removeItemNoUpdate(int slot) {
			ItemStack stack = handler.getStackInSlot(slot);
			handler.setStackInSlot(slot, ItemStack.EMPTY);
			return stack;
		}

		@Override
		public void setItem(int slot, ItemStack stack) {
			handler.setStackInSlot(slot, stack);
		}

		@Override
		public void setChanged() {}

		@Override
		public boolean stillValid(Player player) {
			return true;
		}

		@Override
		public void clearContent() {
			for (int i = 0; i < handler.getSlots(); i++) {
				handler.setStackInSlot(i, ItemStack.EMPTY);
			}
		}
	}

	private static class Renderer implements ICurioRenderer {
		private static final Renderer INSTANCE = new Renderer();

		@Override
		public <T extends LivingEntity, M extends EntityModel<T>>
				void render(ItemStack stack, SlotContext slotContext, PoseStack matrixStack,
						RenderLayerParent<T, M> renderLayerParent, MultiBufferSource renderTypeBuffer,
						int light, float limbSwing, float limbSwingAmount, float partialTicks,
						float ageInTicks, float netHeadYaw, float headPitch) {
			LivingEntity livingEntity = slotContext.entity();
			M contextModel = renderLayerParent.getModel();
			BaubleItem item = (BaubleItem) stack.getItem();

			if (!item.hasRender(stack, livingEntity)) {
				return;
			}

			if (!(contextModel instanceof HumanoidModel<?>)) {
				return;
			}

			matrixStack.pushPose();
			ItemStack cosmetic = item.getCosmeticItem(stack);
			if (!cosmetic.isEmpty()) {
				CuriosRendererRegistry.getRenderer(item)
						.ifPresent(curio -> curio.render(cosmetic, slotContext, matrixStack, renderLayerParent,
								renderTypeBuffer, light, limbSwing, limbSwingAmount,
								partialTicks, ageInTicks, netHeadYaw, headPitch));
			} else {
				var renderer = AccessoryRenderRegistry.get(stack);
				if (renderer != null) {
					renderer.doRender((HumanoidModel<?>) contextModel, stack, livingEntity, matrixStack,
							renderTypeBuffer, light, limbSwing, limbSwingAmount,
							partialTicks, ageInTicks, netHeadYaw, headPitch);
				}
			}
			matrixStack.popPose();
		}
	}

}
