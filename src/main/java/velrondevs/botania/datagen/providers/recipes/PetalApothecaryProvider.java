package velrondevs.botania.datagen.providers.recipes;

import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import velrondevs.botania.common.crafting.PetalsRecipe;
import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaBlocks;
import velrondevs.botania.registry.BotaniaFlowerBlocks;
import velrondevs.botania.registry.BotaniaItems;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class PetalApothecaryProvider extends BotaniaRecipeProvider {
	private static final Ingredient DEFAULT_REAGENT = Ingredient.of(BotaniaTags.Items.SEED_APOTHECARY_REAGENT);
	private static final int WITHER_MANA_ROSE_BLACK_PETALS = 4;
	private static final int WITHER_MANA_ROSE_NETHER_STARS = 4;
	private static final int WITHER_MANA_ROSE_WITHER_ROSES = 4;

	public PetalApothecaryProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania petal apothecary recipes";
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		Ingredient white = tagIngr("petals/white");
		Ingredient orange = tagIngr("petals/orange");
		Ingredient magenta = tagIngr("petals/magenta");
		Ingredient lightBlue = tagIngr("petals/light_blue");
		Ingredient yellow = tagIngr("petals/yellow");
		Ingredient lime = tagIngr("petals/lime");
		Ingredient pink = tagIngr("petals/pink");
		Ingredient gray = tagIngr("petals/gray");
		Ingredient lightGray = tagIngr("petals/light_gray");
		Ingredient cyan = tagIngr("petals/cyan");
		Ingredient purple = tagIngr("petals/purple");
		Ingredient blue = tagIngr("petals/blue");
		Ingredient brown = tagIngr("petals/brown");
		Ingredient green = tagIngr("petals/green");
		Ingredient red = tagIngr("petals/red");
		Ingredient black = tagIngr("petals/black");
		Ingredient runeWater = Ingredient.of(BotaniaItems.runeWater);
		Ingredient runeFire = Ingredient.of(BotaniaItems.runeFire);
		Ingredient runeEarth = Ingredient.of(BotaniaItems.runeEarth);
		Ingredient runeAir = Ingredient.of(BotaniaItems.runeAir);
		Ingredient runeSpring = Ingredient.of(BotaniaItems.runeSpring);
		Ingredient runeSummer = Ingredient.of(BotaniaItems.runeSummer);
		Ingredient runeAutumn = Ingredient.of(BotaniaItems.runeAutumn);
		Ingredient runeWinter = Ingredient.of(BotaniaItems.runeWinter);
		Ingredient runeMana = Ingredient.of(BotaniaItems.runeMana);
		Ingredient runeLust = Ingredient.of(BotaniaItems.runeLust);
		Ingredient runeGluttony = Ingredient.of(BotaniaItems.runeGluttony);
		Ingredient runeGreed = Ingredient.of(BotaniaItems.runeGreed);
		Ingredient runeSloth = Ingredient.of(BotaniaItems.runeSloth);
		Ingredient runeWrath = Ingredient.of(BotaniaItems.runeWrath);
		Ingredient runeEnvy = Ingredient.of(BotaniaItems.runeEnvy);
		Ingredient runePride = Ingredient.of(BotaniaItems.runePride);

		Ingredient redstoneRoot = Ingredient.of(BotaniaItems.redstoneRoot);
		Ingredient pixieDust = Ingredient.of(BotaniaItems.pixieDust);
		Ingredient gaiaSpirit = Ingredient.of(BotaniaItems.lifeEssence);

		make(consumer, BotaniaFlowerBlocks.pureDaisy, white, white, white, white);
		make(consumer, BotaniaFlowerBlocks.manastar, lightBlue, green, red, cyan);

		make(consumer, BotaniaFlowerBlocks.endoflame, brown, brown, red, lightGray);
		make(consumer, BotaniaFlowerBlocks.hydroangeas, blue, blue, cyan, cyan);
		make(consumer, BotaniaFlowerBlocks.thermalily, red, orange, orange, runeEarth, runeFire);
		make(consumer, BotaniaFlowerBlocks.rosaArcana, pink, pink, purple, purple, lime, runeMana);
		make(consumer, BotaniaFlowerBlocks.munchdew, lime, lime, red, red, green, runeGluttony);
		make(consumer, BotaniaFlowerBlocks.entropinnyum, red, red, gray, gray, white, white, runeWrath, runeFire);
		make(consumer, BotaniaFlowerBlocks.kekimurus, white, white, orange, orange, brown, brown, runeGluttony, pixieDust);
		make(consumer, BotaniaFlowerBlocks.gourmaryllis, lightGray, lightGray, yellow, yellow, red, runeFire, runeSummer);
		make(consumer, BotaniaFlowerBlocks.narslimmus, lime, lime, green, green, black, runeSummer, runeWater);
		make(consumer, BotaniaFlowerBlocks.spectrolus, red, red, green, green, blue, blue, white, white, runeWinter, runeAir, pixieDust);
		make(consumer, BotaniaFlowerBlocks.rafflowsia, purple, purple, green, green, black, runeEarth, runePride, pixieDust);
		make(consumer, BotaniaFlowerBlocks.shulkMeNot, purple, purple, magenta, magenta, lightGray, gaiaSpirit, runeEnvy, runeWrath);
		make(consumer, BotaniaFlowerBlocks.dandelifeon, purple, purple, lime, green, runeWater, runeFire, runeEarth, runeAir, redstoneRoot, gaiaSpirit);

		make(consumer, BotaniaFlowerBlocks.jadedAmaranthus, purple, lime, green, runeSpring, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.bellethorn, red, red, red, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.dreadthorn, black, black, black, cyan, cyan, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.heiseiDream, magenta, magenta, purple, pink, runeWrath, pixieDust);
		make(consumer, BotaniaFlowerBlocks.tigerseye, yellow, brown, orange, lime, runeAutumn);

		GogAlternationResult.Captured base = create(BotaniaFlowerBlocks.orechid, gray, gray, yellow, green, red, runePride, runeGreed, redstoneRoot, pixieDust);
		GogAlternationResult.Captured gog = create(BotaniaFlowerBlocks.orechid, gray, gray, yellow, yellow, green, green, red, red);
		GogAlternationResult.accept(consumer, gog, base);

		make(consumer, BotaniaFlowerBlocks.orechidIgnem, red, red, white, white, pink, runePride, runeGreed, redstoneRoot, pixieDust);
		make(consumer, BotaniaFlowerBlocks.fallenKanade, white, white, yellow, yellow, orange, runeSpring);
		make(consumer, BotaniaFlowerBlocks.exoflame, red, red, gray, lightGray, runeFire, runeSummer);
		make(consumer, BotaniaFlowerBlocks.agricarnation, lime, lime, green, yellow, runeSpring, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.hopperhock, gray, gray, lightGray, lightGray, runeAir, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.tangleberrie, cyan, cyan, gray, lightGray, runeAir, runeEarth);
		make(consumer, BotaniaFlowerBlocks.jiyuulia, pink, pink, purple, lightGray, runeWater, runeAir);
		make(consumer, BotaniaFlowerBlocks.rannuncarpus, orange, orange, yellow, runeEarth, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.hyacidus, purple, purple, magenta, magenta, green, runeWater, runeAutumn, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.pollidisiac, red, red, pink, pink, orange, runeLust, runeFire);
		make(consumer, BotaniaFlowerBlocks.clayconia, lightGray, lightGray, gray, cyan, runeEarth);
		make(consumer, BotaniaFlowerBlocks.loonium, green, green, green, green, gray, runeSloth, runeGluttony, runeEnvy, redstoneRoot, pixieDust);
		make(consumer, BotaniaFlowerBlocks.daffomill, white, white, brown, yellow, runeAir, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.vinculotus, black, black, purple, purple, green, runeWater, runeSloth, runeLust, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.spectranthemum, white, white, lightGray, lightGray, cyan, runeEnvy, runeWater, redstoneRoot, pixieDust);
		make(consumer, BotaniaFlowerBlocks.medumone, brown, brown, gray, gray, runeEarth, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.marimorphosis, gray, yellow, green, red, runeEarth, runeFire, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.bubbell, cyan, cyan, lightBlue, lightBlue, blue, blue, runeWater, runeSummer, pixieDust);
		make(consumer, BotaniaFlowerBlocks.solegnolia, brown, brown, red, blue, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.bergamute, orange, green, green, redstoneRoot);
		make(consumer, BotaniaFlowerBlocks.labellia, yellow, yellow, blue, white, black, runeAutumn, redstoneRoot, pixieDust);
		make(consumer, BotaniaItems.unactivatedWitherManaRose, witherManaRoseInputs(black));

		make(consumer, BotaniaBlocks.motifDaybloom, yellow, yellow, orange, lightBlue);
		make(consumer, BotaniaBlocks.motifNightshade, black, black, purple, gray);

		ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
		stack.set(DataComponents.PROFILE, new ResolvableProfile(Optional.of("Vazkii"), Optional.empty(), new PropertyMap()));
		Ingredient[] inputs = new Ingredient[16];
		Arrays.fill(inputs, pink);
		consumer.accept(idFor(prefix("vazkii_head")), new PetalsRecipe(stack, DEFAULT_REAGENT, inputs), null);
	}

	protected static Ingredient tagIngr(String tag) {
		return Ingredient.of(TagKey.create(Registries.ITEM, prefix(tag)));
	}

	private static Ingredient[] witherManaRoseInputs(Ingredient black) {
		Ingredient[] inputs = new Ingredient[WITHER_MANA_ROSE_BLACK_PETALS + WITHER_MANA_ROSE_NETHER_STARS + WITHER_MANA_ROSE_WITHER_ROSES];
		int i = 0;
		for (int n = 0; n < WITHER_MANA_ROSE_BLACK_PETALS; n++) {
			inputs[i++] = black;
		}
		for (int n = 0; n < WITHER_MANA_ROSE_NETHER_STARS; n++) {
			inputs[i++] = Ingredient.of(Items.NETHER_STAR);
		}
		for (int n = 0; n < WITHER_MANA_ROSE_WITHER_ROSES; n++) {
			inputs[i++] = Ingredient.of(Items.WITHER_ROSE);
		}
		return inputs;
	}

	protected static void make(RecipeOutput consumer, ItemLike item, Ingredient... ingredients) {
		GogAlternationResult.Captured recipe = create(item, ingredients);
		consumer.accept(recipe.id(), recipe.recipe(), null);
	}

	protected static GogAlternationResult.Captured create(ItemLike item, Ingredient... ingredients) {
		return new GogAlternationResult.Captured(idFor(BuiltInRegistries.ITEM.getKey(item.asItem())),
				new PetalsRecipe(new ItemStack(item), DEFAULT_REAGENT, ingredients), null);
	}

	protected static ResourceLocation idFor(ResourceLocation name) {
		return ResourceLocation.fromNamespaceAndPath(name.getNamespace(), "petal_apothecary/" + name.getPath());
	}
}
