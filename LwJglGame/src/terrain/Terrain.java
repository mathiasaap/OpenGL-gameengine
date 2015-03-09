package terrain;

import org.lwjgl.util.vector.Vector3f;

import rendering.LoadMesh;
import textures.MeshTexture;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.TexMesh;

public class Terrain {

	private static final float SIZE = 400;
	private static final int VERTICES_W = 256;
	private static final int VERTICES_L = 256;
	private Mesh mesh;
	private MeshTexture tex;
	private MeshInstance terrainIns;
	private float xPos,zPos;
	
	
	public Terrain(MeshTexture tex, int x, int z, LoadMesh meshLdr)
	{
		this.tex=tex;
		this.xPos= x*SIZE;
		this.zPos= z*SIZE;
		this.mesh=generateTerrain(meshLdr);
		terrainIns = new MeshInstance(new TexMesh(mesh,tex), new Vector3f(-100,0,-100),0,0,0,1f);
		
	}
	
	
	private Mesh generateTerrain(LoadMesh loadmesh)
	{
		int TOTAL_VERTS = VERTICES_W*VERTICES_L;
		int TOTAL_POLYS = (VERTICES_W-1)*(VERTICES_L-1)*2;
		float[] arrayVertices=new float[TOTAL_VERTS*3];
		float[] arrayUV=new float[TOTAL_VERTS*2];
		float[] arrayNormals=new float[TOTAL_VERTS*3];
		int[] arrayIndices=new int[3*TOTAL_POLYS];
		
		
		//Generate vertex coords, normals and UV coords
		for(int i =0; i<VERTICES_L;++i)
		{
			for(int j = 0; j<VERTICES_W;++j)
			{
				arrayVertices[3*(j+i*VERTICES_W)]=(float)(j/(VERTICES_W-1))*SIZE;
				arrayVertices[3*(j+i*VERTICES_W)+1]= -2;
				arrayVertices[3*(j+i*VERTICES_W)+2]=(float)(i/(VERTICES_L-1))*SIZE;;
				
				arrayNormals[3*(j+i*VERTICES_W)]=0;
				arrayNormals[3*(j+i*VERTICES_W)+1]=1;
				arrayNormals[3*(j+i*VERTICES_W)+2]=0;
				
				arrayUV[2*(j+i*VERTICES_W)]=(float)(j/(VERTICES_W-1));
				arrayUV[2*(j+i*VERTICES_W)+1]=(float)(i/(VERTICES_L-1));

			}
			
		}
		
		
		
		//Generate indices
		int indexP=0;
		for(int i =0; i<VERTICES_L-1;i++)
		{
			for(int j = 0; j<VERTICES_W-1;j++)
			{
				int topLeft = j+i*VERTICES_W;
				
				arrayIndices[indexP++]= topLeft;
				arrayIndices[indexP++]= topLeft+VERTICES_W;
				arrayIndices[indexP++]= topLeft+1;
				
				arrayIndices[indexP++]= topLeft+1;
				arrayIndices[indexP++]= topLeft+VERTICES_W;
				arrayIndices[indexP++]= topLeft+VERTICES_W+1;
				
			}
			
		}
		
		
		
		
		return loadmesh.loadNewMesh(arrayVertices, arrayIndices, arrayUV, arrayNormals);
	}
	
	public MeshInstance getTerrain()
	{
		
		return terrainIns;
	}
	

}
