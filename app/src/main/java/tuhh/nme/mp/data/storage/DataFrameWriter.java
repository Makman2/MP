package tuhh.nme.mp.data.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import tuhh.nme.mp.data.DataFrame;
import tuhh.nme.mp.data.DataPoint;


/**
 * Base class for all DataFrame writer classes.
 *
 * @param <XType> The x-type of the DataFrame to save.
 * @param <YType> The y-type of the DataFrame to save.
 */
public abstract class DataFrameWriter<XType, YType>
{
    /**
     * Instantiates a new DataFrameWriter.
     */
    public DataFrameWriter()
    {
        m_FrameByteMap = new HashMap<>();
    }

    /**
     * Adds a DataFrame that should be serialized.
     *
     * If the object to add already exists, nothing happens.
     *
     * @param object The DataFrame to add.
     */
    public void add(DataFrame<XType, YType> object)
    {
        if (!m_FrameByteMap.containsKey(object))
        {
            m_FrameByteMap.put(object, null);
        }
    }

    /**
     * Adds all DataFrame's in the given collection for serialization.
     *
     * Already existent DataFrame's are not added.
     *
     * @param collection The collection of DataFrame's to add.
     */
    public void addAll(Collection<DataFrame<XType, YType>> collection)
    {
        for (DataFrame<XType, YType> elem : collection)
        {
            m_FrameByteMap.put(elem, null);
        }
    }

    /**
     * Removes a DataFrame.
     *
     * @param object The DataFrame to remove.
     */
    public void remove(DataFrame<XType, YType> object)
    {
        m_FrameByteMap.remove(object);
    }

    /**
     * Removes all DataFrame's that are in the given collection.
     *
     * @param collection The collection of DataFrame's to remove.
     */
    public void removeAll(Collection<DataFrame<XType, YType>> collection)
    {
        for (DataFrame<XType, YType> elem : collection)
        {
            m_FrameByteMap.remove(elem);
        }
    }

    /**
     * Removes all DataFrame's.
     */
    public void clear()
    {
        m_FrameByteMap.clear();
    }

    /**
     * Checks whether the given DataFrame is existent.
     *
     * @param object The DataFrame to check for.
     * @return       true if existent, false if not.
     */
    public boolean contains(DataFrame<XType, YType> object)
    {
        return m_FrameByteMap.containsKey(object);
    }

    /**
     * Checks whether all given DataFrame's inside the collection are existent.
     *
     * @param collection The collection of DataFrame's to check for.
     * @return           true if all existent, false if not.
     */
    public boolean containsAll(Collection<DataFrame<XType, YType>> collection)
    {
        for (DataFrame<XType, YType> elem : collection)
        {
            if (!m_FrameByteMap.containsKey(elem))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns whether there are DataFrame's stored or not.
     *
     * @return true if empty, false if not.
     */
    public boolean isEmpty()
    {
        return m_FrameByteMap.isEmpty();
    }

    /**
     * Returns the number of stored DataFrame's.
     *
     * @return The number of DataFrame's.
     */
    public int size()
    {
        return m_FrameByteMap.size();
    }

    /**
     * Returns the stored DataFrame's.
     *
     * @return The collection of DataFrame's.
     */
    public Collection<DataFrame<XType, YType>> frames()
    {
        return Collections.unmodifiableSet(m_FrameByteMap.keySet());
    }

    /**
     * Serializes a DataFrame into a byte array.
     *
     * @param object               The DataFrame to serialize.
     * @return                     The serialized DataFrame.
     * @throws IOException         Thrown when errors occurred while writing to the returned array.
     * @throws MPDFFormatException Thrown when a DataPoint is too large to be stored in the file.
     *                             The causing element is stored in the exception (and can be
     *                             retrieved using getMetaDataFrame()).
     */
    private byte[] serializeDataFrame(DataFrame<XType, YType> object)
        throws IOException, MPDFFormatException
    {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();

        for (DataPoint<XType, YType> elem : object)
        {
            byte[] serialized_x = serializeX(elem.X);
            if (serialized_x.length > Short.MAX_VALUE)
            {
                throw new MPDFFormatException("XType of DataPoint is serialized too large for " +
                                              "MPDF format. Is " +
                                              Integer.toString(serialized_x.length) + ", maximum " +
                                              Integer.toString(Short.MAX_VALUE) + ".", object);
            }

            byte[] serialized_y = serializeY(elem.Y);
            if (serialized_y.length > Short.MAX_VALUE)
            {
                throw new MPDFFormatException("YType of DataPoint is serialized too large for " +
                                              "MPDF format. Is " +
                                              Integer.toString(serialized_y.length) + ", maximum " +
                                              Integer.toString(Short.MAX_VALUE) + ".", object);
            }

            buf.write(ByteBuffer.allocate(Short.SIZE / Byte.SIZE)
                      .putShort((short)serialized_x.length).array());
            buf.write(serialized_x);
            buf.write(ByteBuffer.allocate(Short.SIZE / Byte.SIZE)
                      .putShort((short)serialized_y.length).array());
            buf.write(serialized_y);
        }

        return buf.toByteArray();
    }

    /**
     * Serialize and write the stored data.
     *
     * @param stream               The stream to write to.
     * @throws IOException         Thrown when errors occurred during stream write.
     * @throws MPDFFormatException Thrown when the DataFrame's to write have DataPoint's which size
     *                             is too large and exceeds Short.MAX_VALUE. The causing element is
     *                             stored in the exception (and can be retrieved using
     *                             getMetaDataFrame()).
     */
    public void write(OutputStream stream) throws IOException, MPDFFormatException
    {
        stream.write(FILE_HEADER);

        for (DataFrame<XType, YType> key : m_FrameByteMap.keySet())
        {
            byte[] data;
            data = m_FrameByteMap.get(key);

            if (data == null)
            {
                data = serializeDataFrame(key);
                m_FrameByteMap.put(key, data);
            }
        }

        for (byte[] value : m_FrameByteMap.values())
        {
            stream.write(value);
        }
    }

    /**
     * Serializes the x-type of the stored DataFrame's.
     *
     * @param object The x-type to serialize.
     * @return       The serialized data in form of a byte array.
     */
    protected abstract byte[] serializeX(XType object);

    /**
     * Serializes the y-type of the stored DataFrame's.
     *
     * @param object The y-type to serialize.
     * @return       The serialized data in form of a byte array.
     */
    protected abstract byte[] serializeY(YType object);

    /**
     * The underlying hash-map that stores the DataFrame's. Also associates the corresponding
     * data-chunks to write to the stream for performance boost.
     */
    private HashMap<DataFrame<XType, YType>, byte[]> m_FrameByteMap;

    /**
     * The file header.
     */
    private final static byte[] FILE_HEADER = {0x4D, 0x50, 0x44, 0x46, 0x31};
}
