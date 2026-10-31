package velrondevs.botania.registry;

import net.minecraft.core.Holder;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.block.AlfheimPortalBlock;
import velrondevs.botania.common.block.AvatarBlock;
import velrondevs.botania.common.block.CraftyCrateBlock;
import velrondevs.botania.common.block.EnderOverseerBlock;
import velrondevs.botania.common.block.ForceRelayBlock;
import velrondevs.botania.common.block.HoveringHourglassBlock;
import velrondevs.botania.common.block.IncensePlateBlock;
import velrondevs.botania.common.block.LuminizerBlock;
import velrondevs.botania.common.block.LuminizerLauncherBlock;
import velrondevs.botania.common.block.ManastormChargeBlock;
import velrondevs.botania.common.block.OpenCrateBlock;
import velrondevs.botania.common.block.PetalApothecaryBlock;
import velrondevs.botania.common.block.PylonBlock;
import velrondevs.botania.common.block.SparkTinkererBlock;
import velrondevs.botania.common.block.mana.BotanicalBreweryBlock;
import velrondevs.botania.common.item.AncientWillItem;
import velrondevs.botania.common.item.AssemblyHaloItem;
import velrondevs.botania.common.item.AstrolabeItem;
import velrondevs.botania.common.item.BaubleBoxItem;
import velrondevs.botania.common.item.BlackHoleTalismanItem;
import velrondevs.botania.common.item.BlackLotusItem;
import velrondevs.botania.common.item.BotaniaBannerPatternItem;
import velrondevs.botania.common.item.CacophoniumItem;
import velrondevs.botania.common.item.CorporeaSparkItem;
import velrondevs.botania.common.item.CraftingPatternItem;
import velrondevs.botania.common.item.CustomCreativeTabContents;
import velrondevs.botania.common.item.EnderHandItem;
import velrondevs.botania.common.item.ExtrapolatedBucketItem;
import velrondevs.botania.common.item.FlowerPouchItem;
import velrondevs.botania.common.item.FloralFertilizerItem;
import velrondevs.botania.common.item.FloralObedienceStickItem;
import velrondevs.botania.common.item.GrassSeedsItem;
import velrondevs.botania.common.item.HornItem;
import velrondevs.botania.common.item.LaputaShardItem;
import velrondevs.botania.common.item.LexicaBotaniaItem;
import velrondevs.botania.common.item.LifeAggregatorItem;
import velrondevs.botania.common.item.LivingwoodSlingshotItem;
import velrondevs.botania.common.item.ManaBlasterItem;
import velrondevs.botania.common.item.ManaMirrorItem;
import velrondevs.botania.common.item.ManaPoolMinecartItem;
import velrondevs.botania.common.item.ManaSparkItem;
import velrondevs.botania.common.item.ManaTabletItem;
import velrondevs.botania.common.item.ManufactoryHaloItem;
import velrondevs.botania.common.item.OvergrowthSeedItem;
import velrondevs.botania.common.item.PhantomInkItem;
import velrondevs.botania.common.item.ResoluteIvyItem;
import velrondevs.botania.common.item.SparkAugmentItem;
import velrondevs.botania.common.item.SpellbindingClothItem;
import velrondevs.botania.common.item.StoneOfTemperanceItem;
import velrondevs.botania.common.item.TerrasteelIngotItem;
import velrondevs.botania.common.item.ThornChakramItem;
import velrondevs.botania.common.item.VineBallItem;
import velrondevs.botania.common.item.WandOfTheForestItem;
import velrondevs.botania.common.item.WorldshaperssSextantItem;
import velrondevs.botania.common.item.block.SpecialFlowerBlockItem;
import velrondevs.botania.common.item.brew.BaseBrewItem;
import velrondevs.botania.common.item.brew.IncenseStickItem;
import velrondevs.botania.common.item.brew.VialItem;
import velrondevs.botania.common.item.equipment.bauble.BaubleItem;
import velrondevs.botania.common.item.lens.LensItem;
import velrondevs.botania.common.item.material.EnderAirItem;
import velrondevs.botania.common.item.material.ManaResourceItem;
import velrondevs.botania.common.item.material.MysticalPetalItem;
import velrondevs.botania.common.item.material.RuneItem;
import velrondevs.botania.common.item.relic.RelicItem;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.BotaniaModules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BotaniaCreativeSections {
	public static final String GENERATING_FLOWERS = "generating_flowers";
	public static final String FUNCTIONAL_FLOWERS = "functional_flowers";
	public static final String MANA = "mana";
	public static final String GEAR = "gear";
	public static final String ACCESSORIES = "accessories";
	public static final String INGREDIENTS = "ingredients";
	public static final String BREWING = "brewing";
	public static final String DECORATION = "decoration";
	public static final String MISC = "misc";

	private static final String PKG_MANA = "velrondevs.botania.common.block.mana";
	private static final String PKG_CORPOREA = "velrondevs.botania.common.block.corporea";
	private static final String PKG_RED_STRING = "velrondevs.botania.common.block.red_string";
	private static final String PKG_RODS = "velrondevs.botania.common.item.rod";

	private static final Set<Item> BASE_ITEMS = new LinkedHashSet<>();
	private static final List<Section> BASE_SECTIONS = new ArrayList<>();
	private static final List<Section> MODULE_SECTIONS = new ArrayList<>();

	static {
		baseSection(GENERATING_FLOWERS, () -> new ItemStack(BotaniaFlowerBlocks.endoflame));
		baseSection(FUNCTIONAL_FLOWERS, () -> new ItemStack(BotaniaFlowerBlocks.orechid));
		baseSection(MANA, () -> new ItemStack(BotaniaBlocks.manaPool));
		baseSection(GEAR, () -> new ItemStack(BotaniaItems.terraPick));
		baseSection(ACCESSORIES, () -> new ItemStack(BotaniaItems.flightTiara));
		baseSection(INGREDIENTS, () -> new ItemStack(BotaniaItems.runeMana));
		baseSection(BREWING, () -> new ItemStack(BotaniaBlocks.brewery));
		baseSection(DECORATION, () -> new ItemStack(BotaniaBlocks.livingwoodLog));
		baseSection(MISC, () -> new ItemStack(BotaniaItems.lexicon));
	}

	private BotaniaCreativeSections() {}

	public static final class Section {
		private final String id;
		private final BotaniaModule module;
		private final Supplier<ItemStack> icon;
		private final Consumer<CreativeModeTab.Output> filler;
		private final BooleanSupplier hasContent;

		private Section(String id, @Nullable BotaniaModule module, Supplier<ItemStack> icon, Consumer<CreativeModeTab.Output> filler, BooleanSupplier hasContent) {
			this.id = id;
			this.module = module;
			this.icon = icon;
			this.filler = filler;
			this.hasContent = hasContent;
		}

		public String id() {
			return id;
		}

		@Nullable
		public BotaniaModule module() {
			return module;
		}

		public String titleKey() {
			return "itemGroup." + LibMisc.MOD_ID + "." + id;
		}

		public ItemStack icon() {
			return icon.get();
		}

		public void fill(CreativeModeTab.Output output) {
			filler.accept(output);
		}

		public boolean isActive() {
			if (module != null && (!module.isEnabled() || !module.isAvailable())) {
				return false;
			}
			return hasContent.getAsBoolean();
		}
	}

	private static void baseSection(String id, Supplier<ItemStack> icon) {
		BASE_SECTIONS.add(new Section(id, null, icon, output -> {
			for (Item item : BASE_ITEMS) {
				if (id.equals(sectionOf(item))) {
					CustomCreativeTabContents.addItem(item, output);
				}
			}
		}, () -> true));
	}

	public static void addBaseItem(Item item) {
		BASE_ITEMS.add(item);
	}

	public static Section registerModuleSection(BotaniaModule module, String id, Supplier<ItemStack> icon, Collection<Item> items, @Nullable Consumer<CreativeModeTab.Output> extra) {
		for (Section existing : BASE_SECTIONS) {
			if (existing.id.equals(id)) {
				throw new IllegalArgumentException("Duplicate creative section " + id);
			}
		}
		for (Section existing : MODULE_SECTIONS) {
			if (existing.id.equals(id)) {
				throw new IllegalArgumentException("Duplicate creative section " + id);
			}
		}
		Section section = new Section(id, module, icon, output -> {
			for (Item item : items) {
				CustomCreativeTabContents.addItem(item, output);
			}
			if (extra != null) {
				extra.accept(output);
			}
		}, () -> extra != null || !items.isEmpty());
		MODULE_SECTIONS.add(section);
		return section;
	}

	public static List<Section> activeSections() {
		List<Section> result = new ArrayList<>(BASE_SECTIONS);
		List<BotaniaModule> order = BotaniaModules.all();
		MODULE_SECTIONS.stream()
				.filter(Section::isActive)
				.sorted((a, b) -> Integer.compare(order.indexOf(a.module), order.indexOf(b.module)))
				.forEach(result::add);
		return result;
	}

	public static String sectionOf(Item item) {
		if (item instanceof SpecialFlowerBlockItem) {
			return isGeneratingFlower(item) ? GENERATING_FLOWERS : FUNCTIONAL_FLOWERS;
		}
		if (item instanceof BaubleItem || item instanceof BaubleBoxItem) {
			return ACCESSORIES;
		}
		if (item instanceof BlockItem blockItem) {
			return sectionOfBlock(blockItem.getBlock());
		}
		return sectionOfItem(item);
	}

	private static boolean isGeneratingFlower(Item item) {
		Holder.Reference<Item> holder = item.builtInRegistryHolder();
		return holder.is(BotaniaTags.Items.GENERATING_SPECIAL_FLOWERS) || holder.is(BotaniaTags.Items.GENERATING_FLOATING_FLOWERS);
	}

	private static String sectionOfBlock(Block block) {
		if (block instanceof PetalApothecaryBlock || block instanceof BotanicalBreweryBlock || block instanceof IncensePlateBlock) {
			return BREWING;
		}
		String pkg = block.getClass().getPackageName();
		if (pkg.equals(PKG_MANA) || pkg.equals(PKG_CORPOREA) || pkg.equals(PKG_RED_STRING)
				|| block instanceof AlfheimPortalBlock || block instanceof PylonBlock || block instanceof AvatarBlock
				|| block instanceof ForceRelayBlock || block instanceof LuminizerBlock || block instanceof LuminizerLauncherBlock
				|| block instanceof ManastormChargeBlock || block instanceof OpenCrateBlock || block instanceof CraftyCrateBlock
				|| block instanceof SparkTinkererBlock || block instanceof EnderOverseerBlock || block instanceof HoveringHourglassBlock) {
			return MANA;
		}
		return DECORATION;
	}

	private static String sectionOfItem(Item item) {
		if (item instanceof LexicaBotaniaItem || item instanceof WandOfTheForestItem
				|| item instanceof FloralObedienceStickItem || item instanceof FloralFertilizerItem) {
			return MISC;
		}
		if (item instanceof RuneItem || item.builtInRegistryHolder().is(BotaniaTags.Items.RUNES)) {
			return INGREDIENTS;
		}
		if (item instanceof LensItem || item instanceof ManaTabletItem || item instanceof ManaMirrorItem
				|| item instanceof ManaBlasterItem || item instanceof ManaSparkItem || item instanceof SparkAugmentItem
				|| item instanceof CorporeaSparkItem || item instanceof ManaPoolMinecartItem
				|| item instanceof CraftingPatternItem || item == BotaniaItems.clip) {
			return MANA;
		}
		if (item instanceof BaseBrewItem || item instanceof VialItem || item instanceof IncenseStickItem) {
			return BREWING;
		}
		if (item instanceof ArmorItem || item instanceof TieredItem || item instanceof ShearsItem
				|| item instanceof ProjectileWeaponItem || item instanceof RelicItem
				|| item.getClass().getPackageName().equals(PKG_RODS)
				|| item instanceof ThornChakramItem || item instanceof HornItem || item instanceof LivingwoodSlingshotItem
				|| item instanceof ExtrapolatedBucketItem || item instanceof LifeAggregatorItem || item instanceof EnderHandItem
				|| item instanceof AssemblyHaloItem || item instanceof ManufactoryHaloItem || item instanceof SpellbindingClothItem
				|| item instanceof FlowerPouchItem || item instanceof BlackHoleTalismanItem || item instanceof StoneOfTemperanceItem
				|| item instanceof AstrolabeItem || item instanceof WorldshaperssSextantItem || item instanceof CacophoniumItem
				|| item instanceof LaputaShardItem) {
			return GEAR;
		}
		if (item == BotaniaItems.manaCookie) {
			return MISC;
		}
		if (item instanceof ManaResourceItem || item instanceof MysticalPetalItem || item instanceof EnderAirItem
				|| item instanceof TerrasteelIngotItem || item instanceof GrassSeedsItem || item instanceof VineBallItem
				|| item instanceof BotaniaBannerPatternItem || item instanceof BlackLotusItem || item instanceof OvergrowthSeedItem
				|| item instanceof AncientWillItem || item instanceof PhantomInkItem || item instanceof ResoluteIvyItem
				|| item.getClass() == Item.class) {
			return INGREDIENTS;
		}
		return MISC;
	}
}
