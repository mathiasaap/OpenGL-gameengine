package terrain;

import mesh.Mesh;
import rendering.LoadMesh;

public class Water {
	
	private Mesh mesh;
	
	private long waterClock=System.currentTimeMillis();
	private final double pi2SecondsPerPeriod=1.0;
	
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
	
	
	public Water(LoadMesh loadmesh)
	{
		this.mesh = loadmesh.loadNewMesh(vertices, indices, UV, normals);
		//this.mesh=loadmesh.loadNewMesh(vertices, 3);
		
	}
	public Mesh getMesh()
	{
		return mesh;
	}

	
	public void changeWaterLevel(long deltaTime)
	{
		waterPeriod+=(((double)deltaTime)/(1000.0*pi2SecondsPerPeriod));
		if(waterPeriod>=Math.PI*2)
			waterPeriod-=Math.PI*2;
		
	}
	public double getWaterHeight(){
		long deltaTime=System.currentTimeMillis()-waterClock;
		waterClock=System.currentTimeMillis();
		changeWaterLevel(deltaTime);
		return Math.sin(waterPeriod)*30;
	}
}
