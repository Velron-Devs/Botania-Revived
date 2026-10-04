package velrondevs.botania.datagen.providers.sounds;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition.SoundType;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

import velrondevs.botania.common.lib.LibMisc;

import static net.neoforged.neoforge.common.data.SoundDefinition.Sound.sound;
import static net.neoforged.neoforge.common.data.SoundDefinition.definition;
import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaSoundDefinitionsProvider extends SoundDefinitionsProvider {
	public BotaniaSoundDefinitionsProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, LibMisc.MOD_ID, helper);
	}

	@Override
	public void registerSounds() {
		add("agricarnation", definition().with(sound(prefix("agricarnation"), SoundType.SOUND).volume(0.01))
				.subtitle("botania.subtitle.agricarnation"));
		add("air_rod", definition().with(sound(prefix("airrod"), SoundType.SOUND).volume(0.1).pitch(0.25))
				.subtitle("botania.subtitle.airRod"));
		add("altar_craft", definition().with(sound(prefix("altarcraft"), SoundType.SOUND))
				.subtitle("botania.subtitle.altarCraft"));
		add("arcane_rose_disenchant", definition().with(sound(ResourceLocation.parse("minecraft:block.grindstone.use"), SoundType.EVENT).volume(0.3))
				.subtitle("botania.subtitle.arcane_rose_disenchant"));
		add("astrolabe_configure", definition().with(sound(ResourceLocation.parse("minecraft:entity.experience_orb.pickup"), SoundType.EVENT).volume(0.5))
				.subtitle("botania.subtitle.astrolabeConfigure"));
		add("babylon_attack", definition().with(sound(prefix("babylonattack"), SoundType.SOUND))
				.subtitle("botania.subtitle.babylonAttack"));
		add("babylon_spawn", definition().with(sound(prefix("babylonspawn"), SoundType.SOUND))
				.subtitle("botania.subtitle.babylonSpawn"));
		add("bellows", definition().with(sound(prefix("bellows"), SoundType.SOUND).volume(0.1).pitch(3.0))
				.subtitle("botania.subtitle.bellows"));
		add("bifrost_rod", definition().with(sound(prefix("bifrostrod"), SoundType.SOUND).volume(0.5).pitch(0.25))
				.subtitle("botania.subtitle.bifrostRod"));
		add("black_hole_talisman_configure", definition().with(sound(ResourceLocation.parse("minecraft:entity.experience_orb.pickup"), SoundType.EVENT).volume(0.3).pitch(0.1))
				.subtitle("botania.subtitle.blackHoleTalismanConfigure"));
		add("black_lotus", definition().with(sound(prefix("blacklotus"), SoundType.SOUND).volume(0.5))
				.subtitle("botania.subtitle.blackLotus"));
		add("dash", definition().with(sound(prefix("dash"), SoundType.SOUND))
				.subtitle("botania.subtitle.dash"));
		add("dice_of_fate", definition().with(sound(ResourceLocation.parse("minecraft:entity.arrow.shoot"), SoundType.EVENT).volume(0.5))
				.subtitle("botania.subtitle.diceOfFate"));
		add("ding", definition().with(sound(prefix("ding"), SoundType.SOUND))
				.subtitle("botania.subtitle.ding"));
		add("diva_charm", definition().with(sound(prefix("divacharm"), SoundType.SOUND))
				.subtitle("botania.subtitle.divaCharm"));
		add("divination_rod", definition().with(sound(prefix("divinationrod"), SoundType.SOUND))
				.subtitle("botania.subtitle.divinationRod"));
		add("doit", definition().with(sound(prefix("doit"), SoundType.SOUND))
				.subtitle("botania.subtitle.doit"));
		add("drum", definition().with(sound(ResourceLocation.parse("minecraft:block.note_block.basedrum"), SoundType.EVENT))
				.subtitle("botania.subtitle.drum"));
		add("enchanter_enchant", definition().with(sound(prefix("enchanterenchant"), SoundType.SOUND))
				.subtitle("botania.subtitle.enchanterEnchant"));
		add("enchanter_fade", definition().with(sound(prefix("enchanterblock"), SoundType.SOUND).volume(0.5).pitch(10.0))
				.subtitle("botania.subtitle.enchanterFade"));
		add("enchanter_form", definition().with(sound(prefix("enchanterblock"), SoundType.SOUND).volume(0.5).pitch(0.6))
				.subtitle("botania.subtitle.enchanterForm"));
		add("ender_air_throw", definition().with(sound(ResourceLocation.parse("minecraft:entity.arrow.shoot"), SoundType.EVENT).volume(0.5))
				.subtitle("botania.subtitle.enderAirThrow"));
		add("endoflame", definition().with(sound(prefix("endoflame"), SoundType.SOUND).volume(0.2))
				.subtitle("botania.subtitle.endoflame"));
		add("entropinnyum_angry", definition().with(sound(ResourceLocation.parse("minecraft:entity.generic.extinguish_fire"), SoundType.EVENT).volume(0.2))
				.subtitle("botania.subtitle.entropinnyumAngry"));
		add("entropinnyum_happy", definition().with(sound(ResourceLocation.parse("minecraft:entity.generic.explode"), SoundType.EVENT).volume(0.2))
				.subtitle("botania.subtitle.entropinnyumHappy"));
		add("equip_bauble", definition().with(sound(prefix("equipbauble"), SoundType.SOUND))
				.subtitle("botania.subtitle.equipBauble"));
		add("equip_elementium", definition().with(sound(ResourceLocation.parse("minecraft:item.armor.equip_iron"), SoundType.EVENT))
				.subtitle("botania.subtitle.equipElementium"));
		add("equip_manasteel", definition().with(sound(ResourceLocation.parse("minecraft:item.armor.equip_iron"), SoundType.EVENT))
				.subtitle("botania.subtitle.equipManasteel"));
		add("equip_manaweave", definition().with(sound(ResourceLocation.parse("minecraft:item.armor.equip_leather"), SoundType.EVENT))
				.subtitle("botania.subtitle.equipManaweave"));
		add("equip_terrasteel", definition().with(sound(ResourceLocation.parse("minecraft:item.armor.equip_diamond"), SoundType.EVENT))
				.subtitle("botania.subtitle.equipTerrasteel"));
		add("fire_rod", definition().with(sound(ResourceLocation.parse("minecraft:entity.blaze.ambient"), SoundType.EVENT))
				.subtitle("botania.subtitle.fireRod"));
		add("flare_chakram_throw", definition().with(sound(ResourceLocation.parse("minecraft:entity.arrow.shoot"), SoundType.EVENT).volume(0.5))
				.subtitle("botania.subtitle.flareChakramThrow"));
		add("flugel_eye_bind", definition().with(sound(ResourceLocation.parse("minecraft:entity.enderman.teleport"), SoundType.EVENT).pitch(5.0))
				.subtitle("botania.subtitle.flugelEyeBind"));
		add("flugel_eye_teleport", definition().with(sound(ResourceLocation.parse("minecraft:entity.enderman.teleport"), SoundType.EVENT))
				.subtitle("botania.subtitle.flugelEyeTeleport"));
		add("gaia_death", definition().with(sound(ResourceLocation.parse("minecraft:entity.generic.explode"), SoundType.EVENT).volume(20.0))
				.subtitle("botania.subtitle.gaiaDeath"));
		add("gaia_summon", definition().with(sound(ResourceLocation.parse("minecraft:entity.ender_dragon.growl"), SoundType.EVENT).volume(10.0).pitch(0.1))
				.subtitle("botania.subtitle.gaiaSummon"));
		add("gaia_teleport", definition().with(sound(ResourceLocation.parse("minecraft:entity.enderman.teleport"), SoundType.EVENT))
				.subtitle("botania.subtitle.gaiaTeleport"));
		add("gaia_trap", definition().with(sound(prefix("gaiatrap"), SoundType.SOUND).volume(0.3))
				.subtitle("botania.subtitle.gaiaTrap"));
		add("holy_cloak", definition().with(sound(prefix("holycloak"), SoundType.SOUND))
				.subtitle("botania.subtitle.holyCloak"));
		add("horn_doot", definition().with(sound(ResourceLocation.parse("minecraft:block.note_block.bass"), SoundType.EVENT).pitch(0.001))
				.subtitle("botania.subtitle.hornDoot"));
		add("incense_plate_ignite", definition().with(sound(prefix("spreaderfire"), SoundType.SOUND))
				.subtitle("botania.subtitle.incensePlateIgnite"));
		add("labellia", definition().with(sound(ResourceLocation.parse("minecraft:item.book.page_turn"), SoundType.EVENT))
				.subtitle("botania.subtitle.labellia"));
		add("laputa_start", definition().with(sound(prefix("laputastart"), SoundType.SOUND))
				.subtitle("botania.subtitle.laputaStart"));
		add("lexicon_open", definition().with(sound(prefix("lexiconopen"), SoundType.SOUND))
				.subtitle("botania.subtitle.lexiconOpen"));
		add("lexicon_page", definition().with(sound(prefix("lexiconpage"), SoundType.SOUND))
				.subtitle("botania.subtitle.lexiconPage"));
		add("light_relay", definition().with(sound(prefix("lightrelay"), SoundType.SOUND).volume(0.2))
				.subtitle("botania.subtitle.lightRelay"));
		add("mana_blaster", definition().with(sound(prefix("manablaster"), SoundType.SOUND).volume(0.6))
				.subtitle("botania.subtitle.manaBlaster"));
		add("mana_blaster_cycle", definition().with(sound(ResourceLocation.parse("minecraft:block.stone_button.click_on"), SoundType.EVENT))
				.subtitle("botania.subtitle.manaBlasterCycle"));
		add("mana_blaster_misfire", definition().with(sound(ResourceLocation.parse("minecraft:block.lever.click"), SoundType.EVENT))
				.subtitle("botania.subtitle.manaBlasterMisfire"));
		add("mana_pool_craft", definition().with(sound(prefix("manapoolcraft"), SoundType.SOUND).volume(0.4).pitch(4.0))
				.subtitle("botania.subtitle.manaPoolCraft"));
		add("missile", definition().with(sound(prefix("missile"), SoundType.SOUND).volume(0.6))
				.subtitle("botania.subtitle.missile"));
		add("missile_funny", definition().with(sound(prefix("missile"), SoundType.EVENT))
				.subtitle("botania.subtitle.missileFunny"));
		add("music.gaia1", definition().with(sound(prefix("music/endureemptiness"), SoundType.SOUND).stream()));
		add("music.gaia2", definition().with(sound(prefix("music/fightforquiescence"), SoundType.SOUND).stream()));
		add("narslimmus_eat_big", definition().with(sound(ResourceLocation.parse("minecraft:entity.slime.squish"), SoundType.EVENT).pitch(0.02))
				.subtitle("botania.subtitle.narslimmusEat"));
		add("narslimmus_eat_small", definition().with(sound(ResourceLocation.parse("minecraft:entity.slime.squish_small"), SoundType.EVENT).pitch(0.02))
				.subtitle("botania.subtitle.narslimmusEat"));
		add("orechid", definition().with(sound(prefix("orechid"), SoundType.SOUND).volume(2.0))
				.subtitle("botania.subtitle.orechid"));
		add("pinkinator", definition().with(sound(ResourceLocation.parse("minecraft:entity.generic.explode"), SoundType.EVENT).volume(4.0))
				.subtitle("botania.subtitle.pinkinator"));
		add("potion_create", definition().with(sound(prefix("potioncreate"), SoundType.SOUND))
				.subtitle("botania.subtitle.potionCreate"));
		add("red_string_interceptor_click", definition().with(sound(ResourceLocation.parse("minecraft:block.dispenser.dispense"), SoundType.EVENT).volume(0.3).pitch(0.6))
				.subtitle("botania.subtitle.redStringInterceptorClick"));
		add("rune_altar_craft", definition().with(sound(prefix("runealtarcraft"), SoundType.SOUND))
				.subtitle("botania.subtitle.runeAltarCraft"));
		add("rune_altar_start", definition().with(sound(prefix("runealtarstart"), SoundType.SOUND))
				.subtitle("botania.subtitle.runeAltarStart"));
		add("shulk_me_not", definition().with(sound(ResourceLocation.parse("minecraft:entity.shulker.death"), SoundType.EVENT).pitch(0.1))
				.subtitle("botania.subtitle.shulkMeNot"));
		add("smelt_rod", definition().with(sound(ResourceLocation.parse("minecraft:item.flintandsteel.use"), SoundType.EVENT).volume(0.6))
				.subtitle("botania.subtitle.smeltRod"));
		add("smelt_rod_extra_no_subtitle", definition().with(sound(ResourceLocation.parse("minecraft:block.fire.ambient"), SoundType.EVENT)));
		add("smelt_rod_simmer", definition().with(sound(ResourceLocation.parse("minecraft:item.flintandsteel.use"), SoundType.EVENT))
				.subtitle("botania.subtitle.smeltRodSimmer"));
		add("prism_add_lens", definition().with(sound(ResourceLocation.parse("minecraft:block.glass.place"), SoundType.EVENT))
				.subtitle("botania.subtitle.prismAddLens"));
		add("prism_remove_lens", definition().with(sound(ResourceLocation.parse("minecraft:block.glass.place"), SoundType.EVENT))
				.subtitle("botania.subtitle.prismRemoveLens"));
		add("spreader_add_lens", definition().with(sound(ResourceLocation.parse("minecraft:block.glass.place"), SoundType.EVENT))
				.subtitle("botania.subtitle.spreaderAddLens"));
		add("spreader_remove_lens", definition().with(sound(ResourceLocation.parse("minecraft:block.glass.place"), SoundType.EVENT))
				.subtitle("botania.subtitle.spreaderRemoveLens"));
		add("spreader_cover", definition().with(sound(ResourceLocation.parse("minecraft:block.wool.place"), SoundType.EVENT))
				.subtitle("botania.subtitle.spreaderCover"));
		add("spreader_uncover", definition().with(sound(ResourceLocation.parse("minecraft:block.wool.break"), SoundType.EVENT))
				.subtitle("botania.subtitle.spreaderUncover"));
		add("spreader_scaffold", definition().with(sound(ResourceLocation.parse("minecraft:block.scaffolding.place"), SoundType.EVENT))
				.subtitle("botania.subtitle.spreaderScaffold"));
		add("spreader_un_scaffold", definition().with(sound(ResourceLocation.parse("minecraft:block.scaffolding.break"), SoundType.EVENT))
				.subtitle("botania.subtitle.spreaderUnScaffold"));
		add("spreader_fire", definition().with(sound(prefix("spreaderfire"), SoundType.SOUND))
				.subtitle("botania.subtitle.spreaderFire"));
		add("starcaller", definition().with(sound(prefix("starcaller"), SoundType.SOUND).volume(0.4).pitch(1.4))
				.subtitle("botania.subtitle.starcaller"));
		add("temperance_stone_configure", definition().with(sound(ResourceLocation.parse("minecraft:entity.experience_orb.pickup"), SoundType.EVENT).volume(0.3).pitch(0.1))
				.subtitle("botania.subtitle.temperanceStoneConfigure"));
		add("manufactory_halo_configure", definition().with(sound(ResourceLocation.parse("minecraft:entity.experience_orb.pickup"), SoundType.EVENT).volume(0.3).pitch(0.1))
				.subtitle("botania.subtitle.manufactoryHaloConfigure"));
		add("terra_pick_mode", definition().with(sound(prefix("terrapickmode"), SoundType.SOUND).volume(0.5).pitch(0.4))
				.subtitle("botania.subtitle.terraPickMode"));
		add("terrablade", definition().with(sound(prefix("terrablade"), SoundType.SOUND).volume(0.4).pitch(1.4))
				.subtitle("botania.subtitle.terraBlade"));
		add("terraform_rod", definition().with(sound(prefix("terraformrod"), SoundType.SOUND).pitch(0.4))
				.subtitle("botania.subtitle.terraformRod"));
		add("terrasteel_craft", definition().with(sound(prefix("terrasteelcraft"), SoundType.SOUND))
				.subtitle("botania.subtitle.terrasteelCraft"));
		add("thermalily", definition().with(sound(prefix("thermalily"), SoundType.SOUND).volume(0.2))
				.subtitle("botania.subtitle.thermalily"));
		add("thorn_chakram_throw", definition().with(sound(ResourceLocation.parse("minecraft:entity.arrow.shoot"), SoundType.EVENT).volume(0.5))
				.subtitle("botania.subtitle.thornChakramThrow"));
		add("tigerseye_pacify", definition().with(sound(ResourceLocation.parse("minecraft:entity.creeper.hurt"), SoundType.EVENT))
				.subtitle("botania.subtitle.tigerseyePacify"));
		add("unholy_cloak", definition().with(sound(prefix("unholycloak"), SoundType.SOUND))
				.subtitle("botania.subtitle.unholyCloak"));
		add("vine_ball_throw", definition().with(sound(ResourceLocation.parse("minecraft:entity.arrow.shoot"), SoundType.EVENT).volume(0.5))
				.subtitle("botania.subtitle.vineBallThrow"));
		add("virus_infect", definition().with(sound(ResourceLocation.parse("minecraft:entity.zombie_villager.cure"), SoundType.EVENT))
				.subtitle("botania.subtitle.virusInfect"));
		add("way", definition().with(sound(prefix("way"), SoundType.SOUND))
				.subtitle("botania.subtitle.way"));
		add("world_seed_teleport", definition().with(sound(ResourceLocation.parse("minecraft:entity.enderman.teleport"), SoundType.EVENT))
				.subtitle("botania.subtitle.worldSeedTeleport"));
	}
}
