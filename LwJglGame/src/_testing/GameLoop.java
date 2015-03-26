package _testing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lighting.Light;
import matrix.Camera;
import matrix.Matrix;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.OBJLoader;
import mesh.TexMesh;
import misc.Key2D;
import misc.Maths;
import misc.RayCasting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import overlays.Sniper;
import enemies.Enemy;
import enemies.Shrek;
import player.Controls;
import player.Player;
import rendering.DisplayWindow;
import rendering.LoadMesh;
import rendering.RenderMesh;
import rendering.RenderOverlay;
import rendering.RenderTerrain;
import rendering.Renderer;
import shaders.MeshShader;
import shaders.TerrainShader;
import simplexnoise.SimplexNoise;
import terrain.Terrain;
import terrain.TerrainCollision;
import terrain.TerrainHandlingThread;
import textures.MeshTexture;
import textures.OverlayTexture;
import textures.TerrainMultiTexture;
import textures.TerrainTexture;



public class GameLoop {

	private static List<Terrain> terrains= new ArrayList<>();
	
	public static void main(String[] args) {
		long timeClock= System.currentTimeMillis();
		long FPSCounterRefresh= System.currentTimeMillis();
		
		DisplayWindow.create();
		LoadMesh loader= new LoadMesh();
		MeshShader meshShader = new MeshShader("res/shaders/vsMesh.glsl","res/shaders/fsMesh.glsl");
		TerrainShader terrainShader = new TerrainShader("res/shaders/vsTerrain.glsl","res/shaders/fsTerrain.glsl");
		//RenderTerrain terrainRenderer = new RenderTerrain(terrainShader);
		TerrainCollision terrainCollision=new TerrainCollision();
		Renderer mainRenderer = new Renderer(meshShader,terrainShader);
		RenderOverlay renderOverlay = new RenderOverlay(loader);
		
		List<Enemy> enemies = new ArrayList<>();
		Player player= new Player(new Vector3f(1687f,10000f,2459f));
		Sniper sniper=new Sniper(loader);
		Controls controls= new Controls(player,sniper,enemies, meshShader, terrainShader);
		mainRenderer.setController(controls);
		Light light = new Light(new Vector3f(100,1000,-1000),new Vector3f(1,1,1));
		RayCasting mouseRay= new RayCasting(player.getCamera(),Matrix.calcProjectionMatrix());
		
		List<OverlayTexture> oTextures= new ArrayList<>();
		//oTextures.add(new OverlayTexture(loader.loadTexture("1"),new Vector2f(0.598f,-0.420f),new Vector2f(1.598f,1.420f)));
		
		MeshTexture tex= new MeshTexture(loader.loadTexture("sample_pic"));
		TerrainTexture grass= new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rock= new TerrainTexture(loader.loadTexture("rock"));
		TerrainTexture snow= new TerrainTexture(loader.loadTexture("snow"));
		tex.setShine(3);
		tex.setReflectivity(2);
		TerrainHandlingThread terrainHandler= new TerrainHandlingThread();
		
		
	//	Terrain terrain= new Terrain(new TerrainMultiTexture(grass,rock, snow),0,0,loader);		
		//terrains.add(terrain);

		Map<Key2D,Terrain> terrainList=new HashMap<>();
		//terrainList.put(new Key2D(0,0), terrain);
		OBJLoader objloader = new OBJLoader();

				
		Mesh shrekmodel= objloader.loadObj("shrek", loader);
		TexMesh shrekMesh = new TexMesh(shrekmodel,tex);
		
		MeshInstance shrekMeshIns = new MeshInstance(shrekMesh, new Vector3f(0,0,0),0,0,0,1);
		Random random= new Random();
		//Enemy shrek = new Shrek(new Vector3f(150,0,150),player,shrekMeshIns);
		
		for(int i=0;i <20; i++)
		{
			MeshInstance enIns=new MeshInstance(shrekMesh,new Vector3f(0,0,0),0,0,0,1);
			enemies.add(new Shrek(new Vector3f(random.nextInt(4000),random.nextInt(600),random.nextInt(4000)),player,enIns));
			
		}
		
		while(!Display.isCloseRequested())
		{
			timeClock=System.currentTimeMillis();
			Vector3f playerPos=player.getPosition();
			int currentTerrainX=(int) Maths.floor(playerPos.x/Terrain.SIZE);
			int currentTerrainZ=(int) Maths.floor(playerPos.z/Terrain.SIZE);
			//System.out.println(currentTerrainX +"   "+ currentTerrainZ);
			
			for(int i=-2;i<3;i++)
				for(int j=-2;j<3;j++)
			if(!terrainList.containsKey(new Key2D(currentTerrainX+i,currentTerrainZ+j)))
			{
				Terrain ter= new Terrain(new TerrainMultiTexture(grass,rock, snow),currentTerrainX+i,currentTerrainZ+j,loader);
				terrainList.put( new Key2D(currentTerrainX+i,currentTerrainZ+j), ter);
				terrains.add(ter);
				terrainHandler.addTerrainToQueue(ter);
			}
			
			controls.playing();
			player.move();

			mouseRay.update();
			terrainCollision.playerCollission(terrains, player);

			for(Iterator<Enemy> enemy=enemies.iterator();enemy.hasNext();)
			{
				Enemy enem =enemy.next();
				enem.move();

				terrainCollision.enemyCollission(terrains, enem);
				mainRenderer.putInstance(enem.getEnemyMeshInstance());
				if(!enem.getAlive())
					enemy.remove();
					
	
				
			}

			oTextures.add(sniper.getFrame());

			for(Key2D terrainKey:terrainList.keySet())
			{
				if(Vector3f.sub(terrainList.get(terrainKey).getPosition(), player.getPosition(), null).length()<15000)
				mainRenderer.putTerrain(terrainList.get(terrainKey));
			}
			//mainRenderer.putTerrain(terrain);

			
			mainRenderer.render(light, player.getCamera(meshShader, terrainShader));
			renderOverlay.draw(oTextures);
			
			DisplayWindow.update();

			long deltaTime=System.currentTimeMillis()-timeClock;
			if(deltaTime>0&&(System.currentTimeMillis()-FPSCounterRefresh)>400){
			Display.setTitle("MLGSIM9000  "+1000/deltaTime+ " FPS");
			FPSCounterRefresh=System.currentTimeMillis();
			}
		}
		terrainHandler.cleanup();
		meshShader.destroy();
		loader.destroy();
		DisplayWindow.destroy();
	}
	
}
