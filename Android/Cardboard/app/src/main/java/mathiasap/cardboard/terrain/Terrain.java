package mathiasap.cardboard.terrain;


import mathiasap.cardboard.mesh.Mesh;
import mathiasap.cardboard.misc.Vektor3f;
import mathiasap.cardboard.simplexnoise.SimplexNoise;
import mathiasap.cardboard.textures.TerrainMultiTexture;

public class Terrain{

	//private static final float SIZE = 2048;
	public static final int SIZE = 1024;
	//private static final int VERTICES = 1024;
	//private static final int VERTICES = 32;
	public static final int VERTICES = 16;
	
	//private float heightMultiplicator=5*12;
	public static float heightMultiplicator=12;
	public static final double terrainDistConst=(double)((SIZE)/(VERTICES*64.0));
	private TerrainLOD[] terrainLevelOfDetail= new TerrainLOD[3];
	private short currentLOD=0; //0 is highest res, 2 is lowest
	
	private Mesh mesh;
	private Vektor3f position;
	//private float rotX, rotY, rotZ,scale;
	int terrainGridX,terrainGridZ;


	private TerrainMultiTexture multiTex;
	
	public static SimplexNoise snoise= new SimplexNoise(0.7,6);
	private double heightmap[][]=new double[VERTICES][VERTICES];;
	//private float heightMultiplicator=8;
	
	
	public Terrain(TerrainMultiTexture tex, int x, int z)
	{

		this.multiTex=tex;
		this.position=new Vektor3f(x*SIZE,0,z*SIZE);
		//this.rotX=this.rotY=this.rotZ=this.scale=0;
		terrainGridX= x;
		terrainGridZ= z;
		
		terrainLevelOfDetail[0] = new TerrainLOD(terrainGridX, terrainGridZ, heightMultiplicator, VERTICES, SIZE, heightmap, snoise);
		
		for(int i= 1; i < terrainLevelOfDetail.length;i++)
		{
			terrainLevelOfDetail[i] = new TerrainLOD(terrainGridX, terrainGridZ, heightMultiplicator, VERTICES, SIZE,i, snoise);
			
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

		double utvid=1.0;
	//	heightmap=new double[VERTICES][VERTICES];
		for(int i=0;i <VERTICES; i++)
		{
			for(int j=0;j<VERTICES;j++)
			{
				heightmap[i][j]=snoise.getNoise((((i+terrainGridZ*(VERTICES-1))*terrainDistConst)/utvid), ((j+terrainGridX*(VERTICES-1))*terrainDistConst)/utvid)*heightMultiplicator;
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

	
	public void updateCurrentLOD(Vektor3f position)
	{
		/*float distance= Vector3f.sub(position, this.position, null).length();
		if(distance<3000)
			currentLOD=0;
		else if(distance<4000)
			currentLOD=1;
		else
			currentLOD=2;*/
		
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
	
	public Vektor3f getPosition() {
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
	
	public boolean isReadyToDraw()
	{
		return terrainLevelOfDetail[0].isReadyToDraw();
	}
	
	public void cleanup()
	{
		for(int i = 0; i<3;i++){}
		//terrainLevelOfDetail[i].cleanup();
		heightmap=null;
		terrainLevelOfDetail[0].cleanup();
	}


}
