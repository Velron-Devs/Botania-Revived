package velrondevs.botania.client.core.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class DeferredBufferSource implements MultiBufferSource {
	private static final Map<RenderType, Float> LINE_WIDTHS = new IdentityHashMap<>();

	private final OrderedSubmitNodeCollector collector;
	private final Map<RenderType, Recorder> recorders = new HashMap<>();

	public DeferredBufferSource(OrderedSubmitNodeCollector collector) {
		this.collector = collector;
	}

	public static void setLineWidth(RenderType type, float width) {
		LINE_WIDTHS.put(type, width);
	}

	@Override
	public VertexConsumer getBuffer(RenderType renderType) {
		return recorders.computeIfAbsent(BotaniaShaders.resolve(renderType), type -> {
			Recorder recorder = new Recorder(LINE_WIDTHS.getOrDefault(type, 1.0F));
			collector.submitCustomGeometry(new PoseStack(), type, (pose, buffer) -> recorder.replay(buffer));
			return recorder;
		});
	}

	public static class Recorder implements VertexConsumer {
		private static final int STRIDE = 14;
		private static final int F_COLOR = 1;
		private static final int F_UV = 2;
		private static final int F_UV1 = 4;
		private static final int F_UV2 = 8;
		private static final int F_NORMAL = 16;
		private static final int F_LINE = 32;

		private final float defaultLineWidth;
		private int[] data = new int[STRIDE * 64];
		private int count = 0;

		public Recorder(float defaultLineWidth) {
			this.defaultLineWidth = defaultLineWidth;
		}

		private int base() {
			return (count - 1) * STRIDE;
		}

		@Override
		public VertexConsumer addVertex(float x, float y, float z) {
			if ((count + 1) * STRIDE > data.length) {
				data = Arrays.copyOf(data, data.length * 2);
			}
			count++;
			int b = base();
			data[b] = 0;
			data[b + 1] = Float.floatToRawIntBits(x);
			data[b + 2] = Float.floatToRawIntBits(y);
			data[b + 3] = Float.floatToRawIntBits(z);
			data[b + 4] = -1;
			return this;
		}

		@Override
		public VertexConsumer setColor(int r, int g, int b, int a) {
			return setColor((a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF));
		}

		@Override
		public VertexConsumer setColor(int color) {
			int b = base();
			data[b] |= F_COLOR;
			data[b + 4] = color;
			return this;
		}

		@Override
		public VertexConsumer setUv(float u, float v) {
			int b = base();
			data[b] |= F_UV;
			data[b + 5] = Float.floatToRawIntBits(u);
			data[b + 6] = Float.floatToRawIntBits(v);
			return this;
		}

		@Override
		public VertexConsumer setUv1(int u, int v) {
			int b = base();
			data[b] |= F_UV1;
			data[b + 7] = (u & 0xFFFF) | (v & 0xFFFF) << 16;
			return this;
		}

		@Override
		public VertexConsumer setUv2(int u, int v) {
			int b = base();
			data[b] |= F_UV2;
			data[b + 8] = (u & 0xFFFF) | (v & 0xFFFF) << 16;
			return this;
		}

		@Override
		public VertexConsumer setNormal(float x, float y, float z) {
			int b = base();
			data[b] |= F_NORMAL;
			data[b + 9] = Float.floatToRawIntBits(x);
			data[b + 10] = Float.floatToRawIntBits(y);
			data[b + 11] = Float.floatToRawIntBits(z);
			return this;
		}

		@Override
		public VertexConsumer setLineWidth(float width) {
			int b = base();
			data[b] |= F_LINE;
			data[b + 12] = Float.floatToRawIntBits(width);
			return this;
		}

		public void replay(VertexConsumer buffer) {
			for (int i = 0; i < count; i++) {
				int b = i * STRIDE;
				int flags = data[b];
				buffer.addVertex(Float.intBitsToFloat(data[b + 1]), Float.intBitsToFloat(data[b + 2]), Float.intBitsToFloat(data[b + 3]));
				buffer.setColor(data[b + 4]);
				if ((flags & F_UV) != 0) {
					buffer.setUv(Float.intBitsToFloat(data[b + 5]), Float.intBitsToFloat(data[b + 6]));
				} else {
					buffer.setUv(0, 0);
				}
				int uv1 = (flags & F_UV1) != 0 ? data[b + 7] : 10 << 16;
				buffer.setUv1(uv1 & 0xFFFF, uv1 >>> 16);
				int uv2 = (flags & F_UV2) != 0 ? data[b + 8] : 0x00F000F0;
				buffer.setUv2(uv2 & 0xFFFF, uv2 >>> 16);
				if ((flags & F_NORMAL) != 0) {
					buffer.setNormal(Float.intBitsToFloat(data[b + 9]), Float.intBitsToFloat(data[b + 10]), Float.intBitsToFloat(data[b + 11]));
				} else {
					buffer.setNormal(0, 1, 0);
				}
				buffer.setLineWidth((flags & F_LINE) != 0 ? Float.intBitsToFloat(data[b + 12]) : defaultLineWidth);
			}
		}
	}
}
