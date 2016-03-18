package mathiasap.cardboard.shaders;


import java.io.InputStreamReader;

public class UnderWaterPP extends AbstractShader{

	private final static String vs ="res/shaders/vsUnderWaterPP.glsl";
	private final static String fs ="res/shaders/fsUnderWaterPP.glsl";
	
	private int transformation;
	private int timeOffsetLoc;
	
	private long time=System.currentTimeMillis();
	private float timeOffset=0;
	public UnderWaterPP(InputStreamReader vs, InputStreamReader fs) {
		super(vs, fs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateUniformLocation() {
		// TODO Auto-generated method stub
		transformation = super.getUniformLocation("model");
		timeOffsetLoc = super.getUniformLocation("timeOffset");
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

	public void updateTimeOffset()
	{
		float deltaTime=(float)((System.currentTimeMillis()-time)/1000.0);
		time=System.currentTimeMillis();
		timeOffset+=5*deltaTime;
		/*while(timeOffset>2*Math.PI)
		{
			timeOffset-=2*Math.PI;
		}*/
		//System.out.println(timeOffset);
		uploadFloat(timeOffsetLoc,timeOffset);
		
	}
	
	@Override
	protected String getShaderType() {
		// TODO Auto-generated method stub
		return "Under water PP";
	}

	@Override
	public void updateTick() {
		useProgram();
		updateTimeOffset();
		
	}
	
	
}
