#version 330

in float color;
in  vec2 texCoord;

out vec4 FragColor;

uniform sampler2D texture_sampler;

void main() {
    FragColor = vec4(color, color, color, 0) * texture(texture_sampler, texCoord);
}
