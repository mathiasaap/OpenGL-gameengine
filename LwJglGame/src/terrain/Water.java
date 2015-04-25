package terrain;

import mesh.Mesh;
import rendering.LoadMesh;

public class Water {
	
	private Mesh mesh;
	
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

}
