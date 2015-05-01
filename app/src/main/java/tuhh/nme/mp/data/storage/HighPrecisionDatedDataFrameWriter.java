package tuhh.nme.mp.data.storage;

import tuhh.nme.mp.data.HighPrecisionDate;

/**
 * Base class for all DataFrameWriter's with DataFrame's with an x-type of HighPrecisionDate.
 *
 * @param <YType> The y-type of the DataFrame.
 */
public abstract class HighPrecisionDatedDataFrameWriter<YType>
    extends DataFrameWriter<HighPrecisionDate, YType>
{
    /**
     * Instantiates a new HighPrecisionDatedDataFrameWriter.
     */
    public HighPrecisionDatedDataFrameWriter()
    {
        super();
    }

    /**
     * Serializes a HighPrecisionDate.
     *
     * @param object The HighPrecisionDate to serialize.
     * @return       The serialized data in form of a byte array.
     */
    @Override
    protected byte[] serializeX(HighPrecisionDate object)
    {
        return object.getTime().toByteArray();
    }
}
