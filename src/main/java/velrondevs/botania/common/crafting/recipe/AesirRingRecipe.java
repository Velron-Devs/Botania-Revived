package velrondevs.botania.common.crafting.recipe;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
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

import java.util.UUID;

public class AesirRingRecipe extends ShapelessRecipe {
	public static final RecipeSerializer<AesirRingRecipe> SERIALIZER = new Serializer();

	public AesirRingRecipe(CraftingBookCategory category) {
		super("", category, new ItemStack(BotaniaItems.aesirRing), NonNullList.of(Ingredient.EMPTY,
				Ingredient.of(BotaniaItems.thorRing),
				Ingredient.of(BotaniaItems.odinRing),
				Ingredient.of(BotaniaItems.lokiRing)));
	}

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		return super.matches(input, level) && !hasOwnerConflict(input);
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput input, @NotNull HolderLookup.Provider registries) {
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
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements RecipeSerializer<AesirRingRecipe> {
		private static final MapCodec<AesirRingRecipe> CODEC = CraftingBookCategory.CODEC
				.fieldOf("category")
				.orElse(CraftingBookCategory.EQUIPMENT)
				.xmap(AesirRingRecipe::new, AesirRingRecipe::category);
		private static final StreamCodec<RegistryFriendlyByteBuf, AesirRingRecipe> STREAM_CODEC = CraftingBookCategory.STREAM_CODEC
				.<RegistryFriendlyByteBuf>cast()
				.map(AesirRingRecipe::new, AesirRingRecipe::category);

		@Override
		public MapCodec<AesirRingRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, AesirRingRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}
}
