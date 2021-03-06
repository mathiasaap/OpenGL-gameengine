#version 330 core

in vec2 texToFrag;
in vec3 absNormal;
in vec3 toLight;
in vec3 camVector;
in float height;
in vec3 multitextureComponents;

out vec4 out_color;

uniform sampler2D GrassTexture;
uniform sampler2D RockTexture;
uniform sampler2D SnowTexture;


uniform vec3 lightCol;
uniform float shine;
uniform float reflectivity;

void main()
{
vec3 unitNormal= normalize(absNormal);
vec3 unitToLight= normalize(toLight);
vec3 unitCam = normalize(camVector);

float dotprod = abs(dot(unitNormal,unitToLight));
float brightness = max(dotprod,0.2);
vec3 diffuse=brightness*lightCol;
vec3 reflectedDir=reflect(-unitCam,unitNormal);
float specularity = max(dot(reflectedDir,unitNormal),0);
float shineFactor = max(pow(specularity, shine),0.01f);
vec3 specularProduct = shineFactor*lightCol*reflectivity;

//4096:130:30:130
vec4 texColor= texture(GrassTexture,texToFrag*33)*multitextureComponents.x +texture(RockTexture,texToFrag*8)*multitextureComponents.y+texture(SnowTexture,texToFrag*33)*multitextureComponents.z ;


out_color= vec4(diffuse,1) *texColor;

//out_color=vec4(diffuse,1) *texture(RockTexture,texToFrag);
//out_color= vec4(diffuse,1f) * vec4(0.7f,0.85f,0.8f,1.0f);
//out_color=texture(texSampler,texToFrag);
//out_color=vec4(0.9f,0.1f,0.2f,1.0f);
}

