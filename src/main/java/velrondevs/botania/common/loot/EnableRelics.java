package velrondevs.botania.common.loot;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.registry.BotaniaLootModifiers;
import velrondevs.botania.xplat.BotaniaConfig;

public class EnableRelics implements LootItemCondition {
	public static final EnableRelics INSTANCE = new EnableRelics();
	public static final MapCodec<EnableRelics> CODEC = MapCodec.unit(INSTANCE);

	@Override
	public boolean test(@NotNull LootContext context) {
		return BotaniaConfig.common().relicsEnabled();
	}

	@Override
	public LootItemConditionType getType() {
		return BotaniaLootModifiers.ENABLE_RELICS;
	}

}
