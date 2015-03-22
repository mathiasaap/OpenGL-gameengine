package _testing;


import java.util.ArrayList;
import java.util.List;

import lighting.Light;
import matrix.Camera;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.OBJLoader;
import mesh.TexMesh;

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
		DisplayWindow.create();
		LoadMesh loader= new LoadMesh();
		MeshShader meshShader = new MeshShader("res/shaders/vsMesh.glsl","res/shaders/fsMesh.glsl");
		TerrainShader terrainShader = new TerrainShader("res/shaders/vsTerrain.glsl","res/shaders/fsTerrain.glsl");
		//RenderTerrain terrainRenderer = new RenderTerrain(terrainShader);
		TerrainCollision terrainCollision=new TerrainCollision();
		Renderer mainRenderer = new Renderer(meshShader,terrainShader);
		RenderOverlay renderOverlay = new RenderOverlay(loader);
		
		
		Player player= new Player(new Vector3f(0f,0f,0f));
		Sniper sniper=new Sniper(loader);
		Controls controls= new Controls(player,sniper);
		Light light = new Light(new Vector3f(100,2000,100),new Vector3f(1,1,1));
		
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
		
		Enemy shrek = new Shrek(new Vector3f(150,0,150),player,shrekMeshIns);
		
		
		float argument=0;
		while(!Display.isCloseRequested())
		{
			argument+=0.005;
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
			shrek.move();
			terrainCollision.playerCollission(terrains, player);
			terrainCollision.enemyCollission(terrains, shrek);
			/*mainRenderer.putInstance(mIns2);
			mainRenderer.putInstance(mIns3);
			mainRenderer.putInstance(mIns4);
			mainRenderer.putInstance(mIns5);
			mainRenderer.putInstance(mIns6);*/
			
			
			//mainRenderer.putInstance(shrek.getEnemyMeshInstance());
			oTextures.add(sniper.getFrame());
			//mainRenderer.putInstance(terrain.getTerrain());
			mainRenderer.putTerrain(terrain);
			
			mainRenderer.render(light, player.getCamera(meshShader, terrainShader));
			renderOverlay.draw(oTextures);
			
			DisplayWindow.update();/*
			System.out.println(1000000000/(System.nanoTime()-timeClock));
			timeClock=System.nanoTime();*/
			if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
				System.gc();
			}
		}
		meshShader.destroy();
		loader.destroy();
		DisplayWindow.destroy();
	}
	
}
