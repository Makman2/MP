package tuhh.nme.mp.remote;


/**
 * Exception that is raised when a remote socket does not react like expected.
 */
public class InvalidReactionException extends RuntimeException
{
    /**
     * Instantiates a new InvalidReactionException with default message.
     */
    public InvalidReactionException()
    {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Instantiates a new InvalidReactionException with custom message.
     *
     * @param detailMessage The custom message to display.
     */
    public InvalidReactionException(String detailMessage)
    {
        super(detailMessage);
    }

    /**
     * Instantiates a new InvalidReactionException.
     *
     * @param detailMessage The custom message to display.
     * @param throwable     The Throwable that is cause of this exception.
     */
    public InvalidReactionException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    /**
     * Instantiates a new InvalidReactionException.
     *
     * @param throwable The Throwable that is cause of this exception
     */
    public InvalidReactionException(Throwable throwable)
    {
        super(DEFAULT_MESSAGE, throwable);
    }

    /**
     * The default message that is displayed in this exception.
     */
    private static final String DEFAULT_MESSAGE = "Remote socket reacted unexpectedly.";
}
