package rendering;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.OverlayShader;
import textures.OverlayTexture;
import matrix.Matrix;
import mesh.Mesh;

public class RenderOverlay {

	private final Mesh rekt;
	private OverlayShader shader = new OverlayShader();
	private Matrix matrix=new Matrix();
	private final float[] rektPos={
			-1,1,
			-1,-1,
			1,1,
			1,-1	
	};
	
	public RenderOverlay(LoadMesh loader)
	{
		rekt = loader.loadNewMesh(rektPos);
		
	}
	public void draw(List<OverlayTexture> overlayTextures)
	{
		shader.useProgram();
		GL30.glBindVertexArray(rekt.getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		for(OverlayTexture texture:overlayTextures)
		{
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTexId());
			Matrix4f mat=matrix.transformationMatrix(texture.getPos(), texture.getScale());
			shader.loadTranformationMatrix(mat);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP,0,rekt.getVertices());
			
		}
		overlayTextures.clear();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		shader.unbindShader();
	}
}