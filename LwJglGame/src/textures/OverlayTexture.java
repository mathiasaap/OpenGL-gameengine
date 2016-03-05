package textures;


import org.lwjgl.util.vector.Vector2f;

public class OverlayTexture {

	private int texId;
	private Vector2f pos,scale;

	
	public OverlayTexture(int texId, Vector2f pos, Vector2f scale) {
		this.texId = texId;
		this.pos = pos;
		this.scale = scale;
	}

	public int getTexId() {
		return texId;
	}

	public Vector2f getPos() {
		return pos;
	}

	public Vector2f getScale() {
		return scale;
	}
	
	
	
}
