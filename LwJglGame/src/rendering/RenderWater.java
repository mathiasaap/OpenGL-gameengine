package rendering;

import java.util.List;

import matrix.Camera;
import matrix.Matrix;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

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
	private MeshShader meshShader;
	private TerrainShader terrainShader;
	
	private final RenderTerrain renderTerrain;
	private final RenderMesh renderMesh;
	
	public RenderWater(WaterShader waterShader,Vector3f playerPos,Water water,List<Terrain> terrains,Player player, RenderMesh renderMesh,RenderTerrain renderTerrain,MeshShader meshShader,TerrainShader terrainShader)
	{
		reflection=new RenderTexture(Display.getWidth(),Display.getHeight());
		refraction=new RenderTexture(Display.getWidth(),Display.getHeight());
		
		depthReflection= new DepthTexture(Display.getWidth(),Display.getHeight());
		depthRefraction= new DepthTexture(Display.getWidth(),Display.getHeight());
		
		Matrix.uploadProjectionMatrix(waterShader);
		shader=waterShader;
		this.water=water;
		//Water.makeDuDvMap(Terrain.SIZE, Terrain.SIZE);
		this.playerPos=playerPos;
		this.terrains=terrains;
		this.player=player;
		this.renderTerrain=renderTerrain;
		this.renderMesh=renderMesh;
		this.meshShader=meshShader;
		this.terrainShader=terrainShader;
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
return playerPos.y<water.getWaterHeight();	
}
	

	public void drawReflection(Vector4f clipPlane)
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBOReflection);
		Camera cam = player.getCamera();
		
		Vector3f camPos=cam.getPosition();
		float waterHeight= water.getWaterHeight();
		float dist=camPos.y-waterHeight;
		camPos.y-=2*dist;
		cam.invertPitch();
		cam.uploadViewMatrix(meshShader, terrainShader, shader);
		renderTerrain.draw(clipPlane);
		renderMesh.draw(clipPlane);
		
		cam.invertPitch();
		camPos.y+=2*dist;
		cam.uploadViewMatrix(meshShader, terrainShader, shader);
		
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
		drawReflection(new Vector4f(0,1,0,(float)-water.getWaterHeight()));
		drawRefraction(new Vector4f(0,-1,0,(float) water.getWaterHeight()));
		GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
		shader.useProgram();
		shader.uploadDudvOffset(water.getDudvOffset());
		shader.uploadCamera(player.getCamera().getPosition());
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		//GL11.glEnable(GL11.GL_BLEND);
		GL30.glBindVertexArray(water.getMesh().getVAO());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(2);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,reflection.getId());

		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,refraction.getId());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D,water.getDuDvMap().getId());
		
		
//		shader.uploadSpecular(3,3);
		

		for(Terrain terrain: terrains){

			Vector3f wPos=new Vector3f(terrain.getPosition().x,(float) water.getWaterHeight(),terrain.getPosition().z);
			
			Matrix4f transformation = Matrix.transformationMatrix(wPos, (float)0, (float)0.0f, (float)0.0f,(float)terrain.getSIZE()/2);
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
	public void cleanup()
	{
		water.cleanup();
	}
			
}
