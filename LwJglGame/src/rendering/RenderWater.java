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

	private boolean underWater=false;
	private Vector3f playerPos;
	public RenderWater(WaterShader waterShader, LoadMesh loadmesh,Vector3f playerPos,Water water)
	{
		Matrix.uploadProjectionMatrix(waterShader);
		shader=waterShader;
		this.water=water;
		this.playerPos=playerPos;
		shader.useProgram();
		shader.uploadSpecular(3,3);
		shader.unbindShader();
		
		
		
	
	}



	
public boolean isUnderWater()
{
return underWater;	
}
	
	public void draw(List<Terrain> terrains)
	{
		//long curTime=System.nanoTime();
		

		GL11.glEnable(GL11.GL_BLEND);
		GL30.glBindVertexArray(water.getMesh().getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(2);
//		shader.uploadSpecular(3,3);
		float wLevel =0;
		float rotX=0;
		
		if(terrains.size()>0){
		Terrain arbTerrain=terrains.get(0);
	
		wLevel = (float) (arbTerrain.getPosition().y-200+water.getWaterHeight());
		if(playerPos.y<wLevel){
			rotX=(float) 180;
			underWater=true;
		}
		else{
			underWater=false;
		}}
		
		for(Terrain terrain: terrains){

			Vector3f wPos=new Vector3f(terrain.getPosition().x,wLevel,terrain.getPosition().z);
			
			Matrix4f transformation = Matrix.transformationMatrix(wPos, (float)rotX, (float)0.0f, (float)0.0f,(float)terrain.getSIZE()/2);
			shader.loadTranformationMatrix(transformation);	
			
		
			GL11.glDrawElements(GL11.GL_TRIANGLES,water.getMesh().getVertices(), GL11.GL_UNSIGNED_INT,0);
			
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		GL11.glDisable(GL11.GL_BLEND);
	//	System.out.println("Rendering water took "+(System.nanoTime()-curTime)/1000000.0  +" ms");
	}
}
