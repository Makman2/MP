package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.math.BigDecimal;
import java.math.BigInteger;

public class HighPrecisionTimeSpanTest extends AndroidTestCase
{
    /**
     * Tests the construction of an HighPrecisionTimeSpan.
     */
    public void testConstruction()
    {
        HighPrecisionTimeSpan uut;
        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(0L));
        assertEquals(0L, uut.getTime().longValue());
        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(1000L));
        assertEquals(1000L, uut.getTime().longValue());
        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(21987L));
        assertEquals(21987L, uut.getTime().longValue());
        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, uut.getTime().longValue());

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
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromNanoseconds(100L);
        assertEquals(100L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromNanoseconds(82710L);
        assertEquals(82710L, uut.getTime().longValue());

        // From microseconds.
        uut = HighPrecisionTimeSpan.fromMicroseconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromMicroseconds(217L);
        assertEquals(217L * 1000L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromMicroseconds(777L);
        assertEquals(777L * 1000L, uut.getTime().longValue());

        // From milliseconds.
        uut = HighPrecisionTimeSpan.fromMilliseconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromMilliseconds(45L);
        assertEquals(45L * 1000L * 1000L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromMilliseconds(918);
        assertEquals(918L * 1000L * 1000L, uut.getTime().longValue());

        // From seconds.
        uut = HighPrecisionTimeSpan.fromSeconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromSeconds(1111L);
        assertEquals(1111L * 1000L * 1000L * 1000L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromSeconds(217);
        assertEquals(217L * 1000L * 1000L * 1000L, uut.getTime().longValue());

        // From minutes.
        uut = HighPrecisionTimeSpan.fromMinutes(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromMinutes(918L);
        assertEquals(918L * 1000L * 1000L * 1000L * 60L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromMinutes(31);
        assertEquals(31L * 1000L * 1000L * 1000L * 60L, uut.getTime().longValue());

        // From hours.
        uut = HighPrecisionTimeSpan.fromHours(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromHours(11L);
        assertEquals(11L * 1000L * 1000L * 1000L * 60L * 60L, uut.getTime().longValue());
        uut = HighPrecisionTimeSpan.fromHours(9918L);
        assertEquals(9918L * 1000L * 1000L * 1000L * 60L * 60L, uut.getTime().longValue());
    }

    // Test the addition function add().
    public void testAdd()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(21700L));

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(0L));
        assertEquals(21700L, result.getTime().longValue());

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(1L));
        assertEquals(21701L, result.getTime().longValue());

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(5L));
        assertEquals(21705L, result.getTime().longValue());

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(11772L));
        assertEquals(33472L, result.getTime().longValue());

        // Negative addition.
        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(-5L));
        assertEquals(21695L, result.getTime().longValue());

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(-712L));
        assertEquals(20988L, result.getTime().longValue());

        // Edge case: Go beyond long capacity.
        BigInteger comparison = BigInteger.valueOf(Long.MAX_VALUE);
        comparison = comparison.add(BigInteger.valueOf(21700L));

        result = uut.add(HighPrecisionTimeSpan.fromNanoseconds(Long.MAX_VALUE));
        assertEquals(0, result.getTime().compareTo(comparison));
    }

    /**
     * Tests the subtraction function subtract().
     */
    public void testSubtract()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(21700L));

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(0L));
        assertEquals(21700L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(1L));
        assertEquals(21699L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(5L));
        assertEquals(21695L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(11772L));
        assertEquals(9928L, result.getTime().longValue());

        // Negative subtraction.
        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(-5L));
        assertEquals(21705L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(-712L));
        assertEquals(22412L, result.getTime().longValue());

        // Edge case: Go beyond zero.
        result = uut.subtract(HighPrecisionTimeSpan.fromNanoseconds(33000L));
        assertEquals(-11300, result.getTime().longValue());
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
        assertFalse(uut.equals(uut2));
    }

    /**
     * Tests the multiplication function multiply(BigInteger).
     */
    public void testMultiplication()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(20L));

        result = uut.multiply(BigInteger.valueOf(0L));
        assertEquals(0L, result.getTime().longValue());

        result = uut.multiply(BigInteger.valueOf(1L));
        assertEquals(20L, result.getTime().longValue());

        result = uut.multiply(BigInteger.valueOf(2L));
        assertEquals(40L, result.getTime().longValue());

        result = uut.multiply(BigInteger.valueOf(3L));
        assertEquals(60L, result.getTime().longValue());

        result = uut.multiply(BigInteger.valueOf(10L));
        assertEquals(200L, result.getTime().longValue());

        result = uut.multiply(BigInteger.valueOf(1777L));
        assertEquals(35540L, result.getTime().longValue());
    }

    /**
     * Tests the multiplication function multiply(BigDecimal).
     */
    public void testDecimalMultiplication()
    {
        HighPrecisionTimeSpan uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionTimeSpan(BigInteger.valueOf(100L));

        result = uut.multiply(BigDecimal.valueOf(0.5));
        assertEquals(50L, result.getTime().longValue());

        result = uut.multiply(BigDecimal.valueOf(2.0));
        assertEquals(200L, result.getTime().longValue());

        result = uut.multiply(BigDecimal.valueOf(0.75));
        assertEquals(75, result.getTime().longValue());

        result = uut.multiply(BigDecimal.valueOf(1001111.57783));
        assertEquals(100111157, result.getTime().longValue());
    }

    /**
     * Tests the final zero field.
     */
    public void testZeroField()
    {
        // The actual value.
        assertEquals(0, HighPrecisionTimeSpan.zero.getTime().longValue());
    }
}
