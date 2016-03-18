package mathiasap.cardboard.misc;

public class Vektor3f extends Vektor
{
    public float x,y,z;
    public Vektor3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;


    }
    public Vektor3f(){}
    protected float[] getArray()
    {
        float[] tmp= new float[3];
        tmp[0]=x;
        tmp[1]=y;
        tmp[2]=z;

        return tmp;
    }

    @Override
    public Vektor normalise() {
        float length = getArray().length;
        this.x/=length;
        this.y/=length;
        this.z/=length;
        return this;
    }
    public static Vektor3f sub(Vektor3f left,Vektor3f right)
    {
        Vektor3f tmp= new Vektor3f();
        tmp.x=left.x-right.x;
        tmp.y=left.y-right.y;
        tmp.z=left.z-right.z;
        return tmp;
    }

    public static Vektor3f add(Vektor3f left,Vektor3f right)
    {
        Vektor3f tmp= new Vektor3f();
        tmp.x=left.x+right.x;
        tmp.y=left.y+right.y;
        tmp.z=left.z+right.z;
        return tmp;
    }

}
