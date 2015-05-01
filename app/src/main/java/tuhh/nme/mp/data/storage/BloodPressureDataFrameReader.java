package tuhh.nme.mp.data.storage;

import java.nio.ByteBuffer;

/**
 * Deserializes a BloodPressureDataFrame from stream.
 */
public class BloodPressureDataFrameReader extends HighPrecisionDatedDataFrameReader<Float>
{
    /**
     * Converts a chunk of bytes to a Float.
     *
     * @param data                 The data to convert.
     * @return                     The Float.
     * @throws MPDFFormatException Never thrown.
     */
    @Override
    protected Float deserializeY(byte[] data) throws MPDFFormatException
    {
        return ByteBuffer.wrap(data).getFloat();
    }
}
