package tuhh.nme.mp.components;

import android.test.AndroidTestCase;


public class InvalidInvocationExceptionTest extends AndroidTestCase
{
    /**
     * Tests the construction.
     */
    public void testConstruction()
    {
        InvalidInvocationException uut;

        uut = new InvalidInvocationException("custom message");
        assertEquals("custom message", uut.getMessage());

        uut = new InvalidInvocationException("Hello world");
        assertEquals("Hello world", uut.getMessage());
    }
}
