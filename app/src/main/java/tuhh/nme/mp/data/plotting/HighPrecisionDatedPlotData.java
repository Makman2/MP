package tuhh.nme.mp.data.plotting;

import java.math.BigDecimal;
import java.util.ArrayList;

import tuhh.nme.mp.data.DataPoint;
import tuhh.nme.mp.data.HighPrecisionDate;

/**
 * A list of data that maps to Entry's. The contained DataPoint's have as their x-type
 * HighPrecisionDate's. This class supports time scaling to reduce x-axis labels.
 *
 * @param <YType> The associated y-type.
 */
public abstract class HighPrecisionDatedPlotData<YType>
    extends DataFramePlotData<HighPrecisionDate, YType>
{
    /**
     * Instantiates a new HighPrecisionDatedPlotData without scale and relative count from epoch.
     */
    HighPrecisionDatedPlotData()
    {
        this(HighPrecisionDate.epoch, BigDecimal.ONE);
    }

    /**
     * Instantiates a new HighPrecisionDatedPlotData without scale.
     *
     * @param start The relative starting date that indicates the "zero"-time.
     */
    HighPrecisionDatedPlotData(HighPrecisionDate start)
    {
        this(start, BigDecimal.ONE);
    }

    /**
     * Instantiates a new HighPrecisionDatedPlotData with relative count from epoch.
     *
     * @param time_scale The time-span scaling factor.
     */
    HighPrecisionDatedPlotData(BigDecimal time_scale)
    {
        this(HighPrecisionDate.epoch, time_scale);
    }

    /**
     * Instantiates a new HighPrecisionDatedPlotData.
     *
     * @param start      The relative starting date that indicates the "zero"-time.
     * @param time_scale The time-span scaling factor.
     */
    HighPrecisionDatedPlotData(HighPrecisionDate start, BigDecimal time_scale)
    {
        super();
        m_StartingDate = start;
        m_TimeSpanScale = time_scale;
    }

    /**
     * Maps the x-value of a DataPoint.
     *
     * @param x The x-value to map.
     * @return  The mapped value.
     */
    @Override
    protected final int mapX(HighPrecisionDate x)
    {
        return (x.subtract(m_StartingDate)).multiply(m_TimeSpanScale).getTime().intValue();
    }

    /**
     * Returns the time scale.
     *
     * @return The time scale.
     */
    public final BigDecimal getTimeScale()
    {
        return m_TimeSpanScale;
    }

    /**
     * Sets the time scale.
     *
     * @param value The new time scale.
     */
    public final void setTimeScale(BigDecimal value)
    {
        if (!m_TimeSpanScale.equals(value))
        {
            m_TimeSpanScale = value;
            update();
        }
    }

    /**
     * Returns the relative starting date.
     *
     * @return The relative starting date.
     */
    public final HighPrecisionDate getStartingDate()
    {
        return m_StartingDate;
    }

    /**
     * Sets the relative starting date.
     *
     * @param value The new relative starting date.
     */
    public final void setStartingDate(HighPrecisionDate value)
    {
        if (!m_StartingDate.equals(value))
        {
            m_StartingDate = value;
            update();
        }
    }

    /**
     * Sets the time scale and starting date.
     *
     * Use this method if you need to set both, the time scale and starting date. Since the single
     * set functions directly update the mapped values, using them in sequence misses performance.
     *
     * @param scale The new time scale.
     * @param start The new relative starting date.
     */
    public final void setTimeScaleAndStartingDate(BigDecimal scale, HighPrecisionDate start)
    {
        if (!m_TimeSpanScale.equals(scale) || !m_StartingDate.equals(start))
        {
            m_TimeSpanScale = scale;
            m_StartingDate = start;
            update();
        }
    }

    /**
     * Updates the map values.
     */
    private void update()
    {
        // Store each DataFrame in a separate list and add it again later to invoke an update.
        ArrayList<Iterable<DataPoint<HighPrecisionDate, YType>>> backup = new ArrayList<>(inputs());
        clear();
        addAll(backup);
    }

    /**
     * The time-span scaling factor.
     */
    BigDecimal m_TimeSpanScale;
    /**
     * The relative starting date that indicates "zero"-time.
     */
    HighPrecisionDate m_StartingDate;
}
