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
	public static final int SIZE = 1024;
	//private static final int VERTICES = 1024;
	//private static final int VERTICES = 32;
	private static final int VERTICES = 16;
	
	private float heightMultiplicator=12;
	private final double terrainDistConst=(double)((SIZE)/(VERTICES*64.0));
	private TerrainLOD[] terrainLevelOfDetail= new TerrainLOD[3];
	private short currentLOD=0; //0 is highest res, 2 is lowest
	
	LoadMesh meshLdr;
	
	private Mesh mesh;
	private Vector3f position;
	//private float rotX, rotY, rotZ,scale;
	int terrainGridX,terrainGridZ;


	private TerrainMultiTexture multiTex;
	
	private static SimplexNoise snoise= new SimplexNoise(0.7,6);
	private double heightmap[][]=new double[VERTICES][VERTICES];;
	//private float heightMultiplicator=8;
	
	
	public Terrain(TerrainMultiTexture tex, int x, int z, LoadMesh meshLdr)
	{

		this.multiTex=tex;
		this.position=new Vector3f(x*SIZE,0,z*SIZE);
		//this.rotX=this.rotY=this.rotZ=this.scale=0;
		terrainGridX= x;
		terrainGridZ= z;
		this.meshLdr=meshLdr;
		
		terrainLevelOfDetail[0] = new TerrainLOD(terrainGridX, terrainGridZ, heightMultiplicator, VERTICES, SIZE, heightmap, snoise,meshLdr);
		
		for(int i= 1; i < terrainLevelOfDetail.length;i++)
		{
			terrainLevelOfDetail[i] = new TerrainLOD(terrainGridX, terrainGridZ, heightMultiplicator, VERTICES, SIZE,i, snoise,meshLdr);
			
		}
		
		//long generationTime= System.currentTimeMillis();
		//generateHeightmap();
		//System.out.println("Time to generate heightmap: "+(System.currentTimeMillis()-generationTime)+"ms");
		//generationTime=System.currentTimeMillis();
		//this.mesh=generateTerrain(meshLdr);
	//	System.out.println("Time to generate terrain mesh from heightmap: "+(System.currentTimeMillis()-generationTime)+"ms");
		
	}
	public synchronized void generateHeightmap()
	{

	//	heightmap=new double[VERTICES][VERTICES];
		for(int i=0;i <VERTICES; i++)
		{
			for(int j=0;j<VERTICES;j++)
			{
				heightmap[i][j]=snoise.getNoise(((i+terrainGridZ*(VERTICES-1))*terrainDistConst), (j+terrainGridX*(VERTICES-1))*terrainDistConst)*heightMultiplicator;
			}
			
		}
	}
	
	public static double[][] generateHeightmapStatic(int VERTICES, int terrainGridX, int terrainGridZ, double terrainDistConst, float heightMultiplicator,int LOD)
	{

		double factor = 1024.0/(32.0*64.0);
		double[][] heightmap = new double[VERTICES][VERTICES];

		for(int i=0;i <VERTICES; i++)
		{
			for(int j=0;j<VERTICES;j++)
			{
				heightmap[i][j]=snoise.getNoise(((i*terrainDistConst+terrainGridZ*(VERTICES*terrainDistConst-1*factor))), (j*terrainDistConst+terrainGridX*(VERTICES*terrainDistConst-1*factor)))*heightMultiplicator;
			}
			
		}
		return heightmap;
	}

	
	public void updateCurrentLOD(Vector3f position)
	{
		float distance= Vector3f.sub(position, this.position, null).length();
		if(distance<3000)
			currentLOD=0;
		else if(distance<4000)
			currentLOD=1;
		else
			currentLOD=2;
		
	}

	
	
	public synchronized void generateTerrain()
	{
		
		terrainLevelOfDetail[0].generateTerrain();
		//terrainLevelOfDetail[1].generateTerrain();
		//terrainLevelOfDetail[2].generateTerrain();
		

	}

	public void uploadMesh()
	{
		
//		mesh=meshLdr.loadNewMesh(arrayVertices, arrayIndices, arrayUV, arrayNormals);
//		arrayVertices=arrayUV=arrayNormals=null;
//		arrayIndices=null;
		terrainLevelOfDetail[0].uploadMesh();
		//terrainLevelOfDetail[1].uploadMesh();
		//terrainLevelOfDetail[2].uploadMesh();
		
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
//	public float getRotX() {
//		return rotX;
//	}
//	public float getScale() {
//		return scale;
//	}
//	public float getRotY() {
//		return rotY;
//	}
//	public float getRotZ() {
//		return rotZ;
//	}
	
	public float getHeightMultiplicator() {
		return heightMultiplicator;
	}
	public void setHeightMultiplicator(float heightMultiplicator) {
		this.heightMultiplicator = heightMultiplicator;
	}
	public Mesh getMesh() {
		//return mesh;
		//return terrainLevelOfDetail[currentLOD].getMesh();
		return terrainLevelOfDetail[0].getMesh();
	}
	public TerrainMultiTexture getMultiTex() {
		return multiTex;
	}
	
	public void cleanup()
	{
		for(int i = 0; i<3;i++){}
		//terrainLevelOfDetail[i].cleanup();
		heightmap=null;
	}


}
