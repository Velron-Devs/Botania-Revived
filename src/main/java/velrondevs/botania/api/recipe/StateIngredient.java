package velrondevs.botania.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import velrondevs.botania.common.crafting.StateIngredientHelper;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public interface StateIngredient extends Predicate<BlockState> {
	@Override
	boolean test(BlockState state);

	BlockState pick(RandomSource random);

	default JsonObject serialize() {
		return StateIngredientHelper.CODEC.encodeStart(JsonOps.INSTANCE, this).getOrThrow().getAsJsonObject();
	}

	void write(FriendlyByteBuf buffer);

	List<ItemStack> getDisplayedStacks();

	default List<Component> descriptionTooltip() {
		return Collections.emptyList();
	}

	List<BlockState> getDisplayed();
}
