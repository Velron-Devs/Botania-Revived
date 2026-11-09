package velrondevs.botania.common.item;

import java.util.Optional;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.*;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import velrondevs.botania.client.core.handler.ClientTickHandler;
import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.gui.crafting.AssemblyHaloContainer;
import velrondevs.botania.client.lib.ResourcesLib;
import velrondevs.botania.common.annotations.SoftImplement;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.helper.PlayerHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.network.EffectType;
import velrondevs.botania.network.clientbound.BotaniaEffectPacket;
import velrondevs.botania.xplat.XplatAbstractions;

public class AssemblyHaloItem extends Item {

	private static final Identifier glowTexture = Identifier.parse(ResourcesLib.MISC_GLOW_GREEN);
	private static final com.google.common.base.Supplier<ItemStack> craftingTableSupplier = com.google.common.base.Suppliers.memoize(() -> new ItemStack(Blocks.CRAFTING_TABLE));

	public static final int SEGMENTS = 12;

	private static final String TAG_LAST_CRAFTING = "lastCrafting";
	private static final String TAG_LAST_CRAFTING_DISPLAY = "lastCraftingDisplay";
	private static final String TAG_STORED_RECIPE_PREFIX = "storedRecipe";
	private static final String TAG_STORED_DISPLAY_PREFIX = "storedRecipeDisplay";
	private static final String TAG_EQUIPPED = "equipped";
	private static final String TAG_ROTATION_BASE = "rotationBase";
	private static final String TAG_DISPLAY_RESULT = "result";
	private static final String TAG_DISPLAY_WIDTH = "width";
	private static final String TAG_DISPLAY_INGREDIENTS = "ingredients";

	public AssemblyHaloItem(Properties props) {
		super(props);
	}

