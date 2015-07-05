#version 330 core

in vec2 texToFrag;
in vec3 absNormal;
in vec3 toLight;
in vec3 camVector;
in vec3 toCamera;

in vec4 clipSpace;
out vec4 out_color;
uniform vec2 dudvOffset;//fra water-klassen

uniform vec3 lightCol;
uniform float shine;
uniform float reflectivity;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvTexture;

void main()
{
vec3 unitNormal= normalize(absNormal);
vec3 unitToLight= normalize(toLight);
vec3 unitCam = normalize(camVector);

float dotprod = dot(unitNormal,unitToLight);
float brightness = max(dotprod,0.3);
vec3 diffuse=brightness*lightCol;
vec3 reflectedDir=reflect(-unitCam,unitNormal);
float specularity = max(dot(reflectedDir,unitNormal),0);
float shineFactor = pow(specularity, shine);
vec3 specularProduct = shineFactor*lightCol*reflectivity;
vec3 normalizedToCamera=normalize(toCamera);


vec4 clipSpace2=clipSpace;
vec2 coordsWithOffset=vec2(texToFrag);
coordsWithOffset+=dudvOffset;

clipSpace2.x+=texture(dudvTexture,coordsWithOffset).x;
clipSpace2.y+=texture(dudvTexture,coordsWithOffset).x*2;
vec2 ndc = (clipSpace2.xy/clipSpace2.w)/2.0f+0.5f;

vec2 reflectedNdc=vec2(ndc.x,-ndc.y);



//out_color=vec4(specularProduct,1)* vec4(diffuse,1) * vec4(28.0f/255.0f,107.0f/255.0f,180.0f/255.0f,0.8f);
//out_color=vec4(0.0f,0.0f,0.5f,0.4f);
//out_color= vec4(diffuse,1.0f) * vec4(0.0f,0.0f,0.7f,0.4f);
//out_color=texture(texSampler,texToFrag);
//out_color=vec4(0.9f,0.1f,0.2f,1.0f);

out_color=vec4(diffuse,1)*mix(mix(texture(reflectionTexture,reflectedNdc),texture(refractionTexture,ndc),(dot(normalizedToCamera,vec3(0.0, 1.0, 0.0))) ),vec4(0, 0.2 , 0.8 , 1.0 ),0.06);
//out_color=texture(reflectionTexture,reflectedNdc);
}

