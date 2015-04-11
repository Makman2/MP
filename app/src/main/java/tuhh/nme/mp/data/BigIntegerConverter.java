package tuhh.nme.mp.data;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Contains various functions to convert BigInteger's.
 */
public class BigIntegerConverter
{
    private BigIntegerConverter()
    {}

    /**
     * Converts a long into a BigInteger.
     *
     * @param num The long to convert.
     * @return    The BigInteger the long was converted to.
     */
    public static BigInteger toBigInteger(long num)
    {
        ByteBuffer buf = ByteBuffer.allocate(Long.SIZE / Byte.SIZE);
        buf.putLong(num);
        return new BigInteger(buf.array());
    }

    /**
     * Converts an int into a BigInteger.
     *
     * @param num The int to convert.
     * @return    The BigInteger the int was converted to.
     */
    public static BigInteger toBigInteger(int num)
    {
        ByteBuffer buf = ByteBuffer.allocate(Integer.SIZE / Byte.SIZE);
        buf.putInt(num);
        return new BigInteger(buf.array());
    }

    /**
     * Converts a short into a BigInteger.
     *
     * @param num The short to convert.
     * @return    The BigInteger the short was converted to.
     */
    public static BigInteger toBigInteger(short num)
    {
        ByteBuffer buf = ByteBuffer.allocate(Short.SIZE / Byte.SIZE);
        buf.putShort(num);
        return new BigInteger(buf.array());
    }

    /**
     * Converts a byte into a BigInteger.
     *
     * @param num The byte to convert.
     * @return    The BigInteger the byte was converted to.
     */
    public static BigInteger toBigInteger(byte num)
    {
        ByteBuffer buf = ByteBuffer.allocate(1);
        buf.put(num);
        return new BigInteger(buf.array());
    }
}
