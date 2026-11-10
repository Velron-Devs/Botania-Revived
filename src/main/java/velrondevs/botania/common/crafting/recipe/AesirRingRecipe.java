package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import velrondevs.botania.common.item.relic.RingOfLokiItem;
import velrondevs.botania.common.item.relic.RingOfOdinItem;
import velrondevs.botania.common.item.relic.RingOfTheAesirItem;
import velrondevs.botania.common.item.relic.RingOfThorItem;
import velrondevs.botania.registry.BotaniaItems;

import java.util.List;
import java.util.UUID;

public class AesirRingRecipe extends ShapelessRecipe {
	private static final MapCodec<AesirRingRecipe> CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(
					Recipe.CommonInfo.MAP_CODEC.forGetter(r -> r.commonInfo),
					CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(r -> r.bookInfo)
			).apply(i, AesirRingRecipe::new)
	);
	private static final StreamCodec<RegistryFriendlyByteBuf, AesirRingRecipe> STREAM_CODEC = StreamCodec.composite(
			Recipe.CommonInfo.STREAM_CODEC, r -> r.commonInfo,
			CraftingRecipe.CraftingBookInfo.STREAM_CODEC, r -> r.bookInfo,
			AesirRingRecipe::new
	);
	public static final RecipeSerializer<AesirRingRecipe> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

	public AesirRingRecipe(Recipe.CommonInfo commonInfo, CraftingRecipe.CraftingBookInfo bookInfo) {
		super(commonInfo, bookInfo, new ItemStackTemplate(BotaniaItems.aesirRing), List.of(
				Ingredient.of(BotaniaItems.thorRing),
				Ingredient.of(BotaniaItems.odinRing),
				Ingredient.of(BotaniaItems.lokiRing)));
	}

	public AesirRingRecipe(CraftingBookCategory category) {
		this(new Recipe.CommonInfo(true), new CraftingRecipe.CraftingBookInfo(category, ""));
	}

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		return super.matches(input, level) && !hasOwnerConflict(input);
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput input) {
		if (hasOwnerConflict(input)) {
			return ItemStack.EMPTY;
		}
		ItemStack result = new ItemStack(BotaniaItems.aesirRing);
		UUID owner = findOwner(input);
		if (owner != null) {
			RingOfTheAesirItem.makeRelic(result).bindToUUID(owner);
		}
		return result;
	}

	@Nullable
	private static UUID findOwner(CraftingInput input) {
		for (int i = 0; i < input.size(); i++) {
			UUID uuid = getSoulbind(input.getItem(i));
			if (uuid != null) {
				return uuid;
			}
		}
		return null;
	}

	private static boolean hasOwnerConflict(CraftingInput input) {
		UUID owner = findOwner(input);
		if (owner == null) {
			return false;
		}
		for (int i = 0; i < input.size(); i++) {
			UUID uuid = getSoulbind(input.getItem(i));
			if (uuid != null && !owner.equals(uuid)) {
				return true;
			}
		}
		return false;
	}

	@Nullable
	private static UUID getSoulbind(ItemStack stack) {
		if (stack.is(BotaniaItems.thorRing)) {
			return RingOfThorItem.makeRelic(stack).getSoulbindUUID();
		}
		if (stack.is(BotaniaItems.odinRing)) {
			return RingOfOdinItem.makeRelic(stack).getSoulbindUUID();
		}
		if (stack.is(BotaniaItems.lokiRing)) {
			return RingOfLokiItem.makeRelic(stack).getSoulbindUUID();
		}
		return null;
	}

	@NotNull
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return (RecipeSerializer) SERIALIZER;
	}
}
