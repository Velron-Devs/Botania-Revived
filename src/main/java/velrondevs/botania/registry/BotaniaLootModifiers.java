package velrondevs.botania.registry;

import com.mojang.serialization.MapCodec;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.function.BiConsumer;

import velrondevs.botania.common.loot.BindUuid;
import velrondevs.botania.common.loot.EnableRelics;
import velrondevs.botania.common.loot.RealPlayerCondition;
import velrondevs.botania.common.loot.TrueGuardianKiller;

import static velrondevs.botania.common.lib.ResourceLocationHelper.prefix;

public class BotaniaLootModifiers {
	public static final MapCodec<TrueGuardianKiller> TRUE_GUARDIAN_KILLER = TrueGuardianKiller.CODEC;
	public static final MapCodec<EnableRelics> ENABLE_RELICS = EnableRelics.CODEC;
	public static final MapCodec<RealPlayerCondition> KILLED_BY_REAL_PLAYER = RealPlayerCondition.CODEC;
	public static final MapCodec<BindUuid> BIND_UUID = BindUuid.CODEC;

	public static void submitLootConditions(BiConsumer<MapCodec<? extends LootItemCondition>, Identifier> consumer) {
		consumer.accept(TRUE_GUARDIAN_KILLER, prefix("true_guardian_killer"));
		consumer.accept(ENABLE_RELICS, prefix("enable_relics"));
		consumer.accept(KILLED_BY_REAL_PLAYER, prefix("killed_by_player"));
	}

	public static void submitLootFunctions(BiConsumer<MapCodec<? extends LootItemFunction>, Identifier> consumer) {
		consumer.accept(BIND_UUID, prefix("bind_uuid"));
	}
}
