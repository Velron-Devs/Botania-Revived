package velrondevs.botania.api.configdata;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import velrondevs.botania.api.BotaniaAPI;

import java.util.Locale;

public class LooniumMobAttributeModifier {
	public static final Codec<LooniumMobAttributeModifier> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
					Codec.STRING.fieldOf("name").forGetter(mam -> mam.name),
					BuiltInRegistries.ATTRIBUTE.holderByNameCodec().fieldOf("attribute").forGetter(mam -> mam.attribute),
					Codec.DOUBLE.fieldOf("amount").forGetter(mam -> mam.amount),
					Codec.STRING.comapFlatMap(LooniumMobAttributeModifier::operationFromString,
							LooniumMobAttributeModifier::operationToString)
							.fieldOf("operation").forGetter(mam -> mam.operation)
			).apply(instance, LooniumMobAttributeModifier::new)
	);

	private final String name;
	public final Holder<Attribute> attribute;
	private final double amount;
	private final AttributeModifier.Operation operation;

	public LooniumMobAttributeModifier(String name, Holder<Attribute> attribute, double amount,
			AttributeModifier.Operation operation) {
		this.name = name;
		this.attribute = attribute;
		this.amount = amount;
		this.operation = operation;
	}

	public ResourceLocation getId() {
		return ResourceLocation.fromNamespaceAndPath(BotaniaAPI.MODID,
				Util.sanitizeName(name.toLowerCase(Locale.ROOT), ResourceLocation::validPathChar));
	}

	public AttributeModifier createAttributeModifier() {
		return new AttributeModifier(getId(), amount, operation);
	}

	private static String operationToString(AttributeModifier.Operation operation) {
		return switch (operation) {
			case ADD_VALUE -> "addition";
			case ADD_MULTIPLIED_BASE -> "multiply_base";
			case ADD_MULTIPLIED_TOTAL -> "multiply_total";
		};
	}

	private static DataResult<AttributeModifier.Operation> operationFromString(String operation) {
		return switch (operation) {
			case "addition", "add_value" -> DataResult.success(AttributeModifier.Operation.ADD_VALUE);
			case "multiply_base", "add_multiplied_base" -> DataResult.success(AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
			case "multiply_total", "add_multiplied_total" -> DataResult.success(AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
			default -> DataResult.error(() -> "Unknown attribute modifier operation " + operation);
		};
	}

	@Override
	public String toString() {
		return "MobAttributeModifier{" +
				"name='" + name + '\'' +
				", attribute=" + attribute +
				", amount=" + amount +
				", operation=" + operation +
				'}';
	}
}
