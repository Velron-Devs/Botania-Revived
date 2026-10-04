package velrondevs.botania.module;

import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;

public interface ModuleTagSink<T> {
	TagsProvider.TagAppender<T> tag(TagKey<T> key);
}
