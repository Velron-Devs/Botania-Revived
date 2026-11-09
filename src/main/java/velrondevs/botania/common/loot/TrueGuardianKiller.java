package velrondevs.botania.common.loot;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.entity.GaiaGuardianEntity;

public class TrueGuardianKiller implements LootItemCondition {
	public static final TrueGuardianKiller INSTANCE = new TrueGuardianKiller();
	public static final MapCodec<TrueGuardianKiller> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public boolean test(@NotNull LootContext context) {
		Entity victim = context.getOptionalParameter(LootContextParams.THIS_ENTITY);
		return victim instanceof GaiaGuardianEntity gg
				&& context.getOptionalParameter(LootContextParams.ATTACKING_ENTITY) == gg.trueKiller;
	}

	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}

}
