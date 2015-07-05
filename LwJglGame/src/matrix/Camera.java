package matrix;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.WaterShader;

public class Camera {
private Vector3f position;
private float pitch;
private float yaw;
private float roll;
private boolean changed=true;
public Camera() {
	position=new Vector3f(0,0,0);
	pitch=yaw=roll=0;
	
}




public Matrix4f setupViewMatrix()
{
	Matrix4f viewMatrix= new Matrix4f();
	viewMatrix.setIdentity();
	Matrix4f.rotate((float) Math.toRadians(this.getPitch()),new Vector3f(1,0,0),viewMatrix,viewMatrix);
	Matrix4f.rotate((float) Math.toRadians(this.getYaw()),new Vector3f(0,1,0),viewMatrix,viewMatrix);
	Vector3f negCam = new Vector3f(-this.position.x,-this.position.y,-this.position.z);
	Matrix4f.translate(negCam,viewMatrix,viewMatrix);
	
	return viewMatrix;	
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

public Vector3f getPosition() {
	return position;
}
public void setLookat(Vector2f lookat)
{
	pitch=lookat.x;
	yaw=lookat.y;
}

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
public void setPosition(Vector3f position)
{
this.position=position;
}


	
}