package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class UnderWaterPP extends AbstractShader{

	private final static String vs ="res/shaders/vsUnderWaterPP.glsl";
	private final static String fs ="res/shaders/fsUnderWaterPP.glsl";
	
	private int transformation;
	public UnderWaterPP() {
		super(vs, fs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateUniformLocation() {
		// TODO Auto-generated method stub
		transformation = super.getUniformLocation("model");
	}

	public void loadTranformationMatrix(Matrix4f matrix)
	{
		super.useProgram();
		super.uploadMat4f(transformation, matrix);
		
	}
	
	@Override
	protected void attributes() {
		// TODO Auto-generated method stub
		super.bindAttribLocation(0, "position");
		
	}
	
	@Override
	protected String getShaderType() {
		// TODO Auto-generated method stub
		return "Under water PP";
	}
	
	
}