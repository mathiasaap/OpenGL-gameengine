package mesh;

public class Mesh {

	private int vertices;
	private int VAO;
	
	public Mesh(){}
	
	public Mesh(int vertices, int VAO)
	{
		this.vertices=vertices;
		this.VAO=VAO;
	}

	public int getVertices() {
		return vertices;
	}

	public int getVAO() {
		return VAO;
	}
	
	
	
}
