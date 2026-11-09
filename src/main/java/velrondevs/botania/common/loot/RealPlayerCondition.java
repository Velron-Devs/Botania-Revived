package velrondevs.botania.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.helper.PlayerHelper;

import java.util.Set;

public class RealPlayerCondition implements LootItemCondition {
	public static final RealPlayerCondition INSTANCE = new RealPlayerCondition();
	public static final MapCodec<RealPlayerCondition> CODEC = MapCodec.unit(INSTANCE);

	private RealPlayerCondition() {}

	@Override
	public boolean test(LootContext lootContext) {
		Player player = lootContext.getOptionalParameter(LootContextParams.LAST_DAMAGE_PLAYER);
		return PlayerHelper.isTruePlayer(player);
	}

	@NotNull
	@Override
	public Set<ContextKey<?>> getReferencedContextParams() {
		return ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
	}

	@NotNull
	@Override
	public MapCodec<? extends LootItemCondition> codec() {
		return CODEC;
	}
}
