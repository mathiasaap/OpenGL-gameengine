#version 330 core

out vec4 out_Color;
in vec2 tex;
uniform sampler2D textureSampler;



void main()
{
out_Color=texture(textureSampler,tex);

}

