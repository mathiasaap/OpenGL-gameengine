package mathiasap.cardboard.matrix;

import android.opengl.Matrix;


import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;

import mathiasap.cardboard.misc.Vektor3f;
import mathiasap.cardboard.shaders.MeshShader;
import mathiasap.cardboard.shaders.TerrainShader;
import mathiasap.cardboard.shaders.WaterShader;

public class Camera {
private Vektor3f position;
private float pitch;
private float yaw;
private float roll;
private boolean changed=true;





	private float[] cameraMatrix = new float[16];
	private float[] headView = new float[16];
	private float[] viewMatrix = new float[16];
	private float[] perspectiveMatrix = new float[16];

	private float CAMERA_Z = 0.01f;




public Camera() {
	position=new Vektor3f(0,300,0);
	//pitch=yaw=roll=0;
	Matrix.setLookAtM(cameraMatrix, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
}




public float[] setupViewMatrix()
{
	float[] matrix= new float[16];
	Matrix.setIdentityM(matrix,0);

	/*Matrix4f.rotate((float) Math.toRadians(this.getPitch()),new Vector3f(1,0,0),viewMatrix,viewMatrix);
	Matrix4f.rotate((float) Math.toRadians(this.getYaw()),new Vector3f(0,1,0),viewMatrix,viewMatrix);
	Vector3f negCam = new Vector3f(-this.position.x,-this.position.y,-this.position.z);
	Matrix4f.translate(negCam,viewMatrix,viewMatrix);*/
	
	return matrix;
}

	public void updateCamera(HeadTransform headTransform)
	{
		headTransform.getHeadView(headView, 0);
	}
	public void updateView(Eye eye)
	{

		Matrix.multiplyMM(viewMatrix, 0, eye.getEyeView(), 0, cameraMatrix, 0);
		perspectiveMatrix = eye.getPerspective(Matrices.NEAR,Matrices.FAR);

		Matrix.translateM(viewMatrix,0,-position.x,-position.y,-position.z);
	}


	public float[] getCameraMatrix()
	{

		return cameraMatrix;
	}

	public float[] getHeadView()
	{
		return headView;
	}

	public float[] getViewMatrix()
	{
		return viewMatrix;
	}

	public float[] getPerspectiveMatrix()
	{
		return perspectiveMatrix;
	}

public void uploadViewMatrix(MeshShader meshShader, TerrainShader terrainShader, WaterShader waterShader)
{
	if(this.changed){
	meshShader.useProgram();
	meshShader.loadViewMatrix(setupViewMatrix());
	meshShader.unbindShader();
	terrainShader.useProgram();
	terrainShader.loadViewMatrix(setupViewMatrix());
	terrainShader.unbindShader();
	waterShader.useProgram();
	waterShader.loadViewMatrix(setupViewMatrix());
	waterShader.unbindShader();
	this.changed=false;
	}
}

public void cameraIsChanged()
	{
		this.changed=true;
	}

public Vektor3f getPosition() {
	return position;
}
/*public void setLookat(Vektor2f lookat)
{
	pitch=lookat.x;
	yaw=lookat.y;
}*/

public float getPitch() {
	return pitch;
}
public void invertPitch()
{
	cameraIsChanged();
pitch=-pitch;	
}
public float getYaw() {
	return yaw;
}
public float getRoll() {
	return roll;
}
public void setPosition(Vektor3f position)
{
this.position=position;
}


	
}