package velrondevs.botania.api.state.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum AlfheimPortalState implements StringRepresentable {
	OFF,
	ON_Z,
	ON_X;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
