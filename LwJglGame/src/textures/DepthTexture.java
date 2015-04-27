package textures;

import org.lwjgl.opengl.GL11;

public class DepthTexture extends FBOTexture{

	public DepthTexture(int width, int height) {
		super(width, height);
	}

	@Override
	protected int getInternalFormat() {
		return GL11.GL_DEPTH_COMPONENT;
	}

	@Override
	protected int getFormat() {
		return GL11.GL_DEPTH_COMPONENT;
	}

}
