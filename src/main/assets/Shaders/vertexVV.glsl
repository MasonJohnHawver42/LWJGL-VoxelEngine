#version 330

layout (location=0) in int stuff;

out vec2 texCoord;
out float color;

uniform mat4 cam_mat;
uniform ivec3 chunk_pos;

void main() {

    int data = stuff;

    int lighting = data / 8388608; data -= lighting * 8388608;
    int tv_id = data / 2097152; data -= tv_id * 2097152;
    int tex_id = data / 32768; data -= tex_id * 32768;
    int z = data / 1024; data -= z * 1024;
    int y = data / 32; data -= y * 32;
    int x = data;

    float lightings[4] = float[4](1, .7, .5, .3);

    color = lightings[lighting];

    const vec2 ss_size = vec2(8.0, 8.0);

    const vec2[4] tv_ids = vec2[4] (
        vec2(0.0, 0.0),
        vec2(1.0, 0.0),
        vec2(0.0, 1.0),
        vec2(1.0, 1.0)
    );

    vec2 texPos = vec2(mod(tex_id, ss_size.x), floor(tex_id / ss_size.x)) / ss_size;
    texCoord = texPos + (tv_ids[tv_id] / ss_size);

    gl_Position = cam_mat * vec4(vec3(x, y, z) + (chunk_pos * 31), 1.0f);
}
