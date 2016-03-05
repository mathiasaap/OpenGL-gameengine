package rendering;

import mesh.Rekt;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import textures.FBOTexture;



public class RenderFramebuffer {
private Rekt rekt;

public RenderFramebuffer()
{
	
	rekt=new Rekt();
}

public void draw(FBOTexture tex)
{
	GL30.glBindVertexArray(rekt.getMesh().getVAO());
	GL20.glEnableVertexAttribArray(0);
	
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getId());

		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,0,rekt.getMesh().getVertices());
		
	
	

	GL20.glDisableVertexAttribArray(0);
	
}
	

}
