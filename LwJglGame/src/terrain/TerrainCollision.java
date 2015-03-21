package terrain;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import enemies.Enemy;
import player.Player;

public class TerrainCollision {

	
	public TerrainCollision()
	{}
	
	public void playerCollission(List<Terrain> terrains, Player player)
	{
		Vector3f position=player.getPosition();
		for(Terrain terrain:terrains)
		{
			Vector3f terrainPos= terrain.getTerrain().getPosition();
			int xVertLoc=(int) ((position.x+terrainPos.x)*terrain.getW()/terrain.getSIZE());
			int zVertLoc=(int) ((position.z+terrainPos.z)*terrain.getL()/terrain.getSIZE());
			double[][] heightmap=terrain.getHeightmap();
			//System.out.println(heightmap[xVertLoc][zVertLoc]+"   "+ (position.y-player.getHeadHeight()));
			if(xVertLoc>=0&&xVertLoc<terrain.getW()&&zVertLoc>=0&&zVertLoc<terrain.getL()){
			while(position.y-player.getHeadHeight()<heightmap[xVertLoc][zVertLoc])
			{
				position.y+=0.001;
				player.stopFalling();
			}}
			//System.out.println(xVertLoc+ "   "+zVertLoc);
		}
		
	}
	
	
	public void enemyCollission(List<Terrain> terrains, Enemy enemy)
	{
		Vector3f position=enemy.getPosition();
		for(Terrain terrain:terrains)
		{
			Vector3f terrainPos= terrain.getTerrain().getPosition();
			int xVertLoc=(int) ((position.x+terrainPos.x)*terrain.getW()/terrain.getSIZE());
			int zVertLoc=(int) ((position.z+terrainPos.z)*terrain.getL()/terrain.getSIZE());
			double[][] heightmap=terrain.getHeightmap();
			if(xVertLoc>=0&&xVertLoc<terrain.getW()&&zVertLoc>=0&&zVertLoc<terrain.getL()){
			while(position.y-enemy.getHeadHeight()<heightmap[xVertLoc][zVertLoc])
			{
				position.y+=0.001;
				enemy.stopFalling();
			}}
			//System.out.println(xVertLoc+ "   "+zVertLoc);
		}
		
	}
	
	
}
