package mathiasap.cardboard.shaders;


import java.io.InputStreamReader;

import mathiasap.cardboard.misc.Vektor4f;

public class MeshShader extends AbstractShader {

	private int transformation;
	private int projectionMatrix;
	private int viewMatrix;
	private int lightPosition;
	private int lightColor;
	private int reflectivity;
	private int shine;
	private int clipP;
	
	public MeshShader(InputStreamReader vs, InputStreamReader fs) {
		super(vs, fs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void attributes() {
		// TODO Auto-generated method stub
		super.bindAttribLocation(0, "position");
		super.bindAttribLocation(1, "texCoord");
		super.bindAttribLocation(2, "normal");
		
	}

	@Override
	protected void updateUniformLocation() {
		transformation= super.getUniformLocation("model");
		projectionMatrix= super.getUniformLocation("projection");
		viewMatrix= super.getUniformLocation("view");
		lightPosition=super.getUniformLocation("lightPos");
		lightColor=super.getUniformLocation("lightCol");
		reflectivity=super.getUniformLocation("reflectivity");
		shine=super.getUniformLocation("shine");
		clipP=super.getUniformLocation("clipP");
		
	}
	public void loadTranformationMatrix(float[] matrix)
	{
		super.uploadMat4f(transformation, matrix);
	}

	public void loadProjectionMatrix(float[] matrix)
	{
		
		super.uploadMat4f(projectionMatrix, matrix);
	}
/*	public void uploadLight(Light light){
		super.uploadVec3f(lightPosition, light.getPosition());
		super.uploadVec3f(lightColor, light.getColor());
	}*/
	public void uploadSpecular(float shine, float reflectivity)
	{
		super.uploadFloat(this.shine, shine);
		super.uploadFloat(this.reflectivity, reflectivity);
		
	}
	public void uploadClipPlane(Vektor4f plane)
	{
		super.uploadVec4f(clipP, plane);
	}
	
	public void loadViewMatrix(float[] matrix)
	{
		
		super.uploadMat4f(viewMatrix, matrix);
	}

	@Override
	protected String getShaderType() {
		// TODO Auto-generated method stub
		return "Mesh";
	}

	@Override
	public void updateTick() {
		// TODO Auto-generated method stub
		
	}
}
