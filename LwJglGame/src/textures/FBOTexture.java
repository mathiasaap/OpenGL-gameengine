package textures;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;


public abstract class FBOTexture {

	protected int id;
	
	protected FBOTexture(int width, int height)
	{
		
	id = GL11.glGenTextures();
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, getInternalFormat(), width, height, 0, getFormat(), GL11.GL_FLOAT,(ByteBuffer)null);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
	
	protected abstract int getInternalFormat();
	protected abstract int getFormat();
	
	public void cleanup()
	{
		GL11.glDeleteTextures(id);
	}
	public int getId()
	{
		return id;
	}
	
	
}
