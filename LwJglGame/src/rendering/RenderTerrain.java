package rendering;

import java.util.List;

import matrix.Matrix;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import player.Controls;
import shaders.TerrainShader;
import terrain.Terrain;

public class RenderTerrain {
	private TerrainShader shader;
	private List<Terrain> terrains;
	private Controls control;
	public RenderTerrain(TerrainShader terrainShader,List<Terrain> terrains,Controls control)
	{
		Matrix.uploadProjectionMatrix(terrainShader);
		shader=terrainShader;
		this.terrains=terrains;
		this.control=control;
	
	}
	
	public void draw(Vector4f plane)
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
				GL30.glBindVertexArray(terrain.getMesh().getVAO());
				GL20.glEnableVertexAttribArray(0);
				GL20.glEnableVertexAttribArray(1);
				GL20.glEnableVertexAttribArray(2);
			
				//shader.uploadSpecular(terrain.getTerrain().getMesh().getTex().getShine(),terrain.getTerrain().getMesh().getTex().getReflectivity());
				shader.uploadSpecular(3,5);
				/*GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,terrain.getTerrain().getMesh().getTex().getTexId());*/
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,terrain.getMultiTex().getGrass().getTexID());
	
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,terrain.getMultiTex().getRock().getTexID());
	
				GL13.glActiveTexture(GL13.GL_TEXTURE2);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D,terrain.getMultiTex().getSnow().getTexID());
			
	
				
				//Matrix4f transformation = Matrix.transformationMatrix(terrain.getPosition(), terrain.getRotX(), terrain.getRotY(), terrain.getRotZ(), terrain.getScale());
				Matrix4f transformation = Matrix.transformationMatrix(terrain.getPosition(), (float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
				shader.loadTranformationMatrix(transformation);	
				
				if(control.getF1()){
					GL11.glDrawElements(GL11.GL_LINE_STRIP,terrain.getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
				}
				else{
					GL11.glDrawElements(GL11.GL_TRIANGLES,terrain.getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
				}
				//GL11.glDrawElements(GL11.GL_TRIANGLES,terrain.getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
				//GL11.glDrawElements(GL11.GL_LINES,terrain.getTerrain().getMesh().getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
				
				//GL11.glDrawElements(GL11.GL_LINE_LOOP,terrain.getTerrain().getMesh().getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
	
				
				GL20.glDisableVertexAttribArray(0);
				GL20.glDisableVertexAttribArray(2);
				GL30.glBindVertexArray(0);
				
			}
		}
		catch(Exception e)
		{
			System.out.println("Terrain couldn't be drawn. Skipping frame..");
		}
		
	}
}
