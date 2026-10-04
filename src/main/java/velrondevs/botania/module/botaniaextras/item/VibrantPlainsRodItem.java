package velrondevs.botania.module.botaniaextras.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.Avatar;
import velrondevs.botania.api.item.AvatarWieldable;
import velrondevs.botania.api.item.BlockProvider;
import velrondevs.botania.api.mana.ManaItemHandler;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.client.fx.SparkleParticleData;
import velrondevs.botania.client.gui.ItemsRemainingRenderHandler;
import velrondevs.botania.common.helper.ItemNBTHelper;
import velrondevs.botania.common.helper.PlayerHelper;
import velrondevs.botania.common.helper.VecHelper;
import velrondevs.botania.common.item.CustomCreativeTabContents;
import velrondevs.botania.common.item.rod.LandsRodItem;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasBlocks;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasItems;
import velrondevs.botania.module.botaniaextras.IridescentColors;
import velrondevs.botania.module.botaniaextras.block.IridescentDirtBlock;
import velrondevs.botania.registry.BotaniaSounds;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class VibrantPlainsRodItem extends Item implements CustomCreativeTabContents {
	public static final int COST = 150;
	public static final int BIFROST = 16;
	private static final String TAG_COLOR = "color";
	private static final ResourceLocation AVATAR_OVERLAY = prefix("textures/model/botania_extras/avatar_vibrant_plains.png");

	public VibrantPlainsRodItem(Properties props) {
		super(props);
	}

	public static int getColorIndex(ItemStack stack) {
		return Mth.clamp(ItemNBTHelper.getInt(stack, TAG_COLOR, 0), 0, BIFROST);
	}

	public static void setColorIndex(ItemStack stack, int index) {
		ItemNBTHelper.setInt(stack, TAG_COLOR, index);
	}

	@Nullable
	public static DyeColor dyeOf(int index) {
		return index >= BIFROST ? null : DyeColor.byId(index);
	}

	public static Block dirtOf(ItemStack stack) {
		return BotaniaExtrasBlocks.getDirt(dyeOf(getColorIndex(stack)));
	}

	public static ItemStack forColor(int index) {
		ItemStack stack = new ItemStack(BotaniaExtrasItems.vibrantPlainsRod);
		setColorIndex(stack, index);
		return stack;
	}

	private static float[] rgb(ItemStack stack) {
		int color = getColorIndex(stack) >= BIFROST ? 0xFFFFFF : IridescentColors.rgb(dyeOf(getColorIndex(stack)));
		return new float[] { ((color >> 16) & 0xFF) / 255F, ((color >> 8) & 0xFF) / 255F, (color & 0xFF) / 255F };
	}

	@NotNull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		ItemStack stack = ctx.getItemInHand();
		float[] c = rgb(stack);
		return LandsRodItem.place(ctx, dirtOf(stack), COST, c[0], c[1], c[2]);
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (player.isShiftKeyDown()) {
			if (!level.isClientSide) {
				int next = (getColorIndex(stack) + 1) % (BIFROST + 1);
				setColorIndex(stack, next);
				level.playSound(null, player.getX(), player.getY(), player.getZ(), BotaniaSounds.ding, SoundSource.PLAYERS, 0.1F, 1F);
				ItemsRemainingRenderHandler.send(player, new ItemStack(dirtOf(stack)), -2, IridescentColors.displayName(dyeOf(next)));
			}
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
		}
		if (ManaItemHandler.instance().requestManaExactForTool(stack, player, COST * 2, false)) {
			double reach = PriestEmblemItem.hasEmblem(player, PriestEmblemItem.Type.SIF)
					? Math.max(3.0, player.blockInteractionRange() - 1)
					: 3.0;
			Vec3 placeVec = VecHelper.fromEntityCenter(player).add(player.getLookAngle().scale(reach));
			int x = Mth.floor(placeVec.x);
			int y = Mth.floor(placeVec.y) + 1;
			int z = Mth.floor(placeVec.z);
			int entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(x, y, z, x + 1, y + 1, z + 1)).size();
			if (entities == 0) {
				BlockHitResult hit = new BlockHitResult(Vec3.ZERO, Direction.DOWN, new BlockPos(x, y, z), false);
				InteractionResult result = PlayerHelper.substituteUse(new UseOnContext(player, hand, hit), new ItemStack(dirtOf(stack)));
				if (result.consumesAction()) {
					if (!level.isClientSide) {
						ManaItemHandler.instance().requestManaExactForTool(stack, player, COST * 2, true);
					}
					float[] c = rgb(stack);
					SparkleParticleData data = SparkleParticleData.sparkle(1F, c[0], c[1], c[2], 5);
					for (int i = 0; i < 6; i++) {
						level.addParticle(data, x + Math.random(), y + Math.random(), z + Math.random(), 0, 0, 0);
					}
				}
			}
		}
		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		tooltip.add(IridescentColors.displayName(dyeOf(getColorIndex(stack))).copy().withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void addToCreativeTab(Item me, CreativeModeTab.Output output) {
		for (int i = 0; i <= BIFROST; i++) {
			output.accept(forColor(i));
		}
	}

	public static class BlockProviderImpl implements BlockProvider {
		private final ItemStack stack;

		public BlockProviderImpl(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public boolean provideBlock(Player player, ItemStack requestor, Block block, boolean doit) {
			if (block instanceof IridescentDirtBlock) {
				return ManaItemHandler.instance().requestManaExactForTool(requestor, player, COST, doit);
			}
			return false;
		}

		@Override
		public int getBlockCount(Player player, ItemStack requestor, Block block) {
			if (block instanceof IridescentDirtBlock) {
				return ManaItemHandler.instance().getInvocationCountForTool(requestor, player, COST);
			}
			return 0;
		}

		@Override
		public Block getProvidedBlock(Player player, ItemStack requestor) {
			return dirtOf(stack);
		}
	}

	public static class AvatarBehavior implements AvatarWieldable {
		private final ItemStack stack;

		public AvatarBehavior(ItemStack stack) {
			this.stack = stack;
		}

		@Override
		public void onAvatarUpdate(Avatar tile) {
			BlockEntity te = (BlockEntity) tile;
			Level level = te.getLevel();
			ManaReceiver receiver = XplatAbstractions.INSTANCE.findManaReceiver(level, te.getBlockPos(), te.getBlockState(), te, null);
			if (level.isClientSide || receiver == null || receiver.getCurrentMana() < COST || !tile.isEnabled() || tile.getElapsedFunctionalTicks() % 50 != 0) {
				return;
			}
			BlockPos pos = te.getBlockPos().relative(tile.getAvatarFacing());
			if (level.getBlockState(pos).isAir()) {
				BlockState dirt = dirtOf(stack).defaultBlockState();
				level.setBlock(pos, dirt, Block.UPDATE_ALL);
				level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(dirt));
				receiver.receiveMana(-COST);
			}
		}

		@Override
		public ResourceLocation getOverlayResource(Avatar tile) {
			return AVATAR_OVERLAY;
		}
	}
}
