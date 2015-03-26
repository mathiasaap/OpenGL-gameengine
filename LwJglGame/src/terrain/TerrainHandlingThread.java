package terrain;

public class TerrainHandlingThread implements Runnable{
	
	Thread thread = new Thread(this,"terrainHandler");
	private boolean threadRunning;
	public TerrainHandlingThread()
	{
		threadRunning=true;
		System.out.println("thread start");
		thread.start();
		
	}

	@Override
	public void run() 
	{
	
	while(threadRunning){	
		System.out.println("From thread: "+thread.getName());
		
		
		
		try 
		{
			Thread.sleep(1000);
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
		threadRunning=false;
		thread.join();;
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	
	}
}
