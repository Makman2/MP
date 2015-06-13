package tuhh.nme.mp.data;

import android.test.AndroidTestCase;


public class DataPointTest extends AndroidTestCase
{
    /**
     * Tests whether the values are set correctly.
     */
    public void testValues()
    {
        DataPoint<Integer, Integer> uut;

        uut = new DataPoint<>();
        assertEquals(null, uut.X);
        assertEquals(null, uut.Y);

        uut = new DataPoint<>(1, 5);
        assertEquals(1, uut.X.intValue());
        assertEquals(5, uut.Y.intValue());

        uut = new DataPoint<>(1000, 1200);
        assertEquals(1000, uut.X.intValue());
        assertEquals(1200, uut.Y.intValue());

        uut = new DataPoint<>(uut);
        assertEquals(1000, uut.X.intValue());
        assertEquals(1200, uut.Y.intValue());
    }

    /**
     * Tests whether the members are mutable as expected.
     */
    public void testMutability()
    {
        DataPoint<Integer, Integer> uut;

        uut = new DataPoint<>();
        assertEquals(null, uut.X);
        assertEquals(null, uut.Y);

        uut.X = 100;
        uut.Y = 4512;
        assertEquals(100, uut.X.intValue());
        assertEquals(4512, uut.Y.intValue());

        uut.X = 0;
        uut.Y = 111;
        assertEquals(0, uut.X.intValue());
        assertEquals(111, uut.Y.intValue());
    }

    /**
     * Tests the equal() function.
     */
    public void testEqual()
    {
        DataPoint<Integer, Integer> uut;
        DataPoint<Integer, Integer> test_against;

        uut = new DataPoint<>(3, 4);
        assertEquals(uut, uut);

        test_against = new DataPoint<>(3, 4);
        assertEquals(test_against, uut);

        test_against = new DataPoint<>(3, 199);
        assertFalse(uut.equals(test_against));

        test_against = new DataPoint<>(777, 4);
        assertFalse(uut.equals(test_against));

        test_against = new DataPoint<>(777, 199);
        assertFalse(uut.equals(test_against));
    }
}
