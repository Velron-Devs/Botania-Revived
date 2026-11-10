#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

void main() {
    float timeTicks = GameTime * 24000.0;
    float brightness = sin(timeTicks / 20.0) * 0.5 + 0.5;

    vec4 sampled = texture(Sampler0, texCoord0);
    vec4 sum = sampled * 12.0;
    vec4 color;
    if (sampled.r < 0.3) {
        color = sum * sum * 0.012 + sampled;
    } else if (sampled.r < 0.5) {
        color = sum * sum * 0.009 * brightness + sampled;
    } else {
        color = sum * sum * 0.0075 * brightness + sampled;
    }

    color *= vertexColor;
    if (color.a < 0.1) {
        discard;
    }
    fragColor = color * ColorModulator;
}
