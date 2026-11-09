package velrondevs.botania.datagen.providers.botaniaextras;

import com.mojang.math.Quadrant;

import net.minecraft.client.color.item.Dye;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.HopperBlock;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.datagen.providers.models.BlockstateProvider;
import velrondevs.botania.datagen.util.VariantBuilder;
import velrondevs.botania.module.botaniaextras.BotaniaExtrasUtilities;
import velrondevs.botania.module.botaniaextras.item.ClericalColorizerItem;
import velrondevs.botania.module.botaniaextras.item.CoatOfArmsItem;

import java.util.List;
import java.util.Optional;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaExtrasUtilityModelProvider extends BlockstateProvider {
	private static final String DIR = "botania_extras/";
	private static final TextureSlot LAYER1 = TextureSlot.LAYER1;
	private static final ModelTemplate GENERATED_1 = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("item/generated")), Optional.empty(), TextureSlot.LAYER0, LAYER1);
	private static final ModelTemplate FUNNEL_DOWN = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("block/hopper")), Optional.empty(),
			TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);
	private static final ModelTemplate FUNNEL_SIDE = new ModelTemplate(Optional.of(Identifier.withDefaultNamespace("block/hopper_side")), Optional.empty(),
			TextureSlot.PARTICLE, TextureSlot.TOP, TextureSlot.SIDE, TextureSlot.INSIDE);

	public BotaniaExtrasUtilityModelProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@NotNull
	@Override
	public String getName() {
		return "Botania Extras utility blockstates and models";
	}

	private static Material blockTex(String name) {
		return texture(prefix("block/" + DIR + name));
	}

	private static Material itemTex(String name) {
		return texture(prefix("item/" + DIR + name));
	}

	private static Identifier itemModel(ItemLike item) {
		return BuiltInRegistries.ITEM.getKey(item.asItem()).withPrefix("item/");
	}

	private void flatItem(ItemLike item, Material texture) {
		ModelTemplates.FLAT_ITEM.create(itemModel(item), TextureMapping.layer0(texture), modelOutput);
	}

	@Override
	protected List<ItemTintSource> itemTints(Item item) {
		if (item == BotaniaExtrasUtilities.clericalColorizer) {
			return List.of(new Dye(ARGB.opaque(ClericalColorizerItem.DEFAULT_COLOR)));
		}
		return List.of();
	}

	@Override
	protected void registerStatesAndModels() {
		Identifier funnelId = BuiltInRegistries.BLOCK.getKey(BotaniaExtrasUtilities.livingwoodFunnel);
		TextureMapping funnelTextures = new TextureMapping()
				.put(TextureSlot.PARTICLE, blockTex("livingwood_funnel_outside"))
				.put(TextureSlot.TOP, blockTex("livingwood_funnel_top"))
				.put(TextureSlot.SIDE, blockTex("livingwood_funnel_outside"))
				.put(TextureSlot.INSIDE, blockTex("livingwood_funnel_inside"));
		Identifier down = FUNNEL_DOWN.create(funnelId.withPrefix("block/"), funnelTextures, modelOutput);
		Identifier side = FUNNEL_SIDE.create(funnelId.withPrefix("block/").withSuffix("_side"), funnelTextures, modelOutput);
		blockstates.add(MultiVariantGenerator.dispatch(BotaniaExtrasUtilities.livingwoodFunnel).with(
				PropertyDispatch.initial(HopperBlock.FACING)
						.select(Direction.DOWN, VariantBuilder.multi(VariantBuilder.of(down)))
						.select(Direction.NORTH, VariantBuilder.multi(VariantBuilder.of(side)))
						.select(Direction.EAST, VariantBuilder.multi(VariantBuilder.of(side).yRot(Quadrant.R90)))
						.select(Direction.SOUTH, VariantBuilder.multi(VariantBuilder.of(side).yRot(Quadrant.R180)))
						.select(Direction.WEST, VariantBuilder.multi(VariantBuilder.of(side).yRot(Quadrant.R270)))));
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
		flatItem(BotaniaExtrasUtilities.manasealCreeperSpawnEgg, texture(Identifier.withDefaultNamespace("item/creeper_spawn_egg")));
	}
}
