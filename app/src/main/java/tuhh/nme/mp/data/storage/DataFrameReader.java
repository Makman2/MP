package tuhh.nme.mp.data.storage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import tuhh.nme.mp.data.DataFrame;
import tuhh.nme.mp.data.DataPoint;

/**
 * The base class for all readers that support the MPDF (MP-DataFrame) format.
 *
 * @param <XType> The x-type of the DataFrame to convert to.
 * @param <YType> The y-type of the DataFrame to convert to.
 */
public abstract class DataFrameReader<XType, YType>
{
    /**
     * Reads the data from the given InputStream.
     *
     * @param stream               The stream where to read from.
     * @return                     The back-converted DataFrame.
     * @throws IOException         Thrown when IO errors occurred while trying to read data.
     * @throws MPDFFormatException Thrown when the stream data is invalid. The DataFrame's that
     *                             could be read so far until exception occurred are retrievable
     *                             using getMetaDataFrame() at the exception.
     */
    public DataFrame<XType, YType> read(InputStream stream) throws IOException, MPDFFormatException
    {
        BufferedInputStream buffered_stream = new BufferedInputStream(stream);

        readHeader(buffered_stream);

        Collection<DataPoint<XType, YType>> entries = readDataEntries(buffered_stream);

        return new DataFrame<>(entries);
    }

    /**
     * Converts a chunk of data to an XType.
     *
     * @param data                 The data to convert.
     * @return                     The converted XType.
     * @throws MPDFFormatException Thrown on invalid input format.
     */
    protected abstract XType deserializeX(byte[] data) throws MPDFFormatException;

    /**
     * Converts a chunk of data to an YType.
     * @param data                 The data to convert.
     * @return                     The converted YType.
     * @throws MPDFFormatException Thrown on invalid input format.
     */
    protected abstract YType deserializeY(byte[] data) throws MPDFFormatException;

    /**
     * Parses and checks the file header.
     *
     * @param stream               The stream where to read from.
     * @throws IOException         Thrown when IO errors occurred while reading from stream.
     * @throws MPDFFormatException Thrown on invalid file header.
     */
    private void readHeader(InputStream stream) throws IOException, MPDFFormatException
    {
        byte[] buffer = new byte[FILE_HEADER.length];

        if (stream.read(buffer, 0, buffer.length) < buffer.length)
        {
            throwInvalidFileHeaderException();
        }

        if (!Arrays.equals(buffer, FILE_HEADER))
        {
            throwInvalidFileHeaderException();
        }
    }

    /**
     * Parses the DataPoint entries.
     *
     * @param stream               The stream where to read from.
     * @return                     The parsed data.
     * @throws IOException         Thrown when IO errors occurred while reading from stream.
     * @throws MPDFFormatException Thrown on invalid data format. The data read until exception
     *                             raised is stored inside the exception and can be retrieved using
     *                             getMetaDataFrame().
     */
    private Collection<DataPoint<XType, YType>> readDataEntries(InputStream stream)
        throws IOException, MPDFFormatException
    {
        byte[] buffer;

        XType x_value;
        YType y_value;
        int next_data_length;
        byte[] data_length_buffer = new byte[4];
        int read_result;

        LinkedList<DataPoint<XType, YType>> data = new LinkedList<>();

        while((read_result = stream.read(data_length_buffer, 0, data_length_buffer.length)) != -1)
        {
            // Extract next DataPoint.

            if (read_result != Integer.SIZE / Byte.SIZE)
            {
                throwDataCorruptedException(new DataFrame<>(data));
            }

            next_data_length = ByteBuffer.wrap(data_length_buffer).getInt();

            buffer = new byte[next_data_length];
            if (stream.read(buffer, 0, buffer.length) != next_data_length)
            {
                throwDataCorruptedException(new DataFrame<>(data));
            }

            x_value = deserializeX(buffer);

            if (stream.read(data_length_buffer, 0, data_length_buffer.length) !=
                Integer.SIZE / Byte.SIZE)
            {
                throwDataCorruptedException(new DataFrame<>(data));
            }

            next_data_length = ByteBuffer.wrap(data_length_buffer).getInt();

            buffer = new byte[next_data_length];
            if (stream.read(buffer, 0, buffer.length) != next_data_length)
            {
                throwDataCorruptedException(new DataFrame<>(data));
            }

            y_value = deserializeY(buffer);

            data.add(new DataPoint<>(x_value, y_value));
        }

        return data;
    }

    /**
     * Throws an MPDFFormatException for invalid file headers.
     *
     * @throws MPDFFormatException Thrown every time.
     */
    private void throwInvalidFileHeaderException() throws MPDFFormatException
    {
        throw new MPDFFormatException("Invalid file header.");
    }

    /**
     * Throws an MPDFFormatException for corrupted data.
     *
     * @param meta                 The reconstruction data to initialize the exception with.
     * @throws MPDFFormatException Thrown every time.
     */
    private void throwDataCorruptedException(DataFrame meta) throws MPDFFormatException
    {
        throw new MPDFFormatException("Data corrupted.", meta);
    }

    /**
     * The file header.
     */
    private final static byte[] FILE_HEADER = {0x4D, 0x50, 0x44, 0x46, 0x31};
}
