#version 330 core

in vec3 position;
in vec2 texture;
in vec3 normal;

out vec2 texToFrag;
out vec3 absNormal;
out vec3 toLight;
out vec3 camVector;
out float height;
out vec3 multitextureComponents;

uniform mat4 model;
uniform mat4 projection;
uniform mat4 view;
uniform vec3 lightPos;


void main()
{
vec4 modelPosition = model * vec4(position, 1.0);
gl_Position= projection*view*modelPosition;
height=modelPosition.y;

float grassComp=0.0f,rockComp=0.0f, snowComp=0.0f;

if(height>850&&height<900)
{
	snowComp=(height-850.0f)/50.0f;
	rockComp=1.0f-snowComp;
}
else if(height>120&&height<180)
{
	rockComp=(height-120.0f)/60.0f;
	grassComp=1.0f-rockComp;
}
else if(height<-180&&height>-200)
{
	grassComp=(height+180.0f)/(-20.0f);
	rockComp=1.0f-grassComp;
}
else
{
	if(height>900)
	{
		snowComp=1.0f;
	}
	else if(height>180)
	{
		rockComp=1.0f;
	}
	else if(height>-180)
	{
		grassComp=1.0f;
	}
	else
	{
		rockComp=1.0f;
	}
}

multitextureComponents= vec3(grassComp,rockComp,snowComp);

texToFrag=texture*100;

absNormal = (model*vec4(normal,0)).xyz;
toLight= lightPos-(modelPosition).xyz;
camVector=( inverse(view)* vec4(0,0,0,1)).xyz -modelPosition.xyz;
}