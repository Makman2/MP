package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.math.BigInteger;

public class BigIntegerConverterTest extends AndroidTestCase
{
    public void testToBigIntegerLong()
    {
        long[] testvalues = {30L, -12L, 0L, -1000L, 1209409L};
        for (long testvalue : testvalues)
        {
            BigInteger bigint = BigIntegerConverter.toBigInteger(testvalue);
            assertEquals(bigint.longValue(), testvalue);
        }
    }

    public void testToBigIntegerInteger()
    {
        int[] testvalues = {30, -12, 0, -1000, 1209409};
        for (int testvalue : testvalues)
        {
            BigInteger bigint = BigIntegerConverter.toBigInteger(testvalue);
            assertEquals(bigint.intValue(), testvalue);
        }
    }

    public void testToBigIntegerShort()
    {
        short[] testvalues = {30, -12, 0, -1000, 12409};
        for (short testvalue : testvalues)
        {
            BigInteger bigint = BigIntegerConverter.toBigInteger(testvalue);
            assertEquals(bigint.shortValue(), testvalue);
        }
    }

    public void testToBigIntegerByte()
    {
        byte[] testvalues = {127, -128, 0, -11, 71};
        for (byte testvalue : testvalues)
        {
            BigInteger bigint = BigIntegerConverter.toBigInteger(testvalue);
            assertEquals(bigint.byteValue(), testvalue);
        }
    }
}