package overlays;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import rendering.LoadMesh;
import textures.OverlayTexture;

public class Sniper {

	private List<OverlayTexture> frames= new ArrayList<>();
	private LoadMesh loader;
	private Vector2f pos= new Vector2f(0.598f,-0.420f);
	private Vector2f scale= new Vector2f(1.598f,1.420f);
	private long animationClock=System.currentTimeMillis();
	private int frameTime=25;
	
	private enum STATE {STILL, WALKING, SHOOT, RELOAD};
	private STATE state= STATE.STILL;
	
	public Sniper(LoadMesh loader)
	{
		this.loader=loader;
		loadTextures();
	}
	
	private void loadTextures()
	{
		for(int i=1;i<18;i++)
		{
			frames.add(new OverlayTexture(loader.loadTexture("/sniper/shoot/"+i),pos,scale));
		}
	}
	
	public void shoot()
	{
		animationClock=System.currentTimeMillis();
		state=STATE.SHOOT;
	}
	
	public OverlayTexture getFrame()
	{
		long deltaTime=System.currentTimeMillis()-animationClock;
		switch(state){
		case SHOOT:
			if(deltaTime>28*frameTime)
			{
				state=STATE.STILL;
			}
			return getShootFrame();
			
		
		}
		return frames.get(0);
		
	}
	private OverlayTexture getShootFrame()
	{
		long deltaTime=System.currentTimeMillis()-animationClock;
		for(int i=0;i<7;i++){
		if(deltaTime<frameTime*(i+1)){
			return frames.get(i);
		}
			
		}
		
		if(deltaTime<(20*frameTime)){
			return frames.get(7);
		}
		
		for(int i=0;i<8;i++){
			if(deltaTime-(20*frameTime)<(frameTime*(i+1))){
				return frames.get(i+8);
			}
				
			}
		return frames.get(0);
	}
	
}
