package terrain;

import java.util.ArrayList;
import java.util.List;

import mesh.Mesh;

public class TerrainLOD {

	private Mesh mesh;
	private boolean readyToDraw=false;
	public int getVAOFromMesh()
	{
		if(readyToDraw)
			return mesh.getVAO();
		return -1;
	}
	
}
