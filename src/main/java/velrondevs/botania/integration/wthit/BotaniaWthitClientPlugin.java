package velrondevs.botania.integration.wthit;

import mcp.mobius.waila.api.IBlockAccessor;
import mcp.mobius.waila.api.IBlockComponentProvider;
import mcp.mobius.waila.api.IClientRegistrar;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.ITooltipLine;
import mcp.mobius.waila.api.IWailaClientPlugin;
import mcp.mobius.waila.api.WailaHelper;
import mcp.mobius.waila.api.component.BarComponent;
import mcp.mobius.waila.api.component.ItemComponent;
import mcp.mobius.waila.api.component.PairComponent;
import mcp.mobius.waila.api.component.ProgressArrowComponent;
import mcp.mobius.waila.api.component.SpacingComponent;
import mcp.mobius.waila.api.component.WrappedComponent;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import velrondevs.botania.api.recipe.ManaInfusionRecipe;
import velrondevs.botania.common.block.block_entity.mana.ManaPoolBlockEntity;

import java.util.List;

public class BotaniaWthitClientPlugin implements IWailaClientPlugin {
	@Override
	public void register(IClientRegistrar registrar) {
		registrar.body(BodyProvider.INSTANCE, BlockEntity.class);
	}

	public enum BodyProvider implements IBlockComponentProvider {
		INSTANCE;

		@Override
		public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
			BlockEntity be = accessor.getBlockEntity();
			if (be == null || !WthitData.isBotania(be)) {
				return;
			}
			Level level = accessor.getLevel();
			HolderLookup.Provider registries = level.registryAccess();
			CompoundTag raw = accessor.getData().raw();
			CompoundTag tag = raw.contains(WthitData.ROOT) ? raw.getCompound(WthitData.ROOT) : WthitData.collect(be, false);

			appendMana(tooltip, tag, config);

			if (be instanceof ManaPoolBlockEntity pool) {
				if (tag.contains(WthitData.OUTPUTTING) && config.getBoolean(BotaniaWthitPlugin.POOL_MODE)) {
					tooltip.addLine(pair("mode", Component.translatable(tag.getBoolean(WthitData.OUTPUTTING) ? "botania.wthit.pool.charging" : "botania.wthit.pool.draining")));
				}
				if (config.getBoolean(BotaniaWthitPlugin.POOL_RECIPE)) {
					appendPoolRecipe(tooltip, accessor, pool, tag);
				}
			}

			if (tag.contains(WthitData.LENS) && config.getBoolean(BotaniaWthitPlugin.SPREADER_LENS)) {
				ItemStack lens = WthitData.getStack(tag, WthitData.LENS, registries);
				if (!lens.isEmpty()) {
					tooltip.addLine(pair("lens", lens.getHoverName()));
				}
			}

			boolean flower = tag.contains(WthitData.FLOWER_COLOR);
			BlockPos target = WthitData.getPos(tag, WthitData.TARGET);
			if (flower) {
				if (tag.contains(WthitData.BOUND) && config.getBoolean(BotaniaWthitPlugin.FLOWER_BINDING)) {
					if (target != null) {
						tooltip.addLine(pair("bound", describe(level, target)));
					} else {
						tooltip.addLine(Component.translatable("botania.wthit.unbound").withStyle(ChatFormatting.GRAY));
					}
				}
				if (config.getBoolean(BotaniaWthitPlugin.FLOWER_TIMERS)) {
					if (tag.contains(WthitData.BURN)) {
						tooltip.addLine(pair("burn_time", Component.literal(time(tag.getInt(WthitData.BURN)))));
					}
					if (tag.contains(WthitData.COOLDOWN)) {
						tooltip.addLine(pair("cooldown", Component.literal(time(tag.getInt(WthitData.COOLDOWN)))));
					}
					if (tag.contains(WthitData.DIGESTING)) {
						tooltip.addLine(pair("digesting", Component.literal(number(tag.getInt(WthitData.DIGESTING), config))));
					}
					if (tag.contains(WthitData.DECAY)) {
						int decay = tag.getInt(WthitData.DECAY);
						int max = Math.max(1, tag.getInt(WthitData.DECAY_MAX));
						if (config.getBoolean(BotaniaWthitPlugin.PROGRESS_ENABLED)) {
							tooltip.addLine(pair("decay", new BarComponent((float) decay / max, color(config, BotaniaWthitPlugin.PROGRESS_COLOR), time(decay))));
						} else {
							tooltip.addLine(pair("decay", Component.literal(time(decay))));
						}
					}
					if (tag.contains(WthitData.CONVERTING)) {
						tooltip.addLine(pair("converting", Component.literal(String.valueOf(tag.getInt(WthitData.CONVERTING)))));
					}
					if (tag.getBoolean(WthitData.OVERGROWTH)) {
						tooltip.addLine(Component.translatable("botania.wthit.overgrowth").withStyle(ChatFormatting.GREEN));
					}
				}
			} else if (target != null && config.getBoolean(BotaniaWthitPlugin.SPREADER_TARGET)) {
				tooltip.addLine(pair("target", describe(level, target)));
			}

