package velrondevs.botania.datagen.providers.tags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.Tags;

import velrondevs.botania.common.lib.BotaniaTags;
import velrondevs.botania.registry.BotaniaEntities;

import java.util.concurrent.CompletableFuture;

public class EntityTagProvider extends IntrinsicHolderTagsProvider<EntityType<?>> {
	public EntityTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(packOutput, Registries.ENTITY_TYPE, lookupProvider, (entityType) -> entityType.builtInRegistryHolder().key());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		tag(BotaniaTags.Entities.COCOON_COMMON).add(
				EntityType.PIG, EntityType.COW, EntityType.CHICKEN, EntityType.RABBIT, EntityType.SHEEP
		);
		tag(BotaniaTags.Entities.COCOON_RARE)
				.add(EntityType.HORSE, EntityType.DONKEY, EntityType.WOLF, EntityType.OCELOT,
						EntityType.CAT, EntityType.PARROT, EntityType.LLAMA, EntityType.FOX,
						EntityType.PANDA, EntityType.TURTLE, EntityType.GOAT, EntityType.CAMEL);

		tag(BotaniaTags.Entities.COCOON_COMMON_AQUATIC)
				.add(EntityType.COD, EntityType.SALMON, EntityType.TROPICAL_FISH, EntityType.PUFFERFISH, EntityType.SQUID, EntityType.FROG)
				.addOptional(ResourceLocation.fromNamespaceAndPath("quark", "crab"));
		tag(BotaniaTags.Entities.COCOON_RARE_AQUATIC).add(EntityType.DOLPHIN, EntityType.GLOW_SQUID, EntityType.AXOLOTL);

		tag(BotaniaTags.Entities.DRUM_MILKABLE).add(EntityType.COW, EntityType.MOOSHROOM, EntityType.GOAT);
		tag(BotaniaTags.Entities.DRUM_NO_SHEARING).add(EntityType.MOOSHROOM);

		tag(BotaniaTags.Entities.SHADED_MESA_BLACKLIST).add(EntityType.ENDER_DRAGON, EntityType.WITHER,
				EntityType.ITEM_FRAME, EntityType.GLOW_ITEM_FRAME, EntityType.END_CRYSTAL, EntityType.PAINTING,
				EntityType.COMMAND_BLOCK_MINECART, EntityType.MARKER, EntityType.AREA_EFFECT_CLOUD,
				EntityType.EVOKER_FANGS, EntityType.LEASH_KNOT,
				BotaniaEntities.CORPOREA_SPARK, BotaniaEntities.DOPPLEGANGER, BotaniaEntities.FLAME_RING, BotaniaEntities.MAGIC_LANDMINE,
				BotaniaEntities.MAGIC_MISSILE, BotaniaEntities.MANA_BURST, BotaniaEntities.PINK_WITHER, BotaniaEntities.SPARK, BotaniaEntities.PLAYER_MOVER);

		tag(BotaniaTags.Entities.KEY_IMMUNE).add(EntityType.ITEM, EntityType.ITEM_FRAME, EntityType.PAINTING, EntityType.EXPERIENCE_ORB);

		tag(Tags.EntityTypes.BOSSES).add(BotaniaEntities.DOPPLEGANGER);
		tag(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("neoforge", "bosses"))).add(BotaniaEntities.DOPPLEGANGER);
	}
}
