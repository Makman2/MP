package tuhh.nme.mp.data.storage;

import java.nio.ByteBuffer;


/**
 * Reads an MPDF file for pressure data.
 */
public class PressureDataFrameReader extends HighPrecisionDatedDataFrameReader<Short>
{
    // Inherited documentation.
    @Override
    protected Short deserializeY(byte[] data) throws Exception
    {
        return ByteBuffer.wrap(data).getShort();
    }
}
