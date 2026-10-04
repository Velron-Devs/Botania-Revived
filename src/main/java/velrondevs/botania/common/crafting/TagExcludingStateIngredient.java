package velrondevs.botania.common.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.StateIngredient;

import java.util.Collection;
import java.util.List;

public class TagExcludingStateIngredient extends TagStateIngredient {
	private final List<StateIngredient> excludes;

	public TagExcludingStateIngredient(ResourceLocation id, Collection<StateIngredient> excludes) {
		super(id);
		this.excludes = List.copyOf(excludes);
	}

	@Override
	public boolean test(BlockState state) {
		if (!super.test(state)) {
			return false;
		}
		return isNotExcluded(state);
	}

	public List<StateIngredient> getExcludes() {
		return excludes;
	}

	private boolean isNotExcluded(BlockState state) {
		for (StateIngredient exclude : excludes) {
			if (exclude.test(state)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && this.excludes.equals(((TagExcludingStateIngredient) o).excludes);
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public List<ItemStack> getDisplayedStacks() {
		return getBlocks().stream()
				.filter(b -> b.asItem() != Items.AIR)
				.map(ItemStack::new)
				.toList();
	}

	@NotNull
	@Override
	public List<Block> getBlocks() {
		return super.getBlocks().stream()
				.filter(b -> isNotExcluded(b.defaultBlockState()))
				.toList();
	}

	@Override
	public List<BlockState> getDisplayed() {
		return super.getDisplayed().stream()
				.filter(this::isNotExcluded)
				.toList();
	}
}
