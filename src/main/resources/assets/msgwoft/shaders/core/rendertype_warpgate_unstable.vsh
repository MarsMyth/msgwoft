#version 150

in vec3 Position;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec4 texProj0;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    vec4 pos = gl_Position;
    texProj0 = vec4(pos.xy * 0.5 + pos.ww * 0.5, pos.zw);
}