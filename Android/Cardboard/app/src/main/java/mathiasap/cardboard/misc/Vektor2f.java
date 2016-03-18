package mathiasap.cardboard.misc;

public class Vektor2f extends Vektor
{
    public float x,y;
    public Vektor2f(float x, float y) {
        this.x = x;
        this.y = y;


    }
    public Vektor2f(){}
    protected float[] getArray()
    {
        float[] tmp= new float[2];
        tmp[0]=x;
        tmp[1]=y;

        return tmp;
    }

    @Override
    public Vektor normalise() {
        float length = getArray().length;
        this.x/=length;
        this.y/=length;
        return this;
    }

    public static Vektor2f sub(Vektor2f left,Vektor2f right)
    {
        Vektor2f tmp= new Vektor2f();
        tmp.x=left.x-right.x;
        tmp.y=left.y-right.y;
        return tmp;
    }

    public static Vektor2f add(Vektor2f left,Vektor2f right)
    {
        Vektor2f tmp= new Vektor2f();
        tmp.x=left.x+right.x;
        tmp.y=left.y+right.y;
        return tmp;
    }
}
