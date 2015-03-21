package rendering;

import java.util.List;

import matrix.Matrix;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.MeshShader;
import shaders.TerrainShader;
import terrain.Terrain;

public class RenderTerrain {
	private TerrainShader shader;
	
	public RenderTerrain(TerrainShader terrainShader)
	{
		Matrix.uploadProjectionMatrix(terrainShader);
		shader=terrainShader;
		
	}
	
	public void draw(List<Terrain> terrains)
	{
		for(Terrain terrain: terrains){
			GL30.glBindVertexArray(terrain.getTerrain().getMesh().getMesh().getVAO());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			
			shader.uploadSpecular(terrain.getTerrain().getMesh().getTex().getShine(),terrain.getTerrain().getMesh().getTex().getReflectivity());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D,terrain.getTerrain().getMesh().getTex().getTexId());
			
			
			Matrix4f transformation = Matrix.transformationMatrix(terrain.getTerrain().getPosition(), terrain.getTerrain().getRotX(), terrain.getTerrain().getRotY(), terrain.getTerrain().getRotZ(), terrain.getTerrain().getScale());
			shader.loadTranformationMatrix(transformation);	
			
			//GL11.glDrawElements(GL11.GL_TRIANGLES,terrain.getTerrain().getMesh().getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
			GL11.glDrawElements(GL11.GL_LINE_STRIP,terrain.getTerrain().getMesh().getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);

			
			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(1);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
			
		}
		
	}
	public void prepareScene()
	{}

}