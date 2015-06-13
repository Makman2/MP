package tuhh.nme.mp.data;


/**
 * A two dimensional data point.
 *
 * @param <XType> The type for the x-value.
 * @param <YType> The type for the y-value.
 */
public class DataPoint<XType, YType>
{
    /**
     * Instantiates a new DataPoint with null-initialized reference types.
     */
    public DataPoint()
    {
        this(null, null);
    }

    /**
     * Instantiates a new DataPoint.
     *
     * @param x The x-value to initialize with.
     * @param y The y-value to initialize with.
     */
    public DataPoint(XType x, YType y)
    {
        X = x;
        Y = y;
    }

    /**
     * Copy constructor.
     *
     * @param copy The instance to copy.
     */
    public DataPoint(DataPoint<XType, YType> copy)
    {
        X = copy.X;
        Y = copy.Y;
    }

    /**
     * Checks whether the stored values in X and Y do equal.
     *
     * @param o The object to check against.
     * @return  true if equal, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof DataPoint)) return false;

        DataPoint oDataPoint = (DataPoint)o;
        return oDataPoint.X.equals(X) && oDataPoint.Y.equals(Y);
    }

    /**
     * The x-value of this DataPoint.
     */
    public XType X;
    /**
     * The y-value of this DataPoint.
     */
    public YType Y;
}
