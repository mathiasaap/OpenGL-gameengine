package rendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import lighting.Light;
import matrix.Camera;
import mesh.MeshInstance;
import mesh.TexMesh;
import shaders.MeshShader;
import shaders.TerrainShader;
import terrain.Terrain;

public class Renderer {
	
	private MeshShader meshShader;
	private TerrainShader terrainShader;
	

	private RenderMesh renderMesh;
	private RenderTerrain renderTerrain;
	
	private Map<TexMesh, List<MeshInstance>> meshInstances = new HashMap<>();
	private List<Terrain> terrains = new ArrayList<>();
	
	
	public Renderer(MeshShader meshShader,TerrainShader terrainShader)
	{
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		this.meshShader=meshShader;
		this.terrainShader=terrainShader;
		renderMesh= new RenderMesh(meshShader);
		renderTerrain= new RenderTerrain(terrainShader);
		
		
	}
	
	public void render(Light light, Camera cam)
	{
		prepareScene();
		
		renderTerrain.prepareScene();
		terrainShader.useProgram();
		terrainShader.uploadLight(light);
		renderTerrain.draw(terrains);
		terrainShader.unbindShader();
		terrains.clear();
		
		
		meshShader.useProgram();
		meshShader.uploadLight(light);
		renderMesh.draw(meshInstances);
		meshShader.unbindShader();
		meshInstances.clear();
		
		
	}
	
	private void prepareScene()
	{
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		//GL11.glClearColor(0.2f, 0.3f, 0.6f, 1.0f);
		GL11.glClearColor((80f/256f), (16f/256f), (0f/256f), 1.0f);
		
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
	
}