package enemies;

import mesh.MeshInstance;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import player.Player;
import terrain.TerrainCollision;

public class Enemy {

	protected Vector3f position,velocity;
	protected Player player;
	protected boolean alive=true;
	protected MeshInstance mInstance;
	protected float headHeight;
	protected long time= System.currentTimeMillis();
	protected float speed=20;
	TerrainCollision terrainCollision = new TerrainCollision();
	
	public Enemy(Vector3f position, Player player, MeshInstance mInstance)
	{
		this.position=position;
		this.velocity=new Vector3f(0f,0f,0f);
		this.player=player;
		this.mInstance=mInstance;
		
	}
	
	
	public void move()
	{
		double deltaTime= (System.currentTimeMillis()-time)/1000.0;
		time=System.currentTimeMillis();
		Vector2f enemy2player=getPlayerVec();
		gravity(deltaTime);
		position.x+=enemy2player.x*speed*deltaTime;
		position.z+=enemy2player.y*speed*deltaTime;
		position.y+=velocity.y;
		mInstance.setPosition(position);
		
	}
	
	public void stopFalling()
	{
		velocity.y=0;
	}
	
	
	private void gravity(double deltaTime)
	{
		velocity.y-=10*deltaTime;
	}
	
	private Vector2f getPlayerVec()
	{
		Vector2f enemy2player = new Vector2f(player.getPosition().x-position.x,player.getPosition().z-position.z);
		double len=Math.sqrt((enemy2player.x*enemy2player.x+enemy2player.y*enemy2player.y));
		System.out.println(len);
		enemy2player.x/=len;
		enemy2player.y/=len;
		return enemy2player;
	}
	public Vector3f getPosition()
	{
		return position;
	}
	
	public float getHeadHeight()
	{
		return headHeight;
		
	}
	
	public MeshInstance getEnemyMeshInstance()
	{
		return mInstance;
	}
	
}
