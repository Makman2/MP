package tuhh.nme.mp.data.plotting;

import java.math.BigDecimal;

import tuhh.nme.mp.data.HighPrecisionDate;

/**
 * Organizes BloodPressureDataFrame's and converts them into plottable Entry's.
 */
public class BloodPressurePlotData extends HighPrecisionDatedPlotData<Float>
{
    /**
     * Instantiates a new BloodPressurePlotData without scale and relative count from epoch.
     */
    public BloodPressurePlotData()
    {
        super();
    }

    /**
     * Instantiates a new BloodPressurePlotData.
     *
     * @param start The relative starting date that indicates the "zero"-time.
     */
    public BloodPressurePlotData(HighPrecisionDate start)
    {
        super(start);
    }

    /**
     * Instantiates a new BloodPressurePlotData with relative count from epoch.
     *
     * @param time_scale The time-span scaling factor.
     */
    public BloodPressurePlotData(BigDecimal time_scale)
    {
        super(time_scale);
    }

    /**
     * Instantiates a new BloodPressurePlotData.
     *
     * @param start      The relative starting date that indicates the "zero"-time.
     * @param time_scale The time-span scaling factor.
     */
    public BloodPressurePlotData(HighPrecisionDate start, BigDecimal time_scale)
    {
        super(start, time_scale);
    }

    /**
     * Maps the y-value of a DataPoint.
     *
     * @param y The y-value to map.
     * @return  The mapped value.
     */
    @Override
    protected final float mapY(Float y)
    {
        return y;
    }
}
