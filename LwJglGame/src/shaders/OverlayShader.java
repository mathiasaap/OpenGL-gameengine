package shaders;

import org.lwjgl.util.vector.Matrix4f;

public class OverlayShader extends AbstractShader{

	private final static String vs ="res/shaders/vsOverlay.glsl";
	private final static String fs ="res/shaders/fsOverlay.glsl";
	
	private int transformation;
	public OverlayShader() {
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
		return "Overlay";
	}

	@Override
	public void updateTick() {
		// TODO Auto-generated method stub
		
	}
	
	
}
