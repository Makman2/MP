package tuhh.nme.mp.data.storage;

import java.math.BigInteger;

import tuhh.nme.mp.data.HighPrecisionDate;


/**
 * Base class for all DataFrameReader's with DataFrame's with an x-type of HighPrecisionDate.
 *
 * @param <YType> The y-type of the DataFrame.
 */
public abstract class HighPrecisionDatedDataFrameReader<YType>
    extends DataFrameReader<HighPrecisionDate, YType>
{
    /**
     * Converts a byte-chunk to an HighPrecisionDate.
     * @param data                 The data to convert.
     * @return                     The HighPrecisionDate represented by the given data chunk.
     * @throws MPDFFormatException Never thrown.
     */
    @Override
    protected HighPrecisionDate deserializeX(byte[] data) throws MPDFFormatException
    {
        return new HighPrecisionDate(new BigInteger(data));
    }
}
