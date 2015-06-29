package tuhh.nme.mp.data.plotting;

import android.test.AndroidTestCase;
import com.github.mikephil.charting.data.Entry;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import tuhh.nme.mp.data.BloodPressureDataFrame;
import tuhh.nme.mp.data.DataPoint;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionTimeSpan;


public class BloodPressurePlotDataTest extends AndroidTestCase
{
    /**
     * Tests the construction.
     */
    public void testConstructor()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();
        assertEquals(1.0, uut.getTimeScale().doubleValue());
        assertEquals(HighPrecisionDate.epoch, uut.getStartingDate());

        uut = new BloodPressurePlotData(BigDecimal.valueOf(0.001));
        assertEquals(0.001, uut.getTimeScale().doubleValue());
        assertEquals(HighPrecisionDate.epoch, uut.getStartingDate());

        uut = new BloodPressurePlotData(HighPrecisionDate.fromNanoseconds(1250L));
        assertEquals(1.0, uut.getTimeScale().doubleValue());
        assertEquals(HighPrecisionDate.fromNanoseconds(1250L), uut.getStartingDate());

        uut = new BloodPressurePlotData(HighPrecisionDate.fromHours(10L),
                                        BigDecimal.valueOf(125.0));
        assertEquals(125.0, uut.getTimeScale().doubleValue());
        assertEquals(HighPrecisionDate.fromHours(10L), uut.getStartingDate());
    }

    /**
     * Tests the add() function. This function implicitly tests mappings().
     */
    public void testAdd()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("0,1,2,3.0,7,21.7,33.5",
                                       HighPrecisionDate.fromMicroseconds(50),
                                       HighPrecisionTimeSpan.fromMicroseconds(10));

        Collection<Entry> mapping = uut.add(data);

        assertEquals(1, uut.size());

        Iterator<Entry> it = uut.mappings().iterator().next().iterator();
        Iterator<Entry> map_it = mapping.iterator();

        Entry elem = it.next();
        Entry map_elem = map_it.next();
        assertEquals(0.0f, elem.getVal());
        assertEquals(0.0f, map_elem.getVal());
        assertEquals(50000, elem.getXIndex());
        assertEquals(50000, map_elem.getXIndex());
        elem = it.next();
        map_elem = map_it.next();
        assertEquals(1.0f, elem.getVal());
        assertEquals(1.0f, map_elem.getVal());
        assertEquals(60000, elem.getXIndex());
        assertEquals(60000, map_elem.getXIndex());
        elem = it.next();
        map_elem = map_it.next();
        assertEquals(2.0f, elem.getVal());
        assertEquals(2.0f, map_elem.getVal());
        assertEquals(70000, elem.getXIndex());
        assertEquals(70000, map_elem.getXIndex());
        elem = it.next();
        map_elem = map_it.next();
        assertEquals(3.0f, elem.getVal());
        assertEquals(3.0f, map_elem.getVal());
        assertEquals(80000, elem.getXIndex());
        assertEquals(80000, map_elem.getXIndex());
        elem = it.next();
        map_elem = map_it.next();
        assertEquals(7.0f, elem.getVal());
        assertEquals(7.0f, map_elem.getVal());
        assertEquals(90000, elem.getXIndex());
        assertEquals(90000, map_elem.getXIndex());
        elem = it.next();
        map_elem = map_it.next();
        assertEquals(21.7f, elem.getVal());
        assertEquals(21.7f, map_elem.getVal());
        assertEquals(100000, elem.getXIndex());
        assertEquals(100000, map_elem.getXIndex());
        elem = it.next();
        map_elem = map_it.next();
        assertEquals(33.5f, elem.getVal());
        assertEquals(33.5f, map_elem.getVal());
        assertEquals(110000, elem.getXIndex());
        assertEquals(110000, map_elem.getXIndex());
    }

    /**
     * Tests the addAll() function. This function implicitly tests mappings().
     */
    public void testAddAll()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data1 =
            new BloodPressureDataFrame("1,2,33.33",
                                       HighPrecisionDate.fromNanoseconds(100),
                                       HighPrecisionTimeSpan.fromNanoseconds(5));

        BloodPressureDataFrame data2 =
            new BloodPressureDataFrame("44.44,83.1",
                                       HighPrecisionDate.fromNanoseconds(200),
                                       HighPrecisionTimeSpan.fromNanoseconds(15));

        // Need to specify Iterable as type directly, addAll() can't implicitly convert this
        // otherwise internally complex type.
        ArrayList<Iterable<DataPoint<HighPrecisionDate, Float>>> frames = new ArrayList<>();
        frames.add(data1);
        frames.add(data2);

        uut.add(data1);
        uut.addAll(frames);

        assertEquals(2, uut.size());
        assertTrue(uut.containsAll(frames));
    }

    /**
     * Tests the remove() function.
     */
    public void testRemove()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("0,1,4.75,-10.33",
                                       HighPrecisionDate.epoch,
                                       HighPrecisionTimeSpan.fromNanoseconds(100));

        uut.add(data);

        assertEquals(1, uut.size());

        uut.remove(data);

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the removeAll() function.
     */
    public void testRemoveAll()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data1 =
            new BloodPressureDataFrame("500,1000",
                                       HighPrecisionDate.fromNanoseconds(10),
                                       HighPrecisionTimeSpan.fromNanoseconds(1));

        BloodPressureDataFrame data2 =
            new BloodPressureDataFrame("55.4,55.5,555",
                                       HighPrecisionDate.fromNanoseconds(30),
                                       HighPrecisionTimeSpan.fromNanoseconds(2));

        ArrayList<Iterable<DataPoint<HighPrecisionDate, Float>>> frames = new ArrayList<>();
        frames.add(data1);
        frames.add(data2);

        uut.add(data1);
        uut.addAll(frames);

        assertEquals(2, uut.size());

        uut.removeAll(frames);

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the clear() and isEmpty() function.
     */
    public void testClearAndIsEmpty()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        uut.clear();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("500,1000",
                                       HighPrecisionDate.fromNanoseconds(44),
                                       HighPrecisionTimeSpan.fromNanoseconds(4));

        uut.add(data);

        assertEquals(1, uut.size());
        assertFalse(uut.isEmpty());

        uut.clear();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        uut.clear();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        uut.add(data);

        assertEquals(1, uut.size());
        assertFalse(uut.isEmpty());

        uut.clear();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the contains() function.
     */
    public void testContains()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("-10,-20,-30.6",
                                       HighPrecisionDate.epoch,
                                       HighPrecisionTimeSpan.fromMicroseconds(2));

        assertFalse(uut.contains(data));

        uut.add(data);

        assertTrue(uut.contains(data));
        assertTrue(uut.contains(data));

        uut.remove(data);

        assertFalse(uut.contains(data));
        assertFalse(uut.contains(data));

        uut.clear();

        assertFalse(uut.contains(data));
    }

    /**
     * Tests the containsAll() function.
     */
    public void testContainsAll()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data1 =
            new BloodPressureDataFrame("-1,-2,3",
                                       HighPrecisionDate.epoch,
                                       HighPrecisionTimeSpan.fromNanoseconds(1));

        BloodPressureDataFrame data2 =
            new BloodPressureDataFrame("",
                                       HighPrecisionDate.epoch,
                                       HighPrecisionTimeSpan.fromNanoseconds(1));

        ArrayList<Iterable<DataPoint<HighPrecisionDate, Float>>> frames = new ArrayList<>();
        frames.add(data1);
        frames.add(data2);

        assertFalse(uut.containsAll(frames));

        uut.add(data1);

        assertFalse(uut.containsAll(frames));

        uut.add(data2);

        assertTrue(uut.containsAll(frames));

        uut.clear();

        assertFalse(uut.containsAll(frames));

        uut.addAll(frames);

        assertTrue(uut.containsAll(frames));

        uut.removeAll(frames);

        assertFalse(uut.containsAll(frames));
    }

    /**
     * Tests the size() function.
     */
    public void testSize()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        uut.add(new BloodPressureDataFrame("",
                                           HighPrecisionDate.epoch,
                                           HighPrecisionTimeSpan.zero));

        assertEquals(1, uut.size());
        assertFalse(uut.isEmpty());

        uut.add(new BloodPressureDataFrame("",
                                           HighPrecisionDate.epoch,
                                           HighPrecisionTimeSpan.zero));

        assertEquals(2, uut.size());
        assertFalse(uut.isEmpty());

        uut.add(new BloodPressureDataFrame("",
                                           HighPrecisionDate.epoch,
                                           HighPrecisionTimeSpan.zero));

        assertEquals(3, uut.size());
        assertFalse(uut.isEmpty());

        uut.clear();
        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the inputs() function.
     */
    public void testInputs()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData();

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("22,33,44",
                                       HighPrecisionDate.fromMicroseconds(50),
                                       HighPrecisionTimeSpan.fromMicroseconds(10));

        uut.add(data);

        assertEquals(1, uut.size());

        Collection<Iterable<DataPoint<HighPrecisionDate, Float>>> collection = uut.inputs();

        assertEquals(1, collection.size());

        Iterator<DataPoint<HighPrecisionDate, Float>> it = collection.iterator().next().iterator();
        DataPoint<HighPrecisionDate, Float> elem;

        elem = it.next();
        assertEquals(HighPrecisionDate.fromMicroseconds(50), elem.X);
        assertEquals(22.0f, elem.Y);
        elem = it.next();
        assertEquals(HighPrecisionDate.fromMicroseconds(60), elem.X);
        assertEquals(33.0f, elem.Y);
        elem = it.next();
        assertEquals(HighPrecisionDate.fromMicroseconds(70), elem.X);
        assertEquals(44.0f, elem.Y);
    }

    /**
     * Tests the time scale feature (via getTimeScale() and setTimeScale()).
     */
    public void testTimeScale()
    {
        BloodPressurePlotData uut = new BloodPressurePlotData(BigDecimal.valueOf(0.001));

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("0,5,10,15,22.8",
                                       HighPrecisionDate.fromMicroseconds(10),
                                       HighPrecisionTimeSpan.fromMicroseconds(1));

        uut.add(data);

        assertEquals(1, uut.size());

        Iterator<Entry> it = uut.mappings().iterator().next().iterator();

        Entry elem = it.next();
        assertEquals(0.0f, elem.getVal());
        assertEquals(10, elem.getXIndex());
        elem = it.next();
        assertEquals(5.0f, elem.getVal());
        assertEquals(11, elem.getXIndex());
        elem = it.next();
        assertEquals(10.0f, elem.getVal());
        assertEquals(12, elem.getXIndex());
        elem = it.next();
        assertEquals(15.0f, elem.getVal());
        assertEquals(13, elem.getXIndex());
        elem = it.next();
        assertEquals(22.8f, elem.getVal());
        assertEquals(14, elem.getXIndex());

        uut.setTimeScale(BigDecimal.valueOf(2.0));

        it = uut.mappings().iterator().next().iterator();

        elem = it.next();
        assertEquals(0.0f, elem.getVal());
        assertEquals(20000, elem.getXIndex());
        elem = it.next();
        assertEquals(5.0f, elem.getVal());
        assertEquals(22000, elem.getXIndex());
        elem = it.next();
        assertEquals(10.0f, elem.getVal());
        assertEquals(24000, elem.getXIndex());
        elem = it.next();
        assertEquals(15.0f, elem.getVal());
        assertEquals(26000, elem.getXIndex());
        elem = it.next();
        assertEquals(22.8f, elem.getVal());
        assertEquals(28000, elem.getXIndex());
    }

    /**
     * Tests the relative starting date feature (via getStartingDate() and setStartingDate()).
     */
    public void testStartingDate()
    {
        BloodPressurePlotData uut =
            new BloodPressurePlotData(HighPrecisionDate.fromMicroseconds(1000));

        BloodPressureDataFrame data =
            new BloodPressureDataFrame("0,7,14,21,23.9,100.1",
                                       HighPrecisionDate.fromMicroseconds(1015),
                                       HighPrecisionTimeSpan.fromMicroseconds(5));

        uut.add(data);

        assertEquals(1, uut.size());

        Iterator<Entry> it = uut.mappings().iterator().next().iterator();

        Entry elem = it.next();
        assertEquals(0.0f, elem.getVal());
        assertEquals(15000, elem.getXIndex());
        elem = it.next();
        assertEquals(7.0f, elem.getVal());
        assertEquals(20000, elem.getXIndex());
        elem = it.next();
        assertEquals(14.0f, elem.getVal());
        assertEquals(25000, elem.getXIndex());
        elem = it.next();
        assertEquals(21.0f, elem.getVal());
        assertEquals(30000, elem.getXIndex());
        elem = it.next();
        assertEquals(23.9f, elem.getVal());
        assertEquals(35000, elem.getXIndex());
        elem = it.next();
        assertEquals(100.1f, elem.getVal());
        assertEquals(40000, elem.getXIndex());

        uut.setStartingDate(HighPrecisionDate.fromMicroseconds(15));

        it = uut.mappings().iterator().next().iterator();

        elem = it.next();
        assertEquals(0.0f, elem.getVal());
        assertEquals(1000000, elem.getXIndex());
        elem = it.next();
        assertEquals(7.0f, elem.getVal());
        assertEquals(1005000, elem.getXIndex());
        elem = it.next();
        assertEquals(14.0f, elem.getVal());
        assertEquals(1010000, elem.getXIndex());
        elem = it.next();
        assertEquals(21.0f, elem.getVal());
        assertEquals(1015000, elem.getXIndex());
        elem = it.next();
        assertEquals(23.9f, elem.getVal());
        assertEquals(1020000, elem.getXIndex());
        elem = it.next();
        assertEquals(100.1f, elem.getVal());
        assertEquals(1025000, elem.getXIndex());
    }
}
