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

        uut = new HighPrecisionDate(BigInteger.valueOf(0L));
        assertEquals(0L, uut.getTime().longValue());
        uut = new HighPrecisionDate(BigInteger.valueOf(1002L));
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
        assertEquals(0L, uut.toDate().getTime());

        uut = HighPrecisionDate.fromNanoseconds(100000L);
        assertEquals(0L, uut.toDate().getTime());

        uut = HighPrecisionDate.fromNanoseconds(1000000L);
        assertEquals(1L, uut.toDate().getTime());

        uut = HighPrecisionDate.fromMilliseconds(0L);
        assertEquals(0L, uut.toDate().getTime());

        uut = HighPrecisionDate.fromMilliseconds(77L);
        assertEquals(77L, uut.toDate().getTime());

        uut = HighPrecisionDate.fromMilliseconds(1112L);
        assertEquals(1112L, uut.toDate().getTime());

        uut = HighPrecisionDate.fromMilliseconds(99812786L);
        assertEquals(99812786L, uut.toDate().getTime());

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

        uut = new HighPrecisionDate(BigInteger.valueOf(21700L));

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
     * Tests the subtraction function subtract(HighPrecisionTimeSpan).
     */
    public void testSubtract()
    {
        HighPrecisionDate uut;
        HighPrecisionDate result;

        uut = new HighPrecisionDate(BigInteger.valueOf(21700L));

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
     * Tests the subtract(HighPrecisionDate) function.
     */
    public void testDateSubtraction()
    {
        HighPrecisionDate uut;
        HighPrecisionTimeSpan result;

        uut = new HighPrecisionDate(BigInteger.valueOf(21700L));

        result = uut.subtract(HighPrecisionDate.epoch);
        assertEquals(21700L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionDate.fromNanoseconds(1L));
        assertEquals(21699L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionDate.fromNanoseconds(3L));
        assertEquals(21697L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionDate.fromNanoseconds(5177L));
        assertEquals(16523L, result.getTime().longValue());

        // Negative subtraction.
        result = uut.subtract(HighPrecisionDate.fromNanoseconds(-4L));
        assertEquals(21704L, result.getTime().longValue());

        result = uut.subtract(HighPrecisionDate.fromNanoseconds(-812L));
        assertEquals(22512L, result.getTime().longValue());

        // Edge case: Go beyond zero.
        result = uut.subtract(HighPrecisionDate.fromNanoseconds(44000L));
        assertEquals(-22300, result.getTime().longValue());
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
        assertFalse(uut.equals(uut2));
    }

    /**
     * Tests the epoch field.
     */
    public void testEpochField()
    {
        assertEquals(0L, HighPrecisionDate.epoch.getTime().longValue());
    }
}
