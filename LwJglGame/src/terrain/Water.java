package terrain;

import org.lwjgl.util.vector.Vector2f;

import mesh.Mesh;
import rendering.LoadMesh;
import simplexnoise.SimplexNoise;
import textures.DataTexture;

public class Water {
	
	private Mesh mesh;
	private DataTexture dudvMap;
	
	private long waterClock=System.currentTimeMillis();
	private final double pi2SecondsPerPeriod=1.0;
	private Vector2f dudvOffset= new Vector2f(0f,0f);
	private final Vector2f deltaDudvOffset= new Vector2f(0.007f,0.0015f);
	private long time=System.currentTimeMillis();
	private float waterHeight=-200;
	
	//private double[] dudvArr;
	double waterPeriod=0;
	private float[] vertices = {
			-1f, 0f, 1f,
			-1f, 0f, -1f,
			1f, 0f, -1f,
			1f, 0f, 1f
	};
	
	private float[] normals = {
			0f, 1f, 0f,
			0f, 1f, 0f,
			0f, 1f, 0f,
			0f, 1f, 0f
	};
	private int[] indices =
	{
	1,0,2,
	2,0,3
	};
	
	private float[] UV=
		{
			0,0,
			0,1,
			1,1,
			1,0
		};
	
	
	public Water(int width, int height)
	{
		makeDuDvMap(width*2,height*2);
		System.out.println("water");
		this.mesh = LoadMesh.loadNewMesh(vertices, indices, UV, normals);
		//this.mesh=loadmesh.loadNewMesh(vertices, 3);
		
	}
	public Mesh getMesh()
	{
		return mesh;
	}
	
	public Vector2f getDudvOffset()
	{
		long dt = System.currentTimeMillis()-time;
		time=System.currentTimeMillis();
		dudvOffset.x+=deltaDudvOffset.x*(dt/1000.0f);
		dudvOffset.y+=deltaDudvOffset.y*(dt/1000.0f);
		dudvOffset.x%=1.0f;
		dudvOffset.y%=1.0f;
		
		return dudvOffset;
	}

	
	
	public void changeWaterLevel(long deltaTime)
	{
		waterPeriod+=(((double)deltaTime)/(1000.0*pi2SecondsPerPeriod));
		if(waterPeriod>=Math.PI*2)
			waterPeriod-=Math.PI*2;
		
	}
	public float getWaterHeight(){
		return waterHeight;
	}
	
	private void makeDuDvMap(int width, int height)
	{
		float[] dudvArr= new float[width*height];
		SimplexNoise noise = new SimplexNoise(0.7,6);
		int factor=3;
		
		for(int i=0; i<width;i++)
			for(int j=0;j<height;j++){
				dudvArr[i*height+j] = (float) (noise.getNoiseOrd(i*factor, j*factor));
			
										}
				
				dudvMap= new DataTexture(width,height,dudvArr);
				System.out.println("dudv map created");
			
				
		
	}
	public DataTexture getDuDvMap()
	{
		return dudvMap;
	}
	
	public void cleanup()
	{
		dudvMap.cleanup();
	}
}
