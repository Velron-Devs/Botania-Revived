package velrondevs.botania.api.state.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum LuminizerVariant implements StringRepresentable {
	DEFAULT,
	DETECTOR,
	FORK,
	TOGGLE;

	@Override
	public String getSerializedName() {
		return name().toLowerCase(Locale.ROOT);
	}

}
