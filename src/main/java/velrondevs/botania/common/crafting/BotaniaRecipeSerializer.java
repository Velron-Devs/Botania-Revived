package velrondevs.botania.common.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public interface BotaniaRecipeSerializer<T extends Recipe<?>> {
	MapCodec<T> codec();

	StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

	default RecipeSerializer<T> toVanilla() {
		return new RecipeSerializer<>(codec(), streamCodec());
	}
}
