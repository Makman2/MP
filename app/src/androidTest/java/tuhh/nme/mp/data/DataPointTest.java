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
        assertEquals(uut.X, null);
        assertEquals(uut.Y, null);

        uut = new DataPoint<>(1, 5);
        assertEquals(uut.X.intValue(), 1);
        assertEquals(uut.Y.intValue(), 5);

        uut = new DataPoint<>(1000, 1200);
        assertEquals(uut.X.intValue(), 1000);
        assertEquals(uut.Y.intValue(), 1200);

        uut = new DataPoint<>(uut);
        assertEquals(uut.X.intValue(), 1000);
        assertEquals(uut.Y.intValue(), 1200);
    }

    /**
     * Tests whether the members are mutable as expected.
     */
    public void testMutability()
    {
        DataPoint<Integer, Integer> uut;

        uut = new DataPoint<>();
        assertEquals(uut.X, null);
        assertEquals(uut.Y, null);

        uut.X = 100;
        uut.Y = 4512;
        assertEquals(uut.X.intValue(), 100);
        assertEquals(uut.Y.intValue(), 4512);

        uut.X = 0;
        uut.Y = 111;
        assertEquals(uut.X.intValue(), 0);
        assertEquals(uut.Y.intValue(), 111);
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
        assertEquals(uut, test_against);

        test_against = new DataPoint<>(3, 199);
        assertTrue(!uut.equals(test_against));

        test_against = new DataPoint<>(777, 4);
        assertTrue(!uut.equals(test_against));

        test_against = new DataPoint<>(777, 199);
        assertTrue(!uut.equals(test_against));
    }
}
