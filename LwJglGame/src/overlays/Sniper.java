package overlays;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import rendering.LoadMesh;
import textures.OverlayTexture;

public class Sniper {

	private List<OverlayTexture> shootFrames= new ArrayList<>();
	private List<OverlayTexture> walkFrames= new ArrayList<>();
	private List<OverlayTexture> reloadFrames= new ArrayList<>();
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
	public int getFrameTime()
	{
		return frameTime;
	}
	
	private void loadTextures()
	{
		for(int i=1;i<18;i++)
		{
			shootFrames.add(new OverlayTexture(loader.loadTexture("/sniper/shoot/"+i),pos,scale));
		}
		
		for(int i=1;i<19;i++)
		{
			walkFrames.add(new OverlayTexture(loader.loadTexture("/sniper/walk/"+i),pos,scale));
		}
		
		for(int i=1;i<26;i++)
		{
			reloadFrames.add(new OverlayTexture(loader.loadTexture("/sniper/reload/"+i),pos,scale));
		}
	}
	
	public void shoot()
	{
		if(state!=STATE.RELOAD&&state!=STATE.SHOOT){
		animationClock=System.currentTimeMillis();
		state=STATE.SHOOT;
		}
	}
	public void reload()
	{
		if(state!=STATE.RELOAD&&state!=STATE.SHOOT){
		animationClock=System.currentTimeMillis();
		state=STATE.RELOAD;
		}
	}
	
	public void walk()
	{
		
		if(state==STATE.WALKING)
		{
			if((System.currentTimeMillis()-animationClock)>17*frameTime)
			{
				animationClock=System.currentTimeMillis();
			}
			
		}
		
		else if(state!=STATE.SHOOT&&state!=STATE.RELOAD)
		{
			animationClock=System.currentTimeMillis();
			state=STATE.WALKING;
			
		}
	}
	
	public OverlayTexture getFrame()
	{
		long deltaTime=System.currentTimeMillis()-animationClock;
		switch(state){
		case SHOOT:
			if(deltaTime>25*frameTime)
			{
				state=STATE.STILL;
			}
			return getShootFrame();
		case RELOAD:
			if(deltaTime>26*frameTime)
			{
				state=STATE.STILL;
			}
			return getReloadFrame(deltaTime);
		case WALKING:
			if(deltaTime>19*frameTime)
			{
				state=STATE.STILL;
			}
			return getWalkFrame(deltaTime);
		
		}
		return shootFrames.get(0);
		
	}
	private OverlayTexture getShootFrame()
	{
		long deltaTime=System.currentTimeMillis()-animationClock;
		for(int i=0;i<7;i++){
		if(deltaTime<frameTime*(i+1)){
			return shootFrames.get(i);
		}
			
		}
		
		if(deltaTime<(14*frameTime)){
			return shootFrames.get(7);
		}
		
		for(int i=0;i<8;i++){
			if(deltaTime-(14*frameTime)<(frameTime*(i+1))){
				return shootFrames.get(i+8);
			}
				
			}
		return shootFrames.get(0);
	}
	
	private OverlayTexture getWalkFrame(long deltaTime)
	{
		for(int i=0;i<18;i++){
			if(deltaTime<frameTime*(i+1)){
				return walkFrames.get(i);
			}
				
			}
		return shootFrames.get(0);
		
	}
	
	private OverlayTexture getReloadFrame(long deltaTime)
	{
		for(int i=0;i<25;i++){
			if(deltaTime<frameTime*(i+1)){
				return reloadFrames.get(i);
			}
				
			}
		return shootFrames.get(0);
		
		
		
	}
	
}
