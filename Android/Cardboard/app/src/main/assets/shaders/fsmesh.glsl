#version 300 es
precision mediump float;
out vec4 out_color;
in vec2 texToFrag;
/*
attribute vec3 absNormal;
attribute vec3 toLight;
attribute vec3 camVector;
*/
//varying vec4 out_color;

uniform sampler2D texSampler;
//uniform vec3 lightCol;
//uniform float shine;
//uniform float reflectivity;

void main()
{


//vec3 unitNormal= normalize(absNormal);
//vec3 unitToLight= normalize(toLight);
//vec3 unitCam = normalize(camVector);

//float dotprod = dot(unitNormal,unitToLight);
//float brightness = max(dotprod,0.2);
//vec3 diffuse=brightness*lightCol;
//vec3 reflectedDir=reflect(-unitCam,unitNormal);
//float specularity = max(dot(reflectedDir,unitNormal),0);
//float shineFactor = pow(specularity, shine);
//vec3 specularProduct = shineFactor*lightCol*reflectivity;




//out_color =vec4(1.0f,0.0f,0.0f,1.0f);

out_color =vec4(1.0f,1.0f,1.0f,1.0f)* texture(texSampler,texToFrag);
//gl_FragColor=vec4(specularProduct,1)* vec4(diffuse,1) * texture(texSampler,texToFrag);

}

