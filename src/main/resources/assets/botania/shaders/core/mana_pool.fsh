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
    float c = sin(time / 80.0 + sin(texCoord0.x / 5.0 + (texCoord0.y - cos(time / 300.0) * 0.25) / ((1.0 - sin(time / 200.0) / 10.0) * 5.0)) * sin(time / 600.0) * 1000.0) * 0.5 + 0.5;
    float g = 1.0 - c * 0.2 - 0.2;
    float b = 1.0 - c * 0.2;
    fragColor = vec4(0.0, g, b, color.a) * ColorModulator;
}
