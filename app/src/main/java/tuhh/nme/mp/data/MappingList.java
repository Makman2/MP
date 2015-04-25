package tuhh.nme.mp.data;

import java.util.Collection;

/**
 * Interface for defining a map that internally associates an InputType to a MappingType.
 *
 * @param <InputType>   The type that is inputted.
 * @param <MappingType> The type that is associated to InputType.
 */
public interface MappingList<InputType, MappingType> extends Iterable<InputType>
{
    /**
     * Adds an object to the MappingList.
     *
     * @param object The object to add.
     * @return       The mapping of the given object.
     */
    public MappingType add(InputType object);

    /**
     * Adds all objects in the given collection to the MappingList.
     *
     * @param objects The collection of objects to add.
     * @return        The mappings of each object inside the given collection.
     */
    public Collection<MappingType> addAll(Collection<InputType> objects);

    /**
     * Removes an object from the MappingList.
     *
     * @param object The object to remove.
     * @return       The associated mapping of the given object.
     */
    public MappingType remove(InputType object);

    /**
     * Removes all objects in the given collection from the MappingList.
     *
     * @param objects The collection of objects to remove.
     * @return        The associated mappings of each object inside the given collection.
     */
    public Collection<MappingType> removeAll(Collection<InputType> objects);

    /**
     * Removes all elements inside the MappingList.
     */
    public void clear();

    /**
     * Checks whether the given object is inside this MappingList.
     *
     * @param object The object to check for existence.
     * @return       Whether the object is existent.
     */
    public boolean contains(InputType object);

    /**
     * Checks whether the given objects are inside this MappingList.
     *
     * @param objects The collection of objects to check for existence.
     * @return        Whether all objects are existent.
     */
    public boolean containsAll(Collection<InputType> objects);

    /**
     * Returns the number of elements inside this MappingList.
     *
     * @return The size.
     */
    public int size();

    /**
     * Checks whether the MappingList contains no elements.
     *
     * @return true if empty, false if not.
     */
    public boolean isEmpty();

    /**
     * Computes a hash-code out of the MappingList.
     *
     * @return The hash-code.
     */
    public int hashCode();

    /**
     * Returns the collection of stored InputType's.
     *
     * @return The collection of InputType's.
     */
    public Collection<InputType> inputs();

    /**
     * Returns the collection of associated MappingType's.
     *
     * @return The collection of MappingType's.
     */
    public Collection<MappingType> mappings();
}
