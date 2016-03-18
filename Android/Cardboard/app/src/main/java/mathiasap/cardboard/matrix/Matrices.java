package mathiasap.cardboard.matrix;

import android.opengl.Matrix;


import com.google.vrtoolkit.cardboard.Eye;

import mathiasap.cardboard.misc.Vektor2f;
import mathiasap.cardboard.misc.Vektor3f;
import mathiasap.cardboard.shaders.AbstractShader;
import mathiasap.cardboard.shaders.MeshShader;
import mathiasap.cardboard.shaders.TerrainShader;
import mathiasap.cardboard.shaders.WaterShader;

public class Matrices {
	
	
	
	private static final float FOV_WALKING=70;
	private static final float RUNNING=120;
	public static final float NEAR=0.1f;
	public static final float FAR=15000.0f;
	
	public static float[] transformationMatrix(Vektor3f translate, float rotX, float rotY, float rotZ, float scale){
		float[] matrix = new float[16];
		Matrix.setIdentityM(matrix,0);
		

		Matrix.translateM(matrix, 0, translate.x, translate.y, translate.z);
		if(rotX!=0) {
			Matrix.rotateM(matrix, 0, rotX, 1, 0, 0);
		}
		if(rotY!=0) {
			Matrix.rotateM(matrix, 0, rotY, 0, 1, 0);
		}
		if(rotZ!=0) {
			Matrix.rotateM(matrix, 0, rotZ, 0, 0, 1);
		}
		Matrix.scaleM(matrix,0,scale,scale,scale);

		/*Matrix4f.translate(translate, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotX),new Vector3f(1,0,0), matrix,matrix);
		Matrix4f.rotate((float) Math.toRadians(rotY),new Vector3f(0,1,0), matrix,matrix);
		Matrix4f.rotate((float) Math.toRadians(rotZ),new Vector3f(0,0,1), matrix,matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);*/
		
		
	return matrix;	
	}
	
	public static float[] transformationMatrix(Vektor2f translate, Vektor2f scale)
	{
		float[] matrix = new float[16];
		Matrix.setIdentityM(matrix, 0);
		Matrix.translateM(matrix, 0, translate.x, translate.y, 0);
		Matrix.scaleM(matrix, 0, scale.x, scale.y, 1);

		return matrix;
	}
	public static float[] transformationMatrix()
	{
		float[] matrix = new float[16];
		Matrix.setIdentityM(matrix, 0);

		Matrix.rotateM(matrix, 0, (float) Math.PI, 0, 0, 1);

		return matrix;
	}




	public static void uploadProjectionMatrix(AbstractShader shader,float[] projection)
	{
		shader.useProgram();
		shader.loadProjectionMatrix(projection);
		shader.unbindShader();

		System.out.println("Uploaded projection matrix to mesh shader");
		
	}


	
	

}
