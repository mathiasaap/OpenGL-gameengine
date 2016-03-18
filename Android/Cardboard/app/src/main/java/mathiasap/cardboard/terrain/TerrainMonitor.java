package mathiasap.cardboard.terrain;

public class TerrainMonitor {

	private boolean hasHeightmap,readyToDraw,readyToUpload,lockedByGenThread;
	private Terrain terrain;
	
	public TerrainMonitor(Terrain terrain){
		hasHeightmap=false;
		readyToDraw=false;
		readyToUpload=false;
		lockedByGenThread=true;
		
		
		this.terrain=terrain;
		
		
	}
	
	public Terrain getTerrain()
	{
		return terrain;
	}
	
	public synchronized boolean isLockedByGenThread()
	{
		return lockedByGenThread;
	}
	
	public synchronized void setLockedByGenThread(boolean lock)
	{
		lockedByGenThread=lock;
	}

	public synchronized boolean isHasHeightmap() {
		return hasHeightmap;
	}

	public synchronized boolean isReadyToDraw() {
		//return readyToDraw;
		return terrain.isReadyToDraw();
	}

	public synchronized boolean isReadyToUpload() {
		return readyToUpload;
	}

	public synchronized void setHasHeightmap(boolean hasHeightmap) {
		this.hasHeightmap = hasHeightmap;
	}

	public synchronized void setReadyToDraw(boolean readyToDraw) {
		this.readyToDraw = readyToDraw;
	}

	public synchronized void setReadyToUpload(boolean readyToUpload) {
		this.readyToUpload = readyToUpload;
	}
	
	public synchronized void clean()
	{
		terrain.cleanup();
	}
	
	
}
