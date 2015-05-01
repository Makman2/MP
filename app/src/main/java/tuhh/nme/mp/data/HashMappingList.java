package tuhh.nme.mp.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Manages a list of elements, where each element maps onto another type. So this list contains the
 * managed type itself and its mapped elements.
 *
 * This class implements a HashMap to manage content.
 *
 * @param <InputType>   The type that should be mapped.
 * @param <MappingType> The type of the already mapped element.
 */
public abstract class HashMappingList<InputType, MappingType>
    implements MappingList<InputType, MappingType>
{
    /**
     * Instantiates a new HashMappingList.
     */
    public HashMappingList()
    {
        m_Data = new HashMap<>();
    }

    /**
     * Adds an object to the HashMappingList.
     *
     * @param object The object to add.
     * @return       The mapping of the given object.
     */
    @Override
    public final MappingType add(InputType object)
    {
        return m_Data.put(object, map(object));
    }

    /**
     * Adds all objects in the given collection to the HashMappingList.
     *
     * @param objects The collection of objects to add.
     * @return        The mappings of each object inside the given collection.
     */
    @Override
    public final Collection<MappingType> addAll(Collection<InputType> objects)
    {
        ArrayList<MappingType> mapped = new ArrayList<>();
        mapped.ensureCapacity(objects.size());

        for (InputType elem : objects)
        {
            MappingType mapped_element = map(elem);
            mapped.add(mapped_element);
            m_Data.put(elem, mapped_element);
        }

        return mapped;
    }

    /**
     * Removes an object from the HashMappingList.
     *
     * @param object The object to remove.
     * @return       The associated mapping of the given object.
     */
    @Override
    public final MappingType remove(InputType object)
    {
        return m_Data.remove(object);
    }

    /**
     * Removes all objects in the given collections from the HashMappingList.
     *
     * @param objects The collection of objects to remove.
     * @return        The associated mappings of each object inside the given collection.
     */
    @Override
    public final Collection<MappingType> removeAll(Collection<InputType> objects)
    {
        ArrayList<MappingType> mapped = new ArrayList<>();
        mapped.ensureCapacity(objects.size());

        for (InputType elem : objects)
        {
            mapped.add(m_Data.remove(elem));
        }

        return mapped;
    }

    /**
     * Removes all elements inside the HashMappingList.
     */
    @Override
    public final void clear()
    {
        m_Data.clear();
    }

    /**
     * Checks whether the given object is inside this HashMappingList.
     *
     * @param object The object to check for existence.
     * @return       Whether the object is existent.
     */
    @Override
    public final boolean contains(InputType object)
    {
        return m_Data.containsKey(object);
    }

    /**
     * Checks whether the given objects are inside this HashMappingList.
     *
     * @param objects The collection of objects to check for existence.
     * @return        Whether all objects are existent.
     */
    @Override
    public final boolean containsAll(Collection<InputType> objects)
    {
        for (InputType elem : objects)
        {
            if (!m_Data.containsKey(elem))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the number of elements inside this HashMappingList.
     *
     * @return The size.
     */
    @Override
    public final int size()
    {
        return m_Data.size();
    }

    /**
     * Checks whether the HashMappingList contains no elements.
     *
     * @return true if empty, false if not.
     */
    @Override
    public final boolean isEmpty()
    {
        return m_Data.isEmpty();
    }

    /**
     * Checks whether this object equals another one.
     *
     * @param object The object.
     * @return       Whether the object equals this instance.
     */
    @Override
    public boolean equals(Object object)
    {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof MappingList)) return false;

        HashMappingList casted = (HashMappingList)object;

        return m_Data.equals(casted.m_Data);
    }

    /**
     * Computes a hash-code out of the HashMappingList.
     *
     * @return The hash-code.
     */
    @Override
    public final int hashCode()
    {
        return m_Data.hashCode();
    }

    /**
     * Returns an iterator to the stored InputType's.
     *
     * @return The collection of InputType's.
     */
    @Override
    public final Iterator<InputType> iterator()
    {
        return m_Data.keySet().iterator();
    }

    /**
     * Returns the collection of stored InputType's.
     *
     * @return The collection of InputType's.
     */
    @Override
    public final Collection<InputType> inputs()
    {
        return m_Data.keySet();
    }

    /**
     * Returns the collection of associated MappingType's.
     *
     * @return The collection of MappingType's.
     */
    @Override
    public final Collection<MappingType> mappings()
    {
        return m_Data.values();
    }

    /**
     * Maps the InputType onto a MappingType.
     *
     * @param object The InputType to map.
     * @return       The mapped MappingType.
     */
    protected abstract MappingType map(InputType object)
        throws IllegalArgumentException, IllegalStateException, NullPointerException;

    /**
     * Stores the InputType's together with their converted data of type MappingType.
     */
    private HashMap<InputType, MappingType> m_Data;
}
