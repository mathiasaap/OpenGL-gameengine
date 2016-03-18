#version 300 es

in vec3 position;
in vec2 texCoord;
in vec3 normal;

out vec2 texToFrag;
//varying vec3 absNormal;
//varying vec3 toLight;
//varying vec3 camVector;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;
//uniform vec3 lightPos;

//uniform vec4 clipP;


//uniform mat4 MVP;
void main()
{

texToFrag=texCoord;
vec4 modelPosition = model * vec4(position, 1.0);
//absNormal = (model*vec4(normal,0)).xyz;
//toLight= lightPos-(modelPosition).xyz;
//camVector=( inverse(view)* vec4(0,0,0,1)).xyz -modelPosition.xyz;


gl_Position= projection*view*modelPosition;

}