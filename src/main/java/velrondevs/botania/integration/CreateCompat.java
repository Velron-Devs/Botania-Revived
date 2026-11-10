package velrondevs.botania.integration;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

import velrondevs.botania.api.BotaniaAPI;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class CreateCompat {
	public static final String MOD_ID = "create";
	private static final String PACKAGE = "velrondevs.botania.integration.create.";
	private static final String INTEGRATION = PACKAGE + "CreateIntegration";
	private static final String SCENES = PACKAGE + "ManaMotorScenes";

	private CreateCompat() {}

	public static boolean isLoaded() {
		return ModList.get().isLoaded(MOD_ID);
	}

	public static void init(IEventBus modBus, ModContainer container) {
		invoke(INTEGRATION, "init", new Class<?>[] { IEventBus.class, ModContainer.class }, modBus, container);
	}

	public static void registerPonderScenes(Class<?> helperType, Object helper) {
		invokeQuietly(SCENES, "registerScenes", helperType, helper);
	}

	public static void registerPonderTags(Class<?> helperType, Object helper) {
		invokeQuietly(SCENES, "registerTags", helperType, helper);
	}

	private static void invokeQuietly(String className, String methodName, Class<?> helperType, Object helper) {
		try {
			invoke(className, methodName, new Class<?>[] { helperType }, helper);
		} catch (RuntimeException | LinkageError e) {
			BotaniaAPI.LOGGER.warn("Could not register the Mana Motor Ponder scene with this Create/Ponder version", e);
		}
	}

	private static void invoke(String className, String methodName, Class<?>[] types, Object... args) {
		Class<?> type;
		try {
			type = Class.forName(className, true, CreateCompat.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			BotaniaAPI.LOGGER.warn("Create is installed but this Botania build has no Create integration ({} is missing)", className);
			return;
		}
		try {
			Method method = type.getMethod(methodName, types);
			method.invoke(null, args);
		} catch (InvocationTargetException e) {
			Throwable cause = e.getCause();
			if (cause instanceof RuntimeException runtime) {
				throw runtime;
			}
			if (cause instanceof Error error) {
				throw error;
			}
			throw new IllegalStateException(cause);
		} catch (ReflectiveOperationException e) {
			throw new IllegalStateException("Could not call " + className + "." + methodName, e);
		}
	}
}
