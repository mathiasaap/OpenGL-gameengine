package mathiasap.cardboard.mesh;


import mathiasap.cardboard.misc.Vektor3f;

public class MeshInstance {

	private TexMesh mesh;
	private Vektor3f position;
	private float rotX, rotY, rotZ;
	private float scale;
	
	public MeshInstance(TexMesh mesh, Vektor3f position, float rotX,
			float rotY, float rotZ, float scale) {
		this.mesh = mesh;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}

	public TexMesh getMesh() {
		return mesh;
	}

	public void setMesh(TexMesh mesh) {
		this.mesh = mesh;
	}

	public Vektor3f getPosition() {
		return position;
	}

	public void setPosition(Vektor3f position) {
		this.position = position;
	}

	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}
	
	public float getScale()
	{
		return scale;
	}
	
	public void move(float x, float y, float z)
	{
		position.x +=x;
		position.y +=y;
		position.z +=z;
	}
	
	public void rotate(float rotX, float rotY, float rotZ)
	{
		this.rotX +=rotX;
		this.rotY +=rotY;
		this.rotZ +=rotZ;
	}
	
}
