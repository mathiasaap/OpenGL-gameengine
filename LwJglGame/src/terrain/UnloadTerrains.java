package terrain;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import rendering.LoadMesh;

public class UnloadTerrains {

	public static Queue<Integer> VBOsToDelete=new LinkedBlockingQueue<>();
	public static Queue<Integer> VAOsToDelete=new LinkedBlockingQueue<>();
	
	public static void emptyQueues()
	{
		while(!UnloadTerrains.VBOsToDelete.isEmpty())
		{
			LoadMesh.deleteVBO(UnloadTerrains.VBOsToDelete.poll());
		}
		
		while(!UnloadTerrains.VAOsToDelete.isEmpty())
		{
			LoadMesh.deleteVAO(UnloadTerrains.VAOsToDelete.poll());
		}
	}
}
