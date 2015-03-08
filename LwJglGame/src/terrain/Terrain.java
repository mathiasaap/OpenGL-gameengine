package terrain;

import rendering.LoadMesh;
import textures.MeshTexture;
import mesh.Mesh;

public class Terrain {

	private static final float SIZE = 1200;
	private static final int VERTICES = 256;
	private Mesh mesh;
	private MeshTexture tex;
	private float xPos,zPos;
	
	public Terrain(MeshTexture tex, int x, int z, LoadMesh meshLdr)
	{
		this.tex=tex;
		this.xPos= x*SIZE;
		this.zPos= z*SIZE;
	}
}
