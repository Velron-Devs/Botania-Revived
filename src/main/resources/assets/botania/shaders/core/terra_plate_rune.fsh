#version 330

#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor;
    if (color.a < 0.1) {
        discard;
    }

    float time = GameTime * 24000.0;
    float gs = (color.r + color.g + color.b) / 16.0;

    float tx = (texCoord0.x + 0.075) * 10.0;
    float ty = texCoord0.y * 10.0;
    float g = ((sin((time + sin(tx) * 250.0 + sin(ty) * 50.0) / 6.0) + 1.0) * 0.5 + 0.5) / 5.0;
    float b = ((cos((time + sin(tx) * 50.0 + sin(ty) * 100.0) / 6.0) + 1.0) * 0.5 + 0.5) / 5.0;

    fragColor = vec4(gs - 0.1, gs + g, gs + b, color.a * 1.4) * ColorModulator;
}
