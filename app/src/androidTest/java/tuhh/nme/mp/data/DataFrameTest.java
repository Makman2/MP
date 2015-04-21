package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.util.ArrayList;

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
}
