package tuhh.nme.mp.data.storage;

import android.test.AndroidTestCase;

import tuhh.nme.mp.data.BloodPressureDataFrame;
import tuhh.nme.mp.data.HighPrecisionDate;
import tuhh.nme.mp.data.HighPrecisionTimeSpan;

public class MPDFFormatExceptionTest extends AndroidTestCase
{
    /**
     * Tests the construction.
     */
    public void testConstruction()
    {
        MPDFFormatException uut;

        uut = new MPDFFormatException();
        assertEquals(null, uut.getMetaDataFrame());

        uut = new MPDFFormatException("custom message");
        assertEquals(null, uut.getMetaDataFrame());
        assertEquals("custom message", uut.getMessage());

        BloodPressureDataFrame reconstruction =
            new BloodPressureDataFrame("1,2,3,4,10,53.7",
                                       HighPrecisionDate.epoch,
                                       HighPrecisionTimeSpan.fromNanoseconds(1));

        uut = new MPDFFormatException(reconstruction);
        assertSame(reconstruction, uut.getMetaDataFrame());

        uut = new MPDFFormatException("this is a message", reconstruction);
        assertSame(reconstruction, uut.getMetaDataFrame());
        assertEquals("this is a message", uut.getMessage());
    }
}
