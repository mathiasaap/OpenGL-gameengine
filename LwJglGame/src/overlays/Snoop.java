package overlays;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import rendering.LoadMesh;
import textures.OverlayTexture;

public class Snoop {

	private List<OverlayTexture> frames= new ArrayList<>();
	private Vector2f pos= new Vector2f(1.1f,0.120f);
	private Vector2f scale= new Vector2f(0.8f,0.8f);
	
	private long animationClock=System.currentTimeMillis();
	private int frameTime=25;
	private int cycleTime=frameTime*58;
	
	public Snoop()
	{
		for(int i= 1;i<=58; i++)
		{
			frames.add(new OverlayTexture(LoadMesh.loadTexture("/snoop/"+i),pos,scale));
		}
	}
	
	public OverlayTexture getFrame()
	{
		int frameInd= (int)((System.currentTimeMillis()-animationClock)%cycleTime)/frameTime;
		
		return frames.get(frameInd);
	}
	
}
