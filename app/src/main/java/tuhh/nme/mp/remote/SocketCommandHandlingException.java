package tuhh.nme.mp.remote;


/**
 * Exception that is raised when a SocketCommand raised an exception inside handle().
 */
public class SocketCommandHandlingException extends RuntimeException
{
    /**
     * Instantiates a new SocketCommandHandlingException with default message.
     */
    public SocketCommandHandlingException()
    {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Instantiates a new SocketCommandHandlingException with custom message.
     *
     * @param detailMessage The custom message to display.
     */
    public SocketCommandHandlingException(String detailMessage)
    {
        super(detailMessage);
    }

    /**
     * Instantiates a new SocketCommandHandlingException.
     *
     * @param detailMessage The custom message to display.
     * @param throwable     The Throwable that is cause of this exception.
     */
    public SocketCommandHandlingException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    /**
     * Instantiates a new SocketCommandHandlingException.
     *
     * @param throwable The Throwable that is cause of this exception
     */
    public SocketCommandHandlingException(Throwable throwable)
    {
        super(DEFAULT_MESSAGE, throwable);
    }

    /**
     * The default message that is displayed in this exception.
     */
    private static final String DEFAULT_MESSAGE =
        "An error occurred during handle() in SocketCommand.";
}
