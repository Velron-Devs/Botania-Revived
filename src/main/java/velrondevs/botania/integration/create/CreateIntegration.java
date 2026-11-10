package velrondevs.botania.integration.create;

import com.simibubi.create.api.stress.BlockStressValues;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import velrondevs.botania.api.BotaniaCapabilities;
import velrondevs.botania.common.lib.LibMisc;
import velrondevs.botania.registry.BotaniaCreativeSections;

public final class CreateIntegration {
	public static final String MOD_ID = "create";
	public static final String MANA_MOTOR_ID = "mana_motor";

	private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LibMisc.MOD_ID);
	private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LibMisc.MOD_ID);
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LibMisc.MOD_ID);

	public static final DeferredBlock<ManaMotorBlock> MANA_MOTOR = BLOCKS.registerBlock(MANA_MOTOR_ID, ManaMotorBlock::new,
			() -> BlockBehaviour.Properties.of()
					.mapColor(MapColor.COLOR_LIGHT_BLUE)
					.strength(2.0F, 6.0F)
					.sound(SoundType.METAL)
					.noOcclusion());

	public static final DeferredItem<BlockItem> MANA_MOTOR_ITEM = ITEMS.registerItem(MANA_MOTOR_ID, properties -> {
		BlockItem item = new BlockItem(MANA_MOTOR.get(), properties);
		BotaniaCreativeSections.addBaseItem(item, BotaniaCreativeSections.MANA);
		return item;
	}, () -> new Item.Properties().useBlockDescriptionPrefix());

	public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ManaMotorBlockEntity>> MANA_MOTOR_BE = BLOCK_ENTITIES.register(MANA_MOTOR_ID,
			() -> new BlockEntityType<>(CreateIntegration::createManaMotor, MANA_MOTOR.get()));

	private CreateIntegration() {}

	public static void init(IEventBus modBus, ModContainer container) {
		BLOCKS.register(modBus);
		ITEMS.register(modBus);
		BLOCK_ENTITIES.register(modBus);
		modBus.addListener(CreateIntegration::registerCapabilities);
		modBus.addListener(CreateIntegration::commonSetup);
		if (FMLEnvironment.getDist().isClient()) {
			CreateIntegrationClient.init(modBus);
		}
	}

	private static ManaMotorBlockEntity createManaMotor(BlockPos pos, BlockState state) {
		return new ManaMotorBlockEntity(MANA_MOTOR_BE.get(), pos, state);
	}

	private static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerBlockEntity(BotaniaCapabilities.MANA_RECEIVER, MANA_MOTOR_BE.get(), (be, side) -> be);
		event.registerBlockEntity(BotaniaCapabilities.SPARK_ATTACHABLE, MANA_MOTOR_BE.get(), (be, side) -> be);
	}

	private static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			BlockStressValues.CAPACITIES.register(MANA_MOTOR.get(), ManaMotorConfig::stressPerRpm);
			BlockStressValues.RPM.register(MANA_MOTOR.get(), new BlockStressValues.GeneratedRpm(ManaMotorConfig.maxRpm(), true));
		});
	}
}
