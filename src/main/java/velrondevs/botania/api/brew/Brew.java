package velrondevs.botania.api.brew;

import com.google.common.collect.ImmutableList;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

import velrondevs.botania.api.BotaniaAPI;

import java.util.List;
import java.util.function.Supplier;

public class Brew {
	private final Supplier<Integer> color;
	private final int cost;
	private final Supplier<List<MobEffectInstance>> effects;
	private boolean canInfuseBloodPendant = true;
	private boolean canInfuseIncense = true;

	public Brew(int color, int cost, MobEffectInstance... effects) {
		this.color = () -> color;
		this.cost = cost;
		List<MobEffectInstance> savedEffects = ImmutableList.copyOf(effects);
		this.effects = () -> savedEffects;
	}

	public Brew(int cost, Supplier<List<MobEffectInstance>> effects) {
		this.color = () -> PotionContents.getColor(effects.get()) & 0xFFFFFF;
		this.cost = cost;
		this.effects = effects;
	}

	public Brew setNotBloodPendantInfusable() {
		canInfuseBloodPendant = false;
		return this;
	}

	public Brew setNotIncenseInfusable() {
		canInfuseIncense = false;
		return this;
	}

	public boolean canInfuseBloodPendant() {
		return canInfuseBloodPendant;
	}

	public boolean canInfuseIncense() {
		return canInfuseIncense;
	}

	public String getTranslationKey() {
		ResourceLocation id = BotaniaAPI.instance().getBrewRegistry().getKey(this);
		return String.format("%s.brew.%s", id.getNamespace(), id.getPath());
	}

	public String getTranslationKey(ItemStack stack) {
		return getTranslationKey();
	}

	public int getColor(ItemStack stack) {
		return color.get();
	}

	public int getManaCost() {
		return cost;
	}

	public int getManaCost(ItemStack stack) {
		return getManaCost();
	}

	public List<MobEffectInstance> getPotionEffects(ItemStack stack) {
		return effects.get();
	}

}
