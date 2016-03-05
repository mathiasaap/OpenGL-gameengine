package textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import rendering.LoadMesh;

public class DataTexture {
	
	private final int id;
	
	public DataTexture(int width, int height, float[] buffer)
	{
		id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_R32F, width, height, 0, GL11.GL_RED, GL11.GL_FLOAT, LoadMesh.floatToFloatBuffer(buffer));
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
	}
	public int getId()
	{
		return id;
	}
	public void cleanup()
	{
		GL11.glDeleteTextures(id);
	}

}
