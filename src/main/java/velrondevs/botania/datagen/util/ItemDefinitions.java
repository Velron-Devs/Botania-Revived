package velrondevs.botania.datagen.util;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelDispatcher;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ItemDefinitions implements ItemModelOutput {
	private static final String ITEM_PREFIX = "item/";

	private final Map<Item, ClientItem> definitions = new LinkedHashMap<>();
	private final Map<Item, Item> copies = new HashMap<>();

	public void clear() {
		definitions.clear();
		copies.clear();
	}

	@Override
	public void accept(Item item, ItemModel.Unbaked model, ClientItem.Properties properties) {
		register(item, new ClientItem(model, properties));
	}

	@Override
	public void register(Item item, ClientItem clientItem) {
		if (definitions.put(item, clientItem) != null) {
			throw new IllegalStateException("Duplicate item model definition for " + BuiltInRegistries.ITEM.getKey(item));
		}
	}

	@Override
	public void copy(Item donor, Item acceptor) {
		copies.put(acceptor, donor);
	}

	public boolean contains(Item item) {
		return definitions.containsKey(item) || copies.containsKey(item);
	}

	public void fillFromItemModels(Collection<Identifier> modelIds, Function<Item, List<ItemTintSource>> tints) {
		for (Identifier modelId : modelIds) {
			if (!modelId.getPath().startsWith(ITEM_PREFIX)) {
				continue;
			}
			Identifier itemId = modelId.withPath(modelId.getPath().substring(ITEM_PREFIX.length()));
			BuiltInRegistries.ITEM.getOptional(itemId).ifPresent(item -> {
				if (!contains(item)) {
					accept(item, model(modelId, tints.apply(item)));
				}
			});
		}
	}

	public static ItemModel.Unbaked model(Identifier modelId, List<ItemTintSource> tints) {
		return tints.isEmpty()
				? ItemModelUtils.plainModel(modelId)
				: ItemModelUtils.tintedModel(modelId, tints.toArray(ItemTintSource[]::new));
	}

	public CompletableFuture<?> save(CachedOutput cache, PackOutput output) {
		copies.forEach((acceptor, donor) -> {
			ClientItem donorInfo = definitions.get(donor);
			if (donorInfo == null) {
				throw new IllegalStateException("Missing donor: " + donor + " -> " + acceptor);
			}
			register(acceptor, donorInfo);
		});
		copies.clear();
		PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
		return DataProvider.saveAll(cache, ClientItem.CODEC, item -> pathProvider.json(BuiltInRegistries.ITEM.getKey(item)), definitions);
	}

	public static CompletableFuture<?> saveBlockStates(CachedOutput cache, PackOutput output, Collection<BlockModelDefinitionGenerator> generators) {
		PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		Map<Block, BlockStateModelDispatcher> definitions = new LinkedHashMap<>();
		for (BlockModelDefinitionGenerator generator : generators) {
			if (definitions.put(generator.block(), generator.create()) != null) {
				throw new IllegalStateException("Duplicate blockstate definition for " + BuiltInRegistries.BLOCK.getKey(generator.block()));
			}
		}
		return DataProvider.saveAll(cache, BlockStateModelDispatcher.CODEC, block -> pathProvider.json(BuiltInRegistries.BLOCK.getKey(block)), definitions);
	}

	public static CompletableFuture<?> saveModels(CachedOutput cache, PackOutput output, Map<Identifier, ModelInstance> models) {
		PackOutput.PathProvider pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		return DataProvider.saveAll(cache, ModelInstance::get, pathProvider::json, models);
	}
}
