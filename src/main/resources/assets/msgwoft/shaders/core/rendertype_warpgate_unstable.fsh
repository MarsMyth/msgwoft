#version 150

uniform sampler2D Sampler0;
uniform sampler2D Sampler1;

uniform float GameTime;
uniform int EndPortalLayers;

in vec4 texProj0;

const vec3[] COLORS = vec3[](
vec3(0.110818, 0.022087, 0.022087),
vec3(0.089485, 0.011892, 0.011892),
vec3(0.100326, 0.027636, 0.027636),
vec3(0.114838, 0.046564, 0.046564),
vec3(0.097189, 0.064901, 0.030000),
vec3(0.123646, 0.030000, 0.030000),
vec3(0.166380, 0.040000, 0.040000),
vec3(0.150000, 0.030000, 0.030000),
vec3(0.195191, 0.050000, 0.050000),
vec3(0.187229, 0.040000, 0.040000),
vec3(0.200000, 0.060000, 0.060000),
vec3(0.235792, 0.070006, 0.040000),
vec3(0.250000, 0.060000, 0.060000),
vec3(0.321970, 0.080000, 0.047281),
vec3(0.400000, 0.090000, 0.060000),
vec3(0.661491, 0.100000, 0.080955)
);

const mat4 SCALE_TRANSLATE = mat4(
0.5, 0.0, 0.0, 0.25,
0.0, 0.5, 0.0, 0.25,
0.0, 0.0, 1.0, 0.0,
0.0, 0.0, 0.0, 1.0
);

mat2 rotate_z(float angle) {
    float s = sin(angle);
    float c = cos(angle);
    return mat2(c, -s, s, c);
}

mat4 end_portal_layer(float layer) {
    mat4 translate = mat4(
    1.0, 0.0, 0.0, 17.0 / layer,
    0.0, 1.0, 0.0, (2.0 + layer / 1.5) * (GameTime * 1.5),
    0.0, 0.0, 1.0, 0.0,
    0.0, 0.0, 0.0, 1.0
    );

    mat2 rot = rotate_z(radians((layer * layer * 4321.0 + layer * 9.0) * 2.0));
    mat2 scale = mat2((4.5 - layer / 4.0) * 2.0);

    return mat4(scale * rot) * translate * SCALE_TRANSLATE;
}

out vec4 fragColor;

void main() {
    vec3 color = textureProj(Sampler0, texProj0).rgb * COLORS[0];
    for (int i = 0; i < EndPortalLayers; i++) {
        color += textureProj(Sampler1, texProj0 * end_portal_layer(float(i + 1))).rgb * COLORS[i];
    }
    fragColor = vec4(color, 1.0);
}