	@NotNull
	@Override
	public InteractionResult use(Level world, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (world instanceof ServerLevel serverLevel) {
			int segment = getSegmentLookedAt(stack, player);
			RecipeHolder<CraftingRecipe> recipe = getSavedRecipe(serverLevel, stack, segment);

			if (segment == 0) {

				ContainerLevelAccess wp = ContainerLevelAccess.create(world, BlockPos.ZERO);
				player.openMenu(new SimpleMenuProvider(
						(windowId, playerInv, p) -> new AssemblyHaloContainer(windowId, playerInv, wp),
						stack.getHoverName()));
			} else {
				if (recipe == null) {
					RecipeHolder<CraftingRecipe> lastRecipe = getLastRecipe(serverLevel, stack);
					if (lastRecipe != null) {
						saveRecipe(serverLevel, stack, lastRecipe, segment);
					}
				} else {
					tryCraft(player, stack, segment, true);
				}
			}
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel world, Entity entity, @Nullable EquipmentSlot eqSlot) {
		boolean equipped = eqSlot == EquipmentSlot.MAINHAND;
		if (!(entity instanceof LivingEntity living)) {
			return;
		}

		boolean eqLastTick = wasEquipped(stack);

		if (!equipped && living.getOffhandItem() == stack) {
			equipped = true;
		}

		if (eqLastTick != equipped) {
			setEquipped(stack, equipped);
		}

		if (!equipped) {
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = segAngles / 2.0F;
			setRotationBase(stack, getCheckingAngle((LivingEntity) entity) - shift);
		}
	}

	private static boolean hasRoomFor(Inventory inv, ItemStack stack) {
		Inventory dummy = new Inventory(inv.player, new EntityEquipment());
		NonNullList<ItemStack> items = inv.getNonEquipmentItems();
		for (int i = 0; i < items.size(); i++) {
			dummy.getNonEquipmentItems().set(i, items.get(i).copy());
		}

		return dummy.add(stack.copy());
	}

	private static boolean canCraftHeuristic(Player player, CompoundTag display) {
		Map<Item, Integer> counts = new HashMap<>();
		for (ItemStack invStack : player.getInventory().getNonEquipmentItems()) {
			if (!invStack.isEmpty()) {
				counts.merge(invStack.getItem(), invStack.getCount(), Integer::sum);
			}
		}

		for (List<Item> options : getDisplayIngredients(display)) {
			if (options.isEmpty()) {
				continue;
			}
			boolean found = false;
			for (Item option : options) {
				int count = counts.getOrDefault(option, 0);
				if (count > 0) {
					counts.put(option, count - 1);
					found = true;
					break;
				}
			}
			if (!found) {
				return false;
			}
		}
		return true;
	}

	void tryCraft(Player player, ItemStack halo, int slot, boolean particles) {
		if (!(player.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		RecipeHolder<CraftingRecipe> recipe = getSavedRecipe(serverLevel, halo, slot);
		if (recipe == null) {
			return;
		}

		CraftingMenu dummy = new CraftingMenu(-999, player.getInventory());
		CraftingContainer craftInv = (CraftingContainer) dummy.getSlot(1).container;

		RecipeBookMenu.PostPlaceAction placed = dummy.handlePlacement(false, false, recipe, serverLevel, player.getInventory());
		if (placed != RecipeBookMenu.PostPlaceAction.NOTHING) {
			clearGrid(player, craftInv);
			return;
		}

		CraftingInput input = craftInv.asCraftInput();
		if (!recipe.value().matches(input, serverLevel)) {
			clearGrid(player, craftInv);
			return;
		}

		ItemStack result = recipe.value().assemble(input);

		if (!hasRoomFor(player.getInventory(), result)) {
			clearGrid(player, craftInv);
			return;
		}

		player.getInventory().add(result);

		NonNullList<ItemStack> remainingItems = recipe.value().getRemainingItems(input);
		remainingItems.forEach(s -> player.getInventory().placeItemBackInInventory(s));
		craftInv.clearContent();

		if (particles) {
			XplatAbstractions.INSTANCE.sendToTracking(player, new BotaniaEffectPacket(EffectType.HALO_CRAFT,
					player.getX(), player.getY(), player.getZ(), player.getId()));
		}
	}

	private static void clearGrid(Player player, CraftingContainer craftInv) {
		for (int i = 0; i < craftInv.getContainerSize(); i++) {
			ItemStack stackAt = craftInv.removeItemNoUpdate(i);
			if (!stackAt.isEmpty()) {
				player.getInventory().placeItemBackInInventory(stackAt);
			}
		}
	}

	@SoftImplement("IForgeItem")
	public boolean onEntitySwing(ItemStack stack, LivingEntity living, InteractionHand hand) {
		int segment = getSegmentLookedAt(stack, living);
		if (segment == 0) {
			return false;
		}

		if (hasSavedRecipe(stack, segment) && living.isShiftKeyDown()) {
			if (living.level() instanceof ServerLevel serverLevel) {
				saveRecipe(serverLevel, stack, null, segment);
			}
			return true;
		}

		return false;
	}

	protected static int getSegmentLookedAt(ItemStack stack, LivingEntity living) {
		float yaw = getCheckingAngle(living, getRotationBase(stack));

		int angles = 360;
		int segAngles = angles / SEGMENTS;
		for (int seg = 0; seg < SEGMENTS; seg++) {
			float calcAngle = (float) seg * segAngles;
			if (yaw >= calcAngle && yaw < calcAngle + segAngles) {
				return seg;
			}
		}
		return -1;
	}

	private static float getCheckingAngle(LivingEntity living) {
		return getCheckingAngle(living, 0F);
	}

	private static float getCheckingAngle(LivingEntity living, float base) {
		float yaw = Mth.wrapDegrees(living.getYRot()) + 90F;
		int angles = 360;
		int segAngles = angles / SEGMENTS;
		float shift = segAngles / 2;

		if (yaw < 0) {
			yaw = 180F + (180F + yaw);
		}
		yaw -= 360F - base;
		float angle = 360F - yaw + shift;

		if (angle < 0) {
			angle = 360F + angle;
		}

		return angle;
	}

	private static boolean hasSavedRecipe(ItemStack halo, int position) {
		return position > 0 && position < SEGMENTS && !ItemNBTHelper.getString(halo, TAG_STORED_RECIPE_PREFIX + position, "").isEmpty();
	}

	@Nullable
	private static RecipeHolder<CraftingRecipe> getSavedRecipe(ServerLevel world, ItemStack halo, int position) {
		String savedId = ItemNBTHelper.getString(halo, TAG_STORED_RECIPE_PREFIX + position, "");
		Identifier id = savedId.isEmpty() ? null : Identifier.tryParse(savedId);

		if (position <= 0 || position >= SEGMENTS || id == null) {
			return null;
		} else {
			return getCraftingRecipe(world, id);
		}
	}

	private static void saveRecipe(ServerLevel world, ItemStack halo, @Nullable RecipeHolder<CraftingRecipe> recipe, int position) {
		if (recipe == null) {
			ItemNBTHelper.removeEntry(halo, TAG_STORED_RECIPE_PREFIX + position);
			ItemNBTHelper.removeEntry(halo, TAG_STORED_DISPLAY_PREFIX + position);
		} else {
			ItemNBTHelper.setString(halo, TAG_STORED_RECIPE_PREFIX + position, recipe.id().identifier().toString());
			ItemNBTHelper.setCompound(halo, TAG_STORED_DISPLAY_PREFIX + position, createDisplay(world, recipe));
		}
	}

	private static CompoundTag createDisplay(Level world, RecipeHolder<CraftingRecipe> recipe) {
		CompoundTag display = new CompoundTag();
		ItemStack result = recipe.value().display().stream()
				.map(d -> d.result().resolveForFirstStack(SlotDisplayContext.fromLevel(world)))
				.filter(s -> !s.isEmpty())
				.findFirst()
				.orElse(ItemStack.EMPTY);
		display.put(TAG_DISPLAY_RESULT, ItemStackSerialization.write(world.registryAccess(), result));

		List<Optional<Ingredient>> ingredients;
		int width;
		if (recipe.value() instanceof ShapedRecipe shaped) {
			ingredients = shaped.getIngredients();
			width = shaped.getWidth();
		} else {
			ingredients = recipe.value().placementInfo().ingredients().stream().map(Optional::of).toList();
			width = 3;
		}
		display.putInt(TAG_DISPLAY_WIDTH, width);

		ListTag ingredientList = new ListTag();
		for (Optional<Ingredient> ingredient : ingredients) {
			ListTag options = new ListTag();
			ingredient.ifPresent(ingr -> ingr.items()
					.map(holder -> BuiltInRegistries.ITEM.getKey(holder.value()).toString())
					.forEach(id -> options.add(StringTag.valueOf(id))));
			ingredientList.add(options);
		}
		display.put(TAG_DISPLAY_INGREDIENTS, ingredientList);
		return display;
	}

	@Nullable
	private static CompoundTag getSavedDisplay(ItemStack halo, int position) {
		if (position <= 0 || position >= SEGMENTS) {
			return null;
		}
		return getDisplay(halo, TAG_STORED_DISPLAY_PREFIX + position);
	}

	@Nullable
	private static CompoundTag getDisplay(ItemStack halo, String key) {
		if (!ItemNBTHelper.verifyExistance(halo, key)) {
			return null;
		}
		return ItemNBTHelper.getCompound(halo, key, true);
	}

	private static ItemStack getDisplayResult(CompoundTag display) {
		return ItemStackSerialization.read(display.getCompoundOrEmpty(TAG_DISPLAY_RESULT));
	}

	private static List<List<Item>> getDisplayIngredients(CompoundTag display) {
		List<List<Item>> ret = new ArrayList<>();
		for (Tag entry : display.getListOrEmpty(TAG_DISPLAY_INGREDIENTS)) {
			List<Item> options = new ArrayList<>();
			if (entry instanceof ListTag list) {
				for (Tag option : list) {
					option.asString()
							.map(Identifier::tryParse)
							.flatMap(BuiltInRegistries.ITEM::getOptional)
							.ifPresent(options::add);
				}
			}
			ret.add(options);
		}
		return ret;
	}

	private static ItemStack getDisplayItem(ItemStack stack, int position) {
		if (position == 0) {
			return craftingTableSupplier.get();
		} else if (position >= SEGMENTS) {
			return ItemStack.EMPTY;
		} else {
			CompoundTag display = getSavedDisplay(stack, position);
			return display != null ? getDisplayResult(display) : ItemStack.EMPTY;
		}
	}

	public static void onItemCrafted(Player player, Container inv) {
		AbstractContainerMenu container = player.containerMenu;

		if (!(container instanceof AssemblyHaloContainer) ||
				!(inv instanceof CraftingContainer cc) ||
				!(player.level() instanceof ServerLevel serverLevel)) {
			return;
		}

		serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, cc.asCraftInput(), serverLevel).ifPresent(recipe -> {
			for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
				ItemStack stack = player.getInventory().getItem(i);
				if (!stack.isEmpty() && stack.getItem() instanceof AssemblyHaloItem) {
					rememberLastRecipe(serverLevel, recipe, stack);
				}
			}
		});
	}

	private static void rememberLastRecipe(ServerLevel world, RecipeHolder<CraftingRecipe> recipe, ItemStack halo) {
		ItemNBTHelper.setString(halo, TAG_LAST_CRAFTING, recipe.id().identifier().toString());
		ItemNBTHelper.setCompound(halo, TAG_LAST_CRAFTING_DISPLAY, createDisplay(world, recipe));
	}

	@Nullable
	private static RecipeHolder<CraftingRecipe> getLastRecipe(ServerLevel world, ItemStack halo) {
		String savedId = ItemNBTHelper.getString(halo, TAG_LAST_CRAFTING, "");
		Identifier id = savedId.isEmpty() ? null : Identifier.tryParse(savedId);

		return getCraftingRecipe(world, id);
	}

	@Nullable
	@SuppressWarnings("unchecked")
	private static RecipeHolder<CraftingRecipe> getCraftingRecipe(ServerLevel world, @Nullable Identifier id) {
		if (id == null) {
			return null;
		}
		return world.recipeAccess().byKey(ResourceKey.create(Registries.RECIPE, id))
				.filter(holder -> holder.value().getType() == RecipeType.CRAFTING && holder.value() instanceof CraftingRecipe)
				.map(holder -> (RecipeHolder<CraftingRecipe>) holder)
				.orElse(null);
	}

	private static boolean wasEquipped(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false);
	}

