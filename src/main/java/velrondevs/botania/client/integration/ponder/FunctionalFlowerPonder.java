package velrondevs.botania.client.integration.ponder;

import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static velrondevs.botania.registry.BotaniaFlowerBlocks.*;

final class FunctionalFlowerPonder {
	private record Entry(String flower, int plate, PonderStoryBoard board, Block... blocks) {
		List<ResourceLocation> ids() {
			return Arrays.stream(blocks).map(BuiltInRegistries.BLOCK::getKey).toList();
		}
	}

	private static List<Entry> entries;

	private FunctionalFlowerPonder() {}

	private static List<Entry> entries() {
		if (entries == null) {
			List<Entry> list = new ArrayList<>();
			list.add(new Entry("agricarnation", 11, FunctionalFlowerScenesWorld::agricarnation, agricarnation, agricarnationFloating, agricarnationChibi, agricarnationChibiFloating));
			list.add(new Entry("bellethorn", 7, FunctionalFlowerScenesMobs::bellethorn, bellethorn, bellethornFloating, bellethornChibi, bellethornChibiFloating));
			list.add(new Entry("bergamute", 9, FunctionalFlowerScenesWorld::bergamute, bergamute, bergamuteFloating));
			list.add(new Entry("bubbell", 9, FunctionalFlowerScenesWorld::bubbell, bubbell, bubbellFloating, bubbellChibi, bubbellChibiFloating));
			list.add(new Entry("clayconia", 11, FunctionalFlowerScenesWorld::clayconia, clayconia, clayconiaFloating, clayconiaChibi, clayconiaChibiFloating));
			list.add(new Entry("daffomill", 9, FunctionalFlowerScenesItems::daffomill, daffomill, daffomillFloating));
			list.add(new Entry("dreadthorn", 7, FunctionalFlowerScenesMobs::dreadthorn, dreadthorn, dreadthornFloating));
			list.add(new Entry("exoflame", 7, FunctionalFlowerScenesWorld::exoflame, exoflame, exoflameFloating));
			list.add(new Entry("fallen_kanade", 7, FunctionalFlowerScenesMobs::fallenKanade, fallenKanade, fallenKanadeFloating));
			list.add(new Entry("heisei_dream", 9, FunctionalFlowerScenesMobs::heiseiDream, heiseiDream, heiseiDreamFloating));
			list.add(new Entry("hopperhock", 9, FunctionalFlowerScenesItems::hopperhock, hopperhock, hopperhockFloating, hopperhockChibi, hopperhockChibiFloating));
			list.add(new Entry("hyacidus", 7, FunctionalFlowerScenesMobs::hyacidus, hyacidus, hyacidusFloating));
			list.add(new Entry("jaded_amaranthus", 9, FunctionalFlowerScenesWorld::jadedAmaranthus, jadedAmaranthus, jadedAmaranthusFloating));
			list.add(new Entry("jiyuulia", 11, FunctionalFlowerScenesMobs::jiyuulia, jiyuulia, jiyuuliaFloating, jiyuuliaChibi, jiyuuliaChibiFloating));
			list.add(new Entry("labellia", 7, FunctionalFlowerScenesItems::labellia, labellia, labelliaFloating));
			list.add(new Entry("loonium", 9, FunctionalFlowerScenesItems::loonium, loonium, looniumFloating));
			list.add(new Entry("manastar", 7, FunctionalFlowerScenesWorld::manastar, manastar, manastarFloating));
			list.add(new Entry("marimorphosis", 9, FunctionalFlowerScenesWorld::marimorphosis, marimorphosis, marimorphosisFloating, marimorphosisChibi, marimorphosisChibiFloating));
			list.add(new Entry("medumone", 7, FunctionalFlowerScenesMobs::medumone, medumone, medumoneFloating));
			list.add(new Entry("orechid", 7, FunctionalFlowerScenesWorld::orechid, orechid, orechidFloating));
			list.add(new Entry("orechid_ignem", 7, FunctionalFlowerScenesWorld::orechidIgnem, orechidIgnem, orechidIgnemFloating));
			list.add(new Entry("pollidisiac", 7, FunctionalFlowerScenesItems::pollidisiac, pollidisiac, pollidisiacFloating));
			list.add(new Entry("rannuncarpus", 9, FunctionalFlowerScenesItems::rannuncarpus, rannuncarpus, rannuncarpusFloating, rannuncarpusChibi, rannuncarpusChibiFloating));
			list.add(new Entry("solegnolia", 9, FunctionalFlowerScenesItems::solegnolia, solegnolia, solegnoliaFloating, solegnoliaChibi, solegnoliaChibiFloating));
			list.add(new Entry("spectranthemum", 9, FunctionalFlowerScenesItems::spectranthemum, spectranthemum, spectranthemumFloating));
			list.add(new Entry("tangleberrie", 13, FunctionalFlowerScenesMobs::tangleberrie, tangleberrie, tangleberrieFloating, tangleberrieChibi, tangleberrieChibiFloating));
			list.add(new Entry("tigerseye", 9, FunctionalFlowerScenesMobs::tigerseye, tigerseye, tigerseyeFloating));
			list.add(new Entry("vinculotus", 9, FunctionalFlowerScenesMobs::vinculotus, vinculotus, vinculotusFloating));
			entries = list;
		}
		return entries;
	}

	static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		for (Entry entry : entries()) {
			helper.forComponents(entry.ids())
					.addStoryBoard("functional_flower_" + entry.plate(), entry.board(), BotaniaPonderTags.FLOWERS);
		}
	}

	static boolean covers(ResourceLocation id) {
		Set<ResourceLocation> covered = new HashSet<>();
		for (Entry entry : entries()) {
			covered.addAll(entry.ids());
		}
		return covered.contains(id);
	}
}
