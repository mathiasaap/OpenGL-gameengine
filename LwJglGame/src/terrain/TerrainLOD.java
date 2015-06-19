package terrain;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import rendering.LoadMesh;
import simplexnoise.SimplexNoise;
import mesh.Mesh;

public class TerrainLOD {

	private Mesh mesh;
	private boolean readyToDraw=false;
	public int getVAOFromMesh()
	{
		if(readyToDraw)
			return mesh.getVAO();
		return -1;
	}
	
	private double[][] heightmap;
	private final int terrainGridX,terrainGridZ;
	private final double terrainDistConst;
	private final float heightMultiplicator;
	
	private float[] arrayVertices;
	private float[] arrayUV;
	private float[] arrayNormals;
	private int[] arrayIndices;
	private final int VERTICES;
	private final float SIZE;
	LoadMesh meshLdr;
	private final int LOD;
	private int LODFactor=1;
	
	private SimplexNoise snoise;
	
	public Mesh getMesh()
	{
		return mesh;
	}
	
	public TerrainLOD(int terrainGridX, int terrainGridZ, float heightMultiplicator,int VERTICES,float SIZE, double[][] heightmap, SimplexNoise snoise,LoadMesh meshLdr)
	{
		this.VERTICES=VERTICES;
		this.heightmap=heightmap;
		this.snoise=snoise;
		this.terrainGridX=terrainGridX;
		this.terrainGridZ=terrainGridZ;
		this.heightMultiplicator=heightMultiplicator;
		this.SIZE=SIZE;
		this.terrainDistConst=(double)((SIZE)/(VERTICES*64.0));
		this.meshLdr=meshLdr;
		this.LOD=0;
	}
	
	public TerrainLOD(int terrainGridX, int terrainGridZ, float heightMultiplicator,int VERTICES,float SIZE, int LOD, SimplexNoise snoise,LoadMesh meshLdr)
	{
		
		this.VERTICES=(int)(VERTICES/(Math.pow(2, LOD)));
		this.snoise=snoise;
		this.terrainGridX=terrainGridX;
		this.terrainGridZ=terrainGridZ;
		this.heightMultiplicator=heightMultiplicator;
		this.SIZE=SIZE;
		this.terrainDistConst=(double)((SIZE)/(this.VERTICES*64.0));
		this.meshLdr=meshLdr;
		this.LOD=LOD;
		this.LODFactor=(int) Math.pow(2, LOD);
		this.heightmap=Terrain.generateHeightmapStatic(this.VERTICES, terrainGridX, terrainGridZ, terrainDistConst, heightMultiplicator,LOD);
		
	}
	
	private Vector3f terrainNormal(int x, int y)
	{
		
		//If point needed for calculation is outside heightmap, calculate it with simplex noise
		float L = (float) ((float) x>0?heightmap[x-1][y]:   			snoise.getNoise((((x-1)+terrainGridZ*(VERTICES-1))*terrainDistConst), (y+terrainGridX*(VERTICES-1))*terrainDistConst)*heightMultiplicator);
		float R = (float) ((float) x<(VERTICES-1)?heightmap[x+1][y]:    snoise.getNoise((((x+1)+terrainGridZ*(VERTICES-1))*terrainDistConst), (y+terrainGridX*(VERTICES-1))*terrainDistConst)*heightMultiplicator);
		float D = (float) ((float) y>0?heightmap[x][y-1]:   			snoise.getNoise(((x+terrainGridZ*(VERTICES-1))*terrainDistConst), ((y-1)+terrainGridX*(VERTICES-1))*terrainDistConst)*heightMultiplicator);
		float U = (float) ((float) y<(VERTICES-1)?heightmap[x][y+1]:    snoise.getNoise(((x+terrainGridZ*(VERTICES-1))*terrainDistConst), ((y+1)+terrainGridX*(VERTICES-1))*terrainDistConst)*heightMultiplicator);
		
		return (Vector3f)(new Vector3f(L-R,2f,D-U)).normalise();
		
		
	}
	
	public void generateTerrain()
	{
		if(this.heightmap==null)
			heightmap=Terrain.generateHeightmapStatic(VERTICES, terrainGridX, terrainGridZ, terrainDistConst, heightMultiplicator,LOD);
		
		int TOTAL_VERTS = VERTICES*VERTICES;
		int TOTAL_POLYS = (VERTICES-1)*(VERTICES-1)*2;
		arrayVertices=new float[TOTAL_VERTS*3];
		arrayUV=new float[TOTAL_VERTS*2];
		arrayNormals=new float[TOTAL_VERTS*3];
		arrayIndices=new int[3*TOTAL_POLYS];
		
		
		//Generate vertex coords, normals and UV coords
		for(int i =0; i<VERTICES;++i)
		{
			for(int j = 0; j<VERTICES;++j)
			{
				//heightmap[i][j]
				
				arrayVertices[3*(j+i*VERTICES)]=(float)(j/((float)VERTICES-1))*SIZE;
				arrayVertices[3*(j+i*VERTICES)+1]= (float) heightmap[i][j];
				arrayVertices[3*(j+i*VERTICES)+2]=(float)(i/((float)VERTICES-1))*SIZE;
				
				
				Vector3f tNorm=terrainNormal(i,j);
				arrayNormals[3*(j+i*VERTICES)]=tNorm.x;
				arrayNormals[3*(j+i*VERTICES)+1]=tNorm.y;
				arrayNormals[3*(j+i*VERTICES)+2]=tNorm.z;
				
				arrayUV[2*(j+i*VERTICES)]=(float)(j/((float)VERTICES-1));
				arrayUV[2*(j+i*VERTICES)+1]=(float)(i/((float)VERTICES-1));

			}
			
		}
		
		
		
		
		//Generate indices
		int indexP=0;
		for(int i =0; i<VERTICES-1;i++)
		{
			for(int j = 0; j<VERTICES-1;j++)
			{
				int topLeft = j+i*VERTICES;
				
				arrayIndices[indexP++]= topLeft;
				arrayIndices[indexP++]= topLeft+VERTICES;
				arrayIndices[indexP++]= topLeft+1;
				
				arrayIndices[indexP++]= topLeft+1;
				arrayIndices[indexP++]= topLeft+VERTICES;
				arrayIndices[indexP++]= topLeft+VERTICES+1;
				
			}
			
		}
		if(LOD!=0)
			heightmap=null;
	}
	
	public void uploadMesh()
	{
		
		mesh=meshLdr.loadNewMesh(arrayVertices, arrayIndices, arrayUV, arrayNormals);
		arrayVertices=arrayUV=arrayNormals=null;
		arrayIndices=null;
	}
	public void deleteMesh()
	{
		
	}
	
	public void cleanup()
	{
		arrayVertices=arrayUV=arrayNormals=null;
		arrayIndices=null;
	}
	
}
