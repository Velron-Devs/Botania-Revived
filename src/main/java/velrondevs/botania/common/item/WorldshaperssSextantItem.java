package velrondevs.botania.common.item;

import velrondevs.botania.client.core.helper.RenderHelper;
import velrondevs.botania.client.core.helper.CoreShaders;
import net.minecraft.world.InteractionResult;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.client.fx.WispParticleData;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.helper.MathHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.item.equipment.tool.ToolCommons;
import velrondevs.botania.common.proxy.Proxy;
import velrondevs.botania.registry.BotaniaSounds;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.IStateMatcher;
import vazkii.patchouli.api.PatchouliAPI;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class WorldshaperssSextantItem extends Item {
	public static final Identifier MULTIBLOCK_ID = prefix("sextant");
	private static final int MAX_RADIUS = 256;
	private static final String TAG_SOURCE_X = "sourceX";
	private static final String TAG_SOURCE_Y = "sourceY";
	private static final String TAG_SOURCE_Z = "sourceZ";
	private static final String TAG_MODE = "mode";

	public WorldshaperssSextantItem(Properties builder) {
		super(builder);
	}

	@NotNull
	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public void onUseTick(Level world, LivingEntity living, ItemStack stack, int count) {
		if (getUseDuration(stack, living) - count < 10
				|| !(living instanceof Player)
				|| !world.isClientSide()) {
			return;
		}

		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		if (y != Integer.MIN_VALUE) {
			double radius = calculateRadius(stack, living);
			WispParticleData data = WispParticleData.wisp(0.3F, 0F, 1F, 1F, 1);
			world.addParticle(data, x + 0.5, y + 1, z + 0.5, 0, 0.1, 0);
			var visualizer = getMode(stack).getVisualizer();
			for (int i = count % 20; i < 360; i += 20) {
				float radian = (float) (i * Math.PI / 180);
				double cosR = Math.cos(radian) * radius;
				double sinR = Math.sin(radian) * radius;
				visualizer.visualize(world, x, y, z, data, cosR, sinR);
			}
		}
	}

	private static void visualizeSphere(Level world, int x, int y, int z, WispParticleData data, double cosR, double sinR) {
		world.addParticle(data, x + cosR + 0.5, y + 1.3, z + sinR + 0.5, 0, 0.01, 0);
		world.addParticle(data, x + sinR + 0.5, y + cosR + 1.5, z + 0.3, 0, 0, 0.01);
		world.addParticle(data, x + 0.3, y + sinR + 1.5, z + cosR + 0.5, 0.01, 0, 0);
	}

	private static void visualizeCircle(Level world, int x, int y, int z, WispParticleData data, double cosR, double sinR) {
		world.addParticle(data, x + cosR + 0.5, y + 1, z + sinR + 0.5, 0, 0.01, 0);
	}

	private static void makeSphere(IStateMatcher matcher, double radius, Map<BlockPos, IStateMatcher> map) {

		final int maxR2 = (int) Math.floor(radius * radius);
		int zMax = (int) Math.floor(radius);
		for (int x = 0;; x++) {
			while (x * x + zMax * zMax > maxR2 && zMax >= x) {
				zMax--;
			}
			if (zMax < x) {
				break;
			}
			int z = zMax;
			for (int y = 0;; y++) {
				while (x * x + y * y + z * z > maxR2 && z >= x && z >= y) {
					z--;
				}
				if (z < x || z < y) {
					break;
				}

				generateMirroredPositions(x, y, z, map, matcher);
				generateMirroredPositions(y, z, x, map, matcher);
				generateMirroredPositions(z, x, y, map, matcher);
			}
		}
	}

	private static void generateMirroredPositions(int x, int y, int z, Map<BlockPos, IStateMatcher> map, IStateMatcher matcher) {
		Stream.of(
				new BlockPos(x, y, z), new BlockPos(-x, y, z),
				new BlockPos(x, -y, z), new BlockPos(-x, -y, z),
				new BlockPos(x, y, -z), new BlockPos(-x, y, -z),
				new BlockPos(x, -y, -z), new BlockPos(-x, -y, -z)
		).forEach(pos -> map.put(pos, matcher));
	}

	@Override
	public boolean releaseUsing(ItemStack stack, Level world, LivingEntity living, int time) {
		if (!(living instanceof Player)) {
			return false;
		}

		double radius = calculateRadius(stack, living);
		if (1 < radius && radius <= MAX_RADIUS) {
			IStateMatcher matcher = PatchouliAPI.get().predicateMatcher(Blocks.COBBLESTONE, s -> !s.isAir());
			int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
			int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
			int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
			if (y != Integer.MIN_VALUE) {
				Map<BlockPos, IStateMatcher> map = new HashMap<>();
				getMode(stack).getCreator().create(matcher, radius + 0.5, map);
				IMultiblock sparse = PatchouliAPI.get().makeSparseMultiblock(map).setId(MULTIBLOCK_ID);
				Proxy.INSTANCE.showMultiblock(sparse, Component.literal("r = " + getRadiusString(radius)),
						new BlockPos(x, y, z), Rotation.NONE);
			}
		}
		return true;
	}

	private static void makeCircle(IStateMatcher matcher, double radius, Map<BlockPos, IStateMatcher> map) {

		final int maxR2 = (int) Math.floor(radius * radius);
		int z = (int) Math.floor(radius);
		for (int x = 0;; x++) {
			while (x * x + z * z > maxR2 && z >= x) {
				z--;
			}
			if (z < x) {
				break;
			}
			generateMirroredPositions(x, z, map, matcher);
			generateMirroredPositions(z, x, map, matcher);
		}
	}

	private static void generateMirroredPositions(int x, int z, Map<BlockPos, IStateMatcher> map, IStateMatcher matcher) {
		Stream.of(
				new BlockPos(x, 0, z), new BlockPos(-x, 0, z),
				new BlockPos(x, 0, -z), new BlockPos(-x, 0, -z)
		).forEach(pos -> map.put(pos, matcher));
	}

	private static Modes getMode(ItemStack stack) {
		String modeString = ItemNBTHelper.getString(stack, TAG_MODE, "circle");
		return Arrays.stream(Modes.values()).filter(m -> m.getKey().equals(modeString)).findFirst().orElse(Modes.CIRCLE);
	}

	private void reset(Level world, Player player, ItemStack stack) {
		if (ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE) == Integer.MIN_VALUE) {
			if (!world.isClientSide()) {
				Modes currentMode = getMode(stack);
				int numModes = Modes.values().length;
				int nextMode = currentMode.ordinal() + 1;
				setMode(stack, Modes.values()[nextMode >= numModes ? 0 : nextMode]);
			} else {
				player.playSound(BotaniaSounds.ding, 0.1F, 1F);
			}
		} else {
			ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
		}
		if (world.isClientSide()) {
			Proxy.INSTANCE.clearSextantMultiblock();
		}
	}

	private static void setMode(ItemStack stack, Modes mode) {
		ItemNBTHelper.setString(stack, TAG_MODE, mode.getKey());
	}

	@NotNull
	@Override
	public InteractionResult use(Level world, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!player.isSecondaryUseActive()) {
			BlockHitResult rtr = ToolCommons.raytraceFromEntity(player, 128, false);
			if (rtr.getType() == HitResult.Type.BLOCK) {
				if (!world.isClientSide()) {
					BlockPos pos = rtr.getBlockPos();
					ItemNBTHelper.setInt(stack, TAG_SOURCE_X, pos.getX());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Y, pos.getY());
					ItemNBTHelper.setInt(stack, TAG_SOURCE_Z, pos.getZ());
				}
				return ItemUtils.startUsingInstantly(world, player, hand);
			}
			return InteractionResult.PASS;
		} else {
			reset(world, player, stack);
			return InteractionResult.SUCCESS;
		}
	}

	private static double calculateRadius(ItemStack stack, LivingEntity living) {
		int x = ItemNBTHelper.getInt(stack, TAG_SOURCE_X, 0);
		int y = ItemNBTHelper.getInt(stack, TAG_SOURCE_Y, Integer.MIN_VALUE);
		int z = ItemNBTHelper.getInt(stack, TAG_SOURCE_Z, 0);
		Vec3 source = new Vec3(x, y, z);

		Vec3 centerVec = VecHelper.fromEntityCenter(living);
		Vec3 diffVec = source.subtract(centerVec);
		Vec3 lookVec = living.getLookAngle();
		double mul = diffVec.y / lookVec.y;
		lookVec = lookVec.scale(mul).add(centerVec);

		lookVec = new Vec3(net.minecraft.util.Mth.floor(lookVec.x),
				net.minecraft.util.Mth.floor(lookVec.y),
				net.minecraft.util.Mth.floor(lookVec.z));

		return MathHelper.pointDistancePlane(source.x, source.z, lookVec.x, lookVec.z);
	}

	@Override
	public Component getName(@NotNull ItemStack stack) {
		Component mode = Component.literal(" (")
				.append(Component.translatable(getModeString(stack)))
				.append(")");
		return super.getName(stack).plainCopy().append(mode);
	}

	public static String getModeString(ItemStack stack) {
		return "botaniamisc.sextantMode." + getMode(stack).getKey();
	}

	private static String getRadiusString(double radius) {
		NumberFormat format = getNumberFormat();

		return format.format(radius);
	}

	private static NumberFormat getNumberFormat() {
		var format = NumberFormat.getInstance(Proxy.INSTANCE.getLocale());
		format.setRoundingMode(RoundingMode.HALF_UP);
		format.setMaximumFractionDigits(1);
		format.setMinimumFractionDigits(1);
		return format;
	}

	public static class Hud {
		public static void render(GuiGraphicsExtractor gui, Player player, ItemStack stack) {
			ItemStack onUse = player.getUseItem();
			int time = player.getUseItemRemainingTicks();

			if (onUse == stack && stack.getItem().getUseDuration(stack, player) - time >= 10) {
				double radius = calculateRadius(stack, player);
				Font font = Minecraft.getInstance().font;
				int x = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 30;
				int y = Minecraft.getInstance().getWindow().getGuiScaledHeight() / 2;

				String s = getRadiusString(radius);
				boolean inRange = 0 < radius && radius <= MAX_RADIUS;
				if (!inRange) {
					s = ChatFormatting.RED + s;
				}

				gui.text(font, s, x - font.width(s) / 2, y - 4, 0xFFFFFFFF);

				if (inRange) {
					radius += 4;
					float ringRadius = (float) radius;
					int extent = (int) Math.ceil(ringRadius + 2);
					RenderHelper.guiGeometry(gui, CoreShaders.GUI_TRIANGLES, x - extent, y - extent, x + extent, y + extent, (pose, buf) -> {
						for (int i = 0; i < 360; i++) {
							float rad0 = (float) (i * Math.PI / 180);
							float rad1 = (float) ((i + 1) * Math.PI / 180);
							float inner = ringRadius - 1.5F;
							float outer = ringRadius + 1.5F;
							float c0 = net.minecraft.util.Mth.cos(rad0);
							float s0 = net.minecraft.util.Mth.sin(rad0);
							float c1 = net.minecraft.util.Mth.cos(rad1);
							float s1 = net.minecraft.util.Mth.sin(rad1);
							buf.addVertexWith2DPose(pose, x + c0 * inner, y + s0 * inner).setColor(0xFF00FFFF);
							buf.addVertexWith2DPose(pose, x + c0 * outer, y + s0 * outer).setColor(0xFF00FFFF);
							buf.addVertexWith2DPose(pose, x + c1 * outer, y + s1 * outer).setColor(0xFF00FFFF);
							buf.addVertexWith2DPose(pose, x + c0 * inner, y + s0 * inner).setColor(0xFF00FFFF);
							buf.addVertexWith2DPose(pose, x + c1 * outer, y + s1 * outer).setColor(0xFF00FFFF);
							buf.addVertexWith2DPose(pose, x + c1 * inner, y + s1 * inner).setColor(0xFF00FFFF);
						}
					});
				}
			}
		}
	}

	@FunctionalInterface
	private interface ShapeCreator {
		void create(IStateMatcher matcher, double radius, Map<BlockPos, IStateMatcher> map);
	}

	@FunctionalInterface
	private interface ShapeVisualizer {
		void visualize(Level world, int x, int y, int z, WispParticleData data, double cosR, double sinR);
	}

	public enum Modes {
		CIRCLE("circle", WorldshaperssSextantItem::makeCircle, WorldshaperssSextantItem::visualizeCircle),
		SPHERE("sphere", WorldshaperssSextantItem::makeSphere, WorldshaperssSextantItem::visualizeSphere);

		private final String key;
		private final ShapeCreator creator;
		private final ShapeVisualizer visualizer;

		Modes(String key, ShapeCreator creator, ShapeVisualizer visualizer) {
			this.key = key;
			this.creator = creator;
			this.visualizer = visualizer;
		}

		public String getKey() {
			return key;
		}

		public ShapeCreator getCreator() {
			return creator;
		}

		public ShapeVisualizer getVisualizer() {
			return visualizer;
		}
	}
}
