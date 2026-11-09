package velrondevs.botania.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ComponentBindingProvider implements DataProvider {
	private final CompletableFuture<HolderLookup.Provider> lookup;

	public ComponentBindingProvider(CompletableFuture<HolderLookup.Provider> lookup) {
		this.lookup = lookup;
	}

	@NotNull
	@Override
	public CompletableFuture<?> run(@NotNull CachedOutput cache) {
		return lookup.thenAccept(registries -> BuiltInRegistries.ITEM.listElements().forEach(holder -> {
			try {
				holder.components();
			} catch (NullPointerException e) {
				holder.bindComponents(DataComponentMap.EMPTY);
			}
		}));
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania item components binding";
	}
}
