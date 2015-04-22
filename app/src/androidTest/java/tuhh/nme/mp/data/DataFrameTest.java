package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.Iterator;

public class DataFrameTest extends AndroidTestCase
{
    /**
     * Tests the constructor.
     */
    public void testConstructor()
    {
        DataFrame<HighPrecisionDate, Integer> uut;

        ArrayList<DataPoint<HighPrecisionDate, Integer>> testdata = new ArrayList<>();
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMinutes(12), 0));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromHours(1), 7));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromSeconds(1), 6));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromNanoseconds(1029), -1110));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMilliseconds(123), -5));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 5512555));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 5559856));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 2222557));

        uut = new DataFrame<>(testdata);
        assertEquals(testdata, uut.getData());
    }

    /**
     * Tests the iterator() function.
     */
    public void testIterator()
    {
        DataFrame<Integer, Integer> uut;

        ArrayList<DataPoint<Integer, Integer>> data = new ArrayList<>();
        data.add(new DataPoint<>(1, 2));
        data.add(new DataPoint<>(5, 6));
        data.add(new DataPoint<>(99, -10));

        uut = new DataFrame<>(data);

        Iterator<DataPoint<Integer, Integer>> it = uut.iterator();
        assertEquals(data.get(0), it.next());
        assertEquals(data.get(1), it.next());
        assertEquals(data.get(2), it.next());
    }
}
