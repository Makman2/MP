package tuhh.nme.mp.data.storage;

import android.test.AndroidTestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import tuhh.nme.mp.data.BloodPressureDataFrame;
import tuhh.nme.mp.data.DataFrame;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionTimeSpan;

public class BloodPressureDataFrameReaderTest extends AndroidTestCase
{
    /**
     * Tests the construction.
     */
    public void testConstruction()
    {
        new BloodPressureDataFrameReader();
    }

    /**
     * Tests the whole deserialization process.
     */
    public void testReading() throws IOException, MPDFFormatException
    {
        BloodPressureDataFrameWriter writer = new BloodPressureDataFrameWriter();

        BloodPressureDataFrame frame = new BloodPressureDataFrame(
            "0,1,2,3,4,8,3.5,18.991",
            HighPrecisionDate.fromMicroseconds(11),
            HighPrecisionTimeSpan.fromNanoseconds(100));

        writer.add(frame);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        writer.write(output);

        BloodPressureDataFrameReader uut = new BloodPressureDataFrameReader();

        DataFrame<HighPrecisionDate, Float> result =
            uut.read(new ByteArrayInputStream(output.toByteArray()));

        assertEquals(frame, result);
    }

    /**
     * Tests the reaction when the file header is invalid.
     */
    public void testInvalidHeaderReaction() throws IOException
    {
        BloodPressureDataFrameReader uut = new BloodPressureDataFrameReader();

        boolean thrown = false;
        try
        {
            uut.read(new ByteArrayInputStream(new byte[] {1,2,3,4}));
        }
        catch (MPDFFormatException ex)
        {
            thrown = true;
            assertEquals(null, ex.getMetaDataFrame());
        }

        assertTrue(thrown);

        thrown = false;
        try
        {
            uut.read(new ByteArrayInputStream(new byte[] {0x4D, 0x50, 0x44, 0x46, 0x7F}));
        }
        catch (MPDFFormatException ex)
        {
            thrown = true;
            assertEquals(null, ex.getMetaDataFrame());
        }

        assertTrue(thrown);
    }

    /**
     * Tests what happens when the file length markers are invalid and point after EOF.
     */
    public void testInvalidFileLengthDescriptor() throws IOException
    {
        ByteArrayOutputStream input = new ByteArrayOutputStream();
        input.write(new byte[] {0x4D, 0x50, 0x44, 0x46, 0x31});
        input.write(new byte[] {0, 0, 0, 80});
        input.write(new byte[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

        BloodPressureDataFrameReader uut = new BloodPressureDataFrameReader();

        boolean thrown = false;
        try
        {
            uut.read(new ByteArrayInputStream(input.toByteArray()));
        }
        catch (MPDFFormatException ex)
        {
            thrown = true;
            assertTrue(ex.getMetaDataFrame().getData().isEmpty());
        }
        assertTrue(thrown);
    }
}
