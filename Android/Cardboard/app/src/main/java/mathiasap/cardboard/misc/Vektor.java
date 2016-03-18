package mathiasap.cardboard.misc;

public abstract class Vektor
{
    protected float[] values;

    public float length()
    {
        return getArray().length;
    }

    protected abstract float[] getArray();

    public abstract Vektor normalise();

}
