package velrondevs.botania.datagen.providers.asgard;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.datagen.providers.models.BlockstateProvider;
import velrondevs.botania.module.asgard.AsgardFlowers;

import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class AsgardModelProvider extends BlockstateProvider {
	public AsgardModelProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Asgard blockstates and models";
	}

	private static ResourceLocation blockModel(Block block) {
		return BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/");
	}

	@Override
	protected void registerStatesAndModels() {
		ModelTemplate cross = new ModelTemplate(Optional.of(prefix("block/shapes/cross")), Optional.empty(), TextureSlot.CROSS);
		ResourceLocation texture = prefix("block/asgardandelion");
		ResourceLocation model = cross.create(blockModel(AsgardFlowers.asgardandelion), new TextureMapping().put(TextureSlot.CROSS, texture), modelOutput);
		singleVariantBlockState(AsgardFlowers.asgardandelion, model);
		ModelTemplates.FLAT_ITEM.create(BuiltInRegistries.ITEM.getKey(AsgardFlowers.asgardandelion.asItem()).withPrefix("item/"), TextureMapping.layer0(texture), modelOutput);
		singleVariantBlockState(AsgardFlowers.asgardandelionFloating, blockModel(AsgardFlowers.asgardandelionFloating));
		singleVariantBlockState(AsgardFlowers.asgardandelionPotted, blockModel(AsgardFlowers.asgardandelionPotted));
	}
}
