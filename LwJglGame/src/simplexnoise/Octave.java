package simplexnoise;

public class Octave {
	
	private static int[] p;
	private static int grad2[][]={{1,1},{-1,1},{1,-1},{-1,-1}};
	

	
	
	
	
	
	private int octNum;
	private static double freq;
	private static double amp;
	public Octave(int octNum, int totalOct, double persistence, int[] p)
	{
		this.p=p;
		this.octNum=octNum;
		freq=Math.pow(2, octNum);
		amp=Math.pow(persistence, totalOct-octNum);
	}

	private synchronized  static double dot(int[] grad, double x, double y)
	{
		
		return (grad[0]*x+grad[1]*y);
	}
	
	
	//Litt copypaste ja
	public synchronized static  double noise(double xin, double yin)
	{
		 xin/=freq;
		 yin=(yin/freq)*amp;
		 
		 
		 double n0, n1, n2;
		 final double F2 = 0.5*(Math.sqrt(3.0)-1.0);
		 double s = (xin+yin)*F2; 
		 int i = floor(xin+s);
		 int j = floor(yin+s);
		
		 final double G2 = (3.0-Math.sqrt(3.0))/6.0;
		 double t = (i+j)*G2;
		 double X0 = i-t; 
		 double Y0 = j-t;
		 double x0 = xin-X0; 
		 double y0 = yin-Y0;
		 
		 int i1, j1; 
		 if(x0>y0) 
		 {
			 i1=1; j1=0;
		 } 
		 else 
		 {
			 i1=0; j1=1;	 
		 }
		 
		 
		 double x1 = x0 - i1 + G2; 
		 double y1 = y0 - j1 + G2;
		 double x2 = x0 - 1.0 + 2.0 * G2; 
		 double y2 = y0 - 1.0 + 2.0 * G2;
		 
		 int ii = i & 255;
		 int jj = j & 255;
		 int gi0 = p[ii+p[jj]] % 4;
		 int gi1 = p[ii+i1+p[jj+j1]] % 4;
		 int gi2 = p[ii+1+p[jj+1]] % 4;
		 
		 
		 double t0 = 0.5 - x0*x0-y0*y0;
		 if(t0<0) n0 = 0.0;
		 else {
		 t0 *= t0;
		 n0 = t0 * t0 * dot(grad2[gi0], x0, y0); // (x,y) of grad3 used for 2D gradient
		 }
		 double t1 = 0.5 - x1*x1-y1*y1;
		 if(t1<0) n1 = 0.0;
		 else {
		 t1 *= t1;
		 n1 = t1 * t1 * dot(grad2[gi1], x1, y1);
		 }
		 
		 double t2 = 0.5 - x2*x2-y2*y2;
		 if(t2<0) n2 = 0.0;
		 else {
		 t2 *= t2;
		 n2 = t2 * t2 * dot(grad2[gi2], x2, y2);
		 }
		 
		 
		 return 70.0 * (n0 + n1 + n2);
		
	}
	
	

	private synchronized static int floor(double x)
	{
		return x>0?(int)x:((int)x-1);
	}
	
	
}
