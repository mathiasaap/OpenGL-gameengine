package _testing;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import lighting.Light;
import matrix.Camera;
import matrix.Matrix;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.OBJLoader;
import mesh.TexMesh;
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
import textures.MeshTexture;
import textures.OverlayTexture;
import textures.TerrainMultiTexture;
import textures.TerrainTexture;

public class GameLoop {

	private static List<Terrain> terrains= new ArrayList<>();
	
	public static void main(String[] args) {
		long timeClock= System.nanoTime();
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
		Light light = new Light(new Vector3f(100,100000,-1000),new Vector3f(1,1,1));
		RayCasting mouseRay= new RayCasting(player.getCamera(),Matrix.calcProjectionMatrix());
		
		List<OverlayTexture> oTextures= new ArrayList<>();
		//oTextures.add(new OverlayTexture(loader.loadTexture("1"),new Vector2f(0.598f,-0.420f),new Vector2f(1.598f,1.420f)));
		
		MeshTexture tex= new MeshTexture(loader.loadTexture("sample_pic"));
		TerrainTexture grass= new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rock= new TerrainTexture(loader.loadTexture("rock"));
		TerrainTexture snow= new TerrainTexture(loader.loadTexture("snow"));
		tex.setShine(3);
		tex.setReflectivity(2);
		
		
		Terrain terrain= new Terrain(new TerrainMultiTexture(grass,rock, snow),0,0,loader);		
		terrains.add(terrain);

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
		//enemies.add(shrek);
		
		while(!Display.isCloseRequested())
		{
			timeClock=System.nanoTime();
			//System.out.println(enemies.size());
			/*
			mIns2.rotate(0.0f, 1f, 0f);
			mIns4.rotate(0.0f, -1.5f, 0f);
			mIns5.rotate(0.0f, -1.7f, 0f);
			mIns6.rotate(0.0f, -1.9f, 0f);
			mIns7.rotate(0.0f, -2.1f, 0f);
			mIns3.setPosition(new Vector3f(2f,(float)((Math.sin(16*argument)-Math.sin(15*argument))*3),0f));
			*/
			//light.setColor(new Vector3f(1f,1f-0.3f*(float)(Math.sin(argument)),1f-0.2f*(float)(Math.cos(argument))));
			controls.playing();
			player.move();
			//shrek.move();
			mouseRay.update();
			//System.out.println(mouseRay.getRay());
			/*Vector3f mouseray=mouseRay.getRay();
			mIns2.move(mouseray.x, mouseray.y, mouseray.z);*/
			terrainCollision.playerCollission(terrains, player);
			//terrainCollision.enemyCollission(terrains, shrek);
			//wSystem.out.println(shrek.getAlive());
			/*mainRenderer.putInstance(mIns2);
			mainRenderer.putInstance(mIns3);
			mainRenderer.putInstance(mIns4);
			mainRenderer.putInstance(mIns5);
			mainRenderer.putInstance(mIns6);*/
			for(Iterator<Enemy> enemy=enemies.iterator();enemy.hasNext();)
			{
				Enemy enem =enemy.next();
				enem.move();

				terrainCollision.enemyCollission(terrains, enem);
				mainRenderer.putInstance(enem.getEnemyMeshInstance());
				if(!enem.getAlive())
					enemy.remove();
					
	
				
			}
			//mainRenderer.putInstance(shrek.getEnemyMeshInstance());
			oTextures.add(sniper.getFrame());
			//mainRenderer.putInstance(terrain.getTerrain());
			
			mainRenderer.putTerrain(terrain);
			//mainRenderer.putInstance(mIns2);
			
			mainRenderer.render(light, player.getCamera(meshShader, terrainShader));
			renderOverlay.draw(oTextures);
			
			DisplayWindow.update();/*
			System.out.println(1000000000/(System.nanoTime()-timeClock));
			timeClock=System.nanoTime();*/
			long deltaTime=System.nanoTime()-timeClock;
			if(deltaTime>0&&(System.currentTimeMillis()-FPSCounterRefresh)>400){
			Display.setTitle("MLGSIM9000  "+1000.0/(float)(deltaTime/1000000f)+ " FPS");
			FPSCounterRefresh=System.currentTimeMillis();
			}
		}
		meshShader.destroy();
		loader.destroy();
		DisplayWindow.destroy();
	}
	
}
