package velrondevs.botania.integration.kubejs;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ID;

import net.minecraft.world.item.Item;

import velrondevs.botania.event.BotaniaEvents;

public class BotaniaRegistryKubeEvent implements KubeEvent {
	private final BotaniaEvents.RegisterRunes event;

	public BotaniaRegistryKubeEvent(BotaniaEvents.RegisterRunes event) {
		this.event = event;
	}

	public Item rune(Object id) {
		try {
			return event.register(ID.kjs(id));
		} catch (Exception e) {
			ScriptType.STARTUP.console.error("[Botania] Failed to register rune " + id, e);
			return null;
		}
	}
}
