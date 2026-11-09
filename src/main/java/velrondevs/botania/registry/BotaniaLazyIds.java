package velrondevs.botania.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import velrondevs.botania.api.BotaniaAPI;
import velrondevs.botania.mixin.BlockBehaviourLazyAccessor;
import velrondevs.botania.mixin.BlockPropertiesAccessor;
import velrondevs.botania.mixin.ItemLazyAccessor;
import velrondevs.botania.mixin.ItemPropertiesLazyAccessor;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public final class BotaniaLazyIds {
	public static final String PENDING_DESCRIPTION = "botania.pending";
	public static final Identifier PENDING_ID = Identifier.fromNamespaceAndPath(BotaniaAPI.MODID, "pending");

	private static final Map<Item, Item.Properties> PENDING_ITEMS = new IdentityHashMap<>();
	private static final Map<BlockBehaviour, Boolean> PENDING_BLOCKS = new IdentityHashMap<>();

	private BotaniaLazyIds() {}

	public static synchronized void trackItem(Item item, Item.Properties properties) {
		PENDING_ITEMS.put(item, properties);
	}

	public static synchronized void trackBlock(BlockBehaviour block) {
		PENDING_BLOCKS.put(block, Boolean.TRUE);
	}

	public static synchronized void resolveBlocks() {
		Iterator<BlockBehaviour> it = PENDING_BLOCKS.keySet().iterator();
		while (it.hasNext()) {
			BlockBehaviour behaviour = it.next();
			if (!(behaviour instanceof Block block)) {
				it.remove();
				continue;
			}
			Optional<ResourceKey<Block>> key = BuiltInRegistries.BLOCK.getResourceKey(block);
			if (key.isEmpty()) {
				continue;
			}
			BlockPropertiesAccessor properties = (BlockPropertiesAccessor) block.properties();
			properties.botania$setId(key.get());
			BlockBehaviourLazyAccessor accessor = (BlockBehaviourLazyAccessor) block;
			accessor.botania$setDrops(properties.botania$effectiveDrops());
			accessor.botania$setDescriptionId(properties.botania$effectiveDescriptionId());
			it.remove();
		}
	}

	public static synchronized void resolveItems() {
		Iterator<Map.Entry<Item, Item.Properties>> it = PENDING_ITEMS.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Item, Item.Properties> entry = it.next();
			Item item = entry.getKey();
			Optional<ResourceKey<Item>> key = BuiltInRegistries.ITEM.getResourceKey(item);
			if (key.isEmpty()) {
				continue;
			}
			Item.Properties properties = entry.getValue();
			ItemPropertiesLazyAccessor accessor = (ItemPropertiesLazyAccessor) properties;
			accessor.botania$setId(key.get());
			String description;
			if (item instanceof BlockItem blockItem && accessor.botania$getDescriptionId() == ItemPropertiesLazyAccessor.botania$itemDescriptionId()) {
				description = blockItem.getBlock().getDescriptionId();
			} else {
				description = accessor.botania$effectiveDescriptionId();
			}
			((ItemLazyAccessor) item).botania$setDescriptionId(description);
			BuiltInRegistries.DATA_COMPONENT_INITIALIZERS.add(key.get(),
					accessor.botania$finalizeInitializer(Component.translatable(description), properties.effectiveModel()));
			it.remove();
		}
	}
}
