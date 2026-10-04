package velrondevs.botania.common.item.record;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxSong;

public class BotaniaRecordItem extends Item {
	public BotaniaRecordItem(ResourceKey<JukeboxSong> song, Properties builder) {
		super(builder.jukeboxPlayable(song));
	}
}
