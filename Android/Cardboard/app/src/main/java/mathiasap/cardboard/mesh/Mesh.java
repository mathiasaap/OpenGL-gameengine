package mathiasap.cardboard.mesh;

import java.util.List;

//import rendering.LoadMesh;

public class Mesh {

	private int vertices;
	private int VAO;
	private List<Integer> VBOs;
	private boolean alive=true;

	
	public Mesh(){}
	
	public Mesh(int vertices, int VAO,List<Integer> VBOs)
	{
		this.vertices=vertices;
		this.VAO=VAO;
		this.VBOs=VBOs;
	}

	public int getVertices() {
		return vertices;
	}

	public int getVAO() {
		return VAO;
	}
	public List<Integer> getVBOs()
	{
		return VBOs;
	}
	public void deleteMe()
	{
		/*loader.deleteVAO(VAO);
		loader.deleteVBOs(VBOs);*/
		VBOs=null;
		VAO=-1;
		alive=false;
	}
	public boolean isAlive()
	{
		return alive;
	}
	
	
	
	
}
