package rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import player.Controls;
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
	
	private Controls control= new Controls();
	private FBOTexture renderTexture,depthBufferTexture;
	
	private Map<TexMesh, List<MeshInstance>> meshInstances = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	private ArrayList<AbstractShader> shaders=new ArrayList<>();
	
	public Renderer(MeshShader meshShader,TerrainShader terrainShader, WaterShader waterShader, LoadMesh loadmesh,Vector3f playerPos)
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		this.meshShader=meshShader;
		this.terrainShader=terrainShader;
		this.waterShader=waterShader;
		underWaterPP=new UnderWaterPP();
		fbShader=new FramebufferShader();
		
		shaders.add(this.meshShader);
		shaders.add(this.terrainShader);
		shaders.add(this.waterShader);
		shaders.add(underWaterPP);
		shaders.add(fbShader);
		
		water=new Water(loadmesh);
		underWaterPP.loadTranformationMatrix(Matrix.transformationMatrix());
		fbShader.loadTranformationMatrix(Matrix.transformationMatrix());
		
		terrainShader.bindTexId();
		renderMesh= new RenderMesh(meshShader);
		renderTerrain= new RenderTerrain(terrainShader,loadmesh);
		renderWater= new RenderWater(waterShader,loadmesh,playerPos,water);
		renderOverlay=new RenderOverlay(loadmesh);
		renderFramebuffer=new RenderFramebuffer(loadmesh);
		FBO=GL30.glGenFramebuffers();
		
//		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		renderTexture=new RenderTexture(Display.getWidth(),Display.getHeight());
		depthBufferTexture= new DepthTexture(Display.getWidth(),Display.getHeight());

//		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		
	}
	public void setController(Controls control)
	{
		this.control=control;
	}
	
	public void render(Light light, Camera cam)
	{
		/*if(control.getF2())
		{
			if(!fullscreen)
			{
				fullscreen=true;
				try {
					Display.setFullscreen(true);
					System.out.println("fullscreen");
				} catch (LWJGLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		else{
			if(fullscreen)
			{
				fullscreen=false;
				try {
					Display.setFullscreen(false);
				} catch (LWJGLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}*/
		
		for(AbstractShader shader :shaders)
		{
			shader.updateTick();
		}
		prepareScene();
		

		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, FBO);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D, renderTexture.getId(), 0);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D, depthBufferTexture.getId(), 0);
		terrainShader.useProgram();
		terrainShader.uploadLight(light);
		renderTerrain.draw(terrains,control);
//		terrainShader.unbindShader();
		
		
		meshShader.useProgram();
		meshShader.uploadLight(light);
		renderMesh.draw(meshInstances);
//		meshShader.unbindShader();
		meshInstances.clear();
		
		if(control.getF2()){
		waterShader.useProgram();
		waterShader.uploadLight(light);
		renderWater.draw(terrains);
//		waterShader.unbindShader();
		}
		terrains.clear();
		//System.gc();
		
		
		
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
			fbShader.setGamma(15);
			lastF4=!lastF4;
		}
		renderFramebuffer.draw(renderTexture);
//		fbShader.unbindShader();
		//FRAMEBUFFER TO SCREEN END
		renderOverlay.draw(oTextures);

		
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
	
}
