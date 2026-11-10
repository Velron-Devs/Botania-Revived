#version 330

#moj_import <minecraft:light.glsl>
#moj_import <minecraft:fog.glsl>
#moj_import <minecraft:dynamictransforms.glsl>
#moj_import <minecraft:projection.glsl>
#moj_import <minecraft:globals.glsl>
#moj_import <minecraft:sample_lightmap.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler1;
uniform sampler2D Sampler2;

out float sphericalVertexDistance;
out float cylindricalVertexDistance;
out vec4 vertexColor;
out vec4 lightMapColor;
out vec4 overlayColor;
out vec2 texCoord0;
flat out float grainIntensity;

float rand(vec2 co) {
    return (fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453) - 0.5) * 2.0;
}

void main() {
    float disfiguration = 0.025 + (1.0 - Color.g) * 0.5;
    grainIntensity = 0.05 + (1.0 - Color.r) * 0.95;

    float seed = rand(vec2(GameTime, GameTime));
    vec3 offset = disfiguration * vec3(
        rand(seed * Position.yz),
        rand(seed * Position.xz),
        rand(seed * Position.xy)
    );
    gl_Position = ProjMat * ModelViewMat * vec4(Position + offset, 1.0);

    sphericalVertexDistance = fog_spherical_distance(Position);
    cylindricalVertexDistance = fog_cylindrical_distance(Position);
    vertexColor = minecraft_mix_light(Light0_Direction, Light1_Direction, Normal, vec4(1.0, 1.0, 1.0, Color.a));
    lightMapColor = sample_lightmap(Sampler2, UV2);
    overlayColor = texelFetch(Sampler1, UV1, 0);
    texCoord0 = UV0;
}
