package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class AbstractShader {

	private int shaderId;
	private int vsId, fsId;
	
	public AbstractShader(String vs, String fs)
	{
		vsId=loadShader(vs,GL20.GL_VERTEX_SHADER);
		fsId=loadShader(fs,GL20.GL_FRAGMENT_SHADER);
		shaderId=GL20.glCreateProgram();
		GL20.glAttachShader(shaderId, vsId);
		GL20.glAttachShader(shaderId, fsId);
		attributes();
		GL20.glLinkProgram(shaderId);
		GL20.glValidateProgram(shaderId);
		updateUniformLocation();
		
	}
	/*public ~AbstractShader()
	{
		destroy();
	}*/
	
	public void destroy()
	{
		unbindShader();
		GL20.glDetachShader(shaderId, fsId);
		GL20.glDetachShader(shaderId, vsId);
		GL20.glDeleteShader(fsId);
		GL20.glDeleteShader(vsId);
		GL20.glDeleteProgram(shaderId);
		
	}
	protected void bindAttribLocation(int attrib, String name)
	{
		GL20.glBindAttribLocation(shaderId, attrib, name);
		
	}
	
	protected int getUniformLocation(String uniformName)
	{
		return GL20.glGetUniformLocation(shaderId, uniformName);
	}
	
	protected abstract void updateUniformLocation();
	protected abstract void attributes();
	
	public void useProgram()
	{
		
		GL20.glUseProgram(shaderId);
	}
	
	public void unbindShader()
	{
		GL20.glUseProgram(0);
		
	}
	private static int loadShader(String filename, int shaderType)
	{
		StringBuilder sourceCode = new StringBuilder();
		try
		{
			String sourceLn="";
			BufferedReader inStream= new BufferedReader(new FileReader(filename));
			
			while((sourceLn=inStream.readLine())!=null)
			{
				sourceCode.append(sourceLn);
				sourceCode.append('\n');
			}
			inStream.close();
			
		} catch(IOException e){
			System.err.println("Couldn't open file!");
			e.printStackTrace();
			System.exit(-1);
		}
	
		int sid = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(sid,sourceCode);
		GL20.glCompileShader(sid);
		try{
			if(GL20.glGetShaderi(sid, GL20.GL_COMPILE_STATUS)==GL11.GL_FALSE)
				{
					throw new RuntimeException("Cannot compile shader!");
				}
			} catch(RuntimeException e){
				System.out.println(GL20.glGetShaderInfoLog(sid,512));
				System.err.println("Cannot compile shader!");
				System.exit(-1);
			}
		return sid;
	
	}

	protected void uploadFloat(int location, float data)
	{
		GL20.glUniform1f(location, data);
	}
	protected void uploadInt(int location, int data)
	{
		GL20.glUniform1i(location, data);
	}
	
	protected void uploadVec3f(int location, Vector3f data)
	{
		GL20.glUniform3f(location, data.x,data.y,data.z);
	}
	protected void uploadBool(int location, boolean data)
	{
		GL20.glUniform1i(location, data?1:0);
	}
	protected void uploadMat4f(int location, Matrix4f data)
	{

		FloatBuffer mat4=BufferUtils.createFloatBuffer(16);
		data.store(mat4);
		mat4.flip();
		GL20.glUniformMatrix4(location, false, mat4);
		//mat4.clear();
	}
}
