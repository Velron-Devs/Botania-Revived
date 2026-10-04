package velrondevs.botania.common.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.commands.CacheableFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.api.recipe.StateIngredient;
import velrondevs.botania.registry.BotaniaRecipeTypes;

import java.util.Optional;

public class MarimorphosisRecipe extends OrechidRecipe {
	private final int weightBonus;
	private final TagKey<Biome> biomes;

	public MarimorphosisRecipe(StateIngredient input, StateIngredient output, int weight,
			Optional<CacheableFunction> successFunction,
			int weightBonus, TagKey<Biome> biomes) {
		super(input, output, weight, successFunction);
		this.weightBonus = weightBonus;
		this.biomes = biomes;
	}

	@Override
	public int getWeight(@NotNull Level level, @NotNull BlockPos pos) {
		if (level.getBiome(pos).is(this.biomes)) {
			return getWeight() + weightBonus;
		}
		return getWeight();
	}

	@NotNull
	@Override
	public RecipeType<? extends MarimorphosisRecipe> getType() {
		return BotaniaRecipeTypes.MARIMORPHOSIS_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return BotaniaRecipeTypes.MARIMORPHOSIS_SERIALIZER;
	}

	public static class Serializer implements RecipeSerializer<MarimorphosisRecipe> {
		private static final StreamCodec<RegistryFriendlyByteBuf, TagKey<Biome>> BIOME_TAG_STREAM_CODEC = ResourceLocation.STREAM_CODEC
				.<TagKey<Biome>>map(id -> TagKey.create(Registries.BIOME, id), TagKey::location).cast();
		public static final MapCodec<MarimorphosisRecipe> CODEC = RecordCodecBuilder.<MarimorphosisRecipe>mapCodec(instance -> instance.group(
				StateIngredientHelper.NON_AIR_CODEC.fieldOf("input").forGetter(MarimorphosisRecipe::getInput),
				StateIngredientHelper.NON_AIR_CODEC.fieldOf("output").forGetter(MarimorphosisRecipe::getOutput),
				Codec.INT.fieldOf("weight").forGetter(MarimorphosisRecipe::getWeight),
				CacheableFunction.CODEC.optionalFieldOf("success_function").forGetter(MarimorphosisRecipe::getSuccessFunction),
				Codec.INT.optionalFieldOf("biome_bonus", 0).forGetter(MarimorphosisRecipe::getWeightBonus),
				TagKey.codec(Registries.BIOME).fieldOf("biome_bonus_tag").forGetter(MarimorphosisRecipe::getBiomes)
		).apply(instance, MarimorphosisRecipe::new)).validate(recipe -> recipe.getWeight() + recipe.getWeightBonus() <= 0
				? DataResult.error(() -> "Weight combined with bonus cannot be 0 or less")
				: DataResult.success(recipe));
		public static final StreamCodec<RegistryFriendlyByteBuf, MarimorphosisRecipe> STREAM_CODEC = StreamCodec.composite(
				OrechidRecipe.Serializer.STREAM_CODEC, recipe -> recipe,
				BIOME_TAG_STREAM_CODEC, MarimorphosisRecipe::getBiomes,
				ByteBufCodecs.VAR_INT, MarimorphosisRecipe::getWeightBonus,
				(base, biomes, weightBonus) -> new MarimorphosisRecipe(base.getInput(), base.getOutput(),
						base.getWeight(), base.getSuccessFunction(), weightBonus, biomes));

		@Override
		public MapCodec<MarimorphosisRecipe> codec() {
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, MarimorphosisRecipe> streamCodec() {
			return STREAM_CODEC;
		}
	}

	public int getWeightBonus() {
		return weightBonus;
	}

	public TagKey<Biome> getBiomes() {
		return biomes;
	}
}