			if (config.getBoolean(BotaniaWthitPlugin.CRAFTING_STATUS)) {
				if (tag.contains(WthitData.FLUID)) {
					tooltip.addLine(pair("fluid", Component.translatable("botania.wthit.fluid." + tag.getString(WthitData.FLUID))));
				}
				if (tag.contains(WthitData.STAGE)) {
					tooltip.addLine(pair("stage", Component.translatable("botania.wthit.stage." + tag.getString(WthitData.STAGE))));
				}
				if (tag.contains(WthitData.ACTIVE)) {
					tooltip.addLine(pair("status", Component.translatable(tag.getBoolean(WthitData.ACTIVE) ? "botania.wthit.active" : "botania.wthit.inactive")));
				}
			}

			if (config.getBoolean(BotaniaWthitPlugin.CRAFTING_RECIPE) && (tag.contains(WthitData.INPUTS) || tag.contains(WthitData.OUTPUT))) {
				appendRecipeRow(tooltip, WthitData.getStacks(tag, WthitData.INPUTS, registries),
						WthitData.getStack(tag, WthitData.OUTPUT, registries), tag.getFloat(WthitData.PROGRESS));
			}

			if (tag.contains(WthitData.ENERGY) && config.getBoolean(BotaniaWthitPlugin.ENERGY_ENABLED)) {
				int energy = tag.getInt(WthitData.ENERGY);
				int max = Math.max(1, tag.getInt(WthitData.MAX_ENERGY));
				String text = number(energy, config) + " / " + number(max, config) + " FE";
				tooltip.addLine(new BarComponent(Math.min(1F, (float) energy / max), color(config, BotaniaWthitPlugin.ENERGY_COLOR), text));
			}

			if (tag.contains(WthitData.PORTAL) && config.getBoolean(BotaniaWthitPlugin.PORTAL_ENABLED)) {
				boolean open = tag.getBoolean(WthitData.PORTAL);
				tooltip.addLine(pair("portal", Component.translatable(open ? "botania.wthit.portal.open" : "botania.wthit.portal.closed")
						.withStyle(open ? ChatFormatting.GREEN : ChatFormatting.GRAY)));
				if (tag.contains(WthitData.PYLONS)) {
					tooltip.addLine(pair("pylons", Component.literal(String.valueOf(tag.getInt(WthitData.PYLONS)))));
				}
			}

			if (config.getBoolean(BotaniaWthitPlugin.PROGRESS_ENABLED)) {
				if (tag.contains(WthitData.TIME_TOTAL)) {
					int done = tag.getInt(WthitData.TIME_DONE);
					int total = Math.max(1, tag.getInt(WthitData.TIME_TOTAL));
					tooltip.addLine(new BarComponent(Math.min(1F, (float) done / total), color(config, BotaniaWthitPlugin.PROGRESS_COLOR), time(done) + " / " + time(total)));
				}
				if (tag.contains(WthitData.TIME_LEFT)) {
					tooltip.addLine(pair("time_left", Component.literal(time(tag.getInt(WthitData.TIME_LEFT)))));
				}
			}
		}

