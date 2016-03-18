package mathiasap.cardboard.shaders;


import java.io.InputStream;
import java.io.InputStreamReader;

public class FramebufferShader extends AbstractShader{
	
	private int transformation;
	private int gammaLoc;
	private float gamma=0.0f;
	private long gammaClock=System.currentTimeMillis();
	public FramebufferShader(InputStreamReader vs, InputStreamReader fs) {
		super(vs, fs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateUniformLocation() {
		// TODO Auto-generated method stub
		useProgram();
		transformation = super.getUniformLocation("model");
		gammaLoc = super.getUniformLocation("gamma");
		
		uploadFloat(gammaLoc, 1);
		System.out.println(gammaLoc);
		unbindShader();
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
		return "Framebuffer";
	}
	public void setGamma(float gamma)
	{
		if(gamma>=1){
		this.gamma=gamma;
		gammaClock=System.currentTimeMillis();
		}
		
	}

	@Override
	public void updateTick() {
		// TODO Auto-generated method stub
		useProgram();
		float deltaTime=1+System.currentTimeMillis()-gammaClock;
		float currentGamma=(float) Math.exp(gamma/(float)(deltaTime/1000.0))/2;

		if(currentGamma>=1.0)
		super.uploadFloat(gammaLoc, currentGamma);
		
		unbindShader();
	}
	
	
	
}
