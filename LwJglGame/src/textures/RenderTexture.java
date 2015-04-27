package textures;

import org.lwjgl.opengl.GL11;

public class RenderTexture extends FBOTexture{

	public RenderTexture(int width, int height) {
		super(width, height);
	}

	@Override
	protected int getInternalFormat() {
		return GL11.GL_RGBA8;
	}

	@Override
	protected int getFormat() {
		return GL11.GL_RGBA;
	}

}
