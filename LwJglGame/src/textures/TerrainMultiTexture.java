package textures;



public class TerrainMultiTexture {

	private TerrainTexture grass,rock,snow;
	
	public TerrainMultiTexture(TerrainTexture grass, TerrainTexture rock, TerrainTexture snow)
	{
		this.grass=grass;
		this.rock=rock;
		this.snow=snow;
		
	}

	public TerrainTexture getGrass() {
		return grass;
	}

	public TerrainTexture getRock() {
		return rock;
	}

	public TerrainTexture getSnow() {
		return snow;
	}

	
	
}
