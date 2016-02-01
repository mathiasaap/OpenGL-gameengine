package terrain;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import misc.Key2D;

import org.lwjgl.util.vector.Vector3f;

import _testing.GameLoop;


public class TerrainHandlingThread implements Runnable{
	
	private Queue<TerrainMonitor> terrainsToHandle;
	Thread thread = new Thread(this,"terrainHandler");
	private AtomicBoolean threadRunning= new AtomicBoolean();
	private long lastUnloadCheck = System.currentTimeMillis();
	private int unloadWaitTime= 1000;//Checks if terrains can be unloaded every x seconds
	private Vector3f playerPosition;
	private Map<Key2D,TerrainMonitor> terrainMonitorList=new HashMap<>();
	private final ArrayList<Key2D> toRemove = new ArrayList<>();
	
	public TerrainHandlingThread(Vector3f playerPosition,Map<Key2D,TerrainMonitor> terrainMonitorList)
	{
		this.playerPosition=playerPosition;
		this.terrainMonitorList= terrainMonitorList;
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
			if(GameLoop.DEBUG)
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
		
		unloadTerrains();
		
	}
	}
	
	private void unloadTerrains()
	{
		if(System.currentTimeMillis()-lastUnloadCheck>unloadWaitTime)
		{
			Vector3f positionCopy = new Vector3f(playerPosition.x,playerPosition.y,playerPosition.z);
			
			//To make sure that the rendering thread will not try to draw a removed terrain
			for (Key2D key : toRemove)
			{
				/*TerrainMonitor tmon = terrainMonitorList.get(key);
				tmon.setReadyToDraw(false);
				tmon.setReadyToUpload(false);
				tmon.setHasHeightmap(false);
				tmon.clean();
				tmon.setLockedByGenThread(false);*/
				
				try
				{
					TerrainMonitor tmon=terrainMonitorList.get(key);
					tmon.clean();
					terrainMonitorList.remove(key);
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("Tough luck chuck!");
				}
			}
			toRemove.clear();
			
			
			//for (Map.Entry<Key2D, TerrainMonitor> tmonSet: terrainMonitorList.entrySet())
			try{
				for (Iterator< Map.Entry<Key2D, TerrainMonitor>> it = terrainMonitorList.entrySet().iterator();it.hasNext();)
				{
					//terrainMonitorList.g
					
						Map.Entry<Key2D, TerrainMonitor> tmonSet=it.next();
						
						//System.out.println(tmonSet.getKey());
						
						tmonSet.getValue().setLockedByGenThread(true);
						//Vector3f tempTerrainPosition=new Vector3f(tmon.getTerrain().getPosition().x,tmon.getTerrain().getPosition().y,tmon.getTerrain().getPosition().z);
						float tVec=Vector3f.sub(tmonSet.getValue().getTerrain().getPosition(), positionCopy, null).length();
						if(tVec>12000)
						{
							toRemove.add(tmonSet.getKey());
						}
						else
						{
							tmonSet.getValue().setLockedByGenThread(false);
						}
				
					
				}
			}
			catch(Exception e)
			{
				System.out.println("No luck chuck");
			}
			lastUnloadCheck=System.currentTimeMillis();
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
