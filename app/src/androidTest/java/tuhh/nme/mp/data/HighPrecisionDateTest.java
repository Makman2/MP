package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.math.BigInteger;
import java.util.Date;

public class HighPrecisionDateTest extends AndroidTestCase
{
    /**
     * Test constructors.
     */
    public void testConstruction()
    {
        HighPrecisionDate uut;

        uut = new HighPrecisionDate(BigIntegerConverter.toBigInteger(0));
        assertEquals(0L, uut.getTime().longValue());
        uut = new HighPrecisionDate(BigIntegerConverter.toBigInteger(1002));
        assertEquals(1002L, uut.getTime().longValue());

        uut = new HighPrecisionDate(new Date(1900L));
        assertEquals(1900L * 1000L * 1000L, uut.getTime().longValue());
        uut = new HighPrecisionDate(new Date(10291092039L));
        assertEquals(10291092039L * 1000L * 1000L, uut.getTime().longValue());

        HighPrecisionDate uut2 = new HighPrecisionDate(uut);
        assertEquals(uut2.getTime(), uut.getTime());
        assertNotSame(uut, uut2);
    }

    /**
     * Test static construction methods.
     */
    public void testStaticConstructors()
    {
        HighPrecisionDate uut;

        // From nanoseconds.
        uut = HighPrecisionDate.fromNanoseconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromNanoseconds(100L);
        assertEquals(100L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromNanoseconds(82710L);
        assertEquals(82710L, uut.getTime().longValue());

        // From microseconds.
        uut = HighPrecisionDate.fromMicroseconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromMicroseconds(217L);
        assertEquals(217L * 1000L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromMicroseconds(777L);
        assertEquals(777L * 1000L, uut.getTime().longValue());

        // From milliseconds.
        uut = HighPrecisionDate.fromMilliseconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromMilliseconds(45L);
        assertEquals(45L * 1000L * 1000L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromMilliseconds(918);
        assertEquals(918L * 1000L * 1000L, uut.getTime().longValue());

        // From seconds.
        uut = HighPrecisionDate.fromSeconds(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromSeconds(1111L);
        assertEquals(1111L * 1000L * 1000L * 1000L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromSeconds(217);
        assertEquals(217L * 1000L * 1000L * 1000L, uut.getTime().longValue());

        // From minutes.
        uut = HighPrecisionDate.fromMinutes(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromMinutes(918L);
        assertEquals(918L * 1000L * 1000L * 1000L * 60L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromMinutes(31);
        assertEquals(31L * 1000L * 1000L * 1000L * 60L, uut.getTime().longValue());

        // From hours.
        uut = HighPrecisionDate.fromHours(0L);
        assertEquals(0L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromHours(11L);
        assertEquals(11L * 1000L * 1000L * 1000L * 60L * 60L, uut.getTime().longValue());
        uut = HighPrecisionDate.fromHours(9918L);
        assertEquals(9918L * 1000L * 1000L * 1000L * 60L * 60L, uut.getTime().longValue());
    }

    /**
     * Test the toDate() function that converts into a normal date.
     */
    public void testToDateConversion()
    {
        HighPrecisionDate uut;

        uut = HighPrecisionDate.fromNanoseconds(1000L);
        assertEquals(uut.toDate().getTime(), 0L);

        uut = HighPrecisionDate.fromNanoseconds(100000L);
        assertEquals(uut.toDate().getTime(), 0L);

        uut = HighPrecisionDate.fromNanoseconds(1000000L);
        assertEquals(uut.toDate().getTime(), 1L);

        uut = HighPrecisionDate.fromMilliseconds(0L);
        assertEquals(uut.toDate().getTime(), 0L);

        uut = HighPrecisionDate.fromMilliseconds(77L);
        assertEquals(uut.toDate().getTime(), 77L);

        uut = HighPrecisionDate.fromMilliseconds(1112L);
        assertEquals(uut.toDate().getTime(), 1112L);

        uut = HighPrecisionDate.fromMilliseconds(99812786L);
        assertEquals(uut.toDate().getTime(), 99812786L);

        // Test overflow case.
        uut = HighPrecisionDate.fromMilliseconds(Long.MAX_VALUE);
        uut = uut.add(HighPrecisionTimeSpan.fromMilliseconds(1000000L));

        boolean thrown = false;
        try
        {
            uut.toDate();
        }
        catch (Exception ex)
        {
            thrown = true;
        }

        assertTrue(thrown);
    }

    /**
     * Tests the addition function add().
     */
    public void testAdd()
    {
        HighPrecisionDate uut;
        HighPrecisionDate result;

        uut = new HighPrecisionDate(BigIntegerConverter.toBigInteger(21700));

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
        HighPrecisionDate uut;
        HighPrecisionDate result;

        uut = new HighPrecisionDate(BigIntegerConverter.toBigInteger(21700));

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
        HighPrecisionDate uut;
        HighPrecisionDate uut2;

        uut = HighPrecisionDate.fromMilliseconds(100);
        assertEquals(uut, uut);

        uut2 = HighPrecisionDate.fromMilliseconds(100);
        assertEquals(uut, uut2);

        uut2 = HighPrecisionDate.fromHours(1);
        assertNotSame(uut, uut2);
    }

    /**
     * Tests the epoch field.
     */
    public void testEpochField()
    {
        assertEquals(HighPrecisionDate.epoch.getTime().longValue(), 0L);
    }
}
