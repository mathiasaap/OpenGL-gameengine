package mathiasap.cardboard.textures;

import android.opengl.GLES30;

import java.nio.ByteBuffer;



public abstract class FBOTexture {

	protected int[] id;
	
	protected FBOTexture(int width, int height)
	{
		id= new int[1];
		GLES30.glGenTextures(1,id,0);


		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, id[0]);
		GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, getInternalFormat(), width, height, 0, getFormat(), GLES30.GL_FLOAT,(ByteBuffer)null);
		GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);
		GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
	}
	
	protected abstract int getInternalFormat();
	protected abstract int getFormat();
	
	public void cleanup()
	{
		GLES30.glDeleteTextures(1,id,0);
	}
	public int getId()
	{
		return id[0];
	}
	
	
}
