package velrondevs.botania.api;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.block.*;
import velrondevs.botania.api.item.AvatarWieldable;
import velrondevs.botania.api.item.BlockProvider;
import velrondevs.botania.api.item.CoordBoundItem;
import velrondevs.botania.api.item.Relic;
import velrondevs.botania.api.mana.ManaCollisionGhost;
import velrondevs.botania.api.mana.ManaItem;
import velrondevs.botania.api.mana.ManaReceiver;
import velrondevs.botania.api.mana.ManaTrigger;
import velrondevs.botania.api.mana.spark.SparkAttachable;

import net.minecraft.resources.ResourceLocation;

public final class BotaniaCapabilities {
	public static final ItemCapability<AvatarWieldable, Void> AVATAR_WIELDABLE = ItemCapability.createVoid(rl("avatar_wieldable"), AvatarWieldable.class);
	public static final ItemCapability<BlockProvider, Void> BLOCK_PROVIDER = ItemCapability.createVoid(rl("block_provider"), BlockProvider.class);
	public static final ItemCapability<CoordBoundItem, Void> COORD_BOUND_ITEM = ItemCapability.createVoid(rl("coord_bound_item"), CoordBoundItem.class);
	public static final ItemCapability<ManaItem, Void> MANA_ITEM = ItemCapability.createVoid(rl("mana_item"), ManaItem.class);
	public static final ItemCapability<Relic, Void> RELIC = ItemCapability.createVoid(rl("relic"), Relic.class);

	public static final BlockCapability<ExoflameHeatable, Void> EXOFLAME_HEATABLE = BlockCapability.createVoid(rl("exoflame_heatable"), ExoflameHeatable.class);
	public static final BlockCapability<HornHarvestable, Void> HORN_HARVEST = BlockCapability.createVoid(rl("horn_harvestable"), HornHarvestable.class);
	public static final BlockCapability<HourglassTrigger, Void> HOURGLASS_TRIGGER = BlockCapability.createVoid(rl("hourglass_trigger"), HourglassTrigger.class);
	public static final BlockCapability<ManaCollisionGhost, Void> MANA_GHOST = BlockCapability.createVoid(rl("mana_ghost"), ManaCollisionGhost.class);
	public static final BlockCapability<ManaReceiver, @Nullable Direction> MANA_RECEIVER = BlockCapability.createSided(rl("mana_receiver"), ManaReceiver.class);
	public static final BlockCapability<SparkAttachable, @Nullable Direction> SPARK_ATTACHABLE = BlockCapability.createSided(rl("spark_attachable"), SparkAttachable.class);
	public static final BlockCapability<ManaTrigger, Void> MANA_TRIGGER = BlockCapability.createVoid(rl("mana_trigger"), ManaTrigger.class);
	public static final BlockCapability<Wandable, Void> WANDABLE = BlockCapability.createVoid(rl("wandable"), Wandable.class);
	public static final BlockCapability<PhantomInkableBlock, Void> PHANTOM_INKABLE = BlockCapability.createVoid(rl("phantom_inkable"), PhantomInkableBlock.class);

	private static ResourceLocation rl(String path) {
		return ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID, path);
	}

	private BotaniaCapabilities() {}
}
