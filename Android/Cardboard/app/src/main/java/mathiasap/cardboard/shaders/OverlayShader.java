package mathiasap.cardboard.shaders;


import java.io.InputStreamReader;

public class OverlayShader extends AbstractShader{

	private final static String vs ="res/shaders/vsOverlay.glsl";
	private final static String fs ="res/shaders/fsOverlay.glsl";
	
	private int transformation;
	public OverlayShader(InputStreamReader vs, InputStreamReader fs) {
		super(vs, fs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateUniformLocation() {
		// TODO Auto-generated method stub
		transformation = super.getUniformLocation("model");
	}

	public void loadTranformationMatrix(float[] matrix)
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
	public void loadProjectionMatrix(float[] matrix) {

	}

	@Override
	public void loadViewMatrix(float[] matrix) {

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
