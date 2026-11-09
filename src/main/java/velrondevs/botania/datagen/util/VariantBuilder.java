package velrondevs.botania.datagen.util;

import com.mojang.math.Quadrant;

import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;

import java.util.Arrays;
import java.util.Collection;

public final class VariantBuilder {
	private final Identifier model;
	private Quadrant xRot = Quadrant.R0;
	private Quadrant yRot = Quadrant.R0;
	private boolean uvLock;
	private int weight = 1;

	private VariantBuilder(Identifier model) {
		this.model = model;
	}

	public static VariantBuilder of(Identifier model) {
		return new VariantBuilder(model);
	}

	public VariantBuilder xRot(Quadrant rotation) {
		this.xRot = rotation;
		return this;
	}

	public VariantBuilder yRot(Quadrant rotation) {
		this.yRot = rotation;
		return this;
	}

	public VariantBuilder uvLock(boolean uvLock) {
		this.uvLock = uvLock;
		return this;
	}

	public VariantBuilder weight(int weight) {
		this.weight = weight;
		return this;
	}

	public Variant build() {
		return new Variant(model).withXRot(xRot).withYRot(yRot).withUvLock(uvLock);
	}

	public Weighted<Variant> weighted() {
		return new Weighted<>(build(), weight);
	}

	public MultiVariant multi() {
		return multi(this);
	}

	public static MultiVariant multi(VariantBuilder... variants) {
		return multi(Arrays.asList(variants));
	}

	public static MultiVariant multi(Collection<VariantBuilder> variants) {
		return new MultiVariant(WeightedList.of(variants.stream().map(VariantBuilder::weighted).toList()));
	}

	public static MultiVariant plain(Identifier model) {
		return of(model).multi();
	}
}
