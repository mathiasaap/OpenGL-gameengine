#version 330 core

in vec3 position;
uniform mat4 model;

out vec2 tex;
void main()
{
gl_Position = model*vec4(position, 1.0);
tex = vec2((position.x+1.0)/2.0, 1 - (position.y+1.0)/2.0);

}