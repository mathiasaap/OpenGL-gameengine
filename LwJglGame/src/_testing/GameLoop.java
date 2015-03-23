package _testing;


import java.util.ArrayList;
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
		/*Mesh m2= objloader.loadObj("monkey", loader);
		TexMesh m2mesh = new TexMesh(m2,tex);
		MeshInstance mIns2 = new MeshInstance(m2mesh, new Vector3f(0,0,0),0,0,0,1);
		Mesh m3= objloader.loadObj("batman", loader);
		Mesh m4= objloader.loadObj("Avent", loader);
		
		TexMesh m2mesh = new TexMesh(m2,tex);
		TexMesh m3mesh = new TexMesh(m3,tex);
		TexMesh m4mesh = new TexMesh(m4,tex);
		
		MeshInstance mIns2 = new MeshInstance(m2mesh, new Vector3f(0,0,-2.5f),0,0,0,1);
		MeshInstance mIns3 = new MeshInstance(m3mesh, new Vector3f(2f,0,0),0,0,0,1);
		MeshInstance mIns4 = new MeshInstance(m4mesh, new Vector3f(-4f,3f,0),0,0,0,1);
		MeshInstance mIns5 = new MeshInstance(m4mesh, new Vector3f(-4f,0f,0),0,0,0,1);
		MeshInstance mIns6 = new MeshInstance(m4mesh, new Vector3f(-4f,-3f,0),0,0,0,1);
		MeshInstance mIns7 = new MeshInstance(m4mesh, new Vector3f(-4f,-6f,0),0,0,0,1);
		*/
				
		Mesh shrekmodel= objloader.loadObj("shrek", loader);
		TexMesh shrekMesh = new TexMesh(shrekmodel,tex);
		
		MeshInstance shrekMeshIns = new MeshInstance(shrekMesh, new Vector3f(0,0,0),0,0,0,1);
		Random random= new Random();
		//Enemy shrek = new Shrek(new Vector3f(150,0,150),player,shrekMeshIns);
		
		for(int i=0;i <30; i++)
		{
			MeshInstance enIns=new MeshInstance(shrekMesh,new Vector3f(0,0,0),0,0,0,1);
			enemies.add(new Shrek(new Vector3f(random.nextInt(2000),random.nextInt(600),random.nextInt(2000)),player,enIns));
			
		}
		//enemies.add(shrek);
		
		while(!Display.isCloseRequested())
		{
			timeClock=System.nanoTime();
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
			for(Enemy enem: enemies)
			{
				enem.move();

				terrainCollision.enemyCollission(terrains, enem);
				if(enem.getAlive())
					mainRenderer.putInstance(enem.getEnemyMeshInstance());
				
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
