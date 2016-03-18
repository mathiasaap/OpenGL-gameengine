package mathiasap.cardboard.mesh;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import mathiasap.cardboard.MainActivity;
import mathiasap.cardboard.misc.Vektor2f;
import mathiasap.cardboard.misc.Vektor3f;
import mathiasap.cardboard.rendering.LoadMesh;

public class OBJLoader {
	

	public static Mesh loadObj(String filename)
	{

		ArrayList<String> objData= new ArrayList<>();		
			try {
				InputStream instream = MainActivity.assets.open(filename);
				BufferedReader inStream = new BufferedReader(new InputStreamReader(instream));
				String line="";
				while((line=inStream.readLine())!=null)
				{
					objData.add(line);
				}
				inStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			ArrayList<Vektor3f> vertices = new ArrayList<>();
			ArrayList<Vektor2f> uv = new ArrayList<>();
			ArrayList<Vektor3f> normals = new ArrayList<>();
			ArrayList<Integer> indices = new ArrayList<>();
			
			float[] fArrayVertices;
			float[] fArrayUV;
			float[] fArrayNormals;
			int[] iArrayIndices;
			
			String[] lineSplit;
			int linePointer=0;
			try{
				boolean repeat=true;
				for(;linePointer<objData.size()&&repeat;++linePointer)
			{
				lineSplit = objData.get(linePointer).split(" ");
				if(lineSplit.length>4 )
					continue;
				
				
			
				switch (lineSplit[0])
				{
				case "v":
					Vektor3f tmpVert= new Vektor3f();
					tmpVert.x = Float.parseFloat(lineSplit[1]);
					tmpVert.y = Float.parseFloat(lineSplit[2]);
					tmpVert.z = Float.parseFloat(lineSplit[3]);
					vertices.add(tmpVert);
					break;
					
				case "vn":
					Vektor3f tmpNorm= new Vektor3f();
					tmpNorm.x = Float.parseFloat(lineSplit[1]);
					tmpNorm.y = Float.parseFloat(lineSplit[2]);
					tmpNorm.z = Float.parseFloat(lineSplit[3]);
					normals.add(tmpNorm);
					break;
					
				case "vt":
					Vektor2f tmpUv= new Vektor2f();
						tmpUv.x = Float.parseFloat(lineSplit[1]);
						tmpUv.y = Float.parseFloat(lineSplit[2]);
						uv.add(tmpUv);
					break;
					
				case "f":
					repeat=false;
					break;
				
				}}
			}catch(NumberFormatException e){
				
				System.err.println("Something went frong!");
			}
			fArrayVertices= new float[vertices.size()*3];
			fArrayNormals= new float[vertices.size()*3];
			fArrayUV= new float[vertices.size()*2];
			
			linePointer--;
			
			
			for(;linePointer<objData.size();++linePointer)
			{
				lineSplit = objData.get(linePointer).split(" ");
				if(!lineSplit[0].equals("f"))
					continue;

				
				String[] v1 = lineSplit[1].split("/");
				String[] v2 = lineSplit[2].split("/");
				String[] v3 = lineSplit[3].split("/");
				parseVertex(v1,fArrayNormals,fArrayUV, indices, normals,uv);
				parseVertex(v2,fArrayNormals,fArrayUV, indices, normals,uv);
				parseVertex(v3,fArrayNormals,fArrayUV, indices, normals,uv);
				
				
			}
			iArrayIndices= new int[indices.size()];
			for (int i =0;i<indices.size();++i)
				iArrayIndices[i]=indices.get(i);
			
			int vp=0;
			for (Vektor3f verts : vertices)
			{
				fArrayVertices[vp++]=verts.x;
				fArrayVertices[vp++]=verts.y;
				fArrayVertices[vp++]=verts.z;
				
			}

			
		/*for(Vector3f cat:vertices)
			System.out.println(cat);*/
		System.out.println(filename+": "+indices.size()/3 + " polygons!");
			
		return LoadMesh.loadNewMesh(fArrayVertices, iArrayIndices, fArrayUV,fArrayNormals);

		
	}
	
	private static void parseVertex(String[] verts, float[] fNormals, float[] fUv, ArrayList<Integer> indices, ArrayList<Vektor3f> normals, ArrayList<Vektor2f> uv)
	{
		
		int cVert= Integer.parseInt(verts[0])-1; // Index til vertex index, for nullindeksert array
		indices.add(cVert);
		
		try{
		Vektor2f vertexUV = uv.get(Integer.parseInt(verts[1])-1);
		fUv[cVert*2]= vertexUV.x;
		fUv[cVert*2+1]= 1-vertexUV.y; // 1-vertexUV for � korrigere for forskjellig koordinatsystem
		} catch(NumberFormatException e)
		{
			fUv[cVert*2]= fUv[cVert*2+1]=0;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			fUv[cVert*2]= fUv[cVert*2+1]=0;
		}catch(IndexOutOfBoundsException e)
		{
			//fUv[cVert*2]= fUv[cVert*2+1]=0;
		}
		try{
		Vektor3f vertexNormal = normals.get(Integer.parseInt(verts[2])-1);
		fNormals[cVert*3]= vertexNormal.x;
		fNormals[cVert*3+1]= vertexNormal.y;
		fNormals[cVert*3+2]= vertexNormal.z;
		}catch(ArrayIndexOutOfBoundsException e)
		{
			//fUv[cVert*2]= fUv[cVert*2+1]=0;
		}catch(IndexOutOfBoundsException e)
		{
			//fUv[cVert*2]= fUv[cVert*2+1]=0;
		}
		
	}
	
	
	
}
