package tuhh.nme.mp.data.storage;

import java.util.zip.DataFormatException;

import tuhh.nme.mp.data.DataFrame;

/**
 * Data format exception for the MP-DataFrame format. Supports passing reconstructed data.
 */
public class MPDFFormatException extends DataFormatException
{
    /**
     * Instantiates a new MPDFFormatException with default message and no reconstructed data.
     */
    public MPDFFormatException()
    {
        super();
        m_Reconstructed = null;
    }

    /**
     * Instantiates a new MPDFFormatException with no reconstructed data.
     *
     * @param message The custom message of the exception.
     */
    public MPDFFormatException(String message)
    {
        super(message);
        m_Reconstructed = null;
    }

    /**
     * Instantiates a new MPDFFormatException with default message.
     *
     * @param reconstructed The reconstructed data of the corrupted stream.
     */
    public MPDFFormatException(DataFrame reconstructed)
    {
        super();
        m_Reconstructed = reconstructed;
    }

    /**
     * Instantiates a new MPDFFormatException.
     *
     * @param message       The custom message of the exception.
     * @param reconstructed The reconstructed data of the corrupted stream.
     */
    public MPDFFormatException(String message, DataFrame reconstructed)
    {
        super(message);
        m_Reconstructed = reconstructed;
    }

    /**
     * Returns the data that could be reconstructed until exception.
     *
     * @return The reconstructed DataFrame.
     */
    public DataFrame getReconstructedData()
    {
        return m_Reconstructed;
    }

    /**
     * The reconstructed DataFrame.
     */
    private DataFrame m_Reconstructed;
}
