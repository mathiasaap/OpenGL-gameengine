package simplexnoise;

import java.util.ArrayList;
import java.util.List;

public class SimplexNoise {
	
	private List<Octave> octaves = new ArrayList<>(); 

	/*private static final short[] permutation = {
		238,26,13,94,20,66,164,92,169,7,27,82,206,121,43,142,63,54,200,90,170,249,76,203,52,172,160,223,25,65,254,5,60,
		38,168,96,141,61,127,218,67,226,138,228,255,53,111,86,156,193,37,190,231,157,171,112,95,16,181,108,124,145,147,102,69,
		219,91,125,248,80,221,173,250,222,167,189,143,17,128,199,41,42,107,88,215,216,46,79,252,58,158,21,11,98,237,132,225,
		233,251,245,105,106,118,214,163,153,8,34,114,224,119,220,229,146,35,240,182,81,175,133,72,83,28,14,123,104,6,62,126,
		212,99,227,191,244,64,152,209,239,177,100,207,174,73,32,205,197,23,208,39,186,130,161,129,33,194,116,187,162,31,48,196,
		236,10,217,210,77,180,40,192,144,115,166,47,15,159,36,230,188,139,30,211,184,120,135,78,122,195,71,165,247,50,24,246,
		1,198,18,12,253,57,113,85,149,0,150,109,89,75,44,19,241,29,204,131,232,22,93,103,242,59,9,70,155,202,243,148,
		4,234,176,213,183,3,49,101,140,201,179,151,51,185,55,68,87,84,2,178,154,110,136,137,235,97,134,117,56,45,74
		};*/
	private final int[] permutation = {
		97,49,88,44,91,5,188,27,66,30,163,105,202,85,15,55,237,89,21,51,4,93,138,223,210,247,127,106,39,102,171,195,34,
		50,204,162,137,131,228,197,122,176,157,186,153,123,212,99,72,96,220,149,52,125,9,147,59,178,219,77,68,243,140,128,182,
		0,35,67,254,48,217,124,11,170,145,154,183,6,156,187,118,144,227,38,116,193,139,87,200,177,222,143,164,206,161,92,126,
		37,132,58,235,29,78,203,196,136,215,150,112,205,238,213,239,181,83,246,194,47,242,184,211,142,19,155,252,41,57,95,199,
		230,94,84,165,201,65,180,229,36,76,108,73,46,32,24,240,173,110,120,71,101,148,221,231,75,111,152,109,175,250,119,80,
		61,151,103,167,214,129,134,28,207,26,141,63,191,40,249,218,82,33,113,198,158,189,8,3,135,225,159,179,20,74,172,236,
		255,17,60,7,117,23,18,232,62,115,42,43,100,233,12,14,121,253,133,64,168,216,248,208,69,185,1,226,190,2,224,31,
		146,81,86,45,234,245,130,70,22,16,79,244,13,114,160,169,10,166,53,98,107,90,56,54,25,192,104,209,174,241,251
		};
	
	private int[] p;
	
	private int nOctaves;
	public SimplexNoise(double persistence, int nOctaves)
	{
		p= new int[512];
		this.nOctaves=nOctaves;
		for(int i=0;i<512;i++)
			p[i]=permutation[i&0xff];
		
		for(int i =0; i< nOctaves;i++)
		{
			octaves.add(new Octave(i,nOctaves,persistence,p));
		}
		
		
	}
	
	public synchronized double getNoise(double x, double y)
	{
		double res=getNoiseOrd(x,y);
		
		return (res*res*res);
	}
	
	public double getNoiseOrd(double x, double y)
	{
		double res=0.0;
		
		for(Octave octave: octaves)
		{
			res+=octave.noise(x,y);
		}
		
		return res;
	}
	
	
}
