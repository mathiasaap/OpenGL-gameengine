package player;

import java.util.List;

import lighting.Light;
import matrix.Matrix;
import misc.RayCasting;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import enemies.Enemy;
import overlays.Sniper;
import rendering.DisplayWindow;
import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.WaterShader;

public class Controls {

	State state = State.PLAYING;
	private Player player;
	private Sniper sniper;
	
	private MeshShader meshShader;
	private TerrainShader terrainShader;
	private WaterShader waterShader;
	private short shot=0;
	private long shotTime=System.currentTimeMillis();
	
	private List<Enemy> enemies;
	private RayCasting mousecast;
	private long time= System.currentTimeMillis();
	private float speed=(float) 50.0;
	
	private boolean F1=false,F1Int=false;
	private boolean F2=false,F2Int=false;
	private boolean F4=false,F4Int=false;
	private boolean F5=false,F5Int=false;
	private Light light;
	
	public boolean getF1()
	{
		return F1;
	}
	public boolean getF2()
	{
		return F2;
	}
	
	public boolean getF4()
	{
		return F4;
	}
	public boolean getF5()
	{
		return F5;
	}
	public Controls()
	{
	
	}
	public Controls(Player player,Sniper sniper,List<Enemy> enemies, MeshShader meshShader, TerrainShader terrainShader,WaterShader waterShader,Light light)
	{
		this.player=player;
		this.sniper=sniper;
		this.enemies=enemies;
		this.meshShader=meshShader;
		this.terrainShader=terrainShader;
		this.waterShader=waterShader;
		this.light=light;
		mousecast=new RayCasting(player.getCamera(),Matrix.calcProjectionMatrix());
		/*try {
			Mouse.setNativeCursor(new Cursor(0, 0, 0, 0, 0, null, null));
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
	
	public void playing()
	{
		double deltaTime= (System.currentTimeMillis()-time)/1000.0;
		time=System.currentTimeMillis();
		Vector2f lookat=player.getLookat();
		Vector3f position = player.getPosition();
		rotateCamera(lookat);
		boolean runningSpeed=false;
		updateShoot();
		//position.x=100000;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)&&Keyboard.isKeyDown(Keyboard.KEY_E))
		{
			//speed=3000;
			speed=2000*5;
			if(!meshShader.getPlayerRunning())
			{
				meshShader.setPlayerRunning(true);
				Matrix.uploadProjectionMatrix(meshShader, 120);
			}
			if(!terrainShader.getPlayerRunning())
			{
				terrainShader.setPlayerRunning(true);
				Matrix.uploadProjectionMatrix(terrainShader, 120);
			}
			if(!waterShader.getPlayerRunning())
			{
				waterShader.setPlayerRunning(true);
				Matrix.uploadProjectionMatrix(waterShader, 120);
			}
			
		}
		else if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		{
			speed=250;
			if(meshShader.getPlayerRunning())
			{
				meshShader.setPlayerRunning(false);
				Matrix.uploadProjectionMatrix(meshShader, 70);
			}
			if(terrainShader.getPlayerRunning())
			{
				terrainShader.setPlayerRunning(false);
				Matrix.uploadProjectionMatrix(terrainShader, 70);
			}
			if(waterShader.getPlayerRunning())
			{
				waterShader.setPlayerRunning(false);
				Matrix.uploadProjectionMatrix(waterShader, 70);
			}
			
			
		}
		else
		{
			speed=100;
			if(meshShader.getPlayerRunning())
			{
				meshShader.setPlayerRunning(false);
				Matrix.uploadProjectionMatrix(meshShader, 70);
			}
			if(terrainShader.getPlayerRunning())
			{
				terrainShader.setPlayerRunning(false);
				Matrix.uploadProjectionMatrix(terrainShader, 70);
			}
			if(waterShader.getPlayerRunning())
			{
				waterShader.setPlayerRunning(false);
				Matrix.uploadProjectionMatrix(waterShader, 70);
			}
		}
		

		if(Keyboard.isKeyDown(Keyboard.KEY_F1))
		{
			if(!F1Int)
			{
				F1Int=true;
				F1=!F1;
				System.out.println("F1");
			}
		}
		else
		{
			F1Int=false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F2))
		{
			if(!F2Int)
			{
				F2Int=true;
				F2=!F2;
				System.out.println("F2");
			}
		}
		else
		{
			F2Int=false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F4))
		{
			if(!F4Int)
			{
				F4Int=true;
				F4=!F4;
				System.out.println("F4");
			}
		}
		else
		{
			F4Int=false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F5))
		{
			if(!F5Int)
			{
				F5Int=true;
				F5=!F5;
				System.out.println("F5");
			}
		}
		else
		{
			F5Int=false;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_F3))
		{
			light.setPosition(new Vector3f(position.x,position.y,position.z));
			System.out.println("changed lightpos");
		}
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z-=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
			position.x+=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
			sniper.walk();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x+=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
			position.z+=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
			sniper.walk();
			
		}
	if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
		position.z+=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
		position.x-=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
		sniper.walk();
		}
	if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
		position.x-=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
		position.z-=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
		sniper.walk();
		}
	
	if(Keyboard.isKeyDown(Keyboard.KEY_R))
	{
		sniper.reload();
	}
	
	if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
	{
		player.jump();
	}
	
	/*if(Keyboard.isKeyDown(Keyboard.KEY_Z))
	{
		position.y-=speed*deltaTime;
	}
	if(Keyboard.isKeyDown(Keyboard.KEY_X))
	{
		position.y+=speed*deltaTime;
	}*/
	if(Mouse.isButtonDown(0))
	{
		if(shot==0){
		/*mousecast.update();
		Vector3f ray=mousecast.getRay();
		for(Enemy enemy:enemies)
		{
			enemy.bulletCollision(player.getPosition(), ray);
				
		}*/
		shotTime=System.currentTimeMillis();
		shot=1;
		sniper.shoot();
	}}
		
	}
	
	public void updateShoot()
	{
		if(shot==1)
		{	
			if((System.currentTimeMillis()-shotTime)>sniper.getFrameTime()*8)
			{
				
				mousecast.update();
				Vector3f ray=mousecast.getRay();
				for(Enemy enemy:enemies)
				{
					enemy.bulletCollision(player.getPosition(), ray);
						
				}
				shot=2;
			}
		}
		else if(shot==2)
		{
			if((System.currentTimeMillis()-shotTime)>sniper.getFrameTime()*25)
			{
				shot=0;
				
			}
		}
		
	}
	
	private void rotateCamera(Vector2f pitchyaw)
	{
		pitchyaw.x-=Mouse.getDY();
		pitchyaw.y+=Mouse.getDX();
		if(pitchyaw.x<-80)pitchyaw.x=-80;
		else if(pitchyaw.x>80)pitchyaw.x=80;
		if(pitchyaw.y<0)pitchyaw.y+=360;
		else if(pitchyaw.y>360)pitchyaw.y-=360;
	}
	
}
