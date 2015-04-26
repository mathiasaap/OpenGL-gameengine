package terrain;


import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;


public class TerrainHandlingThread implements Runnable{
	
	private Queue<TerrainMonitor> terrainsToHandle;
	Thread thread = new Thread(this,"terrainHandler");
	private AtomicBoolean threadRunning= new AtomicBoolean();
	public TerrainHandlingThread()
	{
		threadRunning.set(true);
		
		terrainsToHandle= new LinkedBlockingQueue<TerrainMonitor>();
		//terrainsToHandle.
		System.out.println("thread start");
		thread.start();
		
	}
	
	public void addTerrainToQueue(TerrainMonitor terrainMonitor)
	{
		if(!terrainsToHandle.contains(terrainMonitor))
		{
			terrainsToHandle.offer(terrainMonitor);
		}
	}


	@Override
	public void run() 
	{
	while(threadRunning.get()){	
		
		while(!terrainsToHandle.isEmpty()){
		TerrainMonitor nextTerrainToHandle = terrainsToHandle.poll();
		//System.out.println(nextTerrainToHandle.getTerrain().terrainGridX);
		//System.out.println("From thread: "+thread.getName());

		
		if(!nextTerrainToHandle.isReadyToUpload()){
			long generationTime= System.currentTimeMillis();
			if(!nextTerrainToHandle.isHasHeightmap()){
				nextTerrainToHandle.getTerrain().generateHeightmap();
				nextTerrainToHandle.setHasHeightmap(true);
				}
			
			nextTerrainToHandle.getTerrain().generateTerrain();
			nextTerrainToHandle.setReadyToUpload(true);
			System.out.println("Time to generate terrain "+nextTerrainToHandle.getTerrain().terrainGridX+","+nextTerrainToHandle.getTerrain().terrainGridZ+": "+(System.currentTimeMillis()-generationTime)+"ms");
		}
		nextTerrainToHandle.setLockedByGenThread(false);
		
		}
		
		try 
		{
			Thread.sleep(100);
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
