package mathiasap.cardboard.misc;

public class Vektor4f extends Vektor
{
    public float x,y,z,w;
    public Vektor4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;


    }
    public Vektor4f(){}
    protected float[] getArray()
    {
        float[] tmp= new float[4];
        tmp[0]=x;
        tmp[1]=y;
        tmp[2]=z;
        tmp[3]=w;
        return tmp;
    }

    @Override
    public Vektor normalise() {
        float length = getArray().length;
        this.x/=length;
        this.y/=length;
        this.z/=length;
        this.w/=length;
        return this;
    }


    public static Vektor4f sub(Vektor4f left,Vektor4f right)
    {
        Vektor4f tmp= new Vektor4f();
        tmp.x=left.x-right.x;
        tmp.y=left.y-right.y;
        tmp.z=left.z-right.z;
        tmp.w=left.w-right.w;
        return tmp;
    }

    public static Vektor4f add(Vektor4f left,Vektor4f right)
    {
        Vektor4f tmp= new Vektor4f();
        tmp.x=left.x+right.x;
        tmp.y=left.y+right.y;
        tmp.z=left.z+right.z;
        tmp.w=left.w+right.w;
        return tmp;
    }
}
