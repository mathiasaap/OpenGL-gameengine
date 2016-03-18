package mathiasap.cardboard.rendering;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;


import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


import mathiasap.cardboard.MainActivity;
import mathiasap.cardboard.mesh.Mesh;



public class LoadMesh {

	private static List<Integer> VaoList= new ArrayList<>();
	private static List<Integer> VboList= new ArrayList<>();
	private static List<Integer> TexList= new ArrayList<>();
	public static Context context;
	
	
	
	
	private static int createVAO()
	{
		int[] VAO = new int[1];
		GLES30.glGenVertexArrays(1,VAO,0);
		GLES30.glBindVertexArray(VAO[0]);
		VaoList.add(VAO[0]);
		return VAO[0];
	}
	
	private static void unbind()
	{
	
	GLES30.glBindVertexArray(0);
	}
	
	public static Mesh loadNewMesh(float[] pos)
	{
		int VAO = createVAO();
		List<Integer> meshVBOs= new ArrayList<>();

		int[] VBO = new int[1];
		GLES30.glGenBuffers(1, VBO, 0);

		meshVBOs.add(storeAttrib(0,pos,2,VBO[0]));
		
		unbind();
		return new Mesh(pos.length/2,VAO,meshVBOs);
	}
	
	public static Mesh loadNewMesh(float[] pos, int[] indices, float[] texUV)
	{
		int VAO = createVAO();

		List<Integer> meshVBOs= new ArrayList<>();



		int[] VBO = new int[3];
		GLES30.glGenBuffers(3, VBO, 0);

		loadIndicesBuffer(indices,VBO[0]);
		meshVBOs.add(storeAttrib(0, pos, 3, VBO[1]));
		meshVBOs.add(storeAttrib(1,texUV,2,VBO[2]));

		unbind();
		return new Mesh(indices.length,VAO,meshVBOs);
	}
	
	public static Mesh loadNewMesh(float[] pos, int v)
	{
		int VAO = createVAO();
		List<Integer> meshVBOs= new ArrayList<>();

		int[] VBO = new int[1];
		GLES30.glGenBuffers(1, VBO, 0);

		meshVBOs.add(storeAttrib(0,pos,v,VBO[0]));
		
		unbind();
		return new Mesh(pos.length/v,VAO,meshVBOs);
	}
	
	public static Mesh loadNewMesh(float[] pos, int[] indices, float[] texUV, float[] normals)
	{
		int VAO = createVAO();

		List<Integer> meshVBOs= new ArrayList<>();

		

		int[] VBO = new int[4];
		GLES30.glGenBuffers(4,VBO,0);

		loadIndicesBuffer(indices, VBO[0]);
		meshVBOs.add(storeAttrib(0, pos, 3, VBO[1]));
		meshVBOs.add(storeAttrib(1, texUV, 2, VBO[2]));
		meshVBOs.add(storeAttrib(2, normals, 3, VBO[3]));
		unbind();
		return new Mesh(indices.length,VAO,meshVBOs);
	}
	
	
	
	private static int storeAttrib(int attrib, float[] bfr,int cSze,int VBO)
	{
		
		VboList.add(VBO);

		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO);
		FloatBuffer fbfr = floatToFloatBuffer(bfr);
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,bfr.length*4,fbfr,GLES30.GL_STATIC_DRAW);
		GLES30.glVertexAttribPointer(attrib,cSze, GLES30.GL_FLOAT,false,0,0);
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
		return VBO;
		
	}
	
	public static int loadTexture(String filename)
	{


		final int[] texid = new int[1];
		GLES30.glGenTextures(1,texid,0);
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inScaled= false;

		//final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),10);

			if (texid[0]==0)
				return 0;



		try {
			final Bitmap bitmap = BitmapFactory.decodeStream(MainActivity.assets.open(filename), null, options);

			GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texid[0]);

			/*GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
			GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_LINEAR);*/
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

			/*GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,
					GLES30.GL_NEAREST);
			GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER,
					GLES30.GL_NEAREST);*/

			GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_REPEAT);
			GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_REPEAT);

			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();

			TexList.add(texid[0]);
			return texid[0];

		} catch (IOException e) {
		e.printStackTrace();
	}
		return -1;
	}
	
	private static void loadIndicesBuffer(int[] indices,int VBO)
	{
		VboList.add(VBO);
		GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, VBO);
		IntBuffer ibfr= intToIntBuffer(indices);
		GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indices.length * 4, ibfr, GLES30.GL_STATIC_DRAW);
	}
	
	public static FloatBuffer floatToFloatBuffer(float[] bfr)
	{

		ByteBuffer vbb = ByteBuffer.allocateDirect(bfr.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = vbb.asFloatBuffer();
		fb.put(bfr);
		fb.position(0);

		return fb;

	}
	private static IntBuffer intToIntBuffer(int[] bfr)
	{
		ByteBuffer vbb = ByteBuffer.allocateDirect(bfr.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		IntBuffer ib = vbb.asIntBuffer();
		ib.put(bfr);
		ib.position(0);

		return ib;
	}
	
	public static void destroy()
	{
		int[] VaoArr = new int[VaoList.size()];
		for(int i=0;i<VaoList.size();i++)
		{
			VaoArr[i]=VaoList.get(i);
		}


		int[] VboArr = new int[VboList.size()];
		for(int i=0;i<VboList.size();i++)
		{
			VboArr[i]=VboList.get(i);
		}

		int[] TexArr = new int[TexList.size()];
		for(int i=0;i<TexList.size();i++)
		{
			TexArr[i]=TexList.get(i);
		}



		GLES30.glDeleteVertexArrays(VaoList.size(),VaoArr,0);
		VaoList.clear();
		GLES30.glDeleteBuffers(VboList.size(), VboArr, 0);
		VboList.clear();
		GLES30.glDeleteTextures(TexList.size(), TexArr, 0);
		TexList.clear();
		
		
	}
	
	public static void deleteVBOs(List<Integer> VBOS)
	{

		int[] VboArr = new int[VBOS.size()];
		for(int i=0;i<VBOS.size();i++)
		{
			VboArr[i]=VBOS.get(i);
			VboList.remove(VBOS.get(i));
		}

		GLES30.glDeleteBuffers(VBOS.size(),VboArr,0);
		VBOS.clear();
		
	}
	
	public static void deleteVBO(int VBO)
	{
			deleteVBOs(new ArrayList<Integer>(VBO));
		
	}
	public static void deleteVAO(int VAO)
	{
			int[] VAOS = new int[1];
			VAOS[0] = VAO;

			if(VaoList.contains(VAO))
			{
				VaoList.remove((Object)VAO);
				GLES30.glDeleteVertexArrays(1,VAOS,0);
				System.out.println("Deleted VAO "+VAO);
			}
		
		
	}
	
	public static void deleteMesh(Mesh mesh)
	{
		deleteVBOs(mesh.getVBOs());
		deleteVAO(mesh.getVAO());
	}
	
	
}
