package terrain;

import org.lwjgl.util.vector.Vector3f;

import rendering.LoadMesh;
import simplexnoise.SimplexNoise;
import textures.MeshTexture;
import textures.TerrainMultiTexture;
import mesh.Mesh;
import mesh.MeshInstance;
import mesh.TexMesh;

public class Terrain{

	//private static final float SIZE = 2048;
	public static final float SIZE = 1024;
	//private static final int VERTICES = 1024;
	private static final int VERTICES = 64;
	
	private float heightMultiplicator=12;
	private final double terrainDistConst=(double)((SIZE*64.0)/(VERTICES*4096.0));
	
	
	
	private Mesh mesh;
	private Vector3f position;
	private float rotX, rotY, rotZ,scale;
	int terrainGridX,terrainGridZ;


	private TerrainMultiTexture multiTex;
	
	private SimplexNoise snoise= new SimplexNoise(0.7,6);
	private double heightmap[][];
	//private float heightMultiplicator=8;
	
	
	public Terrain(TerrainMultiTexture tex, int x, int z, LoadMesh meshLdr)
	{
		this.multiTex=tex;
		this.position=new Vector3f(x*SIZE,0,z*SIZE);
		this.rotX=this.rotY=this.rotZ=this.scale=0;
		terrainGridX= x;
		terrainGridZ= z;
		
		heightmap=new double[VERTICES][VERTICES];
		long generationTime= System.currentTimeMillis();
		generateHeightmap();
		System.out.println("Time to generate heightmap: "+(System.currentTimeMillis()-generationTime)+"ms");
		generationTime=System.currentTimeMillis();
		this.mesh=generateTerrain(meshLdr);
		System.out.println("Time to generate terrain mesh from heightmap: "+(System.currentTimeMillis()-generationTime)+"ms");
		
	}
	private void generateHeightmap()
	{
		for(int i=0;i <VERTICES; i++)
		{
			for(int j=0;j<VERTICES;j++)
			{
				heightmap[i][j]=snoise.getNoise(((i+terrainGridZ*(VERTICES-1))*terrainDistConst), (j+terrainGridX*(VERTICES-1))*terrainDistConst)*heightMultiplicator;
			}
			
		}
		
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
	
	

	
	
	private Mesh generateTerrain(LoadMesh loadmesh)
	{
		int TOTAL_VERTS = VERTICES*VERTICES;
		int TOTAL_POLYS = (VERTICES-1)*(VERTICES-1)*2;
		float[] arrayVertices=new float[TOTAL_VERTS*3];
		float[] arrayUV=new float[TOTAL_VERTS*2];
		float[] arrayNormals=new float[TOTAL_VERTS*3];
		int[] arrayIndices=new int[3*TOTAL_POLYS];
		
		
		//Generate vertex coords, normals and UV coords
		for(int i =0; i<VERTICES;++i)
		{
			for(int j = 0; j<VERTICES;++j)
			{
				arrayVertices[3*(j+i*VERTICES)]=(float)(j/((float)VERTICES-1))*SIZE;
				arrayVertices[3*(j+i*VERTICES)+1]= (float) heightmap[i][j];
				arrayVertices[3*(j+i*VERTICES)+2]=(float)(i/((float)VERTICES-1))*SIZE;;
				
				
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

		
		return loadmesh.loadNewMesh(arrayVertices, arrayIndices, arrayUV, arrayNormals);
	}

	public float getSIZE()
	{
		return SIZE;
	}
	public int getVertices()
	{
		return VERTICES;
	}

	public double[][] getHeightmap()
	{
		return heightmap;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getRotX() {
		return rotX;
	}
	public float getScale() {
		return scale;
	}
	public float getRotY() {
		return rotY;
	}
	public float getRotZ() {
		return rotZ;
	}
	
	public float getHeightMultiplicator() {
		return heightMultiplicator;
	}
	public void setHeightMultiplicator(float heightMultiplicator) {
		this.heightMultiplicator = heightMultiplicator;
	}
	public Mesh getMesh() {
		return mesh;
	}
	public TerrainMultiTexture getMultiTex() {
		return multiTex;
	}


}
