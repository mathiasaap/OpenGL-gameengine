#version 330 core

out vec4 out_Color;
in vec2 tex;
uniform sampler2D textureSampler;
#define WIDTH 1000
#define HEIGHT 750


void main()
{
vec2 pixCoord =gl_FragCoord.xy;
vec4 color= texture(textureSampler,tex);

out_Color=color;

}

