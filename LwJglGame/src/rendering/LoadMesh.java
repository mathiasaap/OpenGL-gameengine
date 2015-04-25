package rendering;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import mesh.Mesh;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class LoadMesh {

	private Stack<Integer> VaoStack = new Stack<>();
	private Stack<Integer> VboStack = new Stack<>();
	private Stack<Integer> TexStack = new Stack<>();
	private List<Integer> VaoList= new ArrayList<>();
	private List<Integer> VboList= new ArrayList<>();
	private List<Integer> TexList= new ArrayList<>();
	
	
	
	
	private int createVAO()
	{
		int VAO = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(VAO);
		VaoStack.push(VAO);
		return VAO;
	}
	
	private void unbind()
	{
	
	GL30.glBindVertexArray(0);
	}
	
	public Mesh loadNewMesh(float[] pos)
	{
		int VAO = createVAO();
		List<Integer> meshVBOs= new ArrayList<>();
		meshVBOs.add(storeAttrib(0,pos,2));
		
		unbind();
		return new Mesh(pos.length/2,VAO,meshVBOs);
	}
	public Mesh loadNewMesh(float[] pos, int v)
	{
		int VAO = createVAO();
		List<Integer> meshVBOs= new ArrayList<>();
		meshVBOs.add(storeAttrib(0,pos,v));
		
		unbind();
		return new Mesh(pos.length/v,VAO,meshVBOs);
	}
	
	public Mesh loadNewMesh(float[] pos, int[] indices, float[] texUV, float[] normals)
	{
		int VAO = createVAO();

		List<Integer> meshVBOs= new ArrayList<>();
		loadIndicesBuffer(indices);
		

		meshVBOs.add(storeAttrib(0,pos,3));
		meshVBOs.add(storeAttrib(1,texUV,2));
		meshVBOs.add(storeAttrib(2,normals,3));
		unbind();
		return new Mesh(indices.length,VAO,meshVBOs);
	}
	
	
	
	private int storeAttrib(int attrib, float[] bfr,int cSze)
	{
		int VBO= GL15.glGenBuffers();
		VboStack.push(VBO);
		VboList.add(VBO);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,VBO);
		FloatBuffer fbfr = floatToFloatBuffer(bfr);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fbfr, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attrib,cSze,GL11.GL_FLOAT,false,0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
		return VBO;
		
	}
	
	public int loadTexture(String filename)
	{
		Texture tex= null;
		try {
			tex= TextureLoader.getTexture("PNG", new FileInputStream("res/"+filename+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR_MIPMAP_LINEAR);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int texId=tex.getTextureID();
		TexStack.push(texId);
		TexList.add(texId);
		return texId;
		
	}
	
	private void loadIndicesBuffer(int[] indices)
	{
		int VBO =GL15.glGenBuffers();
		VboStack.push(VBO);
		VboList.add(VBO);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,VBO);
		IntBuffer ibfr= intToIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,ibfr,GL15.GL_STATIC_DRAW);
		
		
	}
	
	private FloatBuffer floatToFloatBuffer(float[] bfr)
	{
		FloatBuffer fbfr = BufferUtils.createFloatBuffer(bfr.length);
		fbfr.put(bfr);
		fbfr.flip();
		return fbfr;
	}
	private IntBuffer intToIntBuffer(int[] bfr)
	{
		IntBuffer ibfr= BufferUtils.createIntBuffer(bfr.length);
		ibfr.put(bfr);
		ibfr.flip();
		return ibfr;
	}
	
	public void destroy()
	{
		while(VaoStack.size()>0)
			GL30.glDeleteVertexArrays(VaoStack.pop());
		while(VboStack.size()>0)
			GL15.glDeleteBuffers(VboStack.pop());
		while(TexStack.size()>0)
			GL11.glDeleteTextures(TexStack.pop());
		
		
	}
	
	
}