	private static void setEquipped(ItemStack stack, boolean equipped) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, equipped);
	}

	private static float getRotationBase(ItemStack stack) {
		return ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0F);
	}

	private static void setRotationBase(ItemStack stack, float rotation) {
		ItemNBTHelper.setFloat(stack, TAG_ROTATION_BASE, rotation);
	}

	public Identifier getGlowResource(ItemStack stack) {
		return glowTexture;
	}

	public static class Rendering {
		public static void onRenderWorldLast(Vec3 camera, float partialTicks, PoseStack ms, MultiBufferSource bufferSource) {
			onRenderWorldLast(camera, partialTicks, ms, bufferSource, null);
		}

		public static void onRenderWorldLast(Vec3 camera, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, @Nullable SubmitNodeCollector collector) {
			Player player = Minecraft.getInstance().player;
			ItemStack stack = PlayerHelper.getFirstHeldItemClass(player, AssemblyHaloItem.class);
			if (stack.isEmpty()) {
				return;
			}

			double renderPosX = camera.x();
			double renderPosY = camera.y();
			double renderPosZ = camera.z();

			ms.pushPose();
			float alpha = ((float) Math.sin((ClientTickHandler.ticksInGame + partialTicks) * 0.2F) * 0.5F + 0.5F) * 0.4F + 0.3F;

			double posX = player.xo + (player.getX() - player.xo) * partialTicks;
			double posY = player.yo + (player.getY() - player.yo) * partialTicks + player.getEyeHeight();
			double posZ = player.zo + (player.getZ() - player.zo) * partialTicks;

			ms.translate(posX - renderPosX, posY - renderPosY, posZ - renderPosZ);

			float base = getRotationBase(stack);
			int angles = 360;
			int segAngles = angles / SEGMENTS;
			float shift = base - segAngles / 2.0F;

			float u = 1F;
			float v = 0.25F;

			float s = 3F;
			float m = 0.8F;
			float y = v * s * 2;
			float y0 = 0;

			int segmentLookedAt = getSegmentLookedAt(stack, player);
			AssemblyHaloItem item = (AssemblyHaloItem) stack.getItem();
			net.minecraft.client.renderer.rendertype.RenderType layer = RenderHelper.getHaloLayer(item.getGlowResource(stack));

			for (int seg = 0; seg < SEGMENTS; seg++) {
				boolean inside = false;
				float rotationAngle = (seg + 0.5F) * segAngles + shift;
				ms.pushPose();
				ms.mulPose(VecHelper.rotateY(rotationAngle));
				ms.translate(s * m, -0.75F, 0F);

				if (segmentLookedAt == seg) {
					inside = true;
				}

				ItemStack slotStack = getDisplayItem(stack, seg);
				if (!slotStack.isEmpty() && collector != null) {
					float scale = seg == 0 ? 0.9F : 0.8F;
					ms.scale(scale, scale, scale);
					ms.mulPose(VecHelper.rotateY(180F));
					ms.translate(seg == 0 ? 0.5F : 0F, seg == 0 ? -0.1F : 0.6F, 0F);

					ms.mulPose(VecHelper.rotateY(90.0F));
					RenderHelper.renderItem(slotStack, ItemDisplayContext.GUI, ms, collector, 0xF000F0, OverlayTexture.NO_OVERLAY, player.level(), player.getId());
				}
				ms.popPose();

				ms.pushPose();
				ms.mulPose(VecHelper.rotateX(180));
				float r = 1, g = 1, b = 1, a = alpha;
				if (inside) {
					a += 0.3F;
					y0 = -y;
				}

				if (seg % 2 == 0) {
					r = g = b = 0.6F;
				}

				VertexConsumer buffer = bufferSource.getBuffer(layer);
				for (int i = 0; i < segAngles; i++) {
					Matrix4f mat = ms.last().pose();
					float ang = i + seg * segAngles + shift;
					float xp = (float) Math.cos(ang * Math.PI / 180F) * s;
					float zp = (float) Math.sin(ang * Math.PI / 180F) * s;

					buffer.addVertex(mat, xp * m, y, zp * m).setColor(r, g, b, a).setUv(u, v);
					buffer.addVertex(mat, xp, y0, zp).setColor(r, g, b, a).setUv(u, 0);

					xp = (float) Math.cos((ang + 1) * Math.PI / 180F) * s;
					zp = (float) Math.sin((ang + 1) * Math.PI / 180F) * s;

					buffer.addVertex(mat, xp, y0, zp).setColor(r, g, b, a).setUv(0, 0);
					buffer.addVertex(mat, xp * m, y, zp * m).setColor(r, g, b, a).setUv(0, v);
				}
				y0 = 0;
				ms.popPose();
			}
			ms.popPose();
		}

		public static void renderHUD(GuiGraphicsExtractor gui, Player player, ItemStack stack) {
			Minecraft mc = Minecraft.getInstance();
			int slot = getSegmentLookedAt(stack, player);

			if (slot == 0) {
				String name = craftingTableSupplier.get().getHoverName().getString();
				int l = mc.font.width(name);
				int x = mc.getWindow().getGuiScaledWidth() / 2 - l / 2;
				int y = mc.getWindow().getGuiScaledHeight() / 2 - 65;

				gui.fill(x - 6, y - 6, x + l + 6, y + 37, 0x22000000);
				gui.fill(x - 4, y - 4, x + l + 4, y + 35, 0x22000000);
				gui.item(craftingTableSupplier.get(), mc.getWindow().getGuiScaledWidth() / 2 - 8, mc.getWindow().getGuiScaledHeight() / 2 - 52);

				gui.text(mc.font, name, x, y, 0xFFFFFFFF);
			} else {
				CompoundTag display = getSavedDisplay(stack, slot);
				Component label;
				boolean setRecipe = false;

				if (display == null) {
					label = Component.translatable("botaniamisc.unsetRecipe");
					display = getDisplay(stack, TAG_LAST_CRAFTING_DISPLAY);
				} else {
					label = getDisplayResult(display).getHoverName();
					setRecipe = true;
				}

				renderRecipe(gui, label, display, player, setRecipe);
			}
		}

		private static void renderRecipe(GuiGraphicsExtractor gui, Component label, @Nullable CompoundTag display, Player player, boolean isSavedRecipe) {
			Minecraft mc = Minecraft.getInstance();

			ItemStack recipeResult;
			if (display != null && !(recipeResult = getDisplayResult(display)).isEmpty()) {
				int x = mc.getWindow().getGuiScaledWidth() / 2 - 45;
				int y = mc.getWindow().getGuiScaledHeight() / 2 - 90;

				gui.fill(x - 6, y - 6, x + 90 + 6, y + 60, 0x22000000);
				gui.fill(x - 4, y - 4, x + 90 + 4, y + 58, 0x22000000);

				gui.fill(x + 66, y + 14, x + 92, y + 40, 0x22000000);
				gui.fill(x - 2, y - 2, x + 56, y + 56, 0x22000000);

				int wrap = Math.max(1, display.getIntOr(TAG_DISPLAY_WIDTH, 3));
				List<List<Item>> ingredients = getDisplayIngredients(display);
				for (int i = 0; i < ingredients.size(); i++) {
					List<Item> options = ingredients.get(i);
					if (!options.isEmpty()) {
						ItemStack stack = new ItemStack(options.get(ClientTickHandler.ticksInGame / 20 % options.size()));
						int xpos = x + i % wrap * 18;
						int ypos = y + i / wrap * 18;
						gui.fill(xpos, ypos, xpos + 16, ypos + 16, 0x22000000);

						gui.item(stack, xpos, ypos);
					}
				}

				gui.item(recipeResult, x + 72, y + 18);
				gui.itemDecorations(mc.font, recipeResult, x + 72, y + 18);

			}

			int yoff = 110;
			if (isSavedRecipe && display != null && !canCraftHeuristic(player, display)) {
				String warning = ChatFormatting.RED + I18n.get("botaniamisc.cantCraft");
				gui.centeredText(mc.font, warning, mc.getWindow().getGuiScaledWidth() / 2, mc.getWindow().getGuiScaledHeight() / 2 - yoff, 0xFFFFFFFF);
				yoff += 12;
			}

			gui.centeredText(mc.font, label, mc.getWindow().getGuiScaledWidth() / 2, mc.getWindow().getGuiScaledHeight() / 2 - yoff, 0xFFFFFFFF);
		}
	}
}
