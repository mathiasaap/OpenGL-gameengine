package rendering;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayWindow {
	
	private static final int WIDTH=1200, HEIGHT=900;
	private static int MAX_FPS=60;
	
	public static void create()
	{
		ContextAttribs attributes = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setTitle("LWJGL");
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
