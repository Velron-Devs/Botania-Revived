package velrondevs.botania.module.creativecrafting;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import velrondevs.botania.common.item.ManaTabletItem;
import velrondevs.botania.datagen.providers.creativecrafting.CreativeCraftingDatagen;
import velrondevs.botania.module.BotaniaModule;
import velrondevs.botania.module.ModuleContext;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.concurrent.CompletableFuture;

public final class CreativeCraftingModule extends BotaniaModule {
	public static final String ID = "cre";
	public static final CreativeCraftingModule INSTANCE = new CreativeCraftingModule();

	private CreativeCraftingModule() {
		super(ID, "Creative Crafting");
	}

	public static ItemStack creativeTablet() {
		ItemStack stack = new ItemStack(BotaniaItems.manaTablet);
		ManaTabletItem.setMana(stack, ManaTabletItem.MAX_MANA);
		ManaTabletItem.setStackCreative(stack);
		return stack;
	}

	@Override
	public void register(ModuleContext ctx) {
		ctx.creativeTab(() -> new ItemStack(BotaniaBlocks.creativePool), false, output -> {
			output.accept(new ItemStack(BotaniaBlocks.creativePool));
			output.accept(creativeTablet());
			output.accept(new ItemStack(BotaniaItems.corporeaSparkCreative));
			output.accept(new ItemStack(BotaniaBlocks.infrangiblePlatform));
		});
	}

	@Override
	public void gatherData(GatherDataEvent evt, CompletableFuture<HolderLookup.Provider> lookup) {
		CreativeCraftingDatagen.gatherData(evt, lookup);
	}
}
