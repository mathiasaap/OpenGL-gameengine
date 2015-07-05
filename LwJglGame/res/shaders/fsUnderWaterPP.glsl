#version 330 core

out vec4 out_Color;
in vec2 tex;
uniform sampler2D textureSampler;
uniform float timeOffset;
#define WIDTH 1/1000
#define HEIGHT 1/750


void main()
{
vec2 pixCoord =gl_FragCoord.xy;
vec2 newCoord=vec2(tex.x,tex.y);


float blendpix=sin(timeOffset+tex.y)*0.08;

if(tex.x>=0.5f){
newCoord.x+=blendpix*(1-tex.x);
}
else{
newCoord.x+=blendpix*(tex.x);
}
blendpix=sin(0.5*timeOffset+tex.x)*0.08;

if(tex.y>=0.5f){
newCoord.y+=blendpix*(1-tex.y);
}
else{
newCoord.y+=blendpix*(tex.y);
}

float gamma = 2.0;
vec4 color= texture(textureSampler,newCoord);
float intensity=(color.x+color.y+color.z)/3;
color.z=intensity*0.8;
color.y=0.25*intensity;
color.x=0.20*intensity;


out_Color=color*gamma;

}

