package mathiasap.cardboard.rendering;


import android.opengl.GLES30;

import mathiasap.cardboard.mesh.Rekt;
import mathiasap.cardboard.textures.FBOTexture;

public class RenderFramebuffer {
private Rekt rekt;

public RenderFramebuffer()
{
	
	rekt=new Rekt();
}

public void draw(FBOTexture tex)
{
	GLES30.glBindVertexArray(rekt.getMesh().getVAO());
	GLES30.glEnableVertexAttribArray(0);

	GLES30.glActiveTexture(GLES30.GL_TEXTURE0);

	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, tex.getId());

	GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP,0,rekt.getMesh().getVertices());




	GLES30.glDisableVertexAttribArray(0);
	
}
	

}
