package rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import player.Controls;
import player.Player;
import lighting.Light;
import matrix.Camera;
import matrix.Matrix;
import mesh.MeshInstance;
import mesh.TexMesh;
import shaders.AbstractShader;
import shaders.FramebufferShader;
import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.UnderWaterPP;
import shaders.WaterShader;
import terrain.Terrain;
import terrain.Water;
import textures.DepthTexture;
import textures.FBOTexture;
import textures.OverlayTexture;
import textures.RenderTexture;

public class Renderer {
	
	private MeshShader meshShader;
	private TerrainShader terrainShader;
	private WaterShader waterShader;
	private UnderWaterPP underWaterPP;
	private FramebufferShader fbShader;
	
	private int FBO;
	private Water water;
	
	boolean fullscreen=false;
	private RenderMesh renderMesh;
	private RenderTerrain renderTerrain;
	private RenderWater renderWater;
	private RenderOverlay renderOverlay;
	private List<OverlayTexture> oTextures;
	private RenderFramebuffer renderFramebuffer;
	private boolean lastF4=false;
	
	private Controls control;
	//private final Player player;
	private FBOTexture renderTexture,depthBufferTexture;
	
	private Map<TexMesh, List<MeshInstance>> meshInstances = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	private ArrayList<AbstractShader> shaders=new ArrayList<>();
	
	public Renderer(MeshShader meshShader,TerrainShader terrainShader, WaterShader waterShader,Vector3f playerPos,Player player,Controls control)
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		this.meshShader=meshShader;
		this.terrainShader=terrainShader;
		this.waterShader=waterShader;
		underWaterPP=new UnderWaterPP();
		fbShader=new FramebufferShader();
		//this.player=player;
		this.control=control;
		
		shaders.add(this.meshShader);
		shaders.add(this.terrainShader);
		shaders.add(this.waterShader);
		shaders.add(underWaterPP);
		shaders.add(fbShader);
		
		water=new Water(Terrain.SIZE,Terrain.SIZE);
		underWaterPP.loadTranformationMatrix(Matrix.transformationMatrix());
		fbShader.loadTranformationMatrix(Matrix.transformationMatrix());
		
		terrainShader.bindTexId();
		renderMesh= new RenderMesh(meshShader,meshInstances);
		renderTerrain= new RenderTerrain(terrainShader,terrains,control);
		renderWater= new RenderWater(waterShader,playerPos,water,terrains,player,renderMesh,renderTerrain,meshShader,terrainShader);
		renderOverlay=new RenderOverlay();
		renderFramebuffer=new RenderFramebuffer();
		FBO=GL30.glGenFramebuffers();
		
//		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		renderTexture=new RenderTexture(Display.getWidth(),Display.getHeight());
		depthBufferTexture= new DepthTexture(Display.getWidth(),Display.getHeight());

//		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, renderTexture.getId(), 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthBufferTexture.getId(), 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	public void setController(Controls control)
	{
		this.control=control;
	}
	
	public void render(Light light, Camera cam)
	{
		
		for(AbstractShader shader :shaders)
		{
			shader.updateTick();
		}
		prepareScene();
		

		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
//		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, renderTexture.getId(), 0);
//		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthBufferTexture.getId(), 0);
		terrainShader.useProgram();
		terrainShader.uploadLight(light);
		renderTerrain.draw();
//		terrainShader.unbindShader();
		
		
		meshShader.useProgram();
		meshShader.uploadLight(light);
		renderMesh.draw();
//		meshShader.unbindShader();
		
		
		if(!control.getF2()){
		waterShader.useProgram();
		waterShader.uploadLight(light);
		renderWater.draw(FBO);
//		waterShader.unbindShader();
		}
		terrains.clear();
		meshInstances.clear();
		
		
		
		//FRAMEBUFFER TO SCREEN START
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		if(renderWater.isUnderWater()){
			underWaterPP.useProgram();
//			underWaterPP.updateTimeOffset();
		}
		else
		fbShader.useProgram();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		//underWaterPP.loadTranformationMatrix(Matrix.transformationMatrix());
		if(control.getF4()!=lastF4)
		{
			fbShader.setGamma(30);
			lastF4=!lastF4;
		}
		
		renderFramebuffer.draw(renderTexture);
	    //renderFramebuffer.draw(renderWater.getReflectionFBOTex());
		
//		fbShader.unbindShader();
		//FRAMEBUFFER TO SCREEN END
		//oTextures.add(new OverlayTexture(renderWater.getReflectionId(),new Vector2f(100f,100f),new Vector2f(0.1f,0.1f)));
		if(control.getF5())
		{
		renderOverlay.draw(oTextures);
		}
		
	}
	
	private void prepareScene()
	{
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.3f, 0.6f, 1.0f);
		//GL11.glClearColor((80f/256f), (16f/256f), (0f/256f), 1.0f);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.3f, 0.6f, 1.0f);
		//GL11.glClearColor((80f/256f), (16f/256f), (0f/256f), 1.0f);
		
	}
	
	public void putInstance(MeshInstance mInstance)
	{
		TexMesh model = mInstance.getMesh();
		
		if(!meshInstances.containsKey(model))
		{
			List<MeshInstance> tmpLstMIns = new ArrayList<>();
			tmpLstMIns.add(mInstance);
			meshInstances.put(model, tmpLstMIns);
		}
		else
		{
			meshInstances.get(model).add(mInstance);
		}
	}
	
	public void putTerrain(Terrain terrain)
	{
		terrains.add(terrain);
	}
	public void putOverlays(List<OverlayTexture> oTextures)
	{
		this.oTextures=oTextures;
	}
	
	public void cleanup()
	{
		renderWater.cleanup();
	}
	
}
