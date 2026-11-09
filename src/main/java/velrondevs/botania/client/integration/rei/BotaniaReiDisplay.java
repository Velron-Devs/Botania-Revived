package velrondevs.botania.client.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import org.jetbrains.annotations.Nullable;

import velrondevs.botania.api.recipe.OrechidRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BotaniaReiDisplay extends BasicDisplay {
	public static final MapCodec<BotaniaReiDisplay> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Identifier.CODEC.fieldOf("category").forGetter(d -> d.category.getIdentifier()),
			EntryIngredient.codec().listOf().fieldOf("ingredients").forGetter(BotaniaReiDisplay::ingredients),
			EntryIngredient.codec().listOf().fieldOf("extras").forGetter(BotaniaReiDisplay::extras),
			EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(BotaniaReiDisplay::getOutputEntries),
			Identifier.CODEC.optionalFieldOf("location").forGetter(BotaniaReiDisplay::getDisplayLocation),
			Codec.INT.fieldOf("mana").forGetter(BotaniaReiDisplay::mana)
	).apply(i, (category, ingredients, extras, outputs, location, mana) -> new BotaniaReiDisplay(CategoryIdentifier.of(category),
			ingredients, extras, outputs, location.orElse(null), mana, null)));
	private static final StreamCodec<RegistryFriendlyByteBuf, List<EntryIngredient>> INGREDIENTS_STREAM =
			EntryIngredient.streamCodec().apply(ByteBufCodecs.list());
	public static final StreamCodec<RegistryFriendlyByteBuf, BotaniaReiDisplay> STREAM_CODEC = StreamCodec.of(
			(buf, d) -> {
				Identifier.STREAM_CODEC.encode(buf, d.category.getIdentifier());
				INGREDIENTS_STREAM.encode(buf, d.ingredients);
				INGREDIENTS_STREAM.encode(buf, d.extras);
				INGREDIENTS_STREAM.encode(buf, d.getOutputEntries());
				ByteBufCodecs.optional(Identifier.STREAM_CODEC).encode(buf, d.getDisplayLocation());
				ByteBufCodecs.VAR_INT.encode(buf, d.mana);
			},
			buf -> new BotaniaReiDisplay(CategoryIdentifier.of(Identifier.STREAM_CODEC.decode(buf)),
					INGREDIENTS_STREAM.decode(buf), INGREDIENTS_STREAM.decode(buf), INGREDIENTS_STREAM.decode(buf),
					ByteBufCodecs.optional(Identifier.STREAM_CODEC).decode(buf).orElse(null),
					ByteBufCodecs.VAR_INT.decode(buf), null));
	public static final DisplaySerializer<BotaniaReiDisplay> SERIALIZER = DisplaySerializer.of(CODEC, STREAM_CODEC, false);

	private final CategoryIdentifier<BotaniaReiDisplay> category;
	private final List<EntryIngredient> ingredients;
	private final List<EntryIngredient> extras;
	private final int mana;
	@Nullable
	private final OrechidRecipe orechid;

	public BotaniaReiDisplay(CategoryIdentifier<BotaniaReiDisplay> category, List<EntryIngredient> ingredients,
			List<EntryIngredient> extras, List<EntryIngredient> outputs, @Nullable Identifier id, int mana,
			@Nullable OrechidRecipe orechid) {
		super(concat(ingredients, extras), outputs, Optional.ofNullable(id));
		this.category = category;
		this.ingredients = ingredients;
		this.extras = extras;
		this.mana = mana;
		this.orechid = orechid;
	}

	private static List<EntryIngredient> concat(List<EntryIngredient> first, List<EntryIngredient> second) {
		List<EntryIngredient> all = new ArrayList<>(first);
		all.addAll(second);
		return all;
	}

	@Override
	public DisplaySerializer<? extends BotaniaReiDisplay> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public CategoryIdentifier<?> getCategoryIdentifier() {
		return category;
	}

	public List<EntryIngredient> ingredients() {
		return ingredients;
	}

	public List<EntryIngredient> extras() {
		return extras;
	}

	public EntryIngredient output() {
		return outputs.isEmpty() ? EntryIngredient.empty() : outputs.get(0);
	}

	public int mana() {
		return mana;
	}

	@Nullable
	public OrechidRecipe orechid() {
		return orechid;
	}
}
