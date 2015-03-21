package player;


import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.MeshShader;
import shaders.TerrainShader;
import terrain.TerrainCollision;
import matrix.Camera;

public class Player {

	private Vector3f position,velocity;
	private Vector2f lookat;
	private boolean firstPerson=true;
	private float headHeight=15;
	private long gravityClock=System.currentTimeMillis();
	private Camera camera;
	
	public Player(Vector3f position)
	{
		this.position=position;
		lookat=new Vector2f(0f,0f); //pitch/yaw
		this.velocity=new Vector3f(0,0,0);
		camera= new Camera();
		
	}
	public Camera getCamera(MeshShader meshShader, TerrainShader terrainShader)
	{
		camera.setPosition(position);
		updateCamera(meshShader, terrainShader);
		return camera;
	}
	
	private void updateCamera(MeshShader meshShader, TerrainShader terrainShader)
	{
		camera.uploadViewMatrix(meshShader, terrainShader);
	}
	
	
	public void move()
	{
	gravity();
	position.y+=velocity.y;
	camera.setLookat(lookat);
	camera.cameraIsChanged();
	
	}
	
	public Vector3f getPosition()
	{
		return position;
	}
	public void jump()
	{
		if(velocity.y==0)
		{
			velocity.y=4;
		}
	}
	public void stopFalling()
	{
		velocity.y=0;
	}
	public float getHeadHeight()
	{
		return headHeight;
	}
	public Vector2f getLookat()
	{
		return lookat;
	}
	private void gravity()
	{
		double deltaTime=(System.currentTimeMillis()-gravityClock)/1000.0;
		velocity.y-=10*deltaTime;
		gravityClock=System.currentTimeMillis();
	}
	
}
