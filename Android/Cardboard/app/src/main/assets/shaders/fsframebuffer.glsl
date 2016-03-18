#version 330 core

out vec4 out_Color;
in vec2 tex;
uniform sampler2D textureSampler;
uniform float gamma;
#define WIDTH 1000
#define HEIGHT 750


void main()
{
vec2 pixCoord =gl_FragCoord.xy;
vec4 color= texture(textureSampler,tex);
float intensity=(color.x+color.y+color.z)/3;

/*
if(intensity>0.36){
color.x=color.y=color.z=1;
}
else if(intensity>0.15){
color.x=1;
color.y=color.z=0;
}
else{
color.x=color.y=color.z=0;
}*/


color*=gamma;
out_Color=color;

}

