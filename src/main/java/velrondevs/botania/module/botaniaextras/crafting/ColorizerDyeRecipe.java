package velrondevs.botania.module.botaniaextras.crafting;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.module.botaniaextras.item.ClericalColorizerItem;

import java.util.ArrayList;
import java.util.List;

public class ColorizerDyeRecipe extends CustomRecipe {
	public static final ColorizerDyeRecipe INSTANCE = new ColorizerDyeRecipe();
	public static final RecipeSerializer<ColorizerDyeRecipe> SERIALIZER = new RecipeSerializer<>(MapCodec.unit(INSTANCE), StreamCodec.unit(INSTANCE));

	public ColorizerDyeRecipe() {}

	@Override
	public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
		ItemStack colorizer = ItemStack.EMPTY;
		int dyes = 0;
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			if (stack.isEmpty()) {
				continue;
			}
			if (stack.getItem() instanceof ClericalColorizerItem) {
				if (!colorizer.isEmpty()) {
					return false;
				}
				colorizer = stack;
			} else if (stack.get(DataComponents.DYE) != null) {
				dyes++;
			} else {
				return false;
			}
		}
		return !colorizer.isEmpty() && (dyes > 0 || ClericalColorizerItem.hasColor(colorizer));
	}

	@NotNull
	@Override
	public ItemStack assemble(@NotNull CraftingInput input) {
		ItemStack colorizer = ItemStack.EMPTY;
		List<DyeColor> dyes = new ArrayList<>();
		for (int i = 0; i < input.size(); i++) {
			ItemStack stack = input.getItem(i);
			DyeColor dye = stack.get(DataComponents.DYE);
			if (stack.getItem() instanceof ClericalColorizerItem) {
				colorizer = stack;
			} else if (dye != null) {
				dyes.add(dye);
			}
		}
		if (colorizer.isEmpty()) {
			return ItemStack.EMPTY;
		}
		if (dyes.isEmpty()) {
			ItemStack cleared = colorizer.copyWithCount(1);
			cleared.remove(DataComponents.DYED_COLOR);
			return cleared;
		}
		return DyedItemColor.applyDyes(colorizer, dyes);
	}

	@NotNull
	@Override
	public RecipeSerializer<ColorizerDyeRecipe> getSerializer() {
		return SERIALIZER;
	}
}
