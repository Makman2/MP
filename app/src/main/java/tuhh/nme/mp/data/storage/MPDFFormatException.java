package tuhh.nme.mp.data.storage;

import java.util.zip.DataFormatException;

import tuhh.nme.mp.data.DataFrame;


/**
 * Data format exception for the MP-DataFrame format. Supports passing meta data.
 */
public class MPDFFormatException extends DataFormatException
{
    /**
     * Instantiates a new MPDFFormatException with default message and no meta-data.
     */
    public MPDFFormatException()
    {
        super();
        m_DataFrameMeta = null;
    }

    /**
     * Instantiates a new MPDFFormatException with no meta-data.
     *
     * @param message The custom message of the exception.
     */
    public MPDFFormatException(String message)
    {
        super(message);
        m_DataFrameMeta = null;
    }

    /**
     * Instantiates a new MPDFFormatException with default message.
     *
     * @param meta The meta-data DataFrame of this exception.
     */
    public MPDFFormatException(DataFrame meta)
    {
        super();
        m_DataFrameMeta = meta;
    }

    /**
     * Instantiates a new MPDFFormatException.
     *
     * @param message The custom message of the exception.
     * @param meta    The meta-data DataFrame of this exception.
     */
    public MPDFFormatException(String message, DataFrame meta)
    {
        super(message);
        m_DataFrameMeta = meta;
    }

    /**
     * Returns the associated DataFrame meta-data.
     *
     * @return The DataFrame meta-data.
     */
    public DataFrame getMetaDataFrame()
    {
        return m_DataFrameMeta;
    }

    /**
     * The meta DataFrame.
     */
    private DataFrame m_DataFrameMeta;
}
