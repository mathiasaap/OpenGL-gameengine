package mathiasap.cardboard.rendering;

import android.opengl.GLES30;

import java.util.List;

import mathiasap.cardboard.matrix.Matrices;
import mathiasap.cardboard.misc.Vektor4f;
import mathiasap.cardboard.shaders.TerrainShader;
import mathiasap.cardboard.terrain.Terrain;


public class RenderTerrain {
	private TerrainShader shader;
	private List<Terrain> terrains;
	//private Controls control;
	public RenderTerrain(TerrainShader terrainShader,List<Terrain> terrains/*,Controls control*/)
	{
		shader=terrainShader;
		this.terrains=terrains;
		//this.control=control;
	
	}
	
	public void draw(Vektor4f plane)
	{
		shader.useProgram();
		shader.uploadClipPlane(plane);
		draw();
	}
	
	public void draw()
	{
		shader.useProgram();

		try
		{
			for(Terrain terrain: terrains){
				GLES30.glBindVertexArray(terrain.getMesh().getVAO());
				GLES30.glEnableVertexAttribArray(0);
				GLES30.glEnableVertexAttribArray(1);
				GLES30.glEnableVertexAttribArray(2);
			
				//shader.uploadSpecular(terrain.getTerrain().getMesh().getTex().getShine(),terrain.getTerrain().getMesh().getTex().getReflectivity());
				//shader.uploadSpecular(3,5);
				/*GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,terrain.getTerrain().getMesh().getTex().getTexId());*/



				GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,terrain.getMultiTex().getGrass().getTexID());

				GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,terrain.getMultiTex().getRock().getTexID());

				GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,terrain.getMultiTex().getSnow().getTexID());
			
	
				
				//Matrix4f transformation = Matrix.transformationMatrix(terrain.getPosition(), terrain.getRotX(), terrain.getRotY(), terrain.getRotZ(), terrain.getScale());
				float[] transformation = Matrices.transformationMatrix(terrain.getPosition(), (float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
				shader.loadTranformationMatrix(transformation);

				GLES30.glDrawElements(GLES30.GL_TRIANGLES, terrain.getMesh().getVertices(), GLES30.GL_UNSIGNED_INT, 0);


				GLES30.glDisableVertexAttribArray(0);
				GLES30.glDisableVertexAttribArray(1);
				GLES30.glDisableVertexAttribArray(2);
				GLES30.glBindVertexArray(0);
				
			}
		}
		catch(Exception e)
		{
			System.out.println("Terrain couldn't be drawn. Skipping frame..");
		}
		
	}
}
