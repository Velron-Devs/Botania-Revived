package velrondevs.botania.integration.catnip;

import com.mojang.brigadier.arguments.ArgumentType;

import net.createmod.catnip.impl.command.argument.ConfigIdArgument;
import net.createmod.catnip.impl.command.argument.ConfigPathArgument;
import net.createmod.catnip.impl.command.argument.ModIdArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

import velrondevs.botania.common.lib.LibMisc;

import java.util.function.Supplier;

public final class CatnipCompat {
	private CatnipCompat() {}

	public static void init(IEventBus modBus) {
		modBus.addListener((RegisterEvent event) -> {
			if (!event.getRegistryKey().equals(Registries.COMMAND_ARGUMENT_TYPE)) {
				return;
			}
			try {
				register(event, "catnip_mod_id", ModIdArgument.class, ModIdArgument::modId);
				register(event, "catnip_config_id", ConfigIdArgument.class, ConfigIdArgument::configId);
				register(event, "catnip_config_path", ConfigPathArgument.class, ConfigPathArgument::path);
			} catch (LinkageError error) {
				velrondevs.botania.api.BotaniaAPI.LOGGER.warn("The installed Catnip version has no command arguments to register", error);
			}
		});
	}

	private static <A extends ArgumentType<?>> void register(RegisterEvent event, String name, Class<A> type, Supplier<A> factory) {
		if (ArgumentTypeInfos.isClassRecognized(type)) {
			return;
		}
		event.register(Registries.COMMAND_ARGUMENT_TYPE, Identifier.fromNamespaceAndPath(LibMisc.MOD_ID, name),
				() -> ArgumentTypeInfos.registerByClass(type, SingletonArgumentInfo.contextFree(factory)));
	}
}
