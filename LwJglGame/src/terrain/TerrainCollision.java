package terrain;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import enemies.Enemy;
import player.Player;

public class TerrainCollision {

	
	public TerrainCollision()
	{}
	
	
	//https://en.wikipedia.org/wiki/Barycentric_coordinate_system#Conversion_between_barycentric_and_Cartesian_coordinates
	private float getTerrainPosHeight(Vector3f v1,Vector3f v2,Vector3f v3,Vector3f ent)
	{
		float determinant= (v2.z-v3.z)*(v1.x-v3.x) + (v3.x-v2.x)*(v1.z-v3.z);
		float lambda1 = ((v2.z-v3.z)*(ent.x-v3.x)+(v3.x-v2.z)*(ent.z-v3.z))/determinant;
		float lambda2 = ((v3.z-v1.z)*(ent.x-v3.x)+(v1.x-v3.z)*(ent.z-v3.z))/determinant;
		float lambda3 = 1-lambda1-lambda2;
		
		return lambda1*v1.y+lambda2*v2.y+lambda3*v3.y;
		
		
	}
	
	public void playerCollission(List<Terrain> terrains, Player player)
	{
		Vector3f position=player.getPosition();
		for(Terrain terrain:terrains)
		{
			
			double gridSize=getGridSize(terrain);
			int zVertLoc= (int) Math.floor((position.x+terrain.getPosition().x)/gridSize);
			int xVertLoc= (int) Math.floor((position.z+terrain.getPosition().z)/gridSize);

			
			
				float terrainPositionHeight=process(terrain,xVertLoc,zVertLoc,gridSize);
				
				
				
				if(xVertLoc>=0&&xVertLoc<terrain.getVertices()&&zVertLoc>=0&&zVertLoc<terrain.getVertices()){
				if(position.y-player.getHeadHeight()<terrainPositionHeight)
				{
					position.y=(float) (terrainPositionHeight+player.getHeadHeight());
					player.stopFalling();
				}}
				}
			
			
			//System.out.println(xVertLoc+ "   "+zVertLoc);
		}
		

	
	

	
	public void enemyCollission(List<Terrain> terrains, Enemy enemy)
	{
		Vector3f position=enemy.getPosition();
		for(Terrain terrain:terrains)
		{
			double gridSize=getGridSize(terrain);
			int zVertLoc= (int) Math.floor((position.x+terrain.getPosition().x)/gridSize);
			int xVertLoc= (int) Math.floor((position.z+terrain.getPosition().z)/gridSize);

			
			float terrainPositionHeight=process(terrain,xVertLoc,zVertLoc,gridSize);
			
			
			if(xVertLoc>=0&&xVertLoc<terrain.getVertices()-1&&zVertLoc>=0&&zVertLoc<terrain.getVertices()-1){
			if(position.y-enemy.getHeadHeight()<terrainPositionHeight)
			{
				position.y=(float) (terrainPositionHeight+enemy.getHeadHeight());
				enemy.stopFalling();
			}}
			}
		}
		
	
	
	private float process(Terrain terrain,int xVertLoc, int zVertLoc,double gridSize)
	{

		Vector3f terrainPos= terrain.getPosition();
		

		float vertexXCoords = (float) ((terrainPos.x%gridSize)/gridSize);
		float vertexZCoords = (float) ((terrainPos.z%gridSize)/gridSize);
		double[][] heightmap=terrain.getHeightmap();
		
		
		if(xVertLoc>=0&&xVertLoc<terrain.getVertices()-1&&zVertLoc>=0&&zVertLoc<terrain.getVertices()-1){
			
			if(vertexXCoords>1-vertexZCoords)//bottom vertex
			{
				return getTerrainPosHeight(new Vector3f(1,(float) heightmap[xVertLoc+1][zVertLoc],0),new Vector3f(1,(float) heightmap[xVertLoc+1][zVertLoc+1],1), new Vector3f(0,(float) heightmap[xVertLoc][zVertLoc+1],1),new Vector3f(vertexXCoords,0,vertexZCoords));
				
			}
			else{
				
				return getTerrainPosHeight(new Vector3f(0,(float) heightmap[xVertLoc][zVertLoc],0), new Vector3f(1,(float) heightmap[xVertLoc+1][zVertLoc],0), new Vector3f(0,(float) heightmap[xVertLoc][zVertLoc+1],1), new Vector3f(vertexXCoords,0,vertexZCoords));
				
				//Top vertex
			}
			}
		return Integer.MIN_VALUE;
		
		
	}
	
	
	private double getGridSize(Terrain terrain)
	{
	return (float)(terrain.getSIZE()/(float)(terrain.getVertices()-1));	
	}
	
	
}
