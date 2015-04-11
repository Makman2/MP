package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.math.BigInteger;

public class HighPrecisionTimeSpanTest extends AndroidTestCase
{
    /**
     * Tests the construction of an HighPrecisionTimeSpan.
     */
    public void testConstruction()
    {
        HighPrecisionTimeSpan uut;
        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(0));
        assertEquals(uut.getTime().longValue(), 0L);
        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(1000));
        assertEquals(uut.getTime().longValue(), 1000L);
        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(21987L));
        assertEquals(uut.getTime().longValue(), 21987L);
        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(Long.MAX_VALUE));
        assertEquals(uut.getTime().longValue(), Long.MAX_VALUE);

        HighPrecisionTimeSpan uut2 = new HighPrecisionTimeSpan(uut);
        assertEquals(uut.getTime(), uut2.getTime());
        assertNotSame(uut, uut2);
    }

    /**
     * Tests the static constructor functions.
     */
    public void testStaticConstructors()
    {
        HighPrecisionTimeSpan uut;

        // From nanoseconds.
        uut = HighPrecisionTimeSpan.fromNanoseconds(0L);
        assertEquals(uut.getTime().longValue(), 0L);
        uut = HighPrecisionTimeSpan.fromNanoseconds(100L);
        assertEquals(uut.getTime().longValue(), 100L);
        uut = HighPrecisionTimeSpan.fromNanoseconds(82710L);
        assertEquals(uut.getTime().longValue(), 82710L);

        // From microseconds.
        uut = HighPrecisionTimeSpan.fromMicroseconds(0L);
        assertEquals(uut.getTime().longValue(), 0L);
        uut = HighPrecisionTimeSpan.fromMicroseconds(217L);
        assertEquals(uut.getTime().longValue(), 217L * 1000L);
        uut = HighPrecisionTimeSpan.fromMicroseconds(777L);
        assertEquals(uut.getTime().longValue(), 777L * 1000L);

        // From milliseconds.
        uut = HighPrecisionTimeSpan.fromMilliseconds(0L);
        assertEquals(uut.getTime().longValue(), 0L);
        uut = HighPrecisionTimeSpan.fromMilliseconds(45L);
        assertEquals(uut.getTime().longValue(), 45L * 1000L * 1000L);
        uut = HighPrecisionTimeSpan.fromMilliseconds(918);
        assertEquals(uut.getTime().longValue(), 918L * 1000L * 1000L);

        // From seconds.
        uut = HighPrecisionTimeSpan.fromSeconds(0L);
        assertEquals(uut.getTime().longValue(), 0L);
        uut = HighPrecisionTimeSpan.fromSeconds(1111L);
        assertEquals(uut.getTime().longValue(), 1111L * 1000L * 1000L * 1000L);
        uut = HighPrecisionTimeSpan.fromSeconds(217);
        assertEquals(uut.getTime().longValue(), 217L * 1000L * 1000L * 1000L);

        // From minutes.
        uut = HighPrecisionTimeSpan.fromMinutes(0L);
        assertEquals(uut.getTime().longValue(), 0L);
        uut = HighPrecisionTimeSpan.fromMinutes(918L);
        assertEquals(uut.getTime().longValue(), 918L * 1000L * 1000L * 1000L * 60L);
        uut = HighPrecisionTimeSpan.fromMinutes(31);
        assertEquals(uut.getTime().longValue(), 31L * 1000L * 1000L * 1000L * 60L);

        // From hours.
        uut = HighPrecisionTimeSpan.fromHours(0L);
        assertEquals(uut.getTime().longValue(), 0L);
        uut = HighPrecisionTimeSpan.fromHours(11L);
        assertEquals(uut.getTime().longValue(), 11L * 1000L * 1000L * 1000L * 60L * 60L);
        uut = HighPrecisionTimeSpan.fromHours(9918L);
        assertEquals(uut.getTime().longValue(), 9918L * 1000L * 1000L * 1000L * 60L * 60L);
    }

    // Test the addition function add().
    public void testAdd()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(21700));

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(0L));
        assertEquals(result.getTime().longValue(), 21700L);

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(1L));
        assertEquals(result.getTime().longValue(), 21701L);

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(5L));
        assertEquals(result.getTime().longValue(), 21705L);

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(11772L));
        assertEquals(result.getTime().longValue(), 33472L);

        // Negative addition.
        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(-5L));
        assertEquals(result.getTime().longValue(), 21695L);

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(-712L));
        assertEquals(result.getTime().longValue(), 20988L);

        // Edge case: Go beyond long capacity.
        BigInteger comparison = BigIntegerConverter.toBigInteger(Long.MAX_VALUE);
        comparison = comparison.add(BigIntegerConverter.toBigInteger(21700));

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(Long.MAX_VALUE));
        assertEquals(result.getTime().compareTo(comparison), 0);
    }

    /**
     * Tests the subtraction function subtract().
     */
    public void testSubtract()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(21700));

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(0L));
        assertEquals(result.getTime().longValue(), 21700L);

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(1L));
        assertEquals(result.getTime().longValue(), 21699L);

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(5L));
        assertEquals(result.getTime().longValue(), 21695L);

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(11772L));
        assertEquals(result.getTime().longValue(), 9928L);

        // Negative subtraction.
        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(-5L));
        assertEquals(result.getTime().longValue(), 21705L);

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(-712L));
        assertEquals(result.getTime().longValue(), 22412L);

        // Edge case: Go beyond zero.
        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(33000L));
        assertEquals(result.getTime().longValue(), -11300);
    }

    /**
     * Tests the equal() function.
     */
    public void testEqual()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan uut2;

        uut = HighPrecisionTimeSpan.fromMilliseconds(100);
        assertEquals(uut, uut);

        uut2 = HighPrecisionTimeSpan.fromMilliseconds(100);
        assertEquals(uut, uut2);

        uut2 = HighPrecisionTimeSpan.fromHours(1);
        assertNotSame(uut, uut2);
    }

    /**
     * Tests the multiplication function multiply().
     */
    public void testMultiplication()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigIntegerConverter.toBigInteger(20));

        result = uut.multiply(BigIntegerConverter.toBigInteger(0));
        assertEquals(result.getTime().longValue(), 0L);

        result = uut.multiply(BigIntegerConverter.toBigInteger(1));
        assertEquals(result.getTime().longValue(), 20L);

        result = uut.multiply(BigIntegerConverter.toBigInteger(2));
        assertEquals(result.getTime().longValue(), 40L);

        result = uut.multiply(BigIntegerConverter.toBigInteger(3));
        assertEquals(result.getTime().longValue(), 60L);

        result = uut.multiply(BigIntegerConverter.toBigInteger(10));
        assertEquals(result.getTime().longValue(), 200L);

        result = uut.multiply(BigIntegerConverter.toBigInteger(1777));
        assertEquals(result.getTime().longValue(), 35540L);
    }

    /**
     * Tests the final zero field.
     */
    public void testZeroField()
    {
        // The actual value.
        assertEquals(HighPrecisionTimeSpan.zero.getTime().longValue(), 0);
    }
}
