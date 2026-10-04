package velrondevs.botania.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.common.helper.PlayerHelper;
import velrondevs.botania.registry.BotaniaLootModifiers;

import java.util.Set;

public class RealPlayerCondition implements LootItemCondition {
	public static final RealPlayerCondition INSTANCE = new RealPlayerCondition();
	public static final MapCodec<RealPlayerCondition> CODEC = MapCodec.unit(INSTANCE);

	private RealPlayerCondition() {}

	@Override
	public boolean test(LootContext lootContext) {
		Player player = lootContext.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
		return PlayerHelper.isTruePlayer(player);
	}

	@NotNull
	@Override
	public Set<LootContextParam<?>> getReferencedContextParams() {
		return ImmutableSet.of(LootContextParams.LAST_DAMAGE_PLAYER);
	}

	@NotNull
	@Override
	public LootItemConditionType getType() {
		return BotaniaLootModifiers.KILLED_BY_REAL_PLAYER;
	}
}
