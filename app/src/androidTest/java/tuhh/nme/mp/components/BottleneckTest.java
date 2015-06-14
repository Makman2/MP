package tuhh.nme.mp.components;

import android.test.AndroidTestCase;

import java.util.ArrayList;


class TestBottleneck extends Bottleneck
{
    public TestBottleneck()
    {
        texts = new ArrayList<>();
    }

    @Override
    protected void onProcess(Object object) throws Error
    {
        texts.add((String)object);
    }

    public void process(String text)
    {
        super.process(text);
    }

    public ArrayList<String> texts;
}


public class BottleneckTest extends AndroidTestCase
{
    /**
     * Tests the Bottleneck functionality.
     */
    public void test()
    {
        TestBottleneck uut = new TestBottleneck();

        assertTrue(uut.texts.isEmpty());

        uut.process("Hello");

        assertEquals(1, uut.texts.size());
        assertEquals("Hello", uut.texts.get(0));

        uut.process("A");

        assertEquals(2, uut.texts.size());
        assertEquals("Hello", uut.texts.get(0));
        assertEquals("A", uut.texts.get(1));

        uut.setStall(false);
        uut.process("B");

        assertEquals(3, uut.texts.size());
        assertEquals("Hello", uut.texts.get(0));
        assertEquals("A", uut.texts.get(1));
        assertEquals("B", uut.texts.get(2));

        uut.setStall(true);
        uut.process("X");

        assertEquals(3, uut.texts.size());
        assertEquals("Hello", uut.texts.get(0));
        assertEquals("A", uut.texts.get(1));
        assertEquals("B", uut.texts.get(2));

        uut.process("Y");

        assertEquals(3, uut.texts.size());
        assertEquals("Hello", uut.texts.get(0));
        assertEquals("A", uut.texts.get(1));
        assertEquals("B", uut.texts.get(2));

        uut.setStall(false);

        assertEquals(5, uut.texts.size());
        assertEquals("Hello", uut.texts.get(0));
        assertEquals("A", uut.texts.get(1));
        assertEquals("B", uut.texts.get(2));
        assertEquals("X", uut.texts.get(3));
        assertEquals("Y", uut.texts.get(4));
    }
}
