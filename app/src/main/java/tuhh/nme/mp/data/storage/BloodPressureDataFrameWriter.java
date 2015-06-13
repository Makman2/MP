package tuhh.nme.mp.data.storage;

import java.nio.ByteBuffer;


/**
 * Serializes BloodPressureDataFrame's to a stream.
 */
public class BloodPressureDataFrameWriter extends HighPrecisionDatedDataFrameWriter<Float>
{
    /**
     * Instantiates a new BloodPressureDataFrameWriter.
     */
    public BloodPressureDataFrameWriter()
    {
        super();
    }

    /**
     * Serializes a Float.
     *
     * @param object The Float to serialize.
     * @return       The serialized data in form of a byte array.
     */
    @Override
    protected byte[] serializeY(Float object)
    {
        return ByteBuffer.allocate(Float.SIZE / Byte.SIZE).putFloat(object).array();
    }
}
