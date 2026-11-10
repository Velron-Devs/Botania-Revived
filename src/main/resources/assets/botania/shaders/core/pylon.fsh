#version 330

#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:globals.glsl>

uniform sampler2D Sampler0;

in float sphericalVertexDistance;
in float cylindricalVertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 base = texture(Sampler0, texCoord0);
    vec4 color = base * 48.0;
    if (color.a < 0.1) {
        discard;
    }
    float time = GameTime * 24000.0;
    float brightness = sin(time / 20.0) * 0.5 + 0.15;
    float alpha = min(1.0, sin(time / 30.0) * 0.5 + 0.75);

    color = vec4((color * color * 0.0005 * brightness + base).rgb, alpha * vertexColor.a);
    color *= ColorModulator;

    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = apply_fog(color, sphericalVertexDistance, cylindricalVertexDistance, FogEnvironmentalStart, FogEnvironmentalEnd, FogRenderDistanceStart, FogRenderDistanceEnd, FogColor);
}
