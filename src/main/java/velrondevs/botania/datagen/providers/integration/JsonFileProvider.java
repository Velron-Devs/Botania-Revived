package velrondevs.botania.datagen.providers.integration;

import com.google.gson.JsonObject;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class JsonFileProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;
	private final String name;

	protected JsonFileProvider(PackOutput output, PackOutput.Target target, String kind, String name) {
		this.pathProvider = output.createPathProvider(target, kind);
		this.name = name;
	}

	protected abstract void addFiles(Map<ResourceLocation, JsonObject> files);

	@Override
	public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
		Map<ResourceLocation, JsonObject> files = new LinkedHashMap<>();
		addFiles(files);
		List<CompletableFuture<?>> output = new ArrayList<>();
		files.forEach((id, json) -> output.add(DataProvider.saveStable(cache, json, pathProvider.json(id))));
		return CompletableFuture.allOf(output.toArray(CompletableFuture[]::new));
	}

	@Override
	public @NotNull String getName() {
		return name;
	}
}
