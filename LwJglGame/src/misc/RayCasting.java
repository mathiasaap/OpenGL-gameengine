package misc;

import matrix.Camera;






import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import rendering.DisplayWindow;

public class RayCasting {
	
	Vector3f ray;
	Camera camera;
	Matrix4f projection,view,invProjection;

	public RayCasting(Camera camera, Matrix4f projection)
	{
		this.camera=camera;
		this.projection=projection;
		this.invProjection=Matrix4f.invert(projection, null);
		this.view=camera.setupViewMatrix();
		
	}

	public Vector3f getRay()
	{
		return ray;	
	}
	
	public void update()
	{
		view=camera.setupViewMatrix();
		ray=calcRay(0.0075f*DisplayWindow.WIDTH+DisplayWindow.WIDTH/2.0f,0.0044f*DisplayWindow.HEIGHT+DisplayWindow.HEIGHT/2.0f);
		//ray=calcRay(Mouse.getX(),Mouse.getY());
	}
	
	private Vector3f calcRay(float x, float y)
	{
		Vector2f normalized=normalizedSpace(x,y);
		Vector4f clipSpace = new Vector4f(normalized.x,normalized.y,-1f,1f);
		Vector4f camSpace = eyeSpace(clipSpace);
		return worldSpace(camSpace);
		
	}
	
	private Vector2f normalizedSpace(float x,float y)
	{
		x=(float) ((2.0f*x)/DisplayWindow.WIDTH-1f);
		y=(float) ((2.0f*y)/DisplayWindow.HEIGHT-1f);
		return new Vector2f(x,y);
	}

	
	private Vector4f eyeSpace(Vector4f clipSpace)
	{
		Vector4f camSpace = Matrix4f.transform(invProjection,clipSpace,null);
		return new Vector4f(camSpace.x,camSpace.y,-1f,0f);
	}
	
	private Vector3f worldSpace(Vector4f camSpace)
	{
		Matrix4f invView = Matrix4f.invert(view, null);	
		Vector4f worldSpaceCoord= Matrix4f.transform(invView,camSpace,null);
		return (Vector3f) new Vector3f(worldSpaceCoord.x,worldSpaceCoord.y,worldSpaceCoord.z).normalise();
	}
}

