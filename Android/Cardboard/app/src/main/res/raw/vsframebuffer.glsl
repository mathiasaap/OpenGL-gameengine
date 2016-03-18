#version 300 es

in vec3 position;
//in vec3 UV;
uniform mat4 model;

out vec2 tex;
void main()
{
gl_Position = model*vec4(position, 1.0f);
tex = vec2((position.x+1.0)/(2.0f),(position.y+1.0f)/(2.0f));
//tex=position.xy;
}