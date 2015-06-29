package tuhh.nme.mp.data.plotting;


/**
 * The specific PlotData implementation for the incoming pressure data packets.
 */
public class PressurePlotData extends HighPrecisionDatedPlotData<Short>
{
    // Inherited documentation.
    @Override
    protected float mapY(Short y)
    {
        return (float)y;
    }
}
