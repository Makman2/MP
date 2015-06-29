package tuhh.nme.mp.remote;

import java.net.Socket;

import tuhh.nme.mp.components.SimpleLock;


/**
 * A base class for each socket command.
 *
 * @param <T> The type of the result this command produces.
 */
public abstract class SocketCommand<T>
{
    /**
     * Instantiates a new SocketCommand.
     */
    public SocketCommand()
    {
        m_ResultLock = new SimpleLock();
        m_ResultLock.lock();
    }

    /**
     * Runs the socket command.
     *
     * @param socket The socket on which to operate.
     */
    public void run(final Socket socket)
    {
        try
        {
            m_Result = handle(socket);
            m_Exception = null;
        }
        catch (Throwable ex)
        {
            m_Exception = ex;
        }

        m_ResultLock.unlock();
    }

    /**
     * The result of the command.
     *
     * This function blocks until the command has been run.
     *
     * @return                                The result of the command.
     * @throws SocketCommandHandlingException Thrown when something went an exception was raised
     *                                        during handle(). To access this exception, call
     *                                        getCause() on the exception.
     */
    public T getResult() throws SocketCommandHandlingException
    {
        m_ResultLock.lock();
        m_ResultLock.unlock();

        // The following raises and returns are thread-safe since this object does not access the
        // used fields asynchronously any more.
        if (m_Exception != null)
        {
            throw new SocketCommandHandlingException(m_Exception);
        }

        return m_Result;
    }

    /**
     * Raised when the command gets handled from the processing network thread.
     *
     * Be sure to catch a null-socket. null will be passed if the command gets processed after the
     * socket-thread closed.
     *
     * @param socket     The socket to operate on. If the socket of the background thread is already
     *                   closed, the passed value is null.
     * @return           The result of the handling. It can be retrieved via getResult() when this
     *                   function finished.
     * @throws Throwable Any error that occurred during handling. The exception raised will be
     *                   available under getException().
     */
    protected abstract T handle(final Socket socket) throws Throwable;

    /**
     * Locks the result and exception instance until processing is done.
     */
    private final SimpleLock m_ResultLock;
    /**
     * Holds the result.
     */
    private T m_Result;
    /**
     * Holds the exception during handle().
     */
    private Throwable m_Exception;
}
