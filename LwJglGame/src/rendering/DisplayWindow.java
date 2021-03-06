package rendering;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayWindow {
	
	//public static final int WIDTH=1200, HEIGHT=900;
	public static final int WIDTH=1000, HEIGHT=750;
	private static int MAX_FPS=60;
	
	public static void create()
	{
		ContextAttribs attributes = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		try {
			
			DisplayMode displayMode = null;
	        DisplayMode[] modes = Display.getAvailableDisplayModes();

	         for (int i = 0; i < modes.length; i++)
	         {
	             if (modes[i].getWidth() == WIDTH
	             && modes[i].getHeight() == HEIGHT
	             && modes[i].isFullscreenCapable())
	               {
	                    displayMode = modes[i];
	       	         Display.setDisplayMode(displayMode);
	                    
	               }
	         }

			Display.setTitle("LWJGL");
			Display.setVSyncEnabled(true);
			Display.setDisplayMode(new DisplayMode(WIDTH,HEIGHT));
			Display.create(new PixelFormat(),attributes);
	        
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glViewport(0,0,WIDTH,HEIGHT);
		
	}
	public static void update()
	{
		Display.sync(MAX_FPS);
		Display.update();
		
	}
	public static void destroy()
	{
		Display.destroy();
	}
	public static int getWidth() {
		return WIDTH;
	}
	public static int getHeight() {
		return HEIGHT;
	}
	
	

}
