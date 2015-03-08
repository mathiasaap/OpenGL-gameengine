#version 330 core

in vec3 position;
in vec2 texture;
in vec3 normal;

out vec2 texToFrag;
out vec3 absNormal;
out vec3 toLight;
out vec3 camVector;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPos;


void main()
{
vec4 modelPosition = model * vec4(position, 1.0);
gl_Position= projection*view*modelPosition;
texToFrag=texture;

absNormal = (model*vec4(normal,0)).xyz;
toLight= lightPos-(modelPosition).xyz;
camVector=( inverse(view)* vec4(0,0,0,1)).xyz -modelPosition.xyz;
}