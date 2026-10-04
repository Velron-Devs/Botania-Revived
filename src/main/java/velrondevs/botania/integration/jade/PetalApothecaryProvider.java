package velrondevs.botania.integration.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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

public enum PetalApothecaryProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
	INSTANCE;

	private static final String REAGENT = "reagent";

	@Override
	public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
		if (!JadeManaHelper.enabled(config) || !(accessor.getBlockEntity() instanceof PetalApothecaryBlockEntity apothecary)) {
			return;
		}
		PetalApothecary.State fluid = apothecary.getFluid();
		tooltip.add(Component.translatable("botania.jade.fluid", Component.translatable("botania.jade.fluid." + fluid.name().toLowerCase(Locale.ROOT))));

		CompoundTag data = accessor.getServerData();
		ItemStack output = JadeManaHelper.getStack(data, JadeManaHelper.OUTPUT, accessor);
		if (!output.isEmpty()) {
			ItemStack reagent = JadeManaHelper.getStack(data, REAGENT, accessor);
			JadeManaHelper.addRecipeRow(tooltip, List.of(reagent), fluid == PetalApothecary.State.WATER ? 1F : 0F, output);
			if (!reagent.isEmpty()) {
				tooltip.add(JadeManaHelper.gray(Component.translatable("botania.jade.reagent", reagent.getHoverName())));
			}
		}
	}

	@Override
	public void appendServerData(CompoundTag data, BlockAccessor accessor) {
		if (!(accessor.getBlockEntity() instanceof PetalApothecaryBlockEntity apothecary) || apothecary.isEmpty()) {
			return;
		}
		ContainerRecipeInput input = new ContainerRecipeInput(apothecary.getItemHandler());
		accessor.getLevel().getRecipeManager().getRecipeFor(BotaniaRecipeTypes.PETAL_TYPE, input, accessor.getLevel())
				.map(RecipeHolder::value)
				.ifPresent(recipe -> {
					JadeManaHelper.putStack(data, JadeManaHelper.OUTPUT, accessor, recipe.assemble(input, accessor.getLevel().registryAccess()));
					ItemStack[] reagents = recipe.getReagent().getItems();
					if (reagents.length > 0) {
						JadeManaHelper.putStack(data, REAGENT, accessor, reagents[0].copyWithCount(1));
					}
				});
	}

	@Override
	public ResourceLocation getUid() {
		return BotaniaJadeIds.PETAL_APOTHECARY;
	}
}
