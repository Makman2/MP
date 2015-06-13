package tuhh.nme.mp.components;

import android.test.AndroidTestCase;
import java.util.ArrayList;
import java.util.List;

import tuhh.nme.mp.TestUtilities;


class TestListener
{
    public TestListener()
    {
        invocationMessages = new ArrayList<>();
    }

    public void doSomething()
    {
        invocationMessages.add("doSomething called.");
    }

    public List<String> invocationMessages;
}


class TestListenerBeta extends TestListener
{
    public TestListenerBeta()
    {
        super();
    }

    public void doMore(String what_to_write)
    {
        invocationMessages.add(what_to_write);
    }
}


class TestEvent extends Event<TestListener>
{
    @Override
    protected void onRaise(TestListener listener, Object... params)
    {
        listener.doSomething();
    }
}


class TestEventBeta extends Event<TestListenerBeta>
{
    @Override
    protected boolean isInvocationValid(Object... params)
    {
        return params.length == 1;
    }

    @Override
    protected void onRaise(TestListenerBeta listener, Object... params) throws Error
    {
        listener.doMore((String)params[0]);
    }
}


public class EventTest extends AndroidTestCase
{
    // The test for getListeners() is covered automatically since this is the internal state
    // reference for an event used here.

    /**
     * Tests the simple construction of an Event.
     */
    public void testConstruction()
    {
        new TestEvent();
    }

    /**
     * Tests whether listeners get attached successfully.
     */
    public void testAttach()
    {
        TestEvent uut = new TestEvent();
        TestListener uut_listener = new TestListener();

        assertEquals(0, uut.getListeners().size());

        uut.attach(uut_listener);

        assertEquals(1, uut.getListeners().size());

        boolean thrown = false;
        try
        {
            uut.attach(uut_listener);
        }
        catch (InternalError ex)
        {
            thrown = true;
        }
        assertTrue(thrown);

        assertEquals(1, uut.getListeners().size());

        uut.attach(new TestListener());

        assertEquals(2, uut.getListeners().size());
    }

    /**
     * Tests whether listeners get detached successfully.
     */
    public void testDetach()
    {
        TestEvent uut = new TestEvent();
        TestListener uut_listener = new TestListener();

        assertEquals(0, uut.getListeners().size());

        boolean thrown = false;
        try
        {
            uut.detach(uut_listener);
        }
        catch (InternalError ex)
        {
            thrown = true;
        }
        assertTrue(thrown);
        assertEquals(0, uut.getListeners().size());

        uut.attach(uut_listener);

        assertEquals(1, uut.getListeners().size());

        uut.detach(uut_listener);

        assertEquals(0, uut.getListeners().size());

        uut.attach(uut_listener);
        uut.attach(new TestListener());

        assertEquals(2, uut.getListeners().size());

        uut.detach(uut_listener);

        assertEquals(1, uut.getListeners().size());

        uut.detach(uut.getListeners().iterator().next());

        assertEquals(0, uut.getListeners().size());
    }

    /**
     * Tests whether the detachAll() function removes all registered listeners.
     */
    public void testDetachAll()
    {
        TestEvent uut = new TestEvent();

        assertEquals(0, uut.getListeners().size());

        uut.detachAll();

        assertEquals(0, uut.getListeners().size());

        uut.attach(new TestListener());
        uut.attach(new TestListener());

        assertEquals(2, uut.getListeners().size());

        uut.detachAll();

        assertEquals(0, uut.getListeners().size());

        uut.detachAll();

        assertEquals(0, uut.getListeners().size());

        uut.attach(new TestListener());

        assertEquals(1, uut.getListeners().size());

        uut.detachAll();

        assertEquals(0, uut.getListeners().size());
    }

    /**
     * Tests for the correct invocation of onRaise() for each registered listener.
     */
    public void testCall()
    {
        TestEvent uut = new TestEvent();
        TestListener tl1 = new TestListener();
        TestListener tl2 = new TestListener();

        assertEquals(0, tl1.invocationMessages.size());
        assertEquals(0, tl2.invocationMessages.size());

        uut.raise();

        assertEquals(0, tl1.invocationMessages.size());
        assertEquals(0, tl2.invocationMessages.size());

        uut.attach(tl1);
        uut.attach(tl2);

        assertEquals(0, tl1.invocationMessages.size());
        assertEquals(0, tl2.invocationMessages.size());

        uut.raise();

        assertEquals(1, tl1.invocationMessages.size());
        assertEquals(1, tl2.invocationMessages.size());

        uut.raise();

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals(2, tl2.invocationMessages.size());

        uut.detach(tl1);

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals(2, tl2.invocationMessages.size());

        uut.raise();

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals(3, tl2.invocationMessages.size());

        uut.raise();

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals(4, tl2.invocationMessages.size());

        uut.attach(tl1);
        uut.detach(tl2);

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals(4, tl2.invocationMessages.size());

        uut.raise();

        assertEquals(3, tl1.invocationMessages.size());
        assertEquals(4, tl2.invocationMessages.size());

        uut.detachAll();
        uut.raise();

        assertEquals(3, tl1.invocationMessages.size());
        assertEquals(4, tl2.invocationMessages.size());
    }

