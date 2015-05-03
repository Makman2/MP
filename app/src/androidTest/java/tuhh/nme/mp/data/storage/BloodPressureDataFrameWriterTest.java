package tuhh.nme.mp.data.storage;

import android.test.AndroidTestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import tuhh.nme.mp.data.BloodPressureDataFrame;
import tuhh.nme.mp.data.DataFrame;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionTimeSpan;

public class BloodPressureDataFrameWriterTest extends AndroidTestCase
{
    // The following functions are covered automatically from other tests:
    // clear(), isEmpty(), contains(), containsAll(), size()

    /**
     * Tests the construction.
     */
    public void testConstruction()
    {
        new BloodPressureDataFrameWriter();
    }

    /**
     * Tests the add() function.
     */
    public void testAdd()
    {
        BloodPressureDataFrameWriter uut = new BloodPressureDataFrameWriter();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        BloodPressureDataFrame frame = new BloodPressureDataFrame(
            "4,5,12,222",
            HighPrecisionDate.epoch,
            HighPrecisionTimeSpan.fromNanoseconds(1));

        uut.add(frame);

        assertEquals(1, uut.size());
        assertFalse(uut.isEmpty());
        assertEquals(frame, uut.frames().iterator().next());

        uut.clear();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the addAll() function.
     */
    public void testAddAll()
    {
        BloodPressureDataFrameWriter uut = new BloodPressureDataFrameWriter();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        ArrayList<DataFrame<HighPrecisionDate, Float>> frames = new ArrayList<>();
        frames.add(new BloodPressureDataFrame("0,55",
                                              HighPrecisionDate.epoch,
                                              HighPrecisionTimeSpan.fromMicroseconds(100)));
        frames.add(new BloodPressureDataFrame("7.5,8.6,9.7",
                                              HighPrecisionDate.fromMinutes(3),
                                              HighPrecisionTimeSpan.fromNanoseconds(25)));

        uut.addAll(frames);

        assertEquals(2, uut.size());
        assertFalse(uut.isEmpty());
        assertTrue(uut.contains(frames.get(0)));
        assertTrue(uut.contains(frames.get(1)));
        assertTrue(uut.containsAll(frames));
    }

    /**
     * Tests the remove() function.
     */
    public void testRemove()
    {
        BloodPressureDataFrameWriter uut = new BloodPressureDataFrameWriter();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        BloodPressureDataFrame frame = new BloodPressureDataFrame(
            "111",
            HighPrecisionDate.epoch,
            HighPrecisionTimeSpan.fromNanoseconds(7));

        uut.add(frame);

        assertEquals(1, uut.size());
        assertFalse(uut.isEmpty());
        assertEquals(frame, uut.frames().iterator().next());

        uut.remove(frame);

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the removeAll() function.
     */
    public void testRemoveAll()
    {
        BloodPressureDataFrameWriter uut = new BloodPressureDataFrameWriter();

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());

        ArrayList<DataFrame<HighPrecisionDate, Float>> frames = new ArrayList<>();
        frames.add(new BloodPressureDataFrame("33,32",
                   HighPrecisionDate.epoch,
                   HighPrecisionTimeSpan.fromMicroseconds(77)));
        frames.add(new BloodPressureDataFrame("0.5,6.02,2.02",
                   HighPrecisionDate.fromMinutes(4),
                   HighPrecisionTimeSpan.fromNanoseconds(3)));

        uut.addAll(frames);

        assertEquals(2, uut.size());
        assertFalse(uut.isEmpty());
        assertTrue(uut.contains(frames.get(0)));
        assertTrue(uut.contains(frames.get(1)));
        assertTrue(uut.containsAll(frames));

        uut.removeAll(frames);

        assertEquals(0, uut.size());
        assertTrue(uut.isEmpty());
    }

    /**
     * Tests the complete stream writing and data conversion.
     */
    public void testWrite() throws IOException, MPDFFormatException
    {
        BloodPressureDataFrame frame = new BloodPressureDataFrame(
            "1.0,2.4,2.7,0.5",
            HighPrecisionDate.epoch,
            HighPrecisionTimeSpan.fromNanoseconds(100));

        BloodPressureDataFrameWriter uut = new BloodPressureDataFrameWriter();

        uut.add(frame);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        uut.write(stream);

        byte[] written_data = stream.toByteArray();

        ByteArrayOutputStream compare = new ByteArrayOutputStream();
        compare.write(new byte[] {0x4D, 0x50, 0x44, 0x46, 0x31});

        compare.write(toByte((short)(HighPrecisionDate.epoch.getTime().toByteArray().length)));
        compare.write(HighPrecisionDate.epoch.getTime().toByteArray());
        compare.write(toByte((short)(Float.SIZE / Byte.SIZE)));
        compare.write(toByte(1.0f));

        compare.write(
            toByte((short)(HighPrecisionDate.fromNanoseconds(100).getTime().toByteArray().length)));
        compare.write(HighPrecisionDate.fromNanoseconds(100).getTime().toByteArray());
        compare.write(toByte((short)(Float.SIZE / Byte.SIZE)));
        compare.write(toByte(2.4f));

        compare.write(
            toByte((short)(HighPrecisionDate.fromNanoseconds(200).getTime().toByteArray().length)));
        compare.write(HighPrecisionDate.fromNanoseconds(200).getTime().toByteArray());
        compare.write(toByte((short)(Float.SIZE / Byte.SIZE)));
        compare.write(toByte(2.7f));

        compare.write(
            toByte((short)(HighPrecisionDate.fromNanoseconds(300).getTime().toByteArray().length)));
        compare.write(HighPrecisionDate.fromNanoseconds(300).getTime().toByteArray());
        compare.write(toByte((short)(Float.SIZE / Byte.SIZE)));
        compare.write(toByte(0.5f));

        byte[] t = compare.toByteArray();
        assertEquals(t.length, written_data.length);
        for (int i = 0; i < written_data.length; i++)
        {
            assertEquals(t[i], written_data[i]);
        }
    }

    private byte[] toByte(short value)
    {
        return ByteBuffer.allocate(Short.SIZE / Byte.SIZE).putShort(value).array();
    }

    private byte[] toByte(float value)
    {
        return ByteBuffer.allocate(Float.SIZE / Byte.SIZE).putFloat(value).array();
    }
}
