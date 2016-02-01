package terrain;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class UnloadTerrains {

	public static Queue<Integer> VBOsToDelete=new LinkedBlockingQueue<>();
	public static Queue<Integer> VAOsToDelete=new LinkedBlockingQueue<>();
}
