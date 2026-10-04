package velrondevs.botania.common.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import org.jetbrains.annotations.NotNull;

import velrondevs.botania.registry.BotaniaLootModifiers;
import velrondevs.botania.xplat.XplatAbstractions;

import java.util.List;

public class BindUuid extends LootItemConditionalFunction {
	public static final MapCodec<BindUuid> CODEC = RecordCodecBuilder.mapCodec(
			instance -> commonFields(instance).apply(instance, BindUuid::new));

	protected BindUuid(List<LootItemCondition> conditionsIn) {
		super(conditionsIn);
	}

	@NotNull
	@Override
	public ItemStack run(@NotNull ItemStack stack, @NotNull LootContext context) {
		if (context.getParamOrNull(LootContextParams.ATTACKING_ENTITY) instanceof Player player) {
			var relic = XplatAbstractions.INSTANCE.findRelic(stack);
			if (relic != null) {
				relic.bindToUUID(player.getUUID());
			}
		}

		return stack;
	}

	@Override
	public LootItemFunctionType<BindUuid> getType() {
		return BotaniaLootModifiers.BIND_UUID;
	}

	public static LootItemConditionalFunction.Builder<?> builder() {
		return simpleBuilder(BindUuid::new);
	}
}
