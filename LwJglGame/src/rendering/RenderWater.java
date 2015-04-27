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
	private long waterClock=System.currentTimeMillis();
	private final double pi2SecondsPerPeriod=1.0;
	private Vector3f playerPos;
	public RenderWater(WaterShader waterShader, LoadMesh loadmesh,Vector3f playerPos)
	{
		Matrix.uploadProjectionMatrix(waterShader);
		shader=waterShader;
		water=new Water(loadmesh);
		this.playerPos=playerPos;
		shader.useProgram();
		shader.uploadSpecular(3,3);
		shader.unbindShader();
		
		
		
	
	}
	private int counter=0;
	
//	double totTime=0;
//	int iterations;
	double waterPeriod=0;
	
	private void changeWaterLevel(long deltaTime)
	{
		waterPeriod+=(((double)deltaTime)/(1000.0*pi2SecondsPerPeriod));
		if(waterPeriod>=Math.PI*2)
			waterPeriod-=Math.PI*2;
		
	}
	
	public void draw(List<Terrain> terrains)
	{
		//long curTime=System.nanoTime();
		long deltaTime=System.currentTimeMillis()-waterClock;
		waterClock=System.currentTimeMillis();
		changeWaterLevel(deltaTime);
		GL11.glEnable(GL11.GL_BLEND);
		GL30.glBindVertexArray(water.getMesh().getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(2);
//		shader.uploadSpecular(3,3);
		for(Terrain terrain: terrains){

			//shader.uploadSpecular(terrain.getTerrain().getMesh().getTex().getShine(),terrain.getTerrain().getMesh().getTex().getReflectivity());
			
		

			
			//Matrix4f transformation = Matrix.transformationMatrix(terrain.getPosition(), terrain.getRotX(), terrain.getRotY(), terrain.getRotZ(), terrain.getScale());
			Vector3f wPos=new Vector3f(terrain.getPosition().x,terrain.getPosition().y-200,terrain.getPosition().z);
			wPos.y+=Math.sin(waterPeriod)*30;
			float rotX=0;
			if(playerPos.y<wPos.y)
				rotX=(float) 180;
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
