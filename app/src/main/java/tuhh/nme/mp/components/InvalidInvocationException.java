package tuhh.nme.mp.components;


/**
 * Exception that is raised when an Event is invoked with invalid parameters.
 */
public class InvalidInvocationException extends RuntimeException
{
    /**
     * Instantiates a new InvalidInvocationException with default message.
     */
    public InvalidInvocationException()
    {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Instantiates a new InvalidInvocationException with custom message.
     *
     * @param detailMessage The custom message to display.
     */
    public InvalidInvocationException(String detailMessage)
    {
        super(detailMessage);
    }

    /**
     * Instantiates a new InvalidInvocationException.
     *
     * @param detailMessage The custom message to display.
     * @param throwable     The Throwable that is cause of this exception.
     */
    public InvalidInvocationException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    /**
     * Instantiates a new InvalidInvocationException.
     *
     * @param throwable The Throwable that is cause of this exception
     */
    public InvalidInvocationException(Throwable throwable)
    {
        super(DEFAULT_MESSAGE, throwable);
    }

    /**
     * The default message that is displayed in this exception.
     */
    private static final String DEFAULT_MESSAGE = "Invalid invocation.";
}
