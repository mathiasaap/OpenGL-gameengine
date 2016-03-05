package mesh;

import rendering.LoadMesh;

public class Rekt {

	private final Mesh mesh;
	private static final float[] rektPos={
			-1,1,
			-1,-1,
			1,1,
			1,-1	
	};

	
	public Rekt()
	{

		mesh = LoadMesh.loadNewMesh(rektPos);

	}
	
	public Mesh getMesh()
	{
		return mesh;
	}
	
}
