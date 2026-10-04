package velrondevs.botania.datagen.providers.recipes;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import velrondevs.botania.api.brew.Brew;
import velrondevs.botania.common.crafting.BotanicalBreweryRecipe;
import velrondevs.botania.registry.BotaniaBrews;
import velrondevs.botania.registry.BotaniaItems;

import java.util.concurrent.CompletableFuture;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BrewProvider extends BotaniaRecipeProvider {
	public BrewProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
		super(packOutput, registries);
	}

	@Override
	public String getName() {
		return "Botania Brew recipes";
	}

	@Override
	protected void buildRecipes(RecipeOutput consumer) {
		brew(consumer, idFor("speed"), BotaniaBrews.speed, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SUGAR), Ingredient.of(Items.REDSTONE));
		brew(consumer, idFor("strength"), BotaniaBrews.strength, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.GLOWSTONE_DUST));
		brew(consumer, idFor("haste"), BotaniaBrews.haste, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SUGAR), Ingredient.of(Items.GOLD_NUGGET));
		brew(consumer, idFor("healing"), BotaniaBrews.healing, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GLISTERING_MELON_SLICE), Ingredient.of(Items.POTATO));
		brew(consumer, idFor("jump_boost"), BotaniaBrews.jumpBoost, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FEATHER), Ingredient.of(Items.CARROT));
		brew(consumer, idFor("regeneration"), BotaniaBrews.regen, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GHAST_TEAR), Ingredient.of(Items.GLOWSTONE_DUST));
		brew(consumer, idFor("weak_regeneration"), BotaniaBrews.regenWeak, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GHAST_TEAR), Ingredient.of(Items.REDSTONE));
		brew(consumer, idFor("resistance"), BotaniaBrews.resistance, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.LEATHER));
		brew(consumer, idFor("fire_resistance"), BotaniaBrews.fireResistance, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.MAGMA_CREAM), Ingredient.of(Blocks.NETHERRACK));
		brew(consumer, idFor("water_breathing"), BotaniaBrews.waterBreathing, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.PRISMARINE_CRYSTALS), Ingredient.of(Items.GLOWSTONE_DUST));
		brew(consumer, idFor("invisibility"), BotaniaBrews.invisibility, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SNOWBALL), Ingredient.of(Items.GLOWSTONE_DUST));
		brew(consumer, idFor("night_vision"), BotaniaBrews.nightVision, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.SPIDER_EYE), Ingredient.of(Items.GOLDEN_CARROT));
		brew(consumer, idFor("absorption"), BotaniaBrews.absorption, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GOLDEN_APPLE), Ingredient.of(Items.POTATO));

		brew(consumer, idFor("overload"), BotaniaBrews.overload, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.BLAZE_POWDER), Ingredient.of(Items.SUGAR), Ingredient.of(Items.GLOWSTONE_DUST), Ingredient.of(BotaniaItems.manaSteel), Ingredient.of(Items.SPIDER_EYE));
		brew(consumer, idFor("soul_cross"), BotaniaBrews.soulCross, Ingredient.of(Items.NETHER_WART), Ingredient.of(Blocks.SOUL_SAND), Ingredient.of(Items.PAPER), Ingredient.of(Items.APPLE), Ingredient.of(Items.BONE));
		brew(consumer, idFor("feather_feet"), BotaniaBrews.featherfeet, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FEATHER), Ingredient.of(Items.LEATHER), Ingredient.of(ItemTags.WOOL));
		brew(consumer, idFor("emptiness"), BotaniaBrews.emptiness, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.GUNPOWDER), Ingredient.of(Items.ROTTEN_FLESH), Ingredient.of(Items.BONE), Ingredient.of(Items.STRING), Ingredient.of(Items.ENDER_PEARL));
		brew(consumer, idFor("bloodthirst"), BotaniaBrews.bloodthirst, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.FERMENTED_SPIDER_EYE), Ingredient.of(Items.LAPIS_LAZULI), Ingredient.of(Items.FIRE_CHARGE), Ingredient.of(Items.IRON_INGOT));
		brew(consumer, idFor("allure"), BotaniaBrews.allure, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.COD), Ingredient.of(Items.QUARTZ), Ingredient.of(Items.GOLDEN_CARROT));
		brew(consumer, idFor("clear"), BotaniaBrews.clear, Ingredient.of(Items.NETHER_WART), Ingredient.of(Items.QUARTZ), Ingredient.of(Items.EMERALD), Ingredient.of(Items.MELON_SLICE));
	}

	private static ResourceLocation idFor(String s) {
		return prefix("brew/" + s);
	}

	private static void brew(RecipeOutput consumer, ResourceLocation id, Brew brew, Ingredient... inputs) {
		consumer.accept(id, new BotanicalBreweryRecipe(brew, inputs), null);
	}
}
