package mathiasap.cardboard.textures;


import android.opengl.GLES30;

public class RenderTexture extends FBOTexture{

	public RenderTexture(int width, int height) {
		super(width, height);
	}

	@Override
	protected int getInternalFormat() {
		return GLES30.GL_RGBA8;
	}

	@Override
	protected int getFormat() {
		return GLES30.GL_RGBA;
	}

}
