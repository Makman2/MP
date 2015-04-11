package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.util.ArrayList;

public class HighPrecisionDatedDataFrameTest extends AndroidTestCase
{
    /**
     * Tests the interval constructor that associates dates with the given data list.
     */
    public void testAutoIntervalConstructor()
    {
        HighPrecisionDatedDataFrame<Integer> uut;

        ArrayList<Integer> testdata = new ArrayList<>();
        testdata.add(0);
        testdata.add(1);
        testdata.add(2);
        testdata.add(3);
        testdata.add(88);
        testdata.add(10029);

        // Test relatively simple behaviour.
        uut = new HighPrecisionDatedDataFrame<>(testdata,
                                                HighPrecisionDate.epoch,
                                                HighPrecisionTimeSpan.fromMilliseconds(1));

        ArrayList<DataPoint<HighPrecisionDate, Integer>> comparison = new ArrayList<>();
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch, 0));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(1)), 1));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(2)), 2));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(3)), 3));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(4)), 88));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(5)), 10029));


        assertEquals(comparison, uut.getData());

        // Test a bit more complicated.
        uut = new HighPrecisionDatedDataFrame<>(testdata,
                                                HighPrecisionDate.fromMinutes(2),
                                                HighPrecisionTimeSpan.fromSeconds(12));

        comparison.clear();
        HighPrecisionDate reference = HighPrecisionDate.fromMinutes(2);
        comparison.add(new DataPoint<>(reference, 0));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(12)), 1));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(24)), 2));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(36)), 3));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(48)), 88));
        comparison.add(new DataPoint<>(
                           reference.add(HighPrecisionTimeSpan.fromSeconds(60)), 10029));

        assertEquals(comparison, uut.getData());
    }

    /**
     * Tests the constructor that initializes from an already existing data list.
     */
    public void testManualConstructor()
    {
        HighPrecisionDatedDataFrame<Integer> uut;

        ArrayList<DataPoint<HighPrecisionDate, Integer>> testdata = new ArrayList<>();
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMinutes(12), 109));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromHours(1), 1));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromSeconds(1), 0));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromNanoseconds(1029), -100));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMilliseconds(123), 5));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 55555));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 55556));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 55557));

        uut = new HighPrecisionDatedDataFrame<>(testdata);
        assertEquals(testdata, uut.getData());
    }
}
