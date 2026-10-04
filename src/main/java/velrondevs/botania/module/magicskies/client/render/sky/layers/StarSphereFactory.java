package velrondevs.botania.module.magicskies.client.render.sky.layers;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;

import org.joml.Vector3f;

import java.util.Random;

public final class StarSphereFactory {
	private StarSphereFactory() {}

	public static VertexBuffer build(int count, float shellRadius, long seed) {
		Random random = new Random(seed);
		var builder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

		for (int i = 0; i < count; i++) {
			Vector3f dir = randomUnitVector(random);
			Vector3f center = new Vector3f(dir).mul(shellRadius);
			float size = shellRadius * (0.0015F + random.nextFloat() * 0.0015F);

			Vector3f up = Math.abs(dir.y) < 0.99F ? new Vector3f(0, 1, 0) : new Vector3f(1, 0, 0);
			Vector3f u = new Vector3f(dir).cross(up).normalize();
			Vector3f v = new Vector3f(dir).cross(u).normalize();

			float theta = random.nextFloat() * (float) (Math.PI * 2);
			float cos = (float) Math.cos(theta);
			float sin = (float) Math.sin(theta);
			Vector3f uRot = new Vector3f(u).mul(cos).add(new Vector3f(v).mul(sin)).mul(size);
			Vector3f vRot = new Vector3f(u).mul(-sin).add(new Vector3f(v).mul(cos)).mul(size);

			addQuad(builder, center, uRot, vRot);
		}

		VertexBuffer buffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		buffer.bind();
		buffer.upload(builder.buildOrThrow());
		VertexBuffer.unbind();
		return buffer;
	}

	private static void addQuad(BufferBuilder builder, Vector3f center, Vector3f u, Vector3f v) {
		Vector3f p0 = new Vector3f(center).sub(u).sub(v);
		Vector3f p1 = new Vector3f(center).add(u).sub(v);
		Vector3f p2 = new Vector3f(center).add(u).add(v);
		Vector3f p3 = new Vector3f(center).sub(u).add(v);
		builder.addVertex(p0.x, p0.y, p0.z);
		builder.addVertex(p3.x, p3.y, p3.z);
		builder.addVertex(p2.x, p2.y, p2.z);
		builder.addVertex(p1.x, p1.y, p1.z);
	}

	private static Vector3f randomUnitVector(Random random) {
		float x, y, z;
		float lenSq;
		do {
			x = random.nextFloat() * 2 - 1;
			y = random.nextFloat() * 2 - 1;
			z = random.nextFloat() * 2 - 1;
			lenSq = x * x + y * y + z * z;
		} while (lenSq > 1F || lenSq < 0.01F);
		float invLen = (float) (1.0 / Math.sqrt(lenSq));
		return new Vector3f(x * invLen, y * invLen, z * invLen);
	}
}
