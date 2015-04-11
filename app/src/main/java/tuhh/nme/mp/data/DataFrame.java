package tuhh.nme.mp.data;

import java.util.Iterator;
import java.util.List;

/**
 * Represents a frame of data. In fact this class offers an ordered list of DataPoint's.
 *
 * @param <X> The type of the x-values.
 * @param <Y> The type of the associated y-values.
 */
public abstract class DataFrame<X, Y> implements Iterable<DataPoint<X, Y>>
{
    /**
     * Returns the stored data.
     *
     * @return An ordered list of the stored values.
     */
    public abstract List<DataPoint<X, Y>> getData();

    @Override
    public Iterator<DataPoint<X, Y>> iterator()
    {
        return getData().iterator();
    }
}
