package velrondevs.botania.client.fx;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.texture.TextureManager;

import org.jetbrains.annotations.Nullable;

public abstract class BotaniaParticleRenderType implements ParticleRenderType {
	private static ByteBufferBuilder byteBuffer;

	protected abstract void setup(TextureManager textureManager);

	protected abstract void end();

	@Override
	public BufferBuilder begin(Tesselator tesselator, TextureManager textureManager) {
		setup(textureManager);
		if (byteBuffer == null) {
			byteBuffer = new ByteBufferBuilder(0x20000);
		}
		return new EndingBufferBuilder(byteBuffer, VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE, this);
	}

	private static class EndingBufferBuilder extends BufferBuilder {
		private final BotaniaParticleRenderType type;

		EndingBufferBuilder(ByteBufferBuilder buffer, VertexFormat.Mode mode, VertexFormat format, BotaniaParticleRenderType type) {
			super(buffer, mode, format);
			this.type = type;
		}

		@Nullable
		@Override
		public MeshData build() {
			MeshData data = super.build();
			if (data != null) {
				BufferUploader.drawWithShader(data);
			}
			type.end();
			return null;
		}
	}
}
