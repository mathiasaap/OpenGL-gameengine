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
			double gridSize=getGridSize(terrain);
			Vector3f terrainPos= terrain.getTerrain().getPosition();
			/*int xVertLoc=(int) ((position.x+terrainPos.x)*gridSize);
			int zVertLoc=(int) ((position.z+terrainPos.z)*gridSize);*/
			
			int zVertLoc= (int) Math.floor((position.x+terrainPos.x)/gridSize);
			int xVertLoc= (int) Math.floor((position.z+terrainPos.z)/gridSize);
			double[][] heightmap=terrain.getHeightmap();
			//System.out.println(heightmap[xVertLoc][zVertLoc]+"   "+ (position.y-player.getHeadHeight()));
			if(xVertLoc>=0&&xVertLoc<terrain.getVertices()&&zVertLoc>=0&&zVertLoc<terrain.getVertices()){
			if(position.y-player.getHeadHeight()<heightmap[xVertLoc][zVertLoc])
			{
				position.y=(float) (heightmap[xVertLoc][zVertLoc]+player.getHeadHeight());
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
			double gridSize=getGridSize(terrain);
			Vector3f terrainPos= terrain.getTerrain().getPosition();
			int zVertLoc= (int) Math.floor((position.x+terrainPos.x)/gridSize);
			int xVertLoc= (int) Math.floor((position.z+terrainPos.z)/gridSize);
			double[][] heightmap=terrain.getHeightmap();
			if(xVertLoc>=0&&xVertLoc<terrain.getVertices()&&zVertLoc>=0&&zVertLoc<terrain.getVertices()){
			if(position.y-enemy.getHeadHeight()<heightmap[xVertLoc][zVertLoc])
			{
				position.y=(float) (heightmap[xVertLoc][zVertLoc]+enemy.getHeadHeight());
				enemy.stopFalling();
			}}
			//System.out.println(xVertLoc+ "   "+zVertLoc);
		}
		
	}
	
	private double getGridSize(Terrain terrain)
	{
	return (float)(terrain.getSIZE()/(float)(terrain.getVertices()-1));	
	}
	
	
}
