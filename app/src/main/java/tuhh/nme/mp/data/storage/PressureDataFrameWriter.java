package tuhh.nme.mp.data.storage;

import java.nio.ByteBuffer;


/**
 * The DataFrameWriter for pressure data.
 */
public class PressureDataFrameWriter extends HighPrecisionDatedDataFrameWriter<Short>
{
    // Inherited documentation.
    @Override
    protected byte[] serializeY(Short object)
    {
        ByteBuffer buffer = ByteBuffer.allocate(Short.SIZE / Byte.SIZE);
        buffer.putShort(object);
        return buffer.array();
    }
}
