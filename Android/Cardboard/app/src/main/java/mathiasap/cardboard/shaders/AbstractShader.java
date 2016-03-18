package mathiasap.cardboard.shaders;

import android.opengl.GLES30;
import android.renderscript.Matrix4f;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardActivity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import mathiasap.cardboard.misc.Vektor2f;
import mathiasap.cardboard.misc.Vektor3f;
import mathiasap.cardboard.misc.Vektor4f;


public abstract class AbstractShader {

	private boolean playerRunning=false;
	
	private float currentFOV=70;
	private float goalFOV=70;
	private int shaderId;
	private int vsId, fsId;
	
	public AbstractShader(InputStreamReader vs, InputStreamReader fs)
	{
		vsId=loadShader(vs,GLES30.GL_VERTEX_SHADER);
		fsId=loadShader(fs,GLES30.GL_FRAGMENT_SHADER);
		shaderId=GLES30.glCreateProgram();
		GLES30.glAttachShader(shaderId, vsId);
		GLES30.glAttachShader(shaderId, fsId);
		attributes();
		GLES30.glLinkProgram(shaderId);
		GLES30.glValidateProgram(shaderId);
		updateUniformLocation();
		
	}

	
	public boolean getPlayerRunning()
	{
		return playerRunning;
	}
	public void setPlayerRunning(boolean playerRunning)
	{
		this.playerRunning=playerRunning;
	}
	public void destroy()
	{
		unbindShader();
		GLES30.glDetachShader(shaderId, fsId);
		GLES30.glDetachShader(shaderId, vsId);
		GLES30.glDeleteShader(fsId);
		GLES30.glDeleteShader(vsId);
		GLES30.glDeleteProgram(shaderId);
		
	}
	protected void bindAttribLocation(int attrib, String name)
	{
		GLES30.glBindAttribLocation(shaderId, attrib, name);
		
	}
	
	protected int getUniformLocation(String uniformName)
	{
		return GLES30.glGetUniformLocation(shaderId, uniformName);
	}
	
	protected abstract void updateUniformLocation();
	protected abstract void attributes();
	
	public void useProgram()
	{

		GLES30.glUseProgram(shaderId);
	}
	
	public void unbindShader()
	{
		GLES30.glUseProgram(0);
		
	}

	private String loadShaderSource(InputStreamReader resId)
	{
		try {
			BufferedReader reader = new BufferedReader(resId);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}

			reader.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	private int loadShader(InputStreamReader resId, int shaderType)
	{
		String code = loadShaderSource(resId);
		int shader = GLES30.glCreateShader(shaderType);
		GLES30.glShaderSource(shader, code);
		GLES30.glCompileShader(shader);


		final int[] compileStatus = new int[1];
		GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);


	//	readRawTextFile
		try{

			if(compileStatus[0]==0)
				{
					throw new RuntimeException("Cannot compile shader!");
				}
			} catch(RuntimeException e){
				Log.i("shader",GLES30.glGetShaderInfoLog(shader));
				Log.i("shader","Cannot compile " + getShaderType() +" shader!");
				System.exit(-1);
			}
		return shader;
	
	}

	protected void uploadFloat(int location, float data)
	{
		GLES30.glUniform1f(location, data);
	}
	protected void uploadInt(int location, int data)
	{
		GLES30.glUniform1i(location, data);
	}

	protected void uploadVec2f(int location, Vektor2f data)
	{
		GLES30.glUniform2f(location, data.x,data.y);
	}
	protected void uploadVec3f(int location, Vektor3f data)
	{
		GLES30.glUniform3f(location, data.x,data.y,data.z);
	}
	
	protected void uploadVec4f(int location, Vektor4f data)
	{
		GLES30.glUniform4f(location, data.x,data.y,data.z,data.w);
	}
	protected void uploadBool(int location, boolean data)
	{
		GLES30.glUniform1i(location, data?1:0);
	}

	//private FloatBuffer mat4=BufferUtils.createFloatBuffer(16);

	public abstract void loadProjectionMatrix(float[] matrix);
	public abstract void loadViewMatrix(float[] matrix);

	protected void uploadMat4f(int location, float[] data)
	{
		GLES30.glUniformMatrix4fv(location, 1, false,data,0);
	}
	protected abstract String getShaderType();
	public abstract void updateTick();

}
