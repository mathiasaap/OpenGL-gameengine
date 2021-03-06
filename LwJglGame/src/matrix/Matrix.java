package matrix;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import _testing.GameLoop;
import shaders.MeshShader;
import shaders.TerrainShader;
import shaders.WaterShader;

public class Matrix {
	
	
	
	private static final float FOV_WALKING=70;
	private static final float RUNNING=120;
	private static final float NEARF=0.1f;
	private static final float FARF=15000.0f;
	
	public static Matrix4f transformationMatrix(Vector3f translate, float rotX, float rotY, float rotZ, float scale){
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		
		
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, Matrix4f.rotate((float) Math.toRadians(rotZ),new Vector3f(0,0,1), matrix,Matrix4f.rotate((float) Math.toRadians(rotY),new Vector3f(0,1,0), matrix,Matrix4f.rotate((float) Math.toRadians(rotX),new Vector3f(1,0,0), matrix,Matrix4f.translate(translate, matrix, matrix)))));
		
		
		/*Matrix4f.translate(translate, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotX),new Vector3f(1,0,0), matrix,matrix);
		Matrix4f.rotate((float) Math.toRadians(rotY),new Vector3f(0,1,0), matrix,matrix);
		Matrix4f.rotate((float) Math.toRadians(rotZ),new Vector3f(0,0,1), matrix,matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);*/
		
		
	return matrix;	
	}
	
	public static Matrix4f transformationMatrix(Vector2f translate, Vector2f scale)
	{
		Matrix4f mat= new Matrix4f();
		mat.setIdentity();
		mat.translate(translate,mat,mat);
		mat.scale(new Vector3f(scale.x,scale.y,1.0f),mat,mat);
		return mat;
	}
	public static Matrix4f transformationMatrix()
	{
		Matrix4f mat= new Matrix4f();
		mat.setIdentity();
		Matrix4f.rotate((float) Math.PI, new Vector3f(0,0,1), mat, mat);
		Matrix4f.rotate((float) Math.PI, new Vector3f(0,0,0), mat, mat);
		return mat;
	}
	
	
	public static Matrix4f calcProjectionMatrix()
	{
		return calcProjectionMatrix(FOV_WALKING);
	}
	public static Matrix4f calcProjectionMatrix(float FOV)
	{
		Matrix4f pMat= new Matrix4f();
		
		float aspectRatio= (float)Display.getWidth()/(float)Display.getHeight();
		float x= (float) (1f/Math.tan(Math.toRadians(FOV/2f)));
		float y=x/aspectRatio;
		/*float y=(float) (1f/Math.tan(Math.toRadians(FOV/2f))/aspectRatio);
		float x= y*aspectRatio;*/
		
		float len= FARF-NEARF;
		
		pMat.m00=x;
		pMat.m11=y;
		pMat.m22=-(FARF+NEARF)/len;
		pMat.m23=-1;
		pMat.m32= -2*FARF*NEARF/len;
		pMat.m33=0;
		return pMat;
	}
	
	public static void uploadProjectionMatrix(MeshShader meshShader){
		uploadProjectionMatrix(meshShader,FOV_WALKING);
	}
	
	public static void uploadProjectionMatrix(MeshShader meshShader, float FOV)
	{
		meshShader.useProgram();
		meshShader.loadProjectionMatrix(calcProjectionMatrix(FOV));
		meshShader.unbindShader();
		if(GameLoop.DEBUG)
		System.out.println("Uploaded projection matrix to mesh shader");
		
	}
	public static void uploadProjectionMatrix(TerrainShader terrainShader)
	{
		uploadProjectionMatrix(terrainShader,FOV_WALKING);
	}
	
	public static void uploadProjectionMatrix(TerrainShader terrainShader,float FOV)
	{
		terrainShader.useProgram();
		terrainShader.loadProjectionMatrix(calcProjectionMatrix(FOV));
		terrainShader.unbindShader();
		if(GameLoop.DEBUG)
		System.out.println("Uploaded projection matrix to terrain shader");
		
	}
	
	public static void uploadProjectionMatrix(WaterShader waterShader)
	{
		uploadProjectionMatrix(waterShader,FOV_WALKING);
	}
	
	public static void uploadProjectionMatrix(WaterShader waterShader,float FOV)
	{
		waterShader.useProgram();
		waterShader.loadProjectionMatrix(calcProjectionMatrix(FOV));
		waterShader.unbindShader();
		if(GameLoop.DEBUG)
		System.out.println("Uploaded projection matrix to water shader");
		
	}

	
	

}
