package mesh;

import java.util.List;

public class Mesh {

	private int vertices;
	private int VAO;
	List<Integer> VBOs;
	
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
	
	
	
}
