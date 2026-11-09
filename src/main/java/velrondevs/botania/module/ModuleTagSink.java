package velrondevs.botania.module;

import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;

public interface ModuleTagSink<T> {
	TagAppender<T, T> tag(TagKey<T> key);
}
