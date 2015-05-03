package tuhh.nme.mp.data.storage;

import android.test.AndroidTestCase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionTimeSpan;

public class DataFrameFileManagerTest extends AndroidTestCase
{
    /**
     * Sets up the test. In concrete registers the test context for the tested module.
     */
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        DataFrameFileManager.setContext(this.getContext());
    }

    /**
     * Tests every file manager component.
     */
    public void testEverything() throws IOException
    {
        HighPrecisionDate dt = new HighPrecisionDate(new Date());

        DataFrameFileManager.wipe();
        assertTrue(DataFrameFileManager.getStoredFiles().isEmpty());

        boolean thrown = false;
        try
        {
            DataFrameFileManager.openForRead(dt);
        }
        catch (IOException ex)
        {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        try
        {
            DataFrameFileManager.openForWrite(dt);
        }
        catch (IOException ex)
        {
            thrown = true;
        }
        assertTrue(thrown);

        OutputStream stream = DataFrameFileManager.create(dt);
        byte[] test_data = {1, 2, 3, 4};
        stream.write(test_data);
        stream.flush();
        stream.close();

        InputStream test_stream = DataFrameFileManager.openForRead(dt);
        byte[] buffer = new byte[test_data.length];
        assertEquals(buffer.length, test_stream.read(buffer, 0, buffer.length));

        for (int i = 0; i < buffer.length; i++)
        {
            assertEquals(test_data[i], buffer[i]);
        }

        assertEquals(-1, test_stream.read(buffer, 0, 1));

        test_stream.close();

        assertTrue(DataFrameFileManager.exists(dt));
        assertFalse(DataFrameFileManager.exists(HighPrecisionDate.epoch));
        assertFalse(
            DataFrameFileManager.exists(dt.subtract(HighPrecisionTimeSpan.fromMinutes(11))));

        assertEquals(1, DataFrameFileManager.getStoredFiles().size());
        for (HighPrecisionDate key : DataFrameFileManager.getStoredFiles())
        {
            assertEquals(dt, key);
        }

        DataFrameFileManager.delete(dt);

        assertEquals(0, DataFrameFileManager.getStoredFiles().size());
    }
}
