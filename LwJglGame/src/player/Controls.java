package player;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import rendering.DisplayWindow;

public class Controls {

	State state = State.PLAYING;
	private Player player;
	private long time= System.currentTimeMillis();
	private float speed=(float) 50.0;
	public Controls(Player player)
	{
		this.player=player;
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
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			position.z-=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
			position.x+=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			position.x+=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
			position.z+=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
			
		}
	if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
		position.z+=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
		position.x-=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
		}
	if(Keyboard.isKeyDown(Keyboard.KEY_A))
		{
		position.x-=Math.cos(Math.toRadians(lookat.y))*speed*deltaTime;
		position.z-=Math.sin(Math.toRadians(lookat.y))*speed*deltaTime;
		}
	
	if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
	{
		player.jump();
	}
	
	if(Keyboard.isKeyDown(Keyboard.KEY_Z))
	{
		position.y-=speed*deltaTime;
	}
	if(Keyboard.isKeyDown(Keyboard.KEY_X))
	{
		position.y+=speed*deltaTime;
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
