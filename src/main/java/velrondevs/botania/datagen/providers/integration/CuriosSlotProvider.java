package velrondevs.botania.datagen.providers.integration;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import top.theillusivec4.curios.api.CuriosDataProvider;

import velrondevs.botania.common.lib.LibMisc;

import java.util.concurrent.CompletableFuture;

public class CuriosSlotProvider extends CuriosDataProvider {
	public CuriosSlotProvider(PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
		super(LibMisc.MOD_ID, output, fileHelper, registries);
	}

	@Override
	public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
		createSlot("ring").operation("ADD").size(1);
		createEntities("default_player_slots").addPlayer().addSlots("belt", "body", "charm", "head", "necklace", "ring");
	}
}
