package tuhh.nme.mp.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A data frame with an x-value type of HighPrecisionDate.
 *
 * @param <T> The type to associate with the HighPrecisionDate.
 */
public class HighPrecisionDatedDataFrame<T> extends DataFrame<HighPrecisionDate, T>
{
    /**
     * Instantiates a new HighPrecisionDatedDataFrame class and associates the given data with
     * HighPrecisionDate's.
     *
     * @param data The data.
     * @param time The time the first data point is initialized with and the starting time frame for
     *             the next points.
     * @param step The time difference between each data point.
     */
    public HighPrecisionDatedDataFrame(Collection<T> data,
                                       HighPrecisionDate time,
                                       HighPrecisionTimeSpan step)
    {
        this(associateData(data, time, step));
    }

    /**
     * Instantiates a new HighPrecisionDatedDataFrame class from the given data set.
     *
     * @param data A collection of data to initialize with.
     */
    public HighPrecisionDatedDataFrame(Collection<DataPoint<HighPrecisionDate, T>> data)
    {
        super(data);
    }

    /**
     * Associates unresolved data with HighPrecisionDate's.
     *
     * @param data The input data.
     * @param time The time the first data point is initialized with and the starting time frame for
     *             the next points.
     * @param step The time difference between each data point.
     * @return     A list with the associated data.
     */
    private static <T> List<DataPoint<HighPrecisionDate, T>> associateData(
        Collection<T>         data,
        HighPrecisionDate     time,
        HighPrecisionTimeSpan step)
    {
        ArrayList<DataPoint<HighPrecisionDate, T>> list = new ArrayList<>();
        list.ensureCapacity(data.size());

        HighPrecisionDate tmp = new HighPrecisionDate(time);
        for (T elem : data)
        {
            list.add(new DataPoint<>(new HighPrecisionDate(tmp), elem));
            tmp = tmp.add(step);
        }

        return list;
    }
}
