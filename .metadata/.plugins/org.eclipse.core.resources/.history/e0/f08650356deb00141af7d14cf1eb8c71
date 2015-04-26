#version 330 core

in vec2 texToFrag;
in vec3 absNormal;
in vec3 toLight;
in vec3 camVector;

out vec4 out_color;

uniform vec3 lightCol;
uniform float shine;
uniform float reflectivity;

void main()
{
vec3 unitNormal= normalize(absNormal);
vec3 unitToLight= normalize(toLight);
vec3 unitCam = normalize(camVector);

float dotprod = dot(unitNormal,unitToLight);
float brightness = max(dotprod,0.2);
vec3 diffuse=brightness*lightCol;
vec3 reflectedDir=reflect(-unitCam,unitNormal);
float specularity = max(dot(reflectedDir,unitNormal),0);
float shineFactor = pow(specularity, shine);
vec3 specularProduct = shineFactor*lightCol*reflectivity;


out_color=vec4(specularProduct,1)* vec4(diffuse,1) * vec4(0.0f,0.0f,0.7f,0.6f);
//out_color=vec4(0.0f,0.0f,0.5f,0.4f);
//out_color= vec4(diffuse,1.0f) * vec4(0.0f,0.0f,0.7f,0.4f);
//out_color=texture(texSampler,texToFrag);
//out_color=vec4(0.9f,0.1f,0.2f,1.0f);
}

