package velrondevs.botania.module.botaniaextras.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.item.BlockProvider;
import velrondevs.botania.client.render.AccessoryRenderRegistry;
import velrondevs.botania.client.render.AccessoryRenderer;
import velrondevs.botania.common.handler.EquipmentHandler;
import velrondevs.botania.common.helper.BlockProviderHelper;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class ToolbeltItem extends BaubleItem {
	public static final int SEGMENTS = 12;
	public static final TagKey<Item> BLACKLIST = TagKey.create(Registries.ITEM, prefix("toolbelt_blacklisted"));
	public static final ResourceLocation BELT_TEXTURE = prefix("textures/model/botania_extras/toolbelt.png");

	public ToolbeltItem(Properties props) {
		super(props);
		Proxy.INSTANCE.runOnClient(() -> () -> AccessoryRenderRegistry.register(this, new Renderer()));
	}

	public static NonNullList<ItemStack> contents(ItemStack belt) {
		NonNullList<ItemStack> list = NonNullList.withSize(SEGMENTS, ItemStack.EMPTY);
		belt.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(list);
		return list;
	}

	public static void setContents(ItemStack belt, NonNullList<ItemStack> list) {
		boolean empty = true;
		for (ItemStack stack : list) {
			if (!stack.isEmpty()) {
				empty = false;
				break;
			}
		}
		if (empty) {
			belt.remove(DataComponents.CONTAINER);
		} else {
			belt.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(list));
		}
	}

	public static ItemStack getItem(ItemStack belt, int segment) {
		if (segment < 0 || segment >= SEGMENTS) {
			return ItemStack.EMPTY;
		}
		return contents(belt).get(segment);
	}

	public static void setItem(ItemStack belt, int segment, ItemStack stack) {
		if (segment < 0 || segment >= SEGMENTS) {
			return;
		}
		NonNullList<ItemStack> list = contents(belt);
		list.set(segment, stack);
		setContents(belt, list);
	}

	public static boolean allowed(ItemStack stack) {
		return !stack.isEmpty() && !stack.is(BLACKLIST) && !(stack.getItem() instanceof ToolbeltItem);
	}

	public static ItemStack findBelt(Player player) {
		return EquipmentHandler.findOrEmpty(stack -> stack.getItem() instanceof ToolbeltItem, player);
	}

	public static void interact(ServerPlayer player, int segment) {
		if (segment < 0 || segment >= SEGMENTS || player.isSpectator() || !player.isShiftKeyDown()) {
			return;
		}
		ItemStack belt = findBelt(player);
		if (belt.isEmpty()) {
			return;
		}
		ItemStack inSlot = getItem(belt, segment);
		ItemStack held = player.getMainHandItem();
		if (inSlot.isEmpty()) {
			if (allowed(held)) {
				setItem(belt, segment, held.copy());
				player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
				player.getInventory().setChanged();
			}
		} else {
			setItem(belt, segment, ItemStack.EMPTY);
			player.getInventory().placeItemBackInInventory(inSlot);
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		Map<String, Integer> counts = new TreeMap<>();
		Map<String, Component> names = new TreeMap<>();
		for (ItemStack content : contents(stack)) {
			if (!content.isEmpty()) {
				String key = content.getHoverName().getString();
				counts.merge(key, content.getCount(), Integer::sum);
				names.putIfAbsent(key, content.getHoverName());
			}
		}
		if (counts.isEmpty()) {
			tooltip.add(Component.translatable("botaniamisc.botania_extras.containsNothing").withStyle(ChatFormatting.AQUA));
		} else {
			tooltip.add(Component.translatable("botaniamisc.botania_extras.contains").withStyle(ChatFormatting.AQUA));
			counts.forEach((key, count) -> tooltip.add(Component.literal(count + "x ").append(names.get(key).copy().withStyle(ChatFormatting.WHITE))));
		}
		super.appendHoverText(stack, context, tooltip, flags);
	}

	public static class BlockProviderImpl implements BlockProvider {
		private final ItemStack belt;

		public BlockProviderImpl(ItemStack belt) {
			this.belt = belt;
		}

		@Nullable
		private static BlockProvider providerOf(ItemStack slotStack) {
			BlockProvider provider = XplatAbstractions.INSTANCE.findBlockProvider(slotStack);
			if (provider == null && slotStack.getItem() instanceof BlockItem) {
				provider = BlockProviderHelper.asBlockProvider(slotStack);
			}
			return provider;
		}

		@Override
		public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
			NonNullList<ItemStack> list = contents(belt);
			for (int i = 0; i < SEGMENTS; i++) {
				ItemStack slotStack = list.get(i);
				if (slotStack.isEmpty()) {
					continue;
				}
				BlockProvider provider = providerOf(slotStack);
				if (provider != null && provider.provideBlock(player, requestor, block, doit)) {
					if (doit) {
						setContents(belt, list);
					}
					return true;
				}
			}
			return false;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requestor, Block block) {
			int total = 0;
			for (ItemStack slotStack : contents(belt)) {
				if (slotStack.isEmpty()) {
					continue;
				}
				BlockProvider provider = providerOf(slotStack);
				if (provider != null) {
					int count = provider.getBlockCount(player, requestor, block);
					if (count == -1) {
						return -1;
					}
					total += count;
				}
			}
			return total;
		}
	}

	public static class Renderer implements AccessoryRenderer {
		private static ModelPart part;

		private static ModelPart part() {
			if (part == null) {
				MeshDefinition mesh = new MeshDefinition();
				mesh.getRoot().addOrReplaceChild("belt", CubeListBuilder.create().texOffs(16, 16).addBox(-4F, 0F, -2F, 8F, 12F, 4F, new CubeDeformation(0.3F)), PartPose.ZERO);
				part = LayerDefinition.create(mesh, 64, 32).bakeRoot().getChild("belt");
			}
			return part;
		}

		@Override
		public void doRender(HumanoidModel<?> bipedModel, ItemStack stack, LivingEntity living, PoseStack ms, MultiBufferSource buffers, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
			bipedModel.body.translateAndRotate(ms);
			VertexConsumer buffer = buffers.getBuffer(RenderType.entityCutoutNoCull(BELT_TEXTURE));
			part().render(ms, buffer, light, OverlayTexture.NO_OVERLAY);
		}
	}
}
