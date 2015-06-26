package tuhh.nme.mp.components;

import java.util.HashMap;
import java.util.Map;


/**
 * This class allows to store data that can be accessed from different processes in the application.
 *
 * Especially for classes that can't be used as Parcelable's to pass data (i.e. between activities)
 * this class is the best way to go.
 */
public class SharedObjectMemory
{
    /**
     * Instantiates a new SharedObjectMemory.
     */
    public SharedObjectMemory()
    {
        m_Objects = new HashMap<>();
    }

    /**
     * Store an object inside this shared memory.
     *
     * @param obj The object to store.
     * @return    An ID that can be used to retrieve the object.
     */
    public int store(Object obj)
    {
        int id = createID();
        m_Objects.put(id, obj);
        return id;
    }

    /**
     * Retrieves the stored object without removing it from this memory.
     *
     * @param id The ID of the object to retrieve.
     * @return   The previously stored object. If no object was stored, returns null.
     */
    public Object get(int id)
    {
        return m_Objects.get(id);
    }

    /**
     * Retrieves the stored object and takes it off from the memory.
     *
     * @param id The ID of the object to take off.
     * @return   The previously stored object. If no object was stored, returns null.
     */
    public Object take(int id)
    {
        return m_Objects.remove(id);
    }

    /**
     * Generates an unused ID.
     *
     * @return An unused ID.
     */
    private int createID()
    {
        while (m_Objects.containsKey(m_ID))
        {
            m_ID++;
        }

        return m_ID;
    }

    /**
     * The current ID counter used in createID().
     */
    private int m_ID;
    /**
     * Stores all shared objects.
     */
    private Map<Integer, Object> m_Objects;
}