    public void testCallWithParameters()
    {
        TestEventBeta uut = new TestEventBeta();
        TestListenerBeta tl1 = new TestListenerBeta();
        TestListenerBeta tl2 = new TestListenerBeta();

        assertEquals(0, tl1.invocationMessages.size());
        assertEquals(0, tl2.invocationMessages.size());

        uut.raise("0");

        assertEquals(0, tl1.invocationMessages.size());
        assertEquals(0, tl2.invocationMessages.size());

        uut.attach(tl1);
        uut.attach(tl2);

        assertEquals(0, tl1.invocationMessages.size());
        assertEquals(0, tl2.invocationMessages.size());

        boolean thrown = false;
        try
        {
            // raise() takes one argument. Invoking with none shall throw
            // InvalidInvocationException.
            uut.raise();
        }
        catch (InvalidInvocationException ex)
        {
            thrown = true;
        }
        assertTrue(thrown);

        uut.raise("A");

        assertEquals(1, tl1.invocationMessages.size());
        assertEquals("A", tl1.invocationMessages.get(0));
        assertEquals(1, tl2.invocationMessages.size());
        assertEquals("A", tl2.invocationMessages.get(0));

        uut.raise("B");

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals("A", tl1.invocationMessages.get(0));
        assertEquals("B", tl1.invocationMessages.get(1));
        assertEquals(2, tl2.invocationMessages.size());
        assertEquals("A", tl2.invocationMessages.get(0));
        assertEquals("B", tl2.invocationMessages.get(1));

        uut.detach(tl1);
        uut.raise("C");

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals("A", tl1.invocationMessages.get(0));
        assertEquals("B", tl1.invocationMessages.get(1));
        assertEquals(3, tl2.invocationMessages.size());
        assertEquals("A", tl2.invocationMessages.get(0));
        assertEquals("B", tl2.invocationMessages.get(1));
        assertEquals("C", tl2.invocationMessages.get(2));

        uut.raise("D");

        assertEquals(2, tl1.invocationMessages.size());
        assertEquals("A", tl1.invocationMessages.get(0));
        assertEquals("B", tl1.invocationMessages.get(1));
        assertEquals(4, tl2.invocationMessages.size());
        assertEquals("A", tl2.invocationMessages.get(0));
        assertEquals("B", tl2.invocationMessages.get(1));
        assertEquals("C", tl2.invocationMessages.get(2));
        assertEquals("D", tl2.invocationMessages.get(3));

        uut.attach(tl1);
        uut.detach(tl2);
        uut.raise("E");

        assertEquals(3, tl1.invocationMessages.size());
        assertEquals("A", tl1.invocationMessages.get(0));
        assertEquals("B", tl1.invocationMessages.get(1));
        assertEquals("E", tl1.invocationMessages.get(2));
        assertEquals(4, tl2.invocationMessages.size());
        assertEquals("A", tl2.invocationMessages.get(0));
        assertEquals("B", tl2.invocationMessages.get(1));
        assertEquals("C", tl2.invocationMessages.get(2));
        assertEquals("D", tl2.invocationMessages.get(3));

        thrown = false;
        try
        {
            uut.raise(3);
        }
        catch (ClassCastException ex)
        {
            thrown = true;
        }
        assertTrue(thrown);

        thrown = false;
        try
        {
            uut.raise("1", "2", "3");
        }
        catch (InvalidInvocationException ex)
        {
            thrown = true;
        }
        assertTrue(thrown);

        uut.detachAll();
        uut.raise("X");

        assertEquals(3, tl1.invocationMessages.size());
        assertEquals("A", tl1.invocationMessages.get(0));
        assertEquals("B", tl1.invocationMessages.get(1));
        assertEquals("E", tl1.invocationMessages.get(2));
        assertEquals(4, tl2.invocationMessages.size());
        assertEquals("A", tl2.invocationMessages.get(0));
        assertEquals("B", tl2.invocationMessages.get(1));
        assertEquals("C", tl2.invocationMessages.get(2));
        assertEquals("D", tl2.invocationMessages.get(3));
    }

    /**
     * Tests the iterator.
     */
    public void testIterator()
    {
        TestEvent uut = new TestEvent();
        TestListener tl1 = new TestListener();
        TestListener tl2 = new TestListener();
        TestListener tl3 = new TestListener();

        ArrayList<TestListener> collected = TestUtilities.collect(uut.iterator());
        assertEquals(0, collected.size());

        uut.attach(tl1);
        uut.attach(tl2);

        collected = TestUtilities.collect(uut.iterator());
        assertTrue(collected.contains(tl1));
        assertTrue(collected.contains(tl2));
        assertFalse(collected.contains(tl3));

        uut.attach(tl3);
        uut.detach(tl1);

        collected = TestUtilities.collect(uut.iterator());
        assertFalse(collected.contains(tl1));
        assertTrue(collected.contains(tl2));
        assertTrue(collected.contains(tl3));

        uut.attach(tl1);

        collected = TestUtilities.collect(uut.iterator());
        assertTrue(collected.contains(tl1));
        assertTrue(collected.contains(tl2));
        assertTrue(collected.contains(tl3));
    }
}
