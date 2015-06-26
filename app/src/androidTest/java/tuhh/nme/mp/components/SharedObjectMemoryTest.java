package tuhh.nme.mp.components;

import android.test.AndroidTestCase;


public class SharedObjectMemoryTest extends AndroidTestCase
{
    // Tests construction.
    public void testConstruction()
    {
        new SharedObjectMemory();
    }

    // Tests the storing and retrieving capabilities.
    public void testStore()
    {
        SharedObjectMemory uut = new SharedObjectMemory();

        assertNull(uut.get(0));
        assertNull(uut.take(0));

        int id1 = uut.store("Hello");
        int id2 = uut.store("Test2");

        assertEquals("Hello", uut.get(id1));
        assertEquals("Test2", uut.get(id2));
        assertEquals("Hello", uut.get(id1));

        assertEquals("Hello", uut.take(id1));
        assertNull(uut.take(id1));
        assertEquals("Test2", uut.take(id2));
        assertNull(uut.get(id2));
    }
}
