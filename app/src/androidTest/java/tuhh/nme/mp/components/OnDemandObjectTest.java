package tuhh.nme.mp.components;

import android.test.AndroidTestCase;


class TestObject
{
    static
    {
        m_Instances = 0;
    }

    public TestObject()
    {
        ID = 76;
        m_Instances++;
    }

    public static int getInstances()
    {
        return m_Instances;
    }

    private static int m_Instances;

    public int ID;
}


class TestOnDemandObject extends OnDemandObject<TestObject>
{
    @Override
    protected TestObject instantiate()
    {
        return new TestObject();
    }
}


public class OnDemandObjectTest extends AndroidTestCase
{
    /**
     * Tests the OnDemandObject.
     */
    public void test()
    {
        // Save the number of previous instances of the test class to be sure that we recognize
        // instantiation of the OnDemandObject correctly.
        int prev_instances = TestObject.getInstances();

        TestOnDemandObject uut = new TestOnDemandObject();

        assertEquals(prev_instances, TestObject.getInstances());

        TestObject inst = uut.get();

        assertEquals(prev_instances + 1, TestObject.getInstances());

        assertEquals(inst, uut.get());
        assertEquals(prev_instances + 1, TestObject.getInstances());

        assertEquals(inst, uut.get());
    }
}
