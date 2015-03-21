package matrix;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import shaders.MeshShader;
import shaders.TerrainShader;

public class Matrix {
	
	
	
	private static final float FOV=70;
	private static final float NEARF=0.2f;
	private static final float FARF=1000.0f;
	
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
	
	private static Matrix4f calcProjectionMatrix()
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
		System.out.println(aspectRatio);
		return pMat;
	}
	
	public static void uploadProjectionMatrix(MeshShader meshShader)
	{
		meshShader.useProgram();
		meshShader.loadProjectionMatrix(calcProjectionMatrix());
		meshShader.unbindShader();
		System.out.println("Uploaded projection matrix to mesh shader");
		
	}
	
	public static void uploadProjectionMatrix(TerrainShader terrainShader)
	{
		terrainShader.useProgram();
		terrainShader.loadProjectionMatrix(calcProjectionMatrix());
		terrainShader.unbindShader();
		System.out.println("Uploaded projection matrix to terrain shader");
		
	}
	
	

}