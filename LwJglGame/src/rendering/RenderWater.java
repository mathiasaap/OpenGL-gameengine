package rendering;

import java.util.List;

import matrix.Camera;
import matrix.Matrix;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL21;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import player.Controls;
import player.Player;
import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.WaterShader;
import terrain.Terrain;
import terrain.Water;
import textures.DepthTexture;
import textures.FBOTexture;
import textures.RenderTexture;

public class RenderWater {
	private WaterShader shader;
	private Water water;

	private boolean underWater=false;
	private Vector3f playerPos;
	private FBOTexture reflection,refraction,depthReflection,depthRefraction;
	private List<Terrain> terrains;
	private int FBOReflection,FBORefraction;
	private final Player player;
	
	private final RenderTerrain renderTerrain;
	private final RenderMesh renderMesh;
	
	public RenderWater(WaterShader waterShader, LoadMesh loadmesh,Vector3f playerPos,Water water,List<Terrain> terrains,Player player, RenderMesh renderMesh,RenderTerrain renderTerrain)
	{
		reflection=new RenderTexture(Display.getWidth(),Display.getHeight());
		refraction=new RenderTexture(Display.getWidth(),Display.getHeight());
		
		depthReflection= new DepthTexture(Display.getWidth(),Display.getHeight());
		depthRefraction= new DepthTexture(Display.getWidth(),Display.getHeight());
		
		Matrix.uploadProjectionMatrix(waterShader);
		shader=waterShader;
		this.water=water;
		this.playerPos=playerPos;
		this.terrains=terrains;
		this.player=player;
		this.renderTerrain=renderTerrain;
		this.renderMesh=renderMesh;
		shader.useProgram();
		shader.uploadSpecular(3,3);
		shader.unbindShader();
		
		FBOReflection=GL30.glGenFramebuffers();
		FBORefraction=GL30.glGenFramebuffers();
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOReflection);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, reflection.getId(), 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthReflection.getId(), 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBORefraction);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, refraction.getId(), 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthRefraction.getId(), 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		
	
	}



	
public boolean isUnderWater()
{
return underWater;	
}
	

	public void drawReflection(Vector4f clipPlane)
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOReflection);
		Camera cam = player.getCamera();
		
		Vector3f camPos=cam.getPosition();
		float waterHeight=(float) water.getWaterHeight();
		float dist=camPos.y-waterHeight;
		
		camPos.y-=2*dist;
		cam.invertPitch();
		renderTerrain.draw(clipPlane);
		renderMesh.draw(clipPlane);
		
		cam.invertPitch();
		camPos.y+=2*dist;
		
	}
	
	public void drawRefraction(Vector4f clipPlane)
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBORefraction);
		renderTerrain.draw(clipPlane);
		renderMesh.draw(clipPlane);
		
	}

	public void draw(int FBO)
	{
		//long curTime=System.nanoTime();
		
		clearBuffers();
		GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
		drawReflection(new Vector4f(0,1,0,(float) -water.getWaterHeight()));
		drawRefraction(new Vector4f(0,-1,0,(float) water.getWaterHeight()));
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		shader.useProgram();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		//GL11.glEnable(GL11.GL_BLEND);
		GL30.glBindVertexArray(water.getMesh().getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(2);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,reflection.getId());

		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,refraction.getId());
		
		
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
		//GL11.glDisable(GL11.GL_BLEND);
	//	System.out.println("Rendering water took "+(System.nanoTime()-curTime)/1000000.0  +" ms");
	}
	
	private void clearBuffers()
	{
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOReflection);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBORefraction);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	
		
	}
	
	public int getReflectionId()
	{
		return reflection.getId();
	}
	public FBOTexture getReflectionFBOTex()
	{
		return reflection;
	}
}
