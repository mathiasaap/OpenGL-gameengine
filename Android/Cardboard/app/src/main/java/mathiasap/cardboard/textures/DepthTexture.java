package mathiasap.cardboard.textures;


import android.opengl.GLES30;

public class DepthTexture extends FBOTexture{

	public DepthTexture(int width, int height) {
		super(width, height);
	}

	@Override
	protected int getInternalFormat() {
		return GLES30.GL_DEPTH_COMPONENT;
	}

	@Override
	protected int getFormat() {
		return GLES30.GL_DEPTH_COMPONENT;
	}

}
