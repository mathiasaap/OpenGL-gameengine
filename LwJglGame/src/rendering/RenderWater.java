package rendering;

import java.util.List;

import matrix.Matrix;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import player.Controls;
import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.WaterShader;
import terrain.Terrain;
import terrain.Water;

public class RenderWater {
	private WaterShader shader;
	private Water water;
	public RenderWater(WaterShader waterShader, LoadMesh loadmesh)
	{
		Matrix.uploadProjectionMatrix(waterShader);
		shader=waterShader;
		water=new Water(loadmesh);
	
	}
	private int counter=0;
	
	public void draw(List<Terrain> terrains)
	{
		
		GL11.glEnable(GL11.GL_BLEND);
		for(Terrain terrain: terrains){
			GL30.glBindVertexArray(water.getMesh().getVAO());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(2);
			//shader.uploadSpecular(terrain.getTerrain().getMesh().getTex().getShine(),terrain.getTerrain().getMesh().getTex().getReflectivity());
			shader.uploadSpecular(3,2);
		

			
			//Matrix4f transformation = Matrix.transformationMatrix(terrain.getPosition(), terrain.getRotX(), terrain.getRotY(), terrain.getRotZ(), terrain.getScale());
			Vector3f wPos=new Vector3f(terrain.getPosition().x,terrain.getPosition().y-200,terrain.getPosition().z);
			wPos.y+=Math.sin((counter++)/20000.0)*30;
			Matrix4f transformation = Matrix.transformationMatrix(wPos, (float)0.0f, (float)0.0f, (float)0.0f, (float)550);
			shader.loadTranformationMatrix(transformation);	
			
		
			GL11.glDrawElements(GL11.GL_TRIANGLES,water.getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);

			GL20.glDisableVertexAttribArray(0);
			GL20.glDisableVertexAttribArray(2);
			GL30.glBindVertexArray(0);
			
			
			
		}
		GL11.glDisable(GL11.GL_BLEND);
	}
}
