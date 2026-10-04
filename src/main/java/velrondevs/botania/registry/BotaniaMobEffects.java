package velrondevs.botania.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;

import velrondevs.botania.common.brew.effect.*;
import velrondevs.botania.common.lib.LibPotionNames;

import java.util.function.BiConsumer;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaMobEffects {

	public static final Holder<MobEffect> soulCross = DeferredHolder.create(Registries.MOB_EFFECT, prefix(LibPotionNames.SOUL_CROSS));
	public static final Holder<MobEffect> featherfeet = DeferredHolder.create(Registries.MOB_EFFECT, prefix(LibPotionNames.FEATHER_FEET));
	public static final Holder<MobEffect> emptiness = DeferredHolder.create(Registries.MOB_EFFECT, prefix(LibPotionNames.EMPTINESS));
	public static final Holder<MobEffect> bloodthrst = DeferredHolder.create(Registries.MOB_EFFECT, prefix(LibPotionNames.BLOODTHIRST));
	public static final Holder<MobEffect> allure = DeferredHolder.create(Registries.MOB_EFFECT, prefix(LibPotionNames.ALLURE));
	public static final Holder<MobEffect> clear = DeferredHolder.create(Registries.MOB_EFFECT, prefix(LibPotionNames.CLEAR));

	public static void registerPotions(BiConsumer<MobEffect, ResourceLocation> r) {
		r.accept(new SoulCrossMobEffect(), prefix(LibPotionNames.SOUL_CROSS));
		r.accept(new FeatherfeetMobEffect(), prefix(LibPotionNames.FEATHER_FEET));
		r.accept(new EmptinessMobEffect(), prefix(LibPotionNames.EMPTINESS));
		r.accept(new BloodthirstMobEffect(), prefix(LibPotionNames.BLOODTHIRST));
		r.accept(new AllureMobEffect(), prefix(LibPotionNames.ALLURE));
		r.accept(new AbsolutionMobEffect(), prefix(LibPotionNames.CLEAR));
	}
}
