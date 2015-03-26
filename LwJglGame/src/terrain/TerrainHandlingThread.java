package terrain;


import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class TerrainHandlingThread implements Runnable{
	
	private Queue<Terrain> terrainsToHandle;
	Thread thread = new Thread(this,"terrainHandler");
	private AtomicBoolean threadRunning= new AtomicBoolean();
	public TerrainHandlingThread()
	{
		threadRunning.set(true);
		
		terrainsToHandle= new LinkedBlockingQueue<Terrain>();
		//terrainsToHandle.
		System.out.println("thread start");
		thread.start();
		
	}
	
	public void addTerrainToQueue(Terrain terrain)
	{
		if(!terrainsToHandle.contains(terrain))
		{
			terrainsToHandle.offer(terrain);
		}
	}


	@Override
	public void run() 
	{
	while(threadRunning.get()){	
		
		while(!terrainsToHandle.isEmpty()){
		Terrain nextTerrainToHandle = terrainsToHandle.poll();
		System.out.println(nextTerrainToHandle.terrainGridX);
		//System.out.println("From thread: "+thread.getName());
		if(!nextTerrainToHandle.isHasHeightmap()){
			nextTerrainToHandle.generateHeightmap();
			}
		if(!nextTerrainToHandle.isReadyToUpload())
			nextTerrainToHandle.generateTerrain();
		
		
		}
		
		try 
		{
			Thread.sleep(500);
		} catch (InterruptedException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
	}
	}
	public void cleanup()
	{
	try {
		threadRunning.set(false);
		thread.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
}
