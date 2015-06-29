package tuhh.nme.mp.remote;


/**
 * Exception that is raised when a SocketCommand raised an exception inside handle().
 */
public class SocketCommandHandlingException extends RuntimeException
{
    /**
     * Instantiates a new InvalidReactionException with default message.
     */
    public SocketCommandHandlingException()
    {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Instantiates a new InvalidReactionException with custom message.
     *
     * @param detailMessage The custom message to display.
     */
    public SocketCommandHandlingException(String detailMessage)
    {
        super(detailMessage);
    }

    /**
     * Instantiates a new InvalidReactionException.
     *
     * @param detailMessage The custom message to display.
     * @param throwable     The Throwable that is cause of this exception.
     */
    public SocketCommandHandlingException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    /**
     * Instantiates a new InvalidReactionException.
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
