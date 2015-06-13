package tuhh.nme.mp;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Various utility and helper functions used in tests.
 */
public final class TestUtilities
{
    /**
     * Non-instantiatable static class constructor.
     */
    private TestUtilities()
    {}

    /**
     * Collects all elements from an iterator and puts them into an ArrayList.
     *
     * @param it  The iterator where to collect from.
     * @param <T> The type to collect.
     * @return    The ArrayList containing all types collected from iterator.
     */
    public static <T> ArrayList<T> collect(Iterator<T> it)
    {
        ArrayList<T> ls = new ArrayList<>();
        while (it.hasNext())
        {
            ls.add(it.next());
        }
        return ls;
    }
}
