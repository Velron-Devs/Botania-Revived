package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

import velrondevs.botania.api.block.PetalApothecary;
import velrondevs.botania.api.recipe.ContainerRecipeInput;
import velrondevs.botania.api.recipe.PetalApothecaryRecipe;
import velrondevs.botania.common.block.block_entity.PetalApothecaryBlockEntity;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.List;
import java.util.Locale;

public enum PetalApothecaryProviderData implements IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String REAGENT = "reagent";

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof PetalApothecaryBlockEntity apothecary) || apothecary.isEmpty()) {
			return;
		}
		ContainerRecipeInput input = new ContainerRecipeInput(apothecary.getItemHandler());
		BotaniaRecipeTypes.getRecipeFor(accessor.getLevel(), BotaniaRecipeTypes.PETAL_TYPE, input)
				.map(RecipeHolder::value)
				.ifPresent(recipe -> {
					JadeManaHelper.putStack(data, JadeManaHelper.OUTPUT, accessor, recipe.assemble(input, accessor.getLevel().registryAccess()));
					recipe.getReagent().items().findFirst()
							.ifPresent(item -> JadeManaHelper.putStack(data, REAGENT, accessor, new ItemStack(item)));
				});
	}

	@Override
	public Identifier getUid() {
		return BotaniaJadeIds.PETAL_APOTHECARY;
	}
}
