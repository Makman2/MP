package tuhh.nme.mp.data.plotting;

import com.github.mikephil.charting.data.Entry;

import tuhh.nme.mp.data.DataPoint;
import tuhh.nme.mp.data.IterableElementMappingList;


/**
 * A list of data that maps to Entry's. The data needs to be contained from Iterable's (containing
 * DataPoint's itself).
 *
 * @param <XType> The DataPoint XType.
 * @param <YType> The DataPoint YType.
 */
public abstract class DataFramePlotData<XType, YType>
    extends IterableElementMappingList<DataPoint<XType, YType>, Entry>
{
    /**
     * Instantiates a new DataFramePlotData.
     */
    public DataFramePlotData()
    {
        super();
    }

    /**
     * Maps a single DataPoint onto an Entry.
     *
     * @param object The single DataPoint to map.
     * @return       The Entry.
     */
    @Override
    protected final Entry mapElement(DataPoint<XType, YType> object)
    {
        return new Entry(mapY(object.Y), mapX(object.X));
    }

    /**
     * Maps the x-value of a DataPoint.
     *
     * @param x The x-value to map.
     * @return  The mapped value.
     */
    protected abstract int mapX(XType x);

    /**
     * Maps the y-value of a DataPoint.
     *
     * @param y The y-value to map.
     * @return  The mapped value.
     */
    protected abstract float mapY(YType y);
}
