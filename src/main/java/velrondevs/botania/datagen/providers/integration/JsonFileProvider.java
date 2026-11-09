package velrondevs.botania.datagen.providers.integration;

import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class JsonFileProvider implements DataProvider {
	private final Path root;
	private final String kind;
	private final String name;

	protected JsonFileProvider(PackOutput output, PackOutput.Target target, String kind, String name) {
		this.root = output.getOutputFolder(target);
		this.kind = kind;
		this.name = name;
	}

	protected abstract void addFiles(Map<Identifier, JsonObject> files);

	private Path json(Identifier id) {
		String path = kind.isEmpty() ? id.getPath() : kind + "/" + id.getPath();
		return root.resolve(id.getNamespace()).resolve(path + ".json");
	}

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
		Map<Identifier, JsonObject> files = new LinkedHashMap<>();
		addFiles(files);
		List<CompletableFuture<?>> output = new ArrayList<>();
		files.forEach((id, json) -> output.add(DataProvider.saveStable(cache, json, json(id))));
		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	@Override
	public @NotNull String getName() {
		return name;
	}
}
