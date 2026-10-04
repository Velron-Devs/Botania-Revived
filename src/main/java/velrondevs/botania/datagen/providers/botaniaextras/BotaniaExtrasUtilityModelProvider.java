package velrondevs.botania.datagen.providers.botaniaextras;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.HopperBlock;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.datagen.providers.models.BlockstateProvider;
import velrondevs.botania.mixin.TextureSlotAccessor;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.module.botaniaextras.item.CoatOfArmsItem;

import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaExtrasUtilityModelProvider extends BlockstateProvider {
	private static final String DIR = "botania_extras/";
	private static final TextureSlot LAYER1 = TextureSlotAccessor.make("layer1");
	private static final ModelTemplate GENERATED_1 = new ModelTemplate(Optional.of(ResourceLocation.parse("item/generated")), Optional.empty(), TextureSlot.LAYER0, LAYER1);
	private static final ModelTemplate FUNNEL_DOWN = new ModelTemplate(Optional.of(ResourceLocation.parse("block/hopper")), Optional.empty(),
			TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	private static final ModelTemplate FUNNEL_SIDE = new ModelTemplate(Optional.of(ResourceLocation.parse("block/hopper_side")), Optional.empty(),
			TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);

	public BotaniaExtrasUtilityModelProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Extras utility blockstates and models";
	}

	private static ResourceLocation blockTex(String name) {
		return prefix("block/" + DIR + name);
	}

	private static ResourceLocation itemTex(String name) {
		return prefix("item/" + DIR + name);
	}

	private static ResourceLocation itemModel(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem()).withPrefix("item/");
	}

	private void flatItem(ItemLike item, ResourceLocation texture) {
		ModelTemplates.FLAT_ITEM.create(itemModel(item), TextureMapping.layer0(texture), modelOutput);
	}

	@Override
	protected void registerStatesAndModels() {
		ResourceLocation funnelId = BuiltInRegistries.BLOCK.getKey(BotaniaExtrasUtilities.livingwoodFunnel);
		TextureMapping funnelTextures = new TextureMapping()
				.put(TextureSlot.PARTICLE, blockTex("livingwood_funnel_outside"))
				.put(TextureSlot.TOP, blockTex("livingwood_funnel_top"))
				.put(TextureSlot.SIDE, blockTex("livingwood_funnel_outside"))
				.put(TextureSlot.INSIDE, blockTex("livingwood_funnel_inside"));
		ResourceLocation down = FUNNEL_DOWN.create(funnelId.withPrefix("block/"), funnelTextures, modelOutput);
		ResourceLocation side = FUNNEL_SIDE.create(funnelId.withPrefix("block/").withSuffix("_side"), funnelTextures, modelOutput);
		blockstates.add(MultiVariantGenerator.multiVariant(BotaniaExtrasUtilities.livingwoodFunnel).with(
				PropertyDispatch.property(HopperBlock.FACING)
						.select(Direction.DOWN, Variant.variant().with(VariantProperties.MODEL, down))
						.select(Direction.NORTH, Variant.variant().with(VariantProperties.MODEL, side))
						.select(Direction.EAST, Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90))
						.select(Direction.SOUTH, Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
						.select(Direction.WEST, Variant.variant().with(VariantProperties.MODEL, side).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))));
		flatItem(BotaniaExtrasUtilities.livingwoodFunnel, itemTex("livingwood_funnel"));

		flatItem(BotaniaExtrasUtilities.toolbelt, itemTex("toolbelt"));
		GENERATED_1.create(itemModel(BotaniaExtrasUtilities.clericalColorizer), new TextureMapping()
				.put(TextureSlot.LAYER0, itemTex("clerical_colorizer"))
				.put(LAYER1, itemTex("clerical_colorizer_overlay")), modelOutput);
		for (int i = 0; i < CoatOfArmsItem.NAMES.size(); i++) {
			Item coat = BotaniaExtrasUtilities.coats().get(i);
			flatItem(coat, itemTex(CoatOfArmsItem.itemName(i)));
		}
		flatItem(BotaniaExtrasUtilities.wiltedLotus, itemTex("wilted_lotus"));
		flatItem(BotaniaExtrasUtilities.deathlyLotus, itemTex("wilted_lotus"));
		modelOutput.accept(itemModel(BotaniaExtrasUtilities.manasealCreeperSpawnEgg), new DelegatedModel(ResourceLocation.parse("item/template_spawn_egg")));
	}
}
