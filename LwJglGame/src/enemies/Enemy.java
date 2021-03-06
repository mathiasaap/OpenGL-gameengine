package enemies;

import java.util.ArrayList;

import mesh.MeshInstance;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import player.Player;
import terrain.TerrainCollision;

public abstract class Enemy {

	public static ArrayList<Enemy> enemies= new ArrayList<>();
	
	protected Vector3f position,velocity;
	protected Player player;
	protected boolean alive=true;
	protected MeshInstance mInstance;
	protected float headHeight;
	protected long time= System.currentTimeMillis();
	protected float speed=20;
	protected float hitRadius;
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
		if(alive){
		double deltaTime= (System.currentTimeMillis()-time)/1000.0;
		time=System.currentTimeMillis();
		Vector2f enemy2player=getPlayerVec();
		gravity(deltaTime);
		position.x+=enemy2player.x*speed*deltaTime;
		position.z+=enemy2player.y*speed*deltaTime;
		position.y+=velocity.y;
		mInstance.setPosition(position);
		mInstance.setRotY((float)(Math.toDegrees(Math.atan2(enemy2player.x, enemy2player.y))));

		
		}
		
		
	}
	
	public boolean bulletCollision(Vector3f x1, Vector3f direction)
	{
		if(alive){
		Vector3f x0=new Vector3f(position.x,position.y+5*headHeight,position.z);
		//x0.y+=5*headHeight;
		Vector3f dist= Vector3f.sub(x1, x0, null);
		
		float length= dist.length();
		if(length>5000)
		{
			return false;
		}
		Vector3f crossVec=Vector3f.cross(direction, dist, null);
		float top=crossVec.length();
		float bottom=direction.length();
		float d= top/bottom;
		//System.out.println(d);
		if(d<=hitRadius)
		{
			die();
			return false;
		}
		}
		return false;
	}
	
	public void stopFalling()
	{
		velocity.y=0;
	}
	protected void die() {
		alive=false;
	}
	
	public boolean getAlive()
	{
		return alive;
	}
	
	
	private void gravity(double deltaTime)
	{
		velocity.y-=60*deltaTime;
	}
	
	private Vector2f getPlayerVec()
	{
		Vector2f enemy2player = new Vector2f(player.getPosition().x-position.x,player.getPosition().z-position.z);
		double len=Math.sqrt((enemy2player.x*enemy2player.x+enemy2player.y*enemy2player.y));
		//System.out.println(len);
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
