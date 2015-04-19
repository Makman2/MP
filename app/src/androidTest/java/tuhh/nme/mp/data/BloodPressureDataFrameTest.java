package tuhh.nme.mp.data;

import android.test.AndroidTestCase;

import java.util.ArrayList;

public class BloodPressureDataFrameTest extends AndroidTestCase
{
    /**
     * Tests the interval constructor that associates dates with the given data list.
     */
    public void testAutoIntervalConstructor()
    {
        BloodPressureDataFrame uut;

        ArrayList<Float> testdata = new ArrayList<>();
        testdata.add(0.0f);
        testdata.add(1.0f);
        testdata.add(2.0f);
        testdata.add(3.0f);
        testdata.add(77.0f);
        testdata.add(-129.7f);

        // Test relatively simple behaviour.
        uut = new BloodPressureDataFrame(testdata,
                                         HighPrecisionDate.epoch,
                                         HighPrecisionTimeSpan.fromMilliseconds(1));

        ArrayList<DataPoint<HighPrecisionDate, Float>> comparison = new ArrayList<>();
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch, 0.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(1)), 1.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(2)), 2.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(3)), 3.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(4)), 77.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(5)), -129.7f));


        assertEquals(comparison, uut.getData());

        // Test a bit more complicated.
        uut = new BloodPressureDataFrame(testdata,
                                         HighPrecisionDate.fromMinutes(2),
                                         HighPrecisionTimeSpan.fromSeconds(12));

        comparison.clear();
        HighPrecisionDate reference = HighPrecisionDate.fromMinutes(2);
        comparison.add(new DataPoint<>(reference, 0.0f));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(12)), 1.0f));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(24)), 2.0f));
        comparison.add(new DataPoint<>(reference.add(HighPrecisionTimeSpan.fromSeconds(36)), 3.0f));
        comparison.add(new DataPoint<>(
                           reference.add(HighPrecisionTimeSpan.fromSeconds(48)), 77.0f));
        comparison.add(new DataPoint<>(
                           reference.add(HighPrecisionTimeSpan.fromSeconds(60)), -129.7f));

        assertEquals(comparison, uut.getData());
    }

    /**
     * Tests the constructor that initializes from an already existing data list.
     */
    public void testManualConstructor()
    {
        BloodPressureDataFrame uut;

        ArrayList<DataPoint<HighPrecisionDate, Float>> testdata = new ArrayList<>();
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMinutes(12), 109.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromHours(1), 1.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromSeconds(1), 0.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromNanoseconds(1029), -100.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMilliseconds(123), 5.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 2348951.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), 512992961.0f));
        testdata.add(new DataPoint<>(HighPrecisionDate.fromMicroseconds(555), -2139871.0f));

        uut = new BloodPressureDataFrame(testdata);
        assertEquals(testdata, uut.getData());
    }

    /**
     * Tests the constructor that parses the raw data of the remote module. This test automatically
     * covers the private function parseRawData().
     */
    public void testRawDataConstructor()
    {
        BloodPressureDataFrame uut;
        uut = new BloodPressureDataFrame("0,50,30.7,-12.81,33.882,10001221312",
                                         HighPrecisionDate.epoch,
                                         HighPrecisionTimeSpan.fromMilliseconds(7));


        ArrayList<DataPoint<HighPrecisionDate, Float>> comparison = new ArrayList<>();
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch, 0.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(7)), 50.0f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(14)), 30.7f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(21)), -12.81f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(28)), 33.882f));
        comparison.add(new DataPoint<>(HighPrecisionDate.epoch.add(
                           HighPrecisionTimeSpan.fromMilliseconds(35)), 10001221312.0f));

        assertEquals(comparison, uut.getData());

        // Test empty.
        uut = new BloodPressureDataFrame("",
                                         HighPrecisionDate.epoch,
                                         HighPrecisionTimeSpan.fromNanoseconds(1));

        comparison.clear();

        assertEquals(comparison, uut.getData());

        // Test invalid data exception.
        boolean thrown = false;
        try
        {
            uut = new BloodPressureDataFrame("1290,1290;Invalid",
                                             HighPrecisionDate.epoch,
                                             HighPrecisionTimeSpan.fromNanoseconds(1));
        }
        catch (NumberFormatException ex)
        {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
