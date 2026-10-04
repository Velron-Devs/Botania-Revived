package velrondevs.botania.common.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.StateIngredient;

import java.util.*;

public class StateIngredientHelper {
	private static final Codec<Block> LENIENT_BLOCK_CODEC = ResourceLocation.CODEC.xmap(BuiltInRegistries.BLOCK::get, BuiltInRegistries.BLOCK::getKey);

	private record StateData(ResourceLocation name, Map<String, String> properties) {}

	private static final MapCodec<StateData> STATE_DATA_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			ResourceLocation.CODEC.fieldOf("name").forGetter(StateData::name),
			Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("properties", Map.of()).forGetter(StateData::properties)
	).apply(instance, StateData::new));

	public static final MapCodec<BlockState> BLOCK_STATE_MAP_CODEC = STATE_DATA_CODEC.flatXmap(
			StateIngredientHelper::toState, state -> DataResult.success(fromState(state)));
	public static final Codec<BlockState> BLOCK_STATE_CODEC = BLOCK_STATE_MAP_CODEC.codec();

	public static final Codec<StateIngredient> CODEC = Codec.recursive("StateIngredient",
			self -> Codec.STRING.partialDispatch("type",
					StateIngredientHelper::getTypeName,
					type -> getCodec(type, self)));

	public static final Codec<StateIngredient> NON_AIR_CODEC = CODEC.flatXmap(ingredient -> {
		StateIngredient cleared = clearTheAir(ingredient);
		return cleared == null
				? DataResult.error(() -> "State ingredient only matches air: " + ingredient)
				: DataResult.success(cleared);
	}, DataResult::success);

	public static final StreamCodec<RegistryFriendlyByteBuf, StateIngredient> STREAM_CODEC = StreamCodec.ofMember(StateIngredient::write, StateIngredientHelper::read);

	private static DataResult<String> getTypeName(StateIngredient ingredient) {
		if (ingredient instanceof TagExcludingStateIngredient) {
			return DataResult.success("tag_excluding");
		} else if (ingredient instanceof TagStateIngredient) {
			return DataResult.success("tag");
		} else if (ingredient instanceof BlocksStateIngredient) {
			return DataResult.success("blocks");
		} else if (ingredient instanceof BlockStateIngredient) {
			return DataResult.success("block");
		} else if (ingredient instanceof BlockStateStateIngredient) {
			return DataResult.success("state");
		} else if (ingredient instanceof CompoundStateIngredient) {
			return DataResult.success("compound");
		}
		return DataResult.error(() -> "Unknown state ingredient: " + ingredient);
	}

	private static DataResult<MapCodec<? extends StateIngredient>> getCodec(String type, Codec<StateIngredient> self) {
		return switch (type) {
			case "tag" -> DataResult.success(ResourceLocation.CODEC.fieldOf("tag")
					.xmap(TagStateIngredient::new, TagStateIngredient::getTagId));
			case "block" -> DataResult.success(LENIENT_BLOCK_CODEC.fieldOf("block")
					.xmap(BlockStateIngredient::new, BlockStateIngredient::getBlock));
			case "state" -> DataResult.success(BLOCK_STATE_MAP_CODEC
					.xmap(BlockStateStateIngredient::new, BlockStateStateIngredient::getState));
			case "blocks" -> DataResult.success(LENIENT_BLOCK_CODEC.listOf().fieldOf("blocks")
					.xmap(BlocksStateIngredient::new, ingredient -> ingredient.blocks.asList()));
			case "tag_excluding" -> DataResult.success(RecordCodecBuilder.<TagExcludingStateIngredient>mapCodec(instance -> instance.group(
					ResourceLocation.CODEC.fieldOf("tag").forGetter(TagStateIngredient::getTagId),
					self.listOf().fieldOf("exclude").forGetter(TagExcludingStateIngredient::getExcludes)
			).apply(instance, TagExcludingStateIngredient::new)));
			case "compound" -> DataResult.success(self.listOf().fieldOf("ingredients")
					.xmap(CompoundStateIngredient::new, ingredient -> ingredient.getIngredients().asList()));
			default -> DataResult.error(() -> "Unknown state ingredient type: " + type);
		};
	}

	private static DataResult<BlockState> toState(StateData data) {
		Optional<Block> block = BuiltInRegistries.BLOCK.getOptional(data.name());
		if (block.isEmpty()) {
			return DataResult.error(() -> "Invalid or unknown block ID: " + data.name());
		}
		BlockState state = block.get().defaultBlockState();
		StateDefinition<Block, BlockState> definition = block.get().getStateDefinition();
		for (Map.Entry<String, String> entry : data.properties().entrySet()) {
			Property<?> property = definition.getProperty(entry.getKey());
			if (property != null) {
				state = setValue(state, property, entry.getValue());
			}
		}
		return DataResult.success(state);
	}

	private static <T extends Comparable<T>> BlockState setValue(BlockState state, Property<T> property, String value) {
		return property.getValue(value).map(v -> state.setValue(property, v)).orElse(state);
	}

	private static StateData fromState(BlockState state) {
		Map<String, String> properties = new TreeMap<>();
		for (Map.Entry<Property<?>, Comparable<?>> entry : state.getValues().entrySet()) {
			properties.put(entry.getKey().getName(), getValueName(entry.getKey(), entry.getValue()));
		}
		return new StateData(BuiltInRegistries.BLOCK.getKey(state.getBlock()), properties);
	}

	@SuppressWarnings("unchecked")
	private static <T extends Comparable<T>> String getValueName(Property<T> property, Comparable<?> value) {
		return property.getName((T) value);
	}

	public static StateIngredient of(Block block) {
		return new BlockStateIngredient(block);
	}

	public static StateIngredient of(BlockState state) {
		return new BlockStateStateIngredient(state);
	}

	public static StateIngredient of(TagKey<Block> tag) {
		return of(tag.location());
	}

	public static StateIngredient of(ResourceLocation id) {
		return new TagStateIngredient(id);
	}

	public static StateIngredient of(Collection<Block> blocks) {
		return new BlocksStateIngredient(blocks);
	}

	public static StateIngredient compound(Collection<StateIngredient> ingredients) {
		return new CompoundStateIngredient(ingredients);
	}

	public static StateIngredient combine(StateIngredient firstIngredient, StateIngredient secondIngredient) {
		List<StateIngredient> ingredients = new ArrayList<>();

		if (firstIngredient instanceof CompoundStateIngredient compound) {
			ingredients.addAll(compound.getIngredients());
		} else {
			ingredients.add(firstIngredient);
		}
		if (secondIngredient instanceof CompoundStateIngredient compound) {
			ingredients.addAll(compound.getIngredients());
		} else {
			ingredients.add(secondIngredient);
		}

		return new CompoundStateIngredient(ingredients);
	}

	public static StateIngredient tagExcluding(TagKey<Block> tag, StateIngredient... excluded) {
		return new TagExcludingStateIngredient(tag.location(), List.of(excluded));
	}

	public static StateIngredient deserialize(JsonObject object) {
		return CODEC.parse(JsonOps.INSTANCE, object).getOrThrow(JsonParseException::new);
	}

	@Nullable
	public static StateIngredient tryDeserialize(JsonObject object) {
		return clearTheAir(deserialize(object));
	}

	public static StateIngredient clearTheAir(StateIngredient ingredient) {
		if (ingredient instanceof BlockStateIngredient || ingredient instanceof BlockStateStateIngredient) {
			if (ingredient.test(Blocks.AIR.defaultBlockState())) {
				return null;
			}
		} else if (ingredient instanceof BlocksStateIngredient sib) {
			Collection<Block> blocks = sib.blocks;
			List<Block> list = new ArrayList<>(blocks);
			if (list.removeIf(b -> b == Blocks.AIR)) {
				if (list.size() == 0) {
					return null;
				}
				return of(list);
			}
		} else if (ingredient instanceof CompoundStateIngredient sic) {
			List<StateIngredient> newIngredients = sic.getIngredients().stream().map(StateIngredientHelper::clearTheAir).filter(Objects::nonNull).toList();
			if (newIngredients.isEmpty()) {
				return null;
			}
			return compound(newIngredients);
		}
		return ingredient;
	}

	public static StateIngredient read(FriendlyByteBuf buffer) {
		switch (buffer.readVarInt()) {
			case 0:
				int count = buffer.readVarInt();
				Set<Block> set = new HashSet<>();
				for (int i = 0; i < count; i++) {
					int id = buffer.readVarInt();
					Block block = BuiltInRegistries.BLOCK.byId(id);
					set.add(block);
				}
				return new BlocksStateIngredient(set);
			case 1:
				return new BlockStateIngredient(BuiltInRegistries.BLOCK.byId(buffer.readVarInt()));
			case 2:
				return new BlockStateStateIngredient(Block.stateById(buffer.readVarInt()));
			case 3:
				int ingredientCount = buffer.readVarInt();
				Set<StateIngredient> ingredientSet = new HashSet<>();
				for (int i = 0; i < ingredientCount; i++) {
					ingredientSet.add(read(buffer));
				}
				return new CompoundStateIngredient(ingredientSet);
			default:
				throw new IllegalArgumentException("Unknown input discriminator!");
		}
	}

	public static JsonObject serializeBlockState(BlockState state) {
		return BLOCK_STATE_CODEC.encodeStart(JsonOps.INSTANCE, state).getOrThrow().getAsJsonObject();
	}

	public static BlockState readBlockState(JsonObject object) {
		return BLOCK_STATE_CODEC.parse(JsonOps.INSTANCE, object).getOrThrow(IllegalArgumentException::new);
	}
}
