#version 330 core

out vec4 out_Color;
in vec2 tex;
uniform sampler2D textureSampler;
uniform float timeOffset;
#define WIDTH 1/1000
#define HEIGHT 1/750

//#define SHROOMS

void main()
{
vec2 pixCoord =gl_FragCoord.xy;
vec2 newCoord=vec2(tex.x,tex.y);


#ifdef SHROOMS
float blendpix=sin(0.2*(2*timeOffset+1)*tex.y)*0.01;
newCoord.x+=blendpix;
blendpix=sin(0.2*(timeOffset+1)*tex.x)*0.01;
newCoord.y+=blendpix;
#endif

#ifndef SHROOMS
float blendpix=sin(timeOffset+tex.y)*0.05;

//if(blendpix+tex.x>=0.0f&&blendpix+tex.x<1.0f){
newCoord.x+=blendpix;
//}

blendpix=sin(0.5*timeOffset+tex.x)*0.05;
//if(blendpix+tex.y>=0.0f&&blendpix+tex.y<1.0f){
newCoord.y+=blendpix;
//}
#endif

vec4 color= texture(textureSampler,newCoord);



color.x-=0.2;
color.y-=0.1;
out_Color=color;

}

