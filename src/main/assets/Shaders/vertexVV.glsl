#version 330

layout (location=0) in vec3 pos;
layout (location=1) in vec2 tex;
layout (location=2) in float lighting;

out vec2 texCoord;
out float color;

uniform mat4 cam_mat;

void main() {

    float light = float(lighting);
    color = (1.0f - (light / 3.0f)) / 2.0 + .5f;

    int tex_id = int(tex[0]);
    int tv_id = int(tex[1]);

    const vec2 ss_size = vec2(8.0, 8.0);

    const vec2[4] tv_ids = vec2[4] (
        vec2(0.0, 0.0),
        vec2(1.0, 0.0),
        vec2(0.0, 1.0),
        vec2(1.0, 1.0)
    );

    vec2 texPos = vec2(mod(tex_id, ss_size.x), floor(tex_id / ss_size.x)) / ss_size;
    texCoord = texPos + (tv_ids[tv_id] / ss_size);

    gl_Position = cam_mat * vec4(pos, 1.0);
}
