package tuhh.nme.mp.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * A data frame that manages the incoming pressure data from remote module.
 */
public class BloodPressureDataFrame extends HighPrecisionDatedDataFrame<Float>
{
    /**
     * Instantiates a new PressureDataFrame class and associates the given data with
     * HighPrecisionDate's.
     *
     * @param data The data.
     * @param time The time the first data point is initialized with and the starting time frame for
     *             the next points.
     * @param step The time difference between each data point.
     */
    public BloodPressureDataFrame(Collection<Float> data,
                                  HighPrecisionDate time,
                                  HighPrecisionTimeSpan step)
    {
        super(data, time, step);
    }

    /**
     * Instantiates a new PressureDataFrame class from the given data set.
     *
     * @param data A collection of data to initialize with.
     */
    public BloodPressureDataFrame(Collection<DataPoint<HighPrecisionDate, Float>> data)
    {
        super(data);
    }

    /**
     * Instantiates a new PressureDataFrame class from remote module raw data.
     *
     * @param raw                    The raw data string from the module. This is a comma separated
     *                               list of floating-point values.
     * @param time                   The time the first data point is initialized with and the
     *                               starting time frame for the next points.
     * @param step                   The time difference between each data point.
     * @throws NumberFormatException Thrown when the raw data is corrupted.
     */
    public BloodPressureDataFrame(String raw, HighPrecisionDate time, HighPrecisionTimeSpan step)
    throws NumberFormatException
    {
        super(parseRawData(raw), time, step);
    }

    /**
     * Parses the raw data incoming from the remote module.
     *
     * The pressure data is a comma separated list of floating-point-numbers. For example
     * "1,2,3,4,3.5,6,0"
     *
     * @param raw                    The input data string.
     * @return                       An array of floats containing the parsed data.
     * @throws NumberFormatException Thrown when the data format is invalid.
     */
    private static List<Float> parseRawData(String raw) throws NumberFormatException
    {

        ArrayList<Float> data_list = new ArrayList<>();

        if (raw.length() == 0)
        {
            // Nothing to parse. Need to catch here since float parsing an empty string throws.
            // But we want just an empty list.
            return data_list;
        }

        String[] data_string_list = raw.split(",");

        data_list.ensureCapacity(data_string_list.length);

        try
        {
            for (String elem : data_string_list)
            {
                data_list.add(Float.parseFloat(elem));
            }
        }
        catch (NumberFormatException ex)
        {
            throw new NumberFormatException("Invalid data format. Parsing a floating-point value " +
                                            "failed.");
        }

        return data_list;
    }
}
