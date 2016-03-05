package _testing;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lighting.Light;
import matrix.Matrix;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.OBJLoader;
import mesh.TexMesh;
import misc.Key2D;
import misc.Maths;
import misc.RayCasting;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import overlays.Sniper;
import overlays.Snoop;
import enemies.Enemy;
import enemies.Shrek;
import player.Controls;
import player.Player;
import rendering.DisplayWindow;
import rendering.LoadMesh;
import rendering.RenderOverlay;
import rendering.Renderer;
import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.WaterShader;
import terrain.Terrain;
import terrain.TerrainCollision;
import terrain.TerrainHandlingThread;
import terrain.TerrainMonitor;
import terrain.UnloadTerrains;
import textures.MeshTexture;
import textures.OverlayTexture;
import textures.TerrainMultiTexture;
import textures.TerrainTexture;



public class GameLoop {

	public static boolean DEBUG = false;
	
	public static void main(String[] args) {
		long timeClock= System.currentTimeMillis();
		long FPSCounterRefresh= System.currentTimeMillis();
		
		DisplayWindow.create();
		Player player= new Player(new Vector3f(1687f,5000f,2459f));
		MeshShader meshShader = new MeshShader("res/shaders/vsMesh.glsl","res/shaders/fsMesh.glsl");
		TerrainShader terrainShader = new TerrainShader("res/shaders/vsTerrain.glsl","res/shaders/fsTerrain.glsl");
		WaterShader waterShader = new WaterShader("res/shaders/vsWater.glsl","res/shaders/fsWater.glsl");
		
		//RenderTerrain terrainRenderer = new RenderTerrain(terrainShader);
		TerrainCollision terrainCollision=new TerrainCollision();
		
		RenderOverlay renderOverlay = new RenderOverlay();
		
		List<Enemy> enemies = new ArrayList<>();
		
		Sniper sniper=new Sniper();
		Snoop snoop = new Snoop();
		
		Light light = new Light(new Vector3f(0,5000,0),new Vector3f(1,1,1));
		Controls controls= new Controls(player,sniper,enemies, meshShader, terrainShader,waterShader,light);
		Renderer mainRenderer = new Renderer(meshShader , terrainShader, waterShader,player.getPosition(), player,controls);
		
		RayCasting mouseRay= new RayCasting(player.getCamera(),Matrix.calcProjectionMatrix());
		
		List<OverlayTexture> oTextures= new ArrayList<>();
		//oTextures.add(new OverlayTexture(loader.loadTexture("1"),new Vector2f(0.598f,-0.420f),new Vector2f(1.598f,1.420f)));
		
		MeshTexture tex= new MeshTexture(LoadMesh.loadTexture("sample_pic"));
		TerrainTexture grass= new TerrainTexture(LoadMesh.loadTexture("grass"));
		TerrainTexture rock= new TerrainTexture(LoadMesh.loadTexture("rock"));
		TerrainTexture snow= new TerrainTexture(LoadMesh.loadTexture("snow"));
		tex.setShine(3);
		tex.setReflectivity(2);
		Map<Key2D,TerrainMonitor> terrainMonitorList=new HashMap<>();
		TerrainHandlingThread terrainHandler= new TerrainHandlingThread(player.getPosition(),terrainMonitorList);
		
		
	//	Terrain terrain= new Terrain(new TerrainMultiTexture(grass,rock, snow),0,0,loader);		
		//terrains.add(terrain);

		//Map<Key2D,Terrain> terrainList=new HashMap<>();
		
		//terrainList.put(new Key2D(0,0), terrain);

				
		Mesh shrekmodel= OBJLoader.loadObj("shrek");
		TexMesh shrekMesh = new TexMesh(shrekmodel,tex);
		
		//MeshInstance shrekMeshIns = new MeshInstance(shrekMesh, new Vector3f(0,0,0),0,0,0,1);
		Random random= new Random();
		//Enemy shrek = new Shrek(new Vector3f(150,0,150),player,shrekMeshIns);
		
		int noEnemies=50;
		for(int i=0;i <noEnemies; i++)
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

			int genRad=6;//10
			for(int i=-genRad;i<genRad+1;i++)
				for(int j=-genRad;j<genRad+1;j++)
			if(!terrainMonitorList.containsKey(new Key2D(currentTerrainX+i,currentTerrainZ+j)))
			{
				Terrain ter= new Terrain(new TerrainMultiTexture(grass,rock, snow),currentTerrainX+i,currentTerrainZ+j);
				TerrainMonitor monTer= new TerrainMonitor(ter);
				
				/*for(int l=0;l <1; l++)
				{
					MeshInstance enIns=new MeshInstance(shrekMesh,new Vector3f(0,0,0),0,0,0,1);
					enemies.add(new Shrek(new Vector3f(ter.getPosition().x+random.nextInt(1024),random.nextInt(600),ter.getPosition().z+random.nextInt(1024)),player,enIns));
					
				}*/
				
				
				monTer.setLockedByGenThread(true);
				terrainMonitorList.put( new Key2D(currentTerrainX+i,currentTerrainZ+j),monTer);
				terrainHandler.addTerrainToQueue(monTer);
			}
					
					
			
			controls.playing();
			player.move();

			mouseRay.update();
			terrainCollision.playerCollission(player);
			
			
			// Player collission is dependent on wheter or not a terrain is present if following code is used
			/*
			if(!terrainMonitorList.get(new Key2D(currentTerrainX,currentTerrainZ)).isLockedByGenThread())
			//terrainCollision.playerCollission(terrainMonitorList.get(new Key2D(currentTerrainX,currentTerrainZ)).getTerrain(), player);
			terrainCollision.playerCollission(player);
*/
			for(Iterator<Enemy> enemy=enemies.iterator();enemy.hasNext();)
			{
				Enemy enem =enemy.next();
				enem.move();
				int enemCurrentTerrainX=(int) Maths.floor(enem.getPosition().x/Terrain.SIZE);
				int enemCurrentTerrainZ=(int) Maths.floor(enem.getPosition().z/Terrain.SIZE);

				if(!terrainMonitorList.containsKey(new Key2D(enemCurrentTerrainX,enemCurrentTerrainZ)))
				{
					Terrain ter= new Terrain(new TerrainMultiTexture(grass,rock, snow),enemCurrentTerrainX,enemCurrentTerrainZ);
					TerrainMonitor monTer= new TerrainMonitor(ter);
					monTer.setLockedByGenThread(true);
					terrainMonitorList.put( new Key2D(enemCurrentTerrainX,enemCurrentTerrainZ),monTer);
					terrainHandler.addTerrainToQueue(monTer);
					
				}
				if(!terrainMonitorList.get(new Key2D(enemCurrentTerrainX,enemCurrentTerrainZ)).isLockedByGenThread()){
					terrainCollision.enemyCollission(terrainMonitorList.get(new Key2D(enemCurrentTerrainX,enemCurrentTerrainZ)).getTerrain(), enem);
				}
				mainRenderer.putInstance(enem.getEnemyMeshInstance());
				if(!enem.getAlive())
					enemy.remove();
					
	
				
			}

			oTextures.add(sniper.getFrame());
			oTextures.add(snoop.getFrame());

			//for(Key2D terrainMonitorKey:terrainMonitorList.keySet())
			
			//TRY/Catch m� til. the show must go on..
			try
			{
				for(Iterator<Key2D> it=terrainMonitorList.keySet().iterator();it.hasNext();)
				{
					Key2D terrainMonitorKey=it.next();
					
					if(!terrainMonitorList.get(terrainMonitorKey).isLockedByGenThread()){
						if(terrainMonitorList.get(terrainMonitorKey).isReadyToUpload())
						{
							terrainMonitorList.get(terrainMonitorKey).getTerrain().uploadMesh();
							terrainMonitorList.get(terrainMonitorKey).setReadyToUpload(false);
							
						}
					
					if(terrainMonitorList.get(terrainMonitorKey).isReadyToDraw())
					terrainMonitorList.get(terrainMonitorKey).getTerrain().updateCurrentLOD(player.getPosition());
						mainRenderer.putTerrain(terrainMonitorList.get(terrainMonitorKey).getTerrain());
					}
				}
			}
			catch(Exception e)
			{
				System.out.println("Not able to process terrain this frame.");
			}
			//mainRenderer.putTerrain(terrain);

			mainRenderer.putOverlays(oTextures);
			mainRenderer.render(light, player.getCamera(meshShader, terrainShader,waterShader));
			

			
			DisplayWindow.update();

			long deltaTime=System.currentTimeMillis()-timeClock;
			if(System.currentTimeMillis()-FPSCounterRefresh>400){
			Display.setTitle(1000/deltaTime+ " FPS");
			FPSCounterRefresh=System.currentTimeMillis();
			
			UnloadTerrains.emptyQueues();
			}
		}
		mainRenderer.cleanup();
		terrainHandler.cleanup();
		meshShader.destroy();
		waterShader.destroy();
		renderOverlay.cleanup();
		LoadMesh.destroy();
		DisplayWindow.destroy();
		
	}
	
}
