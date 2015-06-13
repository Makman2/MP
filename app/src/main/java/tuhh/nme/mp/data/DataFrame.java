package tuhh.nme.mp.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a frame of data. In fact this class offers an ordered list of DataPoint's.
 *
 * @param <X> The type of the x-values.
 * @param <Y> The type of the associated y-values.
 */
public class DataFrame<X, Y> implements Iterable<DataPoint<X, Y>>
{
    /**
     * Instantiates a new DataFrame class from the given data set.
     *
     * @param data A collection of data to initialize with.
     */
    public DataFrame(Collection<DataPoint<X, Y>> data)
    {
        m_List = new ArrayList<>();
        m_List.addAll(data);
    }

    /**
     * Returns the stored data.
     *
     * @return An ordered list of the stored values.
     */
    public List<DataPoint<X, Y>> getData()
    {
        return Collections.unmodifiableList(m_List);
    }

    /**
     * Returns an iterator to the stored DataPoint's.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<DataPoint<X, Y>> iterator()
    {
        return getData().iterator();
    }

    /**
     * Returns whether this object equals another.
     *
     * @param object The object that should equal.
     * @return       true if equal, false if not.
     */
    @Override
    public boolean equals(Object object)
    {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof DataFrame)) return false;

        DataFrame casted = (DataFrame)object;

        return this.getData().equals(casted.getData());
    }

    /**
     * The list that stores the DataPoint's.
     */
    private ArrayList<DataPoint<X, Y>> m_List;
}
