package mathiasap.cardboard.misc;

public class Maths {
	public static int floor(double x)
	{
		return x>0?(int)x:((int)x-1);
	}
	
	public static int getIntFromTwoShorts(short x, short y)
	{
		int res=x<<16;
		res|=y;
		return res;
	}
	public static long getLongFromTwoInts(int x, int y)
	{
		long res=x<<32;
			res&=y;
		
		return res;
	}

}
