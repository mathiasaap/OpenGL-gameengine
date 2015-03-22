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

if(height>700&&height<950)
{
	snowComp=(height-700.0f)/250.0f;
	rockComp=1.0f-snowComp;
}
else if(height>50&&height<140)
{
	rockComp=(height-50.0f)/90.0f;
	grassComp=1.0f-rockComp;
}
else if(height<-60&&height>-150)
{
	rockComp=(height+60.0f)/(-90.0f);
	grassComp=1.0f-rockComp;
}
else
{
	if(height>950)
	{
		snowComp=1.0f;
	}
	else if(height>140)
	{
		rockComp=1.0f;
	}
	else if(height>-60)
	{
		grassComp=1.0f;
	}
	else
	{
		rockComp=1.0f;
	}
}

multitextureComponents= vec3(grassComp,rockComp,snowComp);

texToFrag=texture;

absNormal = (model*vec4(normal,0)).xyz;
toLight= lightPos-(modelPosition).xyz;
camVector=( inverse(view)* vec4(0,0,0,1)).xyz -modelPosition.xyz;
}