		private static void appendMana(ITooltip tooltip, CompoundTag tag, IPluginConfig config) {
			if (!tag.contains(WthitData.MANA)) {
				return;
			}
			boolean flower = tag.contains(WthitData.FLOWER_COLOR);
			if (!config.getBoolean(flower ? BotaniaWthitPlugin.FLOWER_MANA : BotaniaWthitPlugin.MANA_ENABLED)) {
				return;
			}
			int mana = tag.getInt(WthitData.MANA);
			if (!tag.contains(WthitData.MAX_MANA)) {
				tooltip.addLine(pair("mana", Component.literal(number(mana, config))));
				return;
			}
			int max = tag.getInt(WthitData.MAX_MANA);
			if (max <= 0) {
				return;
			}
			int barColor = flower && config.getBoolean(BotaniaWthitPlugin.FLOWER_COLOR)
					? 0xFF000000 | tag.getInt(WthitData.FLOWER_COLOR)
					: color(config, BotaniaWthitPlugin.MANA_COLOR);
			String text = number(mana, config) + " / " + number(max, config);
			tooltip.addLine(new BarComponent(Math.min(1F, (float) mana / max), barColor, text));
		}

		private static void appendPoolRecipe(ITooltip tooltip, IBlockAccessor accessor, ManaPoolBlockEntity pool, CompoundTag tag) {
			ItemStack held = accessor.getPlayer().getMainHandItem();
			if (held.isEmpty()) {
				return;
			}
			Level level = accessor.getLevel();
			ManaInfusionRecipe recipe = pool.getMatchingRecipe(held, level.getBlockState(pool.getBlockPos().below()));
			if (recipe == null) {
				return;
			}
			int cost = recipe.getManaToConsume();
			int mana = tag.contains(WthitData.MANA) ? tag.getInt(WthitData.MANA) : pool.getCurrentMana();
			float progress = cost <= 0 ? 1F : Math.min(1F, (float) mana / cost);
			ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
			appendRecipeRow(tooltip, List.of(held.copyWithCount(1)), output, progress);
		}

		private static void appendRecipeRow(ITooltip tooltip, List<ItemStack> inputs, ItemStack output, float progress) {
			if (inputs.isEmpty() && output.isEmpty()) {
				return;
			}
			ITooltipLine line = tooltip.addLine();
			int shown = 0;
			for (ItemStack input : inputs) {
				if (shown++ >= 9) {
					break;
				}
				line.with(new ItemComponent(input));
			}
			if (!output.isEmpty()) {
				if (!inputs.isEmpty()) {
					line.with(new SpacingComponent(2, 0));
					line.with(new ProgressArrowComponent(progress));
					line.with(new SpacingComponent(2, 0));
				}
				line.with(new ItemComponent(output));
			}
		}

		private static Component describe(Level level, BlockPos pos) {
			Component name = level.isLoaded(pos) ? level.getBlockState(pos).getBlock().getName() : Component.literal("?");
			return name.copy().append(Component.literal(" (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")").withStyle(ChatFormatting.GRAY));
		}

		private static PairComponent pair(String key, Component value) {
			return new PairComponent(Component.translatable("botania.wthit." + key), value);
		}

		private static PairComponent pair(String key, BarComponent value) {
			return new PairComponent(new WrappedComponent(Component.translatable("botania.wthit." + key)), value);
		}

		private static int color(IPluginConfig config, ResourceLocation key) {
			return 0xFF000000 | (config.getInt(key) & 0xFFFFFF);
		}

		private static String number(int value, IPluginConfig config) {
			return config.getBoolean(BotaniaWthitPlugin.MANA_SHORT) ? WailaHelper.suffix(value) : String.valueOf(value);
		}

		private static String time(int ticks) {
			int seconds = Math.max(0, ticks) / 20;
			int hours = seconds / 3600;
			int minutes = (seconds / 60) % 60;
			int secs = seconds % 60;
			if (hours > 0) {
				return String.format("%d:%02d:%02d", hours, minutes, secs);
			}
			return String.format("%d:%02d", minutes, secs);
		}
	}
}
