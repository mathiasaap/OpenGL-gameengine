package mathiasap.cardboard.textures;

public class MeshTexture {

	private int texId;
	private float shine=1;
	private float reflectivity=0;
	public MeshTexture(int texId)
	{
		this.texId=texId;
	}
	public int getTexId() {
		return texId;
	}
	public float getShine() {
		return shine;
	}
	public void setShine(float shine) {
		this.shine = shine;
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	
}
