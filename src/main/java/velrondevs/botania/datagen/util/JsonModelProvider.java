package velrondevs.botania.datagen.util;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class JsonModelProvider implements DataProvider {
	private final PackOutput.PathProvider modelPathProvider;
	private final String modId;
	private final Map<Identifier, JsonModelBuilder> builders = new LinkedHashMap<>();

	protected JsonModelProvider(PackOutput output, String modId) {
		this.modelPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
		this.modId = modId;
	}

	protected abstract void registerModels();

	protected JsonModelBuilder getBuilder(String path) {
		Identifier id = path.contains(":") ? Identifier.parse(path) : Identifier.fromNamespaceAndPath(modId, path);
		return builders.computeIfAbsent(id, JsonModelBuilder::new);
	}

	@Override
	public CompletableFuture<?> run(CachedOutput cache) {
		builders.clear();
		registerModels();
		List<CompletableFuture<?>> output = new ArrayList<>();
		builders.forEach((id, builder) -> output.add(DataProvider.saveStable(cache, builder.toJson(), modelPathProvider.json(id))));
		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}
}
