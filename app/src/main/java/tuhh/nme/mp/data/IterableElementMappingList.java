package tuhh.nme.mp.data;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Maps an Iterable onto a collection, whereby each element can be mapped as a single.
 *
 * @param <InputType>   The input type contained by the passed Iterable's.
 * @param <MappingType> The mapping type where each element of type InputType is mapped to.
 */
public abstract class IterableElementMappingList<InputType, MappingType>
    extends HashMappingList<Iterable<InputType>, Collection<MappingType>>
{
    /**
     * Instantiates a new IterableElementMappingList.
     */
    public IterableElementMappingList()
    {
        super();
    }

    /**
     * Maps each element in the given Iterable.
     *
     * @param object The object to map.
     * @return       A collection of mapped values.
     */
    @Override
    protected final Collection<MappingType> map(Iterable<InputType> object)
    {
        ArrayList<MappingType> mapped = new ArrayList<>();

        for (InputType elem : object)
        {
            mapped.add(mapElement(elem));
        }

        return mapped;
    }

    /**
     * Maps an element inside the given Iterable.
     *
     * @param object The single object to map.
     * @return       The mapped value.
     */
    protected abstract MappingType mapElement(InputType object);
}
