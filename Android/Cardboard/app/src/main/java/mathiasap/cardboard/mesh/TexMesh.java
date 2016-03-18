package mathiasap.cardboard.mesh;

import mathiasap.cardboard.textures.MeshTexture;

public class TexMesh {


	private Mesh mesh;
	private MeshTexture tex;
	
	public TexMesh(Mesh mesh, MeshTexture tex)
	{
		this.mesh=mesh;
		this.tex=tex;
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public MeshTexture getTex() {
		return tex;
	}
}
